package com.example.ds.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class TaskResultRequest {
    @NotBlank(message = "任务结果不能为空")
    private String result;
    
    private String errorMessage;
} 