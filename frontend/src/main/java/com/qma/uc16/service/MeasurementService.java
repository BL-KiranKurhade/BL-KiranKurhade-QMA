package com.qma.uc16.service;

import com.qma.uc16.model.Measurement;
import com.qma.uc16.repository.MeasurementRepository;
import java.util.*;

/** Service layer — business logic wrapping repository. */
public class MeasurementService {
    private final MeasurementRepository repo;
    public MeasurementService(MeasurementRepository repo) { this.repo = repo; }

    public Measurement save(double value, String unit, String category) {
        return repo.save(new Measurement(null, value, unit, category.toUpperCase()));
    }
    public Optional<Measurement> findById(Long id) { return repo.findById(id); }
    public List<Measurement> findAll()              { return repo.findAll(); }
    public List<Measurement> findByCategory(String cat) { return repo.findByCategory(cat); }
    public boolean delete(Long id) { return repo.deleteById(id) > 0; }
}
