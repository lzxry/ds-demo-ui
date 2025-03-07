package com.example.ds.service;

import com.example.ds.entity.Task;
import java.util.Map;

public interface MonitorService {
    /**
     * 监控任务队列
     */
    void monitorTaskQueue();
    
    /**
     * 监控任务处理速度
     */
    void monitorProcessingSpeed();
    
    /**
     * 监控错误率
     */
    void monitorErrorRate();
    
    /**
     * 获取监控指标
     */
    Map<String, Object> getMetrics();
    
    /**
     * 发送告警
     */
    void sendAlert(String message, String level);
    
    /**
     * 记录任务状态变更
     */
    void recordTaskStatusChange(Task task, Integer oldStatus);
} 