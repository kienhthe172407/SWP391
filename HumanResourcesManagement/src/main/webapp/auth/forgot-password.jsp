<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quên mật khẩu - Hệ thống Quản lý Nhân sự</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- Google Fonts - Nunito -->
    <link href="https://fonts.googleapis.com/css2?family=Nunito:wght@300;400;600;700;800&display=swap" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/css/auth.css" rel="stylesheet">
</head>
<body class="auth-page">
    <div class="auth-container">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-xl-8 col-lg-10 col-md-9">
                    <div class="card auth-card">
                        <div class="card-body p-0">
                            <div class="row">
                                <div class="col-lg-5 d-none d-lg-block">
                                    <div class="p-4 h-100 d-flex flex-column justify-content-center" 
                                         style="background: linear-gradient(135deg, #4e73df 0%, #224abe 100%); color: white;">
                                        <div class="text-center mb-5">
                                            <i class="fas fa-unlock-alt fa-5x mb-4"></i>
                                            <h3>Khôi phục mật khẩu</h3>
                                            <p class="mt-3">Chúng tôi sẽ gửi hướng dẫn đặt lại mật khẩu đến email của bạn</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-7">
                                    <div class="p-5">
                                        <div class="text-center">
                                            <h1 class="auth-title">Quên mật khẩu?</h1>
                                            <p class="auth-subtitle">Nhập email đã đăng ký để nhận hướng dẫn đặt lại mật khẩu</p>
                                        </div>
                                        
                                        <!-- Show error message if any -->
                                        <% if(request.getAttribute("errorMessage") != null) { %>
                                            <div class="auth-error">
                                                <i class="fas fa-exclamation-circle me-2"></i>
                                                <%= request.getAttribute("errorMessage") %>
                                            </div>
                                        <% } %>
                                        
                                        <!-- Show success message if any -->
                                        <% if(request.getAttribute("successMessage") != null) { %>
                                            <div class="auth-success">
                                                <i class="fas fa-check-circle me-2"></i>
                                                <%= request.getAttribute("successMessage") %>
                                            </div>
                                        <% } %>
                                        
                                        <form class="auth-form" action="${pageContext.request.contextPath}/forgot-password" method="post">
                                            <div class="form-group">
                                                <label class="form-label" for="email">Email</label>
                                                <div class="input-group">
                                                    <span class="input-group-text bg-transparent border-end-0">
                                                        <i class="fas fa-envelope"></i>
                                                    </span>
                                                    <input type="email" class="form-control border-start-0" id="email" name="email" 
                                                           placeholder="Nhập email đã đăng ký" required>
                                                </div>
                                            </div>
                                            <button type="submit" class="btn btn-primary btn-block mt-4">
                                                Gửi yêu cầu <i class="fas fa-paper-plane ms-2"></i>
                                            </button>
                                        </form>
                                        <hr>
                                        <div class="auth-links">
                                            <a href="${pageContext.request.contextPath}/auth/login.jsp">
                                                <i class="fas fa-arrow-left me-1"></i> Quay lại trang đăng nhập
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle with Popper -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>