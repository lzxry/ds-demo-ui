package com.example.ds.common.annotation;

import java.lang.annotation.*;

/**
 * 限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    
    /**
     * 限流key
     */
    String key() default "";
    
    /**
     * 限流时间（秒）
     */
    int time() default 60;
    
    /**
     * 限流次数
     */
    int count() default 100;
} 