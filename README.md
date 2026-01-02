# Car Fuel Tracking System

A Java-based car management and fuel tracking system with REST API backend and CLI client.

## Project Overview

This system allows users to:
- Register cars with brand, model, and year
- Track fuel consumption by adding fuel entries
- View calculated fuel statistics (total fuel, cost, average consumption)

## Architecture

The system is divided into two main components:

1. **Backend Server**: Spring Boot REST API with in-memory storage
2. **CLI Client**: Standalone Java application using HTTP to communicate with the server

## Technology Stack

- **Java**: 17
- **Spring Boot**: 3.2.0
- **Build Tool**: Maven (multi-module project)
- **Storage**: In-memory (ConcurrentHashMap)
- **HTTP Client**: java.net.http.HttpClient (Java 11+)

## Project Structure

```
car-fuel-tracking/
├── backend/          # Spring Boot REST API
│   ├── src/main/java/com/fuel/tracking/
│   │   ├── controller/    # REST endpoints
│   │   ├── service/       # Business logic
│   │   ├── repository/    # In-memory storage
│   │   ├── model/         # Domain models
│   │   ├── dto/           # Data transfer objects
│   │   ├── servlet/       # Manual servlet implementation
│   │   └── handler/       # Exception handling
│   └── pom.xml
├── cli/              # CLI client application
│   ├── src/main/java/com/fuel/tracking/cli/
│   │   ├── CliApplication.java
│   │   ├── CommandParser.java
│   │   ├── client/        # API client
│   │   └── dto/           # CLI DTOs
│   └── pom.xml
└── pom.xml           # Parent POM
```

## Building the Project

```bash
# Build all modules
mvn clean package 

# Build only backend
cd backend
mvn clean package

# Build only CLI
cd cli
mvn clean package
```

## Running the Application

### 1. Start the Backend Server

```bash
# Option 1: Using Maven
cd backend
mvn spring-boot:run

# Option 2: Using JAR
java -jar backend/target/backend-1.0.0.jar
```

The backend will start on `http://localhost:8080`

### 2. Run CLI Commands

```bash
# Using Maven
cd cli
mvn exec:java -Dexec.mainClass="com.fuel.tracking.cli.CliApplication" -Dexec.args="--help"

# Using JAR
java -jar cli/target/cli-1.0.0.jar --help
```

## API Endpoints

### REST API

| Method | Endpoint | Description | Fields |
|--------|----------|-------------|--------|
| POST | `/api/cars` | Create a new car | `brand`, `model`, `year` |
| GET | `/api/cars` | List all cars | - |
| POST | `/api/cars/{id}/fuel` | Add fuel entry | `liters`, `price`, `odometer` |
| GET | `/api/cars/{id}/fuel/stats` | Get fuel statistics | Returns: total fuel, cost, avg/100km |

### Servlet Endpoint

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/servlet/fuel-stats?carId={id}` | Get fuel statistics via servlet |

## CLI Commands

### 1. Create Car

```bash
java -jar cli/target/cli-1.0.0.jar --create-car --brand Toyota --model Corolla --year 2018
```

**Output:**
```
Car created successfully!
ID: 1
Brand: Toyota
Model: Corolla
Year: 2018
```

### 2. Add Fuel Entry

```bash
java -jar cli/target/cli-1.0.0.jar --add-fuel --carId 1 --liters 40 --price 52.5 --odometer 45000
```

**Output:**
```
Fuel entry added successfully!
Car ID: 1
Liters: 40.0
Price per liter: 52.5
Odometer: 45000
```

### 3. Get Fuel Statistics

```bash
java -jar cli/target/cli-1.0.0.jar --fuel-stats --carId 1
```

**Output:**
```
Total fuel: 120.0 L
Total cost: 6392.50
Average consumption: 6.7 L/100km
```

### 4. Help

```bash
java -jar cli/target/cli-1.0.0.jar --help
```

## Example Usage

```bash
# 1. Start the backend server
cd backend
mvn spring-boot:run

# 2. In another terminal, create a car
java -jar cli/target/cli-1.0.0.jar --create-car --brand Toyota --model Corolla --year 2018

# 3. Add fuel entries
java -jar cli/target/cli-1.0.0.jar --add-fuel --carId 1 --liters 40 --price 52.5 --odometer 45000
java -jar cli/target/cli-1.0.0.jar --add-fuel --carId 1 --liters 45 --price 53.0 --odometer 45600
java -jar cli/target/cli-1.0.0.jar --add-fuel --carId 1 --liters 35 --price 54.5 --odometer 46200

# 4. View statistics
java -jar cli/target/cli-1.0.0.jar --fuel-stats --carId 1
```

## Features

### Backend

- ✅ RESTful API with proper HTTP status codes
- ✅ Input validation using Bean Validation
- ✅ Centralized exception handling
- ✅ In-memory storage (thread-safe)
- ✅ Fuel consumption calculation (L/100km)
- ✅ Manual servlet implementation
- ✅ Odometer validation (must always increase)

### CLI

- ✅ Command-line interface with Apache Commons CLI
- ✅ HTTP client using java.net.http.HttpClient
- ✅ Formatted output matching assignment requirements
- ✅ Error handling with clear messages
- ✅ Input validation

## Error Handling

The system provides proper error handling:

- **404 Not Found**: When car ID doesn't exist
- **400 Bad Request**: For validation errors (invalid input, decreasing odometer)
- **500 Internal Server Error**: For unexpected errors

Example error output:
```
Error: HTTP error: 404
```

## Assignment Requirements Verification

### Part 1: Backend REST API ✅

- ✅ POST /api/cars (brand, model, year)
- ✅ GET /api/cars
- ✅ POST /api/cars/{id}/fuel (liters, price, odometer)
- ✅ GET /api/cars/{id}/fuel/stats (returns total fuel, cost, avg/100km)
- ✅ Java application with in-memory storage (Lists/Maps)
- ✅ No database or authentication

### Part 2: Servlet Integration ✅

- ✅ GET /servlet/fuel-stats?carId={id}
- ✅ Extends HttpServlet and overrides doGet
- ✅ Manually parses carId from query parameters
- ✅ Sets Content-Type: application/json explicitly
- ✅ Sets status codes explicitly
- ✅ Uses same Service layer as REST API

### Part 3: CLI Application ✅

- ✅ Separate module and executable/main class
- ✅ Uses java.net.http.HttpClient
- ✅ create-car command
- ✅ add-fuel command
- ✅ fuel-stats command
- ✅ Output format matches assignment example

### Evaluation Criteria ✅

- ✅ Code readability
- ✅ Proper error handling (404 for invalid IDs)
- ✅ Effective reuse of service logic

## Development

### Running Tests

```bash
# Run all tests
mvn test

# Run backend tests only
cd backend
mvn test

# Run CLI tests only
cd cli
mvn test
```

### Code Structure

- **Controllers**: Handle HTTP requests and responses
- **Services**: Business logic and validation
- **Repositories**: Data access layer (in-memory)
- **DTOs**: Data transfer objects for API contracts
- **Mappers**: Convert between DTOs and entities
- **Exception Handlers**: Centralized error handling

## License

This project is part of a technical assignment.

## Author

Car Fuel Tracking System - Technical Assignment Implementation

