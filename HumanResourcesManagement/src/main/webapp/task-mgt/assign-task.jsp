<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Assign Task - HR Management System</title>
    
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
        .card-body {
            padding: 2rem;
        }
        .form-section {
            margin-bottom: 2rem;
        }
        .form-section h5 {
            color: #495057;
            font-weight: 600;
            margin-bottom: 1.5rem;
        }
    </style>
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="Assign Task" />
        </jsp:include>

        <nav aria-label="breadcrumb" class="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/task/list">Tasks</a></li>
                <li class="breadcrumb-item active">Assign Task</li>
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

            <!-- Assign Task Form -->
            <div class="card">
                <div class="card-header">
                    <i class="fas fa-plus-circle me-2"></i>Task Assignment Form
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/task/assign" method="post">
                        <!-- Task Information Section -->
                        <div class="form-section">
                            <h5><i class="fas fa-info-circle me-2"></i>Task Information</h5>
                            
                            <div class="mb-3">
                                <label for="taskTitle" class="form-label required-field">Task Title</label>
                                <input type="text" class="form-control" id="taskTitle" name="taskTitle" 
                                       placeholder="Enter task title" maxlength="200" required>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>
                                    Enter a clear and concise title for the task (required, max 200 characters)
                                </small>
                            </div>

                            <div class="mb-3">
                                <label for="taskDescription" class="form-label">Task Description</label>
                                <textarea class="form-control" id="taskDescription" name="taskDescription" 
                                          rows="4" placeholder="Describe the task in detail"></textarea>
                                <small class="form-text text-muted">
                                    <i class="fas fa-info-circle me-1"></i>
                                    Provide detailed information about the task, including objectives, requirements, and any relevant context (optional)
                                </small>
                            </div>
                        </div>

                        <!-- Assignment Section -->
                        <hr class="my-4">
                        <div class="form-section">
                            <h5><i class="fas fa-user-check me-2"></i>Assignment</h5>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="assignedTo" class="form-label required-field">Assign To</label>
                                    <select class="form-select" id="assignedTo" name="assignedTo" required>
                                        <option value="">-- Select Employee --</option>
                                        <c:forEach var="employee" items="${employees}">
                                            <option value="${employee.employeeID}">
                                                ${employee.employeeCode} - ${employee.firstName} ${employee.lastName}
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <c:if test="${empty employees}">
                                        <small class="form-text text-danger">
                                            <i class="fas fa-exclamation-triangle me-1"></i>
                                            No employees found. Please ensure your employee record is linked to a department.
                                        </small>
                                    </c:if>
                                    <c:if test="${not empty employees}">
                                        <small class="form-text text-muted">
                                            <i class="fas fa-info-circle me-1"></i>
                                            Select the employee who will be responsible for completing this task (required)
                                        </small>
                                    </c:if>
                                </div>

                                <div class="col-md-6 mb-3">
                                    <label for="departmentId" class="form-label">Department</label>
                                    <select class="form-select" id="departmentId" name="departmentId">
                                        <option value="">-- Select Department (Optional) --</option>
                                        <c:forEach var="dept" items="${departments}">
                                            <option value="${dept.departmentId}">${dept.departmentName}</option>
                                        </c:forEach>
                                    </select>
                                    <small class="form-text text-muted">
                                        <i class="fas fa-info-circle me-1"></i>
                                        Optionally specify the department associated with this task (optional)
                                    </small>
                                </div>
                            </div>
                        </div>

                        <!-- Priority and Dates Section -->
                        <hr class="my-4">
                        <div class="form-section">
                            <h5><i class="fas fa-calendar-alt me-2"></i>Priority & Timeline</h5>
                            
                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label for="priority" class="form-label">Priority</label>
                                    <select class="form-select" id="priority" name="priority">
                                        <option value="Low">Low</option>
                                        <option value="Medium" selected>Medium</option>
                                        <option value="High">High</option>
                                        <option value="Urgent">Urgent</option>
                                    </select>
                                    <small class="form-text text-muted">
                                        <i class="fas fa-info-circle me-1"></i>
                                        Set the priority level: Low, Medium, High, or Urgent (default: Medium)
                                    </small>
                                </div>

                                <div class="col-md-4 mb-3">
                                    <label for="startDate" class="form-label">Start Date</label>
                                    <input type="date" class="form-control" id="startDate" name="startDate">
                                    <small class="form-text text-muted">
                                        <i class="fas fa-info-circle me-1"></i>
                                        The date when work on this task should begin (optional)
                                    </small>
                                </div>

                                <div class="col-md-4 mb-3">
                                    <label for="dueDate" class="form-label required-field">Due Date</label>
                                    <input type="date" class="form-control" id="dueDate" name="dueDate" required>
                                    <small class="form-text text-muted">
                                        <i class="fas fa-info-circle me-1"></i>
                                        The deadline for completing this task (required, must be on or after start date)
                                    </small>
                                </div>
                            </div>
                        </div>

                        <div class="row mt-3">
                            <div class="col-md-12">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-check me-2"></i>Assign Task
                                </button>
                                <a href="${pageContext.request.contextPath}/task/list" class="btn btn-secondary">
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
        // Set minimum date to today for start and due dates
        document.addEventListener('DOMContentLoaded', function() {
            const today = new Date().toISOString().split('T')[0];
            const startDateInput = document.getElementById('startDate');
            const dueDateInput = document.getElementById('dueDate');
            
            if (startDateInput) {
                startDateInput.setAttribute('min', today);
            }
            
            if (dueDateInput) {
                dueDateInput.setAttribute('min', today);
            }
            
            // Validate due date is after start date
            if (startDateInput && dueDateInput) {
                startDateInput.addEventListener('change', function() {
                    dueDateInput.setAttribute('min', this.value || today);
                });
            }
        });
    </script>
</body>
</html>
