<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Employment Contract - HR Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    <style>
        .contract-card {
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 20px;
            margin-bottom: 20px;
            transition: all 0.3s ease;
        }
        
        .contract-card:hover {
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            transform: translateY(-2px);
        }
        
        .contract-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
            padding-bottom: 15px;
            border-bottom: 2px solid #e9ecef;
        }
        
        .contract-number {
            font-size: 1.2rem;
            font-weight: 600;
            color: #2c3e50;
        }
        
        .status-badge {
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 0.85rem;
            font-weight: 500;
        }
        
        .status-active {
            background-color: #d4edda;
            color: #155724;
        }
        
        .status-expired {
            background-color: #f8d7da;
            color: #721c24;
        }
        
        .status-pending {
            background-color: #fff3cd;
            color: #856404;
        }
        
        .status-draft {
            background-color: #e2e3e5;
            color: #383d41;
        }
        
        .status-terminated {
            background-color: #f8d7da;
            color: #721c24;
        }
        
        .contract-detail-row {
            display: flex;
            padding: 10px 0;
            border-bottom: 1px solid #f0f0f0;
        }
        
        .contract-detail-row:last-child {
            border-bottom: none;
        }
        
        .detail-label {
            font-weight: 600;
            color: #495057;
            width: 200px;
            flex-shrink: 0;
        }
        
        .detail-value {
            color: #212529;
            flex-grow: 1;
        }
        
        .salary-amount {
            font-size: 1.3rem;
            font-weight: 700;
            color: #28a745;
        }
        
        .contract-section {
            margin-top: 20px;
            padding-top: 20px;
            border-top: 2px solid #e9ecef;
        }
        
        .section-title {
            font-size: 1.1rem;
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 15px;
        }
        
        .no-contracts {
            text-align: center;
            padding: 60px 20px;
        }
        
        .no-contracts i {
            font-size: 4rem;
            color: #dee2e6;
            margin-bottom: 20px;
        }
        
        .contract-list-item {
            cursor: pointer;
            padding: 15px;
            border: 1px solid #dee2e6;
            border-radius: 6px;
            margin-bottom: 10px;
            transition: all 0.2s ease;
        }
        
        .contract-list-item:hover {
            background-color: #f8f9fa;
            border-color: #0d6efd;
        }
        
        .contract-list-item.active {
            background-color: #e7f3ff;
            border-color: #0d6efd;
        }
        
        /* Card header icon styling */
        .card-header i {
            color: white !important;
        }
        
        /* Specific styling for My Contracts header icon */
        .card-header:not(.bg-primary):not(.bg-success):not(.bg-warning) i {
            color: black !important;
        }
    </style>
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="My Employment Contract" />
        </jsp:include>

        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <!-- <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li> -->
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard/employee-dashboard.jsp">Dashboard</a></li>
                <li class="breadcrumb-item active">My Contract</li>
            </ol>
        </nav>

        <!-- Content Area -->
        <div class="content-area">
            <!-- Error/Success Messages -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <c:if test="${not empty successMessage}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>${successMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Employee Information Card -->
            <div class="card mb-4">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0"><i class="fas fa-user me-2"></i>Employee Information</h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6">
                            <p><strong>Name:</strong> ${employee.firstName} ${employee.lastName}</p>
                            <p><strong>Employee Code:</strong> ${employee.employeeCode}</p>
                            <p><strong>Email:</strong> ${employee.personalEmail}</p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Department:</strong> ${employee.departmentName != null ? employee.departmentName : 'N/A'}</p>
                            <p><strong>Position:</strong> ${employee.positionName != null ? employee.positionName : 'N/A'}</p>
                            <p><strong>Hire Date:</strong> <fmt:formatDate value="${employee.hireDate}" pattern="MMM dd, yyyy"/></p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Contract Status Information -->
            <c:if test="${not empty nonActiveContracts}">
                <div class="card mb-4">
                    <div class="card-header bg-warning text-dark">
                        <h5 class="mb-0"><i class="fas fa-exclamation-triangle me-2"></i>Contract Status Information</h5>
                    </div>
                    <div class="card-body">
                        <div class="alert alert-info" role="alert">
                            <h6 class="alert-heading"><i class="fas fa-info-circle me-2"></i>Notice</h6>
                            <p class="mb-0">You have contracts with non-active status. Only active contracts are displayed below.</p>
                        </div>
                        
                        <div class="row">
                            <c:forEach var="contract" items="${nonActiveContracts}">
                                <div class="col-md-6 mb-3">
                                    <div class="card border-warning">
                                        <div class="card-body">
                                            <div class="d-flex justify-content-between align-items-start mb-2">
                                                <h6 class="card-title mb-0">
                                                    ${contract.contractNumber != null ? contract.contractNumber : 'Contract #' += contract.contractID}
                                                </h6>
                                                <span class="badge bg-warning text-dark">
                                                    ${contract.contractStatus}
                                                </span>
                                            </div>
                                            <p class="card-text">
                                                <small class="text-muted">
                                                    <i class="fas fa-calendar me-1"></i>
                                                    <fmt:formatDate value="${contract.startDate}" pattern="MMM dd, yyyy"/>
                                                    <c:if test="${contract.endDate != null}">
                                                        - <fmt:formatDate value="${contract.endDate}" pattern="MMM dd, yyyy"/>
                                                    </c:if>
                                                </small>
                                            </p>
                                            <p class="card-text">
                                                <small class="text-muted">
                                                    <i class="fas fa-briefcase me-1"></i>${contract.contractType}
                                                </small>
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </div>
                </div>
            </c:if>

            <c:choose>
                <c:when test="${empty contracts}">
                    <!-- No Active Contracts Found -->
                    <div class="card">
                        <div class="card-body">
                            <div class="no-contracts">
                                <i class="fas fa-file-contract"></i>
                                <h4>No Active Employment Contracts</h4>
                                <c:choose>
                                    <c:when test="${not empty allContracts}">
                                        <p class="text-muted">You don't have any active employment contracts at the moment.</p>
                                        <p class="text-muted">Please check the contract status information above for details about your other contracts.</p>
                                    </c:when>
                                    <c:otherwise>
                                        <p class="text-muted">You don't have any employment contracts in the system yet.</p>
                                        <p class="text-muted">Please contact HR if you believe this is an error.</p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <!-- Contracts Display -->
                    <div class="row">
                        <!-- Contract List (Left Side) -->
                        <div class="col-md-4">
                            <div class="card">
                                <div class="card-header">
                                    <h5 class="mb-0"><i class="fas fa-list me-2"></i>My Contracts (${contracts.size()})</h5>
                                </div>
                                <div class="card-body" style="max-height: 600px; overflow-y: auto;">
                                    <c:forEach var="contract" items="${contracts}">
                                        <a href="${pageContext.request.contextPath}/employee/my-contract?id=${contract.contractID}" 
                                           class="text-decoration-none">
                                            <div class="contract-list-item ${selectedContract != null && selectedContract.contractID == contract.contractID ? 'active' : ''}">
                                                <div class="d-flex justify-content-between align-items-start mb-2">
                                                    <strong>${contract.contractNumber != null ? contract.contractNumber : 'Contract #' += contract.contractID}</strong>
                                                    <span class="status-badge status-${contract.contractStatus.toLowerCase().replace(' ', '-')}">
                                                        ${contract.contractStatus}
                                                    </span>
                                                </div>
                                                <small class="text-muted">
                                                    <i class="fas fa-calendar me-1"></i>
                                                    <fmt:formatDate value="${contract.startDate}" pattern="MMM dd, yyyy"/>
                                                    <c:if test="${contract.endDate != null}">
                                                        - <fmt:formatDate value="${contract.endDate}" pattern="MMM dd, yyyy"/>
                                                    </c:if>
                                                </small>
                                                <br/>
                                                <small class="text-muted">
                                                    <i class="fas fa-briefcase me-1"></i>${contract.contractType}
                                                </small>
                                            </div>
                                        </a>
                                    </c:forEach>
                                </div>
                            </div>
                        </div>

                        <!-- Contract Details (Right Side) -->
                        <div class="col-md-8">
                            <c:choose>
                                <c:when test="${selectedContract != null}">
                                    <!-- Detailed Contract View -->
                                    <div class="card">
                                        <div class="card-header bg-success text-white">
                                            <h5 class="mb-0"><i class="fas fa-file-contract me-2"></i>Contract Details</h5>
                                        </div>
                                        <div class="card-body">
                                            <!-- Contract Header -->
                                            <div class="contract-header">
                                                <div>
                                                    <div class="contract-number">
                                                        ${selectedContract.contractNumber != null ? selectedContract.contractNumber : 'Contract #' += selectedContract.contractID}
                                                    </div>
                                                    <small class="text-muted">Contract ID: ${selectedContract.contractID}</small>
                                                </div>
                                                <span class="status-badge status-${selectedContract.contractStatus.toLowerCase().replace(' ', '-')}">
                                                    ${selectedContract.contractStatus}
                                                </span>
                                            </div>

                                            <!-- Basic Information -->
                                            <div class="contract-section">
                                                <div class="section-title">
                                                    <i class="fas fa-info-circle me-2"></i>Basic Information
                                                </div>

                                                <div class="contract-detail-row">
                                                    <div class="detail-label">Contract Type:</div>
                                                    <div class="detail-value">
                                                        <span class="badge bg-info">${selectedContract.contractType}</span>
                                                    </div>
                                                </div>

                                                <div class="contract-detail-row">
                                                    <div class="detail-label">Start Date:</div>
                                                    <div class="detail-value">
                                                        <fmt:formatDate value="${selectedContract.startDate}" pattern="MMMM dd, yyyy"/>
                                                    </div>
                                                </div>

                                                <div class="contract-detail-row">
                                                    <div class="detail-label">End Date:</div>
                                                    <div class="detail-value">
                                                        <c:choose>
                                                            <c:when test="${selectedContract.endDate != null}">
                                                                <fmt:formatDate value="${selectedContract.endDate}" pattern="MMMM dd, yyyy"/>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-success">Indefinite</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </div>

                                                <c:if test="${selectedContract.signedDate != null}">
                                                    <div class="contract-detail-row">
                                                        <div class="detail-label">Signed Date:</div>
                                                        <div class="detail-value">
                                                            <fmt:formatDate value="${selectedContract.signedDate}" pattern="MMMM dd, yyyy"/>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>

                                            <!-- Salary Information -->
                                            <div class="contract-section">
                                                <div class="section-title">
                                                    <i class="fas fa-dollar-sign me-2"></i>Salary Information
                                                </div>

                                                <div class="contract-detail-row">
                                                    <div class="detail-label">Salary Amount:</div>
                                                    <div class="detail-value">
                                                        <c:choose>
                                                            <c:when test="${selectedContract.salaryAmount != null}">
                                                                <span class="salary-amount">
                                                                    $<fmt:formatNumber value="${selectedContract.salaryAmount}" pattern="#,##0.00"/>
                                                                </span>
                                                                <small class="text-muted ms-2">per month</small>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="text-muted">Not specified</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                </div>
                                            </div>

                                            <!-- Job Description -->
                                            <c:if test="${selectedContract.jobDescription != null && !selectedContract.jobDescription.trim().isEmpty()}">
                                                <div class="contract-section">
                                                    <div class="section-title">
                                                        <i class="fas fa-briefcase me-2"></i>Job Description
                                                    </div>
                                                    <div class="detail-value" style="white-space: pre-wrap;">${selectedContract.jobDescription}</div>
                                                </div>
                                            </c:if>

                                            <!-- Terms and Conditions -->
                                            <c:if test="${selectedContract.termsAndConditions != null && !selectedContract.termsAndConditions.trim().isEmpty()}">
                                                <div class="contract-section">
                                                    <div class="section-title">
                                                        <i class="fas fa-file-alt me-2"></i>Terms and Conditions
                                                    </div>
                                                    <div class="detail-value" style="white-space: pre-wrap;">${selectedContract.termsAndConditions}</div>
                                                </div>
                                            </c:if>

                                            <!-- Approval Information (if approved) -->
                                            <c:if test="${selectedContract.contractStatus == 'Active' && selectedContract.approvedAt != null}">
                                                <div class="contract-section">
                                                    <div class="section-title">
                                                        <i class="fas fa-check-circle me-2"></i>Approval Information
                                                    </div>

                                                    <div class="contract-detail-row">
                                                        <div class="detail-label">Approved Date:</div>
                                                        <div class="detail-value">
                                                            <fmt:formatDate value="${selectedContract.approvedAt}" pattern="MMMM dd, yyyy HH:mm"/>
                                                        </div>
                                                    </div>

                                                    <c:if test="${selectedContract.approvalComment != null && !selectedContract.approvalComment.trim().isEmpty()}">
                                                        <div class="contract-detail-row">
                                                            <div class="detail-label">Approval Comment:</div>
                                                            <div class="detail-value">${selectedContract.approvalComment}</div>
                                                        </div>
                                                    </c:if>
                                                </div>
                                            </c:if>

                                            <!-- Contract Metadata -->
                                            <div class="contract-section">
                                                <div class="section-title">
                                                    <i class="fas fa-info me-2"></i>Contract Information
                                                </div>

                                                <div class="contract-detail-row">
                                                    <div class="detail-label">Created Date:</div>
                                                    <div class="detail-value">
                                                        <fmt:formatDate value="${selectedContract.createdAt}" pattern="MMMM dd, yyyy HH:mm"/>
                                                    </div>
                                                </div>

                                                <c:if test="${selectedContract.updatedAt != null}">
                                                    <div class="contract-detail-row">
                                                        <div class="detail-label">Last Updated:</div>
                                                        <div class="detail-value">
                                                            <fmt:formatDate value="${selectedContract.updatedAt}" pattern="MMMM dd, yyyy HH:mm"/>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </div>

                                            <!-- Action Buttons -->
                                            <div class="mt-4 d-flex gap-2">
                                                <button onclick="window.print()" class="btn btn-primary">
                                                    <i class="fas fa-print me-2"></i>Print Contract
                                                </button>
                                                <a href="${pageContext.request.contextPath}/employee/my-contract" class="btn btn-secondary">
                                                    <i class="fas fa-list me-2"></i>View All Contracts
                                                </a>
                                            </div>
                                        </div>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <!-- No Contract Selected -->
                                    <div class="card">
                                        <div class="card-body text-center py-5">
                                            <i class="fas fa-hand-pointer fa-3x text-muted mb-3"></i>
                                            <h5>Select a Contract</h5>
                                            <p class="text-muted">Please select a contract from the list on the left to view its details.</p>
                                        </div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
