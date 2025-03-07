package com.example.ds.service.impl;

import com.example.ds.common.exception.BusinessException;
import com.example.ds.common.utils.JwtUtils;
import com.example.ds.entity.DemandUser;
import com.example.ds.entity.WorkerUser;
import com.example.ds.mapper.DemandUserMapper;
import com.example.ds.mapper.WorkerUserMapper;
import com.example.ds.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * 认证服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final DemandUserMapper demandUserMapper;
    private final WorkerUserMapper workerUserMapper;

    @Override
    public String demandLogin(String username, String password) {
        DemandUser user = demandUserMapper.selectByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        return jwtUtils.generateToken(user.getUsername(), "DEMAND");
    }

    @Override
    public String workerLogin(String username, String password) {
        WorkerUser user = workerUserMapper.selectByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }
        return generateWorkerToken(user.getProjectCode());
    }

    @Override
    public String generateWorkerToken(String projectCode) {
        // 生成8位随机字符串
        String randomStr = generateRandomString(8);
        // 组合token：项目唯一码 + 随机字符串
        return projectCode + randomStr;
    }

    @Override
    public boolean validateWorkerToken(String token) {
        if (token == null || token.length() < 8) {
            return false;
        }
        // 提取项目唯一码
        String projectCode = token.substring(0, token.length() - 8);
        // 验证项目唯一码是否存在
        WorkerUser user = workerUserMapper.selectByProjectCode(projectCode);
        return user != null && user.getStatus() == 1;
    }

    @Override
    public boolean validateDemandApi(String apiKey, String apiSecret) {
        DemandUser user = demandUserMapper.selectByApiKey(apiKey);
        if (user == null || !apiSecret.equals(user.getApiSecret())) {
            return false;
        }
        return user.getStatus() == 1;
    }

    @Override
    public boolean validateWorkerApi(String apiKey, String apiSecret) {
        WorkerUser user = workerUserMapper.selectByApiKey(apiKey);
        if (user == null || !apiSecret.equals(user.getApiSecret())) {
            return false;
        }
        return user.getStatus() == 1;
    }

    @Override
    public void logout(String token) {
        jwtUtils.addToBlacklist(token);
    }

    /**
     * 生成指定长度的随机字符串
     */
    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
} 