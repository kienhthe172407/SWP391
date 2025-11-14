<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Phân quyền - HRM System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .permission-container {
            max-width: 1400px;
            margin: 20px auto;
            padding: 20px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        
        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            padding-bottom: 15px;
            border-bottom: 2px solid #e0e0e0;
        }
        
        .page-header h1 {
            margin: 0;
            color: #333;
            font-size: 28px;
        }
        
        .init-btn {
            padding: 10px 20px;
            background: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }
        
        .init-btn:hover {
            background: #45a049;
        }
        
        .alert {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 4px;
        }
        
        .alert-success {
            background: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
        
        .role-tabs {
            display: flex;
            gap: 10px;
            margin-bottom: 20px;
            border-bottom: 2px solid #e0e0e0;
            flex-wrap: wrap;
        }
        
        .role-tab {
            padding: 12px 24px;
            background: #f5f5f5;
            border: none;
            border-radius: 4px 4px 0 0;
            cursor: pointer;
            font-size: 16px;
            transition: all 0.3s;
        }
        
        .role-tab:hover {
            background: #e0e0e0;
        }
        
        .role-tab.active {
            background: #2196F3;
            color: white;
        }
        
        .role-content {
            display: none;
        }
        
        .role-content.active {
            display: block;
        }
        
        .permission-category {
            margin-bottom: 30px;
            border: 1px solid #e0e0e0;
            border-radius: 4px;
            overflow: hidden;
        }
        
        .category-header {
            background: #f5f5f5;
            padding: 15px;
            font-weight: bold;
            font-size: 18px;
            color: #333;
            border-bottom: 1px solid #e0e0e0;
        }
        
        .permission-list {
            padding: 15px;
        }
        
        .permission-item {
            display: flex;
            align-items: flex-start;
            padding: 12px;
            margin-bottom: 10px;
            background: #fafafa;
            border-radius: 4px;
            transition: background 0.2s;
        }
        
        .permission-item:hover {
            background: #f0f0f0;
        }
        
        .permission-item input[type="checkbox"] {
            margin-right: 12px;
            margin-top: 4px;
            width: 18px;
            height: 18px;
            cursor: pointer;
        }
        
        .permission-info {
            flex: 1;
        }
        
        .permission-name {
            font-weight: 600;
            color: #333;
            margin-bottom: 4px;
        }
        
        .permission-code {
            font-size: 12px;
            color: #666;
            font-family: monospace;
            background: #e0e0e0;
            padding: 2px 6px;
            border-radius: 3px;
            display: inline-block;
            margin-bottom: 4px;
        }
        
        .permission-desc {
            font-size: 14px;
            color: #666;
        }
        
        .form-actions {
            display: flex;
            gap: 10px;
            justify-content: flex-end;
            margin-top: 20px;
            padding-top: 20px;
            border-top: 2px solid #e0e0e0;
        }
        
        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
            transition: all 0.3s;
        }
        
        .btn-primary {
            background: #2196F3;
            color: white;
        }
        
        .btn-primary:hover {
            background: #1976D2;
        }
        
        .btn-secondary {
            background: #757575;
            color: white;
        }
        
        .btn-secondary:hover {
            background: #616161;
        }
        
        .select-all-container {
            padding: 15px;
            background: #e3f2fd;
            border-bottom: 1px solid #e0e0e0;
        }
        
        .select-all-container label {
            display: flex;
            align-items: center;
            cursor: pointer;
            font-weight: 600;
        }
        
        .select-all-container input[type="checkbox"] {
            margin-right: 10px;
            width: 18px;
            height: 18px;
        }
        
        .admin-notice {
            background: #fff3cd;
            border: 1px solid #ffc107;
            padding: 15px;
            border-radius: 4px;
            margin-bottom: 20px;
            color: #856404;
        }
    </style>
