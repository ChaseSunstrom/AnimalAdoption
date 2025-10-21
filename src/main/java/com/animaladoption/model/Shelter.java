package com.animaladoption.model;

/**
 * Shelter entity class representing shelters table.
 */
public class Shelter {
    private int shelterId;
    private int userId;
    private String shelterName;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;
    private String phone;
    private String email;
    private String website;
    private String description;
    private boolean verified;

    // Constructors
    public Shelter() {
    }

    public Shelter(int userId, String shelterName) {
        this.userId = userId;
        this.shelterName = shelterName;
        this.verified = false;
    }

    // Getters and Setters
    public int getShelterId() {
        return shelterId;
    }

    public void setShelterId(int shelterId) {
        this.shelterId = shelterId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getShelterName() {
        return shelterName;
    }

    public void setShelterName(String shelterName) {
        this.shelterName = shelterName;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
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

    @Override
    public String toString() {
        return "Shelter{" +
                "shelterId=" + shelterId +
                ", shelterName='" + shelterName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", verified=" + verified +
                '}';
    }
}
