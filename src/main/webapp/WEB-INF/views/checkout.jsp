<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="cp" value="${pageContext.request.contextPath}" />
<c:set var="pageTitle" value="Thanh toán - ElectroMart" scope="request"/>

<%@ include file="layout_header.jspf" %>

<c:set var="cart" value="${sessionScope.cart}" />

<c:set var="buyNowCart" value="${sessionScope.buyNowCart}" />
<c:if test="${not empty buyNowCart}">
    <c:set var="cart" value="${buyNowCart}" />
</c:if>

<!-- Checkout Page (stacked sections, no script) -->
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

        <!-- Error -->
        <c:if test="${not empty error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-triangle me-2"></i>${error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Nếu giỏ trống -->
        <c:if test="${empty cart}">
            <div class="card bg-dark border-secondary">
                <div class="card-body text-center py-5">
                    <i class="bi bi-cart-x display-4 text-muted"></i>
                    <p class="text-muted mt-3">Giỏ hàng trống</p>
                    <a href="${cp}/search" class="btn btn-rog">
                        <i class="bi bi-search me-2"></i>Mua sắm ngay
                    </a>
                </div>
            </div>
        </c:if>

        <!-- MAIN FORM: gom tất cả input vào 1 form -->
        <c:if test="${not empty cart}">
            <form method="post" action="${cp}/checkout" id="checkoutForm">

                <!-- 1) ĐỊA CHỈ NHẬN HÀNG -->
                <div class="card bg-dark border-secondary mb-4">
                    <div class="card-header">
                        <h5 class="text-white mb-0"><i class="bi bi-geo-alt me-2"></i>Địa chỉ nhận hàng</h5>
                    </div>
                    <div class="card-body">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label class="form-label text-white">Họ và tên <span class="text-danger">*</span></label>
                                <input name="fullName" class="form-control bg-dark text-white border-secondary" required value="${fullName}" />
                            </div>
                            <div class="col-md-6">
                                <label class="form-label text-white">Số điện thoại <span class="text-danger">*</span></label>
                                <input name="phone" class="form-control bg-dark text-white border-secondary" required value="${phone}" />
                            </div>
                            <div class="col-md-6">
                                <label class="form-label text-white">Email <span class="text-danger">*</span></label>
                                <input type="email" name="email" class="form-control bg-dark text-white border-secondary" required value="${email}" />
                            </div>
                            <div class="col-12">
                                <label class="form-label text-white">Địa chỉ giao hàng <span class="text-danger">*</span></label>
                                <textarea name="address" rows="2" class="form-control bg-dark text-white border-secondary" required
                                          placeholder="Số nhà, Đường, Phường/Xã, Quận/Huyện, Tỉnh/Thành phố">${address}</textarea>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 2) SẢN PHẨM -->
                <div class="card bg-dark border-secondary mb-4">
                    <div class="card-header">
                        <h5 class="text-white mb-0"><i class="bi bi-bag me-2"></i>Sản phẩm</h5>
                    </div>
                    <div class="card-body">
                        <div class="checkout-items mb-3" style="max-height: 600px; overflow-y: auto;">
                            <c:set var="uiTotal" value="0" scope="page"/>
                            <c:forEach items="${cart}" var="item">
                                <div class="d-flex align-items-center mb-3 pb-3 border-bottom border-secondary">
                                    <img src="${cp}/${item.hinh}" alt="${fn:escapeXml(item.ten)}"
                                         class="rounded me-3" style="width:60px;height:60px;object-fit:cover;">
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
                                <c:set var="uiTotal" value="${uiTotal + (item.gia * item.soLuong)}"/>
                            </c:forEach>
                        </div>
                    </div>
                </div>

                <!-- 3) VOUCHER (không script, submit server-side để preview) -->
                <div class="card bg-dark border-secondary mb-4">
                    <div class="card-header">
                        <h5 class="text-white mb-0"><i class="bi bi-ticket-perforated me-2"></i>Mã giảm giá</h5>
                    </div>
                    <div class="card-body">
                        <div class="input-group">
                            <input type="text" name="voucherCode"
                                   value="${appliedVoucherCode}"
                                   class="form-control bg-dark text-white border-secondary"
                                   placeholder="Nhập mã voucher (nếu có)">
                            <button class="btn btn-outline-secondary" type="submit" name="action" value="applyVoucher">
                                Áp dụng
                            </button>
                        </div>
                        <c:if test="${not empty appliedVoucherCode}">
                            <div class="text-success small mt-2">
                                Đang áp dụng: <strong>${appliedVoucherCode}</strong>
                            </div>
                        </c:if>
                    </div>
                </div>

                <!-- 4) PHƯƠNG THỨC THANH TOÁN -->
                <div class="card bg-dark border-secondary mb-4">
                    <div class="card-header">
                        <h5 class="text-white mb-0"><i class="bi bi-credit-card me-2"></i>Phương thức thanh toán</h5>
                    </div>
                    <div class="card-body">
                        <div class="form-check mb-3">
                            <input class="form-check-input" type="radio" name="paymentMethod" id="cod" value="cod" checked>
                            <label class="form-check-label text-white" for="cod">
                                <i class="bi bi-cash-coin me-2"></i><strong>Thanh toán khi nhận hàng (COD)</strong>
                                <p class="text-muted small mb-0 ms-4">Thanh toán bằng tiền mặt khi nhận hàng.</p>
                            </label>
                        </div>

                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="paymentMethod" id="vnpay" value="vnpay">
                            <label class="form-check-label text-white" for="vnpay">
                                <i class="bi bi-credit-card-2-front me-2"></i><strong>Thanh toán qua VNPAY</strong>
                                <p class="text-muted small mb-0 ms-4">Thanh toán an toàn qua cổng VNPAY bằng thẻ ATM, Visa, Master Card.</p>
                            </label>
                        </div>
                    </div>
                </div>

                <!-- 5) TÓM TẮT & ĐẶT HÀNG -->
                <c:choose>
                    <c:when test="${not empty subtotal}">
                        <c:set var="sumSubtotal" value="${subtotal}" />
                        <c:set var="sumShip"     value="${shippingFee}" />
                        <c:set var="sumDiscount" value="${discount}" />
                        <c:set var="sumGrand"    value="${grandTotal}" />
                    </c:when>
                    <c:otherwise>
                        <c:set var="sumSubtotal" value="${uiTotal}" />
                        <c:set var="sumShip"     value="0" />
                        <c:set var="sumDiscount" value="0" />
                        <c:set var="sumGrand"    value="${uiTotal}" />
                    </c:otherwise>
                </c:choose>

                <div class="card bg-dark border-secondary mb-4">
                    <div class="card-header">
                        <h5 class="text-white mb-0"><i class="bi bi-receipt me-2"></i>Tóm tắt đơn hàng</h5>
                    </div>
                    <div class="card-body">
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Tạm tính:</span>
                            <span class="text-white">
                                <fmt:formatNumber value="${sumSubtotal}" type="number" groupingUsed="true"/> đ
                            </span>
                        </div>
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Phí vận chuyển:</span>
                            <span class="${sumShip == 0 ? 'text-success' : 'text-white'}">
                                <fmt:formatNumber value="${sumShip}" type="number" groupingUsed="true"/> đ
                            </span>
                        </div>
                        <div class="d-flex justify-content-between mb-3">
                            <span class="text-muted">Giảm giá:</span>
                            <span class="text-white">
                                <fmt:formatNumber value="${sumDiscount}" type="number" groupingUsed="true"/> đ
                            </span>
                        </div>
                        <div class="d-flex justify-content-between border-top border-secondary pt-3">
                            <span class="text-white fw-bold h5 mb-0">Tổng cộng:</span>
                            <span class="text-warning fw-bold h5 mb-0">
                                <fmt:formatNumber value="${sumGrand}" type="number" groupingUsed="true"/> đ
                            </span>
                        </div>

                        <div class="mt-4 d-grid gap-2">
                            <button type="submit" name="action" value="placeOrder" class="btn btn-rog btn-lg">
                                <i class="bi bi-check-circle me-2"></i>Xác nhận đặt hàng
                            </button>
                            <a href="${cp}/cart" class="btn btn-outline-secondary">
                                <i class="bi bi-arrow-left me-2"></i>Quay lại giỏ hàng
                            </a>
                        </div>

                        <div class="alert alert-info mt-3 mb-0 small">
                            <i class="bi bi-shield-check me-2"></i>Thông tin của bạn được bảo mật và mã hóa
                        </div>
                    </div>
                </div>

            </form>
        </c:if>

    </div>
</div>

<style>
    .checkout-items::-webkit-scrollbar {
        width: 6px;
    }
    .checkout-items::-webkit-scrollbar-track {
        background:#2c2c2c;
        border-radius:10px;
    }
    .checkout-items::-webkit-scrollbar-thumb {
        background:#667eea;
        border-radius:10px;
    }
    .checkout-items::-webkit-scrollbar-thumb:hover {
        background:#764ba2;
    }
    .form-check-input:checked {
        background-color:#667eea;
        border-color:#667eea;
    }
    .btn-rog {
        transition: all 0.3s ease;
    }
    .btn-rog:hover {
        transform: translateY(-2px);
        box-shadow: 0 8px 20px rgba(102,126,234,.4);
    }
</style>

<%@ include file="layout_footer.jspf" %>

