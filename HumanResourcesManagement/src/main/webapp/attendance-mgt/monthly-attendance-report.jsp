<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Monthly Attendance Report - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    
    <!-- Custom Styles -->
    <style>
        .filter-section {
            background-color: #f8f9fa;
            border-radius: 0.375rem;
            padding: 1.5rem 0;
            margin-bottom: 1.5rem;
        }
        .text-nowrap { white-space: nowrap; }
        .badge-soft-success { background-color: #e9f7ef; color: #1e7e34; border: 1px solid #c7ead7; }
        .badge-soft-danger  { background-color: #fdecea; color: #a71d2a; border: 1px solid #f5c6cb; }
        .badge-soft-warning { background-color: #fff6e5; color: #8a6d3b; border: 1px solid #ffe0b3; }
        .badge-soft-primary { background-color: #e8f0fe; color: #1a4fb8; border: 1px solid #c9dbff; }
        .badge-soft-info    { background-color: #e6f7fb; color: #0c5460; border: 1px solid #bde5f1; }
        /* Ensure export icon turns white on hover */
        .btn-outline-success:hover i { color: #fff !important; }
    </style>
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="Monthly Attendance Report" />
        </jsp:include>

        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="#">Contracts & Attendance</a></li>
                <li class="breadcrumb-item active">Monthly Attendance Report</li>
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
                <h5 class="mb-3"><i class="fas fa-filter me-2"></i>Filter Monthly Report</h5>
                <form method="GET" action="${pageContext.request.contextPath}/attendance/monthly-report" id="filterForm">
                    <div class="row g-3">
                        <div class="col-md-2">
                            <label for="year" class="form-label">Year</label>
                            <select class="form-select" id="year" name="year">
                                <c:forEach var="y" begin="2020" end="2030">
                                    <option value="${y}" ${filterYear == y ? 'selected' : ''}>${y}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <label for="month" class="form-label">Month</label>
                            <select class="form-select" id="month" name="month">
                                <option value="">All Months</option>
                                <option value="1" ${filterMonth == 1 ? 'selected' : ''}>January</option>
                                <option value="2" ${filterMonth == 2 ? 'selected' : ''}>February</option>
                                <option value="3" ${filterMonth == 3 ? 'selected' : ''}>March</option>
                                <option value="4" ${filterMonth == 4 ? 'selected' : ''}>April</option>
                                <option value="5" ${filterMonth == 5 ? 'selected' : ''}>May</option>
                                <option value="6" ${filterMonth == 6 ? 'selected' : ''}>June</option>
                                <option value="7" ${filterMonth == 7 ? 'selected' : ''}>July</option>
                                <option value="8" ${filterMonth == 8 ? 'selected' : ''}>August</option>
                                <option value="9" ${filterMonth == 9 ? 'selected' : ''}>September</option>
                                <option value="10" ${filterMonth == 10 ? 'selected' : ''}>October</option>
                                <option value="11" ${filterMonth == 11 ? 'selected' : ''}>November</option>
                                <option value="12" ${filterMonth == 12 ? 'selected' : ''}>December</option>
                            </select>
                        </div>
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
                        <div class="col-md-2 d-flex align-items-end">
                            <div class="d-flex w-100 gap-2">
                                <button type="submit" class="btn btn-primary flex-fill">
                                    <i class="fas fa-search me-1"></i>Filter
                                </button>
                                <a href="${pageContext.request.contextPath}/attendance/monthly-report" class="btn btn-outline-secondary flex-fill text-nowrap">
                                    <i class="fas fa-redo me-1"></i>Reset
                                </a>
                            </div>
                        </div>
                    </div>
                </form>
            </div>

            <!-- Monthly Records Table -->
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <span><i class="fas fa-calendar-alt me-2"></i>Monthly Attendance Records</span>
                    <div class="d-flex align-items-center gap-2">
                        <span class="badge bg-light text-dark">${totalRecords} total records</span>
                        <c:if test="${not empty monthlyRecords}">
                            <div class="btn-group" role="group">
                                <button type="button" class="btn btn-outline-success btn-sm dropdown-toggle" data-bs-toggle="dropdown" aria-expanded="false">
                                    <i class="fas fa-download me-1"></i>Export
                                </button>
                                <ul class="dropdown-menu">
                                    <li>
                                        <a class="dropdown-item" href="${pageContext.request.contextPath}/attendance/export-monthly?format=xlsx&month=${filterMonth}&year=${filterYear}&employeeCode=${filterEmployeeCode}&departmentId=${filterDepartmentId}">
                                            <i class="fas fa-file-excel text-success me-2"></i>Excel (.xlsx)
                                        </a>
                                    </li>
                                    <li>
                                        <a class="dropdown-item" href="${pageContext.request.contextPath}/attendance/export-monthly?format=pdf&month=${filterMonth}&year=${filterYear}&employeeCode=${filterEmployeeCode}&departmentId=${filterDepartmentId}">
                                            <i class="fas fa-file-pdf text-danger me-2"></i>PDF (.pdf)
                                        </a>
                                    </li>
                                </ul>
                            </div>
                        </c:if>
                    </div>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty monthlyRecords}">
                            <div class="text-center py-5">
                                <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                                <p class="text-muted">No monthly attendance records found matching your filters.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover table-striped">
                                    <thead class="table-dark">
                                        <tr>
                                            <th class="text-nowrap" style="width: 5%;">#</th>
                                            <th class="text-nowrap" style="width: 10%;">Employee Code</th>
                                            <th class="text-nowrap" style="width: 15%;">Employee Name</th>
                                            <th class="text-nowrap" style="width: 12%;">Department</th>
                                            <th class="text-nowrap" style="width: 8%;">Month</th>
                                            <th class="text-nowrap" style="width: 7%;">Present</th>
                                            <th class="text-nowrap" style="width: 7%;">Absent</th>
                                            <th class="text-nowrap" style="width: 7%;">Late</th>
                                            <th class="text-nowrap" style="width: 7%;">Remote</th>
                                            <th class="text-nowrap" style="width: 8%;">Overtime</th>
                                            <th class="text-nowrap" style="width: 7%;">Total Days</th>
                                            <th class="text-nowrap" style="width: 7%;">Rate</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="record" items="${monthlyRecords}" varStatus="status">
                                            <tr>
                                                <td>${(currentPage - 1) * pageSize + status.index + 1}</td>
                                                <td><strong>${record.employeeCode}</strong></td>
                                                <td>${record.employeeName}</td>
                                                <td>${record.departmentName}</td>
                                                <td>${record.attendanceMonth}</td>
                                                <td><span class="badge badge-soft-success">${record.presentDays}</span></td>
                                                <td><span class="badge badge-soft-danger">${record.absentDays}</span></td>
                                                <td><span class="badge badge-soft-warning">${record.lateDays}</span></td>
                                                <td><span class="badge badge-soft-primary">${record.remoteDays}</span></td>
                                                <td><fmt:formatNumber value="${record.totalOvertimeHours}" maxFractionDigits="1"/> hrs</td>
                                                <td>${record.totalWorkingDays}</td>
                                                <td>
                                                    <c:set var="rate" value="${record.attendanceRate}" />
                                                    <c:choose>
                                                        <c:when test="${rate >= 95}">
                                                            <span class="badge badge-soft-success"><fmt:formatNumber value="${rate}" maxFractionDigits="1"/>%</span>
                                                        </c:when>
                                                        <c:when test="${rate >= 80}">
                                                            <span class="badge badge-soft-warning"><fmt:formatNumber value="${rate}" maxFractionDigits="1"/>%</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge badge-soft-danger"><fmt:formatNumber value="${rate}" maxFractionDigits="1"/>%</span>
                                                        </c:otherwise>
                                                    </c:choose>
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
                                            <a class="page-link" href="?page=${currentPage - 1}&month=${filterMonth}&year=${filterYear}&employeeCode=${filterEmployeeCode}&departmentId=${filterDepartmentId}">
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
                                                        <a class="page-link" href="?page=${i}&month=${filterMonth}&year=${filterYear}&employeeCode=${filterEmployeeCode}&departmentId=${filterDepartmentId}">
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
                                            <a class="page-link" href="?page=${currentPage + 1}&month=${filterMonth}&year=${filterYear}&employeeCode=${filterEmployeeCode}&departmentId=${filterDepartmentId}">
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

