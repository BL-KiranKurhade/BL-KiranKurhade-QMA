package com.qma.uc10;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {
    private static final double DELTA = 1e-4;

    @Test void testGenericLength() {
        Quantity<LengthUnit> r = new Quantity<>(1, LengthUnit.FEET).convertTo(LengthUnit.INCH);
        assertEquals(12.0, r.getValue(), DELTA);
    }
    @Test void testGenericWeight() {
        Quantity<WeightUnit> r = new Quantity<>(1, WeightUnit.KILOGRAM).convertTo(WeightUnit.GRAM);
        assertEquals(1000.0, r.getValue(), DELTA);
    }
    @Test void testGenericEquality() {
        assertEquals(new Quantity<>(1, LengthUnit.FEET), new Quantity<>(12, LengthUnit.INCH));
    }
    @Test void testGenericAddition() {
        Quantity<WeightUnit> r = new Quantity<>(1, WeightUnit.KILOGRAM)
            .add(new Quantity<>(500, WeightUnit.GRAM));
        assertEquals(1500.0, r.getValue(), DELTA);
    }
    @Test void testAddWithTargetUnit() {
        Quantity<LengthUnit> r = new Quantity<>(1, LengthUnit.FEET)
            .add(new Quantity<>(1, LengthUnit.FEET), LengthUnit.INCH);
        assertEquals(24.0, r.getValue(), DELTA);
    }
}
