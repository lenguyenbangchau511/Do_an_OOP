import java.util.Scanner;

public class Do_an {
    public static void main(String[] args) {

    }
}


abstract class User{
    String id, username, password, role;
    abstract void login(){}
    abstract void logout(){}
    abstract void updateInfo(){}
}

class Customer extends User {
    String name, phonenumber, address, email;
    public void register(){}
    public void viewMenu(){}
    public void placeOrder(){}
}

class Admin extends User {
    public void manageMenu(){}
    public void manageOrder(){}
    public void manageUser(){}
}

class FoodItem {
    String idfood;
    String name, price, category, description;
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
