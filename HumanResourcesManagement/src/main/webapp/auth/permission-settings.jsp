<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Permission Settings - HR Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            background: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .permission-card {
            border: none;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        .permission-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            border-radius: 10px 10px 0 0;
        }
        .category-section {
            background: white;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 15px;
            border-left: 4px solid #667eea;
        }
        .permission-item {
            padding: 12px;
            border-bottom: 1px solid #f0f0f0;
            transition: background 0.2s;
        }
        .permission-item:hover {
            background: #f8f9fa;
        }
        .permission-item:last-child {
            border-bottom: none;
        }
        .form-check-input:checked {
            background-color: #667eea;
            border-color: #667eea;
        }
        .role-selector {
            background: white;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
    <div class="container py-4">
        <!-- Header -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2><i class="fas fa-shield-alt me-2"></i>Quản Lý Phân Quyền</h2>
                <p class="text-muted mb-0">Cấu hình quyền truy cập cho từng vai trò</p>
            </div>
            <a href="${pageContext.request.contextPath}/dashboard/admin-dashboard.jsp" class="btn btn-secondary">
                <i class="fas fa-arrow-left me-2"></i>Quay lại
            </a>
        </div>

        <!-- Success/Error Messages -->
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="alert alert-success alert-dismissible fade show">
                <i class="fas fa-check-circle me-2"></i>${sessionScope.successMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="successMessage" scope="session"/>
        </c:if>

        <c:if test="${not empty sessionScope.errorMessage}">
            <div class="alert alert-danger alert-dismissible fade show">
                <i class="fas fa-exclamation-circle me-2"></i>${sessionScope.errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <c:remove var="errorMessage" scope="session"/>
        </c:if>

        <!-- Role Selector -->
        <div class="role-selector">
            <form method="get" class="row g-3 align-items-center">
                <div class="col-auto">
                    <label class="form-label fw-bold mb-0">
                        <i class="fas fa-user-tag me-2"></i>Chọn Vai Trò:
                    </label>
                </div>
                <div class="col-auto">
                    <select name="role" class="form-select" onchange="this.form.submit()">
                        <c:forEach var="role" items="${allRoles}">
                            <option value="${role}" ${role == selectedRole ? 'selected' : ''}>${role}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-auto">
                    <span class="badge bg-primary">${rolePermissions.size()} quyền được gán</span>
                </div>
            </form>
        </div>

        <!-- Permission Form -->
        <form method="post" action="${pageContext.request.contextPath}/permission-settings">
            <input type="hidden" name="role" value="${selectedRole}">
            
            <div class="permission-card">
                <div class="permission-header">
                    <h4 class="mb-0">
                        <i class="fas fa-key me-2"></i>Phân Quyền cho: <strong>${selectedRole}</strong>
                    </h4>
                </div>
                
                <div class="card-body p-4">
                    <!-- Permissions by Category -->
                    <c:forEach var="entry" items="${permissionsByCategory}">
                        <div class="category-section">
                            <h5 class="mb-3">
                                <i class="fas fa-folder-open me-2 text-primary"></i>${entry.key}
                            </h5>
                            
                            <c:forEach var="permission" items="${entry.value}">
                                <div class="permission-item">
                                    <div class="form-check">
                                        <input class="form-check-input" 
                                               type="checkbox" 
                                               name="permissions" 
                                               value="${permission.code}" 
                                               id="perm_${permission.code}"
                                               ${rolePermissions.contains(permission.code) ? 'checked' : ''}>
                                        <label class="form-check-label w-100" for="perm_${permission.code}">
                                            <div class="d-flex justify-content-between align-items-start">
                                                <div>
                                                    <strong>${permission.name}</strong>
                                                    <br>
                                                    <small class="text-muted">${permission.description}</small>
                                                </div>
                                                <span class="badge bg-light text-dark">${permission.code}</span>
                                            </div>
                                        </label>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:forEach>
                    
                    <!-- Action Buttons -->
                    <div class="d-flex justify-content-between mt-4">
                        <button type="button" class="btn btn-outline-primary" onclick="selectAll()">
                            <i class="fas fa-check-double me-2"></i>Chọn Tất Cả
                        </button>
                        <div>
                            <button type="button" class="btn btn-outline-secondary me-2" onclick="deselectAll()">
                                <i class="fas fa-times me-2"></i>Bỏ Chọn Tất Cả
                            </button>
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save me-2"></i>Lưu Thay Đổi
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function selectAll() {
            document.querySelectorAll('input[type="checkbox"][name="permissions"]').forEach(cb => cb.checked = true);
        }
        
        function deselectAll() {
            document.querySelectorAll('input[type="checkbox"][name="permissions"]').forEach(cb => cb.checked = false);
        }
    </script>
</body>
</html>
