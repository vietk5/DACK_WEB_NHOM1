<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<%@ include file="/WEB-INF/views/layout_header.jspf" %>

<!-- Breadcrumb -->
<nav aria-label="breadcrumb" class="mb-4">
  <ol class="breadcrumb">
    <li class="breadcrumb-item"><a href="${cp}/home" class="text-warning">Trang chủ</a></li>
    <li class="breadcrumb-item"><a href="${cp}/search?category=${fn:escapeXml(product.category)}" class="text-warning">${fn:escapeXml(product.category)}</a></li>
    <li class="breadcrumb-item active text-white">${fn:escapeXml(product.name)}</li>
  </ol>
</nav>

<!-- Product Detail -->
<div class="row mb-5">
  <div class="col-lg-6">
    <!-- Product Image -->
    <div class="product-detail-image position-relative">
      <img src="${cp}/${product.image}" alt="${fn:escapeXml(product.name)}" 
           class="img-fluid rounded-3 shadow" style="width: 100%; max-height: 500px; object-fit: cover;"/>
      
      <!-- Rating Badge -->
      <c:if test="${product.rating > 0}">
        <span class="rating-badge position-absolute top-0 start-0 m-3 px-3 py-2 rounded-pill">
          <i class="bi bi-star-fill me-1"></i>${product.rating}
        </span>
      </c:if>
      
      <!-- Discount Badge -->
      <c:if test="${product.oldPrice != null && product.oldPrice > 0}">
        <span class="discount-badge position-absolute top-0 end-0 m-3 px-3 py-2 rounded-pill">
          -<fmt:formatNumber value="${((product.oldPrice - product.price) / product.oldPrice) * 100}" 
                          type="number" maxFractionDigits="0"/>%
        </span>
      </c:if>
    </div>
  </div>
  
  <div class="col-lg-6">
    <div class="product-detail-info">
      <!-- Product Name -->
      <h1 class="text-white mb-3">${fn:escapeXml(product.name)}</h1>
      
      <!-- Brand & Category -->
      <div class="d-flex align-items-center mb-3">
        <span class="badge bg-primary me-2">
          <i class="bi bi-tag me-1"></i>${fn:escapeXml(product.brand)}
        </span>
        <span class="badge bg-secondary">
          <i class="bi bi-grid me-1"></i>${fn:escapeXml(product.category)}
        </span>
      </div>
      
      <!-- Rating -->
      <c:if test="${product.rating > 0}">
        <div class="d-flex align-items-center mb-3">
          <div class="rating-stars me-2">
            <c:forEach begin="1" end="5" var="i">
              <i class="bi bi-star${i <= product.rating ? '-fill' : ''} text-warning"></i>
            </c:forEach>
          </div>
          <span class="text-muted">(${product.rating}/5)</span>
        </div>
      </c:if>
      
      <!-- Price -->
      <div class="price-section mb-4">
        <div class="d-flex align-items-center">
          <span class="h2 text-warning fw-bold mb-0">
            <fmt:formatNumber value="${product.price}" type="currency" currencyCode="VND"/>
          </span>
          <c:if test="${product.oldPrice != null && product.oldPrice > 0}">
            <span class="text-muted text-decoration-line-through ms-3 h5">
              <fmt:formatNumber value="${product.oldPrice}" type="currency" currencyCode="VND"/>
            </span>
            <span class="badge bg-danger ms-2">
              Tiết kiệm <fmt:formatNumber value="${product.oldPrice - product.price}" type="currency" currencyCode="VND"/>
            </span>
          </c:if>
        </div>
      </div>
      
      <!-- Product Description -->
      <div class="product-description mb-4">
        <h5 class="text-white mb-3">Mô tả sản phẩm</h5>
        <div class="card bg-dark border-secondary">
          <div class="card-body">
            <p class="text-muted mb-0">
              Sản phẩm ${fn:escapeXml(product.name)} từ thương hiệu ${fn:escapeXml(product.brand)} 
              thuộc danh mục ${fn:escapeXml(product.category)}. 
              <c:if test="${product.rating > 0}">
                Được đánh giá ${product.rating}/5 sao bởi khách hàng.
              </c:if>
              <c:if test="${product.oldPrice != null && product.oldPrice > 0}">
                Hiện đang có chương trình giảm giá đặc biệt!
              </c:if>
            </p>
          </div>
        </div>
      </div>
      
      <!-- Add to Cart -->
      <div class="add-to-cart-section">
        <div class="row g-3">
          <div class="col-md-6">
            <label class="form-label text-white">Số lượng</label>
            <div class="input-group">
              <button class="btn btn-outline-secondary" type="button" onclick="decreaseQuantity()">-</button>
              <input type="number" class="form-control text-center bg-dark border-secondary text-white" 
                     id="quantity" value="1" min="1" max="10">
              <button class="btn btn-outline-secondary" type="button" onclick="increaseQuantity()">+</button>
            </div>
          </div>
          <div class="col-md-6 d-flex align-items-end">
            <form method="post" action="${cp}/cart" class="w-100">
              <input type="hidden" name="sku" value="${fn:replace(fn:toLowerCase(product.name),' ','-')}">
              <input type="hidden" name="name" value="${fn:escapeXml(product.name)}">
              <input type="hidden" name="price" value="${product.price}">
              <input type="hidden" name="image" value="${cp}/${product.image}">
              <input type="hidden" name="qty" id="qtyHidden" value="1">
              <button class="btn btn-rog w-100" type="submit">
                <i class="bi bi-cart-plus me-2"></i>Thêm vào giỏ hàng
              </button>
            </form>
          </div>
        </div>
      </div>
      
      <!-- Product Features -->
      <div class="product-features mt-4">
        <h6 class="text-white mb-3">Tính năng nổi bật</h6>
        <div class="row">
          <div class="col-md-6">
            <ul class="list-unstyled">
              <li class="mb-2"><i class="bi bi-check-circle text-success me-2"></i>Chính hãng 100%</li>
              <li class="mb-2"><i class="bi bi-check-circle text-success me-2"></i>Bảo hành chính thức</li>
            </ul>
          </div>
          <div class="col-md-6">
            <ul class="list-unstyled">
              <li class="mb-2"><i class="bi bi-check-circle text-success me-2"></i>Giao hàng nhanh</li>
              <li class="mb-2"><i class="bi bi-check-circle text-success me-2"></i>Hỗ trợ 24/7</li>
            </ul>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<!-- Related Products -->
