package controller.salaryMgt;

import dal.PayrollDAO;
import dal.EmployeeDAO;
import model.MonthlyPayroll;
import model.Employee;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for employees to view their own payslips
 * Employees can view their monthly payslips and download them
 * 
 * @author admin
 */
@WebServlet("/employee/my-payslip")
public class ViewMyPayslipServlet extends HttpServlet {
    
    private EmployeeDAO employeeDAO;
    private PayrollDAO payrollDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        employeeDAO = new EmployeeDAO();
        payrollDAO = new PayrollDAO();
    }
    
    /**
     * Handle GET request - display employee's own payslips
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("ViewMyPayslipServlet: doGet called");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Context Path: " + request.getContextPath());
        
        HttpSession session = request.getSession();
        
        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            System.out.println("ViewMyPayslipServlet: User not logged in, redirecting to login");
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }
        
        System.out.println("ViewMyPayslipServlet: User logged in: " + user.getUsername());
        
        // Get employee information for the logged-in user
        Employee employee = employeeDAO.getEmployeeByUserId(user.getUserId());
        if (employee == null) {
            request.setAttribute("errorMessage", "Employee record not found. Please contact HR.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }
        
        // Get all payslips for this employee
        List<MonthlyPayroll> payslips = payrollDAO.getPayrollsByEmployeeId(employee.getEmployeeID());
        
        System.out.println("ViewMyPayslipServlet: Found " + payslips.size() + " payslips for employee " + employee.getEmployeeID());
        
        // Debug: Print payslip details
        if (payslips != null && !payslips.isEmpty()) {
            for (MonthlyPayroll p : payslips) {
                System.out.println("  Payslip: " + p.getPayrollMonth() + 
                    ", Base: " + p.getBaseSalary() + 
                    ", Gross: " + p.getGrossSalary() + 
                    ", Net: " + p.getNetSalary() +
                    ", Status: " + p.getStatus());
            }
        }
        
        // Set attributes for JSP
        request.setAttribute("employee", employee);
        request.setAttribute("payslips", payslips);
        
        // Forward to JSP
        System.out.println("ViewMyPayslipServlet: Forwarding to /salary-mgt/my-payslip.jsp");
        request.getRequestDispatcher("/salary-mgt/my-payslip.jsp").forward(request, response);
    }
}

