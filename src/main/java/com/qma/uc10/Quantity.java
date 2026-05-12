package com.qma.uc10;

/**
 * Fully Generic Quantity<T extends IUnit>.
 * Concepts: Generic Programming, Type Erasure, Cross-Category Type Safety,
 * Composition Over Inheritance, Immutability, Runtime Type Checking,
 * Wildcards, Scalability.
 */
public final class Quantity<T extends IUnit> {

    private final double value;
    private final T unit;

    public Quantity(double value, T unit) {
        if (value < 0)   throw new IllegalArgumentException("Value cannot be negative");
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        this.value = value;
        this.unit  = unit;
    }

    private double toBaseUnit() { return value * unit.getBaseUnitFactor(); }

    private void ensureSameCategory(Quantity<?> other) {
        if (!this.unit.getCategory().equals(other.unit.getCategory()))
            throw new IllegalArgumentException(
                "Category mismatch: " + this.unit.getCategory() + " vs " + other.unit.getCategory());
    }

    /** Convert to another unit of the SAME type T. */
    public Quantity<T> convertTo(T target) {
        return new Quantity<>(toBaseUnit() / target.getBaseUnitFactor(), target);
    }

    /** Add — result in unit of THIS. */
    public Quantity<T> add(Quantity<T> other) {
        ensureSameCategory(other);
        double base = this.toBaseUnit() + other.toBaseUnit();
        return new Quantity<>(base / unit.getBaseUnitFactor(), unit);
    }

    /** Add — result in specified target unit. */
    public Quantity<T> add(Quantity<T> other, T targetUnit) {
        ensureSameCategory(other);
        double base = this.toBaseUnit() + other.toBaseUnit();
        return new Quantity<>(base / targetUnit.getBaseUnitFactor(), targetUnit);
    }

    public double getValue() { return value; }
    public T getUnit()       { return unit; }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Quantity)) return false;
        if (this == obj) return true;
        Quantity<?> other = (Quantity<?>) obj;
        if (!this.unit.getCategory().equals(other.unit.getCategory())) return false;
        return Double.compare(this.toBaseUnit(), other.toBaseUnit()) == 0;
    }

    @Override public int hashCode() { return Double.hashCode(toBaseUnit()); }
    @Override public String toString() { return String.format("%.4f %s", value, unit.getSymbol()); }
}
