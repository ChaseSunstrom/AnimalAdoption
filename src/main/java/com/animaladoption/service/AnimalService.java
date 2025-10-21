package com.animaladoption.service;

import com.animaladoption.dao.AnimalDAO;
import com.animaladoption.dao.ShelterDAO;
import com.animaladoption.model.Animal;
import com.animaladoption.model.SearchCriteria;
import com.animaladoption.model.Shelter;

import java.sql.SQLException;
import java.util.List;

/**
 * Service class for animal management operations.
 */
public class AnimalService {
    private final AnimalDAO animalDAO;
    private final ShelterDAO shelterDAO;
    private static final int DEFAULT_PAGE_SIZE = 12;

    public AnimalService() {
        this.animalDAO = new AnimalDAO();
        this.shelterDAO = new ShelterDAO();
    }

    /**
     * Search for animals with pagination.
     */
    public List<Animal> searchAnimals(SearchCriteria criteria, int page, int pageSize) throws SQLException {
        if (criteria == null) {
            criteria = new SearchCriteria();
        }

        if (pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        int offset = (page - 1) * pageSize;
        return animalDAO.search(criteria, offset, pageSize);
    }

    /**
     * Search for animals with default page size.
     */
    public List<Animal> searchAnimals(SearchCriteria criteria, int page) throws SQLException {
        return searchAnimals(criteria, page, DEFAULT_PAGE_SIZE);
    }

    /**
     * Get total count of search results.
     */
    public int getSearchResultCount(SearchCriteria criteria) throws SQLException {
        if (criteria == null) {
            criteria = new SearchCriteria();
        }
        return animalDAO.countSearch(criteria);
    }

    /**
     * Calculate total pages for search results.
     */
    public int getTotalPages(SearchCriteria criteria, int pageSize) throws SQLException {
        if (pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        int totalCount = getSearchResultCount(criteria);
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    /**
     * Get animal by ID.
     */
    public Animal getAnimalById(int animalId) throws SQLException {
        return animalDAO.findById(animalId);
    }

    /**
     * Get all animals for a specific shelter.
     */
    public List<Animal> getAnimalsByShelterId(int shelterId) throws SQLException {
        return animalDAO.findByShelterId(shelterId);
    }

    /**
     * Create a new animal listing.
     */
    public int createAnimal(Animal animal, int userId) throws SQLException {
        // Validate that the user is a shelter and owns this shelter
        Shelter shelter = shelterDAO.findById(animal.getShelterId());
        if (shelter == null) {
            throw new IllegalArgumentException("Shelter not found");
        }

        if (shelter.getUserId() != userId) {
            throw new IllegalArgumentException("You don't have permission to add animals to this shelter");
        }

        if (!shelter.isVerified()) {
            throw new IllegalArgumentException("Your shelter must be verified before adding animals");
        }

        // Validate animal data
        validateAnimal(animal);

        // Set default status if not set
        if (animal.getStatus() == null || animal.getStatus().isEmpty()) {
            animal.setStatus("available");
        }

        return animalDAO.create(animal);
    }

    /**
     * Update an existing animal listing.
     */
    public boolean updateAnimal(Animal animal, int userId) throws SQLException {
        // Check if animal exists
        Animal existing = animalDAO.findById(animal.getAnimalId());
        if (existing == null) {
            throw new IllegalArgumentException("Animal not found");
        }

        // Validate that the user is a shelter and owns this shelter
        Shelter shelter = shelterDAO.findById(existing.getShelterId());
        if (shelter == null) {
            throw new IllegalArgumentException("Shelter not found");
        }

        if (shelter.getUserId() != userId) {
            throw new IllegalArgumentException("You don't have permission to update this animal");
        }

        // Validate animal data
        validateAnimal(animal);

        // Ensure shelter_id doesn't change
        animal.setShelterId(existing.getShelterId());

        return animalDAO.update(animal);
    }

    /**
     * Update animal status (available, pending, adopted, etc.).
     */
    public boolean updateAnimalStatus(int animalId, String status, int userId) throws SQLException {
        Animal animal = animalDAO.findById(animalId);
        if (animal == null) {
            throw new IllegalArgumentException("Animal not found");
        }

        // Validate that the user is a shelter and owns this shelter
        Shelter shelter = shelterDAO.findById(animal.getShelterId());
        if (shelter == null) {
            throw new IllegalArgumentException("Shelter not found");
        }

        if (shelter.getUserId() != userId) {
            throw new IllegalArgumentException("You don't have permission to update this animal");
        }

        // Validate status
        String[] validStatuses = {"available", "pending", "adopted", "not_available"};
        boolean isValidStatus = false;
        for (String validStatus : validStatuses) {
            if (validStatus.equals(status)) {
                isValidStatus = true;
                break;
            }
        }

        if (!isValidStatus) {
            throw new IllegalArgumentException("Invalid status. Must be: available, pending, adopted, or not_available");
        }

        animal.setStatus(status);
        return animalDAO.update(animal);
    }

    /**
     * Get featured/recently added animals.
     */
    public List<Animal> getFeaturedAnimals(int limit) throws SQLException {
        SearchCriteria criteria = new SearchCriteria();
        return animalDAO.search(criteria, 0, limit);
    }

    /**
     * Validate animal data.
     */
    private void validateAnimal(Animal animal) {
        if (animal.getName() == null || animal.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Animal name is required");
        }

        if (animal.getSpecies() == null || animal.getSpecies().trim().isEmpty()) {
            throw new IllegalArgumentException("Species is required");
        }

        String[] validSpecies = {"dog", "cat", "bird", "rabbit", "other"};
        boolean isValidSpecies = false;
        for (String species : validSpecies) {
            if (species.equals(animal.getSpecies().toLowerCase())) {
                isValidSpecies = true;
                break;
            }
        }

        if (!isValidSpecies) {
            throw new IllegalArgumentException("Invalid species. Must be: dog, cat, bird, rabbit, or other");
        }

        if (animal.getAgeYears() < 0 || animal.getAgeMonths() < 0 || animal.getAgeMonths() > 11) {
            throw new IllegalArgumentException("Invalid age");
        }

        if (animal.getGender() != null && !animal.getGender().isEmpty()) {
            String[] validGenders = {"male", "female", "unknown"};
            boolean isValidGender = false;
            for (String gender : validGenders) {
                if (gender.equals(animal.getGender().toLowerCase())) {
                    isValidGender = true;
                    break;
                }
            }

            if (!isValidGender) {
                throw new IllegalArgumentException("Invalid gender. Must be: male, female, or unknown");
            }
        }

        if (animal.getSize() != null && !animal.getSize().isEmpty()) {
            String[] validSizes = {"small", "medium", "large", "extra_large"};
            boolean isValidSize = false;
            for (String size : validSizes) {
                if (size.equals(animal.getSize().toLowerCase())) {
                    isValidSize = true;
                    break;
                }
            }

            if (!isValidSize) {
                throw new IllegalArgumentException("Invalid size. Must be: small, medium, large, or extra_large");
            }
        }
    }
}
