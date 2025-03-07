package com.example.ds.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 项目访问配置实体类
 */
@Data
@TableName("project_access")
public class ProjectAccess {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;        // 对接名称
    private String type;        // 对接类型：PULL(拉取)、PUSH(推送)、OFFLINE(离线)
    private Integer status;     // 状态：0-禁用，1-启用
    private String apiKey;      // API密钥
    private String apiSecret;   // API密钥
    private String apiUrl;      // API地址
    private String description; // 描述
    private String config;      // 配置信息(JSON格式)
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;  // 创建时间
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;  // 更新时间
} 