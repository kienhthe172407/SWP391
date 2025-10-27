package controller.jobPostingMgt;

import dal.JobPostingDAO;
import model.JobPosting;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for displaying job posting details to guests (public view)
 * Accessible without authentication
 */
@WebServlet(name = "ViewPublicJobDetailServlet", urlPatterns = {"/jobs/detail"})
public class ViewPublicJobDetailServlet extends HttpServlet {
    
    /**
     * Handle GET request - display job posting details
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get job ID parameter
        String jobIdParam = request.getParameter("jobId");
        
        if (jobIdParam == null || jobIdParam.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/home");
            return;
        }
        
        try {
            int jobId = Integer.parseInt(jobIdParam);
            
            // Create DAO instance
            JobPostingDAO jobPostingDAO = new JobPostingDAO();
            
            // Get job posting details
            JobPosting jobPosting = jobPostingDAO.getJobPostingById(jobId);
            
            if (jobPosting == null) {
                request.setAttribute("errorMessage", "Job posting not found.");
                response.sendRedirect(request.getContextPath() + "/home");
                return;
            }
            
            // Check if job is still open and accepting applications
            boolean canApply = "Open".equals(jobPosting.getJobStatus());
            if (jobPosting.getApplicationDeadline() != null) {
                java.sql.Date deadline = jobPosting.getApplicationDeadline();
                java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
                canApply = canApply && !deadline.before(today);
            }
            
            // Set attributes for JSP
            request.setAttribute("jobPosting", jobPosting);
            request.setAttribute("canApply", canApply);
            
            // Forward to job detail page
            request.getRequestDispatcher("/job-posting-mgt/public-job-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            System.err.println("Invalid job ID format: " + jobIdParam);
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (Exception e) {
            System.err.println("Error in ViewPublicJobDetailServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading job details.");
            response.sendRedirect(request.getContextPath() + "/home");
        }
    }
}

