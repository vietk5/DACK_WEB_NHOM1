package com.demo.controller;

import com.demo.model.SanPham;
import com.demo.persistence.SanPhamDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// ============ CODE CŨ - ĐÃ COMMENT (dùng direct DB classes) ============
// import com.demo.model.*;
// import com.demo.model.database.LoaiSanPhamDB;
// import com.demo.model.database.*;
// import com.demo.repo.DemoRepo;
// import com.demo.repo.Product;
// ==========================================================================

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {
    
    // Sử dụng SanPhamDAO để lấy dữ liệu từ DATABASE (chuẩn JPA pattern)
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // Lấy categories và brands từ DATABASE qua SanPhamDAO
        req.setAttribute("categories", sanPhamDAO.getAllCategories());
        req.setAttribute("brands", sanPhamDAO.getAllBrands());
        
        // ============ CODE CŨ (đã comment) ============
        // req.setAttribute("categories", LoaiSanPhamDB.selectAllTenLoaiSanPham());
        // req.setAttribute("brands", ThuongHieuDB.selectAllTenThuongHieu());
        // ==============================================

        // Đọc tham số brand (nếu có) để lọc sản phẩm
        String brand = req.getParameter("brand");
        if (brand != null) brand = brand.trim();
        final String brandFinal = brand;

        // Best sellers (lấy 8 sản phẩm từ DATABASE)
        List<SanPham> best = sanPhamDAO.findAll(0, 8, "id", false).getContent();
        if (brandFinal != null && !brandFinal.isEmpty()) {
            best = best.stream()
                .filter(p -> p.getThuongHieu() != null && 
                           p.getThuongHieu().getTenThuongHieu().equalsIgnoreCase(brandFinal))
                .collect(Collectors.toList());
        }
        req.setAttribute("best", best);
        
        // ============ CODE CŨ (đã comment) ============
        // List<SanPham> best = SanPhamDB.select8SanPham();
        // List<Product> best = DemoRepo.bestSellers(8);
        // ==============================================

        // Laptop - Lấy từ DATABASE theo loại "Laptop"
        List<SanPham> laptops = sanPhamDAO.findWhere(
            "lower(e.loai.tenLoai) = :loai" + 
            (brandFinal != null && !brandFinal.isEmpty() ? 
                " and lower(e.thuongHieu.tenThuongHieu) = :brand" : ""),
            brandFinal != null && !brandFinal.isEmpty() ? 
                java.util.Map.of("loai", "laptop", "brand", brandFinal.toLowerCase()) : 
                java.util.Map.of("loai", "laptop")
        );
        req.setAttribute("laptops", laptops);
        
        // ============ CODE CŨ (đã comment) ============
        // List<SanPham> laptops = SanPhamDB.selectAllSanPhamByTenLoai("Laptop");
        // ==============================================

        // PC - Lấy từ DATABASE theo loại "PC"
        List<SanPham> pcs = sanPhamDAO.findWhere(
            "lower(e.loai.tenLoai) = :loai" + 
            (brandFinal != null && !brandFinal.isEmpty() ? 
                " and lower(e.thuongHieu.tenThuongHieu) = :brand" : ""),
            brandFinal != null && !brandFinal.isEmpty() ? 
                java.util.Map.of("loai", "pc", "brand", brandFinal.toLowerCase()) : 
                java.util.Map.of("loai", "pc")
        );
        req.setAttribute("pcs", pcs);
        
        // ============ CODE CŨ (đã comment) ============
        // List<SanPham> pcs = SanPhamDB.selectAllSanPhamByTenLoai("PC");
        // ==============================================

        // News - Để empty list (chưa có News trong DATABASE)
        req.setAttribute("news", new ArrayList<>());
        
        // ============ CODE CŨ (đã comment) ============
        // req.setAttribute("news", DemoRepo.latestNews(6));
        // ==============================================

        // Gửi biến activeBrand để JSP biết đang lọc theo brand nào
        req.setAttribute("activeBrand", brandFinal);

        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}
