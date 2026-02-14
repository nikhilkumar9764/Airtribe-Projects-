package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Bill;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.enums.AppointmentStatus;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.util.DataStore;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service class for Appointment operations.
 * Demonstrates exception handling and business logic.
 */
public class AppointmentService {
    private final DataStore<Appointment> appointmentStore;
    private final DoctorService doctorService;
    private final PatientService patientService;

    public AppointmentService(DoctorService doctorService, PatientService patientService) {
        this.appointmentStore = new DataStore<>("Appointment");
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public Appointment createAppointment(String appointmentId, String doctorId, String patientId, 
                                         LocalDateTime appointmentDateTime) 
            throws AppointmentNotFoundException {
        // Validate doctor exists
        Doctor doctor = doctorService.getDoctor(doctorId);
        if (doctor == null) {
            throw new AppointmentNotFoundException("Doctor not found", doctorId);
        }

        // Validate patient exists
        Patient patient = patientService.getPatient(patientId);
        if (patient == null) {
            throw new AppointmentNotFoundException("Patient not found", patientId);
        }

        Appointment appointment = new Appointment(
            appointmentId, 
            doctorId, 
            patientId, 
            appointmentDateTime, 
            AppointmentStatus.PENDING
        );
        
        appointmentStore.add(appointmentId, appointment);
        return appointment;
    }

    public Appointment getAppointment(String appointmentId) throws AppointmentNotFoundException {
        Appointment appointment = appointmentStore.get(appointmentId);
        if (appointment == null) {
            throw new AppointmentNotFoundException("Appointment not found", appointmentId);
        }
        return appointment;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentStore.getAll();
    }

    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return appointmentStore.filter(appointment -> 
            appointment.getPatientId().equals(patientId)
        );
    }

    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return appointmentStore.filter(appointment -> 
            appointment.getDoctorId().equals(doctorId)
        );
    }

    public void cancelAppointment(String appointmentId) throws AppointmentNotFoundException {
        Appointment appointment = getAppointment(appointmentId);
        appointment.setStatus(AppointmentStatus.CANCELLED);
    }

    public void confirmAppointment(String appointmentId) throws AppointmentNotFoundException {
        Appointment appointment = getAppointment(appointmentId);
        appointment.setStatus(AppointmentStatus.CONFIRMED);
    }

    public void completeAppointment(String appointmentId) throws AppointmentNotFoundException {
        Appointment appointment = getAppointment(appointmentId);
        appointment.setStatus(AppointmentStatus.COMPLETED);
    }

    /**
     * Generate bill for an appointment
     * Demonstrates polymorphism through method overriding
     */
    public Bill generateBill(String appointmentId) throws AppointmentNotFoundException {
        Appointment appointment = getAppointment(appointmentId);
        
        if (appointment.getStatus() != AppointmentStatus.COMPLETED) {
            throw new IllegalStateException("Cannot generate bill for non-completed appointment");
        }

        Doctor doctor = doctorService.getDoctor(appointment.getDoctorId());
        if (doctor == null) {
            throw new AppointmentNotFoundException("Doctor not found for appointment", appointmentId);
        }

        String billId = "BL" + appointmentId.substring(3); // Generate bill ID from appointment ID
        Bill bill = new Bill(billId, appointmentId, appointment.getPatientId(), doctor.getConsultationFee());
        
        return bill;
    }

    /**
     * Get analytics - appointments per doctor using streams
     */
    public long getAppointmentCountByDoctor(String doctorId) {
        return getAllAppointments().stream()
            .filter(appointment -> appointment.getDoctorId().equals(doctorId))
            .count();
    }

    public int getAppointmentCount() {
        return appointmentStore.size();
    }
}

