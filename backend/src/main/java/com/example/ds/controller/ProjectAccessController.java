package com.example.ds.controller;

import com.example.ds.common.PageResult;
import com.example.ds.common.Result;
import com.example.ds.dto.ProjectAccessRequest;
import com.example.ds.entity.ProjectAccess;
import com.example.ds.service.ProjectAccessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "项目对接管理", description = "提供项目对接的创建、更新、删除、查询等功能")
@RestController
@RequestMapping("/api/project-access")
@RequiredArgsConstructor
public class ProjectAccessController {

    private final ProjectAccessService projectAccessService;

    @Operation(summary = "创建项目对接", description = "创建一个新的项目对接配置")
    @PostMapping
    @PreAuthorize("hasAuthority('project:access:create')")
    public Result<ProjectAccess> createAccess(@RequestBody @Valid ProjectAccessRequest request) {
        return Result.success(projectAccessService.createAccess(request));
    }

    @Operation(summary = "更新项目对接", description = "更新已存在的项目对接配置")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('project:access:update')")
    public Result<ProjectAccess> updateAccess(
            @Parameter(description = "项目对接ID", required = true)
            @PathVariable Long id,
            @RequestBody @Valid ProjectAccessRequest request) {
        return Result.success(projectAccessService.updateAccess(id, request));
    }

    @Operation(summary = "删除项目对接", description = "删除指定的项目对接配置")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('project:access:delete')")
    public Result<Void> deleteAccess(
            @Parameter(description = "项目对接ID", required = true)
            @PathVariable Long id) {
        projectAccessService.deleteAccess(id);
        return Result.success();
    }

    @Operation(summary = "获取项目对接详情", description = "获取指定项目对接的详细信息")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('project:access:query')")
    public Result<ProjectAccess> getAccess(
            @Parameter(description = "项目对接ID", required = true)
            @PathVariable Long id) {
        return Result.success(projectAccessService.getAccess(id));
    }

    @Operation(summary = "获取项目对接列表", description = "分页获取项目对接列表，支持关键词搜索")
    @GetMapping
    @PreAuthorize("hasAuthority('project:access:list')")
    public Result<PageResult<ProjectAccess>> listAccess(
            @Parameter(description = "页码", required = false)
            @RequestParam(defaultValue = "1") Integer page,
            @Parameter(description = "每页数量", required = false)
            @RequestParam(defaultValue = "10") Integer size,
            @Parameter(description = "搜索关键词", required = false)
            @RequestParam(required = false) String keyword) {
        return Result.success(projectAccessService.listAccess(page, size, keyword));
    }

    @Operation(summary = "更新项目对接状态", description = "更新项目对接的启用/禁用状态")
    @PutMapping("/{id}/status")
    @PreAuthorize("hasAuthority('project:access:update')")
    public Result<Void> updateStatus(
            @Parameter(description = "项目对接ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "状态值(0-禁用,1-启用)", required = true)
            @RequestParam Integer status) {
        projectAccessService.updateStatus(id, status);
        return Result.success();
    }
} 