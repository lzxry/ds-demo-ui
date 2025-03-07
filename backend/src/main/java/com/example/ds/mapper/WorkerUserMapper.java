package com.example.ds.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ds.entity.WorkerUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 接单者用户Mapper接口
 */
@Mapper
public interface WorkerUserMapper extends BaseMapper<WorkerUser> {
    
    /**
     * 根据用户名查询用户
     */
    WorkerUser selectByUsername(@Param("username") String username);
    
    /**
     * 根据API Key查询用户
     */
    WorkerUser selectByApiKey(@Param("apiKey") String apiKey);
    
    /**
     * 根据项目唯一码查询用户
     */
    WorkerUser selectByProjectCode(@Param("projectCode") String projectCode);
} 