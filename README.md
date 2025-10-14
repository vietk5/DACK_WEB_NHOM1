# 📋 **DACK_WEB_NHOM1 (ElectroMart - Website bán hàng điện tử)**

## 🎯 **Tổng quan dự án**

Dự án **ELECTROMART** - Website bán hàng điện tử trực tuyến được xây dựng bằng **Java Servlet/JSP** với kiến trúc MVC.

### **🏗️Công nghệ sử dụng:**
•	**Framework:** Java Servlet/JSP (Jakarta EE 10)
•	**ORM**: JPA (Jakarta Persistence) với EclipseLink 4.0.2
•	**Database**: PostgreSQL (deployed trên Render)
• **Build Tool**: Maven
•	**Java Version**: 11
•	**Server**: Jakarta Servlet 6.0
• **UI Reference**: [phongvu.vn](https://phongvu.vn/))

---

## 📁 **CẤU TRÚC THƯ MỤC CHÍNH**
DACK_WEB_NHOM1-main/
├── src/main/java/com/demo/          # Mã nguồn Java
│   ├── controller/                   # 23 Servlet controllers
│   ├── model/                        # 31 Entity models (JPA)
│   ├── persistence/                  # 7 DAO classes
│   ├── enums/                        # 3 Enum types
│   └── repo/                         # 3 Repository classes
├── src/main/java/com/electromart/
│   └── filter/                       # 2 Filter classes
├── src/main/resources/
│   └── META-INF/persistence.xml     # Cấu hình JPA
├── src/main/webapp/                 # Giao diện web
│   ├── WEB-INF/
│   │   ├── views/                   # JSP views
│   │   └── web.xml                  # Web config
│   ├── assets/                      # CSS, JS, Images
│   └── *.jsp                        # Public JSP pages
└── pom.xml                          # Maven config

---

### **🔧 Development Setup**
```bash
# Clone repository
git clone <repository-url>
cd ute-phone-hub

# Start với Docker
docker-compose up -d

# Access application
http://localhost:8080
```
---

## 🎯 **CHỨC NĂNG CHÍNH CỦA HỆ THỐNG**
**1. QUẢN LÝ NGƯỜI DÙNG**
•	✅ Đăng ký/Đăng nhập khách hàng
• ✅	Quên mật khẩu (gửi email reset)
• ✅ Phân quyền: Admin, Nhân viên, Khách hàng
• ✅	Hệ thống hạng thành viên (BẠC, VÀNG, KIM CƯƠNG)
**2. QUẢN LÝ SẢN PHẨM**
• ✅	Hiển thị danh sách sản phẩm (Laptop, PC, Phụ kiện, Màn hình)
• ✅	Tìm kiếm và lọc sản phẩm theo loại, thương hiệu
• ✅	Chi tiết sản phẩm
•	✅ Autocomplete khi tìm kiếm
**3. GIỎ HÀNG & THANH TOÁN**
• ✅ Thêm/xóa/cập nhật giỏ hàng (lưu trong Session)
•	✅ Checkout và tạo đơn hàng
•	✅ Nhiều phương thức thanh toán
•	✅ Áp dụng phiếu giảm giá
**4. QUẢN LÝ ĐƠN HÀNG**
•	✅ Xem lịch sử đơn hàng
•	✅ Theo dõi trạng thái đơn (MỚI, ĐANG XỬ LÝ, ĐANG GIAO, HOÀN TẤT, HỦY, TRẢ HÀNG)
•	✅ Xác nhận nhận hàng
**5. ADMIN PANEL**
•	✅ Dashboard (thống kê tổng quan)
•	✅ Quản lý sản phẩm (CRUD)
•	✅ Quản lý đơn hàng
•	✅ Quản lý khách hàng
•	✅ Báo cáo doanh thu

---

## 📦 **CHI TIẾT CÁC PACKAGE**
**1. CONTROLLER PACKAGE (com.demo.controller)**
Chức năng người dùng:
File	URL Pattern	Chức năng
HomeServlet.java	/home	Trang chủ - hiển thị sản phẩm, lọc theo brand
LoginServlet.java	/login	Đăng nhập khách hàng
LogoutServlet.java	/logout	Đăng xuất
RegisterServlet.java	/register	Đăng ký tài khoản mới
ProfileServlet.java	/profile	Xem/cập nhật thông tin cá nhân
ForgotPasswordServlet.java	/forgot-password	Yêu cầu reset mật khẩu
requestPassword.java	/request-password	Gửi email reset password
resetPassword.java	/reset-password	Đặt lại mật khẩu mới
resetService.java	N/A	Service xử lý reset password
Chức năng mua hàng:
File	URL Pattern	Chức năng
ProductDetailServlet.java	/product-detail	Chi tiết sản phẩm
SearchServlet.java	/search	Tìm kiếm sản phẩm
AutocompleteServlet.java	/autocomplete	Gợi ý tìm kiếm (AJAX)
CartServlet.java	/cart	Quản lý giỏ hàng (thêm/xóa/xem)
CheckoutServlet.java	/checkout	Thanh toán đơn hàng
OrdersServlet.java	/orders	Xem lịch sử đơn hàng
ReceivingServlet.java	/receiving	Xác nhận nhận hàng
Admin Panel:
File	URL Pattern	Chức năng
AdminLoginServlet.java	/admin/login	Đăng nhập admin
AdminLogoutServlet.java	/admin/logout	Đăng xuất admin
AdminDashboardServlet.java	/admin/dashboard	Dashboard tổng quan
AdminProductsServlet.java	/admin/products	Quản lý sản phẩm (CRUD)
AdminOrdersServlet.java	/admin/orders	Quản lý đơn hàng
AdminCustomersServlet.java	/admin/customers	Quản lý khách hàng
AdminRevenueServlet.java	/admin/revenue	Báo cáo doanh thu

**2. MODEL PACKAGE (com.demo.model)**
Entity chính:
File	Bảng DB	Chức năng
NguoiDung.java	nguoi_dung	Base class cho User (kế thừa)
KhachHang.java	khach_hang	Thông tin khách hàng (extends NguoiDung)
Admin.java	admin	Thông tin admin
NhanVien.java	nhan_vien	Thông tin nhân viên
SanPham.java	san_pham	Sản phẩm (id, tên, giá, số lượng tồn, mô tả)
LoaiSanPham.java	loai_san_pham	Loại/danh mục sản phẩm
ThuongHieu.java	thuong_hieu	Thương hiệu sản phẩm
DiaChi.java	dia_chi	Địa chỉ giao hàng
PhieuGiamGia.java	phieu_giam_gia	Mã giảm giá (voucher)
PhuongThucThanhToan.java	phuong_thuc_thanh_toan	Phương thức thanh toán
PhieuThanhToan.java	phieu_thanh_toan	Phiếu thanh toán
TokenForgetPassword.java	token_forget_password	Token reset password
AuditEntity.java	N/A	Base class có createdAt/updatedAt
Order (Đơn hàng):
File	Bảng DB	Chức năng
DonHang.java	don_hang	Đơn hàng chính
ChiTietDonHang.java	chi_tiet_don_hang	Chi tiết đơn hàng (sản phẩm trong đơn)
ChiTietDonHangKey.java	N/A	Composite key cho ChiTietDonHang
Cart (Giỏ hàng):
File	Bảng DB	Chức năng
GioHang.java	gio_hang	Giỏ hàng của khách
GioHangItem.java	gio_hang_item	Item trong giỏ hàng (Session-based)
Warehouse (Kho):
File	Bảng DB	Chức năng
PhieuNhap.java	phieu_nhap	Phiếu nhập kho
ChiTietPhieuNhap.java	chi_tiet_phieu_nhap	Chi tiết phiếu nhập
PhieuXuat.java	phieu_xuat	Phiếu xuất kho
ChiTietPhieuXuat.java	chi_tiet_phieu_xuat	Chi tiết phiếu xuất
Customer Support:
File	Bảng DB	Chức năng
PhieuHoTro.java	phieu_ho_tro	Phiếu hỗ trợ khách hàng
PhieuPhanHoi.java	phieu_phan_hoi	Phản hồi hỗ trợ
Helper Models:
File	Chức năng
SessionUser.java	Lưu thông tin user trong session
NewsItem.java	Tin tức/bài viết
Database Helper (deprecated):
File	Chức năng
LoaiSanPhamDB.java	Helper lấy loại sản phẩm (code cũ)
SanPhamDB.java	Helper lấy sản phẩm (code cũ)
ThuongHieuDB.java	Helper lấy thương hiệu (code cũ)

**3. PERSISTENCE PACKAGE (com.demo.persistence)**
Pattern: Generic DAO với JPA
File	Chức năng
GenericDAO.java	Base DAO - CRUD operations, pagination, transaction
JPAUtil.java	Utility tạo EntityManager
SanPhamDAO.java	DAO cho Sản phẩm (extends GenericDAO)
KhachHangDAO.java	DAO cho Khách hàng
DonHangDAO.java	DAO cho Đơn hàng
AdminDAO.java	DAO cho Admin
TokenForgetPasswordDAO.java	DAO cho Token reset password
Tính năng GenericDAO:
•	CRUD: find(), findAll(), save(), update(), delete()
•	Pagination: findAll(page, size, orderBy, asc)
•	Dynamic query: findWhere(whereClause, params)
•	Transaction: inTransaction(), inTransactionVoid()
•	Count: count()

**4. ENUMS PACKAGE (com.demo.enums)**
File	Giá trị	Mô tả
TrangThaiDonHang.java	MOI, DANG_XU_LY, DANG_GIAO, HOAN_TAT, DA_HUY, TRA_HANG	Trạng thái đơn hàng
LoaiThanhVien.java	BAC, VANG, KIM_CUONG	Hạng thành viên
LoaiGiamGia.java	(chưa xem)	Loại giảm giá

**5. FILTER PACKAGE (com.electromart.filter)**
File	Chức năng
CharacterEncodingFilter.java	Ép UTF-8 encoding cho request/response
(1 filter khác)	Chưa xác định

---

## 🌐 **VIEW (JSP) STRUCTURE**
Public Pages:
•	index.jsp - Landing page
•	login.jsp - Login page
•	checkout.jsp - Checkout page
•	order.jsp, orders.jsp, order_detail.jsp - Order pages
•	promotions.jsp - Promotions page
Protected Views (/WEB-INF/views/)
User Views:
File	Chức năng
home.jsp	Trang chủ
login.jsp	Đăng nhập
register.jsp	Đăng ký
profile.jsp	Thông tin cá nhân
cart.jsp	Giỏ hàng
checkout.jsp	Thanh toán
orders.jsp	Lịch sử đơn hàng
product_detail.jsp	Chi tiết sản phẩm
search.jsp	Tìm kiếm
receiving.jsp	Xác nhận nhận hàng
forgot-password.jsp	Quên mật khẩu
reset-password.jsp	Reset mật khẩu
reset-forgot-password.jsp	Reset (từ email)
Admin Views (admin/)
File	Chức năng
dashboard.jsp	Dashboard admin
products.jsp	Quản lý sản phẩm
orders.jsp	Quản lý đơn hàng
customers.jsp	Quản lý khách hàng
revenue.jsp	Báo cáo doanh thu
Shared Components:
•	layout_header.jspf - Header chung
•	layout_footer.jspf - Footer chung
•	partials/product_card.jsp - Card sản phẩm
•	partials/product_card_compact.jsp - Card compact

---

## 🗄️ **DATABASE CONFIGURATION**
persistence.xml:
- Database: PostgreSQL on Render Cloud
- URL: jdbc:postgresql://dpg-d3hscdb3fgac73a2joag-a.oregon-postgres.render.com:5432/dack_web_nhom1
- Driver: org.postgresql.Driver
- JPA Provider: EclipseLink 4.0.2
- Schema Generation: UPDATE (tự động cập nhật)

---

## 🎨 **ASSETS STRUCTURE**
CSS:
•	site.css - CSS chính
•	receiving-style.css - CSS cho trang nhận hàng
JavaScript:
•	site.js - JS chính
Images:
•	products/ - Hình sản phẩm (26 files .jpg)
•	uploads/ - Upload images
•	Hero images, placeholders, logos

---

## 🔄 WORKFLOW CHÍNH
1. Quy trình mua hàng:
Home → Browse Products → Product Detail → Add to Cart 
→ View Cart → Checkout → Create Order → Payment 
→ Order Confirmation → Receiving
2. Quy trình quản lý (Admin):
Admin Login → Dashboard → Manage (Products/Orders/Customers) 
→ Update Status → View Reports
3. Xử lý đơn hàng:
MỚI → ĐANG XỬ LÝ → ĐANG GIAO → HOÀN TẤT
       ↓
     DA_HUY / TRẢ HÀNG
   
---

## 🔐**AUTHENTICATION & AUTHORIZATION**
•	Session-based authentication
•	Lưu user trong SessionUser object
•	Password: Plain text (⚠️ không hash - cần cải thiện bảo mật)
•	Token reset password có thời hạn
•	Filter kiểm tra encoding UTF-8
________________________________________
## 📊 **KEY FEATURES**
✅ Đã implement:
•	CRUD sản phẩm, đơn hàng, khách hàng
•	Shopping cart (session-based)
•	Checkout & payment
•	Order tracking
•	Search & filter
•	Admin dashboard
•	Email reset password
•	Pagination
⚠️ Cần cải thiện:
•	Bảo mật mật khẩu (hash password)
•	Input validation
•	Error handling
•	Logging system
•	Unit tests

---

## 📞 **Support & Contact**

### **Team**
- **Leader**: [Name] - [Email]
- **Member**: [Name] - [Email]
- **Member**: [Name] - [Email]
- **Member**: [Name] - [Email]
- **Member**: [Name] - [Email]
- **Member**: [Name] - [Email]
- **Member**: [Name] - [Email]
- **Member**: [Name] - [Email]
- **Member**: [Name] - [Email]

---

**📝 Note**: Đây là đồ án cuối kỳ 1/2025-2026 môn LẬP TRÌNH WEB.
