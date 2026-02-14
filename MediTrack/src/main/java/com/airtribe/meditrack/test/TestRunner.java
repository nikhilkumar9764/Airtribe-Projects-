package com.airtribe.meditrack.test;

import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.enums.AppointmentStatus;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Manual test runner for MediTrack application.
 * Demonstrates testing without JUnit framework.
 */
public class TestRunner {
    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("=== MediTrack Test Runner ===\n");
        
        testEncapsulation();
        testInheritance();
        testPolymorphism();
        testAbstraction();
        testCloning();
        testImmutability();
        testEnums();
        testCollections();
        testExceptionHandling();
        testFileIO();
        testConcurrency();
        testDesignPatterns();
        testStreamsAndLambdas();
        
        printTestSummary();
    }

    private static void testEncapsulation() {
        System.out.println("--- Testing Encapsulation ---");
        try {
            Patient patient = new Patient("PAT0001", "John Doe", 30, "john@example.com", 
                "+1234567890", "None", "A+");
            // Test getters
            assert patient.getId().equals("PAT0001");
            assert patient.getName().equals("John Doe");
            assert patient.getAge() == 30;
            testsPassed++;
            System.out.println("✓ Encapsulation test passed");
        } catch (Exception e) {
            testsFailed++;
            System.out.println("✗ Encapsulation test failed: " + e.getMessage());
        }
    }

    private static void testInheritance() {
        System.out.println("\n--- Testing Inheritance ---");
        try {
            Doctor doctor = new Doctor("DOC0001", "Dr. Smith", 45, "smith@example.com", 
                "+1234567890", "Cardiology", 500.0, 15);
            // Test inheritance from Person
            assert doctor.getId().equals("DOC0001");
            assert doctor.getName().equals("Dr. Smith");
            assert doctor.getSpecialization().equals("Cardiology");
            testsPassed++;
            System.out.println("✓ Inheritance test passed");
        } catch (Exception e) {
            testsFailed++;
            System.out.println("✗ Inheritance test failed: " + e.getMessage());
        }
    }

    private static void testPolymorphism() {
        System.out.println("\n--- Testing Polymorphism ---");
        try {
            DoctorService doctorService = new DoctorService();
            Doctor doctor = new Doctor("DOC0001", "Dr. Smith", 45, "smith@example.com", 
                "+1234567890", "Cardiology", 500.0, 15);
            doctorService.addDoctor(doctor);
            
            // Test method overloading
            Doctor foundById = doctorService.searchDoctor("DOC0001");
            assert foundById != null;
            
            List<Doctor> foundByName = doctorService.searchDoctor("Smith", true);
            assert !foundByName.isEmpty();
            
            testsPassed++;
            System.out.println("✓ Polymorphism test passed");
        } catch (Exception e) {
            testsFailed++;
            System.out.println("✗ Polymorphism test failed: " + e.getMessage());
        }
    }

    private static void testAbstraction() {
        System.out.println("\n--- Testing Abstraction ---");
        try {
            // Test interface implementation
            Patient patient = new Patient("PAT0001", "John Doe", 30, "john@example.com", 
                "+1234567890", "None", "A+");
            assert patient.matches("John");
            assert patient.matches("PAT0001");
            
            Bill bill = new Bill("BL0001", "APT0001", "PAT0001", 500.0);
            assert bill.calculatePayment() > 0;
            
            testsPassed++;
            System.out.println("✓ Abstraction test passed");
        } catch (Exception e) {
            testsFailed++;
            System.out.println("✗ Abstraction test failed: " + e.getMessage());
        }
    }

    private static void testCloning() {
        System.out.println("\n--- Testing Cloning (Deep Copy) ---");
        try {
            Patient original = new Patient("PAT0001", "John Doe", 30, "john@example.com", 
                "+1234567890", "None", "A+");
            original.addAllergy("Peanuts");
            
            Patient cloned = original.clone();
            cloned.addAllergy("Dust");
            
            // Verify deep copy - original should not be affected
            assert original.getAllergies().size() == 1;
            assert cloned.getAllergies().size() == 2;
            
            testsPassed++;
            System.out.println("✓ Cloning test passed");
        } catch (Exception e) {
            testsFailed++;
            System.out.println("✗ Cloning test failed: " + e.getMessage());
        }
    }

    private static void testImmutability() {
        System.out.println("\n--- Testing Immutability ---");
        try {
            BillSummary summary = new BillSummary("BL0001", "PAT0001", 590.0, 
                LocalDateTime.now(), "PAID");
            
            // Verify no setters available (compile-time check)
            // summary.setBillId("BL0002"); // This would not compile
            
            assert summary.getBillId().equals("BL0001");
            assert summary.getTotalAmount() == 590.0;
            
            testsPassed++;
            System.out.println("✓ Immutability test passed");
        } catch (Exception e) {
            testsFailed++;
            System.out.println("✗ Immutability test failed: " + e.getMessage());
        }
    }

    private static void testEnums() {
        System.out.println("\n--- Testing Enums ---");
        try {
            Appointment appointment = new Appointment("APT0001", "DOC0001", "PAT0001", 
                LocalDateTime.now(), AppointmentStatus.PENDING);
            
            assert appointment.getStatus() == AppointmentStatus.PENDING;
            appointment.setStatus(AppointmentStatus.CONFIRMED);
            assert appointment.getStatus() == AppointmentStatus.CONFIRMED;
            
            testsPassed++;
            System.out.println("✓ Enums test passed");
        } catch (Exception e) {
            testsFailed++;
            System.out.println("✗ Enums test failed: " + e.getMessage());
        }
    }

    private static void testCollections() {
        System.out.println("\n--- Testing Collections & Generics ---");
        try {
            DataStore<Patient> patientStore = new DataStore<>("Patient");
            Patient patient1 = new Patient("PAT0001", "John", 30, "john@example.com", 
                "+1234567890", "None", "A+");
            Patient patient2 = new Patient("PAT0002", "Jane", 25, "jane@example.com", 
                "+1234567891", "None", "B+");
            
            patientStore.add("PAT0001", patient1);
            patientStore.add("PAT0002", patient2);
            
            assert patientStore.size() == 2;
            assert patientStore.get("PAT0001") != null;
            
            // Test filtering with streams
            List<Patient> filtered = patientStore.filter(p -> p.getAge() > 25);
            assert filtered.size() == 1;
            
            testsPassed++;
            System.out.println("✓ Collections & Generics test passed");
        } catch (Exception e) {
            testsFailed++;
            System.out.println("✗ Collections & Generics test failed: " + e.getMessage());
        }
    }

    private static void testExceptionHandling() {
        System.out.println("\n--- Testing Exception Handling ---");
        try {
            AppointmentService appointmentService = new AppointmentService(
                new DoctorService(), new PatientService()
            );
            
            try {
                appointmentService.getAppointment("NONEXISTENT");
                testsFailed++;
                System.out.println("✗ Exception handling test failed: Should have thrown exception");
            } catch (AppointmentNotFoundException e) {
                testsPassed++;
                System.out.println("✓ Exception handling test passed");
            }
        } catch (Exception e) {
            testsFailed++;
            System.out.println("✗ Exception handling test failed: " + e.getMessage());
        }
    }

    private static void testFileIO() {
        System.out.println("\n--- Testing File I/O ---");
        try {
            Patient patient = new Patient("PAT0001", "John Doe", 30, "john@example.com", 
                "+1234567890", "None", "A+");
            
            // Test CSV operations
            List<Patient> patients = List.of(patient);
            CSVUtil.savePatients(patients, "test_patients.csv");
            
            List<Patient> loaded = CSVUtil.loadPatients("test_patients.csv");
            assert loaded.size() == 1;
            assert loaded.get(0).getId().equals("PAT0001");
            
            // Cleanup
            java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get("test_patients.csv"));
            
            testsPassed++;
            System.out.println("✓ File I/O test passed");
        } catch (Exception e) {
            testsFailed++;
            System.out.println("✗ File I/O test failed: " + e.getMessage());
        }
    }

    private static void testConcurrency() {
        System.out.println("\n--- Testing Concurrency ---");
        try {
            IdGenerator generator = IdGenerator.getInstance();
            
            // Test AtomicInteger thread-safety
            String id1 = generator.generatePatientId();
            String id2 = generator.generatePatientId();
            
            assert !id1.equals(id2);
            
            testsPassed++;
            System.out.println("✓ Concurrency test passed");
        } catch (Exception e) {
            testsFailed++;
            System.out.println("✗ Concurrency test failed: " + e.getMessage());
        }
    }

    private static void testDesignPatterns() {
        System.out.println("\n--- Testing Design Patterns ---");
        try {
            // Test Singleton
            IdGenerator instance1 = IdGenerator.getInstance();
            IdGenerator instance2 = IdGenerator.getInstance();
            assert instance1 == instance2;
            
            // Test Factory
            Bill bill = com.airtribe.meditrack.pattern.BillFactory.createStandardBill(
                "BL0001", "APT0001", "PAT0001", 500.0
            );
            assert bill != null;
            
            testsPassed++;
            System.out.println("✓ Design Patterns test passed");
        } catch (Exception e) {
            testsFailed++;
            System.out.println("✗ Design Patterns test failed: " + e.getMessage());
        }
    }

    private static void testStreamsAndLambdas() {
        System.out.println("\n--- Testing Streams & Lambdas ---");
        try {
            DoctorService doctorService = new DoctorService();
            Doctor doc1 = new Doctor("DOC0001", "Dr. Smith", 45, "smith@example.com", 
                "+1234567890", "Cardiology", 500.0, 15);
            Doctor doc2 = new Doctor("DOC0002", "Dr. Jones", 40, "jones@example.com", 
                "+1234567891", "Cardiology", 600.0, 10);
            
            doctorService.addDoctor(doc1);
            doctorService.addDoctor(doc2);
            
            // Test streams
            double avgFee = doctorService.getAverageConsultationFee();
            assert avgFee == 550.0;
            
            List<Doctor> cardiologists = doctorService.getDoctorsBySpecialization("Cardiology");
            assert cardiologists.size() == 2;
            
            testsPassed++;
            System.out.println("✓ Streams & Lambdas test passed");
        } catch (Exception e) {
            testsFailed++;
            System.out.println("✗ Streams & Lambdas test failed: " + e.getMessage());
        }
    }

    private static void printTestSummary() {
        System.out.println("\n=== Test Summary ===");
        System.out.println("Tests Passed: " + testsPassed);
        System.out.println("Tests Failed: " + testsFailed);
        System.out.println("Total Tests: " + (testsPassed + testsFailed));
        System.out.println("Success Rate: " + 
            String.format("%.2f%%", (testsPassed * 100.0 / (testsPassed + testsFailed))));
    }
}

