package com.example.ds.service;

import com.example.ds.common.PageResult;
import com.example.ds.dto.TaskRequest;
import com.example.ds.dto.TaskQueryRequest;
import com.example.ds.entity.Task;

public interface TaskService {
    Task createTask(TaskRequest request);
    Task getTask(Long id);
    PageResult<Task> listTasks(Integer page, Integer size, TaskQueryRequest query);
    void submitTaskResult(Long id, TaskRequest request);
    void updateTaskStatus(Long id, Integer status);
    void deleteTask(Long id);
} 