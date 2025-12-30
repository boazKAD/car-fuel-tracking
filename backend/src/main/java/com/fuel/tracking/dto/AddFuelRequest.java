package com.fuel.tracking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class AddFuelRequest {

    @NotNull(message = "Liters is required")
    @Min(value = 0, message = "Liters must be positive")
    private Double liters;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be positive")
    private Double price;

    @NotNull(message = "Odometer is required")
    @Min(value = 0, message = "Odometer must be positive")
    private Integer odometer;

    // Getters and Setters
    public Double getLiters() {
        return liters;
    }

    public void setLiters(Double liters) {
        this.liters = liters;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getOdometer() {
        return odometer;
    }

    public void setOdometer(Integer odometer) {
        this.odometer = odometer;
    }
}