package com.example.ds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ds.common.PageResult;
import com.example.ds.dto.ProjectAccessRequest;
import com.example.ds.entity.ProjectAccess;
import com.example.ds.mapper.ProjectAccessMapper;
import com.example.ds.service.ProjectAccessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectAccessServiceImpl implements ProjectAccessService {

    private final ProjectAccessMapper projectAccessMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String CACHE_KEY = "project:access:";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectAccess createAccess(ProjectAccessRequest request) {
        // 1. 验证请求参数
        validateAccessRequest(request);
        
        // 2. 创建项目对接记录
        ProjectAccess access = new ProjectAccess();
        BeanUtils.copyProperties(request, access);
        access.setStatus(1);
        access.setCreateTime(LocalDateTime.now());
        projectAccessMapper.insert(access);
        
        // 3. 清除缓存
        clearCache();
        
        return access;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProjectAccess updateAccess(Long id, ProjectAccessRequest request) {
        // 1. 验证请求参数
        validateAccessRequest(request);
        
        // 2. 获取并更新记录
        ProjectAccess access = projectAccessMapper.selectById(id);
        if (access == null) {
            throw new RuntimeException("项目对接不存在");
        }
        
        BeanUtils.copyProperties(request, access);
        access.setUpdateTime(LocalDateTime.now());
        projectAccessMapper.updateById(access);
        
        // 3. 清除缓存
        clearCache();
        
        return access;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAccess(Long id) {
        // 1. 检查是否存在
        ProjectAccess access = projectAccessMapper.selectById(id);
        if (access == null) {
            throw new RuntimeException("项目对接不存在");
        }
        
        // 2. 删除记录
        projectAccessMapper.deleteById(id);
        
        // 3. 清除缓存
        clearCache();
    }

    @Override
    public ProjectAccess getAccess(Long id) {
        // 1. 先从缓存获取
        String cacheKey = CACHE_KEY + id;
        ProjectAccess access = (ProjectAccess) redisTemplate.opsForValue().get(cacheKey);
        
        if (access == null) {
            // 2. 从数据库获取
            access = projectAccessMapper.selectById(id);
            if (access != null) {
                // 3. 存入缓存
                redisTemplate.opsForValue().set(cacheKey, access, 1, java.util.concurrent.TimeUnit.HOURS);
            }
        }
        
        return access;
    }

    @Override
    public PageResult<ProjectAccess> listAccess(Integer page, Integer size, String keyword) {
        // 1. 构建查询条件
        LambdaQueryWrapper<ProjectAccess> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(ProjectAccess::getName, keyword)
                  .or()
                  .like(ProjectAccess::getDescription, keyword);
        }
        wrapper.orderByDesc(ProjectAccess::getCreateTime);
        
        // 2. 分页查询
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<ProjectAccess> pageParam = 
            new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, size);
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<ProjectAccess> result = 
            projectAccessMapper.selectPage(pageParam, wrapper);
        
        // 3. 构建返回结果
        return new PageResult<>(result.getRecords(), result.getTotal());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, Integer status) {
        // 1. 检查是否存在
        ProjectAccess access = projectAccessMapper.selectById(id);
        if (access == null) {
            throw new RuntimeException("项目对接不存在");
        }
        
        // 2. 更新状态
        access.setStatus(status);
        access.setUpdateTime(LocalDateTime.now());
        projectAccessMapper.updateById(access);
        
        // 3. 清除缓存
        clearCache();
    }

    /**
     * 验证请求参数
     */
    private void validateAccessRequest(ProjectAccessRequest request) {
        if (!Arrays.asList("PULL", "PUSH", "OFFLINE").contains(request.getType())) {
            throw new RuntimeException("无效的对接类型");
        }
        
        if ("PULL".equals(request.getType()) || "PUSH".equals(request.getType())) {
            if (!StringUtils.hasText(request.getApiKey())) {
                throw new RuntimeException("API密钥不能为空");
            }
            if (!StringUtils.hasText(request.getApiSecret())) {
                throw new RuntimeException("API密钥不能为空");
            }
            if (!StringUtils.hasText(request.getApiUrl())) {
                throw new RuntimeException("API地址不能为空");
            }
        }
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