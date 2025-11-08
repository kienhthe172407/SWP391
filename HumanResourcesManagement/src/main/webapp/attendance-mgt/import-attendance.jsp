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
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    
    <!-- Custom Styles for Buttons and soft badges -->
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

        /* Soft badge tones (gentle colors) */
        .badge-soft-success { background-color: #e9f7ef; color: #1e7e34; border: 1px solid #c7ead7; }
        .badge-soft-danger  { background-color: #fdecea; color: #a71d2a; border: 1px solid #f5c6cb; }
        .badge-soft-warning { background-color: #fff6e5; color: #8a6d3b; border: 1px solid #ffe0b3; }
        .badge-soft-primary { background-color: #e8f0fe; color: #1a4fb8; border: 1px solid #c9dbff; }
        .badge-soft-info    { background-color: #e6f7fb; color: #0c5460; border: 1px solid #bde5f1; }

        /* Leave request indicator styles */
        .leave-indicator {
            display: inline-flex;
            align-items: center;
            gap: 0.5rem;
            padding: 0.25rem 0.5rem;
            border-radius: 0.25rem;
            font-size: 0.875rem;
        }

        .leave-indicator.match {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }

        .leave-indicator.conflict {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }

        .leave-indicator.warning {
            background-color: #fff3cd;
            color: #856404;
            border: 1px solid #ffeaa7;
        }

        .leave-indicator.info {
            background-color: #d1ecf1;
            color: #0c5460;
            border: 1px solid #bee5eb;
        }

        .leave-indicator.normal {
            background-color: #e2e3e5;
            color: #383d41;
            border: 1px solid #d6d8db;
        }

        /* Attendance Summary: outline by default, fill on hover/active */
        .btn-attn-summary {
            --bs-btn-color: #fd7e14;
            --bs-btn-bg: transparent;
            --bs-btn-border-color: #fd7e14;
            --bs-btn-hover-color: #fff;
            --bs-btn-hover-bg: #e56f0f;
            --bs-btn-hover-border-color: #e56f0f;
            --bs-btn-active-color: #fff;
            --bs-btn-active-bg: #cf610d;
            --bs-btn-active-border-color: #cf610d;
            --bs-btn-disabled-color: #fd7e14;
            --bs-btn-disabled-bg: transparent;
            --bs-btn-disabled-border-color: #fd7e14;
            --bs-btn-focus-shadow-rgb: 253,126,20;
        }
    </style>
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="Import Attendance Records" />
        </jsp:include>
        
        <!-- Breadcrumb + View Attendance Summary button -->
        <div class="d-flex justify-content-between align-items-center mb-3">
            <nav aria-label="breadcrumb" class="mb-0">
                <ol class="breadcrumb mb-0">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Dashboard</a></li>
                    <li class="breadcrumb-item"><a href="#">Contracts & Attendance</a></li>
                    <li class="breadcrumb-item active">Import Attendance</li>
                </ol>
            </nav>
            <div class="mt-3 me-3 d-flex gap-2">
                <a href="${pageContext.request.contextPath}/attendance/summary" class="btn btn-attn-summary">
                    <i class="fas fa-chart-line me-2"></i>Attendance Summary
                </a>
                <a href="${pageContext.request.contextPath}/attendance/monthly-report" class="btn btn-outline-primary">
                    <i class="fas fa-calendar-alt me-2"></i>Monthly Report
                </a>
            </div>
        </div>
        
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
            
            <!-- Instructions Card (hidden when previewData exists) -->
            <c:if test="${empty previewData}">
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
                            <a href="${pageContext.request.contextPath}/attendance/download-template"
                               class="btn btn-success btn-lg">
                                <i class="fas fa-download me-2"></i>Download Template
                            </a>
                        </div>
                    </div>
        </div>
            </div>
        </c:if>
        
            <!-- Quick Actions removed; button moved next to breadcrumb -->

            <!-- Import Form Card -->
            <div class="card">
                <div class="card-header">
                    <i class="fas fa-upload me-2"></i>Upload Attendance File
                </div>
                <div class="card-body">
                    <form method="POST" enctype="multipart/form-data" id="importForm">
                        <div class="row">
                            <div class="col-12">
                                <div class="mb-0">
                                    <label for="attendanceFile" class="form-label">Select Excel File</label>
                                    <div class="d-flex align-items-center w-100">
                                        <input type="file" class="form-control me-3 flex-grow-1" id="attendanceFile" name="attendanceFile"
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
        
            <!-- Preview Table Section -->
            <c:if test="${not empty previewData}">
                <div class="card mt-4">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <span><i class="fas fa-table me-2"></i>Preview Imported Data</span>
                        <span class="badge bg-primary">${previewData.totalRecords} records</span>
                    </div>
                    <div class="card-body">

                        <c:if test="${not empty previewData.importBatchID}">
                            <div class="alert alert-info">
                                <strong>Batch ID:</strong> ${previewData.importBatchID}
                            </div>
                        </c:if>

                        <!-- Leave Request Integration Summary -->
                        <div class="row mb-4">
                            <div class="col-md-3">
                                <div class="card border-success">
                                    <div class="card-body text-center">
                                        <i class="fas fa-check-circle text-success fa-2x mb-2"></i>
                                        <h5 class="card-title">${previewData.recordsMatchingLeave}</h5>
                                        <p class="card-text text-muted small mb-0">Matching Approved Leave</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card border-warning">
                                    <div class="card-body text-center">
                                        <i class="fas fa-exclamation-triangle text-warning fa-2x mb-2"></i>
                                        <h5 class="card-title">${previewData.recordsWithConflict}</h5>
                                        <p class="card-text text-muted small mb-0">Conflicts (Leave vs Present)</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card border-danger">
                                    <div class="card-body text-center">
                                        <i class="fas fa-user-times text-danger fa-2x mb-2"></i>
                                        <h5 class="card-title">${previewData.absentWithoutLeave}</h5>
                                        <p class="card-text text-muted small mb-0">Absent Without Leave</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="card border-primary">
                                    <div class="card-body text-center">
                                        <i class="fas fa-check text-success fa-2x mb-2"></i>
                                        <h5 class="card-title">${previewData.validRecords}</h5>
                                        <p class="card-text text-muted small mb-0">Valid Records</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Legend -->
                        <div class="alert alert-light border mb-3">
                            <strong><i class="fas fa-info-circle me-2"></i>Legend:</strong>
                            <div class="d-flex flex-wrap gap-3 mt-2">
                                <span class="leave-indicator match">
                                    <i class="fas fa-check-circle"></i> Matches Approved Leave
                                </span>
                                <span class="leave-indicator conflict">
                                    <i class="fas fa-exclamation-triangle"></i> Conflict
                                </span>
                                <span class="leave-indicator warning">
                                    <i class="fas fa-exclamation-circle"></i> Absent Without Leave
                                </span>
                                <span class="leave-indicator info">
                                    <i class="fas fa-info-circle"></i> Info
                                </span>
                                <span class="leave-indicator normal">
                                    <i class="fas fa-circle"></i> Normal
                                </span>
                            </div>
                        </div>

                        <!-- Preview Table -->
                        <div class="table-responsive">
                            <table class="table table-striped table-hover preview-table">
                                <thead class="table-dark">
                                    <tr>
                                        <th class="text-nowrap" style="width: 4%;">Row</th>
                                        <th class="text-nowrap" style="width: 8%;">Employee Code</th>
                                        <th class="text-nowrap" style="width: 10%;">Employee Name</th>
                                        <th class="text-nowrap" style="width: 8%;">Date</th>
                                        <th class="text-nowrap" style="width: 7%;">Check-in</th>
                                        <th class="text-nowrap" style="width: 7%;">Check-out</th>
                                        <th class="text-nowrap" style="width: 8%;">Status</th>
                                        <th class="text-nowrap" style="width: 18%;">Leave Request Info</th>
                                        <th class="text-nowrap" style="width: 6%;">Overtime</th>
                                        <th class="text-nowrap" style="width: 8%;">Validation</th>
                                        <th class="text-nowrap" style="width: 10%;">Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="previewRecord" items="${previewData.records}">
                                        <tr class="${previewRecord.valid ? '' : 'table-danger'}">
                                            <td>${previewRecord.rowNumber}</td>
                                            <td>
                                                <strong>${previewRecord.employeeCode}</strong>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty previewRecord.employeeName}">
                                                        ${previewRecord.employeeName}
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted">N/A</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:if test="${previewRecord.valid && previewRecord.record != null}">
                                                    <fmt:formatDate value="${previewRecord.record.attendanceDate}" pattern="yyyy-MM-dd"/>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${previewRecord.valid && previewRecord.record != null && previewRecord.record.checkInTime != null}">
                                                    <fmt:formatDate value="${previewRecord.record.checkInTime}" pattern="HH:mm:ss"/>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${previewRecord.valid && previewRecord.record != null && previewRecord.record.checkOutTime != null}">
                                                    <fmt:formatDate value="${previewRecord.record.checkOutTime}" pattern="HH:mm:ss"/>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:if test="${previewRecord.valid && previewRecord.record != null}">
                                                    <c:choose>
                                                        <c:when test="${previewRecord.record.status == 'Present'}">
                                                            <span class="badge badge-soft-success">Present</span>
                                                        </c:when>
                                                        <c:when test="${previewRecord.record.status == 'Absent'}">
                                                            <span class="badge badge-soft-danger">Absent</span>
                                                        </c:when>
                                                        <c:when test="${previewRecord.record.status == 'Late'}">
                                                            <span class="badge badge-soft-warning">Late</span>
                                                        </c:when>
                                                        <c:when test="${previewRecord.record.status == 'Early Leave'}">
                                                            <span class="badge badge-soft-warning">Early Leave</span>
                                                        </c:when>
                                                        <c:when test="${previewRecord.record.status == 'Business Trip'}">
                                                            <span class="badge badge-soft-info">Business Trip</span>
                                                        </c:when>
                                                        <c:when test="${previewRecord.record.status == 'Remote'}">
                                                            <span class="badge badge-soft-primary">Remote</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-light text-secondary">${previewRecord.record.status}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </c:if>
                                            </td>
                                            <td>
                                                <!-- Leave Request Info Column -->
                                                <c:choose>
                                                    <c:when test="${previewRecord.hasApprovedLeave}">
                                                        <div class="leave-indicator ${previewRecord.statusIndicator}">
                                                            <c:choose>
                                                                <c:when test="${previewRecord.statusIndicator == 'match'}">
                                                                    <i class="fas fa-check-circle"></i>
                                                                </c:when>
                                                                <c:when test="${previewRecord.statusIndicator == 'conflict'}">
                                                                    <i class="fas fa-exclamation-triangle"></i>
                                                                </c:when>
                                                                <c:when test="${previewRecord.statusIndicator == 'warning'}">
                                                                    <i class="fas fa-exclamation-circle"></i>
                                                                </c:when>
                                                                <c:when test="${previewRecord.statusIndicator == 'info'}">
                                                                    <i class="fas fa-info-circle"></i>
                                                                </c:when>
                                                            </c:choose>
                                                            <div>
                                                                <small class="text-success">Approved Request</small>
                                                                <c:if test="${previewRecord.autoFilled}">
                                                                    <br><small class="text-muted"><i class="fas fa-magic"></i> Auto-filled</small>
                                                                </c:if>
                                                            </div>
                                                        </div>
                                                        <div class="mt-1">
                                                            <small class="text-muted">${previewRecord.statusMessage}</small>
                                                        </div>
                                                    </c:when>
                                                    <c:when test="${previewRecord.statusIndicator == 'warning'}">
                                                        <div class="leave-indicator warning">
                                                            <i class="fas fa-exclamation-circle"></i>
                                                            <div>
                                                                <small>${previewRecord.statusMessage}</small>
                                                            </div>
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="leave-indicator normal">
                                                            <i class="fas fa-circle"></i>
                                                            <small>No leave request</small>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:if test="${previewRecord.valid && previewRecord.record != null}">
                                                    ${previewRecord.record.overtimeHours} hrs
                                                </c:if>
                                            </td>
                                            <td class="text-center">
                                                <c:choose>
                                                    <c:when test="${previewRecord.valid}">
                                                        <i class="fas fa-check-circle text-success" style="font-size: 1.2rem;" title="Valid"></i>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <i class="fas fa-times-circle text-danger" style="font-size: 1.2rem;" title="Invalid"></i>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <c:if test="${previewRecord.valid}">
                                                    <span class="badge bg-success">
                                                        <i class="fas fa-plus"></i> Insert
                                                    </span>
                                                </c:if>
                                                <c:if test="${!previewRecord.valid && previewRecord.errorMessage.contains('Duplicate')}">
                                                    <span class="badge bg-danger">
                                                        <i class="fas fa-exclamation-triangle"></i> Duplicate
                                                    </span>
                                                </c:if>
                                            </td>
                                            
                                        </tr>
                                        <c:if test="${!previewRecord.valid}">
                                            <tr class="table-danger">
                                                <td colspan="10">
                                                    <small class="text-danger">
                                                        <i class="fas fa-exclamation-triangle me-1"></i>
                                                        <strong>Error:</strong> ${previewRecord.errorMessage}
                                                    </small>
                                                </td>
                                            </tr>
                                        </c:if>
                        </c:forEach>
                                </tbody>
                            </table>
                        </div>

                        <!-- Action Buttons -->
                        <div class="d-flex justify-content-end gap-2 mt-4">
                            <a href="${pageContext.request.contextPath}/attendance/import" class="btn btn-secondary">
                                <i class="fas fa-times me-1"></i>Cancel
                            </a>
                            <c:if test="${previewData.validRecords > 0}">
                                <form method="POST" action="${pageContext.request.contextPath}/attendance/save" style="display: inline;">
                                    <button type="submit" class="btn btn-success btn-lg">
                                        <i class="fas fa-save me-1"></i>Save
                                    </button>
                                </form>
                            </c:if>
                        </div>
                    </div>
            </div>
        </c:if>

            <!-- After save: only show alerts (message/error) already rendered above -->
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