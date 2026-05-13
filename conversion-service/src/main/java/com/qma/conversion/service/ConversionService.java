package com.qma.conversion.service;

import com.qma.conversion.entity.ConversionHistory;
import com.qma.conversion.repository.ConversionHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Core conversion logic — UC21 Conversion Microservice.
 *
 * Supported categories and their internal base unit:
 *   LENGTH  → inch
 *   WEIGHT  → gram
 *   VOLUME  → ml
 *   TEMPERATURE → Celsius (intermediate)
 *
 * Every successful conversion is saved to MySQL (conversion_history).
 */
@Service
public class ConversionService {

    // ── Built-in unit factor maps ─────────────────────────────────────────────

    private static final Map<String, Double> LENGTH_TO_INCH = Map.of(
        "mm", 1.0/25.4, "cm", 1.0/2.54, "in", 1.0, "ft", 12.0,
        "yd", 36.0, "m", 39.3701, "km", 39370.1, "mi", 63360.0
    );
    private static final Map<String, Double> WEIGHT_TO_GRAM = Map.of(
        "g", 1.0, "kg", 1000.0, "lb", 453.592, "oz", 28.3495,
        "t", 1_000_000.0, "mg", 0.001
    );
    private static final Map<String, Double> VOLUME_TO_ML = Map.of(
        "ml", 1.0, "l", 1000.0, "gal", 3785.41, "cup", 236.588,
        "fl_oz", 29.5735, "tbsp", 14.7868, "tsp", 4.92892
    );

    @Autowired private ConversionHistoryRepository historyRepo;

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Perform a unit conversion and save the result to MySQL history.
     *
     * @param value    numeric value to convert
     * @param fromUnit source unit (e.g. cm, kg, l, C)
     * @param toUnit   target unit (e.g. in, lb, gal, F)
     * @param category LENGTH | WEIGHT | VOLUME | TEMPERATURE
     * @param username caller's username from JWT (stored in history)
     * @return converted value
     */
    public double convert(double value, String fromUnit, String toUnit,
                          String category, String username) {

        double result = doConvert(value, fromUnit, toUnit, category);

        historyRepo.save(new ConversionHistory(
            value, fromUnit, toUnit, category, result, username));

        return result;
    }

    /** Backward-compatible overload — records "anonymous" when no JWT is provided. */
    public double convert(double value, String fromUnit, String toUnit, String category) {
        return convert(value, fromUnit, toUnit, category, "anonymous");
    }

    // ── Private conversion logic ──────────────────────────────────────────────

    private double doConvert(double value, String from, String to, String category) {
        return switch (category.toUpperCase()) {
            case "LENGTH"      -> convertLinear(value, from, to, LENGTH_TO_INCH);
            case "WEIGHT"      -> convertLinear(value, from, to, WEIGHT_TO_GRAM);
            case "VOLUME"      -> convertLinear(value, from, to, VOLUME_TO_ML);
            case "TEMPERATURE" -> convertTemperature(value, from, to);
            default -> throw new IllegalArgumentException("Unknown category: " + category);
        };
    }

    private double convertLinear(double value, String from, String to,
                                 Map<String, Double> factors) {
        Double fromF = factors.get(from.toLowerCase());
        Double toF   = factors.get(to.toLowerCase());
        if (fromF == null) throw new IllegalArgumentException("Unknown unit: " + from);
        if (toF   == null) throw new IllegalArgumentException("Unknown unit: " + to);
        return (value * fromF) / toF;
    }

    private double convertTemperature(double value, String from, String to) {
        double celsius = switch (from.toUpperCase()) {
            case "C", "CELSIUS"    -> value;
            case "F", "FAHRENHEIT" -> (value - 32) * 5.0 / 9.0;
            case "K", "KELVIN"     -> value - 273.15;
            default -> throw new IllegalArgumentException("Unknown temperature unit: " + from);
        };
        return switch (to.toUpperCase()) {
            case "C", "CELSIUS"    -> celsius;
            case "F", "FAHRENHEIT" -> celsius * 9.0 / 5.0 + 32;
            case "K", "KELVIN"     -> celsius + 273.15;
            default -> throw new IllegalArgumentException("Unknown temperature unit: " + to);
        };
    }
}
