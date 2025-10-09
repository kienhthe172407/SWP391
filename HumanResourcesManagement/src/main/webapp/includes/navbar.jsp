<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-gradient" style="background: linear-gradient(135deg, #4e73df 0%, #224abe 100%);">
    <div class="container-fluid">
        <a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/dashboard">
            <i class="fas fa-users-cog me-2"></i>
            <span class="fw-bold">HRM System</span>
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" 
                aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/dashboard">
                        <i class="fas fa-home me-1"></i> Trang chủ
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/employees">
                        <i class="fas fa-user-tie me-1"></i> Nhân viên
                    </a>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="contractDropdown" role="button" 
                       data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-file-contract me-1"></i> Hợp đồng
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="contractDropdown">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/contract-mgt/list-contracts.jsp">Danh sách hợp đồng</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/contract-mgt/create-contract.jsp">Tạo hợp đồng mới</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/contract-mgt/approve-contract.jsp">Phê duyệt hợp đồng</a></li>
                    </ul>
                </li>
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="reportDropdown" role="button" 
                       data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-chart-bar me-1"></i> Báo cáo
                    </a>
                    <ul class="dropdown-menu" aria-labelledby="reportDropdown">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/reports/employee-report.jsp">Báo cáo nhân sự</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/reports/salary-report.jsp">Báo cáo lương</a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/reports/performance-report.jsp">Báo cáo hiệu suất</a></li>
                    </ul>
                </li>
            </ul>
            
            <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                <li class="nav-item dropdown">
                    <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" 
                       data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="fas fa-user-circle me-1"></i> 
                        <c:out value="${sessionScope.user.fullName}" default="Người dùng" />
                    </a>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                            <i class="fas fa-id-card me-2"></i> Hồ sơ cá nhân
                        </a></li>
                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/auth/change-password.jsp">
                            <i class="fas fa-key me-2"></i> Đổi mật khẩu
                        </a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li>
                            <form action="${pageContext.request.contextPath}/logout" method="post" class="dropdown-item p-0">
                                <button type="submit" class="btn btn-link text-decoration-none text-danger w-100 text-start px-3 py-1">
                                    <i class="fas fa-sign-out-alt me-2"></i> Đăng xuất
                                </button>
                            </form>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>