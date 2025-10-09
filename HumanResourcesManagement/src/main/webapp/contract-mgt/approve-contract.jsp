<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contract Approval - HR Manager Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
    
    <style>
        .contract-detail {
            margin-bottom: 1rem;
            padding-bottom: 0.5rem;
            border-bottom: 1px solid #eee;
        }
        
        .contract-detail label {
            font-weight: 600;
            color: #555;
        }
        
        .contract-detail p {
            margin-bottom: 0;
        }
        
        .contract-section {
            margin-bottom: 2rem;
            padding-bottom: 1rem;
            border-bottom: 1px solid #eee;
        }
        
        .contract-section h5 {
            margin-bottom: 1.5rem;
            color: #0d6efd;
        }
        
        .badge-pending {
            background-color: #ffc107;
            color: #212529;
        }
        
        .badge-active {
            background-color: #198754;
            color: white;
        }
        
        .badge-expired {
            background-color: #6c757d;
            color: white;
        }
        
        .contract-meta {
            font-size: 0.85rem;
            color: #6c757d;
        }
        
        .btn-approve {
            background-color: #198754;
            color: white;
        }
        
        .btn-reject {
            background-color: #dc3545;
            color: white;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="sidebar-header">
            <h4>HR Manager Management</h4>
            <p>HR Manager Portal</p>
        </div>
        
        <ul class="sidebar-menu">
            <li class="menu-section">Dashboard</li>
            <li>
                <a href="${pageContext.request.contextPath}/">
                    <i class="fas fa-home"></i>
                    <span>Dashboard</span>
                </a>
            </li>
            
            <li class="menu-section">HR Manager Management</li>
            <li>
                <a href="#">
                    <i class="fas fa-tasks"></i>
                    <span>Task Assignment</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/contracts/approve" class="active">
                    <i class="fas fa-check-square"></i>
                    <span>Approval Requests</span>
                    <span class="badge bg-danger rounded-pill ms-2">5</span>
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
                <a href="${pageContext.request.contextPath}/contracts/list">
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
            
            <li class="menu-section">Reports & Analytics</li>
            <li>
                <a href="#">
                    <i class="fas fa-chart-bar"></i>
                    <span>HR Manager Reports</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-chart-line"></i>
                    <span>HR Manager Analytics</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-chart-pie"></i>
                    <span>Statistics</span>
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
            <h1>
                <c:choose>
                    <c:when test="${not empty contract}">Review Contract</c:when>
                    <c:otherwise>Contracts Pending Approval</c:otherwise>
                </c:choose>
            </h1>
            <div class="user-info">
                <span>HR Manager</span>
                <div class="avatar">HRM</div>
            </div>
        </div>
        
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/contracts/list">Contracts</a></li>
                <c:choose>
                    <c:when test="${not empty contract}">
                        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/contracts/approve">Pending Approval</a></li>
                        <li class="breadcrumb-item active">Review Contract #${contract.contractID}</li>
                    </c:when>
                    <c:otherwise>
                        <li class="breadcrumb-item active">Pending Approval</li>
                    </c:otherwise>
                </c:choose>
            </ol>
        </nav>
        
        <!-- Content Area -->
        <div class="content-area">
            <!-- Error/Success Messages -->
            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${sessionScope.errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="errorMessage" scope="session" />
            </c:if>
            
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>${sessionScope.successMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="successMessage" scope="session" />
            </c:if>
            
            <c:choose>
                <c:when test="${not empty contract}">
                    <!-- Single Contract Review Mode -->
                    <div class="card mb-4">
                        <div class="card-header d-flex justify-content-between align-items-center">
                            <div>
                                <i class="fas fa-file-contract me-2"></i>Contract #${contract.contractID}
                                <c:choose>
                                    <c:when test="${contract.contractStatus == 'Pending Approval'}">
                                        <span class="badge badge-pending ms-2">Pending Approval</span>
                                    </c:when>
                                    <c:when test="${contract.contractStatus == 'Draft'}">
                                        <span class="badge bg-secondary ms-2">Draft</span>
                                    </c:when>
                                </c:choose>
                            </div>
                            <div class="contract-meta">
                                <span><i class="fas fa-calendar me-1"></i> Created: <fmt:formatDate value="${contract.createdAt}" pattern="dd/MM/yyyy HH:mm"/></span>
                            </div>
                        </div>
                        <div class="card-body">
                            <!-- Employee Information -->
                            <div class="contract-section">
                                <h5><i class="fas fa-user me-2"></i>Employee Information</h5>
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="contract-detail">
                                            <label>Employee Name</label>
                                            <p>${contract.employeeFullName}</p>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="contract-detail">
                                            <label>Employee Code</label>
                                            <p>${contract.employeeCode}</p>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="contract-detail">
                                            <label>Phone Number</label>
                                            <p>${contract.employeePhone != null ? contract.employeePhone : 'N/A'}</p>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="contract-detail">
                                            <label>Email</label>
                                            <p>${contract.employeeEmail != null ? contract.employeeEmail : 'N/A'}</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Contract Details -->
                            <div class="contract-section">
                                <h5><i class="fas fa-file-alt me-2"></i>Contract Details</h5>
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="contract-detail">
                                            <label>Contract Number</label>
                                            <p>${contract.contractNumber}</p>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="contract-detail">
                                            <label>Contract Type</label>
                                            <p>${contract.contractType}</p>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="contract-detail">
                                            <label>Start Date</label>
                                            <p><fmt:formatDate value="${contract.startDate}" pattern="dd/MM/yyyy"/></p>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="contract-detail">
                                            <label>End Date</label>
                                            <p>
                                                <c:choose>
                                                    <c:when test="${contract.endDate != null}">
                                                        <fmt:formatDate value="${contract.endDate}" pattern="dd/MM/yyyy"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted">Not specified (Indefinite)</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="contract-detail">
                                            <label>Salary Amount</label>
                                            <p>
                                                <c:choose>
                                                    <c:when test="${contract.salaryAmount != null}">
                                                        <fmt:formatNumber value="${contract.salaryAmount}" type="currency" currencySymbol="$"/>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted">Not specified</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </p>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="contract-detail">
                                            <label>Created By</label>
                                            <p>${contract.createdBy != null ? contract.createdBy : 'N/A'}</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- Job Description -->
                            <div class="contract-section">
                                <h5><i class="fas fa-tasks me-2"></i>Job Description</h5>
                                <div class="contract-detail">
                                    <p>${contract.jobDescription != null ? contract.jobDescription : 'No job description provided.'}</p>
                                </div>
                            </div>
                            
                            <!-- Terms and Conditions -->
                            <div class="contract-section">
                                <h5><i class="fas fa-gavel me-2"></i>Terms and Conditions</h5>
                                <div class="contract-detail">
                                    <p>${contract.termsAndConditions != null ? contract.termsAndConditions : 'No terms and conditions provided.'}</p>
                                </div>
                            </div>
                            
                            <!-- Notes from HR Manager -->
                            <c:if test="${not empty contract.approvalComment}">
                                <div class="contract-section">
                                    <h5><i class="fas fa-comment me-2"></i>Notes from HR Manager</h5>
                                    <div class="contract-detail">
                                        <p>${contract.approvalComment}</p>
                                    </div>
                                </div>
                            </c:if>
                            
                            <!-- Approval Form -->
                            <form action="${pageContext.request.contextPath}/contracts/approve" method="POST" id="approvalForm">
                                <input type="hidden" name="contractId" value="${contract.contractID}">
                                
                                <div class="mb-3">
                                    <label for="comment" class="form-label">Comments</label>
                                    <textarea class="form-control" id="comment" name="comment" rows="3" 
                                              placeholder="Enter your comments about this contract..."></textarea>
                                </div>
                                
                                <div class="d-flex justify-content-end gap-2 mt-4">
                                    <a href="${pageContext.request.contextPath}/contracts/approve" class="btn btn-secondary">
                                        <i class="fas fa-arrow-left me-1"></i>Back to List
                                    </a>
                                    <button type="submit" name="action" value="reject" class="btn btn-danger" 
                                            onclick="return confirm('Are you sure you want to reject this contract?')">
                                        <i class="fas fa-times-circle me-1"></i>Reject Contract
                                    </button>
                                    <button type="submit" name="action" value="approve" class="btn btn-success">
                                        <i class="fas fa-check-circle me-1"></i>Approve Contract
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <!-- List Mode - Contracts Pending Approval -->
                    <!-- Search Card -->
                    <div class="card">
                        <div class="card-header">
                            <i class="fas fa-search me-2"></i>Search Contracts
                        </div>
                        <div class="card-body">
                            <form action="${pageContext.request.contextPath}/contracts/approve" method="GET">
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
                                            <option value="approval" ${status == 'approval' ? 'selected' : ''}>All Pending</option>
                                            <option value="Draft" ${status == 'Draft' ? 'selected' : ''}>Draft</option>
                                            <option value="Pending Approval" ${status == 'Pending Approval' ? 'selected' : ''}>Pending Approval</option>
                                        </select>
                                    </div>
                                    <div class="col-md-5 d-flex align-items-end">
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-search me-1"></i>Search
                                        </button>
                                        <a href="${pageContext.request.contextPath}/contracts/approve" class="btn btn-secondary ms-2">
                                            <i class="fas fa-redo me-1"></i>Reset
                                        </a>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                    
                    <!-- Table -->
                    <div class="table-wrapper">
                        <div class="table-header">
                            <h5>Contracts Pending Approval</h5>
                            <div class="table-stats">
                                <i class="fas fa-file-alt me-1"></i>
                                Total: <strong>${totalRecords}</strong> contract(s) | Page <strong>${currentPage}</strong> of <strong>${totalPages}</strong>
                            </div>
                        </div>
                        
                        <c:choose>
                            <c:when test="${empty contracts}">
                                <div class="empty-state">
                                    <i class="fas fa-inbox"></i>
                                    <h4>No Contracts Pending Approval</h4>
                                    <p>There are no contracts waiting for your approval at this time.</p>
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
                                            <th>Created</th>
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
                                                        <c:when test="${contract.contractStatus == 'Pending Approval'}">
                                                            <span class="badge badge-pending">Pending Approval</span>
                                                        </c:when>
                                                        <c:when test="${contract.contractStatus == 'Draft'}">
                                                            <span class="badge bg-secondary">Draft</span>
                                                        </c:when>
                                                    </c:choose>
                                                </td>
                                                <td>
                                                    <small><fmt:formatDate value="${contract.createdAt}" pattern="dd/MM/yyyy"/></small>
                                                </td>
                                                <td class="actions-cell">
                                                    <div class="d-flex gap-2">
                                                        <a href="${pageContext.request.contextPath}/contracts/approve?id=${contract.contractID}" class="btn btn-sm btn-primary">
                                                            <i class="fas fa-eye me-1"></i>Review
                                                        </a>
                                                        <div class="dropdown">
                                                            <button class="btn btn-sm btn-outline-secondary dropdown-toggle" type="button" id="dropdownMenuButton${contract.contractID}" data-bs-toggle="dropdown" aria-expanded="false">
                                                                <i class="fas fa-ellipsis-v"></i>
                                                            </button>
                                                            <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton${contract.contractID}">
                                                                <li>
                                                                    <form action="${pageContext.request.contextPath}/contracts/approve" method="POST" class="d-inline">
                                                                        <input type="hidden" name="contractId" value="${contract.contractID}">
                                                                        <input type="hidden" name="comment" value="Approved without comments">
                                                                        <button type="submit" name="action" value="approve" class="dropdown-item text-success" 
                                                                                onclick="return confirm('Are you sure you want to approve this contract?')">
                                                                            <i class="fas fa-check-circle me-1"></i>Approve
                                                                        </button>
                                                                    </form>
                                                                </li>
                                                                <li>
                                                                    <form action="${pageContext.request.contextPath}/contracts/approve" method="POST" class="d-inline">
                                                                        <input type="hidden" name="contractId" value="${contract.contractID}">
                                                                        <input type="hidden" name="comment" value="Rejected without comments">
                                                                        <button type="submit" name="action" value="reject" class="dropdown-item text-danger" 
                                                                                onclick="return confirm('Are you sure you want to reject this contract?')">
                                                                            <i class="fas fa-times-circle me-1"></i>Reject
                                                                        </button>
                                                                    </form>
                                                                </li>
                                                            </ul>
                                                        </div>
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
                                        <c:url value="/contracts/approve" var="prevUrl">
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
                                            <c:url value="/contracts/approve" var="pageUrl">
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
                                        <c:url value="/contracts/approve" var="nextUrl">
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
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>