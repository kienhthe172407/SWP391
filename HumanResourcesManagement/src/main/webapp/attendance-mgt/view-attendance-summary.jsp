<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Attendance Summary - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
    
    <!-- Custom Styles (soft tone to match system) -->
    <style>
        .filter-section {
            background-color: #f8f9fa;
            border-radius: 0.375rem;
            padding: 1.5rem 0; /* remove left/right padding */
            margin-bottom: 1.5rem;
        }
        .text-nowrap { white-space: nowrap; }
        .badge-soft-success { background-color: #e9f7ef; color: #1e7e34; border: 1px solid #c7ead7; }
        .badge-soft-danger  { background-color: #fdecea; color: #a71d2a; border: 1px solid #f5c6cb; }
        .badge-soft-warning { background-color: #fff6e5; color: #8a6d3b; border: 1px solid #ffe0b3; }
        .badge-soft-primary { background-color: #e8f0fe; color: #1a4fb8; border: 1px solid #c9dbff; }
        .badge-soft-info    { background-color: #e6f7fb; color: #0c5460; border: 1px solid #bde5f1; }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="sidebar-header">
            <c:set var="roleName" value="${sessionScope.userRole != null ? sessionScope.userRole : (sessionScope.user != null ? sessionScope.user.roleDisplayName : '')}" />
            <c:set var="isHRManager" value="${roleName == 'HR Manager' || (sessionScope.user != null && sessionScope.user.role == 'HR_MANAGER')}" />
            <c:set var="isHR" value="${roleName == 'HR' || (sessionScope.user != null && sessionScope.user.role == 'HR')}" />
            <c:choose>
                <c:when test="${isHRManager}">
                    <h4>HR Manager Dashboard</h4>
                    <p>Human Resources</p>
                </c:when>
                <c:when test="${isHR}">
                    <h4>HR Dashboard</h4>
                    <p>Human Resources</p>
                </c:when>
                <c:otherwise>
                    <h4>HR Management</h4>
                    <p>Human Resources System</p>
                </c:otherwise>
            </c:choose>
        </div>
        
        <ul class="sidebar-menu">
            <c:choose>
                <c:when test="${isHRManager}">
            <li class="menu-section">Dashboard</li>
            <li>
                        <a href="${pageContext.request.contextPath}/dashboard/hr-manager-dashboard.jsp">
                    <i class="fas fa-home"></i>
                    <span>Overview</span>
                </a>
            </li>

                    <li class="menu-section">HR Management</li>
                    <li><a href="#"><i class="fas fa-users-cog"></i><span>HR Staff Management</span></a></li>
                    <li><a href="#"><i class="fas fa-tasks"></i><span>Task Assignment</span></a></li>
                    <li><a href="#"><i class="fas fa-clipboard-check"></i><span>Approval Queue</span></a></li>
            
                    <li class="menu-section">Employee Management</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/employees/list">
                            <i class="fas fa-users"></i><span>All Employees</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/employees/addInformation">
                            <i class="fas fa-user-plus"></i>
                            <span>Add Employee Information</span>
                        </a>
                    </li>
                </a>
            </li>
            
                    <li class="menu-section">Contracts & Attendance</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/contracts/list">
                            <i class="fas fa-file-contract"></i><span>Contracts</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/attendance/summary" class="active">
                            <i class="fas fa-clock"></i><span>Attendance</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-calendar-check"></i><span>Leave Requests</span>
                        </a>
                    </li>

                    <li class="menu-section">Recruitment</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/job-postings/list">
                            <i class="fas fa-briefcase"></i><span> Job Postings</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/job-postings/create">
                            <i class="fas fa-plus"></i><span>Create Job Posting</span>
                        </a>
                    </li>

                    <li class="menu-section">Payroll & Benefits</li>
                    <li>
                        <a href="#">
                            <i class="fas fa-dollar-sign"></i><span>Payroll</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-gift"></i><span>Benefits</span>
                        </a>
                    </li>

                    <li class="menu-section">Payroll & Benefits</li>
                    <li><a href="#"><i class="fas fa-dollar-sign"></i><span>Payroll</span></a></li>
                    <li><a href="#"><i class="fas fa-gift"></i><span>Benefits</span></a></li>

                    <li class="menu-section">Reports & Analytics</li>
                    <li><a href="#"><i class="fas fa-chart-bar"></i><span>HR Reports</span></a></li>
                    <li><a href="#"><i class="fas fa-chart-line"></i><span>Analytics</span></a></li>
                    <li><a href="#"><i class="fas fa-chart-pie"></i><span>Statistics</span></a></li>
                </c:when>
                <c:otherwise>
                    <!-- HR Staff Menu -->
                    <li class="menu-section">Dashboard</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/dashboard/hr-dashboard.jsp">
                            <i class="fas fa-home"></i><span>Overview</span>
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
                </a>
            </li>
            
                    <li class="menu-section">Contracts & Attendance</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/contracts/list">
                            <i class="fas fa-file-contract"></i><span>Contracts</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/attendance/summary" class="active">
                            <i class="fas fa-clock"></i><span>Attendance</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-calendar-check"></i><span>Leave Requests</span>
                        </a>
                    </li>

                    <li class="menu-section">Recruitment</li>
            <li>
                <a href="${pageContext.request.contextPath}/job-postings/list">
                            <i class="fas fa-briefcase"></i><span> Job Postings</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/job-postings/create">
                            <i class="fas fa-plus"></i><span>Create Job Posting</span>
                </a>
            </li>
            <li class="menu-section">Payroll & Benefits</li>
                    <li>
                        <a href="#">
                            <i class="fas fa-dollar-sign"></i>
                            <span>Payroll</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-gift"></i>
                            <span>Benefits</span>
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
            <h1>Attendance Summary</h1>
                <div class="user-info">
                <span><c:out value="${sessionScope.user.roleDisplayName}" default="HR"/></span>
                <div class="avatar">HR</div>
            </div>
        </div>

        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="#">Contracts & Attendance</a></li>
                <li class="breadcrumb-item active">Attendance Summary</li>
            </ol>
        </nav>

        <!-- Content Area -->
        <div class="content-area">
            <!-- Success/Error Messages -->
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>${sessionScope.successMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="successMessage" scope="session"/>
            </c:if>
            
            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${sessionScope.errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="errorMessage" scope="session"/>
            </c:if>
            
            <!-- Filter Section -->
            <div class="filter-section">
                <h5 class="mb-3"><i class="fas fa-filter me-2"></i>Filter Attendance Records</h5>
                <form method="GET" action="${pageContext.request.contextPath}/attendance/summary" id="filterForm">
                    <div class="row g-3">
                        <div class="col-md-3">
                            <label for="employeeCode" class="form-label">Employee</label>
                            <select class="form-select" id="employeeCode" name="employeeCode">
                                <option value="">All Employees</option>
                                <c:forEach var="emp" items="${employees}">
                                    <option value="${emp.employeeCode}" ${filterEmployeeCode == emp.employeeCode ? 'selected' : ''}>
                                        ${emp.employeeCode} - ${emp.firstName} ${emp.lastName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label for="departmentId" class="form-label">Department</label>
                            <select class="form-select" id="departmentId" name="departmentId">
                                <option value="">All Departments</option>
                                <c:forEach var="dept" items="${departments}">
                                    <option value="${dept.departmentId}" ${filterDepartmentId == dept.departmentId ? 'selected' : ''}>
                                        ${dept.departmentName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <label for="startDate" class="form-label">Start Date</label>
                            <input type="date" class="form-control" id="startDate" name="startDate" value="${filterStartDate}">
                        </div>
                        <div class="col-md-2">
                            <label for="endDate" class="form-label">End Date</label>
                            <input type="date" class="form-control" id="endDate" name="endDate" value="${filterEndDate}">
                        </div>
                        <div class="col-md-2 d-flex align-items-end">
                            <div class="d-flex w-100 gap-2">
                                <button type="submit" class="btn btn-primary flex-fill">
                                    <i class="fas fa-search me-1"></i>Filter
                                </button>
                                <a href="${pageContext.request.contextPath}/attendance/summary" class="btn btn-outline-secondary flex-fill text-nowrap">
                                    <i class="fas fa-redo me-1"></i>Reset
                                </a>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
            
            <!-- Attendance Summary Section -->
            <c:if test="${(not empty summary) and ((not empty filterEmployeeCode) or (not empty filterDepartmentId))}">
                <div class="card mb-3">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <span>
                            <i class="fas fa-chart-pie me-2"></i>
                            Attendance Summary
                            <c:choose>
                                <c:when test="${not empty filterEmployeeCode}">
                                    <span class="text-muted">(by Employee)</span>
                                </c:when>
                                <c:when test="${empty filterEmployeeCode and not empty filterDepartmentId}">
                                    <span class="text-muted">(by Department)</span>
                                </c:when>
                            </c:choose>
                        </span>
                        <small class="text-muted">
                            <c:if test="${not empty filterStartDate || not empty filterEndDate}">
                                Range: ${empty filterStartDate ? '...' : filterStartDate} - ${empty filterEndDate ? '...' : filterEndDate}
                            </c:if>
                            <c:if test="${not empty filterDepartmentId}">
                                &nbsp;· Dept:
                                <c:forEach var="d" items="${departments}">
                                    <c:if test="${d.departmentId == filterDepartmentId}">${d.departmentName}</c:if>
                                </c:forEach>
                            </c:if>
                            <c:if test="${not empty filterEmployeeCode}">
                                &nbsp;· Emp:
                                <c:forEach var="e" items="${employees}">
                                    <c:if test="${e.employeeCode == filterEmployeeCode}">${e.firstName} ${e.lastName}</c:if>
                                </c:forEach>
                            </c:if>
                        </small>
                    </div>
                    <div class="card-body">
                        <div class="row g-3">
                            <div class="col-6 col-md-3">
                                <div class="p-3 border rounded h-100">
                                    <div class="text-muted small">Total Days</div>
                                    <div class="fs-5 fw-bold">${summary.totalDays}</div>
                                </div>
                            </div>
                            <div class="col-6 col-md-3">
                                <div class="p-3 border rounded h-100">
                                    <div class="text-muted small">Present</div>
                                    <div class="fs-5 fw-bold"><span class="badge badge-soft-success">${summary.presentDays}</span></div>
                                </div>
                            </div>
                            <div class="col-6 col-md-3">
                                <div class="p-3 border rounded h-100">
                                    <div class="text-muted small">Late</div>
                                    <div class="fs-5 fw-bold"><span class="badge badge-soft-warning">${summary.lateDays}</span></div>
                                </div>
                            </div>
                            <div class="col-6 col-md-3">
                                <div class="p-3 border rounded h-100">
                                    <div class="text-muted small">Absent</div>
                                    <div class="fs-5 fw-bold"><span class="badge badge-soft-danger">${summary.absentDays}</span></div>
                                </div>
                            </div>
                            <div class="col-6 col-md-3">
                                <div class="p-3 border rounded h-100">
                                    <div class="text-muted small">Overtime Hours</div>
                                    <div class="fs-5 fw-bold"><fmt:formatNumber value="${summary.totalOvertimeHours}" maxFractionDigits="1"/> hrs</div>
                                </div>
                            </div>
                            <div class="col-6 col-md-3">
                                <div class="p-3 border rounded h-100">
                                    <div class="text-muted small">Working Days</div>
                                    <div class="fs-5 fw-bold">${summary.workingDays}</div>
                                </div>
                            </div>
                            <div class="col-6 col-md-3">
                                <div class="p-3 border rounded h-100">
                                    <div class="text-muted small">Attendance Rate</div>
                                    <div class="fs-5 fw-bold"><fmt:formatNumber value="${summary.attendanceRate}" maxFractionDigits="1"/>%</div>
                                </div>
                            </div>
                            <div class="col-6 col-md-3">
                                <div class="p-3 border rounded h-100">
                                    <div class="text-muted small">Manual Adjustments</div>
                                    <div class="fs-5 fw-bold">${summary.manualAdjustments}</div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${empty filterEmployeeCode and empty filterDepartmentId}">
                <div class="alert alert-info d-flex align-items-center mb-3" role="alert">
                    <i class="fas fa-info-circle me-2"></i>
                    Please select an Employee or a Department to view the aggregated Attendance Summary. Otherwise, the system will only display detailed records.
                </div>
            </c:if>
            
            <!-- Attendance Records Table -->
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <span><i class="fas fa-table me-2"></i>Attendance Records</span>
                    <span class="badge bg-light text-dark">${totalRecords} total records</span>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty attendanceRecords}">
                            <div class="text-center py-5">
                                <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                                <p class="text-muted">No attendance records found matching your filters.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover table-striped">
                                    <thead class="table-dark">
                                        <tr>
                                            <th class="text-nowrap" style="width: 5%;">#</th>
                                            <th class="text-nowrap" style="width: 12%;">Employee Code</th>
                                            <th class="text-nowrap" style="width: 18%;">Employee Name</th>
                                            <th class="text-nowrap" style="width: 12%;">Date</th>
                                            <th class="text-nowrap" style="width: 10%;">Check-in</th>
                                            <th class="text-nowrap" style="width: 10%;">Check-out</th>
                                            <th class="text-nowrap" style="width: 12%;">Status</th>
                                            <th class="text-nowrap" style="width: 8%;">Overtime</th>
                                            <th class="text-nowrap" style="width: 10%;">Adjustment</th>
                                            <th class="text-nowrap" style="width: 10%;">Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="record" items="${attendanceRecords}" varStatus="status">
                                            <tr>
                                                <td>${(currentPage - 1) * pageSize + status.index + 1}</td>
                                                <td><strong>${record.employeeCode}</strong></td>
                                                <td>${record.employeeName}</td>
                                                <td>
                                                    <fmt:formatDate value="${record.attendanceDate}" pattern="yyyy-MM-dd"/>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${record.checkInTime != null}">
                                                            <fmt:formatDate value="${record.checkInTime}" pattern="HH:mm:ss"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">-</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${record.checkOutTime != null}">
                                                            <fmt:formatDate value="${record.checkOutTime}" pattern="HH:mm:ss"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">-</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${record.status == 'Present'}">
                                                            <span class="badge badge-soft-success">Present</span>
                                                        </c:when>
                                                        <c:when test="${record.status == 'Absent'}">
                                                            <span class="badge badge-soft-danger">Absent</span>
                                                        </c:when>
                                                        <c:when test="${record.status == 'Late'}">
                                                            <span class="badge badge-soft-warning">Late</span>
                                                        </c:when>
                                                        <c:when test="${record.status == 'Early Leave'}">
                                                            <span class="badge badge-soft-warning">Early Leave</span>
                                                        </c:when>
                                                        <c:when test="${record.status == 'Business Trip'}">
                                                            <span class="badge badge-soft-info">Business Trip</span>
                                                        </c:when>
                                                        <c:when test="${record.status == 'Remote'}">
                                                            <span class="badge badge-soft-primary">Remote</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-light text-secondary">${record.status}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <fmt:formatNumber value="${record.overtimeHours}" maxFractionDigits="1"/> hrs
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${record.isManualAdjustment}">
                                                            <span class="badge bg-warning text-dark"
                                                                  title="${record.adjustmentReason}">
                                                                <i class="fas fa-edit"></i> Manual
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">-</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <a href="${pageContext.request.contextPath}/attendance/adjust?id=${record.attendanceID}"
                                                       class="btn btn-sm btn-outline-primary d-inline-flex align-items-center gap-1 text-nowrap"
                                                       title="Adjust this record">
                                                        <i class="fas fa-edit me-1"></i> Edit
                                                    </a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>

                            <!-- Pagination -->
                            <c:if test="${totalPages > 1}">
                                <nav aria-label="Page navigation" class="mt-4">
                                    <ul class="pagination justify-content-center">
                                        <!-- Previous Button -->
                                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                            <a class="page-link" href="?page=${currentPage - 1}&employeeCode=${filterEmployeeCode}&departmentId=${filterDepartmentId}&startDate=${filterStartDate}&endDate=${filterEndDate}">
                                                <i class="fas fa-chevron-left"></i> Previous
                                            </a>
                                        </li>

                                        <!-- Page Numbers -->
                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <c:choose>
                                                <c:when test="${i == currentPage}">
                                                    <li class="page-item active">
                                                        <span class="page-link">${i}</span>
                                                    </li>
                                                </c:when>
                                                <c:when test="${i == 1 || i == totalPages || (i >= currentPage - 2 && i <= currentPage + 2)}">
                                                    <li class="page-item">
                                                        <a class="page-link" href="?page=${i}&employeeCode=${filterEmployeeCode}&departmentId=${filterDepartmentId}&startDate=${filterStartDate}&endDate=${filterEndDate}">
                                                            ${i}
                                                        </a>
                                                    </li>
                                                </c:when>
                                                <c:when test="${i == currentPage - 3 || i == currentPage + 3}">
                                                    <li class="page-item disabled">
                                                        <span class="page-link">...</span>
                                                    </li>
                                                </c:when>
                                            </c:choose>
                                        </c:forEach>

                                        <!-- Next Button -->
                                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                            <a class="page-link" href="?page=${currentPage + 1}&employeeCode=${filterEmployeeCode}&departmentId=${filterDepartmentId}&startDate=${filterStartDate}&endDate=${filterEndDate}">
                                                Next <i class="fas fa-chevron-right"></i>
                                            </a>
                                        </li>
                                    </ul>
                                </nav>
                            </c:if>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        // Auto-dismiss alerts after 5 seconds
        setTimeout(function() {
            var alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(alert) {
                var bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            });
        }, 5000);
    </script>
</body>
</html>
