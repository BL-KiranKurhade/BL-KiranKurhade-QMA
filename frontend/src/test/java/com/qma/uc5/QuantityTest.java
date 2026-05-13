package com.qma.uc5;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {
    private static final double DELTA = 1e-6;

    @Test void testFeetToInch() {
        Quantity result = new Quantity(1, LengthUnit.FEET).convertTo(LengthUnit.INCH);
        assertEquals(12.0, result.getValue(), DELTA);
        assertEquals(LengthUnit.INCH, result.getUnit());
    }
    @Test void testInchToCm() {
        Quantity result = new Quantity(1, LengthUnit.INCH).convertTo(LengthUnit.CENTIMETRE);
        assertEquals(2.54, result.getValue(), DELTA);
    }
    @Test void testYardToFeet() {
        Quantity result = new Quantity(1, LengthUnit.YARD).convertTo(LengthUnit.FEET);
        assertEquals(3.0, result.getValue(), DELTA);
    }
    @Test void testImmutability() {
        Quantity q1 = new Quantity(1, LengthUnit.FEET);
        Quantity q2 = q1.convertTo(LengthUnit.INCH);
        assertEquals(1.0, q1.getValue(), DELTA); // original unchanged
        assertEquals(12.0, q2.getValue(), DELTA);
    }
    @Test void testEquality() {
        assertEquals(new Quantity(1, LengthUnit.FEET), new Quantity(12, LengthUnit.INCH));
    }
    @Test void testNullUnit() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity(1, null));
    }
}
