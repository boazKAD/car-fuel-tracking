package com.fuel.tracking.repository;

import com.fuel.tracking.model.Car;
import com.fuel.tracking.model.FuelEntry;

import java.util.List;
import java.util.Optional;

public interface CarRepository {
    Car save(Car car);

    Optional<Car> findById(Long id);

    List<Car> findAll();

    boolean existsById(Long id);

    void delete(Long id);

    // Fuel entry methods
    FuelEntry addFuelEntryToCar(Long carId, FuelEntry fuelEntry);

    List<FuelEntry> getFuelEntriesForCar(Long carId);
}