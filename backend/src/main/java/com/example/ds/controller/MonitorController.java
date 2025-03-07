package com.example.ds.controller;

import com.example.ds.common.Result;
import com.example.ds.service.MonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "监控管理", description = "提供系统监控和告警功能")
@RestController
@RequestMapping("/api/monitor")
@RequiredArgsConstructor
public class MonitorController {

    private final MonitorService monitorService;

    @Operation(summary = "获取监控指标", description = "获取系统当前的监控指标数据")
    @GetMapping("/metrics")
    @PreAuthorize("hasAuthority('monitor:metrics')")
    public Result<Map<String, Object>> getMetrics() {
        return Result.success(monitorService.getMetrics());
    }

    @Operation(summary = "手动触发监控", description = "手动触发系统监控检查")
    @PostMapping("/check")
    @PreAuthorize("hasAuthority('monitor:check')")
    public Result<Void> triggerCheck() {
        monitorService.monitorTaskQueue();
        monitorService.monitorProcessingSpeed();
        monitorService.monitorErrorRate();
        return Result.success();
    }

    @Operation(summary = "发送测试告警", description = "发送测试告警消息")
    @PostMapping("/alert/test")
    @PreAuthorize("hasAuthority('monitor:alert')")
    public Result<Void> testAlert(
            @RequestParam(defaultValue = "这是一条测试告警消息") String message,
            @RequestParam(defaultValue = "info") String level) {
        monitorService.sendAlert(message, level);
        return Result.success();
    }
} 