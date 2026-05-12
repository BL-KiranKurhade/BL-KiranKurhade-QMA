package com.qma.uc15.repository;

import com.qma.uc15.model.Measurement;
import java.util.List;
import java.util.Optional;

/** Repository interface — Dependency Inversion Principle. */
public interface MeasurementRepository {
    Measurement save(Measurement measurement);
    Optional<Measurement> findById(Long id);
    List<Measurement> findAll();
    void deleteById(Long id);
}
