<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contracts List - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="sidebar-header">
            <h4>HR Management</h4>
            <p>Human Resources System</p>
        </div>
        
        <ul class="sidebar-menu">
            <li class="menu-section">Dashboard</li>
            <li>
                <a href="${pageContext.request.contextPath}/">
                    <i class="fas fa-home"></i>
                    <span>Dashboard</span>
                </a>
            </li>
            
            <li class="menu-section">Employee Management</li>
            <li>
                <a href="#">
                    <i class="fas fa-users"></i>
                    <span>Employees</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-user-plus"></i>
                    <span>Recruitment</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-briefcase"></i>
                    <span>Departments</span>
                </a>
            </li>
            
            <li class="menu-section">Contract & Attendance</li>
            <li>
                <a href="${pageContext.request.contextPath}/contracts/list" class="active">
                    <i class="fas fa-file-contract"></i>
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
            <li>
                <a href="#">
                    <i class="fas fa-award"></i>
                    <span>Bonuses</span>
                </a>
            </li>
            
            <li class="menu-section">Reports & Analytics</li>
            <li>
                <a href="#">
                    <i class="fas fa-chart-bar"></i>
                    <span>Reports</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-chart-line"></i>
                    <span>Analytics</span>
                </a>
            </li>
            
            <li class="menu-section">System</li>
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
            </li>
        </ul>
    </div>
    
    <!-- Main Content -->
    <div class="main-content">
        <!-- Top Header -->
        <div class="top-header">
            <h1>Contracts Management</h1>
            <div class="user-info">
                <span>HR Manager</span>
                <div class="avatar">HR</div>
            </div>
        </div>
        
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="breadcrumb-item"><a href="#">Contract & Attendance</a></li>
                <li class="breadcrumb-item active">Contracts List</li>
            </ol>
        </nav>
        
        <!-- Content Area -->
        <div class="content-area">
            <!-- Search Card -->
            <div class="card">
                <div class="card-header">
                    <i class="fas fa-search me-2"></i>Search Contracts
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/contracts/list" method="GET">
                        <div class="row g-3">
                            <div class="col-md-4">
                                <label class="form-label">Keyword</label>
                                <input type="text" name="keyword" class="form-control" 
                                       placeholder="Employee name, contract type..." 
                                       value="${keyword}">
                            </div>
                            <div class="col-md-3">
                                <label class="form-label">Status</label>
                                <select name="status" class="form-select">
                                    <option value="">All Status</option>
                                    <option value="Draft" ${status == 'Draft' ? 'selected' : ''}>Draft</option>
                                    <option value="Pending Approval" ${status == 'Pending Approval' ? 'selected' : ''}>Pending Approval</option>
                                    <option value="Active" ${status == 'Active' ? 'selected' : ''}>Active</option>
                                    <option value="Expired" ${status == 'Expired' ? 'selected' : ''}>Expired</option>
                                    <option value="Terminated" ${status == 'Terminated' ? 'selected' : ''}>Terminated</option>
                                </select>
                            </div>
                            <div class="col-md-5 d-flex align-items-end gap-2">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-search me-1"></i>Search
                                </button>
                                <a href="${pageContext.request.contextPath}/contracts/list" class="btn btn-secondary">
                                    <i class="fas fa-redo me-1"></i>Reset
                                </a>
                                <a href="#" class="btn btn-success ms-auto">
                                    <i class="fas fa-plus me-1"></i>New Contract
                                </a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            
            <!-- Table -->
            <div class="table-wrapper">
                <div class="table-header">
                    <h5>Contracts List</h5>
                    <div class="table-stats">
                        <i class="fas fa-file-alt me-1"></i>
                        Total: <strong>${totalRecords}</strong> contract(s) | Page <strong>${currentPage}</strong> of <strong>${totalPages}</strong>
                    </div>
                </div>
                
                <c:choose>
                    <c:when test="${empty contracts}">
                        <div class="empty-state">
                            <i class="fas fa-inbox"></i>
                            <h4>No Contracts Found</h4>
                            <p>There are no contracts in the system or no matches for your search criteria.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Contract #</th>
                                    <th>Employee</th>
                                    <th>Contract Type</th>
                                    <th>Start Date</th>
                                    <th>End Date</th>
                                    <th>Status</th>
                                    <th>Contact</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="contract" items="${contracts}">
                                    <tr>
                                        <td>
                                            <strong>#${contract.contractID}</strong>
                                            <c:if test="${contract.contractNumber != null}">
                                                <br/><small class="text-muted">${contract.contractNumber}</small>
                                            </c:if>
                                        </td>
                                        <td>
                                            ${contract.employeeFullName != null ? contract.employeeFullName : 'N/A'}
                                            <c:if test="${contract.employeeCode != null}">
                                                <br/><small class="text-muted">(${contract.employeeCode})</small>
                                            </c:if>
                                        </td>
                                        <td>${contract.contractType != null ? contract.contractType : 'N/A'}</td>
                                        <td><fmt:formatDate value="${contract.startDate}" pattern="dd/MM/yyyy"/></td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${contract.endDate != null}">
                                                    <fmt:formatDate value="${contract.endDate}" pattern="dd/MM/yyyy"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <span style="color: #999;">Not specified</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${contract.contractStatus == 'Active'}">
                                                    <span class="badge badge-active">Active</span>
                                                </c:when>
                                                <c:when test="${contract.contractStatus == 'Expired'}">
                                                    <span class="badge badge-expired">Expired</span>
                                                </c:when>
                                                <c:when test="${contract.contractStatus == 'Pending Approval'}">
                                                    <span class="badge badge-pending">Pending Approval</span>
                                                </c:when>
                                                <c:when test="${contract.contractStatus == 'Draft'}">
                                                    <span class="badge bg-secondary">Draft</span>
                                                </c:when>
                                                <c:when test="${contract.contractStatus == 'Terminated'}">
                                                    <span class="badge bg-danger">Terminated</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-pending">${contract.contractStatus}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:if test="${contract.employeePhone != null}">
                                                <small><i class="fas fa-phone"></i> ${contract.employeePhone}</small><br/>
                                            </c:if>
                                            <c:if test="${contract.employeeEmail != null}">
                                                <small><i class="fas fa-envelope"></i> ${contract.employeeEmail}</small>
                                            </c:if>
                                            <c:if test="${contract.employeePhone == null && contract.employeeEmail == null}">
                                                <small class="text-muted">N/A</small>
                                            </c:if>
                                        </td>
                                        <td class="actions-cell">
                                            <div class="actions-wrapper">
                                                <a href="#" class="btn-action btn-view" title="View">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                                <a href="#" class="btn-action btn-edit" title="Edit">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <a href="#" class="btn-action btn-delete" title="Delete" 
                                                   onclick="return confirm('Are you sure you want to delete contract #${contract.contractID}?')">
                                                    <i class="fas fa-trash"></i>
                                                </a>
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
                <div class="pagination-wrapper">
                    <nav>
                        <ul class="pagination">
                            <!-- Previous Button -->
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <c:url value="/contracts/list" var="prevUrl">
                                    <c:param name="page" value="${currentPage - 1}"/>
                                    <c:param name="keyword" value="${keyword}"/>
                                    <c:param name="status" value="${status}"/>
                                </c:url>
                                <a class="page-link" href="${currentPage > 1 ? prevUrl : '#'}" aria-label="Previous">
                                    <i class="fas fa-chevron-left"></i>
                                </a>
                            </li>
                            
                            <!-- Page Numbers -->
                            <c:forEach begin="1" end="${totalPages}" var="pageNum">
                                <c:if test="${pageNum <= 3 || pageNum > totalPages - 3 || (pageNum >= currentPage - 1 && pageNum <= currentPage + 1)}">
                                    <c:url value="/contracts/list" var="pageUrl">
                                        <c:param name="page" value="${pageNum}"/>
                                        <c:param name="keyword" value="${keyword}"/>
                                        <c:param name="status" value="${status}"/>
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
                                <c:url value="/contracts/list" var="nextUrl">
                                    <c:param name="page" value="${currentPage + 1}"/>
                                    <c:param name="keyword" value="${keyword}"/>
                                    <c:param name="status" value="${status}"/>
                                </c:url>
                                <a class="page-link" href="${currentPage < totalPages ? nextUrl : '#'}" aria-label="Next">
                                    <i class="fas fa-chevron-right"></i>
                                </a>
                            </li>
                        </ul>
                    </nav>
                    
                    <div class="pagination-info">
                        Showing ${(currentPage - 1) * pageSize + 1} to ${currentPage * pageSize > totalRecords ? totalRecords : currentPage * pageSize} of ${totalRecords} entries
                    </div>
                </div>
            </c:if>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
