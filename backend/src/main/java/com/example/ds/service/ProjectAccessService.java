package com.example.ds.service;

import com.example.ds.dto.ProjectAccessRequest;
import com.example.ds.entity.ProjectAccess;
import com.example.ds.common.PageResult;

public interface ProjectAccessService {
    ProjectAccess createAccess(ProjectAccessRequest request);
    ProjectAccess updateAccess(Long id, ProjectAccessRequest request);
    void deleteAccess(Long id);
    ProjectAccess getAccess(Long id);
    PageResult<ProjectAccess> listAccess(Integer page, Integer size, String keyword);
    void updateStatus(Long id, Integer status);
} 