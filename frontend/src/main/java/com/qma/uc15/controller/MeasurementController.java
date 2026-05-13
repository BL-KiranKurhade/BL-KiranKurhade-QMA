package com.qma.uc15.controller;

import com.qma.uc15.dto.*;
import com.qma.uc15.service.MeasurementService;

/**
 * Presentation/Controller layer — entry point, delegates to service.
 * (In Spring this would be @RestController; here it's a plain facade.)
 */
public class MeasurementController {

    private final MeasurementService service;

    public MeasurementController(MeasurementService service) {
        this.service = service;
    }

    public MeasurementResponse create(double value, String unit, String category) {
        return service.create(new MeasurementRequest(value, unit, category));
    }

    public MeasurementResponse get(Long id)  { return service.getById(id); }
    public MeasurementResponse getAll()      { return service.getAll(); }
    public MeasurementResponse delete(Long id) { return service.delete(id); }
}
