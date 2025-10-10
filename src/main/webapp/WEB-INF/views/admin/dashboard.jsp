<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"  uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi" data-bs-theme="dark">
    <head>
        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <title>Admin Dashboard</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
        <link href="<c:url value='/assets/css/site.css'/>" rel="stylesheet"/>
    </head>
    <body class="bg-body">
        <div class="container py-4">
            <h3 class="mb-3">Bảng điều khiển</h3>
            <p class="text-muted">Xin chào, <strong>${sessionScope.user.fullName}</strong></p>

            <div class="d-flex gap-2 mb-3">
                <a class="btn btn-outline-light me-2" href="${pageContext.request.contextPath}/admin/orders">Quản lý đơn hàng</a>
                <a class="btn btn-outline-light me-2" href="${pageContext.request.contextPath}/admin/products">Quản lý sản phẩm</a>
                <a class="btn btn-outline-light me-2" href="${pageContext.request.contextPath}/admin/customers">Quản lý khách hàng</a>
                <a class="btn btn-outline-light me-2" href="${pageContext.request.contextPath}/admin/revenue">Quản lý doanh thu</a>

                <form method="post" action="<c:url value='/admin/logout'/>">
                    <button class="btn btn-warning" type="submit">Đăng xuất</button>
                </form>
            </div>

            <div class="alert alert-info">Bạn có thể tiếp tục phát triển các thống kê ở đây.</div>
        </div>
    </body>
</html>
