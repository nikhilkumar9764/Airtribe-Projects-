#!/bin/bash
echo "Compiling MediTrack..."
javac -d out -sourcepath src/main/java src/main/java/com/airtribe/meditrack/**/*.java
if [ $? -eq 0 ]; then
    echo "Compilation successful!"
    echo ""
    echo "To run the application:"
    echo "  java -cp out com.airtribe.meditrack.Main"
    echo ""
    echo "To run tests:"
    echo "  java -cp out com.airtribe.meditrack.test.TestRunner"
else
    echo "Compilation failed!"
    exit 1
fi

