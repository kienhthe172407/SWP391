package controller.contractMgt;

import dal.ContractDAO;
import dal.EmployeeDAO;
import model.Contract;
import model.Employee;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * Servlet for employees to view their own employment contracts
 * Employees can view their contract details including terms, start/end dates, salary, etc.
 * 
 * @author admin
 */
@WebServlet("/employee/my-contract")
public class ViewMyContractServlet extends HttpServlet {
    
    private EmployeeDAO employeeDAO;
    private ContractDAO contractDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        employeeDAO = new EmployeeDAO();
        contractDAO = new ContractDAO();
    }
    
    /**
     * Handle GET request - display employee's own contracts
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }
        
        // Get employee information for the logged-in user
        Employee employee = employeeDAO.getEmployeeByUserId(user.getUserId());
        if (employee == null) {
            request.setAttribute("errorMessage", "Employee record not found. Please contact HR.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }
        
        // Get all contracts for this employee
        List<Contract> allContracts = contractDAO.getContractsByEmployeeId(employee.getEmployeeID());
        
        // Filter only Active contracts for display
        List<Contract> activeContracts = allContracts.stream()
                .filter(contract -> "Active".equalsIgnoreCase(contract.getContractStatus()))
                .collect(java.util.stream.Collectors.toList());
        
        // Check for non-active contracts to show status message
        List<Contract> nonActiveContracts = allContracts.stream()
                .filter(contract -> !"Active".equalsIgnoreCase(contract.getContractStatus()))
                .collect(java.util.stream.Collectors.toList());
        
        // Set attributes for JSP
        request.setAttribute("employee", employee);
        request.setAttribute("contracts", activeContracts);
        request.setAttribute("allContracts", allContracts);
        request.setAttribute("nonActiveContracts", nonActiveContracts);
        
        // Check if there's a specific contract ID to view
        String contractIdParam = request.getParameter("id");
        if (contractIdParam != null && !contractIdParam.trim().isEmpty()) {
            try {
                int contractId = Integer.parseInt(contractIdParam);
                
                // Find the contract in the active contracts list to ensure it belongs to this employee and is Active
                Contract selectedContract = null;
                for (Contract c : activeContracts) {
                    if (c.getContractID() == contractId) {
                        selectedContract = c;
                        break;
                    }
                }
                
                if (selectedContract != null) {
                    request.setAttribute("selectedContract", selectedContract);
                } else {
                    // Check if contract exists but is not active
                    boolean contractExists = allContracts.stream()
                            .anyMatch(c -> c.getContractID() == contractId);
                    
                    if (contractExists) {
                        request.setAttribute("errorMessage", "This contract is not currently active and cannot be viewed.");
                    } else {
                        request.setAttribute("errorMessage", "Contract not found or you don't have permission to view it.");
                    }
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid contract ID.");
            }
        }
        
        // Forward to JSP
        request.getRequestDispatcher("/contract-mgt/my-contract.jsp").forward(request, response);
    }
}
