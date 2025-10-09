<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - Hệ thống Quản lý Nhân sự</title>
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
                <div class="col-xl-10 col-lg-12 col-md-9">
                    <div class="card auth-card">
                        <div class="card-body p-0">
                            <div class="row">
                                <div class="col-lg-6 d-none d-lg-block bg-login-image">
                                    <div class="p-5 h-100 d-flex flex-column justify-content-center" 
                                         style="background: linear-gradient(135deg, #4e73df 0%, #224abe 100%); color: white;">
                                        <h1 class="display-4 mb-4">Hệ thống Quản lý Nhân sự</h1>
                                        <p class="lead">Giải pháp toàn diện cho việc quản lý nguồn nhân lực của doanh nghiệp bạn</p>
                                        <div class="mt-5">
                                            <div class="d-flex align-items-center mb-3">
                                                <i class="fas fa-check-circle me-3"></i>
                                                <span>Quản lý thông tin nhân viên</span>
                                            </div>
                                            <div class="d-flex align-items-center mb-3">
                                                <i class="fas fa-check-circle me-3"></i>
                                                <span>Theo dõi hiệu suất làm việc</span>
                                            </div>
                                            <div class="d-flex align-items-center">
                                                <i class="fas fa-check-circle me-3"></i>
                                                <span>Quản lý hợp đồng và lương thưởng</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-6">
                                    <div class="p-5">
                                        <div class="text-center">
                                            <h1 class="auth-title">Đăng nhập</h1>
                                            <p class="auth-subtitle">Nhập thông tin đăng nhập của bạn để truy cập hệ thống</p>
                                        </div>
                                        
                                        <!-- Show error message if any -->
                                        <% if(request.getAttribute("errorMessage") != null) { %>
                                            <div class="auth-error">
                                                <i class="fas fa-exclamation-circle me-2"></i>
                                                <%= request.getAttribute("errorMessage") %>
                                            </div>
                                        <% } %>
                                        
                                        <form class="auth-form" action="${pageContext.request.contextPath}/login" method="post">
                                            <div class="form-group">
                                                <label class="form-label" for="username">Tên đăng nhập</label>
                                                <div class="input-group">
                                                    <span class="input-group-text bg-transparent border-end-0">
                                                        <i class="fas fa-user"></i>
                                                    </span>
                                                    <input type="text" class="form-control border-start-0" id="username" name="username" 
                                                           placeholder="Nhập tên đăng nhập" required>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="form-label" for="password">Mật khẩu</label>
                                                <div class="input-group">
                                                    <span class="input-group-text bg-transparent border-end-0">
                                                        <i class="fas fa-lock"></i>
                                                    </span>
                                                    <input type="password" class="form-control border-start-0" id="password" name="password" 
                                                           placeholder="Nhập mật khẩu" required>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <div class="form-check">
                                                    <input class="form-check-input" type="checkbox" id="rememberMe" name="rememberMe">
                                                    <label class="form-check-label" for="rememberMe">
                                                        Ghi nhớ đăng nhập
                                                    </label>
                                                </div>
                                            </div>
                                            <button type="submit" class="btn btn-primary btn-block">
                                                Đăng nhập <i class="fas fa-sign-in-alt ms-2"></i>
                                            </button>
                                        </form>
                                        <hr>
                                        <div class="auth-links">
                                            <a href="${pageContext.request.contextPath}/auth/forgot-password.jsp">
                                                <i class="fas fa-key me-1"></i> Quên mật khẩu?
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