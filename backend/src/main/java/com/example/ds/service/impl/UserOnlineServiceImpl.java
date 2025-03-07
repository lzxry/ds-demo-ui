package com.example.ds.service.impl;


import com.example.ds.entity.UserOnline;
import com.example.ds.service.UserOnlineService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 用户在线状态服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserOnlineServiceImpl implements UserOnlineService {

    private final RedisTemplate<String, Object> redisTemplate;
    
    @Value("${online.timeout:30}")
    private long onlineTimeout;
    
    @Value("${online.max-sessions:1}")
    private int maxSessions;
    
    private static final String ONLINE_USER_KEY = "online:user:";
    private static final String ONLINE_USER_SET = "online:users";
    private static final String USER_SESSIONS_KEY = "online:sessions:";

    @Override
    public void recordUserOnline(UserOnline userOnline) {
        String key = ONLINE_USER_KEY + userOnline.getUsername();
        String sessionsKey = USER_SESSIONS_KEY + userOnline.getUsername();
        
        // 检查会话数量限制
        Long sessionCount = redisTemplate.opsForSet().size(sessionsKey);
        if (sessionCount != null && sessionCount >= maxSessions) {
            // 如果超过限制，踢掉最早的会话
            String oldestSession = (String) redisTemplate.opsForSet().pop(sessionsKey);
            if (oldestSession != null) {
                redisTemplate.delete(ONLINE_USER_KEY + oldestSession);
            }
        }
        
        // 记录新的会话
        redisTemplate.opsForValue().set(key, userOnline, onlineTimeout, TimeUnit.MINUTES);
        redisTemplate.opsForSet().add(sessionsKey, userOnline.getUsername());
        redisTemplate.opsForSet().add(ONLINE_USER_SET, userOnline.getUsername());
        
        log.info("用户 {} 上线成功，IP: {}, 浏览器: {}, 操作系统: {}", 
                userOnline.getUsername(), 
                userOnline.getLoginIp(),
                userOnline.getBrowser(),
                userOnline.getOs());
    }

    @Override
    public void updateLastAccessTime(String username) {
        String key = ONLINE_USER_KEY + username;
        UserOnline userOnline = (UserOnline) redisTemplate.opsForValue().get(key);
        if (userOnline != null) {
            userOnline.setLastAccessTime(System.currentTimeMillis());
            redisTemplate.opsForValue().set(key, userOnline, onlineTimeout, TimeUnit.MINUTES);
        }
    }

    @Override
    public void removeUserOnline(String username) {
        String key = ONLINE_USER_KEY + username;
        String sessionsKey = USER_SESSIONS_KEY + username;
        
        redisTemplate.delete(key);
        redisTemplate.delete(sessionsKey);
        redisTemplate.opsForSet().remove(ONLINE_USER_SET, username);
        
        log.info("用户 {} 下线成功", username);
    }

    @Override
    public List<UserOnline> getOnlineUsers() {
        Set<Object> usernames = redisTemplate.opsForSet().members(ONLINE_USER_SET);
        if (usernames == null) {
            return new ArrayList<>();
        }

        List<UserOnline> onlineUsers = new ArrayList<>();
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (Object username : usernames) {
                String key = ONLINE_USER_KEY + username;
                connection.stringCommands().get(key.getBytes());
            }
            return null;
        }).forEach(result -> {
            if (result instanceof UserOnline) {
                onlineUsers.add((UserOnline) result);
            }
        });
        
        return onlineUsers;
    }

    @Override
    public UserOnline getUserOnline(String username) {
        String key = ONLINE_USER_KEY + username;
        return (UserOnline) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void forceLogout(String username) {
        removeUserOnline(username);
        log.info("用户 {} 被强制下线", username);
    }

    @Override
    public UserOnline createUserOnline(UserDetails userDetails) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }

        HttpServletRequest request = attributes.getRequest();
        UserOnline userOnline = new UserOnline();
        userOnline.setUsername(userDetails.getUsername());
        userOnline.setLoginIp(getClientIp(request));
        userOnline.setLoginLocation(getLocation(userOnline.getLoginIp()));
        userOnline.setBrowser(getBrowser(request));
        userOnline.setOs(getOs(request));
        userOnline.setLoginTime(System.currentTimeMillis());
        userOnline.setLastAccessTime(System.currentTimeMillis());
        return userOnline;
    }

    /**
     * 获取客户端真实IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取浏览器类型
     */
    private String getBrowser(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            return "未知";
        }
        if (userAgent.contains("Edge")) {
            return "Edge";
        } else if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Safari")) {
            return "Safari";
        } else {
            return "其他";
        }
    }

    /**
     * 获取操作系统
     */
    private String getOs(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            return "未知";
        }
        if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Mac")) {
            return "Mac";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        } else if (userAgent.contains("Android")) {
            return "Android";
        } else if (userAgent.contains("iPhone")) {
            return "iPhone";
        } else {
            return "其他";
        }
    }

    /**
     * 获取登录地点
     */
    private String getLocation(String ip) {
        try {
            if ("127.0.0.1".equals(ip) || "localhost".equals(ip)) {
                return "本地";
            }
            return ip;
        } catch (Exception e) {
            log.error("IP地址解析失败: {}", e.getMessage());
            return "未知";
        }
    }
} 