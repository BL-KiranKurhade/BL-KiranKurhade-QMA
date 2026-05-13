package com.qma.uc13;

/**
 * Quantity with centralized arithmetic via Lambda / Functional Interface.
 * Concepts: DRY Enforcement, Lambda, Functional Interface, Consistent Error Handling,
 * Method Extraction Refactoring, Single Source of Truth, Scalability.
 *
 * All arithmetic flows through a SINGLE private compute() method,
 * eliminating per-operation duplicated validation logic.
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

    /**
     * Central DRY arithmetic method.
     * ALL operations route through here — one place for validation + execution.
     */
    private Quantity<T> compute(Quantity<T> other, T targetUnit, ArithmeticOperation op) {
        if (other == null)    throw new IllegalArgumentException("Operand cannot be null");
        if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
        if (!this.unit.getCategory().equals(other.unit.getCategory()))
            throw new IllegalArgumentException("Category mismatch: "
                + this.unit.getCategory() + " vs " + other.unit.getCategory());
        double resultBase = op.apply(this.toBaseUnit(), other.toBaseUnit());
        return new Quantity<>(resultBase / targetUnit.getBaseUnitFactor(), targetUnit);
    }

    // ── Public API — all delegate to compute() ────────────────────────────

    public Quantity<T> add(Quantity<T> other) {
        return compute(other, unit, Operation.ADD::apply);
    }
    public Quantity<T> add(Quantity<T> other, T target) {
        return compute(other, target, Operation.ADD::apply);
    }

    public Quantity<T> subtract(Quantity<T> other) {
        return compute(other, unit, Operation.SUBTRACT::apply);
    }
    public Quantity<T> subtract(Quantity<T> other, T target) {
        return compute(other, target, Operation.SUBTRACT::apply);
    }

    /** Scalar multiply — uses lambda directly (no binary Quantity needed). */
    public Quantity<T> multiply(double factor) {
        return new Quantity<>(this.value * factor, this.unit);
    }

    /** Scalar divide — uses lambda directly. */
    public Quantity<T> divide(double divisor) {
        if (divisor == 0) throw new ArithmeticException("Division by zero");
        return new Quantity<>(this.value / divisor, this.unit);
    }

    /** Apply any custom lambda operation. */
    public Quantity<T> apply(Quantity<T> other, ArithmeticOperation customOp) {
        return compute(other, unit, customOp);
    }

    public Quantity<T> convertTo(T target) {
        return new Quantity<>(toBaseUnit() / target.getBaseUnitFactor(), target);
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
