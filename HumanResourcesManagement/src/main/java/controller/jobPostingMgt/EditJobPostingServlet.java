package controller.jobPostingMgt;

import dal.JobPostingDAO;
import model.JobPosting;
import model.Department;
import model.Position;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet to handle editing job postings
 */
public class EditJobPostingServlet extends HttpServlet {
    
    private JobPostingDAO jobPostingDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        jobPostingDAO = new JobPostingDAO();
    }

    /**
     * Handle GET request - display edit job posting form
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
            
            // Get departments and positions for dropdowns
            List<Department> departments = jobPostingDAO.getAllDepartments();
            List<Position> positions = jobPostingDAO.getAllPositions();
            
            // Set attributes for display in JSP
            request.setAttribute("jobPosting", jobPosting);
            request.setAttribute("departments", departments);
            request.setAttribute("positions", positions);
            
            // Forward to JSP page
            request.getRequestDispatcher("/job-posting-mgt/edit-job-posting.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            // Invalid job ID format
            session.setAttribute("errorMessage", "Invalid job posting ID format.");
            response.sendRedirect(request.getContextPath() + "/job-postings/list");
        } catch (Exception e) {
            // Handle any other errors
            System.err.println("Error in EditJobPostingServlet doGet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while retrieving job posting details.");
            response.sendRedirect(request.getContextPath() + "/job-postings/list");
        }
    }

    /**
     * Handle POST request - process job posting update
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        
        // Set default role and user ID if not set (for development)
        if (session.getAttribute("userId") == null) {
            session.setAttribute("userId", 1);
            session.setAttribute("userRole", "HR Manager");
        }

        // Get job ID from request parameter
        String jobIdParam = request.getParameter("jobId");
        
        if (jobIdParam == null || jobIdParam.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Job posting ID is required.");
            response.sendRedirect(request.getContextPath() + "/job-postings/list");
            return;
        }

        try {
            int jobId = Integer.parseInt(jobIdParam);
            
            // Get existing job posting
            JobPosting existingJobPosting = jobPostingDAO.getJobPostingById(jobId);
            
            if (existingJobPosting == null) {
                session.setAttribute("errorMessage", "Job posting with ID " + jobId + " not found.");
                response.sendRedirect(request.getContextPath() + "/job-postings/list");
                return;
            }

            // Get form parameters
            String jobTitle = request.getParameter("jobTitle");
            String departmentIdStr = request.getParameter("departmentId");
            String positionIdStr = request.getParameter("positionId");
            String jobDescription = request.getParameter("jobDescription");
            String requirements = request.getParameter("requirements");
            String benefits = request.getParameter("benefits");
            String salaryFromStr = request.getParameter("salaryRangeFrom");
            String salaryToStr = request.getParameter("salaryRangeTo");
            String numberOfPositionsStr = request.getParameter("numberOfPositions");
            String applicationDeadlineStr = request.getParameter("applicationDeadline");
            String jobStatus = request.getParameter("jobStatus");
            String internalNotes = request.getParameter("internalNotes");

            // Validate required fields
            if (jobTitle == null || jobTitle.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Job title is required.");
                response.sendRedirect(request.getContextPath() + "/job-postings/edit?id=" + jobId);
                return;
            }

            if (jobDescription == null || jobDescription.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Job description is required.");
                response.sendRedirect(request.getContextPath() + "/job-postings/edit?id=" + jobId);
                return;
            }

            // Update JobPosting object
            existingJobPosting.setJobTitle(jobTitle.trim());
            existingJobPosting.setJobDescription(jobDescription.trim());
            existingJobPosting.setRequirements(requirements != null ? requirements.trim() : null);
            existingJobPosting.setBenefits(benefits != null ? benefits.trim() : null);
            existingJobPosting.setInternalNotes(internalNotes != null ? internalNotes.trim() : null);
            existingJobPosting.setJobStatus(jobStatus != null && !jobStatus.trim().isEmpty() ? jobStatus.trim() : "Open");

            // Parse and set department ID
            if (departmentIdStr != null && !departmentIdStr.trim().isEmpty()) {
                try {
                    existingJobPosting.setDepartmentId(Integer.parseInt(departmentIdStr));
                } catch (NumberFormatException e) {
                    // Invalid department ID, leave as is
                }
            } else {
                existingJobPosting.setDepartmentId(null);
            }

            // Parse and set position ID
            if (positionIdStr != null && !positionIdStr.trim().isEmpty()) {
                try {
                    existingJobPosting.setPositionId(Integer.parseInt(positionIdStr));
                } catch (NumberFormatException e) {
                    // Invalid position ID, leave as is
                }
            } else {
                existingJobPosting.setPositionId(null);
            }

            // Parse and set salary range
            if (salaryFromStr != null && !salaryFromStr.trim().isEmpty()) {
                try {
                    existingJobPosting.setSalaryRangeFrom(new BigDecimal(salaryFromStr));
                } catch (NumberFormatException e) {
                    // Invalid salary format, leave as is
                }
            } else {
                existingJobPosting.setSalaryRangeFrom(null);
            }

            if (salaryToStr != null && !salaryToStr.trim().isEmpty()) {
                try {
                    existingJobPosting.setSalaryRangeTo(new BigDecimal(salaryToStr));
                } catch (NumberFormatException e) {
                    // Invalid salary format, leave as is
                }
            } else {
                existingJobPosting.setSalaryRangeTo(null);
            }

            // Parse and set number of positions
            if (numberOfPositionsStr != null && !numberOfPositionsStr.trim().isEmpty()) {
                try {
                    existingJobPosting.setNumberOfPositions(Integer.parseInt(numberOfPositionsStr));
                } catch (NumberFormatException e) {
                    // Keep existing value
                }
            }

            // Parse and set application deadline
            if (applicationDeadlineStr != null && !applicationDeadlineStr.trim().isEmpty()) {
                try {
                    existingJobPosting.setApplicationDeadline(Date.valueOf(applicationDeadlineStr));
                } catch (IllegalArgumentException e) {
                    // Invalid date format, leave as is
                }
            } else {
                existingJobPosting.setApplicationDeadline(null);
            }

            // Update the job posting
            boolean success = jobPostingDAO.updateJobPosting(existingJobPosting);

            if (success) {
                session.setAttribute("successMessage", "Job posting '" + existingJobPosting.getJobTitle() + "' has been successfully updated.");
                response.sendRedirect(request.getContextPath() + "/job-postings/detail?id=" + jobId);
            } else {
                session.setAttribute("errorMessage", "Failed to update job posting. Please try again.");
                response.sendRedirect(request.getContextPath() + "/job-postings/edit?id=" + jobId);
            }

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid job posting ID format.");
            response.sendRedirect(request.getContextPath() + "/job-postings/list");
        } catch (Exception e) {
            System.err.println("Error in EditJobPostingServlet doPost: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while updating the job posting.");
            response.sendRedirect(request.getContextPath() + "/job-postings/list");
        }
    }

    @Override
    public String getServletInfo() {
        return "Edit Job Posting Servlet";
    }
}
