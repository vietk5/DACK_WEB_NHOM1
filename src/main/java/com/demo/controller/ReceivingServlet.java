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
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet(name = "ReceivingServlet", urlPatterns = {"/receiving"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,  // 1 MB
    maxFileSize = 1024 * 1024 * 10,       // 10 MB
    maxRequestSize = 1024 * 1024 * 50     // 50 MB
)
public class ReceivingServlet extends HttpServlet{
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
      
    String url = "/WEB-INF/views/receiving.jsp";

    // Data nền
//    request.setAttribute("categories", LoaiSanPhamDB.selectAllTenLoaiSanPham());
//    request.setAttribute("brands", ThuongHieuDB.selectAllTenThuongHieu());
    
    String action = request.getParameter("action");
    if (action == null) action = "";
    
    if (action.equals("add")) {
        String tenSanPham = request.getParameter("tenSanPham");
        String tenThuongHieu = request.getParameter("thuongHieu");
        String tenLoaiSanPham = request.getParameter("loaiSanPham");
        String moTaNgan = request.getParameter("moTaNgan");
        String giaStr = request.getParameter("gia");
        String soLuongStr = request.getParameter("soLuong");
        
        ThuongHieu thuongHieu = ThuongHieuDB.selectThuongHieuByTen(tenThuongHieu);
        LoaiSanPham loaiSanPham = LoaiSanPhamDB.selectLoaiSanPhamByTen(tenLoaiSanPham);
        
        BigDecimal gia = new BigDecimal(giaStr.trim());
        Integer soLuongNhap = Integer.valueOf(soLuongStr.trim());
        
        SanPham sanPham = SanPhamDB.selectSanPhamByTen(tenSanPham);
        if (sanPham != null) {
            response.sendRedirect(request.getContextPath() + "/receiving?duplicate=true&tenSanPham=" + tenSanPham + "&soLuong=" + soLuongNhap); 
            return;
        }
        else {
            SanPham newSanPham = new SanPham(tenSanPham, thuongHieu, loaiSanPham, gia, moTaNgan, LocalDate.now(), soLuongNhap);
            SanPhamDB.insert(newSanPham);
            Long id = SanPhamDB.selectIDSanPhamByTen(tenSanPham);
            // xu ly hinh anh
            Part filePart = request.getPart("hinhAnh");
            String originalFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

                if (originalFileName != null && !originalFileName.isEmpty()) {
                    // Lấy phần mở rộng của file (vd: .jpg, .png)
                    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                    // Tạo tên file mới theo ID sản phẩm
                    String newFileName = id + fileExtension;

                    // Lấy đường dẫn tuyệt đối của thư mục webapp
                    String appPath = request.getServletContext().getRealPath("");
                    // Tạo đường dẫn đầy đủ đến thư mục lưu ảnh
                    String SAVE_DIR = "assets" + File.separator + "img" + File.separator + "products";
                    String savePath = appPath + File.separator + SAVE_DIR;

                    // Tạo thư mục nếu chưa tồn tại
                    File fileSaveDir = new File(savePath);
                    if (!fileSaveDir.exists()) {
                        fileSaveDir.mkdir();
                    }

                    // Lưu file vào thư mục
                    filePart.write(savePath + File.separator + newFileName);
                }
            
            response.sendRedirect(request.getContextPath() + "/receiving?success=true"); 
            return;
        }
//        response.sendRedirect(request.getContextPath() + "/home");
    }
//    else if (action.equals("confirm_update")) {
//        String tenSanPham = request.getParameter("tenSanPham");
//        SanPham sanPham = SanPhamDB.selectSanPhamByTen(tenSanPham); 
//        SanPhamDB.updateSoLuongTonById(sanPham.getId());
//        response.sendRedirect(request.getContextPath() + "/receiving?update_success=true");
//        return;
//    }
    request.getRequestDispatcher(url).forward(request, response);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        // Luôn chuẩn bị dữ liệu nền cho JSP
        request.setAttribute("categories", LoaiSanPhamDB.selectAllTenLoaiSanPham());
        request.setAttribute("brands", ThuongHieuDB.selectAllTenThuongHieu());
        
        String action = request.getParameter("action");
        if (action == null) action = "";
        if (action.equals("confirm_update")) {
            String tenSanPham = request.getParameter("tenSanPham");
            String soLuongStr = request.getParameter("soLuong");
            Integer soLuongNhap = Integer.valueOf(soLuongStr.trim());
            SanPham sanPham = SanPhamDB.selectSanPhamByTen(tenSanPham); 
            SanPhamDB.updateSoLuongTonById(sanPham.getId(), soLuongNhap);
            response.sendRedirect(request.getContextPath() + "/receiving?update_success=true");
            return;
        }

        // Chuyển tiếp (forward) đến trang JSP
        request.getRequestDispatcher("/WEB-INF/views/receiving.jsp").forward(request, response);
  }
}