</head>
<body>
    <div class="permission-container">
        <div class="page-header">
            <h1>⚙️ Quản lý Phân quyền</h1>
            <a href="${pageContext.request.contextPath}/permission-settings/init" class="init-btn">
                🔄 Khởi tạo Permissions
            </a>
        </div>
        
        <!-- Success/Error Messages -->
        <c:if test="${not empty sessionScope.successMessage}">
            <div class="alert alert-success">
                ✓ ${sessionScope.successMessage}
            </div>
            <c:remove var="successMessage" scope="session"/>
        </c:if>
        
        <c:if test="${not empty sessionScope.errorMessage}">
            <div class="alert alert-error">
                ✗ ${sessionScope.errorMessage}
            </div>
            <c:remove var="errorMessage" scope="session"/>
        </c:if>
        
        <!-- Role Tabs -->
        <div class="role-tabs">
            <c:forEach items="${roles}" var="role" varStatus="status">
                <button class="role-tab ${status.first ? 'active' : ''}" 
                        onclick="showRole('${role}')">
                    ${role}
                </button>
            </c:forEach>
        </div>
        
        <!-- Role Contents -->
        <c:forEach items="${roles}" var="role" varStatus="status">
            <div id="role-${role}" class="role-content ${status.first ? 'active' : ''}">
                <c:if test="${role == 'Admin'}">
                    <div class="admin-notice">
                        <strong>⚠️ Lưu ý:</strong> Admin có tất cả quyền trong hệ thống và không thể thay đổi.
                    </div>
                </c:if>
                
                <form method="post" action="${pageContext.request.contextPath}/permission-settings/update">
                    <input type="hidden" name="role" value="${role}">
                    
                    <c:forEach items="${permissionsByCategory}" var="category">
                        <div class="permission-category">
                            <div class="category-header">
                                📁 ${category.key}
                            </div>
                            
                            <div class="select-all-container">
                                <label>
                                    <input type="checkbox" 
                                           onchange="toggleCategory(this, '${role}', '${category.key}')"
                                           ${role == 'Admin' ? 'checked disabled' : ''}>
                                    Chọn tất cả
                                </label>
                            </div>
                            
                            <div class="permission-list">
                                <c:forEach items="${category.value}" var="permission">
                                    <div class="permission-item">
                                        <input type="checkbox" 
                                               name="permissions" 
                                               value="${permission.code}"
                                               data-category="${category.key}"
                                               ${rolePermissions[role].contains(permission.code) ? 'checked' : ''}
                                               ${role == 'Admin' ? 'disabled' : ''}>
                                        <div class="permission-info">
                                            <div class="permission-name">${permission.name}</div>
                                            <div class="permission-code">${permission.code}</div>
                                            <div class="permission-desc">${permission.description}</div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </c:forEach>
                    
                    <c:if test="${role != 'Admin'}">
                        <div class="form-actions">
                            <button type="button" class="btn btn-secondary" onclick="resetForm(this.form)">
                                ↺ Đặt lại
                            </button>
                            <button type="submit" class="btn btn-primary">
                                💾 Lưu thay đổi
                            </button>
                        </div>
                    </c:if>
                </form>
            </div>
        </c:forEach>
    </div>
    
    <script>
        function showRole(roleName) {
            // Hide all role contents
            document.querySelectorAll('.role-content').forEach(content => {
                content.classList.remove('active');
            });
            
            // Remove active class from all tabs
            document.querySelectorAll('.role-tab').forEach(tab => {
                tab.classList.remove('active');
            });
            
            // Show selected role content
            document.getElementById('role-' + roleName).classList.add('active');
            
            // Add active class to clicked tab
            event.target.classList.add('active');
        }
        
        function toggleCategory(checkbox, role, category) {
            const roleContent = document.getElementById('role-' + role);
            const categoryCheckboxes = roleContent.querySelectorAll(
                'input[type="checkbox"][data-category="' + category + '"]'
            );
            
            categoryCheckboxes.forEach(cb => {
                cb.checked = checkbox.checked;
            });
        }
        
        function resetForm(form) {
            form.reset();
        }
        
        // Update "Select All" checkbox state when individual checkboxes change
        document.addEventListener('DOMContentLoaded', function() {
            document.querySelectorAll('.permission-list input[type="checkbox"]').forEach(checkbox => {
                checkbox.addEventListener('change', function() {
                    updateSelectAllState(this);
                });
            });
        });
        
        function updateSelectAllState(checkbox) {
            const category = checkbox.getAttribute('data-category');
            const roleContent = checkbox.closest('.role-content');
            const categoryCheckboxes = roleContent.querySelectorAll(
                'input[type="checkbox"][data-category="' + category + '"]'
            );
            const selectAllCheckbox = roleContent.querySelector(
                '.permission-category input[type="checkbox"][onchange*="' + category + '"]'
            );
            
            if (selectAllCheckbox) {
                const allChecked = Array.from(categoryCheckboxes).every(cb => cb.checked);
                selectAllCheckbox.checked = allChecked;
            }
        }
    </script>
</body>
</html>
