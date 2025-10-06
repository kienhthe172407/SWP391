<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  Object u = session.getAttribute("user");
  if (u == null) {
    response.sendRedirect("login.jsp");
    return;
  }
  String username = String.valueOf(u);
%>
<html>
<head>
  <meta charset="UTF-8">
  <title>Trang chủ - HRMS</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="icon" href="data:,">
  <meta http-equiv="X-Content-Type-Options" content="nosniff">
  <meta http-equiv="X-Frame-Options" content="SAMEORIGIN">
  <meta http-equiv="Content-Security-Policy" content="default-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
  <link href="assets/css/styles.css" rel="stylesheet">
</head>
<body>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
      <a class="navbar-brand brand-title" href="#">HRMS</a>
      <div class="d-flex text-white">
        <span class="me-3">Xin chào, <strong><%= username %></strong></span>
        <a class="btn btn-danger btn-sm" href="logout.jsp">Đăng xuất</a>
      </div>
    </div>
  </nav>
  <main class="container my-4">
    <section class="py-4 py-lg-5 hero">
      <div class="p-4 p-lg-5 bg-white rounded-3 shadow-sm">
        <h1 class="h3 mb-2">Xin chào, <span class="text-primary"><%= username %></span></h1>
        <p class="text-muted mb-0">Chào mừng bạn đến với hệ thống quản trị nhân sự.</p>
      </div>
    </section>

    <section class="row g-3 mb-4">
      <div class="col-6 col-lg-3">
        <div class="card border-0 shadow-sm h-100">
          <div class="card-body">
            <div class="text-muted small mb-1">Nhân viên</div>

          </div>
        </div>
      </div>
      <div class="col-6 col-lg-3">
        <div class="card border-0 shadow-sm h-100">
          <div class="card-body">
            <div class="text-muted small mb-1">Phòng ban</div>

          </div>
        </div>
      </div>
      <div class="col-6 col-lg-3">
        <div class="card border-0 shadow-sm h-100">
          <div class="card-body">
            <div class="text-muted small mb-1">Chấm công hôm nay</div>

          </div>
        </div>
      </div>
      <div class="col-6 col-lg-3">
        <div class="card border-0 shadow-sm h-100">
          <div class="card-body">
            <div class="text-muted small mb-1">Đơn nghỉ chờ duyệt</div>

          </div>
        </div>
      </div>
    </section>

    <section class="row g-3">
      <div class="col-lg-8">
        <div class="card border-0 shadow-sm">
          <div class="card-body">
            <h2 class="h6 mb-3">Tác vụ nhanh</h2>
            <div class="d-flex flex-wrap gap-2">
              <a href="#" class="btn btn-primary btn-sm">Thêm nhân viên</a>
              <a href="#" class="btn btn-outline-primary btn-sm">Chấm công</a>
              <a href="#" class="btn btn-outline-secondary btn-sm">Xuất báo cáo</a>
            </div>
          </div>
        </div>
      </div>
      <div class="col-lg-4">
        <div class="card border-0 shadow-sm">
          <div class="card-body">
            <h2 class="h6 mb-3">Thông báo</h2>
            <ul class="list-unstyled mb-0 small text-muted">
              <li>- Hạn chốt bảng lương: 25 hàng tháng</li>
              <li>- Cập nhật chính sách OT từ Q4</li>
              <li>- Workshop nội bộ thứ 6 tuần này</li>
            </ul>
          </div>
        </div>
      </div>
    </section>
  </main>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
</body>
</html>


