# 班级学生成绩管理系统（Java Web / 原生 Servlet + JSP）

Eclipse Dynamic Web Project + Tomcat 9 + MySQL + 原生 JDBC，无 Spring。

## 功能

- 教师登录后：录入学生成绩（语文/数学/英语）、查看全班成绩、平均分与排名
- 学生登录后：查看本人成绩、平均分与名次

## 项目结构

```
GradeSystem
├── src/com/grade
│   ├── util/DBUtil.java           # 数据库工具（连接、查询、更新）
│   └── servlet/
│       ├── LoginServlet.java       # 登录 / 退出
│       ├── StudentServlet.java     # 学生查询
│       └── TeacherServlet.java     # 教师录入 / 查看排名
├── WebContent
│   ├── WEB-INF/web.xml            # Servlet 配置（无注解、无 loadOnStartup）
│   ├── WEB-INF/lib/               # 放 mysql 驱动 jar
│   ├── css/style.css
│   ├── login.jsp
│   ├── student.jsp
│   └── teacher.jsp
└── sql/schema.sql                 # 建库建表 + 示例数据
```

## 环境要求

- JDK 8+
- Eclipse（Java EE 版）+ Tomcat 9
- MySQL 5.7 / 8.0

---

## 完整部署步骤

### 第 1 步：初始化数据库

在 MySQL 里执行 `sql/schema.sql`，会创建数据库 `grade_db`、表 `teacher`、`student` 及示例数据：

```bash
mysql -uroot -p123456 < sql/schema.sql
```

或用 Navicat/SQLyog 打开 `schema.sql` 直接运行。

### 第 2 步：确认数据库连接配置

打开 `src/com/grade/util/DBUtil.java`，默认配置为：

- 数据库：`grade_db`
- 用户名：`root`
- 密码：`123456`

如你的 MySQL 用户名/密码不同，改 `USER` 和 `PASSWORD` 两个常量即可。

### 第 3 步：放入 MySQL 驱动 jar

把 `mysql-connector-j-8.0.33.jar` 放进：

```
GradeSystem/WebContent/WEB-INF/lib/
```

> 本项目用的是 JSP 脚本（不用 JSTL），所以**只需要这一个 jar**，不需要 jstl。

### 第 4 步：导入 Eclipse

1. `File → Import → General → Existing Projects into Workspace`，选择 `GradeSystem` 文件夹。
   - 若识别不了，就新建 **Dynamic Web Project**（名字随意，例如 GradeSystem），
     把 `src`、`WebContent` 两个目录里的内容原样拷进去。
2. `Window → Preferences → Server → Runtime Environments` 添加 Tomcat 9。

### 第 5 步：部署运行

1. 右键项目 → `Run As → Run on Server`，选择 Tomcat 9。
2. 若提示上下文路径，设为 `/GradeSystem`。

### 第 6 步：访问

浏览器打开：

```
http://localhost:8080/GradeSystem/login.jsp
```

### 测试账号

| 身份 | 账号 | 密码 |
| ---- | ---- | ---- |
| 教师 | t001 | 123456 |
| 学生 | 2021001（张三） | 123456 |

---

## 常见问题

- **中文乱码**：JSP 已设 UTF-8，Servlet 已 `setCharacterEncoding("UTF-8")`，数据库用 utf8mb4（脚本已设置）。
- **连接数据库报错**：确认 MySQL 已启动、`DBUtil` 的用户名密码正确、驱动 jar 已放入 lib。
- **时区错误**：URL 已带 `serverTimezone=Asia/Shanghai`。
- **找不到驱动**：若用 MySQL 5.x 的驱动 jar，把 `DBUtil` 里 `com.mysql.cj.jdbc.Driver` 换成 `com.mysql.jdbc.Driver`（代码已做双驱动兼容，一般无需改）。
