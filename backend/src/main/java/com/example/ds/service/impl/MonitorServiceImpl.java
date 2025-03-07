package com.example.ds.service.impl;

import com.example.ds.config.MonitorConfig;
import com.example.ds.entity.Task;
import com.example.ds.mapper.TaskMapper;
import com.example.ds.service.MonitorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class MonitorServiceImpl implements MonitorService {

    private final TaskMapper taskMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final MonitorConfig monitorConfig;
    private final JavaMailSender mailSender;
    private final RestTemplate restTemplate;

    private static final String METRICS_KEY = "monitor:metrics";
    private static final String PROCESSING_SPEED_KEY = "monitor:speed";
    private static final String ERROR_COUNT_KEY = "monitor:error";

    @Override
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void monitorTaskQueue() {
        try {
            // 获取待处理任务数量
            Long queueSize = taskMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Task>()
                    .eq(Task::getStatus, 0)
            );

            // 更新指标
            Map<String, Object> metrics = getMetrics();
            metrics.put("queueSize", queueSize);
            redisTemplate.opsForHash().putAll(METRICS_KEY, metrics);

            // 检查是否超过阈值
            if (queueSize > monitorConfig.getQueueSizeThreshold()) {
                String message = String.format("任务队列堆积告警：当前队列大小 %d，超过阈值 %d",
                    queueSize, monitorConfig.getQueueSizeThreshold());
                sendAlert(message, "warning");
            }
        } catch (Exception e) {
            log.error("监控任务队列时发生错误", e);
        }
    }

    @Override
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void monitorProcessingSpeed() {
        try {
            // 获取最近一分钟完成的任务数
            Long completedCount = taskMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Task>()
                    .eq(Task::getStatus, 2)
                    .ge(Task::getEndTime, LocalDateTime.now().minusMinutes(1))
            );

            // 更新处理速度
            redisTemplate.opsForValue().set(PROCESSING_SPEED_KEY, completedCount);

            // 检查是否低于阈值
            if (completedCount < monitorConfig.getProcessingSpeedThreshold()) {
                String message = String.format("任务处理速度告警：当前速度 %d/分钟，低于阈值 %d/分钟",
                    completedCount, monitorConfig.getProcessingSpeedThreshold());
                sendAlert(message, "warning");
            }
        } catch (Exception e) {
            log.error("监控任务处理速度时发生错误", e);
        }
    }

    @Override
    @Scheduled(fixedRate = 300000) // 每5分钟执行一次
    public void monitorErrorRate() {
        try {
            // 获取最近5分钟的任务总数和失败数
            LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
            
            Long totalCount = taskMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Task>()
                    .ge(Task::getEndTime, fiveMinutesAgo)
            );
            
            Long errorCount = taskMapper.selectCount(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Task>()
                    .eq(Task::getStatus, 3)
                    .ge(Task::getEndTime, fiveMinutesAgo)
            );

            // 计算错误率
            double errorRate = totalCount > 0 ? (errorCount * 100.0 / totalCount) : 0;

            // 更新错误计数
            redisTemplate.opsForValue().set(ERROR_COUNT_KEY, errorCount);

            // 检查是否超过阈值
            if (errorRate > monitorConfig.getErrorRateThreshold()) {
                String message = String.format("任务错误率告警：当前错误率 %.2f%%，超过阈值 %d%%",
                    errorRate, monitorConfig.getErrorRateThreshold());
                sendAlert(message, "error");
            }
        } catch (Exception e) {
            log.error("监控错误率时发生错误", e);
        }
    }

    @Override
    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        // 从Redis获取现有指标
        Map<Object, Object> existingMetrics = redisTemplate.opsForHash().entries(METRICS_KEY);
        existingMetrics.forEach((k, v) -> metrics.put(k.toString(), v));
        
        // 添加实时指标
        metrics.put("processingSpeed", redisTemplate.opsForValue().get(PROCESSING_SPEED_KEY));
        metrics.put("errorCount", redisTemplate.opsForValue().get(ERROR_COUNT_KEY));
        
        return metrics;
    }

    @Override
    public void sendAlert(String message, String level) {
        log.warn("告警信息: {}, 级别: {}", message, level);
        
        // 发送邮件告警
        if (monitorConfig.getAlertEmail() != null) {
            try {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setTo(monitorConfig.getAlertEmail());
                mailMessage.setSubject("任务处理系统告警 - " + level);
                mailMessage.setText(message);
                mailSender.send(mailMessage);
            } catch (Exception e) {
                log.error("发送告警邮件失败", e);
            }
        }
        
        // 发送Webhook告警
        if (monitorConfig.getAlertWebhook() != null) {
            try {
                Map<String, String> body = new HashMap<>();
                body.put("message", message);
                body.put("level", level);
                restTemplate.postForEntity(monitorConfig.getAlertWebhook(), body, String.class);
            } catch (Exception e) {
                log.error("发送Webhook告警失败", e);
            }
        }
    }

    @Override
    public void recordTaskStatusChange(Task task, Integer oldStatus) {
        try {
            // 记录状态变更
            String key = String.format("task:status:%d", task.getId());
            String value = String.format("%d->%d", oldStatus, task.getStatus());
            redisTemplate.opsForValue().set(key, value, 7, TimeUnit.DAYS);
            
            // 更新相关指标
            if (task.getStatus() == 2) { // 完成
                redisTemplate.opsForValue().increment(PROCESSING_SPEED_KEY);
            } else if (task.getStatus() == 3) { // 失败
                redisTemplate.opsForValue().increment(ERROR_COUNT_KEY);
            }
        } catch (Exception e) {
            log.error("记录任务状态变更失败", e);
        }
    }
} 