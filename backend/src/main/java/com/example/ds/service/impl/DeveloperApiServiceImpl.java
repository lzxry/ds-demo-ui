package com.example.ds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ds.dto.DeveloperApiRequest;
import com.example.ds.dto.DeveloperApiResponse;
import com.example.ds.entity.DeveloperToken;
import com.example.ds.entity.Task;
import com.example.ds.mapper.DeveloperTokenMapper;
import com.example.ds.mapper.TaskMapper;
import com.example.ds.service.DeveloperApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 开发者API服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeveloperApiServiceImpl implements DeveloperApiService {

    private final DeveloperTokenMapper developerTokenMapper;
    private final TaskMapper taskMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String TOKEN_CACHE_KEY = "developer:token:";
    private static final long TOKEN_CACHE_EXPIRE = 24 * 60 * 60; // 24小时

    @Override
    public DeveloperApiResponse<Task> getTask(DeveloperApiRequest request) {
        // 验证token
        DeveloperToken token = validateToken(request.getToken());
        if (token == null) {
            return DeveloperApiResponse.error("无效的token");
        }

        // 获取待处理的任务
        Task task = taskMapper.selectOne(new LambdaQueryWrapper<Task>()
                .eq(Task::getStatus, 0)
                .orderByAsc(Task::getCreateTime)
                .last("LIMIT 1"));

        if (task == null) {
            return DeveloperApiResponse.error("暂无可用任务");
        }

        // 更新任务状态
        task.setStatus(1);
        task.setDeveloperTokenId(token.getId());
        task.setStartTime(LocalDateTime.now());
        taskMapper.updateById(task);

        // 更新token最后访问时间
        token.setLastAccessTime(LocalDateTime.now());
        developerTokenMapper.updateById(token);

        return DeveloperApiResponse.success(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeveloperApiResponse<Void> submitTaskResult(DeveloperApiRequest request) {
        // 验证token
        DeveloperToken token = validateToken(request.getToken());
        if (token == null) {
            return DeveloperApiResponse.error("无效的token");
        }

        // 获取任务
        Task task = taskMapper.selectById(request.getTaskId());
        if (task == null) {
            return DeveloperApiResponse.error("任务不存在");
        }

        // 验证任务是否属于该token
        if (!token.getId().equals(task.getDeveloperTokenId())) {
            return DeveloperApiResponse.error("无权操作此任务");
        }

        // 更新任务状态和结果
        task.setStatus(2);
        task.setResult(request.getResult());
        task.setEndTime(LocalDateTime.now());
        taskMapper.updateById(task);

        return DeveloperApiResponse.success(null);
    }

    @Override
    public DeveloperApiResponse<Map<String, Integer>> getTaskStatistics(DeveloperApiRequest request) {
        // 验证token
        DeveloperToken token = validateToken(request.getToken());
        if (token == null) {
            return DeveloperApiResponse.error("无效的token");
        }

        // 查询统计结果
        Map<String, Integer> statistics = new HashMap<>();
        
        // 构建查询条件
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<Task>()
                .eq(Task::getDeveloperTokenId, token.getId())
                .between(Task::getCreateTime, request.getStartDate(), request.getEndDate());
        
        // 统计总数
        statistics.put("total", taskMapper.selectCount(wrapper).intValue());
        
        // 统计成功数
        statistics.put("success", taskMapper.selectCount(wrapper.eq(Task::getStatus, 2)).intValue());
        
        // 统计失败数
        statistics.put("failed", taskMapper.selectCount(wrapper.eq(Task::getStatus, 3)).intValue());

        return DeveloperApiResponse.success(statistics);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeveloperApiResponse<Void> releaseTask(DeveloperApiRequest request) {
        // 验证token
        DeveloperToken token = validateToken(request.getToken());
        if (token == null) {
            return DeveloperApiResponse.error("无效的token");
        }

        // 获取任务
        Task task = taskMapper.selectById(request.getTaskId());
        if (task == null) {
            return DeveloperApiResponse.error("任务不存在");
        }

        // 验证任务是否属于该token
        if (!token.getId().equals(task.getDeveloperTokenId())) {
            return DeveloperApiResponse.error("无权操作此任务");
        }

        // 更新任务状态
        task.setStatus(0);
        task.setDeveloperTokenId(null);
        task.setStartTime(null);
        taskMapper.updateById(task);

        return DeveloperApiResponse.success(null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DeveloperApiResponse<Void> blacklistTask(DeveloperApiRequest request) {
        // 验证token
        DeveloperToken token = validateToken(request.getToken());
        if (token == null) {
            return DeveloperApiResponse.error("无效的token");
        }

        // 获取任务
        Task task = taskMapper.selectById(request.getTaskId());
        if (task == null) {
            return DeveloperApiResponse.error("任务不存在");
        }

        // 验证任务是否属于该token
        if (!token.getId().equals(task.getDeveloperTokenId())) {
            return DeveloperApiResponse.error("无权操作此任务");
        }

        // 更新任务状态
        task.setStatus(3);
        task.setErrorMessage("任务被拉黑");
        task.setEndTime(LocalDateTime.now());
        taskMapper.updateById(task);

        return DeveloperApiResponse.success(null);
    }

    /**
     * 验证token
     */
    private DeveloperToken validateToken(String token) {
        if (token == null) {
            return null;
        }
        
        // 先从Redis缓存中获取
        String cacheKey = TOKEN_CACHE_KEY + token;
        DeveloperToken cachedToken = (DeveloperToken) redisTemplate.opsForValue().get(cacheKey);
        if (cachedToken != null) {
            return cachedToken;
        }
        
        // 缓存未命中，从数据库查询
        DeveloperToken dbToken = developerTokenMapper.selectOne(new LambdaQueryWrapper<DeveloperToken>()
                .eq(DeveloperToken::getToken, token)
                .eq(DeveloperToken::getStatus, 1));
                
        if (dbToken != null) {
            // 将token信息存入Redis缓存
            redisTemplate.opsForValue().set(cacheKey, dbToken, TOKEN_CACHE_EXPIRE, TimeUnit.SECONDS);
        }
        
        return dbToken;
    }
} 