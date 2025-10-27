<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apply for ${jobPosting.jobTitle}</title>
    
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
                <a href="${pageContext.request.contextPath}/jobs/detail?jobId=${jobPosting.jobId}" 
                   class="btn btn-outline-primary">
                    <i class="fas fa-arrow-left me-2"></i>Back to Job Details
                </a>
            </div>
        </div>
    </nav>

    <!-- Page Header -->
    <section class="page-header">
        <div class="container">
            <h1><i class="fas fa-file-alt me-2"></i>Apply for Position</h1>
            <p class="mb-0">Fill out the form below to submit your application</p>
        </div>
    </section>

    <!-- Application Form -->
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <!-- Job Info Card -->
                <div class="job-info-card">
                    <h5><i class="fas fa-briefcase me-2"></i>${jobPosting.jobTitle}</h5>
                    <p><i class="fas fa-building me-2"></i>${jobPosting.departmentName}</p>
                    <c:if test="${jobPosting.applicationDeadline != null}">
                        <p><i class="fas fa-calendar-alt me-2"></i>Deadline: 
                            <fmt:formatDate value="${jobPosting.applicationDeadline}" pattern="MMMM dd, yyyy"/>
                        </p>
                    </c:if>
                </div>
                
                <!-- Success Message -->
                <c:if test="${not empty sessionScope.successMessage}">
                    <div class="success-message">
                        <i class="fas fa-check-circle"></i>
                        <h3>Application Submitted Successfully!</h3>
                        <p>Your application has been submitted successfully! We will contact you soon.</p>
                        <div class="redirect-info">You can continue browsing other job opportunities.</div>
                    </div>
                    <c:remove var="successMessage" scope="session"/>
                </c:if>
                
                <!-- Error Message -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle me-2"></i>${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>
                
                <!-- Application Form -->
                <div class="application-form">
                    <form method="POST" action="${pageContext.request.contextPath}/jobs/apply" 
                          enctype="multipart/form-data" id="applicationForm">
                        <input type="hidden" name="jobId" value="${jobPosting.jobId}">
                        
                        <!-- Personal Information -->
                        <div class="form-section">
                            <h4><i class="fas fa-user me-2"></i>Personal Information</h4>
                            
                            <div class="mb-3">
                                <label for="applicantName" class="form-label required-field">Full Name</label>
                                <input type="text" class="form-control" id="applicantName" name="applicantName" 
                                       placeholder="Enter your full name" required>
                                <div class="error-message">Please enter your full name</div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="applicantEmail" class="form-label required-field">Email Address</label>
                                <input type="email" class="form-control" id="applicantEmail" name="applicantEmail" 
                                       placeholder="your.email@example.com" required>
                                <div class="error-message">Please enter a valid email address</div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="applicantPhone" class="form-label">Phone Number</label>
                                <input type="tel" class="form-control" id="applicantPhone" name="applicantPhone" 
                                       placeholder="+1 (555) 123-4567">
                                <small class="text-muted">Optional - We'll use this to contact you</small>
                            </div>
                        </div>
                        
                        <!-- Resume Upload -->
                        <div class="form-section">
                            <h4><i class="fas fa-file-upload me-2"></i>Resume/CV</h4>
                            
                            <div class="file-upload-area">
                                <i class="fas fa-cloud-upload-alt"></i>
                                <h5>Upload Your Resume</h5>
                                <p class="text-muted mb-3">Accepted formats: PDF, DOC, DOCX (Max 5MB)</p>
                                <input type="file" class="form-control" id="resumeFile" name="resumeFile" 
                                       accept=".pdf,.doc,.docx">
                                <div class="error-message">Please upload a valid resume file</div>
                            </div>
                        </div>
                        
                        <!-- Cover Letter -->
                        <div class="form-section">
                            <h4><i class="fas fa-envelope me-2"></i>Cover Letter</h4>
                            
                            <div class="mb-3">
                                <label for="coverLetter" class="form-label">Tell us why you're a great fit</label>
                                <textarea class="form-control" id="coverLetter" name="coverLetter" rows="6" 
                                          placeholder="Write a brief cover letter explaining why you're interested in this position and what makes you a great candidate..."></textarea>
                                <small class="text-muted">Optional - This helps us understand your motivation</small>
                            </div>
                        </div>
                        
                        <!-- Submit Button -->
                        <div class="text-center">
                            <button type="submit" class="btn btn-submit">
                                <i class="fas fa-paper-plane me-2"></i>Submit Application
                            </button>
                        </div>
                    </form>
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
    
    <script>
        // Form validation
        document.getElementById('applicationForm').addEventListener('submit', function(e) {
            let isValid = true;
            
            // Validate name
            const name = document.getElementById('applicantName');
            if (name.value.trim() === '') {
                name.classList.add('is-invalid');
                isValid = false;
            } else {
                name.classList.remove('is-invalid');
            }
            
            // Validate email
            const email = document.getElementById('applicantEmail');
            const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!emailRegex.test(email.value)) {
                email.classList.add('is-invalid');
                isValid = false;
            } else {
                email.classList.remove('is-invalid');
            }
            
            if (!isValid) {
                e.preventDefault();
            }
        });
        
        // File upload feedback
        document.getElementById('resumeFile').addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (file) {
                const fileSize = file.size / 1024 / 1024; // in MB
                if (fileSize > 5) {
                    alert('File size must be less than 5MB');
                    e.target.value = '';
                }
            }
        });
    </script>
</body>
</html>

