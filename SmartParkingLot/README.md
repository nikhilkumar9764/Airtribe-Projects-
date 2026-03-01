# Smart Parking Lot Management System

A low-level architecture design and implementation for a smart parking lot backend system that handles vehicle entry/exit, parking space allocation, fee calculation, and real-time availability updates with robust concurrency handling.

## Table of Contents

- [Overview](#overview)
- [Architecture Design](#architecture-design)
- [Database Schema](#database-schema)
- [Core Components](#core-components)
- [Algorithms](#algorithms)
- [Concurrency Handling](#concurrency-handling)
- [Design Patterns](#design-patterns)
- [Class Diagram](#class-diagram)
- [Usage](#usage)
- [Features](#features)

## Overview

This system efficiently manages a multi-floor parking lot with various parking spot sizes. It automatically assigns parking spots based on vehicle size, tracks entry/exit times, calculates fees dynamically, and maintains real-time availability updates while handling concurrent vehicle operations.

### Key Features

- ✅ **Automatic Spot Allocation**: Intelligent algorithm assigns optimal spots based on vehicle size
- ✅ **Check-In/Check-Out Management**: Records entry and exit times with transaction tracking
- ✅ **Dynamic Fee Calculation**: Calculates fees based on duration and vehicle type with discount tiers
- ✅ **Real-Time Availability**: Updates spot availability instantly as vehicles enter/exit
- ✅ **Concurrency Handling**: Thread-safe operations using fine-grained locking
- ✅ **Multi-Floor Support**: Manages parking spots across multiple floors

## Architecture Design

### System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Client Layer                              │
│              (Main Application / API)                        │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                  Service Layer                               │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │   Parking    │  │    Spot      │  │     Fee      │     │
│  │   Service    │  │ Allocation   │  │ Calculation  │     │
│  │              │  │   Service    │  │   Service    │     │
│  └──────┬───────┘  └──────┬───────┘  └──────────────┘     │
│         │                 │                                 │
└─────────┼─────────────────┼─────────────────────────────────┘
          │                 │
┌─────────▼─────────────────▼─────────────────────────────────┐
│                    Model Layer                               │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐  │
│  │  Vehicle │  │ Parking  │  │ Parking  │  │ Parking  │  │
│  │          │  │   Spot   │  │   Lot    │  │Transaction│  │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘  │
└─────────────────────────────────────────────────────────────┘
```

### Layer Responsibilities

1. **Model Layer**: Core domain entities (Vehicle, ParkingSpot, ParkingLot, ParkingTransaction)
2. **Service Layer**: Business logic and orchestration
3. **Client Layer**: Application entry point and API interface

## Database Schema

### Entity Relationship Diagram

```
┌──────────────┐         ┌──────────────┐         ┌──────────────┐
│   Vehicle    │         │ ParkingSpot  │         │ ParkingLot   │
├──────────────┤         ├──────────────┤         ├──────────────┤
│ licensePlate │         │   spotId     │◄────────│   lotId      │
│ vehicleType  │         │   floor      │         │   name       │
│  ownerName   │         │  sizeUnits   │         └──────────────┘
└──────┬───────┘         │   status     │
       │                 │parkedVehicle │
       │                 └──────┬───────┘
       │                        │
       │                        │
┌──────▼────────────────────────▼───────┐
│      ParkingTransaction               │
├───────────────────────────────────────┤
│    transactionId (PK)                │
│    licensePlate (FK)                  │
│    spotId (FK)                        │
│    entryTime                          │
│    exitTime                           │
│    fee                                │
│    isCompleted                        │
└───────────────────────────────────────┘
```

### Tables Design

#### 1. **vehicles**
```sql
CREATE TABLE vehicles (
    license_plate VARCHAR(20) PRIMARY KEY,
    vehicle_type ENUM('MOTORCYCLE', 'CAR', 'SUV', 'BUS', 'TRUCK') NOT NULL,
    owner_name VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_vehicle_type ON vehicles(vehicle_type);
```

#### 2. **parking_lots**
```sql
CREATE TABLE parking_lots (
    lot_id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

#### 3. **parking_spots**
```sql
CREATE TABLE parking_spots (
    spot_id VARCHAR(50) PRIMARY KEY,
    lot_id VARCHAR(50) NOT NULL,
    floor INT NOT NULL,
    size_units INT NOT NULL CHECK (size_units BETWEEN 1 AND 4),
    status ENUM('AVAILABLE', 'OCCUPIED', 'RESERVED', 'MAINTENANCE') DEFAULT 'AVAILABLE',
    parked_vehicle_plate VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (lot_id) REFERENCES parking_lots(lot_id),
    FOREIGN KEY (parked_vehicle_plate) REFERENCES vehicles(license_plate),
    INDEX idx_lot_floor (lot_id, floor),
    INDEX idx_status_size (status, size_units),
    INDEX idx_parked_vehicle (parked_vehicle_plate)
);
```

#### 4. **parking_transactions**
```sql
CREATE TABLE parking_transactions (
    transaction_id VARCHAR(100) PRIMARY KEY,
    license_plate VARCHAR(20) NOT NULL,
    spot_id VARCHAR(50) NOT NULL,
    lot_id VARCHAR(50) NOT NULL,
    entry_time TIMESTAMP NOT NULL,
    exit_time TIMESTAMP,
    fee DECIMAL(10, 2) DEFAULT 0.00,
    duration_hours DECIMAL(5, 2),
    is_completed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (license_plate) REFERENCES vehicles(license_plate),
    FOREIGN KEY (spot_id) REFERENCES parking_spots(spot_id),
    FOREIGN KEY (lot_id) REFERENCES parking_lots(lot_id),
    INDEX idx_license_plate (license_plate),
    INDEX idx_spot_id (spot_id),
    INDEX idx_entry_time (entry_time),
    INDEX idx_is_completed (is_completed)
);
```

#### 5. **spot_availability_log** (Optional - for analytics)
```sql
CREATE TABLE spot_availability_log (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    spot_id VARCHAR(50) NOT NULL,
    status ENUM('AVAILABLE', 'OCCUPIED', 'RESERVED', 'MAINTENANCE') NOT NULL,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (spot_id) REFERENCES parking_spots(spot_id),
    INDEX idx_spot_changed (spot_id, changed_at)
);
```

## Core Components

### 1. Model Classes

#### **Vehicle**
- Represents a vehicle entering/exiting the parking lot
- Attributes: `licensePlate`, `vehicleType`, `ownerName`
- Immutable identifier: `licensePlate`

#### **VehicleType** (Enum)
- Defines vehicle types: MOTORCYCLE, CAR, SUV, BUS, TRUCK
- Each type has `sizeUnits` (1-4) and `hourlyRate`
- Method: `canFitIn(int spotSizeUnits)` - checks compatibility

#### **ParkingSpot**
- Represents a single parking spot
- Attributes: `spotId`, `floor`, `sizeUnits`, `status`, `parkedVehicle`
- **Thread-safe**: Uses `ReentrantLock` for concurrent access
- Methods: `parkVehicle()`, `releaseVehicle()`, `canAccommodate()`

#### **ParkingLot**
- Represents the entire parking facility
- Manages collection of parking spots
- Thread-safe: Uses `ConcurrentHashMap` for spot storage
- Methods: `addSpot()`, `getAvailableSpots()`, `getStatistics()`

#### **ParkingTransaction**
- Records entry/exit and fee information
- Attributes: `transactionId`, `vehicle`, `parkingSpot`, `entryTime`, `exitTime`, `fee`
- Methods: `complete()`, `getDurationHours()`

### 2. Service Classes

#### **ParkingService**
- **Primary service** for parking operations
- Handles `checkIn()` and `checkOut()` operations
- Manages active transactions and vehicle locations
- Thread-safe with concurrent collections

#### **SpotAllocationService**
- Implements spot allocation algorithm
- Methods: `allocateSpot()`, `allocateBestSpot()`, `getAvailabilityStats()`
- Uses greedy algorithm with floor and size preferences

#### **FeeCalculationService**
- Calculates parking fees based on duration and vehicle type
- Implements discount tiers (6+ hours: 10%, 24+ hours: 20%)
- Minimum charge: 1 hour

## Algorithms

### Spot Allocation Algorithm

The system uses a **greedy algorithm** with the following strategy:

```
Algorithm: AllocateSpot(parkingLot, vehicleType)
1. Filter all spots that can accommodate vehicleType
   - Spot status must be AVAILABLE
   - Spot sizeUnits >= vehicleType.sizeUnits
2. Sort candidate spots by:
   - Floor (ascending) - prefer lower floors
   - Size (ascending) - prefer smaller spots to reserve larger ones
3. For each candidate spot:
   a. Acquire lock on spot
   b. Double-check availability (double-checked locking)
   c. If available, allocate and return spot
   d. Release lock
4. Return null if no spot available
```

**Time Complexity**: O(n log n) where n = number of spots (due to sorting)
**Space Complexity**: O(n) for candidate list

**Optimization**: Uses fine-grained locking (per-spot) instead of global lock, allowing concurrent allocations on different spots.

### Fee Calculation Algorithm

```
Algorithm: CalculateFee(entryTime, exitTime, vehicleType)
1. Calculate duration in hours (rounded up)
   duration = ceil((exitTime - entryTime) / 60 minutes)
   minimum = max(duration, 1.0 hour)
2. Get base hourly rate from vehicleType
3. Apply discount multiplier:
   if duration >= 24 hours:
       multiplier = 0.8 (20% discount)
   else if duration >= 6 hours:
       multiplier = 0.9 (10% discount)
   else:
       multiplier = 1.0 (no discount)
4. Calculate fee = duration * baseRate * multiplier
5. Round to 2 decimal places
6. Return fee
```

**Fee Structure**:
- Motorcycle: $5/hour
- Car: $10/hour
- SUV: $12/hour
- Bus/Truck: $20/hour
- Minimum charge: 1 hour
- Discounts: 10% for 6+ hours, 20% for 24+ hours

## Concurrency Handling

### Thread Safety Mechanisms

1. **Fine-Grained Locking**:
   - Each `ParkingSpot` has its own `ReentrantLock`
   - Locks are acquired only when modifying spot state
   - Allows concurrent operations on different spots

2. **Thread-Safe Collections**:
   - `ConcurrentHashMap` for active transactions and vehicle locations
   - `ConcurrentHashMap` for parking lot spot storage
   - Atomic operations for ID generation

3. **Double-Checked Locking Pattern**:
   ```java
   spot.lock();
   try {
       if (spot.getStatus() == SpotStatus.AVAILABLE) {
           // Allocate spot
       }
   } finally {
       spot.unlock();
   }
   ```

4. **Volatile Fields**:
   - `status` and `parkedVehicle` in `ParkingSpot` are `volatile`
   - Ensures visibility across threads without full synchronization

### Concurrency Scenarios Handled

✅ **Multiple vehicles entering simultaneously**: Fine-grained locks prevent double-booking
✅ **Vehicle exiting while another is entering**: Lock ordering prevents deadlocks
✅ **Concurrent availability queries**: Read operations don't require locks
✅ **Transaction tracking**: ConcurrentHashMap ensures thread-safe updates

## Design Patterns

### 1. **Service Layer Pattern**
- Separation of concerns: Models contain data, Services contain logic
- `ParkingService` orchestrates operations across multiple services

### 2. **Strategy Pattern** (Potential Extension)
- `FeeCalculationService` could use different pricing strategies
- `SpotAllocationService` has `allocateBestSpot()` with scoring strategy

### 3. **Repository Pattern** (Potential Extension)
- Current implementation uses in-memory storage
- Can be extended with database repositories

### 4. **Factory Pattern** (Potential Extension)
- `IdGenerator` acts as a factory for unique IDs
- Could extend to factory for creating different vehicle types

## Class Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                         VehicleType                              │
│                         (Enum)                                   │
├─────────────────────────────────────────────────────────────────┤
│ + MOTORCYCLE, CAR, SUV, BUS, TRUCK                              │
│ + sizeUnits: int                                                 │
│ + hourlyRate: double                                             │
│ + canFitIn(spotSizeUnits: int): boolean                          │
└─────────────────────────────────────────────────────────────────┘
                              ▲
                              │
┌─────────────────────────────┴───────────────────────────────────┐
│                           Vehicle                                │
├──────────────────────────────────────────────────────────────────┤
│ - licensePlate: String                                           │
│ - vehicleType: VehicleType                                       │
│ - ownerName: String                                              │
│ + getLicensePlate(): String                                      │
│ + getVehicleType(): VehicleType                                  │
└──────────────────────────────────────────────────────────────────┘
                              │
                              │ uses
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                        ParkingSpot                               │
├──────────────────────────────────────────────────────────────────┤
│ - spotId: String                                                 │
│ - floor: int                                                     │
│ - sizeUnits: int                                                 │
│ - status: SpotStatus (volatile)                                  │
│ - parkedVehicle: Vehicle (volatile)                              │
│ - lock: ReentrantLock                                            │
│ + lock(): void                                                   │
│ + unlock(): void                                                 │
│ + canAccommodate(vehicleType: VehicleType): boolean              │
│ + parkVehicle(vehicle: Vehicle): void                            │
│ + releaseVehicle(): Vehicle                                      │
└──────────────────────────────────────────────────────────────────┘
                              │
                              │ contains
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                         ParkingLot                               │
├──────────────────────────────────────────────────────────────────┤
│ - lotId: String                                                  │
│ - name: String                                                   │
│ - spots: Map<String, ParkingSpot>                                │
│ - spotsByFloor: Map<Integer, List<ParkingSpot>>                 │
│ + addSpot(spot: ParkingSpot): void                              │
│ + getSpot(spotId: String): ParkingSpot                           │
│ + getAvailableSpots(vehicleType: VehicleType): List<ParkingSpot>│
│ + getStatistics(): Map<String, Object>                           │
└──────────────────────────────────────────────────────────────────┘
                              │
                              │ uses
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    ParkingTransaction                            │
├──────────────────────────────────────────────────────────────────┤
│ - transactionId: String                                          │
│ - vehicle: Vehicle                                               │
│ - parkingSpot: ParkingSpot                                       │
│ - entryTime: LocalDateTime                                       │
│ - exitTime: LocalDateTime                                        │
│ - fee: double                                                    │
│ - isCompleted: boolean                                           │
│ + complete(exitTime: LocalDateTime, fee: double): void           │
│ + getDurationHours(): double                                    │
└──────────────────────────────────────────────────────────────────┘
                              ▲
                              │
                              │ manages
┌─────────────────────────────┴───────────────────────────────────┐
│                       ParkingService                             │
├──────────────────────────────────────────────────────────────────┤
│ - parkingLot: ParkingLot                                         │
│ - allocationService: SpotAllocationService                       │
│ - feeService: FeeCalculationService                              │
│ - activeTransactions: Map<String, ParkingTransaction>            │
│ - transactionHistory: Map<String, ParkingTransaction>            │
│ - vehicleLocations: Map<String, ParkingSpot>                     │
│ + checkIn(vehicle: Vehicle, entryTime: LocalDateTime):           │
│     ParkingTransaction                                           │
│ + checkOut(licensePlate: String, exitTime: LocalDateTime):       │
│     ParkingTransaction                                           │
│ + getActiveTransaction(licensePlate: String): ParkingTransaction  │
│ + getParkingLotStats(): Map<String, Object>                      │
└──────────────────────────────────────────────────────────────────┘
                              │
                              │ uses
        ┌─────────────────────┴─────────────────────┐
        │                                           │
        ▼                                           ▼
┌───────────────────────────┐      ┌──────────────────────────────┐
│ SpotAllocationService      │      │ FeeCalculationService        │
├───────────────────────────┤      ├──────────────────────────────┤
│ + allocateSpot(parkingLot: │      │ + calculateFee(entryTime:    │
│   ParkingLot,              │      │   LocalDateTime,             │
│   vehicleType: VehicleType):│     │   exitTime: LocalDateTime,   │
│   ParkingSpot              │      │   vehicleType: VehicleType): │
│ + allocateBestSpot(...):   │      │   double                     │
│   ParkingSpot              │      │                              │
│ + getAvailabilityStats(...):│     └──────────────────────────────┘
│   Map<String, Object>      │
└───────────────────────────┘
```

## Usage

### Basic Usage Example

```java
// 1. Create parking lot
ParkingLot lot = new ParkingLot("PL-001", "Downtown Parking");

// 2. Add parking spots
for (int i = 1; i <= 10; i++) {
    lot.addSpot(new ParkingSpot("F0-S" + i, 0, 2)); // Floor 0, Car spots
}

// 3. Initialize services
SpotAllocationService allocationService = new SpotAllocationService();
FeeCalculationService feeService = new FeeCalculationService();
ParkingService parkingService = new ParkingService(lot, allocationService, feeService);

// 4. Check in a vehicle
Vehicle car = new Vehicle("ABC-1234", VehicleType.CAR, "John Doe");
ParkingTransaction txn = parkingService.checkIn(car, LocalDateTime.now());

// 5. Check out vehicle
ParkingTransaction completed = parkingService.checkOut("ABC-1234", LocalDateTime.now().plusHours(2));
System.out.println("Fee: $" + completed.getFee());
```

### Running the Demo

```bash
# Compile
javac -d out src/main/java/com/airtribe/parking/**/*.java

# Run
java -cp out com.airtribe.parking.Main
```

## Features

### ✅ Implemented Features

- [x] Vehicle entry/exit management
- [x] Automatic spot allocation based on vehicle size
- [x] Multi-floor parking lot support
- [x] Real-time availability tracking
- [x] Dynamic fee calculation with discount tiers
- [x] Transaction history tracking
- [x] Thread-safe concurrent operations
- [x] Comprehensive logging

### 🔄 Potential Extensions

- [ ] Database persistence layer
- [ ] REST API endpoints
- [ ] Reservation system for future parking
- [ ] Payment integration
- [ ] Analytics and reporting
- [ ] Mobile app integration
- [ ] Real-time notifications
- [ ] Multi-parking-lot management
- [ ] Advanced allocation strategies (ML-based)

## Technical Highlights

- **Concurrency**: Fine-grained locking prevents race conditions
- **Performance**: O(n log n) allocation with efficient sorting
- **Scalability**: Thread-safe design supports high concurrent load
- **Maintainability**: Clean separation of concerns, SOLID principles
- **Extensibility**: Easy to add new vehicle types, pricing strategies

## Author

Designed and implemented as part of low-level system architecture assignment.

## License

This project is for educational purposes.

