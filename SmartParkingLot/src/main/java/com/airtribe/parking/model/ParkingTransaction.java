package com.airtribe.parking.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a parking transaction (entry and exit) for a vehicle.
 */
public class ParkingTransaction {
    private final String transactionId;
    private final Vehicle vehicle;
    private final ParkingSpot parkingSpot;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double fee;
    private boolean isCompleted;

    public ParkingTransaction(String transactionId, Vehicle vehicle, ParkingSpot parkingSpot, LocalDateTime entryTime) {
        if (transactionId == null || transactionId.isBlank()) {
            throw new IllegalArgumentException("Transaction ID must not be blank");
        }
        this.transactionId = transactionId;
        this.vehicle = Objects.requireNonNull(vehicle, "Vehicle must not be null");
        this.parkingSpot = Objects.requireNonNull(parkingSpot, "Parking spot must not be null");
        this.entryTime = Objects.requireNonNull(entryTime, "Entry time must not be null");
        this.exitTime = null;
        this.fee = 0.0;
        this.isCompleted = false;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public double getFee() {
        return fee;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Completes the transaction with exit time and calculated fee.
     */
    public void complete(LocalDateTime exitTime, double fee) {
        if (exitTime == null) {
            throw new IllegalArgumentException("Exit time must not be null");
        }
        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException("Exit time cannot be before entry time");
        }
        if (fee < 0) {
            throw new IllegalArgumentException("Fee cannot be negative");
        }
        this.exitTime = exitTime;
        this.fee = fee;
        this.isCompleted = true;
    }

    /**
     * Calculates the duration in hours between entry and exit.
     * Returns 0 if transaction is not completed.
     */
    public double getDurationHours() {
        if (exitTime == null) {
            return 0.0;
        }
        long minutes = java.time.Duration.between(entryTime, exitTime).toMinutes();
        // Round up to nearest hour for billing
        return Math.ceil(minutes / 60.0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingTransaction that = (ParkingTransaction) o;
        return transactionId.equals(that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }

    @Override
    public String toString() {
        return "ParkingTransaction{" +
                "transactionId='" + transactionId + '\'' +
                ", vehicle=" + vehicle.getLicensePlate() +
                ", parkingSpot=" + parkingSpot.getSpotId() +
                ", entryTime=" + entryTime +
                ", exitTime=" + exitTime +
                ", fee=" + fee +
                ", isCompleted=" + isCompleted +
                '}';
    }
}

