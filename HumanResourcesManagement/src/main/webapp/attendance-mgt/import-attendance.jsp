<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Import Attendance Records - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
    
    <!-- Custom Styles for Buttons -->
    <style>
        .btn-import-attendance {
            padding-left: 2rem !important;
            padding-right: 2rem !important;
            white-space: nowrap !important;
        }
        
        .btn-clear {
            padding-left: 1.5rem !important;
            padding-right: 1.5rem !important;
            white-space: nowrap !important;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="sidebar-header">
            <c:choose>
                <c:when test="${sessionScope.user != null && sessionScope.user.role == 'HR Manager'}">
                    <h4>HR Manager Dashboard</h4>
                    <p>Human Resources</p>
                </c:when>
                <c:when test="${sessionScope.user != null && sessionScope.user.role == 'HR'}">
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
                <a href="${pageContext.request.contextPath}/${sessionScope.user != null && sessionScope.user.role == 'HR Manager' ? 'dashboard/hr-manager-dashboard.jsp' : 'dashboard/hr-dashboard.jsp'}">
                    <i class="fas fa-home"></i>
                    <span>Overview</span>
                </a>
            </li>

            <c:choose>
                <c:when test="${sessionScope.user != null && sessionScope.user.role == 'HR Manager'}">
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
                        <a href="${pageContext.request.contextPath}/attendance/import" class="active">
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
                        <a href="${pageContext.request.contextPath}/attendance/import" class="active">
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
            <h1>Import Attendance Records</h1>
            <div class="user-info">
                <span><c:out value="${sessionScope.user.roleDisplayName}" default="HR"/></span>
                <div class="avatar">HR</div>
            </div>
        </div>
        
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="#">Contracts & Attendance</a></li>
                <li class="breadcrumb-item active">Import Attendance</li>
            </ol>
        </nav>
        
        <!-- Content Area -->
        <div class="content-area">
            <!-- Success/Error Messages -->
            <c:if test="${not empty message}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>${message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            
            <!-- Instructions Card -->
            <div class="card mb-4">
                <div class="card-header">
                    <i class="fas fa-info-circle me-2"></i>Import Instructions
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-8">
                            <h5 class="card-title">How to Import Attendance Records</h5>
                            <ol class="mb-0">
                                <li><strong>Download Template:</strong> Use the template below to ensure correct format</li>
                                <li><strong>Fill Data:</strong> Complete the attendance data with the following columns:
                                    <ul class="mt-2">
                                        <li><strong>Employee Code</strong> - The unique employee code (required)</li>
                                        <li><strong>Attendance Date</strong> - Date in YYYY-MM-DD format (required)</li>
                                        <li><strong>Check-in Time</strong> - Time in HH:MM:SS format (optional)</li>
                                        <li><strong>Check-out Time</strong> - Time in HH:MM:SS format (optional)</li>
                                        <li><strong>Status</strong> - Present, Absent, Late, Early Leave, Business Trip, or Remote (default: Present)</li>
                                        <li><strong>Overtime Hours</strong> - Number of overtime hours (optional, default: 0)</li>
                                    </ul>
                                </li>
                                <li><strong>Save & Upload:</strong> Save as .xlsx or .xls format and upload using the form below</li>
                            </ol>
                        </div>
                        <div class="col-md-4 text-center">
                            <a href="${pageContext.request.contextPath}/templates/attendance-template.xlsx" 
                               class="btn btn-success btn-lg" download>
                                <i class="fas fa-download me-2"></i>Download Template
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Import Form Card -->
            <div class="card">
                <div class="card-header">
                    <i class="fas fa-upload me-2"></i>Upload Attendance File
                </div>
                <div class="card-body">
                    <form method="POST" enctype="multipart/form-data" id="importForm">
                        <div class="row">
                            <div class="col-md-8">
                                <div class="mb-0">
                                    <label for="attendanceFile" class="form-label">Select Excel File</label>
                                    <div class="d-flex align-items-center">
                                        <input type="file" class="form-control me-3" id="attendanceFile" name="attendanceFile" 
                                               accept=".xlsx,.xls" required onchange="updateFileName(this)">
                                        <div class="d-flex gap-2">
                                            <button type="submit" class="btn btn-primary btn-import-attendance">
                                                <i class="fas fa-upload me-1"></i>Import Attendance
                                            </button>
                                            <button type="reset" class="btn btn-secondary btn-clear">
                                                <i class="fas fa-times me-1"></i>Clear
                                            </button>
                                        </div>
                                    </div>
                                    <div class="form-text">Supported formats: .xlsx, .xls (Maximum file size: 10MB)</div>
                                </div>
                            </div>
                        </div>
                        <div id="fileName" class="text-muted mt-2"></div>
                    </form>
                </div>
            </div>
            
            <!-- Results Section -->
            <c:if test="${not empty successCount}">
                <div class="card mt-4">
                    <div class="card-header">
                        <i class="fas fa-chart-bar me-2"></i>Import Results
                    </div>
                    <div class="card-body">
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <div class="card bg-success text-white">
                                    <div class="card-body text-center">
                                        <h3 class="mb-0">${successCount}</h3>
                                        <small>Successful Records</small>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="card bg-danger text-white">
                                    <div class="card-body text-center">
                                        <h3 class="mb-0">${errorCount}</h3>
                                        <small>Failed Records</small>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
                        <c:if test="${not empty importBatchID}">
                            <div class="alert alert-info">
                                <strong>Batch ID:</strong> ${importBatchID}
                            </div>
                        </c:if>
                        
                        <!-- Success Messages -->
                        <c:if test="${not empty successMessages}">
                            <div class="mb-4">
                                <h5 class="text-success">
                                    <i class="fas fa-check-circle me-1"></i>Successful Imports
                                </h5>
                                <div class="table-responsive">
                                    <table class="table table-sm">
                                        <tbody>
                                            <c:forEach var="msg" items="${successMessages}">
                                                <tr class="table-success">
                                                    <td><i class="fas fa-check text-success me-2"></i>${msg}</td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </c:if>
                        
                        <!-- Error Messages -->
                        <c:if test="${not empty errorMessages}">
                            <div class="mb-4">
                                <h5 class="text-danger">
                                    <i class="fas fa-exclamation-circle me-1"></i>Failed Imports
                                </h5>
                                <div class="table-responsive">
                                    <table class="table table-sm">
                                        <tbody>
                                            <c:forEach var="msg" items="${errorMessages}">
                                                <tr class="table-danger">
                                                    <td><i class="fas fa-times text-danger me-2"></i>${msg}</td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </c:if>
                    </div>
                </div>
            </c:if>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Custom JavaScript -->
    <script>
        function updateFileName(input) {
            const fileName = document.getElementById('fileName');
            if (input.files && input.files[0]) {
                fileName.textContent = 'Selected: ' + input.files[0].name;
            } else {
                fileName.textContent = '';
            }
        }

        // Auto-hide alerts after 5 seconds
        document.addEventListener('DOMContentLoaded', function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(alert => {
                setTimeout(() => {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                }, 5000);
            });
        });
    </script>
</body>
</html>