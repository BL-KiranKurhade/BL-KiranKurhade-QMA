// UC12 - Subtraction & Division  |  java RunUC12.java
public class RunUC12 {
    static void ok(String n,boolean c){System.out.printf("  [%s] %s%n",c?"PASS":"FAIL",n);if(!c)throw new AssertionError(n);}
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC12: Subtraction & Division            ║");
        System.out.println("╚══════════════════════════════════════════╝");
        Qty<LU> twoFt=new Qty<>(2,LU.FT),oneFt=new Qty<>(1,LU.FT),oneYd=new Qty<>(1,LU.YD);
        Qty<WU> five=new Qty<>(5,WU.KG),two=new Qty<>(2,WU.KG);
        System.out.println("\n  ── Subtraction ──");
        System.out.println("  2ft - 1ft = "+twoFt.subtract(oneFt));
        System.out.println("  1yd - 1ft in INCH = "+oneYd.subtract(oneFt,LU.IN));
        System.out.println("  5kg - 2kg = "+five.subtract(two));
        System.out.println("\n  ── Division & Multiply ──");
        System.out.println("  2ft ÷ 2   = "+twoFt.divide(2));
        System.out.println("  2ft × 3   = "+twoFt.multiply(3));
        System.out.println("\n  ── Non-Commutativity ──");
        System.out.printf("  5kg - 2kg = %.4f kg%n", five.subtract(two).getValue());
        System.out.printf("  2kg - 5kg = %.4f kg (negative)%n", two.subtract(five).getValue());
        ok("2ft - 1ft = 1ft",        twoFt.subtract(oneFt).equals(oneFt));
        ok("2ft ÷ 2 = 1ft",          twoFt.divide(2).equals(oneFt));
        ok("Non-commutative",         five.subtract(two).getValue()!=two.subtract(five).getValue());
        ok("1yd-1ft in INCH = 24",   Math.abs(oneYd.subtract(oneFt,LU.IN).getValue()-24.0)<1e-9);
        try{twoFt.divide(0);}catch(ArithmeticException e){System.out.println("  [PASS] Div/0 caught: "+e.getMessage());}
        System.out.println("\n✔  UC12 PASSED");
    }
}
interface IUnit{double getBaseUnitFactor();String getSymbol();String getCategory();}
enum LU implements IUnit{IN(1.0,"in"),FT(12.0,"ft"),YD(36.0,"yd");
    private final double f;private final String s;LU(double f,String s){this.f=f;this.s=s;}
    public double getBaseUnitFactor(){return f;}public String getSymbol(){return s;}public String getCategory(){return "LENGTH";}}
enum WU implements IUnit{G(1.0,"g"),KG(1000.0,"kg");
    private final double f;private final String s;WU(double f,String s){this.f=f;this.s=s;}
    public double getBaseUnitFactor(){return f;}public String getSymbol(){return s;}public String getCategory(){return "WEIGHT";}}
class Qty<T extends IUnit>{
    private static final double EPS=1e-9;
    private final double v;private final T u;
    public Qty(double v,T u){this.v=v;this.u=u;}
    private double base(){return v*u.getBaseUnitFactor();}
    private void chk(Qty<T> o){if(o==null||!u.getCategory().equals(o.u.getCategory()))throw new IllegalArgumentException("Category mismatch");}
    public Qty<T> convertTo(T t){return new Qty<>(base()/t.getBaseUnitFactor(),t);}
    public Qty<T> add(Qty<T> o){chk(o);return new Qty<>((base()+o.base())/u.getBaseUnitFactor(),u);}
    public Qty<T> subtract(Qty<T> o){chk(o);return new Qty<>((base()-o.base())/u.getBaseUnitFactor(),u);}
    public Qty<T> subtract(Qty<T> o,T t){chk(o);return new Qty<>((base()-o.base())/t.getBaseUnitFactor(),t);}
    public Qty<T> divide(double d){if(d==0)throw new ArithmeticException("Division by zero");return new Qty<>(v/d,u);}
    public Qty<T> multiply(double m){return new Qty<>(v*m,u);}
    public double getValue(){return v;}public T getUnit(){return u;}
    @Override public boolean equals(Object o){if(!(o instanceof Qty))return false;Qty<?> ot=(Qty<?>)o;
        if(!u.getCategory().equals(ot.u.getCategory()))return false;return Math.abs(base()-ot.base())<EPS;}
    @Override public String toString(){return String.format("%.4f %s",v,u.getSymbol());}
}
