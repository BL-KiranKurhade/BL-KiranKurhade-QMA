// UC5 - Unit to Unit Conversion | java RunUC5.java
public class RunUC5 {
    static final double EPS=1e-9;
    static void ok(String n,boolean c){System.out.printf("  [%s] %s%n",c?"PASS":"FAIL",n);if(!c)throw new AssertionError(n);}
    static void okD(String n,double a,double b){ok(n,Math.abs(a-b)<EPS);}
    public static void main(String[] args){
        System.out.println("=== UC5: Unit-to-Unit Conversion ===");
        Qty q1ft = new Qty(1.0, LU.FT);
        Qty q1in = new Qty(1.0, LU.IN);
        System.out.println("  1ft -> in : " + q1ft.convertTo(LU.IN));
        System.out.println("  1in -> ft : " + q1in.convertTo(LU.FT));
        System.out.println("  1ft -> cm : " + q1ft.convertTo(LU.CM));
        System.out.println("  1ft -> mm : " + q1ft.convertTo(LU.MM));
        ok("1ft == 12in",    q1ft.convertTo(LU.IN).equals(new Qty(12,LU.IN)));
        ok("12in == 1ft",    new Qty(12,LU.IN).convertTo(LU.FT).equals(new Qty(1,LU.FT)));
        ok("1ft == 30.48cm", q1ft.convertTo(LU.CM).equals(new Qty(30.48,LU.CM)));
        ok("Self convert",   q1ft.convertTo(LU.FT).equals(q1ft));
        System.out.println("\n[OK] UC5 PASSED");
    }
}
enum LU {
    MM(1.0,"mm"), CM(10.0,"cm"), IN(25.4,"in"), FT(304.8,"ft"), YD(914.4,"yd");
    final double f; final String s;
    LU(double f,String s){this.f=f;this.s=s;}
    Qty convert(double v, LU target){ return new Qty(v*this.f/target.f, target); }
}
class Qty {
    private static final double EPS=1e-9;
    final double value; final LU unit;
    public Qty(double v,LU u){this.value=v;this.unit=u;}
    private double base(){return value*unit.f;}
    public Qty convertTo(LU target){return unit.convert(value,target);}
    @Override public boolean equals(Object o){
        if(!(o instanceof Qty))return false;
        return Math.abs(base()-((Qty)o).base())<EPS;
    }
    @Override public int hashCode(){return Double.hashCode(base());}
    @Override public String toString(){return String.format("%.4f %s",value,unit.s);}
}
