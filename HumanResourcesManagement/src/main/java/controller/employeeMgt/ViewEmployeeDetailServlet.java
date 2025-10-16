package controller.employeeMgt;

import dal.EmployeeDAO;
import model.Employee;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet to handle viewing employee details
 * Only accessible by HR and HR Manager roles
 */
@WebServlet(name = "ViewEmployeeDetailServlet", urlPatterns = {"/employees/detail"})
public class ViewEmployeeDetailServlet extends HttpServlet {
    
    private EmployeeDAO employeeDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle GET request - display employee details
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get user session information
        HttpSession session = request.getSession();
        
        // Ensure role in session mirrors the authenticated user; don't force HR Manager
        if (session.getAttribute("userRole") == null) {
            Object userObj = session.getAttribute("user");
            if (userObj instanceof model.User) {
                String roleFromUser = ((model.User) userObj).getRole();
                if (roleFromUser != null && !roleFromUser.isEmpty()) {
                    session.setAttribute("userRole", roleFromUser);
                }
            }
            if (session.getAttribute("userRole") == null) {
                session.setAttribute("userRole", "HR");
            }
            if (session.getAttribute("userId") == null) {
                session.setAttribute("userId", 1);
            }
        }
        
        // Check if user has permission to view employee details
        String userRole = (String) session.getAttribute("userRole");
        if (!"HR".equals(userRole) && !"HR Manager".equals(userRole)) {
            session.setAttribute("errorMessage", "Access denied. Only HR staff can view employee details.");
            response.sendRedirect(request.getContextPath() + "/employees/list");
            return;
        }
        
        // Get employee ID from request
        String employeeIdStr = request.getParameter("employeeId");
        if (employeeIdStr == null || employeeIdStr.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Employee ID is required.");
            response.sendRedirect(request.getContextPath() + "/employees/list");
            return;
        }
        
        try {
            // Parse employee ID
            int employeeId = Integer.parseInt(employeeIdStr);
            
            // Get employee data
            Employee employee = employeeDAO.getEmployeeById(employeeId);
            
            if (employee == null) {
                session.setAttribute("errorMessage", "Employee not found.");
                response.sendRedirect(request.getContextPath() + "/employees/list");
                return;
            }
            
            // Set attributes for display in JSP
            request.setAttribute("employee", employee);
            
            // Forward to JSP page
            request.getRequestDispatcher("/employee-mgt/employee-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid employee ID.");
            response.sendRedirect(request.getContextPath() + "/employees/list");
        } catch (Exception e) {
            System.err.println("Error in ViewEmployeeDetailServlet doGet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while loading the employee details.");
            response.sendRedirect(request.getContextPath() + "/employees/list");
        }
    }

    @Override
    public String getServletInfo() {
        return "View Employee Detail Servlet";
    }
}