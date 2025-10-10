<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="../layout_header.jspf" %>

<div class="container my-4">
  <div class="d-flex justify-content-between align-items-center mb-3">
    <h3 class="mb-0">Quản lý sản phẩm</h3>
    <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/admin">← Dashboard</a>
  </div>

  <form class="card border-0 shadow-sm mb-3" method="get">
    <div class="card-body row g-2">
      <div class="col-md-6">
        <input class="form-control" name="q" placeholder="Tìm theo tên / thương hiệu" value="${q}">
      </div>
      <div class="col-md-2">
        <select class="form-select" name="size">
          <c:forEach var="s" items="${[10,20,50]}">
            <option value="${s}" ${size==s?'selected':''}>${s}/trang</option>
          </c:forEach>
        </select>
      </div>
      <div class="col-md-2">
        <button class="btn btn-rog w-100" type="submit">Lọc</button>
      </div>
    </div>
  </form>

  <div class="card border-0 shadow-sm">
    <div class="table-responsive">
      <table class="table table-dark table-striped align-middle mb-0">
        <thead>
        <tr>
          <th>ID</th>
          <th>Tên sản phẩm</th>
          <th>Thương hiệu</th>
          <th>Loại</th>
          <th class="text-end">Giá</th>
          <th class="text-end">Tồn kho</th>
          <th>Ngày nhập/cập nhật</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${items}" var="p">
          <tr>
            <td>${p.id}</td>
            <td>${p.tenSanPham}</td>
            <td><c:out value="${p.thuongHieu != null ? p.thuongHieu.tenThuongHieu : '-'}"/></td>
            <td><c:out value="${p.loai != null ? p.loai.tenLoai : '-'}"/></td>
            <td class="text-end"><fmt:formatNumber value="${p.gia}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></td>
            <td class="text-end"><c:out value="${empty p.soLuongTon ? 0 : p.soLuongTon}"/></td>
            <!-- model hiện có trường ngayCapPhat (LocalDate) => in raw để tránh fmt-date lỗi LocalDate -->
            <td><c:out value="${p.ngayCapPhat}"/></td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>

    <div class="card-footer d-flex justify-content-between">
      <div>Tổng: ${total}</div>
      <div class="btn-group">
        <c:set var="prev" value="${page>1 ? page-1 : 1}"/>
        <c:set var="next" value="${(page*size)<total ? page+1 : page}"/>
        <a class="btn btn-outline-light btn-sm" href="?q=${q}&size=${size}&page=${prev}">«</a>
        <span class="btn btn-outline-light btn-sm disabled">${page}</span>
        <a class="btn btn-outline-light btn-sm" href="?q=${q}&size=${size}&page=${next}">»</a>
      </div>
    </div>
  </div>
</div>

<%@ include file="../layout_footer.jspf" %>
