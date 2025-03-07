package com.example.ds.controller;

import com.example.ds.common.Result;
import com.example.ds.common.annotation.RateLimit;
import com.example.ds.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Tag(name = "认证管理")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    @RateLimit(time = 60, count = 5)  // 每分钟最多5次登录尝试
    public Result<String> login(@RequestParam String username, 
                              @RequestParam String password,
                              @RequestParam(defaultValue = "false") boolean rememberMe) {
        String token = authService.demandLogin(username, password);
        return Result.success(token);
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public Result<Void> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String username = authentication.getName();
            authService.logout(username);
        }
        return Result.success();
    }
} 