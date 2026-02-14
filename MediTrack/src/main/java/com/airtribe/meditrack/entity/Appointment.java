package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.enums.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Appointment entity linking Doctor and Patient.
 * Implements Cloneable for deep copying demonstration.
 */
public class Appointment implements Cloneable {
    private String appointmentId;
    private String doctorId;
    private String patientId;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private String notes;

    public Appointment(String appointmentId, String doctorId, String patientId, 
                       LocalDateTime appointmentDateTime, AppointmentStatus status) {
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.appointmentDateTime = appointmentDateTime;
        this.status = status;
        this.notes = "";
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Deep clone implementation
     */
    @Override
    public Appointment clone() {
        try {
            Appointment cloned = (Appointment) super.clone();
            // LocalDateTime is immutable, so no need to clone it
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported", e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Appointment that = (Appointment) obj;
        return Objects.equals(appointmentId, that.appointmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentId);
    }

    @Override
    public String toString() {
        return String.format("Appointment{id='%s', doctorId='%s', patientId='%s', dateTime=%s, status=%s}", 
            appointmentId, doctorId, patientId, appointmentDateTime, status);
    }
}

