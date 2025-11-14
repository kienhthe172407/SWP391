package controller.reportingAnalytics;

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
import com.google.gson.Gson;

/**
 * Servlet for viewing HR Statistics and Reports
 * Only accessible by HR Manager role
 * @author admin
 */
@WebServlet(name = "ViewHRStatisticsServlet", urlPatterns = {"/hr/statistics"})
public class ViewHRStatisticsServlet extends HttpServlet {

    private HRStatisticsDAO statisticsDAO;
    private Gson gson;

    @Override
    public void init() throws ServletException {
        super.init();
        statisticsDAO = new HRStatisticsDAO();
        gson = new Gson();
    }

    /**
     * Handle GET request - display statistics dashboard
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login.jsp");
            return;
        }

        // Check permission
        if (!util.PermissionChecker.hasPermission(user, util.PermissionConstants.REPORT_VIEW_STATISTICS)) {
            request.setAttribute("errorMessage", "Bạn không có quyền xem thống kê HR");
            request.getRequestDispatcher("/error/403.jsp").forward(request, response);
            return;
        }
        
        String userRole = user.getRole();

        try {
            // Get comprehensive statistics
            HRStatistics statistics = statisticsDAO.getComprehensiveStatistics();

            // Set statistics as request attribute
            request.setAttribute("statistics", statistics);

            // Convert maps to JSON for charts
            request.setAttribute("employeesByDepartmentJson", gson.toJson(statistics.getEmployeesByDepartment()));
            request.setAttribute("employeesByPositionJson", gson.toJson(statistics.getEmployeesByPosition()));
            request.setAttribute("attendanceByStatusJson", gson.toJson(statistics.getAttendanceByStatus()));
            request.setAttribute("applicationsByStatusJson", gson.toJson(statistics.getApplicationsByStatus()));
            request.setAttribute("applicationsByMonthJson", gson.toJson(statistics.getApplicationsByMonth()));
            request.setAttribute("tasksByStatusJson", gson.toJson(statistics.getTasksByStatus()));
            request.setAttribute("tasksByPriorityJson", gson.toJson(statistics.getTasksByPriority()));
            request.setAttribute("requestsByTypeJson", gson.toJson(statistics.getRequestsByType()));
            request.setAttribute("requestsByStatusJson", gson.toJson(statistics.getRequestsByStatus()));
            request.setAttribute("contractsByTypeJson", gson.toJson(statistics.getContractsByType()));
            request.setAttribute("averageSalaryByDepartmentJson", gson.toJson(statistics.getAverageSalaryByDepartment()));
            request.setAttribute("hiringTrendByMonthJson", gson.toJson(statistics.getHiringTrendByMonth()));
            request.setAttribute("attendanceTrendByMonthJson", gson.toJson(statistics.getAttendanceTrendByMonth()));

            // Forward to JSP page in reporting-analytics folder
            request.getRequestDispatcher("/reporting-analytics/hr-statistics.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error in ViewHRStatisticsServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while loading statistics: " + e.getMessage());
            // Redirect to HR Manager dashboard instead of home
            response.sendRedirect(request.getContextPath() + "/dashboard/hr-manager");
        }
    }

    /**
     * Handle POST request - same as GET
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "View HR Statistics Servlet";
    }
}

