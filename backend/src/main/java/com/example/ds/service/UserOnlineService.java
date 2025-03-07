package com.example.ds.service;

import com.example.ds.entity.UserOnline;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * 用户在线状态服务接口
 */
public interface UserOnlineService {
    
    /**
     * 记录用户在线状态
     *
     * @param userOnline 用户在线信息
     */
    void recordUserOnline(UserOnline userOnline);
    
    /**
     * 更新用户最后访问时间
     *
     * @param username 用户名
     */
    void updateLastAccessTime(String username);
    
    /**
     * 移除用户在线状态
     *
     * @param username 用户名
     */
    void removeUserOnline(String username);
    
    /**
     * 获取在线用户列表
     *
     * @return 在线用户列表
     */
    List<UserOnline> getOnlineUsers();
    
    /**
     * 获取用户在线状态
     *
     * @param username 用户名
     * @return 用户在线状态
     */
    UserOnline getUserOnline(String username);
    
    /**
     * 强制用户下线
     *
     * @param username 用户名
     */
    void forceLogout(String username);
    
    /**
     * 创建用户在线信息
     *
     * @param userDetails 用户详情
     * @return 用户在线信息
     */
    UserOnline createUserOnline(UserDetails userDetails);
} 