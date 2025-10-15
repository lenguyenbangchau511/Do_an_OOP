import java.util.*;
import java.io.*;
import java.time.LocalDateTime;

public class Do_an {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<FoodItem> menu = new ArrayList<>();
        ArrayList<Order> orders = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        ArrayList<Payment> payments = new ArrayList<>();
        FileManager fileManager = new FileManager();
        FileManager.UserFileManager userFM = fileManager. new UserFileManager();
        FileManager.FoodFileManager foodFM = fileManager. new FoodFileManager();
        FileManager.OrderFileManager orderFM = fileManager. new OrderFileManager(menu);

// Ghi dữ liệu
        userFM.writeToFile(users, "users.txt");
        foodFM.writeToFile(menu, "foods.txt");
        orderFM.writeToFile(orders, "orders.txt");

// Đọc dữ liệu
        ArrayList<User> usersFromFile = userFM.readFromFile("users.txt");
        ArrayList<FoodItem> foodsFromFile = foodFM.readFromFile("foods.txt");
        ArrayList<Order> ordersFromFile = orderFM.readFromFile("orders.txt");


        // Tạo sẵn 1 admin mẫu
        Admin admin = new Admin("admin", "123");
        admin.id = "A01";
        admin.username = "admin";
        admin.password = "123";
        admin.role = "Admin";
        users.add(admin);

        // Tạo menu mẫu
        menu.add(new FoodItem("F01", "Burger Bò", "Burger", "Ngon tuyệt", 45000));
        menu.add(new FoodItem("F02", "Gà Rán", "Fried Chicken", "Giòn tan", 40000));

        int choice;
        do {
            System.out.println("\n===== HỆ THỐNG QUẢN LÝ CỬA HÀNG =====");
            System.out.println("1. Đăng nhập Admin");
            System.out.println("2. Đăng ký khách hàng");
            System.out.println("3. Đăng nhập Khách hàng");
            System.out.println("4. Xem menu");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> {
                    System.out.print("Tên đăng nhập: ");
                    String adUser = sc.nextLine();
                    System.out.print("Mật khẩu: ");
                    String adPass = sc.nextLine();
                    boolean found = false;
                    for (User u : users) {
                        if (u instanceof Admin && u.login(adUser, adPass)) {
                            System.out.println("Đăng nhập Admin thành công!");
                            found = true;
                            Admin a = (Admin) u;
                            adminMenu(a, menu, orders, users);
                            break;
                        }
                    }
                    if (!found) System.out.println("Sai tài khoản hoặc mật khẩu!");
                }
                case 2 -> {
                    System.out.println("\n===== ĐĂNG KÝ KHÁCH HÀNG =====");
                    System.out.print("ID: ");
                    String id = sc.nextLine();
                    System.out.print("Tên người dùng: ");
                    String username = sc.nextLine();
                    System.out.print("Mật khẩu: ");
                    String password = sc.nextLine();
                    System.out.print("Họ tên: ");
                    String name = sc.nextLine();
                    System.out.print("SĐT: ");
                    String phone = sc.nextLine();
                    System.out.print("Địa chỉ: ");
                    String address = sc.nextLine();
                    System.out.print("Email: ");
                    String email = sc.nextLine();

                    Customer c = new Customer(name, phone, address, email);
                    c.id = id;
                    c.username = username;
                    c.password = password;
                    c.role = "Customer";
                    users.add(c);
                    System.out.println("Đăng ký thành công!");
                }
                case 3 -> {
                    System.out.print("Tên đăng nhập: ");
                    String user = sc.nextLine();
                    System.out.print("Mật khẩu: ");
                    String pass = sc.nextLine();
                    boolean found = false;
                    for (User u : users) {
                        if (u instanceof Customer && u.login(user, pass)) {
                            found = true;
                            Customer c = (Customer) u;
                            customerMenu(c, menu, orders, payments);
                            break;
                        }
                    }
                    if (!found) System.out.println("Sai tài khoản hoặc mật khẩu!");
                }
                case 4 -> {
                    System.out.println("\n===== MENU HIỆN TẠI =====");
                    for (FoodItem f : menu) f.display();
                }
                case 0 -> System.out.println("Thoát chương trình.");
                default -> System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (choice != 0);
    }

