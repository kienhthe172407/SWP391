package controller.contractMgt;

import dal.ContractDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet to handle contract deletion
 */
@WebServlet(name = "DeleteContractServlet", urlPatterns = {"/contracts/delete"})
public class DeleteContractServlet extends HttpServlet {

    /**
     * Handles the HTTP POST method for deleting a contract
     *
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
        
        // Check permission
        if (!util.PermissionChecker.hasPermission(currentUser, util.PermissionConstants.CONTRACT_DELETE)) {
            request.setAttribute("errorMessage", "Bạn không có quyền xóa hợp đồng");
            request.getRequestDispatcher("/error/403.jsp").forward(request, response);
            return;
        }
        
        // Get contract ID from request
        String contractIdParam = request.getParameter("contractId");
        
        if (contractIdParam == null || contractIdParam.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Invalid contract ID");
            response.sendRedirect(request.getContextPath() + "/contracts/list");
            return;
        }
        
        try {
            int contractId = Integer.parseInt(contractIdParam);
            
            // Create DAO instance
            ContractDAO contractDAO = new ContractDAO();
            
            // Delete the contract without permission checking
            boolean success = contractDAO.deleteContract(contractId);
            
            if (success) {
                session.setAttribute("successMessage", "Contract #" + contractId + " has been successfully deleted");
            } else {
                session.setAttribute("errorMessage", "Failed to delete contract #" + contractId);
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid contract ID format");
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error deleting contract: " + e.getMessage());
        }
        
        // Redirect back to contracts list
        response.sendRedirect(request.getContextPath() + "/contracts/list");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for deleting contracts";
    }
}
