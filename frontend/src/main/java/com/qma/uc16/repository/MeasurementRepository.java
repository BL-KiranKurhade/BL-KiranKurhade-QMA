package com.qma.uc16.repository;

import com.qma.uc16.model.Measurement;
import java.util.List;
import java.util.Optional;

/** Repository interface — Separation of Concerns. */
public interface MeasurementRepository {
    Measurement save(Measurement m);
    Optional<Measurement> findById(Long id);
    List<Measurement> findAll();
    List<Measurement> findByCategory(String category);
    int deleteById(Long id);
    int update(Measurement m);
}
