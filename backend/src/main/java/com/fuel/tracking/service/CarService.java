package com.fuel.tracking.service;

import com.fuel.tracking.model.Car;
import com.fuel.tracking.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Car createCar(Car car) {
        validateCar(car);
        return carRepository.save(car);
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException("Car not found with id: " + id));
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public boolean carExists(Long id) {
        return carRepository.existsById(id);
    }

    private void validateCar(Car car) {
        if (car.getBrand() == null || car.getBrand().trim().isEmpty()) {
            throw new IllegalArgumentException("Car brand is required");
        }
        if (car.getModel() == null || car.getModel().trim().isEmpty()) {
            throw new IllegalArgumentException("Car model is required");
        }
        if (car.getYear() == null || car.getYear() < 1900 || car.getYear() > 2100) {
            throw new IllegalArgumentException("Car year must be between 1900 and 2100");
        }
    }

    // Custom exception for better error handling
    public static class CarNotFoundException extends RuntimeException {
        public CarNotFoundException(String message) {
            super(message);
        }
    }
}