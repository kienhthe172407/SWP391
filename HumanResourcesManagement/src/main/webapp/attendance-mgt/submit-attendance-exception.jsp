<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Submit Attendance Exception Request - HR Management System</title>
    
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
        }
        
        .card-header {
            background-color: #0d6efd;
            color: white;
        }
        
        .info-card {
            background-color: #f8f9fa;
            border-left: 4px solid #0d6efd;
            padding: 1rem;
            margin-bottom: 1.5rem;
        }
        
        .attendance-info {
            background-color: #fff3cd;
            border-left: 4px solid #ffc107;
            padding: 1rem;
            margin-bottom: 1.5rem;
        }
        
        /* Ensure icon in submit button is white - override all possible rules */
        .card-body button.btn-primary i,
        .card-body button.btn-primary:hover i,
        .card-body button.btn-primary:focus i,
        .card-body button.btn-primary:active i,
        .card-body button[type="submit"].btn-primary i,
        .card-body button[type="submit"].btn-primary:hover i,
        .card-body button[type="submit"].btn-primary:focus i,
        .card-body button[type="submit"].btn-primary:active i,
        button.btn-primary i,
        button.btn-primary:hover i,
        button.btn-primary:focus i,
        button.btn-primary:active i,
        button[type="submit"].btn-primary i,
        button[type="submit"].btn-primary:hover i,
        button[type="submit"].btn-primary:focus i,
        button[type="submit"].btn-primary:active i {
            color: #fff !important;
        }
        
    </style>
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="Submit Attendance Exception Request" />
        </jsp:include>

        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard/employee">Employee Dashboard</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/attendance/exception/list">My Exception Requests</a></li>
                <li class="breadcrumb-item active">Submit Exception Request</li>
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

            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    ${sessionScope.errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="errorMessage" scope="session"/>
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

            <!-- Attendance Record Info -->
            <c:if test="${not empty attendanceRecord}">
                <div class="attendance-info">
                    <h6><i class="fas fa-info-circle me-2"></i>Current Attendance Record</h6>
                    <div class="row mt-2">
                        <div class="col-md-3">
                            <strong>Date:</strong> 
                            <fmt:formatDate value="${attendanceRecord.attendanceDate}" pattern="dd/MM/yyyy"/>
                        </div>
                        <div class="col-md-3">
                            <strong>Check-in:</strong> 
                            <c:choose>
                                <c:when test="${not empty attendanceRecord.checkInTime}">
                                    <fmt:formatDate value="${attendanceRecord.checkInTime}" pattern="HH:mm" type="time"/>
                                </c:when>
                                <c:otherwise>N/A</c:otherwise>
                            </c:choose>
                        </div>
                        <div class="col-md-3">
                            <strong>Check-out:</strong> 
                            <c:choose>
                                <c:when test="${not empty attendanceRecord.checkOutTime}">
                                    <fmt:formatDate value="${attendanceRecord.checkOutTime}" pattern="HH:mm" type="time"/>
                                </c:when>
                                <c:otherwise>N/A</c:otherwise>
                            </c:choose>
                        </div>
                        <div class="col-md-3">
                            <strong>Status:</strong> 
                            <span class="badge bg-info">${attendanceRecord.status}</span>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Submit Exception Request Form -->
            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0">
                        <i class="fas fa-file-alt me-2"></i>
                        Submit Attendance Exception Request
                    </h5>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty attendanceRecord}">
                            <div class="alert alert-warning">
                                <i class="fas fa-exclamation-triangle me-2"></i>
                                <strong>No attendance record selected.</strong> Please select an attendance record from the Attendance Record page to submit an exception request.
                            </div>
                            <a href="${pageContext.request.contextPath}/attendance/summary" class="btn btn-primary">
                                <i class="fas fa-arrow-left me-2"></i>Go to Attendance Record
                            </a>
                        </c:when>
                        <c:otherwise>
                    <form method="POST" action="${pageContext.request.contextPath}/attendance/exception/submit" id="exceptionRequestForm">
                        <input type="hidden" name="attendanceId" value="${attendanceRecord.attendanceID}">
                        
                        <div class="form-section">
                            <h5>Request Details</h5>
                            
                            <div class="mb-3">
                                <label for="requestReason" class="form-label required-field">Request Reason</label>
                                <textarea class="form-control" id="requestReason" name="requestReason" rows="4" 
                                          placeholder="Please explain why you need to correct this attendance record (e.g., 'I was on a client visit', 'I forgot to check in', etc.)" 
                                          required></textarea>
                                <div class="error-message" id="requestReasonError">Request reason is required.</div>
                            </div>
                        </div>

                        <div class="form-section">
                            <h5>Proposed Changes (Optional)</h5>
                            <p class="text-muted small">If you want to correct specific times or status, fill in the fields below. Leave blank if you only want to explain the attendance.</p>
                            
                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label for="proposedCheckIn" class="form-label">Proposed Check-in Time</label>
                                    <input type="time" class="form-control" id="proposedCheckIn" name="proposedCheckIn" 
                                           placeholder="HH:MM">
                                    <small class="text-muted">Format: HH:MM (e.g., 08:30)</small>
                                </div>
                                
                                <div class="col-md-4 mb-3">
                                    <label for="proposedCheckOut" class="form-label">Proposed Check-out Time</label>
                                    <input type="time" class="form-control" id="proposedCheckOut" name="proposedCheckOut" 
                                           placeholder="HH:MM">
                                    <small class="text-muted">Format: HH:MM (e.g., 17:00)</small>
                                </div>
                                
                                <div class="col-md-4 mb-3">
                                    <label for="proposedStatus" class="form-label">Proposed Status</label>
                                    <select class="form-select" id="proposedStatus" name="proposedStatus">
                                        <option value="">-- Keep Current Status --</option>
                                        <option value="Present">Present</option>
                                        <option value="Absent">Absent</option>
                                        <option value="Late">Late</option>
                                        <option value="Early Leave">Early Leave</option>
                                        <option value="Business Trip">Business Trip</option>
                                        <option value="Remote">Remote</option>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="d-flex justify-content-between">
                            <a href="${pageContext.request.contextPath}/attendance/exception/list" class="btn btn-secondary">
                                <i class="fas fa-arrow-left me-2"></i>Cancel
                            </a>
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-file-alt me-2" style="color: #fff !important;"></i>Submit Attendance Exception Request
                            </button>
                        </div>
                    </form>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JavaScript -->
    <script>
        // Form validation
        document.getElementById('exceptionRequestForm').addEventListener('submit', function(e) {
            const requestReason = document.getElementById('requestReason').value.trim();
            const requestReasonError = document.getElementById('requestReasonError');
            
            if (!requestReason) {
                e.preventDefault();
                requestReasonError.classList.add('show');
                document.getElementById('requestReason').classList.add('is-invalid');
                return false;
            } else {
                requestReasonError.classList.remove('show');
                document.getElementById('requestReason').classList.remove('is-invalid');
            }
        });
    </script>
</body>
</html>

