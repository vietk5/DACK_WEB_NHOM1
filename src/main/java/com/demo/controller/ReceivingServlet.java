package com.demo.controller;

import com.demo.model.*;
import com.demo.model.database.*; // LoaiSanPhamDB, ThuongHieuDB, SanPhamDB
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
        maxFileSize = 2 * 1024 * 1024, // 2MB / file
        maxRequestSize = 10 * 1024 * 1024 // 10MB / request
)
public class ReceivingServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        final String ctx = request.getContextPath();

        // Nền cho JSP nếu forward
        request.setAttribute("categories", LoaiSanPhamDB.selectAllTenLoaiSanPham());
        request.setAttribute("brands", ThuongHieuDB.selectAllTenThuongHieu());

        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        if ("add".equals(action)) {
            String tenSanPham = param(request, "tenSanPham");
            String tenThuongHieu = param(request, "thuongHieu");
            String tenLoaiSanPham = param(request, "loaiSanPham");
            String moTaNgan = param(request, "moTaNgan");
            String giaStr = param(request, "gia");   // có thể "12.500.000"
            int soLuong = parseIntSafe(request.getParameter("soLuong"), 1);

            // Chuẩn hoá giá: giữ số
            String giaDigits = giaStr.replaceAll("[^0-9]", "");
            BigDecimal gia = new BigDecimal(giaDigits.isEmpty() ? "0" : giaDigits);

            ThuongHieu thuongHieu = ThuongHieuDB.selectThuongHieuByTen(tenThuongHieu);
            LoaiSanPham loaiSanPham = LoaiSanPhamDB.selectLoaiSanPhamByTen(tenLoaiSanPham);

            // Kiểm tra đã tồn tại?
            SanPham existed = SanPhamDB.selectSanPhamByTen(tenSanPham);
            if (existed != null) {
                // hỏi xác nhận tăng số lượng
                response.sendRedirect(ctx + "/receiving?duplicate=true&tenSanPham="
                        + URLEncoder.encode(tenSanPham, "UTF-8"));
                return;
            }

            // Lưu ảnh (nếu có)
            String imagePath = null;
            Part img = null;
            try {
                img = request.getPart("hinhAnh");
            } catch (Exception ignore) {
            }
            if (img != null && img.getSize() > 0) {
                imagePath = saveImageToUploads(request, img, tenSanPham);
            }

            // Tạo mới sản phẩm (soLuongTon = số lượng nhập)
            SanPham p = new SanPham(tenSanPham, thuongHieu, loaiSanPham, gia, moTaNgan, LocalDate.now(), soLuong);

            // Nếu entity có field/setter hình ảnh thì gán (không bắt buộc)
            if (imagePath != null) {
                try {
                    p.getClass().getMethod("setHinhAnh", String.class).invoke(p, imagePath);
                } catch (Exception ignore) {
                    // Không có setHinhAnh -> bỏ qua (ảnh vẫn được lưu file)
                }
            }

            SanPhamDB.insert(p);
            response.sendRedirect(ctx + "/receiving?success=true");
            return;
        }

        if ("confirm_update".equals(action)) {
            String tenSanPham = param(request, "tenSanPham");
            int soLuong = parseIntSafe(request.getParameter("soLuong"), 1);

            SanPham sp = SanPhamDB.selectSanPhamByTen(tenSanPham);
            if (sp != null) {
                // Tăng đúng theo số lượng nhập
                // Nếu bạn đã có hàm updateSoLuongTonById(id) tăng +1,
                // hãy thêm overload tăng theo delta; nếu chưa có, sample ở dưới.
                SanPhamDB.updateSoLuongTonById(sp.getId(), soLuong);
            }
            response.sendRedirect(ctx + "/receiving?update_success=true");
            return;
        }

        // Default
        request.getRequestDispatcher("/WEB-INF/views/receiving.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        request.setAttribute("categories", LoaiSanPhamDB.selectAllTenLoaiSanPham());
        request.setAttribute("brands", ThuongHieuDB.selectAllTenThuongHieu());

        // Nếu user vào trực tiếp link confirm_update (hiếm), xử lý và redirect
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

    /**
     * Lưu ảnh vào assets/img/uploads/, trả về đường dẫn tương đối (để hiển thị)
     */
    private String saveImageToUploads(HttpServletRequest req, Part part, String baseName) throws IOException {
        String submitted = Paths.get(part.getSubmittedFileName()).getFileName().toString();
        String ext = "";
        int dot = submitted.lastIndexOf('.');
        if (dot >= 0) {
            ext = submitted.substring(dot).toLowerCase();
        }

        String safeBase = baseName.toLowerCase().replaceAll("[^a-z0-9]+", "-");
        String fileName = System.currentTimeMillis() + "_" + safeBase + ext;

        String root = req.getServletContext().getRealPath("/");
        Path uploadDir = Paths.get(root, "assets", "img", "uploads");
        Files.createDirectories(uploadDir);

        try (InputStream in = part.getInputStream()) {
            Files.copy(in, uploadDir.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
        }
        // Dùng đường dẫn tương đối để hiển thị trên web
        return "assets/img/uploads/" + fileName;
    }
}
