import java.util.*;
import java.io.*;
import java.time.LocalDate;

public class Do_an {
    public static void main(String[] args) {
        // Tao doi tuong quan ly chinh
        QuanLyFile quanLyFile = new QuanLyFile();
        QTVMenu qtvMenu = new QTVMenu(quanLyFile);
        KhachHangMenu khachHangMenu = new KhachHangMenu(qtvMenu.getDsMonAn(), qtvMenu);

        // Doc du lieu tu file
        quanLyFile.docTatCa(qtvMenu);

        // Thao tac chinh
        Scanner sc = new Scanner(System.in);
        int luachon;
        do {
            System.out.println("\n===== CHAO MUNG DEN VOI HE THONG =====");
            System.out.println("1. Dang nhap");
            System.out.println("2. Dang ky khach hang");
            System.out.println("3. Xem menu");
            System.out.println("0. Thoat");
            System.out.print("\nNhap lua chon: ");
            try {
                luachon = sc.nextInt();
                sc.nextLine();
            } catch (Exception e) {
                System.out.println("Vui long nhap so!");
                sc.nextLine();
                luachon = -1;
                continue;
            }

            switch (luachon){
                case 1:
                    System.out.print("Nhap ten dang nhap: ");
                    String tenDangNhap = sc.nextLine();
                    System.out.print("Nhap mat khau: ");
                    String matKhau = sc.nextLine();

                    NguoiDung nguoiDung = NguoiDung.DangNhap.dangNhap(tenDangNhap, matKhau);
                    if (nguoiDung != null) {
                        switch (nguoiDung.getVaiTro()) {
                            case "qtv":
                                System.out.println("Chao mung Quan tri vien " + nguoiDung.getHoTen() + "!");
                                qtvMenu.setQTVHienTai((QTV) nguoiDung);
                                qtvMenu.choice();
                                break;
                            case "khachhang":
                                System.out.println("Chao mung Khach hang " + nguoiDung.getHoTen() + "!");
                                khachHangMenu.setKhachHangHienTai((KhachHang) nguoiDung);
                                khachHangMenu.choice();
                                break;
                            default:
                                System.out.println("Vai tro khong hop le");
                        }
                    } else {
                        System.out.println("Sai thong tin dang nhap");
                    }
                    break;
                case 2:
                    // Tao khach hang moi va goi dangKy()
                    KhachHang khachHangMoi = new KhachHang(qtvMenu.getDsNguoiDung());
                    khachHangMoi.dangKy();
                    qtvMenu.getDsNguoiDung().themNguoiDung(khachHangMoi);

                    // Ghi file
                    quanLyFile.ghiTatCa(qtvMenu);
                    System.out.println("Dang ky thanh cong! Ban co the dang nhap ngay.");
                    break;
                case 3:
                    qtvMenu.getDsMonAn().hienThiTatCa();
                    break;
                case 0:
                    quanLyFile.ghiTatCa(qtvMenu);
                    System.out.println("Da luu du lieu. Ket thuc");
                    break;
                default:
                    System.out.println("Lua chon khong hop le.");
                    break;
            }
        } while (luachon != 0);
        sc.close();
    }
}

// ===== LOP QUAN LY FILE =====
class QuanLyFile {
    public void docTatCa(QTVMenu qtvMenu){
        qtvMenu.getDsNguoiDung().docFile("users.txt");
        qtvMenu.getDsMonAn().docFile("foods.txt");
        qtvMenu.getDsHoaDon().docFile("orders.txt");
        qtvMenu.getDsThanhToan().docFile("payments.txt");
    }

    public void ghiTatCa(QTVMenu qtvMenu){
        qtvMenu.getDsNguoiDung().ghiFile("users.txt");
        qtvMenu.getDsMonAn().ghiFile("foods.txt");
        qtvMenu.getDsHoaDon().ghiFile("orders.txt");
        qtvMenu.getDsThanhToan().ghiFile("payments.txt");
    }

    // Phuong thuc doc, ghi file chung
    public static ArrayList<String> docFile(String filename) {
        ArrayList<String> lines = new ArrayList<>();

        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("File " + filename + " chua ton tai. Tao file moi khi co du lieu.");
            return lines;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Loi doc file " + filename + ": " + e.getMessage());
        }
        return lines;
    }

    public static void ghiFile(String filename, ArrayList<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Loi ghi file " + filename + ": " + e.getMessage());
        }
    }
}

// ===== INTERFACE VA LOP TRUU TUONG =====
interface ChucNang {
    void hienThiTatCa();
    void them();
    void sua();
    void xoa();
    void timKiem();
}

abstract class NguoiDung {
    protected String id;
    protected String tenDangNhap;
    protected String matKhau;
    protected String vaiTro;
    protected String hoTen;
    protected String soDienThoai;

    public NguoiDung(String id, String tenDangNhap, String matKhau, String vaiTro, String hoTen, String soDienThoai) {
        this.id = id;
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
        this.hoTen = hoTen;
        this.soDienThoai = soDienThoai;
    }

    // GETTERS
    public String getId() { return id; }
    public String getTenDangNhap() { return tenDangNhap; }
    public String getMatKhau() { return matKhau; }
    public String getVaiTro() { return vaiTro; }
    public String getHoTen() { return hoTen; }
    public String getSoDienThoai() { return soDienThoai; }

    // SETTERS
    public void setId(String id) { this.id = id; }
    public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
    public void setMatKhau(String matKhau) { this.matKhau = matKhau; }
    public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

    // PHUONG THUC TRUU TUONG
    public abstract void hienThiThongTin();
    public abstract void dangXuat();
    public abstract boolean coQuyen(String quyen);

    // PHUONG THUC DANG NHAP
    public static class DangNhap {
        public static NguoiDung dangNhap(String tenDangNhap, String matKhau) {
            ArrayList<String> lines = QuanLyFile.docFile("users.txt");

            for (String line : lines) {
                String[] data = line.split("\\|");
                if (data[1].equals(tenDangNhap) && data[2].equals(matKhau)) {
                    String vaiTro = data[3];
                    if ("qtv".equals(vaiTro)) {
                        return new QTV(
                                data[0], data[1], data[2], data[3],
                                data[4], data[5], data[6], data[7]
                        );
                    } else {
                        return new KhachHang(
                                data[0], data[1], data[2], data[3],
                                data[4], data[5]
                        );
                    }
                }
            }
            return null;
        }
    }
}

// ===== GIAO DIEN DIEU KHIEN =====
class QTVMenu {
    private int luachon;
    private Scanner sc;
    private QTV qtvHienTai;
    private DSNguoiDung dsNguoiDung;
    private QuanLyFile quanLyFile;
    private DSMonAn dsMonAn;
    private DSHoaDon dsHoaDon;
    private DSThanhToan dsThanhToan;

    public QTVMenu(QuanLyFile quanLyFile) {
        this.dsNguoiDung = new DSNguoiDung();
        this.dsMonAn = new DSMonAn();
        this.dsHoaDon = new DSHoaDon(this.dsMonAn);
        this.dsThanhToan = new DSThanhToan();
        this.quanLyFile = new QuanLyFile();
        this.sc = new Scanner(System.in);
    }

    public void setQTVHienTai(QTV qtv) { this.qtvHienTai = qtv; }
    public DSNguoiDung getDsNguoiDung() { return dsNguoiDung; }
    public DSMonAn getDsMonAn() { return dsMonAn; }
    public DSHoaDon getDsHoaDon() { return dsHoaDon; }
    public DSThanhToan getDsThanhToan() { return dsThanhToan; }


    public void ghiDuLieu() {
        if (quanLyFile != null) {
            quanLyFile.ghiTatCa(this);
        }
    }

