package com.airtribe.meditrack.pattern;

import com.airtribe.meditrack.entity.Appointment;
import java.util.ArrayList;
import java.util.List;

/**
 * Subject class for Observer pattern.
 * Manages observers and notifies them of appointment changes.
 */
public class AppointmentNotifier {
    private final List<AppointmentObserver> observers;

    public AppointmentNotifier() {
        this.observers = new ArrayList<>();
    }

    public void addObserver(AppointmentObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(AppointmentObserver observer) {
        observers.remove(observer);
    }

    public void notifyAppointmentCreated(Appointment appointment) {
        for (AppointmentObserver observer : observers) {
            observer.onAppointmentCreated(appointment);
        }
    }

    public void notifyAppointmentCancelled(Appointment appointment) {
        for (AppointmentObserver observer : observers) {
            observer.onAppointmentCancelled(appointment);
        }
    }

    public void notifyAppointmentConfirmed(Appointment appointment) {
        for (AppointmentObserver observer : observers) {
            observer.onAppointmentConfirmed(appointment);
        }
    }
}

