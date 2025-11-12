package controller.contractMgt;

import dal.ContractDAO;
import dal.EmployeeDAO;
import model.Contract;
import model.Employee;
import model.User;
import util.ContractNumberGenerator;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet to handle creating new employment contracts
 * @author admin
 */
@WebServlet(name = "CreateContractServlet", urlPatterns = {"/contracts/create"})
public class CreateContractServlet extends HttpServlet {
    
    private ContractDAO contractDAO;
    private EmployeeDAO employeeDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        contractDAO = new ContractDAO();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle GET request - display contract creation form
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get employee list for dropdown - only employees without active contracts
        List<Employee> employees = employeeDAO.getEmployeesWithoutActiveContract();
        request.setAttribute("employees", employees);
        
        // Generate a default contract number
        String defaultContractNumber = ContractNumberGenerator.generateContractNumber();
        request.setAttribute("defaultContractNumber", defaultContractNumber);
        
        // Forward to the contract creation form
        request.getRequestDispatcher("/contract-mgt/create-contract.jsp").forward(request, response);
    }

    /**
     * Handle POST request - process contract creation
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get current user from session (for created_by field)
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        
        // If not logged in, redirect to login
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Get user ID from the user object
        int currentUserId = currentUser.getUserId();
        
        try {
            // Get form data
            int employeeId = Integer.parseInt(request.getParameter("employeeId"));
            String contractNumber = request.getParameter("contractNumber");
            String contractType = request.getParameter("contractType");
            String startDateStr = request.getParameter("startDate");
            String endDateStr = request.getParameter("endDate");
            String salaryAmountStr = request.getParameter("salaryAmount");
            String jobDescription = request.getParameter("jobDescription");
            String approvalComment = request.getParameter("approvalComment");
            String saveDraft = request.getParameter("saveDraft");
            String contractStatus = request.getParameter("contractStatus");

            // Create contract object
            Contract contract = new Contract();
            contract.setEmployeeID(employeeId);
            contract.setContractNumber(contractNumber);
            contract.setContractType(contractType);

            // Determine contract status based on user role
            String userRole = currentUser.getRole();
            
            if (contractStatus != null && !contractStatus.isEmpty()) {
                // If contractStatus is explicitly provided, use it
                contract.setContractStatus(contractStatus);
            } else if ("true".equals(saveDraft)) {
                // Save as draft if checkbox is checked
                contract.setContractStatus("Draft");
            } else if ("HR Manager".equals(userRole)) {
                // HR Manager creates contract -> immediately Active
                contract.setContractStatus("Active");
                contract.setApprovedBy(currentUserId); // Auto-approved by creator
                contract.setApprovedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                contract.setSignedDate(new Date(System.currentTimeMillis())); // Auto set signed date
            } else {
                // HR Staff creates contract -> Pending Approval
                contract.setContractStatus("Pending Approval");
            }

            contract.setApprovalComment(approvalComment);
            
            // Parse dates
            if (startDateStr != null && !startDateStr.isEmpty()) {
                contract.setStartDate(Date.valueOf(startDateStr));
            }
            
            if (endDateStr != null && !endDateStr.isEmpty()) {
                contract.setEndDate(Date.valueOf(endDateStr));
            }
            
            // Parse salary amount
            if (salaryAmountStr != null && !salaryAmountStr.isEmpty()) {
                contract.setSalaryAmount(new BigDecimal(salaryAmountStr));
            }
            
            contract.setJobDescription(jobDescription);
            contract.setCreatedBy(currentUserId);
            
            // Save contract to database
            boolean success = contractDAO.createContract(contract);
            
            if (success) {
                // Set success message based on contract status
                String status = contract.getContractStatus();
                if ("Draft".equals(status)) {
                    session.setAttribute("successMessage", "Contract saved as draft successfully!");
                } else if ("Active".equals(status)) {
                    session.setAttribute("successMessage", "Contract created and activated successfully!");
                } else if ("Pending Approval".equals(status)) {
                    session.setAttribute("successMessage", "Contract submitted for HR Manager approval!");
                } else {
                    session.setAttribute("successMessage", "Contract created successfully!");
                }
                response.sendRedirect(request.getContextPath() + "/contracts/list");
            } else {
                // If save failed, return to form with error message
                request.setAttribute("errorMessage", "Failed to create contract. Please try again.");
                
                // Get employee list for dropdown again
                List<Employee> employees = employeeDAO.getEmployeesWithoutActiveContract();
                request.setAttribute("employees", employees);
                
                // Keep form data for re-population
                request.setAttribute("contract", contract);
                
                // Forward back to the form
                request.getRequestDispatcher("/contract-mgt/create-contract.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            // Handle parsing errors
            request.setAttribute("errorMessage", "Invalid numeric value: " + e.getMessage());
            
            // Get employee list for dropdown again
            List<Employee> employees = employeeDAO.getEmployeesWithoutActiveContract();
            request.setAttribute("employees", employees);
            
            // Forward back to the form
            request.getRequestDispatcher("/contract-mgt/create-contract.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            // Handle date format errors
            request.setAttribute("errorMessage", "Invalid date format: " + e.getMessage());
            
            // Get employee list for dropdown again
            List<Employee> employees = employeeDAO.getEmployeesWithoutActiveContract();
            request.setAttribute("employees", employees);
            
            // Forward back to the form
            request.getRequestDispatcher("/contract-mgt/create-contract.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Create Contract Servlet";
    }
}
