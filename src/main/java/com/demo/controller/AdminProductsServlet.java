package com.demo.controller;

import com.demo.model.SanPham;
import com.demo.persistence.JPAUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.*;

@WebServlet(name = "AdminProductsServlet", urlPatterns = {"/admin/products"})
public class AdminProductsServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");

    String q = Optional.ofNullable(req.getParameter("q")).orElse("").trim().toLowerCase();
    int page = parseInt(req.getParameter("page"), 1);
    int size = parseInt(req.getParameter("size"), 20);
    if (page < 1) page = 1; if (size < 1) size = 20;
    int offset = (page - 1) * size;

    EntityManager em = JPAUtil.getEmFactory().createEntityManager();
    try {
      long total = em.createQuery(
          "select count(p) from SanPham p " +
          "where :kw='' or lower(p.tenSanPham) like concat('%',:kw,'%') " +
          "   or lower(p.thuongHieu.tenThuongHieu) like concat('%',:kw,'%')",
          Long.class)
          .setParameter("kw", q)
          .getSingleResult();

      // dùng fetch join để tránh Lazy ở JSP
      List<SanPham> items = em.createQuery(
          "select distinct p from SanPham p " +
          "left join fetch p.thuongHieu th " +
          "left join fetch p.loai lo " +
          "where :kw='' or lower(p.tenSanPham) like concat('%',:kw,'%') " +
          "   or lower(th.tenThuongHieu) like concat('%',:kw,'%') " +
          "order by p.ngayCapPhat desc", SanPham.class)
          .setParameter("kw", q)
          .setFirstResult(offset)
          .setMaxResults(size)
          .getResultList();

      req.setAttribute("items", items);
      req.setAttribute("q", q);
      req.setAttribute("page", page);
      req.setAttribute("size", size);
      req.setAttribute("total", total);

      req.getRequestDispatcher("/WEB-INF/views/admin/products.jsp").forward(req, resp);
    } finally {
      em.close();
    }
  }

  private int parseInt(String s, int def) {
    try { return Integer.parseInt(s); } catch (Exception e) { return def; }
  }
}
