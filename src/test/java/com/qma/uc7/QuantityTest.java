package com.qma.uc7;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {
    private static final double DELTA = 1e-5;

    @Test void testAddDefaultUnit() {
        Quantity r = new Quantity(1, LengthUnit.FEET).add(new Quantity(12, LengthUnit.INCH));
        assertEquals(LengthUnit.FEET, r.getUnit());
        assertEquals(2.0, r.getValue(), DELTA);
    }
    @Test void testAddWithTargetUnitInch() {
        Quantity r = new Quantity(1, LengthUnit.FEET).add(new Quantity(1, LengthUnit.FEET), LengthUnit.INCH);
        assertEquals(LengthUnit.INCH, r.getUnit());
        assertEquals(24.0, r.getValue(), DELTA);
    }
    @Test void testAddWithTargetUnitYard() {
        Quantity r = new Quantity(1, LengthUnit.YARD).add(new Quantity(1, LengthUnit.YARD), LengthUnit.FEET);
        assertEquals(6.0, r.getValue(), DELTA);
    }
    @Test void testNullTargetUnit() {
        assertThrows(IllegalArgumentException.class,
            () -> new Quantity(1, LengthUnit.FEET).add(new Quantity(1, LengthUnit.FEET), null));
    }
}
