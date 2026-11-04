<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>我的成绩 - 班级学生成绩管理系统</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container">
    <div class="topbar">
        <span class="title">班级学生成绩管理系统</span>
        <span class="user">学生：${sessionScope.realname}</span>
        <a class="btn" href="${pageContext.request.contextPath}/login?action=logout">退出登录</a>
    </div>

    <%
        Map<String, Object> me = (Map<String, Object>) request.getAttribute("me");
        double avg = (Double) request.getAttribute("avg");
        int rank = (Integer) request.getAttribute("rank");
        boolean hasScore = me.get("chinese") != null || me.get("math") != null || me.get("english") != null;
    %>

    <div class="stats">
        <div class="stat">
            <div class="label">我的平均分</div>
            <div class="value"><%= hasScore ? avg : "—" %></div>
        </div>
        <div class="stat">
            <div class="label">我的名次</div>
            <div class="value"><%= hasScore ? "第 " + rank + " 名" : "—" %></div>
        </div>
    </div>

    <div class="panel">
        <h2>我的成绩（<%= me.get("name") %> · <%= me.get("sno") %> · <%= me.get("class_name") %>）</h2>
        <table>
            <tr><th>科目</th><th>成绩</th></tr>
            <tr><td>语文</td><td><%= me.get("chinese") == null ? "未录入" : me.get("chinese") %></td></tr>
            <tr><td>数学</td><td><%= me.get("math") == null ? "未录入" : me.get("math") %></td></tr>
            <tr><td>英语</td><td><%= me.get("english") == null ? "未录入" : me.get("english") %></td></tr>
        </table>
    </div>
</div>
</body>
</html>
