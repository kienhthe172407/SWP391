package controller.dashboard;

import dal.HRStatisticsDAO;
import model.HRStatistics;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet for HR Manager Dashboard
 * Displays overview statistics and quick links
 * @author admin
 */
@WebServlet(name = "HRManagerDashboardServlet", urlPatterns = {"/dashboard/hr-manager"})
public class HRManagerDashboardServlet extends HttpServlet {

    private HRStatisticsDAO statisticsDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        statisticsDAO = new HRStatisticsDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Get user from session
        User user = (User) session.getAttribute("user");
        
        // Verify user is HR Manager
        if (!"HR Manager".equals(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        try {
            // Get overview statistics for dashboard
            HRStatistics statistics = statisticsDAO.getComprehensiveStatistics();
            
            // Set statistics as request attribute
            request.setAttribute("statistics", statistics);
            
            // Forward to dashboard JSP
            request.getRequestDispatcher("/dashboard/hr-manager-dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            System.err.println("Error loading HR Manager dashboard: " + e.getMessage());
            e.printStackTrace();
            // Forward to dashboard even if statistics fail
            request.getRequestDispatcher("/dashboard/hr-manager-dashboard.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
