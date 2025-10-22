import java.util.*;
import java.io.*;
import java.time.LocalDate;

public class Do_an {
    public static void main(String[] args) {
        // Tạo đối tượng
        Admin admin = new Admin();
        AdminMenu adminMenu = new AdminMenu();
        Customer customer = new Customer();
        CustomerMenu customerMenu = new CustomerMenu();
        FoodList foodlist = new FoodList();
        FileManager fileManager = new FileManager();

        // Đọc dữ liệu từ file
        fileManager.readAllData(adminMenu);

        // Thao tác chính
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("\n===== CHÀO MỪNG ĐẾN VỚI HỆ THỐNG =====");
            System.out.println("1. Đăng nhập quản lý");
            System.out.println("2. Đăng ký khách hàng");
            System.out.println("3. Đăng nhập khách hàng");
            System.out.println("4. Xem menu");
            System.out.println("0. Thoát");
            System.out.print("\nNhập lựa chọn: ");
            choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice){
                case 1:
                    if (admin.login()) {
                        adminMenu.choice();
                    }
                    break;
                case 2:
                    customer.register();
                    adminMenu.dsKhachHang.addCustomer(customer); // SỬA: Thêm khách hàng vào danh sách
                    fileManager.writeAllData(adminMenu); // Lưu toàn bộ dữ liệu
                    break;
                case 3:
                    if (customer.login()) {
                        customerMenu.setCurrentCustomer(customer);
                        customerMenu.choice();
                    }
                    break;
                case 4:
                    foodlist.showAll();
                    break;
                case 0:
                    // Ghi dữ liệu vào file trước khi thoát
                    fileManager.writeAllData(adminMenu);
                    System.out.println("Đã lưu dữ liệu. Tạm biệt!");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
                    break;
            }
        } while (choice != 0);
        sc.close();
    }
}

// ===== LỚP QUẢN LÝ FILE ============================
class FileManager {

    // Đọc toàn bộ dữ liệu từ file
    public void readAllData(AdminMenu adminMenu) {
        adminMenu.dsKhachHang.readFromFile("customers.txt");
        adminMenu.dsAdmin.readFromFile("admins.txt");
        adminMenu.dsMonAn.readFromFile("foods.txt");
        adminMenu.dsHoaDon.readFromFile("orders.txt");
        adminMenu.dsThanhToan.readFromFile("payments.txt");
    }

    // Ghi toàn bộ dữ liệu vào file
    public void writeAllData(AdminMenu adminMenu) {
        adminMenu.dsKhachHang.writeToFile("customers.txt");
        adminMenu.dsAdmin.writeToFile("admins.txt");
        adminMenu.dsMonAn.writeToFile("foods.txt");
        adminMenu.dsHoaDon.writeToFile("orders.txt");
        adminMenu.dsThanhToan.writeToFile("payments.txt");
    }

    // Phương thức đọc file chung
    public static List<String> readFile(String filename) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Lỗi đọc file " + filename + ": " + e.getMessage());
        }
        return lines;
    }

    // Phương thức ghi file chung
    public static void writeFile(String filename, List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Lỗi ghi file " + filename + ": " + e.getMessage());
        }
    }

    // Kiểm tra file tồn tại
    public static boolean fileExists(String filename) {
        File file = new File(filename);
        return file.exists();
    }

    // Tạo file mới nếu chưa tồn tại
    public static void createFileIfNotExists(String filename) {
        if (!fileExists(filename)) {
            try {
                File file = new File(filename);
                file.createNewFile();
                System.out.println("Đã tạo file mới: " + filename);
            } catch (IOException e) {
                System.out.println("Lỗi tạo file " + filename + ": " + e.getMessage());
            }
        }
    }
}

// ===== GIAO DIỆN ĐIỀU KHIỂN =======================
class AdminMenu {
    CustomerList dsKhachHang = new CustomerList();
    AdminList dsAdmin = new AdminList();
    FoodList dsMonAn = new FoodList();
    OrderList dsHoaDon = new OrderList();
    PaymentList dsThanhToan = new PaymentList(); // THÊM: Quản lý thanh toán
    Scanner sc = new Scanner(System.in);

    int choice;
    public void choice(){
        do {
            System.out.println("\n===== MENU QUẢN LÝ =====");
            System.out.println("1. Quản lý khách hàng");
            System.out.println("2. Quản lý quản trị viên");
            System.out.println("3. Quản lý món ăn");
            System.out.println("4. Quản lý hóa đơn");
            System.out.println("5. Quản lý lịch sử thanh toán");
            System.out.println("0. Thoát.");
            System.out.print("\nChọn: ");
            choice = Integer.parseInt(sc.nextLine());
            switch (choice){
                case 1:
                    int choice1;
                    do {
                        System.out.println("\n===== QUẢN LÝ KHÁCH HÀNG =====");
                        System.out.println("1. Xem danh sách khách hàng");
                        System.out.println("2. Thêm khách hàng mới");
                        System.out.println("3. Cập nhật thông tin khách hàng");
                        System.out.println("4. Xóa khách hàng");
                        System.out.println("5. Tìm kiếm khách hàng");
                        System.out.println("0. Thoát");
                        System.out.print("Chọn: ");
                        choice1 = Integer.parseInt(sc.nextLine());

                        switch (choice1){
                            case 1: dsKhachHang.showAll(); break;
                            case 2: dsKhachHang.add(); break;
                            case 3: dsKhachHang.update(); break;
                            case 4: dsKhachHang.delete(); break;
                            case 5: dsKhachHang.search(); break;
                            case 0: System.out.println("Thoát quản lý khách hàng."); break;
                            default: System.out.println("Lựa chọn không hợp lệ."); break;
                        }
                    } while (choice1 != 0);
                    break;
                case 2:
                    int choice2;
                    do {
                        System.out.println("\n===== QUẢN LÝ QUẢN TRỊ VIÊN =====");
                        System.out.println("1. Xem danh sách quản trị viên");
                        System.out.println("2. Thêm quản trị viên mới");
                        System.out.println("3. Cập nhật thông tin quản trị viên");
                        System.out.println("4. Xóa quản trị viên");
                        System.out.println("5. Tìm kiếm quản trị viên");
                        System.out.println("0. Thoát");
                        System.out.print("Chọn: ");
                        choice2 = Integer.parseInt(sc.nextLine());

                        switch (choice2){
                            case 1: dsAdmin.showAll(); break;
                            case 2: dsAdmin.add(); break;
                            case 3: dsAdmin.update(); break;
                            case 4: dsAdmin.delete(); break;
                            case 5: dsAdmin.search(); break;
                            case 0: System.out.println("Thoát quản lý quản trị viên."); break;
                            default: System.out.println("Lựa chọn không hợp lệ."); break;
                        }
                    } while (choice2 != 0);
                    break;
                case 3:
                    int choice3;
                    do {
                        System.out.println("\n===== QUẢN LÝ MENU =====");
                        System.out.println("1. Xem menu");
                        System.out.println("2. Thêm món mới");
                        System.out.println("3. Cập nhật món");
                        System.out.println("4. Xóa món");
                        System.out.println("5. Tìm kiếm món ăn");
                        System.out.println("0. Thoát");
                        System.out.print("Chọn: ");
                        choice3 = Integer.parseInt(sc.nextLine());

                        switch (choice3){
                            case 1: dsMonAn.showAll(); break;
                            case 2: dsMonAn.add(); break;
                            case 3: dsMonAn.update(); break;
                            case 4: dsMonAn.delete(); break;
                            case 5: dsMonAn.search(); break;
                            case 0: System.out.println("Thoát quản lý menu."); break;
                            default: System.out.println("Lựa chọn không hợp lệ."); break;
                        }
                    } while (choice3 != 0);
                    break;
                case 4:
                    int choice4;
                    do {
                        System.out.println("\n===== QUẢN LÝ HÓA ĐƠN =====");
                        System.out.println("1. Xem tất cả hóa đơn");
                        System.out.println("2. Thêm hóa đơn");
                        System.out.println("3. Cập nhật thông tin hóa đơn");
                        System.out.println("4. Xóa hóa đơn");
                        System.out.println("5. Tìm kiếm hóa đơn");
                        System.out.println("0. Thoát");
                        System.out.print("Chọn: ");
                        choice4 = Integer.parseInt(sc.nextLine());

                        switch (choice4){
                            case 1: dsHoaDon.showAll(); break;
                            case 2: dsHoaDon.add(); break;
                            case 3: dsHoaDon.update(); break;
                            case 4: dsHoaDon.delete(); break;
                            case 5: dsHoaDon.search(); break;
                            case 0: System.out.println("Thoát quản lý hóa đơn."); break;
                            default: System.out.println("Lựa chọn không hợp lệ."); break;
                        }
                    } while (choice4 != 0);
                    break;
                case 5:
                    int choice5;
                    do {
                        System.out.println("\n===== QUẢN LÝ LỊCH SỬ THANH TOÁN =====");
                        System.out.println("1. Xem tất cả giao dịch");
                        System.out.println("2. Tìm kiếm giao dịch");
                        System.out.println("3. Tìm kiếm theo khoảng thời gian");
                        System.out.println("4. Thống kê doanh thu");
                        System.out.println("5. Cập nhật trạng thái thanh toán");
                        System.out.println("0. Thoát");
                        System.out.print("Chọn: ");
                        choice5 = Integer.parseInt(sc.nextLine());

                        switch (choice5){
                            case 1: dsThanhToan.showAll(); break;
                            case 2: dsThanhToan.search(); break;
                            case 3: dsThanhToan.searchByDateRange(); break;
                            case 4: dsThanhToan.showStatistics(); break;
                            case 5: dsThanhToan.updateStatus(); break;
                            case 0: System.out.println("Thoát quản lý thanh toán."); break;
                            default: System.out.println("Lựa chọn không hợp lệ."); break;
                        }
                    } while (choice5 != 0);
                    break;
                case 0:
                    System.out.println("Thoát menu quản lý.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
                    break;
            }
        } while (choice != 0);
    }
}

class CustomerMenu {
    private Customer currentCustomer; // SỬA: Thêm thuộc tính để lưu khách hàng hiện tại
    FoodList food = new FoodList();
    Cart cart = new Cart();
    OrderList orderList = new OrderList(); // SỬA: Dùng OrderList thay vì Order
    private Scanner sc = new Scanner(System.in);
    private int choice;

