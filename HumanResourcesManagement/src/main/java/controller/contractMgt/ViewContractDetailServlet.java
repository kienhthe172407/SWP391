package controller.contractMgt;

import dal.ContractDAO;
import model.Contract;
import model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet to handle viewing contract details
 * @author admin
 */
@WebServlet(name = "ViewContractDetailServlet", urlPatterns = {"/contracts/detail"})
public class ViewContractDetailServlet extends HttpServlet {
    
    private ContractDAO contractDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        contractDAO = new ContractDAO();
    }

    /**
     * Handle GET request - display contract details
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
        User user = (User) session.getAttribute("user");

        // If not logged in, redirect to login
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String userRole = user.getRole();
        Integer userId = user.getUserId();
        
        // Get contract ID from request
        String contractIdStr = request.getParameter("id");
        if (contractIdStr == null || contractIdStr.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Contract ID is required");
            response.sendRedirect(request.getContextPath() + "/contracts/list");
            return;
        }
        
        try {
            int contractId = Integer.parseInt(contractIdStr);
            
            // Get contract details
            Contract contract = contractDAO.getContractById(contractId);
            
            if (contract == null) {
                session.setAttribute("errorMessage", "Contract not found");
                response.sendRedirect(request.getContextPath() + "/contracts/list");
                return;
            }
            
            // Check if user has permission to view this contract
            // HR and HR Manager can only view draft contracts they created
            if ("HR".equals(userRole) || "HR Manager".equals(userRole)) {
                if ("Draft".equals(contract.getContractStatus()) && 
                    !userId.equals(contract.getCreatedBy())) {
                    session.setAttribute("errorMessage", "You don't have permission to view this contract");
                    response.sendRedirect(request.getContextPath() + "/contracts/list");
                    return;
                }
            }
            
            // Set contract details for display
            request.setAttribute("contract", contract);
            
            // Forward to contract detail page
            request.getRequestDispatcher("/contract-mgt/contract-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid contract ID");
            response.sendRedirect(request.getContextPath() + "/contracts/list");
        }
    }

    @Override
    public String getServletInfo() {
        return "View Contract Detail Servlet";
    }
}
