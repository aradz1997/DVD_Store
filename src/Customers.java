import java.util.*;

public class Customers {
    public static long nextid = 0;
    private long id;
    private String name;
    private LinkedList<DVDs> bought;
    private LinkedList<DVDs> borrowed;

    public Customers(String name) {
        this.id = nextid;
        nextid++;
        this.name = name;
        this.borrowed = new LinkedList<>();
        this.bought = new LinkedList<>();
    }
    public Customers(long id , String name , LinkedList<DVDs> borrowed  ,LinkedList<DVDs> bought){
        this.id = id;
        this.name = name;
        this.borrowed = borrowed;
        this.bought = bought;
    }

    public LinkedList getBorrowed() {
        return borrowed;
    }

    public LinkedList getBought() {
        return bought;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void AddBorrowedDVD(DVDs dvd) {
        this.borrowed.add(dvd);
    }

    public void AddBoughtDVD(DVDs dvd){ this.bought.add(dvd); }

    public void DeleteBorrowedDVD(DVDs dvd){
        this.borrowed.remove(dvd);
    }
}
