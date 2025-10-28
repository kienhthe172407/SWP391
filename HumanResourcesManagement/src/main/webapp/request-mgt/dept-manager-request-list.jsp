<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Team Requests - HR Management System</title>
    
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
        .badge-secondary {
            background-color: #6c757d;
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
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="sidebar-header">
            <h4>Dept Manager Dashboard</h4>
            <p>Department Management</p>
        </div>

        <ul class="sidebar-menu">
            <li class="menu-section">Dashboard</li>
            <li>
                <a href="${pageContext.request.contextPath}/dashboard/dept-manager-dashboard.jsp">
                    <i class="fas fa-home"></i>
                    <span>Overview</span>
                </a>
            </li>

            <li class="menu-section">Team Management</li>
            <li>
                <a href="#">
                    <i class="fas fa-users"></i>
                    <span>My Team</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-user-plus"></i>
                    <span>Team Members</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-chart-bar"></i>
                    <span>Team Performance</span>
                </a>
            </li>

            <li class="menu-section">Approval & Requests</li>
            <li>
                <a href="#">
                    <i class="fas fa-clipboard-check"></i>
                    <span>Approval Queue</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/request/manager/list" class="active">
                    <i class="fas fa-file-alt"></i>
                    <span>Employee Requests</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-history"></i>
                    <span>Approval History</span>
                </a>
            </li>

            <li class="menu-section">Task Management</li>
            <li>
                <a href="#">
                    <i class="fas fa-tasks"></i>
                    <span>Assign Tasks</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-clipboard-list"></i>
                    <span>Task Board</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-chart-line"></i>
                    <span>Task Statistics</span>
                </a>
            </li>

            <li class="menu-section">Reports & Analytics</li>
            <li>
                <a href="#">
                    <i class="fas fa-chart-pie"></i>
                    <span>Department Reports</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-clock"></i>
                    <span>Attendance Reports</span>
                </a>
            </li>
        </ul>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="top-header">
            <h1>Team Requests</h1>
            <div class="user-info">
                <span><c:out value="${sessionScope.user.roleDisplayName}" default="Dept Manager"/></span>
                <div class="dropdown">
                    <button class="btn dropdown-toggle avatar" type="button" id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
                        DM
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="userDropdown">
                        <li>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/profile">
                                <i class="fas fa-user me-2"></i>Profile
                            </a>
                        </li>
                        <li>
                            <a class="dropdown-item" href="${pageContext.request.contextPath}/auth/change-password.jsp">
                                <i class="fas fa-key me-2"></i>Change Password
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
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard/dept-manager-dashboard.jsp">Dept Manager Dashboard</a></li>
                <li class="breadcrumb-item active">Team Requests</li>
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

            <!-- Search and Filter Section -->
            <div class="filter-card">
                <h6 class="mb-3">
                    <i class="fas fa-search me-2"></i>Search & Filter Requests
                </h6>
                <form method="GET" action="${pageContext.request.contextPath}/request/manager/list" class="row g-3">
                    <div class="col-md-3">
                        <label for="employeeNameFilter" class="form-label">Employee Name</label>
                        <input type="text" class="form-control" id="employeeNameFilter" name="employeeName"
                               value="${employeeNameFilter}" placeholder="Search by name...">
                    </div>
                    <div class="col-md-3">
                        <label for="statusFilter" class="form-label">Status</label>
                        <select class="form-select" id="statusFilter" name="status">
                            <option value="All" ${statusFilter == null || statusFilter == 'All' ? 'selected' : ''}>All Statuses</option>
                            <option value="Pending" ${statusFilter == 'Pending' ? 'selected' : ''}>Pending</option>
                            <option value="Approved" ${statusFilter == 'Approved' ? 'selected' : ''}>Approved</option>
                            <option value="Rejected" ${statusFilter == 'Rejected' ? 'selected' : ''}>Rejected</option>
                            <option value="Cancelled" ${statusFilter == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <label for="requestTypeFilter" class="form-label">Request Type</label>
                        <select class="form-select" id="requestTypeFilter" name="requestType">
                            <option value="All" ${requestTypeFilter == null || requestTypeFilter == 'All' ? 'selected' : ''}>All Types</option>
                            <option value="Annual Leave" ${requestTypeFilter == 'Annual Leave' ? 'selected' : ''}>Annual Leave</option>
                            <option value="Sick Leave" ${requestTypeFilter == 'Sick Leave' ? 'selected' : ''}>Sick Leave</option>
                            <option value="Personal Leave" ${requestTypeFilter == 'Personal Leave' ? 'selected' : ''}>Personal Leave</option>
                            <option value="Remote Work" ${requestTypeFilter == 'Remote Work' ? 'selected' : ''}>Remote Work</option>
                            <option value="Business Trip" ${requestTypeFilter == 'Business Trip' ? 'selected' : ''}>Business Trip</option>
                        </select>
                    </div>
                    <div class="col-md-3">
                        <label for="startDateFilter" class="form-label">From Date</label>
                        <input type="date" class="form-control" id="startDateFilter" name="startDate" value="${startDateFilter}">
                    </div>
                    <div class="col-md-3">
                        <label for="endDateFilter" class="form-label">To Date</label>
                        <input type="date" class="form-control" id="endDateFilter" name="endDate" value="${endDateFilter}">
                    </div>
                    <div class="col-md-9 d-flex align-items-end">
                        <button type="submit" class="btn btn-primary me-2">
                            <i class="fas fa-search me-2"></i>Search
                        </button>
                        <a href="${pageContext.request.contextPath}/request/manager/list" class="btn btn-secondary">
                            <i class="fas fa-redo me-2"></i>Clear Filters
                        </a>
                    </div>
                </form>
            </div>

            <!-- Requests Table -->
            <div class="card">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0">
                        <i class="fas fa-clipboard-list me-2 text-white"></i>
                        Team Requests
                    </h5>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty requests}">
                            <div class="text-center py-5">
                                <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                                <p class="text-muted">No requests found matching the selected filters.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Request ID</th>
                                            <th>Employee</th>
                                            <th>Type</th>
                                            <th>Start Date</th>
                                            <th>End Date</th>
                                            <th>Days</th>
                                            <th>Status</th>
                                            <th>Submitted</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="req" items="${requests}">
                                            <tr class="request-row">
                                                <td>#${req.requestID}</td>
                                                <td>
                                                    <strong>${req.employeeName}</strong><br>
                                                    <small class="text-muted">${req.employeeCode}</small>
                                                </td>
                                                <td>${req.requestTypeName}</td>
                                                <td>
                                                    <fmt:formatDate value="${req.startDate}" pattern="dd/MM/yyyy"/>
                                                </td>
                                                <td>
                                                    <fmt:formatDate value="${req.endDate}" pattern="dd/MM/yyyy"/>
                                                </td>
                                                <td>${req.numberOfDays}</td>
                                                <td>
                                                    <span class="badge ${req.statusBadgeClass}">
                                                        ${req.requestStatus}
                                                    </span>
                                                </td>
                                                <td>
                                                    <fmt:formatDate value="${req.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/>
                                                </td>
                                                <td>
                                                    <div class="btn-group btn-group-sm" role="group">
                                                        <button type="button" class="btn btn-outline-primary" 
                                                                onclick="viewRequestDetail(${req.requestID})" title="View Details">
                                                            <i class="fas fa-eye"></i>
                                                        </button>
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
                                                    </div>
                                                </td>
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
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header bg-success text-white">
                    <h5 class="modal-title">Approve Request</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                </div>
                <form id="approveForm" method="POST" action="${pageContext.request.contextPath}/request/approve-reject">
                    <div class="modal-body">
                        <input type="hidden" name="requestId" id="approveRequestId">
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
                <form id="rejectForm" method="POST" action="${pageContext.request.contextPath}/request/approve-reject">
                    <div class="modal-body">
                        <input type="hidden" name="requestId" id="rejectRequestId">
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
        // View request detail
        function viewRequestDetail(requestId) {
            window.location.href = '${pageContext.request.contextPath}/request/detail?id=' + requestId;
        }

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

