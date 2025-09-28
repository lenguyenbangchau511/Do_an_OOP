import java.util.Scanner;

public class Do_an {
    public static void main(String[] args) {

    }
}


abstract class User{
    String id;
    String username;
    String password;
    String role;
    boolean status; // true = active, false = banned

    public User() {
        this("", "", "", "");
    }

    public User(String id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = true; // mac dinh la active
    }

    abstract boolean login(String username, String password);
    abstract void logout();
    abstract void updateInfo();
}

class Customer extends User {
    String name;
    String phonenumber;
    String address;
    String email;

    public Customer() {
        this ("", "", "", "");
    }

    public Customer(String name, String phonenumber, String address, String email) {
        this.name = name;
        this.phonenumber = phonenumber;
        this.address = address;
        this.email = email;
    }

    @Override
    boolean login(String username, String password){
        return this.username.equals(username) && this.password.equals(password);
    }

    @Override
    void logout(){
        System.out.println(username + " da dang xuat.");
    }

    @Override
    void updateInfo(){
        System.out.println("Cap nhat thong tin khach hang" + name);
    }

    public void register(){
        System.out.println("Khach hang " + name + " da dang ky tai khoan thanh cong.");
    }
    public void viewMenu(){}
    public void placeOrder(){}
}

class Admin extends User {
    String name, phonenumber;

    public Admin(){
        this("", "");
    }

    public Admin(String name, String phonenumber){
        this.name = name;
        this.phonenumber = phonenumber;
    }

    @Override
    boolean login(String username, String password){
        return this.username.equals(username) && this.password.equals(password);
    }

    @Override
    void logout(){
        System.out.println(username + " da dang xuat.");
    }

    @Override
    void updateInfo(){
        System.out.println("Cap nhat thong tin khach hang" + name);
    }

    public void manageMenu(){}
    public void manageOrder(){}
    public void manageUser(){}
}

class FoodItem {
    String idfood, name, category, description;
    int price;

    public FoodItem() {
        this("", "", "", "",0);
    }

    public FoodItem(String idfood, String name, String category, String description, int price){
        this.idfood = idfood;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
    }

    public void updateInfo(){}
    public void display(){}
}

interface ICRUD {
    void add();
    void update();
    void delete();
    void find();
}
class Menu implements ICRUD {
    String listFoodItems;
    public void showMenu(){}
    @Override public void add(){}
    @Override public void update(){}
    @Override public void delete(){}
    @Override public void find(){}
}

class Cart {

}

class Order {
    private static int orderCount = 0;
}

class Payment {

}
