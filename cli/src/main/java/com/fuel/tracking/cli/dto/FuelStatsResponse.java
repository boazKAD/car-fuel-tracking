package com.fuel.tracking.cli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FuelStatsResponse {
    private Double totalFuelLiters;
    private Double totalCost;
    private Double averageConsumption;
    private Integer totalEntries;
    private Double averagePricePerLiter;
    private Double costPer100km;

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