package com.example.ds.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ds.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色 Mapper 接口
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {
    
    /**
     * 查询用户角色列表
     */
    List<SysRole> selectUserRoles(@Param("userId") Long userId);
    
    /**
     * 删除用户角色关联
     */
    void deleteUserRoles(@Param("userId") Long userId);
    
    /**
     * 批量插入用户角色关联
     */
    void insertUserRoles(@Param("userId") Long userId, @Param("roleIds") List<Long> roleIds);
    
    /**
     * 查询角色权限ID列表
     */
    List<Long> selectRolePermissions(@Param("roleId") Long roleId);
    
    /**
     * 删除角色权限关联
     */
    void deleteRolePermissions(@Param("roleId") Long roleId);
    
    /**
     * 批量插入角色权限关联
     */
    void insertRolePermissions(@Param("roleId") Long roleId, @Param("permissionIds") List<Long> permissionIds);
} 