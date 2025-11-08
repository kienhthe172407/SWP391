<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Import Base Salary Data - HR Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    <style>
        .preview-table {
            font-size: 0.9rem;
        }
        .row-valid {
            background-color: #d4edda;
        }
        .row-invalid {
            background-color: #f8d7da;
        }
        .row-update {
            background-color: #fff3cd;
        }
        .upload-area {
            border: 2px dashed #dee2e6;
            border-radius: 8px;
            padding: 2rem;
            text-align: center;
            background-color: #f8f9fa;
        }
        .btn.btn-view-summary {
            background-color: #ff8c00 !important;
            border: none !important;
            border-color: transparent !important;
            color: white !important;
            box-shadow: none !important;
        }
        .btn.btn-view-summary:hover,
        .btn.btn-view-summary:focus,
        .btn.btn-view-summary:active {
            background-color: #ff7f00 !important;
            border-color: transparent !important;
            color: white !important;
            box-shadow: none !important;
        }
        .btn.btn-view-summary:focus {
            box-shadow: 0 0 0 0.2rem rgba(255, 140, 0, 0.25) !important;
        }
        .button-group-responsive {
            display: flex;
            flex-wrap: nowrap;
            gap: 0.5rem;
            align-items: center;
        }
        .button-group-responsive .btn {
            flex: 1;
            min-width: 0;
            white-space: nowrap;
            font-size: 0.75rem;
            padding: 0.375rem 0.75rem;
        }
        .button-group-responsive .btn.btn-view-summary {
            flex: 1.2;
            padding: 0.375rem 1rem !important;
        }
        @media (max-width: 768px) {
            .button-group-responsive .btn {
                font-size: 0.7rem;
                padding: 0.3rem 0.5rem;
            }
            .button-group-responsive .btn.btn-view-summary {
                padding: 0.3rem 0.75rem !important;
            }
            .button-group-responsive .btn i {
                font-size: 0.7rem;
            }
        }
        @media (max-width: 576px) {
            .button-group-responsive {
                flex-direction: column;
                width: 100%;
            }
            .button-group-responsive .btn {
                width: 100%;
                margin-bottom: 0.25rem;
            }
        }
    </style>
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="Import Base Salary Data" />
        </jsp:include>

        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard/hr-dashboard.jsp">Dashboard</a></li>
                <li class="breadcrumb-item active">Import Salary Data</li>
            </ol>
        </nav>

        <!-- Content Area -->
        <div class="content-area">
            <!-- Existing Salary Data Info Card -->
            <c:if test="${hasExistingSalaryData}">
                <div class="alert alert-info alert-dismissible fade show" role="alert">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <i class="fas fa-info-circle me-2"></i>
                            <strong>Salary data available:</strong> We found records for ${totalSalaryComponents} employees.
                        </div>
                        <div class="button-group-responsive">
                            <a href="${pageContext.request.contextPath}/salary/view-components" class="btn btn-primary">
                                <i class="fas fa-list me-1"></i>View Salary Data
                            </a>
                            <a href="${pageContext.request.contextPath}/salary/view-summary" class="btn btn-view-summary">
                                <i class="fas fa-chart-bar me-1"></i>View Salary Summary
                            </a>
                            <a href="${pageContext.request.contextPath}/salary/calculate" class="btn btn-success">
                                <i class="fas fa-calculator me-1"></i>Calculate Salary
                            </a>
                        </div>
                    </div>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <c:if test="${not hasExistingSalaryData and empty successMessage}">
                <div class="alert alert-warning" role="alert">
                    <i class="fas fa-exclamation-triangle me-2"></i>
                    <strong>No salary data yet:</strong> Please import base salary and allowance data using the form below.
                </div>
            </c:if>

            <!-- Success/Error Messages -->
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>${successMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <!-- Navigation button after successful import -->
                <div class="mb-3 button-group-responsive">
                    <a href="${pageContext.request.contextPath}/salary/view-components" class="btn btn-primary">
                        <i class="fas fa-list me-1"></i>View Imported Salary Data
                    </a>
                    <a href="${pageContext.request.contextPath}/salary/view-summary" class="btn btn-view-summary">
                        <i class="fas fa-chart-bar me-1"></i>View Salary Summary
                    </a>
                    <a href="${pageContext.request.contextPath}/salary/calculate" class="btn btn-success">
                        <i class="fas fa-calculator me-1"></i>Calculate Monthly Salary
                    </a>
                </div>
            </c:if>

            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <c:if test="${not empty message}">
                <div class="alert alert-info alert-dismissible fade show" role="alert">
                    <i class="fas fa-info-circle me-2"></i>${message}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Upload Form -->
            <div class="card mb-4">
                <div class="card-header">
                    <h5 class="mb-0"><i class="fas fa-file-upload me-2"></i>Upload Salary Data File</h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-8">
                            <form method="POST" action="${pageContext.request.contextPath}/salary/import" 
                                  enctype="multipart/form-data" id="uploadForm">
                                <div class="upload-area mb-3">
                                    <i class="fas fa-cloud-upload-alt fa-3x text-muted mb-3"></i>
                                    <h5>Select Excel File to Upload</h5>
                                    <p class="text-muted">Supported formats: .xlsx, .xls</p>
                                    <input type="file" class="form-control" name="salaryFile" id="salaryFile" 
                                           accept=".xlsx,.xls" required>
                                </div>
                                <button type="submit" class="btn btn-primary">
                                    <i class="fas fa-upload me-2"></i>Upload and Preview
                                </button>
                            </form>
                        </div>
                        <div class="col-md-4">
                            <div class="card bg-light">
                                <div class="card-body">
                                    <h6><i class="fas fa-info-circle me-2"></i>File Format</h6>
                                    <p class="small mb-2">Excel file should contain the following columns:</p>
                                    <ol class="small mb-0">
                                        <li>Employee Code</li>
                                        <li>Base Salary</li>
                                        <li>Position Allowance</li>
                                        <li>Housing Allowance</li>
                                        <li>Transportation Allowance</li>
                                        <li>Meal Allowance</li>
                                        <li>Other Allowances</li>
                                        <li>Effective From (yyyy-MM-dd)</li>
                                    </ol>
                                    <hr>
                                    <a href="${pageContext.request.contextPath}/salary/download-template"
                                       class="btn btn-sm btn-outline-primary">
                                        <i class="fas fa-download me-2"></i>Download Template
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Preview Data -->
            <c:if test="${not empty previewData}">
                <div class="card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="mb-0"><i class="fas fa-table me-2"></i>Preview Imported Data</h5>
                        <div>
                            <span class="badge bg-primary me-2">Total: ${previewData.totalRecords}</span>
                            <span class="badge bg-success me-2">Valid: ${previewData.validRecords}</span>
                            <c:if test="${previewData.willUpdateRecords > 0}">
                                <span class="badge bg-warning me-2">Will Update: ${previewData.willUpdateRecords}</span>
                            </c:if>
                            <span class="badge bg-danger">Invalid: ${previewData.invalidRecords}</span>
                        </div>
                    </div>
                    <div class="card-body">
                        <div class="table-responsive">
                            <table class="table table-sm table-bordered preview-table">
                                <thead class="table-dark">
                                    <tr>
                                        <th>Row</th>
                                        <th>Employee Code</th>
                                        <th>Employee Name</th>
                                        <th>Base Salary</th>
                                        <th>Position Allow.</th>
                                        <th>Housing Allow.</th>
                                        <th>Transport Allow.</th>
                                        <th>Meal Allow.</th>
                                        <th>Other Allow.</th>
                                        <th>Total</th>
                                        <th>Effective From</th>
                                        <th>Status</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="record" items="${previewData.records}">
                                        <tr class="${record.valid ? (record.willUpdate ? 'row-update' : 'row-valid') : 'row-invalid'}">
                                            <td>${record.rowNumber}</td>
                                            <td>${record.employeeCode}</td>
                                            <td>${record.employeeName}</td>
                                            <c:choose>
                                                <c:when test="${record.valid}">
                                                    <td><fmt:formatNumber value="${record.salaryComponent.baseSalary}" type="currency" currencySymbol="$"/></td>
                                                    <td><fmt:formatNumber value="${record.salaryComponent.positionAllowance}" type="currency" currencySymbol="$"/></td>
                                                    <td><fmt:formatNumber value="${record.salaryComponent.housingAllowance}" type="currency" currencySymbol="$"/></td>
                                                    <td><fmt:formatNumber value="${record.salaryComponent.transportationAllowance}" type="currency" currencySymbol="$"/></td>
                                                    <td><fmt:formatNumber value="${record.salaryComponent.mealAllowance}" type="currency" currencySymbol="$"/></td>
                                                    <td><fmt:formatNumber value="${record.salaryComponent.otherAllowances}" type="currency" currencySymbol="$"/></td>
                                                    <td><strong><fmt:formatNumber value="${record.salaryComponent.totalMonthlySalary}" type="currency" currencySymbol="$"/></strong></td>
                                                    <td><fmt:formatDate value="${record.salaryComponent.effectiveFrom}" pattern="yyyy-MM-dd"/></td>
                                                    <td>
                                                        <c:choose>
                                                            <c:when test="${record.willUpdate}">
                                                                <span class="badge bg-warning">Will Update</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-success">New</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td colspan="8" class="text-danger">
                                                        <i class="fas fa-exclamation-triangle me-2"></i>${record.errorMessage}
                                                    </td>
                                                    <td>-</td>
                                                    <td><span class="badge bg-danger">Error</span></td>
                                                </c:otherwise>
                                            </c:choose>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                        
                        <div class="mt-3">
                            <form method="POST" action="${pageContext.request.contextPath}/salary/save-imported" class="d-inline">
                                <button type="submit" class="btn btn-success" 
                                        ${previewData.validRecords == 0 ? 'disabled' : ''}>
                                    <i class="fas fa-save me-2"></i>Save ${previewData.validRecords} Valid Record(s)
                                </button>
                            </form>
                            <a href="${pageContext.request.contextPath}/salary/import" class="btn btn-secondary ms-2">
                                <i class="fas fa-times me-2"></i>Cancel
                            </a>
                        </div>
                        
                        <div class="alert alert-info mt-3">
                            <i class="fas fa-info-circle me-2"></i>
                            <strong>Note:</strong> 
                            <ul class="mb-0 mt-2">
                                <li><strong>New</strong> records will be added as new salary components.</li>
                                <li><strong>Will Update</strong> records will deactivate the existing active salary component and create a new one with the new effective date.</li>
                                <li><strong>Error</strong> records will be skipped and not imported.</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Show selected file name
        document.getElementById('salaryFile').addEventListener('change', function(e) {
            const fileName = e.target.files[0]?.name;
            if (fileName) {
                console.log('Selected file:', fileName);
            }
        });
    </script>
</body>
</html>

