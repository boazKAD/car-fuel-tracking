package com.fuel.tracking.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Car {
    private Long id;
    private String brand;
    private String model;
    private Integer year;
    private List<FuelEntry> fuelEntries = new ArrayList<>();
    private LocalDateTime createdAt;

    // Constructors
    public Car() {
        this.createdAt = LocalDateTime.now();
    }

    public Car(String brand, String model, Integer year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<FuelEntry> getFuelEntries() {
        return fuelEntries;
    }

    public void setFuelEntries(List<FuelEntry> fuelEntries) {
        this.fuelEntries = fuelEntries;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper methods
    public void addFuelEntry(FuelEntry fuelEntry) {
        this.fuelEntries.add(fuelEntry);
    }
}