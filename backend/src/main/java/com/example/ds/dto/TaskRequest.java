package com.example.ds.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class TaskRequest {
    @NotNull(message = "项目ID不能为空")
    private Long projectId;
    
    @NotBlank(message = "任务ID不能为空")
    private String taskId;
    
    private String result;
    private String errorMessage;
} 