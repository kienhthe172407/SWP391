<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="jakarta.tags.core"%>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Payslip - HR Management System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/global.css">
</head>
<body>
    <!-- Include Sidebar Component -->
    <jsp:include page="/components/sidebar.jsp">
        <jsp:param name="currentPage" value="payslip" />
    </jsp:include>
    
    <div class="main-content">
        <!-- Include Header Component -->
        <jsp:include page="/components/header.jsp">
            <jsp:param name="pageTitle" value="My Payslip" />
        </jsp:include>

        <!-- Breadcrumb -->
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard/employee">Dashboard</a></li>
                <li class="breadcrumb-item active">My Payslip</li>
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

            <c:choose>
                <c:when test="${empty payslips}">
                    <!-- No Payslips Found -->
                    <div class="card">
                        <div class="card-body">
                            <div class="no-payslips">
                                <i class="fas fa-file-invoice-dollar"></i>
                                <h4>No Payslips Available</h4>
                                <p class="text-muted">You don't have any payslips in the system yet.</p>
                                <p class="text-muted">Payslips will appear here once they are calculated and approved by HR.</p>
                            </div>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <!-- Payslips Display -->
                    <div class="row justify-content-center">
                        <c:forEach var="payslip" items="${payslips}">
                            <%
                                // Get current payslip from the forEach loop
                                model.MonthlyPayroll currentPayslip = (model.MonthlyPayroll) pageContext.getAttribute("payslip");
                                
                                // Initialize with ZERO to avoid null issues
                                java.math.BigDecimal subtotal = java.math.BigDecimal.ZERO;
                                java.math.BigDecimal totalBenefitsBonus = java.math.BigDecimal.ZERO;
                                
                                if (currentPayslip != null) {
                                    // Calculate subtotal safely
                                    java.math.BigDecimal baseSalary = currentPayslip.getBaseSalary();
                                    java.math.BigDecimal totalAllowances = currentPayslip.getTotalAllowances();
                                    
                                    if (baseSalary != null && totalAllowances != null) {
                                        subtotal = baseSalary.add(totalAllowances);
                                    } else if (baseSalary != null) {
                                        subtotal = baseSalary;
                                    } else if (totalAllowances != null) {
                                        subtotal = totalAllowances;
                                    }
                                    
                                    // Calculate total benefits and bonus safely
                                    java.math.BigDecimal totalBenefits = currentPayslip.getTotalBenefits();
                                    java.math.BigDecimal totalBonus = currentPayslip.getTotalBonus();
                                    
                                    if (totalBenefits != null && totalBonus != null) {
                                        totalBenefitsBonus = totalBenefits.add(totalBonus);
                                    } else if (totalBenefits != null) {
                                        totalBenefitsBonus = totalBenefits;
                                    } else if (totalBonus != null) {
                                        totalBenefitsBonus = totalBonus;
                                    }
                                }
                                
                                // Set attributes for use in JSP
                                pageContext.setAttribute("subtotal", subtotal);
                                pageContext.setAttribute("totalBenefitsBonus", totalBenefitsBonus);
                            %>
                            <div class="col-lg-8 col-xl-6 mb-4">
                                <div class="card calculation-card h-100">
                                    <div class="card-header bg-light">
                                        <div class="d-flex justify-content-between align-items-center">
                                            <div>
                                                <h6 class="mb-0">
                                                    <strong><fmt:formatDate value="${payslip.payrollMonth}" pattern="MMMM yyyy" /></strong>
                                                    <br>
                                                    <small class="text-muted">${employee.employeeCode} - ${employee.firstName} ${employee.lastName}</small>
                                                </h6>
                                            </div>
                                            <span class="status-badge status-${payslip.status.toLowerCase()}">
                                                ${payslip.status}
                                            </span>
                                        </div>
                                    </div>
                                    <div class="card-body">
                                        <!-- Base Salary & Allowances -->
                                        <div class="breakdown-section">
                                            <h6 class="text-primary"><i class="fas fa-money-bill-wave me-2"></i>Base Salary & Allowances</h6>
                                            <div class="breakdown-item">
                                                <span>Base Salary:</span>
                                                <span>
                                                    <c:choose>
                                                        <c:when test="${payslip.baseSalary != null}">
                                                            <fmt:formatNumber value="${payslip.baseSalary}" type="currency" currencySymbol="VND" />
                                                        </c:when>
                                                        <c:otherwise>VND 0</c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </div>
                                            <div class="breakdown-item">
                                                <span>Total Allowances:</span>
                                                <span>
                                                    <c:choose>
                                                        <c:when test="${payslip.totalAllowances != null}">
                                                            <fmt:formatNumber value="${payslip.totalAllowances}" type="currency" currencySymbol="VND" />
                                                        </c:when>
                                                        <c:otherwise>VND 0</c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </div>
                                            <div class="breakdown-item breakdown-total">
                                                <span>Subtotal:</span>
                                                <span>
                                                    <c:choose>
                                                        <c:when test="${subtotal != null}">
                                                            <fmt:formatNumber value="${subtotal}" type="currency" currencySymbol="VND" />
                                                        </c:when>
                                                        <c:otherwise>VND 0</c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </div>
                                        </div>

                                        <!-- Attendance & Overtime -->
                                        <div class="breakdown-section">
                                            <h6 class="text-success"><i class="fas fa-clock me-2"></i>Attendance & Overtime</h6>
                                            <div class="breakdown-item">
                                                <span>Working Days:</span>
                                                <span>${payslip.workingDays != null ? payslip.workingDays : 0} days</span>
                                            </div>
                                            <div class="breakdown-item">
                                                <span>Absent Days:</span>
                                                <span>${payslip.absentDays != null ? payslip.absentDays : 0} days</span>
                                            </div>
                                            <div class="breakdown-item">
                                                <span>Late Days:</span>
                                                <span>${payslip.lateDays != null ? payslip.lateDays : 0} days</span>
                                            </div>
                                            <div class="breakdown-item">
                                                <span>Overtime Hours:</span>
                                                <span>
                                                    <c:choose>
                                                        <c:when test="${payslip.overtimeHours != null}">
                                                            <fmt:formatNumber value="${payslip.overtimeHours}" pattern="#,##0.00"/> hours
                                                        </c:when>
                                                        <c:otherwise>0.00 hours</c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </div>
                                            <div class="breakdown-item breakdown-total">
                                                <span>Overtime Pay (1.5x):</span>
                                                <span>
                                                    <c:choose>
                                                        <c:when test="${payslip.overtimePay != null}">
                                                            <fmt:formatNumber value="${payslip.overtimePay}" type="currency" currencySymbol="VND" />
                                                        </c:when>
                                                        <c:otherwise>VND 0</c:otherwise>
                                                    </c:choose>
                                                </span>
                                            </div>
                                        </div>

                                        <!-- Benefits & Bonus -->
                                        <%
                                            // Check if we should show benefits section
                                            boolean showBenefits = false;
                                            if (currentPayslip != null) {
                                                java.math.BigDecimal benefits = currentPayslip.getTotalBenefits();
                                                java.math.BigDecimal bonus = currentPayslip.getTotalBonus();
                                                if ((benefits != null && benefits.compareTo(java.math.BigDecimal.ZERO) > 0) ||
                                                    (bonus != null && bonus.compareTo(java.math.BigDecimal.ZERO) > 0)) {
                                                    showBenefits = true;
                                                }
                                            }
                                            pageContext.setAttribute("showBenefits", showBenefits);
                                        %>
                                        <c:if test="${showBenefits}">
                                            <div class="breakdown-section">
                                                <h6 class="text-info"><i class="fas fa-gift me-2"></i>Benefits & Bonus</h6>
                                                <%
                                                    boolean showBonus = false;
                                                    if (currentPayslip != null && currentPayslip.getTotalBonus() != null) {
                                                        if (currentPayslip.getTotalBonus().compareTo(java.math.BigDecimal.ZERO) > 0) {
                                                            showBonus = true;
                                                        }
                                                    }
                                                    pageContext.setAttribute("showBonus", showBonus);
                                                %>
                                                <c:if test="${showBonus}">
                                                    <div class="breakdown-item">
                                                        <span>Total Bonus:</span>
                                                        <span><fmt:formatNumber value="${payslip.totalBonus}" type="currency" currencySymbol="VND" /></span>
                                                    </div>
                                                </c:if>
                                                <%
                                                    boolean showBenefitsItem = false;
                                                    if (currentPayslip != null && currentPayslip.getTotalBenefits() != null) {
                                                        if (currentPayslip.getTotalBenefits().compareTo(java.math.BigDecimal.ZERO) > 0) {
                                                            showBenefitsItem = true;
                                                        }
                                                    }
                                                    pageContext.setAttribute("showBenefitsItem", showBenefitsItem);
                                                %>
                                                <c:if test="${showBenefitsItem}">
                                                    <div class="breakdown-item">
                                                        <span>Total Benefits:</span>
                                                        <span><fmt:formatNumber value="${payslip.totalBenefits}" type="currency" currencySymbol="VND" /></span>
                                                    </div>
                                                </c:if>
                                                <div class="breakdown-item breakdown-total">
                                                    <span>Total Benefits & Bonus:</span>
                                                    <span>
                                                        <c:choose>
                                                            <c:when test="${totalBenefitsBonus != null}">
                                                                <fmt:formatNumber value="${totalBenefitsBonus}" type="currency" currencySymbol="VND" />
                                                            </c:when>
                                                            <c:otherwise>VND 0</c:otherwise>
                                                        </c:choose>
                                                    </span>
                                                </div>
                                            </div>
                                        </c:if>

                                        <!-- Gross Salary -->
                                        <div class="breakdown-section bg-primary text-white">
                                            <div class="breakdown-item">
                                                <span><strong>GROSS SALARY:</strong></span>
                                                <span>
                                                    <strong>
                                                        <c:choose>
                                                            <c:when test="${payslip.grossSalary != null}">
                                                                <fmt:formatNumber value="${payslip.grossSalary}" type="currency" currencySymbol="VND" />
                                                            </c:when>
                                                            <c:otherwise>VND 0</c:otherwise>
                                                        </c:choose>
                                                    </strong>
                                                </span>
                                            </div>
                                        </div>

                                        <!-- Deductions -->
                                        <%
                                            boolean showDeductions = false;
                                            if (currentPayslip != null && currentPayslip.getTotalDeductions() != null) {
                                                if (currentPayslip.getTotalDeductions().compareTo(java.math.BigDecimal.ZERO) > 0) {
                                                    showDeductions = true;
                                                }
                                            }
                                            pageContext.setAttribute("showDeductions", showDeductions);
                                        %>
                                        <c:if test="${showDeductions}">
                                            <div class="breakdown-section">
                                                <h6 class="text-danger"><i class="fas fa-minus-circle me-2"></i>Deductions</h6>
                                                <div class="breakdown-item">
                                                    <span>Total Deductions:</span>
                                                    <span class="text-danger">-<fmt:formatNumber value="${payslip.totalDeductions}" type="currency" currencySymbol="VND" /></span>
                                                </div>
                                            </div>
                                        </c:if>

                                        <!-- Net Salary -->
                                        <div class="breakdown-section bg-success text-white">
                                            <div class="breakdown-item">
                                                <span><strong>NET SALARY (Take Home):</strong></span>
                                                <span>
                                                    <strong>
                                                        <c:choose>
                                                            <c:when test="${payslip.netSalary != null}">
                                                                <fmt:formatNumber value="${payslip.netSalary}" type="currency" currencySymbol="VND" />
                                                            </c:when>
                                                            <c:otherwise>VND 0</c:otherwise>
                                                        </c:choose>
                                                    </strong>
                                                </span>
                                            </div>
                                        </div>

                                        <!-- Additional Information -->
                                        <div class="mt-3">
                                            <small class="text-muted">
                                                <c:if test="${payslip.calculatedAt != null}">
                                                    <i class="fas fa-calendar me-1"></i>
                                                    Calculated: <fmt:formatDate value="${payslip.calculatedAt}" pattern="MMM dd, yyyy HH:mm" />
                                                    <c:if test="${payslip.paidAt != null}"> | </c:if>
                                                </c:if>
                                                <c:if test="${payslip.paidAt != null}">
                                                    <i class="fas fa-check-circle me-1"></i>
                                                    Paid: <fmt:formatDate value="${payslip.paidAt}" pattern="MMM dd, yyyy HH:mm" />
                                                </c:if>
                                            </small>
                                        </div>

                                        <!-- Export Actions -->
                                        <div class="mt-3 d-flex gap-2">
                                            <%
                                                // Get year and month from payrollMonth
                                                model.MonthlyPayroll payslip = (model.MonthlyPayroll) pageContext.getAttribute("payslip");
                                                if (payslip != null && payslip.getPayrollMonth() != null) {
                                                    java.sql.Date payrollMonth = payslip.getPayrollMonth();
                                                    java.util.Calendar cal = java.util.Calendar.getInstance();
                                                    cal.setTime(payrollMonth);
                                                    int year = cal.get(java.util.Calendar.YEAR);
                                                    int month = cal.get(java.util.Calendar.MONTH) + 1;
                                                    pageContext.setAttribute("payslipYear", year);
                                                    pageContext.setAttribute("payslipMonth", month);
                                                }
                                            %>
                                            <a href="${pageContext.request.contextPath}/employee/export-payslip?employeeId=${employee.employeeID}&year=${payslipYear}&month=${payslipMonth}&format=pdf" 
                                               class="btn btn-danger btn-sm">
                                                <i class="fas fa-file-pdf me-1"></i>Export PDF
                                            </a>
                                            <a href="${pageContext.request.contextPath}/employee/export-payslip?employeeId=${employee.employeeID}&year=${payslipYear}&month=${payslipMonth}&format=excel" 
                                               class="btn btn-success btn-sm">
                                                <i class="fas fa-file-excel me-1"></i>Export Excel
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
