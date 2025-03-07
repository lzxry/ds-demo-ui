package com.example.ds.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 用户在线状态配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "online")
public class OnlineConfig {
    
    /**
     * 用户会话超时时间（分钟）
     */
    private long timeout = 30;
    
    /**
     * 每个用户最大会话数
     */
    private int maxSessions = 1;
    
    /**
     * 是否允许多设备同时登录
     */
    private boolean allowMultiLogin = false;
    
    /**
     * 是否启用IP地址解析
     */
    private boolean enableIpLocation = false;
    
    /**
     * IP地址解析服务提供商（可选：aliyun、ipapi等）
     */
    private String ipLocationProvider;
    
    /**
     * IP地址解析服务API密钥
     */
    private String ipLocationApiKey;
} 