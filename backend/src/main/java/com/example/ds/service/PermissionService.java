package com.example.ds.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.ds.entity.SysPermission;

import java.util.List;

/**
 * 权限服务接口
 */
public interface PermissionService extends IService<SysPermission> {
    
    /**
     * 获取用户权限列表
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<SysPermission> getUserPermissions(Long userId);
    
    /**
     * 获取角色权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<SysPermission> getRolePermissions(Long roleId);
    
    /**
     * 获取菜单树
     *
     * @return 菜单树
     */
    List<SysPermission> getMenuTree();
} 