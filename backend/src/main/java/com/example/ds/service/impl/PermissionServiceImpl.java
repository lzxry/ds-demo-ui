package com.example.ds.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ds.entity.SysPermission;
import com.example.ds.entity.SysRolePermission;
import com.example.ds.entity.SysUserRole;
import com.example.ds.mapper.SysPermissionMapper;
import com.example.ds.mapper.SysRolePermissionMapper;
import com.example.ds.mapper.SysUserRoleMapper;
import com.example.ds.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限服务实现类
 */
@Service
@RequiredArgsConstructor
public class PermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements PermissionService {

    private final SysUserRoleMapper userRoleMapper;
    private final SysRolePermissionMapper rolePermissionMapper;

    @Override
    public List<SysPermission> getUserPermissions(Long userId) {
        // 获取用户角色
        List<SysUserRole> userRoles = userRoleMapper.selectList(
            new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId)
        );

        // 获取角色ID列表
        List<Long> roleIds = userRoles.stream()
            .map(SysUserRole::getRoleId)
            .collect(Collectors.toList());

        // 获取角色权限
        List<SysPermission> permissions = new ArrayList<>();
        for (Long roleId : roleIds) {
            permissions.addAll(getRolePermissions(roleId));
        }

        // 去重
        return permissions.stream()
            .distinct()
            .collect(Collectors.toList());
    }

    @Override
    public List<SysPermission> getRolePermissions(Long roleId) {
        // 查询角色权限关联
        List<SysRolePermission> rolePermissions = rolePermissionMapper.selectList(
            new LambdaQueryWrapper<SysRolePermission>()
                .eq(SysRolePermission::getRoleId, roleId)
        );

        // 获取权限ID列表
        List<Long> permissionIds = rolePermissions.stream()
            .map(SysRolePermission::getPermissionId)
            .collect(Collectors.toList());

        // 查询权限列表
        return permissionIds.isEmpty() ? List.of() : this.listByIds(permissionIds);
    }

    @Override
    public List<SysPermission> getMenuTree() {
        // 查询所有菜单
        List<SysPermission> allMenus = this.list(
            new LambdaQueryWrapper<SysPermission>()
                .eq(SysPermission::getType, 1)
                .eq(SysPermission::getStatus, 1)
                .orderByAsc(SysPermission::getSort)
        );

        // 构建树形结构
        Map<Long, List<SysPermission>> parentMap = allMenus.stream()
            .collect(Collectors.groupingBy(SysPermission::getParentId));

        // 设置子菜单
        allMenus.forEach(menu -> menu.setChildren(parentMap.get(menu.getId())));

        // 返回顶级菜单
        return allMenus.stream()
            .filter(menu -> menu.getParentId() == null || menu.getParentId() == 0)
            .collect(Collectors.toList());
    }
} 