package com.animaladoption.model;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * MatchScore entity class for recommendation system.
 */
public class MatchScore {
    private int scoreId;
    private int adopterId;
    private int animalId;
    private double matchScore;
    private Map<String, Double> scoreDetails;
    private Timestamp calculatedAt;

    // Additional fields for joined data
    private Animal animal;

    public MatchScore() {
        this.scoreDetails = new HashMap<>();
    }

    public MatchScore(int adopterId, int animalId, double matchScore) {
        this.adopterId = adopterId;
        this.animalId = animalId;
        this.matchScore = matchScore;
        this.scoreDetails = new HashMap<>();
    }

    // Getters and Setters
    public int getScoreId() {
        return scoreId;
    }

    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }

    public int getAdopterId() {
        return adopterId;
    }

    public void setAdopterId(int adopterId) {
        this.adopterId = adopterId;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(double matchScore) {
        this.matchScore = matchScore;
    }

    public Map<String, Double> getScoreDetails() {
        return scoreDetails;
    }

    public void setScoreDetails(Map<String, Double> scoreDetails) {
        this.scoreDetails = scoreDetails;
    }

    public Timestamp getCalculatedAt() {
        return calculatedAt;
    }

    public void setCalculatedAt(Timestamp calculatedAt) {
        this.calculatedAt = calculatedAt;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    // Utility methods
    public int getMatchPercentage() {
        return (int) Math.round(matchScore);
    }

    public String getMatchLevel() {
        if (matchScore >= 90) return "Excellent Match";
        if (matchScore >= 75) return "Great Match";
        if (matchScore >= 60) return "Good Match";
        if (matchScore >= 45) return "Fair Match";
        return "Poor Match";
    }
}
