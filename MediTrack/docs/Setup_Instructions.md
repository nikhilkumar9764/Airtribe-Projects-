# MediTrack - Setup Instructions

## Prerequisites

Before setting up MediTrack, ensure you have the following installed on your system:

1. **Java Development Kit (JDK) 8 or higher**
   - Recommended: JDK 11 or JDK 17
   - Download from: [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)

2. **Java Runtime Environment (JRE)**
   - Usually included with JDK installation
   - Required to run the compiled Java application

3. **Text Editor or IDE**
   - Recommended: IntelliJ IDEA, Eclipse, or VS Code with Java extensions

4. **Command Line Interface**
   - Windows: Command Prompt or PowerShell
   - macOS/Linux: Terminal

## Installation Steps

### Step 1: Verify Java Installation

Open your command line and run:

```bash
java -version
javac -version
```

You should see output similar to:
```
java version "17.0.x" 2023-xx-xx LTS
Java(TM) SE Runtime Environment (build 17.0.x+xx-LTS-xxx)
Java HotSpot(TM) 64-Bit Server VM (build 17.0.x+xx-LTS-xxx, mixed mode, sharing)

javac 17.0.x
```

If these commands fail, Java is not installed or not in your PATH.

### Step 2: Set JAVA_HOME Environment Variable

#### Windows:
1. Open System Properties → Advanced → Environment Variables
2. Under System Variables, click "New"
3. Variable name: `JAVA_HOME`
4. Variable value: Path to your JDK installation (e.g., `C:\Program Files\Java\jdk-17`)
5. Click OK

#### macOS/Linux:
Add to your `~/.bashrc` or `~/.zshrc`:
```bash
export JAVA_HOME=/path/to/your/jdk
export PATH=$JAVA_HOME/bin:$PATH
```

### Step 3: Verify Project Structure

Ensure your project structure matches:
```
MediTrack/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── airtribe/
│                   └── meditrack/
│                       ├── Main.java
│                       ├── constants/
│                       ├── entity/
│                       ├── service/
│                       ├── util/
│                       ├── exception/
│                       ├── interface_/
│                       ├── enums/
│                       ├── pattern/
│                       └── test/
└── docs/
```

### Step 4: Compile the Project

Navigate to the `MediTrack` directory and compile:

```bash
cd MediTrack
javac -d out -sourcepath src/main/java src/main/java/com/airtribe/meditrack/**/*.java
```

Or compile from the `src/main/java` directory:

```bash
cd src/main/java
javac com/airtribe/meditrack/**/*.java
```

### Step 5: Run the Application

#### Run Main Application:
```bash
java -cp out com.airtribe.meditrack.Main
```

Or with data loading:
```bash
java -cp out com.airtribe.meditrack.Main --loadData
```

#### Run Test Runner:
```bash
java -cp out com.airtribe.meditrack.test.TestRunner
```

## IDE Setup (IntelliJ IDEA)

1. **Open Project**
   - File → Open → Select `MediTrack` folder

2. **Configure SDK**
   - File → Project Structure → Project
   - Set Project SDK to your JDK version
   - Set Project language level to match JDK

3. **Set Source Folders**
   - File → Project Structure → Modules
   - Mark `src/main/java` as Sources

4. **Run Configuration**
   - Run → Edit Configurations
   - Add Application configuration
   - Main class: `com.airtribe.meditrack.Main`
   - Working directory: `MediTrack`

## Troubleshooting

### Issue: "javac: command not found"
**Solution**: Java is not in your PATH. Add JDK bin directory to PATH.

### Issue: "Error: Could not find or load main class"
**Solution**: 
- Ensure you're running from the correct directory
- Check that classpath includes compiled classes
- Verify package structure matches directory structure

### Issue: "UnsupportedClassVersionError"
**Solution**: Your JDK version is lower than the version used to compile. Upgrade JDK.

### Issue: "Package does not exist"
**Solution**: 
- Verify all source files are present
- Check package declarations match directory structure
- Recompile all classes

## Verification Checklist

- [ ] Java is installed and accessible from command line
- [ ] JAVA_HOME is set correctly
- [ ] All source files are in correct package structure
- [ ] Project compiles without errors
- [ ] Main application runs successfully
- [ ] Test runner executes all tests

## Next Steps

After successful setup:
1. Review the [JVM_Report.md](JVM_Report.md) for JVM understanding
2. Read [Design_Decisions.md](Design_Decisions.md) for architecture details
3. Run the TestRunner to verify all features
4. Explore the Main menu to interact with the application

## Screenshots

### Windows Setup Example:
![Windows Environment Variables](screenshots/windows_env_vars.png)
*Note: Add screenshots of your actual setup process*

### Compilation Success:
```
Compiling...
Compilation successful!
```

### Application Running:
```
=== MediTrack - Medical Appointment Management System ===

========== MAIN MENU ==========
1. Doctor Management
2. Patient Management
...
```

---

**Last Updated**: [Current Date]
**Java Version**: JDK 17 (or your version)
**Author**: MediTrack Development Team

