package com.airtribe.parking.model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Represents the entire parking lot with multiple floors and spots.
 * Thread-safe collection of parking spots.
 */
public class ParkingLot {
    private final String lotId;
    private final String name;
    private final Map<String, ParkingSpot> spots;  // spotId -> ParkingSpot
    private final Map<Integer, List<ParkingSpot>> spotsByFloor;  // floor -> List of spots

    public ParkingLot(String lotId, String name) {
        if (lotId == null || lotId.isBlank()) {
            throw new IllegalArgumentException("Lot ID must not be blank");
        }
        this.lotId = lotId;
        this.name = name != null ? name : "Parking Lot";
        this.spots = new ConcurrentHashMap<>();
        this.spotsByFloor = new ConcurrentHashMap<>();
    }

    public String getLotId() {
        return lotId;
    }

    public String getName() {
        return name;
    }

    /**
     * Adds a parking spot to the lot.
     */
    public void addSpot(ParkingSpot spot) {
        if (spot == null) {
            throw new IllegalArgumentException("Spot must not be null");
        }
        spots.put(spot.getSpotId(), spot);
        spotsByFloor.computeIfAbsent(spot.getFloor(), k -> new ArrayList<>()).add(spot);
    }

    /**
     * Gets a spot by its ID.
     */
    public ParkingSpot getSpot(String spotId) {
        return spots.get(spotId);
    }

    /**
     * Gets all spots in the lot.
     */
    public Collection<ParkingSpot> getAllSpots() {
        return Collections.unmodifiableCollection(spots.values());
    }

    /**
     * Gets all spots on a specific floor.
     */
    public List<ParkingSpot> getSpotsByFloor(int floor) {
        return Collections.unmodifiableList(
            spotsByFloor.getOrDefault(floor, Collections.emptyList())
        );
    }

    /**
     * Gets all available spots that can accommodate the given vehicle type.
     */
    public List<ParkingSpot> getAvailableSpots(VehicleType vehicleType) {
        return spots.values().stream()
                .filter(spot -> spot.canAccommodate(vehicleType))
                .collect(Collectors.toList());
    }

    /**
     * Gets statistics about the parking lot.
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalSpots", spots.size());
        stats.put("availableSpots", spots.values().stream()
                .filter(s -> s.getStatus() == SpotStatus.AVAILABLE)
                .count());
        stats.put("occupiedSpots", spots.values().stream()
                .filter(s -> s.getStatus() == SpotStatus.OCCUPIED)
                .count());
        stats.put("floors", spotsByFloor.size());
        return stats;
    }

    @Override
    public String toString() {
        return "ParkingLot{" +
                "lotId='" + lotId + '\'' +
                ", name='" + name + '\'' +
                ", totalSpots=" + spots.size() +
                '}';
    }
}

