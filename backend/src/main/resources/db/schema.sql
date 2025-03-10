-- 创建数据库
CREATE DATABASE IF NOT EXISTS ds_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ds_platform;

-- 用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(100) NOT NULL COMMENT '密码',
    `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 项目对接配置表
CREATE TABLE IF NOT EXISTS `project_access` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` varchar(100) NOT NULL COMMENT '对接名称',
    `type` varchar(20) NOT NULL COMMENT '对接类型：PULL(拉取)、PUSH(推送)、OFFLINE(离线)',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
    `api_key` varchar(100) DEFAULT NULL COMMENT 'API密钥',
    `api_secret` varchar(100) DEFAULT NULL COMMENT 'API密钥',
    `api_url` varchar(255) DEFAULT NULL COMMENT 'API地址',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
    `config` text DEFAULT NULL COMMENT '配置信息(JSON格式)',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_type` (`type`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目对接配置表';

-- 任务表
CREATE TABLE IF NOT EXISTS `task` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `project_id` bigint(20) NOT NULL COMMENT '项目ID',
    `task_id` varchar(100) NOT NULL COMMENT '外部任务ID',
    `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态：0-待处理，1-处理中，2-已完成，3-失败',
    `result_path` varchar(255) DEFAULT NULL COMMENT '结果文件路径',
    `error_message` varchar(500) DEFAULT NULL COMMENT '错误信息',
    `start_time` datetime DEFAULT NULL COMMENT '开始时间',
    `end_time` datetime DEFAULT NULL COMMENT '结束时间',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_task_id` (`task_id`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';

-- 任务统计表
CREATE TABLE IF NOT EXISTS `task_statistics` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `project_id` bigint(20) NOT NULL COMMENT '项目ID',
    `stat_date` date NOT NULL COMMENT '统计日期',
    `total_count` int(11) NOT NULL DEFAULT '0' COMMENT '总任务数',
    `completed_count` int(11) NOT NULL DEFAULT '0' COMMENT '已完成数',
    `failed_count` int(11) NOT NULL DEFAULT '0' COMMENT '失败数',
    `success_rate` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '成功率',
    `avg_process_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '平均处理时间(毫秒)',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_project_date` (`project_id`,`stat_date`),
    KEY `idx_stat_date` (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务统计表';

-- 数据统计表
CREATE TABLE IF NOT EXISTS `data_statistics` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `project_id` bigint(20) NOT NULL COMMENT '项目ID',
    `stat_date` date NOT NULL COMMENT '统计日期',
    `pull_count` int(11) NOT NULL DEFAULT '0' COMMENT '拉取数量',
    `push_count` int(11) NOT NULL DEFAULT '0' COMMENT '推送数量',
    `success_rate` decimal(5,2) NOT NULL DEFAULT '0.00' COMMENT '成功率',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_project_date` (`project_id`,`stat_date`),
    KEY `idx_stat_date` (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据统计表';

-- 数据验证模板表
CREATE TABLE IF NOT EXISTS `data_template` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `template_name` varchar(100) NOT NULL COMMENT '模板名称',
    `template_content` json NOT NULL COMMENT '模板内容',
    `description` varchar(500) DEFAULT NULL COMMENT '描述',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据验证模板表';

-- 近期数据表
CREATE TABLE IF NOT EXISTS `recent_data` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `project_id` bigint(20) NOT NULL COMMENT '项目ID',
    `data_content` json NOT NULL COMMENT '数据内容',
    `submit_time` datetime NOT NULL COMMENT '提交时间',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态：0-失败，1-成功',
    `error_message` varchar(500) DEFAULT NULL COMMENT '错误信息',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_status` (`status`),
    KEY `idx_submit_time` (`submit_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='近期数据表';

-- 插入初始管理员用户（密码：123456）
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `email`, `status`)
VALUES ('admin', '$2a$10$X/hX4J3.9z9z9z9z9z9z9O9z9z9z9z9z9z9z9z9z9z9z9z9z9z9', '系统管理员', 'admin@example.com', 1);