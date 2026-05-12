package com.qma.uc15;

import com.qma.uc15.controller.MeasurementController;
import com.qma.uc15.dto.MeasurementResponse;
import com.qma.uc15.repository.InMemoryMeasurementRepository;
import com.qma.uc15.service.MeasurementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NTierTest {
    private MeasurementController controller;

    @BeforeEach void setUp() {
        controller = new MeasurementController(new MeasurementService(new InMemoryMeasurementRepository()));
    }

    @Test void testCreateAndGet() {
        MeasurementResponse resp = controller.create(1.0, "ft", "LENGTH");
        assertTrue(resp.isSuccess());
    }
    @Test void testGetAll() {
        controller.create(1.0, "ft", "LENGTH");
        controller.create(500.0, "g", "WEIGHT");
        MeasurementResponse resp = controller.getAll();
        assertTrue(resp.isSuccess());
    }
    @Test void testInvalidCategory() {
        MeasurementResponse resp = controller.create(1.0, "ft", "INVALID");
        assertFalse(resp.isSuccess());
    }
    @Test void testNotFound() {
        MeasurementResponse resp = controller.get(999L);
        assertFalse(resp.isSuccess());
    }
    @Test void testDelete() {
        controller.create(1.0, "ft", "LENGTH");
        MeasurementResponse del = controller.delete(1L);
        assertTrue(del.isSuccess());
        assertFalse(controller.get(1L).isSuccess());
    }
}
