package com.animaladoption.controller;

import com.animaladoption.model.AdoptionApplication;
import com.animaladoption.model.Animal;
import com.animaladoption.service.AdoptionApplicationService;
import com.animaladoption.service.AnimalService;
import com.animaladoption.dao.ShelterDAO;
import com.animaladoption.model.Shelter;
import com.animaladoption.util.ValidationUtil;
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
 * Servlet for managing adoption applications.
 */
@WebServlet(name = "AdoptionApplicationServlet", urlPatterns = {"/applications/*"})
public class AdoptionApplicationServlet extends HttpServlet {
    private AdoptionApplicationService applicationService;
    private AnimalService animalService;
    private ShelterDAO shelterDAO;

    @Override
    public void init() throws ServletException {
        applicationService = new AdoptionApplicationService();
        animalService = new AnimalService();
        shelterDAO = new ShelterDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        String userType = (String) session.getAttribute("userType");

        try {
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/my-applications")) {
                // List user's applications
                showMyApplications(request, response, userId, userType);

            } else if (pathInfo.equals("/new")) {
                // Show application form
                showApplicationForm(request, response);

            } else if (pathInfo.startsWith("/view/")) {
                // View application details
                int applicationId = extractIdFromPath(pathInfo, "/view/");
                viewApplication(request, response, applicationId, userId, userType);

            } else {
                response.sendRedirect(request.getContextPath() + "/applications");
            }

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

        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Integer userId = (Integer) session.getAttribute("userId");
        String userType = (String) session.getAttribute("userType");

        try {
            if (pathInfo != null && pathInfo.equals("/submit")) {
                // Submit new application
                submitApplication(request, response, userId);

            } else if (pathInfo != null && pathInfo.equals("/withdraw")) {
                // Withdraw application
                withdrawApplication(request, response, userId);

            } else if (pathInfo != null && pathInfo.equals("/update-status")) {
                // Update application status (shelter only)
                updateApplicationStatus(request, response, userId, userType);

            } else {
                response.sendRedirect(request.getContextPath() + "/applications");
            }

        } catch (SQLException e) {
            request.setAttribute("error", "Database error. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred. Please try again.");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
        }
    }

    private void showMyApplications(HttpServletRequest request, HttpServletResponse response,
                                    int userId, String userType) throws ServletException, IOException, SQLException {
        List<AdoptionApplication> applications;

        if ("adopter".equals(userType)) {
            applications = applicationService.getAdopterApplications(userId);
        } else if ("shelter".equals(userType)) {
            // For shelter, get their shelter ID first (simplified - should use ShelterDAO)
            int shelterId = ValidationUtil.parseInt(request.getParameter("shelterId"), 0);
            String status = request.getParameter("status");
            applications = applicationService.getShelterApplications(shelterId, status);
        } else {
            applications = List.of();
        }

        request.setAttribute("applications", applications);
        request.getRequestDispatcher("/WEB-INF/views/applications/list.jsp").forward(request, response);
    }

    private void showApplicationForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        int animalId = ValidationUtil.parseInt(request.getParameter("animalId"), 0);

        if (animalId <= 0) {
            response.sendRedirect(request.getContextPath() + "/animals");
            return;
        }

        Animal animal = animalService.getAnimalById(animalId);
        if (animal == null) {
            request.setAttribute("error", "Animal not found");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }

