package com.example.ds.dto;

import lombok.Data;

/**
 * 开发者API响应DTO
 */
@Data
public class DeveloperApiResponse<T> {
    
    /**
     * 状态码
     */
    private Integer code;
    
    /**
     * 消息
     */
    private String message;
    
    /**
     * 数据
     */
    private T data;
    
    public static <T> DeveloperApiResponse<T> success(T data) {
        DeveloperApiResponse<T> response = new DeveloperApiResponse<>();
        response.setCode(200);
        response.setMessage("success");
        response.setData(data);
        return response;
    }
    
    public static <T> DeveloperApiResponse<T> error(String message) {
        DeveloperApiResponse<T> response = new DeveloperApiResponse<>();
        response.setCode(500);
        response.setMessage(message);
        return response;
    }
} 