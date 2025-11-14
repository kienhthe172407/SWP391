package controller.salaryMgt;

import dal.SalaryComponentDAO;
import dal.EmployeeDAO;
import model.SalaryComponent;
import model.Employee;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet to view all imported salary components
 * Shows base salary and allowances for all employees
 * Provides navigation to salary calculation page
 * @author admin
 */
@WebServlet("/salary/view-components")
public class ViewSalaryComponentsServlet extends HttpServlet {
    
    private SalaryComponentDAO salaryComponentDAO;
    private EmployeeDAO employeeDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        salaryComponentDAO = new SalaryComponentDAO();
        employeeDAO = new EmployeeDAO();
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
        
        model.User user = (model.User) session.getAttribute("user");
        
        // Check permission
        if (!util.PermissionChecker.hasPermission(user, util.PermissionConstants.SALARY_VIEW_COMPONENTS)) {
            request.setAttribute("errorMessage", "Bạn không có quyền xem thành phần lương");
            request.getRequestDispatcher("/error/403.jsp").forward(request, response);
            return;
        }
        
        try {
            // Get all active salary components
            List<SalaryComponent> components = salaryComponentDAO.getAllActiveSalaryComponents();
            
            // Create a list to hold component data with employee information
            List<SalaryComponentView> componentViews = new ArrayList<>();
            
            for (SalaryComponent component : components) {
                Employee employee = employeeDAO.getEmployeeById(component.getEmployeeID());
                if (employee != null) {
                    SalaryComponentView view = new SalaryComponentView();
                    view.setComponent(component);
                    view.setEmployee(employee);
                    componentViews.add(view);
                }
            }
            
            // Set attributes
            request.setAttribute("componentViews", componentViews);
            request.setAttribute("totalEmployees", componentViews.size());
            
            // Check for messages from session
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
            request.getRequestDispatcher("/salary-mgt/view-salary-components.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error in ViewSalaryComponentsServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading salary components.");
            request.getRequestDispatcher("/salary-mgt/view-salary-components.jsp").forward(request, response);
        }
    }
    
    /**
     * Helper class to combine SalaryComponent with Employee information
     */
    public static class SalaryComponentView {
        private SalaryComponent component;
        private Employee employee;
        
        public SalaryComponent getComponent() {
            return component;
        }
        
        public void setComponent(SalaryComponent component) {
            this.component = component;
        }
        
        public Employee getEmployee() {
            return employee;
        }
        
        public void setEmployee(Employee employee) {
            this.employee = employee;
        }
    }
}

