package com.qma.uc14;

/**
 * Interface Segregation: Convertible quantities.
 * Temperature CAN be converted (Celsius ↔ Fahrenheit ↔ Kelvin).
 */
public interface IConvertible<T> {
    T convertTo(T targetUnit);
}
