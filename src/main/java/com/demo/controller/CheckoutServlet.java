package com.demo.controller;

import com.demo.enums.TrangThaiDonHang;
import com.demo.model.KhachHang;
import com.demo.model.SanPham;
import com.demo.model.cart.GioHangItem;
import com.demo.model.order.ChiTietDonHang;
import com.demo.model.order.DonHang;
import com.demo.model.session.PendingOrder;
import com.demo.model.session.SessionUser;
import com.demo.persistence.DonHangDAO;
import com.demo.persistence.KhachHangDAO;
import com.demo.persistence.SanPhamDAO;
import com.demo.util.VNPayConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private final DonHangDAO donHangDAO = new DonHangDAO();
    private final SanPhamDAO sanPhamDAO = new SanPhamDAO();
    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        SessionUser user = (SessionUser) session.getAttribute("user");
        if (user == null || user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<GioHangItem> cart = (List<GioHangItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        // TỰ ĐỘNG ĐIỀN THÔNG TIN TỪ PROFILE
        try {
            KhachHang khachHang = khachHangDAO.find(user.getId());
            if (khachHang != null) {
                // Set thông tin vào request để hiển thị trên form
                req.setAttribute("fullName", khachHang.getTen());
                req.setAttribute("phone", khachHang.getSdt());
                req.setAttribute("email", khachHang.getEmail());
                req.setAttribute("address", khachHang.getDiaChi());

                System.out.println("Auto-filled profile for user: " + khachHang.getEmail());
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Nếu lỗi thì vẫn cho phép user nhập thủ công
        }

        req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();

        SessionUser user = (SessionUser) session.getAttribute("user");
        if (user == null || user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<GioHangItem> cart = (List<GioHangItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        // Kiểm tra action từ form
        String action = req.getParameter("action");
        String paymentMethod = req.getParameter("paymentMethod");

        // Nếu user click "Áp dụng voucher" hoặc action không rõ ràng
        if ("applyVoucher".equals(action)) {
            // Xử lý voucher (có thể tính toán lại tổng tiền)
            // Sau đó forward lại checkout.jsp
            req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
            return;
        }

        // Nếu user click "Xác nhận đặt hàng"
        if ("placeOrder".equals(action)) {
            if ("cod".equals(paymentMethod)) {
                processCOD(req, resp);
            } else if ("vnpay".equals(paymentMethod)) {
                processVNPAY(req, resp);
            } else {
                req.setAttribute("error", "Vui lòng chọn phương thức thanh toán");
                req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
            }
            return;
        }

        // Mặc định: forward lại checkout
        req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
    }

    /**
     * Xử lý thanh toán COD
     */
    @SuppressWarnings("unchecked")
    private void processCOD(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession();
        SessionUser user = (SessionUser) session.getAttribute("user");
        List<GioHangItem> cart = (List<GioHangItem>) session.getAttribute("cart");

        try {
            KhachHang khachHang = khachHangDAO.find(user.getId());
            if (khachHang == null) {
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            // Tạo đơn hàng
            DonHang donHang = new DonHang();
            donHang.setKhachHang(khachHang);
            donHang.setNgayDatHang(LocalDateTime.now());
            donHang.setTrangThai(TrangThaiDonHang.MOI);

            List<ChiTietDonHang> chiTietList = new ArrayList<>();
            long totalAmount = 0;

            // Kiểm tra kho và tạo chi tiết đơn hàng
            for (GioHangItem item : cart) {
                long sanPhamId = Long.valueOf(item.getSku().split("-")[1]);
                SanPham sanPham = sanPhamDAO.find(sanPhamId);

                if (sanPham == null || sanPham.getSoLuongTon() < item.getSoLuong()) {
                    req.setAttribute("error", "Sản phẩm '" + item.getTen() + "' không đủ số lượng trong kho.");
                    req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
                    return;
                }

                ChiTietDonHang chiTiet = new ChiTietDonHang();
                chiTiet.setDonHang(donHang);
                chiTiet.setSanPham(sanPham);
                chiTiet.setSoLuong(item.getSoLuong());
                chiTiet.setDonGia(sanPham.getGia());
                chiTietList.add(chiTiet);

                // Cập nhật kho
                sanPham.setSoLuongTon(sanPham.getSoLuongTon() - item.getSoLuong());
                sanPhamDAO.update(sanPham);

                totalAmount += item.getGia() * item.getSoLuong();
            }

            donHang.setChiTiet(chiTietList);
            donHangDAO.save(donHang);

            // Xóa giỏ hàng
            session.removeAttribute("cart");

            resp.sendRedirect(req.getContextPath() + "/orders?checkout_success=true");

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Đã có lỗi xảy ra trong quá trình tạo đơn hàng.");
            try {
                req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
            } catch (ServletException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Xử lý thanh toán VNPAY
     */
    @SuppressWarnings("unchecked")
    private void processVNPAY(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession();
        SessionUser user = (SessionUser) session.getAttribute("user");
        List<GioHangItem> cart = (List<GioHangItem>) session.getAttribute("cart");

        // 1. Tính tổng số tiền từ giỏ hàng
        long totalAmount = 0;
        for (GioHangItem item : cart) {
            totalAmount += item.getGia() * item.getSoLuong();
        }
        String amountStr = String.valueOf(totalAmount * 100); // VNPAY yêu cầu nhân 100

        // 2. Tạo mã giao dịch duy nhất
        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_IpAddr = VNPayConfig.getIpAddress(req);

        // 3. Chuẩn bị các tham số để gửi đi
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", amountStr);
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang " + vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        // 4. Tạo chuỗi hash data và chữ ký
        String hashData = VNPayConfig.hashAllFields(vnp_Params);
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.vnp_HashSecret, hashData);

        // 5. Build URL hoàn chỉnh
        String queryUrl = hashData + "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;

        // 6. LƯU PENDING ORDER VÀO SESSION (QUAN TRỌNG!)
        // Tạo bản sao sâu của cart để tránh bị thay đổi
        List<GioHangItem> cartCopy = new ArrayList<>();
        for (GioHangItem item : cart) {
            // Clone từng item để tránh reference issue
            GioHangItem clonedItem = new GioHangItem();
            clonedItem.setSku(item.getSku());
            clonedItem.setTen(item.getTen());
            clonedItem.setGia(item.getGia());
            clonedItem.setSoLuong(item.getSoLuong());
            clonedItem.setHinh(item.getHinh());
            cartCopy.add(clonedItem);
        }

        PendingOrder pendingOrder = new PendingOrder(
                vnp_TxnRef,
                user.getId(),
                cartCopy,
                totalAmount
        );
        session.setAttribute("pendingOrder", pendingOrder);

        // 7. Chuyển hướng đến VNPAY
        resp.sendRedirect(paymentUrl);
    }
}
