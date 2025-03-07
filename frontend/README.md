# 数据服务对接平台前端

## 项目介绍
数据服务对接平台是一个用于管理和对接不同数据源的项目，支持多种对接方式（拉取、推送、离线），提供完整的任务管理和数据统计功能。

## 技术栈
- Vue 3
- TypeScript
- Vite
- Element Plus
- Pinia
- Vue Router
- Axios
- ECharts

## 功能特性
- 项目对接管理
  - 支持多种对接方式（拉取、推送、离线）
  - 对接配置管理
  - 状态监控
- 任务管理
  - 任务列表
  - 任务详情
  - 任务状态跟踪
- 数据统计
  - 实时统计
  - 历史数据
  - 图表展示
- 系统管理
  - 用户管理
  - 角色权限
  - 系统配置

## 开发环境要求
- Node.js >= 16.0.0
- npm >= 7.0.0

## 安装说明
1. 克隆项目
```bash
git clone [项目地址]
cd frontend
```

2. 安装依赖
```bash
npm install
```

3. 启动开发服务器
```bash
npm run dev
```

4. 构建生产版本
```bash
npm run build
```

## 项目结构
```
frontend/
├── src/
│   ├── api/                # API接口
│   ├── assets/            # 静态资源
│   ├── components/        # 公共组件
│   ├── composables/       # 组合式函数
│   ├── layouts/           # 布局组件
│   ├── router/            # 路由配置
│   ├── stores/            # 状态管理
│   ├── styles/            # 全局样式
│   ├── types/             # TypeScript类型
│   ├── utils/             # 工具函数
│   └── views/             # 页面组件
├── public/                # 公共资源
├── index.html             # HTML模板
├── package.json           # 项目配置
├── tsconfig.json          # TypeScript配置
└── vite.config.ts         # Vite配置
```

## 开发指南

### 代码规范
- 使用ESLint进行代码检查
- 使用Prettier进行代码格式化
- 遵循Vue 3组合式API风格
- 使用TypeScript进行类型检查

### 提交规范
提交信息格式：
```
<type>(<scope>): <subject>

<body>

<footer>
```

type类型：
- feat: 新功能
- fix: 修复bug
- docs: 文档更新
- style: 代码格式
- refactor: 重构
- test: 测试
- chore: 构建过程或辅助工具的变动

### 开发流程
1. 创建功能分支
```bash
git checkout -b feature/xxx
```

2. 开发功能
3. 提交代码
```bash
git add .
git commit -m "feat: xxx"
```

4. 推送到远程
```bash
git push origin feature/xxx
```

5. 创建合并请求

## 部署说明
1. 构建生产版本
```bash
npm run build
```

2. 部署dist目录下的文件到Web服务器

## 环境变量
项目使用以下环境变量：
- VITE_API_BASE_URL: API基础地址
- VITE_APP_TITLE: 应用标题

## 常见问题
1. 安装依赖失败
   - 检查Node.js版本
   - 清除npm缓存后重试
   - 使用cnpm或yarn

2. 开发服务器启动失败
   - 检查端口占用
   - 检查环境变量配置
   - 检查依赖安装

## 联系方式
如有问题，请联系项目负责人或提交Issue。

## 许可证
[MIT License](LICENSE) 