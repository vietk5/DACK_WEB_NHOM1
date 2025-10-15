# 📋 **DACK_WEB_NHOM1 (ElectroMart - Website bán hàng điện tử)**

## 🎯 **Tổng quan dự án**

Dự án **ELECTROMART** - Website bán hàng điện tử trực tuyến được xây dựng bằng **Java Servlet/JSP** với kiến trúc MVC.

## **🏗️Công nghệ sử dụng:**
- **Framework:** Java Servlet/JSP (Jakarta EE 10)
- **ORM**: JPA (Jakarta Persistence) với EclipseLink 4.0.2
- **Database**: PostgreSQL (deployed trên Render)
- **Build Tool**: Maven
- **Java Version**: 11
- **Server**: Jakarta Servlet 6.0
- **UI Reference**: [phongvu.vn](https://phongvu.vn/)

---

## 📁 **Cấu trúc thư mục chính**
<img width="598" height="516" alt="image" src="https://github.com/user-attachments/assets/2b8d3699-1392-427a-882c-3f340dfcaa6e" />

---

## 🎯 **CHỨC NĂNG CHÍNH CỦA HỆ THỐNG**
### **1. QUẢN LÝ NGƯỜI DÙNG**
+	Đăng ký/Đăng nhập khách hàng
+	Quên mật khẩu (gửi email reset)
+	Phân quyền: Admin, Nhân viên, Khách hàng
+	Hệ thống hạng thành viên (BẠC, VÀNG, KIM CƯƠNG)
+	Quản lý thông tin cá nhân
+	Quản lý địa chỉ giao hàng
+	Xem lịch sử mua hàng

### **2. QUẢN LÝ SẢN PHẨM**
+	Hiển thị danh sách sản phẩm (Laptop, PC, Phụ kiện, Màn hình)
+	Tìm kiếm và lọc sản phẩm theo loại, thương hiệu
+	Chi tiết sản phẩm với thông số kỹ thuật đầy đủ
+	Autocomplete khi tìm kiếm
+	So sánh sản phẩm
+	Đánh giá và bình luận sản phẩm
+	Sản phẩm đề xuất
+	Danh sách yêu thích

### **3. GIỎ HÀNG & THANH TOÁN**
+	Thêm/xóa/cập nhật giỏ hàng (lưu trong Session)
+	Checkout và tạo đơn hàng
+	Nhiều phương thức thanh toán
    - Thanh toán khi nhận hàng (COD)
    - Chuyển khoản ngân hàng
    - Ví điện tử
+	Áp dụng phiếu giảm giá
+	Tính phí vận chuyển theo địa chỉ
+	Chọn thời gian giao hàng

### **4. QUẢN LÝ ĐƠN HÀNG**
+	Xem lịch sử đơn hàng
+	Theo dõi trạng thái đơn (MỚI, ĐANG XỬ LÝ, ĐANG GIAO, HOÀN TẤT, HỦY, TRẢ HÀNG, ĐÃ THANH TOÁN)
+	Xác nhận nhận hàng
+	Yêu cầu hủy đơn
+	Yêu cầu trả hàng/hoàn tiền
+	In hóa đơn
+	Đánh giá sản phẩm sau khi nhận hàng

### **5. CHƯƠNG TRÌNH KHUYẾN MÃI**
+	Quản lý mã giảm giá
+	Flash sale theo thời gian
+	Combo giảm giá
+	Ưu đãi theo hạng thành viên
+	Tích điểm đổi quà
+	Quà tặng sinh nhật

### **6. HỖ TRỢ KHÁCH HÀNG**
+	Chatbot tự động
+	Live chat với nhân viên
+	Hệ thống ticket hỗ trợ
+	FAQ - Câu hỏi thường gặp
+	Chính sách bảo hành
+	Hướng dẫn mua hàng/thanh toán

### **7. ADMIN PANEL**
+	Dashboard (thống kê tổng quan)
+	Quản lý sản phẩm (CRUD)
    - Quản lý danh mục
    - Quản lý thương hiệu
    - Quản lý thuộc tính sản phẩm
    - Quản lý kho (nhập/xuất/tồn)
+	Quản lý đơn hàng
    - Xử lý đơn hàng mới
    - Quản lý vận chuyển
    - Xử lý đơn hủy/trả hàng
+	Quản lý khách hàng
    - Phân hạng khách hàng
    - Quản lý điểm thưởng
    - Xử lý khiếu nại
+	Quản lý khuyến mãi
    - Tạo mã giảm giá
    - Thiết lập flash sale
    - Quản lý combo
+	Báo cáo doanh thu
    - Thống kê theo thời gian
    - Thống kê theo danh mục
    - Thống kê theo khu vực
    - Báo cáo lợi nhuận

---

## 📦 **CHI TIẾT CÁC PACKAGE**

## **1. CONTROLLER PACKAGE (com.demo.controller)**
Xử lý các request từ client và điều hướng tới các service tương ứng.

### **Chức năng người dùng:**
<img width="784" height="528" alt="image" src="https://github.com/user-attachments/assets/dea2d0cd-27d3-4342-80fd-bcfc1e5a733d" />
- AuthController: Xử lý đăng nhập, đăng ký, quên mật khẩu
- UserController: Quản lý thông tin người dùng
- ProfileController: Cập nhật hồ sơ, địa chỉ

### **Chức năng mua hàng:**
<img width="763" height="344" alt="image" src="https://github.com/user-attachments/assets/ff40aab2-f064-4847-b1bc-ad7e01f1ff3e" />
- ProductController: Hiển thị và tìm kiếm sản phẩm
- CartController: Quản lý giỏ hàng
- CheckoutController: Xử lý đặt hàng
- PaymentController: Xử lý thanh toán

### **Admin Panel:**
<img width="771" height="342" alt="image" src="https://github.com/user-attachments/assets/62ccff8d-4da1-493a-be51-6729afa12125" />
- AdminController: Dashboard và thống kê
- ProductManagementController: Quản lý sản phẩm
- OrderManagementController: Quản lý đơn hàng
- UserManagementController: Quản lý người dùng

## **2. SERVICE PACKAGE (com.demo.service)**
Chứa business logic của ứng dụng.

### **Core Services:**
- UserService: Xử lý logic người dùng
- ProductService: Xử lý logic sản phẩm
- OrderService: Xử lý logic đơn hàng
- CartService: Xử lý logic giỏ hàng
- PaymentService: Xử lý logic thanh toán

### **Support Services:**
- EmailService: Gửi email
- FileService: Xử lý file
- ValidationService: Kiểm tra dữ liệu
- CacheService: Quản lý cache
- LogService: Ghi log

### **Admin Services:**
- ReportService: Tạo báo cáo
- StatisticsService: Thống kê
- InventoryService: Quản lý kho

## **3. MODEL PACKAGE (com.demo.model)**
Chứa các entity và model của ứng dụng.

### **Entity chính:**
<img width="625" height="698" alt="image" src="https://github.com/user-attachments/assets/e21baa4b-bd75-47ea-adfa-a070e8af4226" />
- User: Thông tin người dùng
- Product: Thông tin sản phẩm
- Category: Danh mục sản phẩm
- Brand: Thương hiệu

### **Order (Đơn hàng):**
<img width="784" height="225" alt="image" src="https://github.com/user-attachments/assets/ddd24626-5a0d-46db-ac35-472282800339" />
- Order: Thông tin đơn hàng
- OrderItem: Chi tiết đơn hàng
- OrderStatus: Trạng thái đơn hàng

### **Cart (Giỏ hàng):**
<img width="726" height="132" alt="image" src="https://github.com/user-attachments/assets/e2ba5723-f017-4305-af16-7e3cf31226c2" />
- Cart: Thông tin giỏ hàng
- CartItem: Sản phẩm trong giỏ

### **Warehouse (Kho):**
<img width="655" height="217" alt="image" src="https://github.com/user-attachments/assets/d83423e2-df68-495d-93a7-4c8991361c36" />
- Inventory: Tồn kho
- InventoryHistory: Lịch sử nhập xuất
- Supplier: Nhà cung cấp

### **Customer Support:**
<img width="634" height="132" alt="image" src="https://github.com/user-attachments/assets/a71d8921-630e-44f3-9e2c-2ef47a2d3338" />
- Ticket: Phiếu hỗ trợ
- ChatMessage: Tin nhắn chat
- FAQ: Câu hỏi thường gặp

### **Helper Models:**
<img width="520" height="133" alt="image" src="https://github.com/user-attachments/assets/a61288d2-d384-465f-8db9-d7200453a594" />
- DTO: Data Transfer Objects
- ViewModels: Models cho view
- Requests/Responses: Models cho API

## **4. PERSISTENCE PACKAGE (com.demo.persistence)**
Xử lý tương tác với database.

**Pattern: Generic DAO với JPA**
<img width="784" height="368" alt="image" src="https://github.com/user-attachments/assets/3fcbff99-03ae-4e6a-9de6-11c9f4d967f5" />

**Tính năng GenericDAO:**
+	CRUD: find(), findAll(), save(), update(), delete()
+	Pagination: findAll(page, size, orderBy, asc)
+	Dynamic query: findWhere(whereClause, params)
+	Transaction: inTransaction(), inTransactionVoid()
+	Count: count()

### **Repository Classes:**
- UserRepository
- ProductRepository
- OrderRepository
- CartRepository
- InventoryRepository

## **5. UTIL PACKAGE (com.demo.util)**
Chứa các utility class và helper functions.

### **Helper Classes:**
- SecurityUtil: Mã hóa và bảo mật
- DateUtil: Xử lý ngày tháng
- StringUtil: Xử lý chuỗi
- ValidationUtil: Kiểm tra dữ liệu
- FileUtil: Xử lý file

### **Constants:**
- AppConstants: Hằng số ứng dụng
- SecurityConstants: Hằng số bảo mật
- MessageConstants: Thông điệp

## **6. FILTER PACKAGE (com.electromart.filter)**
Xử lý request/response trước khi đến controller.

### **Security Filters:**
- AuthenticationFilter: Xác thực người dùng
- AuthorizationFilter: Phân quyền
- JWTFilter: Xử lý JWT

### **Other Filters:**
- LoggingFilter: Ghi log
- CORSFilter: Xử lý CORS
- EncodingFilter: Xử lý encoding

## **7. ENUMS PACKAGE (com.demo.enums)**
Chứa các enum của hệ thống.
<img width="787" height="244" alt="image" src="https://github.com/user-attachments/assets/cdb9a1f8-9b75-409d-9930-6e8f02e21005" />

### **Main Enums:**
- UserRole: ADMIN, STAFF, CUSTOMER
- OrderStatus: NEW, PROCESSING, SHIPPING, COMPLETED, etc.
- PaymentMethod: COD, BANK_TRANSFER, E_WALLET
- MembershipTier: SILVER, GOLD, DIAMOND

---

## 🚀 **Hướng dẫn cài đặt và chạy dự án**

### **Yêu cầu hệ thống**
- JDK 11 trở lên
- Apache Maven 3.8+
- PostgreSQL
- IDE hỗ trợ Java Web (khuyến nghị: NetBeans, IntelliJ IDEA, Eclipse)

### **Các bước cài đặt**

1. **Clone repository**
```bash
git clone [repository-url]
cd DACK_WEB_NHOM1
```

2. **Cấu hình database**
- Tạo database PostgreSQL mới
- Cập nhật thông tin kết nối trong `src/main/resources/META-INF/persistence.xml`

3. **Build project**
```bash
mvn clean install
```

4. **Chạy ứng dụng**
- Deploy file WAR trong thư mục `target` vào application server
- Hoặc chạy trực tiếp trong IDE với server tích hợp

5. **Truy cập ứng dụng**
- Mở trình duyệt và truy cập: `http://localhost:8080/DACK_WEB_NHOM1`

### **Tài khoản mặc định**
- **Admin**: admin@electromart.com / admin123
- **Nhân viên**: staff@electromart.com / staff123
- **Khách hàng**: customer@electromart.com / customer123

---

## 📱 **Demo & Screenshots**

### **Trang chủ**
[Thêm ảnh screenshot trang chủ]

### **Trang sản phẩm**
[Thêm ảnh screenshot trang sản phẩm]

### **Giỏ hàng & Thanh toán**
[Thêm ảnh screenshot giỏ hàng và thanh toán]

### **Admin Dashboard**
[Thêm ảnh screenshot admin dashboard]

---

## 👥 **Thành viên nhóm**
1. [Tên thành viên 1] - [MSSV] - [Vai trò]
2. [Tên thành viên 2] - [MSSV] - [Vai trò]
3. [Tên thành viên 3] - [MSSV] - [Vai trò]

---

## 📝 **Báo cáo lỗi & Đóng góp**
Nếu bạn tìm thấy lỗi hoặc có đề xuất cải tiến, vui lòng:
1. Tạo issue mới trong repository
2. Mô tả chi tiết vấn đề hoặc đề xuất
3. Đính kèm ảnh chụp màn hình nếu cần thiết

---

## 📜 **License**
© 2023 ElectroMart. All rights reserved.

## **5. FILTER PACKAGE (com.electromart.filter)**
<img width="721" height="130" alt="image" src="https://github.com/user-attachments/assets/9eb7c394-be9e-4e54-a64c-f30536c75c54" />

---

## 🌐 **VIEW (JSP) STRUCTURE**
### **Public Pages:**
+	index.jsp - Landing page
+	login.jsp - Login page
+	checkout.jsp - Checkout page
+	order.jsp, orders.jsp, order_detail.jsp - Order pages
+	promotions.jsp - Promotions page
### **Protected Views (/WEB-INF/views/)**
### **User Views:**
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
### **Admin Views (admin/)**
File	Chức năng
dashboard.jsp	Dashboard admin
products.jsp	Quản lý sản phẩm
orders.jsp	Quản lý đơn hàng
customers.jsp	Quản lý khách hàng
revenue.jsp	Báo cáo doanh thu
### **Shared Components:**
+	layout_header.jspf - Header chung
+	layout_footer.jspf - Footer chung
+	partials/product_card.jsp - Card sản phẩm
+	partials/product_card_compact.jsp - Card compact

---

## 🗄️ **DATABASE CONFIGURATION**
**persistence.xml:**
- Database: PostgreSQL on Render Cloud
- URL: jdbc:postgresql://dpg-d3hscdb3fgac73a2joag-a.oregon-postgres.render.com:5432/dack_web_nhom1
- Driver: org.postgresql.Driver
- JPA Provider: EclipseLink 4.0.2
- Schema Generation: UPDATE (tự động cập nhật)

---

## 🎨 **ASSETS STRUCTURE**
### **CSS:**
+	site.css - CSS chính
+	receiving-style.css - CSS cho trang nhận hàng
### **JavaScript:**
+	site.js - JS chính
### **Images:**
+	products/ - Hình sản phẩm (26 files .jpg)
+	uploads/ - Upload images
+	Hero images, placeholders, logos

---

## 🔄 WORKFLOW CHÍNH
### **1. Quy trình mua hàng:**
Home → Browse Products → Product Detail → Add to Cart 
→ View Cart → Checkout → Create Order → Payment 
→ Order Confirmation → Receiving
### **2. Quy trình quản lý (Admin):**
Admin Login → Dashboard → Manage (Products/Orders/Customers) 
→ Update Status → View Reports
### **3. Xử lý đơn hàng:**
MỚI → ĐANG XỬ LÝ → ĐANG GIAO → HOÀN TẤT
       ↓
     DA_HUY / TRẢ HÀNG
   
---

## 🔐**AUTHENTICATION & AUTHORIZATION**
+	Session-based authentication
+	Lưu user trong SessionUser object
+	Password: Plain text (⚠️ không hash - cần cải thiện bảo mật)
+	Token reset password có thời hạn
+	Filter kiểm tra encoding UTF-8

---

## 📊 **KEY FEATURES**
✅ Đã implement:
+	CRUD sản phẩm, đơn hàng, khách hàng
+	Shopping cart (session-based)
+	Checkout & payment
+	Order tracking
+	Search & filter
+	Admin dashboard
+	Email reset password
+	Pagination
⚠️ Cần cải thiện:
+	Bảo mật mật khẩu (hash password)
+	Input validation
+	Error handling
+	Logging system
+	Unit tests

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