    // SỬA: Thêm setter cho currentCustomer
    public void setCurrentCustomer(Customer customer) {
        this.currentCustomer = customer;
    }

    public void choice(){
        do {
            System.out.println("\n===== MENU KHÁCH HÀNG =====");
            System.out.println("1. Xem menu");
            System.out.println("2. Đặt món");
            System.out.println("3. Xem giỏ hàng");
            System.out.println("4. Xem đơn hàng của tôi");
            System.out.println("5. Thanh toán");
            System.out.println("6. Đăng xuất");
            System.out.println("0. Thoát.");
            System.out.print("\nChọn: ");
            choice = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (choice){
                case 1:
                    food.showAll();
                    break;
                case 2:
                    cart.datMon(); // SỬA: Gọi phương thức đặt món từ Cart
                    break;
                case 3:
                    cart.HienThi();
                    break;
                case 4:
                    // SỬA: Hiển thị đơn hàng của khách hàng hiện tại
                    orderList.showCustomerOrders(currentCustomer.getCustomerID());
                    break;
                case 5:
                    // SỬA: Chức năng thanh toán
                    if (cart.isEmpty()) {
                        System.out.println("Giỏ hàng trống!");
                    } else {
                        Order newOrder = cart.Xacnhandon(currentCustomer.getCustomerID());
                        if (newOrder != null) {
                            orderList.addOrder(newOrder);
                            System.out.println("Đặt hàng thành công!");
                            cart.clearCart(); // Xóa giỏ hàng sau khi đặt
                        }
                    }
                    break;
                case 6:
                    currentCustomer.logout();
                    return; // Quay về menu chính
                case 0:
                    System.out.println("Thoát chương trình.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
                    break;
            }
        } while (choice != 0);
    }
}

// ===== INTERFACE VÀ LỚP TRỪU TƯỢNG =================
interface ICRUD {
    void showAll();
    void add();
    void update();
    void delete();
    void search();
}

abstract class User{
    private String username;
    private String password;
    private String role;
    private String name;
    private String phonenumber;

    public User() {
        this("", "", "", "", "");
    }

    public User(String username, String password, String role, String name, String phonenumber) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.phonenumber = phonenumber;
    }

    public final boolean login(){
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập username: ");
        String inputUsername = sc.nextLine();
        System.out.print("Nhập password: ");
        String inputPassword = sc.nextLine();
        if (check(inputUsername, inputPassword)) {
            System.out.println("Đăng nhập thành công với quyền: " + role);
            afterLogin();
            return true;
        } else {
            System.out.println("Sai thông tin đăng nhập.");
            return false;
        }
    }
    protected abstract boolean check(String username, String password);
    protected abstract void afterLogin();
    public abstract void logout();

    public String getUsername(){return username;}
    public String getPassword(){return password;}
    public String getRole(){return role;}
    public String getName(){return name;}
    public String getPhonenumber(){return phonenumber;}

    public void setUsername(String username){this.username = username;}
    public void setPassword(String password){this.password = password;}
    public void setRole(String role){this.role = role;}
    public void setName(String name){this.name = name;}
    public void setPhonenumber(String phonenumber){this.phonenumber = phonenumber;}
}

// ===== NHÓM NGƯỜI DÙNG =============================
class Customer extends User {
    private static int customerCount = 0;
    private String customerID;
    Scanner sc = new Scanner(System.in);

    public Customer() {
        super();
        this.customerID = "C0" + (++customerCount);
    }
    public Customer(String username, String password, String role, String name, String phonenumber, String customerID) {
        super(username, password, "Customer", name, phonenumber);
        this.customerID = customerID == null ? Customer.TuDongTaoIDKhachHang() : customerID;
    }

    // GETTER, SETTER
    public String getCustomerID() {return customerID;}
    public void setCustomerID(String customerID) {this.customerID = customerID;}

    // PHUONG THUC
    public static String TuDongTaoIDKhachHang() {
        return "C0" + (++customerCount);
    }

    @Override
    protected boolean check(String username, String password) {
        // SỬA: Cần kiểm tra trong danh sách khách hàng
        return getUsername().equals(username) && getPassword().equals(password);
    }

