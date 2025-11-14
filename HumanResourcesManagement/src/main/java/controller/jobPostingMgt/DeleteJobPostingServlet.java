package controller.jobPostingMgt;

import dal.JobPostingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet to handle job posting deletion
 */
public class DeleteJobPostingServlet extends HttpServlet {

    /**
     * Handles the HTTP POST method for deleting a job posting
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        model.User currentUser = (model.User) session.getAttribute("user");
        
        // Check authentication
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        // Check permission
        if (!util.PermissionChecker.hasPermission(currentUser, util.PermissionConstants.JOB_DELETE)) {
            request.setAttribute("errorMessage", "Bạn không có quyền xóa tin tuyển dụng");
            request.getRequestDispatcher("/error/403.jsp").forward(request, response);
            return;
        }
        
        // Get job posting ID from request
        String jobIdParam = request.getParameter("jobId");
        
        if (jobIdParam == null || jobIdParam.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Invalid job posting ID");
            response.sendRedirect(request.getContextPath() + "/job-postings/list");
            return;
        }
        
        try {
            int jobId = Integer.parseInt(jobIdParam);
            
            // Create DAO instance
            JobPostingDAO jobPostingDAO = new JobPostingDAO();
            
            // Delete the job posting
            boolean success = jobPostingDAO.deleteJobPosting(jobId);
            
            if (success) {
                session.setAttribute("successMessage", "Job posting #" + jobId + " has been successfully deleted");
            } else {
                session.setAttribute("errorMessage", "Failed to delete job posting #" + jobId);
            }
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid job posting ID format");
        } catch (Exception e) {
            session.setAttribute("errorMessage", "Error deleting job posting: " + e.getMessage());
        }
        
        // Redirect back to job postings list
        response.sendRedirect(request.getContextPath() + "/job-postings/list");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet for deleting job postings";
    }
}
