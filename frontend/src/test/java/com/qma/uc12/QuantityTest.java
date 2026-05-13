package com.qma.uc12;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {
    private static final double DELTA = 1e-4;

    @Test void testSubtractSameUnit() {
        Quantity<LengthUnit> r = new Quantity<>(3,LengthUnit.FEET).subtract(new Quantity<>(1,LengthUnit.FEET));
        assertEquals(2.0, r.getValue(), DELTA);
    }
    @Test void testSubtractCrossUnit() {
        Quantity<LengthUnit> r = new Quantity<>(2,LengthUnit.FEET).subtract(new Quantity<>(12,LengthUnit.INCH));
        assertEquals(new Quantity<>(1,LengthUnit.FEET), r);
    }
    @Test void testSubtractNonCommutative() {
        Quantity<WeightUnit> a = new Quantity<>(5,WeightUnit.KILOGRAM);
        Quantity<WeightUnit> b = new Quantity<>(3,WeightUnit.KILOGRAM);
        assertNotEquals(a.subtract(b).getValue(), b.subtract(a).getValue());
    }
    @Test void testDivide() {
        Quantity<WeightUnit> r = new Quantity<>(6,WeightUnit.KILOGRAM).divide(2);
        assertEquals(3.0, r.getValue(), DELTA);
    }
    @Test void testDivideByZero() {
        assertThrows(ArithmeticException.class, () -> new Quantity<>(5,LengthUnit.FEET).divide(0));
    }
    @Test void testMultiply() {
        Quantity<VolumeUnit> r = new Quantity<>(2,VolumeUnit.LITRE).multiply(3);
        assertEquals(6.0, r.getValue(), DELTA);
    }
    @Test void testSubtractWithTarget() {
        Quantity<LengthUnit> r = new Quantity<>(1,LengthUnit.YARD).subtract(new Quantity<>(1,LengthUnit.FEET), LengthUnit.INCH);
        assertEquals(24.0, r.getValue(), DELTA);
    }
}
