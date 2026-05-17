package com.qma.uc1;

/**
 * Represents a measurement in Feet.
 * Concepts: Object Equality, Floating-point Comparison, Null Checking, Type Checking, OOD.
 */
public class Feet {

    private final double value;

    public Feet(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    /**
     * Equals with: null check, self-reference check, type check, floating-point comparison.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;          // Null check
        if (this == obj) return true;            // Same reference
        if (!(obj instanceof Feet)) return false; // Type check
        Feet other = (Feet) obj;
        return Double.compare(this.value, other.value) == 0; // Floating-point safe compare
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }

    @Override
    public String toString() {
        return value + " feet";
    }
}
