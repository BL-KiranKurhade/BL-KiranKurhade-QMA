// UC3 - Generic Quantity DRY | java RunUC3.java
public class RunUC3 {
    static void ok(String n,boolean c){System.out.printf("  [%s] %s%n",c?"PASS":"FAIL",n);if(!c)throw new AssertionError(n);}
    public static void main(String[] args) {
        System.out.println("=== UC3: Generic Quantity DRY ===");
        Q inch1 = new Q(1, LengthUnit.INCH);
        Q inch2 = new Q(1, LengthUnit.INCH);
        Q feet1 = new Q(1, LengthUnit.FEET);
        Q feet2 = new Q(1, LengthUnit.FEET);
        ok("1in == 1in",   inch1.equals(inch2));
        ok("1ft == 1ft",   feet1.equals(feet2));
        ok("1in != 1ft",  !inch1.equals(feet1));
        ok("Null check",   !inch1.equals(null));
        ok("2in == 2in",   new Q(2, LengthUnit.INCH).equals(new Q(2, LengthUnit.INCH)));
        System.out.println("\n[OK] UC3 PASSED");
    }
}
enum LengthUnit { INCH(1.0), FEET(12.0);
    final double factor;
    LengthUnit(double f){this.factor=f;}
}
class Q {
    private final double value; private final LengthUnit unit;
    public Q(double v, LengthUnit u){this.value=v;this.unit=u;}
    private double toBase(){return value*unit.factor;}
    @Override public boolean equals(Object o){
        if(o==null)return false;
        if(this==o)return true;
        if(!(o instanceof Q))return false;
        return Double.compare(toBase(),((Q)o).toBase())==0;
    }
    @Override public int hashCode(){return Double.hashCode(toBase());}
    @Override public String toString(){return value+" "+unit;}
}
