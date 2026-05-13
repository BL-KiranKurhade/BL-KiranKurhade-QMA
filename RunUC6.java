// UC6 - Addition of Two Length Units | java RunUC6.java
public class RunUC6 {
    static void ok(String n,boolean c){System.out.printf("  [%s] %s%n",c?"PASS":"FAIL",n);if(!c)throw new AssertionError(n);}
    public static void main(String[] args){
        System.out.println("=== UC6: Addition of Two Length Units ===");
        Qty a = new Qty(1.0, LU.FT);
        Qty b = new Qty(12.0, LU.IN);
        Qty c = new Qty(1.0, LU.FT);
        System.out.println("  1ft + 12in = " + a.add(b));
        System.out.println("  1ft + 1ft  = " + a.add(c));
        System.out.println("  1ft + 1yd  = " + a.add(new Qty(1,LU.YD)));
        ok("1ft + 12in = 2ft", a.add(b).equals(new Qty(2.0, LU.FT)));
        ok("1ft + 1ft  = 2ft", a.add(c).equals(new Qty(2.0, LU.FT)));
        ok("1in + 1in  = 2in", new Qty(1,LU.IN).add(new Qty(1,LU.IN)).equals(new Qty(2,LU.IN)));
        System.out.println("\n[OK] UC6 PASSED");
    }
}
enum LU {
    MM(1.0,"mm"),CM(10.0,"cm"),IN(25.4,"in"),FT(304.8,"ft"),YD(914.4,"yd");
    final double f;final String s;
    LU(double f,String s){this.f=f;this.s=s;}
}
class Qty {
    private static final double EPS=1e-9;
    final double value;final LU unit;
    public Qty(double v,LU u){this.value=v;this.unit=u;}
    private double base(){return value*unit.f;}
    public Qty add(Qty o){return new Qty((base()+o.base())/unit.f,unit);}
    public Qty convertTo(LU t){return new Qty(base()/t.f,t);}
    @Override public boolean equals(Object o){
        if(!(o instanceof Qty))return false;
        return Math.abs(base()-((Qty)o).base())<EPS;
    }
    @Override public int hashCode(){return Double.hashCode(base());}
    @Override public String toString(){return String.format("%.4f %s",value,unit.s);}
}
