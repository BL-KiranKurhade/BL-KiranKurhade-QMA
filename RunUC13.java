// UC13 - Centralized Arithmetic (Lambda/Functional Interface)  |  java RunUC13.java
public class RunUC13 {
    static void ok(String n,boolean c){System.out.printf("  [%s] %s%n",c?"PASS":"FAIL",n);if(!c)throw new AssertionError(n);}
    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║  UC13: Centralized Arithmetic (Lambda)   ║");
        System.out.println("╚══════════════════════════════════════════╝");
        Qty<LU> two=new Qty<>(2,LU.FT),one=new Qty<>(1,LU.FT);
        System.out.println("\n  ── All ops via single compute() ──");
        System.out.println("  2ft + 1ft = "+two.add(one));
        System.out.println("  2ft - 1ft = "+two.subtract(one));
        System.out.println("  2ft × 3   = "+two.multiply(3));
        System.out.println("  2ft ÷ 2   = "+two.divide(2));
        System.out.println("\n  ── Operation Enum Dispatch ──");
        System.out.printf("  ADD(3,5)      = %.1f%n",Op.ADD.apply(3,5));
        System.out.printf("  SUBTRACT(8,3) = %.1f%n",Op.SUBTRACT.apply(8,3));
        System.out.printf("  MULTIPLY(2,3) = %.1f%n",Op.MULTIPLY.apply(2,3));
        System.out.printf("  DIVIDE(10,2)  = %.1f%n",Op.DIVIDE.apply(10,2));
        System.out.println("\n  ── Custom Lambda ──");
        Qty<LU> mx=two.apply(one,(a,b)->Math.max(a,b));
        System.out.println("  max(2ft,1ft) = "+mx);
        System.out.println("\n  ── Weight via same compute() ──");
        Qty<WU> w5=new Qty<>(5,WU.KG),w3=new Qty<>(3,WU.KG);
        System.out.println("  5kg+3kg = "+w5.add(w3));
        System.out.println("  5kg-3kg = "+w5.subtract(w3));
        ok("add centralized",      two.add(one).equals(new Qty<>(3,LU.FT)));
        ok("subtract centralized", two.subtract(one).equals(one));
        ok("custom lambda max",    mx.equals(two));
        try{Op.DIVIDE.apply(5,0);}catch(ArithmeticException e){System.out.println("  [PASS] Div/0 in enum: "+e.getMessage());}
        System.out.println("\n✔  UC13 PASSED");
    }
}
@FunctionalInterface interface ArithOp{double apply(double a,double b);}
enum Op{
    ADD((a,b)->a+b),SUBTRACT((a,b)->a-b),MULTIPLY((a,b)->a*b),
    DIVIDE((a,b)->{if(b==0)throw new ArithmeticException("Division by zero");return a/b;});
    private final ArithOp fn;Op(ArithOp fn){this.fn=fn;}
    public double apply(double a,double b){return fn.apply(a,b);}
}
interface IUnit{double getBaseUnitFactor();String getSymbol();String getCategory();}
enum LU implements IUnit{IN(1.0,"in"),FT(12.0,"ft");
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
    private Qty<T> compute(Qty<T> o,T t,ArithOp op){
        if(o==null||t==null)throw new IllegalArgumentException("Null arg");
        if(!u.getCategory().equals(o.u.getCategory()))throw new IllegalArgumentException("Category mismatch");
        return new Qty<>(op.apply(base(),o.base())/t.getBaseUnitFactor(),t);
    }
    public Qty<T> add(Qty<T> o)      {return compute(o,u,Op.ADD::apply);}
    public Qty<T> subtract(Qty<T> o) {return compute(o,u,Op.SUBTRACT::apply);}
    public Qty<T> apply(Qty<T> o,ArithOp fn){return compute(o,u,fn);}
    public Qty<T> multiply(double m)  {return new Qty<>(v*m,u);}
    public Qty<T> divide(double d)    {if(d==0)throw new ArithmeticException("Div/0");return new Qty<>(v/d,u);}
    public double getValue(){return v;}public T getUnit(){return u;}
    @Override public boolean equals(Object o){if(!(o instanceof Qty))return false;Qty<?> ot=(Qty<?>)o;
        if(!u.getCategory().equals(ot.u.getCategory()))return false;return Math.abs(base()-ot.base())<EPS;}
    @Override public String toString(){return String.format("%.4f %s",v,u.getSymbol());}
}
