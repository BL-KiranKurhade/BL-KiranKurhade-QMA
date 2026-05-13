package com.qma.uc3;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {
    @Test void testInchToInch()  { assertEquals(new Quantity(12,LengthUnit.INCH), new Quantity(12,LengthUnit.INCH)); }
    @Test void testFeetToFeet()  { assertEquals(new Quantity(1,LengthUnit.FEET),  new Quantity(1,LengthUnit.FEET)); }
    @Test void testFeetToInch()  { assertEquals(new Quantity(1,LengthUnit.FEET),  new Quantity(12,LengthUnit.INCH)); }
    @Test void testInchToFeet()  { assertEquals(new Quantity(12,LengthUnit.INCH), new Quantity(1,LengthUnit.FEET)); }
    @Test void testNotEqual()    { assertNotEquals(new Quantity(1,LengthUnit.INCH),new Quantity(1,LengthUnit.FEET)); }
}
