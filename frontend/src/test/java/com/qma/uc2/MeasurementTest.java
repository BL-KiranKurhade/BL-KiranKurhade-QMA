package com.qma.uc2;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MeasurementTest {
    @Test void testEqualMeasurements()  { assertEquals(new Measurement(1,0), new Measurement(1,0)); }
    @Test void testFeetInchesEquality() { assertEquals(new Measurement(1,0), new Measurement(0,12)); }
    @Test void testMixedEquality()      { assertEquals(new Measurement(2,6), new Measurement(1,18)); }
    @Test void testNotEqual()           { assertNotEquals(new Measurement(1,0), new Measurement(1,1)); }
    @Test void testNullCheck()          { assertNotEquals(new Measurement(1,0), null); }
}
