// UC8 - Refactoring Unit Enum to Standalone (IUnit interface)  |  java RunUC8.java
public class RunUC8 {
    static void ok(String n,boolean c){System.out.printf("  [%s] %s%n",c?"PASS":"FAIL",n);if(!c)throw new AssertionError(n);}
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC8: Standalone IUnit Interface         ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.println("  Quantity depends on IUnit interface — not concrete enums.");
        Quantity q1=new Quantity(1,LengthUnit.FT), q12=new Quantity(12,LengthUnit.IN);
        System.out.println("  q1  = "+q1+"  ["+q1.getUnit().getCategory()+"]");
        System.out.println("  q12 = "+q12+" ["+q12.getUnit().getCategory()+"]");
        ok("1ft == 12in",               q1.equals(q12));
        ok("1ft → in = 12",             Math.abs(q1.convertTo(LengthUnit.IN).getValue()-12.0)<1e-9);
        ok("1ft+1ft in INCH = 24",      Math.abs(q1.add(q1,LengthUnit.IN).getValue()-24.0)<1e-9);
        ok("Cross-unit add result",     q1.add(q12).equals(new Quantity(2,LengthUnit.FT)));
        System.out.println("\n✔  UC8 PASSED");
    }
}
interface IUnit { double getBaseUnitFactor(); String getSymbol(); String getCategory(); }
enum LengthUnit implements IUnit {
    MM(1.0/25.4,"mm"),CM(1.0/2.54,"cm"),IN(1.0,"in"),FT(12.0,"ft"),YD(36.0,"yd");
    private final double f;private final String s;
    LengthUnit(double f,String s){this.f=f;this.s=s;}
    public double getBaseUnitFactor(){return f;} public String getSymbol(){return s;} public String getCategory(){return "LENGTH";}
}
final class Quantity {
    private static final double EPS=1e-9;
    private final double value;private final IUnit unit;
    public Quantity(double v,IUnit u){if(v<0||u==null)throw new IllegalArgumentException("Bad args");this.value=v;this.unit=u;}
    private double base(){return value*unit.getBaseUnitFactor();}
    private void cat(Quantity o){if(!unit.getCategory().equals(o.unit.getCategory()))throw new IllegalArgumentException("Category mismatch");}
    public Quantity convertTo(IUnit t){return new Quantity(base()/t.getBaseUnitFactor(),t);}
    public Quantity add(Quantity o){cat(o);return new Quantity((base()+o.base())/unit.getBaseUnitFactor(),unit);}
    public Quantity add(Quantity o,IUnit t){cat(o);return new Quantity((base()+o.base())/t.getBaseUnitFactor(),t);}
    public double getValue(){return value;}public IUnit getUnit(){return unit;}
    @Override public boolean equals(Object o){if(!(o instanceof Quantity))return false;Quantity ot=(Quantity)o;
        if(!unit.getCategory().equals(ot.unit.getCategory()))return false;return Math.abs(base()-ot.base())<EPS;}
    @Override public String toString(){return String.format("%.4f %s",value,unit.getSymbol());}
}
