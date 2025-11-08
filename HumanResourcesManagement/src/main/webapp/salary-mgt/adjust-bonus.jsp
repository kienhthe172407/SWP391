<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Adjust Bonus - HR Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    <style>
        .info-card {
            background-color: #f8f9fa;
            border-left: 4px solid #0d6efd;
            padding: 15px;
            margin-bottom: 20px;
        }
        .adjustment-history {
            max-height: 400px;
            overflow-y: auto;
        }
        .adjustment-item {
            border-left: 3px solid #dee2e6;
            padding-left: 15px;
            margin-bottom: 15px;
        }
        .adjustment-item.approved {
            border-left-color: #198754;
        }
        .adjustment-item.rejected {
            border-left-color: #dc3545;
        }
        .adjustment-item.pending {
            border-left-color: #ffc107;
        }
    </style>
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="Adjust Bonus" />
        </jsp:include>

        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/salary/view-summary">Salary Summary</a></li>
                <li class="breadcrumb-item active">Adjust Bonus</li>
            </ol>
        </nav>

        <!-- Content Area -->
        <div class="content-area">
            <div class="row">
                <!-- Left Column: Form -->
                <div class="col-md-7">
                    <!-- Employee Info Card -->
                    <div class="card mb-4">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0"><i class="fas fa-user me-2"></i>Employee Information</h5>
                        </div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <p><strong>Employee Code:</strong> ${employee.employeeCode}</p>
                                    <p><strong>Name:</strong> ${employee.firstName} ${employee.lastName}</p>
                                </div>
                                <div class="col-md-6">
                                    <p><strong>Department:</strong> ${employee.departmentName}</p>
                                    <p><strong>Position:</strong> ${employee.positionName}</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Current Salary Info -->
                    <c:if test="${not empty payroll}">
                        <div class="info-card">
                            <h6 class="mb-3"><i class="fas fa-info-circle me-2"></i>Current Salary for ${year}-${String.format("%02d", month)}</h6>
                            <div class="row">
                                <div class="col-md-6">
                                    <p class="mb-2"><strong>Base Salary:</strong> <fmt:formatNumber value="${payroll.baseSalary}" type="currency" currencySymbol="$"/></p>
                                    <p class="mb-2"><strong>Total Allowances:</strong> <fmt:formatNumber value="${payroll.totalAllowances}" type="currency" currencySymbol="$"/></p>
                                    <p class="mb-2"><strong>Overtime Pay:</strong> <fmt:formatNumber value="${payroll.overtimePay}" type="currency" currencySymbol="$"/></p>
                                </div>
                                <div class="col-md-6">
                                    <p class="mb-2"><strong>Current Bonus:</strong> <fmt:formatNumber value="${payroll.totalBonus}" type="currency" currencySymbol="$"/></p>
                                    <p class="mb-2"><strong>Gross Salary:</strong> <fmt:formatNumber value="${payroll.grossSalary}" type="currency" currencySymbol="$"/></p>
                                    <p class="mb-2"><strong>Net Salary:</strong> <fmt:formatNumber value="${payroll.netSalary}" type="currency" currencySymbol="$"/></p>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <!-- Adjustment Form -->
                    <div class="card">
                        <div class="card-header bg-success text-white">
                            <h5 class="mb-0"><i class="fas fa-edit me-2"></i>New Bonus Adjustment</h5>
                        </div>
                        <div class="card-body">
                            <form method="POST" action="${pageContext.request.contextPath}/salary/adjust-bonus" id="adjustmentForm">
                                <input type="hidden" name="employeeId" value="${employee.employeeID}">
                                <input type="hidden" name="year" value="${year}">
                                <input type="hidden" name="month" value="${month}">
                                
                                <div class="mb-3">
                                    <label for="adjustmentType" class="form-label">Adjustment Type <span class="text-danger">*</span></label>
                                    <select class="form-select" id="adjustmentType" name="adjustmentType" required>
                                        <option value="">Select Type</option>
                                        <option value="Bonus">Bonus</option>
                                        <option value="Deduction">Deduction</option>
                                        <option value="Retroactive">Retroactive</option>
                                        <option value="Correction">Correction</option>
                                    </select>
                                </div>

                                <div class="mb-3">
                                    <label for="amount" class="form-label">Adjustment Amount ($) <span class="text-danger">*</span></label>
                                    <input type="number" step="0.01" class="form-control" id="amount"
                                           name="amount" required placeholder="Enter amount (use negative for deduction)">
                                    <small class="form-text text-muted">
                                        Enter positive value to add bonus, negative value to deduct
                                    </small>
                                </div>
                                
                                <div class="mb-3">
                                    <label for="reason" class="form-label">Reason <span class="text-danger">*</span></label>
                                    <textarea class="form-control" id="reason" name="reason" rows="4" required 
                                              placeholder="Provide detailed reason for this adjustment"></textarea>
                                    <small class="form-text text-muted">
                                        This reason will be recorded in the audit trail
                                    </small>
                                </div>
                                
                                <div class="alert alert-info">
                                    <i class="fas fa-info-circle me-2"></i>
                                    <c:choose>
                                        <c:when test="${sessionScope.user.role == 'HR Manager' || sessionScope.user.role == 'HR_MANAGER'}">
                                            <strong>HR Manager:</strong> Your adjustment will be approved automatically and payroll will be updated immediately.
                                        </c:when>
                                        <c:otherwise>
                                            <strong>HR Staff:</strong> Your adjustment will be pending until approved by HR Manager.
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                
                                <div class="d-flex gap-2">
                                    <button type="submit" class="btn btn-success">
                                        <i class="fas fa-check me-2"></i>Submit Adjustment
                                    </button>
                                    <a href="${pageContext.request.contextPath}/salary/view-summary?year=${year}&month=${month}" 
                                       class="btn btn-secondary">
                                        <i class="fas fa-times me-2"></i>Cancel
                                    </a>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>

                <!-- Right Column: History -->
                <div class="col-md-5">
                    <div class="card">
                        <div class="card-header bg-info text-white">
                            <h5 class="mb-0"><i class="fas fa-history me-2"></i>Adjustment History</h5>
                        </div>
                        <div class="card-body adjustment-history">
                            <c:choose>
                                <c:when test="${empty existingAdjustments}">
                                    <div class="alert alert-info">
                                        <i class="fas fa-info-circle me-2"></i>
                                        No previous adjustments for this period.
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="adj" items="${existingAdjustments}">
                                        <div class="adjustment-item ${adj.status.toLowerCase()}">
                                            <div class="d-flex justify-content-between align-items-start mb-2">
                                                <div>
                                                    <h6 class="mb-1">${adj.adjustmentType}</h6>
                                                    <small class="text-muted">
                                                        <fmt:formatDate value="${adj.createdAt}" pattern="yyyy-MM-dd HH:mm"/>
                                                    </small>
                                                </div>
                                                <div>
                                                    <c:choose>
                                                        <c:when test="${adj.status == 'Approved'}">
                                                            <span class="badge bg-success">Approved</span>
                                                        </c:when>
                                                        <c:when test="${adj.status == 'Rejected'}">
                                                            <span class="badge bg-danger">Rejected</span>
                                                        </c:when>
                                                        <c:when test="${adj.status == 'Pending'}">
                                                            <span class="badge bg-warning">Pending</span>
                                                            <c:if test="${sessionScope.user != null && (sessionScope.user.role == 'HR Manager' || sessionScope.user.role == 'HR_MANAGER')}">
                                                                <br/>
                                                                <div class="btn-group btn-group-sm mt-1" role="group">
                                                                    <button type="button" class="btn btn-success btn-sm approve-btn"
                                                                            data-adjustment-id="${adj.adjustmentId}"
                                                                            data-employee-name="${employee.firstName} ${employee.lastName}"
                                                                            data-amount="${adj.amount}"
                                                                            title="Approve Adjustment">
                                                                        <i class="fas fa-check"></i> Approve
                                                                    </button>
                                                                    <button type="button" class="btn btn-danger btn-sm reject-btn"
                                                                            data-adjustment-id="${adj.adjustmentId}"
                                                                            data-employee-name="${employee.firstName} ${employee.lastName}"
                                                                            data-amount="${adj.amount}"
                                                                            title="Reject Adjustment">
                                                                        <i class="fas fa-times"></i> Reject
                                                                    </button>
                                                                </div>
                                                            </c:if>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="badge bg-secondary">${adj.status}</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>
                                            <p class="mb-2">
                                                <strong>Amount:</strong>
                                                <span class="${adj.amount < 0 ? 'text-danger' : 'text-success'}">
                                                    <fmt:formatNumber value="${adj.amount}" type="currency" currencySymbol="$"/>
                                                </span>
                                            </p>
                                            <p class="mb-2"><strong>Reason:</strong> ${adj.reason}</p>
                                            <c:if test="${not empty adj.approvalComment}">
                                                <p class="mb-0"><small><strong>Approval Comment:</strong> ${adj.approvalComment}</small></p>
                                            </c:if>
                                        </div>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Approve Adjustment Modal -->
    <div class="modal fade" id="approveModal" tabindex="-1" aria-labelledby="approveModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="approveModalLabel">Approve Bonus Adjustment</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form id="approveForm" method="POST" action="${pageContext.request.contextPath}/salary/adjust-bonus">
                    <div class="modal-body">
                        <p>Are you sure you want to approve the bonus adjustment for <strong id="approveEmployeeName"></strong>?</p>
                        <p>Amount: <strong id="approveAmount" class="text-success"></strong></p>
                        <div class="mb-3">
                            <label for="approveComment" class="form-label">Approval Comment (Optional)</label>
                            <textarea class="form-control" id="approveComment" name="approvalComment" rows="3"
                                      placeholder="Enter any comments about the approval..."></textarea>
                        </div>
                        <input type="hidden" id="approveAdjustmentId" name="adjustmentId" value="">
                        <input type="hidden" name="action" value="approve">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-success">
                            <i class="fas fa-check me-1"></i>Approve Adjustment
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Reject Adjustment Modal -->
    <div class="modal fade" id="rejectModal" tabindex="-1" aria-labelledby="rejectModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="rejectModalLabel">Reject Bonus Adjustment</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form id="rejectForm" method="POST" action="${pageContext.request.contextPath}/salary/adjust-bonus">
                    <div class="modal-body">
                        <p>Are you sure you want to reject the bonus adjustment for <strong id="rejectEmployeeName"></strong>?</p>
                        <p>Amount: <strong id="rejectAmount"></strong></p>
                        <div class="mb-3">
                            <label for="rejectComment" class="form-label">Rejection Reason <span class="text-danger">*</span></label>
                            <textarea class="form-control" id="rejectComment" name="approvalComment" rows="3"
                                      placeholder="Please provide a reason for rejecting this adjustment..." required></textarea>
                            <div class="invalid-feedback">
                                Please provide a reason for rejection.
                            </div>
                        </div>
                        <input type="hidden" id="rejectAdjustmentId" name="adjustmentId" value="">
                        <input type="hidden" name="action" value="reject">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-times me-1"></i>Reject Adjustment
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Submit Adjustment Confirmation Modal -->
    <div class="modal fade" id="submitConfirmModal" tabindex="-1" aria-labelledby="submitConfirmModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-success text-white">
                    <h5 class="modal-title" id="submitConfirmModalLabel">
                        <i class="fas fa-check-circle me-2"></i>Confirm Bonus Adjustment
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="alert alert-info">
                        <i class="fas fa-info-circle me-2"></i>
                        <strong>Please review your adjustment details before submitting:</strong>
                    </div>
                    
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <strong>Employee:</strong><br>
                            <span id="confirmEmployeeCode"></span><br>
                            <small class="text-muted" id="confirmEmployeeName"></small>
                        </div>
                        <div class="col-md-6">
                            <strong>Period:</strong><br>
                            <span id="confirmPeriod"></span>
                        </div>
                    </div>
                    
                    <div class="row mb-3">
                        <div class="col-md-6">
                            <strong>Adjustment Type:</strong><br>
                            <span id="confirmAdjustmentType" class="badge bg-primary"></span>
                        </div>
                        <div class="col-md-6">
                            <strong>Amount:</strong><br>
                            <span id="confirmAmount" class="h5"></span>
                        </div>
                    </div>
                    
                    <div class="mb-3">
                        <strong>Reason:</strong><br>
                        <div class="border rounded p-2 bg-light">
                            <span id="confirmReason"></span>
                        </div>
                    </div>
                    
                    <div class="alert alert-warning">
                        <i class="fas fa-exclamation-triangle me-2"></i>
                        <c:choose>
                            <c:when test="${sessionScope.user.role == 'HR Manager' || sessionScope.user.role == 'HR_MANAGER'}">
                                <strong>HR Manager:</strong> This adjustment will be approved automatically and payroll will be updated immediately.
                            </c:when>
                            <c:otherwise>
                                <strong>HR Staff:</strong> This adjustment will be pending until approved by HR Manager.
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="fas fa-times me-1"></i>Cancel
                    </button>
                    <button type="button" class="btn btn-success" id="confirmSubmitBtn">
                        <i class="fas fa-check me-1"></i>Submit Adjustment
                    </button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Form validation and custom confirmation
        document.getElementById('adjustmentForm').addEventListener('submit', function(e) {
            e.preventDefault(); // Prevent default form submission
            
            const amount = parseFloat(document.getElementById('amount').value);
            const reason = document.getElementById('reason').value.trim();
            const adjustmentType = document.getElementById('adjustmentType').value;

            // Validation
            if (isNaN(amount) || amount === 0) {
                alert('Please enter a valid non-zero amount');
                return false;
            }

            if (reason.length < 10) {
                alert('Please provide a detailed reason (at least 10 characters)');
                return false;
            }

            if (!adjustmentType) {
                alert('Please select an adjustment type');
                return false;
            }

            // Populate confirmation modal
            document.getElementById('confirmEmployeeCode').textContent = '${employee.employeeCode}';
            document.getElementById('confirmEmployeeName').textContent = '${employee.firstName} ${employee.lastName}';
            document.getElementById('confirmPeriod').textContent = '${year}-${String.format("%02d", month)}';
            document.getElementById('confirmAdjustmentType').textContent = adjustmentType;
            
            // Format amount with proper styling
            const amountElement = document.getElementById('confirmAmount');
            if (amount < 0) {
                amountElement.innerHTML = '<span class="text-danger">$' + Math.abs(amount).toFixed(2) + '</span>';
            } else {
                amountElement.innerHTML = '<span class="text-success">+$' + amount.toFixed(2) + '</span>';
            }
            
            document.getElementById('confirmReason').textContent = reason;

            // Show confirmation modal
            const confirmModal = new bootstrap.Modal(document.getElementById('submitConfirmModal'));
            confirmModal.show();
        });

        // Handle actual form submission after confirmation
        document.getElementById('confirmSubmitBtn').addEventListener('click', function() {
            // Close the modal
            const confirmModal = bootstrap.Modal.getInstance(document.getElementById('submitConfirmModal'));
            confirmModal.hide();
            
            // Submit the form
            document.getElementById('adjustmentForm').submit();
        });

        // Approve/Reject button handlers
        document.addEventListener('DOMContentLoaded', function() {
            // Approve button handlers
            document.querySelectorAll('.approve-btn').forEach(function(btn) {
                btn.addEventListener('click', function() {
                    const adjustmentId = this.getAttribute('data-adjustment-id');
                    const employeeName = this.getAttribute('data-employee-name');
                    const amount = this.getAttribute('data-amount');

                    document.getElementById('approveAdjustmentId').value = adjustmentId;
                    document.getElementById('approveEmployeeName').textContent = employeeName;
                    document.getElementById('approveAmount').textContent = '$' + parseFloat(amount).toFixed(2);

                    const approveModal = new bootstrap.Modal(document.getElementById('approveModal'));
                    approveModal.show();
                });
            });

            // Reject button handlers
            document.querySelectorAll('.reject-btn').forEach(function(btn) {
                btn.addEventListener('click', function() {
                    const adjustmentId = this.getAttribute('data-adjustment-id');
                    const employeeName = this.getAttribute('data-employee-name');
                    const amount = this.getAttribute('data-amount');

                    document.getElementById('rejectAdjustmentId').value = adjustmentId;
                    document.getElementById('rejectEmployeeName').textContent = employeeName;
                    document.getElementById('rejectAmount').textContent = '$' + parseFloat(amount).toFixed(2);

                    const rejectModal = new bootstrap.Modal(document.getElementById('rejectModal'));
                    rejectModal.show();
                });
            });

            // Form validation for reject form
            document.getElementById('rejectForm').addEventListener('submit', function(e) {
                const comment = document.getElementById('rejectComment').value.trim();
                if (!comment) {
                    e.preventDefault();
                    document.getElementById('rejectComment').classList.add('is-invalid');
                } else {
                    document.getElementById('rejectComment').classList.remove('is-invalid');
                }
            });
        });
    </script>
</body>
</html>

