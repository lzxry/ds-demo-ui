package com.example.ds.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ds.entity.User;
import com.example.ds.entity.SysUser;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    
    /**
     * 根据用户名查询用户
     */
    User getUserByUsername(String username);
    
    /**
     * 注册用户
     */
    void register(User user);
    
    /**
     * 更新用户信息
     */
    void updateUser(User user);
    
    /**
     * 删除用户
     */
    void deleteUser(Long id);

    /**
     * 获取当前登录用户信息
     */
    SysUser getCurrentUser();

    /**
     * 修改密码
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void updatePassword(String oldPassword, String newPassword);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     */
    void updateUserInfo(SysUser user);
} 