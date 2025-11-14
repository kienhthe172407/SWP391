package controller.employeeMgt;

import dal.EmployeeDAO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet to handle deleting employee records
 * Only accessible by HR Manager role
 */
@WebServlet(name = "DeleteEmployeeServlet", urlPatterns = {"/employees/delete"})
public class DeleteEmployeeServlet extends HttpServlet {
    
    private EmployeeDAO employeeDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handles the HTTP POST method for deleting an employee
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        model.User currentUser = (model.User) session.getAttribute("user");
        
        // Check authentication
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Check permission using PermissionChecker
        if (!util.PermissionChecker.hasPermission(currentUser, util.PermissionConstants.EMPLOYEE_DELETE)) {
            request.setAttribute("errorMessage", "Bạn không có quyền xóa nhân viên");
            request.getRequestDispatcher("/error/403.jsp").forward(request, response);
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
            
            // Get employee name for success message
            String employeeName = employeeDAO.getEmployeeNameById(employeeId);
            
            // Delete employee
            boolean success = employeeDAO.deleteEmployee(employeeId);
            
            if (success) {
                session.setAttribute("successMessage", "Employee " + (employeeName != null ? employeeName : "ID #" + employeeId) + " has been successfully deleted.");
            } else {
                session.setAttribute("errorMessage", "Failed to delete employee. The employee may not exist or may have related records.");
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid employee ID format.");
        } catch (Exception e) {
            System.err.println("Error in DeleteEmployeeServlet doPost: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while deleting the employee: " + e.getMessage());
        }
        
        // Redirect back to employee list
        response.sendRedirect(request.getContextPath() + "/employees/list");
    }

    @Override
    public String getServletInfo() {
        return "Delete Employee Servlet";
    }
}
