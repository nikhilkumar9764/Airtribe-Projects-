# MediTrack - Medical Appointment Management System

A comprehensive Java application demonstrating core OOP concepts, design patterns, and Java features.

## Features

- **Core OOP**: Encapsulation, Inheritance, Polymorphism, Abstraction
- **Advanced OOP**: Deep/Shallow Copying, Immutability, Enums, Static Initialization
- **Collections & Generics**: Generic DataStore, ArrayList, HashMap
- **Exception Handling**: Custom exceptions, exception chaining, try-with-resources
- **File I/O**: CSV parsing, serialization support
- **Concurrency**: Thread-safe operations with AtomicInteger
- **Design Patterns**: Singleton, Factory, Strategy, Template Method, Observer
- **Java 8+ Features**: Streams, Lambdas, Default methods
- **Testing**: Manual test runner

## Project Structure

```
MediTrack/
├── src/main/java/com/airtribe/meditrack/
│   ├── Main.java                    # Main application entry point
│   ├── constants/
│   │   └── Constants.java           # Application constants
│   ├── entity/
│   │   ├── Person.java              # Base class
│   │   ├── Doctor.java              # Doctor entity
│   │   ├── Patient.java             # Patient entity (implements Cloneable)
│   │   ├── Appointment.java         # Appointment entity
│   │   ├── Bill.java                # Bill entity
│   │   ├── BillSummary.java         # Immutable bill summary
│   │   └── MedicalEntity.java       # Abstract class
│   ├── service/
│   │   ├── DoctorService.java       # Doctor business logic
│   │   ├── PatientService.java      # Patient business logic
│   │   └── AppointmentService.java # Appointment business logic
│   ├── util/
│   │   ├── Validator.java          # Validation utility
│   │   ├── DateUtil.java            # Date operations
│   │   ├── CSVUtil.java             # CSV file operations
│   │   ├── IdGenerator.java         # ID generation (Singleton)
│   │   ├── DataStore.java           # Generic data storage
│   │   └── AIHelper.java            # AI recommendations
│   ├── exception/
│   │   ├── AppointmentNotFoundException.java
│   │   └── InvalidDataException.java
│   ├── interface_/
│   │   ├── Searchable.java          # Search interface
│   │   └── Payable.java             # Payment interface
│   ├── enums/
│   │   ├── AppointmentStatus.java   # Appointment status enum
│   │   └── Specialization.java      # Doctor specialization enum
│   ├── pattern/
│   │   ├── BillFactory.java         # Factory pattern
│   │   ├── BillingStrategy.java     # Strategy pattern
│   │   ├── AppointmentObserver.java # Observer pattern
│   │   └── TemplateBillingProcessor.java # Template method pattern
│   └── test/
│       └── TestRunner.java          # Manual test runner
└── docs/
    ├── Setup_Instructions.md        # Setup guide
    ├── JVM_Report.md                # JVM internals report
    └── Design_Decisions.md          # Design documentation
```

## Quick Start

### Prerequisites

- Java JDK 8 or higher (recommended: JDK 11 or 17)
- Command line interface

### Compilation

```bash
cd MediTrack
javac -d out -sourcepath src/main/java src/main/java/com/airtribe/meditrack/**/*.java
```

### Running the Application

```bash
# Run main application
java -cp out com.airtribe.meditrack.Main

# Run with data loading
java -cp out com.airtribe.meditrack.Main --loadData

# Run test suite
java -cp out com.airtribe.meditrack.test.TestRunner
```

## Usage

### Main Menu Options

1. **Doctor Management**: Add, view, remove doctors
2. **Patient Management**: Add, view, remove patients
3. **Appointment Management**: Create, view, cancel, confirm appointments
4. **Billing**: Generate bills for completed appointments
5. **Search**: Search doctors and patients
6. **Analytics**: View statistics and analytics
7. **AI Recommendations**: Get doctor recommendations based on symptoms
8. **Save Data**: Save data to CSV files

## Key Concepts Demonstrated

### 1. Encapsulation
- Private fields with public getters/setters
- Centralized validation in `Validator` class

### 2. Inheritance
- `Person` → `Doctor`, `Patient`
- Constructor chaining with `super()`

