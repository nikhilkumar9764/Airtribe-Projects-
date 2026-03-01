package com.airtribe.parking.service;

import com.airtribe.parking.model.VehicleType;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.logging.Logger;

/**
 * Service responsible for calculating parking fees based on duration and vehicle type.
 * Implements different pricing strategies.
 */
public class FeeCalculationService {
    private static final Logger logger = Logger.getLogger(FeeCalculationService.class.getName());

    // Minimum charge (even for less than 1 hour)
    private static final double MINIMUM_CHARGE_HOURS = 1.0;
    
    // Discount rates for longer stays
    private static final double DISCOUNT_AFTER_6_HOURS = 0.9;  // 10% discount
    private static final double DISCOUNT_AFTER_24_HOURS = 0.8; // 20% discount

    /**
     * Calculates the parking fee based on entry time, exit time, and vehicle type.
     * 
     * Pricing rules:
     * - Minimum charge: 1 hour at vehicle's hourly rate
     * - Hourly rate varies by vehicle type
     * - 10% discount for stays over 6 hours
     * - 20% discount for stays over 24 hours
     * 
     * @param entryTime When the vehicle entered
     * @param exitTime When the vehicle exited
     * @param vehicleType Type of vehicle
     * @return Calculated fee amount
     */
    public double calculateFee(LocalDateTime entryTime, LocalDateTime exitTime, VehicleType vehicleType) {
        if (entryTime == null || exitTime == null) {
            throw new IllegalArgumentException("Entry and exit times must not be null");
        }
        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException("Exit time cannot be before entry time");
        }

        // Calculate duration in hours (rounded up)
        long minutes = Duration.between(entryTime, exitTime).toMinutes();
        double hours = Math.max(MINIMUM_CHARGE_HOURS, Math.ceil(minutes / 60.0));

        // Get base hourly rate for vehicle type
        double baseRate = vehicleType.getHourlyRate();
        
        // Apply discounts for longer stays
        double discountMultiplier = 1.0;
        if (hours >= 24) {
            discountMultiplier = DISCOUNT_AFTER_24_HOURS;
            logger.info(String.format("Applying 20%% discount for %s (%.1f hours)", vehicleType, hours));
        } else if (hours >= 6) {
            discountMultiplier = DISCOUNT_AFTER_6_HOURS;
            logger.info(String.format("Applying 10%% discount for %s (%.1f hours)", vehicleType, hours));
        }

        double fee = hours * baseRate * discountMultiplier;
        
        logger.info(String.format("Fee calculated: %.2f for %s (%.1f hours, base rate: %.2f/hour)",
                fee, vehicleType, hours, baseRate));
        
        return Math.round(fee * 100.0) / 100.0; // Round to 2 decimal places
    }

    /**
     * Calculates fee for a completed transaction.
     */
    public double calculateFee(com.airtribe.parking.model.ParkingTransaction transaction) {
        if (!transaction.isCompleted()) {
            throw new IllegalStateException("Transaction must be completed to calculate fee");
        }
        return calculateFee(transaction.getEntryTime(), transaction.getExitTime(), 
                          transaction.getVehicle().getVehicleType());
    }
}

