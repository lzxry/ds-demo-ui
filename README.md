# 多平台任务管理系统

## 项目简介
这是一个基于 Vue 3 + Node.js 开发的多平台任务管理系统，用于管理不同电商平台（拼多多、京东、淘宝）的任务下发、执行和回传流程。系统提供完整的用户管理、Token管理、任务管理和数据统计功能。

## 功能特性
- 🎯 多平台支持：支持拼多多、京东、淘宝等多个电商平台
- 👥 用户管理：完整的用户信息管理，包括联系方式、状态等
- 🔑 Token管理：Token的生成、分配、状态管理和使用统计
- 📊 数据统计：多维度数据统计和可视化展示
- 📈 趋势分析：支持日环比、周环比等数据趋势分析
- 🔄 实时更新：数据实时更新，支持动态刷新

## 技术栈
### 前端
- Vue 3
- TypeScript
- Element Plus
- ECharts
- Vite

### 后端
- Node.js
- Express
- MySQL
- Redis

## 项目结构
```
├── frontend/                # 前端项目
│   ├── src/
│   │   ├── views/          # 页面组件
│   │   │   ├── dashboard/  # 数据统计
│   │   │   ├── user/       # 用户管理
│   │   │   ├── token/      # Token管理
│   │   │   └── task/       # 任务管理
│   │   ├── components/     # 公共组件
│   │   ├── router/         # 路由配置
│   │   ├── store/          # 状态管理
│   │   └── api/            # API接口
│   └── package.json
│
├── backend/                 # 后端项目
│   ├── src/
│   │   ├── controllers/    # 控制器
│   │   ├── models/         # 数据模型
│   │   ├── routes/         # 路由
│   │   ├── services/       # 业务逻辑
│   │   └── utils/          # 工具函数
│   └── package.json
│
└── README.md
```

## 核心功能模块

### 1. 用户管理
- 用户列表展示和管理
- 用户信息维护（用户名、联系方式等）
- 用户状态管理
- Token关联管理

### 2. Token管理
- Token列表展示
- Token状态管理（启用/封禁）
- Token生成和分配
- Token备注管理
- Token使用统计

### 3. 任务管理
- 任务列表展示
- 任务状态管理
- 任务分配
- 任务执行跟踪
- 任务回传管理

### 4. 数据统计
- 项目数据概览
  - 今日下发量和回传量（日环比）
  - 本周下发量和回传量（周环比）
- 趋势分析
  - 任务下发趋势
  - 任务完成率
- 分布统计
  - Token任务分布
  - 任务类型分布

## 开发环境要求
- Node.js >= 16
- MySQL >= 8.0
- Redis >= 6.0

## 安装和运行

### 前端
```bash
# 进入前端项目目录
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build
```

### 后端
```bash
# 进入后端项目目录
cd backend

# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 启动生产服务器
npm start
```

## 环境配置
1. 复制 `.env.example` 为 `.env`
2. 配置数据库连接信息
3. 配置 Redis 连接信息
4. 配置其他必要的环境变量

## 部署
1. 前端构建
```bash
cd frontend
npm run build
```

2. 后端部署
```bash
cd backend
npm install --production
npm start
```

## 贡献指南
1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 版本历史
- v1.0.0
  - 初始版本发布
  - 实现基础功能

## 维护者
- [维护者姓名](https://github.com/username)

## 许可证
本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情