    @Override
    protected void afterLogin() {
        System.out.println("Chào mừng khách hàng " + getName());
    }

    @Override
    public void logout(){
        System.out.println("Khách hàng " + this.getUsername() + " đã đăng xuất.");
    }

    public void register(){
        System.out.println("===== Đăng ký tài khoản khách hàng =====");
        System.out.print("Nhập tên đăng nhập: ");
        this.setUsername(sc.nextLine());
        System.out.print("Nhập mật khẩu: ");
        this.setPassword(sc.nextLine());
        System.out.print("Nhập tên: ");
        this.setName(sc.nextLine());
        System.out.print("Nhập số điện thoại: ");
        this.setPhonenumber(sc.nextLine());

        System.out.println("Khách hàng " + this.getName() + " đã đăng ký thành công với ID = " + this.getCustomerID());
    }
}

class Admin extends User {
    private String adminID;
    private String address;
    private String email;

    public Admin() {
        super();
        this.adminID = "";
        this.address = "";
        this.email = "";
    }

    public Admin(String username, String password, String role, String name, String phonenumber, String adminID, String address, String email) {
        super(username, password, "Admin", name, phonenumber);
        this.adminID = adminID;
        this.address = address;
        this.email = email;
    }

    public String getAdminID() {return adminID;}
    public String getAddress() {return address;}
    public String getEmail() {return email;}
    public void setAdminID(String adminID) {this.adminID = adminID;}
    public void setAddress(String address) {this.address = address;}
    public void setEmail(String email) {this.email = email;}

    @Override
    protected boolean check(String username, String password) {
        // SỬA: Cần kiểm tra trong danh sách admin
        return getUsername().equals(username) && getPassword().equals(password);
    }
    @Override
    protected void afterLogin() {
        System.out.println("Chào mừng quản trị viên " + getName());
    }

    @Override
    public void logout() {
        System.out.println("Admin " + this.getUsername() + " đã đăng xuất.");
    }
}

class CustomerList implements ICRUD{
    private ArrayList<Customer> customers = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    // SỬA: Thêm phương thức để thêm khách hàng từ đăng ký
    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public ArrayList<Customer> getCustomers() {return customers;}

    @Override
    public void showAll() {
        System.out.println("\n===== DANH SÁCH KHÁCH HÀNG =====");
        if (customers.isEmpty()) {
            System.out.println("Chưa có khách hàng nào.");
            return;
        }
        System.out.printf("%-6s | %-15s | %-20s | %-12s\n", "ID", "Username", "Tên", "SĐT");
        System.out.println("--------------------------------------------------------");
        for (Customer c : customers) {
            System.out.printf("%-6s | %-15s | %-20s | %-12s\n",
                    c.getCustomerID(), c.getUsername(), c.getName(), c.getPhonenumber());
        }
    }

    @Override
    public void add() {
        System.out.println("\n===== THÊM KHÁCH HÀNG MỚI =====");
        System.out.print("Nhập ID khách hàng: ");
        String id = sc.nextLine();
        // Kiểm tra trùng ID
        for (Customer c : customers) {
            if (c.getCustomerID().equalsIgnoreCase(id)) {
                System.out.println("ID này đã tồn tại. Không thể thêm mới!");
                return;
            }
        }

        System.out.print("Nhập tên đăng nhập: ");
        String username = sc.nextLine();
        System.out.print("Nhập mật khẩu: ");
        String password = sc.nextLine();
        System.out.print("Nhập họ tên: ");
        String name = sc.nextLine();
        System.out.print("Nhập số điện thoại: ");
        String phone = sc.nextLine();

        Customer newCustomer = new Customer(username, password, "Customer", name, phone, id);
        customers.add(newCustomer);
        System.out.println("Thêm khách hàng thành công!");
    }

    @Override
    public void update() {
        System.out.print("Nhập ID khách hàng cần cập nhật: ");
        String id = sc.nextLine();
        Customer c1 = null;
        for (Customer c : customers) {
            if (c.getCustomerID().equalsIgnoreCase(id)){
                c1 = c;
                break;
            }
        }
        if (c1 == null) {
            System.out.println("Không tìm thấy khách hàng này");
            return;
        }

        int choice;
        do {
            System.out.println("\n==== CẬP NHẬT THÔNG TIN KHÁCH HÀNG ====");
            System.out.println("1. Đổi tên");
            System.out.println("2. Đổi số điện thoại");
            System.out.println("3. Đổi mật khẩu");
            System.out.println("0. Thoát");
            System.out.print("\nChọn thông tin cần chỉnh sửa: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Nhập tên mới: ");
                    c1.setName(sc.nextLine());
                    System.out.println("Cập nhật tên thành công!");
                    break;
                case 2:
                    System.out.print("Nhập số điện thoại mới: ");
                    c1.setPhonenumber(sc.nextLine());
                    System.out.println("Cập nhật số điện thoại thành công!");
                    break;
                case 3:
                    System.out.print("Nhập mật khẩu mới: ");
                    c1.setPassword(sc.nextLine());
                    System.out.println("Cập nhật mật khẩu thành công!");
                    break;
                case 0:
                    System.out.println("Thoát cập nhật.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!"); break;
            }
        } while (choice != 0);
    }

    @Override
    public void delete() {
        System.out.println("\n===== XÓA KHÁCH HÀNG =====");
        System.out.print("Nhập ID khách hàng cần xóa: ");
        String id = sc.nextLine();

        Customer c1 = null;
        for (Customer c : customers) {
            if (c.getCustomerID().equalsIgnoreCase(id)) {
                c1 = c;
                break;
            }
        }
        if (c1 == null) {
            System.out.println("Không tìm thấy khách hàng có ID này!");
            return;
        }

        customers.remove(c1);
        System.out.println("Xóa khách hàng thành công!");
    }

    @Override
    public void search() {
        System.out.println("\n===== TÌM KIẾM KHÁCH HÀNG =====");
        System.out.print("Nhập ID hoặc tên khách hàng cần tìm: ");
        String keyword = sc.nextLine();

        boolean found = false;
        System.out.println("\nKẾT QUẢ TÌM KIẾM:");
        System.out.printf("%-6s | %-15s | %-20s | %-12s\n", "ID", "Username", "Tên", "SĐT");
        System.out.println("--------------------------------------------------------");
        for (Customer c : customers) {
            if (c.getCustomerID().equalsIgnoreCase(keyword) || c.getName().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.printf("%-6s | %-15s | %-20s | %-12s\n",
                        c.getCustomerID(), c.getUsername(), c.getName(), c.getPhonenumber());
                found = true;
            }
        }

        if (!found) {
            System.out.println("Không tìm thấy khách hàng nào phù hợp!");
        }
    }

    // Đọc từ file - SỬA: Sử dụng FileManager
    public void readFromFile(String filename) {
        FileManager.createFileIfNotExists(filename);
        List<String> lines = FileManager.readFile(filename);
        customers.clear();
        for (String line : lines) {
            String[] data = line.split("\\|");
            if (data.length >= 5) {
                Customer customer = new Customer(
                        data[1].trim(), // username
                        data[2].trim(), // password
                        "Customer",
                        data[3].trim(), // name
                        data[4].trim(), // phone
                        data[0].trim()  // customerID
                );
                customers.add(customer);
            }
        }
        System.out.println("Đọc dữ liệu khách hàng thành công!");
    }

