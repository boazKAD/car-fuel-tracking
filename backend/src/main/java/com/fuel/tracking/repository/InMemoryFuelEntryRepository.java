package com.fuel.tracking.repository;

import com.fuel.tracking.model.FuelEntry;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryFuelEntryRepository implements FuelEntryRepository {

    private final Map<Long, FuelEntry> fuelEntryStore = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public FuelEntry save(FuelEntry fuelEntry) {
        if (fuelEntry.getId() == null) {
            fuelEntry.setId(idGenerator.getAndIncrement());
        }
        fuelEntryStore.put(fuelEntry.getId(), fuelEntry);
        return fuelEntry;
    }

    @Override
    public Optional<FuelEntry> findById(Long id) {
        return Optional.ofNullable(fuelEntryStore.get(id));
    }

    @Override
    public List<FuelEntry> findByCarId(Long carId) {
        // We need to track which fuel entries belong to which car
        // This will be implemented differently - see alternative approach below
        return new ArrayList<>();
    }

    @Override
    public void delete(Long id) {
        fuelEntryStore.remove(id);
    }

    @Override
    public Long getNextId() {
        return idGenerator.get();
    }
}