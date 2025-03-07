package com.example.ds.controller;

import com.example.ds.common.Result;
import com.example.ds.entity.UserOnline;
import com.example.ds.service.UserOnlineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户在线状态控制器
 */
@Tag(name = "用户在线状态管理", description = "提供用户在线状态的查询和管理功能")
@RestController
@RequestMapping("/api/online")
@RequiredArgsConstructor
public class UserOnlineController {

    private final UserOnlineService userOnlineService;

    @Operation(summary = "获取在线用户列表", description = "获取当前所有在线用户的详细信息，包括登录IP、浏览器、操作系统等信息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "获取成功", 
            content = @Content(schema = @Schema(implementation = UserOnline.class))),
        @ApiResponse(responseCode = "403", description = "没有权限访问")
    })
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:online:list')")
    public Result<List<UserOnline>> getOnlineUsers() {
        return Result.success(userOnlineService.getOnlineUsers());
    }

    @Operation(summary = "获取指定用户在线状态", description = "根据用户名获取该用户的在线状态信息")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "获取成功", 
            content = @Content(schema = @Schema(implementation = UserOnline.class))),
        @ApiResponse(responseCode = "403", description = "没有权限访问"),
        @ApiResponse(responseCode = "404", description = "用户不在线")
    })
    @GetMapping("/{username}")
    @PreAuthorize("hasAuthority('system:online:query')")
    public Result<UserOnline> getUserOnline(
            @Parameter(description = "用户名", required = true) 
            @PathVariable String username) {
        UserOnline userOnline = userOnlineService.getUserOnline(username);
        if (userOnline == null) {
            return Result.error("用户不在线");
        }
        return Result.success(userOnline);
    }

    @Operation(summary = "强制用户下线", description = "强制指定用户下线，该用户的所有会话将被终止")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "操作成功"),
        @ApiResponse(responseCode = "403", description = "没有权限访问"),
        @ApiResponse(responseCode = "404", description = "用户不在线")
    })
    @DeleteMapping("/{username}")
    @PreAuthorize("hasAuthority('system:online:kick')")
    public Result<Void> forceLogout(
            @Parameter(description = "用户名", required = true) 
            @PathVariable String username) {
        UserOnline userOnline = userOnlineService.getUserOnline(username);
        if (userOnline == null) {
            return Result.error("用户不在线");
        }
        userOnlineService.forceLogout(username);
        return Result.success();
    }
} 