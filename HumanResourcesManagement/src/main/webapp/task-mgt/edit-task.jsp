<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Task - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    
    <style>
        .required-field::after {
            content: " *";
            color: #dc2626;
        }
    </style>
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="Edit Task" />
        </jsp:include>

        <nav aria-label="breadcrumb" class="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/task/list">Tasks</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/task/detail?id=${task.taskId}">Task Details</a></li>
                <li class="breadcrumb-item active">Edit Task</li>
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

            <!-- Edit Task Form -->
            <div class="card">
                <div class="card-header">
                    <i class="fas fa-edit me-2"></i>Edit Task Information
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/task/edit" method="POST">
                        <input type="hidden" name="taskId" value="${task.taskId}">
                        
                        <div class="row">
                            <div class="col-md-12 mb-3">
                                <label for="taskTitle" class="form-label required-field">Task Title</label>
                                <input type="text" class="form-control" id="taskTitle" name="taskTitle" 
                                       value="${task.taskTitle}" placeholder="Enter task title" required maxlength="200">
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-12 mb-3">
                                <label for="taskDescription" class="form-label">Task Description</label>
                                <textarea class="form-control" id="taskDescription" name="taskDescription" 
                                          rows="4" placeholder="Describe the task in detail">${task.taskDescription}</textarea>
                            </div>
                        </div>

                        <div class="row">
                            <c:if test="${userRole == 'HR_MANAGER' || userRole == 'HR Manager' || userRole == 'DEPT_MANAGER' || userRole == 'Dept Manager'}">
                                <div class="col-md-6 mb-3">
                                    <label for="assignedTo" class="form-label required-field">Assign To</label>
                                    <select class="form-select" id="assignedTo" name="assignedTo" required>
                                        <c:forEach var="employee" items="${employees}">
                                            <option value="${employee.employeeID}" 
                                                    ${employee.employeeID == task.assignedTo ? 'selected' : ''}>
                                                ${employee.employeeCode} - ${employee.firstName} ${employee.lastName}
                                                <c:if test="${not empty employee.departmentName}">
                                                    (${employee.departmentName})
                                                </c:if>
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="col-md-6 mb-3">
                                    <label for="departmentId" class="form-label">Department</label>
                                    <select class="form-select" id="departmentId" name="departmentId">
                                        <option value="">-- Select Department (Optional) --</option>
                                        <c:forEach var="dept" items="${departments}">
                                            <option value="${dept.departmentID}" 
                                                    ${dept.departmentID == task.departmentId ? 'selected' : ''}>
                                                ${dept.departmentName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </c:if>
                        </div>

                        <div class="row">
                            <div class="col-md-4 mb-3">
                                <label for="priority" class="form-label">Priority</label>
                                <select class="form-select" id="priority" name="priority">
                                    <option value="Low" ${task.priority == 'Low' ? 'selected' : ''}>Low</option>
                                    <option value="Medium" ${task.priority == 'Medium' ? 'selected' : ''}>Medium</option>
                                    <option value="High" ${task.priority == 'High' ? 'selected' : ''}>High</option>
                                    <option value="Urgent" ${task.priority == 'Urgent' ? 'selected' : ''}>Urgent</option>
                                </select>
                            </div>

                            <div class="col-md-4 mb-3">
                                <label for="startDate" class="form-label">Start Date</label>
                                <input type="date" class="form-control" id="startDate" name="startDate" 
                                       value="<fmt:formatDate value='${task.startDate}' pattern='yyyy-MM-dd'/>">
                            </div>

                            <div class="col-md-4 mb-3">
                                <label for="dueDate" class="form-label required-field">Due Date</label>
                                <input type="date" class="form-control" id="dueDate" name="dueDate" 
                                       value="<fmt:formatDate value='${task.dueDate}' pattern='yyyy-MM-dd'/>" required>
                            </div>
                        </div>

                        <!-- Current Status Info (read-only) -->
                        <div class="row">
                            <div class="col-md-12 mb-3">
                                <div class="alert alert-info">
                                    <strong>Current Status:</strong> 
                                    <span class="badge ${task.statusBadgeClass}">${task.taskStatus}</span>
                                    <strong class="ms-3">Progress:</strong> ${task.progressPercentage}%
                                    <br>
                                    <small class="text-muted">
                                        Note: To update status and progress, use the "Update Status" section in the task detail page.
                                    </small>
                                </div>
                            </div>
                        </div>

                        <div class="row mt-3">
                            <div class="col-md-12">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-save me-2"></i>Save Changes
                                </button>
                                <a href="${pageContext.request.contextPath}/task/detail?id=${task.taskId}" 
                                   class="btn btn-secondary">
                                    <i class="fas fa-times me-2"></i>Cancel
                                </a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Update due date minimum when start date changes
        document.getElementById('startDate').addEventListener('change', function() {
            const startDate = this.value;
            if (startDate) {
                document.getElementById('dueDate').setAttribute('min', startDate);
            }
        });
        
        // Set initial minimum for due date
        const startDateValue = document.getElementById('startDate').value;
        if (startDateValue) {
            document.getElementById('dueDate').setAttribute('min', startDateValue);
        }
    </script>
</body>
</html>

