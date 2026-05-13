package com.qma.uc17;

import com.qma.uc17.dto.MeasurementDTO;
import com.qma.uc17.service.MeasurementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MeasurementControllerTest {

    @Autowired MeasurementService service;

    @Test void testCreate() {
        MeasurementDTO dto = new MeasurementDTO();
        dto.setValue(1.0); dto.setUnit("ft"); dto.setCategory("LENGTH");
        MeasurementDTO saved = service.create(dto);
        assertNotNull(saved.getId());
        assertEquals("LENGTH", saved.getCategory());
    }

    @Test void testGetAll() {
        assertNotNull(service.getAll());
    }
}
