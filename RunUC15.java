// UC15 - N-Tier Architecture (Model/DTO/Repository/Service/Controller) | java RunUC15.java
import java.util.*;import java.util.concurrent.atomic.AtomicLong;import java.util.stream.*;

public class RunUC15{
    public static void main(String[] args){
        System.out.println("=== UC15: N-Tier Architecture ===");
        MeasurementController ctrl=new MeasurementController(new MeasurementService(new InMemoryMeasurementRepo()));
        System.out.println("\n-- CREATE --");
        Response<MeasurementResponse> r1=ctrl.create(new MeasurementRequest(10.5,"LENGTH","FEET"));
        Response<MeasurementResponse> r2=ctrl.create(new MeasurementRequest(5.0,"WEIGHT","KG"));
        Response<MeasurementResponse> r3=ctrl.create(new MeasurementRequest(2.5,"LENGTH","METER"));
        System.out.println("  "+r1.getData());
        System.out.println("  "+r2.getData());
        System.out.println("  "+r3.getData());
        System.out.println("\n-- GET ALL --");
        ctrl.getAll().getData().forEach(m->System.out.println("  "+m));
        System.out.println("\n-- GET BY ID --");
        System.out.println("  id=1: "+ctrl.getById(1L).getData());
        System.out.println("\n-- GET BY CATEGORY --");
        ctrl.getByCategory("LENGTH").getData().forEach(m->System.out.println("  "+m));
        System.out.println("\n-- UPDATE --");
        Response<MeasurementResponse> upd=ctrl.update(1L,new MeasurementRequest(99.9,"LENGTH","METER"));
        System.out.println("  "+upd.getData());
        System.out.println("\n-- DELETE --");
        Response<String> del=ctrl.delete(2L);
        System.out.println("  "+del.getData());
        System.out.println("  after delete count: "+ctrl.getAll().getData().size());
        System.out.println("\n-- ERROR HANDLING (Error as Data) --");
        Response<MeasurementResponse> notFound=ctrl.getById(999L);
        System.out.println("  success="+notFound.isSuccess()+" msg="+notFound.getMessage());
        Response<MeasurementResponse> badReq=ctrl.create(new MeasurementRequest(-1,"LENGTH","FEET"));
        System.out.println("  success="+badReq.isSuccess()+" msg="+badReq.getMessage());
        System.out.println("\n-- Assertions --");
        ok("r1 success",r1.isSuccess());
        ok("r1 id not null",r1.getData().getId()!=null);
        ok("getAll has 2 items after delete",ctrl.getAll().getData().size()==2);
        ok("404 not success",!notFound.isSuccess());
        ok("negative value rejected",!badReq.isSuccess());
        System.out.println("\n[OK] UC15 PASSED");
    }
    static void ok(String n,boolean c){System.out.printf("  [%s] %s%n",c?"PASS":"FAIL",n);if(!c)throw new AssertionError(n);}
}

// ── Model ──────────────────────────────────────────────────────────────
enum Category{LENGTH,WEIGHT,VOLUME,TEMPERATURE}
class Measurement{
    private Long id;private double value;private Category category;private String unit;
    public Measurement(){}
    public Measurement(Long id,double value,Category category,String unit){
        this.id=id;this.value=value;this.category=category;this.unit=unit;
    }
    public Long getId(){return id;}public void setId(Long id){this.id=id;}
    public double getValue(){return value;}public void setValue(double v){this.value=v;}
    public Category getCategory(){return category;}public void setCategory(Category c){this.category=c;}
    public String getUnit(){return unit;}public void setUnit(String u){this.unit=u;}
    @Override public String toString(){return String.format("Measurement{id=%d, value=%.2f, cat=%s, unit=%s}",id,value,category,unit);}
}

// ── DTO ────────────────────────────────────────────────────────────────
class MeasurementRequest{
    private double value;private String category;private String unit;
    public MeasurementRequest(double v,String c,String u){this.value=v;this.category=c;this.unit=u;}
    public double getValue(){return value;}
    public String getCategory(){return category;}
    public String getUnit(){return unit;}
}
class MeasurementResponse{
    private Long id;private double value;private String category;private String unit;
    public MeasurementResponse(Long id,double v,String c,String u){this.id=id;this.value=v;this.category=c;this.unit=u;}
    public Long getId(){return id;}
    @Override public String toString(){return String.format("MeasurementResponse{id=%d, value=%.2f, cat=%s, unit=%s}",id,value,category,unit);}
}
class Response<T>{
    private final boolean success;private final String message;private final T data;
    private Response(boolean s,String m,T d){success=s;message=m;data=d;}
    public static <T> Response<T> ok(T data){return new Response<>(true,"OK",data);}
    public static <T> Response<T> err(String msg){return new Response<>(false,msg,null);}
    public boolean isSuccess(){return success;}public String getMessage(){return message;}public T getData(){return data;}
}

