package com.example.ds.common.constant;

/**
 * 安全相关常量
 */
public interface SecurityConstants {
    
    /**
     * 用户状态：启用
     */
    int USER_STATUS_ENABLED = 1;
    
    /**
     * 用户状态：禁用
     */
    int USER_STATUS_DISABLED = 0;
    
    /**
     * 登录成功
     */
    int LOGIN_SUCCESS = 200;
    
    /**
     * 登录失败
     */
    int LOGIN_FAIL = 500;
    
    /**
     * 未授权
     */
    int UNAUTHORIZED = 401;
    
    /**
     * 无权限
     */
    int FORBIDDEN = 403;
    
    /**
     * Token前缀
     */
    String TOKEN_PREFIX = "Bearer ";
    
    /**
     * Token请求头
     */
    String TOKEN_HEADER = "Authorization";
    
    /**
     * Token过期时间（24小时）
     */
    long TOKEN_EXPIRATION = 86400000L;
} 