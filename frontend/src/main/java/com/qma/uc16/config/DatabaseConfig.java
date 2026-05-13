package com.qma.uc16.config;

import java.sql.*;

/**
 * Database configuration — MySQL.
 * Concepts: JDBC, Connection Management, Schema Design.
 */
public class DatabaseConfig {

    private static final String JDBC_URL =
        "jdbc:mysql://localhost:3306/qma_uc16" +
        "?createDatabaseIfNotExist=true" +
        "&useSSL=false" +
        "&serverTimezone=UTC" +
        "&allowPublicKeyRetrieval=true";
    private static final String JDBC_USER     = "root";
    private static final String JDBC_PASSWORD = "Kiran@12";

    static {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) { throw new RuntimeException("MySQL Driver not found", e); }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
    }

    public static void initializeSchema() {
        String createTable =
            "CREATE TABLE IF NOT EXISTS measurements (" +
            "  id       BIGINT AUTO_INCREMENT PRIMARY KEY," +
            "  value    DOUBLE       NOT NULL," +
            "  unit     VARCHAR(20)  NOT NULL," +
            "  category VARCHAR(20)  NOT NULL" +
            ")";
        try (Connection conn = getConnection();
             Statement stmt  = conn.createStatement()) {
            stmt.execute(createTable);
            System.out.println("  [DB] Schema initialized — table 'measurements' ready (MySQL qma_uc16).");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize schema: " + e.getMessage(), e);
        }
    }
}
