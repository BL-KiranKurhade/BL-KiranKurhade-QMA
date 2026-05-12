package com.qma.uc9;

/**
 * Generic Quantity — supports LENGTH and WEIGHT via IUnit.
 * Concepts: Scalable Design, Category Type Safety, Immutability, Equals/HashCode Contract,
 * Method Overloading, Arithmetic on Value Objects.
 */
public final class Quantity {

    private final double value;
    private final IUnit unit;

    public Quantity(double value, IUnit unit) {
        if (value < 0)   throw new IllegalArgumentException("Value cannot be negative: " + value);
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        this.value = value;
        this.unit  = unit;
    }

    private double toBaseUnit() { return value * unit.getBaseUnitFactor(); }

    private void ensureSameCategory(Quantity other) {
        if (!this.unit.getCategory().equals(other.unit.getCategory()))
            throw new IllegalArgumentException(
                "Category mismatch: " + this.unit.getCategory() + " vs " + other.unit.getCategory());
    }

    public Quantity convertTo(IUnit target) {
        return new Quantity(toBaseUnit() / target.getBaseUnitFactor(), target);
    }

    public Quantity add(Quantity other) {
        ensureSameCategory(other);
        return new Quantity((toBaseUnit() + other.toBaseUnit()) / unit.getBaseUnitFactor(), unit);
    }

    public Quantity add(Quantity other, IUnit target) {
        ensureSameCategory(other);
        return new Quantity((toBaseUnit() + other.toBaseUnit()) / target.getBaseUnitFactor(), target);
    }

    public double getValue() { return value; }
    public IUnit getUnit()   { return unit; }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Quantity)) return false;
        if (this == obj) return true;
        Quantity other = (Quantity) obj;
        if (!this.unit.getCategory().equals(other.unit.getCategory())) return false;
        return Double.compare(this.toBaseUnit(), other.toBaseUnit()) == 0;
    }

    @Override public int hashCode() { return Double.hashCode(toBaseUnit()); }
    @Override public String toString() { return String.format("%.4f %s", value, unit.getSymbol()); }
}
