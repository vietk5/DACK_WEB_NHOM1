<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!doctype html>
<html lang="vi">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <title>Form Nhập Hàng - Nhân viên</title>
  <link href="${pageContext.request.contextPath}/assets/css/receiving-style.css" rel="stylesheet"/>
</head>
<body>
  <div class="container">
    <header>
      <div>
        <h1>Form Nhập Hàng</h1>
        <p>Dành cho nhân viên kho</p>
      </div>
      <div class="help">Vui lòng kiểm tra kỹ trước khi nhấn <strong>Gửi</strong>.</div>
    </header>

    <form id="nhapHangForm" autocomplete="off" action="receiving" method="post">
      <input type="hidden" name="action" value="add"> 
      <div class="two-col">
        <div class="field">
          <label for="idSanPham">ID Sản phẩm</label>
          <input id="idSanPham" name="idSanPham" type="text" maxlength="20" placeholder="VD: SP001" required pattern="[A-Za-z0-9_-]+" title="Chỉ cho phép chữ, số, gạch ngang hoặc gạch dưới" value="${sanPham.id}">
          <div class="help">Mã dùng để định danh (không dấu, không khoảng trắng).</div>
        </div>

        <div class="field">
          <label for="tenSanPham">Tên sản phẩm</label>
          <input id="tenSanPham" name="tenSanPham" type="text" maxlength="120" placeholder="VD: Laptop ASUS VivoBook 15" value="${sanPham.tenSanPham}" required>
        </div>
      </div>

      <div class="two-col">
        <div class="field">
          <label for="thuongHieu">Thương hiệu</label>
          <select id="thuongHieu" name="thuongHieu" required>
            <option value="">-- Chọn thương hiệu --</option>
            <c:forEach var="brandName" items="${brands}">
                <option value="${brandName}">${brandName}</option>
            </c:forEach>
          </select>
        </div>

        <div class="field">
          <label for="loai">Loại sản phẩm</label>
          <select id="loai" name="loaiSanPham" required>
            <option value="">-- Chọn loại sản phẩm --</option>
            <c:forEach var="category" items="${categories}">
                <option value="${category}">${category}</option>
            </c:forEach>
          </select> 
        </div>
      </div>

      <div class="field">
        <label for="moTa">Mô tả</label>
        <textarea id="moTa" name="moTa" placeholder="Mô tả ngắn (tính năng, màu sắc, tình trạng, bảo hành...)" >${sanPham.moTaNgan}</textarea>
      </div>

      <div class="field">
        <label for="gia">Giá (VND)</label>
        <input id="gia" name="gia" type="number" step="0.01" min="0" placeholder="VD: 12500000" value="${sanPham.gia}" required>
        <div class="help">Nhập số (đơn vị VND).</div>
      </div>

      <div class="actions">
        <button type="submit" class="btn-primary">Gửi</button>
        <button type="reset" class="btn-ghost">Xóa</button>
        <!--<button type="reset" class="btn-ghost">Quay về</button>-->
        <div id="formMessage" style="margin-left:auto;font-weight:600"></div>
      </div>
    </form>
        
</body>
</html>
