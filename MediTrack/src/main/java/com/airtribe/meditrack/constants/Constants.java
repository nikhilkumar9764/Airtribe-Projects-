package com.airtribe.meditrack.constants;

/**
 * Constants class for application-wide constants.
 * Demonstrates static final fields.
 */
public class Constants {
    // Tax rate
    public static final double TAX_RATE = 0.18; // 18% GST

    // File paths
    public static final String PATIENTS_FILE = "data/patients.csv";
    public static final String DOCTORS_FILE = "data/doctors.csv";
    public static final String APPOINTMENTS_FILE = "data/appointments.csv";
    public static final String BILLS_FILE = "data/bills.csv";

    // ID prefixes
    public static final String PATIENT_ID_PREFIX = "PAT";
    public static final String DOCTOR_ID_PREFIX = "DOC";
    public static final String APPOINTMENT_ID_PREFIX = "APT";
    public static final String BILL_ID_PREFIX = "BL";

    // Validation constants
    public static final int MIN_AGE = 0;
    public static final int MAX_AGE = 150;
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 100;

    // Static block for initialization
    static {
        System.out.println("Constants class initialized");
    }

    // Private constructor to prevent instantiation
    private Constants() {
        throw new AssertionError("Cannot instantiate Constants class");
    }
}