    public void choice() {
        do {
            System.out.println("\n===== MENU QUAN LY =====");
            System.out.println("1. Quan ly nguoi dung");
            System.out.println("2. Quan ly mon an");
            System.out.println("3. Quan ly hoa don");
            System.out.println("4. Quan ly lich su thanh toan");
            System.out.println("0. Dang xuat.");
            System.out.print("\nChon: ");
            luachon = Integer.parseInt(sc.nextLine());
            switch (luachon) {
                case 1:
                    int choice1;
                    do {
                        System.out.println("\n===== QUAN LY NGUOI DUNG =====");
                        System.out.println("1. Xem danh sach nguoi dung");
                        System.out.println("2. Them nguoi dung moi");
                        System.out.println("3. Cap nhat thong tin nguoi dung");
                        System.out.println("4. Xoa nguoi dung");
                        System.out.println("5. Tim kiem nguoi dung");
                        System.out.println("0. Thoat");
                        System.out.print("Chon: ");
                        choice1 = Integer.parseInt(sc.nextLine());

                        switch (choice1) {
                            case 1:
                                dsNguoiDung.hienThiTatCa();
                                break;
                            case 2:
                                dsNguoiDung.them();
                                ghiDuLieu();
                                break;
                            case 3:
                                dsNguoiDung.sua();
                                ghiDuLieu();
                                break;
                            case 4:
                                dsNguoiDung.xoa();
                                ghiDuLieu();
                                break;
                            case 5:
                                dsNguoiDung.timKiem();
                                break;
                            default:
                                System.out.println("Lua chon khong hop le.");
                                break;
                        }
                    } while (choice1 != 0);
                    break;
                case 2:
                    int choice2;
                    do {
                        System.out.println("\n===== QUAN LY MENU =====");
                        System.out.println("1. Xem menu");
                        System.out.println("2. Them mon moi");
                        System.out.println("3. Cap nhat mon");
                        System.out.println("4. Xoa mon");
                        System.out.println("5. Tim kiem mon an");
                        System.out.println("0. Thoat");
                        System.out.print("Chon: ");
                        choice2 = Integer.parseInt(sc.nextLine());

                        switch (choice2) {
                            case 1:
                                dsMonAn.hienThiTatCa();
                                break;
                            case 2:
                                dsMonAn.them();
                                ghiDuLieu();
                                break;
                            case 3:
                                dsMonAn.sua();
                                ghiDuLieu();
                                break;
                            case 4:
                                dsMonAn.xoa();
                                ghiDuLieu();
                                break;
                            case 5:
                                dsMonAn.timKiem();
                                break;
                            default:
                                System.out.println("Lua chon khong hop le.");
                                break;
                        }
                    } while (choice2 != 0);
                    break;
                case 3:
                    int choice3;
                    do {
                        System.out.println("\n===== QUAN LY HOA DON =====");
                        System.out.println("1. Xem tat ca hoa don");
                        System.out.println("2. Cap nhat thong tin hoa don");
                        System.out.println("3. Tim kiem hoa don");
                        System.out.println("0. Thoat");
                        System.out.print("Chon: ");
                        choice3 = Integer.parseInt(sc.nextLine());

                        switch (choice3) {
                            case 1:
                                dsHoaDon.hienThiTatCa();
                                break;
                            case 2:
                                dsHoaDon.sua();
                                ghiDuLieu();
                                break;
                            case 3:
                                dsHoaDon.timKiem();
                                break;
                            default:
                                System.out.println("Lua chon khong hop le.");
                                break;
                        }
                    } while (choice3 != 0);
                    break;
                case 4:
                    int choice4;
                    do {
                        System.out.println("\n===== QUAN LY LICH SU THANH TOAN =====");
                        System.out.println("1. Xem tat ca giao dich");
                        System.out.println("2. Tim kiem giao dich");
                        System.out.println("3. Tim kiem theo khoang thoi gian");
                        System.out.println("4. Thong ke doanh thu");
                        System.out.println("5. Cap nhat trang thai thanh toan");
                        System.out.println("0. Thoat");
                        System.out.print("Chon: ");
                        choice4 = Integer.parseInt(sc.nextLine());

                        switch (choice4) {
                            case 1:
                                dsThanhToan.hienThiTatCa();
                                break;
                            case 2:
                                dsThanhToan.timKiem();
                                break;
                            case 3:
                                dsThanhToan.timKiemTheoKhoangThoiGian();
                                break;
                            case 4:
                                dsThanhToan.hienThiThongKe();
                                break;
                            case 5:
                                dsThanhToan.capNhatTrangThai();
                                ghiDuLieu();
                                break;
                            default:
                                System.out.println("Lua chon khong hop le.");
                                break;
                        }
                    } while (choice4 != 0);
                    break;
                case 0:
                    qtvHienTai.dangXuat();
                    return;
                default:
                    System.out.println("Lua chon khong hop le.");
                    break;
            }
        } while (luachon != 0);
    }
}

class KhachHangMenu {
    private int luachon;
    private Scanner sc;
    private KhachHang khachHangHienTai;
    private DSNguoiDung dsNguoiDung;
    private DSMonAn dsMonAn;
    private GioHang gioHang;
    private DSHoaDon dsHoaDon;
    private QTVMenu qtvMenu;

    public KhachHangMenu(DSMonAn dsMonAn, QTVMenu qtvMenu) {
        this.dsNguoiDung = new DSNguoiDung();
        this.dsMonAn = dsMonAn;
        this.gioHang = new GioHang(dsMonAn);
        this.dsHoaDon = qtvMenu.getDsHoaDon();
        this.qtvMenu = qtvMenu;
        this.sc = new Scanner(System.in);
    }

    public void setKhachHangHienTai(KhachHang khachHang) { this.khachHangHienTai = khachHang; }

    public void choice() {
        do {
            System.out.println("\n===== MENU KHACH HANG =====");
            System.out.println("1. Sua thong tin ca nhan");
            System.out.println("2. Xem menu");
            System.out.println("3. Chon mon");
            System.out.println("4. Xem gio hang");
            System.out.println("5. Xac nhan dat hang");
            System.out.println("6. Xem don hang");
            System.out.println("7. Thanh toan");
            System.out.println("0. Dang xuat.");
            System.out.print("\nChon: ");
            luachon = sc.nextInt();
            sc.nextLine(); // clear buffer

            switch (luachon) {
                case 1:
                    dsNguoiDung.capNhatThongTin(khachHangHienTai);
                    qtvMenu.ghiDuLieu();
                    break;
                case 2:
                    dsMonAn.hienThiTatCa();
                    break;
                case 3:
                    gioHang.chonMon();
                    break;
                case 4:
                    gioHang.hienThi();
                    break;
                case 5:
                    if (gioHang.rong()) {
                        System.out.println("Gio hang trong!");
                    } else {
                        // Hien thi gio hang truoc khi xac nhan
                        gioHang.hienThi();

                        // Cho nguoi dung chon phuong thuc thanh toan
                        System.out.println("\n===== LUA CHON PHUONG THUC THANH TOAN =====");
                        System.out.println("1. Chuyen khoan");
                        System.out.println("2. Tien mat");
                        System.out.print("Chon phuong thuc thanh toan: ");
                        int phuongThucChoice = sc.nextInt();
                        sc.nextLine(); // clear buffer

                        String phuongThucStr = "";
                        if (phuongThucChoice == 1) {
                            phuongThucStr = "Chuyen khoan";
                        } else if (phuongThucChoice == 2) {
                            phuongThucStr = "Tien mat";
                        } else {
                            System.out.println("Lua chon khong hop le, mac dinh la Chuyen khoan");
                            phuongThucStr = "Chuyen khoan";
                        }

                        System.out.print("Ban co chac chan muon dat hang? (y/n): ");
                        String xacNhan = sc.nextLine();

                        if (xacNhan.equalsIgnoreCase("y")) {
                            HoaDon hoaDonMoi = gioHang.xacNhanDon(khachHangHienTai.getId(), qtvMenu.getDsHoaDon());
                            if (hoaDonMoi != null) {
                                try {
                                    // Them don hang vao danh sach
                                    qtvMenu.getDsHoaDon().themHoaDon(hoaDonMoi);
                                    System.out.println("Da them don hang vao danh sach!");

                                    // Tao thanh toan tu don hang VOI PHUONG THUC DA CHON
                                    qtvMenu.getDsThanhToan().themThanhToanTuHoaDon(hoaDonMoi, phuongThucStr);
                                    System.out.println("Da tao thong tin thanh toan!");

                                    // Luu du lieu
                                    qtvMenu.ghiDuLieu();

                                    System.out.println("====================================");
                                    System.out.println("DAT HANG THANH CONG!");
                                    System.out.println("Ma don hang: " + hoaDonMoi.getMaDon());
                                    System.out.println("Tong tien: " + hoaDonMoi.getTongTien() + " VND");
                                    System.out.println("Phuong thuc thanh toan: " + phuongThucStr);
                                    System.out.println("Trang thai: " + hoaDonMoi.getTrangThai());
                                    System.out.println("====================================");

                                    // Xoa gio hang sau khi dat hang thanh cong
                                    gioHang.xoaGioHang();

                                } catch (Exception e) {
                                    System.out.println("Loi khi luu don hang: " + e.getMessage());
                                }
                            } else {
                                System.out.println("Khong the tao don hang!");
                            }
                        } else {
                            System.out.println("Da huy dat hang!");
                        }
                    }
                    break;
                case 6:
                    dsHoaDon.hienThiHoaDonKhachHang(khachHangHienTai.getId());
                    break;
                case 7:
                    // Hien thi hoa don chua thanh toan cua khach hang
                    ArrayList<HoaDon> hoaDonChuaThanhToan = new ArrayList<>();
                    for (HoaDon hd : qtvMenu.getDsHoaDon().getDSHoaDon()) {
                        if (hd.getMaKhachHang().equals(khachHangHienTai.getId()) &&
                                !hd.getTrangThai().equals("Da thanh toan")) {
                            hoaDonChuaThanhToan.add(hd);
                        }
                    }

                    if (hoaDonChuaThanhToan.isEmpty()) {
                        System.out.println("Khong co hoa don nao can thanh toan.");
                    } else {
                        System.out.println("\n===== HOA DON CAN THANH TOAN =====");
                        for (int i = 0; i < hoaDonChuaThanhToan.size(); i++) {
                            System.out.println((i + 1) + ". Ma don: " + hoaDonChuaThanhToan.get(i).getMaDon() +
                                    " - Tong tien: " + String.format("%,d", hoaDonChuaThanhToan.get(i).getTongTien()) + " VND" +
                                    " - Trang thai: " + hoaDonChuaThanhToan.get(i).getTrangThai());
                        }

                        System.out.print("Chon hoa don can thanh toan (nhap so thu tu): ");
                        int luaChonHoaDon = sc.nextInt();
                        sc.nextLine(); // clear buffer

                        if (luaChonHoaDon > 0 && luaChonHoaDon <= hoaDonChuaThanhToan.size()) {
                            HoaDon hoaDonThanhToan = hoaDonChuaThanhToan.get(luaChonHoaDon - 1);

                            System.out.println("\nTHONG TIN HOA DON:");
                            hoaDonThanhToan.hienThiThongTin();

                            System.out.println("\n===== LUA CHON PHUONG THUC THANH TOAN =====");
                            System.out.println("1. Chuyen khoan");
                            System.out.println("2. Tien mat");
                            System.out.print("Chon phuong thuc thanh toan: ");
                            int phuongThuc = sc.nextInt();
                            sc.nextLine(); // clear buffer

                            String phuongThucStr = (phuongThuc == 1) ? "Chuyen khoan" : "Tien mat";

                            System.out.print("Xac nhan thanh toan? (y/n): ");
                            String xacNhan = sc.nextLine();

                            if (xacNhan.equalsIgnoreCase("y")) {
                                // Cap nhat trang thai hoa don
                                hoaDonThanhToan.setTrangThai("Da thanh toan");

                                // Cap nhat trang thai thanh toan tuong ung
                                for (ThanhToan tt : qtvMenu.getDsThanhToan().getDSThanhToan()) {
                                    if (tt.getMaDon().equals(hoaDonThanhToan.getMaDon())) {
                                        tt.setTrangThai("Thanh cong");
                                        tt.setPhuongThuc(phuongThucStr);
                                        break;
                                    }
                                }

                                // Luu du lieu
                                qtvMenu.ghiDuLieu();

                                System.out.println("====================================");
                                System.out.println("THANH TOAN THANH CONG!");
                                System.out.println("Ma don: " + hoaDonThanhToan.getMaDon());
                                System.out.println("So tien: " + String.format("%,d", hoaDonThanhToan.getTongTien()) + " VND");
                                System.out.println("Phuong thuc: " + phuongThucStr);
                                System.out.println("====================================");
                            } else {
                                System.out.println("Da huy thanh toan!");
                            }
                        } else {
                            System.out.println("Lua chon khong hop le!");
                        }
                    }
                    break;
                case 0:
                    khachHangHienTai.dangXuat();
                    return;
                default:
                    System.out.println("Lua chon khong hop le.");
                    break;
            }
        } while (luachon != 0);
    }
}

