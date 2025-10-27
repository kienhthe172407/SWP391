<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<%-- Role comes from authenticated session; do not override here --%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Job Posting - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    
    <style>
        .form-card {
            border: none;
            box-shadow: 0 0.125rem 0.25rem rgba(0, 0, 0, 0.075);
            border-radius: 0.5rem;
        }
        
        .form-header {
            background: #007bff;
            color: white;
            border-radius: 0.5rem 0.5rem 0 0;
            padding: 1.5rem;
        }
        
        .form-section {
            border-bottom: 1px solid #e9ecef;
            padding: 1.5rem;
        }
        
        .form-section:last-child {
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
        
        .required-field::after {
            content: " *";
            color: #dc3545;
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
        
        .form-actions {
            background-color: #f8f9fa;
            border-radius: 0 0 0.5rem 0.5rem;
            padding: 1.5rem;
        }
        
        /* Error message styling */
        .error-message {
            color: #dc3545 !important;
            font-size: 0.85rem;
            font-style: italic;
            display: none !important;
            margin-top: 0.25rem;
        }
        
        /* Show error message when displayed */
        .error-message.show {
            display: block !important;
            color: #dc3545 !important;
        }
        
        /* Force display when inline style is set */
        .error-message[style*="block"] {
            display: block !important;
            color: #dc3545 !important;
        }
        
        /* Invalid input styling */
        .form-control.is-invalid {
            border-color: #dc3545 !important;
            box-shadow: 0 0 0 0.2rem rgba(220, 53, 69, 0.25) !important;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="sidebar-header">
            <c:set var="roleName" value="${sessionScope.userRole != null ? sessionScope.userRole : (sessionScope.user != null ? sessionScope.user.roleDisplayName : '')}" />
            <c:set var="isHRManager" value="${roleName == 'HR Manager' || (sessionScope.user != null && (sessionScope.user.role == 'HR_MANAGER' || sessionScope.user.role == 'HR Manager'))}" />
            <c:set var="isHR" value="${roleName == 'HR' || (sessionScope.user != null && (sessionScope.user.role == 'HR' || sessionScope.user.role == 'HR_STAFF'))}" />
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
            <li class="menu-section">Dashboard</li>
            <li>
                <a href="${pageContext.request.contextPath}/${isHRManager ? 'dashboard/hr-manager-dashboard.jsp' : 'dashboard/hr-dashboard.jsp'}">
                    <i class="fas fa-home"></i>
                    <span>Overview</span>
                </a>
            </li>

            <c:choose>
                <c:when test="${isHRManager}">
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

                    <li class="menu-section">Recruitment</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/job-postings/list" class="active">
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
                    <!-- HR Staff Menu (mirror hr-dashboard.jsp) -->
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

                    <li class="menu-section">Recruitment</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/job-postings/list" class="active">
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
                </c:otherwise>
            </c:choose>

            
        </ul>
    </div>
    
    <!-- Main Content -->
    <div class="main-content">
        <!-- Top Header -->
        <div class="top-header">
            <h1>Edit Job Posting</h1>
            <div class="user-info">
                <span>HR Management</span>
                <div class="avatar">HR</div>
            </div>
        </div>
        
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/job-postings/list">Job Postings</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/job-postings/detail?id=${jobPosting.jobId}">Job Details</a></li>
                <li class="breadcrumb-item active">Edit Job Posting</li>
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
            <a href="${pageContext.request.contextPath}/job-postings/detail?id=${jobPosting.jobId}" class="back-link">
                <i class="fas fa-arrow-left me-2"></i>Back to Job Posting Details
            </a>
            
            <!-- Edit Job Posting Form -->
            <div class="card form-card">
                <!-- Header -->
                <div class="form-header">
                    <h2 class="mb-0">
                        <i class="fas fa-edit me-2"></i>Edit Job Posting
                    </h2>
                    <p class="mb-0 mt-2">Update the details for job posting: ${jobPosting.jobTitle}</p>
                </div>
                
                <form method="POST" action="${pageContext.request.contextPath}/job-postings/edit" id="jobPostingForm">
                    <!-- Hidden field for job ID -->
                    <input type="hidden" name="jobId" value="${jobPosting.jobId}">
                    
                    <!-- Basic Information Section -->
                    <div class="form-section">
                        <h5 class="section-title">
                            <i class="fas fa-info-circle"></i>Basic Information
                        </h5>
                        <div class="row">
                            <div class="col-md-8">
                                <div class="mb-3">
                                    <label for="jobTitle" class="form-label required-field">Job Title</label>
                                    <input type="text" class="form-control" id="jobTitle" name="jobTitle" 
                                           placeholder="Enter job title" value="${jobPosting.jobTitle}" required>
                                    <div id="jobTitleError" class="error-message">Please enter a job title</div>
                                </div>
                            </div>
                            <div class="col-md-4">
                                <div class="mb-3">
                                    <label for="jobStatus" class="form-label">Status</label>
                                    <select class="form-select" id="jobStatus" name="jobStatus">
                                        <option value="Open" ${jobPosting.jobStatus == 'Open' ? 'selected' : ''}>Open</option>
                                        <option value="Closed" ${jobPosting.jobStatus == 'Closed' ? 'selected' : ''}>Closed</option>
                                        <option value="Filled" ${jobPosting.jobStatus == 'Filled' ? 'selected' : ''}>Filled</option>
                                        <option value="Cancelled" ${jobPosting.jobStatus == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="departmentId" class="form-label">Department</label>
                                    <select class="form-select" id="departmentId" name="departmentId">
                                        <option value="">Select Department</option>
                                        <c:forEach var="department" items="${departments}">
                                            <option value="${department.departmentId}" ${jobPosting.departmentId == department.departmentId ? 'selected' : ''}>${department.departmentName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="positionId" class="form-label">Position</label>
                                    <select class="form-select" id="positionId" name="positionId">
                                        <option value="">Select Position</option>
                                        <c:forEach var="position" items="${positions}">
                                            <option value="${position.positionId}" ${jobPosting.positionId == position.positionId ? 'selected' : ''}>${position.positionName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4">
                                <div class="mb-3">
                                    <label for="numberOfPositions" class="form-label">Number of Positions</label>
                                    <input type="number" class="form-control" id="numberOfPositions" name="numberOfPositions" 
                                           value="${jobPosting.numberOfPositions}" min="1" max="100">
                                    <div id="numberOfPositionsError" class="error-message">Please enter a valid number of positions (1-100)</div>
                                </div>
                            </div>
                            <div class="col-md-8">
                                <div class="mb-3">
                                    <label for="applicationDeadline" class="form-label">Application Deadline</label>
                                    <input type="date" class="form-control" id="applicationDeadline" name="applicationDeadline" 
                                           value="${jobPosting.applicationDeadline}">
                                    <div id="applicationDeadlineError" class="error-message">Application deadline must be in the future</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Job Description Section -->
                    <div class="form-section">
                        <h5 class="section-title">
                            <i class="fas fa-file-alt"></i>Job Description
                        </h5>
                        <div class="mb-3">
                            <label for="jobDescription" class="form-label required-field">Job Description</label>
                            <textarea class="form-control" id="jobDescription" name="jobDescription" 
                                      rows="6" placeholder="Describe the job responsibilities, duties, and expectations..." required>${jobPosting.jobDescription}</textarea>
                            <div id="jobDescriptionError" class="error-message">Please enter a job description</div>
                        </div>
                    </div>
                    
                    <!-- Requirements Section -->
                    <div class="form-section">
                        <h5 class="section-title">
                            <i class="fas fa-list-check"></i>Requirements
                        </h5>
                        <div class="mb-3">
                            <label for="requirements" class="form-label">Requirements</label>
                            <textarea class="form-control" id="requirements" name="requirements" 
                                      rows="4" placeholder="List the required qualifications, skills, and experience...">${jobPosting.requirements}</textarea>
                        </div>
                    </div>
                    
                    <!-- Benefits Section -->
                    <div class="form-section">
                        <h5 class="section-title">
                            <i class="fas fa-gift"></i>Benefits
                        </h5>
                        <div class="mb-3">
                            <label for="benefits" class="form-label">Benefits</label>
                            <textarea class="form-control" id="benefits" name="benefits" 
                                      rows="4" placeholder="List the benefits and perks offered...">${jobPosting.benefits}</textarea>
                        </div>
                    </div>
                    
                    <!-- Salary Information Section -->
                    <div class="form-section">
                        <h5 class="section-title">
                            <i class="fas fa-dollar-sign"></i>Salary Information
                        </h5>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="salaryRangeFrom" class="form-label">Salary Range From</label>
                                    <div class="input-group">
                                        <span class="input-group-text">$</span>
                                        <input type="number" class="form-control" id="salaryRangeFrom" name="salaryRangeFrom" 
                                               step="0.01" min="0" placeholder="0.00" value="${jobPosting.salaryRangeFrom}">
                                    </div>
                                    <div id="salaryRangeFromError" class="error-message">Please enter a valid salary amount</div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="salaryRangeTo" class="form-label">Salary Range To</label>
                                    <div class="input-group">
                                        <span class="input-group-text">$</span>
                                        <input type="number" class="form-control" id="salaryRangeTo" name="salaryRangeTo" 
                                               step="0.01" min="0" placeholder="0.00" value="${jobPosting.salaryRangeTo}">
                                    </div>
                                    <div id="salaryRangeToError" class="error-message">Please enter a valid salary amount</div>
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Internal Notes Section (HR Only) -->
                    <c:if test="${sessionScope.userRole == 'HR Manager'}">
                        <div class="form-section">
                            <h5 class="section-title">
                                <i class="fas fa-sticky-note"></i>Internal Notes
                            </h5>
                            <div class="mb-3">
                                <label for="internalNotes" class="form-label">Internal Notes</label>
                                <textarea class="form-control" id="internalNotes" name="internalNotes" 
                                          rows="3" placeholder="Internal notes for HR team (not visible to applicants)...">${jobPosting.internalNotes}</textarea>
                            </div>
                        </div>
                    </c:if>
                    
                    <!-- Form Actions -->
                    <div class="form-actions">
                        <div class="d-flex gap-2 flex-wrap">
                            <button type="button" id="submitBtn" class="btn btn-primary">
                                <i class="fas fa-save me-1"></i>Save Changes
                            </button>
                            <a href="${pageContext.request.contextPath}/job-postings/detail?id=${jobPosting.jobId}" class="btn btn-secondary">
                                <i class="fas fa-times me-1"></i>Cancel
                            </a>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Form JavaScript -->
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

            // Set minimum date for application deadline to today
            const today = new Date().toISOString().split('T')[0];
            document.getElementById('applicationDeadline').setAttribute('min', today);

            // Form validation
            const form = document.getElementById('jobPostingForm');
            
            if (!form) {
                console.error('Form element not found!');
                return;
            }
            
            // Function to show error message
            const showError = function(inputId, show) {
                const input = document.getElementById(inputId);
                const errorElement = document.getElementById(inputId + 'Error');
                
                if (show) {
                    input.classList.add('is-invalid');
                    errorElement.classList.add('show');
                    errorElement.style.display = 'block';
                    errorElement.style.color = '#dc3545';
                } else {
                    input.classList.remove('is-invalid');
                    errorElement.classList.remove('show');
                    errorElement.style.display = 'none';
                }
            };
            
            // Form validation function
            const validateForm = function() {
                let isValid = true;
                
                // Clear all previous errors
                document.querySelectorAll('.error-message').forEach(el => {
                    el.style.display = 'none';
                });
                document.querySelectorAll('.is-invalid').forEach(el => {
                    el.classList.remove('is-invalid');
                });
                
                // Validate job title
                const jobTitle = document.getElementById('jobTitle').value.trim();
                if (!jobTitle) {
                    showError('jobTitle', true);
                    isValid = false;
                }
                
                // Validate job description
                const jobDescription = document.getElementById('jobDescription').value.trim();
                if (!jobDescription) {
                    showError('jobDescription', true);
                    isValid = false;
                }
                
                // Validate number of positions
                const numberOfPositions = document.getElementById('numberOfPositions').value;
                if (numberOfPositions && (isNaN(numberOfPositions) || numberOfPositions < 1 || numberOfPositions > 100)) {
                    showError('numberOfPositions', true);
                    isValid = false;
                }
                
                // Validate application deadline
                const applicationDeadline = document.getElementById('applicationDeadline').value;
                if (applicationDeadline) {
                    const deadlineDate = new Date(applicationDeadline);
                    const today = new Date();
                    today.setHours(0, 0, 0, 0);
                    
                    if (deadlineDate <= today) {
                        showError('applicationDeadline', true);
                        isValid = false;
                    }
                }
                
                // Validate salary range
                const salaryFrom = document.getElementById('salaryRangeFrom').value;
                const salaryTo = document.getElementById('salaryRangeTo').value;
                
                if (salaryFrom && (isNaN(salaryFrom) || parseFloat(salaryFrom) < 0)) {
                    showError('salaryRangeFrom', true);
                    isValid = false;
                }
                
                if (salaryTo && (isNaN(salaryTo) || parseFloat(salaryTo) < 0)) {
                    showError('salaryRangeTo', true);
                    isValid = false;
                }
                
                // Validate salary range logic
                if (salaryFrom && salaryTo && parseFloat(salaryFrom) > parseFloat(salaryTo)) {
                    showError('salaryRangeFrom', true);
                    showError('salaryRangeTo', true);
                    isValid = false;
                }
                
                return isValid;
            };
            
            // Add event listeners to clear errors when input changes
            document.querySelectorAll('input, select, textarea').forEach(element => {
                element.addEventListener('input', function() {
                    this.classList.remove('is-invalid');
                    const errorId = this.id + 'Error';
                    const errorElement = document.getElementById(errorId);
                    if (errorElement) {
                        errorElement.classList.remove('show');
                        errorElement.style.display = 'none';
                        errorElement.style.color = '';
                    }
                });
            });
            
            // Button click validation
            const submitBtn = document.getElementById('submitBtn');
            submitBtn.addEventListener('click', function() {
                const isValid = validateForm();
                
                if (!isValid) {
                    // Scroll to first error
                    const firstError = document.querySelector('.is-invalid');
                    if (firstError) {
                        firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
                        firstError.focus();
                    }
                } else {
                    // Validation passed - submit the form
                    form.submit();
                }
            });
        });
    </script>
</body>
</html>
