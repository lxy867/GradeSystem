package com.grade.servlet;

import com.grade.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 教师控制器：录入成绩（POST）、查看成绩与排名（GET）
 */
public class TeacherServlet extends HttpServlet {

    private static final String[] SUBJECTS = {"chinese", "math", "english"};

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || !"teacher".equals(session.getAttribute("role"))) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        // 查询所有学生
        List<Map<String, Object>> students = DBUtil.query("SELECT * FROM student ORDER BY sno");
        for (Map<String, Object> s : students) {
            s.put("avg", avgOf(s));
        }
        // 按平均分从高到低排序
        students.sort((a, b) -> Double.compare(
                ((Number) b.get("avg")).doubleValue(),
                ((Number) a.get("avg")).doubleValue()));
        // 计算名次（同分并列）
        int pos = 0, rank = 0;
        double prev = -1;
        for (Map<String, Object> s : students) {
            pos++;
            double a = ((Number) s.get("avg")).doubleValue();
            if (Double.compare(a, prev) != 0) {
                rank = pos;
                prev = a;
            }
            s.put("rank", rank);
        }

        req.setAttribute("students", students);
        req.getRequestDispatcher("/teacher.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String sno = req.getParameter("sno");
        String subject = req.getParameter("subject");
        String scoreStr = req.getParameter("score");

        String msg;
        // 校验科目列名，防止 SQL 注入
        if (subject == null || !Arrays.asList(SUBJECTS).contains(subject)) {
            msg = "科目不合法";
        } else {
            try {
                double score = Double.parseDouble(scoreStr);
                if (score < 0 || score > 100) {
                    msg = "成绩需在 0~100 之间";
                } else {
                    int n = DBUtil.update(
                            "UPDATE student SET " + subject + " = ? WHERE sno = ?",
                            score, sno);
                    msg = n > 0 ? "录入成功" : "未找到该学生";
                }
            } catch (Exception e) {
                msg = "录入失败，请检查输入";
            }
        }
        resp.sendRedirect(req.getContextPath() + "/teacher?msg=" + encode(msg));
    }

    private String encode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
            return s;
        }
    }

    private double avgOf(Map<String, Object> row) {
        double sum = 0;
        int cnt = 0;
        for (String s : SUBJECTS) {
            Object v = row.get(s);
            if (v != null) {
                sum += ((Number) v).doubleValue();
                cnt++;
            }
        }
        if (cnt == 0) {
            return 0;
        }
        return Math.round(sum / cnt * 100) / 100.0;
    }
}
