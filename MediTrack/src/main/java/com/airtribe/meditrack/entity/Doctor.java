package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interface_.Searchable;
import com.airtribe.meditrack.util.Validator;

/**
 * Doctor entity extending Person.
 * Demonstrates inheritance and specialization.
 */
public class Doctor extends Person implements Searchable {
    private String specialization;
    private double consultationFee;
    private int yearsOfExperience;

    public Doctor(String id, String name, int age, String email, String phone, 
                  String specialization, double consultationFee, int yearsOfExperience) {
        super(id, name, age, email, phone); // Constructor chaining
        this.setSpecialization(specialization);
        this.setConsultationFee(consultationFee);
        this.setYearsOfExperience(yearsOfExperience);
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        if (Validator.isValidSpecialization(specialization)) {
            this.specialization = specialization;
        } else {
            throw new IllegalArgumentException("Invalid specialization");
        }
    }

    public double getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(double consultationFee) {
        if (consultationFee >= 0) {
            this.consultationFee = consultationFee;
        } else {
            throw new IllegalArgumentException("Consultation fee cannot be negative");
        }
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        if (yearsOfExperience >= 0) {
            this.yearsOfExperience = yearsOfExperience;
        } else {
            throw new IllegalArgumentException("Years of experience cannot be negative");
        }
    }

    @Override
    public boolean matches(String query) {
        if (query == null || query.isEmpty()) return false;
        String lowerQuery = query.toLowerCase();
        return getName().toLowerCase().contains(lowerQuery) ||
               getSpecialization().toLowerCase().contains(lowerQuery) ||
               getId().equals(query);
    }

    @Override
    public String toString() {
        return String.format("Doctor{id='%s', name='%s', specialization='%s', fee=%.2f, experience=%d years}", 
            getId(), getName(), specialization, consultationFee, yearsOfExperience);
    }
}

