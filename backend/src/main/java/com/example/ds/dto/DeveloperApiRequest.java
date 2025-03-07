package com.example.ds.dto;

import lombok.Data;

/**
 * 开发者API请求DTO
 */
@Data
public class DeveloperApiRequest {
    
    /**
     * 开发者token
     */
    private String token;
    
    /**
     * 任务ID（回传、释放、拉黑时使用）
     */
    private Long taskId;
    
    /**
     * 任务结果（回传时使用）
     */
    private String result;
    
    /**
     * 开始日期（统计时使用）
     */
    private String startDate;
    
    /**
     * 结束日期（统计时使用）
     */
    private String endDate;
} 