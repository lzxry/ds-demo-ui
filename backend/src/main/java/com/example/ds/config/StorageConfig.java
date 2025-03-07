package com.example.ds.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "storage")
public class StorageConfig {
    private String baseDir;
    private Long maxFileSize = 100L * 1024 * 1024; // 默认最大文件大小100MB
    private String[] allowedExtensions = {".txt", ".csv", ".xlsx", ".zip"};
} 