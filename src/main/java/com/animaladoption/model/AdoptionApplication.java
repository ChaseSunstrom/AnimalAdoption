package com.animaladoption.model;

import java.sql.Timestamp;

/**
 * AdoptionApplication entity class.
 */
public class AdoptionApplication {
    private int applicationId;
    private int animalId;
    private int adopterId;
    private int shelterId;
    private String status;
    private String whyAdopt;
    private String previousPetExperience;
    private String veterinarianName;
    private String veterinarianPhone;
    private String reference1Name;
    private String reference1Phone;
    private String reference1Relationship;
    private String reference2Name;
    private String reference2Phone;
    private String reference2Relationship;
    private String shelterNotes;
    private Timestamp submittedAt;
    private Timestamp reviewedAt;
    private Integer reviewedBy;

    // Additional fields for joined data
    private Animal animal;
    private User adopter;
    private Shelter shelter;

    public AdoptionApplication() {
        this.status = "submitted";
    }

    // Getters and Setters
    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public int getAdopterId() {
        return adopterId;
    }

    public void setAdopterId(int adopterId) {
        this.adopterId = adopterId;
    }

    public int getShelterId() {
        return shelterId;
    }

    public void setShelterId(int shelterId) {
        this.shelterId = shelterId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWhyAdopt() {
        return whyAdopt;
    }

    public void setWhyAdopt(String whyAdopt) {
        this.whyAdopt = whyAdopt;
    }

    public String getPreviousPetExperience() {
        return previousPetExperience;
    }

    public void setPreviousPetExperience(String previousPetExperience) {
        this.previousPetExperience = previousPetExperience;
    }

    public String getVeterinarianName() {
        return veterinarianName;
    }

    public void setVeterinarianName(String veterinarianName) {
        this.veterinarianName = veterinarianName;
    }

    public String getVeterinarianPhone() {
        return veterinarianPhone;
    }

    public void setVeterinarianPhone(String veterinarianPhone) {
        this.veterinarianPhone = veterinarianPhone;
    }

    public String getReference1Name() {
        return reference1Name;
    }

    public void setReference1Name(String reference1Name) {
        this.reference1Name = reference1Name;
    }

    public String getReference1Phone() {
        return reference1Phone;
    }

    public void setReference1Phone(String reference1Phone) {
        this.reference1Phone = reference1Phone;
    }

    public String getReference1Relationship() {
        return reference1Relationship;
    }

    public void setReference1Relationship(String reference1Relationship) {
        this.reference1Relationship = reference1Relationship;
    }

    public String getReference2Name() {
        return reference2Name;
    }

    public void setReference2Name(String reference2Name) {
        this.reference2Name = reference2Name;
    }

    public String getReference2Phone() {
        return reference2Phone;
    }

    public void setReference2Phone(String reference2Phone) {
        this.reference2Phone = reference2Phone;
    }

    public String getReference2Relationship() {
        return reference2Relationship;
    }

    public void setReference2Relationship(String reference2Relationship) {
        this.reference2Relationship = reference2Relationship;
    }

    public String getShelterNotes() {
        return shelterNotes;
    }

    public void setShelterNotes(String shelterNotes) {
        this.shelterNotes = shelterNotes;
    }

    public Timestamp getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(Timestamp submittedAt) {
        this.submittedAt = submittedAt;
    }

    public Timestamp getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Timestamp reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public Integer getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Integer reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public User getAdopter() {
        return adopter;
    }

    public void setAdopter(User adopter) {
        this.adopter = adopter;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }
}
