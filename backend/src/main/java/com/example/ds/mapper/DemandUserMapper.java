package com.example.ds.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ds.entity.DemandUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 需求方用户Mapper接口
 */
@Mapper
public interface DemandUserMapper extends BaseMapper<DemandUser> {
    
    /**
     * 根据用户名查询用户
     */
    DemandUser selectByUsername(@Param("username") String username);
    
    /**
     * 根据API Key查询用户
     */
    DemandUser selectByApiKey(@Param("apiKey") String apiKey);
} 