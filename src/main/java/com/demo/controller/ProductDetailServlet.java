package com.demo.controller;

import com.demo.model.SanPham;
import com.demo.persistence.SanPhamDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/product"})
public class ProductDetailServlet extends HttpServlet {
    
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // Lấy tham số sản phẩm ID
        String productIdStr = req.getParameter("id");
        if (productIdStr == null || productIdStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        try {
            Long productId = Long.valueOf(productIdStr.trim());
            SanPham product = sanPhamDAO.find(productId);
            
            if (product == null) {
                resp.sendRedirect(req.getContextPath() + "/home");
                return;
            }
            
            // Lấy các sản phẩm liên quan (cùng loại sản phẩm, trừ sản phẩm hiện tại)
            List<SanPham> relatedProducts = sanPhamDAO.relatedByLoai(
                product.getLoai().getId(), 
                productId, 
                4
            );

            // Gửi dữ liệu đến JSP
            req.setAttribute("product", product);
            req.setAttribute("relatedProducts", relatedProducts);
            req.setAttribute("brands", sanPhamDAO.getAllBrands());
            req.setAttribute("categories", sanPhamDAO.getAllCategories());
            req.setAttribute("categoryBrands", sanPhamDAO.getCategoryBrandsMap());

            req.getRequestDispatcher("/WEB-INF/views/product_detail.jsp").forward(req, resp);
            
        } catch (NumberFormatException e) {
            System.err.println("ID không hợp lệ: " + e.getMessage());
            resp.sendRedirect(req.getContextPath() + "/home");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        doGet(req, resp);
    }
}
