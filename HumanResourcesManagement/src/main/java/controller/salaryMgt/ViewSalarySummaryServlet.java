package controller.salaryMgt;

import dal.BonusAdjustmentDAO;
import dal.EmployeeDAO;
import dal.PayrollDAO;
import model.BonusAdjustment;
import model.Department;
import model.Employee;
import model.MonthlyPayroll;
import model.SalarySummaryView;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servlet to view salary summary with filters
 * Allows filtering by employee, department, and period
 * Shows bonus adjustments and provides export/adjust options
 * @author admin
 */
@WebServlet("/salary/view-summary")
public class ViewSalarySummaryServlet extends HttpServlet {

    private PayrollDAO payrollDAO;
    private EmployeeDAO employeeDAO;
    private BonusAdjustmentDAO bonusAdjustmentDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        payrollDAO = new PayrollDAO();
        employeeDAO = new EmployeeDAO();
        bonusAdjustmentDAO = new BonusAdjustmentDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check authentication
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Check permission
        if (!util.PermissionChecker.hasPermission(user, util.PermissionConstants.SALARY_VIEW_SUMMARY)) {
            request.setAttribute("errorMessage", "Bạn không có quyền xem tổng hợp lương");
            request.getRequestDispatcher("/error/403.jsp").forward(request, response);
            return;
        }
        
