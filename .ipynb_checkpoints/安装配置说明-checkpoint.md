# Code2025 系统安装配置说明

## 一、环境准备

### 1.1 基础环境要求

| 环境项 | 最低要求 | 推荐配置 | 说明 |
| --- | --- | --- | --- |
| 操作系统 | Windows 10/macOS/Linux | Windows 11/Ubuntu 22.04/macOS | 支持各主流操作系统 |
| CPU | 双核 2.0GHz | 四核 2.5GHz 以上 | - |
| 内存 | 4GB | 8GB 以上 | 开发环境建议8GB以上 |
| 存储空间 | 20GB | 50GB | 包含代码、依赖和数据 |
| 网络 | 宽带连接 | 10Mbps 以上 | 用于下载依赖和访问服务 |

### 1.2 开发环境安装

#### 1.2.1 JDK 安装

1. 下载 JDK 17 或更高版本：
   - 官方下载地址：https://www.oracle.com/java/technologies/downloads/
   - 或使用 OpenJDK：https://adoptium.net/

2. 安装 JDK 并配置环境变量：
   ```bash
   # Windows 环境变量设置 (在系统环境变量中添加)
   JAVA_HOME = C:\Program Files\Java\jdk-17
   PATH = %PATH%;%JAVA_HOME%\bin
   
   # Linux/macOS 环境变量设置
   export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
   export PATH=$PATH:$JAVA_HOME/bin
   ```

3. 验证安装：
   ```bash
   java -version
   ```

#### 1.2.2 MySQL 安装

1. 下载 MySQL 8.0 或更高版本：
   - 官方下载地址：https://dev.mysql.com/downloads/mysql/

2. 安装步骤：
   - Windows：运行安装程序，按提示完成安装
   - Linux：`sudo apt install mysql-server` (Ubuntu) 或 `sudo yum install mysql-server` (CentOS)
   - macOS：`brew install mysql`

3. 初始化配置：
   - 设置 root 用户密码为 `123456`（与应用配置一致）
   - 创建数据库：
     ```sql
     CREATE DATABASE code2025 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
     ```

4. 验证安装：
   ```bash
   mysql -u root -p -e "SHOW DATABASES;"
   ```

#### 1.2.3 Node.js 安装

1. 下载 Node.js 16 或更高版本：
   - 官方下载地址：https://nodejs.org/

2. 安装步骤：
   - Windows/macOS：运行安装程序，按提示完成安装
   - Linux：使用包管理器或 NVM 安装

3. 验证安装：
   ```bash
   node -v
   npm -v
   ```

### 1.3 IDE 推荐

- 后端开发：IntelliJ IDEA
- 前端开发：Visual Studio Code
- 数据库管理：MySQL Workbench 或 Navicat

## 二、项目部署

### 2.1 数据库配置

1. 导入数据库结构：
   ```bash
   # 方法一：命令行导入
   mysql -u root -p code2025 < full_schema.sql
   
   # 方法二：使用图形化工具导入
   # 在 MySQL Workbench 或 Navicat 中打开 full_schema.sql 并执行
   ```

2. 检查数据库表是否创建成功：
   ```sql
   USE code2025;
   SHOW TABLES;
   ```

### 2.2 后端配置与启动

1. 获取后端代码：
   ```bash
   git clone [项目仓库地址]
   cd code2025/springboot
   ```

2. 修改配置文件 `src/main/resources/application.properties`：
   ```properties
   # 数据库连接配置
   spring.datasource.url=jdbc:mysql://localhost:3306/code2025
   spring.datasource.username=root
   spring.datasource.password=123456
   
   # 服务器端口配置
   server.port=9999
   ```

3. 构建后端项目：
   ```bash
   # 使用 Maven 构建
   mvn clean package
   ```

4. 启动后端服务：
   ```bash
   # 方法一：直接使用 Java 运行 jar 包
   java -jar target/springboot-0.0.1-SNAPSHOT.jar
   
   # 方法二：使用 Maven 启动
   mvn spring-boot:run
   ```

