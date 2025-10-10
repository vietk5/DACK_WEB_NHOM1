package com.demo.controller;

import com.demo.model.Admin;
import com.demo.model.KhachHang;
import com.demo.model.session.SessionUser;
import com.demo.persistence.AdminDAO;
import com.demo.persistence.KhachHangDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
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
        String account  = req.getParameter("account");   // username hoặc email
        String password = req.getParameter("password");

        // 1) Thử admin trước
        Optional<Admin> aOpt = adminDAO.findByAccount(account);
        if (aOpt.isPresent() && password.equals(aOpt.get().getMatKhau())) {
            Admin a = aOpt.get();
            SessionUser su = new SessionUser(
                    a.getIdAdmin(),
                    a.getTen() != null ? a.getTen() : a.getTaiKhoan(),
                    a.getEmail(),
                    true
            );
            HttpSession ss = req.getSession(true);
            ss.setAttribute("user", su);       // header của bạn đang dùng sessionScope.user.fullName
            ss.setAttribute("IS_ADMIN", true);
            resp.sendRedirect(req.getContextPath() + "/admin");
            return;
        }

        // 2) Khách
        Optional<KhachHang> kOpt = khDAO.findByEmailAndPassword(account, password);
        if (kOpt.isPresent()) {
            KhachHang k = kOpt.get();
            String name = (k.getTen() != null && !k.getTen().isBlank())
                        ? k.getTen()
                        : (k.getHoTen() != null ? k.getHoTen() : k.getEmail());

            SessionUser su = new SessionUser(k.getId(), name, k.getEmail(), false);
            HttpSession ss = req.getSession(true);
            ss.setAttribute("user", su);
            ss.setAttribute("IS_ADMIN", false);
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        // 3) Sai thông tin
        req.setAttribute("error", "Sai tài khoản hoặc mật khẩu");
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }
}
