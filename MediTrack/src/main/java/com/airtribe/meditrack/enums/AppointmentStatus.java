package com.airtribe.meditrack.enums;

/**
 * Enum for Appointment status.
 * Demonstrates enum usage instead of string constants.
 */
public enum AppointmentStatus {
    CONFIRMED("Confirmed"),
    PENDING("Pending"),
    CANCELLED("Cancelled"),
    COMPLETED("Completed");

    private final String displayName;

    AppointmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

