package com.fuel.tracking.controller;

import com.fuel.tracking.dto.ApiResponse;
import com.fuel.tracking.dto.CarResponse;
import com.fuel.tracking.dto.CreateCarRequest;
import com.fuel.tracking.mapper.CarMapper;
import com.fuel.tracking.model.Car;
import com.fuel.tracking.service.CarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    /**
     * POST /api/cars
     * Create a new car
     */
    @PostMapping
    public ResponseEntity<ApiResponse<CarResponse>> createCar(
            @Valid @RequestBody CreateCarRequest request) {

        Car car = CarMapper.toEntity(request);
        Car createdCar = carService.createCar(car);

        CarResponse response = mapToCarResponse(createdCar);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Car created successfully", response));
    }

    /**
     * GET /api/cars
     * Get all cars
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CarResponse>>> getAllCars() {
        List<Car> cars = carService.getAllCars();

        List<CarResponse> response = cars.stream()
                .map(this::mapToCarResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Helper method to convert Car entity to CarResponse DTO
     */
    private CarResponse mapToCarResponse(Car car) {
        return new CarResponse(
                car.getId(),
                car.getBrand(),
                car.getModel(),
                car.getYear(),
                car.getCreatedAt(),
                car.getFuelEntries().size());
    }
}