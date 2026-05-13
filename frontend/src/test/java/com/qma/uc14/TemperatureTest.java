package com.qma.uc14;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TemperatureTest {
    private static final double DELTA = 1e-4;

    @Test void testCelsiusToFahrenheit() {
        Temperature r = new Temperature(100, TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.FAHRENHEIT);
        assertEquals(212.0, r.getValue(), DELTA);
    }
    @Test void testFahrenheitToCelsius() {
        Temperature r = new Temperature(32, TemperatureUnit.FAHRENHEIT).convertTo(TemperatureUnit.CELSIUS);
        assertEquals(0.0, r.getValue(), DELTA);
    }
    @Test void testCelsiusToKelvin() {
        Temperature r = new Temperature(0, TemperatureUnit.CELSIUS).convertTo(TemperatureUnit.KELVIN);
        assertEquals(273.15, r.getValue(), DELTA);
    }
    @Test void testEquality() {
        assertEquals(new Temperature(100,TemperatureUnit.CELSIUS),
                     new Temperature(212,TemperatureUnit.FAHRENHEIT));
    }
    @Test void testAddThrows() {
        assertThrows(UnsupportedOperationException.class, () ->
            new Temperature(30,TemperatureUnit.CELSIUS).add(new Temperature(30,TemperatureUnit.CELSIUS)));
    }
    @Test void testNegativeKelvin() {
        assertThrows(IllegalArgumentException.class, () -> new Temperature(-1, TemperatureUnit.KELVIN));
    }
    @Test void testDelta() {
        Temperature t1 = new Temperature(100,TemperatureUnit.CELSIUS);
        Temperature t2 = new Temperature(50, TemperatureUnit.CELSIUS);
        assertEquals(50.0, t1.deltaFrom(t2), DELTA);
    }
}
