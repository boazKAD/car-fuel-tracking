package com.fuel.tracking.cli;

import com.fuel.tracking.cli.client.ApiClient;
import com.fuel.tracking.cli.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CliApplication {
    private static final Logger logger = LoggerFactory.getLogger(CliApplication.class);

    public static void main(String[] args) {
        try {
            CommandParser parser = new CommandParser();
            CommandParser.Command command = parser.parse(args);

            switch (command.getType()) {
                case HELP:
                    parser.printHelp();
                    break;

                case CREATE_CAR:
                    handleCreateCar(command);
                    break;

                case ADD_FUEL:
                    handleAddFuel(command);
                    break;

                case FUEL_STATS:
                    handleFuelStats(command);
                    break;
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            if (logger.isDebugEnabled()) {
                e.printStackTrace();
            }
            System.exit(1);
        }
    }

    private static void handleCreateCar(CommandParser.Command command) throws Exception {
        // Validate required fields
        validateRequired(command.getBrand(), "brand");
        validateRequired(command.getModel(), "model");
        validateRequired(command.getYear(), "year");

        // Parse year
        Integer year = command.getYearAsInt();

        // Create command DTO
        CreateCarCommand createCommand = new CreateCarCommand(
                command.getBrand(),
                command.getModel(),
                year);

        // Call API
        ApiClient apiClient = new ApiClient();
        CarResponse car = apiClient.createCar(createCommand);

        // Display result
        System.out.println("Car created successfully!");
        System.out.println("ID: " + car.getId());
        System.out.println("Brand: " + car.getBrand());
        System.out.println("Model: " + car.getModel());
        System.out.println("Year: " + car.getYear());
    }

    private static void handleAddFuel(CommandParser.Command command) throws Exception {
        // Validate required fields
        validateRequired(command.getCarId(), "carId");
        validateRequired(command.getLiters(), "liters");
        validateRequired(command.getPrice(), "price");
        validateRequired(command.getOdometer(), "odometer");

        // Parse values
        Long carId = command.getCarIdAsLong();
        Double liters = command.getLitersAsDouble();
        Double price = command.getPriceAsDouble();
        Integer odometer = command.getOdometerAsInt();

        // Create command DTO
        AddFuelCommand addFuelCommand = new AddFuelCommand(carId, liters, price, odometer);

        // Call API
        ApiClient apiClient = new ApiClient();
        apiClient.addFuel(addFuelCommand);

        // Display result
        System.out.println("Fuel entry added successfully!");
        System.out.println("Car ID: " + carId);
        System.out.println("Liters: " + liters);
        System.out.println("Price per liter: " + price);
        System.out.println("Odometer: " + odometer);
    }

    private static void handleFuelStats(CommandParser.Command command) throws Exception {
        // Validate required fields
        validateRequired(command.getCarId(), "carId");

        // Parse carId
        Long carId = command.getCarIdAsLong();

        // Call API
        ApiClient apiClient = new ApiClient();
        FuelStatsResponse stats = apiClient.getFuelStats(carId);

        // Display result in the format specified in assignment
        System.out.println("Total fuel: " + formatDouble(stats.getTotalFuelLiters(), 1) + " L");
        System.out.println("Total cost: " + formatDouble(stats.getTotalCost(), 2));
        System.out.println("Average consumption: " + formatDouble(stats.getAverageConsumption(), 1) + " L/100km");
    }

    private static void validateRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
    }

    private static String formatDouble(Double value, int decimalPlaces) {
        if (value == null) {
            return "0";
        }
        return String.format("%." + decimalPlaces + "f", value);
    }
}