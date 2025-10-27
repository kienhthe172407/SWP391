<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>About Us - Company Information</title>

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
                        <a class="nav-link active" href="${pageContext.request.contextPath}/company-info">About Us</a>
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
            <h1>
                <c:choose>
                    <c:when test="${not empty companyInfo}">
                        About ${companyInfo.companyName}
                    </c:when>
                    <c:otherwise>
                        About Our Company
                    </c:otherwise>
                </c:choose>
            </h1>
            <p class="lead">Learn more about who we are and what we stand for</p>
        </div>
    </section>

    <!-- Error Message -->
    <c:if test="${not empty errorMessage}">
        <div class="container mt-4">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle me-2"></i>${errorMessage}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </div>
    </c:if>

    <!-- Company Information Section -->
    <c:choose>
        <c:when test="${not empty companyInfo}">
            <section class="company-info">
                <div class="container">
                    <!-- About Us -->
                    <c:if test="${not empty companyInfo.aboutUs}">
                        <div class="row mb-5">
                            <div class="col-12">
                                <div class="info-card text-start">
                                    <h3><i class="fas fa-building me-2"></i>Who We Are</h3>
                                    <p>${companyInfo.aboutUs}</p>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    
                    <!-- Mission and Vision -->
                    <div class="row mb-4">
                        <c:if test="${not empty companyInfo.missionStatement}">
                            <div class="col-md-6 mb-4">
                                <div class="info-card text-start">
                                    <i class="fas fa-bullseye"></i>
                                    <h3>Our Mission</h3>
                                    <p>${companyInfo.missionStatement}</p>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${not empty companyInfo.visionStatement}">
                            <div class="col-md-6 mb-4">
                                <div class="info-card text-start">
                                    <i class="fas fa-eye"></i>
                                    <h3>Our Vision</h3>
                                    <p>${companyInfo.visionStatement}</p>
                                </div>
                            </div>
                        </c:if>
                    </div>
                    
                    <!-- Core Values -->
                    <c:if test="${not empty companyInfo.coreValues}">
                        <div class="row mb-4">
                            <div class="col-12">
                                <div class="info-card text-start">
                                    <h3><i class="fas fa-star me-2"></i>Our Core Values</h3>
                                    <p style="white-space: pre-line;">${companyInfo.coreValues}</p>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    
                    <!-- Company Details -->
                    <h2 class="section-title mt-5">Company Details</h2>
                    <div class="row">
                        <c:if test="${not empty companyInfo.industry}">
                            <div class="col-md-3 col-sm-6 mb-4">
                                <div class="info-card">
                                    <i class="fas fa-industry"></i>
                                    <h3>Industry</h3>
                                    <p>${companyInfo.industry}</p>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${companyInfo.foundedYear != null}">
                            <div class="col-md-3 col-sm-6 mb-4">
                                <div class="info-card">
                                    <i class="fas fa-calendar-alt"></i>
                                    <h3>Founded</h3>
                                    <p>${companyInfo.foundedYear}</p>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${not empty companyInfo.numberOfEmployees}">
                            <div class="col-md-3 col-sm-6 mb-4">
                                <div class="info-card">
                                    <i class="fas fa-users"></i>
                                    <h3>Team Size</h3>
                                    <p>${companyInfo.numberOfEmployees} employees</p>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${not empty companyInfo.address}">
                            <div class="col-md-3 col-sm-6 mb-4">
                                <div class="info-card">
                                    <i class="fas fa-map-marker-alt"></i>
                                    <h3>Location</h3>
                                    <p>${companyInfo.address}</p>
                                </div>
                            </div>
                        </c:if>
                    </div>
                    
                    <!-- Contact Information -->
                    <h2 class="section-title mt-5">Get In Touch</h2>
                    <div class="row">
                        <c:if test="${not empty companyInfo.phoneNumber}">
                            <div class="col-md-4 mb-4">
                                <div class="info-card">
                                    <i class="fas fa-phone"></i>
                                    <h3>Phone</h3>
                                    <p><a href="tel:${companyInfo.phoneNumber}" style="color: var(--text-light); text-decoration: none;">${companyInfo.phoneNumber}</a></p>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${not empty companyInfo.email}">
                            <div class="col-md-4 mb-4">
                                <div class="info-card">
                                    <i class="fas fa-envelope"></i>
                                    <h3>Email</h3>
                                    <p><a href="mailto:${companyInfo.email}" style="color: var(--text-light); text-decoration: none;">${companyInfo.email}</a></p>
                                </div>
                            </div>
                        </c:if>
                        <c:if test="${not empty companyInfo.website}">
                            <div class="col-md-4 mb-4">
                                <div class="info-card">
                                    <i class="fas fa-globe"></i>
                                    <h3>Website</h3>
                                    <p><a href="http://${companyInfo.website}" target="_blank" style="color: var(--text-light); text-decoration: none;">${companyInfo.website}</a></p>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>
            </section>
        </c:when>
        <c:otherwise>
            <!-- Fallback content if no company info available -->
            <section class="company-info">
                <div class="container">
                    <div class="text-center py-5">
                        <i class="fas fa-building" style="font-size: 4rem; color: var(--text-light); opacity: 0.5;"></i>
                        <h3 class="mt-3">Company Information Not Available</h3>
                        <p class="text-muted">We're currently updating our company information. Please check back later.</p>
                        <a href="${pageContext.request.contextPath}/home" class="btn btn-primary mt-3">
                            <i class="fas fa-home me-2"></i>Back to Home
                        </a>
                    </div>
                </div>
            </section>
        </c:otherwise>
    </c:choose>

    <!-- Call to Action -->
    <c:if test="${not empty companyInfo}">
        <section class="py-5" style="background-color: var(--bg-light);">
            <div class="container text-center">
                <h2 class="mb-4">Join Our Team</h2>
                <p class="lead mb-4">Explore exciting career opportunities and be part of our success story</p>
                <a href="${pageContext.request.contextPath}/home#jobs" class="btn btn-primary btn-lg">
                    <i class="fas fa-briefcase me-2"></i>View Open Positions
                </a>
            </div>
        </section>
    </c:if>

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