        try {
            // Get filter parameters
            String filterType = request.getParameter("filterType"); // "all", "employee", "department", "period"
            String employeeIdStr = request.getParameter("employeeId");
            String departmentName = request.getParameter("department");
            String yearStr = request.getParameter("year");
            String monthStr = request.getParameter("month");
            
            // Default to current month if no period specified
            java.util.Calendar cal = java.util.Calendar.getInstance();
            int year = (yearStr != null && !yearStr.isEmpty()) ? Integer.parseInt(yearStr) : cal.get(java.util.Calendar.YEAR);
            int month = (monthStr != null && !monthStr.isEmpty()) ? Integer.parseInt(monthStr) : cal.get(java.util.Calendar.MONTH) + 1;
            
            // Create payroll month date
            Date payrollMonth = Date.valueOf(String.format("%d-%02d-01", year, month));
            
            // Get payroll data based on filters
            List<MonthlyPayroll> payrolls = new ArrayList<>();
            
            if ("employee".equals(filterType) && employeeIdStr != null && !employeeIdStr.isEmpty()) {
                // Filter by specific employee
                int employeeId = Integer.parseInt(employeeIdStr);
                MonthlyPayroll payroll = payrollDAO.getPayrollByEmployeeAndMonth(employeeId, payrollMonth);
                if (payroll != null) {
                    payrolls.add(payroll);
                }
            } else if ("department".equals(filterType) && departmentName != null && !departmentName.isEmpty()) {
                // Filter by department - get all payrolls and filter by department
                List<MonthlyPayroll> allPayrolls = payrollDAO.getPayrollByMonth(payrollMonth);
                for (MonthlyPayroll p : allPayrolls) {
                    Employee emp = employeeDAO.getEmployeeById(p.getEmployeeID());
                    if (emp != null && departmentName.equals(emp.getDepartmentName())) {
                        payrolls.add(p);
                    }
                }
            } else {
                // Show all for the period
                payrolls = payrollDAO.getPayrollByMonth(payrollMonth);
            }
            
            // Convert to summary views
            List<SalarySummaryView> summaryViews = new ArrayList<>();
            BigDecimal totalGrossSalary = BigDecimal.ZERO;
            BigDecimal totalNetSalary = BigDecimal.ZERO;
            BigDecimal totalBonusAdjustments = BigDecimal.ZERO;

            // Department-wise summary map
            java.util.Map<String, DepartmentSummary> departmentSummaries = new java.util.HashMap<>();

            for (MonthlyPayroll payroll : payrolls) {
                Employee employee = employeeDAO.getEmployeeById(payroll.getEmployeeID());
                if (employee != null) {
                    SalarySummaryView view = new SalarySummaryView();

                    // Employee info
                    view.setEmployeeId(employee.getEmployeeID());
                    view.setEmployeeCode(employee.getEmployeeCode());
                    view.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
                    view.setDepartmentName(employee.getDepartmentName());
                    view.setPositionName(employee.getPositionName());

                    // Payroll info
                    view.setPayrollId(payroll.getPayrollID());
                    view.setPayrollMonth(payroll.getPayrollMonth());
                    view.setBaseSalary(payroll.getBaseSalary());
                    view.setTotalAllowances(payroll.getTotalAllowances());
                    view.setOvertimePay(payroll.getOvertimePay());
                    view.setTotalBonus(payroll.getTotalBonus());
                    view.setTotalBenefits(payroll.getTotalBenefits());
                    view.setTotalDeductions(payroll.getTotalDeductions());
                    view.setGrossSalary(payroll.getGrossSalary());
                    view.setNetSalary(payroll.getNetSalary());
                    view.setWorkingDays(payroll.getWorkingDays());
                    view.setAbsentDays(payroll.getAbsentDays());
                    view.setLateDays(payroll.getLateDays());
                    view.setOvertimeHours(payroll.getOvertimeHours());
                    view.setStatus(payroll.getStatus());

                    // Bonus adjustments
                    BigDecimal bonusAdj = bonusAdjustmentDAO.getTotalApprovedAdjustments(
                        employee.getEmployeeID(), payrollMonth);
                    view.setBonusAdjustments(bonusAdj);

                    List<BonusAdjustment> pendingAdj = bonusAdjustmentDAO
                        .getAdjustmentsByEmployeeAndMonth(employee.getEmployeeID(), payrollMonth)
                        .stream()
                        .filter(BonusAdjustment::isPending)
                        .collect(Collectors.toList());
                    view.setPendingAdjustmentsCount(pendingAdj.size());

                    summaryViews.add(view);

                    // Calculate totals
                    totalGrossSalary = totalGrossSalary.add(payroll.getGrossSalary());
                    totalNetSalary = totalNetSalary.add(payroll.getNetSalary());
                    totalBonusAdjustments = totalBonusAdjustments.add(bonusAdj);

                    // Update department summary
                    String deptName = employee.getDepartmentName() != null ? employee.getDepartmentName() : "Unknown";
                    DepartmentSummary deptSummary = departmentSummaries.get(deptName);
                    if (deptSummary == null) {
                        deptSummary = new DepartmentSummary(deptName);
                        departmentSummaries.put(deptName, deptSummary);
                    }
                    deptSummary.addEmployee(payroll.getGrossSalary(), payroll.getNetSalary(), bonusAdj);
                }
            }

            // Sort summaryViews by net salary descending for top earners
            List<SalarySummaryView> topEarners = summaryViews.stream()
                .sorted((a, b) -> b.getNetSalary().compareTo(a.getNetSalary()))
                .limit(10)
                .collect(Collectors.toList());

            // Convert department summaries to list and sort by total payroll
            List<DepartmentSummary> departmentList = new ArrayList<>(departmentSummaries.values());
            departmentList.sort((a, b) -> b.getTotalGrossSalary().compareTo(a.getTotalGrossSalary()));
            
            // Get all departments for filter dropdown
            List<Department> departments = employeeDAO.getAllDepartments();

            // Get all employees for filter dropdown
            List<Employee> allEmployees = employeeDAO.getAllActiveEmployees();

            // Get all pending adjustments for HR Manager
            List<BonusAdjustment> allPendingAdjustments = bonusAdjustmentDAO.getPendingAdjustments();

            // Set attributes
            request.setAttribute("summaryViews", summaryViews);
            request.setAttribute("totalGrossSalary", totalGrossSalary);
            request.setAttribute("totalNetSalary", totalNetSalary);
            request.setAttribute("totalBonusAdjustments", totalBonusAdjustments);
            request.setAttribute("totalEmployees", summaryViews.size());
            request.setAttribute("year", year);
            request.setAttribute("month", month);
            request.setAttribute("filterType", filterType != null ? filterType : "all");
            request.setAttribute("selectedEmployeeId", employeeIdStr);
            request.setAttribute("selectedDepartment", departmentName);
            request.setAttribute("departments", departments);
            request.setAttribute("allEmployees", allEmployees);
            request.setAttribute("allPendingAdjustments", allPendingAdjustments);
            request.setAttribute("departmentSummaries", departmentList);
            request.setAttribute("topEarners", topEarners);
            
            // Check for messages
            String successMessage = (String) session.getAttribute("successMessage");
            String errorMessage = (String) session.getAttribute("errorMessage");
            
            if (successMessage != null) {
                request.setAttribute("successMessage", successMessage);
                session.removeAttribute("successMessage");
            }
            
            if (errorMessage != null) {
                request.setAttribute("errorMessage", errorMessage);
                session.removeAttribute("errorMessage");
            }
            
            // Forward to JSP
            request.getRequestDispatcher("/salary-mgt/view-salary-summary.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error in ViewSalarySummaryServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error loading salary summary: " + e.getMessage());
            request.getRequestDispatcher("/salary-mgt/view-salary-summary.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Helper class to store department-wise salary summary
     */
    public static class DepartmentSummary {
        private String departmentName;
        private int employeeCount;
        private BigDecimal totalGrossSalary;
        private BigDecimal totalNetSalary;
        private BigDecimal totalBonusAdjustments;
        private BigDecimal averageGrossSalary;
        private BigDecimal averageNetSalary;

        public DepartmentSummary(String departmentName) {
            this.departmentName = departmentName;
            this.employeeCount = 0;
            this.totalGrossSalary = BigDecimal.ZERO;
            this.totalNetSalary = BigDecimal.ZERO;
            this.totalBonusAdjustments = BigDecimal.ZERO;
        }

        public void addEmployee(BigDecimal grossSalary, BigDecimal netSalary, BigDecimal bonusAdj) {
            this.employeeCount++;
            this.totalGrossSalary = this.totalGrossSalary.add(grossSalary);
            this.totalNetSalary = this.totalNetSalary.add(netSalary);
            this.totalBonusAdjustments = this.totalBonusAdjustments.add(bonusAdj);

            // Calculate averages
            this.averageGrossSalary = this.totalGrossSalary.divide(
                BigDecimal.valueOf(employeeCount), 2, java.math.RoundingMode.HALF_UP);
            this.averageNetSalary = this.totalNetSalary.divide(
                BigDecimal.valueOf(employeeCount), 2, java.math.RoundingMode.HALF_UP);
        }

        // Getters
        public String getDepartmentName() { return departmentName; }
        public int getEmployeeCount() { return employeeCount; }
        public BigDecimal getTotalGrossSalary() { return totalGrossSalary; }
        public BigDecimal getTotalNetSalary() { return totalNetSalary; }
        public BigDecimal getTotalBonusAdjustments() { return totalBonusAdjustments; }
        public BigDecimal getAverageGrossSalary() { return averageGrossSalary; }
        public BigDecimal getAverageNetSalary() { return averageNetSalary; }
    }
}

