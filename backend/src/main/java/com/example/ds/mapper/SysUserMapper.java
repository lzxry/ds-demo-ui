package com.example.ds.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ds.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户 Mapper 接口
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
} 