<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Careers - Join Our Team</title>

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

    <!-- Global CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-light sticky-top">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/home">
                <i class="fas fa-building me-2"></i>HR Management System
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/home">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/company-info">About Us</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/home#jobs">Jobs</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/login">
                            <i class="fas fa-sign-in-alt me-1"></i>Login
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>

    <!-- Hero Section -->
    <section class="hero-section">
        <div class="container text-center">
            <h1>Join Our Amazing Team</h1>
            <p class="lead">Discover exciting career opportunities and grow with us</p>
            <!-- <a href="#jobs" class="btn btn-light btn-lg mt-3">
                <i class="fas fa-briefcase me-2"></i>View Open Positions
            </a> -->
        </div>
    </section>

    <!-- Company Info Section -->
    <section class="company-info">
        <div class="container">
            <h2 class="section-title">Why Work With Us?</h2>
            <div class="row">
                <div class="col-md-4 mb-4">
                    <div class="info-card">
                        <i class="fas fa-users"></i>
                        <h3>Great Team</h3>
                        <p>Work with talented professionals in a collaborative environment</p>
                    </div>
                </div>
                <div class="col-md-4 mb-4">
                    <div class="info-card">
                        <i class="fas fa-chart-line"></i>
                        <h3>Career Growth</h3>
                        <p>Continuous learning opportunities and clear career progression paths</p>
                    </div>
                </div>
                <div class="col-md-4 mb-4">
                    <div class="info-card">
                        <i class="fas fa-heart"></i>
                        <h3>Great Benefits</h3>
                        <p>Competitive salary, health insurance, and work-life balance</p>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <!-- Jobs Section -->
    <section id="jobs" class="py-5">
        <div class="container">
            <h2 class="section-title">Open Positions</h2>

            <!-- Error Message -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <c:choose>
                <c:when test="${not empty openJobs}">
                    <div class="row">
                        <c:forEach var="job" items="${openJobs}">
                            <div class="col-md-6 col-lg-4">
                                <div class="job-card">
                                    <div class="job-card-header">
                                        <div class="job-icon">
                                            <i class="fas fa-briefcase"></i>
                                        </div>
                                        <div>
                                            <div class="job-title">${job.jobTitle}</div>
                                        </div>
                                    </div>

                                    <div class="job-meta">
                                        <c:if test="${not empty job.departmentName}">
                                            <div class="job-meta-item">
                                                <i class="fas fa-building"></i>
                                                <span>${job.departmentName}</span>
                                            </div>
                                        </c:if>
                                        <c:if test="${not empty job.positionName}">
                                            <div class="job-meta-item">
                                                <i class="fas fa-user-tie"></i>
                                                <span>${job.positionName}</span>
                                            </div>
                                        </c:if>
                                        <c:if test="${job.numberOfPositions != null}">
                                            <div class="job-meta-item">
                                                <i class="fas fa-users"></i>
                                                <span>${job.numberOfPositions} position(s)</span>
                                            </div>
                                        </c:if>
                                    </div>

                                    <div class="job-description">
                                        ${job.jobDescription}
                                    </div>

                                    <div class="job-footer">
                                        <div class="salary-range">
                                            <c:choose>
                                                <c:when test="${job.salaryRangeFrom != null && job.salaryRangeTo != null}">
                                                    <fmt:formatNumber value="${job.salaryRangeFrom}" type="currency" currencySymbol="$"/>
                                                    - <fmt:formatNumber value="${job.salaryRangeTo}" type="currency" currencySymbol="$"/>
                                                </c:when>
                                                <c:otherwise>
                                                    Competitive
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <a href="${pageContext.request.contextPath}/jobs/detail?jobId=${job.jobId}"
                                           class="btn btn-apply">
                                            View Details
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- Pagination -->
                    <c:if test="${totalPages > 1}">
                        <nav aria-label="Job listings pagination">
                            <ul class="pagination justify-content-center">
                                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                    <a class="page-link" href="?page=${currentPage - 1}">Previous</a>
                                </li>
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                                        <a class="page-link" href="?page=${i}">${i}</a>
                                    </li>
                                </c:forEach>
                                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                    <a class="page-link" href="?page=${currentPage + 1}">Next</a>
                                </li>
                            </ul>
                        </nav>
                    </c:if>
                </c:when>
                <c:otherwise>
                    <div class="no-jobs">
                        <i class="fas fa-briefcase"></i>
                        <h3>No Open Positions</h3>
                        <p>We don't have any open positions at the moment. Please check back later!</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </section>

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
