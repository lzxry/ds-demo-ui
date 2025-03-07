package com.example.ds.controller;

import com.example.ds.common.PageResult;
import com.example.ds.common.Result;
import com.example.ds.dto.StatisticsQueryRequest;
import com.example.ds.entity.TaskStatistics;
import com.example.ds.service.TaskStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "任务统计", description = "提供任务统计数据的查询和更新功能")
@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class TaskStatisticsController {

    private final TaskStatisticsService statisticsService;

    @Operation(summary = "获取统计数据", description = "获取指定项目和接入点的统计数据")
    @GetMapping("/{projectId}/{accessId}")
    @PreAuthorize("hasAuthority('statistics:query')")
    public Result<TaskStatistics> getStatistics(
            @Parameter(description = "项目ID", required = true)
            @PathVariable Long projectId,
            @Parameter(description = "接入点ID", required = true)
            @PathVariable Long accessId,
            @Parameter(description = "统计日期", required = true)
            @RequestParam LocalDate statDate) {
        return Result.success(statisticsService.getStatistics(projectId, accessId, statDate));
    }

    @Operation(summary = "获取统计列表", description = "分页获取统计数据列表")
    @GetMapping
    @PreAuthorize("hasAuthority('statistics:list')")
    public Result<PageResult<TaskStatistics>> listStatistics(
            @Parameter(description = "页码", required = false)
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量", required = false)
            @RequestParam(defaultValue = "10") Integer size,
            StatisticsQueryRequest query) {
        return Result.success(statisticsService.listStatistics(page, size, query));
    }

    @Operation(summary = "获取统计趋势", description = "获取统计数据的变化趋势")
    @GetMapping("/trend")
    @PreAuthorize("hasAuthority('statistics:trend')")
    public Result<List<TaskStatistics>> getStatisticsTrend(StatisticsQueryRequest query) {
        return Result.success(statisticsService.getStatisticsTrend(query));
    }

    @Operation(summary = "更新统计数据", description = "更新指定项目和接入点的统计数据")
    @PostMapping("/{projectId}/{accessId}")
    @PreAuthorize("hasAuthority('statistics:update')")
    public Result<Void> updateStatistics(
            @Parameter(description = "项目ID", required = true)
            @PathVariable Long projectId,
            @Parameter(description = "接入点ID", required = true)
            @PathVariable Long accessId,
            @Parameter(description = "统计日期", required = true)
            @RequestParam LocalDate statDate) {
        statisticsService.updateStatistics(projectId, accessId, statDate);
        return Result.success();
    }

    @Operation(summary = "同步历史数据", description = "同步指定时间范围内的历史统计数据")
    @PostMapping("/sync")
    @PreAuthorize("hasAuthority('statistics:sync')")
    public Result<Void> syncHistoricalStatistics(
            @Parameter(description = "项目ID", required = true)
            @RequestParam Long projectId,
            @Parameter(description = "接入点ID", required = true)
            @RequestParam Long accessId,
            @Parameter(description = "开始日期", required = true)
            @RequestParam LocalDate startDate,
            @Parameter(description = "结束日期", required = true)
            @RequestParam LocalDate endDate) {
        statisticsService.syncHistoricalStatistics(projectId, accessId, startDate, endDate);
        return Result.success();
    }
} 