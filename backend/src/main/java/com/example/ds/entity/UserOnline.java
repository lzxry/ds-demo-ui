package com.example.ds.entity;

import lombok.Data;

/**
 * 用户在线状态实体类
 */
@Data
public class UserOnline {
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 昵称
     */
    private String nickname;
    
    /**
     * 登录IP
     */
    private String loginIp;
    
    /**
     * 登录地点
     */
    private String loginLocation;
    
    /**
     * 浏览器类型
     */
    private String browser;
    
    /**
     * 操作系统
     */
    private String os;
    
    /**
     * 登录时间
     */
    private Long loginTime;
    
    /**
     * 最后访问时间
     */
    private Long lastAccessTime;
} 