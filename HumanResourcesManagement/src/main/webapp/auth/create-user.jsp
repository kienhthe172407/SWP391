<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tạo Tài Khoản Mới - Hệ Thống Quản Lý Nhân Sự</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
    <style>
        .create-user-container {
            max-width: 800px;
            margin: 20px auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        
        .form-header {
            text-align: center;
            margin-bottom: 30px;
            color: #2c3e50;
        }
        
        .form-header h1 {
            margin: 0;
            font-size: 28px;
            font-weight: 600;
        }
        
        .form-header p {
            margin: 10px 0 0 0;
            color: #7f8c8d;
            font-size: 16px;
        }
        
        .form-row {
            display: flex;
            gap: 20px;
            margin-bottom: 20px;
        }
        
        .form-group {
            flex: 1;
            margin-bottom: 20px;
        }
        
        .form-group label {
            display: block;
            margin-bottom: 8px;
            font-weight: 600;
            color: #2c3e50;
            font-size: 14px;
        }
        
        .form-group input,
        .form-group select {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e1e8ed;
            border-radius: 8px;
            font-size: 16px;
            transition: border-color 0.3s ease;
            box-sizing: border-box;
        }
        
        .form-group input:focus,
        .form-group select:focus {
            outline: none;
            border-color: #3498db;
            box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
        }
        
        .required {
            color: #e74c3c;
        }
        
        .form-actions {
            display: flex;
            gap: 15px;
            justify-content: center;
            margin-top: 30px;
        }
        
        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 8px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
            text-decoration: none;
            display: inline-block;
            text-align: center;
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #3498db, #2980b9);
            color: white;
        }
        
        .btn-primary:hover {
            background: linear-gradient(135deg, #2980b9, #1f5f8b);
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(52, 152, 219, 0.3);
        }
        
        .btn-secondary {
            background: #95a5a6;
            color: white;
        }
        
        .btn-secondary:hover {
            background: #7f8c8d;
            transform: translateY(-2px);
        }
        
        .alert {
            padding: 15px 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-weight: 500;
        }
        
        .alert-error {
            background: #fdf2f2;
            border: 1px solid #fecaca;
            color: #dc2626;
        }
        
        .alert-success {
            background: #f0fdf4;
            border: 1px solid #bbf7d0;
            color: #16a34a;
        }
        
        .back-link {
            display: inline-block;
            margin-bottom: 20px;
            color: #3498db;
            text-decoration: none;
            font-weight: 500;
        }
        
        .back-link:hover {
            text-decoration: underline;
        }
        
        .help-text {
            font-size: 12px;
            color: #7f8c8d;
            margin-top: 5px;
        }
        
        @media (max-width: 768px) {
            .form-row {
                flex-direction: column;
                gap: 0;
            }
            
            .create-user-container {
                margin: 10px;
                padding: 20px;
            }
        }
    </style>
</head>
<body>
    <div class="create-user-container">
        <a href="${pageContext.request.contextPath}/dashboard/admin-dashboard.jsp" class="back-link">
            ← Quay lại Dashboard
        </a>
        
        <div class="form-header">
            <h1>👤 Tạo Tài Khoản Mới</h1>
            <p>Tạo tài khoản cho nhân viên mới trong hệ thống</p>
        </div>
        
        <!-- Hiển thị thông báo lỗi -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">
                ${errorMessage}
            </div>
        </c:if>
        
        <!-- Hiển thị thông báo thành công -->
        <c:if test="${not empty successMessage}">
            <div class="alert alert-success">
                ${successMessage}
            </div>
        </c:if>
        
        <form method="post" action="${pageContext.request.contextPath}/create-user">
            <!-- Thông tin đăng nhập -->
            <h3 style="color: #2c3e50; margin-bottom: 20px; border-bottom: 2px solid #ecf0f1; padding-bottom: 10px;">
                 Thông Tin Đăng Nhập
            </h3>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="username">Tên đăng nhập <span class="required">*</span></label>
                    <input type="text" id="username" name="username" 
                           value="${param.username}" 
                           placeholder="Nhập tên đăng nhập" required>
                    <div class="help-text">Tên đăng nhập phải là duy nhất trong hệ thống</div>
                </div>
                
                <div class="form-group">
                    <label for="email">Email <span class="required">*</span></label>
                    <input type="email" id="email" name="email" 
                           value="${param.email}" 
                           placeholder="example@company.com" required>
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="password">Mật khẩu <span class="required">*</span></label>
                    <input type="password" id="password" name="password" 
                           placeholder="Nhập mật khẩu" required>
                    <div class="help-text">Mật khẩu phải có ít nhất 6 ký tự</div>
                </div>
                
                <div class="form-group">
                    <label for="confirmPassword">Xác nhận mật khẩu <span class="required">*</span></label>
                    <input type="password" id="confirmPassword" name="confirmPassword" 
                           placeholder="Nhập lại mật khẩu" required>
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="role">Vai trò <span class="required">*</span></label>
                    <select id="role" name="role" required>
                        <option value="">-- Chọn vai trò --</option>
                        <option value="Employee" ${param.role == 'Employee' ? 'selected' : ''}>Nhân viên</option>
                        <option value="HR" ${param.role == 'HR' ? 'selected' : ''}>Nhân viên HR</option>
                        <option value="HR Manager" ${param.role == 'HR Manager' ? 'selected' : ''}>Quản lý HR</option>
                        <option value="Dept Manager" ${param.role == 'Dept Manager' ? 'selected' : ''}>Quản lý phòng ban</option>
                        <option value="Admin" ${param.role == 'Admin' ? 'selected' : ''}>Quản trị viên</option>
                    </select>
                </div>
            </div>
            
            <!-- Thông tin cá nhân -->
            <h3 style="color: #2c3e50; margin: 30px 0 20px 0; border-bottom: 2px solid #ecf0f1; padding-bottom: 10px;">
                👨‍💼 Thông Tin Cá Nhân
            </h3>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="firstName">Họ</label>
                    <input type="text" id="firstName" name="firstName" 
                           value="${param.firstName}" 
                           placeholder="Nhập họ">
                </div>
                
                <div class="form-group">
                    <label for="lastName">Tên</label>
                    <input type="text" id="lastName" name="lastName" 
                           value="${param.lastName}" 
                           placeholder="Nhập tên">
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="phone">Số điện thoại</label>
                    <input type="tel" id="phone" name="phone" 
                           value="${param.phone}" 
                           placeholder="0123456789">
                </div>
                
                <div class="form-group">
                    <label for="dateOfBirth">Ngày sinh</label>
                    <input type="date" id="dateOfBirth" name="dateOfBirth" 
                           value="${param.dateOfBirth}">
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="gender">Giới tính</label>
                    <select id="gender" name="gender">
                        <option value="">-- Chọn giới tính --</option>
                        <option value="Nam" ${param.gender == 'Nam' ? 'selected' : ''}>Nam</option>
                        <option value="Nữ" ${param.gender == 'Nữ' ? 'selected' : ''}>Nữ</option>
                        <option value="Khác" ${param.gender == 'Khác' ? 'selected' : ''}>Khác</option>
                    </select>
                </div>
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">
                    ✅ Tạo Tài Khoản
                </button>
                <a href="${pageContext.request.contextPath}/dashboard/admin-dashboard.jsp" class="btn btn-secondary">
                    ❌ Hủy
                </a>
            </div>
        </form>
    </div>
    
    <script>
        // Kiểm tra mật khẩu khớp nhau
        document.getElementById('confirmPassword').addEventListener('input', function() {
            const password = document.getElementById('password').value;
            const confirmPassword = this.value;
            
            if (password !== confirmPassword) {
                this.setCustomValidity('Mật khẩu không khớp');
            } else {
                this.setCustomValidity('');
            }
        });
        
        // Kiểm tra độ dài mật khẩu
        document.getElementById('password').addEventListener('input', function() {
            const password = this.value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            if (password.length < 6) {
                this.setCustomValidity('Mật khẩu phải có ít nhất 6 ký tự');
            } else {
                this.setCustomValidity('');
                // Kiểm tra lại confirm password
                if (confirmPassword && password !== confirmPassword) {
                    document.getElementById('confirmPassword').setCustomValidity('Mật khẩu không khớp');
                } else {
                    document.getElementById('confirmPassword').setCustomValidity('');
                }
            }
        });
        
        // Tự động tạo username từ email
        document.getElementById('email').addEventListener('input', function() {
            const email = this.value;
            const usernameField = document.getElementById('username');
            
            if (email && !usernameField.value) {
                const username = email.split('@')[0];
                usernameField.value = username;
            }
        });
    </script>
</body>
</html>
