<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Hồ sơ cá nhân</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css" />
    <script src="https://kit.fontawesome.com/a2e0e6ad65.js" crossorigin="anonymous"></script>
</head>
<body>
    <!-- Simple Header -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
        <div class="container">
            <a class="navbar-brand" href="#">
                <i class="fas fa-user-circle me-2"></i>HR Management System
            </a>
            <div class="navbar-nav ms-auto">
                <c:choose>
                    <c:when test="${sessionScope.user.role == 'Admin'}">
                        <a class="nav-link" href="${pageContext.request.contextPath}/dashboard/admin-dashboard.jsp">
                            <i class="fas fa-tachometer-alt me-1"></i>Admin Dashboard
                        </a>
                    </c:when>
                    <c:when test="${sessionScope.user.role == 'HR Manager'}">
                        <a class="nav-link" href="${pageContext.request.contextPath}/dashboard/hr-manager-dashboard.jsp">
                            <i class="fas fa-tachometer-alt me-1"></i>HR Manager Dashboard
                        </a>
                    </c:when>
                    <c:when test="${sessionScope.user.role == 'HR'}">
                        <a class="nav-link" href="${pageContext.request.contextPath}/dashboard/hr-dashboard.jsp">
                            <i class="fas fa-tachometer-alt me-1"></i>HR Dashboard
                        </a>
                    </c:when>
                    <c:when test="${sessionScope.user.role == 'Dept Manager'}">
                        <a class="nav-link" href="${pageContext.request.contextPath}/dashboard/dept-manager-dashboard.jsp">
                            <i class="fas fa-tachometer-alt me-1"></i>Dept Manager Dashboard
                        </a>
                    </c:when>
                    <c:when test="${sessionScope.user.role == 'Employee'}">
                        <a class="nav-link" href="${pageContext.request.contextPath}/dashboard/employee-dashboard.jsp">
                            <i class="fas fa-tachometer-alt me-1"></i>Employee Dashboard
                        </a>
                    </c:when>
                </c:choose>
            </div>
        </div>
    </nav>

