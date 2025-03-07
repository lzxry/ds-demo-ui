# DS管理平台

## 项目简介
这是一个基于 Vue 3 + Spring Boot 的数据统计和管理平台，包含前端和后端两个子项目。

## 项目结构
```
ds-platform/
├── frontend/          # 前端项目 (Vue 3 + Element Plus)
└── backend/           # 后端项目 (Spring Boot)
```

## 技术栈
### 前端
- Vue 3
- TypeScript
- Element Plus
- Vue Router
- SCSS
- Vite

### 后端
- Spring Boot 3.x
- Spring Security
- MyBatis-Plus
- Redis
- MySQL 8.0
- Maven
- Lombok
- Hutool
- Knife4j (API文档)

## 快速开始

### 前端开发
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

### 后端开发
```bash
# 进入后端项目目录
cd backend

# 编译项目
mvn clean package

# 运行项目
java -jar target/ds-platform.jar
```

## 详细文档
- [前端文档](frontend/README.md)
- [后端文档](backend/README.md)

## 开发规范
1. 代码规范
   - 前端遵循 Vue 3 官方风格指南
   - 后端遵循阿里巴巴Java开发手册
   - 使用统一的代码格式化工具
   - 保持代码注释的完整性

2. 提交规范
   - feat: 新功能
   - fix: 修复bug
   - docs: 文档更新
   - style: 代码格式调整
   - refactor: 重构
   - test: 测试相关
   - chore: 构建过程或辅助工具的变动

3. 分支管理
   - main: 主分支
   - develop: 开发分支
   - feature/*: 功能分支
   - hotfix/*: 紧急修复分支

## 注意事项
- 确保开发环境满足要求
- 遵循代码规范和提交规范
- 保持代码注释的完整性
- 定期更新依赖包
- 注意数据安全性

## 贡献指南
1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 创建 Pull Request

## 许可证
本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情