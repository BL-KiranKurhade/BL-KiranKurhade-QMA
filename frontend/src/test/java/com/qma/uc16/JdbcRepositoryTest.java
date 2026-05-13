package com.qma.uc16;

import com.qma.uc16.config.DatabaseConfig;
import com.qma.uc16.model.Measurement;
import com.qma.uc16.repository.JdbcMeasurementRepository;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class JdbcRepositoryTest {
    static JdbcMeasurementRepository repo;

    @BeforeAll static void setup() {
        DatabaseConfig.initializeSchema();
        repo = new JdbcMeasurementRepository();
    }

    @Test @Order(1)
    void testSave() {
        Measurement m = repo.save(new Measurement(null, 1.0, "ft", "LENGTH"));
        assertNotNull(m.getId());
    }

    @Test @Order(2)
    void testFindById() {
        repo.save(new Measurement(null, 500.0, "g", "WEIGHT"));
        List<Measurement> all = repo.findAll();
        Measurement found = repo.findById(all.get(0).getId()).orElseThrow();
        assertEquals("LENGTH", found.getCategory());
    }

    @Test @Order(3)
    void testFindByCategory() {
        List<Measurement> lengths = repo.findByCategory("LENGTH");
        assertFalse(lengths.isEmpty());
    }

    @Test @Order(4)
    void testDelete() {
        Measurement m = repo.save(new Measurement(null, 2.5, "l", "VOLUME"));
        int deleted = repo.deleteById(m.getId());
        assertEquals(1, deleted);
        assertTrue(repo.findById(m.getId()).isEmpty());
    }
}
