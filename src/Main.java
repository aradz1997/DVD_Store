
import org.json.simple.JSONArray;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONObject;


public class Main {
    public static void main(String[] args) throws Exception {

        Store store = new Store();
        try {
            store.BuildStore();
        } catch (Exception e) {
            System.out.println("Something went wrong please try again");
        }
        Scanner myscan = new Scanner(System.in);
        Customers activeCustomer = Login(store, myscan);

        while (true) {
            System.out.println("hello " + activeCustomer.getName() + ",");
            boolean admin = IsAdmin( store ,activeCustomer);
            ShowOptions(admin);
            if(!admin) {
                String operation = myscan.nextLine();
                switch (operation) {
                    case "1":
                        store.PrintDVDsAvailable();
                        break;
                    case "2":
                        BorrowDVD(store, myscan, activeCustomer);
                        break;
                    case "3":
                        ReturnDVD(store, myscan, activeCustomer);
                        break;
                    case "4":
                        PrintCustomerDVDBorrowList(activeCustomer);
                        break;
                    case "5":
                        BuyDVD(store, myscan, activeCustomer);
                        break;

                    case "6":
                        activeCustomer = Logout(store, myscan, activeCustomer);
                        break;
                    case "7":
                        ExitStore(store);
                        break;
                    default:
                        System.out.println("Invalid option. Please Try again.");
                        break;
                }
            }
            else {
                String operation = myscan.nextLine();
                switch (operation) {
                    case "1":
                        store.PrintDVDList();
                        break;
                    case "2":
                        store.PrintCustomersBorrowedDVDs();
                        break;
                    case "3":
                        AddDvd(store, myscan);
                        break;
                    case "4":
                        SetDvdAvailability(store,myscan);
                        break;
                    case "5":
                        activeCustomer = Logout(store, myscan, activeCustomer);
                        break;
                    case "6":
                        ExitStore(store);
                        break;
                    default:
                        System.out.println("Invalid option. Please Try again.");
                        break;
                }


            }
        }


    }

    public static Customers Login(Store store, Scanner myscan) {
        boolean logged_in = false;
        while (!logged_in) {
            System.out.println("Welcome to Arad's DVD store \n 1) already a customer? Enter your id \n 2) New customer ? Press C");
            String input = myscan.nextLine();
            if (input.equals("C")) {
                long id = CreateNewCustomer(store, myscan);
                return store.getCustomers().get(id);

            } else {
                try {
                    boolean exists = CheckIfUserExists(store, Long.parseLong(input));
                    if (exists) {
                        logged_in = true;
                        return store.getCustomers().get(Long.parseLong(input));
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return null;
    }

    public static long CreateNewCustomer(Store store, Scanner myscan) {
        System.out.println("Please Enter your name");
        String name = myscan.nextLine();
        Customers customer = new Customers(name);
        store.AddNewCustomer(customer);
        return customer.getId();
    }

    public static boolean CheckIfUserExists(Store store, long id) {
        if (store.getCustomers().get(id) == null) {
            return false;
        } else
            return true;
    }

    public static void ExitStore(Store store) {
        store.CloseStore();
        System.exit(0);
    }

    public static void ShowOptions(boolean admin) {
       if(admin){
           System.out.println("what would you like to do?");
           System.out.println("1) See DVDs list\n2) See customers' names and borrowed DVDs\n3) Add new DVD\n4)Change DVD Availability\n5) Switch User\n6) Exit");
       } else {
           System.out.println("how can I help you?");
           System.out.println("1) see DVDs available\n2) Borrow DVD\n3) Return DVD \n4) See which DVDS you have borrowed \n5) Buy DVD \n6) Switch User\n7) Exit");
           System.out.println("please enter the number of the option you want");
       }



    }

    public static void BorrowDVD(Store store, Scanner myscan, Customers customer) {
        while (true) {
            store.PrintDVDsAvailable();
            System.out.println("Enter the number of the book you wants to borrow: press c to cancel");
            String input = myscan.nextLine();
            if (input.equals("c")) {
                return;
            } else {
                try {
                    long dvdID = Long.parseLong(input);
                    customer.AddBorrowedDVD(store.getDvds().get(dvdID));
                    System.out.println("thank you for choosing Arad's DVD");
                    return;
                } catch (Exception e) {
                    System.out.println("invalid number, try again");
                    continue;
                }


            }
        }
    }

    public static void ReturnDVD(Store store, Scanner myscan, Customers customer) {
        while (true) {
            PrintCustomerDVDBorrowList(customer);
            System.out.println("Enter the number of the dvd you are returning : press c to cancel");
            String input = myscan.nextLine();
            if (input.equals("c")) {
                return;
            } else {
                try {
                    long dvdID = Long.parseLong(input);
                    customer.DeleteBorrowedDVD(store.getDvds().get(dvdID));
                    System.out.println("thank you for choosing Arad's DVD");
                    return;
                } catch (Exception e) {
                    System.out.println("invalid number, try again");
                    continue;

                }
            }
        }
    }

    public static void PrintCustomerDVDBorrowList(Customers customer) {
        for (Object dvd : customer.getBorrowed()) {
            DVDs cur = (DVDs) dvd;
            System.out.println(cur.getId() + ") " + cur.getName());
        }
        System.out.println();
    }

    public static void BuyDVD(Store store ,Scanner myscan , Customers customer){
        while (true) {
            store.PrintDVDsAvailable();
            System.out.println("Enter the number of the book you wants to buy: press c to cancel");
            String input = myscan.nextLine();
            if (input.equals("c")) {
                return;
            } else {
                try {
                    long dvdID = Long.parseLong(input);
                    customer.AddBoughtDVD(store.getDvds().get(dvdID));
                    System.out.println("thank you for choosing Arad's DVD");
                    return;
                } catch (Exception e) {
                    System.out.println("invalid number, try again");
                    continue;
                }


            }
        }

    }

    public static Customers Logout(Store store ,Scanner myscan , Customers customer){
        System.out.println("thanks "+customer.getName());
        return Login(store , myscan);
    }

    public static boolean IsAdmin(Store store , Customers customer){
        return store.IsAdmin(customer);
    }

    public static void AddDvd(Store store , Scanner myscan){
        System.out.println("we are adding a new dvd : press C to cancel , or P to proceed");
        String input = myscan.nextLine();
        if(input.equals("C")){
            return;
        }
        else {
            System.out.println("enter DVD name:");
            String name = myscan.nextLine();
            System.out.println("enter DVDs price : (numbers)");
            long price = Long.parseLong(myscan.nextLine());
            System.out.println("enter availability : T or F");
            input = myscan.nextLine();
            boolean available = false;
            if(input.equals("T")){
                available = true;
            }
            DVDs dvd = new DVDs(name, price , available);
            store.AddDVD(dvd);
            System.out.println("the dvd was added successfully");
            return;
        }
    }

    public static void SetDvdAvailability(Store store , Scanner myscan){
        store.PrintDVDList();
        System.out.println("enter the number of dvd you want to change : press c to cancel");
        String input = myscan.nextLine();
        if(input.equals("c")){
            return;
        } else {
            try{
                long id = Long.parseLong(input);
                System.out.println("Whats the new status : T or F");
                input = myscan.nextLine();
                boolean available = true;
                if(input.equals("F")){
                    available = false;
                }
                store.SetDVDStatus(store.getDvds().get(id),available );
                System.out.println("the status updated successfully");
            }catch (Exception e){
                System.out.println("somthing went wrong, please try again");
            }
        }
    }






}
