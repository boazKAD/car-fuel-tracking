package com.fuel.tracking.service;

import com.fuel.tracking.model.Car;
import com.fuel.tracking.model.FuelEntry;
import com.fuel.tracking.model.FuelStats;
import com.fuel.tracking.repository.CarRepository;
import com.fuel.tracking.repository.InMemoryCarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FuelServiceTest {

    private CarRepository carRepository;
    private CarService carService;
    private FuelService fuelService;

    @BeforeEach
    void setUp() {
        carRepository = new InMemoryCarRepository();
        carService = new CarService(carRepository);
        fuelService = new FuelService(carRepository);
    }

    @Test
    void testCalculateFuelStats_WithMultipleEntries() {
        // Create a car
        Car car = new Car("Toyota", "Corolla", 2018);
        car = carService.createCar(car);
        Long carId = car.getId();

        // Add fuel entries (simulating real fill-ups)
        fuelService.addFuelEntry(carId, new FuelEntry(40.0, 1.5, 10000)); // First fill
        fuelService.addFuelEntry(carId, new FuelEntry(35.0, 1.6, 10600)); // 600km, 35L
        fuelService.addFuelEntry(carId, new FuelEntry(42.0, 1.55, 11250)); // 650km, 42L

        // Calculate stats
        FuelStats stats = fuelService.calculateFuelStats(carId);

        // Verify calculations
        assertEquals(117.0, stats.getTotalFuelLiters(), 0.01); // 40 + 35 + 42
        assertEquals(40 * 1.5 + 35 * 1.6 + 42 * 1.55, stats.getTotalCost(), 0.01);
        assertEquals(3, stats.getTotalEntries());

        // Calculate expected consumption:
        // Segment 1: 10000 to 10600 = 600km, 35L = 5.83 L/100km
        // Segment 2: 10600 to 11250 = 650km, 42L = 6.46 L/100km
        // Average: (5.83 + 6.46) / 2 = 6.145 ≈ 6.1 L/100km
        assertEquals(6.1, stats.getAverageConsumption(), 0.1);
    }

    @Test
    void testCalculateFuelStats_WithSingleEntry() {
        Car car = new Car("Honda", "Civic", 2020);
        car = carService.createCar(car);

        fuelService.addFuelEntry(car.getId(), new FuelEntry(50.0, 1.7, 5000));

        FuelStats stats = fuelService.calculateFuelStats(car.getId());

        assertEquals(50.0, stats.getTotalFuelLiters(), 0.01);
        assertEquals(85.0, stats.getTotalCost(), 0.01); // 50 * 1.7
        assertEquals(1, stats.getTotalEntries());
        assertEquals(0.0, stats.getAverageConsumption(), 0.01); // Can't calculate with 1 entry
    }
}