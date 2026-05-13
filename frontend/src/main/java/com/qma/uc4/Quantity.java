package com.qma.uc4;

/**
 * Quantity — unchanged API, extended via Enum.
 * Backward compatible: existing INCH/FEET still work identically.
 * Concepts: Scalability, DRY, Validation.
 */
public class Quantity {

    private final double value;
    private final LengthUnit unit;

    public Quantity(double value, LengthUnit unit) {
        if (value < 0) throw new IllegalArgumentException("Quantity value cannot be negative: " + value);
        this.value = value;
        this.unit = unit;
    }

    private double toBaseUnit() {
        return value * unit.baseUnitFactor;
    }

    public double getValue() { return value; }
    public LengthUnit getUnit() { return unit; }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Quantity)) return false;
        if (this == obj) return true;
        Quantity other = (Quantity) obj;
        return Double.compare(this.toBaseUnit(), other.toBaseUnit()) == 0;
    }

    @Override
    public int hashCode() { return Double.hashCode(toBaseUnit()); }

    @Override
    public String toString() { return value + " " + unit; }
}
