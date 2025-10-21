package com.animaladoption.controller;

import com.animaladoption.model.User;
import com.animaladoption.service.AuthenticationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet for user login.
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    private AuthenticationService authService;

    @Override
    public void init() throws ServletException {
        authService = new AuthenticationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to login page
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = authService.login(email, password);

            if (user != null) {
                // Login successful
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("userType", user.getUserType());
                session.setAttribute("userName", user.getFirstName() + " " + user.getLastName());

                // Check for redirect URL
                String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
                session.removeAttribute("redirectAfterLogin");

                if (redirectUrl != null && !redirectUrl.isEmpty()) {
                    response.sendRedirect(request.getContextPath() + redirectUrl);
                } else {
                    // Redirect based on user type
                    if ("shelter".equals(user.getUserType())) {
                        response.sendRedirect(request.getContextPath() + "/shelter/dashboard");
                    } else if ("admin".equals(user.getUserType())) {
                        response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/dashboard");
                    }
                }
            } else {
                // Login failed
                request.setAttribute("error", "Invalid email or password");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }

        } catch (IllegalStateException e) {
            // Account inactive
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Database error. Please try again later.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred. Please try again.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}
