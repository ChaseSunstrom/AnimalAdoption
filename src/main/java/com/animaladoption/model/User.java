package com.animaladoption.model;

import java.sql.Timestamp;

/**
 * User entity class representing users table.
 */
public class User {
    private int userId;
    private String email;
    private String passwordHash;
    private String userType; // adopter, shelter, admin
    private String firstName;
    private String lastName;
    private String phone;
    private Timestamp createdAt;
    private Timestamp lastLogin;
    private boolean isActive;

    // Constructors
    public User() {
    }

    public User(int userId, String email, String userType, String firstName, String lastName) {
        this.userId = userId;
        this.email = email;
        this.userType = userType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = true;
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    // Utility methods
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public boolean isAdopter() {
        return "adopter".equals(userType);
    }

    public boolean isShelter() {
        return "shelter".equals(userType);
    }

    public boolean isAdmin() {
        return "admin".equals(userType);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", email='" + email + '\'' +
                ", userType='" + userType + '\'' +
                ", fullName='" + getFullName() + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
