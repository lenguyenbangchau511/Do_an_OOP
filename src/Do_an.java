import java.util.Scanner;

public class Do_an {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        List<FoodItem> menu = new ArrayList<>();
        List<Order> orders = new ArrayList<>();
        List<User> users = new ArrayList<>();
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
    public void view(List<FoodItem> menu){
        System.out.println("\n===== MENU CỬA HÀNG =====");
        for (FoodItem item : menu) {
            item.display();
        }
    }
    public void placeOrder(List<FoodItem> menu){
        Scanner sc = new Scanner(System.in);
        List<OrderItem> orderList = new ArrayList<>();

        System.out.println("\n===== ĐẶT MÓN ĂN =====");
        String choice;
        do {
        System.out.print("Nhập mã món (VD: F01): ");
        String code = sc.nextLine();

        FoodItem selected = null;
        for (FoodItem f : menu) {
            if (f.idfood.equalsIgnoreCase(code)) {
                selected = f;
                break;
            }
        }

        if (selected == null) {
            System.out.println("Không tìm thấy món có mã " + code);
        } else {
            System.out.print("Nhập số lượng: ");
            int qty = Integer.parseInt(sc.nextLine());
            orderList.add(new OrderItem(selected, qty));
            System.out.println("Đã thêm " + selected.name + " x" + qty + " vào giỏ hàng.");
        }

        System.out.print("Bạn muốn chọn thêm món khác không? (y/n): ");
        choice = sc.nextLine();

    } while (choice.equalsIgnoreCase("y"));

    int total = 0;
    System.out.println("\n===== HÓA ĐƠN CỦA BẠN =====");
    for (OrderItem o : orderList) {
        o.display();
        total += o.food.price * o.quantity;
    }
    System.out.println("-----------------------------");
    System.out.println("TỔNG CỘNG: " + total + "đ");
    System.out.println("Cảm ơn " + name + " đã đặt món.");
    }
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
        System.out.println("Cap nhat thong tin khach hang" + name);Scanner sc = new Scanner(System.in);
        System.out.println("\n===== CẬP NHẬT THÔNG TIN ADMIN =====");
        System.out.print("Tên hiện tại (" + name + "): ");
        String newName = sc.nextLine();
        if (!newName.isEmpty()) name = newName;

        System.out.print("Số điện thoại hiện tại (" + phonenumber + "): ");
        String newPhone = sc.nextLine();
        if (!newPhone.isEmpty()) phonenumber = newPhone;

        System.out.println("Cập nhật thông tin admin thành công!");
    }

    public void manageMenu(List<FoodItem> menu){
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n===== QUẢN LÝ MENU =====");
            System.out.println("1. Xem danh sách món");
            System.out.println("2. Thêm món mới");
            System.out.println("3. Cập nhật món");
            System.out.println("4. Xóa món");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    System.out.println("\n===== DANH SÁCH MÓN ĂN =====");
                    for (FoodItem f : menu) f.display();
                    break;

                case 2:
                    System.out.print("Nhập ID món: ");
                    String id = sc.nextLine();
                    System.out.print("Tên món: ");
                    String name = sc.nextLine();
                    System.out.print("Loại: ");
                    String category = sc.nextLine();
                    System.out.print("Mô tả: ");
                    String desc = sc.nextLine();
                    System.out.print("Giá: ");
                    int price = Integer.parseInt(sc.nextLine());
                    menu.add(new FoodItem(id, name, category, desc, price));
                    System.out.println("Đã thêm món mới!");
                    break;

                case 3:
                    System.out.print("Nhập ID món muốn cập nhật: ");
                    String findId = sc.nextLine();
                    FoodItem found = null;
                    for (FoodItem f : menu) {
                        if (f.idfood.equalsIgnoreCase(findId)) {
                            found = f;
                            break;
                        }
                    }
                    if (found != null) found.updateInfo();
                    else System.out.println("Không tìm thấy món!");
                    break;

                case 4:
                    System.out.print("Nhập ID món muốn xóa: ");
                    String delId = sc.nextLine();
                    boolean removed = menu.removeIf(f -> f.idfood.equalsIgnoreCase(delId));
                    if (removed) System.out.println("Đã xóa món " + delId);
                    else System.out.println("Không tìm thấy món!");
                    break;

                case 0:
                    System.out.println("Thoát quản lý menu.");
                    break;

                default:
                    System.out.println("Lựa chọn không hợp lệ!");
            }
        } while (choice != 0);
    }
    public void manageOrder(List<Order> orders){
        System.out.println("\n===== QUẢN LÝ ĐƠN HÀNG =====");
        if (orders.isEmpty()) {
            System.out.println("Chưa có đơn hàng nào.");
            return;
        }
        for (Order o : orders) {
            o.display();
        }
    }
    public void manageUser(List<User> users){
        Scanner sc = new Scanner(System.in);
        System.out.println("\n===== QUẢN LÝ NGƯỜI DÙNG =====");
        for (User u : users) {
            System.out.printf("%s | %-10s | %-8s | %-8s\n",
                    u.id, u.username, u.role, u.status ? "Active" : "Banned");
        }

        System.out.print("\nNhập ID user muốn khóa / mở (hoặc Enter để bỏ qua): ");
        String id = sc.nextLine().trim();
        if (id.isEmpty()) return;

        for (User u : users) {
            if (u.id.equalsIgnoreCase(id)) {
                u.status = !u.status;
                System.out.println("→ User " + u.username + " giờ là " + (u.status ? "Active" : "Banned"));
                return;
            }
        }

        System.out.println("Không tìm thấy user có ID " + id);
    }
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

    public void updateInfo(){Scanner sc = new Scanner(System.in);
        System.out.println("\n===== CẬP NHẬT THÔNG TIN MÓN ĂN =====");

        System.out.print("Tên hiện tại (" + name + "): ");
        String newName = sc.nextLine();
        if (!newName.isEmpty()) name = newName;

        System.out.print("Loại hiện tại (" + category + "): ");
        String newCategory = sc.nextLine();
        if (!newCategory.isEmpty()) category = newCategory;

        System.out.print("Giá hiện tại (" + price + "): ");
        String newPrice = sc.nextLine();
        if (!newPrice.isEmpty()) {
            try {
                price = Integer.parseInt(newPrice);
            } catch (NumberFormatException e) {
                System.out.println("Giá không hợp lệ, giữ nguyên giá cũ.");
            }
        }

        System.out.print("Mô tả hiện tại (" + description + "): ");
        String newDesc = sc.nextLine();
        if (!newDesc.isEmpty()) description = newDesc;

        System.out.println("Cập nhật thông tin món ăn thành công!");
    }

    public void display(){
        System.out.printf("%s | %-20s | %-10s | %dđ | %s\n", idfood, name, category, price, description);
    }
}