    // Ghi vào file - SỬA: Sử dụng FileManager
    public void writeToFile(String filename) {
        List<String> lines = new ArrayList<>();
        for (Customer c : customers) {
            lines.add(String.format("%s|%s|%s|%s|%s",
                    c.getCustomerID(), c.getUsername(), c.getPassword(),
                    c.getName(), c.getPhonenumber()));
        }
        FileManager.writeFile(filename, lines);
        System.out.println("Ghi dữ liệu khách hàng thành công!");
    }
}

class AdminList implements ICRUD{
    private ArrayList<Admin> admins = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    public ArrayList<Admin> getAdmins() {return admins;};

    @Override
    public void showAll() {
        System.out.println("\n===== DANH SÁCH QUẢN TRỊ VIÊN =====");
        if (admins.isEmpty()) {
            System.out.println("Chưa có quản trị viên nào.");
            return;
        }
        System.out.printf("%-6s | %-15s | %-20s | %-12s\n", "ID", "Username", "Tên", "SĐT");
        System.out.println("--------------------------------------------------------");
        for (Admin a : admins) {
            System.out.printf("%-6s | %-15s | %-20s | %-12s\n",
                    a.getAdminID(), a.getUsername(), a.getName(), a.getPhonenumber());
        }
    }

    @Override
    public void add() {
        System.out.println("\n===== THÊM QUẢN TRỊ VIÊN MỚI =====");
        System.out.print("Nhập ID quản trị viên: ");
        String id = sc.nextLine();
        // Kiểm tra trùng ID
        for (Admin a : admins) {
            if (a.getAdminID().equalsIgnoreCase(id)) {
                System.out.println("ID này đã tồn tại. Không thể thêm mới!");
                return;
            }
        }

        System.out.print("Nhập tên đăng nhập: ");
        String username = sc.nextLine();
        System.out.print("Nhập mật khẩu: ");
        String password = sc.nextLine();
        System.out.print("Nhập họ tên: ");
        String name = sc.nextLine();
        System.out.print("Nhập số điện thoại: ");
        String phone = sc.nextLine();
        System.out.print("Nhập địa chỉ: ");
        String address = sc.nextLine();
        System.out.print("Nhập email: ");
        String email = sc.nextLine();

        Admin newAdmin = new Admin(username, password, "Admin", name, phone, id, address, email);
        admins.add(newAdmin);
        System.out.println("Thêm quản trị viên thành công!");
    }

    @Override
    public void update() {
        System.out.print("Nhập ID admin cần cập nhật: ");
        String id = sc.nextLine();
        Admin a1 = null;
        for (Admin a : admins) {
            if (a.getAdminID().equalsIgnoreCase(id)){
                a1 = a;
                break;
            }
        }
        if (a1 == null) {
            System.out.println("Không tìm thấy quản trị viên này");
            return;
        }

        int choice;
        do {
            System.out.println("\n==== CẬP NHẬT THÔNG TIN QUẢN TRỊ VIÊN ====");
            System.out.println("1. Đổi tên");
            System.out.println("2. Đổi số điện thoại");
            System.out.println("3. Đổi địa chỉ");
            System.out.println("4. Đổi email");
            System.out.println("5. Đổi mật khẩu");
            System.out.println("0. Thoát");
            System.out.print("\nChọn thông tin cần chỉnh sửa: ");
            choice = Integer.parseInt(sc.nextLine());

            switch (choice) {
                case 1:
                    System.out.print("Nhập tên mới: ");
                    a1.setName(sc.nextLine());
                    System.out.println("Cập nhật tên thành công!");
                    break;
                case 2:
                    System.out.print("Nhập số điện thoại mới: ");
                    a1.setPhonenumber(sc.nextLine());
                    System.out.println("Cập nhật số điện thoại thành công!");
                    break;
                case 3:
                    System.out.print("Nhập địa chỉ mới: ");
                    a1.setAddress(sc.nextLine());
                    System.out.println("Cập nhật địa chỉ thành công!");
                    break;
                case 4:
                    System.out.print("Nhập email mới: ");
                    a1.setEmail(sc.nextLine());
                    System.out.println("Cập nhật email thành công!");
                    break;
                case 5:
                    System.out.print("Nhập mật khẩu mới: ");
                    a1.setPassword(sc.nextLine());
                    System.out.println("Cập nhật mật khẩu thành công!");
                    break;
                case 0:
                    System.out.println("Thoát cập nhật.");
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ!"); break;
            }
        } while (choice != 0);
    }

    @Override
    public void delete() {
        System.out.println("\n===== XÓA QUẢN TRỊ VIÊN =====");
        System.out.print("Nhập ID quản trị viên cần xóa: ");
        String id = sc.nextLine();

        Admin a1 = null;
        for (Admin a : admins) {
            if (a.getAdminID().equalsIgnoreCase(id)) {
                a1 = a;
                break;
            }
        }
        if (a1 == null) {
            System.out.println("Không tìm thấy quản trị viên có ID này!");
            return;
        }

        admins.remove(a1);
        System.out.println("Xóa quản trị viên thành công!");
    }

    @Override
    public void search() {
        System.out.println("\n===== TÌM KIẾM QUẢN TRỊ VIÊN =====");
        System.out.print("Nhập ID hoặc tên quản trị viên cần tìm: ");
        String keyword = sc.nextLine();

        boolean found = false;
        System.out.println("\nKẾT QUẢ TÌM KIẾM:");
        System.out.printf("%-6s | %-15s | %-20s | %-12s | %-20s | %-15s\n",
                "ID", "Username", "Tên", "SĐT", "Email", "Địa chỉ");
        System.out.println("----------------------------------------------------------------------------------------");
        for (Admin a : admins) {
            if (a.getAdminID().equalsIgnoreCase(keyword) || a.getName().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.printf("%-6s | %-15s | %-20s | %-12s | %-20s | %-15s\n",
                        a.getAdminID(), a.getUsername(), a.getName(),
                        a.getPhonenumber(), a.getEmail(), a.getAddress());
                found = true;
            }
        }

        if (!found) {
            System.out.println("Không tìm thấy quản trị viên nào phù hợp!");
        }
    }

    // Đọc từ file - SỬA: Sử dụng FileManager
    public void readFromFile(String filename) {
        FileManager.createFileIfNotExists(filename);
        List<String> lines = FileManager.readFile(filename);
        admins.clear();
        for (String line : lines) {
            String[] data = line.split("\\|");
            if (data.length >= 5) {
                Admin admin = new Admin(
                        data[1].trim(), // username
                        data[2].trim(), // password
                        "Admin",
                        data[3].trim(), // name
                        data[4].trim(), // phone
                        data[0].trim(), // adminID
                        "", // address
                        ""  // email
                );
                admins.add(admin);
            }
        }
        System.out.println("Đọc dữ liệu admin thành công!");
    }

    // Ghi vào file - SỬA: Sử dụng FileManager
    public void writeToFile(String filename) {
        List<String> lines = new ArrayList<>();
        for (Admin a : admins) {
            lines.add(String.format("%s|%s|%s|%s|%s",
                    a.getAdminID(), a.getUsername(), a.getPassword(),
                    a.getName(), a.getPhonenumber()));
        }
        FileManager.writeFile(filename, lines);
        System.out.println("Ghi dữ liệu admin thành công!");
    }
}

