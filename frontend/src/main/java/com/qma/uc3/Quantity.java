package com.qma.uc3;

/**
 * Generic quantity applying DRY principle.
 * A single class handles equality for any LengthUnit,
 * eliminating per-unit duplicate code.
 *
 * Concepts: DRY, Polymorphism, Abstraction, equals Override, SRP.
 */
public class Quantity {

    private final double value;
    private final LengthUnit unit;

    public Quantity(double value, LengthUnit unit) {
        this.value = value;
        this.unit = unit;
    }

    /** Convert to base unit (INCH) for universal comparison — DRY core logic. */
    private double toBaseUnit() {
        return value * unit.baseUnitFactor;
    }

    public double getValue() { return value; }
    public LengthUnit getUnit() { return unit; }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof Quantity)) return false;
        Quantity other = (Quantity) obj;
        return Double.compare(this.toBaseUnit(), other.toBaseUnit()) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(toBaseUnit());
    }

    @Override
    public String toString() {
        return value + " " + unit;
    }
}
