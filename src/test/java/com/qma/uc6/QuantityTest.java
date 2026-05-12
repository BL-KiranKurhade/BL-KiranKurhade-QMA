package com.qma.uc6;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {
    private static final double DELTA = 1e-6;

    @Test void testAddSameUnit() {
        Quantity result = new Quantity(1, LengthUnit.FEET).add(new Quantity(1, LengthUnit.FEET));
        assertEquals(new Quantity(2, LengthUnit.FEET), result);
    }
    @Test void testAddCrossUnit() {
        Quantity result = new Quantity(1, LengthUnit.FEET).add(new Quantity(12, LengthUnit.INCH));
        assertEquals(new Quantity(2, LengthUnit.FEET), result);
    }
    @Test void testCommutativity() {
        Quantity a = new Quantity(1, LengthUnit.FEET);
        Quantity b = new Quantity(6, LengthUnit.INCH);
        assertEquals(a.add(b).toBaseUnit(), b.add(a).toBaseUnit(), DELTA);
    }
    @Test void testResultUnitIsFirstOperand() {
        Quantity result = new Quantity(1, LengthUnit.FEET).add(new Quantity(12, LengthUnit.INCH));
        assertEquals(LengthUnit.FEET, result.getUnit());
    }
    @Test void testNullAddition() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity(1, LengthUnit.FEET).add(null));
    }
}
