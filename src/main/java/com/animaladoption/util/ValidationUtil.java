package com.animaladoption.util;

import java.util.regex.Pattern;

/**
 * Utility class for input validation.
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^[0-9]{3}-[0-9]{4}$|^[0-9]{3}-[0-9]{3}-[0-9]{4}$|^\\([0-9]{3}\\)\\s?[0-9]{3}-[0-9]{4}$"
    );

    private static final Pattern ZIP_CODE_PATTERN = Pattern.compile(
            "^[0-9]{5}(-[0-9]{4})?$"
    );

    /**
     * Validate email address format.
     *
     * @param email Email address to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validate phone number format.
     * Accepts: 555-1234, 555-555-1234, (555) 555-1234
     *
     * @param phone Phone number to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return true; // Phone is optional in most cases
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    /**
     * Validate ZIP code format.
     * Accepts: 12345, 12345-6789
     *
     * @param zipCode ZIP code to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidZipCode(String zipCode) {
        if (zipCode == null || zipCode.trim().isEmpty()) {
            return false;
        }
        return ZIP_CODE_PATTERN.matcher(zipCode.trim()).matches();
    }

    /**
     * Check if a string is null or empty.
     *
     * @param str String to check
     * @return true if null or empty, false otherwise
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Check if a string is not null and not empty.
     *
     * @param str String to check
     * @return true if not null and not empty, false otherwise
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * Sanitize a string for safe display (prevent XSS).
     *
     * @param input Input string
     * @return Sanitized string
     */
    public static String sanitize(String input) {
        if (input == null) {
            return "";
        }
        return input.replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;")
                .replace("/", "&#x2F;");
    }

    /**
     * Validate integer within range.
     *
     * @param value Value to check
     * @param min   Minimum value (inclusive)
     * @param max   Maximum value (inclusive)
     * @return true if within range, false otherwise
     */
    public static boolean isInRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    /**
     * Parse integer safely.
     *
     * @param str          String to parse
     * @param defaultValue Default value if parsing fails
     * @return Parsed integer or default value
     */
    public static int parseInt(String str, int defaultValue) {
        if (isEmpty(str)) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Parse double safely.
     *
     * @param str          String to parse
     * @param defaultValue Default value if parsing fails
     * @return Parsed double or default value
     */
    public static double parseDouble(String str, double defaultValue) {
        if (isEmpty(str)) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(str.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
