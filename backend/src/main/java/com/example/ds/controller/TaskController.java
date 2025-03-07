package com.example.ds.controller;

import com.example.ds.common.PageResult;
import com.example.ds.common.Result;
import com.example.ds.dto.TaskRequest;
import com.example.ds.dto.TaskQueryRequest;
import com.example.ds.entity.Task;
import com.example.ds.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "任务管理", description = "提供任务的创建、查询、更新、删除等功能")
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "创建任务", description = "创建一个新的任务")
    @PostMapping
    @PreAuthorize("hasAuthority('task:create')")
    public Result<Task> createTask(@RequestBody @Valid TaskRequest request) {
        return Result.success(taskService.createTask(request));
    }

    @Operation(summary = "获取任务详情", description = "获取指定任务的详细信息")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('task:query')")
    public Result<Task> getTask(
            @Parameter(description = "任务ID", required = true)
            @PathVariable Long id) {
        return Result.success(taskService.getTask(id));
    }

    @Operation(summary = "获取任务列表", description = "分页获取任务列表，支持多种查询条件")
    @GetMapping
    @PreAuthorize("hasAuthority('task:list')")
    public Result<PageResult<Task>> listTasks(
            @Parameter(description = "页码", required = false)
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量", required = false)
            @RequestParam(defaultValue = "10") Integer size,
            TaskQueryRequest query) {
        return Result.success(taskService.listTasks(page, size, query));
    }

    @Operation(summary = "提交任务结果", description = "提交任务的处理结果")
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('task:submit')")
    public Result<Void> submitTaskResult(
            @Parameter(description = "任务ID", required = true)
            @PathVariable Long id,
            @RequestBody @Valid TaskRequest request) {
        taskService.submitTaskResult(id, request);
        return Result.success();
    }

    @Operation(summary = "更新任务状态", description = "更新任务的处理状态")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('task:update')")
    public Result<Void> updateTaskStatus(
            @Parameter(description = "任务ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "状态值(0-待处理,1-处理中,2-已完成,3-失败)", required = true)
            @RequestParam Integer status) {
        taskService.updateTaskStatus(id, status);
        return Result.success();
    }

    @Operation(summary = "删除任务", description = "删除指定的任务")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('task:delete')")
    public Result<Void> deleteTask(
            @Parameter(description = "任务ID", required = true)
            @PathVariable Long id) {
        taskService.deleteTask(id);
        return Result.success();
    }
} 