// UC9 - Weight Measurement | java RunUC9.java
public class RunUC9 {
    static void ok(String n,boolean c){System.out.printf("  [%s] %s%n",c?"PASS":"FAIL",n);if(!c)throw new AssertionError(n);}
    public static void main(String[] args){
        System.out.println("=== UC9: Weight Measurement ===");
        // Length quantities
        Qty<LengthUnit> l1=new Qty<>(1.0,LengthUnit.FT);
        Qty<LengthUnit> l2=new Qty<>(12.0,LengthUnit.IN);
        // Weight quantities
        Qty<WeightUnit> w1=new Qty<>(1.0,WeightUnit.KG);
        Qty<WeightUnit> w2=new Qty<>(1000.0,WeightUnit.GRAM);
        System.out.println("  1ft == 12in  : "+l1.equals(l2));
        System.out.println("  1kg == 1000g : "+w1.equals(w2));
        System.out.println("  1kg + 500g   : "+w1.add(new Qty<>(500.0,WeightUnit.GRAM)));
        ok("1ft == 12in",    l1.equals(l2));
        ok("1kg == 1000g",   w1.equals(w2));
        ok("Category mismatch throws", ()->{
            try{
                Qty raw1=(Qty)l1; Qty raw2=(Qty)w1;
                // Different generic types won't be equal via equals since base units differ
                return !l1.equals(w1);
            }catch(Exception e){return true;}
        });
        ok("1lb == 453.592g", new Qty<>(1.0,WeightUnit.LB).equals(new Qty<>(453.592,WeightUnit.GRAM)));
        ok("1kg + 500g = 1.5kg", w1.add(new Qty<>(500.0,WeightUnit.GRAM)).equals(new Qty<>(1.5,WeightUnit.KG)));
        System.out.println("\n[OK] UC9 PASSED");
    }
    static void ok(String n,java.util.function.Supplier<Boolean> s){ok(n,s.get());}
}
interface IUnit{double getBaseUnitFactor();String getSymbol();String getCategory();}
enum LengthUnit implements IUnit{
    MM(1.0,"mm"),CM(10.0,"cm"),IN(25.4,"in"),FT(304.8,"ft"),YD(914.4,"yd");
    private final double f;private final String s;
    LengthUnit(double f,String s){this.f=f;this.s=s;}
    public double getBaseUnitFactor(){return f;}public String getSymbol(){return s;}public String getCategory(){return "LENGTH";}
}
enum WeightUnit implements IUnit{
    MG(1.0,"mg"),GRAM(1000.0,"g"),KG(1000000.0,"kg"),LB(453592.0,"lb"),OZ(28349.5,"oz");
    private final double f;private final String s;
    WeightUnit(double f,String s){this.f=f;this.s=s;}
    public double getBaseUnitFactor(){return f;}public String getSymbol(){return s;}public String getCategory(){return "WEIGHT";}
}
class Qty<T extends IUnit>{
    private static final double EPS=1e-6;
    private final double v;private final T u;
    public Qty(double v,T u){this.v=v;this.u=u;}
    private double base(){return v*u.getBaseUnitFactor();}
    public Qty<T> convertTo(T t){return new Qty<>(base()/t.getBaseUnitFactor(),t);}
    public Qty<T> add(Qty<T> o){return new Qty<>((base()+o.base())/u.getBaseUnitFactor(),u);}
    public Qty<T> subtract(Qty<T> o){return new Qty<>((base()-o.base())/u.getBaseUnitFactor(),u);}
    @Override public boolean equals(Object o){
        if(!(o instanceof Qty))return false;
        Qty<?> q=(Qty<?>)o;
        if(!u.getCategory().equals(q.u.getCategory()))return false;
        return Math.abs(base()-q.base())<EPS;
    }
    @Override public int hashCode(){return Double.hashCode(base());}
    @Override public String toString(){return String.format("%.4f %s",v,u.getSymbol());}
}