### 3. Polymorphism
- Method overloading: `searchPatient()` by ID/name/age
- Method overriding: `matches()` in different classes
- Interface polymorphism: `Searchable`, `Payable`

### 4. Abstraction
- Abstract class: `MedicalEntity`
- Interfaces: `Searchable`, `Payable`

### 5. Advanced OOP
- **Cloning**: Deep copy in `Patient` and `Appointment`
- **Immutability**: `BillSummary` with final fields
- **Enums**: `AppointmentStatus`, `Specialization`
- **Static Blocks**: Initialization in multiple classes

### 6. Design Patterns
- **Singleton**: `IdGenerator` (eager & lazy)
- **Factory**: `BillFactory`
- **Strategy**: `BillingStrategy`
- **Template Method**: `TemplateBillingProcessor`
- **Observer**: `AppointmentNotifier`

### 7. Java 8+ Features
- **Streams**: Filtering, mapping, collecting
- **Lambdas**: Functional programming style
- **Default Methods**: In interfaces

### 8. Collections & Generics
- Generic `DataStore<T>` class
- ArrayList, HashMap usage
- Stream operations

### 9. Exception Handling
- Custom exceptions
- Exception chaining
- Try-with-resources

### 10. File I/O
- CSV reading/writing
- Try-with-resources
- Data persistence

## Testing

Run the test suite to verify all features:

```bash
java -cp out com.airtribe.meditrack.test.TestRunner
```

The test runner covers:
- Encapsulation
- Inheritance
- Polymorphism
- Abstraction
- Cloning
- Immutability
- Enums
- Collections
- Exception Handling
- File I/O
- Concurrency
- Design Patterns
- Streams & Lambdas

## Documentation

- **[Setup Instructions](docs/Setup_Instructions.md)**: Detailed setup guide
- **[JVM Report](docs/JVM_Report.md)**: JVM internals and architecture
- **[Design Decisions](docs/Design_Decisions.md)**: Architecture and design choices

## Example Usage

### Adding a Doctor
```
1. Doctor Management
1. Add Doctor
Enter name: Dr. Smith
Enter age: 45
Enter email: smith@example.com
Enter phone: +1234567890
Enter specialization: Cardiology
Enter consultation fee: 500.0
Enter years of experience: 15
```

### Creating an Appointment
```
3. Appointment Management
1. Create Appointment
Enter doctor ID: DOC0001
Enter patient ID: PAT0001
Enter appointment date and time (yyyy-MM-dd HH:mm): 2024-12-25 10:00
```

### AI Recommendations
```
7. AI Recommendations
Enter your symptoms: chest pain
[Recommended doctors based on symptoms]
```

## Data Persistence

Data can be saved to CSV files:
- `data/patients.csv`
- `data/doctors.csv`
- `data/appointments.csv`

Load data on startup:
```bash
java -cp out com.airtribe.meditrack.Main --loadData
```

## Grading Breakdown

- Environment Setup & JVM Understanding (10 pts)
- Package Structure & Java Basics (10 pts)
- Core OOP Implementation (35 pts)
- Application Logic (15 pts)
- Bonus Features (20 pts)
  - File I/O & Persistence (10 pts)
  - Design Patterns (10 pts)
  - AI Feature (10 pts)
  - Java Streams + Lambdas (10 pts)

## Requirements Met

✅ Java setup and JVM basics  
✅ Core OOP (encapsulation, inheritance, polymorphism, abstraction)  
✅ Advanced OOP (cloning, immutability, enums, static initialization)  
✅ Collections, generics, comparators, iterators, equals/hashCode  
✅ Exception handling (custom exceptions, chaining, try-with-resources)  
✅ File I/O, CSV parsing, serialization/deserialization  
✅ Intro to concurrency (threads, synchronization, AtomicInteger)  
✅ Design patterns (Singleton, Factory, Strategy, Template Method, Observer)  
✅ Java 8+ features (streams & lambdas)  
✅ Testing (manual runner), JavaDocs, command-line usage  
✅ Git-based collaboration ready  

## License

This project is created for educational purposes as part of the Airtribe MediTrack assignment.

## Author

MediTrack Development Team

---

**Version**: 1.0  
**Last Updated**: 2024

