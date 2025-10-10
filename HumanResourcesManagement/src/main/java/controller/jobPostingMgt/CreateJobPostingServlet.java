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
 * Servlet to handle creating new job postings
 */
public class CreateJobPostingServlet extends HttpServlet {
    
    private JobPostingDAO jobPostingDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        jobPostingDAO = new JobPostingDAO();
    }

    /**
     * Handle GET request - display create job posting form
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

        try {
            // Get departments and positions for dropdowns
            List<Department> departments = jobPostingDAO.getAllDepartments();
            List<Position> positions = jobPostingDAO.getAllPositions();
            
            // Set attributes for display in JSP
            request.setAttribute("departments", departments);
            request.setAttribute("positions", positions);
            
            // Forward to JSP page
            request.getRequestDispatcher("/job-posting-mgt/create-job-posting.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error in CreateJobPostingServlet doGet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while loading the create form.");
            response.sendRedirect(request.getContextPath() + "/job-postings/list");
        }
    }

    /**
     * Handle POST request - process job posting creation
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

        try {
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
                response.sendRedirect(request.getContextPath() + "/job-postings/create");
                return;
            }

            if (jobDescription == null || jobDescription.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Job description is required.");
                response.sendRedirect(request.getContextPath() + "/job-postings/create");
                return;
            }

            // Create JobPosting object
            JobPosting jobPosting = new JobPosting();
            jobPosting.setJobTitle(jobTitle.trim());
            jobPosting.setJobDescription(jobDescription.trim());
            jobPosting.setRequirements(requirements != null ? requirements.trim() : null);
            jobPosting.setBenefits(benefits != null ? benefits.trim() : null);
            jobPosting.setInternalNotes(internalNotes != null ? internalNotes.trim() : null);
            jobPosting.setJobStatus(jobStatus != null && !jobStatus.trim().isEmpty() ? jobStatus.trim() : "Open");

            // Parse and set department ID
            if (departmentIdStr != null && !departmentIdStr.trim().isEmpty()) {
                try {
                    jobPosting.setDepartmentId(Integer.parseInt(departmentIdStr));
                } catch (NumberFormatException e) {
                    // Invalid department ID, leave as null
                }
            }

            // Parse and set position ID
            if (positionIdStr != null && !positionIdStr.trim().isEmpty()) {
                try {
                    jobPosting.setPositionId(Integer.parseInt(positionIdStr));
                } catch (NumberFormatException e) {
                    // Invalid position ID, leave as null
                }
            }

            // Parse and set salary range
            if (salaryFromStr != null && !salaryFromStr.trim().isEmpty()) {
                try {
                    jobPosting.setSalaryRangeFrom(new BigDecimal(salaryFromStr));
                } catch (NumberFormatException e) {
                    // Invalid salary format, leave as null
                }
            }

            if (salaryToStr != null && !salaryToStr.trim().isEmpty()) {
                try {
                    jobPosting.setSalaryRangeTo(new BigDecimal(salaryToStr));
                } catch (NumberFormatException e) {
                    // Invalid salary format, leave as null
                }
            }

            // Parse and set number of positions
            if (numberOfPositionsStr != null && !numberOfPositionsStr.trim().isEmpty()) {
                try {
                    jobPosting.setNumberOfPositions(Integer.parseInt(numberOfPositionsStr));
                } catch (NumberFormatException e) {
                    jobPosting.setNumberOfPositions(1); // Default to 1
                }
            } else {
                jobPosting.setNumberOfPositions(1); // Default to 1
            }

            // Parse and set application deadline
            if (applicationDeadlineStr != null && !applicationDeadlineStr.trim().isEmpty()) {
                try {
                    jobPosting.setApplicationDeadline(Date.valueOf(applicationDeadlineStr));
                } catch (IllegalArgumentException e) {
                    // Invalid date format, leave as null
                }
            }

            // Set posted by and posted date
            Integer userId = (Integer) session.getAttribute("userId");
            jobPosting.setPostedBy(userId);
            jobPosting.setPostedDate(new Date(System.currentTimeMillis()));

            // Create the job posting
            boolean success = jobPostingDAO.createJobPosting(jobPosting);

            if (success) {
                session.setAttribute("successMessage", "Job posting '" + jobPosting.getJobTitle() + "' has been successfully created.");
                response.sendRedirect(request.getContextPath() + "/job-postings/list");
            } else {
                session.setAttribute("errorMessage", "Failed to create job posting. Please try again.");
                response.sendRedirect(request.getContextPath() + "/job-postings/create");
            }

        } catch (Exception e) {
            System.err.println("Error in CreateJobPostingServlet doPost: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while creating the job posting.");
            response.sendRedirect(request.getContextPath() + "/job-postings/create");
        }
    }

    @Override
    public String getServletInfo() {
        return "Create Job Posting Servlet";
    }
}
