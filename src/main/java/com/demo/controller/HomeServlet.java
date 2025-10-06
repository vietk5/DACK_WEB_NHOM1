package com.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.demo.repo.DemoRepo;
import com.demo.repo.Product;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");
    resp.setContentType("text/html; charset=UTF-8");

    // Data nền
    req.setAttribute("brands",     DemoRepo.brands());
    req.setAttribute("categories", DemoRepo.categories());

    // Đọc tham số brand (nếu có)
    String brand = req.getParameter("brand");
    if (brand != null) brand = brand.trim();
    final String brandFinal = brand;

    // Best sellers (giới hạn 8) + filter brand nếu có
    List<Product> best = DemoRepo.bestSellers(8);
    if (brandFinal != null && !brandFinal.isEmpty()) {
      best = best.stream()
          .filter(p -> p.getBrand().equalsIgnoreCase(brandFinal))
          .collect(Collectors.toList());
    }
    req.setAttribute("best", best);

    // Laptop
    List<Product> laptops = DemoRepo.byCategory("Laptop");
    if (brandFinal != null && !brandFinal.isEmpty()) {
      laptops = laptops.stream()
          .filter(p -> p.getBrand().equalsIgnoreCase(brandFinal))
          .collect(Collectors.toList());
    }
    req.setAttribute("laptops", laptops);

    // PC
    List<Product> pcs = DemoRepo.byCategory("PC");
    if (brandFinal != null && !brandFinal.isEmpty()) {
      pcs = pcs.stream()
          .filter(p -> p.getBrand().equalsIgnoreCase(brandFinal))
          .collect(Collectors.toList());
    }
    req.setAttribute("pcs", pcs);

    // News
    req.setAttribute("news", DemoRepo.latestNews(6));

    // Gửi thêm biến để JSP biết đang lọc
    req.setAttribute("activeBrand", brandFinal);

    req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.sendRedirect(req.getContextPath() + "/home");
  }
}
