# Manager 管理后台系统

一个基于 **Vue.js 2.6 + SpringBoot 2.5.9 + MySQL** 的前后端分离管理系统，集成了丰富的功能模块和现代化的开发体验。
![Uploading 6f6e2aea4087297a3546781f47ef4a52.png…]()


## 📋 项目简介

Manager 管理后台是一个现代化的 Web 管理系统，采用前后端分离架构设计，支持用户管理、权限控制、文件上传、Markdown 编辑等核心功能。系统具有良好的扩展性和维护性，适合作为企业级管理系统的基础框架。

### 🏗️ 项目架构

```
manager/
├── vue/                    # 前端项目 (Vue.js 2.6)
├── springboot/            # 后端项目 (SpringBoot 2.5.9)
├── database/              # 数据库脚本
├── package.json           # 根目录依赖 (Markdown相关)
└── README.md             # 项目说明文档
```

## 🛠️ 技术栈

### 前端技术栈
- **Vue.js 2.7.16** - 渐进式 JavaScript 框架
- **Element UI 2.15.14** - 基于 Vue 2.0 的桌面端组件库
- **Vue Router 3.5.1** - Vue.js 官方路由管理器
- **Axios 1.5.1** - HTTP 客户端
- **ByteMD 1.22.0** - Markdown 编辑器
- **WangEditor 5.1.23** - 富文本编辑器
- **Highlight.js 11.11.1** - 代码高亮

### 后端技术栈
- **SpringBoot 2.5.9** - Java 应用开发框架 (基于 Java 17)
- **MyBatis 2.2.1** - 持久层框架
- **MySQL 8.0.40** - 关系型数据库
- **PageHelper 1.4.6** - MyBatis 分页插件
- **HuTool 5.8.18** - Java 工具类库
- **JWT 4.3.0** - JSON Web Token 认证
- **Lombok** - Java 注解库

### 开发工具
- **Maven 3.9.9** - 项目管理和构建工具
- **Vue CLI 5.0** - Vue.js 开发工具
- **npm 8.19.4** - Node.js 包管理器

## 📦 环境要求

### 必备环境
| 工具 | 版本要求 | 当前开发环境 | 用途 |
|------|---------|-------------|------|
| **Node.js** | >= 14.0.0 | v16.20.2 | 前端开发环境 |
| **npm** | >= 6.0.0 | 8.19.4 | 包管理器 |
| **Java JDK** | 1.8+ | 17.0.13 (Amazon Corretto) | Java运行环境 |
| **Maven** | >= 3.6.0 | 3.9.9 | Java项目构建 |
| **MySQL** | >= 5.7 | 8.0.40 | 数据库 |
| **Vue.js** | 2.6+ | 2.7.16 | 前端框架 |

### 推荐开发环境
- **操作系统**: Windows 10/11, macOS, Ubuntu 20.04+
- **前端IDE**: VS Code, WebStorm
- **后端IDE**: IntelliJ IDEA, Eclipse
- **数据库工具**: Navicat, MySQL Workbench

### 🏷️ 当前开发环境配置
本项目在以下环境下开发和测试通过：
```
操作系统: Windows 11 (10.0)
Node.js:  v16.20.2
npm:      8.19.4
Vue.js:   2.7.16
Java:     17.0.13 (Amazon Corretto)
Maven:    3.9.9
MySQL:    8.0.40
```

## 🚀 快速开始

### 1. 克隆项目

```bash
git clone <your-repository-url>
cd manager
```

### 2. 数据库配置

#### 创建数据库
```sql
CREATE DATABASE Backend CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

#### 导入数据库脚本
```bash
# 导入主数据库结构
mysql -u root -p Backend < database/backend.sql

# 导入客户包记录表
mysql -u root -p Backend < database/customer_package_record.sql
```

### 3. 后端配置与启动

#### 修改数据库连接配置
编辑 `springboot/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/Backend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
```

#### 配置文件上传路径
```yaml
file:
  upload:
    path: D:/IDEA_Project/Backend/manager/springboot/files/        # 修改为你的文件存储路径
    markdown:
      path: D:/IDEA_Project/Backend/manager/springboot/files/md/   # 修改为你的Markdown文件路径
```

#### 启动后端服务
```bash
# 方式1: Maven 命令启动
cd springboot
mvn clean install
mvn spring-boot:run

# 方式2: IDE 启动
# 使用 IDEA 或 Eclipse 直接运行 SpringbootApplication.java

# 方式3: JAR 包启动
mvn clean package
java -jar target/springboot-0.0.1-SNAPSHOT.jar
```

后端服务默认运行在 `http://localhost:9090`

### 4. 前端配置与启动

#### 安装根目录依赖 (Markdown 相关)
```bash
npm install
```

#### 安装并启动前端
```bash
# 进入前端目录
cd vue

# 安装前端依赖
npm install

# 启动开发服务器
npm run serve

# 构建生产版本
npm run build
```

前端服务默认运行在 `http://localhost:8080`

### 5. 验证安装

访问 `http://localhost:8080`，如果能正常显示登录页面，说明安装成功。

