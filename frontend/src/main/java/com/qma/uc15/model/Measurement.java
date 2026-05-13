package com.qma.uc15.model;

/**
 * Domain Model — represents a single measurement.
 * Concepts: SRP, Immutability in Data Objects.
 */
public class Measurement {
    private Long id;
    private final double value;
    private final String unit;
    private final MeasurementCategory category;

    public Measurement(Long id, double value, String unit, MeasurementCategory category) {
        this.id = id; this.value = value; this.unit = unit; this.category = category;
    }

    public Long getId()                      { return id; }
    public void setId(Long id)               { this.id = id; }
    public double getValue()                 { return value; }
    public String getUnit()                  { return unit; }
    public MeasurementCategory getCategory() { return category; }

    @Override public String toString() {
        return String.format("Measurement{id=%d, value=%.2f, unit='%s', category=%s}", id, value, unit, category);
    }
}