<div class="container py-4">
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm">
                <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
                    <span><i class="fas fa-id-card me-2"></i>Hồ sơ cá nhân</span>
                    <c:choose>
                        <c:when test="${sessionScope.user.role == 'Admin'}">
                            <a class="btn btn-light btn-sm" href="${pageContext.request.contextPath}/dashboard/admin-dashboard.jsp"><i class="fas fa-arrow-left me-1"></i>Quay lại Dashboard</a>
                        </c:when>
                        <c:when test="${sessionScope.user.role == 'HR Manager'}">
                            <a class="btn btn-light btn-sm" href="${pageContext.request.contextPath}/dashboard/hr-manager-dashboard.jsp"><i class="fas fa-arrow-left me-1"></i>Quay lại Dashboard</a>
                        </c:when>
                        <c:when test="${sessionScope.user.role == 'HR'}">
                            <a class="btn btn-light btn-sm" href="${pageContext.request.contextPath}/dashboard/hr-dashboard.jsp"><i class="fas fa-arrow-left me-1"></i>Quay lại Dashboard</a>
                        </c:when>
                        <c:when test="${sessionScope.user.role == 'Dept Manager'}">
                            <a class="btn btn-light btn-sm" href="${pageContext.request.contextPath}/dashboard/dept-manager-dashboard.jsp"><i class="fas fa-arrow-left me-1"></i>Quay lại Dashboard</a>
                        </c:when>
                        <c:when test="${sessionScope.user.role == 'Employee'}">
                            <a class="btn btn-light btn-sm" href="${pageContext.request.contextPath}/dashboard/employee-dashboard.jsp"><i class="fas fa-arrow-left me-1"></i>Quay lại Dashboard</a>
                        </c:when>
                        <c:otherwise>
                            <a class="btn btn-light btn-sm" href="${pageContext.request.contextPath}/manager/home.jsp"><i class="fas fa-arrow-left me-1"></i>Quay lại</a>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="card-body">
                    <c:set var="u" value="${sessionScope.user}" />
                    <div class="d-flex align-items-start mb-3">
                        <div class="me-3">
                            <span class="d-inline-flex align-items-center justify-content-center rounded-circle bg-primary text-white" style="width:56px;height:56px;">
                                <i class="fas fa-user fa-lg"></i>
                            </span>
                        </div>
                        <div>
                            <h5 class="mb-1"><c:out value="${u.fullName}" default="Người dùng" /></h5>
                            <span class="badge bg-info text-dark"><i class="fas fa-user-shield me-1"></i><c:out value="${u.roleDisplayName}" default="User" /></span>
                        </div>
                    </div>

                    <form class="row g-3" action="${pageContext.request.contextPath}/profile" method="post">
                        <div class="col-md-6">
                            <label class="form-label">Tên đăng nhập</label>
                            <input type="text" class="form-control" value="${u.username}" readonly />
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Email</label>
                            <input type="email" name="email" class="form-control" value="${u.email}" required />
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Số điện thoại</label>
                            <input type="tel" name="phone" class="form-control" value="${u.phone}" />
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Ngày sinh</label>
                            <input type="date" name="dateOfBirth" class="form-control" value="${u.dateOfBirth}" />
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Giới tính</label>
                            <select name="gender" class="form-select">
                                <option value="Male" ${u.gender == 'Male' ? 'selected' : ''}>Nam</option>
                                <option value="Female" ${u.gender == 'Female' ? 'selected' : ''}>Nữ</option>
                                <option value="Other" ${u.gender == 'Other' ? 'selected' : ''}>Khác</option>
                            </select>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Họ</label>
                            <input type="text" name="lastName" class="form-control" value="${u.lastName}" />
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Tên</label>
                            <input type="text" name="firstName" class="form-control" value="${u.firstName}" />
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Trạng thái</label>
                            <input type="text" class="form-control" value="${u.status}" readonly />
                        </div>
                        <div class="col-md-6">
                            <label class="form-label">Lần đăng nhập gần nhất</label>
                            <input type="text" class="form-control" value="${u.lastLogin}" readonly />
                        </div>
                        <div class="col-12 d-flex justify-content-end mt-2">
                            <button type="submit" class="btn btn-primary"><i class="fas fa-save me-1"></i>Lưu thay đổi</button>
                        </div>
                    </form>
                </div>
                <div class="card-footer d-flex justify-content-end">
                    <c:choose>
                        <c:when test="${sessionScope.user.role == 'Employee'}">
                            <a class="btn btn-outline-secondary me-2" href="${pageContext.request.contextPath}/auth/change-password.jsp"><i class="fas fa-key me-1"></i>Đổi mật khẩu</a>
                        </c:when>
                        <c:otherwise>
                            <a class="btn btn-outline-secondary me-2" href="${pageContext.request.contextPath}/auth/change-password.jsp"><i class="fas fa-key me-1"></i>Đổi mật khẩu</a>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${sessionScope.user.role == 'Admin'}">
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/dashboard/admin-dashboard.jsp"><i class="fas fa-home me-1"></i>Dashboard</a>
                        </c:when>
                        <c:when test="${sessionScope.user.role == 'HR Manager'}">
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/dashboard/hr-manager-dashboard.jsp"><i class="fas fa-home me-1"></i>Dashboard</a>
                        </c:when>
                        <c:when test="${sessionScope.user.role == 'HR'}">
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/dashboard/hr-dashboard.jsp"><i class="fas fa-home me-1"></i>Dashboard</a>
                        </c:when>
                        <c:when test="${sessionScope.user.role == 'Dept Manager'}">
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/dashboard/dept-manager-dashboard.jsp"><i class="fas fa-home me-1"></i>Dashboard</a>
                        </c:when>
                        <c:when test="${sessionScope.user.role == 'Employee'}">
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/dashboard/employee-dashboard.jsp"><i class="fas fa-home me-1"></i>Dashboard</a>
                        </c:when>
                        <c:otherwise>
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/manager/home.jsp"><i class="fas fa-home me-1"></i>Trang chủ</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>


