package controller.jobPostingMgt;

import dal.JobPostingDAO;
import model.JobPosting;
import model.Department;
import model.Position;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet to handle viewing job postings list
 */
public class ViewJobPostingsServlet extends HttpServlet {
    
    private JobPostingDAO jobPostingDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        jobPostingDAO = new JobPostingDAO();
    }

    /**
     * Handle GET request - display job postings list
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

        // Pagination parameters
        final int PAGE_SIZE = 10;
        int currentPage = 1;

        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) currentPage = 1;
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        // Get search parameters from request
        String keyword = request.getParameter("keyword");
        String status = request.getParameter("status");
        String department = request.getParameter("department");
        String position = request.getParameter("position");

        // Always set search params to preserve them in pagination links
        request.setAttribute("keyword", keyword != null ? keyword : "");
        request.setAttribute("status", status != null ? status : "");
        request.setAttribute("department", department != null ? department : "");
        request.setAttribute("position", position != null ? position : "");

        List<JobPosting> jobPostings;
        int totalRecords;
        
        // If search parameters exist, call searchJobPostings with pagination
        if ((keyword != null && !keyword.trim().isEmpty()) ||
            (status != null && !status.trim().isEmpty()) ||
            (department != null && !department.trim().isEmpty()) ||
            (position != null && !position.trim().isEmpty())) {
            jobPostings = jobPostingDAO.searchJobPostings(keyword, status, department, position, currentPage, PAGE_SIZE);
            totalRecords = jobPostingDAO.getTotalSearchResults(keyword, status, department, position);
        } else {
            // Otherwise, get all job postings with pagination
            jobPostings = jobPostingDAO.getAllJobPostings(currentPage, PAGE_SIZE);
            totalRecords = jobPostingDAO.getTotalJobPostings();
        }
        
        // Calculate pagination info
        int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);
        
        // Get all job posting statuses from database
        List<String> jobStatuses = jobPostingDAO.getAllJobStatuses();
        
        // Get departments and positions for filter dropdowns
        List<Department> departments = jobPostingDAO.getAllDepartments();
        List<Position> positions = jobPostingDAO.getAllPositions();
        
        // Set attributes for display in JSP
        request.setAttribute("jobPostings", jobPostings);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("totalRecords", totalRecords);
        request.setAttribute("pageSize", PAGE_SIZE);
        request.setAttribute("jobStatuses", jobStatuses);
        request.setAttribute("departments", departments);
        request.setAttribute("positions", positions);
        
        // Forward to JSP page
        request.getRequestDispatcher("/job-posting-mgt/list-job-postings.jsp").forward(request, response);
    }

    /**
     * Handle POST request - search job postings
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // POST handles same as GET
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "View Job Postings List Servlet";
    }
}
