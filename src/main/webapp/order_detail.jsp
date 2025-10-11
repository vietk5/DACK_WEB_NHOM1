<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="WEB-INF/views/layout_header.jspf" %>

<style>
/* Order Detail Page Styling */
.order-detail-container {
  min-height: 60vh;
  padding: 2rem 0;
}

.order-detail-header {
  background: linear-gradient(135deg, #a29bfe 0%, #6c5ce7 100%);
  color: white;
  padding: 2rem;
  border-radius: 20px;
  box-shadow: 0 8px 30px rgba(108, 92, 231, 0.3);
  margin-bottom: 2rem;
}

.order-detail-header h2 {
  margin: 0;
  font-weight: 700;
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.order-detail-icon {
  width: 48px;
  height: 48px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}

.order-detail-badge {
  padding: 0.6rem 1.5rem;
  border-radius: 50px;
  font-weight: 700;
  font-size: 1rem;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.card-modern {
  background: white;
  border-radius: 20px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  border: none;
  overflow: hidden;
}

.card-header-modern {
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  padding: 1.5rem 2rem;
  border-bottom: 3px solid #a29bfe;
  font-weight: 700;
  font-size: 1.1rem;
  color: #2c3e50;
}

.order-item {
  padding: 1.5rem 2rem;
  border-bottom: 2px solid #f8f9fa;
  transition: all 0.3s ease;
}

.order-item:hover {
  background: linear-gradient(90deg, #f8f9fa 0%, #e8e7fc 100%);
}

.order-item:last-child {
  border-bottom: none;
}

.item-name {
  font-size: 1.1rem;
  font-weight: 600;
  color: #2c3e50;
}

.item-qty {
  background: linear-gradient(135deg, #ffeaa7 0%, #fdcb6e 100%);
  color: #2c3e50;
  padding: 0.3rem 1rem;
  border-radius: 20px;
  font-weight: 700;
  display: inline-block;
  margin-left: 0.5rem;
  font-size: 0.9rem;
}

.item-price {
  font-size: 1.3rem;
  font-weight: 800;
  background: linear-gradient(135deg, #fd79a8 0%, #e84393 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.order-total-section {
  background: linear-gradient(135deg, #e8e7fc 0%, #f8f9fa 100%);
  padding: 2rem;
  border-radius: 15px;
  margin-top: 1rem;
}

.order-total-label {
  font-size: 1.3rem;
  font-weight: 700;
  color: #2c3e50;
}

.order-total-value {
  font-size: 2rem;
  font-weight: 900;
  background: linear-gradient(135deg, #fd79a8 0%, #e84393 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.customer-info {
  background: white;
  padding: 2rem;
  border-radius: 15px;
}

.customer-info-item {
  padding: 1rem 0;
  border-bottom: 2px dashed #e9ecef;
}

.customer-info-item:last-child {
  border-bottom: none;
}

.customer-label {
  font-weight: 600;
  color: #7f8c8d;
  font-size: 0.9rem;
  margin-bottom: 0.25rem;
}

.customer-value {
  font-weight: 700;
  color: #2c3e50;
  font-size: 1.1rem;
}

.progress-timeline {
  position: relative;
  padding-left: 2rem;
}

.progress-step {
  position: relative;
  padding: 1.5rem 0;
  padding-left: 2rem;
}

.progress-step::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: #e9ecef;
}

.progress-step.active::before {
  background: linear-gradient(180deg, #a29bfe 0%, #6c5ce7 100%);
}

.progress-dot {
  position: absolute;
  left: -9px;
  top: 1.5rem;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: #e9ecef;
  border: 3px solid white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.progress-step.active .progress-dot {
  background: linear-gradient(135deg, #a29bfe 0%, #6c5ce7 100%);
  width: 24px;
  height: 24px;
  left: -11px;
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0%, 100% {
    box-shadow: 0 2px 8px rgba(108, 92, 231, 0.4);
  }
  50% {
    box-shadow: 0 2px 20px rgba(108, 92, 231, 0.8);
  }
}

.progress-label {
  font-weight: 600;
  color: #2c3e50;
  font-size: 1rem;
}

.progress-step.active .progress-label {
  font-weight: 800;
  color: #6c5ce7;
}

.progress-icon {
  display: inline-block;
  margin-right: 0.5rem;
  font-size: 1.2rem;
}

.progress-step.active .progress-icon {
  color: #6c5ce7;
}

.btn-back {
  background: white;
  border: 3px solid #a29bfe;
  color: #6c5ce7;
  padding: 0.75rem 2rem;
  border-radius: 50px;
  font-weight: 700;
  transition: all 0.3s ease;
  text-decoration: none;
  display: inline-block;
  box-shadow: 0 4px 15px rgba(162, 155, 254, 0.2);
}

.btn-back:hover {
  background: linear-gradient(135deg, #a29bfe 0%, #6c5ce7 100%);
  color: white;
  transform: scale(1.05);
  box-shadow: 0 6px 20px rgba(162, 155, 254, 0.5);
}

/* Responsive */
@media (max-width: 768px) {
  .order-detail-header h2 {
    font-size: 1.3rem;
  }
  
  .order-item {
    padding: 1rem;
  }
  
  .order-total-value {
    font-size: 1.6rem;
  }
}
</style>

<div class="container order-detail-container">
  <!-- Header -->
  <div class="order-detail-header">
    <h2>
      <div class="order-detail-icon">
        <i class="bi bi-receipt-cutoff"></i>
      </div>
      <span>Chi Tiết Đơn Hàng</span>
      <c:choose>
        <c:when test="${order.status == 'PENDING'}">
          <span class="order-detail-badge" style="background: linear-gradient(135deg, #ffeaa7 0%, #fdcb6e 100%); color: #2c3e50;">
            <i class="bi bi-clock-history"></i> Chờ xác nhận
          </span>
        </c:when>
        <c:when test="${order.status == 'CONFIRMED'}">
          <span class="order-detail-badge" style="background: linear-gradient(135deg, #81ecec 0%, #00cec9 100%);">
            <i class="bi bi-check-circle"></i> Đã xác nhận
          </span>
        </c:when>
        <c:when test="${order.status == 'SHIPPED'}">
          <span class="order-detail-badge" style="background: linear-gradient(135deg, #74b9ff 0%, #0984e3 100%);">
            <i class="bi bi-truck"></i> Đang giao hàng
          </span>
        </c:when>
        <c:when test="${order.status == 'DELIVERED'}">
          <span class="order-detail-badge" style="background: linear-gradient(135deg, #00b894 0%, #00cec9 100%);">
            <i class="bi bi-check2-all"></i> Đã giao hàng
          </span>
        </c:when>
        <c:otherwise>
          <span class="order-detail-badge" style="background: linear-gradient(135deg, #ff7675 0%, #d63031 100%);">
            <i class="bi bi-x-circle"></i> ${order.status.label}
          </span>
        </c:otherwise>
      </c:choose>
    </h2>
    <div style="margin-top: 1rem; opacity: 0.95; font-size: 0.95rem;">
      <i class="bi bi-hash"></i> Mã đơn: <strong>${order.id}</strong>
      <span style="margin: 0 1rem;">•</span>
      <i class="bi bi-calendar-check"></i> Ngày tạo: <strong><fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/></strong>
    </div>
  </div>

  <div class="row g-4">
    <!-- Order Items -->
    <div class="col-lg-8">
      <div class="card-modern">
        <div class="card-header-modern">
          <i class="bi bi-bag-check"></i> Sản phẩm đã đặt
        </div>
        <div>
          <c:forEach items="${order.items}" var="item">
            <div class="order-item">
              <div class="d-flex justify-content-between align-items-center flex-wrap gap-2">
                <div class="item-name">
                  ${item.name}
                  <span class="item-qty">
                    <i class="bi bi-x"></i> ${item.qty}
                  </span>
                </div>
                <div class="item-price">
                  <fmt:formatNumber value="${item.subtotal}" type="number" groupingUsed="true"/> đ
                </div>
              </div>
            </div>
          </c:forEach>
        </div>
        
        <div class="order-total-section">
          <div class="d-flex justify-content-between align-items-center">
            <div class="order-total-label">
              <i class="bi bi-wallet2"></i> Tổng tiền thanh toán
            </div>
            <div class="order-total-value">
              <fmt:formatNumber value="${order.total}" type="number" groupingUsed="true"/> đ
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Sidebar -->
    <div class="col-lg-4">
      <!-- Customer Info -->
      <div class="card-modern mb-4">
        <div class="card-header-modern">
          <i class="bi bi-person-circle"></i> Thông tin người nhận
        </div>
        <div class="customer-info">
          <div class="customer-info-item">
            <div class="customer-label">
              <i class="bi bi-person"></i> Họ tên
            </div>
            <div class="customer-value">${order.customerName}</div>
          </div>
          <div class="customer-info-item">
            <div class="customer-label">
              <i class="bi bi-telephone"></i> Số điện thoại
            </div>
            <div class="customer-value">${order.phone}</div>
          </div>
          <div class="customer-info-item">
            <div class="customer-label">
              <i class="bi bi-geo-alt"></i> Địa chỉ
            </div>
            <div class="customer-value">${order.address}</div>
          </div>
        </div>
      </div>

      <!-- Progress Timeline -->
      <div class="card-modern">
        <div class="card-header-modern">
          <i class="bi bi-clock-history"></i> Tiến trình đơn hàng
        </div>
        <div style="padding: 2rem;">
          <div class="progress-timeline">
            <div class="progress-step active">
              <div class="progress-dot"></div>
              <div class="progress-label">
                <span class="progress-icon">📝</span>
                Đã đặt hàng
              </div>
            </div>
            
            <div class="progress-step ${order.status == 'CONFIRMED' || order.status == 'SHIPPED' || order.status == 'DELIVERED' ? 'active' : ''}">
              <div class="progress-dot"></div>
              <div class="progress-label">
                <span class="progress-icon">✅</span>
                Đã xác nhận
              </div>
            </div>
            
            <div class="progress-step ${order.status == 'SHIPPED' || order.status == 'DELIVERED' ? 'active' : ''}">
              <div class="progress-dot"></div>
              <div class="progress-label">
                <span class="progress-icon">🚚</span>
                Đang giao hàng
              </div>
            </div>
            
            <div class="progress-step ${order.status == 'DELIVERED' ? 'active' : ''}">
              <div class="progress-dot"></div>
              <div class="progress-label">
                <span class="progress-icon">🎉</span>
                Đã giao hàng
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Back Button -->
      <div class="mt-4 text-center">
        <a class="btn-back" href="${pageContext.request.contextPath}/orders">
          <i class="bi bi-arrow-left"></i> Quay lại danh sách
        </a>
      </div>
    </div>
  </div>
</div>

<%@ include file="WEB-INF/views/layout_footer.jspf" %>
