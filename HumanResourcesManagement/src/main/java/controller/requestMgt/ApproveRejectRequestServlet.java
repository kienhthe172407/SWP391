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
 * Servlet for approving or rejecting employee requests
 * Only accessible by Department Managers and HR Managers
 * @author admin
 */
@WebServlet(name = "ApproveRejectRequestServlet", urlPatterns = {"/request/approve-reject"})
public class ApproveRejectRequestServlet extends HttpServlet {

    private RequestDAO requestDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        requestDAO = new RequestDAO();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle POST request - approve or reject a request
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

        // Check if user has permission (Dept Manager or HR Manager)
        String userRole = user.getRole();
        if (!"Dept Manager".equals(userRole) && !"HR Manager".equals(userRole)) {
            session.setAttribute("errorMessage", "Access denied. You don't have permission to approve/reject requests.");
            response.sendRedirect(request.getContextPath() + "/request/list");
            return;
        }

        try {
            // Get form parameters
            int requestID = Integer.parseInt(request.getParameter("requestId"));
            String action = request.getParameter("action"); // "approve" or "reject"
            String reviewComment = request.getParameter("reviewComment");

            // Validate action
            if (!"approve".equals(action) && !"reject".equals(action)) {
                session.setAttribute("errorMessage", "Invalid action specified.");
                response.sendRedirect(request.getContextPath() + "/request/manager/list");
                return;
            }

            // Get the request details to verify it belongs to the manager's department
            Request req = requestDAO.getRequestById(requestID);
            if (req == null) {
                session.setAttribute("errorMessage", "Request not found.");
                response.sendRedirect(request.getContextPath() + "/request/manager/list");
                return;
            }

            // Verify the request is still pending
            if (!"Pending".equals(req.getRequestStatus())) {
                session.setAttribute("errorMessage", "This request has already been " + req.getRequestStatus().toLowerCase() + ".");
                response.sendRedirect(request.getContextPath() + "/request/manager/list");
                return;
            }

            // For Department Managers, verify the request is from their department
            if ("Dept Manager".equals(userRole)) {
                Employee manager = employeeDAO.getEmployeeByUserId(user.getUserId());
                if (manager == null) {
                    session.setAttribute("errorMessage", "Employee record not found.");
                    response.sendRedirect(request.getContextPath() + "/request/manager/list");
                    return;
                }

                // Get the employee who made the request
                Employee requestEmployee = employeeDAO.getEmployeeById(req.getEmployeeID());
                if (requestEmployee == null || requestEmployee.getDepartmentID() != manager.getDepartmentID()) {
                    session.setAttribute("errorMessage", "You can only approve/reject requests from your department.");
                    response.sendRedirect(request.getContextPath() + "/request/manager/list");
                    return;
                }
            }

            // Determine the new status
            String newStatus = "approve".equals(action) ? "Approved" : "Rejected";

            // Update the request status
            boolean success = requestDAO.approveRejectRequest(
                    requestID,
                    newStatus,
                    user.getUserId(),
                    reviewComment
            );

            if (success) {
                session.setAttribute("successMessage",
                        "Request #" + requestID + " has been " + newStatus.toLowerCase() + " successfully.");
            } else {
                session.setAttribute("errorMessage",
                        "Failed to " + action + " the request. It may have already been processed.");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid request ID format.");
        } catch (Exception e) {
            System.err.println("Error in ApproveRejectRequestServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while processing the request: " + e.getMessage());
        }

        // Redirect back to the manager's request list
        response.sendRedirect(request.getContextPath() + "/request/manager/list");
    }

    /**
     * Handle GET request - redirect to list page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/request/manager/list");
    }
}

