package com.example.ds.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;

@Data
@TableName("task_statistics")
public class TaskStatistics {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long projectId;
    private Long accessId;
    private LocalDate statDate;
    private Integer totalCount;
    private Integer completedCount;
    private Integer failedCount;
    private Integer pendingCount;
    private Double successRate;
    private Double avgProcessTime;
    private LocalDate createTime;
    private LocalDate updateTime;
} 