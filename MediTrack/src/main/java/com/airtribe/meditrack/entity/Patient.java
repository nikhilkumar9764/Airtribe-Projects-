package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interface_.Searchable;
import com.airtribe.meditrack.util.Validator;
import java.util.ArrayList;
import java.util.List;

/**
 * Patient entity extending Person.
 * Implements Cloneable for deep copying demonstration.
 */
public class Patient extends Person implements Searchable, Cloneable {
    private String medicalHistory;
    private List<String> allergies;
    private String bloodGroup;

    public Patient(String id, String name, int age, String email, String phone, 
                   String medicalHistory, String bloodGroup) {
        super(id, name, age, email, phone);
        this.medicalHistory = medicalHistory;
        this.bloodGroup = bloodGroup;
        this.allergies = new ArrayList<>();
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public List<String> getAllergies() {
        return new ArrayList<>(allergies); // Defensive copy
    }

    public void addAllergy(String allergy) {
        if (allergy != null && !allergy.trim().isEmpty()) {
            this.allergies.add(allergy);
        }
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        if (Validator.isValidBloodGroup(bloodGroup)) {
            this.bloodGroup = bloodGroup;
        } else {
            throw new IllegalArgumentException("Invalid blood group");
        }
    }

    @Override
    public boolean matches(String query) {
        if (query == null || query.isEmpty()) return false;
        String lowerQuery = query.toLowerCase();
        return getName().toLowerCase().contains(lowerQuery) ||
               getId().equals(query) ||
               String.valueOf(getAge()).equals(query);
    }

    /**
     * Deep clone implementation
     */
    @Override
    public Patient clone() {
        try {
            Patient cloned = (Patient) super.clone();
            // Deep copy the allergies list
            cloned.allergies = new ArrayList<>(this.allergies);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported", e);
        }
    }

    @Override
    public String toString() {
        return String.format("Patient{id='%s', name='%s', age=%d, bloodGroup='%s', allergies=%s}", 
            getId(), getName(), getAge(), bloodGroup, allergies);
    }
}

