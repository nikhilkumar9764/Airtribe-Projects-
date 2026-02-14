package com.airtribe.meditrack.pattern;

import com.airtribe.meditrack.entity.Appointment;

/**
 * Observer interface for appointment notifications.
 * Demonstrates Observer design pattern.
 */
public interface AppointmentObserver {
    void onAppointmentCreated(Appointment appointment);
    void onAppointmentCancelled(Appointment appointment);
    void onAppointmentConfirmed(Appointment appointment);
}

