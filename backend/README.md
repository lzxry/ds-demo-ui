# DS管理平台后端

## 项目简介
这是DS管理平台的后端服务，基于 Spring Boot 开发，提供 RESTful API 接口，支持数据统计、验证、访问管理等功能。

## 技术栈
- Spring Boot 3.x
- Spring Security
- MyBatis-Plus
- Redis
- MySQL 8.0
- Maven
- Lombok
- Hutool
- Knife4j (API文档)

## 项目结构
```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/example/ds/
│   │   │   ├── config/          # 配置类
│   │   │   ├── controller/      # 控制器
│   │   │   ├── service/         # 服务层
│   │   │   │   └── impl/       # 服务实现
│   │   │   ├── mapper/         # MyBatis映射
│   │   │   ├── entity/         # 实体类
│   │   │   ├── dto/            # 数据传输对象
│   │   │   ├── vo/             # 视图对象
│   │   │   ├── common/         # 公共类
│   │   │   │   ├── constant/   # 常量
│   │   │   │   ├── exception/  # 异常处理
│   │   │   │   └── utils/      # 工具类
│   │   │   └── security/       # 安全相关
│   │   └── resources/
│   │       ├── mapper/         # MyBatis XML
│   │       ├── application.yml # 主配置文件
│   │       └── application-dev.yml # 开发环境配置
│   └── test/                   # 测试目录
└── pom.xml                     # Maven配置
```

## 开发环境要求
- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 6.0+

## 快速开始

### 1. 环境准备
- 安装 JDK 17
- 安装 Maven 3.8+
- 安装 MySQL 8.0+
- 安装 Redis 6.0+

### 2. 数据库配置
```sql
CREATE DATABASE ds_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 配置文件
复制 `application-dev.yml.example` 为 `application-dev.yml`，修改以下配置：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ds_platform?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
  redis:
    host: localhost
    port: 6379
    password: your_password
```

### 4. 运行项目
```bash
# 进入项目目录
cd backend

# 编译项目
mvn clean package

# 运行项目
java -jar target/ds-platform.jar
```

## API文档
启动项目后访问：http://localhost:8080/doc.html

## 主要功能模块

### 1. 项目访问管理
- 访问权限配置
- 访问类型管理
- 任务调度策略
- 访问统计

### 2. 数据统计
- 数据概览
- 趋势分析
- 数据导出
- 自定义统计

### 3. 数据验证
- 数据模板管理
- 格式验证
- 规则管理

### 4. 近期数据
- 数据记录管理
- 条件筛选
- 数据导出

## 开发规范
1. 代码规范
   - 遵循阿里巴巴Java开发手册
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

## 部署说明
1. 打包
```bash
mvn clean package -Dmaven.test.skip=true
```

2. 运行
```bash
java -jar -Dspring.profiles.active=prod target/ds-platform.jar
```

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