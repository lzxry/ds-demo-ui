package com.example.ds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ds.entity.Task;
import com.example.ds.mapper.TaskMapper;
import com.example.ds.service.TaskScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskScheduleServiceImpl implements TaskScheduleService {

    private final TaskMapper taskMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String TASK_LOCK_KEY = "task:lock:";
    private static final long LOCK_EXPIRE_TIME = 5; // 锁过期时间（分钟）
    
    @Override
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void scheduleTasks() {
        log.info("开始调度任务...");
        try {
            // 1. 获取所有项目的待处理任务
            List<Task> pendingTasks = getPendingTasks(null, null, 100);
            if (!pendingTasks.isEmpty()) {
                // 2. 分配任务
                assignTasks(pendingTasks);
            }
            
            // 3. 重试失败任务
            retryFailedTasks();
            
            // 4. 清理过期任务
            cleanExpiredTasks();
        } catch (Exception e) {
            log.error("任务调度失败", e);
        }
    }
    
    @Override
    public List<Task> getPendingTasks(Long projectId, Long accessId, Integer limit) {
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Task::getStatus, 0); // 待处理状态
        
        if (projectId != null) {
            wrapper.eq(Task::getProjectId, projectId);
        }
        if (accessId != null) {
            wrapper.eq(Task::getAccessId, accessId);
        }
        
        wrapper.orderByAsc(Task::getCreateTime)
               .last("LIMIT " + limit);
               
        return taskMapper.selectList(wrapper);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignTasks(List<Task> tasks) {
        for (Task task : tasks) {
            String lockKey = TASK_LOCK_KEY + task.getId();
            Boolean acquired = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", LOCK_EXPIRE_TIME, TimeUnit.MINUTES);
            
            if (Boolean.TRUE.equals(acquired)) {
                try {
                    // 更新任务状态为处理中
                    task.setStatus(1);
                    task.setStartTime(LocalDateTime.now());
                    taskMapper.updateById(task);
                    
                    // TODO: 这里可以添加具体的任务分配逻辑，如发送到消息队列等
                    
                    log.info("任务[{}]已分配", task.getId());
                } catch (Exception e) {
                    log.error("任务分配失败: {}", task.getId(), e);
                    redisTemplate.delete(lockKey);
                }
            }
        }
    }
    
    @Override
    @Scheduled(cron = "0 0/30 * * * ?") // 每30分钟执行一次
    public void retryFailedTasks() {
        log.info("开始重试失败任务...");
        try {
            LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Task::getStatus, 3) // 失败状态
                   .lt(Task::getCreateTime, LocalDateTime.now().minusHours(1)) // 1小时前的任务
                   .last("LIMIT 50");
                   
            List<Task> failedTasks = taskMapper.selectList(wrapper);
            if (!failedTasks.isEmpty()) {
                for (Task task : failedTasks) {
                    task.setStatus(0); // 重置为待处理状态
                    task.setStartTime(null);
                    task.setEndTime(null);
                    taskMapper.updateById(task);
                    log.info("失败任务[{}]已重置为待处理状态", task.getId());
                }
            }
        } catch (Exception e) {
            log.error("重试失败任务时发生错误", e);
        }
    }
    
    @Override
    @Scheduled(cron = "0 0 0 * * ?") // 每天凌晨执行
    public void cleanExpiredTasks() {
        log.info("开始清理过期任务...");
        try {
            LocalDateTime expireTime = LocalDateTime.now().minusDays(7); // 7天前的任务
            
            LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
            wrapper.lt(Task::getCreateTime, expireTime)
                   .in(Task::getStatus, 2, 3); // 已完成或失败的任务
                   
            int count = taskMapper.delete(wrapper);
            log.info("已清理{}个过期任务", count);
        } catch (Exception e) {
            log.error("清理过期任务时发生错误", e);
        }
    }
} 