package com.fuel.tracking.cli.dto;

public class AddFuelCommand {
    private Long carId;
    private Double liters;
    private Double price;
    private Integer odometer;

    public AddFuelCommand() {
    }

    public AddFuelCommand(Long carId, Double liters, Double price, Integer odometer) {
        this.carId = carId;
        this.liters = liters;
        this.price = price;
        this.odometer = odometer;
    }

    // Getters and Setters
    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

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