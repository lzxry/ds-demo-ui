package com.example.ds.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TaskQueryRequest {
    private Long projectId;
    private Integer status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String taskId;
} 