// ===== NHÓM MÓN ĂN =================================
class FoodItem {
    private String idfood, name, loai, mota;
    private int price;

    public FoodItem() {
        this("", "", "", "", 0);
    }

    public FoodItem(String idfood, String name, String loai, String mota, int price){
        this.idfood = idfood;
        this.name = name;
        this.loai = loai;
        this.mota = mota;
        this.price = price;
    }

    public String getIdfood() { return idfood; }
    public String getName() { return name; }
    public String getLoai() { return loai; }
    public String getMota() { return mota; }
    public int getPrice() { return price; }

    public void setIdfood(String idfood) { this.idfood = idfood; }
    public void setName(String name) { this.name = name; }
    public void setLoai(String loai) { this.loai = loai; }
    public void setMota(String mota) { this.mota = mota; }
    public void setPrice(int price) { this.price = price; }

    public void updateInfo() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\n===== CẬP NHẬT THÔNG TIN MÓN ĂN =====");

        System.out.print("Tên hiện tại (" + name + "): ");
        String newName = sc.nextLine();
        if (!newName.isEmpty()) {
            name = newName;
        }

        System.out.print("Loại hiện tại (" + loai + "): ");
        String newloai = sc.nextLine();
        if (!newloai.isEmpty()) {
            loai = newloai;
        }

        System.out.print("Giá hiện tại (" + price + "): ");
        String newPrice = sc.nextLine();
        if (!newPrice.isEmpty()) {
            try {
                price = Integer.parseInt(newPrice);
            } catch (NumberFormatException e) {
                System.out.println("Giá không hợp lệ, giữ nguyên giá cũ.");
            }
        }

        System.out.print("Mô tả hiện tại (" + mota + "): ");
        String newDesc = sc.nextLine();
        if (!newDesc.isEmpty()) {
            mota = newDesc;
        }

        System.out.println("→ Cập nhật thông tin món ăn thành công!\n");
    }

    public void hienthi(){
        System.out.printf("%-5s | %-20s | %-10s | %8dđ | %s\n", idfood, name, loai, price, mota);
    }
}

class FoodList implements ICRUD {
    ArrayList<FoodItem> foods = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    public void showAll(){
        System.out.println("\n=== DANH SÁCH MÓN ĂN ===");
        if (foods.isEmpty()) {
            System.out.println("Chưa có món ăn nào.");
            return;
        }
        System.out.printf("%-5s | %-20s | %-10s | %10s | %s\n", "ID", "Tên món", "Loại", "Giá", "Mô tả");
        System.out.println("-------------------------------------------------------------------");
        for (FoodItem f : foods) {
            f.hienthi();
        }
    }

    @Override
    public void add(){
        System.out.println("\n=== THÊM MÓN MỚI ===");
        System.out.print("Nhập ID: ");
        String id = sc.nextLine();
        if (TimkiembangID(id) != null){
            System.out.println("ID đã tồn tại.");
            return;
        }
        System.out.print("Tên món: ");
        String name = sc.nextLine();
        System.out.print("Loại: ");
        String loai = sc.nextLine();
        System.out.print("Mô tả: ");
        String mota = sc.nextLine();
        System.out.print("Giá: ");
        int gia = Integer.parseInt(sc.nextLine());

        foods.add(new FoodItem(id, name, loai, mota, gia));
        System.out.println("Đã thêm món " + name);
    }

    @Override
    public void update(){
        System.out.print("\nNhập ID món cần cập nhật: ");
        String id = sc.nextLine();
        FoodItem f = TimkiembangID(id);
        if (f == null){
            System.out.println("Không tìm thấy món.");
            return;
        }
        f.updateInfo();
    }

    @Override
    public void delete(){
        System.out.print("\nNhập ID món cần xóa: ");
        String id = sc.nextLine();
        boolean removed = foods.removeIf(f -> f.getIdfood().equalsIgnoreCase(id));
        if (removed)  System.out.println("Đã xóa món " + id);
        else System.out.println("Không tìm thấy món cần xóa.");
    }

    @Override
    public void search(){
        System.out.print("\nNhập tên món ăn cần tìm: ");
        String key = sc.nextLine().toLowerCase();
        boolean found = false;
        System.out.printf("%-5s | %-20s | %-10s | %10s | %s\n", "ID", "Tên món", "Loại", "Giá", "Mô tả");
        System.out.println("-------------------------------------------------------------------");
        for (FoodItem f : foods){
            if (f.getName().toLowerCase().contains(key)){
                f.hienthi();
                found = true;
            }
        }
        if (!found) System.out.println("Không tìm thấy món nào chứa '" + key + "'");
    }

    public FoodItem TimkiembangID(String id){
        for (FoodItem f : foods){
            if (f.getIdfood().equalsIgnoreCase(id))
                return f;
        }
        return null;
    }

    // Đọc từ file - SỬA: Sử dụng FileManager
    public void readFromFile(String filename) {
        FileManager.createFileIfNotExists(filename);
        List<String> lines = FileManager.readFile(filename);
        foods.clear();
        for (String line : lines) {
            String[] data = line.split("\\|");
            if (data.length >= 5) {
                FoodItem food = new FoodItem(
                        data[0].trim(), // idfood
                        data[1].trim(), // name
                        data[3].trim(), // loai
                        data[4].trim(), // mota
                        Integer.parseInt(data[2].trim()) // price
                );
                foods.add(food);
            }
        }
        System.out.println("Đọc dữ liệu món ăn thành công!");
    }

    // Ghi vào file - SỬA: Sử dụng FileManager
    public void writeToFile(String filename) {
        List<String> lines = new ArrayList<>();
        for (FoodItem f : foods) {
            lines.add(String.format("%s|%s|%d|%s|%s",
                    f.getIdfood(), f.getName(), f.getPrice(), f.getLoai(), f.getMota()));
        }
        FileManager.writeFile(filename, lines);
        System.out.println("Ghi dữ liệu món ăn thành công!");
    }
}

// ===== NHÓM ĐẶT HÀNG =====
class OrderItem {
    private FoodItem food;
    private int soluong;

    public OrderItem(){
    }

    public OrderItem(FoodItem food, int soluong){
        this.food = food;
        this.soluong = soluong;
    }

    public FoodItem getFood() {return food;}
    public int getSoluong() {return soluong;}
    public int getGia() {return food.getPrice();}
    public void setSoluong(int soluong) {this.soluong = soluong;}

    public void hienthi(){
        System.out.printf("%-20s | SL: %d | Giá: %dđ | Thành tiền: %dđ\n",
                food.getName(), soluong, food.getPrice(), soluong * food.getPrice());
    }
}

class Cart {
    List<OrderItem> monan = new ArrayList<>();
    Scanner sc = new Scanner(System.in);
    private FoodList foodList = new FoodList(); // SỬA: Thêm FoodList

    // SỬA: Thêm phương thức kiểm tra giỏ hàng rỗng
    public boolean isEmpty() {
        return monan.isEmpty();
    }

    // SỬA: Thêm phương thức xóa giỏ hàng
    public void clearCart() {
        monan.clear();
    }

