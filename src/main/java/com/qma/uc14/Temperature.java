package com.qma.uc14;

/**
 * Temperature — immutable value object.
 * Implements IConvertible but NOT IArithmetic (ISP: arithmetic is meaningless for temperature).
 * Equality via conversion to Celsius pivot.
 * Concepts: ISP, Non-Linear Conversions, Absolute vs Relative, Default Methods,
 * Functional Interface, Validation at Entry Points, Backwards Compatibility.
 */
public final class Temperature implements IConvertible<Temperature> {

    private final double value;
    private final TemperatureUnit unit;

    public Temperature(double value, TemperatureUnit unit) {
        if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
        if (unit == TemperatureUnit.KELVIN && value < 0)
            throw new IllegalArgumentException("Kelvin cannot be negative: " + value);
        this.value = value;
        this.unit  = unit;
    }

    private double toCelsius() { return unit.toCelsius(value); }

    @Override
    public Temperature convertTo(TemperatureUnit targetUnit) {
        if (targetUnit == null) throw new IllegalArgumentException("Target unit cannot be null");
        return new Temperature(unit.convert(value, targetUnit), targetUnit);
    }

    /**
     * Selective arithmetic — temperature difference IS meaningful (delta).
     * Returns a DELTA (not an absolute temperature) as a double.
     */
    public double deltaFrom(Temperature other) {
        return this.toCelsius() - other.unit.toCelsius(other.value);
    }

    /**
     * Throws UnsupportedOperationException — addition of absolute temperatures is undefined.
     * Behavioural customisation via exception semantics.
     */
    public Temperature add(Temperature other) {
        throw new UnsupportedOperationException(
            "Adding absolute temperatures is physically meaningless. Use deltaFrom() for differences.");
    }

    public double getValue() { return value; }
    public TemperatureUnit getUnit() { return unit; }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Temperature)) return false;
        if (this == obj) return true;
        Temperature other = (Temperature) obj;
        return Double.compare(this.toCelsius(), other.toCelsius()) == 0;
    }

    @Override public int hashCode() { return Double.hashCode(toCelsius()); }
    @Override public String toString() { return String.format("%.2f %s", value, unit.getSymbol()); }
}
