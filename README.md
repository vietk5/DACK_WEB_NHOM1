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
### **2. QUẢN LÝ SẢN PHẨM**
+	Hiển thị danh sách sản phẩm (Laptop, PC, Phụ kiện, Màn hình)
+	Tìm kiếm và lọc sản phẩm theo loại, thương hiệu
+	Chi tiết sản phẩm
+	Autocomplete khi tìm kiếm
### **3. GIỎ HÀNG & THANH TOÁN**
+	Thêm/xóa/cập nhật giỏ hàng (lưu trong Session)
+	Checkout và tạo đơn hàng
+	Nhiều phương thức thanh toán
+	Áp dụng phiếu giảm giá
### **4. QUẢN LÝ ĐƠN HÀNG**
+	Xem lịch sử đơn hàng
+	Theo dõi trạng thái đơn (MỚI, ĐANG XỬ LÝ, ĐANG GIAO, HOÀN TẤT, HỦY, TRẢ HÀNG, ĐÃ THANH TOÁN)
+	Xác nhận nhận hàng
### **5. ADMIN PANEL**
+	Dashboard (thống kê tổng quan)
+	Quản lý sản phẩm (CRUD)
+	Quản lý đơn hàng
+	Quản lý khách hàng
+	Báo cáo doanh thu

---

## 📦 **CHI TIẾT CÁC PACKAGE**
## **1. CONTROLLER PACKAGE (com.demo.controller)**
### **Chức năng người dùng:**
<img width="784" height="528" alt="image" src="https://github.com/user-attachments/assets/dea2d0cd-27d3-4342-80fd-bcfc1e5a733d" />

### **Chức năng mua hàng:**
<img width="763" height="344" alt="image" src="https://github.com/user-attachments/assets/ff40aab2-f064-4847-b1bc-ad7e01f1ff3e" />

### **Admin Panel:**
<img width="771" height="342" alt="image" src="https://github.com/user-attachments/assets/62ccff8d-4da1-493a-be51-6729afa12125" />

## **2. MODEL PACKAGE (com.demo.model)**
### **Entity chính:**
<img width="625" height="698" alt="image" src="https://github.com/user-attachments/assets/e21baa4b-bd75-47ea-adfa-a070e8af4226" />

### **Order (Đơn hàng):**
<img width="784" height="225" alt="image" src="https://github.com/user-attachments/assets/ddd24626-5a0d-46db-ac35-472282800339" />

### **Cart (Giỏ hàng):**
<img width="726" height="132" alt="image" src="https://github.com/user-attachments/assets/e2ba5723-f017-4305-af16-7e3cf31226c2" />

### **Warehouse (Kho):**
<img width="655" height="217" alt="image" src="https://github.com/user-attachments/assets/d83423e2-df68-495d-93a7-4c8991361c36" />

### **Customer Support:**
<img width="634" height="132" alt="image" src="https://github.com/user-attachments/assets/a71d8921-630e-44f3-9e2c-2ef47a2d3338" />

### **Helper Models:**
<img width="520" height="133" alt="image" src="https://github.com/user-attachments/assets/a61288d2-d384-465f-8db9-d7200453a594" />

### **Database Helper (deprecated):**
<img width="585" height="172" alt="image" src="https://github.com/user-attachments/assets/9c135d44-0ffd-42d6-be74-8c6faf747f6e" />


## **3. PERSISTENCE PACKAGE (com.demo.persistence)**
**Pattern: Generic DAO với JPA**
<img width="784" height="368" alt="image" src="https://github.com/user-attachments/assets/3fcbff99-03ae-4e6a-9de6-11c9f4d967f5" />

**Tính năng GenericDAO:**
+	CRUD: find(), findAll(), save(), update(), delete()
+	Pagination: findAll(page, size, orderBy, asc)
+	Dynamic query: findWhere(whereClause, params)
+	Transaction: inTransaction(), inTransactionVoid()
+	Count: count()

## **4. ENUMS PACKAGE (com.demo.enums)**
<img width="787" height="244" alt="image" src="https://github.com/user-attachments/assets/cdb9a1f8-9b75-409d-9930-6e8f02e21005" />


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
