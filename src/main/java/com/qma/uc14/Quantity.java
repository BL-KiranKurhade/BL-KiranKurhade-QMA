package com.qma.uc14;

/**
 * Generic linear Quantity — implements both IConvertible and IArithmetic.
 * Backward Compatibility: all previous UC functionality preserved.
 */
public final class Quantity<T extends IUnit> implements IConvertible<Quantity<T>>, IArithmetic<Quantity<T>> {

    private final double value;
    private final T unit;

    public Quantity(double value, T unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        this.value = value;
        this.unit  = unit;
    }

    private double toBaseUnit() { return value * unit.getBaseUnitFactor(); }

    @Override
    public Quantity<T> convertTo(Quantity<T> target) {
        return new Quantity<>(toBaseUnit() / target.unit.getBaseUnitFactor(), target.unit);
    }

    public Quantity<T> convertTo(T target) {
        return new Quantity<>(toBaseUnit() / target.getBaseUnitFactor(), target);
    }

    @Override
    public Quantity<T> add(Quantity<T> other) {
        return new Quantity<>((toBaseUnit() + other.toBaseUnit()) / unit.getBaseUnitFactor(), unit);
    }

    @Override
    public Quantity<T> subtract(Quantity<T> other) {
        return new Quantity<>((toBaseUnit() - other.toBaseUnit()) / unit.getBaseUnitFactor(), unit);
    }

    public double getValue() { return value; }
    public T getUnit()       { return unit; }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Quantity)) return false;
        Quantity<?> other = (Quantity<?>) obj;
        if (!this.unit.getCategory().equals(other.unit.getCategory())) return false;
        return Double.compare(this.toBaseUnit(), other.toBaseUnit()) == 0;
    }

    @Override public int hashCode() { return Double.hashCode(toBaseUnit()); }
    @Override public String toString() { return String.format("%.4f %s", value, unit.getSymbol()); }
}
