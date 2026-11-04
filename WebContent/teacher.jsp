<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, java.util.Map" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>教师 - 成绩管理</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container">
    <div class="topbar">
        <span class="title">班级学生成绩管理系统</span>
        <span class="user">教师：${sessionScope.realname}</span>
        <a class="btn" href="${pageContext.request.contextPath}/login?action=logout">退出登录</a>
    </div>

    <%
        List<Map<String, Object>> students =
                (List<Map<String, Object>>) request.getAttribute("students");
    %>

    <div class="panel">
        <h2>录入成绩</h2>
        <form action="${pageContext.request.contextPath}/teacher" method="post">
            <select name="sno" required>
                <option value="">-- 选择学生 --</option>
                <%
                    if (students != null) {
                        for (Map<String, Object> s : students) {
                %>
                    <option value="<%= s.get("sno") %>"><%= s.get("name") %>（<%= s.get("sno") %>）</option>
                <%
                        }
                    }
                %>
            </select>
            <select name="subject">
                <option value="chinese">语文</option>
                <option value="math">数学</option>
                <option value="english">英语</option>
            </select>
            <input type="number" name="score" step="0.1" min="0" max="100" placeholder="成绩 0~100" required>
            <button type="submit" class="btn">保存成绩</button>
        </form>
        <% if (request.getParameter("msg") != null) { %>
            <div class="msg"><%= request.getParameter("msg") %></div>
        <% } %>
    </div>

    <div class="panel">
        <h2>成绩与排名</h2>
        <table>
            <tr><th>名次</th><th>学号</th><th>姓名</th><th>班级</th><th>语文</th><th>数学</th><th>英语</th><th>平均分</th></tr>
            <%
                if (students != null) {
                    for (Map<String, Object> s : students) {
                        boolean hasScore = s.get("chinese") != null
                                || s.get("math") != null
                                || s.get("english") != null;
            %>
            <tr>
                <td><%= hasScore ? s.get("rank") : "—" %></td>
                <td><%= s.get("sno") %></td>
                <td><%= s.get("name") %></td>
                <td><%= s.get("class_name") %></td>
                <td><%= s.get("chinese") == null ? "未录入" : s.get("chinese") %></td>
                <td><%= s.get("math") == null ? "未录入" : s.get("math") %></td>
                <td><%= s.get("english") == null ? "未录入" : s.get("english") %></td>
                <td><%= hasScore ? s.get("avg") : "—" %></td>
            </tr>
            <%
                    }
                }
            %>
        </table>
    </div>
</div>
</body>
</html>
