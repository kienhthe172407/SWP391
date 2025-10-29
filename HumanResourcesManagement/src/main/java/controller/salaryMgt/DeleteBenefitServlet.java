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
 * Servlet to handle deleting benefit type
 * Only accessible by HR and HR Manager roles
 * @author admin
 */
@WebServlet("/salary/delete-benefit")
public class DeleteBenefitServlet extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Don't create DAO in init() to avoid connection leaks
        // Create DAO per request instead
    }
    
    /**
     * Handle POST request - delete benefit type
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check if user has permission
        String userRole = (String) session.getAttribute("userRole");
        if (!"HR".equals(userRole) && !"HR Manager".equals(userRole)) {
            session.setAttribute("errorMessage", "Access denied. Only HR staff can delete benefits.");
            response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
            return;
        }
        
        SalaryConfigDAO salaryConfigDAO = null;
        try {
            // Create DAO instance for this request
            salaryConfigDAO = new SalaryConfigDAO();
            
            // Get benefit ID from parameter
            String idStr = request.getParameter("id");
            
            if (idStr == null || idStr.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Benefit ID is required.");
                response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
                return;
            }
            
            int benefitTypeID = Integer.parseInt(idStr);
            
            // Delete benefit from database
            boolean success = salaryConfigDAO.deleteBenefitType(benefitTypeID);
            
            if (success) {
                session.setAttribute("successMessage", "Benefit type deleted successfully.");
            } else {
                session.setAttribute("errorMessage", "Failed to delete benefit type. It may be in use by employees.");
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid benefit ID format.");
        } catch (Exception e) {
            System.err.println("Error in DeleteBenefitServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while deleting the benefit: " + e.getMessage());
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
        return "Delete Benefit Type Servlet";
    }
}