    public void datMon() {
        foodList.showAll();
        System.out.print("Nhập ID món muốn đặt: ");
        String foodId = sc.nextLine();
        System.out.print("Nhập số lượng: ");
        int quantity = Integer.parseInt(sc.nextLine());

        FoodItem food = foodList.TimkiembangID(foodId);
        if (food != null) {
            ThemMonan(food, quantity);
            System.out.println("Đã thêm vào giỏ hàng!");
        } else {
            System.out.println("Không tìm thấy món!");
        }
    }

    public void HienThi(){
        System.out.println("\n=== GIỎ HÀNG ===");
        if (monan.isEmpty()){
            System.out.println("Giỏ hàng trống");
            return;
        }
        for (OrderItem i : monan){
            i.hienthi();
        }
        System.out.println("Tổng tiền: " + Tinhtongtien() + "đ");
    }

    public void ThemMonan(FoodItem food, int soluong) {
        for (OrderItem i : monan){
            if (i.getFood().getIdfood().equals(food.getIdfood())){
                i.setSoluong(i.getSoluong() + soluong);
                System.out.println("Đã tăng số lượng món " + food.getName());
                return;
            }
        }
        monan.add(new OrderItem(food, soluong));
        System.out.println("Đã thêm " + food.getName() + " vào giỏ hàng");
    }

    public void SuaSoluong(String id){
        for (OrderItem i : monan){
            if (i.getFood().getIdfood().equalsIgnoreCase(id)){
                System.out.print("Nhập số lượng mới: ");
                int newSoluong = Integer.parseInt(sc.nextLine());
                i.setSoluong(newSoluong);
                System.out.println("Đã cập nhật số lượng món");
                return;
            }
        }
        System.out.println("Không tìm thấy món.");
    }

    public void XoaMonan(String id){
        boolean xoa = monan.removeIf(i -> i.getFood().getIdfood().equalsIgnoreCase(id));
        if (xoa) System.out.println("Đã xóa món khỏi giỏ hàng");
        else System.out.println("Không tìm thấy món trong giỏ hàng");
    }

    public int Tinhtongtien(){
        int tong = 0;
        for (OrderItem i : monan)
            tong += i.getGia() * i.getSoluong();
        return tong;
    }

    public Order Xacnhandon(String makhach){
        if (monan.isEmpty()) return null;
        Order order = new Order();
        order.setMadon("ORD" + System.currentTimeMillis()); // Tạo mã đơn tự động
        order.setMakhach(makhach);
        order.setDsMon(new ArrayList<>(monan)); // SỬA: Chuyển danh sách món
        order.setTongtien(Tinhtongtien());
        order.setNgaydat(LocalDate.now().toString());
        order.setTrangthai("Đang xử lý");
        return order;
    }
}

class Order {
    private String madon, makhach;
    private List<OrderItem> dsMon; // SỬA: Dùng List<OrderItem> thay vì ArrayList<FoodItem>
    private double tongtien;
    private String ngaydat;
    private String trangthai;

    public Order(){
        this("", "", new ArrayList<>(), 0, "", "Đang xử lý");
    }

    public Order(String madon, String makhach, List<OrderItem> dsMon, double tongtien, String ngaydat, String trangthai) {
        this.madon = madon;
        this.makhach = makhach;
        this.dsMon = dsMon;
        this.tongtien = tongtien;
        this.ngaydat = ngaydat;
        this.trangthai = trangthai;
    }

    public String getMadon() {return madon;}
    public String getMakhach() {return makhach;}
    public List<OrderItem> getDsMon() {return dsMon;}
    public double getTongtien() {return tongtien;}
    public String getNgaydat() {return ngaydat;}
    public String getTrangthai() {return trangthai;}

    public void setMadon(String madon) {this.madon = madon;}
    public void setMakhach(String makhach) {this.makhach = makhach;}
    public void setDsMon(List<OrderItem> dsMon) {this.dsMon = dsMon;}
    public void setTongtien(double tongtien) {this.tongtien = tongtien;}
    public void setNgaydat(String ngaydat) {this.ngaydat = ngaydat;}
    public void setTrangthai(String trangthai) {this.trangthai = trangthai;}

    public void HienThi(){
        System.out.println("\n=== CHI TIẾT ĐƠN HÀNG ===");
        System.out.println("Mã đơn: " + madon);
        System.out.println("Mã khách: " + makhach);
        System.out.println("Ngày đặt: " + ngaydat);
        System.out.println("Trạng thái: " + trangthai);
        System.out.println("Danh sách món:");
        for (OrderItem item : dsMon) {
            item.hienthi();
        }
        System.out.println("Tổng tiền: " + tongtien + " VND");
        System.out.println("------------------");
    }
}

class OrderList implements ICRUD {
    ArrayList<Order> dsDon = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    @Override
    public void showAll(){
        System.out.println("\n=== DANH SÁCH ĐƠN HÀNG ===");
        if (dsDon.isEmpty()){
            System.out.println("Chưa có đơn hàng nào.");
            return;
        }
        for (Order don : dsDon)
            don.HienThi();
    }

    // SỬA: Thêm phương thức hiển thị đơn hàng của khách hàng
    public void showCustomerOrders(String customerId) {
        System.out.println("\n=== ĐƠN HÀNG CỦA BẠN ===");
        boolean found = false;
        for (Order don : dsDon) {
            if (don.getMakhach().equals(customerId)) {
                don.HienThi();
                found = true;
            }
        }
        if (!found) {
            System.out.println("Bạn chưa có đơn hàng nào.");
        }
    }

    // SỬA: Thêm phương thức thêm đơn hàng
    public void addOrder(Order order) {
        dsDon.add(order);
    }

    @Override
    public void search(){
        System.out.print("Nhập mã đơn cần tìm: ");
        String madon = sc.nextLine();
        for (Order don : dsDon)
            if (don.getMadon().equals(madon)){
                don.HienThi();
                return;
            }
        System.out.println("Không tìm thấy đơn hàng.");
    }

    @Override
    public void add(){
        System.out.println("\n=== THÊM ĐƠN HÀNG MỚI ===");
        System.out.print("Nhập mã đơn: ");
        String madon = sc.nextLine();
        System.out.print("Nhập mã khách hàng: ");
        String makhach = sc.nextLine();

        List<OrderItem> dsMon = new ArrayList<>();
        FoodList foodList = new FoodList();

        while (true) {
            foodList.showAll();
            System.out.print("Nhập ID món ăn (hoặc 0 để dừng): ");
            String foodId = sc.nextLine();
            if (foodId.equals("0")) break;

            FoodItem food = foodList.TimkiembangID(foodId);
            if (food != null) {
                System.out.print("Nhập số lượng: ");
                int quantity = Integer.parseInt(sc.nextLine());
                dsMon.add(new OrderItem(food, quantity));
                System.out.println("Đã thêm món vào đơn hàng.");
            } else {
                System.out.println("Không tìm thấy món!");
            }
        }

        System.out.print("Nhập ngày đặt (yyyy-mm-dd): ");
        String ngaydat = sc.nextLine();

        double tongtien = 0;
        for (OrderItem item : dsMon) {
            tongtien += item.getGia() * item.getSoluong();
        }

        Order don = new Order(madon, makhach, dsMon, tongtien, ngaydat, "Đang xử lý");
        dsDon.add(don);
        System.out.println("Đã thêm đơn hàng thành công.");
    }

