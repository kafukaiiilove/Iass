# 项目部署指南

## 克隆项目到本地

```bash
git clone https://github.com/kafukaiiilove/Iass.git
cd Iass
```

## 快速启动步骤

### 1. 安装根目录依赖
```bash
npm install
```

### 2. 启动后端服务
```bash
cd springboot
# 修改 src/main/resources/application.yml 中的数据库配置
mvn spring-boot:run
```

### 3. 启动前端服务
```bash
cd vue
npm install
npm run serve
```

### 4. 访问系统
- 前端地址：http://localhost:8080 (Vue配置的端口)
- 后端API：http://localhost:9090

## 环境要求

根据项目实际配置，需要以下环境：

### 必需软件
- **Java**: JDK 1.8+ (项目使用Java 1.8)
- **Maven**: 3.6+ (用于后端依赖管理)
- **Node.js**: 14+ (Vue CLI 5.0需要)
- **npm**: 6+ (随Node.js安装) 
- **MySQL**: 5.7+ (数据库)

### Vue CLI 版本
- 项目使用 `@vue/cli-service ~5.0.0`
- 支持Vue 2.6.x版本

## 详细配置说明

### 1. 数据库配置
```sql
# 创建数据库 (使用配置文件中的数据库名)
CREATE DATABASE Backend CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据库结构和数据
mysql -u root -p Backend < database/backend.sql
mysql -u root -p Backend < database/customer_package_record.sql
```

### 2. 后端配置文件修改
编辑 `springboot/src/main/resources/application.yml`：
```yaml
server:
  port: 9090  # 实际端口为9090，不是8080

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/Backend  # 数据库名为Backend
    username: root  # 修改为您的数据库用户名
    password: 123456  # 修改为您的数据库密码

# 文件上传路径 (需要根据实际部署路径修改)
file:
  upload:
    path: /你的项目路径/springboot/files/
    markdown:
      path: /你的项目路径/springboot/files/md/
```

### 3. 创建必要目录
```bash
# 创建文件上传目录 (这些目录不在Git版本控制中)
mkdir -p springboot/files
mkdir -p springboot/files/md  
mkdir -p springboot/files/admin
```

## 生产部署

### 前端构建
```bash
cd vue
npm run build
```

### 后端打包
```bash
cd springboot
mvn clean package
```

构建完成后，将打包文件部署到服务器即可。 