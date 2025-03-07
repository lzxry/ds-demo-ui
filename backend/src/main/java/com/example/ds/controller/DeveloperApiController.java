package com.example.ds.controller;

import com.example.ds.dto.DeveloperApiRequest;
import com.example.ds.dto.DeveloperApiResponse;
import com.example.ds.entity.Task;
import com.example.ds.service.DeveloperApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 开发者API控制器
 */
@Tag(name = "开发者API")
@RestController
@RequestMapping("/api/developer")
@RequiredArgsConstructor
public class DeveloperApiController {

    private final DeveloperApiService developerApiService;

    @Operation(summary = "获取任务")
    @PostMapping("/task/get")
    public DeveloperApiResponse<Task> getTask(@RequestBody DeveloperApiRequest request) {
        return developerApiService.getTask(request);
    }

    @Operation(summary = "回传任务结果")
    @PostMapping("/task/submit")
    public DeveloperApiResponse<Void> submitTaskResult(@RequestBody DeveloperApiRequest request) {
        return developerApiService.submitTaskResult(request);
    }

    @Operation(summary = "查询任务回传统计")
    @PostMapping("/task/statistics")
    public DeveloperApiResponse<Map<String, Integer>> getTaskStatistics(@RequestBody DeveloperApiRequest request) {
        return developerApiService.getTaskStatistics(request);
    }

    @Operation(summary = "释放任务")
    @PostMapping("/task/release")
    public DeveloperApiResponse<Void> releaseTask(@RequestBody DeveloperApiRequest request) {
        return developerApiService.releaseTask(request);
    }

    @Operation(summary = "拉黑任务")
    @PostMapping("/task/blacklist")
    public DeveloperApiResponse<Void> blacklistTask(@RequestBody DeveloperApiRequest request) {
        return developerApiService.blacklistTask(request);
    }
} 