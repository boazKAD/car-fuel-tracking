package com.fuel.tracking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FuelStatsResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private Double totalFuelLiters;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private Double totalCost;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private Double averageConsumption;

    private Integer totalEntries;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private Double averagePricePerLiter;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private Double costPer100km;

    // Constructors
    public FuelStatsResponse() {
    }

    public FuelStatsResponse(Double totalFuelLiters, Double totalCost,
            Double averageConsumption, Integer totalEntries,
            Double averagePricePerLiter, Double costPer100km) {
        this.totalFuelLiters = totalFuelLiters;
        this.totalCost = totalCost;
        this.averageConsumption = averageConsumption;
        this.totalEntries = totalEntries;
        this.averagePricePerLiter = averagePricePerLiter;
        this.costPer100km = costPer100km;
    }

    // Getters and Setters
    public Double getTotalFuelLiters() {
        return totalFuelLiters;
    }

    public void setTotalFuelLiters(Double totalFuelLiters) {
        this.totalFuelLiters = totalFuelLiters;
    }

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getAverageConsumption() {
        return averageConsumption;
    }

    public void setAverageConsumption(Double averageConsumption) {
        this.averageConsumption = averageConsumption;
    }

    public Integer getTotalEntries() {
        return totalEntries;
    }

    public void setTotalEntries(Integer totalEntries) {
        this.totalEntries = totalEntries;
    }

    public Double getAveragePricePerLiter() {
        return averagePricePerLiter;
    }

    public void setAveragePricePerLiter(Double averagePricePerLiter) {
        this.averagePricePerLiter = averagePricePerLiter;
    }

    public Double getCostPer100km() {
        return costPer100km;
    }

    public void setCostPer100km(Double costPer100km) {
        this.costPer100km = costPer100km;
    }
}