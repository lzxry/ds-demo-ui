package com.example.ds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ds.common.PageResult;
import com.example.ds.dto.TaskRequest;
import com.example.ds.dto.TaskQueryRequest;
import com.example.ds.entity.Task;
import com.example.ds.mapper.TaskMapper;
import com.example.ds.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String CACHE_KEY = "task:";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Task createTask(TaskRequest request) {
        // 1. 创建任务记录
        Task task = new Task();
        BeanUtils.copyProperties(request, task);
        task.setStatus(0);
        task.setCreateTime(LocalDateTime.now());
        taskMapper.insert(task);
        
        // 2. 清除缓存
        clearCache();
        
        return task;
    }

    @Override
    public Task getTask(Long id) {
        // 1. 先从缓存获取
        String cacheKey = CACHE_KEY + id;
        Task task = (Task) redisTemplate.opsForValue().get(cacheKey);
        
        if (task == null) {
            // 2. 从数据库获取
            task = taskMapper.selectById(id);
            if (task != null) {
                // 3. 存入缓存
                redisTemplate.opsForValue().set(cacheKey, task, 1, java.util.concurrent.TimeUnit.HOURS);
            }
        }
        
        return task;
    }

    @Override
    public PageResult<Task> listTasks(Integer page, Integer size, TaskQueryRequest query) {
        // 1. 构建查询条件
        LambdaQueryWrapper<Task> wrapper = new LambdaQueryWrapper<>();
        
        if (query.getProjectId() != null) {
            wrapper.eq(Task::getProjectId, query.getProjectId());
        }
        if (query.getStatus() != null) {
            wrapper.eq(Task::getStatus, query.getStatus());
        }
        if (query.getStartTime() != null) {
            wrapper.ge(Task::getCreateTime, query.getStartTime());
        }
        if (query.getEndTime() != null) {
            wrapper.le(Task::getCreateTime, query.getEndTime());
        }
        if (StringUtils.hasText(query.getTaskId())) {
            wrapper.eq(Task::getTaskId, query.getTaskId());
        }
        
        wrapper.orderByDesc(Task::getCreateTime);
        
        // 2. 分页查询
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Task> pageParam = 
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Task> result = 
            taskMapper.selectPage(pageParam, wrapper);
        
        // 3. 构建返回结果
        return new PageResult<>(result.getRecords(), result.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitTaskResult(Long id, TaskRequest request) {
        // 1. 获取任务
        Task task = taskMapper.selectById(id);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }
        
        // 2. 更新任务状态和结果
        task.setStatus(2);
        task.setResultPath(request.getResult());
        task.setErrorMessage(request.getErrorMessage());
        task.setEndTime(LocalDateTime.now());
        taskMapper.updateById(task);
        
        // 3. 清除缓存
        clearCache();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTaskStatus(Long id, Integer status) {
        // 1. 获取任务
        Task task = taskMapper.selectById(id);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }
        
        // 2. 更新状态
        task.setStatus(status);
        if (status == 1) {
            task.setStartTime(LocalDateTime.now());
        } else if (status == 2 || status == 3) {
            task.setEndTime(LocalDateTime.now());
        }
        taskMapper.updateById(task);
        
        // 3. 清除缓存
        clearCache();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTask(Long id) {
        // 1. 检查是否存在
        Task task = taskMapper.selectById(id);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }
        
        // 2. 删除记录
        taskMapper.deleteById(id);
        
        // 3. 清除缓存
        clearCache();
    }

    /**
     * 清除缓存
     */
    private void clearCache() {
        java.util.Set<String> keys = redisTemplate.keys(CACHE_KEY + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
} 