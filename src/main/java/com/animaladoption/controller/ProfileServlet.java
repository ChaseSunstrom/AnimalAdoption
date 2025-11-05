package com.animaladoption.controller;

import com.animaladoption.model.AdopterProfile;
import com.animaladoption.dao.AdopterProfileDAO;
import com.animaladoption.service.RecommendationService;
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
 * Servlet for adopter profile management.
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile/edit", "/profile/view"})
public class ProfileServlet extends HttpServlet {
    private AdopterProfileDAO adopterProfileDAO;
    private RecommendationService recommendationService;

    @Override
    public void init() throws ServletException {
        adopterProfileDAO = new AdopterProfileDAO();
        recommendationService = new RecommendationService();
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

        if (!"adopter".equals(userType)) {
            response.sendRedirect(request.getContextPath() + "/animals");
            return;
        }

        try {
            AdopterProfile profile = adopterProfileDAO.findByUserId(userId);
            request.setAttribute("profile", profile);

            // Forward to profile edit page
            request.getRequestDispatcher("/WEB-INF/views/adopter/profile-edit.jsp").forward(request, response);

        } catch (SQLException e) {
            request.setAttribute("error", "Database error. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        String userType = (String) session.getAttribute("userType");

        if (!"adopter".equals(userType)) {
            response.sendRedirect(request.getContextPath() + "/animals");
            return;
        }

        try {
            // Get form parameters
            String addressLine1 = ValidationUtil.sanitize(request.getParameter("addressLine1"));
            String addressLine2 = ValidationUtil.sanitize(request.getParameter("addressLine2"));
            String city = ValidationUtil.sanitize(request.getParameter("city"));
            String state = ValidationUtil.sanitize(request.getParameter("state"));
            String zipCode = ValidationUtil.sanitize(request.getParameter("zipCode"));
            String homeType = ValidationUtil.sanitize(request.getParameter("homeType"));
            String homeSize = ValidationUtil.sanitize(request.getParameter("homeSize"));
            boolean hasYard = "true".equals(request.getParameter("hasYard"));
            boolean hasChildren = "true".equals(request.getParameter("hasChildren"));
            String childrenAges = ValidationUtil.sanitize(request.getParameter("childrenAges"));
            boolean hasOtherPets = "true".equals(request.getParameter("hasOtherPets"));
            String otherPetsDescription = ValidationUtil.sanitize(request.getParameter("otherPetsDescription"));
            String experienceLevel = ValidationUtil.sanitize(request.getParameter("experienceLevel"));
            String activityLevel = ValidationUtil.sanitize(request.getParameter("activityLevel"));
            String preferencesNotes = ValidationUtil.sanitize(request.getParameter("preferencesNotes"));

            // Create or update profile
            AdopterProfile profile = adopterProfileDAO.findByUserId(userId);
            boolean isNew = (profile == null);

            if (isNew) {
                profile = new AdopterProfile();
                profile.setUserId(userId);
            }

            profile.setAddressLine1(addressLine1);
            profile.setAddressLine2(addressLine2);
            profile.setCity(city);
            profile.setState(state);
            profile.setZipCode(zipCode);
            profile.setHomeType(homeType);
            profile.setHomeSize(homeSize);
            profile.setHasYard(hasYard);
            profile.setHasChildren(hasChildren);
            profile.setChildrenAges(childrenAges);
            profile.setHasOtherPets(hasOtherPets);
            profile.setOtherPetsDescription(otherPetsDescription);
            profile.setExperienceLevel(experienceLevel);
            profile.setActivityLevel(activityLevel);
            profile.setPreferencesNotes(preferencesNotes);

            if (isNew) {
                adopterProfileDAO.create(profile);
            } else {
                adopterProfileDAO.update(profile);
                // Recalculate match scores when profile is updated
                try {
                    recommendationService.recalculateMatches(userId);
                } catch (SQLException e) {
                    // Match scores are optional, continue without them
                }
            }

            session.setAttribute("successMessage", "Profile saved successfully!");
            response.sendRedirect(request.getContextPath() + "/dashboard");

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        } catch (SQLException e) {
            request.setAttribute("error", "Database error. Please try again later.");
            doGet(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred. Please try again.");
            doGet(request, response);
        }
    }
}




