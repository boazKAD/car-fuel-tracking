#!/bin/bash

# Build the project
echo "Building project..."
mvn clean package -DskipTests

echo "========================================="
echo "1. To run backend: java -jar backend/target/backend-1.0.0.jar"
echo "2. To run CLI: java -jar cli/target/cli-1.0.0-jar-with-dependencies.jar"
echo "========================================="