<c:if test="${not empty relatedProducts}">
  <div class="related-products">
    <h3 class="text-white mb-4">
      <i class="bi bi-grid me-2"></i>Sản phẩm liên quan
    </h3>
    <div class="row">
      <c:forEach items="${relatedProducts}" var="relatedProduct">
        <div class="col-lg-3 col-md-4 col-sm-6 mb-4">
          <div class="card product-card h-100 d-flex flex-column shadow-sm border-0 bg-dark">
            <!-- Image -->
            <div class="product-img-wrap position-relative overflow-hidden">
              <img class="product-img w-100" src="${cp}/${relatedProduct.image}" 
                   alt="${fn:escapeXml(relatedProduct.name)}" 
                   style="height: 200px; object-fit: cover;"/>
              <c:if test="${relatedProduct.rating > 0}">
                <span class="rating-badge position-absolute top-0 start-0 m-2 px-2 py-1 rounded-pill">
                  <i class="bi bi-star-fill me-1"></i>${relatedProduct.rating}
                </span>
              </c:if>
            </div>

            <!-- Body -->
            <div class="card-body d-flex flex-column p-3">
              <h6 class="card-title text-white mb-2" style="font-size: 0.95rem; line-height: 1.3;">
                <a href="${cp}/product?name=${fn:escapeXml(relatedProduct.name)}" class="text-white text-decoration-none">
                  ${fn:escapeXml(relatedProduct.name)}
                </a>
              </h6>
              <p class="text-muted small mb-2">
                <i class="bi bi-tag me-1"></i>${fn:escapeXml(relatedProduct.brand)} 
                <span class="mx-1">•</span>
                <i class="bi bi-grid me-1"></i>${fn:escapeXml(relatedProduct.category)}
              </p>
              
              <div class="mt-auto">
                <div class="d-flex align-items-center mb-3">
                  <span class="h6 text-warning mb-0 fw-bold">
                    <fmt:formatNumber value="${relatedProduct.price}" type="currency" currencyCode="VND"/>
                  </span>
                  <c:if test="${relatedProduct.oldPrice != null && relatedProduct.oldPrice > 0}">
                    <span class="text-muted text-decoration-line-through ms-2 small">
                      <fmt:formatNumber value="${relatedProduct.oldPrice}" type="currency" currencyCode="VND"/>
                    </span>
                  </c:if>
                </div>
                
                <a href="${cp}/product?name=${fn:escapeXml(relatedProduct.name)}" class="btn btn-rog w-100">
                  <i class="bi bi-eye me-1"></i>Xem chi tiết
                </a>
              </div>
            </div>
          </div>
        </div>
      </c:forEach>
    </div>
  </div>
</c:if>

<!-- JavaScript -->
<script>
function increaseQuantity() {
  const quantityInput = document.getElementById('quantity');
  const currentValue = parseInt(quantityInput.value);
  if (currentValue < 10) {
    quantityInput.value = currentValue + 1;
  }
}

function decreaseQuantity() {
  const quantityInput = document.getElementById('quantity');
  const currentValue = parseInt(quantityInput.value);
  if (currentValue > 1) {
    quantityInput.value = currentValue - 1;
  }
}

// Sync quantity field to hidden input for form submit
document.getElementById('quantity').addEventListener('input', function(){
  document.getElementById('qtyHidden').value = this.value || 1;
});
</script>

<%@ include file="/WEB-INF/views/layout_footer.jspf" %>