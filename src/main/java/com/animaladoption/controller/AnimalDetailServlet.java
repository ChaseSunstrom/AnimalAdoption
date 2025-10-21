package com.animaladoption.controller;

import com.animaladoption.model.Animal;
import com.animaladoption.model.MatchScore;
import com.animaladoption.service.AnimalService;
import com.animaladoption.service.RecommendationService;
import com.animaladoption.service.AdoptionApplicationService;
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
 * Servlet for displaying animal details.
 */
@WebServlet(name = "AnimalDetailServlet", urlPatterns = {"/animal-detail"})
public class AnimalDetailServlet extends HttpServlet {
    private AnimalService animalService;
    private RecommendationService recommendationService;
    private AdoptionApplicationService applicationService;

    @Override
    public void init() throws ServletException {
        animalService = new AnimalService();
        recommendationService = new RecommendationService();
        applicationService = new AdoptionApplicationService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int animalId = ValidationUtil.parseInt(request.getParameter("id"), 0);

        if (animalId <= 0) {
            response.sendRedirect(request.getContextPath() + "/animals");
            return;
        }

        try {
            Animal animal = animalService.getAnimalById(animalId);

            if (animal == null) {
                request.setAttribute("error", "Animal not found");
                request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
                return;
            }

            request.setAttribute("animal", animal);

            // If user is logged in as adopter, get match score
            HttpSession session = request.getSession(false);
            if (session != null) {
                Integer userId = (Integer) session.getAttribute("userId");
                String userType = (String) session.getAttribute("userType");

                if (userId != null && "adopter".equals(userType)) {
                    try {
                        MatchScore matchScore = recommendationService.getMatchScore(userId, animalId);
                        request.setAttribute("matchScore", matchScore);

                        // Check if already applied
                        boolean hasApplied = applicationService.hasAppliedForAnimal(userId, animalId);
                        request.setAttribute("hasApplied", hasApplied);
                    } catch (SQLException e) {
                        // Match score is optional, continue without it
                    }
                }
            }

            // Forward to detail page
            request.getRequestDispatcher("/WEB-INF/views/animals/detail.jsp").forward(request, response);

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
