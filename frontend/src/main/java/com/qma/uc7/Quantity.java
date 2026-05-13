package com.qma.uc7;

/**
 * Quantity with overloaded add() — caller can specify the result unit.
 * Concepts: Method Overloading, Private utility method, Flexibility in Result Representation,
 * Precision Across Unit Scales, Caller Intent Clarity, Immutability.
 */
public final class Quantity {

    private final double value;
    private final LengthUnit unit;

    public Quantity(double value, LengthUnit unit) {
        if (value < 0)   throw new IllegalArgumentException("Value cannot be negative");
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        this.value = value;
        this.unit  = unit;
    }

    private double toBaseUnit() {
        return value * unit.getToInchFactor();
    }

    /** Private utility: core addition in base units. */
    private double addBaseUnits(Quantity other) {
        if (other == null) throw new IllegalArgumentException("Operand cannot be null");
        return this.toBaseUnit() + other.toBaseUnit();
    }

    /** Add — result in unit of THIS quantity. */
    public Quantity add(Quantity other) {
        double base = addBaseUnits(other);
        return new Quantity(base / this.unit.getToInchFactor(), this.unit);
    }

    /** Add — result in SPECIFIED target unit (Method Overloading). */
    public Quantity add(Quantity other, LengthUnit targetUnit) {
        if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
        double base = addBaseUnits(other);
        return new Quantity(base / targetUnit.getToInchFactor(), targetUnit);
    }

    public Quantity convertTo(LengthUnit target) {
        return new Quantity(toBaseUnit() / target.getToInchFactor(), target);
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
