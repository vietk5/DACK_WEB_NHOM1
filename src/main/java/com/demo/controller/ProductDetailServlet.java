package com.demo.controller;

import com.demo.model.*;
import com.demo.model.database.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.demo.repo.DemoRepo;
import com.demo.repo.Product;

@WebServlet(name = "ProductDetailServlet", urlPatterns = {"/product"})
public class ProductDetailServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");
    resp.setContentType("text/html; charset=UTF-8");

    // Lấy tham số sản phẩm (có thể là tên hoặc ID)
    String productIdStr = req.getParameter("productId");
    if (productIdStr == null || productIdStr.trim().isEmpty()) {
      resp.sendRedirect(req.getContextPath() + "/home");
      return;
    }

    // Tìm sản phẩm theo tên
//    Long productId = Long.valueOf(productIdStr.trim()); 
//    SanPham product = SanPhamDB.selectAllSanPhamById(productId);
    
    try {
        Long productId = Long.valueOf(productIdStr.trim()); 
        SanPham product = SanPhamDB.selectSanPhamById(productId);
        
        // Lấy các sản phẩm liên quan (cùng thương hiệu hoặc loại)
        List<SanPham> relatedProducts = SanPhamDB.selectAllSanPhamByLoaiHoacTHieu(product.getLoai().getTenLoai(), product.getThuongHieu().getTenThuongHieu());

        // ... Tiếp tục xử lý sản phẩm ...
        req.setAttribute("product", product);
        req.setAttribute("relatedProducts", relatedProducts);
        
        req.setAttribute("categories", LoaiSanPhamDB.selectAllTenLoaiSanPham());
        req.setAttribute("brands", ThuongHieuDB.selectAllTenThuongHieu());
    
    } catch (NumberFormatException e) {
        System.err.println("ID không hợp lệ. Lỗi chuyển đổi số: " + e.getMessage());
        resp.sendRedirect(req.getContextPath() + "/home");
        return;
    }
    
//    // Lấy các sản phẩm liên quan (cùng thương hiệu hoặc danh mục)
//    List<Product> relatedProducts = allProducts.stream()
//        .filter(p -> !p.getName().equals(product.getName()) && 
//                    (p.getBrand().equalsIgnoreCase(product.getBrand()) || 
//                     p.getCategory().equalsIgnoreCase(product.getCategory())))
//        .limit(4)
//        .toList();

    // Gửi dữ liệu đến JSP
//    req.setAttribute("product", product);
////    req.setAttribute("relatedProducts", relatedProducts);
//    req.setAttribute("brands", DemoRepo.brands());
//    req.setAttribute("categories", DemoRepo.categories());

    req.getRequestDispatcher("/WEB-INF/views/product_detail.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    doGet(req, resp);
  }
}
