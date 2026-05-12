package com.qma.uc6;

/**
 * Quantity with addition support.
 * Concepts: Arithmetic on Value Objects, Immutability, Normalization to Base Unit,
 * Commutativity, Precision and Rounding.
 */
public final class Quantity {

    private final double value;
    private final LengthUnit unit;

    public Quantity(double value, LengthUnit unit) {
        if (value < 0)  throw new IllegalArgumentException("Value cannot be negative");
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        this.value = value;
        this.unit  = unit;
    }

    private double toBaseUnit() {
        return value * unit.getToInchFactor();
    }

    public Quantity convertTo(LengthUnit target) {
        double converted = toBaseUnit() / target.getToInchFactor();
        return new Quantity(converted, target);
    }

    /**
     * Adds two quantities. Result is in the unit of 'this'.
     * Normalization: both values converted to base unit before adding.
     */
    public Quantity add(Quantity other) {
        if (other == null) throw new IllegalArgumentException("Cannot add null quantity");
        double totalBase = this.toBaseUnit() + other.toBaseUnit();
        return new Quantity(totalBase / this.unit.getToInchFactor(), this.unit);
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
