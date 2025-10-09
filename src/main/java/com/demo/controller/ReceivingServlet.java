/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.demo.controller;

import com.demo.model.*;
import com.demo.model.database.*;
import com.demo.model.database.ThuongHieuDB;
import com.demo.repo.DemoRepo;
import com.demo.repo.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet(name = "ReceivingServlet", urlPatterns = {"/receiving"})
public class ReceivingServlet extends HttpServlet{
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse respone)
      throws ServletException, IOException {
      
    String url = "/WEB-INF/views/receiving.jsp";
    
    LocalDate currentDate = LocalDate.now();
    request.setAttribute("currentDate", currentDate);
    
    request.setCharacterEncoding("UTF-8");
    respone.setCharacterEncoding("UTF-8");
    respone.setContentType("text/html; charset=UTF-8");

    // Data nền
    request.setAttribute("categories", LoaiSanPhamDB.selectAllTenLoaiSanPham());
    request.setAttribute("brands", ThuongHieuDB.selectAllTenThuongHieu());
    
    String action = request.getParameter("action");
    if (action == null) action = "join";
    
    if (action.equals("add")) {
        String idSanPhamStr = request.getParameter("idSanPham");
        String tenSanPham = request.getParameter("tenSanPham");
        String tenThuongHieu = request.getParameter("thuongHieu");
        String tenLoaiSanPham = request.getParameter("loaiSanPham");
        String moTaNgan = request.getParameter("moTaNgan");
        String giaStr = request.getParameter("gia");
        
        ThuongHieu thuongHieu = ThuongHieuDB.selectThuongHieuByTen(tenThuongHieu);
        LoaiSanPham loaiSanPham = LoaiSanPhamDB.selectLoaiSanPhamByTen(tenLoaiSanPham);
        
        Long idSanPham = Long.parseLong(idSanPhamStr.trim());
        BigDecimal gia = new BigDecimal(giaStr.trim());
        
        boolean sanPhamTonTai = SanPhamDB.isProductExistById(idSanPham);
        if (sanPhamTonTai == true) {
            SanPhamDB.updateSoLuongTonById(idSanPham);
        }
        else {
            SanPham sanPham = new SanPham(idSanPham, tenSanPham, thuongHieu, loaiSanPham, gia, moTaNgan, currentDate, 1);
            SanPhamDB.insert(sanPham);
        }
        url = "/WEB-INF/views/receiving_sucess.jsp";
    }
    request.getRequestDispatcher(url).forward(request, respone);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse respone)
      throws ServletException, IOException {
    respone.sendRedirect(request.getContextPath() + "/receiving");
  }
}
