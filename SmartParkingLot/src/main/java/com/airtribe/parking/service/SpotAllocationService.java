package com.airtribe.parking.service;

import com.airtribe.parking.model.ParkingLot;
import com.airtribe.parking.model.ParkingSpot;
import com.airtribe.parking.model.SpotStatus;
import com.airtribe.parking.model.VehicleType;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Service responsible for allocating parking spots to incoming vehicles.
 * Implements efficient allocation algorithm considering vehicle size and spot availability.
 * 
 * Allocation Strategy:
 * 1. Find all available spots that can accommodate the vehicle
 * 2. Prefer spots on lower floors (closer to entrance)
 * 3. Among same floor, prefer smaller spots (to reserve larger spots for larger vehicles)
 * 4. Use fine-grained locking to prevent race conditions
 */
public class SpotAllocationService {
    private static final Logger logger = Logger.getLogger(SpotAllocationService.class.getName());

    /**
     * Allocates an available parking spot for the given vehicle type.
     * Uses a greedy algorithm that prefers lower floors and optimally-sized spots.
     * 
     * Algorithm:
     * 1. Filter spots that can accommodate the vehicle type
     * 2. Sort by floor (ascending), then by size (ascending) to prefer smaller spots
     * 3. Try to acquire lock on each spot in order
     * 4. Double-check availability after acquiring lock (double-checked locking pattern)
     * 5. Return first successfully locked spot
     * 
     * @param parkingLot The parking lot to search
     * @param vehicleType Type of vehicle needing a spot
     * @return Allocated parking spot, or null if no spot available
     */
    public ParkingSpot allocateSpot(ParkingLot parkingLot, VehicleType vehicleType) {
        if (parkingLot == null || vehicleType == null) {
            throw new IllegalArgumentException("Parking lot and vehicle type must not be null");
        }

        // Get all spots that can accommodate this vehicle type
        List<ParkingSpot> candidateSpots = parkingLot.getAllSpots().stream()
                .filter(spot -> spot.canAccommodate(vehicleType))
                .collect(Collectors.toList());

        if (candidateSpots.isEmpty()) {
            logger.warning(String.format("No available spots for vehicle type: %s", vehicleType));
            return null;
        }

        // Sort by floor (ascending) and then by size (ascending)
        // This ensures we use lower floors first and prefer smaller spots
        candidateSpots.sort(Comparator
                .comparingInt(ParkingSpot::getFloor)
                .thenComparingInt(ParkingSpot::getSizeUnits));

        // Try to allocate a spot using fine-grained locking
        for (ParkingSpot spot : candidateSpots) {
            spot.lock();
            try {
                // Double-check availability after acquiring lock
                if (spot.getStatus() == SpotStatus.AVAILABLE && 
                    vehicleType.canFitIn(spot.getSizeUnits())) {
                    logger.info(String.format("Allocated spot %s (floor %d, size %d) for %s",
                            spot.getSpotId(), spot.getFloor(), spot.getSizeUnits(), vehicleType));
                    return spot;
                }
            } finally {
                spot.unlock();
            }
        }

        // If we reach here, all spots were taken by concurrent requests
        logger.warning(String.format("Failed to allocate spot for %s - all spots taken", vehicleType));
        return null;
    }

    /**
     * Finds the best available spot using a more sophisticated algorithm.
     * This version considers multiple factors including proximity to exits.
     * 
     * @param parkingLot The parking lot to search
     * @param vehicleType Type of vehicle
     * @param preferredFloor Preferred floor (null if no preference)
     * @return Best available spot, or null if none available
     */
    public ParkingSpot allocateBestSpot(ParkingLot parkingLot, VehicleType vehicleType, Integer preferredFloor) {
        List<ParkingSpot> candidateSpots = parkingLot.getAllSpots().stream()
                .filter(spot -> spot.canAccommodate(vehicleType))
                .collect(Collectors.toList());

        if (candidateSpots.isEmpty()) {
            return null;
        }

        // Score spots based on multiple criteria
        candidateSpots.sort((s1, s2) -> {
            int score1 = calculateSpotScore(s1, vehicleType, preferredFloor);
            int score2 = calculateSpotScore(s2, vehicleType, preferredFloor);
            return Integer.compare(score2, score1); // Higher score is better
        });

        // Try to allocate the best spot
        for (ParkingSpot spot : candidateSpots) {
            spot.lock();
            try {
                if (spot.getStatus() == SpotStatus.AVAILABLE && 
                    vehicleType.canFitIn(spot.getSizeUnits())) {
                    return spot;
                }
            } finally {
                spot.unlock();
            }
        }

        return null;
    }

    /**
     * Calculates a score for a spot based on various factors.
     * Higher score = better spot.
     */
    private int calculateSpotScore(ParkingSpot spot, VehicleType vehicleType, Integer preferredFloor) {
        int score = 0;
        
        // Prefer lower floors (closer to entrance)
        score += (10 - spot.getFloor()) * 10;
        
        // Prefer spots that match vehicle size exactly (to reserve larger spots)
        int sizeDifference = spot.getSizeUnits() - vehicleType.getSizeUnits();
        score += (4 - sizeDifference) * 5;
        
        // Bonus for preferred floor
        if (preferredFloor != null && spot.getFloor() == preferredFloor) {
            score += 20;
        }
        
        return score;
    }

    /**
     * Gets statistics about spot availability for a vehicle type.
     */
    public Map<String, Object> getAvailabilityStats(ParkingLot parkingLot, VehicleType vehicleType) {
        Map<String, Object> stats = new HashMap<>();
        
        long total = parkingLot.getAllSpots().stream()
                .filter(s -> s.getSizeUnits() >= vehicleType.getSizeUnits())
                .count();
        
        long available = parkingLot.getAllSpots().stream()
                .filter(s -> s.canAccommodate(vehicleType))
                .count();
        
        stats.put("totalCompatibleSpots", total);
        stats.put("availableSpots", available);
        stats.put("occupiedSpots", total - available);
        
        return stats;
    }
}

