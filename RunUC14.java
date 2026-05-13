// UC14 - Temperature Measurement (Non-linear, ISP) | java RunUC14.java
public class RunUC14{
    static void ok(String n,boolean c){System.out.printf("  [%s] %s%n",c?"PASS":"FAIL",n);if(!c)throw new AssertionError(n);}
    static void ok(String n,java.util.function.Supplier<Boolean> s){ok(n,s.get());}
    public static void main(String[] args){
        System.out.println("=== UC14: Temperature Measurement (Non-linear, ISP) ===");
        Temperature boiling =new Temperature(100,TempUnit.CELSIUS);
        Temperature freezing=new Temperature(0,TempUnit.CELSIUS);
        Temperature bodyF   =new Temperature(98.6,TempUnit.FAHRENHEIT);
        Temperature absZero =new Temperature(0,TempUnit.KELVIN);
        System.out.println("\n-- Non-Linear Conversions --");
        System.out.println("  100C -> F  : "+boiling.convertTo(TempUnit.FAHRENHEIT));
        System.out.println("  100C -> K  : "+boiling.convertTo(TempUnit.KELVIN));
        System.out.println("  32F  -> C  : "+new Temperature(32,TempUnit.FAHRENHEIT).convertTo(TempUnit.CELSIUS));
        System.out.println("  0 K  -> C  : "+absZero.convertTo(TempUnit.CELSIUS));
        System.out.println("  98.6F-> C  : "+bodyF.convertTo(TempUnit.CELSIUS));
        System.out.println("\n-- Equality (via Celsius pivot) --");
        System.out.println("  100C == 212F   : "+boiling.equals(new Temperature(212,TempUnit.FAHRENHEIT)));
        System.out.println("  0C   == 273.15K: "+new Temperature(0,TempUnit.CELSIUS).equals(new Temperature(273.15,TempUnit.KELVIN)));
        System.out.println("\n-- Delta --");
        System.out.printf("  100C - 0C = %.2fC delta%n",boiling.deltaFrom(freezing));
        System.out.println("\n-- ISP: Temperature does NOT support add() --");
        try{boiling.add(freezing);System.out.println("  [FAIL] should have thrown");}
        catch(UnsupportedOperationException e){System.out.println("  [PASS] "+e.getMessage());}
        System.out.println("\n-- IConvertible polymorphism --");
        IConvertible<Temperature> conv=boiling;
        Temperature t212=new Temperature(212,TempUnit.FAHRENHEIT);
        System.out.println("  via IConvertible: "+conv.convertTo(t212));
        System.out.println("\n-- Linear Qty: IConvertible + IArithmetic --");
        Qty<LU> q1=new Qty<>(1,LU.FT),q2=new Qty<>(1,LU.FT);
        System.out.println("  1ft + 1ft = "+q1.add(q2));
        System.out.println("  1ft - 1ft = "+q1.subtract(q2));
        System.out.println("\n-- Assertions --");
        ok("100C == 212F",boiling.equals(new Temperature(212,TempUnit.FAHRENHEIT)));
        ok("0C == 273.15K",new Temperature(0,TempUnit.CELSIUS).equals(new Temperature(273.15,TempUnit.KELVIN)));
        ok("Neg Kelvin throws",()->{ try{new Temperature(-1,TempUnit.KELVIN);return false;}catch(IllegalArgumentException e){return true;} });
        ok("add() throws UnsupportedOpEx",()->{ try{boiling.add(freezing);return false;}catch(UnsupportedOperationException e){return true;} });
        ok("1ft+1ft=2ft",new Qty<>(1,LU.FT).add(new Qty<>(1,LU.FT)).equals(new Qty<>(2,LU.FT)));
        System.out.println("\n[OK] UC14 PASSED");
    }
}
interface IConvertible<T>{ T convertTo(T target); }
interface IArithmetic<T>{ T add(T o); T subtract(T o); }
interface IUnit{double getBaseUnitFactor();String getSymbol();String getCategory();}
enum LU implements IUnit{
    IN(1.0,"in"),FT(12.0,"ft");
    private final double f;private final String s;LU(double f,String s){this.f=f;this.s=s;}
    public double getBaseUnitFactor(){return f;}public String getSymbol(){return s;}public String getCategory(){return "LENGTH";}
}
enum TempUnit{
    CELSIUS{public double toCelsius(double v){return v;}public double fromCelsius(double c){return c;}public String sym(){return "C";}},
    FAHRENHEIT{public double toCelsius(double v){return (v-32)*5.0/9;}public double fromCelsius(double c){return c*9.0/5+32;}public String sym(){return "F";}},
    KELVIN{public double toCelsius(double v){return v-273.15;}public double fromCelsius(double c){return c+273.15;}public String sym(){return "K";}};
    public abstract double toCelsius(double v);
    public abstract double fromCelsius(double c);
    public abstract String sym();
    public double convert(double v,TempUnit t){return t.fromCelsius(toCelsius(v));}
}
final class Temperature implements IConvertible<Temperature>{
    private static final double EPS=1e-9;
    private final double v;private final TempUnit u;
    public Temperature(double v,TempUnit u){
        if(u==null)throw new IllegalArgumentException("Null unit");
        if(u==TempUnit.KELVIN&&v<0)throw new IllegalArgumentException("Negative Kelvin: "+v);
        this.v=v;this.u=u;
    }
    public TempUnit getUnit(){return u;}
    public double getValue(){return v;}
    private double toCelsius(){return u.toCelsius(v);}
    @Override public Temperature convertTo(Temperature target){
        return new Temperature(u.convert(v,target.getUnit()),target.getUnit());
    }
    public Temperature convertTo(TempUnit target){return new Temperature(u.convert(v,target),target);}
    public double deltaFrom(Temperature o){return toCelsius()-o.toCelsius();}
    public Temperature add(Temperature o){throw new UnsupportedOperationException("Adding absolute temps is meaningless. Use deltaFrom().");}
    @Override public boolean equals(Object o){
        if(!(o instanceof Temperature))return false;
        return Math.abs(toCelsius()-((Temperature)o).toCelsius())<EPS;
    }
    @Override public String toString(){return String.format("%.2f %s",v,u.sym());}
}
class Qty<T extends IUnit> implements IConvertible<Qty<T>>,IArithmetic<Qty<T>>{
    private static final double EPS=1e-9;
    private final double v;private final T u;
    public Qty(double v,T u){this.v=v;this.u=u;}
    private double base(){return v*u.getBaseUnitFactor();}
    @Override public Qty<T> convertTo(Qty<T> target){return new Qty<>(base()/target.u.getBaseUnitFactor(),target.u);}
    public Qty<T> convertTo(T t){return new Qty<>(base()/t.getBaseUnitFactor(),t);}
    @Override public Qty<T> add(Qty<T> o){return new Qty<>((base()+o.base())/u.getBaseUnitFactor(),u);}
    @Override public Qty<T> subtract(Qty<T> o){return new Qty<>((base()-o.base())/u.getBaseUnitFactor(),u);}
    public double getValue(){return v;}
    @Override public boolean equals(Object o){if(!(o instanceof Qty))return false;return Math.abs(base()-((Qty<?>)o).base())<EPS;}
    @Override public String toString(){return String.format("%.4f %s",v,u.getSymbol());}
}
