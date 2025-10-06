<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="layout_header.jspf" %>

<div class="container my-4">
  <div class="d-flex justify-content-between align-items-center mb-2">
    <h5 class="mb-0">Giỏ hàng</h5>
    <a class="btn btn-sm btn-outline-light-subtle" href="${pageContext.request.contextPath}/cart?action=clear">Xóa tất cả</a>
  </div>

  <c:set var="cart" value="${sessionScope.cart}" />
  <c:choose>
    <c:when test="${empty cart}">
      <div class="alert alert-info">Giỏ hàng đang trống.</div>
    </c:when>
    <c:otherwise>
      <div class="card border-0 shadow-sm">
        <div class="table-responsive">
          <table class="table align-middle mb-0">
            <thead>
              <tr><th>Sản phẩm</th><th>Đơn giá</th><th width="140">Số lượng</th><th>Tạm tính</th><th></th></tr>
            </thead>
            <tbody>
            <c:forEach items="${cart}" var="it">
              <tr>
                <td>
                  <div class="d-flex align-items-center gap-3">
                    <img src="${empty it.image ? pageContext.request.contextPath.concat('/assets/img/placeholder.jpg') : it.image}" width="64" class="rounded">
                    <div>
                      <div class="fw-semibold">${it.name}</div>
                      <div class="text-muted small">${it.sku}</div>
                    </div>
                  </div>
                </td>
                <td><fmt:formatNumber value="${it.price}" type="number" groupingUsed="true"/> đ</td>
                <td>
                  <form method="post" action="${pageContext.request.contextPath}/cart" class="d-flex">
                    <input type="hidden" name="sku" value="${it.sku}">
                    <input class="form-control form-control-sm text-center" type="number" min="1" name="qty" value="${it.qty}">
                    <button class="btn btn-sm btn-outline-light-subtle ms-2">Cập nhật</button>
                  </form>
                </td>
                <td class="fw-bold text-danger">
                  <fmt:formatNumber value="${it.price * it.qty}" type="number" groupingUsed="true"/> đ
                </td>
                <td>
                  <a class="btn btn-sm btn-outline-danger" href="${pageContext.request.contextPath}/cart?action=remove&sku=${it.sku}">Xóa</a>
                </td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
        <div class="card-body d-flex justify-content-end">
          <div class="text-end">
            <div class="text-muted">Tổng cộng</div>
            <div class="fs-5 fw-bold text-danger">
              <fmt:formatNumber value="${total}" type="number" groupingUsed="true"/> đ
            </div>
            <a class="btn btn-rog mt-2" href="${pageContext.request.contextPath}/checkout">Tiến hành đặt hàng</a>
          </div>
        </div>
      </div>
    </c:otherwise>
  </c:choose>
</div>

<%@ include file="layout_footer.jspf" %>
