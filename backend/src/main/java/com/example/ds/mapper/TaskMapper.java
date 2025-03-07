package com.example.ds.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ds.entity.Task;
import org.apache.ibatis.annotations.Mapper;

/**
 * 任务 Mapper接口
 */
@Mapper
public interface TaskMapper extends BaseMapper<Task> {
} 