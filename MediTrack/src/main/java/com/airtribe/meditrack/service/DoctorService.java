package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.util.DataStore;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Doctor operations.
 * Demonstrates service layer pattern and polymorphism through method overloading.
 */
public class DoctorService {
    private final DataStore<Doctor> doctorStore;

    public DoctorService() {
        this.doctorStore = new DataStore<>("Doctor");
    }

    public void addDoctor(Doctor doctor) {
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor cannot be null");
        }
        doctorStore.add(doctor.getId(), doctor);
    }

    public Doctor getDoctor(String id) {
        return doctorStore.get(id);
    }

    public List<Doctor> getAllDoctors() {
        return doctorStore.getAll();
    }

    public boolean removeDoctor(String id) {
        return doctorStore.remove(id) != null;
    }

    /**
     * Method overloading - search by ID
     */
    public Doctor searchDoctor(String id) {
        return doctorStore.get(id);
    }

    /**
     * Method overloading - search by name
     */
    public List<Doctor> searchDoctor(String name, boolean byName) {
        if (byName) {
            return doctorStore.filter(doctor -> 
                doctor.getName().toLowerCase().contains(name.toLowerCase())
            );
        }
        Doctor doctor = searchDoctor(name);
        return doctor != null ? List.of(doctor) : List.of();
    }

    /**
     * Method overloading - search by specialization
     */
    public List<Doctor> searchDoctorBySpecialization(String specialization) {
        return doctorStore.filter(doctor -> 
            doctor.getSpecialization().equalsIgnoreCase(specialization)
        );
    }

    /**
     * Search using Searchable interface (polymorphism)
     */
    public List<Doctor> searchDoctors(String query) {
        return doctorStore.getAll().stream()
            .filter(doctor -> doctor.matches(query))
            .collect(Collectors.toList());
    }

    /**
     * Get doctors filtered by specialization using streams
     */
    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return getAllDoctors().stream()
            .filter(doctor -> doctor.getSpecialization().equalsIgnoreCase(specialization))
            .collect(Collectors.toList());
    }

    /**
     * Calculate average consultation fee using streams
     */
    public double getAverageConsultationFee() {
        return getAllDoctors().stream()
            .mapToDouble(Doctor::getConsultationFee)
            .average()
            .orElse(0.0);
    }

    public int getDoctorCount() {
        return doctorStore.size();
    }
}