// ===== NHOM NGUOI DUNG =====
class QTV extends NguoiDung {
    private String diaChi;
    private String email;

    public QTV(DSNguoiDung dsNguoiDung) {
        super(taoIDTuDong_QTV(dsNguoiDung), "", "", "qtv", "", "");
    }

    public QTV(String id, String tenDangNhap, String matKhau, String vaiTro,
               String hoTen, String soDienThoai, String diaChi, String email) {
        super(id, tenDangNhap, matKhau, vaiTro, hoTen, soDienThoai);
        this.diaChi = diaChi;
        this.email = email;
    }

    @Override
    public void hienThiThongTin() {
        System.out.println("=== THONG TIN QUAN TRI VIEN ===");
        System.out.println("ID: " + getId());
        System.out.println("Ten: " + getHoTen());
        System.out.println("SDT: " + getSoDienThoai());
        System.out.println("Email: " + email);
        System.out.println("Dia chi: " + diaChi);
        System.out.println("Username: " + getTenDangNhap());
        System.out.println("Vai tro: " + getVaiTro());
    }

    @Override
    public void dangXuat() {
        System.out.println("Quan tri vien " + this.getHoTen() + " da dang xuat.");
    }

    @Override
    public boolean coQuyen(String quyen) {
        return true; // Admin co tat ca quyen
    }

    private static String taoIDTuDong_QTV(DSNguoiDung dsKhachHang) {
        int maxId = 0;

        // Doc file de dam bao luon lay duoc ID moi nhat tu file
        ArrayList<String> lines = QuanLyFile.docFile("users.txt");
        for (String line : lines) {
            String[] data = line.split("\\|");
            if (data.length >= 1 && data[0].startsWith("A")) {
                try {
                    int idNum = Integer.parseInt(data[0].substring(1));
                    if (idNum > maxId) maxId = idNum;
                } catch (NumberFormatException e) {
                    // Bo qua neu ID khong dung dinh dang
                }
            }
        }
        return "A" + String.format("%03d", maxId + 1);
    }

    // GETTERS & SETTERS
    public String getDiaChi() { return diaChi; }
    public String getEmail() { return email; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public void setEmail(String email) { this.email = email; }
}

class KhachHang extends NguoiDung {
    private static int demKhachHang = 0;

    public KhachHang(DSNguoiDung dsNguoiDung) {
        super(taoIDTuDong_KH(dsNguoiDung), "", "", "khachhang", "", "");
    }

    public KhachHang(String id, String tenDangNhap, String matKhau, String vaiTro,
                     String hoTen, String soDienThoai) {
        super(id, tenDangNhap, matKhau, vaiTro, hoTen, soDienThoai);
    }

    @Override
    public void hienThiThongTin() {
        System.out.println("=== THONG TIN KHACH HANG ===");
        System.out.println("ID: " + getId());
        System.out.println("Ten: " + getHoTen());
        System.out.println("SDT: " + getSoDienThoai());
        System.out.println("Username: " + getTenDangNhap());
        System.out.println("Vai tro: " + getVaiTro());
    }

    @Override
    public void dangXuat() {
        System.out.println("Khach hang " + this.getHoTen() + " da dang xuat.");
    }

    @Override
    public boolean coQuyen(String quyen) {
        return "xem_menu".equals(quyen) || "dat_mon".equals(quyen) || "xem_don_hang".equals(quyen);
    }

    // PHUONG THUC DANG KY
    public void dangKy() {
        Scanner sc = new Scanner(System.in);
        System.out.println("===== Dang ky tai khoan khach hang =====");
        System.out.println("ID: " + this.getId());

        boolean trungTenDangNhap;
        do {
            System.out.print("Nhap ten dang nhap: ");
            String tenDangNhap = sc.nextLine();
            trungTenDangNhap = kiemTraTrungTenDangNhap(tenDangNhap);
            if (trungTenDangNhap) {
                System.out.println("Ten dang nhap da ton tai! Vui long chon ten khac.");
            } else {
                this.setTenDangNhap(tenDangNhap);
                break;
            }
        } while (trungTenDangNhap);

        System.out.print("Nhap mat khau: ");
        this.setMatKhau(sc.nextLine());
        System.out.print("Nhap ho ten: ");
        this.setHoTen(sc.nextLine());
        System.out.print("Nhap so dien thoai: ");
        this.setSoDienThoai(sc.nextLine());
        this.setVaiTro("khachhang");
    }

    private static String taoIDTuDong_KH(DSNguoiDung dsKhachHang) {
        int maxId = 0;

        // Doc file de dam bao luon lay duoc ID moi nhat tu file
        ArrayList<String> lines = QuanLyFile.docFile("users.txt");
        for (String line : lines) {
            String[] data = line.split("\\|");
            if (data.length >= 1 && data[0].startsWith("C")) {
                try {
                    int idNum = Integer.parseInt(data[0].substring(1));
                    if (idNum > maxId) maxId = idNum;
                } catch (NumberFormatException e) {
                    // Bo qua neu ID khong dung dinh dang
                }
            }
        }
        return "C" + String.format("%03d", maxId + 1);
    }

    private boolean kiemTraTrungTenDangNhap(String tenDangNhap) {
        ArrayList<String> lines = QuanLyFile.docFile("users.txt");
        for (String line : lines) {
            String[] data = line.split("\\|");
            if (data.length >= 2 && data[1].equals(tenDangNhap)) {
                return true;
            }
        }
        return false;
    }
}

class DSNguoiDung implements ChucNang {
    private ArrayList<NguoiDung> dsNguoiDung = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    public ArrayList<NguoiDung> getDSNguoiDung() { return dsNguoiDung; }

    // THEM NGUOI DUNG VAO DANH SACH
    public void themNguoiDung(NguoiDung nguoiDung) {
        dsNguoiDung.add(nguoiDung);
    }

    @Override
    public void hienThiTatCa() {
        System.out.println("\n===== DANH SACH NGUOI DUNG =====");

        // HIỂN THỊ QUẢN TRỊ VIÊN
        ArrayList<QTV> dsQTV = getDanhSachQTV();
        if (!dsQTV.isEmpty()) {
            System.out.println("\n--- QUAN TRI VIEN ---");
            System.out.printf("%-6s | %-15s | %-20s | %-12s | %-25s | %-20s\n",
                    "ID", "Username", "Ten", "SDT", "Dia chi", "Email");
            System.out.println("---------------------------------------------------------------------------------------------------");
            for (QTV qtv : dsQTV) {
                System.out.printf("%-6s | %-15s | %-20s | %-12s | %-25s | %-20s\n",
                        qtv.getId(), qtv.getTenDangNhap(), qtv.getHoTen(),
                        qtv.getSoDienThoai(), qtv.getDiaChi(), qtv.getEmail());
            }
        }

        // HIỂN THỊ KHÁCH HÀNG
        ArrayList<KhachHang> dsKH = getDanhSachKhachHang();
        if (!dsKH.isEmpty()) {
            System.out.println("\n--- KHACH HANG ---");
            System.out.printf("%-6s | %-15s | %-20s | %-12s\n", "ID", "Username", "Ten", "SDT");
            System.out.println("--------------------------------------------------------");
            for (KhachHang kh : dsKH) {
                System.out.printf("%-6s | %-15s | %-20s | %-12s\n",
                        kh.getId(), kh.getTenDangNhap(), kh.getHoTen(), kh.getSoDienThoai());
            }
        }

        // THÔNG BÁO NẾU KHÔNG CÓ NGƯỜI DÙNG
        if (dsNguoiDung.isEmpty()) {
            System.out.println("Chua co nguoi dung nao.");
        }
    }

    @Override
    public void them() {
        System.out.println("\n===== THEM NGUOI DUNG MOI =====");
        System.out.println("1. Them quan tri vien");
        System.out.println("2. Them khach hang");
        System.out.print("Chon loai nguoi dung: ");
        int luaChon = Integer.parseInt(sc.nextLine());

        if (luaChon == 1) {
            themQTV();
        } else if (luaChon == 2) {
            themKhachHang();
        } else {
            System.out.println("Lua chon khong hop le!");
        }
    }

