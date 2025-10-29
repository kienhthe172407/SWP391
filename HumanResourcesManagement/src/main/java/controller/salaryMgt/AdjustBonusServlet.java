package controller.salaryMgt;

import dal.BonusAdjustmentDAO;
import dal.EmployeeDAO;
import dal.PayrollDAO;
import model.BonusAdjustment;
import model.Employee;
import model.MonthlyPayroll;
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
import java.util.List;

/**
 * Servlet to handle bonus adjustments
 * Allows HR to add/approve/reject bonus adjustments
 * @author admin
 */
@WebServlet("/salary/adjust-bonus")
public class AdjustBonusServlet extends HttpServlet {
    
    private BonusAdjustmentDAO bonusAdjustmentDAO;
    private EmployeeDAO employeeDAO;
    private PayrollDAO payrollDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        bonusAdjustmentDAO = new BonusAdjustmentDAO();
        employeeDAO = new EmployeeDAO();
        payrollDAO = new PayrollDAO();
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
        
        // Check authorization (HR and HR Manager only)
        User user = (User) session.getAttribute("user");
        String role = user.getRole();
        if (!"HR".equals(role) && !"HR Manager".equals(role) && !"HR_MANAGER".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }
        
        try {
            String action = request.getParameter("action");
            
            if ("approve".equals(action)) {
                handleApproval(request, response, user);
            } else if ("reject".equals(action)) {
                handleRejection(request, response, user);
            } else {
                showAdjustmentForm(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Error in AdjustBonusServlet GET: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/salary/view-summary");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Check authentication
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // Check authorization
        User user = (User) session.getAttribute("user");
        String role = user.getRole();
        if (!"HR".equals(role) && !"HR Manager".equals(role) && !"HR_MANAGER".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/dashboard");
            return;
        }

        try {
            String action = request.getParameter("action");

            // Handle approve/reject actions
            if ("approve".equals(action)) {
                handleApproval(request, response, user);
                return;
            } else if ("reject".equals(action)) {
                handleRejection(request, response, user);
                return;
            }

            // Otherwise, handle creating new adjustment
            // Get form parameters
            int employeeId = Integer.parseInt(request.getParameter("employeeId"));
            int year = Integer.parseInt(request.getParameter("year"));
            int month = Integer.parseInt(request.getParameter("month"));
            String adjustmentType = request.getParameter("adjustmentType");
            BigDecimal amount = new BigDecimal(request.getParameter("amount"));
            String reason = request.getParameter("reason");

            // Create payroll month date
            Date payrollMonth = Date.valueOf(String.format("%d-%02d-01", year, month));

            // Get or create payroll record to get payroll_id
            MonthlyPayroll payroll = payrollDAO.getPayrollByEmployeeAndMonth(employeeId, payrollMonth);
            Integer payrollId = (payroll != null) ? payroll.getPayrollID() : null;

            // Create bonus adjustment
            BonusAdjustment adjustment = new BonusAdjustment();
            adjustment.setPayrollId(payrollId);
            adjustment.setEmployeeId(employeeId);
            adjustment.setAdjustmentType(adjustmentType);
            adjustment.setAmount(amount);
            adjustment.setReason(reason);
            adjustment.setRequestedBy(user.getUserID());

            // Determine status based on user role
            // HR Manager can approve immediately, HR staff needs approval
            if ("HR Manager".equals(role) || "HR_MANAGER".equals(role)) {
                adjustment.setStatus(BonusAdjustment.STATUS_APPROVED);
                adjustment.setApprovedBy(user.getUserID());
            } else {
                adjustment.setStatus(BonusAdjustment.STATUS_PENDING);
            }

            // Save to database
            int adjustmentId = bonusAdjustmentDAO.createAdjustment(adjustment);

            if (adjustmentId > 0) {
                // If approved, update the payroll record
                if (BonusAdjustment.STATUS_APPROVED.equals(adjustment.getStatus())) {
                    updatePayrollBonus(employeeId, payrollMonth);
                    session.setAttribute("successMessage",
                        "Bonus adjustment created and approved successfully. Payroll updated.");
                } else {
                    session.setAttribute("successMessage",
                        "Bonus adjustment created successfully. Pending HR Manager approval.");
                }
            } else {
                session.setAttribute("errorMessage", "Failed to create bonus adjustment.");
            }

            response.sendRedirect(request.getContextPath() + "/salary/view-summary?year=" + year + "&month=" + month);

        } catch (Exception e) {
            System.err.println("Error in AdjustBonusServlet POST: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error creating adjustment: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/salary/view-summary");
        }
    }
    
    private void showAdjustmentForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        int year = Integer.parseInt(request.getParameter("year"));
        int month = Integer.parseInt(request.getParameter("month"));
        
        // Get employee details
        Employee employee = employeeDAO.getEmployeeById(employeeId);
        
        // Get payroll details
        Date payrollMonth = Date.valueOf(String.format("%d-%02d-01", year, month));
        MonthlyPayroll payroll = payrollDAO.getPayrollByEmployeeAndMonth(employeeId, payrollMonth);
        
        // Get existing adjustments
        List<BonusAdjustment> existingAdjustments = 
            bonusAdjustmentDAO.getAdjustmentsByEmployeeAndMonth(employeeId, payrollMonth);
        
        // Set attributes
        request.setAttribute("employee", employee);
        request.setAttribute("payroll", payroll);
        request.setAttribute("existingAdjustments", existingAdjustments);
        request.setAttribute("year", year);
        request.setAttribute("month", month);
        
        // Forward to JSP
        request.getRequestDispatcher("/salary-mgt/adjust-bonus.jsp").forward(request, response);
    }
    
    private void handleApproval(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {

        HttpSession session = request.getSession();

        try {
            int adjustmentId = Integer.parseInt(request.getParameter("adjustmentId"));
            String approvalComment = request.getParameter("approvalComment");

            // Only HR Manager can approve
            if (!"HR Manager".equals(user.getRole()) && !"HR_MANAGER".equals(user.getRole())) {
                session.setAttribute("errorMessage", "Only HR Manager can approve adjustments.");
                response.sendRedirect(request.getContextPath() + "/salary/view-summary");
                return;
            }

            // Get the adjustment
            BonusAdjustment adjustment = bonusAdjustmentDAO.getAdjustmentById(adjustmentId);
            if (adjustment == null) {
                session.setAttribute("errorMessage", "Adjustment not found.");
                response.sendRedirect(request.getContextPath() + "/salary/view-summary");
                return;
            }

            // Approve the adjustment
            boolean success = bonusAdjustmentDAO.approveAdjustment(adjustmentId, user.getUserID(), approvalComment);

            if (success) {
                // Update payroll
                updatePayrollBonus(adjustment.getEmployeeId(), adjustment.getPayrollMonth());
                session.setAttribute("successMessage", "Bonus adjustment approved and payroll updated.");
            } else {
                session.setAttribute("errorMessage", "Failed to approve adjustment.");
            }

        } catch (Exception e) {
            System.err.println("Error approving adjustment: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error approving adjustment: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/salary/view-summary");
    }

    private void handleRejection(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {

        HttpSession session = request.getSession();

        try {
            int adjustmentId = Integer.parseInt(request.getParameter("adjustmentId"));
            String approvalComment = request.getParameter("approvalComment");

            // Only HR Manager can reject
            if (!"HR Manager".equals(user.getRole()) && !"HR_MANAGER".equals(user.getRole())) {
                session.setAttribute("errorMessage", "Only HR Manager can reject adjustments.");
                response.sendRedirect(request.getContextPath() + "/salary/view-summary");
                return;
            }

            // Reject the adjustment
            boolean success = bonusAdjustmentDAO.rejectAdjustment(adjustmentId, user.getUserID(), approvalComment);

            if (success) {
                session.setAttribute("successMessage", "Bonus adjustment rejected.");
            } else {
                session.setAttribute("errorMessage", "Failed to reject adjustment.");
            }

        } catch (Exception e) {
            System.err.println("Error rejecting adjustment: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error rejecting adjustment: " + e.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/salary/view-summary");
    }
    
    /**
     * Update payroll record with approved bonus adjustments
     */
    private void updatePayrollBonus(int employeeId, Date payrollMonth) {
        try {
            // Get current payroll
            MonthlyPayroll payroll = payrollDAO.getPayrollByEmployeeAndMonth(employeeId, payrollMonth);
            if (payroll == null) {
                System.err.println("Payroll not found for employee " + employeeId + " and month " + payrollMonth);
                return;
            }
            
            // Get total approved adjustments
            BigDecimal totalAdjustments = bonusAdjustmentDAO.getTotalApprovedAdjustments(employeeId, payrollMonth);
            
            // Update total bonus (original bonus + adjustments)
            BigDecimal newTotalBonus = payroll.getTotalBonus().add(totalAdjustments);
            payroll.setTotalBonus(newTotalBonus);
            
            // Recalculate gross and net salary
            BigDecimal grossSalary = payroll.getBaseSalary()
                .add(payroll.getTotalAllowances())
                .add(payroll.getOvertimePay())
                .add(newTotalBonus)
                .add(payroll.getTotalBenefits());
            payroll.setGrossSalary(grossSalary);
            
            BigDecimal netSalary = grossSalary.subtract(payroll.getTotalDeductions());
            payroll.setNetSalary(netSalary);
            
            // Update payroll
            payrollDAO.updatePayroll(payroll);
            
        } catch (Exception e) {
            System.err.println("Error updating payroll bonus: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

