package com.demo.controller;

import com.demo.model.KhachHang;
import com.demo.model.session.SessionUser;
import com.demo.persistence.KhachHangDAO;
import com.demo.util.PasswordPolicy;
import com.demo.util.PasswordUtil;
import com.demo.util.SecurityLogger;
import com.demo.util.ValidationResult;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    private final KhachHangDAO khDAO = new KhachHangDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String fullName = req.getParameter("fullName");
        String email    = req.getParameter("email");
        String pass     = req.getParameter("password");
        String confirm  = req.getParameter("confirm");
        String clientIP = req.getRemoteAddr();

        if (fullName == null || email == null || pass == null
                || fullName.isBlank() || email.isBlank() || pass.isBlank()) {
            req.setAttribute("error", "Vui lòng nhập đủ thông tin.");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }
        if (!pass.equals(confirm)) {
            req.setAttribute("error", "Mật khẩu xác nhận không khớp.");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }
        
        // 🔒 Security Enhancement - Vũ Văn Thông
        // Fix A07: Validate password policy
        ValidationResult passwordValidation = PasswordPolicy.validate(pass);
        if (!passwordValidation.isValid()) {
            req.setAttribute("error", passwordValidation.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }
        
        fullName = fullName.trim();
        email = email.trim().toLowerCase();

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            req.setAttribute("error", "Email không hợp lệ.");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }
        
        if (khDAO.emailExists(email)) {
            req.setAttribute("error", "Email đã được sử dụng.");
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        KhachHang kh = new KhachHang();
        kh.setTen(fullName);
        kh.setEmail(email);
        
        // 🔒 Fix A07: Hash password with BCrypt before saving
        String hashedPassword = PasswordUtil.hashPassword(pass);
        kh.setMatKhau(hashedPassword);

        khDAO.create(kh);
        
        // 🔒 Fix A09: Log registration
        SecurityLogger.logRegistration(email, clientIP);

        // Auto login
        SessionUser su = new SessionUser(kh.getId(), kh.getTen(), kh.getEmail(), false);
        HttpSession ss = req.getSession(true);
        ss.setAttribute("user", su);
        ss.setAttribute("IS_ADMIN", false);

        resp.sendRedirect(req.getContextPath() + "/home");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }
}
