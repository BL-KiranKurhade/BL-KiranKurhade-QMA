package com.qma.uc17.service;

import com.qma.uc17.dto.MeasurementDTO;
import com.qma.uc17.model.Measurement;
import com.qma.uc17.repository.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Spring Service — @Service enables component scanning + DI.
 * Concepts: Spring Services, DI, Spring Scopes.
 */
@Service
public class MeasurementService {

    private final MeasurementRepository repository;

    @Autowired
    public MeasurementService(MeasurementRepository repository) {
        this.repository = repository;
    }

    public MeasurementDTO create(MeasurementDTO dto) {
        Measurement m = repository.save(new Measurement(dto.getValue(), dto.getUnit(), dto.getCategory()));
        return toDTO(m);
    }

    public Optional<MeasurementDTO> getById(Long id) {
        return repository.findById(id).map(this::toDTO);
    }

    public List<MeasurementDTO> getAll() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<MeasurementDTO> getByCategory(String category) {
        return repository.findByCategory(category.toUpperCase()).stream()
            .map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<MeasurementDTO> update(Long id, MeasurementDTO dto) {
        return repository.findById(id).map(m -> {
            m.setValue(dto.getValue());
            m.setUnit(dto.getUnit());
            m.setCategory(dto.getCategory());
            return toDTO(repository.save(m));
        });
    }

    public boolean delete(Long id) {
        if (!repository.existsById(id)) return false;
        repository.deleteById(id);
        return true;
    }

    private MeasurementDTO toDTO(Measurement m) {
        return new MeasurementDTO(m.getId(), m.getValue(), m.getUnit(), m.getCategory());
    }
}
