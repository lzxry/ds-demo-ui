# DS 管理端系统架构图

```mermaid
graph TB
    subgraph 用户管理模块
        A1[接单者管理]
        A2[Token管理]
        A3[任务统计]
    end

    subgraph 项目管理模块
        B1[项目CRUD]
        B2[项目列表]
        B3[项目状态管理]
    end

    subgraph 数据统计模块
        C1[数据筛选]
        C2[数据展示]
        C3[核算管理]
    end

    subgraph 项目接入模块
        D1[API接入]
        D2[数据任务管理]
        D3[规则引擎]
    end

    subgraph 近期数据模块
        E1[最新数据展示]
        E2[数据筛选]
        E3[原始数据查看]
    end

    subgraph 安全管理
        F1[访问控制]
        F2[认证授权]
        F3[日志管理]
    end

    %% 模块间关系
    A1 --> A2
    A2 --> A3
    B1 --> B2
    B2 --> B3
    C1 --> C2
    C2 --> C3
    D1 --> D2
    D2 --> D3
    E1 --> E2
    E2 --> E3
    F1 --> F2
    F2 --> F3

    %% 跨模块关系
    B1 --> D1
    D2 --> C1
    C2 --> E1
    F1 --> A1
    F1 --> B1
    F1 --> C1
    F1 --> D1
    F1 --> E1
```

## 系统模块说明

### 1. 用户管理模块
- 接单者账号管理
- Token 生成与管理
- 任务完成情况统计

### 2. 项目管理模块
- 项目创建与编辑
- 项目列表管理
- 项目状态控制

### 3. 数据统计模块
- 多维度数据筛选
- 数据可视化展示
- 核算状态管理

### 4. 项目接入模块
- 多方式 API 接入
- 数据任务管理
- 规则引擎配置

### 5. 近期数据模块
- 最新数据展示
- 数据筛选功能
- 原始数据查看

### 6. 安全管理
- 访问控制
- 认证授权
- 日志管理 