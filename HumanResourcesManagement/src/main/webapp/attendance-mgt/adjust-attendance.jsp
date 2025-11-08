<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Adjust Attendance Record - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    
    <!-- Custom Styles -->
    <style>
        .info-card {
            background-color: #f8f9fa;
            border-left: 4px solid #0d6efd;
            padding: 1rem;
            margin-bottom: 1.5rem;
        }
        .required-field::after {
            content: " *";
            color: red;
        }
        .badge-soft-warning {
            background-color: #fff6e5;
            color: #8a6d3b;
            border: 1px solid #ffe0b3;
        }
            /* Inline validation styling */
            .invalid-feedback {
                display: block; /* always reserve space when we inject text */
                font-style: italic;
                color: #dc3545; /* Bootstrap danger */
            }
            .is-invalid {
                border-color: #dc3545;
            }
    </style>
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="Adjust Attendance Record" />
        </jsp:include>

        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="#">Contracts & Attendance</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/attendance/summary">Attendance Summary</a></li>
                <li class="breadcrumb-item active">Adjust Record</li>
            </ol>
        </nav>

        <!-- Content Area -->
        <div class="content-area">
            <!-- Error Messages -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            
            <!-- Information Card -->
            <div class="info-card">
                <h5><i class="fas fa-info-circle me-2"></i>Manual Adjustment Notice</h5>
                <p class="mb-0">
                    This is an <strong>exception handling workflow</strong> for correcting attendance errors 
                    (e.g., forgot to check out, system glitch, business trip). All adjustments require a reason 
                    and will be tracked in the audit trail.
                </p>
            </div>

            <!-- Adjustment Form -->
            <div class="card">
                <div class="card-header">
                    <i class="fas fa-edit me-2"></i>Attendance Record Details
                </div>
                <div class="card-body">
                    <form method="POST" action="${pageContext.request.contextPath}/attendance/adjust" id="adjustmentForm" novalidate>
                        <input type="hidden" name="attendanceId" value="${record.attendanceID}">
                        
                        <!-- Employee Information (Read-only) -->
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Employee Code</label>
                                <input type="text" class="form-control" value="${record.employeeCode}" readonly>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Employee Name</label>
                                <input type="text" class="form-control" value="${record.employeeName}" readonly>
                            </div>
                        </div>
                        
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Attendance Date</label>
                                <input type="text" class="form-control" value="<fmt:formatDate value='${record.attendanceDate}' pattern='yyyy-MM-dd'/>" readonly>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label fw-bold">Current Status</label>
                                <div>
                                    <c:if test="${record.isManualAdjustment}">
                                        <span class="badge badge-soft-warning">
                                            <i class="fas fa-edit"></i> Previously Adjusted
                                        </span>
                                    </c:if>
                                    <c:if test="${!record.isManualAdjustment}">
                                        <span class="badge bg-light text-dark">Original Record</span>
                                    </c:if>
                                </div>
                            </div>
                        </div>

                        <hr class="my-4">

                        <!-- Editable Fields -->
                        <h5 class="mb-3">Adjust Attendance Data</h5>
                        
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="checkInTime" class="form-label">Check-in Time (24-hour format)</label>
                                <input type="time" class="form-control" id="checkInTime" name="checkInTime"
                                       value="<fmt:formatDate value='${record.checkInTime}' pattern='HH:mm'/>">
                                <div class="form-text">
                                    <i class="fas fa-info-circle"></i> Use 24-hour format: 08:00 for 8 AM, 20:00 for 8 PM. Leave empty if absent.
                                    <span id="checkInPreview" class="text-primary ms-2"></span>
                                </div>
                                <div class="invalid-feedback" id="checkInTimeError"></div>
                            </div>
                            <div class="col-md-6">
                                <label for="checkOutTime" class="form-label">Check-out Time (24-hour format)</label>
                                <input type="time" class="form-control" id="checkOutTime" name="checkOutTime"
                                       value="<fmt:formatDate value='${record.checkOutTime}' pattern='HH:mm'/>">
                                <div class="form-text">
                                    <i class="fas fa-info-circle"></i> Use 24-hour format: 17:00 for 5 PM, 18:00 for 6 PM. Leave empty if forgot to check out.
                                    <span id="checkOutPreview" class="text-primary ms-2"></span>
                                </div>
                                <div class="invalid-feedback" id="checkOutTimeError"></div>
                            </div>
                        </div>
                        
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label for="status" class="form-label required-field">Status</label>
                                <select class="form-select" id="status" name="status" required>
                                    <option value="Present" ${record.status == 'Present' ? 'selected' : ''}>Present</option>
                                    <option value="Absent" ${record.status == 'Absent' ? 'selected' : ''}>Absent</option>
                                    <option value="Late" ${record.status == 'Late' ? 'selected' : ''}>Late</option>
                                    <option value="Early Leave" ${record.status == 'Early Leave' ? 'selected' : ''}>Early Leave</option>
                                    <option value="Business Trip" ${record.status == 'Business Trip' ? 'selected' : ''}>Business Trip</option>
                                    <option value="Remote" ${record.status == 'Remote' ? 'selected' : ''}>Remote</option>
                                </select>
                                <div class="invalid-feedback" id="statusError"></div>
                            </div>
                            <div class="col-md-6">
                                <label for="overtimeHours" class="form-label">Overtime Hours</label>
                                <input type="number" class="form-control" id="overtimeHours" name="overtimeHours" 
                                       step="0.5" min="0" max="24" value="${record.overtimeHours}">
                                <div class="form-text">Enter 0 if no overtime</div>
                                <div class="invalid-feedback" id="overtimeHoursError"></div>
                            </div>
                        </div>
                        
                        <div class="mb-4">
                            <label for="adjustmentReason" class="form-label required-field">Adjustment Reason</label>
                            <textarea class="form-control" id="adjustmentReason" name="adjustmentReason" 
                                      rows="4" required placeholder="Explain why this adjustment is necessary (e.g., 'Employee forgot to check out', 'System glitch', 'Business trip - manual entry')"></textarea>
                            <div class="form-text">This reason will be recorded in the audit trail</div>
                            <div class="invalid-feedback" id="adjustmentReasonError"></div>
                        </div>

                        <!-- Previous Adjustment Info (if exists) -->
                        <c:if test="${record.isManualAdjustment && not empty record.adjustmentReason}">
                            <div class="alert alert-info">
                                <h6><i class="fas fa-history me-2"></i>Previous Adjustment</h6>
                                <p class="mb-1"><strong>Reason:</strong> ${record.adjustmentReason}</p>
                                <p class="mb-0">
                                    <strong>Adjusted:</strong> 
                                    <fmt:formatDate value="${record.adjustedAt}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                </p>
                            </div>
                        </c:if>

                        <!-- Action Buttons -->
                        <div class="d-flex justify-content-end gap-2 mt-4">
                            <a href="${pageContext.request.contextPath}/attendance/summary" class="btn btn-secondary">
                                <i class="fas fa-times me-1"></i>Cancel
                            </a>
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save me-1"></i>Save Adjustment
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        // Custom inline validation
        (function() {
            const form = document.getElementById('adjustmentForm');
            const statusEl = document.getElementById('status');
            const reasonEl = document.getElementById('adjustmentReason');
            const overtimeEl = document.getElementById('overtimeHours');
            const checkInEl = document.getElementById('checkInTime');
            const checkOutEl = document.getElementById('checkOutTime');

            // Function to convert 24-hour time to 12-hour format for preview
            function formatTime12Hour(time24) {
                if (!time24) return '';
                const [hours, minutes] = time24.split(':');
                const hour = parseInt(hours);
                const ampm = hour >= 12 ? 'PM' : 'AM';
                const hour12 = hour % 12 || 12;
                return `(${hour12}:${minutes} ${ampm})`;
            }

            // Update time preview when user changes time
            checkInEl.addEventListener('change', function() {
                document.getElementById('checkInPreview').textContent = formatTime12Hour(this.value);
            });

            checkOutEl.addEventListener('change', function() {
                document.getElementById('checkOutPreview').textContent = formatTime12Hour(this.value);
            });

            // Initialize previews on page load
            if (checkInEl.value) {
                document.getElementById('checkInPreview').textContent = formatTime12Hour(checkInEl.value);
            }
            if (checkOutEl.value) {
                document.getElementById('checkOutPreview').textContent = formatTime12Hour(checkOutEl.value);
            }

            function clearError(input, errorEl) {
                input.classList.remove('is-invalid');
                if (errorEl) errorEl.textContent = '';
            }

            function setError(input, errorEl, message) {
                input.classList.add('is-invalid');
                if (errorEl) errorEl.textContent = message;
            }

            function validate() {
                let valid = true;

                // Clear previous errors
                clearError(statusEl, document.getElementById('statusError'));
                clearError(reasonEl, document.getElementById('adjustmentReasonError'));
                clearError(overtimeEl, document.getElementById('overtimeHoursError'));
                clearError(checkInEl, document.getElementById('checkInTimeError'));
                clearError(checkOutEl, document.getElementById('checkOutTimeError'));

                // Status (required)
                if (!statusEl.value) {
                    setError(statusEl, document.getElementById('statusError'), 'Please choose a status.');
                    valid = false;
                }

                // Reason (required, min length 10)
                const reason = reasonEl.value.trim();
                if (!reason) {
                    setError(reasonEl, document.getElementById('adjustmentReasonError'), 'Please enter an adjustment reason.');
                    valid = false;
                } else if (reason.length < 10) {
                    setError(reasonEl, document.getElementById('adjustmentReasonError'), 'Reason must be at least 10 characters.');
                    valid = false;
                }

                // Overtime within range if provided
                if (overtimeEl.value !== '' && (Number(overtimeEl.value) < 0 || Number(overtimeEl.value) > 24)) {
                    setError(overtimeEl, document.getElementById('overtimeHoursError'), 'Overtime must be between 0 and 24 hours.');
                    valid = false;
                }

                // If both check-in and check-out provided, ensure logical order
                if (checkInEl.value && checkOutEl.value && checkInEl.value > checkOutEl.value) {
                    setError(checkInEl, document.getElementById('checkInTimeError'), 'Check-in cannot be later than check-out.');
                    setError(checkOutEl, document.getElementById('checkOutTimeError'), 'Check-out must be later than check-in.');
                    valid = false;
                }

                return valid;
            }

            form.addEventListener('submit', function(e) {
                if (!validate()) {
                    e.preventDefault();
                    const firstInvalid = form.querySelector('.is-invalid');
                    if (firstInvalid) firstInvalid.focus();
                }
            });

            // Live validation on blur
            [statusEl, reasonEl, overtimeEl, checkInEl, checkOutEl].forEach(function(el) {
                el.addEventListener('blur', validate);
            });
        })();

        // Auto-dismiss alerts after 5 seconds
        setTimeout(function() {
            var alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(alert) {
                var bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            });
        }, 5000);
    </script>
</body>
</html>

