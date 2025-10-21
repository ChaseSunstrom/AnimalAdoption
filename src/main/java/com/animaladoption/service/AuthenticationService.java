package com.animaladoption.service;

import com.animaladoption.dao.UserDAO;
import com.animaladoption.dao.AdopterProfileDAO;
import com.animaladoption.dao.ShelterDAO;
import com.animaladoption.model.User;
import com.animaladoption.model.AdopterProfile;
import com.animaladoption.model.Shelter;
import com.animaladoption.util.PasswordUtil;
import com.animaladoption.util.ValidationUtil;

import java.sql.SQLException;

/**
 * Service class for authentication and user management.
 */
public class AuthenticationService {
    private final UserDAO userDAO;
    private final AdopterProfileDAO adopterProfileDAO;
    private final ShelterDAO shelterDAO;

    public AuthenticationService() {
        this.userDAO = new UserDAO();
        this.adopterProfileDAO = new AdopterProfileDAO();
        this.shelterDAO = new ShelterDAO();
    }

    /**
     * Authenticate a user with email and password.
     */
    public User login(String email, String password) throws SQLException {
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        User user = userDAO.findByEmail(email);
        if (user == null) {
            return null;
        }

        if (!user.isActive()) {
            throw new IllegalStateException("Account is inactive");
        }

        if (PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
            userDAO.updateLastLogin(user.getUserId());
            return user;
        }

        return null;
    }

    /**
     * Register a new adopter user.
     */
    public User registerAdopter(String email, String password, String firstName, String lastName, String phone)
            throws SQLException {

        // Validate inputs
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (!PasswordUtil.isPasswordStrong(password)) {
            throw new IllegalArgumentException(PasswordUtil.getPasswordRequirements());
        }

        if (ValidationUtil.isEmpty(firstName) || ValidationUtil.isEmpty(lastName)) {
            throw new IllegalArgumentException("First name and last name are required");
        }

        if (!ValidationUtil.isEmpty(phone) && !ValidationUtil.isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone number format");
        }

        // Check if email already exists
        if (userDAO.emailExists(email)) {
            throw new IllegalArgumentException("Email address already registered");
        }

        // Create user
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(PasswordUtil.hashPassword(password));
        user.setUserType("adopter");
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setActive(true);

        int userId = userDAO.create(user);
        if (userId > 0) {
            user.setUserId(userId);

            // Create empty adopter profile
            AdopterProfile profile = new AdopterProfile(userId);
            adopterProfileDAO.create(profile);

            return user;
        }

        throw new SQLException("Failed to create user account");
    }

    /**
     * Register a new shelter user.
     */
    public User registerShelter(String email, String password, String firstName, String lastName,
                                String phone, String shelterName, String city, String state)
            throws SQLException {

        // Validate inputs
        if (!ValidationUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (!PasswordUtil.isPasswordStrong(password)) {
            throw new IllegalArgumentException(PasswordUtil.getPasswordRequirements());
        }

        if (ValidationUtil.isEmpty(firstName) || ValidationUtil.isEmpty(lastName)) {
            throw new IllegalArgumentException("First name and last name are required");
        }

        if (ValidationUtil.isEmpty(shelterName)) {
            throw new IllegalArgumentException("Shelter name is required");
        }

        if (!ValidationUtil.isEmpty(phone) && !ValidationUtil.isValidPhone(phone)) {
            throw new IllegalArgumentException("Invalid phone number format");
        }

        // Check if email already exists
        if (userDAO.emailExists(email)) {
            throw new IllegalArgumentException("Email address already registered");
        }

        // Create user
        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(PasswordUtil.hashPassword(password));
        user.setUserType("shelter");
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setActive(true);

        int userId = userDAO.create(user);
        if (userId > 0) {
            user.setUserId(userId);

            // Create shelter profile
            Shelter shelter = new Shelter(userId, shelterName);
            shelter.setCity(city);
            shelter.setState(state);
            shelter.setEmail(email);
            shelter.setPhone(phone);
            shelter.setVerified(false); // Shelters need admin verification

            int shelterId = shelterDAO.create(shelter);
            if (shelterId <= 0) {
                // Rollback would be ideal here, but we'll throw an exception
                throw new SQLException("Failed to create shelter profile");
            }

            return user;
        }

        throw new SQLException("Failed to create user account");
    }

    /**
     * Change user password.
     */
    public boolean changePassword(int userId, String currentPassword, String newPassword)
            throws SQLException {

        User user = userDAO.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (!PasswordUtil.verifyPassword(currentPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        if (!PasswordUtil.isPasswordStrong(newPassword)) {
            throw new IllegalArgumentException(PasswordUtil.getPasswordRequirements());
        }

        user.setPasswordHash(PasswordUtil.hashPassword(newPassword));
        return userDAO.update(user);
    }

    /**
     * Update user profile information.
     */
    public boolean updateProfile(User user) throws SQLException {
        if (user == null || user.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user");
        }

        if (!ValidationUtil.isValidEmail(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (!ValidationUtil.isEmpty(user.getPhone()) && !ValidationUtil.isValidPhone(user.getPhone())) {
            throw new IllegalArgumentException("Invalid phone number format");
        }

        // Check if email is being changed to one that already exists
        User existingUser = userDAO.findById(user.getUserId());
        if (!existingUser.getEmail().equals(user.getEmail()) && userDAO.emailExists(user.getEmail())) {
            throw new IllegalArgumentException("Email address already in use");
        }

        return userDAO.update(user);
    }

    /**
     * Deactivate user account.
     */
    public boolean deactivateAccount(int userId) throws SQLException {
        User user = userDAO.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        user.setActive(false);
        return userDAO.update(user);
    }

    /**
     * Reactivate user account.
     */
    public boolean reactivateAccount(int userId) throws SQLException {
        User user = userDAO.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        user.setActive(true);
        return userDAO.update(user);
    }

    /**
     * Get user by ID.
     */
    public User getUserById(int userId) throws SQLException {
        return userDAO.findById(userId);
    }

    /**
     * Get user by email.
     */
    public User getUserByEmail(String email) throws SQLException {
        return userDAO.findByEmail(email);
    }
}
