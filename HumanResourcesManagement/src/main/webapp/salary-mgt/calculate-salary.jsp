<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Calculate Monthly Salary - HR Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    <style>
        .calculation-card {
            border-left: 4px solid #0d6efd;
        }
        .breakdown-section {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 10px;
        }
        .breakdown-item {
            display: flex;
            justify-content: space-between;
            padding: 5px 0;
            border-bottom: 1px solid #dee2e6;
        }
        .breakdown-item:last-child {
            border-bottom: none;
        }
        .breakdown-total {
            font-weight: bold;
            font-size: 1.1rem;
            color: #0d6efd;
        }
        .summary-card {
            background-color: #f8f9fa; /* subtle, less prominent */
            color: #212529; /* make text black */
            border: 1px solid #dee2e6;
        }
        .summary-card .card-body {
            padding: 1rem; /* reduce spacing */
        }
        .summary-card h6 {
            color: #212529; /* headings black */
            margin-bottom: .5rem;
            font-weight: 600;
        }
        .summary-value {
            font-size: 1.5rem; /* reduce size */
            line-height: 1.2;
            font-weight: 700;
            color: #212529; /* numbers black */
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
            <h1>Calculate Monthly Salary</h1>
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
                <li class="breadcrumb-item active">Calculate Salary</li>
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

            <!-- Search Form (only show when calculations exist) -->
            <c:if test="${not empty calculations}">
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="fas fa-search me-2"></i>Search Employee</h5>
                    </div>
                    <div class="card-body">
                        <form method="GET" action="${pageContext.request.contextPath}/salary/calculate">
                            <div class="row g-3">
                                <div class="col-md-4">
                                    <label class="form-label">Employee Code</label>
                                    <input type="text" name="employeeCode" class="form-control" 
                                           placeholder="Enter employee code..." value="${param.employeeCode}">
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Employee Name</label>
                                    <input type="text" name="employeeName" class="form-control" 
                                           placeholder="Enter employee name..." value="${param.employeeName}">
                                </div>
                                <div class="col-md-4 d-flex align-items-end gap-2">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="fas fa-search me-1"></i>Search
                                    </button>
                                    <a href="${pageContext.request.contextPath}/salary/calculate" class="btn btn-secondary">
                                        <i class="fas fa-redo me-1"></i>Reset
                                    </a>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </c:if>

            <!-- Calculation Form -->
            <div class="card mb-4">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0"><i class="fas fa-calendar-alt me-2 text-white"></i>Select Month for Salary Calculation</h5>
                </div>
                <div class="card-body">
                    <form method="POST" action="${pageContext.request.contextPath}/salary/calculate">
                        <input type="hidden" name="action" value="calculate">
                        <div class="row">
                            <div class="col-md-4">
                                <label for="year" class="form-label">Year</label>
                                <select class="form-select" id="year" name="year" required>
                                    <c:forEach var="y" begin="2020" end="2030">
                                        <option value="${y}" ${y == defaultYear ? 'selected' : ''}>${y}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label for="month" class="form-label">Month</label>
                                <select class="form-select" id="month" name="month" required>
                                    <option value="1" ${defaultMonth == 1 ? 'selected' : ''}>January</option>
                                    <option value="2" ${defaultMonth == 2 ? 'selected' : ''}>February</option>
                                    <option value="3" ${defaultMonth == 3 ? 'selected' : ''}>March</option>
                                    <option value="4" ${defaultMonth == 4 ? 'selected' : ''}>April</option>
                                    <option value="5" ${defaultMonth == 5 ? 'selected' : ''}>May</option>
                                    <option value="6" ${defaultMonth == 6 ? 'selected' : ''}>June</option>
                                    <option value="7" ${defaultMonth == 7 ? 'selected' : ''}>July</option>
                                    <option value="8" ${defaultMonth == 8 ? 'selected' : ''}>August</option>
                                    <option value="9" ${defaultMonth == 9 ? 'selected' : ''}>September</option>
                                    <option value="10" ${defaultMonth == 10 ? 'selected' : ''}>October</option>
                                    <option value="11" ${defaultMonth == 11 ? 'selected' : ''}>November</option>
                                    <option value="12" ${defaultMonth == 12 ? 'selected' : ''}>December</option>
                                </select>
                            </div>
                            <div class="col-md-4 d-flex align-items-end">
                                <button type="submit" class="btn btn-primary w-100">
                                    <i class="fas fa-calculator me-2"></i>Calculate Salaries
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Calculation Results -->
            <c:if test="${not empty calculations}">
                <!-- Summary Cards -->
                <div class="row mb-4">
                    <div class="col-md-4">
                        <div class="card summary-card">
                            <div class="card-body text-center">
                                <h6>Total Employees</h6>
                                <div class="summary-value">${totalEmployees}</div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card summary-card">
                            <div class="card-body text-center">
                                <h6>Total Gross Salary</h6>
                                <div class="summary-value"><fmt:formatNumber value="${totalGrossSalary}" type="currency" currencySymbol="$"/></div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="card summary-card">
                            <div class="card-body text-center">
                                <h6>Total Net Salary</h6>
                                <div class="summary-value"><fmt:formatNumber value="${totalNetSalary}" type="currency" currencySymbol="$"/></div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Save Button -->
                <div class="card mb-4">
                    <div class="card-body">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <h5 class="mb-0">Salary Calculation for ${year}-${String.format("%02d", month)}</h5>
                                <p class="text-muted mb-0">Review the calculations below and save to database</p>
                            </div>
                            <form method="POST" action="${pageContext.request.contextPath}/salary/calculate" class="d-inline">
                                <input type="hidden" name="action" value="save">
                                <button type="submit" class="btn btn-success btn-lg">
                                    <i class="fas fa-save me-2"></i>Save All Calculations
                                </button>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- Detailed Calculations -->
                <div class="row">
                    <c:forEach var="calc" items="${calculations}">
                        <div class="col-md-6 mb-4">
                            <div class="card calculation-card h-100">
                                <div class="card-header bg-light">
                                    <h6 class="mb-0">
                                        <strong>${calc.employeeCode}</strong> - ${calc.employeeName}
                                        <br>
                                        <small class="text-muted">${calc.departmentName} | ${calc.positionName}</small>
                                    </h6>
                                </div>
                                <div class="card-body">
                                    <!-- Base Salary & Allowances -->
                                    <div class="breakdown-section">
                                        <h6 class="text-primary"><i class="fas fa-money-bill-wave me-2"></i>Base Salary & Allowances</h6>
                                        <div class="breakdown-item">
                                            <span>Base Salary:</span>
                                            <span><fmt:formatNumber value="${calc.baseSalary}" type="currency" currencySymbol="$"/></span>
                                        </div>
                                        <div class="breakdown-item">
                                            <span>Position Allowance:</span>
                                            <span><fmt:formatNumber value="${calc.positionAllowance}" type="currency" currencySymbol="$"/></span>
                                        </div>
                                        <div class="breakdown-item">
                                            <span>Housing Allowance:</span>
                                            <span><fmt:formatNumber value="${calc.housingAllowance}" type="currency" currencySymbol="$"/></span>
                                        </div>
                                        <div class="breakdown-item">
                                            <span>Transportation Allowance:</span>
                                            <span><fmt:formatNumber value="${calc.transportationAllowance}" type="currency" currencySymbol="$"/></span>
                                        </div>
                                        <div class="breakdown-item">
                                            <span>Meal Allowance:</span>
                                            <span><fmt:formatNumber value="${calc.mealAllowance}" type="currency" currencySymbol="$"/></span>
                                        </div>
                                        <div class="breakdown-item">
                                            <span>Other Allowances:</span>
                                            <span><fmt:formatNumber value="${calc.otherAllowances}" type="currency" currencySymbol="$"/></span>
                                        </div>
                                        <div class="breakdown-item breakdown-total">
                                            <span>Total Allowances:</span>
                                            <span><fmt:formatNumber value="${calc.totalAllowances}" type="currency" currencySymbol="$"/></span>
                                        </div>
                                    </div>

                                    <!-- Attendance & Overtime -->
                                    <div class="breakdown-section">
                                        <h6 class="text-success"><i class="fas fa-clock me-2"></i>Attendance & Overtime</h6>
                                        <div class="breakdown-item">
                                            <span>Working Days:</span>
                                            <span>${calc.workingDays} days</span>
                                        </div>
                                        <div class="breakdown-item">
                                            <span>Absent Days:</span>
                                            <span>${calc.absentDays} days</span>
                                        </div>
                                        <div class="breakdown-item">
                                            <span>Late Days:</span>
                                            <span>${calc.lateDays} days</span>
                                        </div>
                                        <div class="breakdown-item">
                                            <span>Overtime Hours:</span>
                                            <span><fmt:formatNumber value="${calc.overtimeHours}" pattern="#,##0.00"/> hours</span>
                                        </div>
                                        <div class="breakdown-item breakdown-total">
                                            <span>Overtime Pay (1.5x):</span>
                                            <span><fmt:formatNumber value="${calc.overtimePay}" type="currency" currencySymbol="$"/></span>
                                        </div>
                                    </div>

                                    <!-- Benefits -->
                                    <c:if test="${not empty calc.benefits}">
                                        <div class="breakdown-section">
                                            <h6 class="text-info"><i class="fas fa-gift me-2"></i>Benefits</h6>
                                            <c:forEach var="benefit" items="${calc.benefits}">
                                                <div class="breakdown-item">
                                                    <span>${benefit.benefitName} (${benefit.calculationType}):</span>
                                                    <span><fmt:formatNumber value="${benefit.amount}" type="currency" currencySymbol="$"/></span>
                                                </div>
                                            </c:forEach>
                                            <div class="breakdown-item breakdown-total">
                                                <span>Total Benefits:</span>
                                                <span><fmt:formatNumber value="${calc.totalBenefits}" type="currency" currencySymbol="$"/></span>
                                            </div>
                                        </div>
                                    </c:if>

                                    <!-- Gross Salary -->
                                    <div class="breakdown-section bg-primary text-white">
                                        <div class="breakdown-item">
                                            <span><strong>GROSS SALARY:</strong></span>
                                            <span><strong><fmt:formatNumber value="${calc.grossSalary}" type="currency" currencySymbol="$"/></strong></span>
                                        </div>
                                    </div>

                                    <!-- Deductions -->
                                    <c:if test="${not empty calc.deductions}">
                                        <div class="breakdown-section">
                                            <h6 class="text-danger"><i class="fas fa-minus-circle me-2"></i>Deductions</h6>
                                            <c:forEach var="deduction" items="${calc.deductions}">
                                                <div class="breakdown-item">
                                                    <span>${deduction.deductionName} (${deduction.calculationType}):</span>
                                                    <span class="text-danger">-<fmt:formatNumber value="${deduction.amount}" type="currency" currencySymbol="$"/></span>
                                                </div>
                                            </c:forEach>
                                            <div class="breakdown-item breakdown-total text-danger">
                                                <span>Total Deductions:</span>
                                                <span>-<fmt:formatNumber value="${calc.totalDeductions}" type="currency" currencySymbol="$"/></span>
                                            </div>
                                        </div>
                                    </c:if>

                                    <!-- Net Salary -->
                                    <div class="breakdown-section bg-success text-white">
                                        <div class="breakdown-item">
                                            <span><strong>NET SALARY (Take Home):</strong></span>
                                            <span><strong><fmt:formatNumber value="${calc.netSalary}" type="currency" currencySymbol="$"/></strong></span>
                                        </div>
                                    </div>

                                    <!-- Calculation Notes -->
                                    <c:if test="${not empty calc.calculationNotes}">
                                        <div class="mt-3">
                                            <small class="text-muted">
                                                <i class="fas fa-info-circle me-1"></i>
                                                <c:forEach var="note" items="${calc.calculationNotes}">
                                                    ${note}<br>
                                                </c:forEach>
                                            </small>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                
                <!-- Pagination -->
                <c:if test="${totalPages > 1}">
                    <div class="d-flex justify-content-between align-items-center mt-4">
                        <div class="pagination-info">
                            Showing ${(currentPage - 1) * pageSize + 1} to ${currentPage * pageSize > totalRecords ? totalRecords : currentPage * pageSize} of ${totalRecords} employees
                        </div>
                        <nav>
                            <ul class="pagination mb-0">
                                <!-- Previous Button -->
                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <c:url value="/salary/calculate" var="prevUrl">
                                        <c:param name="page" value="${currentPage - 1}"/>
                                        <c:param name="employeeCode" value="${param.employeeCode}"/>
                                        <c:param name="employeeName" value="${param.employeeName}"/>
                                        <c:param name="year" value="${year}"/>
                                        <c:param name="month" value="${month}"/>
                                    </c:url>
                                    <a class="page-link" href="${currentPage > 1 ? prevUrl : '#'}" aria-label="Previous">
                                        <i class="fas fa-chevron-left"></i>
                                    </a>
                                </li>
                                
                                <!-- Page Numbers -->
                                <c:forEach begin="1" end="${totalPages}" var="pageNum">
                                    <c:if test="${pageNum <= 3 || pageNum > totalPages - 3 || (pageNum >= currentPage - 1 && pageNum <= currentPage + 1)}">
                                        <c:url value="/salary/calculate" var="pageUrl">
                                            <c:param name="page" value="${pageNum}"/>
                                            <c:param name="employeeCode" value="${param.employeeCode}"/>
                                            <c:param name="employeeName" value="${param.employeeName}"/>
                                            <c:param name="year" value="${year}"/>
                                            <c:param name="month" value="${month}"/>
                                        </c:url>
                                        <li class="page-item ${currentPage == pageNum ? 'active' : ''}">
                                            <a class="page-link" href="${pageUrl}">${pageNum}</a>
                                        </li>
                                    </c:if>
                                    <c:if test="${(pageNum == 3 && currentPage > 4 && totalPages > 7) || (pageNum == totalPages - 3 && currentPage < totalPages - 3 && totalPages > 7)}">
                                        <li class="page-item disabled">
                                            <span class="page-link">...</span>
                                        </li>
                                    </c:if>
                                </c:forEach>
                                
                                <!-- Next Button -->
                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <c:url value="/salary/calculate" var="nextUrl">
                                        <c:param name="page" value="${currentPage + 1}"/>
                                        <c:param name="employeeCode" value="${param.employeeCode}"/>
                                        <c:param name="employeeName" value="${param.employeeName}"/>
                                        <c:param name="year" value="${year}"/>
                                        <c:param name="month" value="${month}"/>
                                    </c:url>
                                    <a class="page-link" href="${currentPage < totalPages ? nextUrl : '#'}" aria-label="Next">
                                        <i class="fas fa-chevron-right"></i>
                                    </a>
                                </li>
                            </ul>
                        </nav>
                    </div>
                </c:if>
            </c:if>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            var successAlerts = document.querySelectorAll('.alert.alert-success');
            successAlerts.forEach(function(alertEl) {
                setTimeout(function() {
                    try {
                        var bsAlert = new bootstrap.Alert(alertEl);
                        bsAlert.close();
                    } catch (e) {
                        alertEl.remove();
                    }
                }, 3000);
            });
        });
    </script>
</body>
</html>

