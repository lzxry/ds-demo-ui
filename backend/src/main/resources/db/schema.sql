-- 创建数据库
CREATE DATABASE IF NOT EXISTS ds_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ds_platform;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    UNIQUE KEY uk_username (username)
) COMMENT '用户表';

-- 项目访问配置表
CREATE TABLE IF NOT EXISTS project_access (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    project_name VARCHAR(100) NOT NULL COMMENT '项目名称',
    access_type VARCHAR(20) NOT NULL COMMENT '访问类型：PULL_PUSH-拉取推送，PUSH_PULL-推送拉取，OFFLINE-离线',
    task_strategy VARCHAR(50) COMMENT '任务调度策略',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'
) COMMENT '项目访问配置表';

-- 数据统计表
CREATE TABLE IF NOT EXISTS data_statistics (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    project_id BIGINT NOT NULL COMMENT '项目ID',
    stat_date DATE NOT NULL COMMENT '统计日期',
    pull_count INT DEFAULT 0 COMMENT '拉取数量',
    push_count INT DEFAULT 0 COMMENT '推送数量',
    success_rate DECIMAL(5,2) DEFAULT 0 COMMENT '成功率',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    UNIQUE KEY uk_project_date (project_id, stat_date)
) COMMENT '数据统计表';

-- 数据验证模板表
CREATE TABLE IF NOT EXISTS data_template (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    template_name VARCHAR(100) NOT NULL COMMENT '模板名称',
    template_content JSON NOT NULL COMMENT '模板内容',
    description VARCHAR(500) COMMENT '描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'
) COMMENT '数据验证模板表';

-- 近期数据表
CREATE TABLE IF NOT EXISTS recent_data (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    project_id BIGINT NOT NULL COMMENT '项目ID',
    data_content JSON NOT NULL COMMENT '数据内容',
    submit_time DATETIME NOT NULL COMMENT '提交时间',
    status TINYINT DEFAULT 1 COMMENT '状态：0-失败，1-成功',
    error_message VARCHAR(500) COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除'
) COMMENT '近期数据表';

-- 插入初始管理员用户（密码：123456）
INSERT INTO sys_user (username, password, real_name, email, status)
VALUES ('admin', '$2a$10$X/hX4J3.9z9z9z9z9z9z9O9z9z9z9z9z9z9z9z9z9z9z9z9z9z9', '系统管理员', 'admin@example.com', 1); 