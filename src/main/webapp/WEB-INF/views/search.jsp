<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="cp" value="${pageContext.request.contextPath}" />

<!-- Đặt title để header.jspf dùng -->
<c:set var="pageTitle" value="Tìm kiếm - ElectroMart" scope="request"/>

<!-- Nếu servlet chưa set 'keyword', fallback từ param.q -->
<c:if test="${empty keyword}">
  <c:set var="keyword" value="${param.q}" scope="request"/>
</c:if>

<%@ include file="layout_header.jspf" %>

<!-- Search Results -->
<div class="row">
  <div class="col-12">
    <!-- Search Header -->
    <div class="search-header">
      <div class="d-flex justify-content-between align-items-center">
        <div>
          <h2 class="text-white mb-2">
            <i class="bi bi-search me-2"></i>
            <c:choose>
              <c:when test="${not empty keyword}">
                Kết quả tìm kiếm cho "<span class="text-warning">${fn:escapeXml(keyword)}</span>"
              </c:when>
              <c:otherwise>
                Tất cả sản phẩm
              </c:otherwise>
            </c:choose>
          </h2>
          <div class="search-stats">
            <span class="text-muted">Tìm thấy</span>
            <span class="badge ms-2">${resultCount} sản phẩm</span>
          </div>
        </div>
        <div>
          <a href="${cp}/home" class="btn btn-outline-light">
            <i class="bi bi-house me-1"></i>Về trang chủ
          </a>
        </div>
      </div>
    </div>

    <!-- Filters -->
    <div class="row mb-4">
      <div class="col-12">
        <div class="search-filters">
          <div class="card-body">
            <div class="d-flex align-items-center mb-3">
              <i class="bi bi-funnel text-warning me-2"></i>
              <h5 class="card-title text-white mb-0">Bộ lọc tìm kiếm</h5>
            </div>
            <form method="get" action="${cp}/search">
              <input type="hidden" name="q" value="${fn:escapeXml(keyword)}"/>
              <div class="row g-3">
                <div class="col-md-4">
                  <label class="form-label">
                    <i class="bi bi-tag me-1"></i>Thương hiệu
                  </label>
                  <select class="form-select" name="brand">
                    <option value="">Tất cả thương hiệu</option>
                    <c:forEach items="${brands}" var="b">
                      <option value="${b}" ${b == activeBrand ? 'selected' : ''}>${b}</option>
                    </c:forEach>
                  </select>
                </div>
                <div class="col-md-4">
                  <label class="form-label">
                    <i class="bi bi-grid me-1"></i>Danh mục
                  </label>
                  <select class="form-select" name="category">
                    <option value="">Tất cả danh mục</option>
                    <c:forEach items="${categories}" var="c">
                      <option value="${c}" ${c == activeCategory ? 'selected' : ''}>${c}</option>
                    </c:forEach>
                  </select>
                </div>
                <div class="col-md-4 d-flex align-items-end">
                  <button type="submit" class="btn btn-rog me-2">
                    <i class="bi bi-search me-1"></i>Lọc
                  </button>
                  <a href="${cp}/search" class="btn btn-outline-secondary">
                    <i class="bi bi-x-circle me-1"></i>Xóa bộ lọc
                  </a>
                </div>
              </div>
              <div class="row g-3 mt-1">
                <div class="col-md-4">
                  <label class="form-label"><i class="bi bi-currency-exchange me-1"></i>Khoảng giá (VND)</label>
                  <div class="input-group">
                    <input type="number" class="form-control" name="min" placeholder="Từ" value="${min}">
                    <input type="number" class="form-control" name="max" placeholder="Đến" value="${max}">
                  </div>
                </div>
                <div class="col-md-4">
                  <label class="form-label"><i class="bi bi-sort-down-alt me-1"></i>Sắp xếp</label>
                  <select class="form-select" name="sort">
                    <option value="">Mặc định</option>
                    <option value="price_asc" ${sort == 'price_asc' ? 'selected' : ''}>Giá tăng dần</option>
                    <option value="price_desc" ${sort == 'price_desc' ? 'selected' : ''}>Giá giảm dần</option>
                    <option value="bestseller" ${sort == 'bestseller' ? 'selected' : ''}>Bán chạy nhất</option>
                  </select>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>

    <!-- Search Results -->
    <c:choose>
      <c:when test="${empty searchResults}">
        <div class="no-results">
          <i class="bi bi-search"></i>
          <h4 class="text-white mb-3">Không tìm thấy sản phẩm nào</h4>
          <p class="text-muted mb-4">Hãy thử với từ khóa khác hoặc điều chỉnh bộ lọc</p>
          <div>
            <a href="${cp}/home" class="btn btn-rog me-2">
              <i class="bi bi-house me-1"></i>Về trang chủ
            </a>
            <a href="${cp}/search" class="btn btn-outline-light">
              <i class="bi bi-arrow-clockwise me-1"></i>Xem tất cả sản phẩm
            </a>
          </div>
        </div>
      </c:when>
      <c:otherwise>
        <div class="search-results-grid">
          <c:forEach items="${searchResults}" var="product">
            <div class="search-product-card">
              <!-- Image -->
              <div class="position-relative overflow-hidden">
                <img class="product-img" src="${cp}/${product.image}" alt="${fn:escapeXml(product.name)}"/>
                <c:if test="${product.rating > 0}">
                  <span class="rating-badge position-absolute top-0 start-0 m-2 px-2 py-1 rounded-pill">
                    <i class="bi bi-star-fill me-1"></i>${product.rating}
                  </span>
                </c:if>
                <c:if test="${product.oldPrice != null && product.oldPrice > 0}">
                  <span class="discount-badge position-absolute top-0 end-0 m-2 px-2 py-1 rounded-pill">
                    -<fmt:formatNumber value="${((product.oldPrice - product.price) / product.oldPrice) * 100}"
                                       type="number" maxFractionDigits="0"/>%
                  </span>
                </c:if>
              </div>

              <!-- Body -->
              <div class="card-body d-flex flex-column p-3">
                <h6 class="card-title text-white mb-2" style="font-size: 0.95rem; line-height: 1.3;">
                  <a href="${cp}/product?name=${fn:escapeXml(product.name)}" class="text-white text-decoration-none">
                    ${fn:escapeXml(product.name)}
                  </a>
                </h6>
                <p class="text-muted small mb-2">
                  <i class="bi bi-tag me-1"></i>${fn:escapeXml(product.brand)}
                  <span class="mx-1">•</span>
                  <i class="bi bi-grid me-1"></i>${fn:escapeXml(product.category)}
                </p>

                <div class="mt-auto">
                  <div class="d-flex align-items-center mb-3">
                    <span class="h5 text-warning mb-0 fw-bold">
                      <fmt:formatNumber value="${product.price}" type="currency" currencyCode="VND"/>
                    </span>
                    <c:if test="${product.oldPrice != null && product.oldPrice > 0}">
                      <span class="text-muted text-decoration-line-through ms-2 small">
                        <fmt:formatNumber value="${product.oldPrice}" type="currency" currencyCode="VND"/>
                      </span>
                    </c:if>
                  </div>

                  <div class="d-grid gap-2">
                    <a href="${cp}/product?name=${fn:escapeXml(product.name)}" class="btn btn-outline-light">
                      <i class="bi bi-eye me-1"></i>Xem chi tiết
                    </a>
                    <form method="post" action="${cp}/cart" class="d-grid">
                      <input type="hidden" name="sku" value="${fn:replace(fn:toLowerCase(product.name),' ','-')}">
                      <input type="hidden" name="name" value="${fn:escapeXml(product.name)}">
                      <input type="hidden" name="price" value="${product.price}">
                      <input type="hidden" name="qty" value="1">
                      <input type="hidden" name="image" value="${cp}/${product.image}">
                      <button class="btn btn-rog" type="submit">
                        <i class="bi bi-cart-plus me-1"></i>Thêm vào giỏ
                      </button>
                    </form>
                  </div>
                </div>
              </div>
            </div>
          </c:forEach>
        </div>
      </c:otherwise>
    </c:choose>
  </div>
</div>

<!-- JavaScript for cart functionality -->
<script>
function addToCart(productName, price) {
  alert('Đã thêm ' + productName + ' vào giỏ hàng!');
}
</script>

<%@ include file="layout_footer.jspf" %>
