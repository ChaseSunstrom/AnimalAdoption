package com.animaladoption.model;

import java.util.ArrayList;
import java.util.List;

/**
 * SearchCriteria class for animal search filters.
 */
public class SearchCriteria {
    private String species;
    private String breed;
    private String size;
    private String color;
    private Integer minAge;
    private Integer maxAge;
    private String gender;
    private Boolean goodWithKids;
    private Boolean goodWithDogs;
    private Boolean goodWithCats;
    private List<String> energyLevels;
    private String city;
    private String state;
    private String zipCode;
    private Integer radiusMiles;

    public SearchCriteria() {
        this.energyLevels = new ArrayList<>();
    }

    // Getters and Setters
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

    public Integer getMinAge() {
        return minAge;
    }

    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }

    public Integer getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge) {
        this.maxAge = maxAge;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Boolean getGoodWithKids() {
        return goodWithKids;
    }

    public void setGoodWithKids(Boolean goodWithKids) {
        this.goodWithKids = goodWithKids;
    }

    public Boolean getGoodWithDogs() {
        return goodWithDogs;
    }

    public void setGoodWithDogs(Boolean goodWithDogs) {
        this.goodWithDogs = goodWithDogs;
    }

    public Boolean getGoodWithCats() {
        return goodWithCats;
    }

    public void setGoodWithCats(Boolean goodWithCats) {
        this.goodWithCats = goodWithCats;
    }

    public List<String> getEnergyLevels() {
        return energyLevels;
    }

    public void setEnergyLevels(List<String> energyLevels) {
        this.energyLevels = energyLevels;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Integer getRadiusMiles() {
        return radiusMiles;
    }

    public void setRadiusMiles(Integer radiusMiles) {
        this.radiusMiles = radiusMiles;
    }

    public boolean hasFilters() {
        return species != null || breed != null || size != null ||
               color != null || gender != null || goodWithKids != null ||
               goodWithDogs != null || goodWithCats != null ||
               (energyLevels != null && !energyLevels.isEmpty()) ||
               city != null || state != null;
    }

    public void setMinAgeYears(int minAge) {
        this.minAge = minAge;
    }

    public void setMaxAgeYears(int maxAge) {
        this.maxAge = maxAge;
    }
}
