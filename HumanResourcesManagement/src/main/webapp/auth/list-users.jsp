<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản Lý Người Dùng - Hệ Thống Quản Lý Nhân Sự</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
    <style>
        .users-container {
            max-width: 1200px;
            margin: 20px auto;
            padding: 30px;
            background: white;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        
        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            padding-bottom: 20px;
            border-bottom: 2px solid #ecf0f1;
        }
        
        .page-title {
            color: #2c3e50;
            margin: 0;
            font-size: 28px;
            font-weight: 600;
        }
        
        .btn {
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            font-size: 14px;
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
        
        .btn-success {
            background: #27ae60;
            color: white;
        }
        
        .btn-success:hover {
            background: #229954;
        }
        
        .btn-warning {
            background: #f39c12;
            color: white;
        }
        
        .btn-warning:hover {
            background: #e67e22;
        }
        
        .btn-danger {
            background: #e74c3c;
            color: white;
        }
        
        .btn-danger:hover {
            background: #c0392b;
        }
        
        .btn-sm {
            padding: 6px 12px;
            font-size: 12px;
        }
        
        .filters-section {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 30px;
        }
        
        .filters-row {
            display: flex;
            gap: 15px;
            align-items: end;
            flex-wrap: wrap;
        }
        
        .filter-group {
            flex: 1;
            min-width: 200px;
        }
        
        .filter-group label {
            display: block;
            margin-bottom: 5px;
            font-weight: 600;
            color: #2c3e50;
            font-size: 14px;
        }
        
        .filter-group input,
        .filter-group select {
            width: 100%;
            padding: 10px 12px;
            border: 2px solid #e1e8ed;
            border-radius: 6px;
            font-size: 14px;
            transition: border-color 0.3s ease;
            box-sizing: border-box;
        }
        
        .filter-group input:focus,
        .filter-group select:focus {
            outline: none;
            border-color: #3498db;
        }
        
        .users-table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }
        
        .users-table th {
            background: linear-gradient(135deg, #34495e, #2c3e50);
            color: white;
            padding: 15px 12px;
            text-align: left;
            font-weight: 600;
            font-size: 14px;
        }
        
        .users-table td {
            padding: 12px;
            border-bottom: 1px solid #ecf0f1;
            font-size: 14px;
        }
        
        .users-table tr:hover {
            background: #f8f9fa;
        }
        
        .status-badge {
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 600;
            text-transform: uppercase;
        }
        
        .status-active {
            background: #d4edda;
            color: #155724;
        }
        
        .status-inactive {
            background: #f8d7da;
            color: #721c24;
        }
        
        .role-badge {
            padding: 4px 8px;
            border-radius: 12px;
            font-size: 12px;
            font-weight: 600;
            background: #e3f2fd;
            color: #1565c0;
        }
        
        .actions-cell {
            white-space: nowrap;
        }
        
        .actions-cell .btn {
            margin-right: 5px;
        }
        
        .alert {
            padding: 15px 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            font-weight: 500;
        }
        
        .alert-success {
            background: #f0fdf4;
            border: 1px solid #bbf7d0;
            color: #16a34a;
        }
        
        .alert-error {
            background: #fdf2f2;
            border: 1px solid #fecaca;
            color: #dc2626;
        }
        
        .no-data {
            text-align: center;
            padding: 40px;
            color: #7f8c8d;
            font-size: 16px;
        }
        
        .user-avatar {
            width: 40px;
            height: 40px;
            border-radius: 50%;
            background: linear-gradient(135deg, #3498db, #2980b9);
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 600;
            font-size: 16px;
        }
        
        .user-info {
            display: flex;
            align-items: center;
            gap: 12px;
        }
        
        .user-details h4 {
            margin: 0;
            font-size: 14px;
            color: #2c3e50;
        }
        
        .user-details p {
            margin: 2px 0 0 0;
            font-size: 12px;
            color: #7f8c8d;
        }
        
        @media (max-width: 768px) {
            .users-container {
                margin: 10px;
                padding: 20px;
            }
            
            .filters-row {
                flex-direction: column;
            }
            
            .users-table {
                font-size: 12px;
            }
            
            .users-table th,
            .users-table td {
                padding: 8px 6px;
            }
        }
    </style>
</head>
<body>
    <div class="users-container">
        <div class="page-header">
            <h1 class="page-title">👥 Quản Lý Người Dùng</h1>
            <div style="display: flex; gap: 10px; align-items: center;">
                <a href="${pageContext.request.contextPath}/dashboard/admin-dashboard.jsp" class="btn btn-secondary" style="background-color: #6c757d; border-color: #6c757d; color: white;">
                     Back
                </a>
                <a href="${pageContext.request.contextPath}/create-user" class="btn btn-primary">
                     Tạo Tài Khoản Mới
                </a>
            </div>
        </div>
        
        <!-- Hiển thị thông báo -->
        <c:if test="${not empty param.success}">
            <div class="alert alert-success">
                ${param.success}
            </div>
        </c:if>
        
        <c:if test="${not empty param.error}">
            <div class="alert alert-error">
                ${param.error}
            </div>
        </c:if>
        
        <!-- Bộ lọc -->
        <div class="filters-section">
            <form method="get" action="${pageContext.request.contextPath}/list-users">
                <div class="filters-row">
                    <div class="filter-group">
                        <label for="search">🔍 Tìm kiếm</label>
                        <input type="text" id="search" name="search" 
                               value="${param.search}" 
                               placeholder="Tên, email, username...">
                    </div>
                    
                    <div class="filter-group">
                        <label for="role">👤 Vai trò</label>
                        <select id="role" name="role">
                            <option value="">-- Tất cả vai trò --</option>
                            <option value="Admin" ${param.role == 'Admin' ? 'selected' : ''}>Quản trị viên</option>
                            <option value="HR Manager" ${param.role == 'HR Manager' ? 'selected' : ''}>Quản lý HR</option>
                            <option value="HR" ${param.role == 'HR' ? 'selected' : ''}>Nhân viên HR</option>
                            <option value="Dept Manager" ${param.role == 'Dept Manager' ? 'selected' : ''}>Quản lý phòng ban</option>
                            <option value="Employee" ${param.role == 'Employee' ? 'selected' : ''}>Nhân viên</option>
                        </select>
                    </div>
                    
                    <div class="filter-group">
                        <label for="status"> Trạng thái</label>
                        <select id="status" name="status">
                            <option value="">-- Tất cả trạng thái --</option>
                            <option value="Active" ${param.status == 'Active' ? 'selected' : ''}>Hoạt động</option>
                            <option value="Inactive" ${param.status == 'Inactive' ? 'selected' : ''}>Không hoạt động</option>
                        </select>
                    </div>
                    
                    <div class="filter-group">
                        <button type="submit" class="btn btn-primary"> Lọc</button>
                        <a href="${pageContext.request.contextPath}/list-users" class="btn btn-secondary">🔄 Làm mới</a>
                    </div>
                </div>
            </form>
        </div>
        
        <!-- Bảng danh sách người dùng -->
        <c:choose>
            <c:when test="${not empty users}">
                <table class="users-table">
                    <thead>
                        <tr>
                            <th>Người dùng</th>
                            <th>Email</th>
                            <th>Vai trò</th>
                            <th>Trạng thái</th>
                            <th>Ngày tạo</th>
                            <th>Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="user" items="${users}">
                            <tr>
                                <td>
                                    <div class="user-info">
                                        <div class="user-avatar">
                                            ${user.fullName.charAt(0)}
                                        </div>
                                        <div class="user-details">
                                            <h4>${user.fullName}</h4>
                                            <p>@${user.username}</p>
                                        </div>
                                    </div>
                                </td>
                                <td>${user.email}</td>
                                <td>
                                    <span class="role-badge">${user.roleDisplayName}</span>
                                </td>
                                <td>
                                    <span class="status-badge ${user.status == 'Active' ? 'status-active' : 'status-inactive'}">
                                        ${user.status == 'Active' ? 'Hoạt động' : 'Không hoạt động'}
                                    </span>
                                </td>
                                <td>
                                    <fmt:formatDate value="${user.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                </td>
                                <td class="actions-cell">
                                    <!-- Thay đổi trạng thái -->
                                    <form method="post" action="${pageContext.request.contextPath}/list-users" style="display: inline;">
                                        <input type="hidden" name="action" value="toggle_status">
                                        <input type="hidden" name="userId" value="${user.userId}">
                                        <button type="submit" class="btn btn-sm ${user.status == 'Active' ? 'btn-warning' : 'btn-success'}"
                                                onclick="return confirm('Bạn có chắc muốn ${user.status == 'Active' ? 'vô hiệu hóa' : 'kích hoạt'} tài khoản này?')">
                                            ${user.status == 'Active' ? ' Vô hiệu hóa' : ' Kích hoạt'}
                                        </button>
                                    </form>
                                    
                                    <!-- Sửa thông tin -->
                                    <a class="btn btn-sm btn-primary" href="${pageContext.request.contextPath}/edit-user?id=${user.userId}"> Sửa</a>

                                    <!-- Thay đổi vai trò -->
                                    <form method="post" action="${pageContext.request.contextPath}/list-users" style="display: inline;">
                                        <input type="hidden" name="action" value="change_role">
                                        <input type="hidden" name="userId" value="${user.userId}">
                                        <select name="newRole" onchange="this.form.submit()" class="btn btn-sm" style="padding: 6px 8px; font-size: 12px;">
                                            <option value="">Đổi vai trò</option>
                                            <option value="Employee" ${user.role == 'Employee' ? 'selected' : ''}>Nhân viên</option>
                                            <option value="HR" ${user.role == 'HR' ? 'selected' : ''}>Nhân viên HR</option>
                                            <option value="HR Manager" ${user.role == 'HR Manager' ? 'selected' : ''}>Quản lý HR</option>
                                            <option value="Dept Manager" ${user.role == 'Dept Manager' ? 'selected' : ''}>Quản lý phòng ban</option>
                                            <option value="Admin" ${user.role == 'Admin' ? 'selected' : ''}>Quản trị viên</option>
                                        </select>
                                    </form>
                                    
                                    <!-- Xóa tài khoản -->
                                    <form method="post" action="${pageContext.request.contextPath}/list-users" style="display: inline;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="userId" value="${user.userId}">
                                        <button type="submit" class="btn btn-sm btn-danger"
                                                onclick="return confirm('Bạn có chắc muốn xóa tài khoản này? Hành động này không thể hoàn tác!')">
                                             Xóa
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="no-data">
                    <h3>📭 Không có dữ liệu</h3>
                    <p>Không tìm thấy người dùng nào phù hợp với tiêu chí tìm kiếm.</p>
                    <a href="${pageContext.request.contextPath}/create-user" class="btn btn-primary">
                         Tạo tài khoản đầu tiên
                    </a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
    <script>
        // Tự động submit form khi thay đổi vai trò
        document.querySelectorAll('select[name="newRole"]').forEach(select => {
            select.addEventListener('change', function() {
                if (this.value) {
                    if (confirm('Bạn có chắc muốn thay đổi vai trò của người dùng này?')) {
                        this.form.submit();
                    } else {
                        this.value = '';
                    }
                }
            });
        });
        
        // Hiển thị thông báo thành công/ lỗi
        const urlParams = new URLSearchParams(window.location.search);
        if (urlParams.get('success')) {
            setTimeout(() => {
                const alert = document.querySelector('.alert-success');
                if (alert) alert.style.display = 'none';
            }, 5000);
        }
        
        if (urlParams.get('error')) {
            setTimeout(() => {
                const alert = document.querySelector('.alert-error');
                if (alert) alert.style.display = 'none';
            }, 5000);
        }
    </script>
</body>
</html>
