package com.example.ds.service.impl;

import com.example.ds.entity.DemandUser;
import com.example.ds.entity.WorkerUser;
import com.example.ds.mapper.DemandUserMapper;
import com.example.ds.mapper.WorkerUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 用户详情服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final DemandUserMapper demandUserMapper;
    private final WorkerUserMapper workerUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 先查询需求方用户
        DemandUser demandUser = demandUserMapper.selectByUsername(username);
        if (demandUser != null) {
            return new User(
                demandUser.getUsername(),
                demandUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_DEMAND"))
            );
        }

        // 再查询接单者用户
        WorkerUser workerUser = workerUserMapper.selectByUsername(username);
        if (workerUser != null) {
            return new User(
                workerUser.getUsername(),
                workerUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_WORKER"))
            );
        }

        throw new UsernameNotFoundException("用户不存在");
    }
} 