package com.qma.uc14;

/**
 * Temperature units with NON-LINEAR conversions.
 * Concepts: Non-Linear Conversions, Absolute vs Relative Temperatures,
 * Category-Specific Offset Handling, Enum as Polymorphic Carrier,
 * Method Overriding for Behavioural Customisation.
 *
 * Conversion pivot: CELSIUS.
 */
public enum TemperatureUnit {

    CELSIUS {
        @Override public double toCelsius(double v)    { return v; }
        @Override public double fromCelsius(double c)  { return c; }
        @Override public String getSymbol()            { return "°C"; }
    },
    FAHRENHEIT {
        @Override public double toCelsius(double v)    { return (v - 32.0) * 5.0 / 9.0; }
        @Override public double fromCelsius(double c)  { return c * 9.0 / 5.0 + 32.0; }
        @Override public String getSymbol()            { return "°F"; }
    },
    KELVIN {
        @Override public double toCelsius(double v)    { return v - 273.15; }
        @Override public double fromCelsius(double c)  { return c + 273.15; }
        @Override public String getSymbol()            { return "K"; }
    };

    public abstract double toCelsius(double value);
    public abstract double fromCelsius(double celsius);
    public abstract String getSymbol();

    /** Convert from THIS unit to targetUnit. */
    public double convert(double value, TemperatureUnit target) {
        return target.fromCelsius(this.toCelsius(value));
    }
}
