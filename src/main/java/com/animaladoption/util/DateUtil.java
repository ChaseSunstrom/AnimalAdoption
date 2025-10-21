package com.animaladoption.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;

/**
 * Utility class for date formatting and calculations.
 */
public class DateUtil {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat DISPLAY_FORMAT = new SimpleDateFormat("MMM dd, yyyy");

    /**
     * Format a Date object to string (yyyy-MM-dd).
     *
     * @param date Date to format
     * @return Formatted date string
     */
    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return DATE_FORMAT.format(date);
    }

    /**
     * Format a Date object to display format (MMM dd, yyyy).
     *
     * @param date Date to format
     * @return Formatted date string
     */
    public static String formatDisplayDate(Date date) {
        if (date == null) {
            return "";
        }
        return DISPLAY_FORMAT.format(date);
    }

    /**
     * Format a Timestamp to datetime string.
     *
     * @param timestamp Timestamp to format
     * @return Formatted datetime string
     */
    public static String formatDateTime(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        return DATETIME_FORMAT.format(timestamp);
    }

    /**
     * Calculate age from date of birth.
     *
     * @param birthDate Date of birth
     * @return Age in years
     */
    public static int calculateAge(Date birthDate) {
        if (birthDate == null) {
            return 0;
        }
        LocalDate birth = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate now = LocalDate.now();
        return Period.between(birth, now).getYears();
    }

    /**
     * Get current date as java.sql.Date.
     *
     * @return Current date
     */
    public static java.sql.Date getCurrentSqlDate() {
        return new java.sql.Date(System.currentTimeMillis());
    }

    /**
     * Get current timestamp.
     *
     * @return Current timestamp
     */
    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * Convert java.util.Date to java.sql.Date.
     *
     * @param date java.util.Date
     * @return java.sql.Date
     */
    public static java.sql.Date toSqlDate(Date date) {
        if (date == null) {
            return null;
        }
        return new java.sql.Date(date.getTime());
    }

    /**
     * Format age in human-readable form (e.g., "2 years 3 months").
     *
     * @param years  Years
     * @param months Months
     * @return Formatted age string
     */
    public static String formatAge(int years, int months) {
        if (years == 0 && months == 0) {
            return "Less than 1 month";
        }

        StringBuilder age = new StringBuilder();
        if (years > 0) {
            age.append(years).append(years == 1 ? " year" : " years");
        }
        if (months > 0) {
            if (years > 0) {
                age.append(" ");
            }
            age.append(months).append(months == 1 ? " month" : " months");
        }
        return age.toString();
    }

    /**
     * Get relative time string (e.g., "2 days ago", "3 hours ago").
     *
     * @param timestamp Past timestamp
     * @return Relative time string
     */
    public static String getRelativeTime(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }

        long diff = System.currentTimeMillis() - timestamp.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (days > 0) {
            return days + (days == 1 ? " day ago" : " days ago");
        } else if (hours > 0) {
            return hours + (hours == 1 ? " hour ago" : " hours ago");
        } else if (minutes > 0) {
            return minutes + (minutes == 1 ? " minute ago" : " minutes ago");
        } else {
            return "Just now";
        }
    }
}
