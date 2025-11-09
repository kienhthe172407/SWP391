package controller.contractMgt;

import dal.ContractDAO;
import dal.EmployeeDAO;
import model.Contract;
import model.Employee;
import model.User;
import util.ContractPDFExportUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Servlet to handle exporting contract to PDF format
 * @author admin
 */
@WebServlet(name = "ExportContractServlet", urlPatterns = {"/contracts/export"})
public class ExportContractServlet extends HttpServlet {
    
    private ContractDAO contractDAO;
    private EmployeeDAO employeeDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        contractDAO = new ContractDAO();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle GET request - export contract as PDF
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
        
        // Determine redirect URL based on role
        String redirectUrl = "/contracts/list";
        if ("Employee".equals(userRole) || "EMPLOYEE".equals(userRole)) {
            redirectUrl = "/employee/my-contract";
        }
        
        // Get contract ID from request
        String contractIdStr = request.getParameter("id");
        if (contractIdStr == null || contractIdStr.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Contract ID is required");
            response.sendRedirect(request.getContextPath() + redirectUrl);
            return;
        }
        
        try {
            int contractId = Integer.parseInt(contractIdStr);
            
            // Get contract details
            Contract contract = contractDAO.getContractById(contractId);
            
            if (contract == null) {
                session.setAttribute("errorMessage", "Contract not found");
                response.sendRedirect(request.getContextPath() + redirectUrl);
                return;
            }
            
            // Check if user has permission to export this contract
            // HR and HR Manager can export any contract
            // Employees can only export their own contracts
            if (!"HR".equals(userRole) && !"HR Manager".equals(userRole)) {
                // Get employee information for the logged-in user
                Employee employee = employeeDAO.getEmployeeByUserId(userId);
                if (employee == null) {
                    session.setAttribute("errorMessage", "Employee record not found. Please contact HR.");
                    response.sendRedirect(request.getContextPath() + redirectUrl);
                    return;
                }
                
                // Check if this is the employee's own contract
                if (employee.getEmployeeID() != contract.getEmployeeID()) {
                    session.setAttribute("errorMessage", "You don't have permission to export this contract");
                    response.sendRedirect(request.getContextPath() + redirectUrl);
                    return;
                }
            }
            
            // Generate PDF
            byte[] pdfData = ContractPDFExportUtil.generateContractPDF(contract);
            
            // Generate filename
            String contractNumber = contract.getContractNumber() != null 
                ? contract.getContractNumber().replaceAll("[^a-zA-Z0-9]", "_")
                : "Contract_" + contract.getContractID();
            String filename = contractNumber + ".pdf";
            
            // Set response headers
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", 
                "attachment; filename=\"" + filename + "\"");
            response.setContentLength(pdfData.length);
            
            // Write PDF to response
            try (OutputStream out = response.getOutputStream()) {
                out.write(pdfData);
                out.flush();
            }
            
            System.out.println("Contract exported successfully: " + filename + 
                             " by user: " + userId);
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid contract ID");
            response.sendRedirect(request.getContextPath() + redirectUrl);
        } catch (Exception e) {
            System.err.println("Error exporting contract: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error generating PDF: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + redirectUrl);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Export Contract to PDF Servlet";
    }
}

