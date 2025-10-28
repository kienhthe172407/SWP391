package controller.salaryMgt;

import dal.SalaryConfigDAO;
import model.DeductionType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet to handle adding new deduction type
 * Only accessible by HR and HR Manager roles
 * @author admin
 */
@WebServlet("/salary/add-deduction")
public class AddDeductionServlet extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Don't create DAO in init() to avoid connection leaks
        // Create DAO per request instead
    }
    
    /**
     * Handle GET request - display add deduction form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check permission
        String userRole = (String) session.getAttribute("userRole");
        if (!"HR".equals(userRole) && !"HR Manager".equals(userRole)) {
            session.setAttribute("errorMessage", "Access denied. Only HR staff can add deductions.");
            response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
            return;
        }
        
        request.getRequestDispatcher("/salary-mgt/add-deduction.jsp").forward(request, response);
    }
    
    /**
     * Handle POST request - process add deduction form
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check permission
        String userRole = (String) session.getAttribute("userRole");
        if (!"HR".equals(userRole) && !"HR Manager".equals(userRole)) {
            session.setAttribute("errorMessage", "Access denied. Only HR staff can add deductions.");
            response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
            return;
        }
        
        SalaryConfigDAO salaryConfigDAO = null;
        try {
            // Create DAO instance for this request
            salaryConfigDAO = new SalaryConfigDAO();
            
            // Get form parameters
            String deductionName = request.getParameter("deductionName");
            String description = request.getParameter("description");
            String calculationType = request.getParameter("calculationType");
            String defaultAmountStr = request.getParameter("defaultAmount");
            String defaultPercentageStr = request.getParameter("defaultPercentage");
            String isMandatoryStr = request.getParameter("isMandatory");
            
            // Validate required fields
            if (deductionName == null || deductionName.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Deduction name is required.");
                request.getRequestDispatcher("/salary-mgt/add-deduction.jsp").forward(request, response);
                return;
            }
            
            if (calculationType == null || calculationType.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Calculation type is required.");
                request.getRequestDispatcher("/salary-mgt/add-deduction.jsp").forward(request, response);
                return;
            }
            
            // Check if deduction name already exists
            if (salaryConfigDAO.deductionNameExists(deductionName.trim(), null)) {
                request.setAttribute("errorMessage", "A deduction with this name already exists.");
                request.setAttribute("deductionName", deductionName);
                request.setAttribute("description", description);
                request.setAttribute("calculationType", calculationType);
                request.getRequestDispatcher("/salary-mgt/add-deduction.jsp").forward(request, response);
                return;
            }
            
            // Parse numeric fields
            Double defaultAmount = null;
            Double defaultPercentage = null;
            
            if (defaultAmountStr != null && !defaultAmountStr.trim().isEmpty()) {
                try {
                    defaultAmount = Double.parseDouble(defaultAmountStr.trim());
                    if (defaultAmount < 0) {
                        request.setAttribute("errorMessage", "Default amount cannot be negative.");
                        request.getRequestDispatcher("/salary-mgt/add-deduction.jsp").forward(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid default amount format.");
                    request.getRequestDispatcher("/salary-mgt/add-deduction.jsp").forward(request, response);
                    return;
                }
            }
            
            if (defaultPercentageStr != null && !defaultPercentageStr.trim().isEmpty()) {
                try {
                    defaultPercentage = Double.parseDouble(defaultPercentageStr.trim());
                    if (defaultPercentage < 0 || defaultPercentage > 100) {
                        request.setAttribute("errorMessage", "Default percentage must be between 0 and 100.");
                        request.getRequestDispatcher("/salary-mgt/add-deduction.jsp").forward(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid default percentage format.");
                    request.getRequestDispatcher("/salary-mgt/add-deduction.jsp").forward(request, response);
                    return;
                }
            }
            
            // Validate based on calculation type
            if ("Fixed".equals(calculationType) && defaultAmount == null) {
                request.setAttribute("errorMessage", "Default amount is required for Fixed calculation type.");
                request.getRequestDispatcher("/salary-mgt/add-deduction.jsp").forward(request, response);
                return;
            }
            
            if ("Percentage".equals(calculationType) && defaultPercentage == null) {
                request.setAttribute("errorMessage", "Default percentage is required for Percentage calculation type.");
                request.getRequestDispatcher("/salary-mgt/add-deduction.jsp").forward(request, response);
                return;
            }
            
            boolean isMandatory = "on".equals(isMandatoryStr) || "true".equals(isMandatoryStr);
            
            // Create DeductionType object
            DeductionType deduction = new DeductionType(
                deductionName.trim(),
                description != null ? description.trim() : null,
                calculationType,
                defaultAmount,
                defaultPercentage,
                isMandatory
            );
            
            // Add to database
            boolean success = salaryConfigDAO.addDeductionType(deduction);
            
            if (success) {
                session.setAttribute("successMessage", "Deduction type added successfully.");
                response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
            } else {
                request.setAttribute("errorMessage", "Failed to add deduction type. Please try again.");
                request.getRequestDispatcher("/salary-mgt/add-deduction.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Error in AddDeductionServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while adding the deduction: " + e.getMessage());
            request.getRequestDispatcher("/salary-mgt/add-deduction.jsp").forward(request, response);
        } finally {
            // Always close the DAO connection
            if (salaryConfigDAO != null) {
                salaryConfigDAO.close();
            }
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Add Deduction Type Servlet";
    }
}

