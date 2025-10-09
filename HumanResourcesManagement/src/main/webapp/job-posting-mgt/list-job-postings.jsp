<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<%-- Set admin role by default for all users --%>
<% 
    session.setAttribute("userRole", "HR Manager");
    session.setAttribute("userId", 1);
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Job Postings - HR Management System</title>
    
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

            <c:choose>
                <c:when test="${sessionScope.userRole == 'HR Manager'}">
                    <!-- HR Manager Menu -->
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
                        <a href="#">
                            <i class="fas fa-users"></i>
                            <span>All Employees</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/job-postings/list" class="active">
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
                    <!-- HR Staff Menu -->
                    <li class="menu-section">Employee Management</li>
                    <li>
                        <a href="#">
                            <i class="fas fa-users"></i>
                            <span>Employees</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/job-postings/list" class="active">
                            <i class="fas fa-user-plus"></i>
                            <span>Recruitment</span>
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
            <h1>Job Postings Management</h1>
            <div class="user-info">
                <span>HR Management</span>
                <div class="avatar">HR</div>
            </div>
        </div>
        
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="breadcrumb-item"><a href="#">Recruitment</a></li>
                <li class="breadcrumb-item active">Job Postings</li>
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
            <div class="card">
                <div class="card-header">
                    <i class="fas fa-search me-2"></i>Search & Filter Job Postings
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/job-postings/list" method="GET">
                        <div class="row g-3">
                            <div class="col-md-3">
                                <label class="form-label">Keyword</label>
                                <input type="text" name="keyword" class="form-control" 
                                       placeholder="Job title, department, position..." 
                                       value="${keyword}">
                            </div>
                            <div class="col-md-2">
                                <label class="form-label">Status</label>
                                <select name="status" class="form-select">
                                    <option value="">All Status</option>
                                    <c:forEach var="statusOption" items="${jobStatuses}">
                                        <option value="${statusOption}" ${status == statusOption ? 'selected' : ''}>${statusOption}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <label class="form-label">Department</label>
                                <select name="department" class="form-select">
                                    <option value="">All Departments</option>
                                    <c:forEach var="deptOption" items="${departments}">
                                        <option value="${deptOption.departmentId}" ${department == deptOption.departmentId ? 'selected' : ''}>${deptOption.departmentName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-2">
                                <label class="form-label">Position</label>
                                <select name="position" class="form-select">
                                    <option value="">All Positions</option>
                                    <c:forEach var="posOption" items="${positions}">
                                        <option value="${posOption.positionId}" ${position == posOption.positionId ? 'selected' : ''}>${posOption.positionName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-3 d-flex align-items-end gap-2">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-search me-1"></i>Search
                                </button>
                                <a href="${pageContext.request.contextPath}/job-postings/list" class="btn btn-secondary">
                                    <i class="fas fa-redo me-1"></i>Reset
                                </a>
                                <a href="${pageContext.request.contextPath}/job-postings/create" class="btn btn-success ms-auto">
                                    <i class="fas fa-plus me-1"></i>New Job Posting
                                </a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            
            <!-- Table -->
            <div class="table-wrapper">
                <div class="table-header">
                    <h5>Job Postings List</h5>
                    <div class="table-stats">
                        <i class="fas fa-briefcase me-1"></i>
                        Total: <strong>${totalRecords}</strong> job posting(s) | Page <strong>${currentPage}</strong> of <strong>${totalPages}</strong>
                    </div>
                </div>
                
                <c:choose>
                    <c:when test="${empty jobPostings}">
                        <div class="empty-state">
                            <i class="fas fa-inbox"></i>
                            <h4>No Job Postings Found</h4>
                            <p>There are no job postings in the system or no matches for your search criteria.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Job Title</th>
                                    <th>Department</th>
                                    <th>Positions</th>
                                    <th>Salary Range</th>
                                    <th>Deadline</th>
                                    <th>Status</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="jobPosting" items="${jobPostings}">
                                    <tr>
                                        <td>
                                            <strong>#${jobPosting.jobId}</strong>
                                        </td>
                                        <td>
                                            ${jobPosting.jobTitle}
                                            <c:choose>
                                                <c:when test="${jobPosting.positionName != null && jobPosting.positionName != 'N/A'}">
                                                    <br/><small class="text-muted">${jobPosting.positionName}</small>
                                                </c:when>
                                                <c:when test="${jobPosting.positionId != null}">
                                                    <br/><small class="text-muted">Position ID: ${jobPosting.positionId}</small>
                                                </c:when>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${jobPosting.departmentName != null && jobPosting.departmentName != 'N/A'}">
                                                    ${jobPosting.departmentName}
                                                </c:when>
                                                <c:when test="${jobPosting.departmentId != null}">
                                                    <span class="text-muted">Department ID: ${jobPosting.departmentId}</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">N/A</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <span class="badge bg-info">${jobPosting.numberOfPositions}</span>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${jobPosting.salaryRangeFrom != null && jobPosting.salaryRangeTo != null}">
                                                    <fmt:formatNumber value="${jobPosting.salaryRangeFrom}" type="currency" currencySymbol="$" /> - 
                                                    <fmt:formatNumber value="${jobPosting.salaryRangeTo}" type="currency" currencySymbol="$" />
                                                </c:when>
                                                <c:when test="${jobPosting.salaryRangeFrom != null}">
                                                    From <fmt:formatNumber value="${jobPosting.salaryRangeFrom}" type="currency" currencySymbol="$" />
                                                </c:when>
                                                <c:when test="${jobPosting.salaryRangeTo != null}">
                                                    Up to <fmt:formatNumber value="${jobPosting.salaryRangeTo}" type="currency" currencySymbol="$" />
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Not specified</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${jobPosting.applicationDeadline != null}">
                                                    <fmt:formatDate value="${jobPosting.applicationDeadline}" pattern="dd/MM/yyyy"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">Not specified</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${jobPosting.jobStatus == 'Open'}">
                                                    <span class="badge badge-active">Open</span>
                                                </c:when>
                                                <c:when test="${jobPosting.jobStatus == 'Closed'}">
                                                    <span class="badge badge-expired">Closed</span>
                                                </c:when>
                                                <c:when test="${jobPosting.jobStatus == 'Filled'}">
                                                    <span class="badge bg-success">Filled</span>
                                                </c:when>
                                                <c:when test="${jobPosting.jobStatus == 'Cancelled'}">
                                                    <span class="badge bg-danger">Cancelled</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-pending">${jobPosting.jobStatus}</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="actions-cell">
                                            <div class="actions-wrapper">
                                                <a href="${pageContext.request.contextPath}/job-postings/detail?id=${jobPosting.jobId}"
                                                   class="btn-action btn-view" title="View Details">
                                                    <i class="fas fa-eye"></i>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/job-postings/edit?id=${jobPosting.jobId}" 
                                                   class="btn-action btn-edit" title="Edit">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                                <a href="#" class="btn-action btn-delete" title="Delete"
                                                   data-job-id="${jobPosting.jobId}"
                                                   data-job-title="${jobPosting.jobTitle}">
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
                                <c:url value="/job-postings/list" var="prevUrl">
                                    <c:param name="page" value="${currentPage - 1}"/>
                                    <c:param name="keyword" value="${keyword}"/>
                                    <c:param name="status" value="${status}"/>
                                    <c:param name="department" value="${department}"/>
                                    <c:param name="position" value="${position}"/>
                                </c:url>
                                <a class="page-link" href="${currentPage > 1 ? prevUrl : '#'}" aria-label="Previous">
                                    <i class="fas fa-chevron-left"></i>
                                </a>
                            </li>
                            
                            <!-- Page Numbers -->
                            <c:forEach begin="1" end="${totalPages}" var="pageNum">
                                <c:if test="${pageNum <= 3 || pageNum > totalPages - 3 || (pageNum >= currentPage - 1 && pageNum <= currentPage + 1)}">
                                    <c:url value="/job-postings/list" var="pageUrl">
                                        <c:param name="page" value="${pageNum}"/>
                                        <c:param name="keyword" value="${keyword}"/>
                                        <c:param name="status" value="${status}"/>
                                        <c:param name="department" value="${department}"/>
                                        <c:param name="position" value="${position}"/>
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
                                <c:url value="/job-postings/list" var="nextUrl">
                                    <c:param name="page" value="${currentPage + 1}"/>
                                    <c:param name="keyword" value="${keyword}"/>
                                    <c:param name="status" value="${status}"/>
                                    <c:param name="department" value="${department}"/>
                                    <c:param name="position" value="${position}"/>
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

    <!-- Delete Job Posting Modal -->
    <div class="modal fade" id="deleteJobPostingModal" tabindex="-1" aria-labelledby="deleteJobPostingModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title" id="deleteJobPostingModalLabel">
                        <i class="fas fa-exclamation-triangle me-2"></i>Delete Job Posting
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form id="deleteJobPostingForm" method="POST" action="${pageContext.request.contextPath}/job-postings/delete">
                    <div class="modal-body">
                        <div class="text-center mb-4">
                            <div class="mb-3">
                                <i class="fas fa-trash-alt text-danger" style="font-size: 3rem;"></i>
                            </div>
                            <h5>Are you sure you want to delete this job posting?</h5>
                            <p class="text-muted">Job posting #<strong id="deleteJobId"></strong>: <strong id="deleteJobTitle"></strong></p>
                            <p class="text-danger"><strong>Warning:</strong> This action cannot be undone.</p>
                        </div>
                        <input type="hidden" id="deleteJobIdInput" name="jobId" value="">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <i class="fas fa-times me-1"></i>Cancel
                        </button>
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-trash-alt me-1"></i>Delete Job Posting
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Job Posting Deletion JavaScript -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Delete button handlers
            document.querySelectorAll('.btn-delete').forEach(function(btn) {
                btn.addEventListener('click', function(e) {
                    e.preventDefault();
                    const jobId = this.getAttribute('data-job-id');
                    const jobTitle = this.getAttribute('data-job-title');

                    document.getElementById('deleteJobIdInput').value = jobId;
                    document.getElementById('deleteJobId').textContent = jobId;
                    document.getElementById('deleteJobTitle').textContent = jobTitle;

                    const deleteModal = new bootstrap.Modal(document.getElementById('deleteJobPostingModal'));
                    deleteModal.show();
                });
            });
        });
    </script>
</body>
</html>
