# YoOJ - 在线做题平台

![Java](https://img.shields.io/badge/Java-17-blue.svg)
![SpringBoot](https://img.shields.io/badge/Spring_Boot-3.2.3-brightgreen.svg)
![SpringCloudAlibaba](https://img.shields.io/badge/Spring_Cloud_Alibaba-3.1.5-brightgreen.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0.26-blue.svg)
![Redis](https://img.shields.io/badge/Redis-7.2.4-red.svg)
![Docker](https://img.shields.io/badge/Docker-20.10.12-blue.svg)

[![](https://img.shields.io/badge/blog-@YounGCY-red.svg)](https://blog.yngcy.com)
![GitHub License](https://img.shields.io/github/license/yngcy/YoOJ-backend)
![GitHub Repo stars](https://img.shields.io/github/stars/yngcy/YoOJ-backend)
![Email](https://img.shields.io/badge/Email-2792783811@qq.com-yellow.svg)

## 什么是 YoOJ

YoOJ（Yocy Online Judge，阳曦 OJ）是一个基于 Spring Cloud 与 React、分布式架构的在线做题平台，具有配置简单、易于扩展、功能丰富等特点.

### 配置简单

开发者可以使用简单的配置文件即可快速搭建在线做题平台，无需任何代码编写.

### 易于扩展

YoOJ 采用微服务架构，通过 Spring Cloud 与 Spring Cloud Alibaba 构建分布式系统，实现扩展性强.

### 功能丰富

支持主流 OJ 功能，并且……

## 特性

### 功能特性

> 孵化中……

### 性能特性

> 孵化中……

更多请参考：[YoOJ 特性]()

## 源码地址

|    | GitHub                                                       | Gitee |
|----|--------------------------------------------------------------|-------|
| 后端 | [YoOJ-backend（开发中）](https://github.com/yngcy/YoOJ-backend)   |       | 
| 前端 | [YoOJ-frontend（设计中）](https://github.com/yngcy/YoOJ-frontend) |       |

## 目录结构

```bash
YoOJ-backend
├─docs
│  ├─asset                  # 文档附件
│  └─devel                  # 贡献相关文档
├─oj-ums                    # 会员服务
├─YoOJ-admin                # 后台管理服务
│  ├─admin-api              # 后台管理Feign接口
│  └─admin-boot             # 后台管理接口
├─YoOJ-auth                 # 统一认证授权服务
├─YoOJ-common               # 公共模块
└─YoOJ-gateway              # 网关模块
```

## 快速上手

### 环境要求

- JDK 17
- Windows 11（其他平台未测试）

### 中间件版本

|       | Windows   | Linux     |
|-------|-----------|-----------|
| Nacos | Nacos 2.2 | Nacos 2.3 |
| Redis | Redis 7.2 | Redis 7.2 |
| MySQL | MySQL 8.0 | MySQL 8.0 |

### 初始化数据库

进入 `sql/mysql-init` 目录，先执行 [database.sql](./sql/mysql-init/database.sql) 完成数据库创建；

再执行 [YoOJ-system.sql](./sql/mysql-init/YoOJ-system.sql) 、 [auth_db](./sql/mysql-init/auth_db.sql) 以及 oj_*.sql
完成数据库表创建以及相关数据的初始化。

### Nacos 配置

#### 导入

在启动 Nacos 服务后，打开浏览器，进入 Nacos 控制台页面。

依次点击 `配置管理` - `配置列表`，点击 `导入配置`，将 `nacos` 目录下的 `nacos_cofig.zip` 导入。

#### 修改

在共享配置文件 `yocy-common.yaml` 中，包括 MySQL、Redis、RabbitMQ 和 Seata 的连接信息。可以根据实际情况修改配置。

### 启动服务

- 进入 `yocy-gateway` 模块的启动类 GatewayApplication 启动网关；
- 进入 `yocy-auth` 模块的启动类 AuthApplication 启动认证服务；
- 进入 `yocy-admin` → `admin-boot` 模块的启动类 SystemApplication 启动系统服务；
- 至此，所有基础服务启动完成，即可进入登录页面进行登录. 其他服务按需启动，启动方式与启动系统服务一致.
- 访问接口文档地址测试：http://localhost:8101/doc.html

## 技术选型

### 前端

- React：使用 React 开发框架进行开发.
- Open API：提供 Open API 代码生成工具，方便生成 API 文档和客户端 SDK.
- Ant Design Pro：使用 Ant Design Pro 构建前端页面，提供开箱即用的前端开发框架.
- 前端工程化：采用 EsLint、Prettier 和 Typescript 进行前端项目工程化管理，提升代码质量和可维护性.

### 后端

- Java Spring Boot：使用 Java Spring Boot 构建后端应用程序，提供稳定可靠的 API 接口.
- MySQL 和 MyBatis Plus：使用 MySQL 作为数据库存储，结合 MyBatis Plus 简化数据库操作和对象映射.
- Caffeine 和 Redis：使用 Caffeine 和 Redis 提供多级缓存机制，提高系统性能和响应速度.

### 其他

- bytemd: 轻量级的 Markdown 编辑器，支持 Markdown、LaTeX、GitHub Flavored Markdown、CodeMirror、Katex、Mermaid 等特性.
- monaco-editor: 一款功能强大的代码编辑器.

## 软件架构

> 孵化中……

架构解析：[YoOJ 架构]()

## 使用指南

[YoOJ Documentation]()

## 特别鸣谢

- 所有为本项目做出贡献的伙伴
- 所有使用本项目作为 OJ 平台的组织&个人
- 所有提供意见和建议的同学

如果您觉得本项目不错，就 star 吧🤩~

## 社区支持

最后，目前研发团队中只有 YounGCY，显然一个人的力量是薄弱的，期待同学加入~

> 联系邮箱：2792783811@qq.com
>
> YounGCY 的频道：[点击链接加入QQ频道【YounGCY的频道】](https://pd.qq.com/s/2x04429nz)


当然你也可以提交特性需求的 issue。在写特性功能之前，请先阅读 [issue 指南](./docs/devel/issue.md).

也欢迎 PR，代码贡献流程可以参考 [代码贡献指南](./docs/devel/development.md).

## 许可

[MIT Licence](LICENSE). 