    private void themQTV() {
        System.out.println("\n===== THEM QUAN TRI VIEN MOI =====");
        System.out.print("Nhap ID quan tri vien: ");
        String id = sc.nextLine();

        String tenDangNhap;
        boolean trungTenDangNhap;
        do {
            System.out.print("Nhap ten dang nhap: ");
            tenDangNhap = sc.nextLine();
            trungTenDangNhap = kiemTraTrungTenDangNhap(tenDangNhap);
            if (trungTenDangNhap) {
                System.out.println("Ten dang nhap da ton tai! Vui long chon ten khac.");
            }
        } while (trungTenDangNhap);

        System.out.print("Nhap mat khau: ");
        String matKhau = sc.nextLine();
        System.out.print("Nhap ho ten: ");
        String hoTen = sc.nextLine();
        System.out.print("Nhap so dien thoai: ");
        String soDienThoai = sc.nextLine();
        System.out.print("Nhap dia chi: ");
        String diaChi = sc.nextLine();
        System.out.print("Nhap email: ");
        String email = sc.nextLine();

        QTV qtv = new QTV(id, tenDangNhap, matKhau, "qtv", hoTen, soDienThoai, diaChi, email);
        dsNguoiDung.add(qtv);
        System.out.println("Them quan tri vien thanh cong!");
    }

    private void themKhachHang() {
        System.out.println("\n===== THEM KHACH HANG MOI =====");
        KhachHang khachHang = new KhachHang(this);

        System.out.println("ID: " + khachHang.getId());

        boolean trungTenDangNhap;
        do {
            System.out.print("Nhap ten dang nhap: ");
            String tenDangNhap = sc.nextLine();
            trungTenDangNhap = kiemTraTrungTenDangNhap(tenDangNhap);
            if (trungTenDangNhap) {
                System.out.println("Ten dang nhap da ton tai! Vui long chon ten khac.");
            } else {
                khachHang.setTenDangNhap(tenDangNhap);
                break;
            }
        } while (trungTenDangNhap);

        System.out.print("Nhap mat khau: ");
        khachHang.setMatKhau(sc.nextLine());
        System.out.print("Nhap ho ten: ");
        khachHang.setHoTen(sc.nextLine());
        System.out.print("Nhap so dien thoai: ");
        khachHang.setSoDienThoai(sc.nextLine());
        khachHang.setVaiTro("khachhang");

        dsNguoiDung.add(khachHang);
        System.out.println("Them khach hang thanh cong!");
    }

    // admin dung
    @Override
    public void sua() {
        System.out.print("Nhap ID nguoi dung can cap nhat: ");
        String id = sc.nextLine();
        NguoiDung nguoiDung = timNguoiDungTheoId(id);

        if (nguoiDung == null) {
            System.out.println("Khong tim thay nguoi dung!");
            return;
        }

        capNhatThongTin(nguoiDung);
    }

    @Override
    public void xoa() {
        System.out.println("\n===== XOA NGUOI DUNG =====");
        System.out.print("Nhap ID nguoi dung can xoa: ");
        String id = sc.nextLine();

        NguoiDung nguoiDung = timNguoiDungTheoId(id);
        if (nguoiDung == null) {
            System.out.println("Khong tim thay nguoi dung co ID nay!");
            return;
        }

        // Hien thi thong tin nguoi dung truoc khi xoa
        System.out.println("\nTHONG TIN NGUOI DUNG SE BI XOA:");
        nguoiDung.hienThiThongTin();

        System.out.print("\nBan co CHAC CHAN muon xoa nguoi dung nay? (y/n): ");
        String xacNhan = sc.nextLine();

        if (xacNhan.equalsIgnoreCase("y")) {
            dsNguoiDung.remove(nguoiDung);
            System.out.println("Xoa nguoi dung thanh cong!");
        } else {
            System.out.println("Da huy thao tac xoa!");
        }
    }

    @Override
    public void timKiem() {
        System.out.println("\n===== TIM KIEM NGUOI DUNG =====");
        System.out.print("Nhap ID hoac ten nguoi dung can tim: ");
        String tuKhoa = sc.nextLine();

        boolean timThay = false;
        System.out.println("\nKET QUA TIM KIEM:");
        System.out.printf("%-6s | %-15s | %-20s | %-12s | %-10s\n",
                "ID", "Username", "Ten", "SDT", "Vai tro");
        System.out.println("------------------------------------------------------------");

        for (NguoiDung nd : dsNguoiDung) {
            if (nd.getId().equalsIgnoreCase(tuKhoa) || nd.getHoTen().toLowerCase().contains(tuKhoa.toLowerCase())) {
                System.out.printf("%-6s | %-15s | %-20s | %-12s | %-10s\n",
                        nd.getId(), nd.getTenDangNhap(), nd.getHoTen(),
                        nd.getSoDienThoai(), nd.getVaiTro());
                timThay = true;
            }
        }

        if (!timThay) {
            System.out.println("Khong tim thay nguoi dung nao phu hop!");
        }
    }

    // PHUONG THUC HO TRO
    public NguoiDung timNguoiDungTheoId(String id) {
        for (NguoiDung nd : dsNguoiDung) {
            if (nd.getId().equalsIgnoreCase(id)) {
                return nd;
            }
        }
        return null;
    }

    public NguoiDung timNguoiDungTheoTenDangNhap(String tenDangNhap) {
        for (NguoiDung nd : dsNguoiDung) {
            if (nd.getTenDangNhap().equals(tenDangNhap)) {
                return nd;
            }
        }
        return null;
    }

    public ArrayList<KhachHang> getDanhSachKhachHang() {
        ArrayList<KhachHang> dsKhachHang = new ArrayList<>();
        for (NguoiDung nd : dsNguoiDung) {
            if (nd instanceof KhachHang) {
                dsKhachHang.add((KhachHang) nd);
            }
        }
        return dsKhachHang;
    }

    public ArrayList<QTV> getDanhSachQTV() {
        ArrayList<QTV> dsQTV = new ArrayList<>();
        for (NguoiDung nd : dsNguoiDung) {
            if (nd instanceof QTV) {
                dsQTV.add((QTV) nd);
            }
        }
        return dsQTV;
    }

    // khach dung
    public void capNhatThongTin(NguoiDung nguoiDung) {
        int luaChon;
        do {
            System.out.println("\n==== CAP NHAT THONG TIN NGUOI DUNG ====");
            System.out.println("1. Doi ten: " + nguoiDung.getHoTen());
            System.out.println("2. Doi so dien thoai: " + nguoiDung.getSoDienThoai());
            System.out.println("3. Doi mat khau");

            if (nguoiDung instanceof QTV) {
                System.out.println("4. Doi dia chi");
                System.out.println("5. Doi email");
            }

            System.out.println("0. Thoat");
            System.out.print("\nChon thong tin can chinh sua: ");
            luaChon = Integer.parseInt(sc.nextLine());

            switch (luaChon) {
                case 1:
                    System.out.print("Nhap ten moi: ");
                    nguoiDung.setHoTen(sc.nextLine());
                    System.out.println("Cap nhat ten thanh cong!");
                    break;
                case 2:
                    System.out.print("Nhap so dien thoai moi: ");
                    nguoiDung.setSoDienThoai(sc.nextLine());
                    System.out.println("Cap nhat so dien thoai thanh cong!");
                    break;
                case 3:
                    System.out.print("Nhap mat khau moi: ");
                    nguoiDung.setMatKhau(sc.nextLine());
                    System.out.println("Cap nhat mat khau thanh cong!");
                    break;
                case 4:
                    if (nguoiDung instanceof QTV) {
                        System.out.print("Nhap dia chi moi: ");
                        ((QTV) nguoiDung).setDiaChi(sc.nextLine());
                        System.out.println("Cap nhat dia chi thanh cong!");
                    }
                    break;
                case 5:
                    if (nguoiDung instanceof QTV) {
                        System.out.print("Nhap email moi: ");
                        ((QTV) nguoiDung).setEmail(sc.nextLine());
                        System.out.println("Cap nhat email thanh cong!");
                    }
                    break;
                case 0:
                    System.out.println("Thoat cap nhat.");
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
                    break;
            }
        } while (luaChon != 0);
    }

    private boolean kiemTraTrungTenDangNhap(String tenDangNhap) {
        for (NguoiDung nd : dsNguoiDung) {
            if (nd.getTenDangNhap().equals(tenDangNhap)) {
                return true;
            }
        }

        ArrayList<String> lines = QuanLyFile.docFile("users.txt");
        for (String line : lines) {
            String[] data = line.split("\\|");
            if (data.length >= 2 && data[1].equals(tenDangNhap)) {
                return true;
            }
        }
        return false;
    }

    public void xoaTatCa() {
        dsNguoiDung.clear();
    }

    public void docFile(String filename) {
        ArrayList<String> lines = QuanLyFile.docFile(filename);
        dsNguoiDung.clear();

        for (String line : lines) {
            String[] data = line.split("\\|");
            if (data.length >= 6) {
                String vaiTro = data[3];
                if ("qtv".equals(vaiTro) && data.length >= 8) {
                    QTV qtv = new QTV(data[0], data[1], data[2], data[3],
                            data[4], data[5], data[6], data[7]);
                    dsNguoiDung.add(qtv);
                } else if ("khachhang".equals(vaiTro)) {
                    KhachHang kh = new KhachHang(data[0], data[1], data[2], data[3],
                            data[4], data[5]);
                    dsNguoiDung.add(kh);
                }
            }
        }
    }

    public void ghiFile(String filename) {
        ArrayList<String> oldLines = QuanLyFile.docFile(filename);
        ArrayList<String> newlines = new ArrayList<>();
        for (NguoiDung nd : dsNguoiDung) {
            if (nd instanceof QTV) {
                QTV a = (QTV) nd;
                newlines.add(String.format("%s|%s|%s|%s|%s|%s|%s|%s",
                        a.getId(), a.getTenDangNhap(), a.getMatKhau(), a.getVaiTro(),
                        a.getHoTen(), a.getSoDienThoai(), a.getDiaChi(), a.getEmail()));
            } else if (nd instanceof KhachHang) {
                KhachHang c = (KhachHang) nd;
                newlines.add(String.format("%s|%s|%s|%s|%s|%s",
                        c.getId(), c.getTenDangNhap(), c.getMatKhau(),
                        c.getVaiTro(), c.getHoTen(), c.getSoDienThoai()));
            }
        }
        QuanLyFile.ghiFile(filename, newlines);
    }
}

// ===== NHOM MON AN =====
class MonAn {
    private String maMon;
    private String tenMon;
    private String loai;
    private int gia;
    private String moTa;

