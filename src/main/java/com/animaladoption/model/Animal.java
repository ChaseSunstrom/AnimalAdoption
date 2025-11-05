package com.animaladoption.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Animal entity class representing animals table.
 */
public class Animal {
    private int animalId;
    private int shelterId;
    private String name;
    private String species;
    private String breed;
    private int ageYears;
    private int ageMonths;
    private String gender;
    private String size;
    private String color;
    private double weightLbs;
    private String description;
    private String medicalNotes;
    private String behavioralNotes;
    private boolean goodWithKids;
    private boolean goodWithDogs;
    private boolean goodWithCats;
    private boolean requiresExperiencedOwner;
    private boolean requiresYard;
    private String energyLevel;
    private String status;
    private Date intakeDate;
    private Date adoptionDate;
    private String primaryImageUrl;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Additional field for joined data
    private Shelter shelter;

    // Constructors
    public Animal() {
        this.status = "available";
    }

    public Animal(int shelterId, String name, String species) {
        this.shelterId = shelterId;
        this.name = name;
        this.species = species;
        this.status = "available";
    }

    // Getters and Setters
    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public int getShelterId() {
        return shelterId;
    }

    public void setShelterId(int shelterId) {
        this.shelterId = shelterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public int getAgeYears() {
        return ageYears;
    }

    public void setAgeYears(int ageYears) {
        this.ageYears = ageYears;
    }

    public int getAgeMonths() {
        return ageMonths;
    }

    public void setAgeMonths(int ageMonths) {
        this.ageMonths = ageMonths;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getWeightLbs() {
        return weightLbs;
    }

    public void setWeightLbs(double weightLbs) {
        this.weightLbs = weightLbs;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMedicalNotes() {
        return medicalNotes;
    }

    public void setMedicalNotes(String medicalNotes) {
        this.medicalNotes = medicalNotes;
    }

    public String getBehavioralNotes() {
        return behavioralNotes;
    }

    public void setBehavioralNotes(String behavioralNotes) {
        this.behavioralNotes = behavioralNotes;
    }

    public boolean isGoodWithKids() {
        return goodWithKids;
    }

    public void setGoodWithKids(boolean goodWithKids) {
        this.goodWithKids = goodWithKids;
    }

    public boolean isGoodWithDogs() {
        return goodWithDogs;
    }

    public void setGoodWithDogs(boolean goodWithDogs) {
        this.goodWithDogs = goodWithDogs;
    }

    public boolean isGoodWithCats() {
        return goodWithCats;
    }

    public void setGoodWithCats(boolean goodWithCats) {
        this.goodWithCats = goodWithCats;
    }

    public boolean isRequiresExperiencedOwner() {
        return requiresExperiencedOwner;
    }

    public void setRequiresExperiencedOwner(boolean requiresExperiencedOwner) {
        this.requiresExperiencedOwner = requiresExperiencedOwner;
    }

    public boolean isRequiresYard() {
        return requiresYard;
    }

    public void setRequiresYard(boolean requiresYard) {
        this.requiresYard = requiresYard;
    }

    public String getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(String energyLevel) {
        this.energyLevel = energyLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getIntakeDate() {
        return intakeDate;
    }

    public void setIntakeDate(Date intakeDate) {
        this.intakeDate = intakeDate;
    }

    public Date getAdoptionDate() {
        return adoptionDate;
    }

    public void setAdoptionDate(Date adoptionDate) {
        this.adoptionDate = adoptionDate;
    }

    public String getPrimaryImageUrl() {
        return primaryImageUrl;
    }

    public void setPrimaryImageUrl(String primaryImageUrl) {
        this.primaryImageUrl = primaryImageUrl;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Shelter getShelter() {
        return shelter;
    }

    public void setShelter(Shelter shelter) {
        this.shelter = shelter;
    }

    // Utility methods
    public String getFormattedAge() {
        if (ageYears == 0 && ageMonths == 0) {
            return "Less than 1 month";
        }
        StringBuilder age = new StringBuilder();
        if (ageYears > 0) {
            age.append(ageYears).append(ageYears == 1 ? " year" : " years");
        }
        if (ageMonths > 0) {
            if (ageYears > 0) age.append(" ");
            age.append(ageMonths).append(ageMonths == 1 ? " month" : " months");
        }
        return age.toString();
    }

    public boolean isAvailable() {
        return "available".equals(status);
    }

    public String getDisplayImage() {
        if (primaryImageUrl != null && !primaryImageUrl.isEmpty()) {
            return primaryImageUrl;
        }
        // Default placeholder image based on species
        return "/images/animals/default-" + species.toLowerCase() + ".jpg";
    }

    @Override
    public String toString() {
        return "Animal{" +
                "animalId=" + animalId +
                ", name='" + name + '\'' +
                ", species='" + species + '\'' +
                ", breed='" + breed + '\'' +
                ", age='" + getFormattedAge() + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
