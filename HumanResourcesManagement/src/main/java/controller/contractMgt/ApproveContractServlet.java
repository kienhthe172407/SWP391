package controller.contractMgt;

import dal.ContractDAO;
import model.Contract;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet to handle contract approval by HR Manager
 * @author admin
 */
@WebServlet(name = "ApproveContractServlet", urlPatterns = {"/contracts/approve"})
public class ApproveContractServlet extends HttpServlet {
    
    private ContractDAO contractDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        contractDAO = new ContractDAO();
    }

    /**
     * Handle GET request - display contract approval form or list of contracts pending approval
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get current user role from session
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("userRole");
        
        // Check if user is HR Manager
        if (userRole == null || !userRole.equals("HR Manager")) {
            session.setAttribute("errorMessage", "You don't have permission to approve contracts");
            response.sendRedirect(request.getContextPath() + "/contracts/list");
            return;
        }
        
        // Get contract ID from request
        String contractIdStr = request.getParameter("id");
        
        // If contract ID is provided, show single contract approval page
        if (contractIdStr != null && !contractIdStr.isEmpty()) {
            try {
                int contractId = Integer.parseInt(contractIdStr);
                
                // Get contract details
                Contract contract = contractDAO.getContractById(contractId);
                
                if (contract == null) {
                    session.setAttribute("errorMessage", "Contract not found");
                    response.sendRedirect(request.getContextPath() + "/contracts/list");
                    return;
                }
                
                // Check if contract is pending approval
                if (!contract.getContractStatus().equals("Pending Approval") && !contract.getContractStatus().equals("Draft")) {
                    session.setAttribute("errorMessage", "Contract is not eligible for approval");
                    response.sendRedirect(request.getContextPath() + "/contracts/list");
                    return;
                }
                
                // Set contract in request and forward to approval page
                request.setAttribute("contract", contract);
                request.getRequestDispatcher("/contract-mgt/approve-contract.jsp").forward(request, response);
                return;
            } catch (NumberFormatException e) {
                session.setAttribute("errorMessage", "Invalid contract ID");
                response.sendRedirect(request.getContextPath() + "/contracts/list");
                return;
            }
        }
        
        // If no contract ID provided, show list of contracts pending approval
        // Pagination parameters
        int pageSize = 10;
        int currentPage = 1;
        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageStr);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                // Ignore and use default
            }
        }
        
        // Search parameters
        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");
        
        // If no status filter is provided, default to showing Draft and Pending Approval
        if (status == null || status.isEmpty()) {
            status = "approval"; // Special value to indicate we want both Draft and Pending Approval
        }
        
        // Get contracts for approval
        java.util.List<Contract> contracts;
        int totalRecords;
        
        if ("approval".equals(status)) {
            // Get contracts with Draft or Pending Approval status
            contracts = contractDAO.getContractsForApproval(keyword, currentPage, pageSize);
            totalRecords = contractDAO.countContractsForApproval(keyword);
        } else {
            // Get contracts with specific status
            contracts = contractDAO.getContractsByStatus(status, keyword, currentPage, pageSize);
            totalRecords = contractDAO.countContractsByStatus(status, keyword);
        }
        
        // Calculate total pages
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (totalPages == 0) totalPages = 1;
        
        // Set attributes for the view
        request.setAttribute("contracts", contracts);
        request.setAttribute("totalRecords", totalRecords);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("keyword", keyword);
        request.setAttribute("status", status);
        request.setAttribute("approvalMode", true); // Flag to indicate we're in approval mode
        
        // Forward to approval page
        request.getRequestDispatcher("/contract-mgt/approve-contract.jsp").forward(request, response);
    }

    /**
     * Handle POST request - process contract approval/rejection
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get current user from session
        HttpSession session = request.getSession();
        String userRole = (String) session.getAttribute("userRole");
        Integer currentUserId = (Integer) session.getAttribute("userId");
        
        // Check if user is HR Manager
        if (userRole == null || !userRole.equals("HR Manager") || currentUserId == null) {
            session.setAttribute("errorMessage", "You don't have permission to approve contracts");
            response.sendRedirect(request.getContextPath() + "/contracts/list");
            return;
        }
        
        // Get form data
        String contractIdStr = request.getParameter("contractId");
        String action = request.getParameter("action");
        String comment = request.getParameter("comment");
        
        if (contractIdStr == null || contractIdStr.isEmpty() || action == null || action.isEmpty()) {
            session.setAttribute("errorMessage", "Missing required parameters");
            response.sendRedirect(request.getContextPath() + "/contracts/list");
            return;
        }
        
        try {
            int contractId = Integer.parseInt(contractIdStr);
            
            boolean success = false;
            if (action.equals("approve")) {
                success = contractDAO.approveContract(contractId, currentUserId, comment);
                if (success) {
                    session.setAttribute("successMessage", "Contract approved successfully");
                }
            } else if (action.equals("reject")) {
                success = contractDAO.rejectContract(contractId, currentUserId, comment);
                if (success) {
                    session.setAttribute("successMessage", "Contract rejected successfully");
                }
            }
            
            if (!success) {
                session.setAttribute("errorMessage", "Failed to process contract approval");
            }
            
            response.sendRedirect(request.getContextPath() + "/contracts/list");
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid contract ID");
            response.sendRedirect(request.getContextPath() + "/contracts/list");
        }
    }

    @Override
    public String getServletInfo() {
        return "Contract Approval Servlet";
    }
}
