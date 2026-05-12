package com.qma.uc5;

/**
 * Immutable Quantity with unit conversion support.
 * Concepts: Immutability, Value Object Semantics, Method Overloading, JavaDoc.
 */
public final class Quantity {

    private final double value;
    private final LengthUnit unit;

    /**
     * Creates a Quantity.
     * @param value the numeric magnitude (must be ≥ 0)
     * @param unit  the unit of measurement
     */
    public Quantity(double value, LengthUnit unit) {
        if (value < 0) throw new IllegalArgumentException("Value cannot be negative");
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        this.value = value;
        this.unit  = unit;
    }

    /** @return value in base unit (INCH) for comparison. */
    private double toBaseUnit() {
        return value * unit.getToInchFactor();
    }

    /**
     * Converts this Quantity to the specified target unit.
     * @param targetUnit the unit to convert to
     * @return new immutable Quantity in targetUnit
     */
    public Quantity convertTo(LengthUnit targetUnit) {
        if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
        double converted = unit.convertTo(value, targetUnit);
        return new Quantity(converted, targetUnit);
    }

    /** Method overloading — convert to same unit (no-op). */
    public Quantity convertTo() {
        return new Quantity(value, unit);
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
    public String toString() { return String.format("%.4f %s", value, unit.getSymbol()); }
}
