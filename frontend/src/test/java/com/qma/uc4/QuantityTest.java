package com.qma.uc4;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {
    @Test void testInchEquality()        { assertEquals(new Quantity(1,LengthUnit.INCH), new Quantity(1,LengthUnit.INCH)); }
    @Test void testFeetToInch()          { assertEquals(new Quantity(1,LengthUnit.FEET), new Quantity(12,LengthUnit.INCH)); }
    @Test void testYardToFeet()          { assertEquals(new Quantity(1,LengthUnit.YARD), new Quantity(3,LengthUnit.FEET)); }
    @Test void testCentimetreToInch()    { assertEquals(new Quantity(2.54,LengthUnit.CENTIMETRE), new Quantity(1,LengthUnit.INCH)); }
    @Test void testMillimetreToInch()    { assertEquals(new Quantity(25.4,LengthUnit.MILLIMETRE), new Quantity(1,LengthUnit.INCH)); }
    @Test void testNegativeValidation()  { assertThrows(IllegalArgumentException.class, ()-> new Quantity(-1, LengthUnit.INCH)); }
}
