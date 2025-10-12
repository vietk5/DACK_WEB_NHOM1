package com.demo.controller;

import com.demo.model.cart.GioHangItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        List<GioHangItem> cart = (List<GioHangItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        String action = req.getParameter("action");
        if (action == null) action = "view";

        System.out.println("[DEBUG] Action GET nhận được: " + action);

        switch (action) {
            case "add": {
                String sku = req.getParameter("sku");
                String ten = req.getParameter("name");
                String hinh = req.getParameter("image");
                long gia = parseLong(req.getParameter("price"));
                int soLuong = parseInt(req.getParameter("qty"), 1);

                System.out.println("🛒 [DEBUG] Thêm sản phẩm vào giỏ (GET):");
                System.out.println("     SKU: " + sku);
                System.out.println("     Tên: " + ten);
                System.out.println("     Hình: " + hinh);
                System.out.println("     Giá: " + gia);
                System.out.println("     Số lượng thêm: " + soLuong);

                Optional<GioHangItem> existing = cart.stream()
                        .filter(i -> i.getSku().equals(sku))
                        .findFirst();

                if (existing.isPresent()) {
                    GioHangItem item = existing.get();
                    item.setSoLuong(item.getSoLuong() + soLuong);
                    System.out.println("🟡 [DEBUG] Sản phẩm đã tồn tại, cập nhật số lượng mới: " + item.getSoLuong());
                } else {
                    cart.add(new GioHangItem(sku, ten, hinh, gia, soLuong));
                    System.out.println("🟢 [DEBUG] Sản phẩm mới đã thêm vào giỏ.");
                }

                session.setAttribute("cart", cart);
                System.out.println("✅ [DEBUG] Tổng sản phẩm trong giỏ: " + cart.size());
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }

            case "remove": {
                String sku = req.getParameter("sku");
                System.out.println("🗑️ [DEBUG] Xóa sản phẩm: " + sku);
                cart.removeIf(i -> i.getSku().equals(sku));
                session.setAttribute("cart", cart);
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }

            case "clear": {
                System.out.println("🧹 [DEBUG] Xóa toàn bộ giỏ hàng");
                cart.clear();
                session.setAttribute("cart", cart);
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }

            default:
                System.out.println("📦 [DEBUG] Hiển thị giỏ hàng – tổng sản phẩm: " + cart.size());
                req.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(req, resp);
        }
    }

    private long parseLong(String val) {
        try { return Long.parseLong(val); } catch (Exception e) { return 0; }
    }

    private int parseInt(String val, int def) {
        try { return Integer.parseInt(val); } catch (Exception e) { return def; }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        List<GioHangItem> cart = (List<GioHangItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        String action = req.getParameter("action");
        if (action == null) action = "view";

        System.out.println("🛒 [DEBUG] Action POST nhận được: " + action);

        switch (action) {
            case "add": {
                String sku = req.getParameter("sku");
                String ten = req.getParameter("name");
                String hinh = req.getParameter("image");
                long gia = parseLong(req.getParameter("price"));
                int soLuong = parseInt(req.getParameter("qty"), 1);

                System.out.println("🛒 [DEBUG] Thêm sản phẩm vào giỏ (POST):");
                System.out.println("     SKU: " + sku);
                System.out.println("     Tên: " + ten);
                System.out.println("     Giá: " + gia);
                System.out.println("     Số lượng thêm: " + soLuong);

                Optional<GioHangItem> existing = cart.stream()
                        .filter(i -> i.getSku().equals(sku))
                        .findFirst();

                if (existing.isPresent()) {
                    GioHangItem item = existing.get();
                    item.setSoLuong(item.getSoLuong() + soLuong);
                    System.out.println("🟡 [DEBUG] Sản phẩm đã tồn tại, cập nhật số lượng mới: " + item.getSoLuong());
                } else {
                    cart.add(new GioHangItem(sku, ten, hinh, gia, soLuong));
                    System.out.println("🟢 [DEBUG] Sản phẩm mới đã thêm vào giỏ.");
                }

                session.setAttribute("cart", cart);
                System.out.println("✅ [DEBUG] Số lượng sản phẩm trong giỏ hiện tại: " + cart.size());
                break;
            }

            case "remove": {
                String sku = req.getParameter("sku");
                System.out.println("🗑️ [DEBUG] Xóa sản phẩm: " + sku);
                cart.removeIf(i -> i.getSku().equals(sku));
                session.setAttribute("cart", cart);
                break;
            }

            case "clear": {
                System.out.println("🧹 [DEBUG] Xóa toàn bộ giỏ hàng");
                cart.clear();
                session.setAttribute("cart", cart);
                break;
            }
        }

        resp.sendRedirect(req.getContextPath() + "/cart");
    }
}
