public class DVDs {
    public static long nextid = 0;
    private long id;
    private String name;
    private long price;
    private boolean available ;

    public DVDs (String name , long price , boolean available){
        this.id = nextid;
        nextid++;
        this.available = available;
        this.name = name;
        this.price = price;
    }
    public DVDs (long id ,String name , long price , boolean available){
        this.id = id;
        this.available = available;
        this.name = name;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public boolean isAvailable() {
        return available;
    }

    public long getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setPrice(long price) {
        this.price = price;
    }
}