    // Menu cho Admin
    static void adminMenu(Admin admin, ArrayList<FoodItem> menu, ArrayList<Order> orders, ArrayList<User> users) {
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n===== MENU ADMIN =====");
            System.out.println("1. Quản lý món ăn");
            System.out.println("2. Quản lý đơn hàng");
            System.out.println("3. Quản lý người dùng");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(sc.nextLine());
            switch (choice) {
                case 1 -> admin.manageMenu(menu);
                case 2 -> admin.manageOrder(orders);
                case 3 -> admin.manageUser(users);
                case 0 -> admin.logout();
                default -> System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (choice != 0);
    }

    // Menu cho Customer
    static void customerMenu(Customer c, ArrayList<FoodItem> menu, ArrayList<Order> orders, ArrayList<Payment> payments) {
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n===== MENU KHÁCH HÀNG =====");
            System.out.println("1. Xem menu");
            System.out.println("2. Đặt món");
            System.out.println("3. Xem đơn hàng");
            System.out.println("0. Đăng xuất");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> c.view(menu);
                case 2 -> {
                    List<OrderItem> orderList = new ArrayList<>();
                    c.placeOrder(menu);
                    System.out.println("Nhập tổng tiền đơn hàng để tạo thanh toán: ");
                    int total = Integer.parseInt(sc.nextLine());
                    Order o = new Order(c.name, orderList);
                    o.total = total;
                    orders.add(o);
                    Payment p = c.payOrder(o);
                    payments.add(p);
                }
                case 3 -> {
                    if (orders.isEmpty()) System.out.println("Chưa có đơn hàng nào.");
                    else for (Order o : orders) o.display();
                }
                case 0 -> c.logout();
                default -> System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (choice != 0);
    }
}

// ========================== CÁC LỚP NGHIỆP VỤ ===============================

abstract class User {
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
        this.status = true;
    }

    abstract boolean login(String username, String password);
    abstract void logout();
    abstract void updateInfo();
}

class Customer extends User {
    String name, phonenumber, address, email;

    public Customer() { this("", "", "", ""); }

    public Customer(String name, String phonenumber, String address, String email) {
        this.name = name;
        this.phonenumber = phonenumber;
        this.address = address;
        this.email = email;
    }

    @Override
    boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    @Override
    void logout() {
        System.out.println(username + " đã đăng xuất.");
    }

    @Override
    void updateInfo() {
        System.out.println("Cập nhật thông tin khách hàng " + name);
    }

    public void view(List<FoodItem> menu) {
        System.out.println("\n===== MENU CỬA HÀNG =====");
        for (FoodItem item : menu) item.display();
    }

    public void placeOrder(List<FoodItem> menu) {
        System.out.println("\nĐặt món thành công (giả lập).");
    }

    public Payment payOrder(Order order) {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n===== THANH TOÁN =====");
        System.out.println("1. Tiền mặt");
        System.out.println("2. Chuyển khoản");
        System.out.print("Chọn phương thức (1-2): ");
        int choice = Integer.parseInt(sc.nextLine());
        String method = switch (choice) {
            case 1 -> "Tiền mặt";
            case 2 -> "Chuyển khoản";
            default -> "Khác";
        };

        Payment payment = new Payment(order.orderId, this.name, method, order.total);
        System.out.println("Thanh toán thành công bằng " + method + "!");
        payment.display();
        return payment;
    }
}

class Admin extends User {
    String name, phonenumber;

    public Admin(String name, String phonenumber) {
        this.name = name;
        this.phonenumber = phonenumber;
    }

    @Override
    boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    @Override
    void logout() {
        System.out.println(username + " đã đăng xuất.");
    }

    @Override
    void updateInfo() {
        System.out.println("Cập nhật thông tin admin " + name);
    }