    public MonAn(String maMon, String tenMon, String loai, int gia, String moTa) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.loai = loai;
        this.gia = gia;
        this.moTa = moTa;
    }

    // GETTERS
    public String getMaMon() { return maMon; }
    public String getTenMon() { return tenMon; }
    public String getLoai() { return loai; }
    public int  getGia() { return gia; }
    public String getMoTa() { return moTa; }

    // SETTERS
    public void setMaMon(String maMon) { this.maMon = maMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }
    public void setLoai(String loai) { this.loai = loai; }
    public void setGia(int gia) { this.gia = gia; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

    public void hienThiThongTin() {
        System.out.printf("%-8s | %-20s | %-15s | %10s | %s\n", maMon, tenMon, loai, String.format("%,d", gia) + " VND", moTa);
    }
}

class DSMonAn implements ChucNang {
    private ArrayList<MonAn> dsMonAn = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    public ArrayList<MonAn> getDSMonAn() { return dsMonAn; }

    @Override
    public void hienThiTatCa() {
        System.out.println("\n===== DANH SACH MON AN =====");
        if (dsMonAn.isEmpty()) {
            System.out.println("Chua co mon an nao.");
            return;
        }
        System.out.printf("%-8s | %-20s | %-15s | %12s | %s\n", "Ma mon", "Ten mon", "Loai", "Gia", "Mo ta");
        System.out.println("----------------------------------------------------------------------------------------");
        for (MonAn m : dsMonAn) {
            m.hienThiThongTin();
        }
    }

    @Override
    public void them() {
        System.out.println("\n===== THEM MON AN MOI =====");

        // Tao ma mon tu dong
        String newMaMon = taoMaMonTuDong();

        System.out.println("Ma mon: " + newMaMon);
        System.out.print("Nhap ten mon: ");
        String tenMon = sc.nextLine();
        System.out.print("Nhap loai mon: ");
        String loai = sc.nextLine();
        System.out.print("Nhap gia: ");
        int gia = sc.nextInt();
        sc.nextLine(); // clear buffer
        System.out.print("Nhap mo ta: ");
        String moTa = sc.nextLine();

        MonAn monAnMoi = new MonAn(newMaMon, tenMon, loai, gia, moTa);
        dsMonAn.add(monAnMoi);
        System.out.println("Them mon an thanh cong!");
    }

    private String taoMaMonTuDong() {
        int maxId = 0;
        ArrayList<String> lines = QuanLyFile.docFile("foods.txt");
        for (String line : lines) {
            String[] data = line.split("\\|");
            if (data.length >= 1 && data[0].startsWith("F")) {
                try {
                    int idNum = Integer.parseInt(data[0].substring(1));
                    if (idNum > maxId) maxId = idNum;
                } catch (NumberFormatException e) {
                    // Bo qua neu ID khong dung dinh dang
                }
            }
        }

        String newId = "F" + String.format("%03d", maxId + 1);
        return newId;
    }

    @Override
    public void sua() {
        System.out.print("Nhap ma mon can cap nhat: ");
        String maMon = sc.nextLine();
        MonAn monAn = null;
        for (MonAn m : dsMonAn) {
            if (m.getMaMon().equalsIgnoreCase(maMon)){
                monAn = m;
                break;
            }
        }
        if (monAn == null) {
            System.out.println("Khong tim thay mon an nay");
            return;
        }

        int luaChon;
        do {
            System.out.println("\n==== CAP NHAT THONG TIN MON AN ====");
            System.out.println("1. Doi ten: " + monAn.getTenMon());
            System.out.println("2. Doi loai: " + monAn.getLoai());
            System.out.println("3. Doi gia: " + String.format("%,d", monAn.getGia()) + " VND");
            System.out.println("4. Doi mo ta: " + monAn.getMoTa());
            System.out.println("0. Thoat");
            System.out.print("\nChon thong tin can chinh sua: ");
            luaChon = Integer.parseInt(sc.nextLine());

            switch (luaChon) {
                case 1:
                    System.out.print("Nhap ten moi: ");
                    monAn.setTenMon(sc.nextLine());
                    ghiFile("foods.txt");
                    System.out.println("Cap nhat ten thanh cong!");
                    break;
                case 2:
                    System.out.print("Nhap loai moi: ");
                    monAn.setLoai(sc.nextLine());
                    ghiFile("foods.txt");
                    System.out.println("Cap nhat loai thanh cong!");
                    break;
                case 3:
                    System.out.print("Nhap gia moi: ");
                    monAn.setGia(sc.nextInt());
                    sc.nextLine(); // clear buffer
                    ghiFile("foods.txt");
                    System.out.println("Cap nhat gia thanh cong!");
                    break;
                case 4:
                    System.out.print("Nhap mo ta moi: ");
                    monAn.setMoTa(sc.nextLine());
                    ghiFile("foods.txt");
                    System.out.println("Cap nhat mo ta thanh cong!");
                    break;
                case 0:
                    System.out.println("Thoat cap nhat.");
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
                    break;
            }
        } while (luaChon != 0);
    }

    @Override
    public void xoa() {
        System.out.println("\n===== XOA MON AN =====");
        System.out.print("Nhap ma mon an can xoa: ");
        String maMon = sc.nextLine();

        MonAn monAn = null;
        for (MonAn m : dsMonAn) {
            if (m.getMaMon().equalsIgnoreCase(maMon)) {
                monAn = m;
                break;
            }
        }
        if (monAn == null) {
            System.out.println("Khong tim thay mon an co ma nay!");
            return;
        }

        // Hien thi thong tin mon an truoc khi xoa
        System.out.println("\nTHONG TIN MON AN SE BI XOA:");
        monAn.hienThiThongTin();

        System.out.print("\nBan co CHAC CHAN muon xoa mon an nay? (y/n): ");
        String xacNhan = sc.nextLine();

        if (xacNhan.equalsIgnoreCase("y")) {
            dsMonAn.remove(monAn);
            System.out.println("Xoa mon an thanh cong!");
        } else {
            System.out.println("Da huy thao tac xoa!");
        }
    }

    @Override
    public void timKiem() {
        System.out.println("\n===== TIM KIEM MON AN =====");
        System.out.print("Nhap ma mon hoac ten mon can tim: ");
        String tuKhoa = sc.nextLine();

        boolean timThay = false;
        System.out.println("\nKET QUA TIM KIEM:");
        System.out.printf("%-8s | %-20s | %-15s | %12s | %s\n", "Ma mon", "Ten mon", "Loai", "Gia", "Mo ta");
        System.out.println("----------------------------------------------------------------------------------------");
        for (MonAn m : dsMonAn) {
            if (m.getMaMon().equalsIgnoreCase(tuKhoa) || m.getTenMon().toLowerCase().contains(tuKhoa.toLowerCase())) {
                m.hienThiThongTin();
                timThay = true;
            }
        }

        if (!timThay) {
            System.out.println("Khong tim thay mon an nao phu hop!");
        }
    }

    // Tim mon an theo ma mon
    public MonAn timMonAnTheoMa(String maMon) {
        for (MonAn m : dsMonAn) {
            if (m.getMaMon().equals(maMon)) {
                return m;
            }
        }
        return null;
    }

    // Doc du lieu tu file
    public void docFile(String filename) {
        ArrayList<String> lines = QuanLyFile.docFile(filename);
        dsMonAn.clear();

        for (String line : lines) {
            String[] data = line.split("\\|");
            if (data.length >= 5) {
                try {
                    int gia = Integer.parseInt(data[2]);
                    MonAn monAn = new MonAn(data[0], data[1], data[3], gia, data[4]);
                    dsMonAn.add(monAn);
                } catch (NumberFormatException e) {
                    System.out.println("Loi dinh dang gia cho mon: " + data[0]);
                }
            }
        }
        System.out.println("Doc du lieu mon an thanh cong! " + dsMonAn.size() + " mon an");
    }

    // Ghi du lieu vao file
    public void ghiFile(String filename) {
        ArrayList<String> oldLines = QuanLyFile.docFile(filename);
        ArrayList<String> newlines = new ArrayList<>();
        for (MonAn m : dsMonAn) {
            newlines.add(String.format("%s|%s|%d|%s|%s",
                    m.getMaMon(), m.getTenMon(), m.getGia(), m.getLoai(), m.getMoTa()));
        }
        QuanLyFile.ghiFile(filename, newlines);
    }
}

// ===== NHOM GIO HANG =====
class ChiTietHoaDon {
    private String maMon;
    private String tenMon;
    private int soLuong;
    private int donGia;
    private int  thanhTien;

    public ChiTietHoaDon(String maMon, String tenMon, int soLuong, int donGia) {
        this.maMon = maMon;
        this.tenMon = tenMon;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = soLuong * donGia;
    }

    // GETTERS
    public String getMaMon() { return maMon; }
    public String getTenMon() { return tenMon; }
    public int getSoLuong() { return soLuong; }
    public int  getDonGia() { return donGia; }
    public int  getThanhTien() { return thanhTien; }

    // SETTERS
    public void setMaMon(String maMon) { this.maMon = maMon; }
    public void setTenMon(String tenMon) { this.tenMon = tenMon; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; this.thanhTien = soLuong * donGia; }
    public void setDonGia(int donGia) { this.donGia = donGia; this.thanhTien = soLuong * donGia; }
}

class GioHang {
    private ArrayList<ChiTietHoaDon> dsGioHang = new ArrayList<>();
    private DSMonAn dsMonAn;
    private Scanner sc = new Scanner(System.in);

