package com.airtribe.parking.model;

import java.util.Objects;

/**
 * Represents a vehicle entering or exiting the parking lot.
 */
public class Vehicle {
    private final String licensePlate;
    private final VehicleType vehicleType;
    private final String ownerName;

    public Vehicle(String licensePlate, VehicleType vehicleType, String ownerName) {
        if (licensePlate == null || licensePlate.isBlank()) {
            throw new IllegalArgumentException("License plate must not be blank");
        }
        this.licensePlate = licensePlate.trim().toUpperCase();
        this.vehicleType = Objects.requireNonNull(vehicleType, "Vehicle type must not be null");
        this.ownerName = ownerName != null ? ownerName : "Unknown";
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return licensePlate.equals(vehicle.licensePlate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(licensePlate);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "licensePlate='" + licensePlate + '\'' +
                ", vehicleType=" + vehicleType +
                ", ownerName='" + ownerName + '\'' +
                '}';
    }
}

