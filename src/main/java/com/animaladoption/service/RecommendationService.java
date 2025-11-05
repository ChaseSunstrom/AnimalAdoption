package com.animaladoption.service;

import com.animaladoption.dao.MatchScoreDAO;
import com.animaladoption.dao.AnimalDAO;
import com.animaladoption.dao.AdopterProfileDAO;
import com.animaladoption.model.MatchScore;
import com.animaladoption.model.Animal;
import com.animaladoption.model.AdopterProfile;
import com.animaladoption.model.SearchCriteria;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class for pet recommendation and matching algorithm.
 */
public class RecommendationService {
    private final MatchScoreDAO matchScoreDAO;
    private final AnimalDAO animalDAO;
    private final AdopterProfileDAO adopterProfileDAO;

    // Scoring weights
    private static final double WEIGHT_COMPATIBILITY = 0.35;
    private static final double WEIGHT_EXPERIENCE = 0.20;
    private static final double WEIGHT_HOME = 0.25;
    private static final double WEIGHT_ACTIVITY = 0.20;

    public RecommendationService() {
        this.matchScoreDAO = new MatchScoreDAO();
        this.animalDAO = new AnimalDAO();
        this.adopterProfileDAO = new AdopterProfileDAO();
    }

    /**
     * Calculate match score between an adopter and an animal.
     */
    public MatchScore calculateMatchScore(int adopterId, int animalId) throws SQLException {
        AdopterProfile profile = adopterProfileDAO.findByUserId(adopterId);
        if (profile == null) {
            throw new IllegalArgumentException("Adopter profile not found");
        }

        Animal animal = animalDAO.findById(animalId);
        if (animal == null) {
            throw new IllegalArgumentException("Animal not found");
        }

        Map<String, Double> scoreDetails = new HashMap<>();
        double totalScore = 0;

        // 1. Compatibility Score (35%) - kids, other pets
        double compatibilityScore = calculateCompatibilityScore(profile, animal);
        scoreDetails.put("compatibility", compatibilityScore);
        totalScore += compatibilityScore * WEIGHT_COMPATIBILITY;

        // 2. Experience Score (20%) - match experience level with animal needs
        double experienceScore = calculateExperienceScore(profile, animal);
        scoreDetails.put("experience", experienceScore);
        totalScore += experienceScore * WEIGHT_EXPERIENCE;

        // 3. Home Environment Score (25%) - size, yard, space
        double homeScore = calculateHomeScore(profile, animal);
        scoreDetails.put("home", homeScore);
        totalScore += homeScore * WEIGHT_HOME;

        // 4. Activity Level Match (20%) - energy levels
        double activityScore = calculateActivityScore(profile, animal);
        scoreDetails.put("activity", activityScore);
        totalScore += activityScore * WEIGHT_ACTIVITY;

        MatchScore matchScore = new MatchScore(adopterId, animalId, totalScore);
        matchScore.setScoreDetails(scoreDetails);

        return matchScore;
    }

    /**
     * Calculate and save match scores for all available animals for an adopter.
     */
    public void calculateAllMatches(int adopterId) throws SQLException {
        AdopterProfile profile = adopterProfileDAO.findByUserId(adopterId);
        if (profile == null) {
            throw new IllegalArgumentException("Adopter profile not found");
        }

        // Get all available animals
        SearchCriteria criteria = new SearchCriteria();
        List<Animal> animals = animalDAO.search(criteria, 0, 1000); // Get up to 1000 animals

        // Calculate and save scores
        for (Animal animal : animals) {
            if ("available".equals(animal.getStatus())) {
                MatchScore score = calculateMatchScore(adopterId, animal.getAnimalId());
                matchScoreDAO.upsert(score);
            }
        }
    }

    /**
     * Get top recommended animals for an adopter.
     */
    public List<MatchScore> getTopRecommendations(int adopterId, int limit) throws SQLException {
        return matchScoreDAO.getTopMatches(adopterId, limit);
    }

    /**
     * Get match score for a specific adopter-animal pair.
     */
    public MatchScore getMatchScore(int adopterId, int animalId) throws SQLException {
        MatchScore existing = matchScoreDAO.findByAdopterAndAnimal(adopterId, animalId);
        if (existing != null) {
            return existing;
        }

        // Calculate if not found
        MatchScore score = calculateMatchScore(adopterId, animalId);
        matchScoreDAO.create(score);
        return score;
    }

