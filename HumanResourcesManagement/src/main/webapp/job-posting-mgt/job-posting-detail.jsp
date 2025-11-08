<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<%-- Role comes from authenticated session; do not override here --%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Job Posting Details - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    
    <style>
        .detail-card {
            border: none;
            box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
            border-radius: 0.5rem;
        }
        
        .detail-header {
            background: #007bff;
            color: white;
            border-radius: 0.5rem 0.5rem 0 0;
            padding: 1.5rem;
        }
        
        .detail-section {
            border-bottom: 1px solid #e9ecef;
            padding: 1.5rem;
        }
        
        .detail-section:last-child {
            border-bottom: none;
        }
        
        .section-title {
            color: #495057;
            font-weight: 600;
            margin-bottom: 1rem;
            display: flex;
            align-items: center;
        }
        
        .section-title i {
            margin-right: 0.5rem;
            color: #6c757d;
        }
        
        .info-row {
            display: flex;
            margin-bottom: 0.75rem;
        }
        
        .info-label {
            font-weight: 600;
            color: #6c757d;
            min-width: 150px;
            flex-shrink: 0;
        }
        
        .info-value {
            color: #212529;
            flex: 1;
        }
        
        .status-badge {
            font-size: 0.875rem;
            padding: 0.5rem 1rem;
            border-radius: 0.375rem;
        }
        
        .salary-range {
            font-size: 1.1rem;
            font-weight: 600;
            color: white;
        }
        
        .content-text {
            line-height: 1.6;
            color: #495057;
            white-space: pre-wrap;
        }
        
        .action-buttons {
            background-color: #f8f9fa;
            border-radius: 0 0 0.5rem 0.5rem;
            padding: 1.5rem;
        }
        
        .back-link {
            color: #6c757d;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            margin-bottom: 1rem;
        }
        
        .back-link:hover {
            color: #495057;
        }
    </style>
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="Job Posting Details" />
        </jsp:include>
        
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/job-postings/list">Job Postings</a></li>
                <li class="breadcrumb-item active">Job Posting #${jobPosting.jobId}</li>
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
            
            <!-- Back Link -->
            <a href="${pageContext.request.contextPath}/job-postings/list" class="back-link">
                <i class="fas fa-arrow-left me-2"></i>Back to Job Postings List
            </a>
            
            <!-- Job Posting Detail Card -->
            <div class="card detail-card">
                <!-- Header Section -->
                <div class="detail-header">
                    <div class="row align-items-center">
                        <div class="col-md-8">
                            <h2 class="mb-2">${jobPosting.jobTitle}</h2>
                            <div class="d-flex flex-wrap gap-3 align-items-center">
                                <span class="badge bg-light text-dark">
                                    <i class="fas fa-hashtag me-1"></i>#${jobPosting.jobId}
                                </span>
                                <c:choose>
                                    <c:when test="${jobPosting.jobStatus == 'Open'}">
                                        <span class="badge bg-success status-badge">
                                            <i class="fas fa-check-circle me-1"></i>Open
                                        </span>
                                    </c:when>
                                    <c:when test="${jobPosting.jobStatus == 'Closed'}">
                                        <span class="badge bg-secondary status-badge">
                                            <i class="fas fa-times-circle me-1"></i>Closed
                                        </span>
                                    </c:when>
                                    <c:when test="${jobPosting.jobStatus == 'Filled'}">
                                        <span class="badge bg-info status-badge">
                                            <i class="fas fa-user-check me-1"></i>Filled
                                        </span>
                                    </c:when>
                                    <c:when test="${jobPosting.jobStatus == 'Cancelled'}">
                                        <span class="badge bg-danger status-badge">
                                            <i class="fas fa-ban me-1"></i>Cancelled
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-warning status-badge">
                                            <i class="fas fa-clock me-1"></i>${jobPosting.jobStatus}
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="col-md-4 text-md-end">
                            <c:if test="${jobPosting.salaryRangeFrom != null || jobPosting.salaryRangeTo != null}">
                                <div class="salary-range">
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
                                    </c:choose>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
                
                <!-- Basic Information Section -->
                <div class="detail-section">
                    <h5 class="section-title">
                        <i class="fas fa-info-circle"></i>Basic Information
                    </h5>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="info-row">
                                <span class="info-label">Job ID:</span>
                                <span class="info-value">#${jobPosting.jobId}</span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">Department:</span>
                                <span class="info-value">
                                    <c:choose>
                                        <c:when test="${jobPosting.departmentName != null && jobPosting.departmentName != 'N/A'}">
                                            ${jobPosting.departmentName}
                                        </c:when>
                                        <c:when test="${jobPosting.departmentId != null}">
                                            <span class="text-muted">Department ID: ${jobPosting.departmentId}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">Not specified</span>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">Position:</span>
                                <span class="info-value">
                                    <c:choose>
                                        <c:when test="${jobPosting.positionName != null && jobPosting.positionName != 'N/A'}">
                                            ${jobPosting.positionName}
                                        </c:when>
                                        <c:when test="${jobPosting.positionId != null}">
                                            <span class="text-muted">Position ID: ${jobPosting.positionId}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">Not specified</span>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="info-row">
                                <span class="info-label">Number of Positions:</span>
                                <span class="info-value">
                                    <span class="badge bg-info">${jobPosting.numberOfPositions}</span>
                                </span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">Application Deadline:</span>
                                <span class="info-value">
                                    <c:choose>
                                        <c:when test="${jobPosting.applicationDeadline != null}">
                                            <fmt:formatDate value="${jobPosting.applicationDeadline}" pattern="dd/MM/yyyy"/>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">Not specified</span>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                            <div class="info-row">
                                <span class="info-label">Posted Date:</span>
                                <span class="info-value">
                                    <c:choose>
                                        <c:when test="${jobPosting.postedDate != null}">
                                            <fmt:formatDate value="${jobPosting.postedDate}" pattern="dd/MM/yyyy"/>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">Not specified</span>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Job Description Section -->
                <c:if test="${jobPosting.jobDescription != null && !jobPosting.jobDescription.trim().isEmpty()}">
                    <div class="detail-section">
                        <h5 class="section-title">
                            <i class="fas fa-file-alt"></i>Job Description
                        </h5>
                        <div class="content-text">${jobPosting.jobDescription}</div>
                    </div>
                </c:if>
                
                <!-- Requirements Section -->
                <c:if test="${jobPosting.requirements != null && !jobPosting.requirements.trim().isEmpty()}">
                    <div class="detail-section">
                        <h5 class="section-title">
                            <i class="fas fa-list-check"></i>Requirements
                        </h5>
                        <div class="content-text">${jobPosting.requirements}</div>
                    </div>
                </c:if>
                
                <!-- Benefits Section -->
                <c:if test="${jobPosting.benefits != null && !jobPosting.benefits.trim().isEmpty()}">
                    <div class="detail-section">
                        <h5 class="section-title">
                            <i class="fas fa-gift"></i>Benefits
                        </h5>
                        <div class="content-text">${jobPosting.benefits}</div>
                    </div>
                </c:if>
                
                <!-- Internal Notes Section (HR Only) -->
                <c:if test="${sessionScope.userRole == 'HR Manager' && jobPosting.internalNotes != null && !jobPosting.internalNotes.trim().isEmpty()}">
                    <div class="detail-section">
                        <h5 class="section-title">
                            <i class="fas fa-sticky-note"></i>Internal Notes
                        </h5>
                        <div class="content-text">${jobPosting.internalNotes}</div>
                    </div>
                </c:if>
                
                <!-- System Information Section -->
                <div class="detail-section">
                    <h5 class="section-title">
                        <i class="fas fa-cog"></i>System Information
                    </h5>
                    <div class="row">
                        <div class="col-md-6">
                            <div class="info-row">
                                <span class="info-label">Posted By:</span>
                                <span class="info-value">
                                    <c:choose>
                                        <c:when test="${jobPosting.posterName != null && jobPosting.posterName != 'N/A'}">
                                            ${jobPosting.posterName}
                                        </c:when>
                                        <c:when test="${jobPosting.postedBy != null}">
                                            <span class="text-muted">User ID: ${jobPosting.postedBy}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">Not specified</span>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="info-row">
                                <span class="info-label">Last Updated:</span>
                                <span class="info-value">
                                    <c:choose>
                                        <c:when test="${jobPosting.updatedAt != null}">
                                            <fmt:formatDate value="${jobPosting.updatedAt}" pattern="dd/MM/yyyy HH:mm"/>
                                        </c:when>
                                        <c:when test="${jobPosting.createdAt != null}">
                                            <fmt:formatDate value="${jobPosting.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">Not available</span>
                                        </c:otherwise>
                                    </c:choose>
                                </span>
                            </div>
                        </div>
                    </div>
                </div>
                
                <!-- Action Buttons -->
                <div class="action-buttons">
                    <div class="d-flex gap-2 flex-wrap">
                        <a href="${pageContext.request.contextPath}/job-postings/edit?id=${jobPosting.jobId}" 
                           class="btn btn-primary">
                            <i class="fas fa-edit me-1"></i>Edit Job Posting
                        </a>
                        <a href="${pageContext.request.contextPath}/job-postings/list" 
                           class="btn btn-secondary">
                            <i class="fas fa-list me-1"></i>Back to List
                        </a>
                        <button type="button" class="btn btn-danger" 
                                data-job-id="${jobPosting.jobId}"
                                data-job-title="${jobPosting.jobTitle}"
                                onclick="showDeleteModal(this)">
                            <i class="fas fa-trash me-1"></i>Delete
                        </button>
                    </div>
                </div>
            </div>
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

    <!-- JavaScript for Delete Modal -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Auto-hide success/error messages after 3 seconds
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(alert) {
                if (alert.classList.contains('alert-success')) {
                    setTimeout(function() {
                        const bsAlert = new bootstrap.Alert(alert);
                        bsAlert.close();
                    }, 3000);
                }
            });
        });

        function showDeleteModal(button) {
            const jobId = button.getAttribute('data-job-id');
            const jobTitle = button.getAttribute('data-job-title');

            document.getElementById('deleteJobIdInput').value = jobId;
            document.getElementById('deleteJobId').textContent = jobId;
            document.getElementById('deleteJobTitle').textContent = jobTitle;

            const deleteModal = new bootstrap.Modal(document.getElementById('deleteJobPostingModal'));
            deleteModal.show();
        }
    </script>
</body>
</html>
