package com.qma.uc2;

/**
 * Represents a measurement combining Feet and Inches.
 * Concept: Object Encapsulation — internal state (toInches) is hidden.
 * Equality compares total inches to handle equivalent representations.
 */
public class Measurement {

    private final double feet;
    private final double inches;

    public Measurement(double feet, double inches) {
        this.feet = feet;
        this.inches = inches;
    }

    /** Encapsulated conversion: total inches = feet*12 + inches */
    private double toTotalInches() {
        return (feet * 12.0) + inches;
    }

    public double getFeet()   { return feet; }
    public double getInches() { return inches; }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof Measurement)) return false;
        Measurement other = (Measurement) obj;
        return Double.compare(this.toTotalInches(), other.toTotalInches()) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(toTotalInches());
    }

    @Override
    public String toString() {
        return feet + " ft " + inches + " in  (" + toTotalInches() + " in total)";
    }
}
