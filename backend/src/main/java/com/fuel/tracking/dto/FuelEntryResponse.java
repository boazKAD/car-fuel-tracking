package com.fuel.tracking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class FuelEntryResponse {
    private Long id;
    private Double liters;
    private Double pricePerLiter;
    private Double totalCost;
    private Integer odometer;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    // Constructors
    public FuelEntryResponse() {
    }

    public FuelEntryResponse(Long id, Double liters, Double pricePerLiter,
            Double totalCost, Integer odometer, LocalDateTime timestamp) {
        this.id = id;
        this.liters = liters;
        this.pricePerLiter = pricePerLiter;
        this.totalCost = totalCost;
        this.odometer = odometer;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLiters() {
        return liters;
    }

    public void setLiters(Double liters) {
        this.liters = liters;
    }

    public Double getPricePerLiter() {
        return pricePerLiter;
    }

    public void setPricePerLiter(Double pricePerLiter) {
        this.pricePerLiter = pricePerLiter;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Integer getOdometer() {
        return odometer;
    }

    public void setOdometer(Integer odometer) {
        this.odometer = odometer;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}