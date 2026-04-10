//package com.demo.controller;
//
//import com.demo.model.Admin;
//import com.demo.model.KhachHang;
//import com.demo.model.session.SessionUser;
//import com.demo.persistence.AdminDAO;
//import com.demo.persistence.KhachHangDAO;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.*;
//import java.io.IOException;
//import java.util.Optional;
//
//@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
//public class LoginServlet extends HttpServlet {
//
//    private final AdminDAO adminDAO = new AdminDAO();
//    private final KhachHangDAO khDAO = new KhachHangDAO();
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//        // Tạo admin mặc định nếu chưa có
//        adminDAO.ensureDefaultAdmin();
//        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//
//        req.setCharacterEncoding("UTF-8");
//        String account  = req.getParameter("account");   // username hoặc email
//        String password = req.getParameter("password");
//
//        // 1) Thử admin trước
//        Optional<Admin> aOpt = adminDAO.findByAccount(account);
//        if (aOpt.isPresent() && password.equals(aOpt.get().getMatKhau())) {
//            Admin a = aOpt.get();
//            SessionUser su = new SessionUser(
//                    a.getIdAdmin(),
//                    a.getTen() != null ? a.getTen() : a.getTaiKhoan(),
//                    a.getEmail(),
//                    true
//            );
//            HttpSession ss = req.getSession(true);
//            ss.setAttribute("user", su);       // header của bạn đang dùng sessionScope.user.fullName
//            ss.setAttribute("IS_ADMIN", true);
//            resp.sendRedirect(req.getContextPath() + "/admin");
//            return;
//        }
//
//        // 2) Khách
//        Optional<KhachHang> kOpt = khDAO.findByEmailAndPassword(account, password);
//        if (kOpt.isPresent()) {
//            KhachHang k = kOpt.get();
//            String name = (k.getTen() != null && !k.getTen().isBlank())
//                        ? k.getTen()
//                        : (k.getHoTen() != null ? k.getHoTen() : k.getEmail());
//
//            SessionUser su = new SessionUser(k.getId(), name, k.getEmail(), false);
//            HttpSession ss = req.getSession(true);
//            ss.setAttribute("user", su);
//            ss.setAttribute("IS_ADMIN", false);
//            resp.sendRedirect(req.getContextPath() + "/home");
//            return;
//        }
//
//        // 3) Sai thông tin
//        req.setAttribute("error", "Sai tài khoản hoặc mật khẩu");
//        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
//    }
//}


package com.demo.controller;

