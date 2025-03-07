package com.example.ds.service;

import com.example.ds.entity.Task;
import java.util.List;

public interface TaskScheduleService {
    /**
     * 调度任务
     */
    void scheduleTasks();
    
    /**
     * 获取待处理任务
     */
    List<Task> getPendingTasks(Long projectId, Long accessId, Integer limit);
    
    /**
     * 分配任务
     */
    void assignTasks(List<Task> tasks);
    
    /**
     * 重试失败任务
     */
    void retryFailedTasks();
    
    /**
     * 清理过期任务
     */
    void cleanExpiredTasks();
} 