package com.airtribe.meditrack.exception;

/**
 * Custom exception for appointment not found scenarios.
 * Demonstrates custom exception creation.
 */
public class AppointmentNotFoundException extends Exception {
    private String appointmentId;

    public AppointmentNotFoundException(String message) {
        super(message);
    }

    public AppointmentNotFoundException(String message, String appointmentId) {
        super(message);
        this.appointmentId = appointmentId;
    }

    public AppointmentNotFoundException(String message, Throwable cause) {
        super(message, cause); // Exception chaining
    }

    public String getAppointmentId() {
        return appointmentId;
    }
}

