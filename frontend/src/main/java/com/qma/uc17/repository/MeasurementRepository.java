package com.qma.uc17.repository;

import com.qma.uc17.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Spring Data JPA Repository.
 * Spring auto-generates CRUD SQL — zero boilerplate.
 */
@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findByCategory(String category);
    List<Measurement> findByUnit(String unit);
}