## 📁 项目结构详解

```
manager/
├── vue/                          # 前端 Vue.js 项目
│   ├── public/                   # 静态资源
│   │   ├── favicon.ico          # 网站图标
│   │   └── index.html           # HTML模板
│   ├── src/                      # 源代码
│   │   ├── assets/              # 静态资源 (CSS、图片等)
│   │   │   ├── css/             # 样式文件
│   │   │   └── imgs/            # 图片资源
│   │   ├── components/          # 公共组件
│   │   ├── router/              # 路由配置
│   │   │   └── index.js         # 路由定义
│   │   ├── utils/               # 工具类
│   │   │   ├── request.js       # HTTP请求封装
│   │   │   └── permissions.js   # 权限工具
│   │   ├── views/               # 页面组件
│   │   │   ├── manager/         # 管理员页面
│   │   │   ├── customer/        # 客户页面
│   │   │   └── front/           # 前台页面
│   │   ├── App.vue              # 根组件
│   │   └── main.js              # 入口文件
│   ├── babel.config.js          # Babel 配置
│   ├── jsconfig.json            # JavaScript 配置
│   ├── package.json             # 前端依赖
│   └── vue.config.js            # Vue CLI 配置
├── springboot/                   # 后端 SpringBoot 项目
│   ├── src/main/
│   │   ├── java/com/example/
│   │   │   ├── common/          # 公共类
│   │   │   │   ├── config/      # 配置类
│   │   │   │   ├── enums/       # 枚举类
│   │   │   │   ├── Constants.java    # 常量类
│   │   │   │   └── Result.java       # 统一返回结果
│   │   │   ├── controller/      # 控制器层
│   │   │   ├── entity/          # 实体类
│   │   │   ├── exception/       # 异常处理
│   │   │   ├── mapper/          # 数据访问层
│   │   │   ├── service/         # 业务逻辑层
│   │   │   ├── utils/           # 工具类
│   │   │   └── SpringbootApplication.java  # 启动类
│   │   └── resources/           # 配置文件
│   │       ├── application.yml  # 主配置文件
│   │       ├── mapper/          # MyBatis XML映射
│   │       ├── db/              # 数据库脚本
│   │       └── templates/       # 模板文件
│   ├── files/                   # 文件上传目录 (运行时生成)
│   └── pom.xml                  # Maven 配置
├── database/                     # 数据库脚本
│   ├── backend.sql              # 主数据库结构
│   └── customer_package_record.sql  # 客户包记录表
├── package.json                 # 根目录依赖 (Markdown)
├── .gitignore                   # Git 忽略文件
├── DEPLOY.md                    # 部署指南
└── README.md                    # 项目说明
```

## ⚙️ 配置详解

### 后端配置 (application.yml)

```yaml
server:
  address: 0.0.0.0              # 服务绑定地址
  port: 9090                    # 服务端口

spring:
  datasource:                   # 数据库配置
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/Backend?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
  servlet:
    multipart:                  # 文件上传配置
      max-file-size: 100MB
      max-request-size: 100MB
  jackson:                      # JSON序列化配置
    date-format: yyyy-MM-dd HH:mm:ss
    time-zone: Asia/Shanghai

mybatis:                        # MyBatis配置
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.example.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl

pagehelper:                     # 分页配置
  helper-dialect: mysql
  reasonable: true
```

### 前端配置 (vue.config.js)

```javascript
module.exports = {
  transpileDependencies: true,
  devServer: {
    host: '0.0.0.0',
    port: 8080,
    proxy: {                    # 开发环境代理配置
      '/api': {
        target: 'http://localhost:9090',
        changeOrigin: true
      }
    }
  },
  chainWebpack: config => {
    config.plugin('html')
      .tap(args => {
        args[0].title = "管理系统";
        return args;
      })
  }
}
```

## 🎯 核心功能

### 已实现功能
- ✅ **用户认证系统** - JWT Token 认证
- ✅ **权限管理** - 基于角色的访问控制 (RBAC)
- ✅ **响应式管理界面** - 支持多种屏幕尺寸
- ✅ **数据分页查询** - PageHelper 分页插件
- ✅ **文件上传下载** - 支持多种文件格式
- ✅ **Markdown 编辑器** - ByteMD 集成
- ✅ **富文本编辑器** - WangEditor 集成
- ✅ **代码高亮显示** - Highlight.js 支持
- ✅ **数学公式支持** - KaTeX 数学公式渲染
- ✅ **流程图支持** - Mermaid 图表渲染
- ✅ **暗黑模式** - 支持主题切换
- ✅ **幸运大转盘** - 团队决策工具
- ✅ **一键打包** - CI/CD 集成工具

### 系统特色
- 🎨 **现代化UI设计** - Element UI + 自定义主题
- 🚀 **高性能** - 前后端分离 + 数据库优化
- 🔒 **安全可靠** - JWT认证 + 权限控制
- 📱 **响应式设计** - 支持移动端访问
- 🛠️ **易于扩展** - 模块化架构设计

## 📊 依赖版本管理

