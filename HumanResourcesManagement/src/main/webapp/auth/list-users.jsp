<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Management - HR System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { background: #f8f9fa; padding: 20px 0; }
        .role-Admin { background: #dc3545; }
        .role-HR { background: #17a2b8; }
        .role-HR-Manager { background: #007bff; }
        .role-Employee { background: #6c757d; }
        .role-Dept-Manager { background: #28a745; }
    </style>
</head>
<body>
    <div class="container">
        <!-- Header -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>👥 User Management</h2>
            <div class="d-flex gap-2">
                <a href="${pageContext.request.contextPath}/dashboard/admin-dashboard.jsp" class="btn btn-secondary">← Back</a>
                <a href="${pageContext.request.contextPath}/create-user" class="btn btn-success">+ Create User</a>
            </div>
        </div>

        <!-- Messages -->
        <c:if test="${not empty param.success}">
            <div class="alert alert-success alert-dismissible fade show">
                ✓ ${param.success}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger alert-dismissible fade show">
                ✗ ${param.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <!-- Filter & Search -->
        <div class="card mb-4">
            <div class="card-body">
                <form method="get" class="row g-3">
                    <div class="col-md-4">
                        <label class="form-label">🔍 Search</label>
                        <input type="text" name="search" class="form-control" 
                               placeholder="Username, email..." value="${param.search}">
                    </div>
                    <div class="col-md-3">
                        <label class="form-label">Filter by Role</label>
                        <select name="role" class="form-select">
                            <option value="">-- All Roles --</option>
                            <option value="Admin" ${param.role == 'Admin' ? 'selected' : ''}>Admin</option>
                            <option value="HR" ${param.role == 'HR' ? 'selected' : ''}>HR</option>
                            <option value="HR Manager" ${param.role == 'HR Manager' ? 'selected' : ''}>HR Manager</option>
                            <option value="Employee" ${param.role == 'Employee' ? 'selected' : ''}>Employee</option>
                            <option value="Dept Manager" ${param.role == 'Dept Manager' ? 'selected' : ''}>Dept Manager</option>
                        </select>
                    </div>
                    <div class="col-md-2">
                        <label class="form-label">Status</label>
                        <select name="status" class="form-select">
                            <option value="">-- All --</option>
                            <option value="Active" ${param.status == 'Active' ? 'selected' : ''}>Active</option>
                            <option value="Inactive" ${param.status == 'Inactive' ? 'selected' : ''}>Inactive</option>
                        </select>
                    </div>
                    <div class="col-md-3 d-flex align-items-end gap-2">
                        <button type="submit" class="btn btn-primary">🔍 Search</button>
                        <a href="${pageContext.request.contextPath}/list-users" class="btn btn-secondary">🔄 Reset</a>
                    </div>
                </form>
            </div>
        </div>

        <!-- Table -->
        <div class="card">
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${not empty users}">
                        <div class="table-responsive">
                            <table class="table table-hover mb-0">
                                <thead class="table-primary">
                                    <tr>
                                        <th>ID</th>
                                        <th>Username</th>
                                        <th>First Name</th>
                                        <th>Last Name</th>
                                        <th>Email</th>
                                        <th>Role</th>
                                        <th>Status</th>
                                        <th>Created</th>
                                        <th>Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="user" items="${users}">
                                        <tr>
                                            <td>${user.userId}</td>
                                            <td><strong>${user.username}</strong></td>
                                            <td>${user.firstName}</td>
                                            <td>${user.lastName}</td>
                                            <td>${user.email}</td>
                                            <td>
                                                <span class="badge role-${user.role.replace(' ', '-')}">${user.role}</span>
                                            </td>
                                            <td>
                                                <span class="badge bg-${user.status == 'Active' ? 'success' : 'secondary'}">${user.status}</span>
                                            </td>
                                            <td><fmt:formatDate value="${user.createdAt}" pattern="dd/MM/yyyy" /></td>
                                            <td>
                                                <div class="btn-group btn-group-sm">
                                                    <a href="${pageContext.request.contextPath}/edit-user?id=${user.userId}" 
                                                       class="btn btn-primary">Edit</a>
                                                    <form method="post" class="d-inline">
                                                        <input type="hidden" name="action" value="toggle_status">
                                                        <input type="hidden" name="userId" value="${user.userId}">
                                                        <button type="submit" class="btn btn-${user.status == 'Active' ? 'warning' : 'success'}"
                                                                onclick="return confirm('Change status?')">
                                                            ${user.status == 'Active' ? 'Deactivate' : 'Activate'}
                                                        </button>
                                                    </form>
                                                </div>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="text-center py-5">
                            <h4>📭 No Data</h4>
                            <p class="text-muted">No users found</p>
                            <a href="${pageContext.request.contextPath}/create-user" class="btn btn-primary">Create First User</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
        
        <!-- Pagination -->
        <c:if test="${totalPages > 1}">
            <nav class="mt-4">
                <ul class="pagination justify-content-center">
                    <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                        <a class="page-link" href="?page=${currentPage - 1}<c:if test='${not empty param.search}'>&search=${param.search}</c:if><c:if test='${not empty param.role}'>&role=${param.role}</c:if><c:if test='${not empty param.status}'>&status=${param.status}</c:if>">Previous</a>
                    </li>
                    
                    <c:forEach var="i" begin="1" end="${totalPages}">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link" href="?page=${i}<c:if test='${not empty param.search}'>&search=${param.search}</c:if><c:if test='${not empty param.role}'>&role=${param.role}</c:if><c:if test='${not empty param.status}'>&status=${param.status}</c:if>">${i}</a>
                        </li>
                    </c:forEach>
                    
                    <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                        <a class="page-link" href="?page=${currentPage + 1}<c:if test='${not empty param.search}'>&search=${param.search}</c:if><c:if test='${not empty param.role}'>&role=${param.role}</c:if><c:if test='${not empty param.status}'>&status=${param.status}</c:if>">Next</a>
                    </li>
                </ul>
                <p class="text-center text-muted">Page ${currentPage} of ${totalPages}</p>
            </nav>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
