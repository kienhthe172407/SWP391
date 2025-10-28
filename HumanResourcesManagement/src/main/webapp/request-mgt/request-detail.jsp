<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Request Details - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    
    <!-- Custom Styles -->
    <style>
        .detail-card {
            background-color: #fff;
            border: 1px solid #dee2e6;
            border-radius: 0.375rem;
            padding: 1.5rem;
            margin-bottom: 1.5rem;
        }
        .detail-row {
            padding: 0.75rem 0;
            border-bottom: 1px solid #f0f0f0;
        }
        .detail-row:last-child {
            border-bottom: none;
        }
        .detail-label {
            font-weight: 600;
            color: #495057;
            margin-bottom: 0.25rem;
        }
        .detail-value {
            color: #212529;
        }
        .status-badge-large {
            font-size: 1.1rem;
            padding: 0.5rem 1rem;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="sidebar-header">
            <h4>Employee Dashboard</h4>
            <p>Personal Portal</p>
        </div>

        <ul class="sidebar-menu">
            <li class="menu-section">Dashboard</li>
            <li>
                <a href="${pageContext.request.contextPath}/dashboard/employee-dashboard.jsp">
                    <i class="fas fa-home"></i>
                    <span>Overview</span>
                </a>
            </li>

            <li class="menu-section">Personal Information</li>
            <li>
                <a href="${pageContext.request.contextPath}/profile">
                    <i class="fas fa-user"></i>
                    <span>My Profile</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/auth/change-password.jsp">
                    <i class="fas fa-key"></i>
                    <span>Change Password</span>
                </a>
            </li>

            <li class="menu-section">Leave & Requests</li>
            <li>
                <a href="${pageContext.request.contextPath}/request/submit">
                    <i class="fas fa-file-alt"></i>
                    <span>Personal Requests</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/request/list" class="active">
                    <i class="fas fa-history"></i>
                    <span>Request History</span>
                </a>
            </li>

            <li class="menu-section">Tasks & Work</li>
            <li>
                <a href="#">
                    <i class="fas fa-tasks"></i>
                    <span>My Tasks</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-clipboard-check"></i>
                    <span>Task Status</span>
                </a>
            </li>

            <li class="menu-section">Attendance & Payroll</li>
            <li>
                <a href="#">
                    <i class="fas fa-clock"></i>
                    <span>Attendance Record</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-dollar-sign"></i>
                    <span>Payslip</span>
                </a>
            </li>
        </ul>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="top-header">
            <h1>Request Details</h1>
            <div class="user-info">
                <span><c:out value="${sessionScope.user.roleDisplayName}" default="Employee"/></span>
                <div class="dropdown">
                    <button class="btn dropdown-toggle avatar" type="button" id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                        EMP
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                        <li>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                <i class="fas fa-user me-2"></i>Profile
                            </a>
                        </li>
                        <li><hr class="dropdown-divider"></li>
                        <li>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/logout">
                                <i class="fas fa-sign-out-alt me-2"></i>Logout
                            </a>
                        </li>
                    </ul>
                </div>
            </div>
        </div>

        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard/employee-dashboard.jsp">Employee Dashboard</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/request/list">Request History</a></li>
                <li class="breadcrumb-item active">Request Details</li>
            </ol>
        </nav>

        <!-- Page Content -->
        <div class="content-area">
            <!-- Back Button -->
            <div class="mb-3">
                <c:choose>
                    <c:when test="${isOwnRequest}">
                        <a href="${pageContext.request.contextPath}/request/list" class="btn btn-secondary">
                            <i class="fas fa-arrow-left me-2"></i>Back to My Requests
                        </a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/request/manager/list" class="btn btn-secondary">
                            <i class="fas fa-arrow-left me-2"></i>Back to Team Requests
                        </a>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- Request Header -->
            <div class="card mb-4">
                <div class="card-header bg-primary text-white">
                    <div class="d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">
                            <i class="fas fa-file-alt me-2 text-white"></i>
                            Request #${request.requestID}
                        </h5>
                        <span class="badge ${request.statusBadgeClass} status-badge-large">
                            ${request.requestStatus}
                        </span>
                    </div>
                </div>
                <div class="card-body">
                    <!-- Employee Information -->
                    <div class="detail-card">
                        <h6 class="text-primary mb-3">
                            <i class="fas fa-user me-2"></i>Employee Information
                        </h6>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="detail-row">
                                    <div class="detail-label">Employee Name</div>
                                    <div class="detail-value">${request.employeeName}</div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="detail-row">
                                    <div class="detail-label">Employee Code</div>
                                    <div class="detail-value">${request.employeeCode}</div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="detail-row">
                                    <div class="detail-label">Department</div>
                                    <div class="detail-value">${request.departmentName}</div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="detail-row">
                                    <div class="detail-label">Position</div>
                                    <div class="detail-value">${requestEmployee.positionName}</div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Request Details -->
                    <div class="detail-card">
                        <h6 class="text-primary mb-3">
                            <i class="fas fa-info-circle me-2"></i>Request Details
                        </h6>
                        <div class="row">
                            <div class="col-md-6">
                                <div class="detail-row">
                                    <div class="detail-label">Request Type</div>
                                    <div class="detail-value">
                                        <strong>${request.requestTypeName}</strong>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="detail-row">
                                    <div class="detail-label">Number of Days</div>
                                    <div class="detail-value">${request.numberOfDays} days</div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="detail-row">
                                    <div class="detail-label">Start Date</div>
                                    <div class="detail-value">
                                        <fmt:formatDate value="${request.startDate}" pattern="EEEE, dd MMMM yyyy"/>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="detail-row">
                                    <div class="detail-label">End Date</div>
                                    <div class="detail-value">
                                        <fmt:formatDate value="${request.endDate}" pattern="EEEE, dd MMMM yyyy"/>
                                    </div>
                                </div>
                            </div>
                            <div class="col-12">
                                <div class="detail-row">
                                    <div class="detail-label">Reason</div>
                                    <div class="detail-value">
                                        <p class="mb-0">${request.reason}</p>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="detail-row">
                                    <div class="detail-label">Submitted On</div>
                                    <div class="detail-value">
                                        <fmt:formatDate value="${request.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="detail-row">
                                    <div class="detail-label">Last Updated</div>
                                    <div class="detail-value">
                                        <fmt:formatDate value="${request.updatedAt}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Review Information (if reviewed) -->
                    <c:if test="${request.isApproved() || request.isRejected()}">
                        <div class="detail-card">
                            <h6 class="text-primary mb-3">
                                <i class="fas fa-clipboard-check me-2"></i>Review Information
                            </h6>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="detail-row">
                                        <div class="detail-label">Reviewed By</div>
                                        <div class="detail-value">${request.reviewerName}</div>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="detail-row">
                                        <div class="detail-label">Reviewed On</div>
                                        <div class="detail-value">
                                            <fmt:formatDate value="${request.reviewedAt}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                        </div>
                                    </div>
                                </div>
                                <c:if test="${not empty request.reviewComment}">
                                    <div class="col-12">
                                        <div class="detail-row">
                                            <div class="detail-label">Review Comment</div>
                                            <div class="detail-value">
                                                <div class="alert ${request.isApproved() ? 'alert-success' : 'alert-danger'} mb-0">
                                                    ${request.reviewComment}
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </c:if>

                    <!-- Cancellation Information (if cancelled) -->
                    <c:if test="${request.isCancelled()}">
                        <div class="detail-card">
                            <h6 class="text-secondary mb-3">
                                <i class="fas fa-ban me-2"></i>Cancellation Information
                            </h6>
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="detail-row">
                                        <div class="detail-label">Cancelled On</div>
                                        <div class="detail-value">
                                            <fmt:formatDate value="${request.cancelledAt}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:if>

                    <!-- Action Buttons -->
                    <c:if test="${canApproveReject}">
                        <div class="mt-4 d-flex gap-2">
                            <button type="button" class="btn btn-success" onclick="approveRequest(${request.requestID})">
                                <i class="fas fa-check me-2"></i>Approve Request
                            </button>
                            <button type="button" class="btn btn-danger" onclick="rejectRequest(${request.requestID})">
                                <i class="fas fa-times me-2"></i>Reject Request
                            </button>
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>

    <!-- Approve Modal -->
    <div class="modal fade" id="approveModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-success text-white">
                    <h5 class="modal-title">Approve Request</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <form method="POST" action="${pageContext.request.contextPath}/request/approve-reject">
                    <div class="modal-body">
                        <input type="hidden" name="requestId" value="${request.requestID}">
                        <input type="hidden" name="action" value="approve">
                        <p>Are you sure you want to approve this request?</p>
                        <div class="mb-3">
                            <label for="approveComment" class="form-label">Comment (Optional)</label>
                            <textarea class="form-control" id="approveComment" name="reviewComment" rows="3" 
                                      placeholder="Add any comments or notes..."></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-success">
                            <i class="fas fa-check me-2"></i>Approve Request
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Reject Modal -->
    <div class="modal fade" id="rejectModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title">Reject Request</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <form method="POST" action="${pageContext.request.contextPath}/request/approve-reject">
                    <div class="modal-body">
                        <input type="hidden" name="requestId" value="${request.requestID}">
                        <input type="hidden" name="action" value="reject">
                        <p>Are you sure you want to reject this request?</p>
                        <div class="mb-3">
                            <label for="rejectComment" class="form-label">Reason for Rejection <span class="text-danger">*</span></label>
                            <textarea class="form-control" id="rejectComment" name="reviewComment" rows="3" 
                                      required placeholder="Please provide a reason for rejection..."></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-times me-2"></i>Reject Request
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JavaScript -->
    <script>
        // Approve request
        function approveRequest(requestId) {
            const approveModal = new bootstrap.Modal(document.getElementById('approveModal'));
            approveModal.show();
        }

        // Reject request
        function rejectRequest(requestId) {
            const rejectModal = new bootstrap.Modal(document.getElementById('rejectModal'));
            rejectModal.show();
        }
    </script>
</body>
</html>

