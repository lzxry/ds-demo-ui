package com.example.ds.service;

import com.example.ds.common.PageResult;
import com.example.ds.dto.StatisticsQueryRequest;
import com.example.ds.entity.TaskStatistics;
import java.time.LocalDate;
import java.util.List;

public interface TaskStatisticsService {
    /**
     * 获取任务统计数据
     */
    TaskStatistics getStatistics(Long projectId, Long accessId, LocalDate statDate);
    
    /**
     * 分页查询统计数据
     */
    PageResult<TaskStatistics> listStatistics(Integer page, Integer size, StatisticsQueryRequest query);
    
    /**
     * 获取统计数据趋势
     */
    List<TaskStatistics> getStatisticsTrend(StatisticsQueryRequest query);
    
    /**
     * 更新统计数据
     */
    void updateStatistics(Long projectId, Long accessId, LocalDate statDate);
    
    /**
     * 同步历史统计数据
     */
    void syncHistoricalStatistics(Long projectId, Long accessId, LocalDate startDate, LocalDate endDate);
} 