5. 验证后端服务启动：
   - 访问 http://localhost:9999
   - 应看到 Spring Boot 欢迎页面或 API 文档

### 2.3 前端配置与启动

1. 进入前端项目目录：
   ```bash
   cd code2025/vue
   ```

2. 安装依赖：
   ```bash
   npm install
   ```

3. 修改 API 配置（如需要）：
   - 编辑 `.env` 或 `.env.development` 文件
   - 确保 API 地址指向后端服务

4. 启动开发服务器：
   ```bash
   npm run serve  # 或 npm run dev
   ```

5. 构建生产版本：
   ```bash
   npm run build
   ```

6. 部署生产版本：
   - 将 `dist` 目录下的文件复制到 Web 服务器根目录
   - 配置 Nginx 或 Apache 服务器（见下方配置示例）

### 2.4 Web 服务器配置

#### Nginx 配置示例

```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    # 前端文件
    location / {
        root /path/to/vue/dist;
        try_files $uri $uri/ /index.html;
        index index.html;
    }
    
    # API 代理
    location /api {
        proxy_pass http://localhost:9999;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

#### Apache 配置示例

```apache
<VirtualHost *:80>
    ServerName your-domain.com
    DocumentRoot /path/to/vue/dist
    
    <Directory "/path/to/vue/dist">
        Options Indexes FollowSymLinks
        AllowOverride All
        Require all granted
    </Directory>
    
    ProxyPass /api http://localhost:9999/api
    ProxyPassReverse /api http://localhost:9999/api
</VirtualHost>
```

## 三、系统配置与优化

### 3.1 性能优化建议

1. 数据库优化：
   - 添加适当的索引
   - 优化查询语句
   - 适当增加连接池大小

2. JVM 调优：
   ```bash
   java -Xms512m -Xmx1024m -jar springboot-0.0.1-SNAPSHOT.jar
   ```

3. 静态资源优化：
   - 启用 Gzip 压缩
   - 配置浏览器缓存
   - 使用 CDN 分发静态资源

### 3.2 安全配置

1. 数据库安全：
   - 更改默认密码
   - 限制数据库远程访问
   - 定期备份数据

2. Web 安全：
   - 配置 HTTPS
   - 添加 CSRF 保护
   - 设置合理的 CORS 策略

3. 系统安全：
   - 定期更新系统和依赖包
   - 配置防火墙规则
   - 监控系统日志

## 四、故障排除

### 4.1 常见问题

| 问题 | 可能原因 | 解决方案 |
| --- | --- | --- |
| 数据库连接失败 | 数据库未启动或连接信息错误 | 检查数据库服务状态和连接配置 |
| 后端服务启动失败 | 端口被占用或权限不足 | 更改端口号或使用管理员权限启动 |
| 前端编译错误 | 依赖包冲突或版本不兼容 | 清除 node_modules 并重新安装依赖 |
| 接口调用失败 | 跨域问题或API地址配置错误 | 检查跨域配置和API基础URL |

### 4.2 日志查看

1. 后端日志：
   - 开发环境：控制台输出
   - 生产环境：`logs/spring.log`

2. 前端日志：
   - 浏览器控制台
   - Vue Devtools

### 4.3 联系支持

如有其他技术问题，请联系：

- 技术支持邮箱：support@code2025.com
- 项目 GitHub 仓库：https://github.com/code2025/code2025

## 五、维护与更新

### 5.1 数据库备份

```bash
# 创建数据库备份
mysqldump -u root -p code2025 > code2025_backup_$(date +%Y%m%d).sql

# 恢复数据库备份
mysql -u root -p code2025 < code2025_backup_20230101.sql
```

### 5.2 系统更新

1. 后端更新：
   ```bash
   git pull
   mvn clean package
   # 停止旧服务
   # 启动新服务
   ```

2. 前端更新：
   ```bash
   git pull
   npm install
   npm run build
   # 部署新的 dist 目录
   ```

### 5.3 版本管理

遵循语义化版本规范：
- 主版本号：不兼容的 API 修改
- 次版本号：向下兼容的功能性新增
- 修订号：向下兼容的问题修正 
