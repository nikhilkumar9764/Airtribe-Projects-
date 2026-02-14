package com.airtribe.meditrack.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for date operations.
 * Demonstrates static utility methods.
 */
public class DateUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

    // Private constructor to prevent instantiation
    private DateUtil() {
        throw new AssertionError("Cannot instantiate DateUtil class");
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "N/A";
        }
        return dateTime.format(DISPLAY_FORMATTER);
    }

    public static String formatDateTimeForCSV(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(FORMATTER);
    }

    public static LocalDateTime parseDateTime(String dateTimeString) throws DateTimeParseException {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(dateTimeString.trim(), FORMATTER);
    }

    public static boolean isFutureDateTime(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isAfter(LocalDateTime.now());
    }

    public static boolean isPastDateTime(LocalDateTime dateTime) {
        return dateTime != null && dateTime.isBefore(LocalDateTime.now());
    }
}

