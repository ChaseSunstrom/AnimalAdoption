package com.animaladoption.controller;

import com.animaladoption.model.Animal;
import com.animaladoption.model.Shelter;
import com.animaladoption.dao.ShelterDAO;
import com.animaladoption.service.AnimalService;
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
 * Servlet for shelter dashboard.
 */
@WebServlet(name = "ShelterDashboardServlet", urlPatterns = {"/shelter/dashboard"})
public class ShelterDashboardServlet extends HttpServlet {
    private AnimalService animalService;
    private ShelterDAO shelterDAO;

    @Override
    public void init() throws ServletException {
        animalService = new AnimalService();
        shelterDAO = new ShelterDAO();
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

        // Only shelter users can access this dashboard
        if (!"shelter".equals(userType)) {
            response.sendRedirect(request.getContextPath() + "/animals");
            return;
        }

        try {
            // Get shelter information
            Shelter shelter = shelterDAO.findByUserId(userId);
            
            if (shelter == null) {
                session.setAttribute("errorMessage", "Shelter profile not found. Please contact support.");
                response.sendRedirect(request.getContextPath() + "/");
                return;
            }

            request.setAttribute("shelter", shelter);

            // Get all animals for this shelter
            List<Animal> animals = animalService.getAnimalsByShelterId(shelter.getShelterId());
            request.setAttribute("animals", animals);

            // Count animals by status
            long availableCount = animals.stream().filter(a -> "available".equals(a.getStatus())).count();
            long pendingCount = animals.stream().filter(a -> "pending".equals(a.getStatus())).count();
            long adoptedCount = animals.stream().filter(a -> "adopted".equals(a.getStatus())).count();

            request.setAttribute("availableCount", availableCount);
            request.setAttribute("pendingCount", pendingCount);
            request.setAttribute("adoptedCount", adoptedCount);

            // Forward to dashboard page
            request.getRequestDispatcher("/WEB-INF/views/shelter/dashboard.jsp").forward(request, response);

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