    @Override
    public void update(){
        System.out.print("Nhập mã đơn cần cập nhật: ");
        String madon = sc.nextLine();
        for (Order don : dsDon) {
            if (don.getMadon().equals(madon)) {
                System.out.print("Nhập trạng thái mới (Đang xử lý / Hoàn thành / Đã hủy): ");
                String trangthai = sc.nextLine();
                don.setTrangthai(trangthai);
                System.out.println("Cập nhật thành công.");
                return;
            }
        }
        System.out.println("Không tìm thấy đơn hàng.");
    }

    @Override
    public void delete(){
        System.out.print("Nhập mã đơn cần xóa: ");
        String madon = sc.nextLine();
        boolean removed = dsDon.removeIf(don -> don.getMadon().equals(madon));
        if (removed) {
            System.out.println("Đã xóa đơn hàng.");
        } else {
            System.out.println("Không tìm thấy đơn hàng.");
        }
    }

    // Đọc từ file - SỬA: Sử dụng FileManager
    public void readFromFile(String filename) {
        FileManager.createFileIfNotExists(filename);
        List<String> lines = FileManager.readFile(filename);
        dsDon.clear();
        for (String line : lines) {
            // Triển khai logic đọc đơn hàng từ file
            // (Cần thêm triển khai chi tiết dựa trên cấu trúc file)
        }
        System.out.println("Đọc dữ liệu đơn hàng thành công!");
    }

    // Ghi vào file - SỬA: Sử dụng FileManager
    public void writeToFile(String filename) {
        List<String> lines = new ArrayList<>();
        for (Order order : dsDon) {
            // Triển khai logic ghi đơn hàng vào file
            // (Cần thêm triển khai chi tiết dựa trên cấu trúc file)
        }
        FileManager.writeFile(filename, lines);
        System.out.println("Ghi dữ liệu đơn hàng thành công!");
    }
}

// ===== LỚP PAYMENT VÀ PAYMENTLIST =====
class Payment {
    private String paymentId;
    private String orderId;
    private String customerId;
    private double amount;
    private String paymentDate;
    private String paymentMethod;
    private String status;

    public Payment() {
        this("", "", "", 0, "", "", "");
    }

