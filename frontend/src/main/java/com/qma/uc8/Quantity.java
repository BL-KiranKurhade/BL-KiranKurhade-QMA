package com.qma.uc8;

/**
 * Quantity depending on IUnit interface — Dependency Inversion.
 * Concepts: SRP, OCP, Delegation Pattern, Immutability, Architectural Scalability.
 */
public final class Quantity {

    private final double value;
    private final IUnit unit;

    public Quantity(double value, IUnit unit) {
        if (value < 0)   throw new IllegalArgumentException("Value cannot be negative");
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        this.value = value;
        this.unit  = unit;
    }

    private double toBaseUnit() {
        return value * unit.getBaseUnitFactor();
    }

    private void validateSameCategory(Quantity other) {
        if (!this.unit.getCategory().equals(other.unit.getCategory()))
            throw new IllegalArgumentException(
                "Cannot operate on different categories: " +
                this.unit.getCategory() + " vs " + other.unit.getCategory());
    }

    public Quantity convertTo(IUnit target) {
        if (target == null) throw new IllegalArgumentException("Target unit cannot be null");
        return new Quantity(toBaseUnit() / target.getBaseUnitFactor(), target);
    }

    public Quantity add(Quantity other) {
        validateSameCategory(other);
        double base = this.toBaseUnit() + other.toBaseUnit();
        return new Quantity(base / this.unit.getBaseUnitFactor(), this.unit);
    }

    public Quantity add(Quantity other, IUnit targetUnit) {
        validateSameCategory(other);
        double base = this.toBaseUnit() + other.toBaseUnit();
        return new Quantity(base / targetUnit.getBaseUnitFactor(), targetUnit);
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

    @Override
    public int hashCode() { return Double.hashCode(toBaseUnit()); }

    @Override
    public String toString() { return String.format("%.4f %s", value, unit.getSymbol()); }
}
