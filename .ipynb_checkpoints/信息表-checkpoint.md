# Code2025系统信息表

## 基本信息

| 项目 | 内容 |
| --- | --- |
| 系统名称 | Code2025 |
| 版本号 | 1.0.0 |
| 开发团队 | Code2025团队 |
| 目标用户 | 程序员群体 |
| 主要功能 | 心理测评、资源分享、AI辅助咨询 |

## 技术栈信息

### 前端

| 技术/框架 | 版本 | 用途 |
| --- | --- | --- |
| Vue.js | 3.x | 前端框架 |
| Element Plus | 最新版 | UI组件库 |
| Vue Router | 最新版 | 前端路由 |
| Axios | 最新版 | HTTP请求 |

### 后端

| 技术/框架 | 版本 | 用途 |
| --- | --- | --- |
| Spring Boot | 2.7.x | 后端框架 |
| MyBatis | 3.0.3 | ORM框架 |
| MySQL | 8.0+ | 数据库 |
| JWT | 4.3.0 | 身份验证 |
| Lombok | 1.18.30 | 简化开发 |
| Hutool | 5.8.25 | 工具类库 |

## 数据库信息

| 数据库名称 | 表数量 | 主要表 |
| --- | --- | --- |
| code2025 | 8 | user, admin, category, notice, introduction, test_result, community_post, post_comment |

## 功能模块信息

| 模块名称 | 核心功能 | 相关接口 |
| --- | --- | --- |
| 用户管理 | 用户注册、登录、信息管理 | /api/user/* |
| 社区交流 | 发帖、评论、点赞、匿名发布 | /api/community/* |
| 心理测评 | 多种类型测评、结果分析 | /api/test/* |
| AI辅助咨询 | 智能对话、个性化建议 | /api/ai/* |
| 资源分享 | 学习资源、职业规划工具 | /api/resource/* |

## 部署信息

| 环境 | 要求 |
| --- | --- |
| JDK | 17+ |
| MySQL | 8.0+ |
| Node.js | 16+ |
| 内存 | 至少4GB |
| 存储 | 至少20GB |

## API接口统计

| 模块 | 接口数量 | 示例接口 |
| --- | --- | --- |
| 用户管理 | 10+ | /api/user/login, /api/user/register |
| 社区交流 | 15+ | /api/community/posts, /api/community/comments |
| 心理测评 | 8+ | /api/test/list, /api/test/submit |
| AI辅助咨询 | 5+ | /api/ai/chat, /api/ai/analysis |
| 资源分享 | 6+ | /api/resource/list, /api/resource/detail |

## 文件结构信息

| 目录 | 用途 | 主要文件类型 |
| --- | --- | --- |
| springboot/src/main/java | 后端Java源码 | .java |
| springboot/src/main/resources | 配置文件和资源 | .properties, .xml, .sql |
| vue/src | 前端Vue源码 | .vue, .js |
| vue/public | 静态资源 | .html, .css, .js |

## 安全信息

| 安全机制 | 实现方式 |
| --- | --- |
| 身份验证 | JWT令牌 |
| 密码存储 | 加密算法 |
| 输入验证 | 前后端双重验证 |
| 敏感数据保护 | 数据脱敏 | 