package com.airtribe.parking;

import com.airtribe.parking.model.*;
import com.airtribe.parking.service.*;
import com.airtribe.parking.util.IdGenerator;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Main application class demonstrating the Smart Parking Lot system.
 * Shows vehicle entry/exit, spot allocation, fee calculation, and concurrency handling.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Smart Parking Lot Management System ===\n");

        // Initialize parking lot with multiple floors and spots
        ParkingLot parkingLot = createParkingLot();
        
        // Initialize services
        SpotAllocationService allocationService = new SpotAllocationService();
        FeeCalculationService feeService = new FeeCalculationService();
        ParkingService parkingService = new ParkingService(parkingLot, allocationService, feeService);

        // Display initial statistics
        System.out.println("Initial Parking Lot Status:");
        displayStatistics(parkingService);
        System.out.println();

        // Demo: Vehicle entries
        System.out.println("=== Vehicle Entries ===\n");
        
        Vehicle car1 = new Vehicle("ABC-1234", VehicleType.CAR, "John Doe");
        Vehicle motorcycle1 = new Vehicle("XYZ-5678", VehicleType.MOTORCYCLE, "Jane Smith");
        Vehicle suv1 = new Vehicle("DEF-9012", VehicleType.SUV, "Bob Johnson");
        Vehicle bus1 = new Vehicle("GHI-3456", VehicleType.BUS, "City Transit");

        LocalDateTime baseTime = LocalDateTime.now();

        // Check in vehicles
        ParkingTransaction txn1 = parkingService.checkIn(car1, baseTime);
        ParkingTransaction txn2 = parkingService.checkIn(motorcycle1, baseTime.plusMinutes(5));
        ParkingTransaction txn3 = parkingService.checkIn(suv1, baseTime.plusMinutes(10));
        ParkingTransaction txn4 = parkingService.checkIn(bus1, baseTime.plusMinutes(15));

        System.out.println("\n=== Current Status After Entries ===\n");
        displayStatistics(parkingService);
        System.out.println();

        // Display active transactions
        System.out.println("Active Transactions:");
        parkingService.getActiveTransactions().forEach(txn -> {
            System.out.printf("  %s: %s at %s (Floor %d) - Entry: %s\n",
                    txn.getVehicle().getLicensePlate(),
                    txn.getVehicle().getVehicleType(),
                    txn.getParkingSpot().getSpotId(),
                    txn.getParkingSpot().getFloor(),
                    txn.getEntryTime());
        });
        System.out.println();

        // Demo: Vehicle exits with fee calculation
        System.out.println("=== Vehicle Exits ===\n");

        // Car exits after 2 hours
        ParkingTransaction completed1 = parkingService.checkOut("ABC-1234", baseTime.plusHours(2));
        if (completed1 != null) {
            System.out.printf("Vehicle %s exited:\n", completed1.getVehicle().getLicensePlate());
            System.out.printf("  Spot: %s\n", completed1.getParkingSpot().getSpotId());
            System.out.printf("  Duration: %.1f hours\n", completed1.getDurationHours());
            System.out.printf("  Fee: $%.2f\n", completed1.getFee());
            System.out.println();
        }

        // Motorcycle exits after 30 minutes (minimum charge applies)
        ParkingTransaction completed2 = parkingService.checkOut("XYZ-5678", baseTime.plusMinutes(35));
        if (completed2 != null) {
            System.out.printf("Vehicle %s exited:\n", completed2.getVehicle().getLicensePlate());
            System.out.printf("  Spot: %s\n", completed2.getParkingSpot().getSpotId());
            System.out.printf("  Duration: %.1f hours (minimum charge)\n", completed2.getDurationHours());
            System.out.printf("  Fee: $%.2f\n", completed2.getFee());
            System.out.println();
        }

        // SUV exits after 7 hours (discount applies)
        ParkingTransaction completed3 = parkingService.checkOut("DEF-9012", baseTime.plusHours(7));
        if (completed3 != null) {
            System.out.printf("Vehicle %s exited:\n", completed3.getVehicle().getLicensePlate());
            System.out.printf("  Spot: %s\n", completed3.getParkingSpot().getSpotId());
            System.out.printf("  Duration: %.1f hours (10%% discount applied)\n", completed3.getDurationHours());
            System.out.printf("  Fee: $%.2f\n", completed3.getFee());
            System.out.println();
        }

        // Final statistics
        System.out.println("=== Final Status ===\n");
        displayStatistics(parkingService);

        // Demo: Availability check
        System.out.println("\n=== Availability Check ===\n");
        Map<String, Object> carAvailability = allocationService.getAvailabilityStats(parkingLot, VehicleType.CAR);
        System.out.println("Car Availability:");
        System.out.printf("  Total Compatible Spots: %d\n", carAvailability.get("totalCompatibleSpots"));
        System.out.printf("  Available Spots: %d\n", carAvailability.get("availableSpots"));
        System.out.printf("  Occupied Spots: %d\n", carAvailability.get("occupiedSpots"));
    }

    /**
     * Creates a sample parking lot with multiple floors and spots.
     */
    private static ParkingLot createParkingLot() {
        ParkingLot lot = new ParkingLot("PL-001", "Downtown Smart Parking");

        // Floor 0: Ground floor - mix of all sizes
        for (int i = 1; i <= 10; i++) {
            lot.addSpot(new ParkingSpot(IdGenerator.generateSpotId(0, i), 0, 4)); // Large spots
        }
        for (int i = 11; i <= 20; i++) {
            lot.addSpot(new ParkingSpot(IdGenerator.generateSpotId(0, i), 0, 2)); // Car spots
        }
        for (int i = 21; i <= 30; i++) {
            lot.addSpot(new ParkingSpot(IdGenerator.generateSpotId(0, i), 0, 1)); // Motorcycle spots
        }

        // Floor 1: Mostly cars and SUVs
        for (int i = 1; i <= 15; i++) {
            lot.addSpot(new ParkingSpot(IdGenerator.generateSpotId(1, i), 1, 3)); // SUV spots
        }
        for (int i = 16; i <= 40; i++) {
            lot.addSpot(new ParkingSpot(IdGenerator.generateSpotId(1, i), 1, 2)); // Car spots
        }

        // Floor 2: Mix
        for (int i = 1; i <= 5; i++) {
            lot.addSpot(new ParkingSpot(IdGenerator.generateSpotId(2, i), 2, 4)); // Large spots
        }
        for (int i = 6; i <= 30; i++) {
            lot.addSpot(new ParkingSpot(IdGenerator.generateSpotId(2, i), 2, 2)); // Car spots
        }

        return lot;
    }

    /**
     * Displays parking lot statistics.
     */
    private static void displayStatistics(ParkingService parkingService) {
        Map<String, Object> stats = parkingService.getParkingLotStats();
        System.out.printf("Total Spots: %d\n", stats.get("totalSpots"));
        System.out.printf("Available Spots: %d\n", stats.get("availableSpots"));
        System.out.printf("Occupied Spots: %d\n", stats.get("occupiedSpots"));
        System.out.printf("Active Vehicles: %d\n", stats.get("activeVehicles"));
        System.out.printf("Total Transactions: %d\n", stats.get("totalTransactions"));
    }
}

