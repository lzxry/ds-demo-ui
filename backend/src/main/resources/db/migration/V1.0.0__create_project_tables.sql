-- 创建项目对接表
CREATE TABLE `project_access` (
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
    PRIMARY KEY (`id`),
    KEY `idx_type` (`type`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目对接配置表';

-- 创建任务表
CREATE TABLE `task` (
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
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_task_id` (`task_id`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务表';

-- 创建任务统计表
CREATE TABLE `task_statistics` (
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
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_project_date` (`project_id`,`stat_date`),
    KEY `idx_stat_date` (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务统计表'; 