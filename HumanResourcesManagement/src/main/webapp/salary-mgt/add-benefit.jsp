<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Benefit Type - HR Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    <style>
        .card-header {
            background-color: #f8f9fa;
            border-bottom: 1px solid #dee2e6;
        }
        .invalid-feedback { font-style: italic; }
    </style>
</head>
<body>
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="sidebar-header">
            <h4>HR System</h4>
            <p>Salary Management</p>
        </div>
        
        <ul class="sidebar-menu">
            <c:choose>
                <c:when test="${sessionScope.user != null && sessionScope.user.role == 'HR Manager'}">
                    <li class="menu-section">Dashboard</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/dashboard/hr-manager-dashboard.jsp">
                            <i class="fas fa-home"></i>
                            <span>Overview</span>
                        </a>
                    </li>

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
                        <a href="${pageContext.request.contextPath}/employee-mgt/add-employee-information.jsp">
                            <i class="fas fa-user-plus"></i>
                            <span>Add Employee Information</span>
                        </a>
                    </li>

                    <li class="menu-section">Contracts & Attendance</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/contracts/list">
                            <i class="fas fa-file"></i>
                            <span>Contracts</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/attendance/import">
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
                        <a href="${pageContext.request.contextPath}/job-postings/list">
                            <i class="fas fa-briefcase"></i>
                            <span> Job Postings</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/job-postings/create">
                            <i class="fas fa-plus"></i>
                            <span>Create Job Posting</span>
                        </a>
                    </li>

                    <li class="menu-section">Payroll & Benefits</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/salary/manage-benefits-deductions" class="active">
                            <i class="fas fa-gift"></i>
                            <span>Benefits & Deductions</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/salary/import">
                            <i class="fas fa-dollar-sign"></i>
                            <span>Payroll</span>
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
                        <a href="${pageContext.request.contextPath}/contracts/list">
                            <i class="fas fa-file"></i>
                            <span>Contracts</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/attendance/import">
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
                        <a href="${pageContext.request.contextPath}/job-postings/list">
                            <i class="fas fa-briefcase"></i>
                            <span> Job Postings</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/job-postings/create">
                            <i class="fas fa-plus"></i>
                            <span>Create Job Posting</span>
                        </a>
                    </li>

                    <li class="menu-section">Payroll & Benefits</li>
                    <li>
                        <a href="${pageContext.request.contextPath}/salary/manage-benefits-deductions" class="active">
                            <i class="fas fa-gift"></i>
                            <span>Benefits & Deductions</span>
                        </a>
                    </li>
                    <li>
                        <a href="${pageContext.request.contextPath}/salary/import">
                            <i class="fas fa-dollar-sign"></i>
                            <span>Payroll</span>
                        </a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <!-- Top Header -->
        <div class="top-header">
            <h1>Add Benefit Type</h1>
            <div class="user-info">
                <span><c:out value="${sessionScope.user.roleDisplayName}" default="HR"/></span>
                <div class="avatar">HR</div>
            </div>
        </div>

        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/salary/manage-benefits-deductions">Benefits & Deductions</a></li>
                <li class="breadcrumb-item active">Add Benefit</li>
            </ol>
        </nav>

        <!-- Content Area -->
        <div class="content-area">
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <div class="card">
                <div class="card-header">
                    <h5 class="mb-0"><i class="fas fa-gift me-2"></i>Benefit Type Information</h5>
                </div>
                <div class="card-body">
                    <form method="POST" action="${pageContext.request.contextPath}/salary/add-benefit" id="addBenefitForm" class="needs-validation" novalidate>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="benefitName" class="form-label">Benefit Name <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="benefitName" name="benefitName" 
                                       value="${param.benefitName}" required maxlength="100"
                                       placeholder="e.g., Health Insurance, Meal Allowance">
                                <small class="text-muted">Unique name for this benefit type</small>
                                <div class="invalid-feedback">Please enter a benefit name.</div>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label for="calculationType" class="form-label">Calculation Type <span class="text-danger">*</span></label>
                                <select class="form-select" id="calculationType" name="calculationType" required onchange="toggleCalculationFields()">
                                    <option value="">-- Select Type --</option>
                                    <option value="Fixed" ${param.calculationType == 'Fixed' ? 'selected' : ''}>Fixed Amount</option>
                                    <option value="Percentage" ${param.calculationType == 'Percentage' ? 'selected' : ''}>Percentage of Salary</option>
                                    <option value="Formula" ${param.calculationType == 'Formula' ? 'selected' : ''}>Formula-based</option>
                                </select>
                                <small class="text-muted">How this benefit is calculated</small>
                                <div class="invalid-feedback">Please select a calculation type.</div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3" id="amountField" style="display: none;">
                                <label for="defaultAmount" class="form-label">Default Amount ($)</label>
                                <input type="number" class="form-control" id="defaultAmount" name="defaultAmount" 
                                       value="${param.defaultAmount}" step="0.01" min="0"
                                       placeholder="e.g., 500.00">
                                <small class="text-muted">Default fixed amount for this benefit</small>
                                <div class="invalid-feedback">Please enter a valid amount.</div>
                            </div>

                            <div class="col-md-6 mb-3" id="percentageField" style="display: none;">
                                <label for="defaultPercentage" class="form-label">Default Percentage (%)</label>
                                <input type="number" class="form-control" id="defaultPercentage" name="defaultPercentage" 
                                       value="${param.defaultPercentage}" step="0.01" min="0" max="100"
                                       placeholder="e.g., 10.00">
                                <small class="text-muted">Default percentage of base salary</small>
                                <div class="invalid-feedback">Please enter a valid percentage (0 - 100).</div>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="description" class="form-label">Description</label>
                            <textarea class="form-control" id="description" name="description" rows="3"
                                      placeholder="Describe this benefit type...">${param.description}</textarea>
                            <small class="text-muted">Optional description of this benefit</small>
                        </div>

                        <div class="mb-3">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" id="isTaxable" name="isTaxable" 
                                       ${param.isTaxable == 'on' || param.isTaxable == null ? 'checked' : ''}>
                                <label class="form-check-label" for="isTaxable">
                                    This benefit is taxable
                                </label>
                                <small class="d-block text-muted">Check if this benefit should be included in taxable income</small>
                            </div>
                        </div>

                        <div class="d-flex justify-content-between mt-4">
                            <a href="${pageContext.request.contextPath}/salary/manage-benefits-deductions" class="btn btn-secondary">
                                <i class="fas fa-times me-2"></i>Cancel
                            </a>
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save me-2"></i>Save Benefit Type
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function toggleCalculationFields() {
            const calculationType = document.getElementById('calculationType').value;
            const amountField = document.getElementById('amountField');
            const percentageField = document.getElementById('percentageField');
            const amountInput = document.getElementById('defaultAmount');
            const percentageInput = document.getElementById('defaultPercentage');

            // Hide all fields first
            amountField.style.display = 'none';
            percentageField.style.display = 'none';
            amountInput.removeAttribute('required');
            percentageInput.removeAttribute('required');

            // Show relevant field based on calculation type
            if (calculationType === 'Fixed') {
                amountField.style.display = 'block';
                amountInput.setAttribute('required', 'required');
            } else if (calculationType === 'Percentage') {
                percentageField.style.display = 'block';
                percentageInput.setAttribute('required', 'required');
            }
        }

        // Initialize on page load
        document.addEventListener('DOMContentLoaded', function() {
            toggleCalculationFields();
            // Bootstrap-like custom validation
            const form = document.getElementById('addBenefitForm');
            form.addEventListener('submit', function(event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            });
        });
    </script>
</body>
</html>

