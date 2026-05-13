// UC1 - Feet Measurement Equality | java RunUC1.java
public class RunUC1 {
    static void ok(String n,boolean c){System.out.printf("  [%s] %s%n",c?"PASS":"FAIL",n);if(!c)throw new AssertionError(n);}
    public static void main(String[] args) {
        System.out.println("=== UC1: Feet Measurement Equality ===");
        Feet f1=new Feet(1.0),f2=new Feet(1.0),f3=new Feet(2.0);
        ok("1.0ft == 1.0ft",      f1.equals(f2));
        ok("1.0ft != 2.0ft",     !f1.equals(f3));
        ok("Null check",          !f1.equals(null));
        ok("Type check (String)", !f1.equals("1.0"));
        ok("Self equality",        f1.equals(f1));
        ok("Zero equality",        new Feet(0.0).equals(new Feet(0.0)));
        ok("Negative feet",        new Feet(-3.5).equals(new Feet(-3.5)));
        System.out.println("\n[OK] UC1 PASSED");
    }
}
class Feet {
    private final double value;
    public Feet(double value) { this.value = value; }
    public double getValue()  { return value; }
    @Override public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof Feet)) return false;
        return Double.compare(this.value, ((Feet) obj).value) == 0;
    }
    @Override public int hashCode() { return Double.hashCode(value); }
    @Override public String toString() { return value + " feet"; }
}
