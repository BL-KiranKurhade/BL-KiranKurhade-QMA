package com.qma.uc8;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {
    private static final double DELTA = 1e-5;

    @Test void testLengthEquality() {
        assertEquals(new Quantity(1, LengthUnit.FEET), new Quantity(12, LengthUnit.INCH));
    }
    @Test void testConversion() {
        Quantity r = new Quantity(1, LengthUnit.FEET).convertTo(LengthUnit.INCH);
        assertEquals(12.0, r.getValue(), DELTA);
    }
    @Test void testAddWithTargetUnit() {
        Quantity r = new Quantity(1, LengthUnit.FEET).add(new Quantity(1, LengthUnit.FEET), LengthUnit.INCH);
        assertEquals(24.0, r.getValue(), DELTA);
    }
    @Test void testSameCategory() {
        assertDoesNotThrow(() ->
            new Quantity(1, LengthUnit.FEET).add(new Quantity(1, LengthUnit.INCH)));
    }
}
