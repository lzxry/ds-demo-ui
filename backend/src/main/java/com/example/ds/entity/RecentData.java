package com.example.ds.entity;

import com.example.ds.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 近期数据实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RecentData extends BaseEntity {
    
    /**
     * 项目ID
     */
    private Long projectId;
    
    /**
     * 数据内容
     */
    private String dataContent;
    
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
    
    /**
     * 状态：0-失败，1-成功
     */
    private Integer status;
    
    /**
     * 错误信息
     */
    private String errorMessage;
} 