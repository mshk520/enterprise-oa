# 企业办公系统（Enterprise OA）

基于 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 脚手架二次开发的前后端分离企业 OA 系统，覆盖会议室预约、车辆调度、消息通知等日常办公场景。

## 功能模块

| 模块 | 功能 |
| --- | --- |
| 会议室管理 | 会议室/服务项/设备台账、日历视图预约、固定预约、预约记录、订单报表 |
| 车辆管理 | 用车申请与审批、车辆档案、司机档案、值班排班、费用记录、进出记录 |
| 消息通知 | WebSocket 实时公告推送、SMTP 邮件通知（审批结果、会议提醒） |
| 系统管理 | 用户、角色、菜单、部门、岗位、字典、操作日志、代码生成、定时任务 |
| 多语言 | 简体中文 / 繁體中文 / English 三语切换（vue-i18n） |
| 界面 | 响应式布局（桌面 / 平板 / 手机）、动态主题换肤、ECharts 报表 |

## 技术栈

**后端**：Java 17 · Spring Boot · MyBatis · PageHelper · Druid 连接池 · Redis（会话 / 缓存）· JWT 认证 · WebSocket · Quartz · MySQL

**前端**：Vue 2 · Element UI · Vuex · Vue Router · vue-i18n · Axios · ECharts · Vue CLI (Webpack)

**工程**：Maven 多模块、前后端分离、Nginx 静态资源 + 反向代理

## 目录结构

```
├── Mingxing-admin        # 启动模块（Controller 层、配置文件）
├── Mingxing-framework    # 核心框架（安全、数据源、WebSocket、拦截器）
├── Mingxing-system       # 系统模块（业务 Service / Mapper / Domain）
├── Mingxing-common       # 通用工具、枚举、异常
├── Mingxing-generator    # 代码生成
├── Mingxing-quartz       # 定时任务
├── Mingxing-ui           # 前端工程（Vue 2 + Element UI）
└── sql/                  # 数据库初始化脚本
```

## 快速开始

**环境**：JDK 17+、Maven 3.8+、Node 16+、MySQL 8、Redis

1. 创建数据库并导入脚本：

```bash
mysql -uroot -p -e "create database web_db default charset utf8mb4"
mysql -uroot -p web_db < sql/ry_20260417.sql
mysql -uroot -p web_db < sql/quartz.sql
```

2. 修改数据源与 Redis 配置：`Mingxing-admin/src/main/resources/application-druid.yml`、`application.yml`

3. 启动后端：

```bash
mvn clean install
mvn spring-boot:run -pl Mingxing-admin
```

4. 启动前端：

```bash
cd Mingxing-ui
npm install --registry=https://registry.npmmirror.com
npm run dev
```

5. 访问 `http://localhost`，默认账号 `admin / admin123`

## 主要工作

- 会议室预约、车辆管理等业务模块的前后端开发与联调
- 基于 WebSocket 的实时公告推送，以及 SMTP 审批结果邮件通知
- 中英双语国际化改造，登录页 / 首页 / 菜单的交互与样式优化
- 修复首页与弹窗相关 Bug，优化侧边菜单栏布局

## License

[MIT](LICENSE)（基于 RuoYi-Vue）
