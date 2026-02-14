package com.airtribe.meditrack.pattern;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.util.DateUtil;

/**
 * Console-based observer implementation.
 * Prints appointment notifications to console.
 */
public class ConsoleAppointmentObserver implements AppointmentObserver {
    @Override
    public void onAppointmentCreated(Appointment appointment) {
        System.out.println("\n[NOTIFICATION] New appointment created:");
        System.out.println("  Appointment ID: " + appointment.getAppointmentId());
        System.out.println("  Date/Time: " + DateUtil.formatDateTime(appointment.getAppointmentDateTime()));
        System.out.println("  Status: " + appointment.getStatus());
    }

    @Override
    public void onAppointmentCancelled(Appointment appointment) {
        System.out.println("\n[NOTIFICATION] Appointment cancelled:");
        System.out.println("  Appointment ID: " + appointment.getAppointmentId());
        System.out.println("  Date/Time: " + DateUtil.formatDateTime(appointment.getAppointmentDateTime()));
    }

    @Override
    public void onAppointmentConfirmed(Appointment appointment) {
        System.out.println("\n[NOTIFICATION] Appointment confirmed:");
        System.out.println("  Appointment ID: " + appointment.getAppointmentId());
        System.out.println("  Date/Time: " + DateUtil.formatDateTime(appointment.getAppointmentDateTime()));
        System.out.println("  Reminder: Please arrive 15 minutes before your appointment time.");
    }
}

