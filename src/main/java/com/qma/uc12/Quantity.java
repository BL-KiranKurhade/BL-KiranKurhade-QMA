package com.qma.uc12;

/**
 * Quantity with full arithmetic: add, subtract, multiply, divide.
 * Concepts: Comprehensive Arithmetic, Non-Commutative Operations,
 * Division by Zero Handling, Target Unit Specification, Private Helper Methods,
 * Validation Consistency, Precision Handling, Method Overloading.
 */
public final class Quantity<T extends IUnit> {

    private final double value;
    private final T unit;

    public Quantity(double value, T unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        this.value = value;
        this.unit  = unit;
    }

    public double toBaseUnit() { return value * unit.getBaseUnitFactor(); }

    /** Private helper — validates category and sums base values. */
    private double combineBase(Quantity<T> other) {
        if (other == null) throw new IllegalArgumentException("Operand cannot be null");
        if (!this.unit.getCategory().equals(other.unit.getCategory()))
            throw new IllegalArgumentException("Category mismatch: "
                + this.unit.getCategory() + " vs " + other.unit.getCategory());
        return this.toBaseUnit() + other.toBaseUnit();
    }

    public Quantity<T> convertTo(T target) {
        return new Quantity<>(toBaseUnit() / target.getBaseUnitFactor(), target);
    }

    // ── Addition ──────────────────────────────────────────────────────────
    public Quantity<T> add(Quantity<T> other) {
        return new Quantity<>(combineBase(other) / unit.getBaseUnitFactor(), unit);
    }
    public Quantity<T> add(Quantity<T> other, T target) {
        return new Quantity<>(combineBase(other) / target.getBaseUnitFactor(), target);
    }

    // ── Subtraction (non-commutative) ─────────────────────────────────────
    public Quantity<T> subtract(Quantity<T> other) {
        if (other == null) throw new IllegalArgumentException("Operand cannot be null");
        if (!this.unit.getCategory().equals(other.unit.getCategory()))
            throw new IllegalArgumentException("Category mismatch");
        double base = this.toBaseUnit() - other.toBaseUnit();
        return new Quantity<>(base / unit.getBaseUnitFactor(), unit);
    }
    public Quantity<T> subtract(Quantity<T> other, T target) {
        if (other == null || target == null) throw new IllegalArgumentException("Null argument");
        if (!this.unit.getCategory().equals(other.unit.getCategory()))
            throw new IllegalArgumentException("Category mismatch");
        double base = this.toBaseUnit() - other.toBaseUnit();
        return new Quantity<>(base / target.getBaseUnitFactor(), target);
    }

    // ── Division ─────────────────────────────────────────────────────────
    public Quantity<T> divide(double divisor) {
        if (divisor == 0.0) throw new ArithmeticException("Division by zero is undefined");
        return new Quantity<>(this.value / divisor, this.unit);
    }

    // ── Multiply ────────────────────────────────────────────────────────
    public Quantity<T> multiply(double factor) {
        return new Quantity<>(this.value * factor, this.unit);
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
