package com.example.ds.service;

import com.example.ds.dto.DeveloperApiRequest;
import com.example.ds.dto.DeveloperApiResponse;
import com.example.ds.entity.Task;

import java.util.Map;

/**
 * 开发者API服务接口
 */
public interface DeveloperApiService {
    
    /**
     * 获取任务
     */
    DeveloperApiResponse<Task> getTask(DeveloperApiRequest request);
    
    /**
     * 回传任务结果
     */
    DeveloperApiResponse<Void> submitTaskResult(DeveloperApiRequest request);
    
    /**
     * 查询任务回传统计
     */
    DeveloperApiResponse<Map<String, Integer>> getTaskStatistics(DeveloperApiRequest request);
    
    /**
     * 释放任务
     */
    DeveloperApiResponse<Void> releaseTask(DeveloperApiRequest request);
    
    /**
     * 拉黑任务
     */
    DeveloperApiResponse<Void> blacklistTask(DeveloperApiRequest request);
} 