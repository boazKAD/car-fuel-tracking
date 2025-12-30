package com.fuel.tracking.repository;

import com.fuel.tracking.model.Car;
import com.fuel.tracking.model.FuelEntry;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryCarRepository implements CarRepository {

    private final Map<Long, Car> carStore = new ConcurrentHashMap<>();
    private final AtomicLong carIdGenerator = new AtomicLong(1);
    private final AtomicLong fuelEntryIdGenerator = new AtomicLong(1);

    @Override
    public Car save(Car car) {
        if (car.getId() == null) {
            car.setId(carIdGenerator.getAndIncrement());
        }
        carStore.put(car.getId(), car);
        return car;
    }

    @Override
    public Optional<Car> findById(Long id) {
        return Optional.ofNullable(carStore.get(id));
    }

    @Override
    public List<Car> findAll() {
        return new ArrayList<>(carStore.values());
    }

    @Override
    public boolean existsById(Long id) {
        return carStore.containsKey(id);
    }

    @Override
    public void delete(Long id) {
        carStore.remove(id);
    }

    @Override
    public FuelEntry addFuelEntryToCar(Long carId, FuelEntry fuelEntry) {
        Car car = carStore.get(carId);
        if (car == null) {
            throw new IllegalArgumentException("Car with id " + carId + " not found");
        }

        // Generate ID for fuel entry
        fuelEntry.setId(fuelEntryIdGenerator.getAndIncrement());

        // Add to car's fuel entries
        car.addFuelEntry(fuelEntry);

        // Update car in store
        carStore.put(carId, car);

        return fuelEntry;
    }

    @Override
    public List<FuelEntry> getFuelEntriesForCar(Long carId) {
        Car car = carStore.get(carId);
        if (car == null) {
            throw new IllegalArgumentException("Car with id " + carId + " not found");
        }
        return new ArrayList<>(car.getFuelEntries());
    }
}