package com.animaladoption.model;

/**
 * AdopterProfile entity class representing adopter_profiles table.
 */
public class AdopterProfile {
    private int profileId;
    private int userId;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;
    private String homeType;
    private String homeSize;
    private boolean hasYard;
    private boolean hasChildren;
    private String childrenAges;
    private boolean hasOtherPets;
    private String otherPetsDescription;
    private String experienceLevel;
    private String activityLevel;
    private String preferencesNotes;

    // Constructors
    public AdopterProfile() {
    }

    public AdopterProfile(int userId) {
        this.userId = userId;
    }

    // Getters and Setters
    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
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

    public String getHomeType() {
        return homeType;
    }

    public void setHomeType(String homeType) {
        this.homeType = homeType;
    }

    public String getHomeSize() {
        return homeSize;
    }

    public void setHomeSize(String homeSize) {
        this.homeSize = homeSize;
    }

    public boolean hasYard() {
        return hasYard;
    }

    public void setHasYard(boolean hasYard) {
        this.hasYard = hasYard;
    }

    public boolean hasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public String getChildrenAges() {
        return childrenAges;
    }

    public void setChildrenAges(String childrenAges) {
        this.childrenAges = childrenAges;
    }

    public boolean hasOtherPets() {
        return hasOtherPets;
    }

    public void setHasOtherPets(boolean hasOtherPets) {
        this.hasOtherPets = hasOtherPets;
    }

    public String getOtherPetsDescription() {
        return otherPetsDescription;
    }

    public void setOtherPetsDescription(String otherPetsDescription) {
        this.otherPetsDescription = otherPetsDescription;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public String getActivityLevel() {
        return activityLevel;
    }

    public void setActivityLevel(String activityLevel) {
        this.activityLevel = activityLevel;
    }

    public String getPreferencesNotes() {
        return preferencesNotes;
    }

    public void setPreferencesNotes(String preferencesNotes) {
        this.preferencesNotes = preferencesNotes;
    }

    // Utility methods
    public String getFullAddress() {
        StringBuilder address = new StringBuilder();
        if (addressLine1 != null) address.append(addressLine1);
        if (addressLine2 != null && !addressLine2.trim().isEmpty()) {
            address.append(", ").append(addressLine2);
        }
        if (city != null) address.append(", ").append(city);
        if (state != null) address.append(", ").append(state);
        if (zipCode != null) address.append(" ").append(zipCode);
        return address.toString();
    }

    public boolean hasPets(String petType) {
        if (!hasOtherPets || otherPetsDescription == null) {
            return false;
        }
        return otherPetsDescription.toLowerCase().contains(petType.toLowerCase());
    }

    @Override
    public String toString() {
        return "AdopterProfile{" +
                "profileId=" + profileId +
                ", userId=" + userId +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", homeType='" + homeType + '\'' +
                ", homeSize='" + homeSize + '\'' +
                ", experienceLevel='" + experienceLevel + '\'' +
                ", activityLevel='" + activityLevel + '\'' +
                '}';
    }
}
