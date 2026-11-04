package com.grade.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库工具类（JDBC 连接 MySQL）
 */
public class DBUtil {

    // ============ 数据库连接信息（按本机实际修改）============
    private static final String URL =
            "jdbc:mysql://localhost:3306/grade_db"
            + "?useUnicode=true&characterEncoding=utf8"
            + "&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";   // 默认密码 123456
    // =======================================================

    /** 获取数据库连接（在方法内加载驱动，不使用静态代码块） */
    public static Connection getConnection() {
        try {
            // MySQL 8.x 驱动
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            try {
                // MySQL 5.x 驱动
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /** 通用查询：把结果集转成 List&lt;Map&lt;列名, 值&gt;&gt; */
    public static List<Map<String, Object>> query(String sql, Object... params) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Connection conn = getConnection();
        if (conn == null) {
            return list;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> row = new LinkedHashMap<String, Object>();
                    for (int i = 1; i <= cols; i++) {
                        row.put(md.getColumnLabel(i), rs.getObject(i));
                    }
                    list.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, null, null);
        }
        return list;
    }

    /** 通用更新（增/删/改），返回受影响行数 */
    public static int update(String sql, Object... params) {
        Connection conn = getConnection();
        if (conn == null) {
            return 0;
        }
        int n = 0;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            n = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, null, null);
        }
        return n;
    }

    /** 释放资源 */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
