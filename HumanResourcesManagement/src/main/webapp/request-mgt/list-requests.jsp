<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Requests - HR Management System</title>
    
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
    </style>
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="Request History" />
        </jsp:include>

        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard/employee-dashboard.jsp">Employee Dashboard</a></li>
                <li class="breadcrumb-item active">Request History</li>
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

            <!-- Action Buttons -->
            <div class="mb-3">
                <a href="${pageContext.request.contextPath}/request/submit" class="btn btn-primary">
                    <i class="fas fa-plus me-2"></i>Submit New Request
                </a>
            </div>

            <!-- Search and Filter Section -->
            <div class="card mb-4">
                <div class="card-header bg-light">
                    <h6 class="mb-0">
                        <i class="fas fa-filter me-2"></i>Search & Filter
                    </h6>
                </div>
                <div class="card-body">
                    <form method="GET" action="${pageContext.request.contextPath}/request/list" class="row g-3">
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
                                <c:forEach var="requestType" items="${requestTypes}">
                                    <option value="${requestType.requestTypeName}" ${requestTypeFilter == requestType.requestTypeName ? 'selected' : ''}>
                                        ${requestType.requestTypeName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-2">
                            <label for="startDateFilter" class="form-label">From Date</label>
                            <input type="date" class="form-select" id="startDateFilter" name="startDate" value="${startDateFilter}">
                        </div>
                        <div class="col-md-2">
                            <label for="endDateFilter" class="form-label">To Date</label>
                            <input type="date" class="form-select" id="endDateFilter" name="endDate" value="${endDateFilter}">
                        </div>
                        <div class="col-md-2 d-flex align-items-end gap-2">
                            <button type="submit" class="btn btn-primary flex-fill">
                                <i class="fas fa-search me-2"></i>Search
                            </button>
                            <a href="${pageContext.request.contextPath}/request/list" class="btn btn-secondary">
                                <i class="fas fa-redo me-2"></i>Clear
                            </a>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Requests Table -->
            <div class="card">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0">
                        <i class="fas fa-list me-2"></i>
                        My Request History
                    </h5>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty requests}">
                            <div class="text-center py-5">
                                <i class="fas fa-inbox fa-3x text-muted mb-3"></i>
                                <p class="text-muted">No requests found. Click "Submit New Request" to create your first request.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="table-responsive">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Request ID</th>
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
                                                    <strong>${req.requestTypeName}</strong>
                                                </td>
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
                                                        <a href="${pageContext.request.contextPath}/request/detail?id=${req.requestID}" 
                                                           class="btn btn-outline-primary" title="View Details">
                                                            <i class="fas fa-eye"></i>
                                                        </a>
                                                        <c:if test="${req.canBeEdited()}">
                                                            <a href="${pageContext.request.contextPath}/request/edit?id=${req.requestID}" 
                                                               class="btn btn-outline-warning" title="Edit">
                                                                <i class="fas fa-edit"></i>
                                                            </a>
                                                        </c:if>
                                                        <c:if test="${req.canBeCancelled()}">
                                                            <button type="button" class="btn btn-outline-danger" 
                                                                    onclick="confirmCancel('${req.requestID}')" title="Cancel">
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

                            <!-- Summary Statistics -->
                            <div class="row mt-4">
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
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <!-- Cancel Request Confirmation Modal -->
    <div class="modal fade" id="cancelRequestModal" tabindex="-1" aria-labelledby="cancelRequestModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header bg-warning text-dark">
                    <h5 class="modal-title" id="cancelRequestModalLabel">
                        <i class="fas fa-exclamation-triangle me-2"></i>Cancel Request
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="text-center mb-3">
                        <i class="fas fa-times-circle fa-3x text-warning mb-3"></i>
                    </div>
                    <h6 class="text-center mb-3">Are you sure you want to cancel this request?</h6>
                    <p class="text-muted text-center mb-0">
                        This action cannot be undone. The request will be permanently cancelled and cannot be recovered.
                    </p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="fas fa-times me-2"></i>Keep Request
                    </button>
                    <button type="button" class="btn btn-danger" id="confirmCancelBtn">
                        <i class="fas fa-ban me-2"></i>Cancel Request
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Custom JavaScript -->
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
        });

        // Confirm cancel request
        function confirmCancel(requestId) {
            // Store the request ID for later use
            document.getElementById('confirmCancelBtn').setAttribute('data-request-id', requestId);
            
            // Show the modal
            const cancelModal = new bootstrap.Modal(document.getElementById('cancelRequestModal'));
            cancelModal.show();
        }

        // Handle confirm cancel button click
        document.getElementById('confirmCancelBtn').addEventListener('click', function() {
            const requestId = this.getAttribute('data-request-id');
            
            // Create a form and submit
            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '${pageContext.request.contextPath}/request/cancel';
            
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = 'requestId';
            input.value = requestId;
            
            form.appendChild(input);
            document.body.appendChild(form);
            form.submit();
        });
    </script>
</body>
</html>