interface ICRUD {
    void add();
    void update();
    void delete();
    void find();
}

class Menu implements ICRUD {
    String listFoodItems;
    public void showMenu(){
        if (listFoodItems.isEmpty()) {
            System.out.println("\nMenu hiện đang trống.");
            return;
        }

        System.out.println("\n===== DANH SÁCH MÓN ĂN =====");
        for (FoodItem f : listFoodItems) {
            f.display();
        }
    }
    @Override public void add(){
        System.out.println("\n===== THÊM MÓN MỚI =====");
        System.out.print("Nhập ID món: ");
        String id = sc.nextLine();

        for (FoodItem f : listFoodItems) {
            if (f.idfood.equalsIgnoreCase(id)) {
                System.out.println("ID món này đã tồn tại!");
                return;
            }
        }

        System.out.print("Tên món: ");
        String name = sc.nextLine();
        System.out.print("Loại: ");
        String category = sc.nextLine();
        System.out.print("Mô tả: ");
        String desc = sc.nextLine();
        System.out.print("Giá: ");
        int price = Integer.parseInt(sc.nextLine());

        listFoodItems.add(new FoodItem(id, name, category, desc, price));
        System.out.println("Đã thêm món mới thành công!");
    }
    @Override public void update(){
        System.out.print("\nNhập ID món muốn cập nhật: ");
        String id = sc.nextLine();

        for (FoodItem f : listFoodItems) {
            if (f.idfood.equalsIgnoreCase(id)) {
                f.updateInfo();
                return;
            }
        }
        System.out.println("Không tìm thấy món có ID " + id);
    }
    @Override public void delete(){
        System.out.print("\nNhập ID món muốn xóa: ");
        String id = sc.nextLine();

        boolean removed = listFoodItems.removeIf(f -> f.idfood.equalsIgnoreCase(id));
        if (removed)
            System.out.println("Đã xóa món có ID " + id);
        else
            System.out.println("Không tìm thấy món cần xóa!");
    }
    @Override public void find(){
        System.out.print("\nNhập tên hoặc loại món cần tìm: ");
        String keyword = sc.nextLine().toLowerCase();

        boolean found = false;
        for (FoodItem f : listFoodItems) {
            if (f.name.toLowerCase().contains(keyword) ||
                f.category.toLowerCase().contains(keyword)) {
                if (!found) {
                    System.out.println("\n===== KẾT QUẢ TÌM KIẾM =====");
                    found = true;
                }
                f.display();
            }
        }

        if (!found) System.out.println("Không tìm thấy món phù hợp!");
    }
}

class Cart {
    List<OrderItem> items = new ArrayList<>();

    public void addItem(FoodItem food, int qty) {
        items.add(new OrderItem(food, qty));
    }

    public void display() {
        System.out.println("\n===== GIỎ HÀNG =====");
        for (OrderItem i : items) i.display();
    }

    public List<OrderItem> getItems() {
        return items;
    }
}

class Order {
    private static int orderCount = 0;
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
        System.out.println("Khách hàng: " + customerName);
        for (OrderItem o : items) o.display();
        System.out.println("TỔNG CỘNG: " + total + "đ");
    }
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
