package com.fuel.tracking.model;

import com.fasterxml.jackson.annotation.JsonFormat;

public class FuelStats {
    private Double totalFuelLiters;
    private Double totalCost;
    private Double averageConsumption; // L/100km
    private Integer totalEntries;
    private Double averagePricePerLiter;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00")
    private Double costPer100km;

    // Constructors
    public FuelStats() {
    }

    public FuelStats(Double totalFuelLiters, Double totalCost,
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

    // Builder pattern for easier creation
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Double totalFuelLiters = 0.0;
        private Double totalCost = 0.0;
        private Double averageConsumption = 0.0;
        private Integer totalEntries = 0;
        private Double averagePricePerLiter = 0.0;
        private Double costPer100km = 0.0;

        public Builder totalFuelLiters(Double totalFuelLiters) {
            this.totalFuelLiters = totalFuelLiters;
            return this;
        }

        public Builder totalCost(Double totalCost) {
            this.totalCost = totalCost;
            return this;
        }

        public Builder averageConsumption(Double averageConsumption) {
            this.averageConsumption = averageConsumption;
            return this;
        }

        public Builder totalEntries(Integer totalEntries) {
            this.totalEntries = totalEntries;
            return this;
        }

        public Builder averagePricePerLiter(Double averagePricePerLiter) {
            this.averagePricePerLiter = averagePricePerLiter;
            return this;
        }

        public Builder costPer100km(Double costPer100km) {
            this.costPer100km = costPer100km;
            return this;
        }

        public FuelStats build() {
            return new FuelStats(totalFuelLiters, totalCost, averageConsumption,
                    totalEntries, averagePricePerLiter, costPer100km);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "Total fuel: %.1f L\n" +
                        "Total cost: %.2f\n" +
                        "Average consumption: %.1f L/100km\n" +
                        "Total entries: %d\n" +
                        "Average price per liter: %.2f\n" +
                        "Cost per 100km: %.2f",
                totalFuelLiters, totalCost, averageConsumption,
                totalEntries, averagePricePerLiter, costPer100km);
    }
}