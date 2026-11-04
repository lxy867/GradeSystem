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
 * 学生查询成绩控制器（本人成绩、平均分、名次）
 */
public class StudentServlet extends HttpServlet {

    private static final String[] SUBJECTS = {"chinese", "math", "english"};

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || !"student".equals(session.getAttribute("role"))) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        String sno = (String) session.getAttribute("sno");

        List<Map<String, Object>> rows = DBUtil.query("SELECT * FROM student WHERE sno = ?", sno);
        if (rows.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        Map<String, Object> me = rows.get(0);

        double avg = avgOf(me);

        // 名次 = 平均分比我高的学生人数 + 1
        List<Map<String, Object>> all = DBUtil.query("SELECT * FROM student");
        int better = 0;
        for (Map<String, Object> row : all) {
            if (avgOf(row) > avg) {
                better++;
            }
        }

        req.setAttribute("me", me);
        req.setAttribute("avg", avg);
        req.setAttribute("rank", better + 1);
        req.getRequestDispatcher("/student.jsp").forward(req, resp);
    }

    /** 计算某学生的平均分（未录入的科目不参与平均） */
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
