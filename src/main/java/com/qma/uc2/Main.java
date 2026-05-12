package com.qma.uc2;

/**
 * UC2 - Feet and Inches Measurement Equality
 * Concept: Encapsulation — equality via encapsulated toTotalInches().
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC2: Feet & Inches Measurement Equality ║");
        System.out.println("╚══════════════════════════════════════════╝");

        check("1 ft 0 in == 1 ft 0 in",  new Measurement(1,0).equals(new Measurement(1,0)),  true);
        check("1 ft 0 in == 0 ft 12 in", new Measurement(1,0).equals(new Measurement(0,12)), true);
        check("2 ft 6 in == 1 ft 18 in", new Measurement(2,6).equals(new Measurement(1,18)), true);
        check("1 ft 0 in != 1 ft 1 in",  !new Measurement(1,0).equals(new Measurement(1,1)), true);
        check("Null check",               !new Measurement(1,0).equals(null),                 true);

        System.out.println("\n  Measurements:");
        System.out.println("  " + new Measurement(2,6));
        System.out.println("  " + new Measurement(1,18));
        System.out.println("\n✔  All UC2 tests passed!");
    }

    static void check(String name, boolean result, boolean expected) {
        System.out.printf("  [%s] %s%n", (result==expected?"PASS":"FAIL"), name);
        if (result != expected) throw new AssertionError("FAILED: " + name);
    }
}
