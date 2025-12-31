package com.fuel.tracking.service;

import com.fuel.tracking.model.Car;
import com.fuel.tracking.model.FuelEntry;
import com.fuel.tracking.model.FuelStats;
import com.fuel.tracking.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class FuelService {

    private final CarRepository carRepository;

    public FuelService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public FuelEntry addFuelEntry(Long carId, FuelEntry fuelEntry) {
        validateFuelEntry(fuelEntry);

        // Check if car exists
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarService.CarNotFoundException("Car not found with id: " + carId));

        // Validate odometer reading is increasing
        validateOdometerReading(car, fuelEntry.getOdometer());

        return carRepository.addFuelEntryToCar(carId, fuelEntry);
    }

    public FuelStats calculateFuelStats(Long carId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarService.CarNotFoundException("Car not found with id: " + carId));

        List<FuelEntry> fuelEntries = car.getFuelEntries();

        if (fuelEntries.isEmpty()) {
            return FuelStats.builder()
                    .totalFuelLiters(0.0)
                    .totalCost(0.0)
                    .averageConsumption(0.0)
                    .totalEntries(0)
                    .averagePricePerLiter(0.0)
                    .costPer100km(0.0)
                    .build();
        }

        // Calculate basic totals
        double totalFuel = fuelEntries.stream()
                .mapToDouble(FuelEntry::getLiters)
                .sum();

        double totalCost = fuelEntries.stream()
                .mapToDouble(FuelEntry::getTotalCost)
                .sum();

        double averagePricePerLiter = totalFuel > 0 ? totalCost / totalFuel : 0;

        // Calculate average consumption (L/100km)
        double averageConsumption = calculateAverageConsumption(fuelEntries);

        // Calculate cost per 100km
        double costPer100km = averageConsumption * averagePricePerLiter;

        return FuelStats.builder()
                .totalFuelLiters(round(totalFuel, 1))
                .totalCost(round(totalCost, 2))
                .averageConsumption(round(averageConsumption, 1))
                .totalEntries(fuelEntries.size())
                .averagePricePerLiter(round(averagePricePerLiter, 2))
                .costPer100km(round(costPer100km, 2))
                .build();
    }

    /**
     * Calculate average fuel consumption in L/100km
     * Formula: (total_fuel_used / total_distance) * 100
     * Requires at least 2 fuel entries with valid odometer readings
     */
    private double calculateAverageConsumption(List<FuelEntry> fuelEntries) {
        if (fuelEntries.size() < 2) {
            return 0.0; // Not enough data to calculate
        }

        // Sort fuel entries by odometer (ascending)
        List<FuelEntry> sortedEntries = fuelEntries.stream()
                .sorted(Comparator.comparing(FuelEntry::getOdometer))
                .toList();

        double totalFuelUsed = 0;
        double totalDistance = 0;

        // Calculate fuel used between consecutive fill-ups
        for (int i = 1; i < sortedEntries.size(); i++) {
            FuelEntry previous = sortedEntries.get(i - 1);
            FuelEntry current = sortedEntries.get(i);

            double distance = current.getOdometer() - previous.getOdometer();
            double fuelUsed = current.getLiters();

            // Only include valid segments (positive distance)
            if (distance > 0) {
                totalDistance += distance;
                totalFuelUsed += fuelUsed;
            }
        }

        // Avoid division by zero
        if (totalDistance == 0) {
            return 0.0;
        }

        // Calculate consumption in L/100km
        return (totalFuelUsed / totalDistance) * 100;
    }

    private void validateFuelEntry(FuelEntry fuelEntry) {
        if (fuelEntry.getLiters() == null || fuelEntry.getLiters() <= 0) {
            throw new IllegalArgumentException("Fuel liters must be positive");
        }
        if (fuelEntry.getPricePerLiter() == null || fuelEntry.getPricePerLiter() <= 0) {
            throw new IllegalArgumentException("Price per liter must be positive");
        }
        if (fuelEntry.getOdometer() == null || fuelEntry.getOdometer() < 0) {
            throw new IllegalArgumentException("Odometer must be non-negative");
        }
    }

    private void validateOdometerReading(Car car, Integer newOdometer) {
        List<FuelEntry> fuelEntries = car.getFuelEntries();
        if (!fuelEntries.isEmpty()) {
            // Get the highest odometer reading so far
            Integer highestOdometer = fuelEntries.stream()
                    .map(FuelEntry::getOdometer)
                    .max(Integer::compareTo)
                    .orElse(0);

            if (newOdometer < highestOdometer) {
                throw new IllegalArgumentException(
                        String.format("New odometer reading (%d) is less than previous highest (%d). " +
                                "Odometer must always increase.",
                                newOdometer, highestOdometer));
            }
        }
    }

    private double round(double value, int decimalPlaces) {
        double scale = Math.pow(10, decimalPlaces);
        return Math.round(value * scale) / scale;
    }

    public List<FuelEntry> getFuelEntriesForCar(Long carId) {
        return carRepository.getFuelEntriesForCar(carId);
    }
}