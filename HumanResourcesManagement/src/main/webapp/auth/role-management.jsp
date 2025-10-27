<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <!DOCTYPE html>
            <html lang="vi">

            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Role Management - HR System</title>
                <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
                <style>
                    body {
                        background: #f8f9fa;
                        padding: 20px 0;
                    }

                    .role-Admin {
                        background: #dc3545;
                    }

                    .role-HR {
                        background: #17a2b8;
                    }

                    .role-HR-Manager {
                        background: #007bff;
                    }

                    .role-Employee {
                        background: #6c757d;
                    }

                    .role-Dept-Manager {
                        background: #28a745;
                    }
                </style>
            </head>

            <body>
                <div class="container">
                    <!-- Header -->
                    <div class="d-flex justify-content-between align-items-center mb-4">
                        <h2>🔐 Role Management</h2>
                        <a href="${pageContext.request.contextPath}/dashboard/admin-dashboard.jsp"
                            class="btn btn-secondary">← Back to Dashboard</a>
                    </div>

                    <!-- Messages -->
                    <c:if test="${not empty sessionScope.successMessage}">
                        <div class="alert alert-success alert-dismissible fade show">
                            ✓ ${sessionScope.successMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                        <c:remove var="successMessage" scope="session" />
                    </c:if>

                    <c:if test="${not empty sessionScope.errorMessage}">
                        <div class="alert alert-danger alert-dismissible fade show">
                            ✗ ${sessionScope.errorMessage}
                            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                        </div>
                        <c:remove var="errorMessage" scope="session" />
                    </c:if>

                    <!-- Statistics -->
                    <div class="row g-3 mb-4">
                        <c:forEach var="entry" items="${roleStats}">
                            <div class="col-md-2">
                                <div class="card text-white bg-primary text-center">
                                    <div class="card-body">
                                        <h3 class="mb-0">${entry.value}</h3>
                                        <small>${entry.key}</small>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>



                    <!-- Filter & Search -->
                    <div class="card mb-4">
                        <div class="card-body">
                            <form method="get" class="row g-3">
                                <input type="hidden" name="action" value="filter">
                                <div class="col-md-4">
                                    <label class="form-label">🔍 Search by Username</label>
                                    <input type="text" name="search" id="searchInput" class="form-control"
                                        placeholder="Enter username..." value="${param.search}" autocomplete="off">
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label">Filter by Role</label>
                                    <select name="role" class="form-select">
                                        <option value="">-- All Roles --</option>
                                        <option value="Admin" ${selectedRole=='Admin' ? 'selected' : '' }>Admin</option>
                                        <option value="HR" ${selectedRole=='HR' ? 'selected' : '' }>HR</option>
                                        <option value="HR Manager" ${selectedRole=='HR Manager' ? 'selected' : '' }>HR
                                            Manager</option>
                                        <option value="Employee" ${selectedRole=='Employee' ? 'selected' : '' }>Employee
                                        </option>
                                        <option value="Dept Manager" ${selectedRole=='Dept Manager' ? 'selected' : '' }>
                                            Dept Manager</option>
                                    </select>
                                </div>
                                <div class="col-md-2 d-flex align-items-end">
                                    <button type="submit" class="btn btn-primary w-100">🔍 Search</button>
                                </div>
                                <div class="col-md-2 d-flex align-items-end">
                                    <a href="${pageContext.request.contextPath}/role-management"
                                        class="btn btn-secondary w-100">🔄 Reset</a>
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
                                                        <td>${user.userID}</td>
                                                        <td><strong>${user.username}</strong></td>
                                                        <td>${user.email}</td>
                                                        <td>
                                                            <span
                                                                class="badge role-${user.role.replace(' ', '-')}">${user.role}</span>
                                                        </td>
                                                        <td>
                                                            <span
                                                                class="badge bg-${user.status == 'Active' ? 'success' : 'secondary'}">${user.status}</span>
                                                        </td>
                                                        <td>
                                                            <fmt:formatDate value="${user.createdAt}"
                                                                pattern="dd/MM/yyyy" />
                                                        </td>
                                                        <td>
                                                            <form method="post" class="d-inline">
                                                                <input type="hidden" name="action" value="updateRole">
                                                                <input type="hidden" name="userId"
                                                                    value="${user.userID}">
                                                                <select name="role" class="form-select form-select-sm"
                                                                    onchange="if(confirm('Change role?')) this.form.submit();">
                                                                    <option value="">Change Role</option>
                                                                    <option value="Employee">Employee</option>
                                                                    <option value="HR">HR</option>
                                                                    <option value="HR Manager">HR Manager</option>
                                                                    <option value="Dept Manager">Dept Manager</option>
                                                                    <option value="Admin">Admin</option>
                                                                </select>
                                                            </form>
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
                                    <a class="page-link"
                                        href="?page=${currentPage - 1}<c:if test='${not empty param.role}'>&role=${param.role}&action=filter</c:if>">Previous</a>
                                </li>

                                <c:forEach var="i" begin="1" end="${totalPages}">
                                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                                        <a class="page-link"
                                            href="?page=${i}<c:if test='${not empty param.role}'>&role=${param.role}&action=filter</c:if>">${i}</a>
                                    </li>
                                </c:forEach>

                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="page-link"
                                        href="?page=${currentPage + 1}<c:if test='${not empty param.role}'>&role=${param.role}&action=filter</c:if>">Next</a>
                                </li>
                            </ul>
                            <p class="text-center text-muted">Page ${currentPage} of ${totalPages} (Total: ${totalUsers}
                                users)</p>
                        </nav>
                    </c:if>
                </div>

                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
            </body>

            </html>