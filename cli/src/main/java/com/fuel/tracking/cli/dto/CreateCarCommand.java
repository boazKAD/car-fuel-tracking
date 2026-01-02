package com.fuel.tracking.cli.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateCarCommand {

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("model")
    private String model;

    @JsonProperty("year")
    private Integer year;

    // MUST HAVE public no-args constructor
    public CreateCarCommand() {
    }

    public CreateCarCommand(String brand, String model, Integer year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }

    // MUST HAVE public getters and setters
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
}