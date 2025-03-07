package com.example.ds.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class StatisticsQueryRequest {
    private Long projectId;
    private Long accessId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String timeUnit; // day, week, month
} 