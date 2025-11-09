<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Attendance Exception Requests - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    
    <style>
        .info-card {
            background-color: #f8f9fa;
            border-left: 4px solid #0d6efd;
            padding: 1rem;
            margin-bottom: 1.5rem;
        }
        .badge-warning {
            background-color: #ffc107;
            color: #000;
        }
        .badge-success {
            background-color: #198754;
        }
        .badge-danger {
            background-color: #dc3545;
        }
        .request-row:hover {
            background-color: #f8f9fa;
        }
        .filter-card {
            background-color: #fff;
            border: 1px solid #dee2e6;
            border-radius: 0.375rem;
            padding: 1rem;
            margin-bottom: 1.5rem;
        }
        .comparison-badge {
            font-size: 0.75rem;
            padding: 0.25rem 0.5rem;
        }
    </style>
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="${isManagerView ? 'Team Attendance Exception Requests' : 'My Attendance Exception Requests'}" />
        </jsp:include>

        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item">
                    <a href="${pageContext.request.contextPath}/dashboard/${isManagerView ? 'dept-manager' : 'employee'}">
                        ${isManagerView ? 'Dept Manager' : 'Employee'} Dashboard
                    </a>
                </li>
                <li class="breadcrumb-item active">Attendance Exception Requests</li>
            </ol>
        </nav>

        <!-- Page Content -->
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

            <!-- Statistics Cards -->
            <div class="row mb-4">
                <div class="col-md-3">
                    <div class="card text-center">
                        <div class="card-body">
                            <h6 class="text-muted">Total Requests</h6>
                            <h3>${totalRequests}</h3>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card text-center border-warning">
                        <div class="card-body">
                            <h6 class="text-warning">Pending</h6>
                            <h3>${pendingCount}</h3>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card text-center border-success">
                        <div class="card-body">
                            <h6 class="text-success">Approved</h6>
                            <h3>${approvedCount}</h3>
                        </div>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="card text-center border-danger">
                        <div class="card-body">
                            <h6 class="text-danger">Rejected</h6>
                            <h3>${rejectedCount}</h3>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Action Buttons -->
            <c:if test="${!isManagerView}">
                <div class="mb-3">
                    <a href="${pageContext.request.contextPath}/attendance/exception/submit" class="btn btn-primary">
                        <i class="fas fa-plus me-2"></i>Submit New Exception Request
                    </a>
                </div>
            </c:if>

            <!-- Search and Filter Section -->
            <c:if test="${isManagerView}">
                <div class="filter-card">
                    <h6 class="mb-3">
                        <i class="fas fa-search me-2"></i>Search & Filter Requests
                    </h6>
                    <form method="GET" action="${pageContext.request.contextPath}/attendance/exception/list" class="row g-3">
                        <div class="col-md-4">
                            <label for="employeeNameFilter" class="form-label">Employee Name</label>
                            <input type="text" class="form-control" id="employeeNameFilter" name="employeeName"
                                   value="${employeeNameFilter}" placeholder="Search by name...">
                        </div>
                        <div class="col-md-4">
                            <label for="statusFilter" class="form-label">Status</label>
                            <select class="form-select" id="statusFilter" name="status">
                                <option value="All" ${statusFilter == null || statusFilter == 'All' ? 'selected' : ''}>All Statuses</option>
                                <option value="Pending" ${statusFilter == 'Pending' ? 'selected' : ''}>Pending</option>
                                <option value="Approved" ${statusFilter == 'Approved' ? 'selected' : ''}>Approved</option>
                                <option value="Rejected" ${statusFilter == 'Rejected' ? 'selected' : ''}>Rejected</option>
                            </select>
                        </div>
                        <div class="col-md-4 d-flex align-items-end">
                            <button type="submit" class="btn btn-primary me-2">
                                <i class="fas fa-search me-2"></i>Search
                            </button>
                            <a href="${pageContext.request.contextPath}/attendance/exception/list" class="btn btn-secondary">
                                <i class="fas fa-redo me-2"></i>Clear Filters
                            </a>
                        </div>
                    </form>
                </div>
            </c:if>

            <!-- Requests Table -->
            <div class="card">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0">
                        <i class="fas fa-clipboard-list me-2 text-white"></i>
                        ${isManagerView ? 'Team Attendance Exception Requests' : 'My Attendance Exception Requests'}
                    </h5>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty requests}">
                            <div class="text-center py-5">
                                <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                                <p class="text-muted">No attendance exception requests found.</p>
                                <c:if test="${!isManagerView}">
                                    <a href="${pageContext.request.contextPath}/attendance/exception/submit" class="btn btn-primary mt-3">
                                        <i class="fas fa-plus me-2"></i>Submit Your First Request
                                    </a>
                                </c:if>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Request ID</th>
                                            <c:if test="${isManagerView}">
                                                <th>Employee</th>
                                            </c:if>
                                            <th>Attendance Date</th>
                                            <th>Current Status</th>
                                            <th>Request Reason</th>
                                            <th>Proposed Changes</th>
                                            <th>Status</th>
                                            <th>Submitted</th>
                                            <c:if test="${isManagerView}">
                                                <th>Actions</th>
                                            </c:if>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="req" items="${requests}">
                                            <tr class="request-row">
                                                <td>#${req.requestID}</td>
                                                <c:if test="${isManagerView}">
                                                    <td>
                                                        <strong>${req.employeeName}</strong><br>
                                                        <small class="text-muted">${req.employeeCode}</small>
                                                    </td>
                                                </c:if>
                                                <td>
                                                    <fmt:formatDate value="${req.attendanceDate}" pattern="dd/MM/yyyy"/>
                                                </td>
                                                <td>
                                                    <span class="badge bg-info">${req.currentStatus}</span>
                                                    <c:if test="${not empty req.currentCheckIn}">
                                                        <br><small class="text-muted">
                                                            <fmt:formatDate value="${req.currentCheckIn}" pattern="HH:mm" type="time"/> - 
                                                            <c:choose>
                                                                <c:when test="${not empty req.currentCheckOut}">
                                                                    <fmt:formatDate value="${req.currentCheckOut}" pattern="HH:mm" type="time"/>
                                                                </c:when>
                                                                <c:otherwise>N/A</c:otherwise>
                                                            </c:choose>
                                                        </small>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <small>${req.requestReason}</small>
                                                </td>
                                                <td>
                                                    <c:if test="${not empty req.proposedStatus || not empty req.proposedCheckIn || not empty req.proposedCheckOut}">
                                                        <c:if test="${not empty req.proposedStatus}">
                                                            <span class="badge bg-success comparison-badge">Status: ${req.proposedStatus}</span>
                                                        </c:if>
                                                        <c:if test="${not empty req.proposedCheckIn}">
                                                            <br><small class="text-muted">
                                                                Check-in: <fmt:formatDate value="${req.proposedCheckIn}" pattern="HH:mm" type="time"/>
                                                            </small>
                                                        </c:if>
                                                        <c:if test="${not empty req.proposedCheckOut}">
                                                            <br><small class="text-muted">
                                                                Check-out: <fmt:formatDate value="${req.proposedCheckOut}" pattern="HH:mm" type="time"/>
                                                            </small>
                                                        </c:if>
                                                    </c:if>
                                                    <c:if test="${empty req.proposedStatus && empty req.proposedCheckIn && empty req.proposedCheckOut}">
                                                        <small class="text-muted">Explanation only</small>
                                                    </c:if>
                                                </td>
                                                <td>
                                                    <span class="badge ${req.statusBadgeClass}">
                                                        ${req.status}
                                                    </span>
                                                </td>
                                                <td>
                                                    <fmt:formatDate value="${req.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                                </td>
                                                <c:if test="${isManagerView}">
                                                    <td>
                                                        <div class="btn-group btn-group-sm" role="group">
                                                            <c:if test="${req.isPending()}">
                                                                <button type="button" class="btn btn-outline-success" 
                                                                        onclick="approveRequest(${req.requestID})" title="Approve">
                                                                    <i class="fas fa-check"></i>
                                                                </button>
                                                                <button type="button" class="btn btn-outline-danger" 
                                                                        onclick="rejectRequest(${req.requestID})" title="Reject">
                                                                    <i class="fas fa-times"></i>
                                                                </button>
                                                            </c:if>
                                                            <c:if test="${!req.isPending()}">
                                                                <span class="text-muted small">${req.status}</span>
                                                            </c:if>
                                                        </div>
                                                    </td>
                                                </c:if>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <!-- Approve Modal -->
    <div class="modal fade" id="approveModal" tabindex="-1">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header bg-success text-white">
                    <h5 class="modal-title">Approve Attendance Exception Request</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <form id="approveForm" method="POST" action="${pageContext.request.contextPath}/attendance/exception/approve-reject">
                    <div class="modal-body">
                        <input type="hidden" name="requestId" id="approveRequestId">
                        <input type="hidden" name="action" value="approve">
                        <p>Are you sure you want to approve this attendance exception request?</p>
                        <p class="text-muted small">The attendance record will be updated with the proposed changes (if any).</p>
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
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title">Reject Attendance Exception Request</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <form id="rejectForm" method="POST" action="${pageContext.request.contextPath}/attendance/exception/approve-reject">
                    <div class="modal-body">
                        <input type="hidden" name="requestId" id="rejectRequestId">
                        <input type="hidden" name="action" value="reject">
                        <p>Are you sure you want to reject this attendance exception request?</p>
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
            document.getElementById('approveRequestId').value = requestId;
            const approveModal = new bootstrap.Modal(document.getElementById('approveModal'));
            approveModal.show();
        }

        // Reject request
        function rejectRequest(requestId) {
            document.getElementById('rejectRequestId').value = requestId;
            const rejectModal = new bootstrap.Modal(document.getElementById('rejectModal'));
            rejectModal.show();
        }
    </script>
</body>
</html>

