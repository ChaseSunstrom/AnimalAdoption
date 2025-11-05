package com.animaladoption.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database utility class for managing database connections.
 * Uses direct JDBC connections for simplicity.
 */
public class DatabaseUtil {

    // Direct JDBC connection parameters
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/animal_adoption?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String JDBC_USER = "root"; // Change this to your MySQL username
    private static final String JDBC_PASSWORD = "1234"; // Change this to your MySQL password

    // Static block to load MySQL driver
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: MySQL JDBC Driver not found!");
            System.err.println("Make sure mysql-connector-java JAR is in your classpath");
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    /**
     * Get a database connection using direct JDBC.
     *
     * @return Database connection
     * @throws SQLException if connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        try {
            System.out.println("Attempting to connect to database: " + JDBC_URL);
            Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
            System.out.println("Database connection established successfully");
            return conn;
        } catch (SQLException e) {
            System.err.println("ERROR: Failed to connect to database!");
            System.err.println("URL: " + JDBC_URL);
            System.err.println("User: " + JDBC_USER);
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Close a database connection safely.
     *
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Test database connectivity.
     *
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        } finally {
            closeConnection(conn);
        }
    }
}
