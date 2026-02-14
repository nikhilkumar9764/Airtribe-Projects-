package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.Doctor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AI Helper for rule-based doctor recommendations.
 * Demonstrates strategy pattern and Java 8+ streams.
 */
public class AIHelper {
    // Rule-based symptom to specialization mapping
    private static final Map<String, String> SYMPTOM_SPECIALIZATION_MAP = new HashMap<>();
    
    static {
        SYMPTOM_SPECIALIZATION_MAP.put("chest pain", "Cardiology");
        SYMPTOM_SPECIALIZATION_MAP.put("heart", "Cardiology");
        SYMPTOM_SPECIALIZATION_MAP.put("skin", "Dermatology");
        SYMPTOM_SPECIALIZATION_MAP.put("rash", "Dermatology");
        SYMPTOM_SPECIALIZATION_MAP.put("headache", "Neurology");
        SYMPTOM_SPECIALIZATION_MAP.put("migraine", "Neurology");
        SYMPTOM_SPECIALIZATION_MAP.put("bone", "Orthopedics");
        SYMPTOM_SPECIALIZATION_MAP.put("fracture", "Orthopedics");
        SYMPTOM_SPECIALIZATION_MAP.put("child", "Pediatrics");
        SYMPTOM_SPECIALIZATION_MAP.put("pediatric", "Pediatrics");
    }

    // Private constructor to prevent instantiation
    private AIHelper() {
        throw new AssertionError("Cannot instantiate AIHelper class");
    }

    /**
     * Recommend doctors based on symptoms
     */
    public static List<Doctor> recommendDoctors(String symptoms, List<Doctor> allDoctors) {
        if (symptoms == null || symptoms.trim().isEmpty()) {
            return allDoctors;
        }

        String lowerSymptoms = symptoms.toLowerCase();
        String recommendedSpecialization = findSpecialization(lowerSymptoms);

        if (recommendedSpecialization == null) {
            return allDoctors; // Return all if no match
        }

        // Use streams to filter doctors by specialization
        return allDoctors.stream()
            .filter(doctor -> doctor.getSpecialization().equalsIgnoreCase(recommendedSpecialization))
            .sorted(Comparator.comparing(Doctor::getYearsOfExperience).reversed())
            .collect(Collectors.toList());
    }

    private static String findSpecialization(String symptoms) {
        for (Map.Entry<String, String> entry : SYMPTOM_SPECIALIZATION_MAP.entrySet()) {
            if (symptoms.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Suggest available appointment slots (simplified - just suggests next available times)
     */
    public static List<String> suggestAppointmentSlots(int count) {
        List<String> slots = new ArrayList<>();
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        
        for (int i = 1; i <= count; i++) {
            java.time.LocalDateTime slot = now.plusDays(i).withHour(10).withMinute(0);
            slots.add(slot.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        }
        
        return slots;
    }
}

