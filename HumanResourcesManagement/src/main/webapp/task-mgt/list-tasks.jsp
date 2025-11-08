<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Task List - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    
    <style>
        .task-card {
            border-left: 4px solid #0d6efd;
            transition: all 0.2s;
        }
        .task-card:hover {
            box-shadow: 0 0.25rem 0.5rem rgba(0, 0, 0, 0.1);
            transform: translateY(-2px);
        }
        .task-card.overdue {
            border-left-color: #dc3545;
        }
        .task-card.done {
            border-left-color: #198754;
            opacity: 0.8;
        }
        .stats-card {
            border-radius: 0.5rem;
            padding: 1rem;
            text-align: center;
            background: white;
            border: 1px solid #e5e7eb;
        }
        .stats-card h3 {
            font-size: 2rem;
            font-weight: 700;
            margin-bottom: 0.25rem;
        }
        .stats-card p {
            font-size: 0.875rem;
            color: #6b7280;
            margin: 0;
        }
        .badge-warning {
            background-color: #ffc107;
            color: #000;
        }
    </style>
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="Task List" />
        </jsp:include>

        <nav aria-label="breadcrumb" class="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="breadcrumb-item active">Tasks</li>
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

            <!-- Task Statistics (for assigned_to_me view) -->
            <c:if test="${viewType == 'assigned_to_me'}">
                <div class="row mb-4">
                    <div class="col-md-2">
                        <div class="stats-card">
                            <h3 class="text-secondary">${notStartedCount}</h3>
                            <p>Not Started</p>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <div class="stats-card">
                            <h3 class="text-warning">${inProgressCount}</h3>
                            <p>In Progress</p>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <div class="stats-card">
                            <h3 class="text-success">${doneCount}</h3>
                            <p>Done</p>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <div class="stats-card">
                            <h3 class="text-danger">${blockedCount}</h3>
                            <p>Blocked</p>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <div class="stats-card">
                            <h3 class="text-muted">${cancelledCount}</h3>
                            <p>Cancelled</p>
                        </div>
                    </div>
                    <div class="col-md-2">
                        <div class="stats-card">
                            <h3 class="text-primary">${notStartedCount + inProgressCount + doneCount + blockedCount + cancelledCount}</h3>
                            <p>Total</p>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- Filter and View Options -->
            <div class="card mb-3">
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/task/list" method="GET" class="row g-3">
                        <!-- View Type (for managers) -->
                        <c:if test="${userRole == 'HR_MANAGER' || userRole == 'DEPT_MANAGER'}">
                            <div class="col-md-3">
                                <label class="form-label">View</label>
                                <select class="form-select" name="view" onchange="this.form.submit()">
                                    <option value="assigned_to_me" ${viewType == 'assigned_to_me' ? 'selected' : ''}>Assigned to Me</option>
                                    <option value="assigned_by_me" ${viewType == 'assigned_by_me' ? 'selected' : ''}>Assigned by Me</option>
                                    <c:if test="${userRole == 'DEPT_MANAGER'}">
                                        <option value="my_department" ${viewType == 'my_department' ? 'selected' : ''}>My Department</option>
                                    </c:if>
                                </select>
                            </div>
                        </c:if>

                        <!-- Status Filter -->
                        <div class="col-md-3">
                            <label class="form-label">Status</label>
                            <select class="form-select" name="status">
                                <option value="">All Statuses</option>
                                <option value="Not Started" ${statusFilter == 'Not Started' ? 'selected' : ''}>Not Started</option>
                                <option value="In Progress" ${statusFilter == 'In Progress' ? 'selected' : ''}>In Progress</option>
                                <option value="Done" ${statusFilter == 'Done' ? 'selected' : ''}>Done</option>
                                <option value="Blocked" ${statusFilter == 'Blocked' ? 'selected' : ''}>Blocked</option>
                                <option value="Cancelled" ${statusFilter == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
                            </select>
                        </div>

                        <!-- Priority Filter -->
                        <div class="col-md-3">
                            <label class="form-label">Priority</label>
                            <select class="form-select" name="priority">
                                <option value="">All Priorities</option>
                                <option value="Low" ${priorityFilter == 'Low' ? 'selected' : ''}>Low</option>
                                <option value="Medium" ${priorityFilter == 'Medium' ? 'selected' : ''}>Medium</option>
                                <option value="High" ${priorityFilter == 'High' ? 'selected' : ''}>High</option>
                                <option value="Urgent" ${priorityFilter == 'Urgent' ? 'selected' : ''}>Urgent</option>
                            </select>
                        </div>

                        <!-- Filter Buttons -->
                        <div class="col-md-3">
                            <label class="form-label">&nbsp;</label>
                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-filter me-2"></i>Filter
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Task List -->
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <div>
                        <i class="fas fa-tasks me-2"></i>
                        <c:choose>
                            <c:when test="${viewType == 'assigned_by_me'}">Tasks I Assigned</c:when>
                            <c:when test="${viewType == 'my_department'}">Department Tasks</c:when>
                            <c:otherwise>My Tasks</c:otherwise>
                        </c:choose>
                    </div>
                    <c:if test="${userRole == 'HR_MANAGER' || userRole == 'DEPT_MANAGER'}">
                        <a href="${pageContext.request.contextPath}/task/assign" class="btn btn-primary btn-sm">
                            <i class="fas fa-plus me-2"></i>Assign New Task
                        </a>
                    </c:if>
                </div>
                <div class="card-body">
                    <c:choose>
                        <c:when test="${empty tasks}">
                            <div class="empty-state">
                                <i class="fas fa-clipboard-list"></i>
                                <h4>No Tasks Found</h4>
                                <p>There are no tasks matching your criteria.</p>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="row">
                                <c:forEach var="task" items="${tasks}">
                                    <div class="col-md-6 mb-3">
                                        <div class="card task-card ${task.overdue ? 'overdue' : ''} ${task.done ? 'done' : ''}">
                                            <div class="card-body">
                                                <div class="d-flex justify-content-between align-items-start mb-2">
                                                    <h5 class="card-title mb-0">
                                                        <a href="${pageContext.request.contextPath}/task/detail?id=${task.taskId}" 
                                                           class="text-decoration-none">
                                                            ${task.taskTitle}
                                                        </a>
                                                    </h5>
                                                    <span class="badge ${task.priorityBadgeClass}">${task.priority}</span>
                                                </div>
                                                
                                                <p class="card-text text-muted small mb-2">
                                                    <c:if test="${not empty task.taskDescription}">
                                                        ${task.taskDescription.length() > 100 ? task.taskDescription.substring(0, 100).concat('...') : task.taskDescription}
                                                    </c:if>
                                                </p>

                                                <div class="mb-2">
                                                    <span class="badge ${task.statusBadgeClass}">${task.taskStatus}</span>
                                                    <c:if test="${task.overdue && !task.done && !task.cancelled}">
                                                        <span class="badge badge-expired">Overdue</span>
                                                    </c:if>
                                                </div>

                                                <div class="small text-muted">
                                                    <div><i class="fas fa-user me-1"></i> 
                                                        <c:choose>
                                                            <c:when test="${viewType == 'assigned_by_me'}">
                                                                Assigned to: ${task.assignedToName}
                                                            </c:when>
                                                            <c:otherwise>
                                                                Assigned by: ${task.assignedByName}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                    <div><i class="fas fa-calendar me-1"></i> 
                                                        Due: <fmt:formatDate value="${task.dueDate}" pattern="MMM dd, yyyy"/>
                                                    </div>
                                                    <c:if test="${not empty task.departmentName}">
                                                        <div><i class="fas fa-building me-1"></i> ${task.departmentName}</div>
                                                    </c:if>
                                                    <div class="mt-2">
                                                        <div class="progress" style="height: 20px;">
                                                            <div class="progress-bar" role="progressbar" 
                                                                 style="width: ${task.progressPercentage}%;" 
                                                                 aria-valuenow="${task.progressPercentage}" 
                                                                 aria-valuemin="0" aria-valuemax="100">
                                                                ${task.progressPercentage}%
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="mt-3">
                                                    <a href="${pageContext.request.contextPath}/task/detail?id=${task.taskId}" 
                                                       class="btn btn-sm btn-action btn-view">
                                                        <i class="fas fa-eye me-1"></i>View
                                                    </a>
                                                    <c:if test="${task.canBeEdited() && (userRole == 'HR_MANAGER' || userRole == 'DEPT_MANAGER')}">
                                                        <a href="${pageContext.request.contextPath}/task/edit?id=${task.taskId}" 
                                                           class="btn btn-sm btn-action btn-edit">
                                                            <i class="fas fa-edit me-1"></i>Edit
                                                        </a>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

