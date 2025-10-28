package controller.salaryMgt;

import util.SalaryTemplateGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Servlet to download salary import template
 * @author admin
 */
@WebServlet(name = "DownloadSalaryTemplateServlet", urlPatterns = {"/salary/download-template"})
public class DownloadSalaryTemplateServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        try {
            // Generate template
            byte[] templateBytes = SalaryTemplateGenerator.generateTemplate();
            
            // Set response headers
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=salary-import-template.xlsx");
            response.setContentLength(templateBytes.length);
            
            // Write to output stream
            try (OutputStream out = response.getOutputStream()) {
                out.write(templateBytes);
                out.flush();
            }
            
        } catch (Exception e) {
            System.err.println("Error generating salary template: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                             "Error generating template: " + e.getMessage());
        }
    }
    
    @Override
    public String getServletInfo() {
        return "Download Salary Import Template Servlet";
    }
}