    public void manageMenu(List<FoodItem> menu) {
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n===== QUẢN LÝ MENU =====");
            System.out.println("1. Xem danh sách món");
            System.out.println("2. Thêm món mới");
            System.out.println("3. Cập nhật món");
            System.out.println("4. Xóa món");
            System.out.println("5. Tìm kiếm món ăn");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1 -> { for (FoodItem f : menu) f.display(); }
                case 2 -> {
                    System.out.print("ID: "); String id = sc.nextLine();
                    System.out.print("Tên: "); String name = sc.nextLine();
                    System.out.print("Loại: "); String cat = sc.nextLine();
                    System.out.print("Mô tả: "); String desc = sc.nextLine();
                    System.out.print("Giá: "); int price = Integer.parseInt(sc.nextLine());
                    menu.add(new FoodItem(id, name, cat, desc, price));
                }
                case 3 -> {
                    System.out.print("Nhập ID món: ");
                    String findId = sc.nextLine();
                    for (FoodItem f : menu)
                        if (f.idfood.equalsIgnoreCase(findId)) { f.updateInfo(); break; }
                }
                case 4 -> {
                    System.out.print("Nhập ID muốn xóa: ");
                    String del = sc.nextLine();
                    menu.removeIf(f -> f.idfood.equalsIgnoreCase(del));
                }
                case 5 -> {
                    System.out.print("Nhập tên món cần tìm: ");
                    String keyword = sc.nextLine().toLowerCase();
                    boolean found = false;
                    for (FoodItem f : menu) {
                        if (f.name.toLowerCase().contains(keyword)) {
                            f.display();
                            found = true;
                        }
                    }
                    if (!found) System.out.println("❌ Không tìm thấy món nào!");
                }
                case 0 -> System.out.println("Thoát quản lý menu.");
                default -> System.out.println("Sai lựa chọn!");
            }
        } while (choice != 0);
    }

    public void searchFood(List<FoodItem> menu) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập tên món cần tìm: ");
        String keyword = sc.nextLine().toLowerCase();
        for (FoodItem f : menu) {
            if (f.name.toLowerCase().contains(keyword)) f.display();
        }
    }

    public void manageOrder(List<Order> orders) {
        if (orders.isEmpty()) System.out.println("Chưa có đơn hàng.");
        else for (Order o : orders) o.display();
    }

    public void manageUser(List<User> users) {
        for (User u : users) {
            System.out.printf("%s | %s | %s | %s\n",
                    u.id, u.username, u.role, u.status ? "Active" : "Banned");
        }
    }
}

class FoodItem {
    String idfood, name, category, description;
    int price;

    public FoodItem(String idfood, String name, String category, String description, int price) {
        this.idfood = idfood;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
    }

    public void updateInfo() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Tên mới: ");
        String newName = sc.nextLine();
        if (!newName.isEmpty()) name = newName;
    }

    public void display() {
        System.out.printf("%s | %-15s | %-10s | %dđ | %s\n", idfood, name, category, price, description);
    }
}

class OrderItem {
    FoodItem food;
    int quantity;

    public OrderItem(FoodItem f, int q) { food = f; quantity = q; }

    public void display() {
        System.out.printf("%s x%d = %dđ\n", food.name, quantity, food.price * quantity);
    }
}

class Order {
    static int orderCount = 0;
    int orderId;
    List<OrderItem> items;
    String customerName;
    int total;

    public Order(String customerName, List<OrderItem> items) {
        this.orderId = ++orderCount;
        this.customerName = customerName;
        this.items = items;
        this.total = calculateTotal();
    }

    private int calculateTotal() {
        int sum = 0;
        for (OrderItem o : items) sum += o.food.price * o.quantity;
        return sum;
    }

    public void display() {
        System.out.println("\n===== ĐƠN HÀNG #" + orderId + " =====");
        System.out.println("Khách: " + customerName);
        for (OrderItem o : items) o.display();
        System.out.println("Tổng cộng: " + total + "đ");
    }
}

class Payment {
    static int count = 0;
    int paymentId, orderId, amount;
    String customerName, method;
    boolean isPaid;
    LocalDateTime paymentTime;

    public Payment(int orderId, String customerName, String method, int amount) {
        this.paymentId = ++count;
        this.orderId = orderId;
        this.customerName = customerName;
        this.method = method;
        this.amount = amount;
        this.isPaid = true;
        this.paymentTime = LocalDateTime.now();
    }

    public void display() {
        System.out.println("\nThanh toán #" + paymentId + " - " + method + " - " + amount + "đ");
    }
}
interface IFileHandler<T> {
    void writeToFile(ArrayList<T> list, String filename);
    ArrayList<T> readFromFile(String filename);
}
class FileManager {

    class UserFileManager implements IFileHandler<User> {

