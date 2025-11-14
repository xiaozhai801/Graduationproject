package com.zpq.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

    static final String DB_URL = "jdbc:mysql://localhost:3306/repairdb?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai";

    static final String USER = "root";
    static final String PASS = "123456789";

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load JDBC driver", e);
        }
    }

    private Connection conn;
    private Statement stat;
    private PreparedStatement pstmt;

    // 打开连接并复用现有连接
    private void openConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
                // 默认自动提交为true
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to open database connection", e);
        }
    }

    // 添加 getConnection 方法
    public Connection getConnection() {
        openConnection();
        return conn;
    }

    // 开始事务（禁用自动提交）
    public void beginTransaction() {
        openConnection();
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to begin transaction", e);
        }
    }

    // 提交事务
    public void commit() {
        try {
            if (conn != null && !conn.isClosed() && !conn.getAutoCommit()) {
                conn.commit();
                // 提交后恢复自动提交
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to commit transaction", e);
        }
    }

    // 回滚事务
    public void rollback() {
        try {
            if (conn != null && !conn.isClosed() && !conn.getAutoCommit()) {
                conn.rollback();
                // 回滚后恢复自动提交
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to rollback transaction", e);
        }
    }

    // 获取Statement对象（关闭之前的Statement）
    public Statement getStatement() {
        openConnection();
        try {
            closeStatement(); // 关闭之前的Statement
            stat = conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create Statement", e);
        }
        return stat;
    }

    // 获取PreparedStatement对象（关闭之前的PreparedStatement）
    public PreparedStatement getPreparedStatement(String sql) {
        openConnection();
        try {
            closePreparedStatement(); // 关闭之前的PreparedStatement
            pstmt = conn.prepareStatement(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create PreparedStatement", e);
        }
        return pstmt;
    }

    // 关闭所有数据库资源
    public void closeResource() {
        closePreparedStatement();
        closeStatement();
        closeConnection();
    }

    // 私有方法用于关闭各个资源
    private void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            conn = null;
        }
    }

    private void closeStatement() {
        try {
            if (stat != null && !stat.isClosed()) {
                stat.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            stat = null;
        }
    }

    private void closePreparedStatement() {
        try {
            if (pstmt != null && !pstmt.isClosed()) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pstmt = null;
        }
    }
}