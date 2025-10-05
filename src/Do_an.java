import java.util.*;
import java.io.*;
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
// Ghi nhập và đọc file
// Ở đây dùng filename là để khi chạy code có thể tự do dặt tên file , không cần cố định tên file
class FileManager {
    //  Ghi danh sách người dùng 
    public static void writeUsersToFile(ArrayList<User> users, String filename) {
        //Phương thức ghi danh sách người dùng (ArrayList<User>) ra file văn bản tên là filename
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            //Sử dụng try-with-resources, đảm bảo bw (đối tượng ghi file) tự động đóng sau khi kết thúc khối try
            for (User u : users) {
                //duyệt qua từng phần tử trong file
                if (u instanceof Admin) {
                    //kiểm tra tính đa hình xem user có nằm trong lớp con ko
                    Admin a = (Admin) u;
                    bw.write("Admin," + a.id + "," + a.username + "," + a.password + "," + a.role + "," + a.name + "," + a.phonenumber);
                    //ép u thành admin để truy cặp các thuộc tính của lớp này
                } else if (u instanceof Customer) {
                    Customer c = (Customer) u;
                    bw.write("Customer," + c.id + "," + c.username + "," + c.password + "," + c.role + "," + c.name + "," + c.phonenumber + "," + c.address + "," + c.email);
                    //cũng giống như trên nhưng mà là ép thành customer
                }
                bw.newLine();
                //xuống dòng sau mỗi người dùng
            }
            System.out.println("Đã ghi file người dùng: " + filename);
            //in ra thông báo
        } catch (IOException e) {
            System.out.println("Lỗi ghi file người dùng: " + e.getMessage());
            // nếu có lỗi thì in ra lỗi ở đâu
        }
    }

    // Đọc danh sách người dùng
    public static ArrayList<User> readUsersFromFile(String filename) {
        ArrayList<User> list = new ArrayList<>();
        //tạo các danh sách rỗng có chứa user từ file
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            //mở file để đọc
            String line;
            while ((line = br.readLine()) != null) {
                //đọc từng dòng cho đến khi hết
                String[] data = line.split(",");
                // tách bằng dấu phẩy để tách thành cột dữ liệu
                if (data[0].equals("Admin") && data.length >= 7) {
                    list.add(new Admin(data[1], data[2], data[3], data[4], data[5], data[6]));
                } else if (data[0].equals("Customer") && data.length >= 9) {
                    list.add(new Customer(data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8]));
                }
                //dựa vào data[0] để xác định loại người dùng
                // các ô data còn lại dùng để chứa các thuộc tính của đối tượng
            }
            System.out.println("Đã đọc " + list.size() + " người dùng từ file " + filename);
            //in ra thông báo số người đã đọc được
        } catch (IOException e) {
            System.out.println("Lỗi đọc file người dùng: " + e.getMessage());
            // nếu có lỗi thì in ra lỗi
        }
        return list;
    }

    // Ghi danh sách món ăn
    public static void writeFoodItemsToFile(ArrayList<FoodItem> foods, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (FoodItem f : foods) {
                bw.write(f.idfood + "," + f.name + "," + f.category + "," + f.description + "," + f.price);
                // ghi dữ liệu mỗi món ăn ra một dòng
                bw.newLine();
            }
            System.out.println("Đã ghi file món ăn: " + filename);
        } catch (IOException e) {
            System.out.println("Lỗi ghi file món ăn: " + e.getMessage());
        }
    }

    //Đọc danh sách món ăn
    public static ArrayList<FoodItem> readFoodItemsFromFile(String filename) {
        ArrayList<FoodItem> foods = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    foods.add(new FoodItem(data[0], data[1], data[2], data[3], Integer.parseInt(data[4])));
                    // kiểm tra đủ 5 phần tử thì thêm fooditem mới vào danh sách
                }
            }
            System.out.println("Đã đọc " + foods.size() + " món ăn từ file " + filename);
        } catch (IOException e) {
            System.out.println("Lỗi đọc file món ăn: " + e.getMessage());
        }
        return foods;
    }
}
