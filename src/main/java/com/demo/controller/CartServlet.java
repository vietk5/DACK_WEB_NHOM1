package com.demo.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

  @SuppressWarnings("unchecked")
  private List<Map<String, Object>> getSessionCart(HttpSession session) {
    Object obj = session.getAttribute("cart");
    if (obj instanceof List) {
      return (List<Map<String, Object>>) obj;
    }
    List<Map<String, Object>> cart = new ArrayList<>();
    session.setAttribute("cart", cart);
    return cart;
  }

  private static String nvl(String s) { return s == null ? "" : s; }

  private static long parseLongSafe(String v, long def) {
    try { return Long.parseLong(v); } catch (Exception e) { return def; }
  }

  private static int parseIntSafe(String v, int def) {
    try { return Integer.parseInt(v); } catch (Exception e) { return def; }
  }

  private void computeTotals(HttpServletRequest req, List<Map<String, Object>> cart){
    long total = 0L;
    int count = 0;
    for (Map<String, Object> it : cart) {
      long price = (Long) it.getOrDefault("price", 0L);
      int qty = (Integer) it.getOrDefault("qty", 0);
      total += price * qty;
      count += qty;
    }
    req.setAttribute("total", total);
    req.getSession().setAttribute("cartCount", count);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");

    HttpSession session = req.getSession(true);
    List<Map<String, Object>> cart = getSessionCart(session);

    String action = nvl(req.getParameter("action"));
    if ("remove".equalsIgnoreCase(action)) {
      String sku = nvl(req.getParameter("sku"));
      cart.removeIf(it -> sku.equalsIgnoreCase(String.valueOf(it.get("sku"))));
    } else if ("clear".equalsIgnoreCase(action)) {
      cart.clear();
    }

    computeTotals(req, cart);
    req.getRequestDispatcher("/cart.jsp").forward(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");

    HttpSession session = req.getSession(true);
    List<Map<String, Object>> cart = getSessionCart(session);

    String action = nvl(req.getParameter("action"));
    if ("update".equalsIgnoreCase(action)) {
      String sku = nvl(req.getParameter("sku"));
      int qty = Math.max(1, parseIntSafe(req.getParameter("qty"), 1));
      for (Map<String, Object> it : cart) {
        if (sku.equalsIgnoreCase(String.valueOf(it.get("sku")))) {
          it.put("qty", qty);
          break;
        }
      }
    } else {
      // default: add
      String sku = nvl(req.getParameter("sku"));
      String name = nvl(req.getParameter("name"));
      long price = Math.max(0L, parseLongSafe(req.getParameter("price"), 0L));
      int qty = Math.max(1, parseIntSafe(req.getParameter("qty"), 1));
      String image = nvl(req.getParameter("image"));

      Optional<Map<String, Object>> existed = cart.stream()
          .filter(it -> sku.equalsIgnoreCase(String.valueOf(it.get("sku"))))
          .findFirst();
      if (existed.isPresent()) {
        Map<String, Object> it = existed.get();
        int oldQty = (Integer) it.getOrDefault("qty", 0);
        it.put("qty", oldQty + qty);
      } else {
        Map<String, Object> item = new HashMap<>();
        item.put("sku", sku.isEmpty() ? name.replaceAll("\\s+", "-").toLowerCase() : sku);
        item.put("name", name);
        item.put("price", price);
        item.put("qty", qty);
        item.put("image", image);
        cart.add(item);
      }
    }

    // After mutation, forward to cart page
    computeTotals(req, cart);
    req.getRequestDispatcher("/cart.jsp").forward(req, resp);
  }
}


