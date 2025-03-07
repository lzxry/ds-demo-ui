package com.example.ds.service;

/**
 * 认证服务接口
 */
public interface AuthService {
    
    /**
     * 需求方登录
     */
    String demandLogin(String username, String password);
    
    /**
     * 接单者登录
     */
    String workerLogin(String username, String password);
    
    /**
     * 生成接单者token
     */
    String generateWorkerToken(String projectCode);
    
    /**
     * 验证接单者token
     */
    boolean validateWorkerToken(String token);
    
    /**
     * 需求方API认证
     */
    boolean validateDemandApi(String apiKey, String apiSecret);
    
    /**
     * 接单者API认证
     */
    boolean validateWorkerApi(String apiKey, String apiSecret);
    
    /**
     * 登出
     */
    void logout(String token);
} 