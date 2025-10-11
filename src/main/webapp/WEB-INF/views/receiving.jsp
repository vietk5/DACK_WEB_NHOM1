<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/sweetalert2@11/dist/sweetalert2.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<!doctype html>
<html lang="vi">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <title>Form Nhập Hàng - Nhân viên</title>
  <link href="${pageContext.request.contextPath}/assets/css/receiving-style.css" rel="stylesheet"/>
</head>
<body>
  <div id="custom-alert" class="alert-popup">
    <i class="fa-solid fa-circle-check"></i> 
    <span class="message-text">Nhận hàng thành công!</span>
</div>
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
<!--        <div class="field">
          <label for="idSanPham">ID Sản phẩm</label>
          <input id="idSanPham" name="idSanPham" type="text" maxlength="20" placeholder="VD: SP001" required pattern="[A-Za-z0-9_-]+" title="Chỉ cho phép chữ, số, gạch ngang hoặc gạch dưới">
          <div class="help">Mã dùng để định danh (không dấu, không khoảng trắng).</div>
        </div>-->

        <div class="field">
          <label for="tenSanPham">Tên sản phẩm</label>
          <input id="tenSanPham" name="tenSanPham" type="text" maxlength="120" placeholder="VD: Laptop ASUS VivoBook 15"  required>
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
        <textarea id="moTa" name="moTaNgan" placeholder="Mô tả ngắn (tính năng, màu sắc, tình trạng, bảo hành...)" ></textarea>
      </div>

<!--      <div class="field">
        <label for="gia">Giá (VND)</label>
        <input id="gia" name="gia" type="number" step="0.01" min="0" placeholder="VD: 12500000" required>
        <div class="help">Nhập số (đơn vị VND).</div>
      </div>-->
      <div class="field">
        <label for="gia">Giá (VND)</label>
        <input id="gia" name="gia" type="text" 
               placeholder="VD: 12.500.000" required
               onkeyup="formatCurrency(this)"> 
        <div class="help">Nhập số (đơn vị VND).</div>
      </div>

      <div class="actions">
        <button type="submit" class="btn-primary">Gửi</button>
        <button type="reset" class="btn-ghost">Xóa</button>
        <!--<button type="reset" class="btn-ghost">Quay về</button>-->
        <div id="formMessage" style="margin-left:auto;font-weight:600"></div>
      </div>
    </form>
    
    <script>
        const urlParams = new URLSearchParams(window.location.search);

        // Hàm làm sạch URL
        function cleanUrlParameters() {
            if (window.history.replaceState) {
                const cleanUrl = window.location.protocol + "//" + window.location.host + window.location.pathname;
                window.history.replaceState({path: cleanUrl}, '', cleanUrl);
            }
        }
        // Thêm sản phẩm mới Thành công
        if (urlParams.get('success') === 'true') {
            Swal.fire({
                icon: 'success',
                title: 'Hoàn tất!',
                text: 'Đã thêm sản phẩm mới thành công!',
                showConfirmButton: false, // Ẩn nút OK
                timer: 5000 // tự động đóng sau 5 giây
            }).then(() => {
                cleanUrlParameters();
            });
        } 

        // Cập nhật số lượng Thành công
        else if (urlParams.get('update_success') === 'true') {
            Swal.fire({
                icon: 'success',
                title: 'Hoàn tất!',
                text: 'Đã cập nhật số lượng tồn kho thành công!',
                showConfirmButton: false,
                timer: 5000
            }).then(() => {
                cleanUrlParameters();
            });
        } 

        // Xử lý Trùng lặp (Cần xác nhận)
        else if (urlParams.get('duplicate') === 'true') {
            const tenSanPham = urlParams.get('tenSanPham');

            Swal.fire({
                icon: 'warning',
                title: 'Sản phẩm đã tồn tại',
                html: "Sản phẩm đã có trong kho.<br>Bạn có muốn cập nhật số lượng tồn kho lên 1 không?",
                showCancelButton: true,
                confirmButtonText: 'Đồng ý Cập nhật',
                cancelButtonText: 'Quay lại',
                reverseButtons: true

            }).then((result) => {
                if (result.isConfirmed) {
                    // Hành động khi nhấn "Đồng ý Cập nhật"
                    // Chuyển hướng lại Servlet với action="confirm_update"
                    window.location.href = '${pageContext.request.contextPath}/receiving?action=confirm_update&tenSanPham=' + encodeURIComponent(tenSanPham);

                } else if (result.dismiss === Swal.DismissReason.cancel) {
                    cleanUrlParameters();
                }
            });
        }

        // Đảm bảo URL được làm sạch nếu không có popup nào được hiển thị
        // Điều này sẽ xử lý các tham số cũ nếu người dùng refresh trang mà không có popup hiển thị
        if (urlParams.has('success') || urlParams.has('update_success') || urlParams.has('duplicate')) {
            // Không làm sạch ngay, vì việc làm sạch đã được xử lý trong .then() hoặc .else if()
        } else {
            cleanUrlParameters();
        }
        
        function formatCurrency(input) {
            // Lấy giá trị hiện tại và loại bỏ tất cả các ký tự không phải số
            let value = input.value.replace(/\D/g, ''); 
            if (value === '') return;
            // định dạng lại số, thêm dấu chấm (.) làm dấu phân cách hàng nghìn
            // định dạng chuẩn theo tiền tệ Việt Nam
            let formattedValue = new Intl.NumberFormat('vi-VN', { 
            }).format(value);
            input.value = formattedValue;
        }

        document.getElementById('nhapHangForm').addEventListener('submit', function() {
            const giaInput = document.getElementById('gia');
            // Loại bỏ dấu chấm, chỉ giữ lại số nguyên thủy để Servlet có thể xử lý BigDecimal
            const rawValue = giaInput.value.replace(/\./g, ''); 
            giaInput.value = rawValue;
            return true;
        });
    </script>
</body>
</html>
