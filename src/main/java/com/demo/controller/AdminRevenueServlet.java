package com.demo.controller;

import com.demo.enums.TrangThaiDonHang;
import com.demo.persistence.JPAUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;

@WebServlet(name = "AdminRevenueServlet", urlPatterns = {"/admin/revenue"})
public class AdminRevenueServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    req.setCharacterEncoding("UTF-8");
    resp.setCharacterEncoding("UTF-8");

    String period = Optional.ofNullable(req.getParameter("period")).orElse("month"); // day|week|month|quarter|year
    LocalDate toDate = Optional.ofNullable(req.getParameter("to")).map(LocalDate::parse).orElse(LocalDate.now());
    LocalDate fromDate = Optional.ofNullable(req.getParameter("from")).map(LocalDate::parse).orElse(toDate.minusMonths(3));
    boolean csv = "csv".equalsIgnoreCase(req.getParameter("export"));

    String bucket;
    if ("day".equalsIgnoreCase(period)) bucket = "day";
    else if ("week".equalsIgnoreCase(period)) bucket = "week";
    else if ("quarter".equalsIgnoreCase(period)) bucket = "quarter";
    else if ("year".equalsIgnoreCase(period)) bucket = "year";
    else bucket = "month";

    LocalDateTime fromTs = fromDate.atStartOfDay();
    LocalDateTime toTs   = toDate.plusDays(1).atStartOfDay(); // upper exclusive

    EntityManager em = JPAUtil.getEmFactory().createEntityManager();
    try {
      Number totalRevenueN = em.createQuery(
          "select coalesce(sum(ct.soLuong*ct.donGia),0) " +
          "from DonHang d join d.chiTiet ct " +
          "where d.trangThai=:st and d.ngayDatHang between :f and :t",
          Number.class)
          .setParameter("st", TrangThaiDonHang.HOAN_TAT)
          .setParameter("f", fromTs)
          .setParameter("t", toTs)
          .getSingleResult();
      double totalRevenue = totalRevenueN == null ? 0.0 : totalRevenueN.doubleValue();

      Long orderCount = em.createQuery(
          "select count(distinct d.id) from DonHang d " +
          "where d.trangThai=:st and d.ngayDatHang between :f and :t",
          Long.class)
          .setParameter("st", TrangThaiDonHang.HOAN_TAT)
          .setParameter("f", fromTs)
          .setParameter("t", toTs)
          .getSingleResult();
      double avgOrder = (orderCount != null && orderCount > 0) ? totalRevenue / orderCount : 0.0;

      String ql =
          "select function('date_trunc','" + bucket + "', d.ngayDatHang), " +
          "       sum(ct.soLuong * ct.donGia) " +
          "from DonHang d join d.chiTiet ct " +
          "where d.trangThai = :done and d.ngayDatHang between :f and :t " +
          "group by function('date_trunc','" + bucket + "', d.ngayDatHang) " +
          "order by 1";

      TypedQuery<Object[]> q = em.createQuery(ql, Object[].class)
          .setParameter("done", TrangThaiDonHang.HOAN_TAT)
          .setParameter("f", fromTs)
          .setParameter("t", toTs);

      var weekFields = WeekFields.ISO;
      List<String> labels = new ArrayList<>();
      List<Double> values = new ArrayList<>();

      for (Object[] row : q.getResultList()) {
        // label
        java.time.LocalDateTime ldt;
        Object ts = row[0];
        if (ts instanceof java.time.LocalDateTime) ldt = (java.time.LocalDateTime) ts;
        else if (ts instanceof java.sql.Timestamp) ldt = ((java.sql.Timestamp) ts).toLocalDateTime();
        else ldt = fromTs;

        String label;
        switch (bucket) {
          case "day":     label = ldt.toLocalDate().toString(); break;
          case "week":    label = String.format("W%02d %d",
                            ldt.get(weekFields.weekOfWeekBasedYear()),
                            ldt.get(weekFields.weekBasedYear())); break;
          case "quarter": label = "Q" + ((ldt.getMonthValue()-1)/3 + 1) + " " + ldt.getYear(); break;
          case "year":    label = Integer.toString(ldt.getYear()); break;
          default:        label = String.format("%02d/%d", ldt.getMonthValue(), ldt.getYear());
        }

        labels.add(label);

        // value (Number -> double)
        Number revN = (Number) row[1];
        values.add(revN == null ? 0.0 : revN.doubleValue());
      }

      if (csv) {
        resp.setContentType("text/csv; charset=UTF-8");
        resp.setHeader("Content-Disposition", "attachment; filename=revenue.csv");
        try (PrintWriter out = resp.getWriter()) {
          out.println("label,value");
          for (int i = 0; i < labels.size(); i++) {
            out.printf("%s,%.2f%n", labels.get(i).replace(",", " "), values.get(i));
          }
        }
        return;
      }

      req.setAttribute("period", period);
      req.setAttribute("from", fromDate);
      req.setAttribute("to", toDate);
      req.setAttribute("labels", labels);
      req.setAttribute("values", values);
      req.setAttribute("totalRevenue", totalRevenue);
      req.setAttribute("orderCount", orderCount);
      req.setAttribute("avgOrder", avgOrder);

      req.getRequestDispatcher("/WEB-INF/views/admin/revenue.jsp").forward(req, resp);

    } finally {
      if (em.isOpen()) em.close();
    }
  }
}
