# Iass 组管理后台系统

一个基于 Vue.js + SpringBoot + MySQL 的前后端分离管理系统，支持 Markdown 编辑和富文本功能。

## 📋 项目简介

Iass 组管理后台是一个现代化的 Web 管理系统，采用前后端分离架构设计，提供了完整的用户管理、数据管理等功能。

### 🏗️ 技术架构

```
├── vue/                    # 前端项目 (Vue.js)
├── springboot/            # 后端项目 (SpringBoot)
├── database/              # 数据库脚本
├── package.json           # 根目录依赖 (Markdown相关)
└── README.md             # 项目说明文档
```

## 🛠️ 技术栈

### 前端技术栈
- **Vue.js 2.6** - 渐进式 JavaScript 框架
- **Element UI 2.15** - 基于 Vue 2.0 的桌面端组件库
- **Vue Router 3.5** - Vue.js 官方路由管理器
- **Axios** - HTTP 客户端
- **ByteMD** - Markdown 编辑器
- **WangEditor** - 富文本编辑器

### 后端技术栈
- **SpringBoot 2.5.9** - Java 应用开发框架
- **MyBatis** - 持久层框架
- **MySQL** - 关系型数据库
- **PageHelper** - MyBatis 分页插件
- **HuTool** - Java 工具类库
- **JWT** - JSON Web Token 认证
- **Lombok** - Java 注解库

### 开发工具
- **Maven** - 项目管理和构建工具
- **npm/yarn** - Node.js 包管理器

## 📦 环境要求

### 开发环境
- **Node.js**: >= 14.0.0
- **npm**: >= 6.0.0
- **Java**: JDK 1.8+
- **Maven**: >= 3.6.0
- **MySQL**: >= 5.7

### 推荐 IDE
- **前端**: VS Code, WebStorm
- **后端**: IntelliJ IDEA, Eclipse

## 🚀 快速开始

### 1. 克隆项目

```bash
git clone https://github.com/kafukaiiilove/Iass.git
cd Iass
```

### 2. 数据库配置

```sql
# 创建数据库
CREATE DATABASE iass_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据库结构和数据
mysql -u root -p iass_db < database/backend.sql
mysql -u root -p iass_db < database/customer_package_record.sql
```

### 3. 后端配置与启动

```bash
# 进入后端目录
cd springboot

# 修改数据库连接配置
# 编辑 src/main/resources/application.properties 或 application.yml
# 配置数据库连接信息：
# spring.datasource.url=jdbc:mysql://localhost:3306/iass_db
# spring.datasource.username=your_username
# spring.datasource.password=your_password

# 安装依赖并启动
mvn clean install
mvn spring-boot:run

# 或者使用 IDE 直接运行 SpringbootApplication.java
```

后端服务默认运行在 `http://localhost:8080`

### 4. 前端配置与启动

```bash
# 安装根目录 Markdown 相关依赖
npm install

# 进入前端目录
cd vue

# 安装前端依赖
npm install

# 启动开发服务器
npm run serve
```

前端服务默认运行在 `http://localhost:8081`

### 5. 生产环境构建

```bash
# 前端构建
cd vue
npm run build

# 后端构建
cd ../springboot
mvn clean package
```

## 📁 项目结构详解

```
Iass/
├── vue/                          # 前端 Vue.js 项目
│   ├── public/                   # 静态资源文件
│   │   ├── assets/              # 资源文件 (图片、样式等)
│   │   ├── src/                 # 源代码目录
│   │   │   ├── components/      # Vue 组件
│   │   │   ├── router/          # 路由配置
│   │   │   ├── utils/           # 工具类
│   │   │   ├── views/           # 页面组件
│   │   │   ├── App.vue          # 根组件
│   │   │   └── main.js          # 入口文件
│   │   ├── babel.config.js      # Babel 配置
│   │   ├── jsconfig.json        # JavaScript 配置
│   │   ├── package.json         # 前端依赖配置
│   │   └── vue.config.js        # Vue CLI 配置
│   ├── springboot/               # 后端 SpringBoot 项目
│   │   ├── src/main/java/com/example/
│   │   │   ├── common/          # 公共类
│   │   │   ├── controller/      # 控制器层
│   │   │   ├── entity/          # 实体类
│   │   │   ├── exception/       # 异常处理
│   │   │   ├── mapper/          # 数据访问层
│   │   │   ├── service/         # 业务逻辑层
│   │   │   ├── utils/           # 工具类
│   │   │   └── SpringbootApplication.java  # 启动类
│   │   ├── src/main/resources/  # 配置文件
│   │   └── pom.xml              # Maven 配置
│   ├── database/                 # 数据库脚本
│   │   ├── backend.sql          # 主数据库结构
│   │   └── customer_package_record.sql  # 客户包记录表
│   ├── package.json             # 根目录依赖 (Markdown 插件)
│   ├── .gitignore               # Git 忽略文件
│   └── README.md                # 项目说明文档
```

## 🔧 开发指南

### API 接口

后端 API 接口默认地址：`http://localhost:8080/api`

### 前端代理配置

前端开发时已配置代理，可直接调用后端接口：

```javascript
// vue/src/utils/request.js
const request = axios.create({
  baseURL: process.env.VUE_APP_BASEURL || 'http://localhost:8080',
  timeout: 5000
})
```

### Markdown 功能

项目集成了多个 Markdown 相关插件：
- `@bytemd/vue2` - Vue 2 Markdown 编辑器
- `@bytemd/plugin-gfm` - GitHub 风格 Markdown 支持
- `@bytemd/plugin-highlight` - 代码高亮
- `@bytemd/plugin-math` - 数学公式支持
- `@bytemd/plugin-mermaid` - 流程图支持

## 🎯 功能特性

- ✅ 用户认证与授权 (JWT)
- ✅ 响应式后台管理界面
- ✅ Markdown 编辑器支持
- ✅ 富文本编辑功能
- ✅ 数据分页查询
- ✅ RESTful API 设计
- ✅ 异常统一处理
- ✅ 数据库连接池
- ✅ 跨域请求支持

## 🤝 贡献指南

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 📝 更新日志

### v0.1.0 (Current)
- 初始版本发布
- 基础的前后端架构搭建
- 用户管理功能
- Markdown 编辑器集成

## 📄 许可证

本项目采用 MIT 许可证 - 详情请查看 [LICENSE](LICENSE) 文件

## 📞 联系方式

- 项目地址: [https://github.com/kafukaiiilove/Iass](https://github.com/kafukaiiilove/Iass)
- 问题反馈: [Issues](https://github.com/kafukaiiilove/Iass/issues)

## 🙏 致谢

感谢所有为本项目做出贡献的开发者！

---

**注意**: 本项目仅供学习和研究使用，请勿用于商业用途。 