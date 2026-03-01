package com.airtribe.parking.model;

import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents a single parking spot in the parking lot.
 * Thread-safe with internal locking mechanism for concurrent access.
 */
public class ParkingSpot {
    private final String spotId;
    private final int floor;
    private final int sizeUnits;  // 1-4, determines what vehicle types can park here
    private volatile SpotStatus status;
    private volatile Vehicle parkedVehicle;
    private final ReentrantLock lock = new ReentrantLock();

    public ParkingSpot(String spotId, int floor, int sizeUnits) {
        if (spotId == null || spotId.isBlank()) {
            throw new IllegalArgumentException("Spot ID must not be blank");
        }
        if (floor < 0) {
            throw new IllegalArgumentException("Floor must be non-negative");
        }
        if (sizeUnits < 1 || sizeUnits > 4) {
            throw new IllegalArgumentException("Size units must be between 1 and 4");
        }
        this.spotId = spotId;
        this.floor = floor;
        this.sizeUnits = sizeUnits;
        this.status = SpotStatus.AVAILABLE;
        this.parkedVehicle = null;
    }

    public String getSpotId() {
        return spotId;
    }

    public int getFloor() {
        return floor;
    }

    public int getSizeUnits() {
        return sizeUnits;
    }

    public SpotStatus getStatus() {
        return status;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    /**
     * Acquires the lock for this spot. Must be called before any state modification.
     */
    public void lock() {
        lock.lock();
    }

    /**
     * Releases the lock for this spot.
     */
    public void unlock() {
        lock.unlock();
    }

    /**
     * Checks if this spot can accommodate the given vehicle type.
     */
    public boolean canAccommodate(VehicleType vehicleType) {
        return status == SpotStatus.AVAILABLE && vehicleType.canFitIn(sizeUnits);
    }

    /**
     * Parks a vehicle in this spot. Must be called while holding the lock.
     */
    public void parkVehicle(Vehicle vehicle) {
        if (status != SpotStatus.AVAILABLE) {
            throw new IllegalStateException("Spot is not available for parking");
        }
        if (!vehicle.getVehicleType().canFitIn(sizeUnits)) {
            throw new IllegalArgumentException("Vehicle cannot fit in this spot");
        }
        this.parkedVehicle = vehicle;
        this.status = SpotStatus.OCCUPIED;
    }

    /**
     * Removes the vehicle from this spot. Must be called while holding the lock.
     * @return The vehicle that was parked, or null if spot was empty
     */
    public Vehicle releaseVehicle() {
        if (status != SpotStatus.OCCUPIED) {
            return null;
        }
        Vehicle vehicle = this.parkedVehicle;
        this.parkedVehicle = null;
        this.status = SpotStatus.AVAILABLE;
        return vehicle;
    }

    /**
     * Sets the spot status (for maintenance, reservation, etc.)
     * Must be called while holding the lock.
     */
    public void setStatus(SpotStatus status) {
        if (status == SpotStatus.OCCUPIED && this.status != SpotStatus.OCCUPIED) {
            throw new IllegalStateException("Cannot set status to OCCUPIED without parking a vehicle");
        }
        if (status != SpotStatus.OCCUPIED && this.status == SpotStatus.OCCUPIED) {
            throw new IllegalStateException("Cannot change status while spot is occupied");
        }
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSpot that = (ParkingSpot) o;
        return spotId.equals(that.spotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spotId);
    }

    @Override
    public String toString() {
        return "ParkingSpot{" +
                "spotId='" + spotId + '\'' +
                ", floor=" + floor +
                ", sizeUnits=" + sizeUnits +
                ", status=" + status +
                ", parkedVehicle=" + (parkedVehicle != null ? parkedVehicle.getLicensePlate() : "none") +
                '}';
    }
}

