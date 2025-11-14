package controller.salaryMgt;

import dal.BonusAdjustmentDAO;
import dal.PayrollDAO;
import model.MonthlyPayroll;
import model.SalaryCalculationDetail;
import model.User;
import service.SalaryCalculationService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

/**
 * Servlet to calculate monthly salaries for all employees
 * Displays detailed breakdown of salary calculation
 * Allows saving calculated salaries to database
 * @author admin
 */
@WebServlet("/salary/calculate")
public class CalculateSalaryServlet extends HttpServlet {

    private SalaryCalculationService calculationService;
    private PayrollDAO payrollDAO;
    private BonusAdjustmentDAO bonusAdjustmentDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        calculationService = new SalaryCalculationService();
        payrollDAO = new PayrollDAO();
        bonusAdjustmentDAO = new BonusAdjustmentDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check authentication
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Check permission
        if (!util.PermissionChecker.hasPermission(user, util.PermissionConstants.SALARY_CALCULATE)) {
            request.setAttribute("errorMessage", "Bạn không có quyền tính lương");
            request.getRequestDispatcher("/error/403.jsp").forward(request, response);
            return;
        }
        
        // Get current month and year as default
        java.util.Calendar cal = java.util.Calendar.getInstance();
        int currentYear = cal.get(java.util.Calendar.YEAR);
        int currentMonth = cal.get(java.util.Calendar.MONTH) + 1; // Calendar.MONTH is 0-based
        
        request.setAttribute("defaultYear", currentYear);
        request.setAttribute("defaultMonth", currentMonth);
        
        // Handle search and pagination for existing calculations
        handleSearchAndPagination(request, response);
        
        // Forward to calculation page
        request.getRequestDispatcher("/salary-mgt/calculate-salary.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check authentication
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Check permission
        if (!util.PermissionChecker.hasPermission(user, util.PermissionConstants.SALARY_CALCULATE)) {
            request.setAttribute("errorMessage", "Bạn không có quyền tính lương");
            request.getRequestDispatcher("/error/403.jsp").forward(request, response);
            return;
        }
        
        String action = request.getParameter("action");
        
        if ("calculate".equals(action)) {
            handleCalculate(request, response, user);
        } else if ("save".equals(action)) {
            handleSave(request, response, user);
        } else {
            response.sendRedirect(request.getContextPath() + "/salary/calculate");
        }
    }
    
    /**
     * Handle search and pagination for existing calculations
     */
    private void handleSearchAndPagination(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check if we have existing calculations in session
        @SuppressWarnings("unchecked")
        List<SalaryCalculationDetail> allCalculations = 
                (List<SalaryCalculationDetail>) session.getAttribute("salaryCalculations");
        
        if (allCalculations == null || allCalculations.isEmpty()) {
            return; // No calculations to search/paginate
        }
        
        // Get search parameters
        String employeeCode = request.getParameter("employeeCode");
        String employeeName = request.getParameter("employeeName");
        
        // Filter calculations based on search criteria
        List<SalaryCalculationDetail> filteredCalculations = allCalculations;
        
        if (employeeCode != null && !employeeCode.trim().isEmpty()) {
            String searchCode = employeeCode.trim().toLowerCase();
            filteredCalculations = filteredCalculations.stream()
                    .filter(calc -> calc.getEmployeeCode().toLowerCase().contains(searchCode))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        if (employeeName != null && !employeeName.trim().isEmpty()) {
            String searchName = employeeName.trim().toLowerCase();
            filteredCalculations = filteredCalculations.stream()
                    .filter(calc -> calc.getEmployeeName().toLowerCase().contains(searchName))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        // Pagination parameters
        final int PAGE_SIZE = 6; // 6 employees per page (2 rows of 3 cards)
        int currentPage = 1;
        
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }
        
        int totalRecords = filteredCalculations.size();
        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
        
        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
        }
        
        // Get paginated results
        int startIndex = (currentPage - 1) * PAGE_SIZE;
        int endIndex = Math.min(startIndex + PAGE_SIZE, totalRecords);
        
        List<SalaryCalculationDetail> paginatedCalculations = filteredCalculations.subList(startIndex, endIndex);
        
        // Set attributes for display
        request.setAttribute("calculations", paginatedCalculations);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);
        request.setAttribute("pageSize", PAGE_SIZE);
        
