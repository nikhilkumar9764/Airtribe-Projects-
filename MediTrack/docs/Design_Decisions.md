# MediTrack - Design Decisions

## Overview

This document outlines the key design decisions, architectural patterns, and implementation choices made in the MediTrack application.

## Table of Contents

1. [Package Structure](#package-structure)
2. [Object-Oriented Design](#object-oriented-design)
3. [Design Patterns](#design-patterns)
4. [Data Storage](#data-storage)
5. [Exception Handling](#exception-handling)
6. [File I/O Strategy](#file-io-strategy)
7. [Concurrency Considerations](#concurrency-considerations)
8. [Java 8+ Features](#java-8-features)
9. [Testing Strategy](#testing-strategy)

---

## Package Structure

### Rationale

The package structure follows Java naming conventions and logical separation of concerns:

```
com.airtribe.meditrack/
├── entity/          # Domain models
├── service/         # Business logic
├── util/            # Utility classes
├── exception/       # Custom exceptions
├── interface_/      # Interfaces
├── enums/           # Enumeration types
├── constants/       # Application constants
├── pattern/         # Design pattern implementations
└── test/            # Test classes
```

**Decision**: Use `interface_` instead of `interfaces` to avoid Java keyword conflicts.

**Benefits**:
- Clear separation of concerns
- Easy navigation
- Scalable structure
- Follows Java best practices

---

## Object-Oriented Design

### 1. Encapsulation

**Implementation**:
- All entity classes use private fields with public getters/setters
- Validation logic centralized in `Validator` utility class
- Data access controlled through methods

**Example**:
```java
public class Person {
    private String id;
    private String name;
    
    public void setName(String name) {
        if (Validator.isValidName(name)) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Invalid name");
        }
    }
}
```

**Rationale**: 
- Prevents invalid data entry
- Centralizes validation logic
- Maintains data integrity

### 2. Inheritance

**Hierarchy**:
```
Person (abstract base class)
├── Doctor
└── Patient
```

**Decision**: Use abstract `Person` class for common attributes and behavior.

**Benefits**:
- Code reuse
- Consistent structure
- Easy to extend

**Example**:
```java
public abstract class Person {
    protected String id;
    protected String name;
    // Common fields and methods
}

public class Doctor extends Person {
    private String specialization;
    // Doctor-specific fields
}
```

### 3. Polymorphism

**Method Overloading**:
```java
// Search by ID
public Doctor searchDoctor(String id)

// Search by name
public List<Doctor> searchDoctor(String name, boolean byName)

// Search by specialization
public List<Doctor> searchDoctorBySpecialization(String specialization)
```

**Method Overriding**:
```java
// Different behavior in subclasses
@Override
public boolean matches(String query) {
    // Implementation specific to Doctor
}
```

**Interface Polymorphism**:
```java
// Both Doctor and Patient implement Searchable
Searchable searchable = new Doctor(...);
searchable.matches("query");
```

**Rationale**: Provides flexible and extensible search functionality.

### 4. Abstraction

**Abstract Class**:
```java
public abstract class MedicalEntity {
    public abstract String getEntityType();
    
    // Template method
    public final String getEntityInfo() {
        return String.format("%s [ID: %s]", getEntityType(), id);
    }
}
```

**Interfaces**:
- `Searchable`: For entities that can be searched
- `Payable`: For entities that can be billed

**Rationale**: 
- Defines contracts without implementation
- Enables polymorphism
- Supports multiple inheritance (interfaces)

---

## Design Patterns

### 1. Singleton Pattern

**Implementation**: `IdGenerator` class

**Two Variants**:
- **Eager Singleton**: Instance created at class loading
- **Lazy Singleton**: Instance created on first access (double-checked locking)

**Example**:
```java
// Eager
private static final IdGenerator EAGER_INSTANCE = new IdGenerator();

// Lazy
private static volatile IdGenerator LAZY_INSTANCE;
public static IdGenerator getLazyInstance() {
    if (LAZY_INSTANCE == null) {
        synchronized (IdGenerator.class) {
            if (LAZY_INSTANCE == null) {
                LAZY_INSTANCE = new IdGenerator();
            }
        }
    }
    return LAZY_INSTANCE;
}
```

**Rationale**: 
- Ensures single instance for ID generation
- Thread-safe ID generation
- Prevents ID conflicts

### 2. Factory Pattern

**Implementation**: `BillFactory` class

**Purpose**: Create different types of bills without exposing creation logic.

**Example**:
```java
Bill standardBill = BillFactory.createStandardBill(...);
Bill discountedBill = BillFactory.createBillWithDiscount(..., 10.0);
```

**Rationale**: 
- Encapsulates object creation
- Easy to extend with new bill types
- Centralizes creation logic

### 3. Strategy Pattern

**Implementation**: `BillingStrategy` interface

**Purpose**: Allow runtime selection of billing algorithms.

**Example**:
```java
BillingStrategy strategy = new StandardBillingStrategy();
double total = strategy.calculateBill(baseAmount);

BillingStrategy discountStrategy = new DiscountBillingStrategy(10.0);
double discountedTotal = discountStrategy.calculateBill(baseAmount);
```

**Rationale**: 
- Flexible billing calculations
- Easy to add new strategies
- Follows Open/Closed Principle

### 4. Template Method Pattern

**Implementation**: `TemplateBillingProcessor` abstract class

**Purpose**: Define algorithm structure with customizable steps.

**Example**:
```java
public abstract class TemplateBillingProcessor {
    public final Bill processBill(...) {
        validateBillData(...);
        double amount = calculateAmount(...); // Abstract
        Bill bill = createBill(...);
        applyAdditionalProcessing(bill); // Hook
        return bill;
    }
}
```

**Rationale**: 
- Defines algorithm skeleton
- Allows customization of specific steps
- Prevents code duplication

### 5. Observer Pattern

**Implementation**: `AppointmentNotifier` and `AppointmentObserver`

**Purpose**: Notify observers of appointment state changes.

**Example**:
```java
AppointmentNotifier notifier = new AppointmentNotifier();
notifier.addObserver(new ConsoleAppointmentObserver());
notifier.notifyAppointmentCreated(appointment);
```

**Rationale**: 
- Loose coupling between subject and observers
- Easy to add new notification mechanisms
- Supports event-driven architecture

---

## Data Storage

### Generic DataStore<T>

**Implementation**: `DataStore<T>` class

**Purpose**: Type-safe, generic storage for entities.

**Features**:
- Generic type parameter
- HashMap-based storage
- Stream-based filtering
- Thread-safe operations (with proper synchronization)

**Example**:
```java
DataStore<Patient> patientStore = new DataStore<>("Patient");
patientStore.add(patient.getId(), patient);
List<Patient> filtered = patientStore.filter(p -> p.getAge() > 18);
```

**Rationale**: 
- Type safety
- Reusable across entity types
- Modern Java features (generics, streams)

### Collections Used

- **ArrayList**: Dynamic lists for entities
- **HashMap**: Fast lookup by ID
- **HashSet**: Unique ID collections

---

## Exception Handling

### Custom Exceptions

**Exceptions Created**:
1. `AppointmentNotFoundException`: For missing appointments
2. `InvalidDataException`: For invalid input data

**Features**:
- Exception chaining support
- Context information (field names, values)
- Meaningful error messages

**Example**:
```java
try {
    appointment = appointmentService.getAppointment(id);
} catch (AppointmentNotFoundException e) {
    System.out.println("Error: " + e.getMessage());
    System.out.println("Appointment ID: " + e.getAppointmentId());
}
```

**Rationale**: 
- Better error handling
- Meaningful error messages
- Exception chaining for debugging

### Try-With-Resources

**Usage**: File I/O operations

**Example**:
```java
try (BufferedReader reader = Files.newBufferedReader(path)) {
    // Read file
} // Automatically closes reader
```

**Rationale**: 
- Automatic resource management
- Prevents resource leaks
- Cleaner code

---

## File I/O Strategy

### CSV Format

**Decision**: Use CSV for data persistence

**Rationale**:
- Human-readable
- Easy to parse
- No external dependencies
- Simple implementation

**Implementation**:
- `CSVUtil` class handles all CSV operations
- Try-with-resources for file handling
- Proper escaping for special characters

**Example**:
```java
CSVUtil.savePatients(patients, Constants.PATIENTS_FILE);
List<Patient> loaded = CSVUtil.loadPatients(Constants.PATIENTS_FILE);
```

### Serialization (Optional)

**Note**: Java Serialization can be added for binary persistence.

**Trade-offs**:
- **CSV**: Human-readable, portable, simple
- **Serialization**: Faster, binary format, Java-specific

---

## Concurrency Considerations

### Thread Safety

**AtomicInteger**: Used in `IdGenerator` for thread-safe counters

**Example**:
```java
private final AtomicInteger patientCounter = new AtomicInteger(1);

public String generatePatientId() {
    return String.format("PAT%04d", patientCounter.getAndIncrement());
}
```

**Rationale**: 
- Thread-safe ID generation
- No synchronization overhead
- Prevents race conditions

### Synchronization

**Singleton Pattern**: Double-checked locking for lazy initialization

**Example**:
```java
if (LAZY_INSTANCE == null) {
    synchronized (IdGenerator.class) {
        if (LAZY_INSTANCE == null) {
            LAZY_INSTANCE = new IdGenerator();
        }
    }
}
```

**Rationale**: 
- Thread-safe singleton creation
- Performance optimization
- Prevents multiple instances

---

## Java 8+ Features

### Streams and Lambdas

**Usage**: Throughout the application

**Examples**:
```java
// Filter doctors by specialization
List<Doctor> cardiologists = doctors.stream()
    .filter(d -> d.getSpecialization().equals("Cardiology"))
    .collect(Collectors.toList());

// Calculate average fee
double avgFee = doctors.stream()
    .mapToDouble(Doctor::getConsultationFee)
    .average()
    .orElse(0.0);
```

**Rationale**: 
- Functional programming style
- Concise code
- Better readability
- Parallel processing support

### Default Methods in Interfaces

**Example**:
```java
public interface Searchable {
    boolean matches(String query);
    
    default boolean matchesIgnoreCase(String query) {
        return matches(query.toLowerCase());
    }
}
```

**Rationale**: 
- Backward compatibility
- Code reuse in interfaces
- Optional implementation

---

## Testing Strategy

### Manual Test Runner

**Implementation**: `TestRunner` class

**Approach**: Manual test execution without JUnit

**Rationale**: 
- No external dependencies
- Demonstrates testing concepts
- Simple assertion-based testing

**Test Categories**:
1. Encapsulation
2. Inheritance
3. Polymorphism
4. Abstraction
5. Cloning
6. Immutability
7. Enums
8. Collections
9. Exception Handling
10. File I/O
11. Concurrency
12. Design Patterns
13. Streams & Lambdas

---

## Advanced OOP Features

### Deep vs Shallow Copy

**Implementation**: `Cloneable` interface in `Patient` and `Appointment`

**Example**:
```java
Patient cloned = original.clone();
cloned.addAllergy("New Allergy");
// Original's allergies list is not affected (deep copy)
```

**Rationale**: 
- Demonstrates cloning concepts
- Prevents unintended side effects
- Proper object copying

### Immutability

**Implementation**: `BillSummary` class

**Features**:
- Final fields
- No setters
- Thread-safe
- Defensive copying (if needed)

**Example**:
```java
public final class BillSummary {
    private final String billId;
    private final double totalAmount;
    // No setters - immutable
}
```

**Rationale**: 
- Thread safety
- Prevents accidental modification
- Better for concurrent access

### Enums

**Usage**: `AppointmentStatus`, `Specialization`

**Benefits**:
- Type safety
- No magic strings
- Easy to extend
- Built-in methods

**Example**:
```java
public enum AppointmentStatus {
    CONFIRMED, PENDING, CANCELLED, COMPLETED
}
```

---

## Constants Management

### Constants Class

**Implementation**: `Constants` class with static final fields

**Purpose**: Centralized configuration

**Example**:
```java
public class Constants {
    public static final double TAX_RATE = 0.18;
    public static final String PATIENTS_FILE = "data/patients.csv";
}
```

**Rationale**: 
- Single source of truth
- Easy to modify
- Type-safe constants

---

## Summary

### Key Design Principles Applied:

1. **SOLID Principles**:
   - Single Responsibility: Each class has one purpose
   - Open/Closed: Extensible through interfaces and inheritance
   - Liskov Substitution: Subtypes are substitutable
   - Interface Segregation: Focused interfaces
   - Dependency Inversion: Depend on abstractions

2. **DRY (Don't Repeat Yourself)**: Utility classes, inheritance, generics

3. **Separation of Concerns**: Clear package structure, service layer

4. **Encapsulation**: Private fields, controlled access

5. **Polymorphism**: Interfaces, method overloading/overriding

### Trade-offs Made:

1. **CSV vs Database**: Chose CSV for simplicity, can be upgraded
2. **Manual Tests vs JUnit**: Manual tests for educational purposes
3. **In-Memory vs Persistent**: In-memory with optional file persistence
4. **Synchronous vs Async**: Synchronous for simplicity

### Future Enhancements:

1. Database integration (JDBC/JPA)
2. REST API (Spring Boot)
3. Unit tests with JUnit
4. Logging framework (Log4j/SLF4J)
5. Configuration files (properties/YAML)
6. Dependency injection framework

---

**Document Version**: 1.0
**Last Updated**: [Current Date]
**Author**: MediTrack Development Team

