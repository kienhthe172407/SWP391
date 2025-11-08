<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Deduction Type - HR Management System</title>
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
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp" />
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="Edit Deduction Type" />
        </jsp:include>

        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/salary/manage-benefits-deductions">Benefits & Deductions</a></li>
                <li class="breadcrumb-item active">Edit Deduction</li>
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
                    <h5 class="mb-0"><i class="fas fa-minus-circle me-2"></i>Edit Deduction Type Information</h5>
                </div>
                <div class="card-body">
                    <form method="POST" action="${pageContext.request.contextPath}/salary/edit-deduction" class="needs-validation" novalidate id="editDeductionForm">
                        <input type="hidden" name="deductionTypeID" value="${deduction.deductionTypeID}">
                        
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="deductionName" class="form-label">Deduction Name <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" id="deductionName" name="deductionName" 
                                       value="${deduction.deductionName}" required maxlength="100">
                                <small class="text-muted">Unique name for this deduction type</small>
                                <div class="invalid-feedback">Please enter a deduction name.</div>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label for="calculationType" class="form-label">Calculation Type <span class="text-danger">*</span></label>
                                <select class="form-select" id="calculationType" name="calculationType" required onchange="toggleCalculationFields()">
                                    <option value="">-- Select Type --</option>
                                    <option value="Fixed" ${deduction.calculationType == 'Fixed' ? 'selected' : ''}>Fixed Amount</option>
                                    <option value="Percentage" ${deduction.calculationType == 'Percentage' ? 'selected' : ''}>Percentage of Salary</option>
                                    <option value="Formula" ${deduction.calculationType == 'Formula' ? 'selected' : ''}>Formula-based</option>
                                </select>
                                <small class="text-muted">How this deduction is calculated</small>
                                <div class="invalid-feedback">Please select a calculation type.</div>
                            </div>
                        </div>

                        <div class="row">
                            <div class="col-md-6 mb-3" id="amountField">
                                <label for="defaultAmount" class="form-label">Default Amount ($)</label>
                                <input type="number" class="form-control" id="defaultAmount" name="defaultAmount" 
                                       value="${deduction.defaultAmount}" step="0.01" min="0">
                                <small class="text-muted">Default fixed amount for this deduction</small>
                                <div class="invalid-feedback">Please enter a valid amount.</div>
                            </div>

                            <div class="col-md-6 mb-3" id="percentageField">
                                <label for="defaultPercentage" class="form-label">Default Percentage (%)</label>
                                <input type="number" class="form-control" id="defaultPercentage" name="defaultPercentage" 
                                       value="${deduction.defaultPercentage}" step="0.01" min="0" max="100">
                                <small class="text-muted">Default percentage of base salary</small>
                                <div class="invalid-feedback">Please enter a valid percentage (0 - 100).</div>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="description" class="form-label">Description</label>
                            <textarea class="form-control" id="description" name="description" rows="3">${deduction.description}</textarea>
                            <small class="text-muted">Optional description of this deduction</small>
                        </div>

                        <div class="mb-3">
                            <div class="form-check">
                                <input class="form-check-input" type="checkbox" id="isMandatory" name="isMandatory" 
                                       ${deduction.mandatory ? 'checked' : ''}>
                                <label class="form-check-label" for="isMandatory">
                                    This deduction is mandatory
                                </label>
                                <small class="d-block text-muted">Check if this deduction must be applied to all employees</small>
                            </div>
                        </div>

                        <div class="d-flex justify-content-between mt-4">
                            <a href="${pageContext.request.contextPath}/salary/manage-benefits-deductions" class="btn btn-secondary">
                                <i class="fas fa-times me-2"></i>Cancel
                            </a>
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-save me-2"></i>Update Deduction Type
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

            amountField.style.display = 'none';
            percentageField.style.display = 'none';
            amountInput.removeAttribute('required');
            percentageInput.removeAttribute('required');

            if (calculationType === 'Fixed') {
                amountField.style.display = 'block';
                amountInput.setAttribute('required', 'required');
            } else if (calculationType === 'Percentage') {
                percentageField.style.display = 'block';
                percentageInput.setAttribute('required', 'required');
            }
        }

        document.addEventListener('DOMContentLoaded', function() {
            toggleCalculationFields();
            const form = document.getElementById('editDeductionForm');
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

