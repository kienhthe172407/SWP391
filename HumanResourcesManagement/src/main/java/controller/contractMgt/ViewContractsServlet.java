package controller.contractMgt;

import dal.ContractDAO;
import model.Contract;
import model.User;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet to handle viewing contracts list
 * @author admin
 */
public class ViewContractsServlet extends HttpServlet {
    
    private ContractDAO contractDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        contractDAO = new ContractDAO();
    }

    /**
     * Handle GET request - display contracts list
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

        // Pagination parameters
        final int PAGE_SIZE = 10;
        int currentPage = 1;

        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        // Get search parameters from request
        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");
        // Normalize status: treat "All Status" (or variants) the same as empty
        if (status != null) {
            status = status.trim();
            if ("All Status".equalsIgnoreCase(status)) {
                status = ""; // do not filter by status
            }
        }

        // Always set search params to preserve them in pagination links
        request.setAttribute("keyword", keyword != null ? keyword : "");
        request.setAttribute("status", status != null ? status : "");

        List<Contract> contracts;
        int totalRecords;
        
        // If search parameters exist, call searchContracts with pagination and role filtering
        if ((keyword != null && !keyword.trim().isEmpty()) ||
            (status != null && !status.trim().isEmpty())) {
            contracts = contractDAO.searchContracts(keyword, status, currentPage, PAGE_SIZE, userRole, userId);
            totalRecords = contractDAO.getTotalSearchResults(keyword, status, userRole, userId);
        } else {
            // Otherwise, get all contracts with pagination and role filtering
            contracts = contractDAO.getAllContracts(currentPage, PAGE_SIZE, userRole, userId);
            totalRecords = contractDAO.getTotalContracts(userRole, userId);
        }
        
        // Calculate pagination info
        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
        
        // Get all contract statuses from database
        List<String> contractStatuses = contractDAO.getAllContractStatuses();
        
        // Set attributes for display in JSP
        request.setAttribute("contracts", contracts);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);
        request.setAttribute("pageSize", PAGE_SIZE);
        request.setAttribute("contractStatuses", contractStatuses);
        
        // Forward to JSP page
        request.getRequestDispatcher("/contract-mgt/list-contracts.jsp").forward(request, response);
    }

    /**
     * Handle POST request - search contracts
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST handles same as GET
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "View Contracts List Servlet";
    }
}

