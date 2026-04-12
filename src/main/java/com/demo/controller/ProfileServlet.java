package com.demo.controller;

import com.demo.model.KhachHang;
import com.demo.model.session.SessionUser;
import com.demo.persistence.KhachHangDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    private final KhachHangDAO khDAO = new KhachHangDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // Kiểm tra đăng nhập
        SessionUser user = (SessionUser) req.getSession().getAttribute("user");
        if (user == null || user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Lấy thông tin khách hàng từ DB
        KhachHang kh = khDAO.find(user.getId());
        if (kh == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.setAttribute("khachHang", kh);
        req.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        req.setCharacterEncoding("UTF-8");
        
        // Kiểm tra đăng nhập
        SessionUser user = (SessionUser) req.getSession().getAttribute("user");
        if (user == null || user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        
        if ("update".equals(action)) {
            updateProfile(req, resp, user);
        } else if ("changePassword".equals(action)) {
            changePassword(req, resp, user);
        } else {
            doGet(req, resp);
        }
    }

    private void updateProfile(HttpServletRequest req, HttpServletResponse resp, SessionUser user)
            throws ServletException, IOException {
        
        // SỬA — thêm validation trước khi setTen()
        String fullName = req.getParameter("fullName");
        String phone    = req.getParameter("phone");

        if (fullName == null || fullName.isBlank()) {
            req.setAttribute("error", "Vui lòng nhập họ tên.");
            doGet(req, resp); return;
        }

        // Giới hạn độ dài — cột VARCHAR(255), payload XSS thường > 100 ký tự
        if (fullName.length() > 50) {
            req.setAttribute("error", "Họ tên không được vượt quá 50 ký tự.");
            doGet(req, resp); return;
        }

        // Chỉ cho phép chữ cái, khoảng trắng, dấu tiếng Việt
        if (!fullName.matches("^[\\p{L}\\s]{1,50}$")) {
            req.setAttribute("error", "Họ tên chỉ được chứa chữ cái và khoảng trắng.");
            doGet(req, resp); return;
        }

        // Validate số điện thoại — chỉ số và dấu +, -
        if (phone != null && !phone.isBlank()
                && !phone.matches("^[0-9+\\-]{7,15}$")) {
            req.setAttribute("error", "Số điện thoại không hợp lệ.");
            doGet(req, resp); return;
        }

        KhachHang kh = khDAO.find(user.getId());
        if (kh != null) {
            kh.setTen(fullName.trim());
            kh.setSdt(phone != null ? phone.trim() : null);
            khDAO.update(kh);

            // Cập nhật session
            SessionUser newUser = new SessionUser(kh.getId(), kh.getTen(), kh.getEmail(), false);
            req.getSession().setAttribute("user", newUser);

            req.setAttribute("success", "Cập nhật thông tin thành công!");
        }

        doGet(req, resp);
    }

    private void changePassword(HttpServletRequest req, HttpServletResponse resp, SessionUser user)
            throws ServletException, IOException {
        
        String currentPassword = req.getParameter("currentPassword");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        KhachHang kh = khDAO.find(user.getId());
        if (kh == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Kiểm tra mật khẩu hiện tại
        if (!currentPassword.equals(kh.getMatKhau())) {
            req.setAttribute("errorPassword", "Mật khẩu hiện tại không đúng!");
            doGet(req, resp);
            return;
        }

        // Kiểm tra mật khẩu mới
        if (newPassword == null || newPassword.length() < 6) {
            req.setAttribute("errorPassword", "Mật khẩu mới phải có ít nhất 6 ký tự!");
            doGet(req, resp);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            req.setAttribute("errorPassword", "Mật khẩu xác nhận không khớp!");
            doGet(req, resp);
            return;
        }

        // Cập nhật mật khẩu
        kh.setMatKhau(newPassword);
        khDAO.update(kh);

        req.setAttribute("successPassword", "Đổi mật khẩu thành công!");
        doGet(req, resp);
    }
}

