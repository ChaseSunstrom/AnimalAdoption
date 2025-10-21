package com.animaladoption.dao;

import com.animaladoption.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Base DAO class with common database operations.
 */
public abstract class BaseDAO {

    /**
     * Get a database connection.
     *
     * @return Database connection
     * @throws SQLException if connection fails
     */
    protected Connection getConnection() throws SQLException {
        return DatabaseUtil.getConnection();
    }

    /**
     * Close database resources safely.
     *
     * @param conn Connection to close
     * @param stmt Statement to close
     * @param rs   ResultSet to close
     */
    protected void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
        if (conn != null) {
            DatabaseUtil.closeConnection(conn);
        }
    }

    /**
     * Close connection and statement.
     */
    protected void closeResources(Connection conn, PreparedStatement stmt) {
        closeResources(conn, stmt, null);
    }

    /**
     * Execute an update query and return affected rows.
     *
     * @param sql    SQL query
     * @param params Parameters for the query
     * @return Number of affected rows
     * @throws SQLException if query fails
     */
    protected int executeUpdate(String sql, Object... params) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(sql);

            // Set parameters
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            return stmt.executeUpdate();
        } finally {
            closeResources(conn, stmt);
        }
    }
}
