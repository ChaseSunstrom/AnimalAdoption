package com.animaladoption.controller;

import com.animaladoption.model.AdopterProfile;
import com.animaladoption.model.MatchScore;
import com.animaladoption.dao.AdopterProfileDAO;
import com.animaladoption.service.RecommendationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet for adopter dashboard.
 */
@WebServlet(name = "DashboardServlet", urlPatterns = {"/dashboard"})
public class DashboardServlet extends HttpServlet {
    private RecommendationService recommendationService;
    private AdopterProfileDAO adopterProfileDAO;

    @Override
    public void init() throws ServletException {
        recommendationService = new RecommendationService();
        adopterProfileDAO = new AdopterProfileDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        String userType = (String) session.getAttribute("userType");

        // Only adopters can access this dashboard
        if (!"adopter".equals(userType)) {
            response.sendRedirect(request.getContextPath() + "/animals");
            return;
        }

        try {
            // Check if adopter has completed profile
            AdopterProfile profile = adopterProfileDAO.findByUserId(userId);
            
            if (profile == null) {
                // Redirect to profile creation
                session.setAttribute("message", "Please complete your profile to get personalized recommendations!");
                response.sendRedirect(request.getContextPath() + "/profile/edit");
                return;
            }

            request.setAttribute("profile", profile);

            // Get top recommended animals
            try {
                List<MatchScore> recommendations = recommendationService.getTopRecommendations(userId, 12);
                request.setAttribute("recommendations", recommendations);
            } catch (SQLException e) {
                // Recommendations are optional, continue without them
                request.setAttribute("recommendationsError", "Could not load recommendations at this time.");
            }

            // Forward to dashboard page
            request.getRequestDispatcher("/WEB-INF/views/adopter/dashboard.jsp").forward(request, response);

        } catch (SQLException e) {
            request.setAttribute("error", "Database error. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}




