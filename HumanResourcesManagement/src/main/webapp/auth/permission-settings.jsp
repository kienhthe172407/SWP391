<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ma Trận Phân Quyền - HR Management</title>
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
            background: white;
        }
        .permission-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 20px;
            border-radius: 10px 10px 0 0;
        }
        
        /* Matrix Table Styles */
        .permission-matrix {
            width: 100%;
            overflow-x: auto;
        }
        .matrix-table {
            width: 100%;
            border-collapse: separate;
            border-spacing: 0;
            margin-top: 20px;
        }
        .matrix-table th {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 15px 10px;
            text-align: center;
            font-weight: 600;
            position: sticky;
            top: 0;
            z-index: 10;
            border: 1px solid #5a67d8;
        }
        .matrix-table th:first-child {
            background: #4c51bf;
            text-align: left;
            min-width: 300px;
            max-width: 300px;
            border-radius: 8px 0 0 0;
        }
        .matrix-table th:last-child {
            border-radius: 0 8px 0 0;
        }
        .matrix-table td {
            padding: 12px;
            border: 1px solid #e2e8f0;
            background: white;
        }
        .matrix-table td:first-child {
            font-weight: 500;
            background: #f7fafc;
            position: sticky;
            left: 0;
            z-index: 5;
            min-width: 300px;
            max-width: 300px;
        }
        .matrix-table tbody tr:hover td {
            background: #edf2f7;
        }
        .matrix-table tbody tr:hover td:first-child {
            background: #e2e8f0;
        }
        .permission-checkbox {
            width: 20px;
            height: 20px;
            cursor: pointer;
            margin: 0 auto;
            display: block;
        }
        .permission-checkbox:checked {
            background-color: #48bb78;
            border-color: #48bb78;
        }
        .category-header {
            background: #edf2f7 !important;
            font-weight: 600;
            color: #2d3748;
            text-align: left !important;
            padding: 15px !important;
        }
        .permission-name {
            display: block;
            font-weight: 500;
            margin-bottom: 3px;
        }
        .permission-desc {
            display: block;
            font-size: 0.85em;
            color: #718096;
        }
        .role-header {
            min-width: 140px;
            writing-mode: horizontal-tb;
        }
        .role-column {
            text-align: center;
            vertical-align: middle;
            min-width: 80px;
        }
        .btn-xs {
            padding: 2px 6px;
            font-size: 0.75rem;
        }
        .role-header .btn-group {
            display: inline-flex;
        }
        .role-header .btn-outline-light {
            color: white;
            border-color: rgba(255,255,255,0.3);
        }
        .role-header .btn-outline-light:hover {
            background-color: rgba(255,255,255,0.2);
            border-color: white;
        }
        .stats-card {
            background: white;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 20px;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        }
        .stat-item {
            display: inline-block;
            margin-right: 25px;
        }
        .stat-label {
            font-size: 0.9em;
            color: #718096;
        }
        .stat-value {
            font-size: 1.3em;
            font-weight: 600;
            color: #2d3748;
        }
    </style>
