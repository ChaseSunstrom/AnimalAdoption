package com.animaladoption.controller;

import com.animaladoption.model.User;
import com.animaladoption.service.AuthenticationService;
import com.animaladoption.util.ValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet for user registration.
 */
@WebServlet(name = "RegistrationServlet", urlPatterns = {"/register"})
public class RegistrationServlet extends HttpServlet {
    private AuthenticationService authService;

    @Override
    public void init() throws ServletException {
        authService = new AuthenticationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to registration page
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userType = request.getParameter("userType");
        String email = ValidationUtil.sanitize(request.getParameter("email"));
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String firstName = ValidationUtil.sanitize(request.getParameter("firstName"));
        String lastName = ValidationUtil.sanitize(request.getParameter("lastName"));
        String phone = ValidationUtil.sanitize(request.getParameter("phone"));

        // Validate passwords match
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            request.setAttribute("formData", request.getParameterMap());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        try {
            User user;

            if ("shelter".equals(userType)) {
                // Shelter registration
                String shelterName = ValidationUtil.sanitize(request.getParameter("shelterName"));
                String city = ValidationUtil.sanitize(request.getParameter("city"));
                String state = ValidationUtil.sanitize(request.getParameter("state"));

                if (ValidationUtil.isEmpty(shelterName)) {
                    request.setAttribute("error", "Shelter name is required");
                    request.setAttribute("formData", request.getParameterMap());
                    request.getRequestDispatcher("/register.jsp").forward(request, response);
                    return;
                }

                user = authService.registerShelter(email, password, firstName, lastName, phone,
                                                   shelterName, city, state);

                // Set success message for shelters
                request.setAttribute("success",
                    "Registration successful! Your shelter account is pending verification. " +
                    "You can log in, but you'll need to be verified before adding animals.");

            } else {
                // Adopter registration (default)
                user = authService.registerAdopter(email, password, firstName, lastName, phone);
            }

            if (user != null) {
                // Auto-login after registration
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("userId", user.getUserId());
                session.setAttribute("userType", user.getUserType());
                session.setAttribute("userName", user.getFirstName() + " " + user.getLastName());

                // Set success message
                if (!"shelter".equals(userType)) {
                    session.setAttribute("successMessage",
                        "Welcome to Animal Adoption! Please complete your profile to get personalized recommendations.");
                }

                // Redirect based on user type
                if ("shelter".equals(user.getUserType())) {
                    response.sendRedirect(request.getContextPath() + "/shelter/dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/profile/edit");
                }
            }

        } catch (IllegalArgumentException e) {
            // Validation error
            request.setAttribute("error", e.getMessage());
            request.setAttribute("formData", request.getParameterMap());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Database error. Please try again later.");
            request.setAttribute("formData", request.getParameterMap());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred. Please try again.");
            request.setAttribute("formData", request.getParameterMap());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
}
