<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="WEB-INF/views/layout_header.jspf" %>

<style>
/* Orders Page Styling */
.orders-container {
  min-height: 60vh;
  padding: 2rem 0;
  background: linear-gradient(180deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 20px;
}

.orders-header {
  background: linear-gradient(135deg, #a29bfe 0%, #6c5ce7 100%);
  color: #1a1a1a;
  padding: 2.5rem;
  border-radius: 20px 20px 0 0;
  box-shadow: 0 8px 30px rgba(108, 92, 231, 0.3);
  position: relative;
  overflow: hidden;
}

.orders-header::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -10%;
  width: 300px;
  height: 300px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
}

.orders-header h2 {
  margin: 0;
  font-weight: 700;
  display: flex;
  align-items: center;
  gap: 12px;
  position: relative;
  z-index: 1;
}

.orders-icon {
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, #fd79a8 0%, #e84393 100%);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
  box-shadow: 0 6px 20px rgba(232, 67, 147, 0.4);
  animation: spin 3s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.orders-empty {
  text-align: center;
  padding: 5rem 2rem;
  background: white;
  border-radius: 0 0 20px 20px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
}

.orders-empty-icon {
  font-size: 100px;
  background: linear-gradient(135deg, #a29bfe 0%, #6c5ce7 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin-bottom: 1.5rem;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-20px); }
}

.orders-empty h4 {
  color: #2c3e50;
  margin-bottom: 0.75rem;
  font-weight: 700;
  font-size: 1.5rem;
}

.orders-empty p {
  color: #7f8c8d;
  margin-bottom: 2.5rem;
  font-size: 1.1rem;
}

.orders-table {
  background: white;
  border-radius: 0 0 20px 20px;
  overflow: hidden;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
}

.order-card {
  padding: 2rem;
  border-bottom: 2px solid #f8f9fa;
  transition: all 0.4s ease;
  background: white;
  cursor: pointer;
}

.order-card:hover {
  background: linear-gradient(90deg, #f8f9fa 0%, #e8e7fc 100%);
  transform: translateX(5px);
  box-shadow: 0 4px 15px rgba(162, 155, 254, 0.2);
}

.order-card:last-child {
  border-bottom: none;
}

.order-id {
  font-size: 0.9rem;
  font-weight: 700;
  color: #7f8c8d;
  background: #f8f9fa;
  padding: 0.4rem 1rem;
  border-radius: 20px;
  display: inline-block;
  font-family: 'Courier New', monospace;
}

.order-date {
  font-size: 1rem;
  color: #2c3e50;
  display: flex;
  align-items: center;
  gap: 8px;
  margin-top: 0.5rem;
}

.order-date i {
  color: #a29bfe;
  font-size: 1.1rem;
}

.order-status {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 0.6rem 1.5rem;
  border-radius: 50px;
  font-weight: 700;
  font-size: 0.95rem;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.status-pending {
  background: linear-gradient(135deg, #ffeaa7 0%, #fdcb6e 100%);
  color: #2c3e50;
}

.status-confirmed {
  background: linear-gradient(135deg, #81ecec 0%, #00cec9 100%);
  color: white;
}

.status-shipped {
  background: linear-gradient(135deg, #74b9ff 0%, #0984e3 100%);
  color: white;
}

.status-delivered {
  background: linear-gradient(135deg, #00b894 0%, #00cec9 100%);
  color: white;
}

.status-cancelled {
  background: linear-gradient(135deg, #ff7675 0%, #d63031 100%);
  color: white;
}

.order-total {
  font-size: 1.8rem;
  font-weight: 900;
  background: linear-gradient(135deg, #fd79a8 0%, #e84393 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.btn-view-detail {
  background: linear-gradient(135deg, #a29bfe 0%, #6c5ce7 100%);
  color: white;
  border: none;
  padding: 0.75rem 2rem;
  border-radius: 50px;
  font-weight: 700;
  transition: all 0.3s ease;
  text-decoration: none;
  display: inline-block;
  box-shadow: 0 4px 15px rgba(108, 92, 231, 0.3);
  font-size: 1rem;
}

.btn-view-detail:hover {
  transform: translateY(-3px) scale(1.05);
  box-shadow: 0 8px 25px rgba(108, 92, 231, 0.5);
  color: white;
}

.btn-shop-now {
  background: white;
  border: 3px solid #a29bfe;
  color: #6c5ce7;
  padding: 0.9rem 2.5rem;
  border-radius: 50px;
  font-weight: 700;
  transition: all 0.3s ease;
  text-decoration: none;
  display: inline-block;
  margin-top: 1.5rem;
  box-shadow: 0 4px 15px rgba(162, 155, 254, 0.2);
  font-size: 1.1rem;
}

.btn-shop-now:hover {
  background: linear-gradient(135deg, #a29bfe 0%, #6c5ce7 100%);
  color: white;
  transform: scale(1.08);
  box-shadow: 0 6px 20px rgba(162, 155, 254, 0.5);
}

/* Animations */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.order-card {
  animation: fadeInUp 0.5s ease;
}

.order-card:nth-child(1) { animation-delay: 0.1s; }
.order-card:nth-child(2) { animation-delay: 0.2s; }
.order-card:nth-child(3) { animation-delay: 0.3s; }
.order-card:nth-child(4) { animation-delay: 0.4s; }
.order-card:nth-child(5) { animation-delay: 0.5s; }

/* Responsive */
@media (max-width: 768px) {
  .orders-header {
    padding: 1.5rem;
  }
  
  .orders-header h2 {
    font-size: 1.5rem;
  }
  
  .order-card {
    padding: 1.5rem;
  }
  
  .order-total {
    font-size: 1.5rem;
  }
}
</style>

<div class="container orders-container">
  <!-- Orders Header -->
  <div class="orders-header">
    <h2>
      <div class="orders-icon">
        <i class="bi bi-receipt"></i>
      </div>
      Đơn Hàng Của Tôi
    </h2>
  </div>

  <c:choose>
    <c:when test="${empty orders}">
      <!-- Empty Orders State -->
      <div class="orders-empty">
        <div class="orders-empty-icon">
          <i class="bi bi-inbox"></i>
        </div>
        <h4>Chưa có đơn hàng nào</h4>
        <p>Hãy mua sắm và tạo đơn hàng đầu tiên của bạn</p>
        <a class="btn-shop-now" href="${pageContext.request.contextPath}/">
          <i class="bi bi-shop"></i> Mua sắm ngay
        </a>
      </div>
    </c:when>
    <c:otherwise>
      <!-- Orders List -->
      <div class="orders-table">
        <c:forEach items="${orders}" var="order" varStatus="status">
          <div class="order-card" style="animation-delay: ${status.index * 0.1}s;">
            <div class="row align-items-center g-3">
              <!-- Order Info -->
              <div class="col-lg-3">
                <div class="order-id">
                  <i class="bi bi-hash"></i> ${order.id}
                </div>
                <div class="order-date">
                  <i class="bi bi-calendar-check"></i>
                  <fmt:formatDate value="${order.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                </div>
              </div>

              <!-- Status -->
              <div class="col-lg-3">
                <c:choose>
                  <c:when test="${order.status == 'PENDING'}">
                    <span class="order-status status-pending">
                      <i class="bi bi-clock-history"></i>
                      Chờ xác nhận
                    </span>
                  </c:when>
                  <c:when test="${order.status == 'CONFIRMED'}">
                    <span class="order-status status-confirmed">
                      <i class="bi bi-check-circle"></i>
                      Đã xác nhận
                    </span>
                  </c:when>
                  <c:when test="${order.status == 'SHIPPED'}">
                    <span class="order-status status-shipped">
                      <i class="bi bi-truck"></i>
                      Đang giao hàng
                    </span>
                  </c:when>
                  <c:when test="${order.status == 'DELIVERED'}">
                    <span class="order-status status-delivered">
                      <i class="bi bi-check2-all"></i>
                      Đã giao hàng
                    </span>
                  </c:when>
                  <c:otherwise>
                    <span class="order-status status-cancelled">
                      <i class="bi bi-x-circle"></i>
                      ${order.status.label}
                    </span>
                  </c:otherwise>
                </c:choose>
              </div>

              <!-- Total -->
              <div class="col-lg-3 text-center">
                <div style="color: #7f8c8d; font-size: 0.9rem; margin-bottom: 0.25rem;">Tổng tiền</div>
                <div class="order-total">
                  <fmt:formatNumber value="${order.total}" type="number" groupingUsed="true"/> đ
                </div>
              </div>

              <!-- Action -->
              <div class="col-lg-3 text-end">
                <a class="btn-view-detail" href="${pageContext.request.contextPath}/order?id=${order.id}">
                  <i class="bi bi-eye"></i> Xem chi tiết
                </a>
              </div>
            </div>
          </div>
            </c:forEach>
      </div>
    </c:otherwise>
  </c:choose>
</div>

<%@ include file="WEB-INF/views/layout_footer.jspf" %>
