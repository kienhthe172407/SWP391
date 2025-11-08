<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Salary Summary - HR Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    <style>
        .summary-card {
            border-left: 4px solid #0d6efd;
        }
        .summary-value {
            font-size: 1.5rem;
            font-weight: bold;
            color: #0d6efd;
        }
        .filter-section {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        .filter-section .row {
            margin: 0;
            display: flex;
            align-items: flex-start;
            gap: 15px;
        }
        .filter-section .col-md-2,
        .filter-section .col-md-3 {
            padding: 0;
            flex: 1;
            display: flex;
            flex-direction: column;
            justify-content: flex-start;
        }
        .filter-section .col-md-2:first-child,
        .filter-section .col-md-2:nth-child(2) {
            flex: 0 0 150px;
        }
        .filter-section .col-md-3:first-of-type {
            flex: 0 0 180px;
        }
        .filter-section .col-md-2:last-child {
            flex: 0 0 140px;
        }
        .filter-section .form-select,
        .filter-section .btn {
            width: 100%;
        }
        .filter-section .form-label {
            margin-bottom: 5px;
            height: 20px;
            line-height: 20px;
        }
        .table-actions {
            white-space: nowrap;
        }
        .status-badge {
            font-size: 0.85rem;
        }
        .bonus-adjustment {
            color: #198754;
            font-weight: 500;
        }
        .bonus-adjustment.negative {
            color: #dc3545;
        }
    </style>
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="Salary Summary" />
        </jsp:include>

        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard/hr-dashboard.jsp">Dashboard</a></li>
                <li class="breadcrumb-item active">Salary Summary</li>
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

            <!-- Filter Section -->
            <div class="filter-section">
                <form method="GET" action="${pageContext.request.contextPath}/salary/view-summary" id="filterForm">
                    <div class="row">
                        <div class="col-md-2">
                            <label class="form-label">Year</label>
                            <select name="year" class="form-select" onchange="document.getElementById('filterForm').submit()">
                                <c:forEach var="y" begin="2020" end="2030">
                                    <option value="${y}" ${y == year ? 'selected' : ''}>${y}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <label class="form-label">Month</label>
                            <select name="month" class="form-select" onchange="document.getElementById('filterForm').submit()">
                                <c:forEach var="m" begin="1" end="12">
                                    <option value="${m}" ${m == month ? 'selected' : ''}>${String.format("%02d", m)}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <label class="form-label">Filter Type</label>
                            <select name="filterType" class="form-select" id="filterTypeSelect" onchange="toggleFilters()">
                                <option value="all" ${filterType == 'all' ? 'selected' : ''}>All Employees</option>
                                <option value="employee" ${filterType == 'employee' ? 'selected' : ''}>By Employee</option>
                                <option value="department" ${filterType == 'department' ? 'selected' : ''}>By Department</option>
                            </select>
                        </div>
                        <div class="col-md-3" id="employeeFilter" style="display: ${filterType == 'employee' ? 'flex' : 'none'}">
                            <label class="form-label">Employee</label>
                            <select name="employeeId" class="form-select">
                                <option value="">Select Employee</option>
                                <c:forEach var="emp" items="${allEmployees}">
                                    <option value="${emp.employeeID}" ${emp.employeeID == selectedEmployeeId ? 'selected' : ''}>
                                        ${emp.employeeCode} - ${emp.firstName} ${emp.lastName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-3" id="departmentFilter" style="display: ${filterType == 'department' ? 'flex' : 'none'}">
                            <label class="form-label">Department</label>
                            <select name="department" class="form-select">
                                <option value="">Select Department</option>
                                <c:forEach var="dept" items="${departments}">
                                    <option value="${dept}" ${dept == selectedDepartment ? 'selected' : ''}>${dept}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <label class="form-label">&nbsp;</label>
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-filter me-2"></i>Apply Filter
                            </button>
                        </div>
                    </div>
                </form>
            </div>

            <!-- Summary Cards -->
            <div class="row mb-4">
                <div class="col-md-3">
                    <div class="card summary-card">
                        <div class="card-body">
                            <h6 class="text-muted mb-2">Total Employees</h6>
                            <div class="summary-value">${totalEmployees}</div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card summary-card">
                        <div class="card-body">
                            <h6 class="text-muted mb-2">Total Gross Salary</h6>
                            <div class="summary-value"><fmt:formatNumber value="${totalGrossSalary}" type="currency" currencySymbol="$"/></div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card summary-card">
                        <div class="card-body">
                            <h6 class="text-muted mb-2">Total Net Salary</h6>
                            <div class="summary-value"><fmt:formatNumber value="${totalNetSalary}" type="currency" currencySymbol="$"/></div>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card summary-card">
                        <div class="card-body">
                            <h6 class="text-muted mb-2">Bonus Adjustments</h6>
                            <div class="summary-value ${totalBonusAdjustments < 0 ? 'text-danger' : 'text-success'}">
                                <fmt:formatNumber value="${totalBonusAdjustments}" type="currency" currencySymbol="$"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Department Summary Section -->
            <c:if test="${not empty departmentSummaries && filterType == 'all'}">
                <div class="card mb-4">
                     <div class="card-header bg-primary text-white">
                         <h5 class="mb-0"><i class="fas fa-building me-2 text-white"></i>Payroll Summary by Department</h5>
                     </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead class="table-light">
                                    <tr>
                                        <th>Department</th>
                                        <th class="text-center">Employees</th>
                                        <th class="text-end">Total Gross Salary</th>
                                        <th class="text-end">Total Net Salary</th>
                                        <th class="text-end">Avg Gross Salary</th>
                                        <th class="text-end">Avg Net Salary</th>
                                        <th class="text-end">Bonus Adjustments</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="dept" items="${departmentSummaries}">
                                        <tr>
                                            <td><strong>${dept.departmentName}</strong></td>
                                            <td class="text-center">${dept.employeeCount}</td>
                                            <td class="text-end"><fmt:formatNumber value="${dept.totalGrossSalary}" type="currency" currencySymbol="$"/></td>
                                            <td class="text-end"><fmt:formatNumber value="${dept.totalNetSalary}" type="currency" currencySymbol="$"/></td>
                                            <td class="text-end"><fmt:formatNumber value="${dept.averageGrossSalary}" type="currency" currencySymbol="$"/></td>
                                            <td class="text-end"><fmt:formatNumber value="${dept.averageNetSalary}" type="currency" currencySymbol="$"/></td>
                                            <td class="text-end">
                                                <span class="${dept.totalBonusAdjustments < 0 ? 'text-danger' : 'text-success'}">
                                                    <fmt:formatNumber value="${dept.totalBonusAdjustments}" type="currency" currencySymbol="$"/>
                                                </span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Top Earners Section -->
            <c:if test="${not empty topEarners && filterType == 'all'}">
                <div class="card mb-4">
                     <div class="card-header bg-success text-white">
                         <h5 class="mb-0"><i class="fas fa-trophy me-2 text-white"></i>Top 10 Earners - ${year}-${String.format("%02d", month)}</h5>
                     </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead class="table-light">
                                    <tr>
                                        <th style="width: 50px;">Rank</th>
                                        <th>Employee</th>
                                        <th>Department</th>
                                        <th>Position</th>
                                        <th class="text-end">Gross Salary</th>
                                        <th class="text-end">Net Salary</th>
                                        <th class="text-end">Bonus Adj.</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="earner" items="${topEarners}" varStatus="status">
                                        <tr>
                                            <td class="text-center">
                                                <c:choose>
                                                    <c:when test="${status.index == 0}">
                                                        <span class="badge bg-warning text-dark" style="font-size: 1rem;">
                                                            <i class="fas fa-crown"></i> 1
                                                        </span>
                                                    </c:when>
                                                    <c:when test="${status.index == 1}">
                                                        <span class="badge bg-secondary" style="font-size: 0.9rem;">2</span>
                                                    </c:when>
                                                    <c:when test="${status.index == 2}">
                                                        <span class="badge bg-secondary" style="font-size: 0.9rem;">3</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-light text-dark">${status.index + 1}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <strong>${earner.employeeCode}</strong><br>
                                                <small>${earner.employeeName}</small>
                                            </td>
                                            <td>${earner.departmentName}</td>
                                            <td>${earner.positionName}</td>
                                            <td class="text-end"><fmt:formatNumber value="${earner.grossSalary}" type="currency" currencySymbol="$"/></td>
                                            <td class="text-end"><strong><fmt:formatNumber value="${earner.netSalary}" type="currency" currencySymbol="$"/></strong></td>
                                            <td class="text-end">
                                                <span class="${earner.bonusAdjustments < 0 ? 'text-danger' : 'text-success'}">
                                                    <fmt:formatNumber value="${earner.bonusAdjustments}" type="currency" currencySymbol="$"/>
                                                </span>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Pending Adjustments Section (HR Manager Only) -->
            <c:if test="${sessionScope.user != null && (sessionScope.user.role == 'HR Manager' || sessionScope.user.role == 'HR_MANAGER') && not empty allPendingAdjustments}">
                <div class="card mb-4">
                    <div class="card-header bg-warning text-dark">
                        <h5 class="mb-0"><i class="fas fa-clock me-2"></i>Pending Bonus Adjustments (${allPendingAdjustments.size()})</h5>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-hover">
                                <thead class="table-light">
                                    <tr>
                                        <th>Employee</th>
                                        <th>Month</th>
                                        <th>Type</th>
                                        <th class="text-end">Amount</th>
                                        <th>Reason</th>
                                        <th>Requested By</th>
                                        <th>Status</th>
                                        <th class="text-center">Actions</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="adj" items="${allPendingAdjustments}">
                                        <tr>
                                            <td>
                                                <strong>${adj.employeeCode}</strong><br>
                                                <small>${adj.employeeName}</small>
                                            </td>
                                            <td><fmt:formatDate value="${adj.payrollMonth}" pattern="yyyy-MM"/></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${adj.adjustmentType == 'Bonus'}">
                                                        <span class="badge bg-success">Bonus</span>
                                                    </c:when>
                                                    <c:when test="${adj.adjustmentType == 'Deduction'}">
                                                        <span class="badge bg-danger">Deduction</span>
                                                    </c:when>
                                                    <c:when test="${adj.adjustmentType == 'Retroactive'}">
                                                        <span class="badge bg-info">Retroactive</span>
                                                    </c:when>
                                                    <c:when test="${adj.adjustmentType == 'Correction'}">
                                                        <span class="badge bg-warning">Correction</span>
                                                    </c:when>
                                                </c:choose>
                                            </td>
                                            <td class="text-end">
                                                <strong class="${adj.adjustmentType == 'Deduction' ? 'text-danger' : 'text-success'}">
                                                    <fmt:formatNumber value="${adj.amount}" type="currency" currencySymbol="$"/>
                                                </strong>
                                            </td>
                                            <td><small>${adj.reason}</small></td>
                                            <td><small>User #${adj.requestedBy}</small></td>
                                            <td>
                                                <span class="badge bg-warning">Pending</span>
                                                <br/>
                                                <div class="btn-group btn-group-sm mt-1" role="group">
                                                    <button type="button" class="btn btn-success btn-sm approve-btn"
                                                            data-adjustment-id="${adj.adjustmentId}"
                                                            data-employee-name="${adj.employeeName}"
                                                            data-amount="${adj.amount}"
                                                            title="Approve Adjustment">
                                                        <i class="fas fa-check"></i> Approve
                                                    </button>
                                                    <button type="button" class="btn btn-danger btn-sm reject-btn"
                                                            data-adjustment-id="${adj.adjustmentId}"
                                                            data-employee-name="${adj.employeeName}"
                                                            data-amount="${adj.amount}"
                                                            title="Reject Adjustment">
                                                        <i class="fas fa-times"></i> Reject
                                                    </button>
                                                </div>
                                            </td>
                                            <td class="text-center">
                                                <a href="${pageContext.request.contextPath}/salary/adjust-bonus?employeeId=${adj.employeeId}&year=${year}&month=${month}"
                                                   class="btn btn-outline-info btn-sm" title="View Details">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Salary Summary Table -->
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0"><i class="fas fa-table me-2"></i>Salary Details - ${year}-${String.format("%02d", month)}</h5>
                    <div>
                        <c:if test="${not empty summaryViews && filterType == 'all'}">
                            <a href="${pageContext.request.contextPath}/salary/export-payroll-report?year=${year}&month=${month}&format=excel"
                               class="btn btn-success btn-sm me-2" title="Export Payroll Report to Excel">
                                <i class="fas fa-file-excel me-1" style="color: white;"></i>Export Excel
                            </a>
                            <a href="${pageContext.request.contextPath}/salary/export-payroll-report?year=${year}&month=${month}&format=pdf"
                               class="btn btn-danger btn-sm me-2" title="Export Payroll Report to PDF">
                                <i class="fas fa-file-pdf me-1" style="color: white;"></i>Export PDF
                            </a>
                        </c:if>
                    </div>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty summaryViews}">
                            <div class="alert alert-info">
                                <i class="fas fa-info-circle me-2"></i>
                                No salary data found for the selected period. Please calculate salaries first.
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover table-striped">
                                    <thead class="table-dark">
                                        <tr>
                                            <th>Employee</th>
                                            <th>Department</th>
                                            <th>Position</th>
                                            <th class="text-end">Base Salary</th>
                                            <th class="text-end">Allowances</th>
                                            <th class="text-end">Overtime</th>
                                            <th class="text-end">Bonus</th>
                                            <th class="text-end">Bonus Adj.</th>
                                            <th class="text-end">Gross Salary</th>
                                            <th class="text-end">Deductions</th>
                                            <th class="text-end">Net Salary</th>
                                            <th>Status</th>
                                            <th class="text-center">Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="view" items="${summaryViews}">
                                            <tr>
                                                <td>
                                                    <strong>${view.employeeCode}</strong><br>
                                                    <small>${view.employeeName}</small>
                                                </td>
                                                <td>${view.departmentName}</td>
                                                <td>${view.positionName}</td>
                                                <td class="text-end"><fmt:formatNumber value="${view.baseSalary}" type="currency" currencySymbol="$"/></td>
                                                <td class="text-end"><fmt:formatNumber value="${view.totalAllowances}" type="currency" currencySymbol="$"/></td>
                                                <td class="text-end"><fmt:formatNumber value="${view.overtimePay}" type="currency" currencySymbol="$"/></td>
                                                <td class="text-end"><fmt:formatNumber value="${view.totalBonus}" type="currency" currencySymbol="$"/></td>
                                                <td class="text-end">
                                                    <span class="bonus-adjustment ${view.bonusAdjustments < 0 ? 'negative' : ''}">
                                                        <fmt:formatNumber value="${view.bonusAdjustments}" type="currency" currencySymbol="$"/>
                                                        <c:if test="${view.pendingAdjustmentsCount > 0}">
                                                            <span class="badge bg-warning text-dark ms-1">PENDING</span>
                                                        </c:if>
                                                    </span>
                                                </td>
                                                <td class="text-end"><strong><fmt:formatNumber value="${view.grossSalary}" type="currency" currencySymbol="$"/></strong></td>
                                                <td class="text-end"><fmt:formatNumber value="${view.totalDeductions}" type="currency" currencySymbol="$"/></td>
                                                <td class="text-end"><strong><fmt:formatNumber value="${view.netSalary}" type="currency" currencySymbol="$"/></strong></td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${view.status == 'Paid'}">
                                                            <span class="badge bg-success status-badge">Paid</span>
                                                        </c:when>
                                                        <c:when test="${view.status == 'Approved'}">
                                                            <span class="badge bg-info status-badge">Approved</span>
                                                        </c:when>
                                                        <c:when test="${view.status == 'Calculated'}">
                                                            <span class="badge bg-warning status-badge">Calculated</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary status-badge">${view.status}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="text-center table-actions">
                                                    <div class="btn-group btn-group-sm" role="group">
                                                        <a href="${pageContext.request.contextPath}/salary/export-payslip?employeeId=${view.employeeId}&year=${year}&month=${month}&format=excel"
                                                           class="btn btn-outline-success" title="Export Excel">
                                                            <i class="fas fa-file-excel"></i>
                                                        </a>
                                                        <a href="${pageContext.request.contextPath}/salary/export-payslip?employeeId=${view.employeeId}&year=${year}&month=${month}&format=pdf"
                                                           class="btn btn-outline-danger" title="Export PDF">
                                                            <i class="fas fa-file-pdf"></i>
                                                        </a>
                                                        <a href="${pageContext.request.contextPath}/salary/adjust-bonus?employeeId=${view.employeeId}&year=${year}&month=${month}"
                                                           class="btn btn-outline-primary" title="Adjust Bonus">
                                                            <i class="fas fa-edit"></i>
                                                        </a>
                                                    </div>
                                                </td>
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

    <!-- Approve Adjustment Modal -->
    <div class="modal fade" id="approveModal" tabindex="-1" aria-labelledby="approveModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="approveModalLabel">Approve Bonus Adjustment</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form id="approveForm" method="POST" action="${pageContext.request.contextPath}/salary/adjust-bonus">
                    <div class="modal-body">
                        <p>Are you sure you want to approve the bonus adjustment for <strong id="approveEmployeeName"></strong>?</p>
                        <p>Amount: <strong id="approveAmount" class="text-success"></strong></p>
                        <div class="mb-3">
                            <label for="approveComment" class="form-label">Approval Comment (Optional)</label>
                            <textarea class="form-control" id="approveComment" name="approvalComment" rows="3"
                                      placeholder="Enter any comments about the approval..."></textarea>
                        </div>
                        <input type="hidden" id="approveAdjustmentId" name="adjustmentId" value="">
                        <input type="hidden" name="action" value="approve">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-success">
                            <i class="fas fa-check me-1"></i>Approve Adjustment
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Reject Adjustment Modal -->
    <div class="modal fade" id="rejectModal" tabindex="-1" aria-labelledby="rejectModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="rejectModalLabel">Reject Bonus Adjustment</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form id="rejectForm" method="POST" action="${pageContext.request.contextPath}/salary/adjust-bonus">
                    <div class="modal-body">
                        <p>Are you sure you want to reject the bonus adjustment for <strong id="rejectEmployeeName"></strong>?</p>
                        <p>Amount: <strong id="rejectAmount"></strong></p>
                        <div class="mb-3">
                            <label for="rejectComment" class="form-label">Rejection Reason <span class="text-danger">*</span></label>
                            <textarea class="form-control" id="rejectComment" name="approvalComment" rows="3"
                                      placeholder="Please provide a reason for rejecting this adjustment..." required></textarea>
                            <div class="invalid-feedback">
                                Please provide a reason for rejection.
                            </div>
                        </div>
                        <input type="hidden" id="rejectAdjustmentId" name="adjustmentId" value="">
                        <input type="hidden" name="action" value="reject">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-times me-1"></i>Reject Adjustment
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function toggleFilters() {
            const filterType = document.getElementById('filterTypeSelect').value;
            document.getElementById('employeeFilter').style.display = filterType === 'employee' ? 'flex' : 'none';
            document.getElementById('departmentFilter').style.display = filterType === 'department' ? 'flex' : 'none';
        }

        // Auto-hide alerts after 3 seconds
        document.addEventListener('DOMContentLoaded', function() {
            // Auto-hide success and error alerts after 3 seconds
            const alerts = document.querySelectorAll('.alert-dismissible');
            alerts.forEach(function(alert) {
                setTimeout(function() {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                }, 3000);
            });
        });

        // Approve/Reject button handlers
        document.addEventListener('DOMContentLoaded', function() {
            // Approve button handlers
            document.querySelectorAll('.approve-btn').forEach(function(btn) {
                btn.addEventListener('click', function() {
                    const adjustmentId = this.getAttribute('data-adjustment-id');
                    const employeeName = this.getAttribute('data-employee-name');
                    const amount = this.getAttribute('data-amount');

                    document.getElementById('approveAdjustmentId').value = adjustmentId;
                    document.getElementById('approveEmployeeName').textContent = employeeName;
                    document.getElementById('approveAmount').textContent = '$' + parseFloat(amount).toFixed(2);

                    const approveModal = new bootstrap.Modal(document.getElementById('approveModal'));
                    approveModal.show();
                });
            });

            // Reject button handlers
            document.querySelectorAll('.reject-btn').forEach(function(btn) {
                btn.addEventListener('click', function() {
                    const adjustmentId = this.getAttribute('data-adjustment-id');
                    const employeeName = this.getAttribute('data-employee-name');
                    const amount = this.getAttribute('data-amount');

                    document.getElementById('rejectAdjustmentId').value = adjustmentId;
                    document.getElementById('rejectEmployeeName').textContent = employeeName;
                    document.getElementById('rejectAmount').textContent = '$' + parseFloat(amount).toFixed(2);

                    const rejectModal = new bootstrap.Modal(document.getElementById('rejectModal'));
                    rejectModal.show();
                });
            });

            // Form validation for reject form
            document.getElementById('rejectForm').addEventListener('submit', function(e) {
                const comment = document.getElementById('rejectComment').value.trim();
                if (!comment) {
                    e.preventDefault();
                    document.getElementById('rejectComment').classList.add('is-invalid');
                } else {
                    document.getElementById('rejectComment').classList.remove('is-invalid');
                }
            });
        });
    </script>
</body>
</html>

