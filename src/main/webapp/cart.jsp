<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ include file="WEB-INF/views/layout_header.jspf" %>

<style>
/* Modern Cart Styling with Better Colors */
.cart-container {
  min-height: 60vh;
  padding: 2rem 0;
  background: linear-gradient(180deg, #f8f9fa 0%, #e9ecef 100%);
  border-radius: 20px;
}

.cart-header {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: #1a1a1a;
  padding: 2.5rem;
  border-radius: 20px 20px 0 0;
  box-shadow: 0 8px 30px rgba(79, 172, 254, 0.3);
  position: relative;
  overflow: hidden;
}

.cart-header::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -10%;
  width: 300px;
  height: 300px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 50%;
}

.cart-header h2 {
  margin: 0;
  font-weight: 700;
  display: flex;
  align-items: center;
  gap: 12px;
  position: relative;
  z-index: 1;
}

.cart-header .cart-icon {
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
  animation: bounce 2s infinite;
}

@keyframes bounce {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.cart-count-badge {
  background: linear-gradient(135deg, #ffeaa7 0%, #fdcb6e 100%);
  color: #2c3e50;
  padding: 0.6rem 1.5rem;
  border-radius: 50px;
  font-size: 1.2rem;
  font-weight: 800;
  box-shadow: 0 6px 20px rgba(253, 203, 110, 0.5);
  display: inline-flex;
  align-items: center;
  gap: 8px;
  border: 3px solid white;
  animation: scaleIn 0.5s ease;
}

.cart-count-badge i {
  color: #00b894;
  font-size: 1.4rem;
}

@keyframes scaleIn {
  from {
    transform: scale(0);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}

.cart-empty {
  text-align: center;
  padding: 5rem 2rem;
  background: white;
  border-radius: 0 0 20px 20px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
}

.cart-empty-icon {
  font-size: 100px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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

.cart-empty h4 {
  color: #2c3e50;
  margin-bottom: 0.75rem;
  font-weight: 700;
  font-size: 1.5rem;
}

.cart-empty p {
  color: #7f8c8d;
  margin-bottom: 2.5rem;
  font-size: 1.1rem;
}

.cart-table {
  background: white;
  border-radius: 0 0 20px 20px;
  overflow: hidden;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
}

.cart-item {
  padding: 2rem;
  border-bottom: 2px solid #f8f9fa;
  transition: all 0.4s ease;
  background: white;
}

.cart-item:hover {
  background: linear-gradient(90deg, #f8f9fa 0%, #e3f2fd 100%);
  transform: translateX(5px);
  box-shadow: 0 4px 15px rgba(79, 172, 254, 0.1);
}

.cart-item:last-child {
  border-bottom: none;
}

.product-image {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 16px;
  box-shadow: 0 6px 20px rgba(79, 172, 254, 0.3);
  transition: all 0.4s ease;
  border: 3px solid #e3f2fd;
}

.product-image:hover {
  transform: scale(1.1) rotate(2deg);
  box-shadow: 0 10px 30px rgba(79, 172, 254, 0.5);
}

.product-info {
  flex: 1;
  padding: 0 1.5rem;
}

.product-name {
  font-size: 1.2rem;
  font-weight: 700;
  color: #2c3e50;
  margin-bottom: 0.75rem;
  line-height: 1.4;
}

.product-sku {
  color: #7f8c8d;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  gap: 6px;
  background: #f8f9fa;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  display: inline-flex;
  font-weight: 500;
}

.product-sku i {
  color: #4facfe;
}

.product-price {
  font-size: 1.4rem;
  font-weight: 800;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  white-space: nowrap;
  margin-top: 0.5rem;
}

.quantity-section {
  display: flex;
  flex-direction: column;
  gap: 10px;
  align-items: center;
}

.quantity-label {
  font-size: 1rem;
  font-weight: 700;
  color: #2c3e50;
  background: linear-gradient(135deg, #ffeaa7 0%, #fdcb6e 100%);
  padding: 0.4rem 1rem;
  border-radius: 20px;
  box-shadow: 0 3px 10px rgba(253, 203, 110, 0.4);
  display: flex;
  align-items: center;
  gap: 6px;
  white-space: nowrap;
}

.quantity-label i {
  font-size: 1.1rem;
  color: #e17055;
}

.quantity-controls {
  display: flex;
  align-items: center;
  gap: 12px;
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
  border-radius: 50px;
  padding: 8px 12px;
  box-shadow: 0 4px 15px rgba(79, 172, 254, 0.3);
  border: 2px solid #4facfe;
}

.qty-btn {
  width: 42px;
  height: 42px;
  border: none;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  border-radius: 50%;
  font-size: 20px;
  font-weight: 700;
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 10px rgba(79, 172, 254, 0.4);
}

.qty-btn:hover {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  transform: scale(1.15);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

.qty-btn:active {
  transform: scale(0.9);
}

.qty-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  background: #95a5a6;
}

.qty-input {
  width: 70px;
  height: 42px;
  border: 3px solid #4facfe;
  border-radius: 12px;
  text-align: center;
  font-weight: 800;
  font-size: 20px;
  color: #2c3e50;
  background: white;
  transition: all 0.3s ease;
  box-shadow: inset 0 2px 6px rgba(79, 172, 254, 0.2);
}

.qty-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.2), inset 0 2px 6px rgba(102, 126, 234, 0.3);
  transform: scale(1.05);
}

.item-subtotal {
  font-size: 1.6rem;
  font-weight: 900;
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  white-space: nowrap;
  min-width: 160px;
  text-align: right;
  text-shadow: 0 2px 10px rgba(238, 90, 111, 0.3);
}

.btn-remove {
  width: 48px;
  height: 48px;
  border: none;
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  color: white;
  border-radius: 50%;
  font-size: 20px;
  cursor: pointer;
  transition: all 0.4s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 15px rgba(255, 107, 107, 0.4);
}

.btn-remove:hover {
  background: linear-gradient(135deg, #c0392b 0%, #e74c3c 100%);
  transform: rotate(180deg) scale(1.2);
  box-shadow: 0 8px 25px rgba(231, 76, 60, 0.6);
}

.cart-summary {
  background: white;
  padding: 2.5rem;
  border-radius: 20px;
  margin-top: 2rem;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.1);
  border: 3px solid #e3f2fd;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.25rem 0;
  border-bottom: 2px dashed #e3f2fd;
}

.summary-row:last-child {
  border-bottom: none;
  padding-top: 2rem;
  margin-top: 1.5rem;
  border-top: 4px solid transparent;
  background: linear-gradient(white, white) padding-box,
              linear-gradient(135deg, #4facfe 0%, #667eea 100%) border-box;
  border-radius: 12px;
  padding: 1.5rem;
  margin: 1.5rem -0.5rem 0 -0.5rem;
}

.summary-label {
  font-size: 1.1rem;
  font-weight: 600;
  color: #7f8c8d;
}

.summary-value {
  font-size: 1.3rem;
  font-weight: 700;
  color: #2c3e50;
}

.summary-total .summary-label {
  font-size: 1.5rem;
  font-weight: 800;
  color: #2c3e50;
}

.summary-total .summary-value {
  font-size: 2.2rem;
  font-weight: 900;
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.btn-checkout {
  width: 100%;
  padding: 1.25rem;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 50%, #667eea 100%);
  border: none;
  color: white;
  font-size: 1.3rem;
  font-weight: 800;
  border-radius: 50px;
  cursor: pointer;
  transition: all 0.4s ease;
  text-transform: uppercase;
  letter-spacing: 2px;
  margin-top: 2rem;
  box-shadow: 0 8px 30px rgba(79, 172, 254, 0.5);
  position: relative;
  overflow: hidden;
}

.btn-checkout::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 50%;
  transform: translate(-50%, -50%);
  transition: width 0.6s, height 0.6s;
}

.btn-checkout:hover::before {
  width: 400px;
  height: 400px;
}

.btn-checkout:hover {
  transform: translateY(-4px) scale(1.02);
  box-shadow: 0 12px 40px rgba(79, 172, 254, 0.7);
}

.btn-clear-all {
  background: white;
  border: 3px solid #ff6b6b;
  color: #ff6b6b;
  padding: 0.6rem 1.8rem;
  border-radius: 50px;
  font-weight: 700;
  transition: all 0.3s ease;
  text-decoration: none;
  display: inline-block;
  box-shadow: 0 4px 15px rgba(255, 107, 107, 0.2);
  position: relative;
  z-index: 1;
}

.btn-clear-all:hover {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  color: white;
  transform: scale(1.08);
  box-shadow: 0 6px 20px rgba(255, 107, 107, 0.5);
}

.btn-continue {
  background: white;
  border: 3px solid #4facfe;
  color: #4facfe;
  padding: 0.9rem 2.5rem;
  border-radius: 50px;
  font-weight: 700;
  transition: all 0.3s ease;
  text-decoration: none;
  display: inline-block;
  margin-top: 1.5rem;
  box-shadow: 0 4px 15px rgba(79, 172, 254, 0.2);
  font-size: 1.1rem;
}

.btn-continue:hover {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
  transform: scale(1.08);
  box-shadow: 0 6px 20px rgba(79, 172, 254, 0.5);
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

.cart-item {
  animation: fadeInUp 0.5s ease;
}

.cart-item:nth-child(1) { animation-delay: 0.1s; }
.cart-item:nth-child(2) { animation-delay: 0.2s; }
.cart-item:nth-child(3) { animation-delay: 0.3s; }
.cart-item:nth-child(4) { animation-delay: 0.4s; }
.cart-item:nth-child(5) { animation-delay: 0.5s; }

/* Responsive */
@media (max-width: 768px) {
  .cart-header {
    padding: 1.5rem;
  }
  
  .cart-header h2 {
    font-size: 1.5rem;
    flex-wrap: wrap;
  }
  
  .cart-item {
    flex-direction: column;
    gap: 1.5rem;
    padding: 1.5rem;
  }
  
  .product-info {
    padding: 0;
    text-align: center;
  }
  
  .product-image {
    width: 150px;
    height: 150px;
  }
  
  .item-subtotal {
    text-align: center;
    font-size: 1.8rem;
  }
  
  .quantity-section {
    width: 100%;
  }
  
  .quantity-label {
    font-size: 1.1rem;
    padding: 0.5rem 1.5rem;
  }
  
  .quantity-controls {
    justify-content: center;
    padding: 10px 15px;
  }
  
  .qty-input {
    font-size: 22px;
    width: 80px;
  }
  
  .btn-remove {
    width: 56px;
    height: 56px;
    font-size: 24px;
  }
  
  .cart-summary {
    padding: 1.5rem;
  }
  
  .summary-total .summary-value {
    font-size: 2rem;
  }
}

/* Toast Notification */
.toast-notification {
  position: fixed;
  top: 100px;
  right: 20px;
  background: white;
  padding: 1rem 1.5rem;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  display: flex;
  align-items: center;
  gap: 12px;
  z-index: 9999;
  animation: slideInRight 0.3s ease;
  border-left: 4px solid #28a745;
}

@keyframes slideInRight {
  from {
    transform: translateX(400px);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

.toast-icon {
  font-size: 24px;
  color: #28a745;
}

.toast-message {
  font-weight: 600;
  color: #333;
}
</style>

<div class="container cart-container">
  <!-- Cart Header -->
  <div class="cart-header">
    <h2>
      <div class="cart-icon">
        <i class="bi bi-cart-fill"></i>
      </div>
      Giỏ Hàng Của Bạn
      <c:if test="${not empty sessionScope.cart}">
        <span class="cart-count-badge">
          <i class="bi bi-check-circle-fill"></i>
          ${sessionScope.cartCount} sản phẩm
        </span>
      </c:if>
    </h2>
    <c:if test="${not empty sessionScope.cart}">
      <div style="margin-top: 1.5rem;">
        <a class="btn-clear-all" href="${pageContext.request.contextPath}/cart?action=clear" 
           onclick="return confirm('Bạn có chắc muốn xóa tất cả sản phẩm?')">
          <i class="bi bi-trash"></i> Xóa tất cả
        </a>
      </div>
    </c:if>
  </div>

  <c:set var="cart" value="${sessionScope.cart}" />
  <c:choose>
    <c:when test="${empty cart}">
      <!-- Empty Cart State -->
      <div class="cart-empty">
        <div class="cart-empty-icon">
          <i class="bi bi-cart-x"></i>
        </div>
        <h4>Giỏ hàng trống</h4>
        <p>Hãy thêm sản phẩm vào giỏ hàng để tiếp tục mua sắm</p>
        <a class="btn-continue" href="${pageContext.request.contextPath}/">
          <i class="bi bi-shop"></i> Tiếp tục mua sắm
        </a>
      </div>
    </c:when>
    <c:otherwise>
      <!-- Cart Items -->
      <div class="cart-table">
        <c:forEach items="${cart}" var="item" varStatus="status">
          <div class="cart-item" style="animation-delay: ${status.index * 0.1}s;">
            <div class="d-flex align-items-center flex-wrap gap-3">
              <!-- Product Image -->
                    <div>
                <img src="${empty item.image ? pageContext.request.contextPath.concat('/assets/img/laptop_placeholder.jpg') : item.image}" 
                     class="product-image" 
                     alt="${item.name}">
              </div>

              <!-- Product Info -->
              <div class="product-info">
                <div class="product-name">${item.name}</div>
                <div class="product-sku">
                  <i class="bi bi-tag"></i> ${item.sku}
                </div>
                <div class="product-price mt-2">
                  <fmt:formatNumber value="${item.price}" type="number" groupingUsed="true"/> đ
                    </div>
                  </div>

              <!-- Quantity Controls -->
              <div class="quantity-section">
                <div class="quantity-label">
                  <i class="bi bi-bag-check-fill"></i> Số lượng
                </div>
                <div class="quantity-controls">
                  <form method="post" action="${pageContext.request.contextPath}/cart" style="display: contents;">
                    <input type="hidden" name="action" value="decrease">
                    <input type="hidden" name="sku" value="${item.sku}">
                    <button type="submit" class="qty-btn" ${item.qty <= 1 ? 'disabled' : ''}>
                      <i class="bi bi-dash"></i>
                    </button>
                  </form>
                  
                  <input type="number" 
                         class="qty-input" 
                         value="${item.qty}" 
                         min="1" 
                         max="99"
                         data-sku="${item.sku}"
                         onchange="updateQuantity(this)"
                         title="Số lượng sản phẩm">
                  
                  <form method="post" action="${pageContext.request.contextPath}/cart" style="display: contents;">
                    <input type="hidden" name="action" value="increase">
                    <input type="hidden" name="sku" value="${item.sku}">
                    <button type="submit" class="qty-btn">
                      <i class="bi bi-plus"></i>
                    </button>
                  </form>
                </div>
              </div>

              <!-- Subtotal -->
              <div class="item-subtotal">
                <fmt:formatNumber value="${item.price * item.qty}" type="number" groupingUsed="true"/> đ
        </div>

              <!-- Remove Button -->
              <form method="get" action="${pageContext.request.contextPath}/cart" style="margin: 0;">
                <input type="hidden" name="action" value="remove">
                <input type="hidden" name="sku" value="${item.sku}">
                <button type="submit" class="btn-remove" 
                        onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?')">
                  <i class="bi bi-x-lg"></i>
                </button>
              </form>
            </div>
          </div>
        </c:forEach>
      </div>

      <!-- Cart Summary -->
      <div class="cart-summary">
        <div class="summary-row">
          <span class="summary-label">Tạm tính</span>
          <span class="summary-value">
            <fmt:formatNumber value="${total}" type="number" groupingUsed="true"/> đ
          </span>
        </div>
        <div class="summary-row">
          <span class="summary-label">Phí vận chuyển</span>
          <span class="summary-value" style="color: #28a745;">Miễn phí</span>
        </div>
        <div class="summary-row summary-total">
          <span class="summary-label">Tổng cộng</span>
          <span class="summary-value">
            <fmt:formatNumber value="${total}" type="number" groupingUsed="true"/> đ
          </span>
        </div>
        
        <a href="${pageContext.request.contextPath}/checkout" class="btn-checkout">
          <i class="bi bi-credit-card"></i> Thanh toán ngay
        </a>
        
        <div class="text-center">
          <a class="btn-continue" href="${pageContext.request.contextPath}/">
            <i class="bi bi-arrow-left"></i> Tiếp tục mua sắm
          </a>
        </div>
      </div>
    </c:otherwise>
  </c:choose>
</div>

<script>
function updateQuantity(input) {
  const sku = input.dataset.sku;
  const qty = parseInt(input.value) || 1;
  
  if (qty < 1) {
    input.value = 1;
    return;
  }
  
  // Create and submit form
  const form = document.createElement('form');
  form.method = 'POST';
  form.action = '${pageContext.request.contextPath}/cart';
  
  const actionInput = document.createElement('input');
  actionInput.type = 'hidden';
  actionInput.name = 'action';
  actionInput.value = 'update';
  
  const skuInput = document.createElement('input');
  skuInput.type = 'hidden';
  skuInput.name = 'sku';
  skuInput.value = sku;
  
  const qtyInput = document.createElement('input');
  qtyInput.type = 'hidden';
  qtyInput.name = 'qty';
  qtyInput.value = qty;
  
  form.appendChild(actionInput);
  form.appendChild(skuInput);
  form.appendChild(qtyInput);
  
  document.body.appendChild(form);
  form.submit();
}

// Add smooth scroll
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
  anchor.addEventListener('click', function (e) {
    e.preventDefault();
    const target = document.querySelector(this.getAttribute('href'));
    if (target) {
      target.scrollIntoView({ behavior: 'smooth' });
    }
  });
});
</script>

<%@ include file="WEB-INF/views/layout_footer.jspf" %>