        @Override
        public void writeToFile(ArrayList<User> users, String filename) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
                for (User u : users) {
                    bw.write(u.role + "," + u.id + "," + u.username + "," + u.password + "," + u.status);
                    bw.newLine();
                }
                System.out.println("✅ Ghi người dùng thành công!");
            } catch (IOException e) {
                System.out.println("❌ Lỗi ghi người dùng: " + e.getMessage());
            }
        }

        @Override
        public ArrayList<User> readFromFile(String filename) {
            ArrayList<User> users = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length >= 5) {
                        String role = data[0];
                        String id = data[1];
                        String username = data[2];
                        String password = data[3];
                        boolean status = Boolean.parseBoolean(data[4]);

                        if (role.equalsIgnoreCase("Admin")) {
                            Admin a = new Admin("Tên admin", "SĐT");
                            a.id = id;
                            a.username = username;
                            a.password = password;
                            a.role = role;
                            a.status = status;
                            users.add(a);
                        } else if (role.equalsIgnoreCase("Customer")) {
                            Customer c = new Customer("Tên KH", "SĐT", "Địa chỉ", "Email");
                            c.id = id;
                            c.username = username;
                            c.password = password;
                            c.role = role;
                            c.status = status;
                            users.add(c);
                        }
                    }
                }
                System.out.println("✅ Đọc file người dùng thành công!");
            } catch (IOException e) {
                System.out.println("❌ Lỗi đọc người dùng: " + e.getMessage());
            }
            return users;
        }
    }


    class FoodFileManager implements IFileHandler<FoodItem> {

        @Override
        public void writeToFile(ArrayList<FoodItem> foods, String filename) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
                for (FoodItem f : foods) {
                    bw.write(f.idfood + "," + f.name + "," + f.category + "," + f.description + "," + f.price);
                    bw.newLine();
                }
                System.out.println("✅ Ghi danh sách món ăn thành công!");
            } catch (IOException e) {
                System.out.println("❌ Lỗi ghi món ăn: " + e.getMessage());
            }
        }

        @Override
        public ArrayList<FoodItem> readFromFile(String filename) {
            ArrayList<FoodItem> foods = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length == 5) {
                        foods.add(new FoodItem(
                                data[0], data[1], data[2], data[3], Integer.parseInt(data[4])
                        ));
                    }
                }
                System.out.println("✅ Đọc danh sách món ăn thành công!");
            } catch (IOException e) {
                System.out.println("❌ Lỗi đọc món ăn: " + e.getMessage());
            }
            return foods;
        }
    }
    class OrderFileManager implements IFileHandler<Order> {

        private ArrayList<FoodItem> foodList; // cần truyền vào để ánh xạ món ăn trong file

        public OrderFileManager(ArrayList<FoodItem> foodList) {
            this.foodList = foodList;
        }

        @Override
        public void writeToFile(ArrayList<Order> orders, String filename) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
                for (Order o : orders) {
                    StringBuilder items = new StringBuilder();
                    for (OrderItem oi : o.items) {
                        items.append(oi.food.idfood).append(":").append(oi.quantity).append(";");
                    }
                    if (items.length() > 0) items.deleteCharAt(items.length() - 1);
                    bw.write(o.orderId + "," + o.customerName + "," + o.total + "," + items);
                    bw.newLine();
                }
                System.out.println("✅ Ghi đơn hàng thành công!");
            } catch (IOException e) {
                System.out.println("❌ Lỗi ghi đơn hàng: " + e.getMessage());
            }
        }

        @Override
        public ArrayList<Order> readFromFile(String filename) {
            ArrayList<Order> orders = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (data.length >= 4) {
                        int orderId = Integer.parseInt(data[0]);
                        String customerName = data[1];
                        int total = Integer.parseInt(data[2]);
                        String[] foodData = data[3].split(";");

                        List<OrderItem> items = new ArrayList<>();
                        for (String fd : foodData) {
                            String[] parts = fd.split(":");
                            String foodId = parts[0];
                            int quantity = Integer.parseInt(parts[1]);
                            for (FoodItem f : foodList) {
                                if (f.idfood.equalsIgnoreCase(foodId)) {
                                    items.add(new OrderItem(f, quantity));
                                    break;
                                }
                            }
                        }
                        Order o = new Order(customerName, items);
                        o.total = total;
                        o.orderId = orderId;
                        orders.add(o);
                    }
                }
                System.out.println("✅ Đọc đơn hàng thành công!");
            } catch (IOException e) {
                System.out.println("❌ Lỗi đọc đơn hàng: " + e.getMessage());
            }
            return orders;
        }
    }
}

