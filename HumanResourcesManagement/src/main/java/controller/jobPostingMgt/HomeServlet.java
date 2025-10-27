package controller.jobPostingMgt;

import dal.JobPostingDAO;
import model.JobPosting;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet for displaying the public home page with open job postings
 * Accessible to guests without authentication
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"/home", "/"})
public class HomeServlet extends HttpServlet {
    
    private static final int PAGE_SIZE = 9; // Show 9 jobs per page (3x3 grid)
    
    /**
     * Handle GET request - display home page with open job postings
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get page parameter
        String pageParam = request.getParameter("page");
        int currentPage = 1;
        
        if (pageParam != null && !pageParam.trim().isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageParam);
                if (currentPage < 1) {
                    currentPage = 1;
                }
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }
        
        try {
            // Create DAO instance
            JobPostingDAO jobPostingDAO = new JobPostingDAO();

            // Get open job postings
            List<JobPosting> openJobs = jobPostingDAO.getOpenJobPostings(currentPage, PAGE_SIZE);
            int totalRecords = jobPostingDAO.getTotalOpenJobPostings();
            int totalPages = (int) Math.ceil((double) totalRecords / PAGE_SIZE);

            // Set attributes for JSP
            request.setAttribute("openJobs", openJobs);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalRecords", totalRecords);

            // Forward to home page
            request.getRequestDispatcher("/home/home.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error in HomeServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while loading job postings.");
            request.getRequestDispatcher("/home/home.jsp").forward(request, response);
        }
    }
}

