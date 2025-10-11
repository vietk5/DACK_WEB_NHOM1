<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<c:set var="cp" value="${pageContext.request.contextPath}" />
<c:set var="imgSrc" value="${empty param.image ? cp.concat('/assets/img/placeholder.jpg') : param.image}" />
<c:set var="skuVal" value="${empty param.sku ? fn:replace(param.name,' ','-') : param.sku}" />

<div class="card product-card h-100 d-flex flex-column shadow-sm">
  <!-- Image -->
  <div class="product-img-wrap position-relative">
    <img class="product-img" src="${imgSrc}" alt="${fn:escapeXml(param.name)}"/>
    <c:if test="${not empty param.rating}">
      <span class="rating-badge position-absolute top-0 start-0 m-2 px-2 py-1 rounded-pill">
        ★ ${param.rating}
      </span>
    </c:if>
  </div>

  <!-- Body -->
  <div class="card-body d-flex flex-column">
    <div class="small text-muted product-meta mb-1">
      <c:out value="${param.brand}" /> • <c:out value="${param.category}" />
    </div>

    <h6 class="product-title mb-2" title="${fn:escapeXml(param.name)}">
      <c:out value="${param.name}" />
    </h6>

    <!-- Price -->
    <div class="price-block mb-2">
      <c:choose>
        <c:when test="${not empty param.oldPrice and param.oldPrice ne '0'}">
          <div class="old-price text-decoration-line-through text-muted">
            <fmt:formatNumber value="${param.oldPrice}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ
          </div>
        </c:when>
        <c:otherwise>
          <div class="old-price">&nbsp;</div>
        </c:otherwise>
      </c:choose>

      <div class="current-price">
        <fmt:formatNumber value="${param.price}" type="number" groupingUsed="true" maxFractionDigits="0"/> đ
      </div>
    </div>

    <!-- Actions -->
    <div class="mt-auto d-grid gap-2 product-foot">
      <a class="btn btn-sm btn-rog"
         href="${cp}/cart?action=add
               &sku=${fn:escapeXml(skuVal)}
               &name=${fn:escapeXml(param.name)}
               &price=${param.price}
               &qty=1
               &image=${fn:escapeXml(imgSrc)}">
        Thêm vào giỏ
      </a>
      <a class="btn btn-sm btn-outline-light-subtle" 
         href="${cp}/product?productId=${param.id}">Xem chi tiết</a>
    </div>
  </div>
</div>
