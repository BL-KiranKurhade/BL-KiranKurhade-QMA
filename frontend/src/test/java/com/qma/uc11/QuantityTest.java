package com.qma.uc11;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {
    private static final double DELTA = 1e-3;

    @Test void testLitreToMl()      { assertEquals(new Quantity<>(1,VolumeUnit.LITRE), new Quantity<>(1000,VolumeUnit.MILLILITRE)); }
    @Test void testGallonToLitre()  {
        Quantity<VolumeUnit> r = new Quantity<>(1,VolumeUnit.GALLON).convertTo(VolumeUnit.LITRE);
        assertEquals(3.785, r.getValue(), DELTA);
    }
    @Test void testAddVolumes()     {
        Quantity<VolumeUnit> r = new Quantity<>(1,VolumeUnit.LITRE).add(new Quantity<>(500,VolumeUnit.MILLILITRE));
        assertEquals(1500.0, r.getValue(), DELTA);
    }
    @Test void testCrossCategory()  {
        assertThrows(IllegalArgumentException.class, () ->
            new Quantity<>(1,VolumeUnit.LITRE).equals(new Quantity<>(1,WeightUnit.KILOGRAM)));
    }
    @Test void testConvertToGallon(){
        Quantity<VolumeUnit> r = new Quantity<>(3785.411784, VolumeUnit.MILLILITRE).convertTo(VolumeUnit.GALLON);
        assertEquals(1.0, r.getValue(), DELTA);
    }
}
