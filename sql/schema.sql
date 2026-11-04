-- ============================================================
-- 班级学生成绩管理系统 - 数据库脚本（MySQL）
-- 数据库：grade_db
-- 执行方式：命令行 mysql -uroot -p123456 < schema.sql
--          或 Navicat / SQLyog 中直接运行
-- ============================================================

DROP DATABASE IF EXISTS grade_db;
CREATE DATABASE grade_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE grade_db;

-- 教师表
CREATE TABLE teacher (
    id       INT         PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '工号(登录账号)',
    password VARCHAR(50) NOT NULL COMMENT '密码',
    realname VARCHAR(50) COMMENT '姓名'
) COMMENT = '教师表';

-- 学生表（语文/数学/英语三科成绩作为列）
CREATE TABLE student (
    id         INT         PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
    sno        VARCHAR(50) NOT NULL UNIQUE COMMENT '学号(登录账号)',
    name       VARCHAR(50) NOT NULL COMMENT '姓名',
    password   VARCHAR(50) NOT NULL COMMENT '密码',
    class_name VARCHAR(50) COMMENT '班级',
    chinese    DECIMAL(5,1) DEFAULT NULL COMMENT '语文',
    math       DECIMAL(5,1) DEFAULT NULL COMMENT '数学',
    english    DECIMAL(5,1) DEFAULT NULL COMMENT '英语'
) COMMENT = '学生表';

-- ============ 示例数据 ============

-- 教师：t001 / 123456
INSERT INTO teacher (username, password, realname) VALUES ('t001', '123456', '张老师');

-- 学生：学号 / 123456
INSERT INTO student (sno, name, password, class_name, chinese, math, english) VALUES
('2021001', '张三', '123456', '计算机2101', 88, 92, 85),
('2021002', '李四', '123456', '计算机2101', 75, 80, 78),
('2021003', '王五', '123456', '计算机2101', 90, 95, 91),
('2021004', '赵六', '123456', '计算机2102', 68, 72, 70),
('2021005', '孙七', '123456', '计算机2102', 82, 78, 86);
