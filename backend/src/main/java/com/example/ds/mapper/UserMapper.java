package com.example.ds.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ds.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper 接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
} 