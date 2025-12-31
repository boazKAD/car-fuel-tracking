package com.fuel.tracking.mapper;

import com.fuel.tracking.dto.CreateCarRequest;
import com.fuel.tracking.model.Car;

public class CarMapper {

    public static Car toEntity(CreateCarRequest request) {
        Car car = new Car();
        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setYear(request.getYear());
        return car;
    }
}