package controller.salaryMgt;

import dal.SalaryConfigDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet to handle deleting deduction type
 * Only accessible by HR and HR Manager roles
 * @author admin
 */
@WebServlet("/salary/delete-deduction")
public class DeleteDeductionServlet extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Don't create DAO in init() to avoid connection leaks
        // Create DAO per request instead
    }
    
    /**
     * Handle POST request - delete deduction type
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check permission
        String userRole = (String) session.getAttribute("userRole");
        if (!"HR".equals(userRole) && !"HR Manager".equals(userRole)) {
            session.setAttribute("errorMessage", "Access denied. Only HR staff can delete deductions.");
            response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
            return;
        }
        
        SalaryConfigDAO salaryConfigDAO = null;
        try {
            // Create DAO instance for this request
            salaryConfigDAO = new SalaryConfigDAO();
            
            String idStr = request.getParameter("id");
            
            if (idStr == null || idStr.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Deduction ID is required.");
                response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
                return;
            }
            
            int deductionTypeID = Integer.parseInt(idStr);
            
            // Delete deduction from database
            boolean success = salaryConfigDAO.deleteDeductionType(deductionTypeID);
            
            if (success) {
                session.setAttribute("successMessage", "Deduction type deleted successfully.");
            } else {
                session.setAttribute("errorMessage", "Failed to delete deduction type. It may be in use by employees.");
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid deduction ID format.");
        } catch (Exception e) {
            System.err.println("Error in DeleteDeductionServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while deleting the deduction: " + e.getMessage());
        } finally {
            // Always close the DAO connection
            if (salaryConfigDAO != null) {
                salaryConfigDAO.close();
            }
        }
        
        // Redirect back to manage page
        response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
    }
    
    @Override
    public String getServletInfo() {
        return "Delete Deduction Type Servlet";
    }
}

