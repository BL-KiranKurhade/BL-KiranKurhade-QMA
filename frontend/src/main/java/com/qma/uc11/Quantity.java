package com.qma.uc11;

/**
 * Generic Quantity<T> — now supports LENGTH, WEIGHT, and VOLUME.
 * Architectural Readiness Validation: adding VolumeUnit required zero changes here.
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
            throw new IllegalArgumentException("Category mismatch: "
                + this.unit.getCategory() + " vs " + other.unit.getCategory());
    }

    public Quantity<T> convertTo(T target) {
        return new Quantity<>(toBaseUnit() / target.getBaseUnitFactor(), target);
    }

    public Quantity<T> add(Quantity<T> other) {
        ensureSameCategory(other);
        return new Quantity<>((toBaseUnit() + other.toBaseUnit()) / unit.getBaseUnitFactor(), unit);
    }

    public Quantity<T> add(Quantity<T> other, T target) {
        ensureSameCategory(other);
        return new Quantity<>((toBaseUnit() + other.toBaseUnit()) / target.getBaseUnitFactor(), target);
    }

    public double getValue() { return value; }
    public T getUnit()       { return unit; }

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
