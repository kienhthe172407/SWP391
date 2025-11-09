package controller.attendanceMgt;

import dal.AttendanceExceptionRequestDAO;
import dal.AttendanceDAO;
import dal.EmployeeDAO;
import model.AttendanceExceptionRequest;
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
 * Servlet for approving or rejecting attendance exception requests
 * Only accessible by Department Managers and HR Managers
 * @author admin
 */
@WebServlet(name = "ApproveRejectAttendanceExceptionServlet", urlPatterns = {"/attendance/exception/approve-reject"})
public class ApproveRejectAttendanceExceptionServlet extends HttpServlet {

    private AttendanceExceptionRequestDAO exceptionRequestDAO;
    private AttendanceDAO attendanceDAO;
    private EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        exceptionRequestDAO = new AttendanceExceptionRequestDAO();
        attendanceDAO = new AttendanceDAO();
        employeeDAO = new EmployeeDAO();
    }

    /**
     * Handle POST request - approve or reject an attendance exception request
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
            session.setAttribute("errorMessage", 
                "Access denied. You don't have permission to approve/reject attendance exception requests.");
            response.sendRedirect(request.getContextPath() + "/attendance/exception/list");
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
                response.sendRedirect(request.getContextPath() + "/attendance/exception/list");
                return;
            }

            // Get the request details
            AttendanceExceptionRequest exceptionRequest = exceptionRequestDAO.getAttendanceExceptionRequestById(requestID);
            if (exceptionRequest == null) {
                session.setAttribute("errorMessage", "Attendance exception request not found.");
                response.sendRedirect(request.getContextPath() + "/attendance/exception/list");
                return;
            }

            // Verify the request is still pending
            if (!"Pending".equals(exceptionRequest.getStatus())) {
                session.setAttribute("errorMessage", 
                    "This request has already been " + exceptionRequest.getStatus().toLowerCase() + ".");
                response.sendRedirect(request.getContextPath() + "/attendance/exception/list");
                return;
            }

            // For Department Managers, verify the request is from their department
            if ("Dept Manager".equals(userRole)) {
                Employee manager = employeeDAO.getEmployeeByUserId(user.getUserId());
                if (manager == null) {
                    session.setAttribute("errorMessage", "Employee record not found.");
                    response.sendRedirect(request.getContextPath() + "/attendance/exception/list");
                    return;
                }

                // Get the employee who made the request
                Employee requestEmployee = employeeDAO.getEmployeeById(exceptionRequest.getEmployeeID());
                if (requestEmployee == null || 
                    requestEmployee.getDepartmentID() == null ||
                    !requestEmployee.getDepartmentID().equals(manager.getDepartmentID())) {
                    session.setAttribute("errorMessage", 
                        "You can only approve/reject requests from your department.");
                    response.sendRedirect(request.getContextPath() + "/attendance/exception/list");
                    return;
                }
            }
            // HR Manager can approve/reject requests from any department (no restriction)

            // Determine the new status
            String newStatus = "approve".equals(action) ? "Approved" : "Rejected";

            // Update the request status
            boolean success = exceptionRequestDAO.approveRejectAttendanceExceptionRequest(
                requestID, newStatus, user.getUserId(), reviewComment);

            if (success) {
                // If approved, apply the changes to the attendance record
                if ("Approved".equals(newStatus)) {
                    boolean applied = exceptionRequestDAO.applyApprovedExceptionRequest(requestID);
                    if (!applied) {
                        System.err.println("Warning: Failed to apply approved exception request to attendance record.");
                    }
                }

                session.setAttribute("successMessage",
                    "Attendance exception request #" + requestID + " has been " + 
                    newStatus.toLowerCase() + " successfully.");
            } else {
                session.setAttribute("errorMessage",
                    "Failed to " + action + " the request. It may have already been processed.");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid request ID format.");
        } catch (Exception e) {
            System.err.println("Error in ApproveRejectAttendanceExceptionServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", 
                "An error occurred while processing the request: " + e.getMessage());
        }

        // Redirect back to the list page
        response.sendRedirect(request.getContextPath() + "/attendance/exception/list");
    }

    /**
     * Handle GET request - redirect to list page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/attendance/exception/list");
    }
}

