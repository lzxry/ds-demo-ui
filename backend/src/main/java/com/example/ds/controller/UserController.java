package com.example.ds.controller;

import com.example.ds.common.Result;
import com.example.ds.entity.SysUser;
import com.example.ds.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/info")
    public Result<SysUser> getUserInfo() {
        return Result.success(userService.getCurrentUser());
    }

    @Operation(summary = "修改密码")
    @PostMapping("/password")
    public Result<Void> updatePassword(@RequestParam String oldPassword, @RequestParam String newPassword) {
        userService.updatePassword(oldPassword, newPassword);
        return Result.success();
    }
} 