package com.fuel.tracking.mapper;

import com.fuel.tracking.dto.AddFuelRequest;
import com.fuel.tracking.model.FuelEntry;

public class FuelMapper {

    public static FuelEntry toEntity(AddFuelRequest request) {
        // Note: The request says "price" - assuming it's price per liter
        return new FuelEntry(
                request.getLiters(),
                request.getPrice(), // price per liter
                request.getOdometer());
    }
}