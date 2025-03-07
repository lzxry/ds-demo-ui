package com.example.ds.service;

import com.example.ds.entity.SysRole;

import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService {
    
    /**
     * 获取用户角色列表
     */
    List<SysRole> getUserRoles(Long userId);
    
    /**
     * 分配用户角色
     */
    void assignUserRoles(Long userId, List<Long> roleIds);
    
    /**
     * 获取角色权限列表
     */
    List<Long> getRolePermissions(Long roleId);
    
    /**
     * 分配角色权限
     */
    void assignRolePermissions(Long roleId, List<Long> permissionIds);
} 