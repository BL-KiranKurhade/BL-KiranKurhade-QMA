package com.qma.uc13;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {
    private static final double DELTA = 1e-4;

    @Test void testAdd()      { assertEquals(new Quantity<>(2,LengthUnit.FEET),
        new Quantity<>(1,LengthUnit.FEET).add(new Quantity<>(12,LengthUnit.INCH))); }
    @Test void testSubtract() { assertEquals(new Quantity<>(1,LengthUnit.FEET),
        new Quantity<>(2,LengthUnit.FEET).subtract(new Quantity<>(12,LengthUnit.INCH))); }
    @Test void testCustomOp() {
        // Custom lambda: max of two values
        Quantity<LengthUnit> a = new Quantity<>(3,LengthUnit.FEET);
        Quantity<LengthUnit> b = new Quantity<>(1,LengthUnit.FEET);
        Quantity<LengthUnit> max = a.apply(b, (x, y) -> Math.max(x,y));
        assertEquals(3.0, max.getValue(), DELTA);
    }
    @Test void testEnumOperation() {
        assertEquals(8.0, Operation.ADD.apply(3,5), DELTA);
        assertEquals(6.0, Operation.MULTIPLY.apply(2,3), DELTA);
    }
    @Test void testDivByZeroInOperation() {
        assertThrows(ArithmeticException.class, () -> Operation.DIVIDE.apply(5, 0));
    }
}
