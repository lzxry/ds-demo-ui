package com.example.ds.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ds.entity.SysRole;
import com.example.ds.mapper.SysRoleMapper;
import com.example.ds.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色服务实现类
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements RoleService {

    private final SysRoleMapper roleMapper;

    @Override
    public List<SysRole> getUserRoles(Long userId) {
        return roleMapper.selectUserRoles(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignUserRoles(Long userId, List<Long> roleIds) {
        roleMapper.deleteUserRoles(userId);
        if (!roleIds.isEmpty()) {
            roleMapper.insertUserRoles(userId, roleIds);
        }
    }

    @Override
    public List<Long> getRolePermissions(Long roleId) {
        return roleMapper.selectRolePermissions(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRolePermissions(Long roleId, List<Long> permissionIds) {
        roleMapper.deleteRolePermissions(roleId);
        if (!permissionIds.isEmpty()) {
            roleMapper.insertRolePermissions(roleId, permissionIds);
        }
    }
} 