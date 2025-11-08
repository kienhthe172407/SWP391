<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Task Details - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    
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
        .badge-warning {
            background-color: #ffc107;
            color: #000;
        }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="sidebar-header">
            <c:set var="roleName" value="${sessionScope.user.role}" />
            <c:choose>
                <c:when test="${roleName == 'HR_MANAGER'}">
                    <h4>HR Manager Dashboard</h4>
                    <p>Human Resources</p>
                </c:when>
                <c:when test="${roleName == 'DEPT_MANAGER'}">
                    <h4>Department Manager</h4>
                    <p>Team Management</p>
                </c:when>
                <c:otherwise>
                    <h4>Employee Dashboard</h4>
                    <p>Personal Portal</p>
                </c:otherwise>
            </c:choose>
        </div>

        <ul class="sidebar-menu">
            <li class="menu-section">Dashboard</li>
            <li>
                <a href="${pageContext.request.contextPath}/">
                    <i class="fas fa-home"></i>
                    <span>Overview</span>
                </a>
            </li>

            <li class="menu-section">Task Management</li>
            <c:if test="${roleName == 'HR_MANAGER' || roleName == 'DEPT_MANAGER'}">
                <li>
                    <a href="${pageContext.request.contextPath}/task/assign">
                        <i class="fas fa-plus-circle"></i>
                        <span>Assign Task</span>
                    </a>
                </li>
            </c:if>
            <li>
                <a href="${pageContext.request.contextPath}/task/list" class="active">
                    <i class="fas fa-tasks"></i>
                    <span>View Tasks</span>
                </a>
            </li>

            <li class="menu-section">Account</li>
            <li>
                <a href="${pageContext.request.contextPath}/profile">
                    <i class="fas fa-user"></i>
                    <span>My Profile</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/logout">
                    <i class="fas fa-sign-out-alt"></i>
                    <span>Logout</span>
                </a>
            </li>
        </ul>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <div class="top-header">
            <h1>Task Details</h1>
            <div class="user-info">
                <span><c:out value="${sessionScope.user.roleDisplayName}" default="User"/></span>
                <div class="dropdown">
                    <button class="btn dropdown-toggle avatar" type="button" id="userDropdown" data-bs-toggle="dropdown">
                        <c:choose>
                            <c:when test="${roleName == 'HR_MANAGER'}">HRM</c:when>
                            <c:when test="${roleName == 'DEPT_MANAGER'}">MGR</c:when>
                            <c:otherwise>EMP</c:otherwise>
                        </c:choose>
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end">
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

        <nav aria-label="breadcrumb" class="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/task/list">Tasks</a></li>
                <li class="breadcrumb-item active">Task Details</li>
            </ol>
        </nav>

        <div class="content-area">
            <!-- Success/Error Messages -->
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>
                    ${sessionScope.successMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="successMessage" scope="session" />
            </c:if>

            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    ${sessionScope.errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="errorMessage" scope="session" />
            </c:if>

            <!-- Task Information -->
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <div>
                        <i class="fas fa-info-circle me-2"></i>Task Information
                    </div>
                    <div>
                        <c:if test="${canEdit}">
                            <a href="${pageContext.request.contextPath}/task/edit?id=${task.taskId}" 
                               class="btn btn-sm btn-primary">
                                <i class="fas fa-edit me-1"></i>Edit Task
                            </a>
                        </c:if>
                        <a href="${pageContext.request.contextPath}/task/list" class="btn btn-sm btn-secondary">
                            <i class="fas fa-arrow-left me-1"></i>Back to List
                        </a>
                    </div>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-8">
                            <div class="detail-card">
                                <h4 class="mb-3">${task.taskTitle}</h4>
                                
                                <div class="detail-row">
                                    <div class="detail-label">Status</div>
                                    <div class="detail-value">
                                        <span class="badge status-badge-large ${task.statusBadgeClass}">${task.taskStatus}</span>
                                        <c:if test="${task.overdue && !task.done && !task.cancelled}">
                                            <span class="badge status-badge-large badge-expired ms-2">Overdue</span>
                                        </c:if>
                                    </div>
                                </div>

                                <div class="detail-row">
                                    <div class="detail-label">Priority</div>
                                    <div class="detail-value">
                                        <span class="badge ${task.priorityBadgeClass}">${task.priority}</span>
                                    </div>
                                </div>

                                <div class="detail-row">
                                    <div class="detail-label">Description</div>
                                    <div class="detail-value">
                                        <c:choose>
                                            <c:when test="${not empty task.taskDescription}">
                                                <p style="white-space: pre-wrap;">${task.taskDescription}</p>
                                            </c:when>
                                            <c:otherwise>
                                                <em class="text-muted">No description provided</em>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>

                                <div class="detail-row">
                                    <div class="detail-label">Progress</div>
                                    <div class="detail-value">
                                        <div class="progress" style="height: 25px;">
                                            <div class="progress-bar" role="progressbar" 
                                                 style="width: ${task.progressPercentage}%;" 
                                                 aria-valuenow="${task.progressPercentage}" 
                                                 aria-valuemin="0" aria-valuemax="100">
                                                ${task.progressPercentage}%
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="detail-row">
                                    <div class="detail-label">Assigned To</div>
                                    <div class="detail-value">
                                        ${task.assignedToName} (${task.assignedToCode})
                                    </div>
                                </div>

                                <div class="detail-row">
                                    <div class="detail-label">Assigned By</div>
                                    <div class="detail-value">
                                        ${task.assignedByName}
                                    </div>
                                </div>

                                <c:if test="${not empty task.departmentName}">
                                    <div class="detail-row">
                                        <div class="detail-label">Department</div>
                                        <div class="detail-value">
                                            ${task.departmentName}
                                        </div>
                                    </div>
                                </c:if>
                            </div>
                        </div>

                        <div class="col-md-4">
                            <!-- Dates Card -->
                            <div class="detail-card">
                                <h5 class="mb-3"><i class="fas fa-calendar me-2"></i>Timeline</h5>
                                
                                <c:if test="${not empty task.startDate}">
                                    <div class="detail-row">
                                        <div class="detail-label">Start Date</div>
                                        <div class="detail-value">
                                            <fmt:formatDate value="${task.startDate}" pattern="MMM dd, yyyy"/>
                                        </div>
                                    </div>
                                </c:if>

                                <div class="detail-row">
                                    <div class="detail-label">Due Date</div>
                                    <div class="detail-value">
                                        <fmt:formatDate value="${task.dueDate}" pattern="MMM dd, yyyy"/>
                                    </div>
                                </div>

                                <c:if test="${not empty task.completedDate}">
                                    <div class="detail-row">
                                        <div class="detail-label">Completed Date</div>
                                        <div class="detail-value">
                                            <fmt:formatDate value="${task.completedDate}" pattern="MMM dd, yyyy"/>
                                        </div>
                                    </div>
                                </c:if>

                                <div class="detail-row">
                                    <div class="detail-label">Created At</div>
                                    <div class="detail-value">
                                        <fmt:formatDate value="${task.createdAt}" pattern="MMM dd, yyyy HH:mm"/>
                                    </div>
                                </div>

                                <div class="detail-row">
                                    <div class="detail-label">Last Updated</div>
                                    <div class="detail-value">
                                        <fmt:formatDate value="${task.updatedAt}" pattern="MMM dd, yyyy HH:mm"/>
                                    </div>
                                </div>
                            </div>

                            <!-- Update Status Card (if allowed) -->
                            <c:if test="${canUpdateStatus}">
                                <div class="detail-card">
                                    <h5 class="mb-3"><i class="fas fa-edit me-2"></i>Update Status</h5>
                                    <form action="${pageContext.request.contextPath}/task/update-status" method="POST">
                                        <input type="hidden" name="taskId" value="${task.taskId}">
                                        <input type="hidden" name="redirect" value="detail">
                                        
                                        <div class="mb-3">
                                            <label for="status" class="form-label">Status</label>
                                            <select class="form-select" id="status" name="status" required>
                                                <option value="Not Started" ${task.taskStatus == 'Not Started' ? 'selected' : ''}>Not Started</option>
                                                <option value="In Progress" ${task.taskStatus == 'In Progress' ? 'selected' : ''}>In Progress</option>
                                                <option value="Done" ${task.taskStatus == 'Done' ? 'selected' : ''}>Done</option>
                                                <option value="Blocked" ${task.taskStatus == 'Blocked' ? 'selected' : ''}>Blocked</option>
                                            </select>
                                        </div>

                                        <div class="mb-3">
                                            <label for="progress" class="form-label">Progress (%)</label>
                                            <input type="number" class="form-control" id="progress" name="progress" 
                                                   min="0" max="100" value="${task.progressPercentage}">
                                        </div>

                                        <button type="submit" class="btn btn-primary w-100">
                                            <i class="fas fa-save me-2"></i>Update Status
                                        </button>
                                    </form>
                                </div>
                            </c:if>

                            <!-- Cancel Task Card (if allowed) -->
                            <c:if test="${canCancel}">
                                <div class="detail-card">
                                    <h5 class="mb-3 text-danger"><i class="fas fa-ban me-2"></i>Cancel Task</h5>
                                    <form action="${pageContext.request.contextPath}/task/cancel" method="POST" 
                                          onsubmit="return confirm('Are you sure you want to cancel this task?');">
                                        <input type="hidden" name="taskId" value="${task.taskId}">
                                        <input type="hidden" name="redirect" value="detail">
                                        
                                        <div class="mb-3">
                                            <label for="cancellationReason" class="form-label">Reason</label>
                                            <textarea class="form-control" id="cancellationReason" name="cancellationReason" 
                                                      rows="3" placeholder="Why is this task being cancelled?"></textarea>
                                        </div>

                                        <button type="submit" class="btn btn-danger w-100">
                                            <i class="fas fa-ban me-2"></i>Cancel Task
                                        </button>
                                    </form>
                                </div>
                            </c:if>

                            <!-- Cancellation Info (if cancelled) -->
                            <c:if test="${task.cancelled}">
                                <div class="detail-card border-danger">
                                    <h5 class="mb-3 text-danger"><i class="fas fa-info-circle me-2"></i>Cancellation Info</h5>
                                    
                                    <div class="detail-row">
                                        <div class="detail-label">Cancelled By</div>
                                        <div class="detail-value">
                                            ${task.cancelledByName}
                                        </div>
                                    </div>

                                    <div class="detail-row">
                                        <div class="detail-label">Cancelled At</div>
                                        <div class="detail-value">
                                            <fmt:formatDate value="${task.cancelledAt}" pattern="MMM dd, yyyy HH:mm"/>
                                        </div>
                                    </div>

                                    <c:if test="${not empty task.cancellationReason}">
                                        <div class="detail-row">
                                            <div class="detail-label">Reason</div>
                                            <div class="detail-value">
                                                <p style="white-space: pre-wrap;">${task.cancellationReason}</p>
                                            </div>
                                        </div>
                                    </c:if>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

