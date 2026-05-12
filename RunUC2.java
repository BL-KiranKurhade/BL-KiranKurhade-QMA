// UC2 - Feet and Inches Measurement Equality | java RunUC2.java
public class RunUC2 {
    static void ok(String n,boolean c){System.out.printf("  [%s] %s%n",c?"PASS":"FAIL",n);if(!c)throw new AssertionError(n);}
    public static void main(String[] args) {
        System.out.println("=== UC2: Feet and Inches Measurement Equality ===");
        Measurement m1=new Measurement(1,0);
        Measurement m2=new Measurement(1,0);
        Measurement m3=new Measurement(0,12);
        Measurement m4=new Measurement(1,1);
        ok("1ft0in == 1ft0in",    m1.equals(m2));
        ok("1ft0in == 0ft12in",   m1.equals(m3));
        ok("1ft0in != 1ft1in",   !m1.equals(m4));
        ok("Null check",          !m1.equals(null));
        ok("Type check",          !m1.equals("1ft"));
        ok("2ft6in == 0ft30in",   new Measurement(2,6).equals(new Measurement(0,30)));
        ok("Self equality",        m1.equals(m1));
        System.out.println("\n[OK] UC2 PASSED");
    }
}
class Measurement {
    private final int feet; private final int inches;
    public Measurement(int feet,int inches){this.feet=feet;this.inches=inches;}
    public int toInches(){return feet*12+inches;}
    @Override public boolean equals(Object obj){
        if(obj==null)return false;
        if(this==obj)return true;
        if(!(obj instanceof Measurement))return false;
        return this.toInches()==((Measurement)obj).toInches();
    }
    @Override public int hashCode(){return Integer.hashCode(toInches());}
    @Override public String toString(){return feet+" ft "+inches+" in ("+toInches()+" total in)";}
}
