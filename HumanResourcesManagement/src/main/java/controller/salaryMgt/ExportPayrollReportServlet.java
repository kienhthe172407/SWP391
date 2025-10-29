package controller.salaryMgt;

import model.User;
import model.SalarySummaryView;
import model.MonthlyPayroll;
import model.Employee;
import dal.PayrollDAO;
import dal.EmployeeDAO;
import dal.BonusAdjustmentDAO;
import util.PayrollReportExportUtil;
import util.PayrollReportPDFExportUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.math.BigDecimal;

/**
 * Servlet to export payroll reports for all employees in Excel or PDF format
 * Used by HR/HR Manager for finance, tax, and audit purposes
 * @author admin
 */
@WebServlet("/salary/export-payroll-report")
public class ExportPayrollReportServlet extends HttpServlet {
    
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
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        User user = (User) session.getAttribute("user");
        
        // Check authorization - only HR and HR Manager can export payroll reports
        if (!"HR".equals(user.getRole()) && !"HR Manager".equals(user.getRole()) && 
            !"HR_MANAGER".equals(user.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, 
                "Access denied. Only HR staff can export payroll reports.");
            return;
        }
        
        try {
            // Get parameters
            String format = request.getParameter("format"); // "excel" or "pdf"
            String yearStr = request.getParameter("year");
            String monthStr = request.getParameter("month");
            
            if (format == null || (!format.equals("excel") && !format.equals("pdf"))) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, 
                    "Invalid format. Use 'excel' or 'pdf'");
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
            
            // If not provided, use current month
            if (year == null || month == null) {
                Calendar cal = Calendar.getInstance();
                if (year == null) year = cal.get(Calendar.YEAR);
                if (month == null) month = cal.get(Calendar.MONTH) + 1;
            }
            
            // Create payroll month date
            Date payrollMonth = Date.valueOf(String.format("%d-%02d-01", year, month));
            
            // Get all payroll records for the month
            List<MonthlyPayroll> payrolls = payrollDAO.getPayrollByMonth(payrollMonth);
            
            if (payrolls == null || payrolls.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, 
                    String.format("No payroll records found for %d-%02d. Please calculate salaries first.", 
                    year, month));
                return;
            }
            
            // Convert to SalarySummaryView list
            List<SalarySummaryView> summaryViews = new ArrayList<>();
            BigDecimal totalGrossSalary = BigDecimal.ZERO;
            BigDecimal totalNetSalary = BigDecimal.ZERO;
            BigDecimal totalBonusAdjustments = BigDecimal.ZERO;
            
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
                    
                    summaryViews.add(view);
                    
                    // Calculate totals
                    totalGrossSalary = totalGrossSalary.add(payroll.getGrossSalary());
                    totalNetSalary = totalNetSalary.add(payroll.getNetSalary());
                    totalBonusAdjustments = totalBonusAdjustments.add(bonusAdj);
                }
            }
            
            // Generate filename
            String filename = String.format("Payroll_Report_%d-%02d", year, month);
            
            // Generate and send the file
            byte[] fileData;
            String contentType;
            String fileExtension;
            
            if ("excel".equals(format)) {
                fileData = PayrollReportExportUtil.generatePayrollReportExcel(
                    summaryViews, year, month, totalGrossSalary, totalNetSalary, totalBonusAdjustments);
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                fileExtension = ".xlsx";
            } else { // pdf
                fileData = PayrollReportPDFExportUtil.generatePayrollReportPDF(
                    summaryViews, year, month, totalGrossSalary, totalNetSalary, totalBonusAdjustments);
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
            
            System.out.println("Payroll report exported successfully: " + filename + fileExtension + 
                             " by user: " + user.getUsername() + 
                             " (" + summaryViews.size() + " employees)");
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid year or month format");
        } catch (Exception e) {
            System.err.println("Error exporting payroll report: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                "Error generating payroll report: " + e.getMessage());
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doGet(request, response);
    }
}