        // Recalculate totals for filtered results
        BigDecimal totalGross = BigDecimal.ZERO;
        BigDecimal totalNet = BigDecimal.ZERO;
        for (SalaryCalculationDetail calc : filteredCalculations) {
            totalGross = totalGross.add(calc.getGrossSalary());
            totalNet = totalNet.add(calc.getNetSalary());
        }
        request.setAttribute("totalGrossSalary", totalGross);
        request.setAttribute("totalNetSalary", totalNet);
        request.setAttribute("totalEmployees", filteredCalculations.size());
        
        // Get year and month from session
        Integer year = (Integer) session.getAttribute("calculationYear");
        Integer month = (Integer) session.getAttribute("calculationMonth");
        if (year != null && month != null) {
            request.setAttribute("year", year);
            request.setAttribute("month", month);
        }
    }
    
    /**
     * Handle salary calculation request
     */
    private void handleCalculate(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        
        try {
            // Get parameters
            String yearStr = request.getParameter("year");
            String monthStr = request.getParameter("month");
            
            if (yearStr == null || monthStr == null || yearStr.trim().isEmpty() || monthStr.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Year and month are required.");
                request.getRequestDispatcher("/salary-mgt/calculate-salary.jsp").forward(request, response);
                return;
            }
            
            int year = Integer.parseInt(yearStr);
            int month = Integer.parseInt(monthStr);
            
            // Validate year and month
            if (year < 2000 || year > 2100) {
                request.setAttribute("errorMessage", "Invalid year. Please enter a year between 2000 and 2100.");
                request.getRequestDispatcher("/salary-mgt/calculate-salary.jsp").forward(request, response);
                return;
            }
            
            if (month < 1 || month > 12) {
                request.setAttribute("errorMessage", "Invalid month. Please enter a month between 1 and 12.");
                request.getRequestDispatcher("/salary-mgt/calculate-salary.jsp").forward(request, response);
                return;
            }
            
            // Calculate salaries for all employees
            List<SalaryCalculationDetail> calculations = calculationService.calculateAllEmployeesSalary(year, month);
            
            if (calculations.isEmpty()) {
                request.setAttribute("errorMessage", "No active employees with salary components found.");
                request.setAttribute("defaultYear", year);
                request.setAttribute("defaultMonth", month);
                request.getRequestDispatcher("/salary-mgt/calculate-salary.jsp").forward(request, response);
                return;
            }
            
            // Store calculations in session for saving later
            request.getSession().setAttribute("salaryCalculations", calculations);
            request.getSession().setAttribute("calculationYear", year);
            request.getSession().setAttribute("calculationMonth", month);
            
            // Set attributes for display
            request.setAttribute("calculations", calculations);
            request.setAttribute("year", year);
            request.setAttribute("month", month);
            request.setAttribute("totalEmployees", calculations.size());
            
            // Calculate totals
            BigDecimal totalGross = BigDecimal.ZERO;
            BigDecimal totalNet = BigDecimal.ZERO;
            for (SalaryCalculationDetail calc : calculations) {
                totalGross = totalGross.add(calc.getGrossSalary());
                totalNet = totalNet.add(calc.getNetSalary());
            }
            request.setAttribute("totalGrossSalary", totalGross);
            request.setAttribute("totalNetSalary", totalNet);
            
            // Handle search and pagination for the new calculations
            handleSearchAndPagination(request, response);
            
            request.setAttribute("successMessage", "Salary calculated successfully for " + calculations.size() + " employees.");
            request.getRequestDispatcher("/salary-mgt/calculate-salary.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Invalid year or month format.");
            request.getRequestDispatcher("/salary-mgt/calculate-salary.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Error in handleCalculate: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred during salary calculation: " + e.getMessage());
            request.getRequestDispatcher("/salary-mgt/calculate-salary.jsp").forward(request, response);
        }
    }
    
    /**
     * Handle save calculated salaries to database
     */
    private void handleSave(HttpServletRequest request, HttpServletResponse response, User user)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        try {
            // Get calculations from session
            @SuppressWarnings("unchecked")
            List<SalaryCalculationDetail> calculations = 
                    (List<SalaryCalculationDetail>) session.getAttribute("salaryCalculations");
            Integer year = (Integer) session.getAttribute("calculationYear");
            Integer month = (Integer) session.getAttribute("calculationMonth");
            
            if (calculations == null || year == null || month == null) {
                session.setAttribute("errorMessage", "No salary calculations found. Please calculate salaries first.");
                response.sendRedirect(request.getContextPath() + "/salary/calculate");
                return;
            }
            
            // Create payroll month date (first day of the month)
            Date payrollMonth = Date.valueOf(String.format("%d-%02d-01", year, month));
            
            int savedCount = 0;
            int updatedCount = 0;
            int errorCount = 0;
            
            // Save each calculation to database
            for (SalaryCalculationDetail calc : calculations) {
                try {
                    // Get approved bonus adjustments for this employee and month
                    BigDecimal approvedBonusAdjustments = bonusAdjustmentDAO.getTotalApprovedAdjustments(
                            calc.getEmployeeID(), payrollMonth);

                    // Create MonthlyPayroll object
                    MonthlyPayroll payroll = new MonthlyPayroll();
                    payroll.setEmployeeID(calc.getEmployeeID());
                    payroll.setPayrollMonth(payrollMonth);
                    payroll.setBaseSalary(calc.getBaseSalary());
                    payroll.setTotalAllowances(calc.getTotalAllowances());
                    payroll.setOvertimePay(calc.getOvertimePay());
                    payroll.setTotalBonus(approvedBonusAdjustments); // Include approved bonus adjustments
                    payroll.setTotalBenefits(calc.getTotalBenefits());
                    payroll.setTotalDeductions(calc.getTotalDeductions());

                    // Recalculate gross and net salary with bonus adjustments
                    BigDecimal grossSalary = calc.getGrossSalary().add(approvedBonusAdjustments);
                    BigDecimal netSalary = grossSalary.subtract(calc.getTotalDeductions());

                    payroll.setGrossSalary(grossSalary);
                    payroll.setNetSalary(netSalary);
                    payroll.setWorkingDays(calc.getWorkingDays());
                    payroll.setAbsentDays(calc.getAbsentDays());
                    payroll.setLateDays(calc.getLateDays());
                    payroll.setOvertimeHours(calc.getOvertimeHours());
                    payroll.setStatus("Calculated");
                    payroll.setCalculatedBy(user.getUserID());

                    // Add note about bonus adjustments if any
                    String notes = "Auto-calculated by system";
                    if (approvedBonusAdjustments.compareTo(BigDecimal.ZERO) != 0) {
                        notes += String.format(" (includes bonus adjustments: $%.2f)", approvedBonusAdjustments);
                    }
                    payroll.setNotes(notes);
                    
                    // Check if payroll already exists
                    MonthlyPayroll existing = payrollDAO.getPayrollByEmployeeAndMonth(
                            calc.getEmployeeID(), payrollMonth);
                    
                    if (existing != null) {
                        // Update existing record
                        payroll.setPayrollID(existing.getPayrollID());
                        if (payrollDAO.updatePayroll(payroll)) {
                            updatedCount++;
                        } else {
                            errorCount++;
                        }
                    } else {
                        // Insert new record
                        if (payrollDAO.insertPayroll(payroll)) {
                            savedCount++;
                        } else {
                            errorCount++;
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error saving payroll for employee " + calc.getEmployeeCode() + ": " + e.getMessage());
                    errorCount++;
                }
            }
            
            // Clear session data
            session.removeAttribute("salaryCalculations");
            session.removeAttribute("calculationYear");
            session.removeAttribute("calculationMonth");
            
            // Set success message
            String message = String.format("Salary calculation saved: %d new, %d updated", savedCount, updatedCount);
            if (errorCount > 0) {
                message += String.format(", %d errors", errorCount);
            }
            session.setAttribute("successMessage", message);
            
            // Redirect to view components page
            response.sendRedirect(request.getContextPath() + "/salary/view-components");
            
        } catch (Exception e) {
            System.err.println("Error in handleSave: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while saving salary calculations: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/salary/calculate");
        }
    }
}

