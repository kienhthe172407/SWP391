package controller.salaryMgt;

import model.SalaryCalculationDetail;
import model.User;
import model.MonthlyPayroll;
import model.Employee;
import dal.PayrollDAO;
import dal.EmployeeDAO;
import util.PayslipExportUtil;
import util.PayslipPDFExportUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.util.Calendar;

/**
 * Servlet to export payslip for employees
 * Employees can export their own payslips in Excel or PDF format
 * @author admin
 */
@WebServlet("/employee/export-payslip")
public class EmployeeExportPayslipServlet extends HttpServlet {

    private PayrollDAO payrollDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        payrollDAO = new PayrollDAO();
        employeeDAO = new EmployeeDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        // Get employee information for the logged-in user
        Employee employee = employeeDAO.getEmployeeByUserId(user.getUserId());
        if (employee == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee record not found.");
            return;
        }

        try {
            // Get parameters
            String employeeIdStr = request.getParameter("employeeId");
            String format = request.getParameter("format"); // "excel" or "pdf"
            String yearStr = request.getParameter("year");
            String monthStr = request.getParameter("month");

            // Validate that employee can only export their own payslip
            int requestedEmployeeId = -1;
            if (employeeIdStr != null && !employeeIdStr.trim().isEmpty()) {
                requestedEmployeeId = Integer.parseInt(employeeIdStr);
            }

            // Ensure employee can only export their own payslip
            if (requestedEmployeeId != employee.getEmployeeID()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, 
                    "Access denied. You can only export your own payslip.");
                return;
            }

            if (format == null || (!format.equals("excel") && !format.equals("pdf"))) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid format. Use 'excel' or 'pdf'");
                return;
            }

            // Get year and month
            Integer year = null;
            Integer month = null;

            if (yearStr != null && !yearStr.trim().isEmpty()) {
                year = Integer.parseInt(yearStr);
            }

            if (monthStr != null && !monthStr.trim().isEmpty()) {
                month = Integer.parseInt(monthStr);
            }

            // If still null, use current month
            if (year == null || month == null) {
                Calendar cal = Calendar.getInstance();
                if (year == null) year = cal.get(Calendar.YEAR);
                if (month == null) month = cal.get(Calendar.MONTH) + 1;
            }

            // Create payroll month date
            Date payrollMonth = Date.valueOf(String.format("%d-%02d-01", year, month));

            // Get payroll from database
            MonthlyPayroll payroll = payrollDAO.getPayrollByEmployeeAndMonth(employee.getEmployeeID(), payrollMonth);

            if (payroll == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND,
                    String.format("No payslip found for %d-%02d.", year, month));
                return;
            }

            // Convert MonthlyPayroll to SalaryCalculationDetail
            SalaryCalculationDetail targetCalc = convertPayrollToCalculation(payroll, employee.getEmployeeID());
            
            // Generate filename
            String filename = String.format("Payslip_%s_%d-%02d", 
                targetCalc.getEmployeeCode(), year, month);
            
            // Generate and send the file
            byte[] fileData;
            String contentType;
            String fileExtension;
            
            if ("excel".equals(format)) {
                fileData = PayslipExportUtil.generatePayslipExcel(targetCalc, year, month);
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                fileExtension = ".xlsx";
            } else { // pdf
                fileData = PayslipPDFExportUtil.generatePayslipPDF(targetCalc, year, month);
                contentType = "application/pdf";
                fileExtension = ".pdf";
            }
            
            // Set response headers
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", 
                "attachment; filename=\"" + filename + fileExtension + "\"");
            response.setContentLength(fileData.length);
            
            // Write file to response
            try (OutputStream out = response.getOutputStream()) {
                out.write(fileData);
                out.flush();
            }
            
            System.out.println("Payslip exported successfully by employee: " + filename + fileExtension + 
                             " by user: " + user.getUsername());
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter format");
        } catch (Exception e) {
            System.err.println("Error exporting payslip: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Error generating payslip: " + e.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Convert MonthlyPayroll to SalaryCalculationDetail for export
     */
    private SalaryCalculationDetail convertPayrollToCalculation(MonthlyPayroll payroll, int employeeId) {
        SalaryCalculationDetail calc = new SalaryCalculationDetail();

        // Get employee details
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        if (employee != null) {
            calc.setEmployeeID(employee.getEmployeeID());
            calc.setEmployeeCode(employee.getEmployeeCode());
            calc.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
            calc.setDepartmentName(employee.getDepartmentName());
            calc.setPositionName(employee.getPositionName());
        }

        // Set salary components from payroll
        calc.setBaseSalary(payroll.getBaseSalary());
        calc.setPositionAllowance(java.math.BigDecimal.ZERO); // Not stored separately in payroll
        calc.setHousingAllowance(java.math.BigDecimal.ZERO);
        calc.setTransportationAllowance(java.math.BigDecimal.ZERO);
        calc.setMealAllowance(java.math.BigDecimal.ZERO);
        calc.setOtherAllowances(java.math.BigDecimal.ZERO);
        calc.setTotalAllowances(payroll.getTotalAllowances());

        // Set attendance and overtime
        calc.setWorkingDays(payroll.getWorkingDays());
        calc.setAbsentDays(payroll.getAbsentDays());
        calc.setLateDays(payroll.getLateDays());
        calc.setOvertimeHours(payroll.getOvertimeHours());
        calc.setOvertimePay(payroll.getOvertimePay());

        // Add benefits as list items
        if (payroll.getTotalBenefits().compareTo(java.math.BigDecimal.ZERO) > 0) {
            calc.addBenefit(new SalaryCalculationDetail.BenefitDetail(
                "Total Benefits", "Fixed", payroll.getTotalBenefits(),
                "Benefits included in payroll"));
        }
        if (payroll.getTotalBonus().compareTo(java.math.BigDecimal.ZERO) > 0) {
            calc.addBenefit(new SalaryCalculationDetail.BenefitDetail(
                "Total Bonus", "Fixed", payroll.getTotalBonus(),
                "Bonus included in payroll"));
        }
        calc.setTotalBenefits(payroll.getTotalBenefits().add(payroll.getTotalBonus()));

        // Add deductions as list items
        if (payroll.getTotalDeductions().compareTo(java.math.BigDecimal.ZERO) > 0) {
            calc.addDeduction(new SalaryCalculationDetail.DeductionDetail(
                "Total Deductions", "Fixed", payroll.getTotalDeductions(),
                "Deductions from payroll"));
        }
        calc.setTotalDeductions(payroll.getTotalDeductions());

        // Set totals
        calc.setGrossSalary(payroll.getGrossSalary());
        calc.setNetSalary(payroll.getNetSalary());

        return calc;
    }
}