</head>
<body>
    <div class="container-fluid py-4">
        <!-- Header -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h2><i class="fas fa-th me-2"></i>Ma Trận Phân Quyền</h2>
                <p class="text-muted mb-0">Quản lý quyền truy cập cho tất cả vai trò</p>
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

        <!-- Statistics -->
        <div class="stats-card">
            <div class="d-flex justify-content-between align-items-center">
                <div>
                    <div class="stat-item">
                        <div class="stat-label">Tổng số vai trò</div>
                        <div class="stat-value">${allRoles.size()}</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-label">Tổng số quyền</div>
                        <div class="stat-value" id="totalPermissions">0</div>
                    </div>
                    <div class="stat-item">
                        <div class="stat-label">Quyền đã gán</div>
                        <div class="stat-value text-success" id="assignedPermissions">0</div>
                    </div>
                </div>
                <div>
                    <a href="${pageContext.request.contextPath}/init-permissions" 
                       class="btn btn-warning btn-sm me-2"
                       onclick="return confirm('Bạn có chắc muốn khởi tạo/đồng bộ permissions vào database?')">
                        <i class="fas fa-sync me-1"></i>Khởi Tạo Permissions
                    </a>
                    <a href="${pageContext.request.contextPath}/auth/test-permissions.jsp" 
                       class="btn btn-outline-info btn-sm" target="_blank">
                        <i class="fas fa-bug me-1"></i>Test Permissions Debug
                    </a>
                </div>
            </div>
        </div>

        <!-- Permission Matrix Form -->
        <form method="post" action="${pageContext.request.contextPath}/permission-settings" id="permissionForm">
            <div class="permission-card">
                <div class="permission-header">
                    <div class="d-flex justify-content-between align-items-center">
                        <h4 class="mb-0">
                            <i class="fas fa-key me-2"></i>Ma Trận Phân Quyền
                        </h4>
                        <div>
                            <button type="button" class="btn btn-light btn-sm me-2" onclick="selectAllPermissions()">
                                <i class="fas fa-check-square me-1"></i>Chọn Tất Cả
                            </button>
                            <button type="button" class="btn btn-light btn-sm me-2" onclick="clearAllPermissions()">
                                <i class="fas fa-times me-1"></i>Bỏ Chọn Tất Cả
                            </button>
                            <button type="submit" class="btn btn-success">
                                <i class="fas fa-save me-1"></i>Lưu Thay Đổi
                            </button>
                        </div>
                    </div>
                </div>
                
                <div class="card-body p-0">
                    <div class="permission-matrix">
                        <table class="matrix-table">
                            <thead>
                                <tr>
                                    <th>Chức Năng / Quyền</th>
                                    <c:forEach var="role" items="${allRoles}">
                                        <th class="role-header">
                                            <div>
                                                <i class="fas fa-user-shield d-block mb-2"></i>
                                                <strong>${role}</strong>
                                                <div class="mt-2">
                                                    <small class="badge bg-light text-dark" id="count_${role.replace(' ', '_')}">
                                                        ${rolePermissionsMap[role].size()}
                                                    </small>
                                                </div>
                                                <div class="btn-group btn-group-sm mt-2" role="group">
                                                    <button type="button" class="btn btn-outline-light btn-xs" 
                                                            onclick="selectRoleColumn('${role}')" 
                                                            title="Chọn tất cả">
                                                        <i class="fas fa-check"></i>
                                                    </button>
                                                    <button type="button" class="btn btn-outline-light btn-xs" 
                                                            onclick="clearRoleColumn('${role}')" 
                                                            title="Bỏ chọn tất cả">
                                                        <i class="fas fa-times"></i>
                                                    </button>
                                                </div>
                                            </div>
                                        </th>
                                    </c:forEach>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="entry" items="${permissionsByCategory}">
                                    <!-- Category Header -->
                                    <tr>
                                        <td colspan="${allRoles.size() + 1}" class="category-header">
                                            <i class="fas fa-folder-open me-2"></i><strong>${entry.key}</strong>
                                        </td>
                                    </tr>
                                    
                                    <!-- Permissions -->
                                    <c:forEach var="permission" items="${entry.value}">
                                        <tr>
                                            <td>
                                                <span class="permission-name">${permission.name}</span>
                                                <span class="permission-desc">${permission.description}</span>
                                                <span class="badge bg-secondary mt-1">${permission.code}</span>
                                            </td>
                                            <c:forEach var="role" items="${allRoles}" varStatus="status">
                                                <td class="role-column">
                                                    <input type="checkbox" 
                                                           class="form-check-input permission-checkbox" 
                                                           name="permissions_${role.replace(' ', '_')}" 
                                                           value="${permission.code}"
                                                           data-role="${role}"
                                                           data-permission="${permission.code}"
                                                           ${rolePermissionsMap[role].contains(permission.code) ? 'checked' : ''}
                                                           onchange="updateStats()">
                                                </td>
                                            </c:forEach>
                                        </tr>
                                    </c:forEach>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                
                <div class="card-footer bg-light p-3">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <i class="fas fa-info-circle text-primary me-2"></i>
                            <small class="text-muted">Tích vào ô checkbox để gán quyền cho vai trò tương ứng</small>
                        </div>
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-save me-2"></i>Lưu Tất Cả Thay Đổi
                        </button>
                    </div>
                </div>
            </div>
        </form>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Calculate and update statistics
        function updateStats() {
            const totalCheckboxes = document.querySelectorAll('.permission-checkbox').length;
            const checkedCheckboxes = document.querySelectorAll('.permission-checkbox:checked').length;
            
            // Count unique permissions
            const uniquePermissions = new Set();
            document.querySelectorAll('.permission-checkbox').forEach(cb => {
                uniquePermissions.add(cb.getAttribute('data-permission'));
            });
            
            document.getElementById('totalPermissions').textContent = uniquePermissions.size;
            document.getElementById('assignedPermissions').textContent = checkedCheckboxes;
            
            // Update count for each role
            const roles = new Set();
            document.querySelectorAll('.permission-checkbox').forEach(cb => {
                roles.add(cb.getAttribute('data-role'));
            });
            
            roles.forEach(role => {
                const roleCheckboxes = document.querySelectorAll(`.permission-checkbox[data-role="${role}"]:checked`);
                const countElement = document.getElementById('count_' + role.replace(' ', '_'));
                if (countElement) {
                    countElement.textContent = roleCheckboxes.length;
                }
            });
        }
        
        // Select all checkboxes
        function selectAllPermissions() {
            document.querySelectorAll('.permission-checkbox').forEach(cb => {
                cb.checked = true;
            });
            updateStats();
        }
        
        // Clear all checkboxes
        function clearAllPermissions() {
            if (confirm('Bạn có chắc muốn bỏ chọn tất cả quyền?')) {
                document.querySelectorAll('.permission-checkbox').forEach(cb => {
                    cb.checked = false;
                });
                updateStats();
            }
        }
        
        // Select all for a specific role
        function selectRoleColumn(role) {
            document.querySelectorAll(`.permission-checkbox[data-role="${role}"]`).forEach(cb => {
                cb.checked = true;
            });
            updateStats();
        }
        
        // Clear all for a specific role
        function clearRoleColumn(role) {
            document.querySelectorAll(`.permission-checkbox[data-role="${role}"]`).forEach(cb => {
                cb.checked = false;
            });
            updateStats();
        }
        
        // Initialize stats on page load
        document.addEventListener('DOMContentLoaded', function() {
            updateStats();
        });
        
        // Confirm before submit
        document.getElementById('permissionForm').addEventListener('submit', function(e) {
            const checkedCount = document.querySelectorAll('.permission-checkbox:checked').length;
            const message = `Bạn có chắc muốn lưu các thay đổi phân quyền?\n\nTổng số quyền đã gán: ${checkedCount}`;
            
            if (!confirm(message)) {
                e.preventDefault();
                return false;
            }
            
            // Show loading state
            const submitButtons = document.querySelectorAll('button[type="submit"]');
            submitButtons.forEach(btn => {
                btn.disabled = true;
                btn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Đang lưu...';
            });
            
            return true;
        });
    </script>
</body>
</html>