// ── Repository ─────────────────────────────────────────────────────────
interface MeasurementRepository{
    Measurement save(Measurement m);
    Optional<Measurement> findById(Long id);
    List<Measurement> findAll();
    List<Measurement> findByCategory(Category c);
    Measurement update(Measurement m);
    boolean deleteById(Long id);
}
class InMemoryMeasurementRepo implements MeasurementRepository{
    private final Map<Long,Measurement> store=new LinkedHashMap<>();
    private final AtomicLong seq=new AtomicLong(1);
    @Override public Measurement save(Measurement m){
        m.setId(seq.getAndIncrement());store.put(m.getId(),m);return m;
    }
    @Override public Optional<Measurement> findById(Long id){return Optional.ofNullable(store.get(id));}
    @Override public List<Measurement> findAll(){return new ArrayList<>(store.values());}
    @Override public List<Measurement> findByCategory(Category c){
        return store.values().stream().filter(m->m.getCategory()==c).collect(Collectors.toList());
    }
    @Override public Measurement update(Measurement m){store.put(m.getId(),m);return m;}
    @Override public boolean deleteById(Long id){return store.remove(id)!=null;}
}

// ── Service ────────────────────────────────────────────────────────────
class MeasurementService{
    private final MeasurementRepository repo;
    public MeasurementService(MeasurementRepository repo){this.repo=repo;}
    public Response<MeasurementResponse> create(MeasurementRequest req){
        if(req.getValue()<0) return Response.err("Value must be non-negative");
        try{
            Category cat=Category.valueOf(req.getCategory());
            Measurement m=new Measurement(null,req.getValue(),cat,req.getUnit());
            Measurement saved=repo.save(m);
            return Response.ok(toResponse(saved));
        }catch(IllegalArgumentException e){return Response.err("Unknown category: "+req.getCategory());}
    }
    public Response<MeasurementResponse> getById(Long id){
        return repo.findById(id).map(m->Response.<MeasurementResponse>ok(toResponse(m)))
                .orElse(Response.err("Not found: "+id));
    }
    public Response<List<MeasurementResponse>> getAll(){
        List<MeasurementResponse> list=repo.findAll().stream().map(this::toResponse).collect(Collectors.toList());
        return Response.ok(list);
    }
    public Response<List<MeasurementResponse>> getByCategory(String cat){
        try{
            Category c=Category.valueOf(cat);
            List<MeasurementResponse> list=repo.findByCategory(c).stream().map(this::toResponse).collect(Collectors.toList());
            return Response.ok(list);
        }catch(IllegalArgumentException e){return Response.err("Unknown category: "+cat);}
    }
    public Response<MeasurementResponse> update(Long id,MeasurementRequest req){
        return repo.findById(id).map(existing->{
            try{
                existing.setValue(req.getValue());
                existing.setCategory(Category.valueOf(req.getCategory()));
                existing.setUnit(req.getUnit());
                return Response.<MeasurementResponse>ok(toResponse(repo.update(existing)));
            }catch(IllegalArgumentException e){return Response.<MeasurementResponse>err("Unknown category");}
        }).orElse(Response.err("Not found: "+id));
    }
    public Response<String> delete(Long id){
        return repo.deleteById(id)?Response.ok("Deleted id="+id):Response.err("Not found: "+id);
    }
    private MeasurementResponse toResponse(Measurement m){
        return new MeasurementResponse(m.getId(),m.getValue(),m.getCategory().name(),m.getUnit());
    }
}

// ── Controller ─────────────────────────────────────────────────────────
class MeasurementController{
    private final MeasurementService svc;
    public MeasurementController(MeasurementService svc){this.svc=svc;}
    public Response<MeasurementResponse> create(MeasurementRequest req){return svc.create(req);}
    public Response<MeasurementResponse> getById(Long id){return svc.getById(id);}
    public Response<List<MeasurementResponse>> getAll(){return svc.getAll();}
    public Response<List<MeasurementResponse>> getByCategory(String cat){return svc.getByCategory(cat);}
    public Response<MeasurementResponse> update(Long id,MeasurementRequest req){return svc.update(id,req);}
    public Response<String> delete(Long id){return svc.delete(id);}
}
