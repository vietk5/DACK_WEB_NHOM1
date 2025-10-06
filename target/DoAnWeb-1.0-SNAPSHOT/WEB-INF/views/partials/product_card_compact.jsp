<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="pName"     value="${param.name}" />
<c:set var="pBrand"    value="${param.brand}" />
<c:set var="pCat"      value="${param.category}" />
<c:set var="pPrice"    value="${param.price}" />
<c:set var="pOld"      value="${param.oldPrice}" />
<c:set var="pRating"   value="${param.rating}" />
<c:set var="pImageRel" value="${empty param.image ? 'assets/img/products/placeholder.png' : param.image}" />

<c:url var="pImageUrl"   value="/${pImageRel}" />
<c:url var="fallbackUrl" value="/assets/img/products/placeholder.png" />

<div class="flash-item">
  <div class="card product-card product-card--sm h-100 position-relative">
    <span class="position-absolute top-0 start-0 m-2 badge rating-badge">★ ${pRating}</span>
    
  <img
    src="${pImageUrl}"
    alt="${pName}"
    onerror="this.onerror=null;this.src='${fallbackUrl}'"
    loading="lazy" decoding="async"
    width="100" height="100"  <%-- vuông cho compact --%>
  >

    <div class="card-body py-2">
      <div class="brand-line">${pBrand} • ${pCat}</div>
      <div class="title-2 mb-1" title="${pName}">${pName}</div>

      <div class="price-block mb-2">
        <small class="text-decoration-line-through text-muted old-price">
          <c:choose>
            <c:when test="${pOld ne null && pOld gt 0}">
              <fmt:formatNumber value="${pOld}" type="number" />
            </c:when>
            <c:otherwise>&nbsp;</c:otherwise> <%-- chừa chỗ nếu không có giá cũ --%>
          </c:choose>
        </small>
        <div class="current-price">
          <fmt:formatNumber value="${pPrice}" type="number" />
        </div>
      </div>

      <div class="actions d-grid">
        <a href="#" class="btn btn-rog btn-sm">Thêm vào giỏ</a>
      </div>
    </div>
  </div>
</div>
