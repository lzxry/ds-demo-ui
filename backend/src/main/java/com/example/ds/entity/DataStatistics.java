package com.example.ds.entity;

import com.example.ds.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 数据统计实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataStatistics extends BaseEntity {
    
    /**
     * 项目ID
     */
    private Long projectId;
    
    /**
     * 统计日期
     */
    private LocalDate statDate;
    
    /**
     * 拉取数量
     */
    private Integer pullCount;
    
    /**
     * 推送数量
     */
    private Integer pushCount;
    
    /**
     * 成功率
     */
    private BigDecimal successRate;
} 