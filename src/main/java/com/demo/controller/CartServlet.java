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

        // ✅ Kiểm tra đăng nhập trước khi cho phép thêm giỏ
        Object user = session.getAttribute("user"); // hoặc "account" tùy bạn lưu
        String action = req.getParameter("action");
        if ("add".equals(action) && user == null) {
            // Lưu lại trang hiện tại để quay lại sau đăng nhập
            String referer = req.getHeader("Referer");
            session.setAttribute("redirectAfterLogin", referer);
            resp.sendRedirect(req.getContextPath() + "/login?requireLogin=true");
            return;
        }

        if (action == null) action = "view";

        System.out.println("[DEBUG] Action GET nhận được: " + action);

        switch (action) {
            case "add": {
                String sku = req.getParameter("sku");
                String ten = req.getParameter("name");
                String hinh = req.getParameter("image");
                long gia = parseLong(req.getParameter("price"));
                int soLuong = parseInt(req.getParameter("qty"), 1);

                System.out.println("🛒 Giá nhận được từ request: " + req.getParameter("price"));
                System.out.println("[DEBUG] Giá nhận được từ form: " + req.getParameter("price"));

                Optional<GioHangItem> existing = cart.stream()
                        .filter(i -> i.getSku().equals(sku))
                        .findFirst();

                if (existing.isPresent()) {
                    GioHangItem item = existing.get();
                    item.setSoLuong(item.getSoLuong() + soLuong);
                } else {
                    cart.add(new GioHangItem(sku, ten, hinh, gia, soLuong));
                }

                session.setAttribute("cart", cart);
                int totalQty = cart.stream().mapToInt(GioHangItem::getSoLuong).sum();
                session.setAttribute("cartCount", totalQty);

                String referer = req.getHeader("Referer");
                if (referer != null && !referer.isBlank()) {
                    resp.sendRedirect(referer);
                } else {
                    resp.sendRedirect(req.getContextPath() + "/cart");
                }
                return;
            }

            case "remove": {
                String sku = req.getParameter("sku");
                cart.removeIf(i -> i.getSku().equals(sku));
                session.setAttribute("cart", cart);
                session.setAttribute("cartCount", cart.stream().mapToInt(GioHangItem::getSoLuong).sum());
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }

            case "clear": {
                cart.clear();
                session.setAttribute("cart", cart);
                session.setAttribute("cartCount", 0);
                resp.sendRedirect(req.getContextPath() + "/cart");
                return;
            }

            default:
                req.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(req, resp);
        }
    }

    private long parseLong(String val) {
        try {
            if (val == null) return 0;
            val = val.replaceAll("\\.\\d+$", ""); // cắt .00
            val = val.replaceAll("[^0-9]", "");   // bỏ dấu chấm/thừa
            if (val.isEmpty()) return 0;
            return Long.parseLong(val);
        } catch (Exception e) {
            System.out.println("⚠️ parseLong lỗi với: " + val);
            return 0;
        }
    }

    private int parseInt(String val, int def) {
        try { return Integer.parseInt(val); } catch (Exception e) { return def; }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        Object user = session.getAttribute("user");
        List<GioHangItem> cart = (List<GioHangItem>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();

        String action = req.getParameter("action");
        if (action == null) action = "view";

        // ✅ Kiểm tra đăng nhập khi thêm (POST)
        if ("add".equals(action) && user == null) {
            String referer = req.getHeader("Referer");
            session.setAttribute("redirectAfterLogin", referer);
            resp.sendRedirect(req.getContextPath() + "/login?requireLogin=true");
            return;
        }

        System.out.println("🛒 [DEBUG] Action POST nhận được: " + action);

        switch (action) {
            case "add": {
                String sku = req.getParameter("sku");
                String ten = req.getParameter("name");
                String hinh = req.getParameter("image");
                long gia = parseLong(req.getParameter("price"));
                int soLuong = parseInt(req.getParameter("qty"), 1);

                Optional<GioHangItem> existing = cart.stream()
                        .filter(i -> i.getSku().equals(sku))
                        .findFirst();

                if (existing.isPresent()) {
                    GioHangItem item = existing.get();
                    item.setSoLuong(item.getSoLuong() + soLuong);
                } else {
                    cart.add(new GioHangItem(sku, ten, hinh, gia, soLuong));
                }

                session.setAttribute("cart", cart);
                break;
            }

            case "update": {
                String sku = req.getParameter("sku");
                int newQty = parseInt(req.getParameter("qty"), 1);
                cart.stream().filter(i -> i.getSku().equals(sku))
                        .findFirst().ifPresent(item -> item.setSoLuong(newQty));
                session.setAttribute("cart", cart);
                break;
            }

            case "remove": {
                String sku = req.getParameter("sku");
                cart.removeIf(i -> i.getSku().equals(sku));
                session.setAttribute("cart", cart);
                break;
            }

            case "clear": {
                cart.clear();
                session.setAttribute("cart", cart);
                break;
            }
        }

        session.setAttribute("cartCount", cart.stream().mapToInt(GioHangItem::getSoLuong).sum());
        resp.sendRedirect(req.getContextPath() + "/cart");
    }
}
