package controller.salaryMgt;

import dal.SalaryConfigDAO;
import model.BenefitType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Servlet to handle adding new benefit type
 * Only accessible by HR and HR Manager roles
 * @author admin
 */
@WebServlet("/salary/add-benefit")
public class AddBenefitServlet extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Don't create DAO in init() to avoid connection leaks
        // Create DAO per request instead
    }
    
    /**
     * Handle GET request - display add benefit form
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
        
        // Check if user has permission
        String userRole = (String) session.getAttribute("userRole");
        if (!"HR".equals(userRole) && !"HR Manager".equals(userRole)) {
            session.setAttribute("errorMessage", "Access denied. Only HR staff can add benefits.");
            response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
            return;
        }
        
        // Forward to add benefit form
        request.getRequestDispatcher("/salary-mgt/add-benefit.jsp").forward(request, response);
    }
    
    /**
     * Handle POST request - process add benefit form
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
            session.setAttribute("errorMessage", "Access denied. Only HR staff can add benefits.");
            response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
            return;
        }
        
        SalaryConfigDAO salaryConfigDAO = null;
        try {
            // Create DAO instance for this request
            salaryConfigDAO = new SalaryConfigDAO();
            
            // Get form parameters
            String benefitName = request.getParameter("benefitName");
            String description = request.getParameter("description");
            String calculationType = request.getParameter("calculationType");
            String defaultAmountStr = request.getParameter("defaultAmount");
            String defaultPercentageStr = request.getParameter("defaultPercentage");
            String isTaxableStr = request.getParameter("isTaxable");
            
            // Validate required fields
            if (benefitName == null || benefitName.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Benefit name is required.");
                request.getRequestDispatcher("/salary-mgt/add-benefit.jsp").forward(request, response);
                return;
            }
            
            if (calculationType == null || calculationType.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Calculation type is required.");
                request.getRequestDispatcher("/salary-mgt/add-benefit.jsp").forward(request, response);
                return;
            }
            
            // Check if benefit name already exists
            if (salaryConfigDAO.benefitNameExists(benefitName.trim(), null)) {
                request.setAttribute("errorMessage", "A benefit with this name already exists.");
                request.setAttribute("benefitName", benefitName);
                request.setAttribute("description", description);
                request.setAttribute("calculationType", calculationType);
                request.getRequestDispatcher("/salary-mgt/add-benefit.jsp").forward(request, response);
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
                        request.getRequestDispatcher("/salary-mgt/add-benefit.jsp").forward(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid default amount format.");
                    request.getRequestDispatcher("/salary-mgt/add-benefit.jsp").forward(request, response);
                    return;
                }
            }
            
            if (defaultPercentageStr != null && !defaultPercentageStr.trim().isEmpty()) {
                try {
                    defaultPercentage = Double.parseDouble(defaultPercentageStr.trim());
                    if (defaultPercentage < 0 || defaultPercentage > 100) {
                        request.setAttribute("errorMessage", "Default percentage must be between 0 and 100.");
                        request.getRequestDispatcher("/salary-mgt/add-benefit.jsp").forward(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid default percentage format.");
                    request.getRequestDispatcher("/salary-mgt/add-benefit.jsp").forward(request, response);
                    return;
                }
            }
            
            // Validate based on calculation type
            if ("Fixed".equals(calculationType) && defaultAmount == null) {
                request.setAttribute("errorMessage", "Default amount is required for Fixed calculation type.");
                request.getRequestDispatcher("/salary-mgt/add-benefit.jsp").forward(request, response);
                return;
            }
            
            if ("Percentage".equals(calculationType) && defaultPercentage == null) {
                request.setAttribute("errorMessage", "Default percentage is required for Percentage calculation type.");
                request.getRequestDispatcher("/salary-mgt/add-benefit.jsp").forward(request, response);
                return;
            }
            
            boolean isTaxable = "on".equals(isTaxableStr) || "true".equals(isTaxableStr);
            
            // Create BenefitType object
            BenefitType benefit = new BenefitType(
                benefitName.trim(),
                description != null ? description.trim() : null,
                calculationType,
                defaultAmount,
                defaultPercentage,
                isTaxable
            );
            
            // Add to database
            boolean success = salaryConfigDAO.addBenefitType(benefit);
            
            if (success) {
                session.setAttribute("successMessage", "Benefit type added successfully.");
                response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
            } else {
                request.setAttribute("errorMessage", "Failed to add benefit type. Please try again.");
                request.getRequestDispatcher("/salary-mgt/add-benefit.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("Error in AddBenefitServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while adding the benefit: " + e.getMessage());
            request.getRequestDispatcher("/salary-mgt/add-benefit.jsp").forward(request, response);
        } finally {
            // Always close the DAO connection
            if (salaryConfigDAO != null) {
                salaryConfigDAO.close();
            }
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Add Benefit Type Servlet";
    }
}

