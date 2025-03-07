package com.example.ds.entity;

import com.example.ds.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据验证模板实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataTemplate extends BaseEntity {
    
    /**
     * 模板名称
     */
    private String templateName;
    
    /**
     * 模板内容
     */
    private String templateContent;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;
} 