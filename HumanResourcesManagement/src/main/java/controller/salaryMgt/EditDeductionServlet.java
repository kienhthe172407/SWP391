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
 * Servlet to handle editing existing deduction type
 * Only accessible by HR and HR Manager roles
 * @author admin
 */
@WebServlet("/salary/edit-deduction")
public class EditDeductionServlet extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
        super.init();
        // Don't create DAO in init() to avoid connection leaks
        // Create DAO per request instead
    }
    
    /**
     * Handle GET request - display edit deduction form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check permission
        String userRole = (String) session.getAttribute("userRole");
        if (!"HR".equals(userRole) && !"HR Manager".equals(userRole)) {
            session.setAttribute("errorMessage", "Access denied. Only HR staff can edit deductions.");
            response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
            return;
        }
        
        SalaryConfigDAO salaryConfigDAO = null;
        try {
            // Create DAO instance for this request
            salaryConfigDAO = new SalaryConfigDAO();
            
            String idStr = request.getParameter("id");
            if (idStr == null || idStr.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Deduction ID is required.");
                response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
                return;
            }
            
            int deductionTypeID = Integer.parseInt(idStr);
            DeductionType deduction = salaryConfigDAO.getDeductionTypeById(deductionTypeID);
            
            if (deduction == null) {
                session.setAttribute("errorMessage", "Deduction not found.");
                response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
                return;
            }
            
            request.setAttribute("deduction", deduction);
            request.getRequestDispatcher("/salary-mgt/edit-deduction.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid deduction ID format.");
            response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
        } catch (Exception e) {
            System.err.println("Error in EditDeductionServlet doGet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while loading the deduction.");
            response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
        } finally {
            // Always close the DAO connection
            if (salaryConfigDAO != null) {
                salaryConfigDAO.close();
            }
        }
    }
    
    /**
     * Handle POST request - process edit deduction form
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check permission
        String userRole = (String) session.getAttribute("userRole");
        if (!"HR".equals(userRole) && !"HR Manager".equals(userRole)) {
            session.setAttribute("errorMessage", "Access denied. Only HR staff can edit deductions.");
            response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
            return;
        }
        
        SalaryConfigDAO salaryConfigDAO = null;
        try {
            // Create DAO instance for this request
            salaryConfigDAO = new SalaryConfigDAO();
            
            // Get form parameters
            String idStr = request.getParameter("deductionTypeID");
            String deductionName = request.getParameter("deductionName");
            String description = request.getParameter("description");
            String calculationType = request.getParameter("calculationType");
            String defaultAmountStr = request.getParameter("defaultAmount");
            String defaultPercentageStr = request.getParameter("defaultPercentage");
            String isMandatoryStr = request.getParameter("isMandatory");
            
            // Validate required fields
            if (idStr == null || idStr.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Deduction ID is required.");
                response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
                return;
            }
            
            int deductionTypeID = Integer.parseInt(idStr);
            
            if (deductionName == null || deductionName.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Deduction name is required.");
                DeductionType deduction = salaryConfigDAO.getDeductionTypeById(deductionTypeID);
                request.setAttribute("deduction", deduction);
                request.getRequestDispatcher("/salary-mgt/edit-deduction.jsp").forward(request, response);
                return;
            }
            
            if (calculationType == null || calculationType.trim().isEmpty()) {
                request.setAttribute("errorMessage", "Calculation type is required.");
                DeductionType deduction = salaryConfigDAO.getDeductionTypeById(deductionTypeID);
                request.setAttribute("deduction", deduction);
                request.getRequestDispatcher("/salary-mgt/edit-deduction.jsp").forward(request, response);
                return;
            }
            
            // Check if deduction name already exists (excluding current deduction)
            if (salaryConfigDAO.deductionNameExists(deductionName.trim(), deductionTypeID)) {
                request.setAttribute("errorMessage", "A deduction with this name already exists.");
                DeductionType deduction = salaryConfigDAO.getDeductionTypeById(deductionTypeID);
                request.setAttribute("deduction", deduction);
                request.getRequestDispatcher("/salary-mgt/edit-deduction.jsp").forward(request, response);
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
                        DeductionType deduction = salaryConfigDAO.getDeductionTypeById(deductionTypeID);
                        request.setAttribute("deduction", deduction);
                        request.getRequestDispatcher("/salary-mgt/edit-deduction.jsp").forward(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid default amount format.");
                    DeductionType deduction = salaryConfigDAO.getDeductionTypeById(deductionTypeID);
                    request.setAttribute("deduction", deduction);
                    request.getRequestDispatcher("/salary-mgt/edit-deduction.jsp").forward(request, response);
                    return;
                }
            }
            
            if (defaultPercentageStr != null && !defaultPercentageStr.trim().isEmpty()) {
                try {
                    defaultPercentage = Double.parseDouble(defaultPercentageStr.trim());
                    if (defaultPercentage < 0 || defaultPercentage > 100) {
                        request.setAttribute("errorMessage", "Default percentage must be between 0 and 100.");
                        DeductionType deduction = salaryConfigDAO.getDeductionTypeById(deductionTypeID);
                        request.setAttribute("deduction", deduction);
                        request.getRequestDispatcher("/salary-mgt/edit-deduction.jsp").forward(request, response);
                        return;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("errorMessage", "Invalid default percentage format.");
                    DeductionType deduction = salaryConfigDAO.getDeductionTypeById(deductionTypeID);
                    request.setAttribute("deduction", deduction);
                    request.getRequestDispatcher("/salary-mgt/edit-deduction.jsp").forward(request, response);
                    return;
                }
            }
            
            // Validate based on calculation type
            if ("Fixed".equals(calculationType) && defaultAmount == null) {
                request.setAttribute("errorMessage", "Default amount is required for Fixed calculation type.");
                DeductionType deduction = salaryConfigDAO.getDeductionTypeById(deductionTypeID);
                request.setAttribute("deduction", deduction);
                request.getRequestDispatcher("/salary-mgt/edit-deduction.jsp").forward(request, response);
                return;
            }
            
            if ("Percentage".equals(calculationType) && defaultPercentage == null) {
                request.setAttribute("errorMessage", "Default percentage is required for Percentage calculation type.");
                DeductionType deduction = salaryConfigDAO.getDeductionTypeById(deductionTypeID);
                request.setAttribute("deduction", deduction);
                request.getRequestDispatcher("/salary-mgt/edit-deduction.jsp").forward(request, response);
                return;
            }
            
            boolean isMandatory = "on".equals(isMandatoryStr) || "true".equals(isMandatoryStr);
            
            // Create DeductionType object with updated data
            DeductionType deduction = new DeductionType();
            deduction.setDeductionTypeID(deductionTypeID);
            deduction.setDeductionName(deductionName.trim());
            deduction.setDescription(description != null ? description.trim() : null);
            deduction.setCalculationType(calculationType);
            deduction.setDefaultAmount(defaultAmount);
            deduction.setDefaultPercentage(defaultPercentage);
            deduction.setMandatory(isMandatory);
            
            // Update in database
            boolean success = salaryConfigDAO.updateDeductionType(deduction);
            
            if (success) {
                session.setAttribute("successMessage", "Deduction type updated successfully.");
                response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
            } else {
                request.setAttribute("errorMessage", "Failed to update deduction type. Please try again.");
                request.setAttribute("deduction", deduction);
                request.getRequestDispatcher("/salary-mgt/edit-deduction.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid deduction ID format.");
            response.sendRedirect(request.getContextPath() + "/salary/manage-benefits-deductions");
        } catch (Exception e) {
            System.err.println("Error in EditDeductionServlet doPost: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while updating the deduction: " + e.getMessage());
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
        return "Edit Deduction Type Servlet";
    }
}

