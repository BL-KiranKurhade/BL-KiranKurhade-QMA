// UC11 - Volume Measurement  |  java RunUC11.java
public class RunUC11 {
    static void ok(String n,boolean c){System.out.printf("  [%s] %s%n",c?"PASS":"FAIL",n);if(!c)throw new AssertionError(n);}
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC11: Volume Measurement                ║");
        System.out.println("╚══════════════════════════════════════════╝");
        Qty<VU> L=new Qty<>(1,VU.LITRE),gal=new Qty<>(1,VU.GALLON),ml500=new Qty<>(500,VU.ML);
        System.out.println("\n  ── Volume Conversions ──");
        System.out.println("  1L   → ml  : "+L.convertTo(VU.ML));
        System.out.println("  1gal → L   : "+gal.convertTo(VU.LITRE));
        System.out.println("  1L   → gal : "+L.convertTo(VU.GALLON));
        System.out.println("  1L+500ml   : "+L.add(ml500));
        System.out.println("  1L+500ml → ML: "+L.add(ml500,VU.ML));
        ok("1L == 1000ml",       L.equals(new Qty<>(1000,VU.ML)));
        ok("1L+500ml == 1500ml", L.add(ml500).equals(new Qty<>(1500,VU.ML)));
        ok("Gallon conversion",  Math.abs(gal.convertTo(VU.LITRE).getValue()-3.785)<0.001);
        System.out.println("\n  ── Zero changes to Qty class — architectural scalability! ──");
        System.out.println("\n✔  UC11 PASSED");
    }
}
interface IUnit{double getBaseUnitFactor();String getSymbol();String getCategory();}
enum VU implements IUnit{
    ML(1.0,"ml"),LITRE(1000.0,"l"),GALLON(3785.411784,"gal"),CUP(236.5882365,"cup");
    private final double f;private final String s;VU(double f,String s){this.f=f;this.s=s;}
    public double getBaseUnitFactor(){return f;}public String getSymbol(){return s;}public String getCategory(){return "VOLUME";}
}
enum LU implements IUnit{IN(1.0,"in"),FT(12.0,"ft");
    private final double f;private final String s;LU(double f,String s){this.f=f;this.s=s;}
    public double getBaseUnitFactor(){return f;}public String getSymbol(){return s;}public String getCategory(){return "LENGTH";}}
class Qty<T extends IUnit>{
    private static final double EPS=1e-6;
    private final double v;private final T u;
    public Qty(double v,T u){this.v=v;this.u=u;}
    private double base(){return v*u.getBaseUnitFactor();}
    public Qty<T> convertTo(T t){return new Qty<>(base()/t.getBaseUnitFactor(),t);}
    public Qty<T> add(Qty<T> o){return new Qty<>((base()+o.base())/u.getBaseUnitFactor(),u);}
    public Qty<T> add(Qty<T> o,T t){return new Qty<>((base()+o.base())/t.getBaseUnitFactor(),t);}
    public double getValue(){return v;}
    @Override public boolean equals(Object o){if(!(o instanceof Qty))return false;Qty<?> ot=(Qty<?>)o;
        if(!u.getCategory().equals(ot.u.getCategory()))return false;return Math.abs(base()-ot.base())<EPS;}
    @Override public String toString(){return String.format("%.4f %s",v,u.getSymbol());}
}
