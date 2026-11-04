<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>登录 - 班级学生成绩管理系统</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body class="login-body">
<div class="login-box">
    <h1>班级学生成绩管理系统</h1>
    <form action="${pageContext.request.contextPath}/login" method="post">
        <select name="role">
            <option value="student">学生</option>
            <option value="teacher">教师</option>
        </select>
        <input type="text" name="username" placeholder="账号（学生=学号，教师=工号）" required>
        <input type="password" name="password" placeholder="密码" required>
        <button type="submit">登 录</button>
    </form>
    <% if (request.getAttribute("msg") != null) { %>
        <div class="msg"><%= request.getAttribute("msg") %></div>
    <% } %>
    <div class="tip">教师 t001 / 123456　|　学生 2021001 / 123456</div>
</div>
</body>
</html>
