<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Request - HR Management System</title>
    
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
            font-size: 0.85rem;
            font-style: italic;
            margin-top: 0.25rem;
            display: none;
        }
        
        .error-message.show {
            display: block !important;
            color: #dc3545 !important;
        }
        
        /* Force display when inline style is set */
        .error-message[style*="block"] {
            display: block !important;
            color: #dc3545 !important;
        }
        
        .is-invalid {
            border-color: #dc3545;
        }
        
        .card-header {
            background-color: #0d6efd;
            color: white;
        }
        
        .form-control:focus, .form-select:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 0.2rem rgba(102, 126, 234, 0.25);
        }
        
        .request-type-card {
            border: 1px solid #dee2e6;
            border-radius: 0.375rem;
            padding: 0.75rem;
            margin-bottom: 0.5rem;
            cursor: pointer;
            transition: all 0.2s;
        }
        .request-type-card:hover {
            background-color: #f8f9fa;
            border-color: #0d6efd;
        }
        .request-type-card.selected {
            background-color: #e7f1ff;
            border-color: #0d6efd;
            border-width: 2px;
        }
        .request-type-info {
            font-size: 0.875rem;
            color: #6c757d;
        }
        
        .info-card {
            background-color: #f8f9fa;
            border-left: 4px solid #0d6efd;
            padding: 1rem;
            margin-bottom: 1.5rem;
        }
    </style>
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="Edit Request" />
        </jsp:include>

        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard/employee-dashboard.jsp">Employee Dashboard</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/request/list">Request History</a></li>
                <li class="breadcrumb-item active">Edit Request</li>
            </ol>
        </nav>

        <!-- Content Area -->
        <div class="content-area">
            <!-- Success/Error Messages -->
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>
                    ${sessionScope.successMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="successMessage" scope="session"/>
            </c:if>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    ${errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Employee Info Card -->
            <div class="info-card">
                <div class="row">
                    <div class="col-md-6">
                        <strong>Employee:</strong> ${employee.firstName} ${employee.lastName} (${employee.employeeCode})
                    </div>
                    <div class="col-md-6">
                        <strong>Department:</strong> ${employee.departmentName}
                    </div>
                </div>
            </div>

            <!-- Edit Request Form -->
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0"><i class="fas fa-edit text-white"></i> Edit Request Details</h5>
                </div>
                <div class="card-body">
                    <form method="POST" action="${pageContext.request.contextPath}/request/edit" id="editRequestForm">
                        <input type="hidden" name="requestId" value="${request.requestID}">
                        
                        <!-- Request Type Selection Section -->
                        <div class="form-section">
                            <h5><i class="fas fa-list-check text-primary"></i> Request Type</h5>
                            <div id="requestTypeContainer">
                                <c:forEach var="requestType" items="${requestTypes}">
                                    <div class="request-type-card" data-type-id="${requestType.requestTypeID}">
                                        <div class="form-check">
                                            <input class="form-check-input" type="radio" 
                                                   name="requestTypeId" 
                                                   id="requestType${requestType.requestTypeID}" 
                                                   value="${requestType.requestTypeID}"
                                                   ${request.requestTypeID == requestType.requestTypeID ? 'checked' : ''}>
                                            <label class="form-check-label w-100" for="requestType${requestType.requestTypeID}">
                                                <strong>${requestType.requestTypeName}</strong>
                                                <div class="request-type-info">
                                                    ${requestType.description}
                                                    <c:if test="${requestType.maxDaysPerYear != null}">
                                                        <span class="badge bg-info ms-2">Max: ${requestType.maxDaysPerYear} days/year</span>
                                                    </c:if>
                                                    <span class="badge ${requestType.paid ? 'bg-success' : 'bg-warning'} ms-2">
                                                        ${requestType.paid ? 'Paid' : 'Unpaid'}
                                                    </span>
                                                </div>
                                            </label>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                            <div id="requestTypeIdError" class="error-message">Please select a request type</div>
                        </div>

                        <!-- Date Information Section -->
                        <div class="form-section">
                            <h5><i class="fas fa-calendar-alt text-primary"></i> Date Information</h5>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="startDate" class="form-label required-field">Start Date</label>
                                        <input type="date" class="form-control" id="startDate" name="startDate" 
                                               value="<fmt:formatDate value='${request.startDate}' pattern='yyyy-MM-dd'/>" required>
                                        <div id="startDateError" class="error-message">Please select a start date</div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="endDate" class="form-label required-field">End Date</label>
                                        <input type="date" class="form-control" id="endDate" name="endDate" 
                                               value="<fmt:formatDate value='${request.endDate}' pattern='yyyy-MM-dd'/>" required>
                                        <div id="endDateError" class="error-message">Please select an end date</div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="mb-3">
                                        <label for="numberOfDays" class="form-label required-field">Number of Days</label>
                                        <input type="number" class="form-control" id="numberOfDays" name="numberOfDays" 
                                               step="0.5" min="0.5" value="${request.numberOfDays}" required>
                                        <div class="form-text">
                                            You can enter 0.5 for half day. The system will calculate automatically based on start and end dates.
                                        </div>
                                        <div id="numberOfDaysError" class="error-message">Please enter a valid number of days</div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Request Details Section -->
                        <div class="form-section">
                            <h5><i class="fas fa-file-alt text-primary"></i> Request Details</h5>
                            <div class="mb-3">
                                <label for="reason" class="form-label required-field">Reason</label>
                                <textarea class="form-control" id="reason" name="reason" rows="4" 
                                          required placeholder="Please provide a detailed reason for your request...">${request.reason}</textarea>
                                <div class="form-text">
                                    Minimum 10 characters required.
                                </div>
                                <div id="reasonError" class="error-message">Please provide a detailed reason (minimum 10 characters)</div>
                            </div>
                        </div>

                        <!-- Form Actions -->
                        <div class="d-flex justify-content-end gap-2 mt-4">
                            <a href="${pageContext.request.contextPath}/request/list" class="btn btn-secondary">
                                <i class="fas fa-times me-1"></i>Cancel
                            </a>
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save me-1"></i>Update Request
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Form JavaScript -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Auto-hide success messages after 3 seconds
            const successAlerts = document.querySelectorAll('.alert-success');
            successAlerts.forEach(function(alert) {
                setTimeout(function() {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                }, 3000);
            });

            // Request type card selection
            document.querySelectorAll('.request-type-card').forEach(card => {
                card.addEventListener('click', function() {
                    document.querySelectorAll('.request-type-card').forEach(c => c.classList.remove('selected'));
                    this.classList.add('selected');
                    this.querySelector('input[type="radio"]').checked = true;
                    
                    // Clear error when selection is made
                    const errorElement = document.getElementById('requestTypeIdError');
                    if (errorElement) {
                        errorElement.classList.remove('show');
                        errorElement.style.display = 'none';
                        errorElement.style.color = '';
                    }
                });
            });

            // Auto-calculate number of days based on date range
            const startDateInput = document.getElementById('startDate');
            const endDateInput = document.getElementById('endDate');
            const numberOfDaysInput = document.getElementById('numberOfDays');

            function calculateDays() {
                const startDate = new Date(startDateInput.value);
                const endDate = new Date(endDateInput.value);
                
                if (startDate && endDate && endDate >= startDate) {
                    const diffTime = Math.abs(endDate - startDate);
                    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1; // +1 to include both start and end dates
                    numberOfDaysInput.value = diffDays;
                }
            }

            startDateInput.addEventListener('change', calculateDays);
            endDateInput.addEventListener('change', calculateDays);

            // Set minimum date to today
            const today = new Date().toISOString().split('T')[0];
            startDateInput.setAttribute('min', today);
            endDateInput.setAttribute('min', today);

            // Form validation
            const form = document.getElementById('editRequestForm');
            
            // Function to show error message
            const showError = function(fieldId, show = true) {
                const field = document.getElementById(fieldId);
                const errorDiv = document.getElementById(fieldId + 'Error');
                
                if (show) {
                    if (field) field.classList.add('is-invalid');
                    if (errorDiv) {
                        errorDiv.classList.add('show');
                        errorDiv.style.display = 'block';
                        errorDiv.style.color = '#dc3545';
                    }
                } else {
                    if (field) field.classList.remove('is-invalid');
                    if (errorDiv) {
                        errorDiv.classList.remove('show');
                        errorDiv.style.display = 'none';
                    }
                }
            };
            
            // Function to validate the form
            const validateForm = function() {
                let isValid = true;
                
                // Clear all previous errors
                document.querySelectorAll('.error-message').forEach(el => {
                    el.classList.remove('show');
                    el.style.display = 'none';
                    el.style.color = '';
                });
                
                document.querySelectorAll('.is-invalid').forEach(el => {
                    el.classList.remove('is-invalid');
                });
                
                // Check if request type is selected
                const requestTypeSelected = document.querySelector('input[name="requestTypeId"]:checked');
                if (!requestTypeSelected) {
                    const errorElement = document.getElementById('requestTypeIdError');
                    if (errorElement) {
                        errorElement.classList.add('show');
                        errorElement.style.display = 'block';
                    }
                    isValid = false;
                }
                
                // Check if start date is provided
                const startDate = document.getElementById('startDate').value;
                if (!startDate) {
                    showError('startDate', true);
                    isValid = false;
                }
                
                // Check if end date is provided
                const endDate = document.getElementById('endDate').value;
                if (!endDate) {
                    showError('endDate', true);
                    isValid = false;
                }
                
                // Validate date range
                if (startDate && endDate && new Date(endDate) < new Date(startDate)) {
                    showError('endDate', true);
                    isValid = false;
                }
                
                // Check if number of days is valid
                const numberOfDays = document.getElementById('numberOfDays').value;
                if (!numberOfDays || parseFloat(numberOfDays) <= 0) {
                    showError('numberOfDays', true);
                    isValid = false;
                }
                
                // Check if reason is provided and meets minimum length
                const reason = document.getElementById('reason').value.trim();
                if (!reason || reason.length < 10) {
                    showError('reason', true);
                    isValid = false;
                }
                
                return isValid;
            };
            
            // Add event listeners to clear errors when input changes
            document.querySelectorAll('input, textarea').forEach(element => {
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
            
            // Disable browser default validation
            form.setAttribute('novalidate', 'novalidate');
            
            // Form submission handler
            form.addEventListener('submit', function(e) {
                e.preventDefault(); // Always prevent default to use custom validation
                
                if (validateForm()) {
                    // If validation passes, submit the form programmatically
                    form.submit();
                } else {
                    // Scroll to first error
                    const firstError = document.querySelector('.is-invalid');
                    if (firstError) {
                        firstError.scrollIntoView({ behavior: 'smooth', block: 'center' });
                        firstError.focus();
                    }
                }
            });
        });
    </script>
</body>
</html>

