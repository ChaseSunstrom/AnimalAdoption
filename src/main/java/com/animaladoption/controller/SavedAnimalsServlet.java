package com.animaladoption.controller;

import com.animaladoption.dao.SavedAnimalDAO;
import com.animaladoption.model.Animal;
import com.animaladoption.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for managing saved/favorite animals.
 */
@WebServlet(name = "SavedAnimalsServlet", urlPatterns = {"/saved-animals", "/saved-animals/*"})
public class SavedAnimalsServlet extends HttpServlet {
    
    private final SavedAnimalDAO savedAnimalDAO = new SavedAnimalDAO();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Check if user is an adopter
        if (!"adopter".equals(user.getUserType())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Only adopters can save animals");
            return;
        }
        
        // Get all saved animals
        List<Animal> savedAnimals = savedAnimalDAO.getSavedAnimals(user.getUserId());
        
        request.setAttribute("savedAnimals", savedAnimals);
        request.setAttribute("totalSaved", savedAnimals.size());
        
        request.getRequestDispatcher("/WEB-INF/views/adopter/saved-animals.jsp")
               .forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"success\": false, \"message\": \"Not authenticated\"}");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Check if user is an adopter
        if (!"adopter".equals(user.getUserType())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"success\": false, \"message\": \"Only adopters can save animals\"}");
            return;
        }
        
        String action = request.getParameter("action");
        String animalIdStr = request.getParameter("animalId");
        
        if (animalIdStr == null || action == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Missing parameters\"}");
            return;
        }
        
        try {
            int animalId = Integer.parseInt(animalIdStr);
            boolean success;
            
            if ("save".equals(action)) {
                success = savedAnimalDAO.saveAnimal(user.getUserId(), animalId);
            } else if ("unsave".equals(action)) {
                success = savedAnimalDAO.unsaveAnimal(user.getUserId(), animalId);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("{\"success\": false, \"message\": \"Invalid action\"}");
                return;
            }
            
            response.setContentType("application/json");
            if (success) {
                int count = savedAnimalDAO.getSavedAnimalsCount(user.getUserId());
                response.getWriter().write("{\"success\": true, \"count\": " + count + "}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Database error\"}");
            }
            
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid animal ID\"}");
        }
    }
}


