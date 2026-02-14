package com.airtribe.meditrack;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.pattern.*;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

/**
 * Main class with menu-driven console UI.
 * Demonstrates command-line usage and application integration.
 */
public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static DoctorService doctorService;
    private static PatientService patientService;
    private static AppointmentService appointmentService;
    private static AppointmentNotifier appointmentNotifier;
    private static IdGenerator idGenerator;

    public static void main(String[] args) {
        System.out.println("=== MediTrack - Medical Appointment Management System ===\n");
        
        // Initialize services
        initializeServices();
        
        // Load data if --loadData argument is provided
        if (args.length > 0 && args[0].equals("--loadData")) {
            loadDataFromFiles();
        }
        
        // Setup observer pattern
        setupObservers();
        
        // Main menu loop
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            try {
                switch (choice) {
                    case 1 -> handleDoctorMenu();
                    case 2 -> handlePatientMenu();
                    case 3 -> handleAppointmentMenu();
                    case 4 -> handleBillingMenu();
                    case 5 -> handleSearchMenu();
                    case 6 -> handleAnalyticsMenu();
                    case 7 -> handleAIMenu();
                    case 8 -> saveDataToFiles();
                    case 9 -> {
                        System.out.println("Thank you for using MediTrack!");
                        running = false;
                    }
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        scanner.close();
    }

    private static void initializeServices() {
        doctorService = new DoctorService();
        patientService = new PatientService();
        appointmentService = new AppointmentService(doctorService, patientService);
        idGenerator = IdGenerator.getInstance();
    }

    private static void setupObservers() {
        appointmentNotifier = new AppointmentNotifier();
        appointmentNotifier.addObserver(new ConsoleAppointmentObserver());
    }

    private static void displayMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. Doctor Management");
        System.out.println("2. Patient Management");
        System.out.println("3. Appointment Management");
        System.out.println("4. Billing");
        System.out.println("5. Search");
        System.out.println("6. Analytics");
        System.out.println("7. AI Recommendations");
        System.out.println("8. Save Data to Files");
        System.out.println("9. Exit");
        System.out.println("===============================");
    }

    private static void handleDoctorMenu() {
        System.out.println("\n--- Doctor Management ---");
        System.out.println("1. Add Doctor");
        System.out.println("2. View All Doctors");
        System.out.println("3. View Doctor by ID");
        System.out.println("4. Remove Doctor");
        System.out.println("5. Back to Main Menu");
        
        int choice = getIntInput("Enter your choice: ");
        switch (choice) {
            case 1 -> addDoctor();
            case 2 -> viewAllDoctors();
            case 3 -> viewDoctorById();
            case 4 -> removeDoctor();
            default -> System.out.println("Returning to main menu...");
        }
    }

    private static void addDoctor() {
        System.out.println("\n--- Add Doctor ---");
        String id = idGenerator.generateDoctorId();
        System.out.println("Generated ID: " + id);
        String name = getStringInput("Enter name: ");
        int age = getIntInput("Enter age: ");
        String email = getStringInput("Enter email: ");
        String phone = getStringInput("Enter phone: ");
        String specialization = getStringInput("Enter specialization: ");
        double fee = getDoubleInput("Enter consultation fee: ");
        int experience = getIntInput("Enter years of experience: ");
        
        Doctor doctor = new Doctor(id, name, age, email, phone, specialization, fee, experience);
        doctorService.addDoctor(doctor);
        System.out.println("Doctor added successfully!");
    }

    private static void viewAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
        } else {
            System.out.println("\n--- All Doctors ---");
            doctors.forEach(System.out::println);
        }
    }

    private static void viewDoctorById() {
        String id = getStringInput("Enter doctor ID: ");
        Doctor doctor = doctorService.getDoctor(id);
        if (doctor != null) {
            System.out.println(doctor);
        } else {
            System.out.println("Doctor not found.");
        }
    }

    private static void removeDoctor() {
        String id = getStringInput("Enter doctor ID to remove: ");
        if (doctorService.removeDoctor(id)) {
            System.out.println("Doctor removed successfully.");
        } else {
            System.out.println("Doctor not found.");
        }
    }

    private static void handlePatientMenu() {
        System.out.println("\n--- Patient Management ---");
        System.out.println("1. Add Patient");
        System.out.println("2. View All Patients");
        System.out.println("3. View Patient by ID");
        System.out.println("4. Remove Patient");
        System.out.println("5. Back to Main Menu");
        
        int choice = getIntInput("Enter your choice: ");
        switch (choice) {
            case 1 -> addPatient();
            case 2 -> viewAllPatients();
            case 3 -> viewPatientById();
            case 4 -> removePatient();
            default -> System.out.println("Returning to main menu...");
        }
    }

    private static void addPatient() {
        System.out.println("\n--- Add Patient ---");
        String id = idGenerator.generatePatientId();
        System.out.println("Generated ID: " + id);
        String name = getStringInput("Enter name: ");
        int age = getIntInput("Enter age: ");
        String email = getStringInput("Enter email: ");
        String phone = getStringInput("Enter phone: ");
        String medicalHistory = getStringInput("Enter medical history: ");
        String bloodGroup = getStringInput("Enter blood group (e.g., A+, B-): ");
        
        Patient patient = new Patient(id, name, age, email, phone, medicalHistory, bloodGroup);
        patientService.addPatient(patient);
        System.out.println("Patient added successfully!");
    }

    private static void viewAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            System.out.println("\n--- All Patients ---");
            patients.forEach(System.out::println);
        }
    }

    private static void viewPatientById() {
        String id = getStringInput("Enter patient ID: ");
        Patient patient = patientService.getPatient(id);
        if (patient != null) {
            System.out.println(patient);
        } else {
            System.out.println("Patient not found.");
        }
    }

    private static void removePatient() {
        String id = getStringInput("Enter patient ID to remove: ");
        if (patientService.removePatient(id)) {
            System.out.println("Patient removed successfully.");
        } else {
            System.out.println("Patient not found.");
        }
    }

    private static void handleAppointmentMenu() {
        System.out.println("\n--- Appointment Management ---");
        System.out.println("1. Create Appointment");
        System.out.println("2. View All Appointments");
        System.out.println("3. View Appointment by ID");
        System.out.println("4. Cancel Appointment");
        System.out.println("5. Confirm Appointment");
        System.out.println("6. Complete Appointment");
        System.out.println("7. Back to Main Menu");
        
        int choice = getIntInput("Enter your choice: ");
        switch (choice) {
            case 1 -> createAppointment();
            case 2 -> viewAllAppointments();
            case 3 -> viewAppointmentById();
            case 4 -> cancelAppointment();
            case 5 -> confirmAppointment();
            case 6 -> completeAppointment();
            default -> System.out.println("Returning to main menu...");
        }
    }

    private static void createAppointment() {
        System.out.println("\n--- Create Appointment ---");
        String appointmentId = idGenerator.generateAppointmentId();
        System.out.println("Generated ID: " + appointmentId);
        String doctorId = getStringInput("Enter doctor ID: ");
        String patientId = getStringInput("Enter patient ID: ");
        System.out.println("Enter appointment date and time (yyyy-MM-dd HH:mm): ");
        String dateTimeStr = scanner.nextLine();
        LocalDateTime dateTime = DateUtil.parseDateTime(dateTimeStr);
        
        try {
            Appointment appointment = appointmentService.createAppointment(
                appointmentId, doctorId, patientId, dateTime
            );
            appointmentNotifier.notifyAppointmentCreated(appointment);
            System.out.println("Appointment created successfully!");
        } catch (AppointmentNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            System.out.println("\n--- All Appointments ---");
            appointments.forEach(apt -> {
                System.out.println(apt);
                System.out.println("  Formatted: " + DateUtil.formatDateTime(apt.getAppointmentDateTime()));
            });
        }
    }

    private static void viewAppointmentById() {
        String id = getStringInput("Enter appointment ID: ");
        try {
            Appointment appointment = appointmentService.getAppointment(id);
            System.out.println(appointment);
        } catch (AppointmentNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void cancelAppointment() {
        String id = getStringInput("Enter appointment ID to cancel: ");
        try {
            appointmentService.cancelAppointment(id);
            Appointment appointment = appointmentService.getAppointment(id);
            appointmentNotifier.notifyAppointmentCancelled(appointment);
            System.out.println("Appointment cancelled successfully.");
        } catch (AppointmentNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void confirmAppointment() {
        String id = getStringInput("Enter appointment ID to confirm: ");
        try {
            appointmentService.confirmAppointment(id);
            Appointment appointment = appointmentService.getAppointment(id);
            appointmentNotifier.notifyAppointmentConfirmed(appointment);
            System.out.println("Appointment confirmed successfully.");
        } catch (AppointmentNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void completeAppointment() {
        String id = getStringInput("Enter appointment ID to complete: ");
        try {
            appointmentService.completeAppointment(id);
            System.out.println("Appointment completed successfully.");
        } catch (AppointmentNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void handleBillingMenu() {
        System.out.println("\n--- Billing ---");
        System.out.println("1. Generate Bill for Appointment");
        System.out.println("2. View Bill Summary");
        System.out.println("3. Back to Main Menu");
        
        int choice = getIntInput("Enter your choice: ");
        switch (choice) {
            case 1 -> generateBill();
            case 2 -> viewBillSummary();
            default -> System.out.println("Returning to main menu...");
        }
    }

    private static void generateBill() {
        String appointmentId = getStringInput("Enter appointment ID: ");
        try {
            Bill bill = appointmentService.generateBill(appointmentId);
            System.out.println("\n--- Bill Generated ---");
            System.out.println(bill);
            
            // Create immutable BillSummary
            BillSummary summary = new BillSummary(
                bill.getBillId(),
                bill.getPatientId(),
                bill.getTotalAmount(),
                bill.getBillDate(),
                bill.getPaymentStatus()
            );
            System.out.println("\n--- Bill Summary (Immutable) ---");
            System.out.println(summary);
        } catch (AppointmentNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewBillSummary() {
        System.out.println("Bill summary feature - to be implemented with bill storage");
    }

    private static void handleSearchMenu() {
        System.out.println("\n--- Search ---");
        System.out.println("1. Search Doctors");
        System.out.println("2. Search Patients");
        System.out.println("3. Back to Main Menu");
        
        int choice = getIntInput("Enter your choice: ");
        switch (choice) {
            case 1 -> searchDoctors();
            case 2 -> searchPatients();
            default -> System.out.println("Returning to main menu...");
        }
    }

    private static void searchDoctors() {
        String query = getStringInput("Enter search query (ID, name, or specialization): ");
        List<Doctor> results = doctorService.searchDoctors(query);
        if (results.isEmpty()) {
            System.out.println("No doctors found.");
        } else {
            System.out.println("\n--- Search Results ---");
            results.forEach(System.out::println);
        }
    }

    private static void searchPatients() {
        String query = getStringInput("Enter search query (ID, name, or age): ");
        List<Patient> results = patientService.searchPatients(query);
        if (results.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            System.out.println("\n--- Search Results ---");
            results.forEach(System.out::println);
        }
    }

    private static void handleAnalyticsMenu() {
        System.out.println("\n--- Analytics ---");
        System.out.println("1. Average Consultation Fee");
        System.out.println("2. Doctors by Specialization");
        System.out.println("3. Appointment Statistics");
        System.out.println("4. Back to Main Menu");
        
        int choice = getIntInput("Enter your choice: ");
        switch (choice) {
            case 1 -> {
                double avgFee = doctorService.getAverageConsultationFee();
                System.out.println("Average Consultation Fee: " + String.format("%.2f", avgFee));
            }
            case 2 -> {
                String specialization = getStringInput("Enter specialization: ");
                List<Doctor> doctors = doctorService.getDoctorsBySpecialization(specialization);
                System.out.println("Doctors in " + specialization + ": " + doctors.size());
                doctors.forEach(System.out::println);
            }
            case 3 -> {
                System.out.println("Total Appointments: " + appointmentService.getAppointmentCount());
                System.out.println("Total Doctors: " + doctorService.getDoctorCount());
                System.out.println("Total Patients: " + patientService.getPatientCount());
            }
            default -> System.out.println("Returning to main menu...");
        }
    }

    private static void handleAIMenu() {
        System.out.println("\n--- AI Recommendations ---");
        String symptoms = getStringInput("Enter your symptoms: ");
        List<Doctor> recommendations = AIHelper.recommendDoctors(symptoms, doctorService.getAllDoctors());
        
        if (recommendations.isEmpty()) {
            System.out.println("No specific recommendations. Showing all doctors:");
            doctorService.getAllDoctors().forEach(System.out::println);
        } else {
            System.out.println("\n--- Recommended Doctors ---");
            recommendations.forEach(System.out::println);
        }
        
        System.out.println("\n--- Suggested Appointment Slots ---");
        List<String> slots = AIHelper.suggestAppointmentSlots(5);
        slots.forEach(slot -> System.out.println("  " + slot));
    }

    private static void loadDataFromFiles() {
        System.out.println("Loading data from files...");
        try {
            List<Patient> patients = CSVUtil.loadPatients(Constants.PATIENTS_FILE);
            patients.forEach(patientService::addPatient);
            System.out.println("Loaded " + patients.size() + " patients");
            
            List<Doctor> doctors = CSVUtil.loadDoctors(Constants.DOCTORS_FILE);
            doctors.forEach(doctorService::addDoctor);
            System.out.println("Loaded " + doctors.size() + " doctors");
            
            List<Appointment> appointments = CSVUtil.loadAppointments(Constants.APPOINTMENTS_FILE);
            appointments.forEach(apt -> {
                try {
                    appointmentService.createAppointment(
                        apt.getAppointmentId(),
                        apt.getDoctorId(),
                        apt.getPatientId(),
                        apt.getAppointmentDateTime()
                    );
                } catch (AppointmentNotFoundException e) {
                    System.out.println("Warning: Could not load appointment " + apt.getAppointmentId());
                }
            });
            System.out.println("Loaded " + appointments.size() + " appointments");
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private static void saveDataToFiles() {
        System.out.println("Saving data to files...");
        try {
            CSVUtil.savePatients(patientService.getAllPatients(), Constants.PATIENTS_FILE);
            System.out.println("Saved patients to " + Constants.PATIENTS_FILE);
            
            CSVUtil.saveDoctors(doctorService.getAllDoctors(), Constants.DOCTORS_FILE);
            System.out.println("Saved doctors to " + Constants.DOCTORS_FILE);
            
            CSVUtil.saveAppointments(appointmentService.getAllAppointments(), Constants.APPOINTMENTS_FILE);
            System.out.println("Saved appointments to " + Constants.APPOINTMENTS_FILE);
            
            System.out.println("Data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Helper methods for input
    private static String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return getIntInput(prompt);
        }
    }

    private static double getDoubleInput(String prompt) {
        System.out.print(prompt);
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return getDoubleInput(prompt);
        }
    }
}

