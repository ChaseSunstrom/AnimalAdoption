package com.animaladoption.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for password hashing and verification using BCrypt.
 */
public class PasswordUtil {

    // Number of salt rounds for BCrypt (12 is a good balance of security and performance)
    private static final int SALT_ROUNDS = 12;

    /**
     * Hash a plain text password using BCrypt.
     *
     * @param plainPassword The plain text password
     * @return The hashed password
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(SALT_ROUNDS));
    }

    /**
     * Verify a plain text password against a hashed password.
     *
     * @param plainPassword  The plain text password to verify
     * @param hashedPassword The hashed password to compare against
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (IllegalArgumentException e) {
            // Invalid hash format
            return false;
        }
    }

    /**
     * Check if a password meets minimum strength requirements.
     *
     * @param password The password to check
     * @return true if password meets requirements, false otherwise
     */
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        // Check for at least one digit, one letter
        boolean hasDigit = password.matches(".*\\d.*");
        boolean hasLetter = password.matches(".*[a-zA-Z].*");

        return hasDigit && hasLetter;
    }

    /**
     * Get password strength requirements message.
     *
     * @return Requirements message
     */
    public static String getPasswordRequirements() {
        return "Password must be at least 8 characters long and contain both letters and numbers.";
    }
}
