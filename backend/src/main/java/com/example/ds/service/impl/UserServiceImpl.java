package com.example.ds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ds.common.exception.BusinessException;
import com.example.ds.entity.User;
import com.example.ds.entity.SysUser;
import com.example.ds.mapper.UserMapper;
import com.example.ds.mapper.SysUserMapper;
import com.example.ds.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final SysUserMapper sysUserMapper;

    public UserServiceImpl(PasswordEncoder passwordEncoder, SysUserMapper sysUserMapper) {
        this.passwordEncoder = passwordEncoder;
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public User getUserByUsername(String username) {
        return getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(User user) {
        // 检查用户名是否已存在
        if (getUserByUsername(user.getUsername()) != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(1);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(User user) {
        // 检查用户名是否已存在
        User existingUser = getUserByUsername(user.getUsername());
        if (existingUser != null && !existingUser.getId().equals(user.getId())) {
            throw new RuntimeException("用户名已存在");
        }

        // 如果密码不为空，则加密密码
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        user.setUpdateTime(LocalDateTime.now());
        updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        removeById(id);
    }

    @Override
    public SysUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("用户未登录");
        }
        return (SysUser) authentication.getPrincipal();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String oldPassword, String newPassword) {
        SysUser currentUser = getCurrentUser();
        if (!passwordEncoder.matches(oldPassword, currentUser.getPassword())) {
            throw new BusinessException("旧密码错误");
        }
        currentUser.setPassword(passwordEncoder.encode(newPassword));
        currentUser.setUpdateTime(LocalDateTime.now());
        sysUserMapper.updateById(currentUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(SysUser user) {
        SysUser currentUser = getCurrentUser();
        // 只允许更新部分字段
        currentUser.setNickname(user.getNickname());
        currentUser.setEmail(user.getEmail());
        currentUser.setPhone(user.getPhone());
        currentUser.setUpdateTime(LocalDateTime.now());
        sysUserMapper.updateById(currentUser);
    }
} 