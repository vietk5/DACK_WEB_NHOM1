package com.demo.controller;

import com.demo.enums.TrangThaiDonHang;
import com.demo.model.order.DonHang;
import com.demo.model.session.SessionUser;
import com.demo.persistence.DonHangDAO;
import com.demo.persistence.GenericDAO.Page;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "OrdersServlet", urlPatterns = {"/orders"})
public class OrdersServlet extends HttpServlet {

    private final DonHangDAO donHangDAO = new DonHangDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        SessionUser user = (SessionUser) session.getAttribute("user");
        
        // Kiểm tra đăng nhập
        if (user == null || user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Lấy tham số lọc (nếu có)
        String statusParam = req.getParameter("status");
        TrangThaiDonHang status = null;
        if (statusParam != null && !statusParam.isEmpty()) {
            try {
                status = TrangThaiDonHang.valueOf(statusParam.toUpperCase());
            } catch (IllegalArgumentException e) {
                // Bỏ qua nếu status không hợp lệ
            }
        }
        
        // Phân trang
        int page = 0;
        int size = 10; // 10 đơn hàng mỗi trang
        try {
            String pageParam = req.getParameter("page");
            if (pageParam != null) page = Math.max(0, Integer.parseInt(pageParam));
        } catch (Exception ignored) {}

        // Lấy đơn hàng của khách hàng từ database
        Page<DonHang> ordersPage = donHangDAO.byCustomer(user.getId(), status, page, size);

        // Gửi dữ liệu đến JSP
        req.setAttribute("orders", ordersPage.getContent());
        req.setAttribute("currentPage", ordersPage.getPage());
        req.setAttribute("totalPages", ordersPage.getTotalPages());
        req.setAttribute("totalOrders", ordersPage.getTotalElements());
        req.setAttribute("filterStatus", status);
        
        req.getRequestDispatcher("/WEB-INF/views/orders.jsp").forward(req, resp);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
