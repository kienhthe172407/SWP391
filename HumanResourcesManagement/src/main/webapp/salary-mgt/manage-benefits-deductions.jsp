<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<%@taglib prefix="fn" uri="jakarta.tags.functions"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Benefits & Deductions - HR Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    <style>
        .table-actions {
            white-space: nowrap;
        }
        .badge-calculation {
            font-size: 0.85rem;
        }
        .section-header {
            background-color: #f8f9fa; /* align with Bootstrap bg-light */
            color: inherit;
            padding: 1rem;
            border: 1px solid #dee2e6;
            border-radius: 0.375rem;
            margin-bottom: 1rem;
        }
        .section-header.section-primary {
            background-color: #0d6efd;
            color: #fff;
            border-color: #0d6efd;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="sidebar-header">
            <h4>HR System</h4>
            <p>Salary Management</p>
        </div>
        
        <ul class="sidebar-menu">
            <c:choose>
                <c:when test="${sessionScope.user != null && sessionScope.user.role == 'HR Manager'}">
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
                        <a href="${pageContext.request.contextPath}/salary/manage-benefits-deductions" class="active">
                            <i class="fas fa-gift"></i>
                            <span>Benefits & Deductions</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/salary/import">
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
                        <a href="${pageContext.request.contextPath}/salary/manage-benefits-deductions" class="active">
                            <i class="fas fa-gift"></i>
                            <span>Benefits & Deductions</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/salary/import">
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
            <h1>Benefits & Deductions</h1>
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
                <li class="breadcrumb-item active">Benefits & Deductions</li>
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

            <!-- Benefits Section -->
            <div class="section-header section-primary d-flex justify-content-between align-items-center">
                <div>
                    <h4 class="mb-0"><i class="fas fa-gift me-2"></i>Benefit Types</h4>
                    <small>Configure benefit types that can be applied to employee salaries</small>
                </div>
                <a href="${pageContext.request.contextPath}/salary/add-benefit" class="btn btn-light">
                    <i class="fas fa-plus me-2"></i>Add Benefit
                </a>
            </div>

            <div class="card mb-4">
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty benefits}">
                            <div class="text-center py-5">
                                <i class="fas fa-gift fa-3x text-muted mb-3"></i>
                                <p class="text-muted">No benefit types configured yet.</p>
                                <a href="${pageContext.request.contextPath}/salary/add-benefit" class="btn btn-primary">
                                    <i class="fas fa-plus me-2"></i>Add First Benefit
                                </a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Benefit Name</th>
                                            <th>Description</th>
                                            <th>Calculation Type</th>
                                            <th>Default Value</th>
                                            <th>Taxable</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="benefit" items="${benefits}">
                                            <tr>
                                                <td><strong>${benefit.benefitName}</strong></td>
                                                <td>${benefit.description != null ? benefit.description : '-'}</td>
                                                <td>
                                                    <span class="badge badge-calculation 
                                                        ${benefit.calculationType == 'Fixed' ? 'bg-primary' : 
                                                          benefit.calculationType == 'Percentage' ? 'bg-success' : 'bg-info'}">
                                                        ${benefit.calculationType}
                                                    </span>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${benefit.calculationType == 'Fixed'}">
                                                            <fmt:formatNumber value="${benefit.defaultAmount}" type="currency" currencySymbol="$"/>
                                                        </c:when>
                                                        <c:when test="${benefit.calculationType == 'Percentage'}">
                                                            <fmt:formatNumber value="${benefit.defaultPercentage}" pattern="#0.00"/>%
                                                        </c:when>
                                                        <c:otherwise>
                                                            Formula-based
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${benefit.taxable}">
                                                            <span class="badge bg-warning">Taxable</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary">Non-taxable</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="table-actions">
                                                    <a href="${pageContext.request.contextPath}/salary/edit-benefit?id=${benefit.benefitTypeID}" 
                                                       class="btn btn-sm btn-outline-primary" title="Edit">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                   <button type="button" class="btn btn-sm btn-outline-danger" 
                                                           data-type="benefit" 
                                                           data-id="${benefit.benefitTypeID}" 
                                                           data-name="${fn:escapeXml(benefit.benefitName)}" 
                                                           onclick="handleDelete(this)" 
                                                           title="Delete">
                                                        <i class="fas fa-trash"></i>
                                                    </button>
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

            <!-- Deductions Section -->
            <div class="section-header section-primary d-flex justify-content-between align-items-center">
                <div>
                    <h4 class="mb-0"><i class="fas fa-minus-circle me-2"></i>Deduction Types</h4>
                    <small>Configure deduction types that can be applied to employee salaries</small>
                </div>
                <a href="${pageContext.request.contextPath}/salary/add-deduction" class="btn btn-light">
                    <i class="fas fa-plus me-2"></i>Add Deduction
                </a>
            </div>

            <div class="card">
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty deductions}">
                            <div class="text-center py-5">
                                <i class="fas fa-minus-circle fa-3x text-muted mb-3"></i>
                                <p class="text-muted">No deduction types configured yet.</p>
                                <a href="${pageContext.request.contextPath}/salary/add-deduction" class="btn btn-primary">
                                    <i class="fas fa-plus me-2"></i>Add First Deduction
                                </a>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Deduction Name</th>
                                            <th>Description</th>
                                            <th>Calculation Type</th>
                                            <th>Default Value</th>
                                            <th>Mandatory</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="deduction" items="${deductions}">
                                            <tr>
                                                <td><strong>${deduction.deductionName}</strong></td>
                                                <td>${deduction.description != null ? deduction.description : '-'}</td>
                                                <td>
                                                    <span class="badge badge-calculation 
                                                        ${deduction.calculationType == 'Fixed' ? 'bg-primary' : 
                                                          deduction.calculationType == 'Percentage' ? 'bg-success' : 'bg-info'}">
                                                        ${deduction.calculationType}
                                                    </span>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${deduction.calculationType == 'Fixed'}">
                                                            <fmt:formatNumber value="${deduction.defaultAmount}" type="currency" currencySymbol="$"/>
                                                        </c:when>
                                                        <c:when test="${deduction.calculationType == 'Percentage'}">
                                                            <fmt:formatNumber value="${deduction.defaultPercentage}" pattern="#0.00"/>%
                                                        </c:when>
                                                        <c:otherwise>
                                                            Formula-based
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${deduction.mandatory}">
                                                            <span class="badge bg-danger">Mandatory</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary">Optional</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>
                                                <td class="table-actions">
                                                    <a href="${pageContext.request.contextPath}/salary/edit-deduction?id=${deduction.deductionTypeID}" 
                                                       class="btn btn-sm btn-outline-primary" title="Edit">
                                                        <i class="fas fa-edit"></i>
                                                    </a>
                                                   <button type="button" class="btn btn-sm btn-outline-danger" 
                                                           data-type="deduction" 
                                                           data-id="${deduction.deductionTypeID}" 
                                                           data-name="${fn:escapeXml(deduction.deductionName)}" 
                                                           onclick="handleDelete(this)" 
                                                           title="Delete">
                                                        <i class="fas fa-trash"></i>
                                                    </button>
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

    <!-- Delete Confirmation Modal -->
    <div class="modal fade" id="deleteItemModal" tabindex="-1" aria-labelledby="deleteItemModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title" id="deleteItemModalLabel">
                        <i class="fas fa-exclamation-triangle me-2"></i>Delete <span id="deleteItemTypeTitle">Item</span>
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form id="deleteItemForm" method="POST" action="#">
                    <div class="modal-body">
                        <div class="text-center mb-4">
                            <div class="mb-3">
                                <i class="fas fa-trash-alt text-danger" style="font-size: 3rem;"></i>
                            </div>
                            <h5>Are you sure you want to delete this <span id="deleteItemTypeText">item</span>?</h5>
                            <p class="text-muted"><span id="deleteItemTypeLabel">Item</span> name: <strong id="deleteItemNameText"></strong></p>
                            <p class="text-danger"><strong>Warning:</strong> This action cannot be undone.</p>
                        </div>
                        <input type="hidden" id="deleteItemIdInput" name="id" value="">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <i class="fas fa-times me-1"></i>Cancel
                        </button>
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-trash-alt me-1"></i>Delete
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function handleDelete(buttonEl) {
            var type = buttonEl.getAttribute('data-type'); // 'benefit' or 'deduction'
            var id = buttonEl.getAttribute('data-id');
            var name = buttonEl.getAttribute('data-name');

            // Set modal texts
            var typeTitle = type === 'benefit' ? 'Benefit' : 'Deduction';
            document.getElementById('deleteItemTypeTitle').textContent = typeTitle;
            document.getElementById('deleteItemTypeText').textContent = typeTitle.toLowerCase();
            document.getElementById('deleteItemTypeLabel').textContent = typeTitle;
            document.getElementById('deleteItemNameText').textContent = name;

            // Set form action and hidden id
            var form = document.getElementById('deleteItemForm');
            var actionUrl = type === 'benefit'
                ? '${pageContext.request.contextPath}/salary/delete-benefit'
                : '${pageContext.request.contextPath}/salary/delete-deduction';
            form.setAttribute('action', actionUrl);
            document.getElementById('deleteItemIdInput').value = id;

            // Show modal
            var deleteModal = new bootstrap.Modal(document.getElementById('deleteItemModal'));
            deleteModal.show();
        }
    </script>
</body>
</html>

