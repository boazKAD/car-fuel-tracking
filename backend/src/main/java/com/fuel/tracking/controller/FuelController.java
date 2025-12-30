package com.fuel.tracking.controller;

import com.fuel.tracking.dto.AddFuelRequest;
import com.fuel.tracking.dto.ApiResponse;
import com.fuel.tracking.dto.FuelEntryResponse;
import com.fuel.tracking.dto.FuelStatsResponse;
import com.fuel.tracking.mapper.FuelMapper;
import com.fuel.tracking.model.FuelEntry;
import com.fuel.tracking.model.FuelStats;
import com.fuel.tracking.service.FuelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cars/{carId}/fuel")
public class FuelController {

    private final FuelService fuelService;

    public FuelController(FuelService fuelService) {
        this.fuelService = fuelService;
    }

    /**
     * POST /api/cars/{carId}/fuel
     * Add a fuel entry for a specific car
     */
    @PostMapping
    public ResponseEntity<ApiResponse<FuelEntryResponse>> addFuelEntry(
            @PathVariable("carId") Long carId,
            @Valid @RequestBody AddFuelRequest request) {

        FuelEntry fuelEntry = FuelMapper.toEntity(request);
        FuelEntry createdEntry = fuelService.addFuelEntry(carId, fuelEntry);

        FuelEntryResponse response = mapToFuelEntryResponse(createdEntry);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Fuel entry added successfully", response));
    }

    /**
     * GET /api/cars/{carId}/fuel/stats
     * Get fuel statistics for a specific car
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<FuelStatsResponse>> getFuelStats(
            @PathVariable("carId") Long carId) {

        FuelStats stats = fuelService.calculateFuelStats(carId);
        FuelStatsResponse response = mapToFuelStatsResponse(stats);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Additional endpoint: Get all fuel entries for a car (optional)
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<FuelEntryResponse>>> getFuelEntries(
            @PathVariable("carId") Long carId) {

        List<FuelEntry> entries = fuelService.getFuelEntriesForCar(carId);

        List<FuelEntryResponse> response = entries.stream()
                .map(this::mapToFuelEntryResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /**
     * Helper methods for mapping entities to DTOs
     */
    private FuelEntryResponse mapToFuelEntryResponse(FuelEntry entry) {
        return new FuelEntryResponse(
                entry.getId(),
                entry.getLiters(),
                entry.getPricePerLiter(),
                entry.getTotalCost(),
                entry.getOdometer(),
                entry.getTimestamp());
    }

    private FuelStatsResponse mapToFuelStatsResponse(FuelStats stats) {
        return new FuelStatsResponse(
                stats.getTotalFuelLiters(),
                stats.getTotalCost(),
                stats.getAverageConsumption(),
                stats.getTotalEntries(),
                stats.getAveragePricePerLiter(),
                stats.getCostPer100km());
    }
}