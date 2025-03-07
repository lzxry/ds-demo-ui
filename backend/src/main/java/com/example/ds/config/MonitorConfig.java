package com.example.ds.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "monitor")
public class MonitorConfig {
    private Integer queueSizeThreshold = 1000; // 队列大小阈值
    private Integer processingSpeedThreshold = 100; // 处理速度阈值（每分钟）
    private Integer errorRateThreshold = 10; // 错误率阈值（百分比）
    private String alertEmail; // 告警邮箱
    private String alertWebhook; // 告警webhook地址
} 