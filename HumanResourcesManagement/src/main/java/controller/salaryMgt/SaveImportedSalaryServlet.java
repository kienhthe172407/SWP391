package controller.salaryMgt;

import model.SalaryComponent;
import model.SalaryPreviewData;
import model.SalaryPreviewData.SalaryPreviewRecord;
import model.User;
import dal.SalaryComponentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

/**
 * Servlet to save imported salary data to database
 * Handles deactivating old salary components and creating new ones
 * @author admin
 */
@WebServlet(name = "SaveImportedSalaryServlet", urlPatterns = {"/salary/save-imported"})
public class SaveImportedSalaryServlet extends HttpServlet {
    
    private final SalaryComponentDAO salaryComponentDAO = new SalaryComponentDAO();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check authentication
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        // Check authorization (HR and HR Manager only)
        User user = (User) session.getAttribute("user");
        String role = user.getRole();
        if (!"HR".equals(role) && !"HR Manager".equals(role) && !"HR_MANAGER".equals(role)) {
            session.setAttribute("errorMessage", "Access denied. Only HR staff can save salary data.");
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        try {
            // Get preview data from session
            SalaryPreviewData previewData = (SalaryPreviewData) session.getAttribute("salaryPreviewData");
            
            if (previewData == null) {
                session.setAttribute("errorMessage", "No salary data to save. Please upload a file first.");
                response.sendRedirect(request.getContextPath() + "/salary/import");
                return;
            }
            
            int successCount = 0;
            int failCount = 0;
            int updateCount = 0;
            StringBuilder errorMessages = new StringBuilder();
            
            // Process each valid record
            for (SalaryPreviewRecord previewRecord : previewData.getRecords()) {
                if (!previewRecord.isValid()) {
                    failCount++;
                    continue;
                }
                
                SalaryComponent component = previewRecord.getSalaryComponent();
                
                try {
                    // If employee has active salary component, deactivate it first
                    if (previewRecord.isWillUpdate()) {
                        // Calculate effective_to date (one day before new effective_from)
                        LocalDate newEffectiveFrom = component.getEffectiveFrom().toLocalDate();
                        LocalDate oldEffectiveTo = newEffectiveFrom.minusDays(1);
                        
                        boolean deactivated = salaryComponentDAO.deactivateActiveSalaryComponents(
                            component.getEmployeeID(),
                            Date.valueOf(oldEffectiveTo)
                        );
                        
                        if (!deactivated) {
                            errorMessages.append("Row ").append(previewRecord.getRowNumber())
                                       .append(": Failed to deactivate old salary component. ");
                            failCount++;
                            continue;
                        }
                        updateCount++;
                    }
                    
                    // Add new salary component
                    boolean added = salaryComponentDAO.addSalaryComponent(component);
                    
                    if (added) {
                        successCount++;
                    } else {
                        errorMessages.append("Row ").append(previewRecord.getRowNumber())
                                   .append(": Failed to save salary component. ");
                        failCount++;
                    }
                    
                } catch (Exception e) {
                    errorMessages.append("Row ").append(previewRecord.getRowNumber())
                               .append(": ").append(e.getMessage()).append(". ");
                    failCount++;
                }
            }
            
            // Clear preview data from session
            session.removeAttribute("salaryPreviewData");
            session.removeAttribute("uploadedFileName");
            
            // Set success/error messages
            if (successCount > 0) {
                String message = "Successfully imported " + successCount + " salary record(s).";
                if (updateCount > 0) {
                    message += " Updated " + updateCount + " existing record(s).";
                }
                session.setAttribute("successMessage", message);
            }
            
            if (failCount > 0) {
                String errorMsg = "Failed to import " + failCount + " record(s). " + errorMessages.toString();
                session.setAttribute("errorMessage", errorMsg);
            }
            
            // Redirect to import page
            response.sendRedirect(request.getContextPath() + "/salary/import");
            
        } catch (Exception e) {
            System.err.println("Error in SaveImportedSalaryServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "Error saving salary data: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/salary/import");
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Save Imported Salary Data Servlet";
    }
}

