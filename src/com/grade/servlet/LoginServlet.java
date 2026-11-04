package com.grade.servlet;

import com.grade.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 登录 / 退出控制器
 */
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 退出登录
        if ("logout".equals(req.getParameter("action"))) {
            HttpSession session = req.getSession(false);
            if (session != null) {
                session.invalidate();
            }
        }
        resp.sendRedirect(req.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String role = req.getParameter("role");
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || username.trim().isEmpty()
                || password == null || password.isEmpty()) {
            req.setAttribute("msg", "请输入账号和密码");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession();

        if ("teacher".equals(role)) {
            List<Map<String, Object>> rows = DBUtil.query(
                    "SELECT * FROM teacher WHERE username = ? AND password = ?",
                    username.trim(), password);
            if (!rows.isEmpty()) {
                Map<String, Object> t = rows.get(0);
                session.setAttribute("role", "teacher");
                session.setAttribute("username", t.get("username"));
                session.setAttribute("realname", t.get("realname"));
                resp.sendRedirect(req.getContextPath() + "/teacher");
                return;
            }
        } else if ("student".equals(role)) {
            List<Map<String, Object>> rows = DBUtil.query(
                    "SELECT * FROM student WHERE sno = ? AND password = ?",
                    username.trim(), password);
            if (!rows.isEmpty()) {
                Map<String, Object> stu = rows.get(0);
                session.setAttribute("role", "student");
                session.setAttribute("sno", stu.get("sno"));
                session.setAttribute("realname", stu.get("name"));
                resp.sendRedirect(req.getContextPath() + "/student");
                return;
            }
        }

        req.setAttribute("msg", "账号、密码或身份错误");
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}