        request.setAttribute("animal", animal);
        request.getRequestDispatcher("/WEB-INF/views/applications/form.jsp").forward(request, response);
    }

    private void viewApplication(HttpServletRequest request, HttpServletResponse response,
                                 int applicationId, int userId, String userType)
            throws ServletException, IOException, SQLException {

        AdoptionApplication application = applicationService.getApplicationById(applicationId);

        if (application == null) {
            request.setAttribute("error", "Application not found");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }

        // Verify user has permission to view this application
        boolean canView = false;
        if ("adopter".equals(userType) && application.getAdopterId() == userId) {
            canView = true;
        } else if ("shelter".equals(userType)) {
            // Verify shelter ownership - only allow if application belongs to this shelter
            Shelter shelter = shelterDAO.findByUserId(userId);
            if (shelter != null && shelter.getShelterId() == application.getShelterId()) {
                canView = true;
            }
        }

        if (!canView) {
            request.setAttribute("error", "You don't have permission to view this application");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }

        request.setAttribute("application", application);
        request.getRequestDispatcher("/WEB-INF/views/applications/detail.jsp").forward(request, response);
    }

    private void submitApplication(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException, SQLException {

        int animalId = ValidationUtil.parseInt(request.getParameter("animalId"), 0);

        AdoptionApplication application = new AdoptionApplication();
        application.setAnimalId(animalId);
        application.setWhyAdopt(ValidationUtil.sanitize(request.getParameter("whyAdopt")));
        application.setPreviousPetExperience(ValidationUtil.sanitize(request.getParameter("previousPetExperience")));
        application.setVeterinarianName(ValidationUtil.sanitize(request.getParameter("veterinarianName")));
        application.setVeterinarianPhone(ValidationUtil.sanitize(request.getParameter("veterinarianPhone")));
        application.setReference1Name(ValidationUtil.sanitize(request.getParameter("reference1Name")));
        application.setReference1Phone(ValidationUtil.sanitize(request.getParameter("reference1Phone")));
        application.setReference1Relationship(ValidationUtil.sanitize(request.getParameter("reference1Relationship")));
        application.setReference2Name(ValidationUtil.sanitize(request.getParameter("reference2Name")));
        application.setReference2Phone(ValidationUtil.sanitize(request.getParameter("reference2Phone")));
        application.setReference2Relationship(ValidationUtil.sanitize(request.getParameter("reference2Relationship")));

        try {
            int applicationId = applicationService.submitApplication(application, userId);

            if (applicationId > 0) {
                request.getSession().setAttribute("successMessage",
                    "Application submitted successfully! The shelter will review your application.");
                response.sendRedirect(request.getContextPath() + "/applications/view/" + applicationId);
            } else {
                request.setAttribute("error", "Failed to submit application");
                request.setAttribute("animal", animalService.getAnimalById(animalId));
                request.setAttribute("formData", application);
                request.getRequestDispatcher("/WEB-INF/views/applications/form.jsp").forward(request, response);
            }

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("animal", animalService.getAnimalById(animalId));
            request.setAttribute("formData", application);
            request.getRequestDispatcher("/WEB-INF/views/applications/form.jsp").forward(request, response);
        }
    }

    private void withdrawApplication(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException, SQLException {

        int applicationId = ValidationUtil.parseInt(request.getParameter("applicationId"), 0);

        try {
            boolean success = applicationService.withdrawApplication(applicationId, userId);

            if (success) {
                request.getSession().setAttribute("successMessage", "Application withdrawn successfully");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to withdraw application");
            }

        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/applications");
    }

    private void updateApplicationStatus(HttpServletRequest request, HttpServletResponse response,
                                         int userId, String userType)
            throws ServletException, IOException, SQLException {

        if (!"shelter".equals(userType)) {
            request.setAttribute("error", "Only shelters can update application status");
            request.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(request, response);
            return;
        }

        int applicationId = ValidationUtil.parseInt(request.getParameter("applicationId"), 0);
        String status = ValidationUtil.sanitize(request.getParameter("status"));
        String notes = ValidationUtil.sanitize(request.getParameter("shelterNotes"));

        try {
            boolean success = applicationService.updateApplicationStatus(applicationId, status, userId, notes);

            if (success) {
                request.getSession().setAttribute("successMessage", "Application status updated successfully");
            } else {
                request.getSession().setAttribute("errorMessage", "Failed to update application status");
            }

        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("errorMessage", e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/applications/view/" + applicationId);
    }

    private int extractIdFromPath(String pathInfo, String prefix) {
        if (pathInfo != null && pathInfo.startsWith(prefix)) {
            String idStr = pathInfo.substring(prefix.length());
            return ValidationUtil.parseInt(idStr, 0);
        }
        return 0;
    }
}
