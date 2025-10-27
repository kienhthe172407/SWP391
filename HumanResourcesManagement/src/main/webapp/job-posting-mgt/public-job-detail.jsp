<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${jobPosting.jobTitle} - Job Details</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-light">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/home">
                <i class="fas fa-building me-2"></i>HR Management System
            </a>
            <div class="ms-auto">
                <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-primary">
                    <i class="fas fa-arrow-left me-2"></i>Back to Jobs
                </a>
            </div>
        </div>
    </nav>

    <!-- Job Header -->
    <section class="job-header">
        <div class="container">
            <h1>${jobPosting.jobTitle}</h1>
            <div class="job-meta">
                <c:if test="${not empty jobPosting.departmentName}">
                    <div class="job-meta-item">
                        <i class="fas fa-building"></i>
                        <span>${jobPosting.departmentName}</span>
                    </div>
                </c:if>
                <c:if test="${not empty jobPosting.positionName}">
                    <div class="job-meta-item">
                        <i class="fas fa-user-tie"></i>
                        <span>${jobPosting.positionName}</span>
                    </div>
                </c:if>
                <c:if test="${jobPosting.numberOfPositions != null}">
                    <div class="job-meta-item">
                        <i class="fas fa-users"></i>
                        <span>${jobPosting.numberOfPositions} Opening(s)</span>
                    </div>
                </c:if>
                <c:if test="${jobPosting.applicationDeadline != null}">
                    <div class="job-meta-item">
                        <i class="fas fa-calendar-alt"></i>
                        <span>Deadline: <fmt:formatDate value="${jobPosting.applicationDeadline}" pattern="MMM dd, yyyy"/></span>
                    </div>
                </c:if>
            </div>
        </div>
    </section>

    <!-- Main Content -->
    <div class="container mb-5">
        <div class="row">
            <!-- Left Column - Job Details -->
            <div class="col-lg-8">
                <!-- Job Description -->
                <div class="content-card">
                    <h3><i class="fas fa-file-alt me-2"></i>Job Description</h3>
                    <p>${jobPosting.jobDescription}</p>
                </div>
                
                <!-- Requirements -->
                <c:if test="${not empty jobPosting.requirements}">
                    <div class="content-card">
                        <h3><i class="fas fa-check-circle me-2"></i>Requirements</h3>
                        <p>${jobPosting.requirements}</p>
                    </div>
                </c:if>
                
                <!-- Benefits -->
                <c:if test="${not empty jobPosting.benefits}">
                    <div class="content-card">
                        <h3><i class="fas fa-gift me-2"></i>Benefits</h3>
                        <p>${jobPosting.benefits}</p>
                    </div>
                </c:if>
            </div>
            
            <!-- Right Column - Apply Section -->
            <div class="col-lg-4">
                <div class="apply-section">
                    <h4 class="mb-4">Job Information</h4>
                    
                    <!-- Salary -->
                    <div class="salary-display">
                        <c:choose>
                            <c:when test="${jobPosting.salaryRangeFrom != null && jobPosting.salaryRangeTo != null}">
                                <fmt:formatNumber value="${jobPosting.salaryRangeFrom}" type="currency" currencySymbol="$"/>
                                - <fmt:formatNumber value="${jobPosting.salaryRangeTo}" type="currency" currencySymbol="$"/>
                            </c:when>
                            <c:otherwise>
                                Competitive Salary
                            </c:otherwise>
                        </c:choose>
                    </div>
                    
                    <!-- Additional Info -->
                    <div class="mb-4">
                        <div class="info-item">
                            <span class="info-label">Status:</span>
                            <span class="info-value">
                                <span class="badge bg-success">${jobPosting.jobStatus}</span>
                            </span>
                        </div>
                        <c:if test="${jobPosting.postedDate != null}">
                            <div class="info-item">
                                <span class="info-label">Posted:</span>
                                <span class="info-value">
                                    <fmt:formatDate value="${jobPosting.postedDate}" pattern="MMM dd, yyyy"/>
                                </span>
                            </div>
                        </c:if>
                    </div>
                    
                    <!-- Deadline Warning -->
                    <c:if test="${jobPosting.applicationDeadline != null && canApply}">
                        <div class="deadline-warning">
                            <i class="fas fa-clock me-2"></i>
                            <strong>Application Deadline:</strong><br>
                            <fmt:formatDate value="${jobPosting.applicationDeadline}" pattern="MMMM dd, yyyy"/>
                        </div>
                    </c:if>
                    
                    <!-- Apply Button -->
                    <c:choose>
                        <c:when test="${canApply}">
                            <a href="${pageContext.request.contextPath}/jobs/apply?jobId=${jobPosting.jobId}" 
                               class="btn btn-apply-now">
                                <i class="fas fa-paper-plane me-2"></i>Apply Now
                            </a>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-apply-now" disabled>
                                <i class="fas fa-times-circle me-2"></i>Applications Closed
                            </button>
                            <p class="text-muted text-center mt-3 mb-0">
                                <small>This position is no longer accepting applications.</small>
                            </p>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer class="text-white py-4 mt-5" style="background-color: #2563eb;">
        <div class="container text-center">
            <p class="mb-0">&copy; 2024 HR Management System. All rights reserved.</p>
        </div>
    </footer>

    <!-- Bootstrap 5 JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

