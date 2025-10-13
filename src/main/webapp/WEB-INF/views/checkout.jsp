<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="cp" value="${pageContext.request.contextPath}" />
<c:set var="pageTitle" value="Thanh toán - ElectroMart" scope="request"/>

<%@ include file="layout_header.jspf" %>

<c:set var="cart" value="${sessionScope.cart}" />

<!-- Checkout Page -->
<div class="row">
  <div class="col-12">
    <!-- Page Header -->
    <div class="mb-4">
      <h2 class="text-white">
        <i class="bi bi-credit-card me-2"></i>Thanh toán đơn hàng
      </h2>
      <nav aria-label="breadcrumb">
        <ol class="breadcrumb">
          <li class="breadcrumb-item"><a href="${cp}/home" class="text-warning">Trang chủ</a></li>
          <li class="breadcrumb-item"><a href="${cp}/cart" class="text-warning">Giỏ hàng</a></li>
          <li class="breadcrumb-item active text-white">Thanh toán</li>
        </ol>
      </nav>
    </div>

    <!-- Error Message -->
    <c:if test="${not empty error}">
      <div class="alert alert-danger alert-dismissible fade show" role="alert">
        <i class="bi bi-exclamation-triangle me-2"></i>
        ${error}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
      </div>
    </c:if>

    <!-- Main Checkout Form -->
    <div class="row g-4">
      <!-- Left Column - Customer Info & Payment -->
      <div class="col-lg-7">
        <div class="card bg-dark border-secondary mb-4">
          <div class="card-header">
            <h5 class="text-white mb-0">
              <i class="bi bi-person me-2"></i>Thông tin khách hàng
            </h5>
          </div>
          <div class="card-body">
            <form method="post" action="${cp}/checkout" id="checkoutForm">
              <div class="row g-3">
                <div class="col-md-12">
                  <label class="form-label text-white">Họ và tên <span class="text-danger">*</span></label>
                  <input type="text" class="form-control bg-dark text-white border-secondary" 
                         name="fullName" 
                         value="${sessionScope.user.fullName}" 
                         required>
                </div>
                <div class="col-md-6">
                  <label class="form-label text-white">Email <span class="text-danger">*</span></label>
                  <input type="email" class="form-control bg-dark text-white border-secondary" 
                         name="email" 
                         value="${sessionScope.user.email}" 
                         required>
                </div>
                <div class="col-md-6">
                  <label class="form-label text-white">Số điện thoại <span class="text-danger">*</span></label>
                  <input type="tel" class="form-control bg-dark text-white border-secondary" 
                         name="phone" 
                         placeholder="0123456789"
                         required>
                </div>
                <div class="col-12">
                  <label class="form-label text-white">Địa chỉ giao hàng <span class="text-danger">*</span></label>
                  <textarea class="form-control bg-dark text-white border-secondary" 
                            name="address" 
                            rows="3" 
                            placeholder="Nhập địa chỉ chi tiết: Số nhà, tên đường, phường/xã, quận/huyện, tỉnh/thành phố"
                            required></textarea>
                </div>
              </div>
            </form>
          </div>
        </div>

        <!-- Payment Method -->
        <div class="card bg-dark border-secondary">
          <div class="card-header">
            <h5 class="text-white mb-0">
              <i class="bi bi-credit-card me-2"></i>Phương thức thanh toán
            </h5>
          </div>
          <div class="card-body">
            <div class="form-check mb-3">
              <input class="form-check-input" type="radio" name="paymentMethod" id="cod" value="cod" checked>
              <label class="form-check-label text-white" for="cod">
                <i class="bi bi-cash-coin me-2"></i>
                <strong>Thanh toán khi nhận hàng (COD)</strong>
                <p class="text-muted small mb-0 ms-4">Thanh toán bằng tiền mặt khi nhận hàng</p>
              </label>
            </div>
            <div class="form-check mb-3">
              <input class="form-check-input" type="radio" name="paymentMethod" id="bank" value="bank" disabled>
              <label class="form-check-label text-muted" for="bank">
                <i class="bi bi-bank me-2"></i>
                <strong>Chuyển khoản ngân hàng</strong>
                <p class="text-muted small mb-0 ms-4">(Tính năng đang phát triển)</p>
              </label>
            </div>
            <div class="form-check">
              <input class="form-check-input" type="radio" name="paymentMethod" id="card" value="card" disabled>
              <label class="form-check-label text-muted" for="card">
                <i class="bi bi-credit-card-2-front me-2"></i>
                <strong>Thẻ tín dụng/Thẻ ghi nợ</strong>
                <p class="text-muted small mb-0 ms-4">(Tính năng đang phát triển)</p>
              </label>
            </div>
          </div>
        </div>
      </div>

      <!-- Right Column - Order Summary -->
      <div class="col-lg-5">
        <div class="card bg-dark border-secondary sticky-top" style="top: 20px;">
          <div class="card-header">
            <h5 class="text-white mb-0">
              <i class="bi bi-receipt me-2"></i>Tóm tắt đơn hàng
            </h5>
          </div>
          <div class="card-body">
            <!-- Cart Items -->
            <c:if test="${empty cart}">
              <div class="text-center py-4">
                <i class="bi bi-cart-x display-4 text-muted"></i>
                <p class="text-muted mt-3">Giỏ hàng trống</p>
                <a href="${cp}/search" class="btn btn-rog">
                  <i class="bi bi-search me-2"></i>Mua sắm ngay
                </a>
              </div>
            </c:if>

            <c:if test="${not empty cart}">
              <div class="checkout-items mb-3" style="max-height: 400px; overflow-y: auto;">
                <c:set var="total" value="0" scope="page"/>
                <c:forEach items="${cart}" var="item">
                  <div class="d-flex align-items-center mb-3 pb-3 border-bottom border-secondary">
                    <img src="${cp}/${item.hinh}" 
                         alt="${fn:escapeXml(item.ten)}" 
                         class="rounded me-3" 
                         style="width: 60px; height: 60px; object-fit: cover;">
                    <div class="flex-grow-1">
                      <h6 class="text-white mb-1 small">${fn:escapeXml(item.ten)}</h6>
                      <p class="text-muted small mb-0">
                        <fmt:formatNumber value="${item.gia}" type="number" groupingUsed="true"/> đ × ${item.soLuong}
                      </p>
                    </div>
                    <div class="text-end">
                      <span class="text-warning fw-bold">
                        <fmt:formatNumber value="${item.gia * item.soLuong}" type="number" groupingUsed="true"/> đ
                      </span>
                    </div>
                  </div>
                  <c:set var="total" value="${total + (item.gia * item.soLuong)}"/>
                </c:forEach>
              </div>

              <!-- Order Summary -->
              <div class="border-top border-secondary pt-3">
                <div class="d-flex justify-content-between mb-2">
                  <span class="text-muted">Tạm tính:</span>
                  <span class="text-white">
                    <fmt:formatNumber value="${total}" type="number" groupingUsed="true"/> đ
                  </span>
                </div>
                <div class="d-flex justify-content-between mb-2">
                  <span class="text-muted">Phí vận chuyển:</span>
                  <span class="text-success">Miễn phí</span>
                </div>
                <div class="d-flex justify-content-between mb-3">
                  <span class="text-muted">Giảm giá:</span>
                  <span class="text-white">0 đ</span>
                </div>
                <div class="d-flex justify-content-between border-top border-secondary pt-3">
                  <span class="text-white fw-bold h5 mb-0">Tổng cộng:</span>
                  <span class="text-warning fw-bold h5 mb-0">
                    <fmt:formatNumber value="${total}" type="number" groupingUsed="true"/> đ
                  </span>
                </div>
              </div>

              <!-- Checkout Button -->
              <div class="mt-4">
                <button type="submit" form="checkoutForm" class="btn btn-rog w-100 btn-lg">
                  <i class="bi bi-check-circle me-2"></i>Xác nhận đặt hàng
                </button>
                <a href="${cp}/cart" class="btn btn-outline-secondary w-100 mt-2">
                  <i class="bi bi-arrow-left me-2"></i>Quay lại giỏ hàng
                </a>
              </div>

              <!-- Security Note -->
              <div class="alert alert-info mt-3 mb-0 small">
                <i class="bi bi-shield-check me-2"></i>
                Thông tin của bạn được bảo mật và mã hóa
              </div>
            </c:if>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<style>
.checkout-items::-webkit-scrollbar {
  width: 6px;
}

.checkout-items::-webkit-scrollbar-track {
  background: #2c2c2c;
  border-radius: 10px;
}

.checkout-items::-webkit-scrollbar-thumb {
  background: #667eea;
  border-radius: 10px;
}

.checkout-items::-webkit-scrollbar-thumb:hover {
  background: #764ba2;
}

.form-check-input:checked {
  background-color: #667eea;
  border-color: #667eea;
}

.btn-rog {
  transition: all 0.3s ease;
}

.btn-rog:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}
</style>

<%@ include file="layout_footer.jspf" %>

