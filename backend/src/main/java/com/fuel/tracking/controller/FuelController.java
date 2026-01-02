package com.fuel.tracking.controller;

import com.fuel.tracking.dto.AddFuelRequest;
import com.fuel.tracking.dto.ApiResponse;
import com.fuel.tracking.dto.FuelStatsResponse;
import com.fuel.tracking.mapper.FuelMapper;
import com.fuel.tracking.model.FuelEntry;
import com.fuel.tracking.model.FuelStats;
import com.fuel.tracking.service.FuelService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse<Object>> addFuelEntry(
            @PathVariable("carId") Long carId,
            @Valid @RequestBody AddFuelRequest request) {

        FuelEntry fuelEntry = FuelMapper.toEntity(request);
        fuelService.addFuelEntry(carId, fuelEntry);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Fuel entry added successfully"));
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
     * Helper method for mapping FuelStats to DTO
     */
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