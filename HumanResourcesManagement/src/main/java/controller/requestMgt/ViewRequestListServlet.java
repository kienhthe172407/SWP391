package controller.requestMgt;

import dal.RequestDAO;
import dal.EmployeeDAO;
import model.Request;
import model.RequestType;
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

/**
 * Servlet for viewing employee's request list
 * Displays all requests submitted by the logged-in employee
 * @author admin
 */
@WebServlet(name = "ViewRequestListServlet", urlPatterns = {"/request/list"})
public class ViewRequestListServlet extends HttpServlet {

    private RequestDAO requestDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        requestDAO = new RequestDAO();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle GET request - display the request list
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

        // Get employee information for the logged-in user
        Employee employee = employeeDAO.getEmployeeByUserId(user.getUserId());
        if (employee == null) {
            request.setAttribute("errorMessage", "Employee record not found. Please contact HR.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        // Get filter parameters
        String statusFilter = request.getParameter("status");
        String requestTypeFilter = request.getParameter("requestType");
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
        List<Request> requests;
        if (statusFilter != null || requestTypeFilter != null || startDate != null || endDate != null) {
            // Use search method with filters
            requests = requestDAO.searchRequestsByEmployee(
                    employee.getEmployeeID(),
                    statusFilter,
                    requestTypeFilter,
                    startDate,
                    endDate
            );
        } else {
            // Get all requests for this employee
            requests = requestDAO.getRequestsByEmployeeId(employee.getEmployeeID());
        }

        // Get all request types for the filter dropdown
        List<RequestType> requestTypes = requestDAO.getAllRequestTypes();

        // Calculate statistics
        long totalRequests = requests.size();
        long pendingCount = requests.stream().filter(Request::isPending).count();
        long approvedCount = requests.stream().filter(Request::isApproved).count();
        long rejectedCount = requests.stream().filter(Request::isRejected).count();

        // Set attributes for JSP
        request.setAttribute("requests", requests);
        request.setAttribute("requestTypes", requestTypes);
        request.setAttribute("employee", employee);
        request.setAttribute("totalRequests", totalRequests);
        request.setAttribute("pendingCount", pendingCount);
        request.setAttribute("approvedCount", approvedCount);
        request.setAttribute("rejectedCount", rejectedCount);
        request.setAttribute("statusFilter", statusFilter);
        request.setAttribute("requestTypeFilter", requestTypeFilter);
        request.setAttribute("startDateFilter", startDateStr);
        request.setAttribute("endDateFilter", endDateStr);

        // Forward to the request list view
        request.getRequestDispatcher("/request-mgt/list-requests.jsp").forward(request, response);
    }
}

