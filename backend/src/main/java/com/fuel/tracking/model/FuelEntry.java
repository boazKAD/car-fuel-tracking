package com.fuel.tracking.model;

import java.time.LocalDateTime;

public class FuelEntry {
    private Long id;
    private Double liters;
    private Double pricePerLiter; // Price per liter
    private Integer odometer;
    private LocalDateTime timestamp;
    private Double totalCost; // Calculated: liters * pricePerLiter

    // Constructors
    public FuelEntry() {
        this.timestamp = LocalDateTime.now();
    }

    public FuelEntry(Double liters, Double pricePerLiter, Integer odometer) {
        this.liters = liters;
        this.pricePerLiter = pricePerLiter;
        this.odometer = odometer;
        this.timestamp = LocalDateTime.now();
        this.totalCost = liters * pricePerLiter;
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
        calculateTotalCost();
    }

    public Double getPricePerLiter() {
        return pricePerLiter;
    }

    public void setPricePerLiter(Double pricePerLiter) {
        this.pricePerLiter = pricePerLiter;
        calculateTotalCost();
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

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    // Helper method
    private void calculateTotalCost() {
        if (liters != null && pricePerLiter != null) {
            this.totalCost = liters * pricePerLiter;
        }
    }

    @Override
    public String toString() {
        return "FuelEntry{" +
                "id=" + id +
                ", liters=" + liters +
                ", pricePerLiter=" + pricePerLiter +
                ", odometer=" + odometer +
                ", totalCost=" + totalCost +
                ", timestamp=" + timestamp +
                '}';
    }
}