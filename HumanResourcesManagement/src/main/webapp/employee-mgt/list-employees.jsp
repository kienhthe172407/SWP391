<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<%-- Role comes from authenticated session; do not override here --%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employee Records - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
    
    <!-- Custom Styles for this page -->
    <style>
        .contact-item {
            display: flex;
            align-items: flex-start;
            margin-bottom: 4px;
        }
        .contact-item:last-child {
            margin-bottom: 0;
        }
        .contact-item i {
            flex-shrink: 0;
            margin-right: 5px;
            margin-top: 2px;
        }
        .contact-text {
            word-break: break-word;
            white-space: normal;
            display: inline-block;
        }
        .terminated-employee {
            background-color: rgba(220, 53, 69, 0.05);
        }
        .terminated-employee td {
            color: #6c757d;
        }
        .terminated-employee strong {
            text-decoration: line-through;
            text-decoration-color: rgba(220, 53, 69, 0.5);
            text-decoration-thickness: 1px;
        }
        .card-header {
            background-color: #0d6efd;
            color: white;
        }
        .search-form .form-label {
            font-weight: 500;
            margin-bottom: 0.3rem;
        }
        .search-form .form-select,
        .search-form .form-control {
            border-radius: 0.375rem;
            border: 1px solid #dee2e6;
        }
        .search-form .input-group-text {
            border-right: 0;
        }
        .search-form .form-control:focus {
            border-color: #86b7fe;
            box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.25);
        }
        .search-form .btn-primary {
            background-color: #0d6efd;
            border-color: #0d6efd;
        }
        .search-form .btn-outline-secondary {
            color: #6c757d;
            border-color: #6c757d;
        }
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
            <!-- <li class="menu-section">Dashboard</li>
            <li>
                <a href="${pageContext.request.contextPath}/">
                    <i class="fas fa-home"></i>
                    <span>Dashboard</span>
                </a>
            </li> -->

            <c:choose>
                <c:when test="${isHRManager}">
                    <!-- HR Manager Menu (mirrors hr-manager-dashboard.jsp) -->
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
                        <a href="${pageContext.request.contextPath}/employees/list" class="active">
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
                        <a href="#">
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

                <c:when test="${isHR}">
                    <!-- HR Staff Menu (mirrors hr-dashboard.jsp) -->
                    <li class="menu-section">Dashboard</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/dashboard/hr-dashboard.jsp">
                            <i class="fas fa-home"></i>
                            <span>Overview</span>
                        </a>
                    </li>

                    <li class="menu-section">Employee Management</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/employees/list" class="active">
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
                        <a href="#">
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
                </c:when>

                <c:otherwise>
                    <!-- Default Employee Menu -->
                    <li class="menu-section">My Profile</li>
                    <li>
                        <a href="#">
                            <i class="fas fa-user"></i>
                            <span>View Profile</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-edit"></i>
                            <span>Edit Profile</span>
                        </a>
                    </li>

                    <li class="menu-section">My Records</li>
                    <li>
                        <a href="#">
                            <i class="fas fa-clock"></i>
                            <span>My Attendance</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-calendar-alt"></i>
                            <span>My Leave Requests</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-money-bill-wave"></i>
                            <span>My Payroll</span>
                        </a>
                    </li>
                </c:otherwise>
            </c:choose>

            <!-- <li class="menu-section">System</li>
            <li>
                <a href="#">
                    <i class="fas fa-cog"></i>
                    <span>Settings</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-sign-out-alt"></i>
                    <span>Logout</span>
                </a>
            </li> -->
        </ul>
    </div>
    
    <!-- Main Content -->
    <div class="main-content">
        <!-- Top Header -->
        <div class="top-header">
            <h1>Employee Records Management</h1>
            <div class="user-info">
                <c:if test="${sessionScope.userRole == 'HR Manager' || sessionScope.userRole == 'HR'}">
                    <a href="${pageContext.request.contextPath}/employees/addInformation" class="btn btn-primary me-3">
                        <i class="fas fa-user-plus"></i> Add Employee Information
                    </a>
                </c:if>
                <c:choose>
                    <c:when test="${sessionScope.userRole == 'HR Manager'}">
                        <span><c:out value="${sessionScope.user.roleDisplayName}" default="HR Manager"/></span>
                    </c:when>
                    <c:when test="${sessionScope.userRole == 'HR'}">
                        <span><c:out value="${sessionScope.user.roleDisplayName}" default="HR"/></span>
                    </c:when>
                    <c:otherwise>
                        <span>HR Management</span>
                    </c:otherwise>
                </c:choose>
                <div class="avatar">HR</div>
            </div>
        </div>
        
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="#">Employee Management</a></li>
                <li class="breadcrumb-item active">All Employees</li>
            </ol>
        </nav>
        
        <!-- Content Area -->
        <div class="content-area">
            <!-- Success/Error Messages -->
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>${sessionScope.successMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="successMessage" scope="session" />
            </c:if>
            
            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${sessionScope.errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="errorMessage" scope="session" />
            </c:if>
            
            <!-- Search Card -->
            <div class="card shadow-sm border-0 mb-4">
                <div class="card-body py-4 px-4">
                    <form class="search-form" action="${pageContext.request.contextPath}/employees/list" method="GET">
                        <div class="row align-items-end g-3">
                            <div class="col-md-3">
                                <label class="form-label fw-medium">Search</label>
                                <div class="input-group">
                                    <span class="input-group-text bg-white border-end-0"><i class="fas fa-search text-secondary"></i></span>
                                    <input type="text" name="keyword" class="form-control border-start-0" 
                                           placeholder="Name, code, email, phone..." 
                                           value="${keyword}">
                                </div>
                                <!-- <div class="form-text small text-muted">Search across multiple fields</div> -->
                            </div>
                            <div class="col-md-2">
                                <label class="form-label fw-medium">Department</label>
                                <select name="department" class="form-select">
                                    <option value="">All Departments</option>
                                    <c:forEach var="deptOption" items="${departments}">
                                        <option value="${deptOption.departmentId}" ${department == deptOption.departmentId ? 'selected' : ''}>${deptOption.departmentName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <label class="form-label fw-medium">Position</label>
                                <select name="position" class="form-select">
                                    <option value="">All Positions</option>
                                    <c:forEach var="posOption" items="${positions}">
                                        <option value="${posOption.positionId}" ${position == posOption.positionId ? 'selected' : ''}>${posOption.positionName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <label class="form-label fw-medium">Employment Status</label>
                                <select name="status" class="form-select" id="statusFilter">
                                    <option value="">All Statuses</option>
                                    <c:forEach var="statusOption" items="${employmentStatuses}">
                                        <option value="${statusOption}" ${status == statusOption ? 'selected' : ''}>${statusOption}</option>
                                    </c:forEach>
                                    <option value="!Terminated" ${status == '!Terminated' ? 'selected' : ''}>Hide Terminated</option>
                                </select>
                            </div>
                            <div class="col-md-3 d-flex gap-2">
                                <button type="submit" class="btn btn-primary px-3 py-2">
                                    <i class="fas fa-search me-1"></i>Search
                                </button>
                                <a href="${pageContext.request.contextPath}/employees/list" class="btn btn-outline-secondary px-2 py-2">
                                    <i class="fas fa-sync-alt me-1"></i>Reset
                                </a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Table -->
            <div class="table-wrapper">
                <div class="table-header">
                    <h5>Employee Records List</h5>
                    <div class="table-stats">
                        <i class="fas fa-users me-1"></i>
                        Total: <strong>${totalRecords}</strong> employee(s) | Page <strong>${currentPage}</strong> of <strong>${totalPages}</strong>
                    </div>
                </div>

                <c:choose>
                    <c:when test="${empty employees}">
                        <div class="empty-state">
                            <i class="fas fa-inbox"></i>
                            <h4>No Employees Found</h4>
                            <p>There are no employees in the system or no matches for your search criteria.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="table">
                            <thead>
                                <tr>
                                    <th style="width: 10%;">Employee ID</th>
                                    <th style="width: 15%;">Employee Info</th>
                                    <th style="width: 10%;">Department</th>
                                    <th style="width: 10%;">Position</th>
                                    <th style="width: 10%;">Manager</th>
                                    <th style="width: 8%;">Hire Date</th>
                                    <th style="width: 7%;">Status</th>
                                    <th style="width: 15%;">Contact</th>
                                    <th style="width: 15%;">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="employee" items="${employees}">
                                    <tr class="${employee.employmentStatus == 'Terminated' ? 'terminated-employee' : ''}">
                                        <td>
                                            <strong>#${employee.employeeID}</strong>
                                            <c:if test="${employee.employeeCode != null}">
                                                <br/><small class="text-muted">${employee.employeeCode}</small>
                                            </c:if>
                                        </td>
                                        <td>
                                            <strong>${employee.firstName} ${employee.lastName}</strong>
                                            <c:if test="${employee.dateOfBirth != null}">
                                                <br/><small class="text-muted">DOB: <fmt:formatDate value="${employee.dateOfBirth}" pattern="dd/MM/yyyy"/></small>
                                            </c:if>
                                            <c:if test="${employee.gender != null}">
                                                <br/><small class="text-muted">${employee.gender}</small>
                                            </c:if>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${employee.departmentName != null && employee.departmentName != 'N/A'}">
                                                    ${employee.departmentName}
                                                </c:when>
                                                <c:when test="${employee.departmentID != null}">
                                                    <span class="text-muted">Dept ID: ${employee.departmentID}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">N/A</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${employee.positionName != null && employee.positionName != 'N/A'}">
                                                    ${employee.positionName}
                                                </c:when>
                                                <c:when test="${employee.positionID != null}">
                                                    <span class="text-muted">Position ID: ${employee.positionID}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">N/A</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${employee.managerName != null && employee.managerName != 'N/A'}">
                                                    ${employee.managerName}
                                                </c:when>
                                                <c:when test="${employee.managerID != null}">
                                                    <span class="text-muted">Manager ID: ${employee.managerID}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">N/A</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${employee.hireDate != null}">
                                                    <fmt:formatDate value="${employee.hireDate}" pattern="dd/MM/yyyy"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">N/A</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${employee.employmentStatus == 'Active'}">
                                                    <span class="badge badge-active">Active</span>
                                                </c:when>
                                                <c:when test="${employee.employmentStatus == 'On Leave'}">
                                                    <span class="badge bg-warning">On Leave</span>
                                                </c:when>
                                                <c:when test="${employee.employmentStatus == 'Terminated'}">
                                                    <span class="badge bg-danger">Terminated</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-secondary">${employee.employmentStatus}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:if test="${employee.phoneNumber != null}">
                                                <div class="contact-item">
                                                    <i class="fas fa-phone"></i>
                                                    <small class="contact-text">${employee.phoneNumber}</small>
                                                </div>
                                            </c:if>
                                            <c:if test="${employee.personalEmail != null}">
                                                <div class="contact-item">
                                                    <i class="fas fa-envelope"></i>
                                                    <small class="contact-text">${employee.personalEmail}</small>
                                                </div>
                                            </c:if>
                                            <c:if test="${employee.phoneNumber == null && employee.personalEmail == null}">
                                                <small class="text-muted">N/A</small>
                                            </c:if>
                                        </td>
                                        <td class="actions-cell">
                                            <div class="actions-wrapper">
                                                <a href="${pageContext.request.contextPath}/employees/detail?employeeId=${employee.employeeID}"
                                                   class="btn-action btn-view" title="View Details">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/employees/edit?employeeId=${employee.employeeID}"
                                                   class="btn-action btn-edit" title="Edit">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <c:if test="${sessionScope.userRole == 'HR Manager' || sessionScope.userRole == 'HR'}">
                                                    <a href="#" class="btn-action btn-delete" title="Delete"
                                                       data-employee-id="${employee.employeeID}"
                                                       data-employee-name="${employee.firstName} ${employee.lastName}">
                                                        <i class="fas fa-trash"></i>
                                                    </a>
                                                </c:if>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <nav class="mt-3" aria-label="Employee list pagination">
                    <ul class="pagination justify-content-center">
                        <!-- Previous button -->
                        <c:choose>
                            <c:when test="${currentPage > 1}">
                                <li class="page-item">
                                    <a class="page-link" href="?page=${currentPage - 1}${searchQueryString}">
                                        <i class="fas fa-chevron-left"></i> Previous
                                    </a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="page-item disabled">
                                    <span class="page-link"><i class="fas fa-chevron-left"></i> Previous</span>
                                </li>
                            </c:otherwise>
                        </c:choose>

                        <!-- Page numbers -->
                        <c:forEach begin="1" end="${totalPages}" var="pageNum">
                            <c:choose>
                                <c:when test="${pageNum == currentPage}">
                                    <li class="page-item active">
                                        <span class="page-link">${pageNum}</span>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item">
                                        <a class="page-link" href="?page=${pageNum}${searchQueryString}">${pageNum}</a>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <!-- Next button -->
                        <c:choose>
                            <c:when test="${currentPage < totalPages}">
                                <li class="page-item">
                                    <a class="page-link" href="?page=${currentPage + 1}${searchQueryString}">
                                        Next <i class="fas fa-chevron-right"></i>
                                    </a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="page-item disabled">
                                    <span class="page-link">Next <i class="fas fa-chevron-right"></i></span>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>

    <!-- Delete Employee Modal -->
    <div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title" id="deleteModalLabel">
                        <i class="fas fa-exclamation-triangle me-2"></i>Delete Employee
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form id="deleteEmployeeForm" method="POST" action="${pageContext.request.contextPath}/employees/delete">
                    <div class="modal-body">
                        <div class="text-center mb-4">
                            <div class="mb-3">
                                <i class="fas fa-trash-alt text-danger" style="font-size: 3rem;"></i>
                            </div>
                            <h5>Are you sure you want to delete this employee?</h5>
                            <p class="text-muted">Employee <strong id="employeeName"></strong></p>
                            <p class="text-danger"><strong>Warning:</strong> This action cannot be undone.</p>
                        </div>
                        <input type="hidden" id="deleteEmployeeId" name="employeeId" value="">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <i class="fas fa-times me-1"></i>Cancel
                        </button>
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-trash-alt me-1"></i>Delete Employee
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Custom JavaScript -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Delete button handlers
            document.querySelectorAll('.btn-delete').forEach(function(btn) {
                btn.addEventListener('click', function(e) {
                    e.preventDefault();
                    const employeeId = this.getAttribute('data-employee-id');
                    const employeeName = this.getAttribute('data-employee-name');

                    document.getElementById('deleteEmployeeId').value = employeeId;
                    document.getElementById('employeeName').textContent = employeeName;

                    const deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
                    deleteModal.show();
                });
            });

            // Auto-hide alerts after 5 seconds
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                setTimeout(() => {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                }, 5000);
            });
        });
    </script>
</body>
</html>
