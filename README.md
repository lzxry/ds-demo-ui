# 数据统计和管理系统

## 项目简介
这是一个基于 Vue 3 + Element Plus 的数据统计和管理系统，用于管理和展示项目访问、数据统计、数据验证和近期数据等功能。系统提供直观的用户界面和完整的数据管理功能。

## 功能特性
- 📊 数据统计：支持多维度数据统计和可视化展示
- 🔍 数据验证：提供数据验证和模板管理功能
- 📈 近期数据：展示最新的数据记录
- 🎯 项目访问：管理项目访问权限和配置
- 🔄 实时更新：数据实时更新，支持动态刷新

## 技术栈
- Vue 3
- TypeScript
- Element Plus
- Vue Router
- SCSS
- Vite

## 项目结构
```
├── src/
│   ├── views/          # 页面组件
│   │   ├── project-access/  # 项目访问管理
│   │   ├── statistics/      # 数据统计
│   │   ├── data-validation/ # 数据验证
│   │   └── recent-data/     # 近期数据
│   ├── components/     # 公共组件
│   ├── router/         # 路由配置
│   ├── store/          # 状态管理
│   ├── api/            # API接口
│   └── styles/         # 样式文件
├── public/             # 静态资源
└── package.json        # 项目配置
```

## 开发环境要求
- Node.js >= 16
- npm >= 7

## 安装和运行

```bash
# 安装依赖
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build
```

## 使用说明

### 1. 项目访问管理
- 配置项目访问权限
- 管理访问类型（PULL_PUSH/PUSH_PULL/OFFLINE）
- 设置任务调度策略
- 查看访问统计

### 2. 数据统计
- 查看数据概览
- 分析数据趋势
- 导出统计数据
- 自定义统计维度

### 3. 数据验证
- 查看数据模板
- 验证数据格式
- 复制模板数据
- 管理验证规则

### 4. 近期数据
- 查看最新数据记录
- 按条件筛选数据
- 导出数据记录
- 查看数据详情

## 注意事项
- 确保开发环境满足要求
- 遵循代码规范和提交规范
- 保持代码注释的完整性
- 定期更新依赖包

## 贡献指南
1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 许可证
本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情