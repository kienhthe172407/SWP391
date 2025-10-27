<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đổi mật khẩu - Hệ thống Quản lý Nhân sự</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <!-- Google Fonts - Nunito -->
    <link href="https://fonts.googleapis.com/css2?family=Nunito:wght@300;400;600;700;800&display=swap" rel="stylesheet">
    <!-- Custom CSS -->
    <link href="<%= request.getContextPath() %>/css/auth.css" rel="stylesheet">
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
                                            <i class="fas fa-key fa-5x mb-4"></i>
                                            <h3>Đổi mật khẩu</h3>
                                            <p class="mt-3">Cập nhật mật khẩu mới để bảo vệ tài khoản của bạn</p>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-7">
                                    <div class="p-5">
                                        <div class="text-center">
                                            <h1 class="auth-title">Đổi mật khẩu</h1>
                                            <p class="auth-subtitle">Cập nhật mật khẩu mới cho tài khoản của bạn</p>
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
                                        
                                        <form class="auth-form" action="${pageContext.request.contextPath}/change-password" method="post">
                                            <div class="form-group">
                                                <label class="form-label" for="currentPassword">Mật khẩu hiện tại</label>
                                                <div class="input-group">
                                                    <span class="input-group-text bg-transparent border-end-0">
                                                        <i class="fas fa-lock"></i>
                                                    </span>
                                                    <input type="password" class="form-control border-start-0" id="currentPassword" name="currentPassword" 
                                                           placeholder="Nhập mật khẩu hiện tại" required>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="form-label" for="newPassword">Mật khẩu mới</label>
                                                <div class="input-group">
                                                    <span class="input-group-text bg-transparent border-end-0">
                                                        <i class="fas fa-key"></i>
                                                    </span>
                                                    <input type="password" class="form-control border-start-0" id="newPassword" name="newPassword" 
                                                           placeholder="Nhập mật khẩu mới" required minlength="8" title="Mật khẩu phải có ít nhất 8 ký tự">
                                                </div>
                                                <div class="password-strength" id="passwordStrength"></div>
                                                <small class="form-text text-muted">Mật khẩu phải có ít nhất 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt</small>
                                                <div id="newPasswordError" class="text-danger small mt-1" style="display:none;">Mật khẩu phải có ít nhất 8 ký tự.</div>
                                            </div>
                                            <div class="form-group">
                                                <label class="form-label" for="confirmPassword">Xác nhận mật khẩu mới</label>
                                                <div class="input-group">
                                                    <span class="input-group-text bg-transparent border-end-0">
                                                        <i class="fas fa-check-double"></i>
                                                    </span>
                                                    <input type="password" class="form-control border-start-0" id="confirmPassword" name="confirmPassword" 
                                                           placeholder="Nhập lại mật khẩu mới" required minlength="8" title="Mật khẩu phải có ít nhất 8 ký tự">
                                                </div>
                                            </div>
                                            <button type="submit" class="btn btn-primary btn-block mt-4">
                                                Cập nhật mật khẩu <i class="fas fa-save ms-2"></i>
                                            </button>
                                        </form>
                                        <hr>
                                        <div class="auth-links">
                                            <a id="backHomeLink" href="#">
                                                <i class="fas fa-arrow-left me-1"></i> Quay lại trang chủ
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
    
    <!-- Password Strength Script -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const passwordInput = document.getElementById('newPassword');
            const strengthIndicator = document.getElementById('passwordStrength');
            const newPasswordError = document.getElementById('newPasswordError');
            
            passwordInput.addEventListener('input', function() {
                const password = passwordInput.value;
                let strength = 0;
                
                // Check password length
                if (password.length >= 8) {
                    strength += 1;
                    newPasswordError.style.display = 'none';
                    passwordInput.setCustomValidity('');
                } else {
                    newPasswordError.style.display = 'block';
                    passwordInput.setCustomValidity('Mật khẩu phải có ít nhất 8 ký tự');
                }
                
                // Check for uppercase letters
                if (/[A-Z]/.test(password)) {
                    strength += 1;
                }
                
                // Check for lowercase letters
                if (/[a-z]/.test(password)) {
                    strength += 1;
                }
                
                // Check for numbers
                if (/[0-9]/.test(password)) {
                    strength += 1;
                }
                
                // Check for special characters
                if (/[^A-Za-z0-9]/.test(password)) {
                    strength += 1;
                }
                
                // Update strength indicator
                strengthIndicator.className = 'password-strength';
                
                if (password.length === 0) {
                    strengthIndicator.style.width = '0';
                } else if (strength <= 2) {
                    strengthIndicator.classList.add('strength-weak');
                } else if (strength <= 4) {
                    strengthIndicator.classList.add('strength-medium');
                } else {
                    strengthIndicator.classList.add('strength-strong');
                }
            });
            
            // Check password match
            const confirmInput = document.getElementById('confirmPassword');
            confirmInput.addEventListener('input', function() {
                if (confirmInput.value.length < 8) {
                    confirmInput.setCustomValidity('Mật khẩu phải có ít nhất 8 ký tự');
                } else if (confirmInput.value === passwordInput.value) {
                    confirmInput.setCustomValidity('');
                } else {
                    confirmInput.setCustomValidity('Mật khẩu không khớp');
                }
            });
        });
        
        // Dynamic back-to-home link based on role from session (server-side render fallback to /)
        (function() {
            try {
                var role = "${sessionScope.user.role}";
                var ctx = "${pageContext.request.contextPath}";
                var href = ctx + "/";
                if (role === 'Admin') href = ctx + "/dashboard/admin-dashboard.jsp";
                else if (role === 'HR Manager') href = ctx + "/dashboard/hr-manager-dashboard.jsp";
                else if (role === 'HR') href = ctx + "/dashboard/hr-dashboard.jsp";
                else if (role === 'Dept Manager') href = ctx + "/dashboard/dept-manager-dashboard.jsp";
                else href = ctx + "/dashboard/employee-dashboard.jsp";
                var link = document.getElementById('backHomeLink');
                if (link) link.setAttribute('href', href);
            } catch (e) {}
        })();
    </script>
</body>
</html>