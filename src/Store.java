

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONObject;


public class Store {
   private HashMap<Long , DVDs> dvds ;
   private HashMap<Long,Customers> customers;

   public  Store (){
      this.customers = new HashMap<>();
      this.dvds = new HashMap<>();
   }

   public HashMap<Long, DVDs> getDvds() {
      return dvds;
   }

   public HashMap<Long, Customers> getCustomers() {
      return customers;
   }

   public void BuildStore() throws Exception {
      LoadDVDFromJson();

      LoadCustomersFromJson();
   }

   public void LoadDVDFromJson() throws IOException, ParseException {
      JSONParser jsonParser = new JSONParser();
      FileReader reader = new FileReader("jsonFiles/DVDs.json");
      JSONArray dvdList = (JSONArray) jsonParser.parse(reader);
      //Update next id;
      JSONObject nextidObject = (JSONObject)  dvdList.get(0);
      JSONObject nextid = (JSONObject) nextidObject.get("nextid") ;
      DVDs.nextid = (long) nextid.get("id");
      for (int i = 1; i < dvdList.size(); i++) {
         JSONObject curObject = (JSONObject)  dvdList.get(i);
         JSONObject cur = (JSONObject) curObject.get("dvd") ;
         DVDs dvd = parseJsonToDvd(cur);
         this.dvds.put(dvd.getId() , dvd);
      }

   }

   private DVDs parseJsonToDvd(JSONObject JsonObj){
      long id = (long) JsonObj.get("id");
      String name = (String) JsonObj.get("name");
      long price = (long) JsonObj.get("price");
      boolean available = (boolean) JsonObj.get("available");
      DVDs dvd = new DVDs(id, name , price , available);
      return dvd;
   }

   public void LoadCustomersFromJson() throws IOException, ParseException {
      JSONParser jsonParser = new JSONParser();
      FileReader reader = new FileReader("jsonFiles/Customers.json");
      JSONArray customerList = (JSONArray) jsonParser.parse(reader);
      //Update next id;
      JSONObject nextidObject = (JSONObject) customerList.get(0);
      JSONObject nextid = (JSONObject) nextidObject.get("nextid") ;
      Customers.nextid = (long) nextid.get("id");
      for (int i = 1; i < customerList.size(); i++) {
         JSONObject curObject = (JSONObject)  customerList.get(i);
         JSONObject cur = (JSONObject) curObject.get("client") ;
         Customers customer = parseJsonToCustomer(cur);
         this.customers.put(customer.getId(),customer);
      }
   }

   private Customers parseJsonToCustomer(JSONObject JsonObj){
      long id = (long) JsonObj.get("id");
      String name =(String) JsonObj.get("name");
      JSONArray borrowedJson = (JSONArray) JsonObj.get("borrowed");
      JSONArray boughtJson = (JSONArray) JsonObj.get("bought");
      LinkedList<DVDs> borrowed = new LinkedList<>();

      for (Object dvdID : borrowedJson){
         long dvdid = Long.parseLong(dvdID.toString());
         borrowed.add(dvds.get(dvdid));
      }
      LinkedList<DVDs> bought = new LinkedList<>();
      for (Object dvdID : boughtJson){
         long dvdid = Long.parseLong(dvdID.toString());
         bought.add(dvds.get(dvdid));
      }
      Customers customer = new Customers(id,name,borrowed,bought);
      return  customer;
   }

   public void AddNewCustomer(Customers customer){
      this.customers.put(customer.getId() ,customer);
   }

   private void UpdateDvdsJson(){
      JSONArray dvdsArray = new JSONArray();
      JSONObject nextidObj =new JSONObject();
      JSONObject nextidData = new JSONObject();
      nextidData.put("id" ,DVDs.nextid);
      nextidObj.put("nextid" ,nextidData);
      dvdsArray.add(0,nextidObj);
      for(DVDs dvd : this.dvds.values()){
         JSONObject dvdObj = new JSONObject();
         JSONObject dvdData = new JSONObject();

         dvdData.put("name" , dvd.getName());
         dvdData.put("price",dvd.getPrice());
         dvdData.put("available" , dvd.isAvailable());
         dvdObj.put("dvd" ,dvdData);
         dvdData.put("id",dvd.getId());

         dvdsArray.add(dvdObj);
      }

      try (FileWriter file = new FileWriter("jsonFiles/DVDs.json")) {
         file.write(dvdsArray.toJSONString());
         file.flush();
      } catch (IOException e) {
         e.printStackTrace();
      }

   }

   private void UpdateCustomersJson(){

      JSONArray customersArray = new JSONArray();

     //updating the nextid object
      JSONObject nextidObj =new JSONObject();
      JSONObject nextidData = new JSONObject();
      nextidData.put("id" ,Customers.nextid);
      nextidObj.put("nextid" ,nextidData);
      customersArray.add(0,nextidObj);

      for(Customers customer : this.customers.values()){
         JSONObject customerObj = new JSONObject();
         JSONObject customerData = new JSONObject();

         JSONArray borrowed = new JSONArray();
         for(Object dvd : customer.getBorrowed()){
            DVDs cur = (DVDs) dvd;
            borrowed.add(cur.getId());

         }
         JSONArray bought = new JSONArray();
         for(Object dvd : customer.getBought()){
            DVDs cur = (DVDs) dvd;
            bought.add(cur.getId());
         }

         customerData.put("name" , customer.getName());
         customerData.put("borrowed" , borrowed);
         customerData.put("bought" ,bought);
         customerData.put("id" , customer.getId());

         customerObj.put("client" ,customerData);
         customersArray.add(customerObj);


      }



     try (FileWriter file = new FileWriter("jsonFiles/Customers.json")) {
         file.write(customersArray.toJSONString());
         file.flush();
      } catch (IOException e) {
        e.printStackTrace();
      }
   }

   public void CloseStore(){
      UpdateDvdsJson();
      UpdateCustomersJson();
   }

   public boolean IsAdmin(Customers customer){
      if(customer.getId() == (long)1){
         return true;
      } else {
         return false;
      }
   }

   public void PrintCustomersBorrowedDVDs(){
      for(Customers customer : this.customers.values()){
         if(customer.getId() == (long)1){
            continue;
         }
         System.out.println("--"+customer.getName() +" has borrowed : ");
         if(customer.getBorrowed().size() == 0){
            System.out.println("  NO DVDs borrowed");
         }
         for(Object dvd : customer.getBorrowed()){
            DVDs cur = (DVDs) dvd;
            System.out.print("  "+cur.getId() +") " + cur.getName() +", ");
         }
         System.out.println();
      }
   }

   public void PrintDVDList(){
      for(DVDs dvd : this.dvds.values()){
         System.out.println(dvd.getId()+") "+dvd.getName()+ " is for "+ dvd.getPrice() + "$, and is in stock: " + dvd.isAvailable());
      }
      System.out.println();
   }

   public void AddDVD(DVDs dvd){
      this.dvds.put(dvd.getId(),dvd);
   }

   public void PrintDVDsAvailable() {
      for (Object dvd : this.dvds.values()) {
         DVDs cur = (DVDs) dvd;
         if (cur.isAvailable()) {
            System.out.println(cur.getId() + ") " + cur.getName() + "is " + cur.getPrice() + "$");
         }
      }
      System.out.println();

   }

   public void SetDVDStatus(DVDs dvd , boolean available){
      this.dvds.get(dvd.getId()).setAvailable(available);
   }

}

