package com.animaladoption.service;

import com.animaladoption.dao.AdoptionApplicationDAO;
import com.animaladoption.dao.AnimalDAO;
import com.animaladoption.dao.AdopterProfileDAO;
import com.animaladoption.dao.ShelterDAO;
import com.animaladoption.model.AdoptionApplication;
import com.animaladoption.model.Animal;
import com.animaladoption.model.AdopterProfile;
import com.animaladoption.model.Shelter;

import java.sql.SQLException;
import java.util.List;

/**
 * Service class for adoption application workflow.
 */
public class AdoptionApplicationService {
    private final AdoptionApplicationDAO applicationDAO;
    private final AnimalDAO animalDAO;
    private final AdopterProfileDAO adopterProfileDAO;
    private final ShelterDAO shelterDAO;

    public AdoptionApplicationService() {
        this.applicationDAO = new AdoptionApplicationDAO();
        this.animalDAO = new AnimalDAO();
        this.adopterProfileDAO = new AdopterProfileDAO();
        this.shelterDAO = new ShelterDAO();
    }

    /**
     * Submit a new adoption application.
     */
    public int submitApplication(AdoptionApplication application, int adopterId) throws SQLException {
        // Validate animal exists and is available
        Animal animal = animalDAO.findById(application.getAnimalId());
        if (animal == null) {
            throw new IllegalArgumentException("Animal not found");
        }

        if (!"available".equals(animal.getStatus())) {
            throw new IllegalArgumentException("This animal is no longer available for adoption");
        }

        // Check if adopter has a profile
        AdopterProfile profile = adopterProfileDAO.findByUserId(adopterId);
        if (profile == null) {
            throw new IllegalArgumentException("Please complete your adopter profile before applying");
        }

        // Check if already applied for this animal
        if (applicationDAO.hasAppliedForAnimal(adopterId, application.getAnimalId())) {
            throw new IllegalArgumentException("You have already submitted an application for this animal");
        }

        // Set required fields
        application.setAdopterId(adopterId);
        application.setShelterId(animal.getShelterId());
        application.setStatus("submitted");

        // Validate application data
        validateApplication(application);

        return applicationDAO.create(application);
    }

    /**
     * Get application by ID.
     */
    public AdoptionApplication getApplicationById(int applicationId) throws SQLException {
        return applicationDAO.findById(applicationId);
    }

    /**
     * Get all applications for an adopter.
     */
    public List<AdoptionApplication> getAdopterApplications(int adopterId) throws SQLException {
        return applicationDAO.findByAdopterId(adopterId);
    }

    /**
     * Get all applications for a shelter.
     */
    public List<AdoptionApplication> getShelterApplications(int shelterId, String status) throws SQLException {
        return applicationDAO.findByShelterId(shelterId, status);
    }

    /**
     * Get all applications for a specific animal.
     */
    public List<AdoptionApplication> getAnimalApplications(int animalId) throws SQLException {
        return applicationDAO.findByAnimalId(animalId);
    }

    /**
     * Update application status (shelter only).
     */
    public boolean updateApplicationStatus(int applicationId, String newStatus, int userId, String shelterNotes)
            throws SQLException {

        AdoptionApplication application = applicationDAO.findById(applicationId);
        if (application == null) {
            throw new IllegalArgumentException("Application not found");
        }

        // Verify user is from the shelter
        Shelter shelter = shelterDAO.findById(application.getShelterId());
        if (shelter == null || shelter.getUserId() != userId) {
            throw new IllegalArgumentException("You don't have permission to update this application");
        }

        // Validate status
        String[] validStatuses = {"submitted", "under_review", "approved", "rejected", "withdrawn"};
        boolean isValidStatus = false;
        for (String status : validStatuses) {
            if (status.equals(newStatus)) {
                isValidStatus = true;
                break;
            }
        }

        if (!isValidStatus) {
            throw new IllegalArgumentException(
                "Invalid status. Must be: submitted, under_review, approved, rejected, or withdrawn");
        }

        // If approved, update animal status to pending
        if ("approved".equals(newStatus)) {
            Animal animal = animalDAO.findById(application.getAnimalId());
            if (animal != null) {
                animal.setStatus("pending");
                animalDAO.update(animal);
            }
        }

        return applicationDAO.updateStatus(applicationId, newStatus, userId, shelterNotes);
    }

