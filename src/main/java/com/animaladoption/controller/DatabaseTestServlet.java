package com.animaladoption.controller;

import com.animaladoption.util.DatabaseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Diagnostic servlet to test database connectivity.
 * Access at: http://localhost:8080/AnimalAdoption/db-test
 */
@WebServlet(name = "DatabaseTestServlet", urlPatterns = {"/db-test"})
public class DatabaseTestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Database Connection Test</title>");
        out.println("<style>");
        out.println("body { font-family: Arial, sans-serif; margin: 40px; }");
        out.println(".success { color: green; font-weight: bold; }");
        out.println(".error { color: red; font-weight: bold; }");
        out.println(".info { background: #f0f0f0; padding: 10px; margin: 10px 0; border-radius: 5px; }");
        out.println("table { border-collapse: collapse; margin-top: 20px; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #4CAF50; color: white; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Animal Adoption - Database Connection Test</h1>");

        Connection conn = null;
        try {
            // Test 1: Load JDBC Driver
            out.println("<h2>Test 1: JDBC Driver</h2>");
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                out.println("<p class='success'>✓ MySQL JDBC Driver loaded successfully</p>");
            } catch (ClassNotFoundException e) {
                out.println("<p class='error'>✗ MySQL JDBC Driver NOT FOUND</p>");
                out.println("<p class='error'>Error: " + e.getMessage() + "</p>");
                out.println("<div class='info'>");
                out.println("<strong>Fix:</strong> Add mysql-connector-java-8.x.x.jar to your project's lib folder or WEB-INF/lib");
                out.println("</div>");
                return;
            }

            // Test 2: Database Connection
            out.println("<h2>Test 2: Database Connection</h2>");
            try {
                conn = DatabaseUtil.getConnection();
                out.println("<p class='success'>✓ Database connection successful!</p>");
            } catch (Exception e) {
                out.println("<p class='error'>✗ Failed to connect to database</p>");
                out.println("<p class='error'>Error: " + e.getMessage() + "</p>");
                out.println("<div class='info'>");
                out.println("<strong>Common Issues:</strong><br>");
                out.println("1. MySQL server is not running<br>");
                out.println("2. Database 'animal_adoption' does not exist<br>");
                out.println("3. Wrong username/password in DatabaseUtil.java<br>");
                out.println("4. MySQL not listening on localhost:3306<br><br>");
                out.println("<strong>Current Configuration:</strong><br>");
                out.println("Database: animal_adoption<br>");
                out.println("Check DatabaseUtil.java for username/password settings");
                out.println("</div>");
                e.printStackTrace(out);
                return;
            }

            // Test 3: Database Metadata
            out.println("<h2>Test 3: Database Information</h2>");
            DatabaseMetaData metaData = conn.getMetaData();
            out.println("<div class='info'>");
            out.println("<strong>Database Product:</strong> " + metaData.getDatabaseProductName() + "<br>");
            out.println("<strong>Database Version:</strong> " + metaData.getDatabaseProductVersion() + "<br>");
            out.println("<strong>JDBC Driver:</strong> " + metaData.getDriverName() + "<br>");
            out.println("<strong>Driver Version:</strong> " + metaData.getDriverVersion() + "<br>");
            out.println("<strong>Connection URL:</strong> " + metaData.getURL() + "<br>");
            out.println("<strong>Username:</strong> " + metaData.getUserName() + "<br>");
            out.println("</div>");

            // Test 4: List Tables
            out.println("<h2>Test 4: Database Tables</h2>");
            ResultSet tables = metaData.getTables("animal_adoption", null, "%", new String[]{"TABLE"});
            out.println("<table>");
            out.println("<tr><th>Table Name</th><th>Status</th></tr>");

            String[] requiredTables = {
                "users", "adopter_profiles", "shelters", "animals",
                "adoption_applications", "match_scores", "saved_animals", "animal_images"
            };

            boolean allTablesExist = true;
            java.util.Set<String> foundTables = new java.util.HashSet<>();

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                foundTables.add(tableName.toLowerCase());
            }
            tables.close();

            for (String tableName : requiredTables) {
                if (foundTables.contains(tableName.toLowerCase())) {
                    out.println("<tr><td>" + tableName + "</td><td class='success'>✓ Exists</td></tr>");
                } else {
                    out.println("<tr><td>" + tableName + "</td><td class='error'>✗ Missing</td></tr>");
                    allTablesExist = false;
                }
            }
            out.println("</table>");

            if (!allTablesExist) {
                out.println("<div class='info'>");
                out.println("<strong>Missing Tables Detected!</strong><br>");
                out.println("Run the schema.sql script to create all required tables:<br>");
                out.println("<code>mysql -u root -p animal_adoption &lt; database/schema.sql</code>");
                out.println("</div>");
            }

            // Test 5: Sample Query
            out.println("<h2>Test 5: Sample Query</h2>");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
            if (rs.next()) {
                int userCount = rs.getInt("count");
                out.println("<p class='success'>✓ Query successful!</p>");
                out.println("<div class='info'>");
                out.println("<strong>Users in database:</strong> " + userCount);
                if (userCount == 0) {
                    out.println("<br><br><strong>Note:</strong> No users found. You may want to load sample data:<br>");
                    out.println("<code>mysql -u root -p animal_adoption &lt; database/sample_data.sql</code>");
                }
                out.println("</div>");
            }
            rs.close();
            stmt.close();

            // Summary
            out.println("<h2>Summary</h2>");
            out.println("<div class='info'>");
            out.println("<p class='success'>✓ All database tests passed!</p>");
            out.println("<p>Your database is configured correctly and ready to use.</p>");
            out.println("<p><a href='" + request.getContextPath() + "/'>Go to Home Page</a></p>");
            out.println("</div>");

        } catch (Exception e) {
            out.println("<h2>Unexpected Error</h2>");
            out.println("<p class='error'>An unexpected error occurred:</p>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    // Ignore
                }
            }
        }

        out.println("</body>");
        out.println("</html>");
    }
}
