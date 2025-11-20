#!/bin/bash
# Auto-configure Java environment for this project

# Set JAVA_HOME to Java 21
export JAVA_HOME=/usr/lib/jvm/jdk-21.0.9-oracle-x64

# Make sure the java alternatives are set correctly for the system
if [ "$(readlink /etc/alternatives/java)" != "/usr/lib/jvm/jdk-21.0.9-oracle-x64/bin/java" ]; then
    echo "Setting Java alternatives to Java 21..."
    sudo update-alternatives --set java /usr/lib/jvm/jdk-21.0.9-oracle-x64/bin/java
fi

if [ "$(readlink /etc/alternatives/javac)" != "/usr/lib/jvm/jdk-21.0.9-oracle-x64/bin/javac" ]; then
    echo "Setting Javac alternatives to Java 21..."
    sudo update-alternatives --set javac /usr/lib/jvm/jdk-21.0.9-oracle-x64/bin/javac
fi

echo "Java environment configured for Sudoku Solver project:"
echo "JAVA_HOME: $JAVA_HOME"
java -version
mvn -version