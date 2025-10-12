package com.demo.controller;

import com.demo.model.cart.GioHangItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(req, resp);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        List<GioHangItem> cart = (List<GioHangItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        String fullName = req.getParameter("fullName");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");

        long total = cart.stream().mapToLong(i -> i.getGia() * i.getSoLuong()).sum();

        Map<String, Object> order = new LinkedHashMap<>();
        order.put("id", UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.put("customerName", fullName);
        order.put("phone", phone);
        order.put("address", address);
        order.put("total", total);
        order.put("createdAt", new Date());
        order.put("items", new ArrayList<>(cart));

        List<Map<String, Object>> orders = (List<Map<String, Object>>) session.getAttribute("orders");
        if (orders == null) orders = new ArrayList<>();
        orders.add(order);
        session.setAttribute("orders", orders);

        cart.clear();
        session.setAttribute("cart", cart);

        resp.sendRedirect(req.getContextPath() + "/orders");
    }
}
