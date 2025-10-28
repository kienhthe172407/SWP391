<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Salary Components - HR Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    <style>
        .salary-table {
            font-size: 0.9rem;
        }
        .total-row {
            font-weight: bold;
            background-color: #f8f9fa;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="sidebar-header">
            <h4>HR Manager Dashboard</h4>
            <p>Human Resources</p>
        </div>
        
        <ul class="sidebar-menu">
            <c:choose>
                <c:when test="${sessionScope.user != null && (sessionScope.user.role == 'HR Manager' || sessionScope.user.role == 'HR_MANAGER')}">
                    <li class="menu-section">Dashboard</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/dashboard/hr-manager-dashboard.jsp">
                            <i class="fas fa-home"></i>
                            <span>Overview</span>
                        </a>
                    </li>

                    <li class="menu-section">HR Management</li>
                    <li>
                        <a href="#">
                            <i class="fas fa-users-cog"></i>
                            <span>HR Staff Management</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-tasks"></i>
                            <span>Task Assignment</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-clipboard-check"></i>
                            <span>Approval Queue</span>
                        </a>
                    </li>

                    <li class="menu-section">Employee Management</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/employees/list">
                            <i class="fas fa-users"></i>
                            <span>All Employees</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/employee-mgt/add-employee-information.jsp">
                            <i class="fas fa-user-plus"></i>
                            <span>Add Employee Information</span>
                        </a>
                    </li>

                    <li class="menu-section">Contracts & Attendance</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/contracts/list">
                            <i class="fas fa-file"></i>
                            <span>Contracts</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/attendance/import">
                            <i class="fas fa-clock"></i>
                            <span>Attendance</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-calendar-check"></i>
                            <span>Leave Requests</span>
                        </a>
                    </li>

                    <li class="menu-section">Recruitment</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/job-postings/list">
                            <i class="fas fa-briefcase"></i>
                            <span> Job Postings</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/job-postings/create">
                            <i class="fas fa-plus"></i>
                            <span>Create Job Posting</span>
                        </a>
                    </li>

                    <li class="menu-section">Payroll & Benefits</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/salary/manage-benefits-deductions">
                            <i class="fas fa-gift"></i>
                            <span>Benefits & Deductions</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/salary/import" class="active">
                            <i class="fas fa-dollar-sign"></i>
                            <span>Payroll</span>
                        </a>
                    </li>

                    <li class="menu-section">Reports & Analytics</li>
                    <li>
                        <a href="#">
                            <i class="fas fa-chart-bar"></i>
                            <span>HR Reports</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-chart-line"></i>
                            <span>Analytics</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-chart-pie"></i>
                            <span>Statistics</span>
                        </a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="menu-section">Dashboard</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/dashboard/hr-dashboard.jsp">
                            <i class="fas fa-home"></i>
                            <span>Overview</span>
                        </a>
                    </li>

                    <li class="menu-section">Employee Management</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/employees/list">
                            <i class="fas fa-users"></i>
                            <span>All Employees</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/employees/addInformation">
                            <i class="fas fa-user-plus"></i>
                            <span>Add Employee Information</span>
                        </a>
                    </li>

                    <li class="menu-section">Contracts & Attendance</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/contracts/list">
                            <i class="fas fa-file"></i>
                            <span>Contracts</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/attendance/import">
                            <i class="fas fa-clock"></i>
                            <span>Attendance</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-calendar-check"></i>
                            <span>Leave Requests</span>
                        </a>
                    </li>

                    <li class="menu-section">Recruitment</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/job-postings/list">
                            <i class="fas fa-briefcase"></i>
                            <span> Job Postings</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/job-postings/create">
                            <i class="fas fa-plus"></i>
                            <span>Create Job Posting</span>
                        </a>
                    </li>

                    <li class="menu-section">Payroll & Benefits</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/salary/manage-benefits-deductions">
                            <i class="fas fa-gift"></i>
                            <span>Benefits & Deductions</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/salary/import" class="active">
                            <i class="fas fa-dollar-sign"></i>
                            <span>Payroll</span>
                        </a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <!-- Top Header -->
        <div class="top-header">
            <h1>View Salary Components</h1>
            <div class="user-info">
                <span><c:out value="${sessionScope.user.roleDisplayName}" default="HR"/></span>
                <div class="avatar">HR</div>
            </div>
        </div>

        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard/hr-dashboard.jsp">Dashboard</a></li>
                <li class="breadcrumb-item active">View Salary Components</li>
            </ol>
        </nav>

        <!-- Content Area -->
        <div class="content-area">
            <!-- Success/Error Messages -->
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>${successMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Action Buttons -->
            <div class="card mb-4">
                <div class="card-body">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h5 class="mb-0">Salary Components Overview</h5>
                            <p class="text-muted mb-0">Total Employees: <strong>${totalEmployees}</strong></p>
                        </div>
                        <div>
                            <a href="${pageContext.request.contextPath}/salary/import" class="btn btn-outline-primary me-2">
                                <i class="fas fa-file-import me-2"></i>Import More Data
                            </a>
                            <a href="${pageContext.request.contextPath}/salary/calculate" class="btn btn-success">
                                <i class="fas fa-calculator me-2"></i>Calculate Monthly Salary
                            </a>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Salary Components Table -->
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0"><i class="fas fa-table me-2"></i>Employee Salary Components</h5>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty componentViews}">
                            <div class="alert alert-info">
                                <i class="fas fa-info-circle me-2"></i>
                                No salary components found. Please import salary data first.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-striped table-hover salary-table">
                                    <thead class="table-dark">
                                        <tr>
                                            <th>Employee Code</th>
                                            <th>Employee Name</th>
                                            <th>Department</th>
                                            <th>Position</th>
                                            <th class="text-end">Base Salary</th>
                                            <th class="text-end">Position Allow.</th>
                                            <th class="text-end">Housing Allow.</th>
                                            <th class="text-end">Transport Allow.</th>
                                            <th class="text-end">Meal Allow.</th>
                                            <th class="text-end">Other Allow.</th>
                                            <th class="text-end">Total Monthly</th>
                                            <th>Effective From</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="view" items="${componentViews}">
                                            <tr>
                                                <td><strong>${view.employee.employeeCode}</strong></td>
                                                <td>${view.employee.firstName} ${view.employee.lastName}</td>
                                                <td>${view.employee.departmentName}</td>
                                                <td>${view.employee.positionName}</td>
                                                <td class="text-end"><fmt:formatNumber value="${view.component.baseSalary}" type="currency" currencySymbol="$"/></td>
                                                <td class="text-end"><fmt:formatNumber value="${view.component.positionAllowance}" type="currency" currencySymbol="$"/></td>
                                                <td class="text-end"><fmt:formatNumber value="${view.component.housingAllowance}" type="currency" currencySymbol="$"/></td>
                                                <td class="text-end"><fmt:formatNumber value="${view.component.transportationAllowance}" type="currency" currencySymbol="$"/></td>
                                                <td class="text-end"><fmt:formatNumber value="${view.component.mealAllowance}" type="currency" currencySymbol="$"/></td>
                                                <td class="text-end"><fmt:formatNumber value="${view.component.otherAllowances}" type="currency" currencySymbol="$"/></td>
                                                <td class="text-end"><strong><fmt:formatNumber value="${view.component.totalMonthlySalary}" type="currency" currencySymbol="$"/></strong></td>
                                                <td><fmt:formatDate value="${view.component.effectiveFrom}" pattern="yyyy-MM-dd"/></td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

