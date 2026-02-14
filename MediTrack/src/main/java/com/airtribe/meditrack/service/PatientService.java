package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.util.DataStore;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Patient operations.
 * Demonstrates polymorphism through method overloading.
 */
public class PatientService {
    private final DataStore<Patient> patientStore;

    public PatientService() {
        this.patientStore = new DataStore<>("Patient");
    }

    public void addPatient(Patient patient) {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }
        patientStore.add(patient.getId(), patient);
    }

    public Patient getPatient(String id) {
        return patientStore.get(id);
    }

    public List<Patient> getAllPatients() {
        return patientStore.getAll();
    }

    public boolean removePatient(String id) {
        return patientStore.remove(id) != null;
    }

    /**
     * Method overloading - search by ID
     */
    public Patient searchPatient(String id) {
        return patientStore.get(id);
    }

    /**
     * Method overloading - search by name
     */
    public List<Patient> searchPatient(String name, boolean byName) {
        if (byName) {
            return patientStore.filter(patient -> 
                patient.getName().toLowerCase().contains(name.toLowerCase())
            );
        }
        return List.of(searchPatient(name));
    }

    /**
     * Method overloading - search by age
     */
    public List<Patient> searchPatient(int age) {
        return patientStore.filter(patient -> patient.getAge() == age);
    }

    /**
     * Search using Searchable interface (polymorphism)
     */
    public List<Patient> searchPatients(String query) {
        return patientStore.getAll().stream()
            .filter(patient -> patient.matches(query))
            .collect(Collectors.toList());
    }

    public int getPatientCount() {
        return patientStore.size();
    }
}