    public GioHang(DSMonAn dsMonAn) {
        this.dsMonAn = dsMonAn;
    }

    public void chonMon() {
        System.out.println("\n===== CHON MON =====");
        dsMonAn.hienThiTatCa();

        System.out.print("Nhap ma mon an muon chon (0 de thoat): ");
        String maMon = sc.nextLine();

        if (maMon.equals("0")) return;

        MonAn monAnChon = dsMonAn.timMonAnTheoMa(maMon);
        if (monAnChon == null) {
            System.out.println("Khong tim thay mon an voi ma nay!");
            return;
        }

        int soLuong;
        while (true) {
            System.out.print("Nhap so luong: ");
            try {
                soLuong = sc.nextInt();
                sc.nextLine();

                if (soLuong <= 0) {
                    System.out.println("So luong phai lon hon 0! Vui long nhap lai.");
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("Vui long nhap so nguyen hop le!");
                sc.nextLine();
            }
        }

        // Kiem tra xem mon da co trong gio chua
        boolean timThay = false;
        for (ChiTietHoaDon item : dsGioHang) {
            if (item.getMaMon().equals(maMon)) {
                item.setSoLuong(item.getSoLuong() + soLuong);
                timThay = true;
                break;
            }
        }

        if (!timThay) {
            ChiTietHoaDon itemMoi = new ChiTietHoaDon(maMon, monAnChon.getTenMon(), soLuong, monAnChon.getGia());
            dsGioHang.add(itemMoi);
        }

        System.out.println("Da them " + soLuong + " " + monAnChon.getTenMon() + " vao gio hang!");
    }

    public void hienThi() {
        System.out.println("\n===== GIO HANG =====");
        if (dsGioHang.isEmpty()) {
            System.out.println("Gio hang trong!");
            return;
        }

        int  tongTien = 0;
        System.out.printf("%-8s | %-20s | %8s | %12s | %12s\n", "Ma mon", "Ten mon", "So luong", "Don gia", "Thanh tien");
        System.out.println("--------------------------------------------------------------------------------");
        for (ChiTietHoaDon item : dsGioHang) {
            System.out.printf("%-8s | %-20s | %8d | %12s | %12s\n",
                    item.getMaMon(), item.getTenMon(), item.getSoLuong(),
                    String.format("%,d", item.getDonGia()) + " VND",
                    String.format("%,d", item.getThanhTien()) + " VND");
            tongTien += item.getThanhTien();
        }
        System.out.println("--------------------------------------------------------------------------------");
        System.out.printf("TONG TIEN: %s\n", String.format("%,d", tongTien) + " VND");
    }

    public HoaDon xacNhanDon(String maKhachHang, DSHoaDon dsHoaDon) {
        if (dsGioHang.isEmpty()) {
            System.out.println("Gio hang trong, khong the dat hang!");
            return null;
        }

        // Tao ma don tu dong
        String maDonMoi = taoMaDonTuDong(dsHoaDon);

        // Lay ngay hien tai
        String ngayDat = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Tinh tong tien
        int tongTien = 0;
        for (ChiTietHoaDon item : dsGioHang) {
            tongTien += item.getThanhTien();
        }

        // Tao don hang moi
        HoaDon hoaDonMoi = new HoaDon(maDonMoi, maKhachHang, ngayDat, "Cho xac nhan", tongTien, new ArrayList<>(dsGioHang));
        return hoaDonMoi;
    }

    private String taoMaDonTuDong(DSHoaDon dsHoaDon) {
        int maxId = 0;

        // Tim ma don lon nhat trong danh sach hien tai
        for (HoaDon hd : dsHoaDon.getDSHoaDon()) {
            if (hd.getMaDon().startsWith("OD")) {
                try {
                    int idNum = Integer.parseInt(hd.getMaDon().substring(2));
                    if (idNum > maxId) maxId = idNum;
                } catch (NumberFormatException e) {
                    // Bo qua neu ID khong dung dinh dang
                }
            }
        }

        return "OD" + String.format("%03d", maxId + 1);
    }

    public boolean rong() {
        return dsGioHang.isEmpty();
    }

    public void xoaGioHang() {
        dsGioHang.clear();
    }
}

// ===== NHOM DON HANG =====
class HoaDon {
    private String maDon;
    private String maKhachHang;
    private String ngayDat;
    private String trangThai;
    private int  tongTien;
    private ArrayList<ChiTietHoaDon> chiTiet;

    public HoaDon(String maDon, String maKhachHang, String ngayDat, String trangThai, int  tongTien, ArrayList<ChiTietHoaDon> chiTiet) {
        this.maDon = maDon;
        this.maKhachHang = maKhachHang;
        this.ngayDat = ngayDat;
        this.trangThai = trangThai;
        this.tongTien = tongTien;
        this.chiTiet = chiTiet;
    }

    // GETTERS
    public String getMaDon() { return maDon; }
    public String getMaKhachHang() { return maKhachHang; }
    public String getNgayDat() { return ngayDat; }
    public String getTrangThai() { return trangThai; }
    public int  getTongTien() { return tongTien; }
    public ArrayList<ChiTietHoaDon> getChiTiet() { return chiTiet; }

    // SETTERS
    public void setMaDon(String maDon) { this.maDon = maDon; }
    public void setMaKhachHang(String maKhachHang) { this.maKhachHang = maKhachHang; }
    public void setNgayDat(String ngayDat) { this.ngayDat = ngayDat; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public void setTongTien(int  tongTien) { this.tongTien = tongTien; }
    public void setChiTiet(ArrayList<ChiTietHoaDon> chiTiet) { this.chiTiet = chiTiet; }

    public void hienThiThongTin() {
        System.out.println("\n====================================");
        System.out.println("MA DON: " + maDon);
        System.out.println("MA KH: " + maKhachHang);
        System.out.println("NGAY DAT: " + ngayDat);
        System.out.println("TRANG THAI: " + trangThai);
        System.out.println("TONG TIEN: " + String.format("%,d", tongTien) + " VND");
        System.out.println("CHI TIET DON HANG:");
        System.out.printf("%-8s | %-20s | %8s | %12s\n", "Ma mon", "Ten mon", "So luong", "Thanh tien");
        System.out.println("--------------------------------------------------------");
        for (ChiTietHoaDon cthd : chiTiet) {
            System.out.printf("%-8s | %-20s | %8d | %12s\n",
                    cthd.getMaMon(), cthd.getTenMon(), cthd.getSoLuong(),
                    String.format("%,d", cthd.getThanhTien()) + " VND");
        }
        System.out.println("====================================");
    }
}

class DSHoaDon implements ChucNang {
    private ArrayList<HoaDon> dsHoaDon = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);
    private DSMonAn dsMonAn;

    public DSHoaDon() {
        this.dsMonAn = new DSMonAn();
    }

    public DSHoaDon(DSMonAn dsMonAn) {
        this.dsMonAn = dsMonAn;
    }
    public ArrayList<HoaDon> getDSHoaDon() { return dsHoaDon; }

    // Them phuong thuc de them don hang
    public void themHoaDon(HoaDon hoaDon) {
        dsHoaDon.add(hoaDon);
    }

    @Override
    public void hienThiTatCa() {
        System.out.println("\n===== DANH SACH HOA DON =====");
        if (dsHoaDon.isEmpty()) {
            System.out.println("Chua co hoa don nao.");
            return;
        }
        for (HoaDon hd : dsHoaDon) {
            hd.hienThiThongTin();
        }
    }

    @Override
    public void them() {
        System.out.println("\n===== THEM HOA DON MOI =====");

        // Tao ma don tu dong
        String maDonMoi = taoMaDonTuDong();

        System.out.println("Ma don: " + maDonMoi);
        System.out.print("Nhap ma khach hang: ");
        String maKhachHang = sc.nextLine();
        System.out.print("Nhap ngay dat (dd/MM/yyyy): ");
        String ngayDat = sc.nextLine();
        System.out.print("Nhap trang thai: ");
        String trangThai = sc.nextLine();

        // Tao chi tiet don hang
        ArrayList<ChiTietHoaDon> chiTiet = new ArrayList<>();
        int  tongTien = 0;

        System.out.println("Nhap chi tiet don hang (nhap ma mon = '0' de ket thuc):");
        while (true) {
            System.out.print("Nhap ma mon: ");
            String maMon = sc.nextLine();
            if (maMon.equals("0")) break;

            System.out.print("Nhap so luong: ");
            int soLuong = sc.nextInt();
            sc.nextLine(); // clear buffer

            System.out.print("Nhap don gia: ");
            int donGia = sc.nextInt();
            sc.nextLine(); // clear buffer

            System.out.print("Nhap ten mon: ");
            String tenMon = sc.nextLine();

            ChiTietHoaDon cthd = new ChiTietHoaDon(maMon, tenMon, soLuong, donGia);
            chiTiet.add(cthd);
            tongTien += cthd.getThanhTien();
        }

        HoaDon hoaDonMoi = new HoaDon(maDonMoi, maKhachHang, ngayDat, trangThai, tongTien, chiTiet);
        dsHoaDon.add(hoaDonMoi);
        System.out.println("Them hoa don thanh cong!");
    }

    private String taoMaDonTuDong() {
        int maxId = 0;

        // Doc tu file de dam bao co du lieu moi nhat
        ArrayList<String> lines = QuanLyFile.docFile("orders.txt");
        for (String line : lines) {
            String[] data = line.split("\\|");
            if (data.length >= 1 && data[0].startsWith("OD")) {
                try {
                    int idNum = Integer.parseInt(data[0].substring(2));
                    if (idNum > maxId) maxId = idNum;
                } catch (NumberFormatException e) {
                    // Bo qua neu ID khong dung dinh dang
                }
            }
        }

        String newId = "OD" + String.format("%03d", maxId + 1);
        return newId;
    }

