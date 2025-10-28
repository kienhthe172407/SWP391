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

/**
 * Servlet for cancelling employee requests (only pending requests can be cancelled)
 */
@WebServlet("/request/cancel")
public class CancelRequestServlet extends HttpServlet {

    private RequestDAO requestDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        requestDAO = new RequestDAO();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle POST request - cancel the request
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
            session.setAttribute("errorMessage", "Employee record not found. Please contact HR.");
            response.sendRedirect(request.getContextPath() + "/request/list");
            return;
        }

        // Get request ID from parameter
        String requestIdStr = request.getParameter("requestId");
        if (requestIdStr == null || requestIdStr.isEmpty()) {
            session.setAttribute("errorMessage", "Request ID is required.");
            response.sendRedirect(request.getContextPath() + "/request/list");
            return;
        }

        int requestId;
        try {
            requestId = Integer.parseInt(requestIdStr);
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid request ID.");
            response.sendRedirect(request.getContextPath() + "/request/list");
            return;
        }

        // Get the request from database
        Request req = requestDAO.getRequestById(requestId);
        if (req == null) {
            session.setAttribute("errorMessage", "Request not found.");
            response.sendRedirect(request.getContextPath() + "/request/list");
            return;
        }

        // Verify that the request belongs to the logged-in employee
        if (req.getEmployeeID() != employee.getEmployeeID()) {
            session.setAttribute("errorMessage", "You don't have permission to cancel this request.");
            response.sendRedirect(request.getContextPath() + "/request/list");
            return;
        }

        // Verify that the request is still pending
        if (!req.isPending()) {
            session.setAttribute("errorMessage", "Only pending requests can be cancelled.");
            response.sendRedirect(request.getContextPath() + "/request/list");
            return;
        }

        // Cancel the request
        boolean success = requestDAO.cancelRequest(requestId);

        if (success) {
            session.setAttribute("successMessage", "Request cancelled successfully!");
        } else {
            session.setAttribute("errorMessage", "Failed to cancel request. The request may no longer be pending.");
        }

        response.sendRedirect(request.getContextPath() + "/request/list");
    }

    /**
     * Handle GET request - redirect to list page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/request/list");
    }
}

