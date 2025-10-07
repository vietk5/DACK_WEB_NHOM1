package com.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.*;
import java.util.stream.*;

import com.demo.repo.DemoRepo;
import com.demo.repo.Product;

@WebServlet(name = "SearchServlet", urlPatterns = {"/search"})
public class SearchServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");
    resp.setContentType("text/html; charset=UTF-8");

    // Lấy từ khóa tìm kiếm
    String keyword = req.getParameter("q");
    if (keyword == null) keyword = "";
    keyword = keyword.trim();
    
    // Lấy tham số lọc (nếu có)
    String brand = req.getParameter("brand");
    if (brand != null) brand = brand.trim();
    final String brandFinal = brand;
    
    String category = req.getParameter("category");
    if (category != null) category = category.trim();
    final String categoryFinal = category;

    // Lấy tất cả sản phẩm
    List<Product> allProducts = DemoRepo.getAllProducts();

    // Price range
    String minStr = req.getParameter("min");
    String maxStr = req.getParameter("max");
    Long min = null, max = null;
    try { if (minStr != null && !minStr.isBlank()) min = Long.parseLong(minStr.trim()); } catch (Exception ignored) {}
    try { if (maxStr != null && !maxStr.isBlank()) max = Long.parseLong(maxStr.trim()); } catch (Exception ignored) {}
    
    // Lọc theo từ khóa tìm kiếm
    List<Product> searchResults = allProducts;
    if (!keyword.isEmpty()) {
      final String keywordLower = keyword.toLowerCase();
      searchResults = allProducts.stream()
          .filter(p -> p.getName().toLowerCase().contains(keywordLower) ||
                      p.getBrand().toLowerCase().contains(keywordLower) ||
                      p.getCategory().toLowerCase().contains(keywordLower))
          .collect(Collectors.toList());
    }
    
    // Lọc theo brand (nếu có)
    if (brandFinal != null && !brandFinal.isEmpty()) {
      searchResults = searchResults.stream()
          .filter(p -> p.getBrand().equalsIgnoreCase(brandFinal))
          .collect(Collectors.toList());
    }
    
    // Lọc theo category (nếu có)
    if (categoryFinal != null && !categoryFinal.isEmpty()) {
      searchResults = searchResults.stream()
          .filter(p -> p.getCategory().equalsIgnoreCase(categoryFinal))
          .collect(Collectors.toList());
    }

    // Lọc theo khoảng giá
    if (min != null) {
      final long minVal = min;
      searchResults = searchResults.stream().filter(p -> p.getPrice() >= minVal).collect(Collectors.toList());
    }
    if (max != null) {
      final long maxVal = max;
      searchResults = searchResults.stream().filter(p -> p.getPrice() <= maxVal).collect(Collectors.toList());
    }

    // Sort
    String sort = req.getParameter("sort"); // price_asc, price_desc, bestseller
    if (sort != null) sort = sort.trim();
    if ("price_asc".equalsIgnoreCase(sort)) {
      searchResults = searchResults.stream()
          .sorted(Comparator.comparingLong(Product::getPrice))
          .collect(Collectors.toList());
    } else if ("price_desc".equalsIgnoreCase(sort)) {
      searchResults = searchResults.stream()
          .sorted(Comparator.comparingLong(Product::getPrice).reversed())
          .collect(Collectors.toList());
    } else if ("bestseller".equalsIgnoreCase(sort)) {
      searchResults = searchResults.stream()
          .sorted(Comparator.comparingDouble(Product::getRating).reversed())
          .collect(Collectors.toList());
    }

    // Gửi dữ liệu đến JSP
    req.setAttribute("searchResults", searchResults);
    req.setAttribute("keyword", keyword);
    req.setAttribute("brands", DemoRepo.brands());
    req.setAttribute("categories", DemoRepo.categories());
    req.setAttribute("activeBrand", brandFinal);
    req.setAttribute("activeCategory", categoryFinal);
    req.setAttribute("resultCount", searchResults.size());
    req.setAttribute("min", min);
    req.setAttribute("max", max);
    req.setAttribute("sort", sort);

    req.getRequestDispatcher("/WEB-INF/views/search.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    doGet(req, resp);
  }
}
