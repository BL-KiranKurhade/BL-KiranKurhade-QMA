package com.qma.measurement.repository;
import com.qma.measurement.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement,Long> {
    List<Measurement> findByCategory(String category);
}