    @Override
    public void sua() {
        System.out.print("Nhap ma don can cap nhat: ");
        String maDon = sc.nextLine();
        HoaDon hoaDon = null;
        for (HoaDon hd : dsHoaDon) {
            if (hd.getMaDon().equalsIgnoreCase(maDon)){
                hoaDon = hd;
                break;
            }
        }
        if (hoaDon == null) {
            System.out.println("Khong tim thay hoa don nay");
            return;
        }

        int luaChon;
        do {
            System.out.println("\n==== CAP NHAT THONG TIN HOA DON ====");
            System.out.println("1. Doi trang thai: " + hoaDon.getTrangThai());
            System.out.println("2. Cap nhat chi tiet don hang");
            System.out.println("0. Thoat");
            System.out.print("\nChon thong tin can chinh sua: ");
            luaChon = Integer.parseInt(sc.nextLine());

            switch (luaChon) {
                case 1:
                    System.out.print("Nhap trang thai moi: ");
                    hoaDon.setTrangThai(sc.nextLine());
                    System.out.println("Cap nhat trang thai thanh cong!");
                    break;
                case 2:
                    capNhatChiTietHoaDon(hoaDon);
                    break;
                case 0:
                    System.out.println("Thoat cap nhat.");
                    break;
                default:
                    System.out.println("Lua chon khong hop le!");
                    break;
            }
        } while (luaChon != 0);
    }

    private void capNhatChiTietHoaDon(HoaDon hoaDon) {
        System.out.println("\n==== CAP NHAT CHI TIET DON HANG ====");
        ArrayList<ChiTietHoaDon> chiTiet = hoaDon.getChiTiet();

        // Hien thi chi tiet hien tai
        System.out.println("Chi tiet hien tai:");
        for (int i = 0; i < chiTiet.size(); i++) {
            ChiTietHoaDon cthd = chiTiet.get(i);
            System.out.printf("%d. %s - %s - %d - %s\n", i+1, cthd.getMaMon(), cthd.getTenMon(), cthd.getSoLuong(),
                    String.format("%,d", cthd.getThanhTien()) + " VND");
        }

        System.out.print("Chon so thu tu mon an can cap nhat (0 de them moi): ");
        int index = sc.nextInt();
        sc.nextLine(); // clear buffer

        if (index == 0) {
            // Them mon an moi
            System.out.print("Nhap ma mon: ");
            String maMon = sc.nextLine();
            System.out.print("Nhap ten mon: ");
            String tenMon = sc.nextLine();
            System.out.print("Nhap so luong: ");
            int soLuong = sc.nextInt();
            sc.nextLine(); // clear buffer
            System.out.print("Nhap don gia: ");
            int donGia = sc.nextInt();
            sc.nextLine(); // clear buffer

            ChiTietHoaDon cthdMoi = new ChiTietHoaDon(maMon, tenMon, soLuong, donGia);
            chiTiet.add(cthdMoi);
            System.out.println("Them mon an thanh cong!");

        } else if (index > 0 && index <= chiTiet.size()) {
            // Cap nhat mon an cu
            ChiTietHoaDon cthd = chiTiet.get(index-1);
            System.out.print("Nhap so luong moi: ");
            int soLuongMoi = sc.nextInt();
            sc.nextLine(); // clear buffer
            cthd.setSoLuong(soLuongMoi);
            System.out.println("Cap nhat so luong thanh cong!");

        } else {
            System.out.println("So thu tu khong hop le!");
            return;
        }

        // Tinh lai tong tien
        int  tongTienMoi = 0;
        for (ChiTietHoaDon cthd : chiTiet) {
            tongTienMoi += cthd.getThanhTien();
        }
        hoaDon.setTongTien(tongTienMoi);
        System.out.println("Da cap nhat tong tien: " + String.format("%,d", tongTienMoi) + " VND");
    }

    @Override
    public void xoa() {
        System.out.println("\n===== XOA HOA DON =====");
        System.out.print("Nhap ma don can xoa: ");
        String maDon = sc.nextLine();

        HoaDon hoaDon = null;
        for (HoaDon hd : dsHoaDon) {
            if (hd.getMaDon().equalsIgnoreCase(maDon)) {
                hoaDon = hd;
                break;
            }
        }
        if (hoaDon == null) {
            System.out.println("Khong tim thay hoa don co ma nay!");
            return;
        }

        dsHoaDon.remove(hoaDon);
        System.out.println("Xoa hoa don thanh cong!");
    }

    @Override
    public void timKiem() {
        System.out.println("\n===== TIM KIEM HOA DON =====");
        System.out.print("Nhap ma don hoac ma khach hang can tim: ");
        String tuKhoa = sc.nextLine();

        boolean timThay = false;
        for (HoaDon hd : dsHoaDon) {
            if (hd.getMaDon().equalsIgnoreCase(tuKhoa) || hd.getMaKhachHang().equalsIgnoreCase(tuKhoa)) {
                hd.hienThiThongTin();
                timThay = true;
            }
        }

        if (!timThay) {
            System.out.println("Khong tim thay hoa don nao phu hop!");
        }
    }

    // Hien thi don hang cua khach hang cu the
    public void hienThiHoaDonKhachHang(String maKhachHang) {
        boolean timThay = false;
        System.out.println("\n===== DON HANG CUA BAN =====");
        for (HoaDon hd : dsHoaDon) {
            if (hd.getMaKhachHang().equals(maKhachHang)) {
                hd.hienThiThongTin();
                timThay = true;
            }
        }

        if (!timThay) {
            System.out.println("Ban chua co don hang nao.");
        }
    }

    // Doc du lieu tu file
    public void docFile(String filename) {
        ArrayList<String> lines = QuanLyFile.docFile(filename);
        dsHoaDon.clear();

        for (String line : lines) {
            String[] data = line.split("\\|");
            if (data.length >= 5) {
                try {
                    String maDon = data[0];
                    String maKhachHang = data[1];
                    int tongTien = Integer.parseInt(data[2]);
                    String ngayDat = data[3];
                    String trangThai = data[4];

                    // Tao chi tiet don hang
                    ArrayList<ChiTietHoaDon> chiTiet = new ArrayList<>();
                    if (data.length > 5) {
                        String[] chiTietData = data[5].split(";");
                        for (String ct : chiTietData) {
                            String[] itemData = ct.split(":");
                            if (itemData.length >= 2) {
                                String maMon = itemData[0];
                                int soLuong = Integer.parseInt(itemData[1]);

                                // Dung dsMonAn da duoc truyen vao
                                MonAn monAn = dsMonAn.timMonAnTheoMa(maMon);
                                String tenMon = (monAn != null) ? monAn.getTenMon() : "Unknown";
                                int donGia = (monAn != null) ? monAn.getGia() : 0;

                                chiTiet.add(new ChiTietHoaDon(maMon, tenMon, soLuong, donGia));
                            }
                        }
                    }

                    HoaDon hoaDon = new HoaDon(maDon, maKhachHang, ngayDat, trangThai, tongTien, chiTiet);
                    dsHoaDon.add(hoaDon);

                } catch (NumberFormatException e) {
                    System.out.println("Loi dinh dang so cho don hang: " + data[0]);
                }
            }
        }
    }

    // Ghi du lieu vao file
    public void ghiFile(String filename) {
        ArrayList<String> oldLines = QuanLyFile.docFile(filename);
        ArrayList<String> newlines = new ArrayList<>();
        for (HoaDon hd : dsHoaDon) {
            // Chuyen chi tiet don hang thanh chuoi
            StringBuilder chiTietStr = new StringBuilder();
            for (ChiTietHoaDon cthd : hd.getChiTiet()) {
                if (chiTietStr.length() > 0) chiTietStr.append(";");
                chiTietStr.append(String.format("%s:%d", cthd.getMaMon(), cthd.getSoLuong()));
            }

            newlines.add(String.format("%s|%s|%d|%s|%s|%s",
                    hd.getMaDon(), hd.getMaKhachHang(), hd.getTongTien(), hd.getNgayDat(),
                    hd.getTrangThai(), chiTietStr.toString()));
        }
        QuanLyFile.ghiFile(filename, newlines);
    }
}

// ===== NHOM THANH TOAN =====
class ThanhToan {
    private String maGiaoDich;
    private String maDon;
    private String ngayGiaoDich;
    private int soTien;
    private String phuongThuc;
    private String trangThai;
    private String maKhachHang;

    public ThanhToan(String maGiaoDich, String maDon, String ngayGiaoDich, int soTien, String phuongThuc, String trangThai, String maKhachHang) {
        this.maGiaoDich = maGiaoDich;
        this.maDon = maDon;
        this.ngayGiaoDich = ngayGiaoDich;
        this.soTien = soTien;
        this.phuongThuc = phuongThuc;
        this.trangThai = trangThai;
        this.maKhachHang = maKhachHang;
    }

    // GETTERS
    public String getMaGiaoDich() { return maGiaoDich; }
    public String getMaDon() { return maDon; }
    public String getNgayGiaoDich() { return ngayGiaoDich; }
    public int  getSoTien() { return soTien; }
    public String getPhuongThuc() { return phuongThuc; }
    public String getTrangThai() { return trangThai; }
    public String getMaKhachHang() { return maKhachHang; }

    // SETTERS
    public void setMaGiaoDich(String maGiaoDich) { this.maGiaoDich = maGiaoDich; }
    public void setMaDon(String maDon) { this.maDon = maDon; }
    public void setNgayGiaoDich(String ngayGiaoDich) { this.ngayGiaoDich = ngayGiaoDich; }
    public void setSoTien(int soTien) { this.soTien = soTien; }
    public void setPhuongThuc(String phuongThuc) { this.phuongThuc = phuongThuc; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
    public void setMaKhachHang(String maKhachHang) { this.maKhachHang = maKhachHang; }

    public void hienThiThongTin() {
        System.out.printf("%-10s | %-10s | %-12s | %12s | %-15s | %-15s | %-10s\n",
                maGiaoDich, maDon, ngayGiaoDich, String.format("%,d", soTien) + " VND", phuongThuc, trangThai, maKhachHang);
    }
}

class DSThanhToan implements ChucNang {
    private ArrayList<ThanhToan> dsThanhToan = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    public ArrayList<ThanhToan> getDSThanhToan() { return dsThanhToan; }

