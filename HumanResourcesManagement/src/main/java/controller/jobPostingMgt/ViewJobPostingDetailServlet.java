package controller.jobPostingMgt;

import dal.JobPostingDAO;
import model.JobPosting;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet to handle viewing job posting details
 */
public class ViewJobPostingDetailServlet extends HttpServlet {
    
    private JobPostingDAO jobPostingDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        jobPostingDAO = new JobPostingDAO();
    }

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

        // Get user session information
        HttpSession session = request.getSession();
        
        // Set default role as HR Manager for full access (for development)
        if (session.getAttribute("userRole") == null) {
            session.setAttribute("userRole", "HR Manager");
            session.setAttribute("userId", 1);
        }

        // Get job ID from request parameter
        String jobIdParam = request.getParameter("id");
        
        if (jobIdParam == null || jobIdParam.trim().isEmpty()) {
            // Redirect to job postings list if no ID provided
            session.setAttribute("errorMessage", "Job posting ID is required.");
            response.sendRedirect(request.getContextPath() + "/job-postings/list");
            return;
        }

        try {
            int jobId = Integer.parseInt(jobIdParam);
            
            // Get job posting details from database
            JobPosting jobPosting = jobPostingDAO.getJobPostingById(jobId);
            
            if (jobPosting == null) {
                // Job posting not found
                session.setAttribute("errorMessage", "Job posting with ID " + jobId + " not found.");
                response.sendRedirect(request.getContextPath() + "/job-postings/list");
                return;
            }
            
            // Set job posting details for display in JSP
            request.setAttribute("jobPosting", jobPosting);
            
            // Forward to JSP page
            request.getRequestDispatcher("/job-posting-mgt/job-posting-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            // Invalid job ID format
            session.setAttribute("errorMessage", "Invalid job posting ID format.");
            response.sendRedirect(request.getContextPath() + "/job-postings/list");
        } catch (Exception e) {
            // Handle any other errors
            System.err.println("Error in ViewJobPostingDetailServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while retrieving job posting details.");
            response.sendRedirect(request.getContextPath() + "/job-postings/list");
        }
    }

    /**
     * Handle POST request - redirect to GET
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST redirects to GET
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "View Job Posting Detail Servlet";
    }
}