import com.demo.model.Admin;
import com.demo.model.KhachHang;
import com.demo.model.cart.GioHang;
import com.demo.model.cart.GioHangItem;
import com.demo.model.session.SessionUser;
import com.demo.persistence.AdminDAO;
import com.demo.persistence.GioHangDAO;
import com.demo.persistence.KhachHangDAO;
import com.demo.util.AccountLockoutManager;
import com.demo.util.PasswordUtil;
import com.demo.util.SecurityLogger;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final AdminDAO adminDAO = new AdminDAO();
    private final KhachHangDAO khDAO = new KhachHangDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Tạo admin mặc định nếu chưa có
        adminDAO.ensureDefaultAdmin();
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String account = req.getParameter("account");   // username hoặc email
        String password = req.getParameter("password");
        String clientIP = req.getRemoteAddr();

        // 🔒 Security Enhancement - Vũ Văn Thông
        // Fix A06: Check account lockout
        if (AccountLockoutManager.isAccountLocked(account)) {
            long remainingMinutes = AccountLockoutManager.getRemainingLockoutMinutes(account);
            SecurityLogger.logAccountLockout(account, clientIP);
            req.setAttribute("error", "Tài khoản đã bị khóa do đăng nhập sai quá nhiều lần. Vui lòng thử lại sau " + remainingMinutes + " phút.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        // 1) Thử admin trước
        Optional<Admin> aOpt = adminDAO.findByAccount(account);
        if (aOpt.isPresent()) {
            Admin a = aOpt.get();
            
            // 🔒 Fix A07: BCrypt password verification (with fallback for backward compatibility)
            boolean passwordMatch = false;
            try {
                passwordMatch = PasswordUtil.verifyPassword(password, a.getMatKhau());
            } catch (Exception e) {
                // Fallback to plaintext comparison for backward compatibility
                passwordMatch = password.equals(a.getMatKhau());
            }
            
            if (passwordMatch) {
                // 🔒 Fix A06: Reset failed attempts on successful login
                AccountLockoutManager.resetAttempts(account);
                
                // 🔒 Fix A09: Log successful login
                SecurityLogger.logLoginSuccess(account, clientIP);
                
                SessionUser su = new SessionUser(
                        a.getIdAdmin(),
                        a.getTen() != null ? a.getTen() : a.getTaiKhoan(),
                        a.getEmail(),
                        true
                );
                HttpSession ss = req.getSession(true);
                ss.setAttribute("user", su);
                ss.setAttribute("IS_ADMIN", true);
                resp.sendRedirect(req.getContextPath() + "/admin");
                return;
            }
        }

        // 2) Khách - 🔒 Fix A07: Use BCrypt verification instead of findByEmailAndPassword
        Optional<KhachHang> kOpt = khDAO.findByEmail(account);
        if (kOpt.isPresent()) {
            KhachHang k = kOpt.get();
            
            // 🔒 Fix A07: BCrypt password verification (with fallback)
            boolean passwordMatch = false;
            try {
                passwordMatch = PasswordUtil.verifyPassword(password, k.getMatKhau());
            } catch (Exception e) {
                // Fallback to plaintext comparison for backward compatibility
                passwordMatch = password.equals(k.getMatKhau());
            }
            
            if (passwordMatch) {
                // 🔒 Fix A06: Reset failed attempts on successful login
                AccountLockoutManager.resetAttempts(account);
                
                // 🔒 Fix A09: Log successful login
                SecurityLogger.logLoginSuccess(account, clientIP);
                
                String name = (k.getTen() != null && !k.getTen().isBlank())
                        ? k.getTen()
                        : (k.getHoTen() != null ? k.getHoTen() : k.getEmail());

                SessionUser su = new SessionUser(k.getId(), name, k.getEmail(), false);
                HttpSession ss = req.getSession(true);
                ss.setAttribute("user", su);
                ss.setAttribute("IS_ADMIN", false);

            // 🆕 --- BẮT ĐẦU: Gộp giỏ hàng tạm với giỏ hàng DB ---
            List<com.demo.model.cart.GioHangItem> cartSession
                    = (List<com.demo.model.cart.GioHangItem>) ss.getAttribute("cart");
            if (cartSession == null) {
                cartSession = new ArrayList<>();
            }
            GioHangDAO gioHangDAO = new GioHangDAO();
            GioHang gioHangDB = gioHangDAO.findByKhachHang(k);

            if (gioHangDB == null) {
                gioHangDB = gioHangDAO.createForUser(k);
            }

            // ✅ Nếu giỏ hàng DB đã có dữ liệu, gộp các sản phẩm session vào
            List<GioHangItem> dbItems = gioHangDB.getItems();
            if (dbItems == null) {
                dbItems = new ArrayList<>();
            }

            for (GioHangItem itemSession : cartSession) {
                boolean exists = false;
                for (GioHangItem itemDB : dbItems) {
                    if (itemDB.getSku().equals(itemSession.getSku())) {
                        itemDB.setSoLuong(itemDB.getSoLuong() + itemSession.getSoLuong());
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    dbItems.add(itemSession);
                }
            }

            // Cập nhật lại danh sách item cho giỏ hàng DB
            gioHangDB.setItems(dbItems);
            gioHangDAO.saveItems(gioHangDB, dbItems);

            // Cập nhật session
            ss.setAttribute("cart", dbItems);
            // 🆕 --- KẾT THÚC phần gộp giỏ hàng ---
            
            // 🧩 --- Nạp giỏ hàng từ DB sau khi đăng nhập ---
            List<GioHangItem> dbCart = gioHangDAO.loadCartAfterLogin(k.getId());
            if (!dbCart.isEmpty()) {
                ss.setAttribute("cart", dbCart);
                System.out.println("🛒 [DEBUG] Giỏ hàng đã được tải từ DB cho khách ID " + k.getId());
            } else {
                System.out.println("🛒 [DEBUG] Giỏ hàng DB trống, dùng giỏ session hiện tại.");
            }
// 🧩 --- Kết thúc ---
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }
        }

        // 3) Sai thông tin
        // 🔒 Fix A06: Record failed attempt
        AccountLockoutManager.recordFailedAttempt(account);
        
        // 🔒 Fix A09: Log failed login
        SecurityLogger.logLoginFailure(account, clientIP, "Invalid credentials");
        
        // 🔒 Fix A10: Generic error message (không tiết lộ thông tin)
        req.setAttribute("error", "Email hoặc mật khẩu không chính xác");
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }
}