    @Override
    public void hienThiTatCa() {
        System.out.println("\n===== DANH SACH GIAO DICH =====");
        if (dsThanhToan.isEmpty()) {
            System.out.println("Chua co giao dich nao.");
            return;
        }
        System.out.printf("%-10s | %-10s | %-12s | %12s | %-15s | %-15s | %-10s\n", "Ma GD", "Ma don", "Ngay GD", "So tien", "Phuong thuc", "Trang thai", "Ma KH");
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
        for (ThanhToan tt : dsThanhToan) {
            tt.hienThiThongTin();
        }
    }

    @Override
    public void them() {
        System.out.println("\n===== THEM GIAO DICH MOI =====");

        // Tao ma giao dich tu dong
        String maGiaoDichMoi = taoMaGiaoDichTuDong();

        System.out.println("Ma giao dich: " + maGiaoDichMoi);
        System.out.print("Nhap ma don: ");
        String maDon = sc.nextLine();
        System.out.print("Nhap ngay giao dich (dd/MM/yyyy): ");
        String ngayGiaoDich = sc.nextLine();
        System.out.print("Nhap so tien: ");
        int soTien = sc.nextInt();
        sc.nextLine(); // clear buffer
        System.out.print("Nhap phuong thuc: ");
        String phuongThuc = sc.nextLine();
        System.out.print("Nhap trang thai: ");
        String trangThai = sc.nextLine();
        System.out.print("Nhap ma khach hang: ");
        String maKhachHang = sc.nextLine();

        ThanhToan thanhToanMoi = new ThanhToan(maGiaoDichMoi, maDon, ngayGiaoDich, soTien, phuongThuc, trangThai, maKhachHang);
        dsThanhToan.add(thanhToanMoi);
        System.out.println("Them giao dich thanh cong!");
    }

    // Them giao dich tu don hang
    public void themThanhToanTuHoaDon(HoaDon hoaDon, String phuongThuc) {
        String maGiaoDichMoi = taoMaGiaoDichTuDong();
        String ngayHienTai = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        ThanhToan thanhToanMoi = new ThanhToan(maGiaoDichMoi, hoaDon.getMaDon(), ngayHienTai,
                hoaDon.getTongTien(), phuongThuc, "Cho thanh toan", hoaDon.getMaKhachHang());
        dsThanhToan.add(thanhToanMoi);
    }

    private String taoMaGiaoDichTuDong() {
        int maxId = 0;

        // Doc tu file de dam bao co du lieu moi nhat
        ArrayList<String> lines = QuanLyFile.docFile("payments.txt");
        for (String line : lines) {
            String[] data = line.split("\\|");
            if (data.length >= 1 && data[0].startsWith("P")) {
                try {
                    int idNum = Integer.parseInt(data[0].substring(1));
                    if (idNum > maxId) maxId = idNum;
                } catch (NumberFormatException e) {
                    // Bo qua neu ID khong dung dinh dang
                }
            }
        }

        String newId = "P" + String.format("%03d", maxId + 1);
        return newId;
    }

    @Override
    public void sua() {
        // Duoc trien khai trong capNhatTrangThai()
    }

    public void capNhatTrangThai() {
        System.out.println("\n===== CAP NHAT TRANG THAI THANH TOAN =====");
        System.out.print("Nhap ma giao dich can cap nhat: ");
        String maGiaoDich = sc.nextLine();

        ThanhToan thanhToan = null;
        for (ThanhToan tt : dsThanhToan) {
            if (tt.getMaGiaoDich().equalsIgnoreCase(maGiaoDich)) {
                thanhToan = tt;
                break;
            }
        }
        if (thanhToan == null) {
            System.out.println("Khong tim thay giao dich nay!");
            return;
        }

        System.out.println("Trang thai hien tai: " + thanhToan.getTrangThai());
        System.out.print("Nhap trang thai moi: ");
        String trangThaiMoi = sc.nextLine();

        thanhToan.setTrangThai(trangThaiMoi);
        System.out.println("Cap nhat trang thai thanh cong!");
    }

    @Override
    public void xoa() {
        System.out.println("\n===== XOA GIAO DICH =====");
        System.out.print("Nhap ma giao dich can xoa: ");
        String maGiaoDich = sc.nextLine();

        ThanhToan thanhToan = null;
        for (ThanhToan tt : dsThanhToan) {
            if (tt.getMaGiaoDich().equalsIgnoreCase(maGiaoDich)) {
                thanhToan = tt;
                break;
            }
        }
        if (thanhToan == null) {
            System.out.println("Khong tim thay giao dich co ma nay!");
            return;
        }

        dsThanhToan.remove(thanhToan);
        System.out.println("Xoa giao dich thanh cong!");
    }

    @Override
    public void timKiem() {
        System.out.println("\n===== TIM KIEM GIAO DICH =====");
        System.out.print("Nhap ma giao dich hoac ma don can tim: ");
        String tuKhoa = sc.nextLine();

        boolean timThay = false;
        System.out.println("\nKET QUA TIM KIEM:");
        System.out.printf("%-10s | %-10s | %-12s | %12s | %-15s | %-15s | %-10s\n", "Ma GD", "Ma don", "Ngay GD", "So tien", "Phuong thuc", "Trang thai", "Ma KH");
        System.out.println("-------------------------------------------------------------------------------------------------------------------");
        for (ThanhToan tt : dsThanhToan) {
            if (tt.getMaGiaoDich().equalsIgnoreCase(tuKhoa) || tt.getMaDon().equalsIgnoreCase(tuKhoa)) {
                tt.hienThiThongTin();
                timThay = true;
            }
        }

        if (!timThay) {
            System.out.println("Khong tim thay giao dich nao phu hop!");
        }
    }

    // Tim kiem theo khoang thoi gian
    public void timKiemTheoKhoangThoiGian() {
        System.out.println("\n===== TIM KIEM THEO KHOANG THOI GIAN =====");
        System.out.print("Nhap ngay bat dau (dd/MM/yyyy): ");
        String ngayBatDau = sc.nextLine();
        System.out.print("Nhap ngay ket thuc (dd/MM/yyyy): ");
        String ngayKetThuc = sc.nextLine();

        boolean timThay = false;
        System.out.println("\nKET QUA TIM KIEM:");
        System.out.printf("%-10s | %-10s | %-12s | %12s | %-15s | %-15s | %-10s\n", "Ma GD", "Ma don", "Ngay GD", "So tien", "Phuong thuc", "Trang thai", "Ma KH");
        System.out.println("-------------------------------------------------------------------------------------------------------------------");

        for (ThanhToan tt : dsThanhToan) {
            String ngayGiaoDich = tt.getNgayGiaoDich();
            if (ngayGiaoDich.compareTo(ngayBatDau) >= 0 && ngayGiaoDich.compareTo(ngayKetThuc) <= 0) {
                tt.hienThiThongTin();
                timThay = true;
            }
        }

        if (!timThay) {
            System.out.println("Khong tim thay giao dich nao trong khoang thoi gian nay!");
        }
    }

    // Thong ke doanh thu
    public void hienThiThongKe() {
        System.out.println("\n===== THONG KE DOANH THU =====");

        if (dsThanhToan.isEmpty()) {
            System.out.println("Chua co giao dich nao.");
            return;
        }

        int tongDoanhThu = 0;
        int soGiaoDich = 0;

        for (ThanhToan tt : dsThanhToan) {
            if ("Thanh cong".equalsIgnoreCase(tt.getTrangThai())) {
                tongDoanhThu += tt.getSoTien();
            }
            soGiaoDich++;
        }

        System.out.println("Tong so giao dich: " + soGiaoDich);
        System.out.println("Tong doanh thu: " + String.format("%,d", tongDoanhThu) + " VND");
    }

    // Doc du lieu tu file
    public void docFile(String filename) {
        ArrayList<String> lines = QuanLyFile.docFile(filename);
        dsThanhToan.clear();

        for (String line : lines) {
            String[] data = line.split("\\|");
            if (data.length >= 6) {
                try {
                    String maGiaoDich = data[0];
                    String maDon = data[1];
                    int soTien = Integer.parseInt(data[2]);
                    String ngayGiaoDich = data[3];
                    String phuongThuc = data[4];
                    String trangThai = data[5];
                    String maKhachHang = data.length > 6 ? data[6] : "";

                    ThanhToan thanhToan = new ThanhToan(maGiaoDich, maDon, ngayGiaoDich, soTien, phuongThuc, trangThai, maKhachHang);
                    dsThanhToan.add(thanhToan);

                } catch (NumberFormatException e) {
                    System.out.println("Loi dinh dang so cho giao dich: " + data[0]);
                }
            }
        }
        System.out.println("Doc du lieu thanh toan thanh cong! " + dsThanhToan.size() + " giao dich");
    }

    // Ghi du lieu vao file
    public void ghiFile(String filename) {
        ArrayList<String> oldLines = QuanLyFile.docFile(filename);
        ArrayList<String> newlines = new ArrayList<>();
        for (ThanhToan tt : dsThanhToan) {
            newlines.add(String.format("%s|%s|%d|%s|%s|%s|%s",
                    tt.getMaGiaoDich(), tt.getMaDon(), tt.getSoTien(), tt.getNgayGiaoDich(),
                    tt.getPhuongThuc(), tt.getTrangThai(), tt.getMaKhachHang()));
        }
        QuanLyFile.ghiFile(filename, newlines);
    }
}