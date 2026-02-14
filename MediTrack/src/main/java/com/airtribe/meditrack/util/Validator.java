package com.airtribe.meditrack.util;

import com.airtribe.meditrack.constants.Constants;
import java.util.regex.Pattern;

/**
 * Centralized validation utility class.
 * Demonstrates static methods and utility class pattern.
 */
public class Validator {
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[(]?[0-9]{1,4}[)]?[-\\s.]?[(]?[0-9]{1,4}[)]?[-\\s.]?[0-9]{1,9}$"
    );
    private static final Pattern ID_PATTERN = Pattern.compile("^[A-Z]{3}\\d+$");
    private static final Pattern BLOOD_GROUP_PATTERN = Pattern.compile(
        "^(A|B|AB|O)[+-]$"
    );

    // Private constructor to prevent instantiation
    private Validator() {
        throw new AssertionError("Cannot instantiate Validator class");
    }

    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        int length = name.trim().length();
        return length >= Constants.MIN_NAME_LENGTH && length <= Constants.MAX_NAME_LENGTH;
    }

    public static boolean isValidAge(int age) {
        return age >= Constants.MIN_AGE && age <= Constants.MAX_AGE;
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidId(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return ID_PATTERN.matcher(id.trim()).matches();
    }

    public static boolean isValidSpecialization(String specialization) {
        return specialization != null && !specialization.trim().isEmpty();
    }

    public static boolean isValidBloodGroup(String bloodGroup) {
        if (bloodGroup == null || bloodGroup.trim().isEmpty()) {
            return false;
        }
        return BLOOD_GROUP_PATTERN.matcher(bloodGroup.trim().toUpperCase()).matches();
    }

    public static boolean isValidAmount(double amount) {
        return amount >= 0;
    }
}

