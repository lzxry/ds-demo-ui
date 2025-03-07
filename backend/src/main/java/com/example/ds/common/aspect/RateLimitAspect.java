package com.example.ds.common.aspect;

import com.example.ds.common.annotation.RateLimit;
import com.example.ds.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.TimeUnit;

/**
 * 限流切面
 */
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final RedisTemplate<String, Object> redisTemplate;

    @Before("@annotation(rateLimit)")
    public void doBefore(RateLimit rateLimit) {
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }

        HttpServletRequest request = attributes.getRequest();
        String ip = request.getRemoteAddr();
        String uri = request.getRequestURI();
        String method = request.getMethod();

        // 构建限流key
        String key = rateLimit.key();
        if (key.isEmpty()) {
            key = String.format("%s:%s:%s", ip, uri, method);
        }

        // 获取当前访问次数
        Integer count = (Integer) redisTemplate.opsForValue().get(key);
        if (count == null) {
            // 第一次访问，设置初始值和过期时间
            redisTemplate.opsForValue().set(key, 1, rateLimit.time(), TimeUnit.SECONDS);
        } else if (count >= rateLimit.count()) {
            // 超过限流次数
            throw new BusinessException("访问过于频繁，请稍后再试");
        } else {
            // 增加访问次数
            redisTemplate.opsForValue().increment(key);
        }
    }
} 