<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="layout_header.jspf" %>

<div class="container my-4">
  <h3 class="mb-4">Giỏ hàng</h3>

  <c:choose>
    <c:when test="${empty sessionScope.cart}">
      <div class="alert alert-info">Giỏ hàng trống.  
        <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-light ms-2">Tiếp tục mua hàng</a>
      </div>
    </c:when>

    <c:otherwise>
      <table class="table table-dark table-striped text-center align-middle">
        <thead>
          <tr>
            <th>Hình</th>
            <th>Sản phẩm</th>
            <th>Giá</th>
            <th>Số lượng</th>
            <th>Tổng</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <c:set var="sum" value="0"/>
          <c:forEach var="it" items="${sessionScope.cart}">
            <tr>
              <td><img src="${pageContext.request.contextPath}/${it.hinh}" width="60" class="rounded"
                       onerror="this.onerror=null; this.src='${pageContext.request.contextPath}/assets/img/laptop_placeholder.jpg';"/></td>
              <td>${it.ten}</td>
              <td><fmt:formatNumber value="${it.gia}" type="number"/> đ</td>
              <td>${it.soLuong}</td>
              <td><fmt:formatNumber value="${it.gia * it.soLuong}" type="number"/> đ</td>
              <td>
                <form method="post" action="${pageContext.request.contextPath}/cart">
                  <input type="hidden" name="action" value="remove">
                  <input type="hidden" name="sku" value="${it.sku}">
                  <button class="btn btn-outline-danger btn-sm">Xóa</button>
                </form>
              </td>
            </tr>
            <c:set var="sum" value="${sum + (it.gia * it.soLuong)}"/>
          </c:forEach>
        </tbody>
      </table>

      <div class="d-flex justify-content-between align-items-center">
        <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-light">← Tiếp tục mua hàng</a>
        <div>
          <strong class="text-warning me-3 fs-5">Tổng: 
            <fmt:formatNumber value="${sum}" type="number"/> đ</strong>
          <a href="${pageContext.request.contextPath}/checkout" class="btn btn-rog">Thanh toán</a>
        </div>
      </div>
    </c:otherwise>
  </c:choose>
</div>

<%@ include file="layout_footer.jspf" %>
