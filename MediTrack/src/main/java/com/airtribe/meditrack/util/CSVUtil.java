package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.enums.AppointmentStatus;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for CSV file operations.
 * Demonstrates try-with-resources and file I/O.
 */
public class CSVUtil {
    // Private constructor to prevent instantiation
    private CSVUtil() {
        throw new AssertionError("Cannot instantiate CSVUtil class");
    }

    /**
     * Save patients to CSV file
     */
    public static void savePatients(List<Patient> patients, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("id,name,age,email,phone,medicalHistory,bloodGroup,allergies\n");
            for (Patient patient : patients) {
                writer.write(String.format("%s,%s,%d,%s,%s,%s,%s,%s\n",
                    patient.getId(),
                    patient.getName(),
                    patient.getAge(),
                    patient.getEmail(),
                    patient.getPhone(),
                    escapeCSV(patient.getMedicalHistory()),
                    patient.getBloodGroup(),
                    String.join(";", patient.getAllergies())
                ));
            }
        }
    }

    /**
     * Load patients from CSV file
     */
    public static List<Patient> loadPatients(String filePath) throws IOException {
        List<Patient> patients = new ArrayList<>();
        Path path = Paths.get(filePath);
        
        if (!Files.exists(path)) {
            return patients;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    Patient patient = new Patient(
                        parts[0].trim(),
                        parts[1].trim(),
                        Integer.parseInt(parts[2].trim()),
                        parts[3].trim(),
                        parts[4].trim(),
                        unescapeCSV(parts[5].trim()),
                        parts[6].trim()
                    );
                    if (parts.length > 7 && !parts[7].trim().isEmpty()) {
                        String[] allergies = parts[7].split(";");
                        for (String allergy : allergies) {
                            patient.addAllergy(allergy.trim());
                        }
                    }
                    patients.add(patient);
                }
            }
        }
        return patients;
    }

    /**
     * Save doctors to CSV file
     */
    public static void saveDoctors(List<Doctor> doctors, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("id,name,age,email,phone,specialization,consultationFee,yearsOfExperience\n");
            for (Doctor doctor : doctors) {
                writer.write(String.format("%s,%s,%d,%s,%s,%s,%.2f,%d\n",
                    doctor.getId(),
                    doctor.getName(),
                    doctor.getAge(),
                    doctor.getEmail(),
                    doctor.getPhone(),
                    doctor.getSpecialization(),
                    doctor.getConsultationFee(),
                    doctor.getYearsOfExperience()
                ));
            }
        }
    }

    /**
     * Load doctors from CSV file
     */
    public static List<Doctor> loadDoctors(String filePath) throws IOException {
        List<Doctor> doctors = new ArrayList<>();
        Path path = Paths.get(filePath);
        
        if (!Files.exists(path)) {
            return doctors;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    Doctor doctor = new Doctor(
                        parts[0].trim(),
                        parts[1].trim(),
                        Integer.parseInt(parts[2].trim()),
                        parts[3].trim(),
                        parts[4].trim(),
                        parts[5].trim(),
                        Double.parseDouble(parts[6].trim()),
                        Integer.parseInt(parts[7].trim())
                    );
                    doctors.add(doctor);
                }
            }
        }
        return doctors;
    }

    /**
     * Save appointments to CSV file
     */
    public static void saveAppointments(List<Appointment> appointments, String filePath) throws IOException {
        Path path = Paths.get(filePath);
        Files.createDirectories(path.getParent());
        
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            writer.write("appointmentId,doctorId,patientId,appointmentDateTime,status,notes\n");
            for (Appointment appointment : appointments) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s\n",
                    appointment.getAppointmentId(),
                    appointment.getDoctorId(),
                    appointment.getPatientId(),
                    DateUtil.formatDateTimeForCSV(appointment.getAppointmentDateTime()),
                    appointment.getStatus().name(),
                    escapeCSV(appointment.getNotes())
                ));
            }
        }
    }

    /**
     * Load appointments from CSV file
     */
    public static List<Appointment> loadAppointments(String filePath) throws IOException {
        List<Appointment> appointments = new ArrayList<>();
        Path path = Paths.get(filePath);
        
        if (!Files.exists(path)) {
            return appointments;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line = reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    Appointment appointment = new Appointment(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        DateUtil.parseDateTime(parts[3].trim()),
                        AppointmentStatus.valueOf(parts[4].trim())
                    );
                    if (parts.length > 5) {
                        appointment.setNotes(unescapeCSV(parts[5].trim()));
                    }
                    appointments.add(appointment);
                }
            }
        }
        return appointments;
    }

    private static String escapeCSV(String value) {
        if (value == null) return "";
        return value.replace(",", "|").replace("\n", " ");
    }

    private static String unescapeCSV(String value) {
        if (value == null) return "";
        return value.replace("|", ",");
    }
}

