// UC7 - Addition with Target Unit  |  java RunUC7.java
public class RunUC7 {
    static void ok(String n,boolean c){System.out.printf("  [%s] %s%n",c?"PASS":"FAIL",n);if(!c)throw new AssertionError(n);}
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC7: Addition with Target Unit          ║");
        System.out.println("╚══════════════════════════════════════════╝");
        Qty ft=new Qty(1,LU.FT), yd=new Qty(1,LU.YD);
        System.out.println("\n  add(other)           → unit of first operand:");
        System.out.println("  1ft + 1ft = "+ft.add(ft));
        System.out.println("\n  add(other, targetUnit) → caller-specified unit:");
        System.out.println("  1ft + 1ft in INCH = "+ft.add(ft,LU.IN));
        System.out.println("  1ft + 1ft in CM   = "+ft.add(ft,LU.CM));
        System.out.println("  1yd + 1yd in FEET = "+yd.add(yd,LU.FT));
        System.out.println("  1yd + 1ft in INCH = "+yd.add(ft,LU.IN));
        ok("1ft+1ft in INCH = 24", Math.abs(ft.add(ft,LU.IN).getValue()-24.0)<1e-9);
        ok("1yd+1yd in FEET =  6", Math.abs(yd.add(yd,LU.FT).getValue()-6.0)<1e-9);
        ok("Default unit = FT",    ft.add(ft).getUnit()==LU.FT);
        System.out.println("\n✔  UC7 PASSED");
    }
}
enum LU { MM(1.0/25.4,"mm"),CM(1.0/2.54,"cm"),IN(1.0,"in"),FT(12.0,"ft"),YD(36.0,"yd");
    public final double f;public final String s;LU(double f,String s){this.f=f;this.s=s;} }
final class Qty {
    private final double v; private final LU u;
    public Qty(double v,LU u){ if(v<0)throw new IllegalArgumentException("Neg"); this.v=v; this.u=u; }
    private double base(){return v*u.f;}
    private double addBase(Qty o){ if(o==null)throw new IllegalArgumentException("Null"); return base()+o.base(); }
    public Qty add(Qty o){ return new Qty(addBase(o)/u.f,u); }
    public Qty add(Qty o,LU t){ if(t==null)throw new IllegalArgumentException("Null target"); return new Qty(addBase(o)/t.f,t); }
    public double getValue(){return v;} public LU getUnit(){return u;}
    @Override public boolean equals(Object o){ if(!(o instanceof Qty))return false; return Math.abs(base()-((Qty)o).base())<1e-9; }
    @Override public String toString(){return String.format("%.4f %s",v,u.s);}
}
