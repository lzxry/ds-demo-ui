package com.example.ds.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ds.entity.SysRolePermission;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色权限关联Mapper接口
 */
@Mapper
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {
} 