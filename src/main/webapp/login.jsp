<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
  String method = request.getMethod();
  String error = null;
  if ("POST".equalsIgnoreCase(method)) {
    String username = request.getParameter("username");
    String password = request.getParameter("password");
    if (username != null && !username.trim().isEmpty() && password != null && !password.trim().isEmpty()) {
      session.setAttribute("user", username.trim());
      response.sendRedirect("home.jsp");
      return;
    } else {
      error = "Vui lòng nhập tài khoản và mật khẩu";
    }
  }
%>
<html>
<head>
  <meta charset="UTF-8">
  <title>Đăng nhập - HRMS</title>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="robots" content="noindex, nofollow">
  <link rel="icon" href="data:,">
  <meta http-equiv="Content-Security-Policy" content="default-src 'self' 'unsafe-inline' https://cdn.jsdelivr.net https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
  <meta http-equiv="X-Frame-Options" content="SAMEORIGIN">
  <meta http-equiv="X-Content-Type-Options" content="nosniff">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
  <link href="assets/css/styles.css" rel="stylesheet">
</head>
<body>
  <div class="container auth-card">
    <div class="card content-card">
      <div class="card-body">
        <h1 class="h4 mb-3 brand-title">HRMS - Đăng nhập</h1>
        <% if (error != null) { %>
          <div class="alert alert-danger"><%= error %></div>
        <% } %>
        <form method="post" action="login.jsp" class="needs-validation" novalidate>
          <div class="mb-3">
            <label for="username" class="form-label">Tài khoản</label>
            <input id="username" name="username" type="text" class="form-control" autofocus autocomplete="username" required />
            <div class="invalid-feedback">Vui lòng nhập tài khoản</div>
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">Mật khẩu</label>
            <input id="password" name="password" type="password" class="form-control" autocomplete="current-password" required />
            <div class="invalid-feedback">Vui lòng nhập mật khẩu</div>
          </div>
          <button class="btn btn-primary w-100" type="submit">Đăng nhập</button>
          <p class="text-muted mt-2 mb-0" style="font-size:12px">Gợi ý: nhập bất kỳ tài khoản và mật khẩu để vào demo.</p>
        </form>
      </div>
    </div>
  </div>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
  <script>
    (function(){
      var u = document.getElementById('username');
      if (u && !u.value) try { u.focus(); } catch(e) {}
      // Bootstrap validation
      var forms = document.querySelectorAll('.needs-validation');
      Array.prototype.slice.call(forms).forEach(function(form){
        form.addEventListener('submit', function (event) {
          if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
          }
          form.classList.add('was-validated');
        }, false);
      });
    })();
  </script>
  <!-- Lưu ý: Đây là demo phía giao diện. Tích hợp xác thực thật sẽ dùng Servlet/Filter hoặc Spring Security. -->
  </body>
  </html>


