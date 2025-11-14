package controller.salaryMgt;

import dal.SalaryConfigDAO;
import model.BenefitType;
import model.DeductionType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet to handle viewing and managing benefits and deductions setup
 * Displays list of all benefit types and deduction types
 * Only accessible by HR and HR Manager roles
 * @author admin
 */
@WebServlet("/salary/manage-benefits-deductions")
public class ManageBenefitsDeductionsServlet extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Don't create DAO in init() to avoid connection leaks
        // Create DAO per request instead
    }
    
    /**
     * Handle GET request - display benefits and deductions list
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
        
        // Ensure role in session
        if (session.getAttribute("userRole") == null) {
            Object userObj = session.getAttribute("user");
            if (userObj instanceof model.User) {
                String roleFromUser = ((model.User) userObj).getRole();
                if (roleFromUser != null && !roleFromUser.isEmpty()) {
                    session.setAttribute("userRole", roleFromUser);
                }
            }
            // Development fallback
            if (session.getAttribute("userRole") == null) {
                session.setAttribute("userRole", "HR");
            }
            if (session.getAttribute("userId") == null) {
                session.setAttribute("userId", 1);
            }
        }
        
        // Check permission
        model.User currentUser = (model.User) session.getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (!util.PermissionChecker.hasPermission(currentUser, util.PermissionConstants.SALARY_VIEW)) {
            request.setAttribute("errorMessage", "Bạn không có quyền quản lý lương và phúc lợi");
            request.getRequestDispatcher("/error/403.jsp").forward(request, response);
            return;
        }
        
        SalaryConfigDAO salaryConfigDAO = null;
        try {
            // Create DAO instance for this request
            salaryConfigDAO = new SalaryConfigDAO();
            
            // Get all benefit types
            List<BenefitType> benefits = salaryConfigDAO.getAllBenefitTypes();
            
            // Get all deduction types
            List<DeductionType> deductions = salaryConfigDAO.getAllDeductionTypes();
            
            // Set attributes for JSP
            request.setAttribute("benefits", benefits);
            request.setAttribute("deductions", deductions);
            
            // Check for success/error messages from session
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
            request.getRequestDispatcher("/salary-mgt/manage-benefits-deductions.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error in ManageBenefitsDeductionsServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading benefits and deductions.");
            request.getRequestDispatcher("/salary-mgt/manage-benefits-deductions.jsp").forward(request, response);
        } finally {
            // Always close the DAO connection
            if (salaryConfigDAO != null) {
                salaryConfigDAO.close();
            }
        }
    }
    
    /**
     * Handle POST request - same as GET
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "Manage Benefits and Deductions Setup Servlet";
    }
}

