package com.qma.uc1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Feet class.
 */
class FeetTest {

    @Test
    void testEqualFeet() {
        assertEquals(new Feet(1.0), new Feet(1.0));
    }

    @Test
    void testUnequalFeet() {
        assertNotEquals(new Feet(1.0), new Feet(2.0));
    }

    @Test
    void testNullEquality() {
        assertNotEquals(new Feet(1.0), null);
    }

    @Test
    void testTypeEquality() {
        assertNotEquals(new Feet(1.0), "1.0");
    }

    @Test
    void testSelfEquality() {
        Feet f = new Feet(5.0);
        assertEquals(f, f);
    }

    @Test
    void testZeroFeet() {
        assertEquals(new Feet(0.0), new Feet(0.0));
    }
}
