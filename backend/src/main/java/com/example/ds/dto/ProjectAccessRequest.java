package com.example.ds.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class ProjectAccessRequest {
    @NotBlank(message = "对接名称不能为空")
    private String name;
    
    @NotBlank(message = "对接类型不能为空")
    private String type;
    
    private String apiKey;
    private String apiSecret;
    private String apiUrl;
    private String description;
    private String config;
} 