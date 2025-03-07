package com.example.ds.controller;

import com.example.ds.common.Result;
import com.example.ds.entity.SysRole;
import com.example.ds.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色控制器
 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/api/role")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "获取用户角色列表")
    @GetMapping("/user/{userId}")
    public Result<List<SysRole>> getUserRoles(@PathVariable Long userId) {
        return Result.success(roleService.getUserRoles(userId));
    }

    @Operation(summary = "分配用户角色")
    @PostMapping("/user/{userId}")
    public Result<Void> assignUserRoles(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        roleService.assignUserRoles(userId, roleIds);
        return Result.success();
    }

    @Operation(summary = "获取角色权限列表")
    @GetMapping("/{roleId}/permissions")
    public Result<List<Long>> getRolePermissions(@PathVariable Long roleId) {
        return Result.success(roleService.getRolePermissions(roleId));
    }

    @Operation(summary = "分配角色权限")
    @PostMapping("/{roleId}/permissions")
    public Result<Void> assignRolePermissions(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        roleService.assignRolePermissions(roleId, permissionIds);
        return Result.success();
    }
} 