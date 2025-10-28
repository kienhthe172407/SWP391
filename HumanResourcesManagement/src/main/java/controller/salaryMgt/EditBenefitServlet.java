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
 * Servlet to handle editing existing benefit type
 * Only accessible by HR and HR Manager roles
 * @author admin
 */
@WebServlet("/salary/edit-benefit")
public class EditBenefitServlet extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Don't create DAO in init() to avoid connection leaks
        // Create DAO per request instead
    }
    
    /**
     * Handle GET request - display edit benefit form
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check if user has permission
        String userRole = (String) session.getAttribute("userRole");
        if (!"HR".equals(userRole) && !"HR Manager".equals(userRole)) {
            session.setAttribute("errorMessage", "Access denied. Only HR staff can edit benefits.");
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
            
            // Get benefit from database
            BenefitType benefit = salaryConfigDAO.getBenefitTypeById(benefitTypeID);
            
            if (benefit == null) {
                session.setAttribute("errorMessage", "Benefit not found.");
                response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
                return;
            }
            
            // Set attribute for JSP
            request.setAttribute("benefit", benefit);
            
            // Forward to edit form
            request.getRequestDispatcher("/salary-mgt/edit-benefit.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid benefit ID format.");
            response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
        } catch (Exception e) {
            System.err.println("Error in EditBenefitServlet doGet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while loading the benefit.");
            response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
        } finally {
            // Always close the DAO connection
            if (salaryConfigDAO != null) {
                salaryConfigDAO.close();
            }
        }
    }
    
    /**
     * Handle POST request - process edit benefit form
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
            session.setAttribute("errorMessage", "Access denied. Only HR staff can edit benefits.");
            response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
            return;
        }
        
        SalaryConfigDAO salaryConfigDAO = null;
        try {
            // Create DAO instance for this request
            salaryConfigDAO = new SalaryConfigDAO();
            
            // Get form parameters
            String idStr = request.getParameter("benefitTypeID");
            String benefitName = request.getParameter("benefitName");
            String description = request.getParameter("description");
            String calculationType = request.getParameter("calculationType");
            String defaultAmountStr = request.getParameter("defaultAmount");
            String defaultPercentageStr = request.getParameter("defaultPercentage");
            String isTaxableStr = request.getParameter("isTaxable");
            
            // Validate required fields
            if (idStr == null || idStr.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Benefit ID is required.");
                response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
                return;
            }
            
            int benefitTypeID = Integer.parseInt(idStr);
            
            if (benefitName == null || benefitName.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Benefit name is required.");
                BenefitType benefit = salaryConfigDAO.getBenefitTypeById(benefitTypeID);
                request.setAttribute("benefit", benefit);
                request.getRequestDispatcher("/salary-mgt/edit-benefit.jsp").forward(request, response);
                return;
            }
            
            if (calculationType == null || calculationType.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Calculation type is required.");
                BenefitType benefit = salaryConfigDAO.getBenefitTypeById(benefitTypeID);
                request.setAttribute("benefit", benefit);
                request.getRequestDispatcher("/salary-mgt/edit-benefit.jsp").forward(request, response);
                return;
            }
            
            // Check if benefit name already exists (excluding current benefit)
            if (salaryConfigDAO.benefitNameExists(benefitName.trim(), benefitTypeID)) {
                request.setAttribute("errorMessage", "A benefit with this name already exists.");
                BenefitType benefit = salaryConfigDAO.getBenefitTypeById(benefitTypeID);
                request.setAttribute("benefit", benefit);
                request.getRequestDispatcher("/salary-mgt/edit-benefit.jsp").forward(request, response);
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
                        BenefitType benefit = salaryConfigDAO.getBenefitTypeById(benefitTypeID);
                        request.setAttribute("benefit", benefit);
                        request.getRequestDispatcher("/salary-mgt/edit-benefit.jsp").forward(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid default amount format.");
                    BenefitType benefit = salaryConfigDAO.getBenefitTypeById(benefitTypeID);
                    request.setAttribute("benefit", benefit);
                    request.getRequestDispatcher("/salary-mgt/edit-benefit.jsp").forward(request, response);
                    return;
                }
            }
            
            if (defaultPercentageStr != null && !defaultPercentageStr.trim().isEmpty()) {
                try {
                    defaultPercentage = Double.parseDouble(defaultPercentageStr.trim());
                    if (defaultPercentage < 0 || defaultPercentage > 100) {
                        request.setAttribute("errorMessage", "Default percentage must be between 0 and 100.");
                        BenefitType benefit = salaryConfigDAO.getBenefitTypeById(benefitTypeID);
                        request.setAttribute("benefit", benefit);
                        request.getRequestDispatcher("/salary-mgt/edit-benefit.jsp").forward(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid default percentage format.");
                    BenefitType benefit = salaryConfigDAO.getBenefitTypeById(benefitTypeID);
                    request.setAttribute("benefit", benefit);
                    request.getRequestDispatcher("/salary-mgt/edit-benefit.jsp").forward(request, response);
                    return;
                }
            }
            
            // Validate based on calculation type
            if ("Fixed".equals(calculationType) && defaultAmount == null) {
                request.setAttribute("errorMessage", "Default amount is required for Fixed calculation type.");
                BenefitType benefit = salaryConfigDAO.getBenefitTypeById(benefitTypeID);
                request.setAttribute("benefit", benefit);
                request.getRequestDispatcher("/salary-mgt/edit-benefit.jsp").forward(request, response);
                return;
            }
            
            if ("Percentage".equals(calculationType) && defaultPercentage == null) {
                request.setAttribute("errorMessage", "Default percentage is required for Percentage calculation type.");
                BenefitType benefit = salaryConfigDAO.getBenefitTypeById(benefitTypeID);
                request.setAttribute("benefit", benefit);
                request.getRequestDispatcher("/salary-mgt/edit-benefit.jsp").forward(request, response);
                return;
            }
            
            boolean isTaxable = "on".equals(isTaxableStr) || "true".equals(isTaxableStr);
            
            // Create BenefitType object with updated data
            BenefitType benefit = new BenefitType();
            benefit.setBenefitTypeID(benefitTypeID);
            benefit.setBenefitName(benefitName.trim());
            benefit.setDescription(description != null ? description.trim() : null);
            benefit.setCalculationType(calculationType);
            benefit.setDefaultAmount(defaultAmount);
            benefit.setDefaultPercentage(defaultPercentage);
            benefit.setTaxable(isTaxable);
            
            // Update in database
            boolean success = salaryConfigDAO.updateBenefitType(benefit);
            
            if (success) {
                session.setAttribute("successMessage", "Benefit type updated successfully.");
                response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
            } else {
                request.setAttribute("errorMessage", "Failed to update benefit type. Please try again.");
                request.setAttribute("benefit", benefit);
                request.getRequestDispatcher("/salary-mgt/edit-benefit.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid benefit ID format.");
            response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
        } catch (Exception e) {
            System.err.println("Error in EditBenefitServlet doPost: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while updating the benefit: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
        } finally {
            // Always close the DAO connection
            if (salaryConfigDAO != null) {
                salaryConfigDAO.close();
            }
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Edit Benefit Type Servlet";
    }
}

