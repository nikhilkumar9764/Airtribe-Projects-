package com.airtribe.meditrack.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * ID Generator using Singleton pattern (both eager and lazy examples).
 * Demonstrates Singleton pattern and AtomicInteger for thread-safety.
 */
public class IdGenerator {
    // Eager singleton instance
    private static final IdGenerator EAGER_INSTANCE = new IdGenerator();
    
    // Lazy singleton instance (volatile for double-checked locking)
    private static volatile IdGenerator LAZY_INSTANCE;
    
    // Atomic counters for thread-safety
    private final AtomicInteger patientCounter = new AtomicInteger(1);
    private final AtomicInteger doctorCounter = new AtomicInteger(1);
    private final AtomicInteger appointmentCounter = new AtomicInteger(1);
    private final AtomicInteger billCounter = new AtomicInteger(1);

    // Private constructor to prevent instantiation
    private IdGenerator() {
        System.out.println("IdGenerator instance created");
    }

    /**
     * Eager singleton - instance created at class loading time
     */
    public static IdGenerator getEagerInstance() {
        return EAGER_INSTANCE;
    }

    /**
     * Lazy singleton - instance created on first access
     * Uses double-checked locking for thread-safety
     */
    public static IdGenerator getLazyInstance() {
        if (LAZY_INSTANCE == null) {
            synchronized (IdGenerator.class) {
                if (LAZY_INSTANCE == null) {
                    LAZY_INSTANCE = new IdGenerator();
                }
            }
        }
        return LAZY_INSTANCE;
    }

    /**
     * Get default instance (uses eager singleton)
     */
    public static IdGenerator getInstance() {
        return getEagerInstance();
    }

    public String generatePatientId() {
        return String.format("PAT%04d", patientCounter.getAndIncrement());
    }

    public String generateDoctorId() {
        return String.format("DOC%04d", doctorCounter.getAndIncrement());
    }

    public String generateAppointmentId() {
        return String.format("APT%04d", appointmentCounter.getAndIncrement());
    }

    public String generateBillId() {
        return String.format("BL%04d", billCounter.getAndIncrement());
    }

    public void resetCounters() {
        patientCounter.set(1);
        doctorCounter.set(1);
        appointmentCounter.set(1);
        billCounter.set(1);
    }
}

