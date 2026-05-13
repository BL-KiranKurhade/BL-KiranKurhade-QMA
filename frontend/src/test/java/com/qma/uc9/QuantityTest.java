package com.qma.uc9;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {
    private static final double DELTA = 1e-4;

    @Test void testKgToGram()    { assertEquals(new Quantity(1,WeightUnit.KILOGRAM), new Quantity(1000,WeightUnit.GRAM)); }
    @Test void testPoundToGram() { assertEquals(new Quantity(453.59237,WeightUnit.GRAM), new Quantity(1,WeightUnit.POUND)); }
    @Test void testAddWeights()  {
        Quantity r = new Quantity(1,WeightUnit.KILOGRAM).add(new Quantity(500,WeightUnit.GRAM));
        assertEquals(1500.0, r.getValue(), DELTA);
    }
    @Test void testCategoryMismatch() {
        assertThrows(IllegalArgumentException.class, () ->
            new Quantity(1,LengthUnit.FEET).add(new Quantity(1,WeightUnit.KILOGRAM)));
    }
    @Test void testLengthStillWorks() {
        assertEquals(new Quantity(1,LengthUnit.FEET), new Quantity(12,LengthUnit.INCH));
    }
    @Test void testConvertKgToTonne() {
        Quantity r = new Quantity(1000, WeightUnit.KILOGRAM).convertTo(WeightUnit.TONNE);
        assertEquals(1.0, r.getValue(), DELTA);
    }
}
