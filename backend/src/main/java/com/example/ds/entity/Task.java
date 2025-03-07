package com.example.ds.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 任务实体类
 */
@Data
@TableName("task")
public class Task {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long projectId;     // 项目ID
    private Long accessId;      // 访问ID
    private Long developerTokenId; // 开发者token ID
    private String taskId;      // 外部任务ID
    private Integer status;     // 状态：0-待处理，1-处理中，2-已完成，3-失败
    private String result;      // 处理结果
    private String resultPath;  // 结果文件路径
    private String errorMessage;// 错误信息
    private LocalDateTime startTime;    // 开始时间
    private LocalDateTime endTime;      // 结束时间
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;    // 创建时间
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;    // 更新时间
} 