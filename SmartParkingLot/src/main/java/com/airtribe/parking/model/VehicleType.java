package com.airtribe.parking.model;

/**
 * Enum representing different types of vehicles that can park.
 * Each vehicle type has associated size requirements and fee rates.
 */
public enum VehicleType {
    MOTORCYCLE(1, 5.0),      // Smallest, lowest fee
    CAR(2, 10.0),            // Medium size, standard fee
    SUV(3, 12.0),            // Large car, higher fee
    BUS(4, 20.0),           // Largest, highest fee
    TRUCK(4, 20.0);         // Same as bus

    private final int sizeUnits;  // Size requirement (1 = smallest, 4 = largest)
    private final double hourlyRate; // Base hourly parking rate

    VehicleType(int sizeUnits, double hourlyRate) {
        this.sizeUnits = sizeUnits;
        this.hourlyRate = hourlyRate;
    }

    public int getSizeUnits() {
        return sizeUnits;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    /**
     * Checks if this vehicle type can fit in a spot of given size.
     */
    public boolean canFitIn(int spotSizeUnits) {
        return this.sizeUnits <= spotSizeUnits;
    }
}

