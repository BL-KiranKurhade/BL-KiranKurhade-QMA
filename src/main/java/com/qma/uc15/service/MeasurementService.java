package com.qma.uc15.service;

import com.qma.uc15.dto.*;
import com.qma.uc15.model.*;
import com.qma.uc15.repository.MeasurementRepository;

/** Business logic layer — orchestrates between Repository and Controller. */
public class MeasurementService {

    private final MeasurementRepository repository;

    /** Dependency Injection via constructor. */
    public MeasurementService(MeasurementRepository repository) {
        this.repository = repository;
    }

    public MeasurementResponse create(MeasurementRequest request) {
        try {
            MeasurementCategory category = MeasurementCategory.valueOf(request.getCategory().toUpperCase());
            Measurement m = new Measurement(null, request.getValue(), request.getUnit(), category);
            Measurement saved = repository.save(m);
            return MeasurementResponse.ok(saved);
        } catch (IllegalArgumentException e) {
            return MeasurementResponse.error("Invalid category: " + request.getCategory());
        }
    }

    public MeasurementResponse getById(Long id) {
        return repository.findById(id)
            .map(MeasurementResponse::ok)
            .orElse(MeasurementResponse.error("Measurement not found: id=" + id));
    }

    public MeasurementResponse getAll() {
        return MeasurementResponse.ok(repository.findAll());
    }

    public MeasurementResponse delete(Long id) {
        if (repository.findById(id).isEmpty())
            return MeasurementResponse.error("Measurement not found: id=" + id);
        repository.deleteById(id);
        return MeasurementResponse.ok("Deleted id=" + id);
    }
}