    public Payment(String paymentId, String orderId, String customerId, double amount,
                   String paymentDate, String paymentMethod, String status) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public String getPaymentId() { return paymentId; }
    public void setPaymentId(String paymentId) { this.paymentId = paymentId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentDate() { return paymentDate; }
    public void setPaymentDate(String paymentDate) { this.paymentDate = paymentDate; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public void display() {
        System.out.printf("%-8s | %-8s | %-6s | %10.0f | %-12s | %-15s | %-10s\n",
                paymentId, orderId, customerId, amount, paymentDate, paymentMethod, status);
    }
}

class PaymentList {
    private ArrayList<Payment> payments = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    // === CÁC CHỨC NĂNG CHÍNH ===

    // 1. Hiển thị tất cả giao dịch (có phân trang)
    public void showAll() {
        System.out.println("\n===== DANH SÁCH GIAO DỊCH THANH TOÁN =====");
        if (payments.isEmpty()) {
            System.out.println("Chưa có giao dịch thanh toán nào.");
            return;
        }

        System.out.printf("%-8s | %-8s | %-6s | %10s | %-12s | %-15s | %-10s\n",
                "Mã TT", "Mã ĐH", "Mã KH", "Số tiền", "Ngày TT", "Phương thức", "Trạng thái");
        System.out.println("------------------------------------------------------------------------------");

        for (Payment p : payments) {
            p.display();
        }
    }

    // 2. Tìm kiếm giao dịch
    public void search() {
        System.out.println("\n===== TÌM KIẾM GIAO DỊCH THANH TOÁN =====");
        System.out.println("1. Theo mã thanh toán");
        System.out.println("2. Theo mã đơn hàng");
        System.out.println("3. Theo mã khách hàng");
        System.out.println("4. Theo trạng thái");
        System.out.print("Chọn loại tìm kiếm: ");
        int choice = Integer.parseInt(sc.nextLine());

        System.out.print("Nhập từ khóa: ");
        String keyword = sc.nextLine();

        boolean found = false;
        System.out.printf("%-8s | %-8s | %-6s | %10s | %-12s | %-15s | %-10s\n",
                "Mã TT", "Mã ĐH", "Mã KH", "Số tiền", "Ngày TT", "Phương thức", "Trạng thái");
        System.out.println("------------------------------------------------------------------------------");

        for (Payment p : payments) {
            boolean match = false;
            switch (choice) {
                case 1: match = p.getPaymentId().equalsIgnoreCase(keyword); break;
                case 2: match = p.getOrderId().equalsIgnoreCase(keyword); break;
                case 3: match = p.getCustomerId().equalsIgnoreCase(keyword); break;
                case 4: match = p.getStatus().equalsIgnoreCase(keyword); break;
            }

            if (match) {
                p.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("Không tìm thấy giao dịch thanh toán phù hợp!");
        }
    }

    // 3. Tìm kiếm theo khoảng thời gian
    public void searchByDateRange() {
        System.out.println("\n===== TÌM KIẾM THEO KHOẢNG THỜI GIAN =====");
        System.out.print("Nhập ngày bắt đầu (yyyy-mm-dd): ");
        String startDate = sc.nextLine();
        System.out.print("Nhập ngày kết thúc (yyyy-mm-dd): ");
        String endDate = sc.nextLine();

        boolean found = false;
        System.out.printf("%-8s | %-8s | %-6s | %10s | %-12s | %-15s | %-10s\n",
                "Mã TT", "Mã ĐH", "Mã KH", "Số tiền", "Ngày TT", "Phương thức", "Trạng thái");
        System.out.println("------------------------------------------------------------------------------");

        for (Payment p : payments) {
            if (p.getPaymentDate().compareTo(startDate) >= 0 &&
                    p.getPaymentDate().compareTo(endDate) <= 0) {
                p.display();
                found = true;
            }
        }

        if (!found) {
            System.out.println("Không có giao dịch nào trong khoảng thời gian này!");
        }
    }

    // 4. Thống kê doanh thu
    public void showStatistics() {
        System.out.println("\n===== THỐNG KÊ DOANH THU =====");

        double totalRevenue = 0;
        double successfulRevenue = 0;
        int successfulCount = 0;
        int failedCount = 0;
        int processingCount = 0;

        // Thống kê theo phương thức thanh toán
        Map<String, Double> revenueByMethod = new HashMap<>();
        Map<String, Integer> countByMethod = new HashMap<>();

        for (Payment p : payments) {
            totalRevenue += p.getAmount();

            // Thống kê theo trạng thái
            switch (p.getStatus()) {
                case "Thành công":
                    successfulRevenue += p.getAmount();
                    successfulCount++;
                    break;
                case "Thất bại":
                    failedCount++;
                    break;
                case "Đang xử lý":
                    processingCount++;
                    break;
            }

            // Thống kê theo phương thức
            String method = p.getPaymentMethod();
            revenueByMethod.put(method, revenueByMethod.getOrDefault(method, 0.0) + p.getAmount());
            countByMethod.put(method, countByMethod.getOrDefault(method, 0) + 1);
        }

        // Hiển thị tổng quan
        System.out.println("=== TỔNG QUAN ===");
        System.out.printf("Tổng doanh thu: %,d VND\n", (int)totalRevenue);
        System.out.printf("Doanh thu thành công: %,d VND\n", (int)successfulRevenue);
        System.out.printf("Số giao dịch thành công: %d\n", successfulCount);
        System.out.printf("Số giao dịch thất bại: %d\n", failedCount);
        System.out.printf("Số giao dịch đang xử lý: %d\n", processingCount);
        System.out.printf("Tổng số giao dịch: %d\n", payments.size());

        // Thống kê theo phương thức thanh toán
        System.out.println("\n=== THEO PHƯƠNG THỨC THANH TOÁN ===");
        for (Map.Entry<String, Double> entry : revenueByMethod.entrySet()) {
            String method = entry.getKey();
            double revenue = entry.getValue();
            int count = countByMethod.get(method);
            System.out.printf("%-15s: %,d VND (%d giao dịch)\n",
                    method, (int)revenue, count);
        }
    }

    // 5. Cập nhật trạng thái thanh toán (chỉ cho phép cập nhật trạng thái)
    public void updateStatus() {
        System.out.println("\n===== CẬP NHẬT TRẠNG THÁI THANH TOÁN =====");
        System.out.print("Nhập mã thanh toán cần cập nhật: ");
        String paymentId = sc.nextLine();

        Payment paymentToUpdate = null;
        for (Payment p : payments) {
            if (p.getPaymentId().equalsIgnoreCase(paymentId)) {
                paymentToUpdate = p;
                break;
            }
        }

        if (paymentToUpdate == null) {
            System.out.println("Không tìm thấy giao dịch thanh toán!");
            return;
        }

        System.out.println("Thông tin hiện tại:");
        paymentToUpdate.display();

        System.out.println("\nChọn trạng thái mới:");
        System.out.println("1. Thành công");
        System.out.println("2. Thất bại");
        System.out.println("3. Đang xử lý");
        System.out.println("4. Đã hủy");
        System.out.print("Lựa chọn: ");
        int statusChoice = Integer.parseInt(sc.nextLine());

        String newStatus = paymentToUpdate.getStatus(); // giữ nguyên nếu không hợp lệ
        switch (statusChoice) {
            case 1: newStatus = "Thành công"; break;
            case 2: newStatus = "Thất bại"; break;
            case 3: newStatus = "Đang xử lý"; break;
            case 4: newStatus = "Đã hủy"; break;
            default:
                System.out.println("Lựa chọn không hợp lệ, giữ nguyên trạng thái!");
                return;
        }

        paymentToUpdate.setStatus(newStatus);
        System.out.println("Cập nhật trạng thái thành công!");
    }

    // 6. Thêm giao dịch thanh toán mới (tự động từ đơn hàng)
    public void addPaymentFromOrder(Order order) {
        String paymentId = "PAY" + System.currentTimeMillis();

        Payment newPayment = new Payment(
                paymentId,
                order.getMadon(),
                order.getMakhach(),
                order.getTongtien(),
                LocalDate.now().toString(),
                "Chưa chọn", // Phương thức sẽ được chọn khi thanh toán
                "Đang xử lý"
        );

        payments.add(newPayment);
        System.out.println("Đã tạo giao dịch thanh toán: " + paymentId);
    }

    // 7. Xử lý thanh toán (cho khách hàng)
    public void processPayment(String paymentId) {
        Payment payment = findPaymentById(paymentId);
        if (payment == null) {
            System.out.println("Không tìm thấy giao dịch thanh toán!");
            return;
        }

        System.out.println("\n===== THANH TOÁN =====");
        System.out.println("Mã thanh toán: " + payment.getPaymentId());
        System.out.println("Số tiền: " + payment.getAmount() + " VND");

        System.out.println("Chọn phương thức thanh toán:");
        System.out.println("1. Tiền mặt");
        System.out.println("2. Chuyển khoản");
        System.out.println("3. Thẻ tín dụng");
        System.out.print("Lựa chọn: ");
        int methodChoice = Integer.parseInt(sc.nextLine());

        String paymentMethod = "";
        switch (methodChoice) {
            case 1: paymentMethod = "Tiền mặt"; break;
            case 2: paymentMethod = "Chuyển khoản"; break;
            case 3: paymentMethod = "Thẻ tín dụng"; break;
            default:
                System.out.println("Lựa chọn không hợp lệ!");
                return;
        }

        payment.setPaymentMethod(paymentMethod);
        payment.setStatus("Thành công");
        payment.setPaymentDate(LocalDate.now().toString());

        System.out.println("Thanh toán thành công!");
    }

    // 8. Hiển thị lịch sử thanh toán của khách hàng
    public void showCustomerPaymentHistory(String customerId) {
        System.out.println("\n===== LỊCH SỬ THANH TOÁN =====");
        boolean found = false;

        System.out.printf("%-8s | %-8s | %10s | %-12s | %-15s | %-10s\n",
                "Mã TT", "Mã ĐH", "Số tiền", "Ngày TT", "Phương thức", "Trạng thái");
        System.out.println("------------------------------------------------------------------------");

        for (Payment p : payments) {
            if (p.getCustomerId().equals(customerId)) {
                System.out.printf("%-8s | %-8s | %10.0f | %-12s | %-15s | %-10s\n",
                        p.getPaymentId(), p.getOrderId(), p.getAmount(),
                        p.getPaymentDate(), p.getPaymentMethod(), p.getStatus());
                found = true;
            }
        }

        if (!found) {
            System.out.println("Bạn chưa có giao dịch thanh toán nào.");
        }
    }

    // === CÁC PHƯƠNG THỨC HỖ TRỢ ===

    private Payment findPaymentById(String paymentId) {
        for (Payment p : payments) {
            if (p.getPaymentId().equalsIgnoreCase(paymentId)) {
                return p;
            }
        }
        return null;
    }

    public Payment findPaymentByOrderId(String orderId) {
        for (Payment p : payments) {
            if (p.getOrderId().equalsIgnoreCase(orderId)) {
                return p;
            }
        }
        return null;
    }

    // Đọc từ file
    public void readFromFile(String filename) {
        FileManager.createFileIfNotExists(filename);
        List<String> lines = FileManager.readFile(filename);
        payments.clear();
        for (String line : lines) {
            String[] data = line.split("\\|");
            if (data.length >= 7) {
                Payment payment = new Payment(
                        data[0].trim(), // paymentId
                        data[1].trim(), // orderId
                        data[6].trim(), // customerId
                        Double.parseDouble(data[2].trim()), // amount
                        data[3].trim(), // paymentDate
                        data[4].trim(), // paymentMethod
                        data[5].trim()  // status
                );
                payments.add(payment);
            }
        }
        System.out.println("Đọc dữ liệu thanh toán thành công!");
    }

    // Ghi vào file
    public void writeToFile(String filename) {
        List<String> lines = new ArrayList<>();
        for (Payment p : payments) {
            lines.add(String.format("%s|%s|%.0f|%s|%s|%s|%s",
                    p.getPaymentId(), p.getOrderId(), p.getAmount(), p.getPaymentDate(),
                    p.getPaymentMethod(), p.getStatus(), p.getCustomerId()));
        }
        FileManager.writeFile(filename, lines);
        System.out.println("Ghi dữ liệu thanh toán thành công!");
    }
}