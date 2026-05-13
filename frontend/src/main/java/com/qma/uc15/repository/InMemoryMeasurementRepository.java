package com.qma.uc15.repository;

import com.qma.uc15.model.Measurement;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/** In-memory implementation — swappable with JDBC/JPA in UC16+. */
public class InMemoryMeasurementRepository implements MeasurementRepository {

    private final Map<Long, Measurement> store = new LinkedHashMap<>();
    private final AtomicLong idSequence = new AtomicLong(1);

    @Override
    public Measurement save(Measurement m) {
        if (m.getId() == null) m.setId(idSequence.getAndIncrement());
        store.put(m.getId(), m);
        return m;
    }

    @Override
    public Optional<Measurement> findById(Long id) { return Optional.ofNullable(store.get(id)); }

    @Override
    public List<Measurement> findAll() { return new ArrayList<>(store.values()); }

    @Override
    public void deleteById(Long id) { store.remove(id); }
}
