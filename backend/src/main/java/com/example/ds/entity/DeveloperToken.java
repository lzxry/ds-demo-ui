package com.example.ds.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 开发者token实体类
 */
@Data
@TableName("developer_token")
public class DeveloperToken {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * token名称
     */
    private String name;
    
    /**
     * token值
     */
    private String token;
    
    /**
     * 状态（0：禁用，1：启用）
     */
    private Integer status;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 最后访问时间
     */
    private LocalDateTime lastAccessTime;
} 