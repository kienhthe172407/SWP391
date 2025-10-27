<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<%-- Role comes from authenticated session; do not override here --%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contract Details - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="sidebar-header">
            <c:choose>
                <c:when test="${sessionScope.userRole == 'HR Manager'}">
                    <h4>HR Manager Dashboard</h4>
                    <p>Human Resources</p>
                </c:when>
                <c:when test="${sessionScope.userRole == 'HR'}">
                    <h4>HR Dashboard</h4>
                    <p>Human Resources</p>
                </c:when>
                <c:otherwise>
                    <h4>HR Management</h4>
                    <p>Human Resources System</p>
                </c:otherwise>
            </c:choose>
        </div>
        
        <ul class="sidebar-menu">
            <!-- <li class="menu-section">Dashboard</li>
            <li>
                <a href="${pageContext.request.contextPath}/${sessionScope.userRole == 'HR Manager' ? 'dashboard/hr-manager-dashboard.jsp' : 'dashboard/hr-dashboard.jsp'}">
                    <i class="fas fa-home"></i>
                    <span>Overview</span>
                </a>
            </li> -->

            <c:choose>
                <c:when test="${sessionScope.userRole == 'HR Manager'}">
                    <!-- HR Manager Menu -->
                    <li class="menu-section">HR Management</li>
                    <li>
                        <a href="#">
                            <i class="fas fa-users-cog"></i>
                            <span>HR Staff Management</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-tasks"></i>
                            <span>Task Assignment</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-clipboard-check"></i>
                            <span>Approval Queue</span>
                        </a>
                    </li>

                    <li class="menu-section">Employee Management</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/employees/list">
                            <i class="fas fa-users"></i>
                            <span>All Employees</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/employees/addInformation">
                            <i class="fas fa-user-plus"></i>
                            <span>Add Employee Information</span>
                        </a>
                    </li>

                    <li class="menu-section">Contracts & Attendance</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/contracts/list" class="active">
                            <i class="fas fa-file-contract"></i>
                            <span>Contracts</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-clock"></i>
                            <span>Attendance</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-calendar-check"></i>
                            <span>Leave Requests</span>
                        </a>
                    </li>

                    <li class="menu-section">Recruitment</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/job-posting-mgt/list-job-postings.jsp">
                            <i class="fas fa-briefcase"></i>
                            <span>Job Postings</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/job-posting-mgt/create-job-posting.jsp">
                            <i class="fas fa-plus"></i>
                            <span>Create Job Posting</span>
                        </a>
                    </li>

                    <li class="menu-section">Payroll & Benefits</li>
                    <li>
                        <a href="#">
                            <i class="fas fa-dollar-sign"></i>
                            <span>Payroll</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-gift"></i>
                            <span>Benefits</span>
                        </a>
                    </li>

                    <li class="menu-section">Reports & Analytics</li>
                    <li>
                        <a href="#">
                            <i class="fas fa-chart-bar"></i>
                            <span>HR Reports</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-chart-line"></i>
                            <span>Analytics</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-chart-pie"></i>
                            <span>Statistics</span>
                        </a>
                    </li>
                </c:when>
                <c:otherwise>
                    <!-- HR Staff Menu -->
                    <li class="menu-section">Dashboard</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/dashboard/hr-dashboard.jsp">
                            <i class="fas fa-home"></i>
                            <span>Overview</span>
                        </a>
                    </li>

                    <li class="menu-section">Employee Management</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/employees/list">
                            <i class="fas fa-users"></i>
                            <span>All Employees</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/employees/addInformation">
                            <i class="fas fa-user-plus"></i>
                            <span>Add Employee Information</span>
                        </a>
                    </li>

                    <li class="menu-section">Contracts & Attendance</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/contracts/list" class="active">
                            <i class="fas fa-file-contract"></i>
                            <span>Contracts</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-clock"></i>
                            <span>Attendance</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-calendar-check"></i>
                            <span>Leave Requests</span>
                        </a>
                    </li>

                    <li class="menu-section">Recruitment</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/job-posting-mgt/list-job-postings.jsp">
                            <i class="fas fa-briefcase"></i>
                            <span>Job Postings</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/job-posting-mgt/create-job-posting.jsp">
                            <i class="fas fa-plus"></i>
                            <span>Create Job Posting</span>
                        </a>
                    </li>

                    <li class="menu-section">Payroll & Benefits</li>
                    <li>
                        <a href="#">
                            <i class="fas fa-dollar-sign"></i>
                            <span>Payroll</span>
                        </a>
                    </li>
                    <li>
                        <a href="#">
                            <i class="fas fa-gift"></i>
                            <span>Benefits</span>
                        </a>
                    </li>
                </c:otherwise>
            </c:choose>

            <!-- <li class="menu-section">System</li>
            <li>
                <a href="#">
                    <i class="fas fa-cog"></i>
                    <span>Settings</span>
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-sign-out-alt"></i>
                    <span>Logout</span>
                </a>
            </li> -->
        </ul>
    </div>
    
    <!-- Main Content -->
    <div class="main-content">
        <!-- Top Header -->
        <div class="top-header">
            <h1>Contract Details</h1>
            <div class="user-info">
                <span>HR Management</span>
                <div class="avatar">HR</div>
            </div>
        </div>
        
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/contracts/list">Contracts</a></li>
                <li class="breadcrumb-item active">Contract #${contract.contractID}</li>
            </ol>
        </nav>
        
        <!-- Content Area -->
        <div class="content-area">
            <!-- Contract Header Card -->
            <div class="card mb-4">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <h5 class="mb-0">
                        <i class="fas fa-file-contract me-2"></i>Contract #${contract.contractID}
                        <c:if test="${not empty contract.contractNumber}">
                            - ${contract.contractNumber}
                        </c:if>
                    </h5>
                    <div>
                        <c:choose>
                            <c:when test="${contract.contractStatus == 'Active'}">
                                <span class="badge badge-active fs-6">Active</span>
                            </c:when>
                            <c:when test="${contract.contractStatus == 'Expired'}">
                                <span class="badge badge-expired fs-6">Expired</span>
                            </c:when>
                            <c:when test="${contract.contractStatus == 'Pending Approval'}">
                                <span class="badge badge-pending fs-6">Pending Approval</span>
                            </c:when>
                            <c:when test="${contract.contractStatus == 'Draft'}">
                                <span class="badge bg-secondary fs-6">Draft</span>
                            </c:when>
                            <c:when test="${contract.contractStatus == 'Terminated'}">
                                <span class="badge bg-danger fs-6">Terminated</span>
                            </c:when>
                            <c:when test="${contract.contractStatus == 'Rejected'}">
                                <span class="badge bg-danger fs-6">Rejected</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge bg-secondary fs-6">${contract.contractStatus}</span>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <h6 class="text-muted">Employee Information</h6>
                            <p class="mb-1"><strong>Name:</strong> ${contract.employeeFullName != null ? contract.employeeFullName : 'N/A'}</p>
                            <p class="mb-1"><strong>Employee Code:</strong> ${contract.employeeCode != null ? contract.employeeCode : 'N/A'}</p>
                            <p class="mb-1"><strong>Phone:</strong> ${contract.employeePhone != null ? contract.employeePhone : 'N/A'}</p>
                            <p class="mb-0"><strong>Email:</strong> ${contract.employeeEmail != null ? contract.employeeEmail : 'N/A'}</p>
                        </div>
                        <div class="col-md-6">
                            <h6 class="text-muted">Contract Information</h6>
                            <p class="mb-1"><strong>Type:</strong> ${contract.contractType != null ? contract.contractType : 'N/A'}</p>
                            <p class="mb-1"><strong>Start Date:</strong> <fmt:formatDate value="${contract.startDate}" pattern="dd/MM/yyyy"/></p>
                            <p class="mb-1"><strong>End Date:</strong> 
                                <c:choose>
                                    <c:when test="${contract.endDate != null}">
                                        <fmt:formatDate value="${contract.endDate}" pattern="dd/MM/yyyy"/>
                                    </c:when>
                                    <c:otherwise>
                                        Indefinite
                                    </c:otherwise>
                                </c:choose>
                            </p>
                            <p class="mb-0"><strong>Salary:</strong> 
                                <c:if test="${contract.salaryAmount != null}">
                                    <fmt:formatNumber value="${contract.salaryAmount}" type="currency" currencySymbol="$"/>
                                </c:if>
                                <c:if test="${contract.salaryAmount == null}">N/A</c:if>
                            </p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Contract Details -->
            <div class="row">
                <div class="col-md-8">
                    <!-- Job Description -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h6 class="mb-0"><i class="fas fa-briefcase me-2"></i>Job Description</h6>
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${not empty contract.jobDescription}">
                                    <p class="mb-0">${contract.jobDescription}</p>
                                </c:when>
                                <c:otherwise>
                                    <p class="text-muted mb-0">No job description provided.</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- Terms and Conditions -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h6 class="mb-0"><i class="fas fa-file-alt me-2"></i>Terms and Conditions</h6>
                        </div>
                        <div class="card-body">
                            <c:choose>
                                <c:when test="${not empty contract.termsAndConditions}">
                                    <p class="mb-0">${contract.termsAndConditions}</p>
                                </c:when>
                                <c:otherwise>
                                    <p class="text-muted mb-0">No terms and conditions specified.</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>

                <div class="col-md-4">
                    <!-- Status Information -->
                    <div class="card mb-4">
                        <div class="card-header">
                            <h6 class="mb-0"><i class="fas fa-info-circle me-2"></i>Status Information</h6>
                        </div>
                        <div class="card-body">
                            <p class="mb-2"><strong>Current Status:</strong> ${contract.contractStatus}</p>
                            
                            <c:if test="${contract.signedDate != null}">
                                <p class="mb-2"><strong>Signed Date:</strong> <fmt:formatDate value="${contract.signedDate}" pattern="dd/MM/yyyy"/></p>
                            </c:if>
                            
                            <c:if test="${contract.approvedAt != null}">
                                <p class="mb-2"><strong>Approved/Rejected At:</strong> <fmt:formatDate value="${contract.approvedAt}" pattern="dd/MM/yyyy HH:mm"/></p>
                            </c:if>
                            
                            <p class="mb-2"><strong>Created At:</strong> <fmt:formatDate value="${contract.createdAt}" pattern="dd/MM/yyyy HH:mm:ss"/></p>
                            
                            <c:if test="${contract.updatedAt != null}">
                                <p class="mb-0"><strong>Last Updated:</strong> <fmt:formatDate value="${contract.updatedAt}" pattern="dd/MM/yyyy HH:mm"/></p>
                            </c:if>
                        </div>
                    </div>

                    <!-- Approval/Rejection Information -->
                    <c:if test="${contract.contractStatus == 'Rejected' and not empty contract.approvalComment}">
                        <div class="card mb-4 border-danger">
                            <div class="card-header bg-danger text-white">
                                <h6 class="mb-0"><i class="fas fa-times-circle me-2"></i>Rejection Reason</h6>
                            </div>
                            <div class="card-body">
                                <p class="mb-0 text-danger">${contract.approvalComment}</p>
                            </div>
                        </div>
                    </c:if>
                    
                    <c:if test="${contract.contractStatus == 'Active' and not empty contract.approvalComment}">
                        <div class="card mb-4 border-success">
                            <div class="card-header bg-success text-white">
                                <h6 class="mb-0"><i class="fas fa-check-circle me-2"></i>Approval Comment</h6>
                            </div>
                            <div class="card-body">
                                <p class="mb-0 text-success">${contract.approvalComment}</p>
                            </div>
                        </div>
                    </c:if>

                    <!-- Actions -->
                    <div class="card">
                        <div class="card-header">
                            <h6 class="mb-0"><i class="fas fa-cogs me-2"></i>Actions</h6>
                        </div>
                        <div class="card-body">
                            <div class="d-grid gap-2">
                                <a href="${pageContext.request.contextPath}/contracts/list" class="btn btn-secondary">
                                    <i class="fas fa-arrow-left me-1"></i>Back to List
                                </a>
                                
                                <c:if test="${contract.contractStatus == 'Draft' and sessionScope.userId == contract.createdBy}">
                                    <a href="${pageContext.request.contextPath}/contracts/edit?id=${contract.contractID}" class="btn btn-primary">
                                        <i class="fas fa-edit me-1"></i>Edit Contract
                                    </a>
                                </c:if>
                                
                                <c:if test="${contract.contractStatus == 'Pending Approval' and sessionScope.userRole == 'HR Manager'}">
                                    <button type="button" class="btn btn-success approve-btn" 
                                            data-contract-id="${contract.contractID}" 
                                            data-employee-name="${contract.employeeFullName}">
                                        <i class="fas fa-check me-1"></i>Approve
                                    </button>
                                    <button type="button" class="btn btn-danger reject-btn" 
                                            data-contract-id="${contract.contractID}" 
                                            data-employee-name="${contract.employeeFullName}">
                                        <i class="fas fa-times me-1"></i>Reject
                                    </button>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Approve Contract Modal -->
    <div class="modal fade" id="approveModal" tabindex="-1" aria-labelledby="approveModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="approveModalLabel">Approve Contract</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form id="approveForm" method="POST" action="${pageContext.request.contextPath}/contracts/approve">
                    <div class="modal-body">
                        <p>Are you sure you want to approve the contract for <strong id="approveEmployeeName"></strong>?</p>
                        <div class="mb-3">
                            <label for="approveComment" class="form-label">Approval Comment (Optional)</label>
                            <textarea class="form-control" id="approveComment" name="comment" rows="3"
                                      placeholder="Enter any comments about the approval..."></textarea>
                        </div>
                        <input type="hidden" id="approveContractId" name="contractId" value="">
                        <input type="hidden" name="action" value="approve">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-success">
                            <i class="fas fa-check me-1"></i>Approve Contract
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Reject Contract Modal -->
    <div class="modal fade" id="rejectModal" tabindex="-1" aria-labelledby="rejectModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="rejectModalLabel">Reject Contract</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form id="rejectForm" method="POST" action="${pageContext.request.contextPath}/contracts/approve">
                    <div class="modal-body">
                        <p>Are you sure you want to reject the contract for <strong id="rejectEmployeeName"></strong>?</p>
                        <div class="mb-3">
                            <label for="rejectComment" class="form-label">Rejection Reason <span class="text-danger">*</span></label>
                            <textarea class="form-control" id="rejectComment" name="comment" rows="3"
                                      placeholder="Please provide a reason for rejecting this contract..." required></textarea>
                            <div class="invalid-feedback">
                                Please provide a reason for rejection.
                            </div>
                        </div>
                        <input type="hidden" id="rejectContractId" name="contractId" value="">
                        <input type="hidden" name="action" value="reject">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-times me-1"></i>Reject Contract
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Contract Approval/Rejection JavaScript -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Approve button handlers
            document.querySelectorAll('.approve-btn').forEach(function(btn) {
                btn.addEventListener('click', function() {
                    const contractId = this.getAttribute('data-contract-id');
                    const employeeName = this.getAttribute('data-employee-name');

                    document.getElementById('approveContractId').value = contractId;
                    document.getElementById('approveEmployeeName').textContent = employeeName;

                    const approveModal = new bootstrap.Modal(document.getElementById('approveModal'));
                    approveModal.show();
                });
            });

            // Reject button handlers
            document.querySelectorAll('.reject-btn').forEach(function(btn) {
                btn.addEventListener('click', function() {
                    const contractId = this.getAttribute('data-contract-id');
                    const employeeName = this.getAttribute('data-employee-name');

                    document.getElementById('rejectContractId').value = contractId;
                    document.getElementById('rejectEmployeeName').textContent = employeeName;

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
