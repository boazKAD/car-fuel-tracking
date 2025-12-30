package com.fuel.tracking.repository;

import com.fuel.tracking.model.FuelEntry;
import java.util.List;
import java.util.Optional;

public interface FuelEntryRepository {
    FuelEntry save(FuelEntry fuelEntry);

    Optional<FuelEntry> findById(Long id);

    List<FuelEntry> findByCarId(Long carId);

    void delete(Long id);

    Long getNextId();
}