### 后端核心依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| spring-boot-starter-parent | 2.5.9 | SpringBoot 基础框架 |
| mybatis-spring-boot-starter | 2.2.1 | MyBatis 集成 |
| pagehelper-spring-boot-starter | 1.4.6 | 分页插件 |
| hutool-all | 5.8.18 | Java 工具库 |
| java-jwt | 4.3.0 | JWT 认证 |
| mysql-connector-java | - | MySQL 驱动 |
| lombok | - | 代码简化注解 |

### 前端核心依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| vue | 2.7.16 | Vue.js 框架 |
| element-ui | 2.15.14 | UI 组件库 |
| vue-router | 3.5.1 | 路由管理 |
| axios | 1.5.1 | HTTP 客户端 |
| @bytemd/vue | 1.22.0 | Markdown 编辑器 |
| @wangeditor/editor | 5.1.23 | 富文本编辑器 |
| highlight.js | 11.11.1 | 代码高亮 |

### Markdown 插件生态

| 插件 | 版本 | 功能 |
|------|------|------|
| @bytemd/plugin-gfm | 1.22.0 | GitHub 风格 Markdown |
| @bytemd/plugin-highlight | 1.22.0 | 代码高亮 |
| @bytemd/plugin-math | 1.22.0 | 数学公式 (KaTeX) |
| @bytemd/plugin-mermaid | 1.22.0 | 流程图、时序图 |
| github-markdown-css | 5.8.1 | GitHub 样式 |
| juejin-markdown-themes | 1.34.0 | 掘金主题 |

## 🔧 开发指南

### 本地开发环境搭建

1. **安装 Node.js 环境**
   ```bash
   # 使用 nvm 管理 Node.js 版本 (推荐)
   nvm install 16.20.2
   nvm use 16.20.2
   
   # 验证安装 (期望输出：v16.20.2)
   node -v
   # 验证安装 (期望输出：8.19.4)
   npm -v
   ```

2. **安装 Java 开发环境**
   ```bash
   # 验证 Java 安装 (期望输出：java 17.0.13)
   java -version
   javac -version
   
   # 验证 Maven 安装 (期望输出：Apache Maven 3.9.9)
   mvn -version
   ```

3. **配置 MySQL 数据库**
   ```sql
   # 创建用户 (可选)
   CREATE USER 'manager'@'localhost' IDENTIFIED BY 'your_password';
   GRANT ALL PRIVILEGES ON Backend.* TO 'manager'@'localhost';
   FLUSH PRIVILEGES;
   ```

### API 接口文档

后端 API 基础地址：`http://localhost:9090/api`

主要接口模块：
- `/admin/**` - 管理员接口
- `/customer/**` - 客户管理接口
- `/notice/**` - 公告管理接口
- `/file/**` - 文件管理接口

### 前端开发规范

1. **组件命名**: 使用 PascalCase
2. **文件命名**: 使用 kebab-case
3. **路由配置**: 在 `router/index.js` 中统一管理
4. **API 调用**: 使用 `utils/request.js` 封装的 axios 实例

### 后端开发规范

1. **包结构**: 按功能模块划分
2. **命名规范**: 遵循 Java 驼峰命名法
3. **异常处理**: 使用 `@ControllerAdvice` 全局异常处理
4. **接口返回**: 统一使用 `Result` 类封装

## 🚀 部署指南

### 开发环境部署
```bash
# 前端开发服务器
cd vue && npm run serve

# 后端开发服务器
cd springboot && mvn spring-boot:run
```

### 生产环境部署
```bash
# 前端构建
cd vue
npm run build

# 后端构建
cd ../springboot
mvn clean package

# 启动后端服务
java -jar target/springboot-0.0.1-SNAPSHOT.jar
```

### Docker 部署 (可选)
```dockerfile
# Dockerfile 示例
FROM openjdk:8-jre-slim
COPY target/springboot-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🐛 常见问题

### 1. 数据库连接失败
- 检查 MySQL 服务是否启动
- 确认数据库名、用户名、密码是否正确
- 检查防火墙和端口配置

### 2. 前端代理失败
- 确认后端服务已启动 (9090端口)
- 检查 `vue.config.js` 中的代理配置
- 清除浏览器缓存

### 3. Maven 依赖下载慢
- 配置阿里云 Maven 镜像 (已在 pom.xml 中配置)
- 使用 VPN 或更换网络环境

### 4. Node.js 依赖安装失败
```bash
# 清除缓存
npm cache clean --force
rm -rf node_modules package-lock.json

# 重新安装
npm install
```

## 📄 许可证

本项目采用 MIT 许可证，详情请参见 [LICENSE](LICENSE) 文件。

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request 来帮助改进项目！

## 📞 联系方式

如有问题或建议，请通过以下方式联系：
- 提交 GitHub Issue
- 发送邮件至项目维护者

---

**开发环境**: Windows 11 + Node.js v16.20.2 + Java 17.0.13 + Maven 3.9.9 + MySQL 8.0.40

**最后更新时间**: 2024年12月19日

**项目版本**: v1.0.0 
