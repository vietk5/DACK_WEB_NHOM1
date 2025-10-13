package com.demo.controller;

import com.demo.enums.TrangThaiDonHang;
import com.demo.model.KhachHang;
import com.demo.model.SanPham;
import com.demo.model.cart.GioHangItem;
import com.demo.model.order.ChiTietDonHang;
import com.demo.model.order.DonHang;
import com.demo.model.session.SessionUser;
import com.demo.persistence.DonHangDAO;
import com.demo.persistence.KhachHangDAO;
import com.demo.persistence.SanPhamDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private final DonHangDAO donHangDAO = new DonHangDAO();
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        
        // Kiểm tra đăng nhập
        HttpSession session = req.getSession();
        SessionUser user = (SessionUser) session.getAttribute("user");
        if (user == null || user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        // Kiểm tra giỏ hàng
        List<GioHangItem> cart = (List<GioHangItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }
        
        req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        
        // Kiểm tra đăng nhập
        SessionUser user = (SessionUser) session.getAttribute("user");
        if (user == null || user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        
        // Kiểm tra giỏ hàng
        List<GioHangItem> cart = (List<GioHangItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        try {
            // Lấy thông tin khách hàng từ database
            KhachHang khachHang = khachHangDAO.find(user.getId());
            if (khachHang == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            // Tạo đơn hàng mới
            DonHang donHang = new DonHang();
            donHang.setKhachHang(khachHang);
            donHang.setNgayDatHang(LocalDateTime.now());
            donHang.setTrangThai(TrangThaiDonHang.MOI);
            // Phương thức thanh toán mặc định: COD (ID = 1)
            // Bạn có thể tùy chỉnh để lấy từ form nếu có nhiều phương thức
            
            // Tạo chi tiết đơn hàng từ giỏ hàng
            List<ChiTietDonHang> chiTietList = new ArrayList<>();
            for (GioHangItem item : cart) {
                // Parse ID từ SKU (format: "SP-123")
                String[] parts = item.getSku().split("-");
                if (parts.length < 2) continue;
                
                Long sanPhamId = Long.valueOf(parts[1]);
                SanPham sanPham = sanPhamDAO.find(sanPhamId);
                
                if (sanPham == null || sanPham.getSoLuongTon() < item.getSoLuong()) {
                    // Sản phẩm không tồn tại hoặc không đủ số lượng
                    req.setAttribute("error", "Sản phẩm '" + item.getTen() + "' không đủ số lượng trong kho.");
                    req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
                    return;
                }
                
                ChiTietDonHang chiTiet = new ChiTietDonHang();
                chiTiet.setDonHang(donHang);
                chiTiet.setSanPham(sanPham);
                chiTiet.setSoLuong(item.getSoLuong());
                chiTiet.setDonGia(sanPham.getGia());
                
                chiTietList.add(chiTiet);
                
                // Cập nhật số lượng tồn kho
                sanPham.setSoLuongTon(sanPham.getSoLuongTon() - item.getSoLuong());
                sanPhamDAO.update(sanPham);
            }
            
            donHang.setChiTiet(chiTietList);
            
            // Lưu đơn hàng vào database
            donHangDAO.save(donHang);
            
            System.out.println("✅ [DEBUG] Đơn hàng đã được tạo thành công - ID: " + donHang.getId());

            // Xóa giỏ hàng
            cart.clear();
            session.setAttribute("cart", cart);

            // Chuyển đến trang đơn hàng
            resp.sendRedirect(req.getContextPath() + "/orders");
            
        } catch (Exception e) {
            System.err.println("❌ [ERROR] Lỗi khi tạo đơn hàng: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("error", "Có lỗi xảy ra khi tạo đơn hàng. Vui lòng thử lại.");
            req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
        }
    }
}
