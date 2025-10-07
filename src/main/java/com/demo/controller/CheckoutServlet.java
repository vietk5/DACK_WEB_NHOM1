package com.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

  @SuppressWarnings("unchecked")
  private List<Map<String, Object>> getSessionCart(HttpSession session) {
    Object obj = session.getAttribute("cart");
    if (obj instanceof List) return (List<Map<String, Object>>) obj;
    List<Map<String, Object>> cart = new ArrayList<>();
    session.setAttribute("cart", cart);
    return cart;
  }

  private long computeSum(List<Map<String, Object>> cart){
    long total = 0L;
    for (Map<String, Object> it : cart) {
      long price = (Long) it.getOrDefault("price", 0L);
      int qty = (Integer) it.getOrDefault("qty", 0);
      total += price * qty;
    }
    return total;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");
    req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");

    HttpSession session = req.getSession(true);
    List<Map<String, Object>> cart = getSessionCart(session);
    if (cart.isEmpty()) {
      resp.sendRedirect(req.getContextPath() + "/cart");
      return;
    }

    // Collect basic checkout info
    String fullName = req.getParameter("fullName");
    String phone = req.getParameter("phone");
    String address = req.getParameter("address");

    long sum = computeSum(cart);

    // Simulate order creation (store brief summary in session and clear cart)
    Map<String, Object> lastOrder = new HashMap<>();
    lastOrder.put("orderId", UUID.randomUUID().toString().substring(0, 8));
    lastOrder.put("customer", fullName);
    lastOrder.put("phone", phone);
    lastOrder.put("address", address);
    lastOrder.put("total", sum);
    lastOrder.put("items", new ArrayList<>(cart));
    session.setAttribute("lastOrder", lastOrder);

    // Clear cart
    cart.clear();
    session.setAttribute("cart", cart);
    session.setAttribute("cartCount", 0);

    // Redirect to orders page (or success page)
    resp.sendRedirect(req.getContextPath() + "/orders");
  }
}


