package controller.companyInfo;

import dal.CompanyInformationDAO;
import model.CompanyInformation;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for displaying company information page
 * Accessible to guests without authentication
 */
@WebServlet(name = "ViewCompanyInfoServlet", urlPatterns = {"/company-info"})
public class ViewCompanyInfoServlet extends HttpServlet {
    
    /**
     * Handle GET request - display company information page
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Create DAO instance
            CompanyInformationDAO companyInfoDAO = new CompanyInformationDAO();
            
            // Get active company information
            CompanyInformation companyInfo = companyInfoDAO.getActiveCompanyInformation();
            
            // Set attribute for JSP
            request.setAttribute("companyInfo", companyInfo);
            
            // Forward to company info page
            request.getRequestDispatcher("/home/company-info.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error in ViewCompanyInfoServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading company information.");
            request.getRequestDispatcher("/home/company-info.jsp").forward(request, response);
        }
    }
}