    /**
     * Recalculate all match scores for an adopter (when profile changes).
     */
    public void recalculateMatches(int adopterId) throws SQLException {
        matchScoreDAO.deleteByAdopterId(adopterId);
        calculateAllMatches(adopterId);
    }

    /**
     * Calculate compatibility score based on household composition.
     */
    private double calculateCompatibilityScore(AdopterProfile profile, Animal animal) {
        double score = 100.0;

        // Check kids compatibility
        if (profile.isHasChildren()) {
            if (!animal.isGoodWithKids()) {
                score -= 50; // Major penalty
            }
        }

        // Check other pets compatibility
        if (profile.isHasOtherPets()) {
            String petsDesc = profile.getOtherPetsDescription();
            if (petsDesc != null) {
                if (petsDesc.toLowerCase().contains("dog") && !animal.isGoodWithDogs()) {
                    score -= 30;
                }
                if (petsDesc.toLowerCase().contains("cat") && !animal.isGoodWithCats()) {
                    score -= 30;
                }
            }
        }

        return Math.max(0, Math.min(100, score));
    }

    /**
     * Calculate experience score.
     */
    private double calculateExperienceScore(AdopterProfile profile, Animal animal) {
        if (profile.getExperienceLevel() == null) {
            return 70; // Neutral score
        }

        String experience = profile.getExperienceLevel().toLowerCase();
        boolean requiresExperience = animal.isRequiresExperiencedOwner();

        if (requiresExperience) {
            // Animal requires experienced owner
            switch (experience) {
                case "expert":
                    return 100;
                case "experienced":
                    return 85;
                case "some_experience":
                    return 50;
                case "first_time":
                    return 20;
                default:
                    return 50;
            }
        } else {
            // Animal is good for any experience level
            switch (experience) {
                case "first_time":
                    return 100; // Perfect for beginners
                case "some_experience":
                    return 95;
                case "experienced":
                    return 90;
                case "expert":
                    return 85;
                default:
                    return 80;
            }
        }
    }

    /**
     * Calculate home environment score.
     */
    private double calculateHomeScore(AdopterProfile profile, Animal animal) {
        double score = 100.0;

        // Check yard requirement
        if (animal.isRequiresYard() && !profile.isHasYard()) {
            score -= 40; // Major penalty
        }

        // Check home size vs animal size
        String homeSize = profile.getHomeSize();
        String animalSize = animal.getSize();

        if (homeSize != null && animalSize != null) {
            homeSize = homeSize.toLowerCase();
            animalSize = animalSize.toLowerCase();

            if ("extra_large".equals(animalSize) || "large".equals(animalSize)) {
                if ("small".equals(homeSize)) {
                    score -= 30;
                } else if ("medium".equals(homeSize)) {
                    score -= 10;
                }
            }

            if ("small".equals(homeSize) && "apartment".equals(profile.getHomeType())) {
                if ("extra_large".equals(animalSize)) {
                    score -= 40;
                }
            }
        }

        // Bonus for house with yard
        if (profile.isHasYard() && "house".equals(profile.getHomeType())) {
            score += 10;
        }

        return Math.max(0, Math.min(100, score));
    }

    /**
     * Calculate activity level match score.
     */
    private double calculateActivityScore(AdopterProfile profile, Animal animal) {
        String adopterActivity = profile.getActivityLevel();
        String animalEnergy = animal.getEnergyLevel();

        if (adopterActivity == null || animalEnergy == null) {
            return 70; // Neutral score
        }

        adopterActivity = adopterActivity.toLowerCase();
        animalEnergy = animalEnergy.toLowerCase();

        // Perfect matches
        if (adopterActivity.equals(animalEnergy)) {
            return 100;
        }

        // Compatible matches
        Map<String, String[]> compatibleMap = new HashMap<>();
        compatibleMap.put("very_active", new String[]{"high", "medium"});
        compatibleMap.put("active", new String[]{"high", "medium", "low"});
        compatibleMap.put("moderate", new String[]{"medium", "low"});
        compatibleMap.put("low", new String[]{"low", "medium"});

        String[] compatibleLevels = compatibleMap.get(adopterActivity);
        if (compatibleLevels != null) {
            for (String level : compatibleLevels) {
                if (level.equals(animalEnergy)) {
                    return 80; // Good match
                }
            }
        }

        // Mismatched activity levels
        if ("very_active".equals(adopterActivity) && "low".equals(animalEnergy)) {
            return 50; // May work but not ideal
        }

        if ("low".equals(adopterActivity) && "high".equals(animalEnergy)) {
            return 30; // Poor match
        }

        return 60; // Default for other combinations
    }
}
