package com.airtribe.parking.model;

/**
 * Enum representing the status of a parking spot.
 */
public enum SpotStatus {
    AVAILABLE,      // Spot is free and available for parking
    OCCUPIED,       // Spot is currently occupied by a vehicle
    RESERVED,       // Spot is reserved (for future use)
    MAINTENANCE     // Spot is under maintenance and unavailable
}

