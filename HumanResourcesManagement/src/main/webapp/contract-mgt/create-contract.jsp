<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Contract - HR Management System</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <!-- Global CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
    
    <style>
        .form-section {
            margin-bottom: 2rem;
            padding-bottom: 1rem;
            border-bottom: 1px solid #eee;
        }
        
        .form-section h5 {
            margin-bottom: 1.5rem;
            color: #0d6efd;
        }
        
        .required-field::after {
            content: "*";
            color: red;
            margin-left: 4px;
        }
        
        /* Custom dropdown styling */
        select.form-select {
            height: auto;
        }
        
        /* Limit dropdown height to show only 5 items */
        select.form-select option {
            padding: 8px 12px;
        }
        
        /* Style for dropdown with limited items */
        select.form-select[size]:not([size="1"]) {
            height: auto !important;
            max-height: 200px !important;
            overflow-y: scroll !important; /* Force scrollbar to always appear */
            position: absolute !important;
            z-index: 1050 !important;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.2);
            border-radius: 0.25rem;
            scrollbar-width: thin; /* For Firefox */
            width: 100% !important;
            left: 0 !important;
            top: 100% !important;
        }
        
        /* Scrollbar styling for Webkit browsers */
        select.form-select[size]:not([size="1"])::-webkit-scrollbar {
            width: 8px;
        }
        
        select.form-select[size]:not([size="1"])::-webkit-scrollbar-track {
            background: #f1f1f1;
            border-radius: 4px;
        }
        
        select.form-select[size]:not([size="1"])::-webkit-scrollbar-thumb {
            background: #888;
            border-radius: 4px;
        }
        
        select.form-select[size]:not([size="1"])::-webkit-scrollbar-thumb:hover {
            background: #555;
        }
        
        /* Style for dropdown options */
        select.form-select option {
            padding: 8px 12px;
            cursor: pointer;
        }
        
        /* Hover effect for options */
        select.form-select option:hover {
            background-color: #f8f9fa;
        }
        
        /* Selected option style */
        select.form-select option:checked {
            background-color: #0d6efd;
            color: white;
        }
        
        /* Dropdown with scroll class */
        .dropdown-with-scroll {
            transition: all 0.2s ease;
        }
        
        /* Wrapper for dropdown positioning */
        .select-wrapper {
            position: relative;
            width: 100%;
        }
        
        /* Ensure the dropdown container has proper positioning */
        .form-select-container {
            position: relative;
        }
        
        /* Override Bootstrap's default select styling when expanded */
        select.form-select:focus {
            box-shadow: none;
            border-color: #86b7fe;
        }
        
        /* Error message styling */
        .error-message {
            color: #dc3545;
            font-size: 0.85rem;
            font-style: italic;
            margin-top: 0.25rem;
            display: none;
            padding: 0.2rem 0;
        }
        
        /* Invalid field styling */
        .is-invalid {
            border-color: #dc3545 !important;
            background-color: #fff8f8 !important;
        }
        
        .is-invalid:focus {
            box-shadow: 0 0 0 0.25rem rgba(220, 53, 69, 0.25) !important;
            border-color: #dc3545 !important;
        }
        
        /* Error icon for invalid fields */
        .input-group .is-invalid {
            background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 12 12' width='12' height='12' fill='none' stroke='%23dc3545'%3e%3ccircle cx='6' cy='6' r='4.5'/%3e%3cpath stroke-linejoin='round' d='M5.8 3.6h.4L6 6.5z'/%3e%3ccircle cx='6' cy='8.2' r='.6' fill='%23dc3545' stroke='none'/%3e%3c/svg%3e");
            background-repeat: no-repeat;
            background-position: right calc(0.375em + 0.1875rem) center;
            background-size: calc(0.75em + 0.375rem) calc(0.75em + 0.375rem);
        }
    </style>
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="${editMode ? 'Edit Contract' : 'Create New Contract'}" />
        </jsp:include>
        
        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Dashboard</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/contracts/list">Contracts</a></li>
                <li class="breadcrumb-item active"><c:choose><c:when test="${editMode}">Edit Contract</c:when><c:otherwise>Create New Contract</c:otherwise></c:choose></li>
            </ol>
        </nav>
        
        <!-- Content Area -->
        <div class="content-area">
            <!-- Error/Success Messages -->
            <c:if test="${not empty errorMessage}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${errorMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            
            <c:if test="${not empty successMessage}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i>${successMessage}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            
            <!-- Contract Form Card -->
            <div class="card">
                <div class="card-header">
                    <i class="fas fa-file-contract me-2"></i>Contract Details
                </div>
                <div class="card-body">
                    <form action="<c:choose><c:when test='${editMode}'>${pageContext.request.contextPath}/contracts/edit?id=${contract.contractID}</c:when><c:otherwise>${pageContext.request.contextPath}/contracts/create</c:otherwise></c:choose>" method="POST" id="contractForm" novalidate>
                        <!-- Basic Information Section -->
                        <div class="form-section">
                            <h5><i class="fas fa-info-circle me-2"></i>Basic Information</h5>
                            <div class="row g-3">
                                <!-- Employee Selection -->
                                <div class="col-md-6">
                                    <label for="employeeId" class="form-label required-field">Employee</label>
                                    <select name="employeeId" id="employeeId" class="form-select" novalidate>
                                        <option value="">-- Select Employee --</option>
                                        <c:forEach var="employee" items="${employees}">
                                            <option value="${employee.employeeID}" ${contract.employeeID == employee.employeeID ? 'selected' : ''}>
                                                ${employee.fullName} (${employee.employeeCode})
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <div id="employeeIdError" class="error-message">Please select an employee</div>
                                </div>
                                
                                <!-- Contract Number -->
                                <div class="col-md-6">
                                    <label for="contractNumber" class="form-label required-field">Contract Number</label>
                                    <div class="input-group">
                                        <span class="input-group-text">
                                            <i class="fas fa-hashtag"></i>
                                        </span>
                                        <input type="text" class="form-control" id="contractNumber" name="contractNumber" 
                                               placeholder="e.g., HD-2023-001" value="${contract.contractNumber != null ? contract.contractNumber : defaultContractNumber}" novalidate>
                                    </div>
                                    <small class="text-muted">Must be unique across all contracts</small>
                                    <div id="contractNumberError" class="error-message">Please enter a valid contract number</div>
                                </div>
                                
                                <!-- Contract Type -->
                                <div class="col-md-6">
                                    <label for="contractType" class="form-label required-field">Contract Type</label>
                                    <select name="contractType" id="contractType" class="form-select" novalidate>
                                        <option value="">-- Select Type --</option>
                                        <option value="Probation" ${contract.contractType == 'Probation' ? 'selected' : ''}>Probation</option>
                                        <option value="Fixed-term" ${contract.contractType == 'Fixed-term' ? 'selected' : ''}>Fixed-term</option>
                                        <option value="Indefinite" ${contract.contractType == 'Indefinite' ? 'selected' : ''}>Indefinite</option>
                                        <option value="Seasonal" ${contract.contractType == 'Seasonal' ? 'selected' : ''}>Seasonal</option>
                                    </select>
                                    <div id="contractTypeError" class="error-message">Please select a contract type</div>
                                </div>
                                
                                <!-- Salary Amount -->
                                <div class="col-md-6">
                                    <label for="salaryAmount" class="form-label">Salary Amount</label>
                                    <div class="input-group">
                                        <span class="input-group-text">
                                            <i class="fas fa-dollar-sign"></i>
                                        </span>
                                        <input type="number" class="form-control" id="salaryAmount" name="salaryAmount" 
                                               placeholder="e.g., 5000.00" step="0.01" min="0" 
                                               value="${contract.salaryAmount}">
                                    </div>
                                    <div id="salaryAmountError" class="error-message">Please enter a valid salary amount (must be a positive number)</div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Contract Period Section -->
                        <div class="form-section">
                            <h5><i class="fas fa-calendar-alt me-2"></i>Contract Period</h5>
                            <div class="row g-3">
                                <!-- Start Date -->
                                <div class="col-md-6">
                                    <label for="startDate" class="form-label required-field">Start Date</label>
                                    <input type="date" class="form-control" id="startDate" name="startDate" 
                                           value="${contract.startDate}" novalidate>
                                    <div id="startDateError" class="error-message">Please enter a valid start date</div>
                                </div>
                                
                                <!-- End Date -->
                                <div class="col-md-6">
                                    <label for="endDate" class="form-label">End Date</label>
                                    <input type="date" class="form-control" id="endDate" name="endDate" 
                                           value="${contract.endDate}">
                                    <small class="text-muted">Leave blank for indefinite contracts</small>
                                    <div id="endDateError" class="error-message">End date must be after start date</div>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Contract Details Section -->
                        <div class="form-section">
                            <h5><i class="fas fa-file-alt me-2"></i>Contract Details</h5>
                            <div class="row g-3">
                                <!-- Job Description -->
                                <div class="col-12">
                                    <label for="jobDescription" class="form-label">Job Description</label>
                                    <textarea class="form-control" id="jobDescription" name="jobDescription" 
                                              rows="4" placeholder="Enter job description...">${contract.jobDescription}</textarea>
                                </div>
                                
                                <!-- Terms and Conditions -->
                                <div class="col-12">
                                    <label for="termsAndConditions" class="form-label">Terms and Conditions</label>
                                    <textarea class="form-control" id="termsAndConditions" name="termsAndConditions" 
                                              rows="6" placeholder="Enter terms and conditions...">${contract.termsAndConditions}</textarea>
                                </div>
                                
                                <!-- Contract Status -->
                                <div class="col-md-6">
                                    <label for="contractStatus" class="form-label">Contract Status</label>
                                    <div class="form-check mb-2">
                                        <input class="form-check-input" type="checkbox" id="saveDraft" name="saveDraft" value="true" <c:if test='${editMode and contract.contractStatus == "Draft"}'>checked</c:if>>
                                        <label class="form-check-label" for="saveDraft">
                                            Save as Draft
                                        </label>
                                    </div>
                                    <select id="contractStatus" class="form-select" disabled>
                                        <option value="Draft">Draft</option>
                                        <option value="Pending Approval" selected>Pending Approval</option>
                                        <option value="Active">Active</option>
                                        <option value="Expired">Expired</option>
                                        <option value="Terminated">Terminated</option>
                                    </select>
                                    <!-- Hidden input to submit the actual contract status -->
                                    <input type="hidden" id="contractStatusInput" name="contractStatus" value="Pending Approval">
                                    <small class="text-muted">Check "Save as Draft" to save without sending for approval, or leave unchecked to submit for HR Manager approval</small>
                                </div>
                                
                                <!-- Notes for Approver -->
                                <div class="col-md-6">
                                    <label for="approvalComment" class="form-label">Notes for Approver</label>
                                    <textarea class="form-control" id="approvalComment" name="approvalComment" 
                                              rows="2" placeholder="Optional notes for the HR Manager who will approve this contract...">${contract.approvalComment}</textarea>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Form Actions -->
                        <div class="d-flex justify-content-end gap-2 mt-4">
                            <a href="<c:choose><c:when test='${editMode}'>${pageContext.request.contextPath}/contracts/detail?id=${contract.contractID}</c:when><c:otherwise>${pageContext.request.contextPath}/contracts/list</c:otherwise></c:choose>" class="btn btn-secondary">
                                <i class="fas fa-times me-1"></i>Cancel
                            </a>
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-paper-plane me-1"></i><c:choose><c:when test='${editMode}'>Save Changes</c:when><c:otherwise>Submit for Approval</c:otherwise></c:choose>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <!-- Bootstrap 5 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Form validation script -->
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Configure dropdowns to show only 5 items
            const configureDropdowns = function() {
                // Get all select elements
                const selects = document.querySelectorAll('select.form-select');
                
                selects.forEach(select => {
                    // Count options (excluding the first placeholder option)
                    const optionsCount = select.options.length - 1;
                    
                    // If more than 5 options, add custom event handlers
                    if (optionsCount > 5) {
                        // Add class for styling
                        select.classList.add('dropdown-with-scroll');
                        
                        // Pre-create a hidden dummy option to ensure scrollbar appears
                        // This option will never be visible but forces the scrollbar to appear
                        const dummyOption = document.createElement('option');
                        dummyOption.style.display = 'none';
                        dummyOption.disabled = true;
                        dummyOption.value = '';
                        dummyOption.classList.add('dummy-option');
                        select.appendChild(dummyOption);
                        
                        // Create a wrapper div for positioning
                        const wrapper = document.createElement('div');
                        wrapper.className = 'select-wrapper';
                        select.parentNode.insertBefore(wrapper, select);
                        wrapper.appendChild(select);
                        
                        // Create a custom dropdown element
                        const createCustomDropdown = function() {
                            // Create a custom dropdown element
                            const customDropdown = document.createElement('div');
                            customDropdown.className = 'custom-dropdown-menu';
                            customDropdown.style.display = 'none';
                            customDropdown.style.position = 'absolute';
                            customDropdown.style.zIndex = '1050';
                            customDropdown.style.width = '100%';
                            customDropdown.style.maxHeight = '200px';
                            customDropdown.style.overflowY = 'scroll';
                            customDropdown.style.backgroundColor = '#fff';
                            customDropdown.style.border = '1px solid #ced4da';
                            customDropdown.style.borderRadius = '0.25rem';
                            customDropdown.style.boxShadow = '0 3px 10px rgba(0, 0, 0, 0.2)';
                            customDropdown.style.top = '100%';
                            customDropdown.style.left = '0';
                            
                            // Add options to custom dropdown
                            for (let i = 1; i < select.options.length; i++) {
                                const option = select.options[i];
                                if (option.classList.contains('dummy-option')) continue;
                                
                                const item = document.createElement('div');
                                item.className = 'custom-dropdown-item';
                                item.textContent = option.textContent;
                                item.dataset.value = option.value;
                                item.style.padding = '8px 12px';
                                item.style.cursor = 'pointer';
                                
                                if (option.selected) {
                                    item.style.backgroundColor = '#0d6efd';
                                    item.style.color = 'white';
                                }
                                
                                item.addEventListener('mouseover', function() {
                                    if (!this.classList.contains('selected')) {
                                        this.style.backgroundColor = '#f8f9fa';
                                    }
                                });
                                
                                item.addEventListener('mouseout', function() {
                                    if (!this.classList.contains('selected')) {
                                        this.style.backgroundColor = '';
                                    }
                                });
                                
                                item.addEventListener('click', function() {
                                    select.value = this.dataset.value;
                                    select.dispatchEvent(new Event('change'));
                                    customDropdown.style.display = 'none';
                                });
                                
                                customDropdown.appendChild(item);
                            }
                            
                            wrapper.appendChild(customDropdown);
                            return customDropdown;
                        };
                        
                        const customDropdown = createCustomDropdown();
                        
                        // Add event listener to expand dropdown when clicked
                        select.addEventListener('mousedown', function(event) {
                            // Prevent default dropdown behavior
                            event.preventDefault();
                            
                            // Close any other open dropdowns first
                            document.querySelectorAll('.custom-dropdown-menu').forEach(dropdown => {
                                if (dropdown !== customDropdown) {
                                    dropdown.style.display = 'none';
                                }
                            });
                            
                            // Toggle dropdown visibility
                            if (customDropdown.style.display === 'none') {
                                customDropdown.style.display = 'block';
                                
                                // Add a click event listener to the document to close the dropdown when clicking outside
                                setTimeout(() => {
                                    document.addEventListener('click', function closeDropdown(e) {
                                        if (!customDropdown.contains(e.target) && e.target !== select) {
                                            customDropdown.style.display = 'none';
                                            document.removeEventListener('click', closeDropdown);
                                        }
                                    });
                                }, 0);
                            } else {
                                customDropdown.style.display = 'none';
                            }
                        });
                        
                        // Add event listener to update custom dropdown when select changes
                        select.addEventListener('change', function() {
                            // Update selected item in custom dropdown
                            const items = customDropdown.querySelectorAll('.custom-dropdown-item');
                            items.forEach(item => {
                                if (item.dataset.value === this.value) {
                                    item.style.backgroundColor = '#0d6efd';
                                    item.style.color = 'white';
                                    item.classList.add('selected');
                                } else {
                                    item.style.backgroundColor = '';
                                    item.style.color = '';
                                    item.classList.remove('selected');
                                }
                            });
                        });
                        
                        // Add event listener to collapse dropdown when focus is lost
                        select.addEventListener('blur', function() {
                            this.size = 1;
                        });
                    }
                });
            };
            
            // Call the function to configure dropdowns
            configureDropdowns();
            
            // Get contract type select element
            const contractTypeSelect = document.getElementById('contractType');
            const endDateInput = document.getElementById('endDate');
            const endDateError = document.getElementById('endDateError');
            
            // Add event listener to contract type
            contractTypeSelect.addEventListener('change', function() {
                // Clear previous error
                endDateError.style.display = 'none';
                endDateInput.classList.remove('is-invalid');
                
                // If Indefinite contract type is selected, disable end date
                if (this.value === 'Indefinite') {
                    endDateInput.value = '';
                    endDateInput.disabled = true;
                    endDateInput.parentElement.querySelector('small').textContent = 'Not applicable for Indefinite contracts';
                } else if (this.value === 'Probation') {
                    endDateInput.disabled = false;
                    endDateInput.parentElement.querySelector('small').textContent = 'Required for Probation contracts (max 6 months)';
                } else if (this.value === 'Fixed-term') {
                    endDateInput.disabled = false;
                    endDateInput.parentElement.querySelector('small').textContent = 'Required for Fixed-term contracts (max 3 years)';
                } else if (this.value === 'Seasonal') {
                    endDateInput.disabled = false;
                    endDateInput.parentElement.querySelector('small').textContent = 'Required for Seasonal contracts (max 1 year)';
                } else {
                    endDateInput.disabled = false;
                    endDateInput.parentElement.querySelector('small').textContent = 'Leave blank for indefinite contracts';
                }
            });
            
            // Trigger change event on load to set initial state
            contractTypeSelect.dispatchEvent(new Event('change'));
            
            // Form validation
            const form = document.getElementById('contractForm');
            
            // Function to show error message
            const showError = function(inputId, show) {
                const input = document.getElementById(inputId);
                const errorElement = document.getElementById(inputId + 'Error');
                
                if (show) {
                    input.classList.add('is-invalid');
                    errorElement.style.display = 'block';
                } else {
                    input.classList.remove('is-invalid');
                    errorElement.style.display = 'none';
                }
            };
            
            // Function to validate the form
            const validateForm = function() {
                let isValid = true;
                
                // Clear all previous errors
                document.querySelectorAll('.error-message').forEach(el => {
                    el.style.display = 'none';
                });
                
                document.querySelectorAll('.is-invalid').forEach(el => {
                    el.classList.remove('is-invalid');
                });
                
                // Check if employee is selected
                const employeeId = document.getElementById('employeeId').value;
                if (!employeeId) {
                    showError('employeeId', true);
                    isValid = false;
                }
                
                // Check if contract number is provided
                const contractNumber = document.getElementById('contractNumber').value.trim();
                if (!contractNumber) {
                    showError('contractNumber', true);
                    isValid = false;
                }
                
                // Check if contract type is selected
                const contractType = document.getElementById('contractType').value;
                if (!contractType) {
                    showError('contractType', true);
                    isValid = false;
                }
                
                // Check if start date is provided
                const startDate = document.getElementById('startDate').value;
                if (!startDate) {
                    showError('startDate', true);
                    isValid = false;
                }
                
                // Check if salary amount is valid (if provided)
                const salaryAmount = document.getElementById('salaryAmount').value;
                if (salaryAmount && (isNaN(parseFloat(salaryAmount)) || parseFloat(salaryAmount) < 0)) {
                    showError('salaryAmount', true);
                    isValid = false;
                }
                
                // Validate end date based on contract type
                const endDate = document.getElementById('endDate').value;
                
                // For Indefinite contracts, end date should be empty
                if (contractType === 'Indefinite' && endDate) {
                    document.getElementById('endDateError').textContent = 'End date should be empty for Indefinite contracts';
                    showError('endDate', true);
                    isValid = false;
                }
                // For other contract types, end date is required and must be after start date
                else if (contractType && contractType !== 'Indefinite') {
                    if (!endDate) {
                        document.getElementById('endDateError').textContent = 'Please enter an end date for ' + contractType + ' contracts';
                        showError('endDate', true);
                        isValid = false;
                    } else if (startDate && new Date(endDate) <= new Date(startDate)) {
                        document.getElementById('endDateError').textContent = 'End date must be after start date';
                        showError('endDate', true);
                        isValid = false;
                    }
                    
                    // Additional validation based on contract type
                    if (contractType === 'Probation' && startDate && endDate) {
                        // Probation period should not exceed 6 months
                        const startDateObj = new Date(startDate);
                        const endDateObj = new Date(endDate);
                        const diffMonths = (endDateObj.getFullYear() - startDateObj.getFullYear()) * 12 + 
                                          (endDateObj.getMonth() - startDateObj.getMonth());
                        
                        if (diffMonths > 6) {
                            document.getElementById('endDateError').textContent = 'Probation period should not exceed 6 months';
                            showError('endDate', true);
                            isValid = false;
                        }
                    } else if (contractType === 'Fixed-term' && startDate && endDate) {
                        // Fixed-term contracts should not exceed 3 years
                        const startDateObj = new Date(startDate);
                        const endDateObj = new Date(endDate);
                        const diffYears = (endDateObj.getFullYear() - startDateObj.getFullYear()) + 
                                         (endDateObj.getMonth() - startDateObj.getMonth()) / 12;
                        
                        if (diffYears > 3) {
                            document.getElementById('endDateError').textContent = 'Fixed-term contracts should not exceed 3 years';
                            showError('endDate', true);
                            isValid = false;
                        }
                    } else if (contractType === 'Seasonal' && startDate && endDate) {
                        // Seasonal contracts should not exceed 1 year
                        const startDateObj = new Date(startDate);
                        const endDateObj = new Date(endDate);
                        const diffYears = (endDateObj.getFullYear() - startDateObj.getFullYear()) + 
                                         (endDateObj.getMonth() - startDateObj.getMonth()) / 12;
                        
                        if (diffYears > 1) {
                            document.getElementById('endDateError').textContent = 'Seasonal contracts should not exceed 1 year';
                            showError('endDate', true);
                            isValid = false;
                        }
                    }
                }
                
                return isValid;
            };
            
            // Add event listeners to clear errors when input changes
            document.querySelectorAll('input, select').forEach(element => {
                element.addEventListener('input', function() {
                    this.classList.remove('is-invalid');
                    const errorId = this.id + 'Error';
                    const errorElement = document.getElementById(errorId);
                    if (errorElement) {
                        errorElement.style.display = 'none';
                    }
                });
            });
            
            // Handle draft checkbox
            const saveDraftCheckbox = document.getElementById('saveDraft');
            const contractStatusSelect = document.getElementById('contractStatus');
            const contractStatusInput = document.getElementById('contractStatusInput');

            saveDraftCheckbox.addEventListener('change', function() {
                if (this.checked) {
                    contractStatusSelect.value = 'Draft';
                    contractStatusInput.value = 'Draft';
                } else {
                    contractStatusSelect.value = 'Pending Approval';
                    contractStatusInput.value = 'Pending Approval';
                }
            });

            // Form submission handler
            form.addEventListener('submit', function(event) {
                if (!validateForm()) {
                    event.preventDefault();
                }
            });
        });
    </script>
</body>
</html>
