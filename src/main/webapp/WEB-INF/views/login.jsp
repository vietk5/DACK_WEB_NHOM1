<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ include file="layout_header.jspf" %>

<div class="container my-4">
  <div class="row justify-content-center">
    <div class="col-md-6">
      <div class="card border-0 shadow-sm p-3">
        <h5 class="mb-3">Đăng nhập</h5>
        <c:if test="${not empty error}">
          <div class="alert alert-danger">${error}</div>
        </c:if>
        <form method="post" action="${pageContext.request.contextPath}/login" class="row g-3">
          <div class="col-12">
            <label class="form-label">Tài khoản / Email</label>
            <input class="form-control" name="account" required>
          </div>
          <div class="col-12">
            <label class="form-label">Mật khẩu</label>
            <input class="form-control" type="password" name="password" required>
          </div>
          <div class="col-12">
            <button class="btn btn-rog w-100" type="submit">Đăng nhập</button>
          </div>
          
          <!-- Divider -->
          <div class="col-12">
            <div class="d-flex align-items-center my-2">
              <hr class="flex-grow-1">
              <span class="px-3 text-muted small">HOẶC</span>
              <hr class="flex-grow-1">
            </div>
          </div>
          
          <!-- Google Login Button -->
          <c:if test="${googleOAuthEnabled}">
          <div class="col-12">
            <a href="${pageContext.request.contextPath}/google-login" 
               class="btn btn-outline-secondary w-100 d-flex align-items-center justify-content-center gap-2"
               style="border-color: #dadce0;">
              <svg width="18" height="18" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 48 48">
                <path fill="#EA4335" d="M24 9.5c3.54 0 6.71 1.22 9.21 3.6l6.85-6.85C35.9 2.38 30.47 0 24 0 14.62 0 6.51 5.38 2.56 13.22l7.98 6.19C12.43 13.72 17.74 9.5 24 9.5z"/>
                <path fill="#4285F4" d="M46.98 24.55c0-1.57-.15-3.09-.38-4.55H24v9.02h12.94c-.58 2.96-2.26 5.48-4.78 7.18l7.73 6c4.51-4.18 7.09-10.36 7.09-17.65z"/>
                <path fill="#FBBC05" d="M10.53 28.59c-.48-1.45-.76-2.99-.76-4.59s.27-3.14.76-4.59l-7.98-6.19C.92 16.46 0 20.12 0 24c0 3.88.92 7.54 2.56 10.78l7.97-6.19z"/>
                <path fill="#34A853" d="M24 48c6.48 0 11.93-2.13 15.89-5.81l-7.73-6c-2.15 1.45-4.92 2.3-8.16 2.3-6.26 0-11.57-4.22-13.47-9.91l-7.98 6.19C6.51 42.62 14.62 48 24 48z"/>
                <path fill="none" d="M0 0h48v48H0z"/>
              </svg>
              <span>Tiếp tục với Google</span>
            </a>
          </div>
          </c:if>
          <c:if test="${!googleOAuthEnabled}">
          <div class="col-12">
            <div class="alert alert-info small mb-0">
              <i class="bi bi-info-circle me-1"></i>
              <strong>Google OAuth chưa được cấu hình.</strong><br>
              Xem file <code>GOOGLE_OAUTH_SETUP.md</code> để setup.
            </div>
          </div>
          </c:if>
          <div class="col-12 text-center">
            <a href="${pageContext.request.contextPath}/forgot-password" class="small text-muted">
              Quên mật khẩu?
            </a>
          </div>
          <div class="col-12 text-center">
            Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
          </div>
        </form>
      </div>
    </div>
  </div>
</div>

<%@ include file="layout_footer.jspf" %>
