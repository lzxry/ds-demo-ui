package com.example.ds.controller;

import com.example.ds.common.Result;
import com.example.ds.entity.SysPermission;
import com.example.ds.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限控制器
 */
@Tag(name = "权限管理")
@RestController
@RequestMapping("/api/permission")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @Operation(summary = "获取用户权限列表")
    @GetMapping("/user/{userId}")
    public Result<List<SysPermission>> getUserPermissions(@PathVariable Long userId) {
        return Result.success(permissionService.getUserPermissions(userId));
    }

    @Operation(summary = "获取角色权限列表")
    @GetMapping("/role/{roleId}")
    public Result<List<SysPermission>> getRolePermissions(@PathVariable Long roleId) {
        return Result.success(permissionService.getRolePermissions(roleId));
    }

    @Operation(summary = "获取菜单树")
    @GetMapping("/menu/tree")
    public Result<List<SysPermission>> getMenuTree() {
        return Result.success(permissionService.getMenuTree());
    }
} 