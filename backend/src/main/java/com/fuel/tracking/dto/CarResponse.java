package com.fuel.tracking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarResponse {
    private Long id;
    private String brand;
    private String model;
    private Integer year;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private Integer fuelEntryCount;

    // Constructors
    public CarResponse() {
    }

    public CarResponse(Long id, String brand, String model, Integer year,
            LocalDateTime createdAt, Integer fuelEntryCount) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.createdAt = createdAt;
        this.fuelEntryCount = fuelEntryCount;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getFuelEntryCount() {
        return fuelEntryCount;
    }

    public void setFuelEntryCount(Integer fuelEntryCount) {
        this.fuelEntryCount = fuelEntryCount;
    }
}