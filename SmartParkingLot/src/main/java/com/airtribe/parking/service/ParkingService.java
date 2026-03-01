package com.airtribe.parking.service;

import com.airtribe.parking.model.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Main service for managing parking lot operations: entry, exit, and transaction tracking.
 * Handles concurrency through fine-grained locking and thread-safe collections.
 */
public class ParkingService {
    private static final Logger logger = Logger.getLogger(ParkingService.class.getName());

    private final ParkingLot parkingLot;
    private final SpotAllocationService allocationService;
    private final FeeCalculationService feeService;
    
    // Active transactions: licensePlate -> ParkingTransaction
    private final Map<String, ParkingTransaction> activeTransactions;
    
    // Transaction history: transactionId -> ParkingTransaction
    private final Map<String, ParkingTransaction> transactionHistory;
    
    // Vehicle location tracking: licensePlate -> ParkingSpot
    private final Map<String, ParkingSpot> vehicleLocations;

    public ParkingService(ParkingLot parkingLot, 
                         SpotAllocationService allocationService,
                         FeeCalculationService feeService) {
        this.parkingLot = parkingLot;
        this.allocationService = allocationService;
        this.feeService = feeService;
        this.activeTransactions = new ConcurrentHashMap<>();
        this.transactionHistory = new ConcurrentHashMap<>();
        this.vehicleLocations = new ConcurrentHashMap<>();
    }

    /**
     * Handles vehicle entry into the parking lot.
     * Allocates a spot, creates a transaction, and parks the vehicle.
     * 
     * Thread-safe: Uses fine-grained locking on individual spots.
     * 
     * @param vehicle Vehicle entering the lot
     * @param entryTime Entry timestamp
     * @return ParkingTransaction if successful, null if no spot available
     */
    public ParkingTransaction checkIn(Vehicle vehicle, LocalDateTime entryTime) {
        if (vehicle == null || entryTime == null) {
            throw new IllegalArgumentException("Vehicle and entry time must not be null");
        }

        // Check if vehicle is already parked
        if (vehicleLocations.containsKey(vehicle.getLicensePlate())) {
            throw new IllegalStateException(
                String.format("Vehicle %s is already parked", vehicle.getLicensePlate()));
        }

        // Allocate a spot
        ParkingSpot spot = allocationService.allocateSpot(parkingLot, vehicle.getVehicleType());
        if (spot == null) {
            logger.warning(String.format("No available spot for vehicle %s (%s)",
                    vehicle.getLicensePlate(), vehicle.getVehicleType()));
            return null;
        }

        // Lock the spot and park the vehicle
        spot.lock();
        try {
            // Double-check availability (another vehicle might have taken it)
            if (!spot.canAccommodate(vehicle.getVehicleType())) {
                logger.warning(String.format("Spot %s no longer available for %s",
                        spot.getSpotId(), vehicle.getLicensePlate()));
                return null;
            }

            // Park the vehicle
            spot.parkVehicle(vehicle);

            // Create transaction
            String transactionId = generateTransactionId();
            ParkingTransaction transaction = new ParkingTransaction(
                    transactionId, vehicle, spot, entryTime);

            // Record transaction and location
            activeTransactions.put(vehicle.getLicensePlate(), transaction);
            transactionHistory.put(transactionId, transaction);
            vehicleLocations.put(vehicle.getLicensePlate(), spot);

            logger.info(String.format("Vehicle %s checked in at spot %s (floor %d) - Transaction: %s",
                    vehicle.getLicensePlate(), spot.getSpotId(), spot.getFloor(), transactionId));

            return transaction;
        } finally {
            spot.unlock();
        }
    }

    /**
     * Handles vehicle exit from the parking lot.
     * Calculates fee, releases the spot, and completes the transaction.
     * 
     * Thread-safe: Uses fine-grained locking on individual spots.
     * 
     * @param licensePlate License plate of exiting vehicle
     * @param exitTime Exit timestamp
     * @return Completed ParkingTransaction with fee, or null if vehicle not found
     */
    public ParkingTransaction checkOut(String licensePlate, LocalDateTime exitTime) {
        if (licensePlate == null || licensePlate.isBlank() || exitTime == null) {
            throw new IllegalArgumentException("License plate and exit time must not be null");
        }

        // Find active transaction
        ParkingTransaction transaction = activeTransactions.get(licensePlate.toUpperCase());
        if (transaction == null) {
            logger.warning(String.format("No active transaction found for vehicle %s", licensePlate));
            return null;
        }

        ParkingSpot spot = transaction.getParkingSpot();
        Vehicle vehicle = transaction.getVehicle();

        // Lock the spot
        spot.lock();
        try {
            // Verify vehicle is actually parked here
            if (spot.getParkedVehicle() == null || 
                !spot.getParkedVehicle().getLicensePlate().equals(vehicle.getLicensePlate())) {
                logger.severe(String.format("Vehicle %s not found at spot %s", licensePlate, spot.getSpotId()));
                return null;
            }

            // Calculate fee
            double fee = feeService.calculateFee(transaction.getEntryTime(), exitTime, vehicle.getVehicleType());

            // Release the spot
            spot.releaseVehicle();

            // Complete transaction
            transaction.complete(exitTime, fee);

            // Remove from active transactions and locations
            activeTransactions.remove(licensePlate.toUpperCase());
            vehicleLocations.remove(licensePlate.toUpperCase());

            logger.info(String.format("Vehicle %s checked out from spot %s - Fee: $%.2f, Duration: %.1f hours - Transaction: %s",
                    licensePlate, spot.getSpotId(), fee, transaction.getDurationHours(), transaction.getTransactionId()));

            return transaction;
        } finally {
            spot.unlock();
        }
    }

    /**
     * Gets the current parking spot for a vehicle.
     */
    public ParkingSpot getVehicleLocation(String licensePlate) {
        return vehicleLocations.get(licensePlate.toUpperCase());
    }

    /**
     * Gets active transaction for a vehicle.
     */
    public ParkingTransaction getActiveTransaction(String licensePlate) {
        return activeTransactions.get(licensePlate.toUpperCase());
    }

    /**
     * Gets transaction history by transaction ID.
     */
    public ParkingTransaction getTransaction(String transactionId) {
        return transactionHistory.get(transactionId);
    }

    /**
     * Gets all active transactions.
     */
    public Collection<ParkingTransaction> getActiveTransactions() {
        return Collections.unmodifiableCollection(activeTransactions.values());
    }

    /**
     * Gets parking lot statistics.
     */
    public Map<String, Object> getParkingLotStats() {
        Map<String, Object> stats = parkingLot.getStatistics();
        stats.put("activeVehicles", activeTransactions.size());
        stats.put("totalTransactions", transactionHistory.size());
        return stats;
    }

    /**
     * Generates a unique transaction ID.
     */
    private String generateTransactionId() {
        return "TXN-" + System.currentTimeMillis() + "-" + 
               UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