    /**
     * Withdraw application (adopter only).
     */
    public boolean withdrawApplication(int applicationId, int adopterId) throws SQLException {
        AdoptionApplication application = applicationDAO.findById(applicationId);
        if (application == null) {
            throw new IllegalArgumentException("Application not found");
        }

        if (application.getAdopterId() != adopterId) {
            throw new IllegalArgumentException("You don't have permission to withdraw this application");
        }

        if ("approved".equals(application.getStatus()) || "rejected".equals(application.getStatus())) {
            throw new IllegalArgumentException("Cannot withdraw an application that has been " + application.getStatus());
        }

        // Get shelter user ID (we'll use shelter_id as placeholder since we need a user_id)
        Shelter shelter = shelterDAO.findById(application.getShelterId());
        int shelterUserId = shelter != null ? shelter.getUserId() : 0;

        return applicationDAO.updateStatus(applicationId, "withdrawn", shelterUserId, "Withdrawn by adopter");
    }

    /**
     * Update application information (adopter only, before review).
     */
    public boolean updateApplication(AdoptionApplication application, int adopterId) throws SQLException {
        AdoptionApplication existing = applicationDAO.findById(application.getApplicationId());
        if (existing == null) {
            throw new IllegalArgumentException("Application not found");
        }

        if (existing.getAdopterId() != adopterId) {
            throw new IllegalArgumentException("You don't have permission to update this application");
        }

        if (!"submitted".equals(existing.getStatus())) {
            throw new IllegalArgumentException("Can only update applications in 'submitted' status");
        }

        validateApplication(application);

        return applicationDAO.update(application);
    }

    /**
     * Approve application and mark animal as adopted.
     */
    public boolean finalizeAdoption(int applicationId, int userId) throws SQLException {
        AdoptionApplication application = applicationDAO.findById(applicationId);
        if (application == null) {
            throw new IllegalArgumentException("Application not found");
        }

        // Verify user is from the shelter
        Shelter shelter = shelterDAO.findById(application.getShelterId());
        if (shelter == null || shelter.getUserId() != userId) {
            throw new IllegalArgumentException("You don't have permission to finalize this adoption");
        }

        if (!"approved".equals(application.getStatus())) {
            throw new IllegalArgumentException("Application must be approved before finalizing adoption");
        }

        // Update animal status to adopted
        Animal animal = animalDAO.findById(application.getAnimalId());
        if (animal != null) {
            animal.setStatus("adopted");
            animalDAO.update(animal);

            // Reject all other pending applications for this animal
            List<AdoptionApplication> otherApplications = applicationDAO.findByAnimalId(application.getAnimalId());
            for (AdoptionApplication otherApp : otherApplications) {
                if (otherApp.getApplicationId() != applicationId &&
                    ("submitted".equals(otherApp.getStatus()) || "under_review".equals(otherApp.getStatus()))) {
                    applicationDAO.updateStatus(otherApp.getApplicationId(), "rejected", userId,
                        "Animal has been adopted by another applicant");
                }
            }
        }

        return true;
    }

    /**
     * Get application count for an adopter.
     */
    public int getAdopterApplicationCount(int adopterId) throws SQLException {
        return applicationDAO.countByAdopter(adopterId);
    }

    /**
     * Check if adopter has applied for a specific animal.
     */
    public boolean hasAppliedForAnimal(int adopterId, int animalId) throws SQLException {
        return applicationDAO.hasAppliedForAnimal(adopterId, animalId);
    }

    /**
     * Validate application data.
     */
    private void validateApplication(AdoptionApplication application) {
        if (application.getWhyAdopt() == null || application.getWhyAdopt().trim().isEmpty()) {
            throw new IllegalArgumentException("Please explain why you want to adopt this animal");
        }

        if (application.getWhyAdopt().length() < 50) {
            throw new IllegalArgumentException("Please provide a more detailed explanation (at least 50 characters)");
        }

        // References are optional but if provided, should be complete
        boolean hasRef1 = application.getReference1Name() != null && !application.getReference1Name().trim().isEmpty();
        boolean hasRef1Phone = application.getReference1Phone() != null && !application.getReference1Phone().trim().isEmpty();

        if (hasRef1 && !hasRef1Phone) {
            throw new IllegalArgumentException("Please provide phone number for reference 1");
        }

        boolean hasRef2 = application.getReference2Name() != null && !application.getReference2Name().trim().isEmpty();
        boolean hasRef2Phone = application.getReference2Phone() != null && !application.getReference2Phone().trim().isEmpty();

        if (hasRef2 && !hasRef2Phone) {
            throw new IllegalArgumentException("Please provide phone number for reference 2");
        }
    }
}
