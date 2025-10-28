<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Employee Information - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    
    <style>
        .form-section {
            margin-bottom: 2rem;
            padding-bottom: 1rem;
            border-bottom: 1px solid #eee;
        }
        
        .form-section h5 {
            margin-bottom: 1.5rem;
            color: #0d6efd;
        }
        
        .required-field::after {
            content: "*";
            color: red;
            margin-left: 4px;
        }
        
        .error-message {
            color: #dc3545;
            font-size: 0.875rem;
            margin-top: 0.25rem;
            display: none;
        }
        
        .is-invalid {
            border-color: #dc3545;
        }
        
        .btn-group-custom {
            gap: 10px;
        }
        
        .card-header {
            background-color: #0d6efd;
            color: white;
        }
        
        .form-control:focus, .form-select:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
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
                    <!-- HR Manager Menu -->
                    <li class="menu-section">Dashboard</li>
                    <li><a href="${pageContext.request.contextPath}/dashboard/hr-manager-dashboard.jsp"><i class="fas fa-home"></i><span>Overview</span></a></li>

                    <li class="menu-section">HR Management</li>
                    <li><a href="#"><i class="fas fa-users-cog"></i><span>HR Staff Management</span></a></li>
                    <li><a href="#"><i class="fas fa-tasks"></i><span>Task Assignment</span></a></li>
                    <li><a href="#"><i class="fas fa-clipboard-check"></i><span>Approval Queue</span></a></li>

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
                        <a href="${pageContext.request.contextPath}/job-postings/list">
                            <i class="fas fa-briefcase"></i>
                            <span>Job Postings</span>
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
                <c:otherwise>
                    <!-- HR Staff Menu -->
                    <li class="menu-section">Dashboard</li>
                    <li><a href="${pageContext.request.contextPath}/dashboard/hr-dashboard.jsp"><i class="fas fa-home"></i><span>Overview</span></a></li>

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
                        <a href="${pageContext.request.contextPath}/job-postings/list">
                            <i class="fas fa-briefcase"></i>
                            <span>Job Postings</span>
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
            <h1>Edit Employee Information</h1>
            <div class="user-info">
                <a href="${pageContext.request.contextPath}/employees/addInformation" class="btn btn-primary me-3">
                    <i class="fas fa-user-plus"></i> Add Employee
                </a>
                <span>HR Management</span>
                <div class="avatar">HR</div>
            </div>
        </div>

        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/employees/list">Employee Management</a></li>
                <li class="breadcrumb-item active">Edit Employee</li>
            </ol>
        </nav>

        <!-- Content Area -->
        <div class="content-area">

                <!-- Success/Error Messages -->
                <c:if test="${not empty sessionScope.successMessage}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        <i class="fas fa-check-circle"></i> ${sessionScope.successMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <c:remove var="successMessage" scope="session"/>
                </c:if>

                <c:if test="${not empty sessionScope.errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle"></i> ${sessionScope.errorMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <c:remove var="errorMessage" scope="session"/>
                </c:if>

                <!-- Edit Employee Form -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0"><i class="fas fa-user-edit text-white"></i> Employee Information</h5>
                    </div>
                    <div class="card-body">
                        <form id="editEmployeeForm" method="post" action="${pageContext.request.contextPath}/employees/edit">
                            <!-- Hidden employee ID field -->
                            <input type="hidden" name="employeeId" value="${employee.employeeID}">
                            
                            <!-- User Account Section -->
                            <div class="form-section">
                                <h5><i class="fas fa-user-circle text-primary"></i> User Account Information</h5>
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="mb-3">
                                            <label class="form-label">Linked User Account</label>
                                            <input type="text" class="form-control" value="${employee.userID}" readonly disabled>
                                            <div class="form-text">
                                                <i class="fas fa-info-circle"></i>
                                                User account linking cannot be changed after employee creation.
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Basic Information Section -->
                            <div class="form-section">
                                <h5><i class="fas fa-user text-primary"></i> Basic Information</h5>
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="employeeCode" class="form-label required-field">Employee Code</label>
                                            <input type="text" class="form-control" id="employeeCode" name="employeeCode"
                                                   value="${employee.employeeCode}" readonly>
                                            <div class="form-text">
                                                <i class="fas fa-info-circle"></i>
                                                Employee code cannot be changed after creation.
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="employmentStatus" class="form-label">Employment Status</label>
                                            <select class="form-select" id="employmentStatus" name="employmentStatus">
                                                <option value="Active" ${employee.employmentStatus == 'Active' ? 'selected' : ''}>Active</option>
                                                <option value="On Leave" ${employee.employmentStatus == 'On Leave' ? 'selected' : ''}>On Leave</option>
                                                <option value="Terminated" ${employee.employmentStatus == 'Terminated' ? 'selected' : ''}>Terminated</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="firstName" class="form-label required-field">First Name</label>
                                            <input type="text" class="form-control" id="firstName" name="firstName" value="${employee.firstName}" required>
                                            <div class="error-message" id="firstNameError">First name is required.</div>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="lastName" class="form-label required-field">Last Name</label>
                                            <input type="text" class="form-control" id="lastName" name="lastName" value="${employee.lastName}" required>
                                            <div class="error-message" id="lastNameError">Last name is required.</div>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="dateOfBirth" class="form-label">Date of Birth</label>
                                            <input type="date" class="form-control" id="dateOfBirth" name="dateOfBirth" 
                                                   value="<fmt:formatDate value='${employee.dateOfBirth}' pattern='yyyy-MM-dd'/>">
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="gender" class="form-label">Gender</label>
                                            <select class="form-select" id="gender" name="gender">
                                                <option value="">Select Gender</option>
                                                <option value="Male" ${employee.gender == 'Male' ? 'selected' : ''}>Male</option>
                                                <option value="Female" ${employee.gender == 'Female' ? 'selected' : ''}>Female</option>
                                                <option value="Other" ${employee.gender == 'Other' ? 'selected' : ''}>Other</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Contact Information Section -->
                            <div class="form-section">
                                <h5><i class="fas fa-address-book text-primary"></i> Contact Information</h5>
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="phoneNumber" class="form-label">Phone Number</label>
                                            <input type="tel" class="form-control" id="phoneNumber" name="phoneNumber" value="${employee.phoneNumber}">
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="personalEmail" class="form-label">Personal Email</label>
                                            <input type="email" class="form-control" id="personalEmail" name="personalEmail" value="${employee.personalEmail}">
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="homeAddress" class="form-label">Home Address</label>
                                    <textarea class="form-control" id="homeAddress" name="homeAddress" rows="3">${employee.homeAddress}</textarea>
                                </div>
                                
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="emergencyContactName" class="form-label">Emergency Contact Name</label>
                                            <input type="text" class="form-control" id="emergencyContactName" name="emergencyContactName" value="${employee.emergencyContactName}">
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="emergencyContactPhone" class="form-label">Emergency Contact Phone</label>
                                            <input type="tel" class="form-control" id="emergencyContactPhone" name="emergencyContactPhone" value="${employee.emergencyContactPhone}">
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Job Information Section -->
                            <div class="form-section">
                                <h5><i class="fas fa-briefcase text-primary"></i> Job Information</h5>
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="departmentId" class="form-label">Department</label>
                                            <select class="form-select" id="departmentId" name="departmentId">
                                                <option value="">Select Department</option>
                                                <c:forEach var="department" items="${departments}">
                                                    <option value="${department.departmentId}" ${employee.departmentID == department.departmentId ? 'selected' : ''}>${department.departmentName}</option>
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
                                                    <option value="${position.positionId}" ${employee.positionID == position.positionId ? 'selected' : ''}>${position.positionName}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="managerId" class="form-label">Manager</label>
                                            <select class="form-select" id="managerId" name="managerId">
                                                <option value="">Select Manager</option>
                                                <c:forEach var="manager" items="${managers}">
                                                    <option value="${manager.employeeID}" ${employee.managerID == manager.employeeID ? 'selected' : ''}>${manager.employeeCode} - ${manager.firstName} ${manager.lastName}</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="hireDate" class="form-label">Hire Date</label>
                                            <input type="date" class="form-control" id="hireDate" name="hireDate" 
                                                   value="<fmt:formatDate value='${employee.hireDate}' pattern='yyyy-MM-dd'/>">
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <!-- Form Actions -->
                            <div class="d-flex justify-content-end btn-group-custom">
                                <a href="${pageContext.request.contextPath}/employees/list" class="btn btn-secondary">
                                    <i class="fas fa-times"></i> Cancel
                                </a>
                                <button type="button" id="submitBtn" class="btn btn-primary">
                                    <i class="fas fa-save"></i> Update Employee Information
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Termination Confirmation Modal -->
    <div class="modal fade" id="terminationModal" tabindex="-1" aria-labelledby="terminationModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title" id="terminationModalLabel">
                        <i class="fas fa-exclamation-triangle me-2"></i>Confirm Employee Termination
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="text-center mb-4">
                        <div class="mb-3">
                            <i class="fas fa-user-slash text-danger" style="font-size: 3rem;"></i>
                        </div>
                        <h5>Are you sure you want to terminate this employee?</h5>
                        <p>Employee: <strong>${employee.firstName} ${employee.lastName}</strong></p>
                        <p>Employee Code: <strong>${employee.employeeCode}</strong></p>
                        <p class="text-danger"><strong>Note:</strong> This will mark the employee as "Terminated" but their information will be retained in the system.</p>
                    </div>
                    <div class="mb-3">
                        <label for="terminationReason" class="form-label">Termination Reason</label>
                        <textarea class="form-control" id="terminationReason" rows="3" placeholder="Enter reason for termination (optional)"></textarea>
                    </div>
                    <div class="mb-3">
                        <label for="terminationDate" class="form-label">Termination Date</label>
                        <input type="date" class="form-control" id="terminationDate" value="${currentDate != null ? currentDate : ''}">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="fas fa-times me-1"></i>Cancel
                    </button>
                    <button type="button" id="confirmTerminationBtn" class="btn btn-danger">
                        <i class="fas fa-user-slash me-1"></i>Confirm Termination
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const form = document.getElementById('editEmployeeForm');
            const employmentStatusSelect = document.getElementById('employmentStatus');
            const terminationModal = new bootstrap.Modal(document.getElementById('terminationModal'));
            const confirmTerminationBtn = document.getElementById('confirmTerminationBtn');
            
            // Store original status to detect changes
            const originalStatus = employmentStatusSelect.value;
            
            // Function to show error message
            const showError = function(fieldId, show = true) {
                const field = document.getElementById(fieldId);
                const errorDiv = document.getElementById(fieldId + 'Error');
                
                if (show) {
                    field.classList.add('is-invalid');
                    if (errorDiv) errorDiv.style.display = 'block';
                } else {
                    field.classList.remove('is-invalid');
                    if (errorDiv) errorDiv.style.display = 'none';
                }
            };
            
            // Function to validate the form
            const validateForm = function() {
                let isValid = true;
                
                // Clear all previous errors
                document.querySelectorAll('.error-message').forEach(el => {
                    el.style.display = 'none';
                });
                
                document.querySelectorAll('.is-invalid').forEach(el => {
                    el.classList.remove('is-invalid');
                });
                
                // Check required fields (only those marked with required-field class)
                const firstName = document.getElementById('firstName').value.trim();
                if (!firstName) {
                    showError('firstName', true);
                    isValid = false;
                }

                const lastName = document.getElementById('lastName').value.trim();
                if (!lastName) {
                    showError('lastName', true);
                    isValid = false;
                }
                
                return isValid;
            };
            
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
                    return;
                }
                
                // Check if status is being changed to Terminated
                if (employmentStatusSelect.value === 'Terminated' && originalStatus !== 'Terminated') {
                    // Show termination confirmation modal
                    terminationModal.show();
                } else {
                    // Submit the form normally
                    form.submit();
                }
            });
            
            // Confirm termination button click
            confirmTerminationBtn.addEventListener('click', function() {
                // Set termination reason and date if needed (could be added to hidden fields)
                // const reason = document.getElementById('terminationReason').value;
                // const date = document.getElementById('terminationDate').value;
                
                // Close the modal
                terminationModal.hide();
                
                // Submit the form
                form.submit();
            });
            
            // Auto-dismiss alerts after 5 seconds
            setTimeout(function() {
                const alerts = document.querySelectorAll('.alert');
                alerts.forEach(function(alert) {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                });
            }, 5000);
        });
    </script>
</body>
</html>
