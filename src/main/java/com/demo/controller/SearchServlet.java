package com.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import com.demo.persistence.SanPhamDAO;
import com.demo.persistence.GenericDAO.Page;
import com.demo.model.SanPham;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {
    
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        // Lấy tham số tìm kiếm
        String keyword = req.getParameter("q");
        if (keyword == null) keyword = "";
        keyword = keyword.trim();
        
        String brand = req.getParameter("brand");
        if (brand != null) brand = brand.trim();
        
        String category = req.getParameter("category");
        if (category != null) category = category.trim();

        // Khoảng giá
        String minStr = req.getParameter("min");
        String maxStr = req.getParameter("max");
        BigDecimal minPrice = null, maxPrice = null;
        try { 
            if (minStr != null && !minStr.isBlank()) 
                minPrice = new BigDecimal(minStr.trim()); 
        } catch (Exception ignored) {}
        try { 
            if (maxStr != null && !maxStr.isBlank()) 
                maxPrice = new BigDecimal(maxStr.trim()); 
        } catch (Exception ignored) {}
        
        // Phân trang
        int page = 0;
        int size = 20; // 20 sản phẩm mỗi trang
        try {
            String pageParam = req.getParameter("page");
            if (pageParam != null) page = Math.max(0, Integer.parseInt(pageParam));
        } catch (Exception ignored) {}
        
        // Sắp xếp
        String sort = req.getParameter("sort");
        String sortBy = "id";
        boolean asc = true;
        
        if ("price_asc".equalsIgnoreCase(sort)) {
            sortBy = "gia";
            asc = true;
        } else if ("price_desc".equalsIgnoreCase(sort)) {
            sortBy = "gia";
            asc = false;
        } else if ("name_asc".equalsIgnoreCase(sort)) {
            sortBy = "tenSanPham";
            asc = true;
        }

        // Tìm kiếm từ database
        Page<SanPham> searchPage = sanPhamDAO.searchAdvanced(
            keyword, brand, category, minPrice, maxPrice, 
            page, size, sortBy, asc
        );
        
        // Lấy danh sách thương hiệu và loại sản phẩm
        List<String> brands = sanPhamDAO.getAllBrands();
        List<String> categories = sanPhamDAO.getAllCategories();

        // Gửi dữ liệu đến JSP
        req.setAttribute("searchResults", searchPage.getContent());
        req.setAttribute("keyword", keyword);
        req.setAttribute("brands", brands);
        req.setAttribute("categories", categories);
        req.setAttribute("activeBrand", brand);
        req.setAttribute("activeCategory", category);
        req.setAttribute("resultCount", searchPage.getTotalElements());
        req.setAttribute("currentPage", searchPage.getPage());
        req.setAttribute("totalPages", searchPage.getTotalPages());
        req.setAttribute("minPrice", minPrice);
        req.setAttribute("maxPrice", maxPrice);
        req.setAttribute("sort", sort);

        req.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        doGet(req, resp);
    }
}
