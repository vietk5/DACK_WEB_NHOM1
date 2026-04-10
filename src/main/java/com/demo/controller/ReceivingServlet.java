package com.demo.controller;

import com.demo.model.*;
import com.demo.model.database.*; // LoaiSanPhamDB, ThuongHieuDB, SanPhamDB
import com.demo.persistence.SanPhamDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.*;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.file.*;
import java.time.LocalDate;

@WebServlet(name = "ReceivingServlet", urlPatterns = {"/receiving"})
@MultipartConfig(
        fileSizeThreshold = 512 * 1024, // 512KB -> đệm
        maxFileSize = 2 * 1024 * 1024,  // 2MB / file
        maxRequestSize = 10 * 1024 * 1024 // 10MB / request
)
public class ReceivingServlet extends HttpServlet {

    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        final String ctx = request.getContextPath();

        // Dữ liệu nền cho JSP nếu forward
        request.setAttribute("categories", sanPhamDAO.getAllCategories());
        request.setAttribute("brands", sanPhamDAO.getAllBrands());
        request.setAttribute("categoryBrands", sanPhamDAO.getCategoryBrandsMap());

        String action = request.getParameter("action");
        if (action == null) action = "";

        if ("add".equals(action)) {
            String tenSanPham = param(request, "tenSanPham");
            String tenThuongHieu = param(request, "thuongHieu");
            String tenLoaiSanPham = param(request, "loaiSanPham");
            String moTaNgan = param(request, "moTaNgan");
            String giaStr = param(request, "gia");
            int soLuong = parseIntSafe(request.getParameter("soLuong"), 1);

            // Chuẩn hoá giá: chỉ giữ chữ số
            String giaDigits = giaStr.replaceAll("[^0-9]", "");
            BigDecimal gia = new BigDecimal(giaDigits.isEmpty() ? "0" : giaDigits);

            ThuongHieu thuongHieu = ThuongHieuDB.selectThuongHieuByTen(tenThuongHieu);
            LoaiSanPham loaiSanPham = LoaiSanPhamDB.selectLoaiSanPhamByTen(tenLoaiSanPham);

            // Kiểm tra đã tồn tại sản phẩm
            SanPham existed = SanPhamDB.selectSanPhamByTen(tenSanPham);
            if (existed != null) {
                response.sendRedirect(ctx + "/receiving?duplicate=true&tenSanPham="
                        + URLEncoder.encode(tenSanPham, "UTF-8"));
                return;
            }

            // Tạo mới sản phẩm
            SanPham p = new SanPham(tenSanPham, thuongHieu, loaiSanPham, gia, moTaNgan, LocalDate.now(), soLuong);
            // Lưu ảnh (nếu có)
            Part img = null;
            try {
                img = request.getPart("hinhAnh");
            } catch (Exception ignore) {}
            
            if (img != null && img.getSize() > 0) {
                String submitted = Paths.get(img.getSubmittedFileName()).getFileName().toString();
                String ext = getExtension(submitted);
                String contentType = img.getContentType();

                // Validate nhanh (giống logic trong saveImage cũ)
                if (!isValidImage(ext, contentType)) {
                    response.sendRedirect(ctx + "/receiving?image_error=true");
                    return;
                }
            }
            
            SanPhamDB.insert(p);
            long newId = p.getId(); 

            // BƯỚC 3: Lưu file với tên là ID + EXT
            if (img != null && img.getSize() > 0) {
                try {
                    String ext = getExtension(Paths.get(img.getSubmittedFileName()).getFileName().toString());
                    String fileName = newId + ext; // Tên file là ID

                    String imagePath = saveFinalImage(request, img, fileName);
                } catch (IOException e) {
                    // Xử lý lỗi lưu file nếu cần
                }
            }
                
            response.sendRedirect(ctx + "/receiving?success=true");
            return;
        }

        if ("confirm_update".equals(action)) {
            String tenSanPham = param(request, "tenSanPham");
            int soLuong = parseIntSafe(request.getParameter("soLuong"), 1);

            SanPham sp = SanPhamDB.selectSanPhamByTen(tenSanPham);
            if (sp != null) {
                SanPhamDB.updateSoLuongTonById(sp.getId(), soLuong);
            }
            response.sendRedirect(ctx + "/receiving?update_success=true");
            return;
        }

        // Mặc định
        request.getRequestDispatcher("/WEB-INF/views/receiving.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        request.setAttribute("categories", sanPhamDAO.getAllCategories());
        request.setAttribute("brands", sanPhamDAO.getAllBrands());
        request.setAttribute("categoryBrands", sanPhamDAO.getCategoryBrandsMap());

        String action = request.getParameter("action");
        if ("confirm_update".equals(action)) {
            String tenSanPham = param(request, "tenSanPham");
            int soLuong = parseIntSafe(request.getParameter("soLuong"), 1);

            SanPham sp = SanPhamDB.selectSanPhamByTen(tenSanPham);
            if (sp != null) {
                SanPhamDB.updateSoLuongTonById(sp.getId(), soLuong);
            }
            response.sendRedirect(request.getContextPath() + "/receiving?update_success=true");
            return;
        }

        request.getRequestDispatcher("/WEB-INF/views/receiving.jsp").forward(request, response);
    }

    // ==== HÀM PHỤ ====

    private static String param(HttpServletRequest req, String name) {
        String v = req.getParameter(name);
        return v == null ? "" : v.trim();
    }

    private static int parseIntSafe(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return def;
        }
    }

    // Hàm lấy đuôi file
    private String getExtension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return (dot >= 0) ? fileName.substring(dot).toLowerCase() : "";
    }

    // Hàm kiểm tra tính hợp lệ của ảnh
    private boolean isValidImage(String ext, String contentType) {
        boolean validExt = ext.equals(".jpg") || ext.equals(".jpeg") || ext.equals(".png");
        boolean validMime = contentType != null && contentType.startsWith("image/");
        return validExt && validMime;
    }

    // Hàm thực hiện ghi file lên ổ cứng
    private String saveFinalImage(HttpServletRequest req, Part part, String fileName) throws IOException {
        String root = req.getServletContext().getRealPath("/");
        Path uploadDir = Paths.get(root, "assets", "img", "products");

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path filePath = uploadDir.resolve(fileName);
        try (InputStream in = part.getInputStream()) {
            Files.copy(in, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return "assets/img/products/" + fileName;
    }
}
