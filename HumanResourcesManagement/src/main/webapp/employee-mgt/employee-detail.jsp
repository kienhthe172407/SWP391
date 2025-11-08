<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Employee Details - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    
    <style>
        .detail-section {
            margin-bottom: 2rem;
            padding-bottom: 1rem;
            border-bottom: 1px solid #eee;
        }
        
        .detail-section h5 {
            margin-bottom: 1.5rem;
            color: #0d6efd;
        }
        
        .detail-label {
            font-weight: 600;
            color: #495057;
        }
        
        .detail-value {
            color: #212529;
        }
        
        .empty-value {
            color: #6c757d;
            font-style: italic;
        }
        
        .card-header {
            background-color: #0d6efd;
            color: white;
        }
        
        .badge-active {
            background-color: #198754;
            color: white;
        }
        
        .employee-avatar {
            width: 120px;
            height: 120px;
            border-radius: 50%;
            background-color: #e9ecef;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 3rem;
            color: #6c757d;
            margin: 0 auto 1rem;
        }
    </style>
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="Employee Details" />
        </jsp:include>

        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/employees/list">Employee Management</a></li>
                <li class="breadcrumb-item active">Employee Details</li>
            </ol>
        </nav>

        <!-- Content Area -->
        <div class="content-area">
            <!-- Success/Error Messages -->
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle"></i> ${sessionScope.successMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="successMessage" scope="session"/>
            </c:if>

            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle"></i> ${sessionScope.errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <c:remove var="errorMessage" scope="session"/>
            </c:if>

            <!-- Employee Details -->
            <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0"><i class="fas fa-user text-white"></i> Employee Information</h5>
                    <div>
                        <a href="${pageContext.request.contextPath}/employees/edit?employeeId=${employee.employeeID}" class="btn btn-sm btn-outline-light">
                            <i class="fas fa-edit text-white"></i> Edit
                        </a>
                        <c:if test="${sessionScope.userRole == 'HR Manager'}">
                            <button type="button" class="btn btn-sm btn-outline-light ms-2" data-bs-toggle="modal" data-bs-target="#deleteModal">
                                <i class="fas fa-trash-alt text-white"></i> Delete
                            </button>
                        </c:if>
                    </div>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-3 text-center mb-4">
                            <div class="employee-avatar">
                                <i class="fas fa-user"></i>
                            </div>
                            <h4>${employee.firstName} ${employee.lastName}</h4>
                            <p class="text-muted mb-2">${employee.employeeCode}</p>
                            <c:choose>
                                <c:when test="${employee.employmentStatus == 'Active'}">
                                    <span class="badge badge-active">Active</span>
                                </c:when>
                                <c:when test="${employee.employmentStatus == 'On Leave'}">
                                    <span class="badge bg-warning">On Leave</span>
                                </c:when>
                                <c:when test="${employee.employmentStatus == 'Terminated'}">
                                    <span class="badge bg-danger">Terminated</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary">${employee.employmentStatus}</span>
                                </c:otherwise>
                            </c:choose>
                            <div class="mt-3">
                                <c:if test="${not empty employee.positionName}">
                                    <p class="mb-0">${employee.positionName}</p>
                                </c:if>
                                <c:if test="${not empty employee.departmentName}">
                                    <p class="text-muted">${employee.departmentName}</p>
                                </c:if>
                            </div>
                        </div>
                        
                        <div class="col-md-9">
                            <!-- Basic Information Section -->
                            <div class="detail-section">
                                <h5><i class="fas fa-id-card text-primary"></i> Basic Information</h5>
                                <div class="row mb-3">
                                    <div class="col-md-3 detail-label">Employee ID</div>
                                    <div class="col-md-9 detail-value">#${employee.employeeID}</div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-3 detail-label">Full Name</div>
                                    <div class="col-md-9 detail-value">${employee.firstName} ${employee.lastName}</div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-3 detail-label">Date of Birth</div>
                                    <div class="col-md-9">
                                        <c:choose>
                                            <c:when test="${not empty employee.dateOfBirth}">
                                                <span class="detail-value"><fmt:formatDate value="${employee.dateOfBirth}" pattern="dd/MM/yyyy"/></span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="empty-value">Not provided</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-3 detail-label">Gender</div>
                                    <div class="col-md-9">
                                        <c:choose>
                                            <c:when test="${not empty employee.gender}">
                                                <span class="detail-value">${employee.gender}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="empty-value">Not provided</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>

                            <!-- Contact Information Section -->
                            <div class="detail-section">
                                <h5><i class="fas fa-address-book text-primary"></i> Contact Information</h5>
                                <div class="row mb-3">
                                    <div class="col-md-3 detail-label">Phone Number</div>
                                    <div class="col-md-9">
                                        <c:choose>
                                            <c:when test="${not empty employee.phoneNumber}">
                                                <span class="detail-value">${employee.phoneNumber}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="empty-value">Not provided</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-3 detail-label">Personal Email</div>
                                    <div class="col-md-9">
                                        <c:choose>
                                            <c:when test="${not empty employee.personalEmail}">
                                                <span class="detail-value">${employee.personalEmail}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="empty-value">Not provided</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-3 detail-label">Home Address</div>
                                    <div class="col-md-9">
                                        <c:choose>
                                            <c:when test="${not empty employee.homeAddress}">
                                                <span class="detail-value">${employee.homeAddress}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="empty-value">Not provided</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>

                            <!-- Emergency Contact Section -->
                            <div class="detail-section">
                                <h5><i class="fas fa-ambulance text-primary"></i> Emergency Contact</h5>
                                <div class="row mb-3">
                                    <div class="col-md-3 detail-label">Contact Name</div>
                                    <div class="col-md-9">
                                        <c:choose>
                                            <c:when test="${not empty employee.emergencyContactName}">
                                                <span class="detail-value">${employee.emergencyContactName}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="empty-value">Not provided</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-3 detail-label">Contact Phone</div>
                                    <div class="col-md-9">
                                        <c:choose>
                                            <c:when test="${not empty employee.emergencyContactPhone}">
                                                <span class="detail-value">${employee.emergencyContactPhone}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="empty-value">Not provided</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>

                            <!-- Job Information Section -->
                            <div class="detail-section">
                                <h5><i class="fas fa-briefcase text-primary"></i> Job Information</h5>
                                <div class="row mb-3">
                                    <div class="col-md-3 detail-label">Department</div>
                                    <div class="col-md-9">
                                        <c:choose>
                                            <c:when test="${not empty employee.departmentName}">
                                                <span class="detail-value">${employee.departmentName}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="empty-value">Not assigned</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-3 detail-label">Position</div>
                                    <div class="col-md-9">
                                        <c:choose>
                                            <c:when test="${not empty employee.positionName}">
                                                <span class="detail-value">${employee.positionName}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="empty-value">Not assigned</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-3 detail-label">Manager</div>
                                    <div class="col-md-9">
                                        <c:choose>
                                            <c:when test="${not empty employee.managerName}">
                                                <span class="detail-value">${employee.managerName}</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="empty-value">Not assigned</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-3 detail-label">Hire Date</div>
                                    <div class="col-md-9">
                                        <c:choose>
                                            <c:when test="${not empty employee.hireDate}">
                                                <span class="detail-value"><fmt:formatDate value="${employee.hireDate}" pattern="dd/MM/yyyy"/></span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="empty-value">Not provided</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-3 detail-label">Employment Status</div>
                                    <div class="col-md-9">
                                        <c:choose>
                                            <c:when test="${employee.employmentStatus == 'Active'}">
                                                <span class="badge badge-active">Active</span>
                                            </c:when>
                                            <c:when test="${employee.employmentStatus == 'On Leave'}">
                                                <span class="badge bg-warning">On Leave</span>
                                            </c:when>
                                            <c:when test="${employee.employmentStatus == 'Terminated'}">
                                                <span class="badge bg-danger">Terminated</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">${employee.employmentStatus}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                            
                            <!-- System Information Section -->
                            <div class="detail-section">
                                <h5><i class="fas fa-cog text-primary"></i> System Information</h5>
                                <div class="row mb-3">
                                    <div class="col-md-3 detail-label">Created At</div>
                                    <div class="col-md-9">
                                        <c:choose>
                                            <c:when test="${not empty employee.createdAt}">
                                                <span class="detail-value"><fmt:formatDate value="${employee.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="empty-value">Not available</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-3 detail-label">Last Updated</div>
                                    <div class="col-md-9">
                                        <c:choose>
                                            <c:when test="${not empty employee.updatedAt}">
                                                <span class="detail-value"><fmt:formatDate value="${employee.updatedAt}" pattern="dd/MM/yyyy HH:mm:ss"/></span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="empty-value">Not available</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="card-footer">
                    <div class="d-flex justify-content-between">
                        <a href="${pageContext.request.contextPath}/employees/list" class="btn btn-secondary">
                            <i class="fas fa-arrow-left me-1"></i> Back to List
                        </a>
                        <div>
                            <a href="${pageContext.request.contextPath}/employees/edit?employeeId=${employee.employeeID}" class="btn btn-primary">
                                <i class="fas fa-edit me-1"></i> Edit Employee
                            </a>
                            <c:if test="${sessionScope.userRole == 'HR Manager'}">
                                <button type="button" class="btn btn-danger ms-2" data-bs-toggle="modal" data-bs-target="#deleteModal">
                                    <i class="fas fa-trash-alt me-1"></i> Delete Employee
                                </button>
                            </c:if>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Delete Employee Modal -->
    <div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title" id="deleteModalLabel">
                        <i class="fas fa-exclamation-triangle me-2"></i>Delete Employee
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form id="deleteEmployeeForm" method="POST" action="${pageContext.request.contextPath}/employees/delete">
                    <div class="modal-body">
                        <div class="text-center mb-4">
                            <div class="mb-3">
                                <i class="fas fa-trash-alt text-danger" style="font-size: 3rem;"></i>
                            </div>
                            <h5>Are you sure you want to delete this employee?</h5>
                            <p class="text-muted">Employee: <strong>${employee.firstName} ${employee.lastName}</strong></p>
                            <p class="text-danger"><strong>Warning:</strong> This action cannot be undone.</p>
                        </div>
                        <input type="hidden" name="employeeId" value="${employee.employeeID}">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <i class="fas fa-times me-1"></i>Cancel
                        </button>
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-trash-alt me-1"></i>Delete Employee
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Auto-dismiss alerts after 5 seconds
            setTimeout(function() {
                const alerts = document.querySelectorAll('.alert');
                alerts.forEach(function(alert) {
                    const bsAlert = new bootstrap.Alert(alert);
                    bsAlert.close();
                });
            }, 5000);
        });
    </script>
</body>
</html>
