// UC10 - Generic Quantity<T extends IUnit>  |  java RunUC10.java
public class RunUC10 {
    static void ok(String n,boolean c){System.out.printf("  [%s] %s%n",c?"PASS":"FAIL",n);if(!c)throw new AssertionError(n);}
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC10: Generic Quantity<T extends IUnit> ║");
        System.out.println("╚══════════════════════════════════════════╝");
        Quantity<LU> ft=new Quantity<>(1,LU.FT), in12=new Quantity<>(12,LU.IN);
        Quantity<WU> kg=new Quantity<>(1,WU.KG), g500=new Quantity<>(500,WU.G);
        System.out.println("\n  ── Length (generic) ──");
        System.out.println("  1ft → in : "+ft.convertTo(LU.IN));
        System.out.println("  1ft == 12in: "+ft.equals(in12));
        System.out.println("\n  ── Weight (same class, different T) ──");
        System.out.println("  1kg → g  : "+kg.convertTo(WU.G));
        System.out.println("  1kg+500g : "+kg.add(g500));
        System.out.println("  1kg+500g in KG: "+kg.add(g500,WU.KG));
        ok("Generic length: 1ft == 12in",    ft.equals(in12));
        ok("Generic weight: 1kg == 1000g",   kg.equals(new Quantity<>(1000,WU.G)));
        ok("1kg+500g = 1500g",               kg.add(g500).equals(new Quantity<>(1500,WU.G)));
        System.out.println("\n✔  UC10 PASSED");
    }
}
interface IUnit{double getBaseUnitFactor();String getSymbol();String getCategory();}
enum LU implements IUnit{MM(1.0/25.4,"mm"),IN(1.0,"in"),FT(12.0,"ft"),YD(36.0,"yd");
    private final double f;private final String s;LU(double f,String s){this.f=f;this.s=s;}
    public double getBaseUnitFactor(){return f;}public String getSymbol(){return s;}public String getCategory(){return "LENGTH";}}
enum WU implements IUnit{G(1.0,"g"),KG(1000.0,"kg"),LB(453.59237,"lb");
    private final double f;private final String s;WU(double f,String s){this.f=f;this.s=s;}
    public double getBaseUnitFactor(){return f;}public String getSymbol(){return s;}public String getCategory(){return "WEIGHT";}}
class Quantity<T extends IUnit>{
    private static final double EPS=1e-6;
    private final double v;private final T u;
    public Quantity(double v,T u){if(v<0||u==null)throw new IllegalArgumentException("Bad");this.v=v;this.u=u;}
    private double base(){return v*u.getBaseUnitFactor();}
    private void chk(Quantity<T> o){if(!u.getCategory().equals(o.u.getCategory()))throw new IllegalArgumentException("Category mismatch");}
    public Quantity<T> convertTo(T t){return new Quantity<>(base()/t.getBaseUnitFactor(),t);}
    public Quantity<T> add(Quantity<T> o){chk(o);return new Quantity<>((base()+o.base())/u.getBaseUnitFactor(),u);}
    public Quantity<T> add(Quantity<T> o,T t){chk(o);return new Quantity<>((base()+o.base())/t.getBaseUnitFactor(),t);}
    public double getValue(){return v;}public T getUnit(){return u;}
    @Override public boolean equals(Object o){if(!(o instanceof Quantity))return false;Quantity<?> ot=(Quantity<?>)o;
        if(!u.getCategory().equals(ot.u.getCategory()))return false;return Math.abs(base()-ot.base())<EPS;}
    @Override public String toString(){return String.format("%.4f %s",v,u.getSymbol());}
}
