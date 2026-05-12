// UC4 - Extended Unit Support  |  java RunUC4.java
public class RunUC4 {
    static void ok(String n,boolean c){System.out.printf("  [%s] %s%n",c?"PASS":"FAIL",n);if(!c)throw new AssertionError(n);}
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC4: Extended Unit Support              ║");
        System.out.println("╚══════════════════════════════════════════╝");
        ok("1 ft   == 12 in",   new Q(1,LU.FEET).equals(new Q(12,LU.INCH)));
        ok("1 yd   == 3 ft",    new Q(1,LU.YARD).equals(new Q(3,LU.FEET)));
        ok("2.54cm == 1 in",    new Q(2.54,LU.CENTIMETRE).equals(new Q(1,LU.INCH)));
        ok("25.4mm == 1 in",    new Q(25.4,LU.MILLIMETRE).equals(new Q(1,LU.INCH)));
        ok("1 yd   == 36 in",   new Q(1,LU.YARD).equals(new Q(36,LU.INCH)));
        ok("10 mm  == 1 cm",    new Q(10,LU.MILLIMETRE).equals(new Q(1,LU.CENTIMETRE)));
        try { new Q(-1,LU.INCH); System.out.println("  [FAIL] Negative allowed!"); }
        catch(IllegalArgumentException e){ System.out.println("  [PASS] Negative rejected: "+e.getMessage()); }
        System.out.println("\n✔  UC4 PASSED");
    }
}
enum LU {
    MILLIMETRE(1.0/25.4,"mm"), CENTIMETRE(1.0/2.54,"cm"),
    INCH(1.0,"in"), FEET(12.0,"ft"), YARD(36.0,"yd");
    public final double f; public final String s;
    LU(double f,String s){this.f=f;this.s=s;}
}
class Q {
    private static final double EPS = 1e-9;
    private final double v; private final LU u;
    public Q(double v,LU u){ if(v<0)throw new IllegalArgumentException("Negative: "+v); this.v=v; this.u=u; }
    private double base(){return v*u.f;}
    @Override public boolean equals(Object o){ if(!(o instanceof Q))return false; return Math.abs(base()-((Q)o).base()) < EPS; }
    @Override public String toString(){ return v+" "+u.s; }
}
