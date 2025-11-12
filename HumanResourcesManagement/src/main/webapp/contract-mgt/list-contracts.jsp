<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<%-- Role will be read from sessionScope.user set at login; no hardcoded defaults here --%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Contracts List - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    
    <style>
        /* Contract Card Styles - Based on my-contract.jsp */
        .contract-card {
            border: 1px solid #dee2e6;
            border-radius: 8px;
            margin-bottom: 20px;
            transition: all 0.3s ease;
            background-color: #fff;
            height: 100%;
            display: flex;
            flex-direction: column;
        }
        
        .contract-card:hover {
            box-shadow: 0 4px 8px rgba(0,0,0,0.1);
            transform: translateY(-2px);
        }
        
        /* Card header icon styling */
        .contract-card .card-header i {
            color: white !important;
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
            margin: 0;
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
        
        .status-rejected {
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
        
        
        .contract-approval-actions {
            margin-top: 15px;
            padding: 10px;
            background-color: #f8f9fa;
            border-radius: 6px;
        }
        
        .alert-sm {
            padding: 0.5rem 0.75rem;
            font-size: 0.8125rem;
            margin-bottom: 0;
        }
        
        /* Remove border from Edit button and set white color */
        .contract-card .btn-warning,
        .contract-card .btn-warning:hover,
        .contract-card .btn-warning:focus,
        .contract-card .btn-warning:active {
            border: none !important;
            color: #fff !important;
        }
        
        .contract-card .btn-warning i,
        .contract-card .btn-warning:hover i,
        .contract-card .btn-warning:focus i,
        .contract-card .btn-warning:active i {
            color: #fff !important;
        }
        
        /* Pagination Styles */
        .pagination-wrapper {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 20px;
            padding-top: 20px;
            border-top: 1px solid #dee2e6;
        }
        
        .pagination {
            margin-bottom: 0;
        }
        
        .page-item.active .page-link {
            background-color: #0d6efd !important;
            border-color: #0d6efd !important;
            color: white !important;
            font-weight: 600 !important;
            z-index: 3;
        }
        
        .page-item.active .page-link:hover {
            background-color: #0a58ca !important;
            border-color: #0a58ca !important;
            color: white !important;
        }
        
        .page-item .page-link {
            color: #0d6efd !important;
            border: 1px solid #dee2e6 !important;
            padding: 0.5rem 0.75rem;
            transition: all 0.3s ease;
        }
        
        .page-item .page-link:hover {
            background-color: #e9ecef !important;
            border-color: #dee2e6 !important;
            color: #0a58ca !important;
        }
        
        .page-item.disabled .page-link {
            color: #6c757d;
            pointer-events: none;
            background-color: #fff;
            border-color: #dee2e6;
        }
        
        .pagination-info {
            color: #6c757d;
            font-size: 0.875rem;
        }
        
        /* Responsive adjustments */
        @media (max-width: 768px) {
            .contract-card {
                margin-bottom: 1rem;
            }
            
            .detail-label {
                width: 150px;
                font-size: 0.875rem;
            }
            
            .detail-value {
                font-size: 0.875rem;
            }
            
            .contract-number {
                font-size: 1rem;
            }
            
            .pagination-wrapper {
                flex-direction: column;
                gap: 15px;
            }
            
            .pagination-info {
                order: -1;
            }
        }
    </style>
</head>
<body>
    <c:if test="${empty requestScope.contracts}">
        <c:redirect url="/contracts/list"/>
    </c:if>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="Contracts Management" />
        </jsp:include>
        
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="#">Contract & Attendance</a></li>
                <li class="breadcrumb-item active">Contracts List</li>
            </ol>
        </nav>
        
        <!-- Content Area -->
        <div class="content-area">
            <!-- Success/Error Messages -->
            <c:if test="${not empty sessionScope.successMessage}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>${sessionScope.successMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="successMessage" scope="session" />
            </c:if>
            
            <c:if test="${not empty sessionScope.errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${sessionScope.errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="errorMessage" scope="session" />
            </c:if>
            
            <!-- Role selection removed -->
            
            <!-- Search Card -->
            <div class="card">
                <div class="card-header">
                    <i class="fas fa-search me-2"></i>Search Contracts
                </div>
                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/contracts/list" method="GET">
                        <div class="row g-3">
                            <div class="col-md-4">
                                <label class="form-label">Keyword</label>
                                <input type="text" name="keyword" class="form-control" 
                                       placeholder="Employee name, contract type..." 
                                       value="${keyword}">
                            </div>
                            <div class="col-md-3">
                                <label class="form-label">Status</label>
                                <select name="status" class="form-select">
                                    <option value="">All Status</option>
                                    <c:forEach var="statusOption" items="${contractStatuses}">
                                        <option value="${statusOption}" ${status == statusOption ? 'selected' : ''}>${statusOption}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-5 d-flex align-items-end gap-2">
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-search me-1"></i>Search
                                </button>
                                <a href="${pageContext.request.contextPath}/contracts/list" class="btn btn-secondary">
                                    <i class="fas fa-redo me-1"></i>Reset
                                </a>
                                <a href="${pageContext.request.contextPath}/contracts/create" class="btn btn-success ms-auto">
                                    <i class="fas fa-plus me-1"></i>New Contract
                                </a>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
            
            <!-- Contracts Cards -->
            <div class="table-wrapper">
                <div class="table-header">
                    <h5>Contracts List</h5>
                    <div class="table-stats">
                        <i class="fas fa-file-alt me-1"></i>
                        Total: <strong>${totalRecords}</strong> contract(s) | Page <strong>${currentPage}</strong> of <strong>${totalPages}</strong>
                    </div>
                </div>
                
                <c:choose>
                    <c:when test="${empty contracts}">
                        <div class="empty-state">
                            <i class="fas fa-inbox"></i>
                            <h4>No Contracts Found</h4>
                            <p>There are no contracts in the system or no matches for your search criteria.</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="container-fluid p-3">
                            <div class="row g-3">
                                <c:forEach var="contract" items="${contracts}">
                                    <div class="col-md-6">
                                        <div class="card contract-card">
                                            <div class="card-header bg-success text-white">
                                                <h5 class="mb-0"><i class="fas fa-file-contract me-2"></i>Contract Details</h5>
                                            </div>
                                            <div class="card-body">
                                            <!-- Contract Header -->
                                            <div class="contract-header">
                                                <div>
                                                    <div class="contract-number">
                                                        <c:choose>
                                                            <c:when test="${contract.contractNumber != null}">
                                                                ${contract.contractNumber}
                                                            </c:when>
                                                            <c:otherwise>
                                                                Contract #${contract.contractID}
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </div>
                                                    <small class="text-muted">Contract ID: ${contract.contractID}</small>
                                                </div>
                                                <c:choose>
                                                    <c:when test="${contract.contractStatus == 'Active'}">
                                                        <span class="status-badge status-active">${contract.contractStatus}</span>
                                                    </c:when>
                                                    <c:when test="${contract.contractStatus == 'Expired'}">
                                                        <span class="status-badge status-expired">${contract.contractStatus}</span>
                                                    </c:when>
                                                    <c:when test="${contract.contractStatus == 'Pending Approval'}">
                                                        <span class="status-badge status-pending">${contract.contractStatus}</span>
                                                    </c:when>
                                                    <c:when test="${contract.contractStatus == 'Draft'}">
                                                        <span class="status-badge status-draft">${contract.contractStatus}</span>
                                                    </c:when>
                                                    <c:when test="${contract.contractStatus == 'Terminated'}">
                                                        <span class="status-badge status-terminated">${contract.contractStatus}</span>
                                                    </c:when>
                                                    <c:when test="${contract.contractStatus == 'Rejected'}">
                                                        <span class="status-badge status-rejected">${contract.contractStatus}</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="status-badge status-pending">${contract.contractStatus}</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>

                                            <!-- Basic Information -->
                                            <div class="contract-detail-row">
                                                <div class="detail-label">Employee:</div>
                                                <div class="detail-value">
                                                    ${contract.employeeFullName != null ? contract.employeeFullName : 'N/A'}
                                                    <c:if test="${contract.employeeCode != null}">
                                                        <small class="text-muted ms-1">(${contract.employeeCode})</small>
                                                    </c:if>
                                                </div>
                                            </div>

                                            <div class="contract-detail-row">
                                                <div class="detail-label">Contract Type:</div>
                                                <div class="detail-value">
                                                    ${contract.contractType != null ? contract.contractType : 'N/A'}
                                                </div>
                                            </div>

                                            <div class="contract-detail-row">
                                                <div class="detail-label">Start Date:</div>
                                                <div class="detail-value">
                                                    <fmt:formatDate value="${contract.startDate}" pattern="dd/MM/yyyy"/>
                                                </div>
                                            </div>

                                            <div class="contract-detail-row">
                                                <div class="detail-label">End Date:</div>
                                                <div class="detail-value">
                                                    <c:choose>
                                                        <c:when test="${contract.endDate != null}">
                                                            <fmt:formatDate value="${contract.endDate}" pattern="dd/MM/yyyy"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="text-muted">Not specified</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </div>
                                            </div>

                                            <div class="contract-detail-row">
                                                <div class="detail-label">Contact:</div>
                                                <div class="detail-value">
                                                    <c:if test="${contract.employeePhone != null}">
                                                        <div><small><i class="fas fa-phone me-1"></i>${contract.employeePhone}</small></div>
                                                    </c:if>
                                                    <c:if test="${contract.employeeEmail != null}">
                                                        <div><small><i class="fas fa-envelope me-1"></i>${contract.employeeEmail}</small></div>
                                                    </c:if>
                                                    <c:if test="${contract.employeePhone == null && contract.employeeEmail == null}">
                                                        <span class="text-muted">N/A</span>
                                                    </c:if>
                                                </div>
                                            </div>

                                            <!-- Approval Actions for Pending Contracts -->
                                            <c:if test="${contract.contractStatus == 'Pending Approval' && sessionScope.user != null && sessionScope.user.role == 'HR Manager'}">
                                                <div class="contract-approval-actions">
                                                    <div class="btn-group w-100" role="group">
                                                        <button type="button" class="btn btn-success btn-sm approve-btn"
                                                                data-contract-id="${contract.contractID}"
                                                                data-employee-name="${contract.employeeFullName}">
                                                            <i class="fas fa-check me-1"></i> Approve
                                                        </button>
                                                        <button type="button" class="btn btn-danger btn-sm reject-btn"
                                                                data-contract-id="${contract.contractID}"
                                                                data-employee-name="${contract.employeeFullName}">
                                                            <i class="fas fa-times me-1"></i> Reject
                                                        </button>
                                                    </div>
                                                </div>
                                            </c:if>

                                            <!-- Rejection Reason -->
                                            <c:if test="${contract.contractStatus == 'Rejected' && not empty contract.approvalComment}">
                                                <div class="alert alert-danger alert-sm mt-2">
                                                    <small><strong>Rejection Reason:</strong> ${contract.approvalComment}</small>
                                                </div>
                                            </c:if>

                                            <!-- Action Buttons Footer -->
                                            <div class="mt-4 d-flex gap-2">
                                                <a href="${pageContext.request.contextPath}/contracts/detail?id=${contract.contractID}"
                                                   class="btn btn-primary" title="View Details">
                                                    <i class="fas fa-eye me-2"></i>View
                                                </a>
                                                <a href="${pageContext.request.contextPath}/contracts/export?id=${contract.contractID}" 
                                                   class="btn btn-danger" title="Export PDF">
                                                    <i class="fas fa-file-pdf me-2"></i>Export PDF
                                                </a>
                                                <a href="${pageContext.request.contextPath}/contracts/edit?id=${contract.contractID}" 
                                                   class="btn btn-warning" title="Edit">
                                                    <i class="fas fa-edit me-2"></i>Edit
                                                </a>
                                                <a href="#" class="btn btn-danger btn-delete" title="Delete"
                                                   data-contract-id="${contract.contractID}"
                                                   data-employee-name="${contract.employeeFullName != null ? contract.employeeFullName : 'N/A'}">
                                                    <i class="fas fa-trash me-2"></i>Delete
                                                </a>
                                            </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <!-- Pagination -->
            <c:if test="${totalPages > 1}">
                <div class="pagination-wrapper">
                    <nav>
                        <ul class="pagination">
                            <!-- Previous Button -->
                            <li class="page-item" id="prevPageItem">
                                <a class="page-link" href="#" id="prevPageLink" aria-label="Previous">
                                    <i class="fas fa-chevron-left"></i>
                                </a>
                            </li>
                            
                            <!-- Page Numbers (will be populated by JavaScript) -->
                            <div id="pageNumbersContainer" style="display: contents;"></div>
                            
                            <!-- Next Button -->
                            <li class="page-item" id="nextPageItem">
                                <a class="page-link" href="#" id="nextPageLink" aria-label="Next">
                                    <i class="fas fa-chevron-right"></i>
                                </a>
                            </li>
                        </ul>
                    </nav>
                    
                    <div class="pagination-info">
                        Showing ${(currentPage - 1) * pageSize + 1} to ${currentPage * pageSize > totalRecords ? totalRecords : currentPage * pageSize} of ${totalRecords} entries
                    </div>
                </div>
            </c:if>
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
    
    <!-- Delete Contract Modal -->
    <div class="modal fade" id="deleteContractModal" tabindex="-1" aria-labelledby="deleteContractModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header bg-danger text-white">
                    <h5 class="modal-title" id="deleteContractModalLabel">
                        <i class="fas fa-exclamation-triangle me-2"></i>Delete Contract
                    </h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <form id="deleteContractForm" method="POST" action="${pageContext.request.contextPath}/contracts/delete">
                    <div class="modal-body">
                        <div class="text-center mb-4">
                            <div class="mb-3">
                                <i class="fas fa-trash-alt text-danger" style="font-size: 3rem;"></i>
                            </div>
                            <h5>Are you sure you want to delete this contract?</h5>
                            <p class="text-muted">Contract #<strong id="deleteContractId"></strong> for <strong id="deleteEmployeeName"></strong></p>
                            <p class="text-danger"><strong>Warning:</strong> This action cannot be undone.</p>
                        </div>
                        <input type="hidden" id="deleteContractIdInput" name="contractId" value="">
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            <i class="fas fa-times me-1"></i>Cancel
                        </button>
                        <button type="submit" class="btn btn-danger">
                            <i class="fas fa-trash-alt me-1"></i>Delete Contract
                        </button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Contract Approval/Rejection/Deletion JavaScript -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Get current page from URL parameter (more reliable than server-side value)
            const urlParams = new URLSearchParams(window.location.search);
            const pageFromUrl = urlParams.get('page');
            
            // Use URL param if available, otherwise use server value, default to 1
            let currentPage = pageFromUrl ? parseInt(pageFromUrl) : (${not empty currentPage ? currentPage : 1});
            const totalPages = ${not empty totalPages ? totalPages : 1};
            
            console.log('Page from URL:', pageFromUrl);
            console.log('Current Page (final):', currentPage);
            console.log('Total Pages:', totalPages);
            
            // Render page numbers dynamically
            const pageNumbersContainer = document.getElementById('pageNumbersContainer');
            
            function buildPageUrl(page) {
                const url = new URL(window.location.href);
                url.searchParams.set('page', page);
                return url.toString();
            }
            
            function renderPageNumbers() {
                let html = '';
                let lastShown = 0;
                
                // Determine which pages to show
                // Logic: Show first 3, last 3, and current +/- 1
                for (let i = 1; i <= totalPages; i++) {
                    const shouldShow = i <= 3 || 
                                      i > totalPages - 3 || 
                                      (i >= currentPage - 1 && i <= currentPage + 1);
                    
                    if (shouldShow) {
                        // Add ellipsis if there's a gap
                        if (lastShown > 0 && i > lastShown + 1) {
                            html += '<li class="page-item disabled">' +
                                    '<span class="page-link">...</span>' +
                                    '</li>';
                        }
                        
                        const activeClass = i === currentPage ? 'active' : '';
                        const pageUrl = buildPageUrl(i);
                        html += '<li class="page-item ' + activeClass + '">' +
                                '<a class="page-link" href="' + pageUrl + '">' + i + '</a>' +
                                '</li>';
                        lastShown = i;
                    }
                }
                
                pageNumbersContainer.innerHTML = html;
                console.log('✓ Rendered page numbers, current page:', currentPage, 'total:', totalPages);
            }
            
            renderPageNumbers();
            
            // Handle Previous/Next buttons
            const prevPageItem = document.getElementById('prevPageItem');
            const prevPageLink = document.getElementById('prevPageLink');
            const nextPageItem = document.getElementById('nextPageItem');
            const nextPageLink = document.getElementById('nextPageLink');
            
            // Setup Previous button
            if (currentPage > 1) {
                prevPageLink.href = buildPageUrl(currentPage - 1);
                prevPageItem.classList.remove('disabled');
            } else {
                prevPageLink.href = '#';
                prevPageItem.classList.add('disabled');
                prevPageLink.addEventListener('click', function(e) {
                    e.preventDefault();
                });
            }
            
            // Setup Next button
            if (currentPage < totalPages) {
                nextPageLink.href = buildPageUrl(currentPage + 1);
                nextPageItem.classList.remove('disabled');
            } else {
                nextPageLink.href = '#';
                nextPageItem.classList.add('disabled');
                nextPageLink.addEventListener('click', function(e) {
                    e.preventDefault();
                });
            }
            
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

            // Delete button handlers
            document.querySelectorAll('.btn-delete').forEach(function(btn) {
                btn.addEventListener('click', function(e) {
                    e.preventDefault();
                    const contractId = this.getAttribute('data-contract-id');
                    const employeeName = this.getAttribute('data-employee-name');

                    document.getElementById('deleteContractIdInput').value = contractId;
                    document.getElementById('deleteContractId').textContent = contractId;
                    document.getElementById('deleteEmployeeName').textContent = employeeName;

                    const deleteModal = new bootstrap.Modal(document.getElementById('deleteContractModal'));
                    deleteModal.show();
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
