package controller.requestMgt;

import dal.RequestDAO;
import dal.EmployeeDAO;
import model.Request;
import model.Employee;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servlet for Department Managers to view and manage team requests
 * Displays all requests from employees in the manager's department
 * @author admin
 */
@WebServlet(name = "DeptManagerRequestListServlet", urlPatterns = {"/request/manager/list"})
public class DeptManagerRequestListServlet extends HttpServlet {

    private RequestDAO requestDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        requestDAO = new RequestDAO();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle GET request - display the request list for department manager
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

        // Check if user is a Department Manager
        if (!"Dept Manager".equals(user.getRole())) {
            request.setAttribute("errorMessage", "Access denied. This page is only for Department Managers.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        // Get employee information for the logged-in manager
        Employee manager = employeeDAO.getEmployeeByUserId(user.getUserId());
        if (manager == null) {
            request.setAttribute("errorMessage", "Employee record not found. Please contact HR.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        // Get filter parameters
        String statusFilter = request.getParameter("status");
        String requestTypeFilter = request.getParameter("requestType");
        String employeeNameFilter = request.getParameter("employeeName");
        String startDateStr = request.getParameter("startDate");
        String endDateStr = request.getParameter("endDate");

        // Parse dates
        Date startDate = null;
        Date endDate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (startDateStr != null && !startDateStr.isEmpty()) {
                startDate = new Date(dateFormat.parse(startDateStr).getTime());
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                endDate = new Date(dateFormat.parse(endDateStr).getTime());
            }
        } catch (ParseException e) {
            System.err.println("Error parsing dates: " + e.getMessage());
        }

        // Get requests with filters
        List<Request> filteredRequests;
        if (statusFilter != null || requestTypeFilter != null || employeeNameFilter != null ||
            startDate != null || endDate != null) {
            // Use search method with filters
            filteredRequests = requestDAO.searchRequestsByDepartment(
                    manager.getDepartmentID(),
                    statusFilter,
                    requestTypeFilter,
                    employeeNameFilter,
                    startDate,
                    endDate
            );
        } else {
            // Get all requests for the manager's department
            filteredRequests = requestDAO.getRequestsByDepartment(manager.getDepartmentID());
        }

        // Get all requests for statistics (without filters)
        List<Request> allRequests = requestDAO.getRequestsByDepartment(manager.getDepartmentID());

        // Calculate statistics
        long totalRequests = allRequests.size();
        long pendingCount = allRequests.stream().filter(Request::isPending).count();
        long approvedCount = allRequests.stream().filter(Request::isApproved).count();
        long rejectedCount = allRequests.stream().filter(Request::isRejected).count();

        // Set attributes for JSP
        request.setAttribute("requests", filteredRequests);
        request.setAttribute("allRequests", allRequests);
        request.setAttribute("manager", manager);
        request.setAttribute("totalRequests", totalRequests);
        request.setAttribute("pendingCount", pendingCount);
        request.setAttribute("approvedCount", approvedCount);
        request.setAttribute("rejectedCount", rejectedCount);
        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("requestTypeFilter", requestTypeFilter);
        request.setAttribute("employeeNameFilter", employeeNameFilter);
        request.setAttribute("startDateFilter", startDateStr);
        request.setAttribute("endDateFilter", endDateStr);

        // Forward to the department manager request list view
        request.getRequestDispatcher("/request-mgt/dept-manager-request-list.jsp").forward(request, response);
    }
}

