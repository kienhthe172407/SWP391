package controller.jobPostingMgt;

import dal.JobPostingDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Test servlet to debug job posting issues
 */
public class TestJobPostingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>Test Job Postings</title></head>");
        out.println("<body>");
        out.println("<h1>Job Postings Test</h1>");
        
        try {
            JobPostingDAO jobPostingDAO = new JobPostingDAO();
            
            // Test database connection
            out.println("<h2>Database Connection Test</h2>");
            out.println("<p>Connection status: " + (jobPostingDAO.getConnection() != null ? "SUCCESS" : "FAILED") + "</p>");
            
            // Test total count
            int totalCount = jobPostingDAO.getTotalJobPostings();
            out.println("<p>Total job postings in database: " + totalCount + "</p>");
            
            // Test getting job postings
            out.println("<h2>Job Postings List</h2>");
            var jobPostings = jobPostingDAO.getAllJobPostings(1, 10);
            out.println("<p>Retrieved " + jobPostings.size() + " job postings</p>");
            
            if (jobPostings.isEmpty()) {
                out.println("<p style='color: red;'>No job postings found!</p>");
                out.println("<p>Let's check if there are any records in the database...</p>");
                
                // Direct SQL query test
                out.println("<h3>Direct SQL Test</h3>");
                try {
                    var connection = jobPostingDAO.getConnection();
                    if (connection != null) {
                        var stmt = connection.createStatement();
                        var rs = stmt.executeQuery("SELECT COUNT(*) as count FROM job_postings");
                        if (rs.next()) {
                            out.println("<p>Direct SQL count: " + rs.getInt("count") + "</p>");
                        }
                        rs.close();
                        stmt.close();
                    }
                } catch (Exception sqlE) {
                    out.println("<p style='color: red;'>SQL Error: " + sqlE.getMessage() + "</p>");
                }
            } else {
                out.println("<ul>");
                for (var job : jobPostings) {
                    out.println("<li>ID: " + job.getJobId() + " - Title: " + job.getJobTitle() + " - Status: " + job.getJobStatus() + "</li>");
                }
                out.println("</ul>");
            }
            
        } catch (Exception e) {
            out.println("<p style='color: red;'>Error: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
        
        out.println("</body>");
        out.println("</html>");
    }
}
