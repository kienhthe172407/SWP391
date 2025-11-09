package controller.attendanceMgt;

import dal.AttendanceExceptionRequestDAO;
import dal.AttendanceDAO;
import dal.EmployeeDAO;
import model.AttendanceExceptionRequest;
import model.AttendanceRecord;
import model.Employee;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet for listing attendance exception requests
 * For employees: shows their own requests
 * For managers: shows requests from their department
 * @author admin
 */
@WebServlet(name = "ListAttendanceExceptionsServlet", urlPatterns = {"/attendance/exception/list"})
public class ListAttendanceExceptionsServlet extends HttpServlet {

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
     * Handle GET request - display list of attendance exception requests
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

        String userRole = user.getRole();
        List<AttendanceExceptionRequest> requests = null;
        int pendingCount = 0;
        int approvedCount = 0;
        int rejectedCount = 0;

        try {
            // Get employee information
            Employee employee = employeeDAO.getEmployeeByUserId(user.getUserId());
            if (employee == null) {
                session.setAttribute("errorMessage", "Employee record not found. Please contact HR.");
                response.sendRedirect(request.getContextPath() + "/dashboard/employee");
                return;
            }

            // Get filter parameters
            String statusFilter = request.getParameter("status");
            String employeeNameFilter = request.getParameter("employeeName");

            if ("Dept Manager".equals(userRole) || "HR Manager".equals(userRole)) {
                // Manager view: show requests from their department (Dept Manager) or all requests (HR Manager)
                if ("Dept Manager".equals(userRole)) {
                    if (employee.getDepartmentID() == null) {
                        session.setAttribute("errorMessage", 
                            "You are not assigned to a department. Please contact HR.");
                        response.sendRedirect(request.getContextPath() + "/dashboard/dept-manager");
                        return;
                    }

                    requests = exceptionRequestDAO.getAttendanceExceptionRequestsByDepartment(
                        employee.getDepartmentID(), statusFilter, employeeNameFilter);
                } else {
                    // HR Manager: get all requests from all departments
                    requests = exceptionRequestDAO.getAllAttendanceExceptionRequests(statusFilter, employeeNameFilter);
                }

                // Calculate statistics
                for (AttendanceExceptionRequest req : requests) {
                    if ("Pending".equals(req.getStatus())) {
                        pendingCount++;
                    } else if ("Approved".equals(req.getStatus())) {
                        approvedCount++;
                    } else if ("Rejected".equals(req.getStatus())) {
                        rejectedCount++;
                    }
                }

                request.setAttribute("isManagerView", true);
            } else {
                // Employee view: show only their own requests
                requests = exceptionRequestDAO.getAttendanceExceptionRequestsByEmployee(employee.getEmployeeID());

                // Apply status filter if provided
                if (statusFilter != null && !statusFilter.trim().isEmpty() && !"All".equals(statusFilter)) {
                    requests = requests.stream()
                        .filter(req -> statusFilter.equals(req.getStatus()))
                        .collect(java.util.stream.Collectors.toList());
                }

                // Calculate statistics
                for (AttendanceExceptionRequest req : requests) {
                    if ("Pending".equals(req.getStatus())) {
                        pendingCount++;
                    } else if ("Approved".equals(req.getStatus())) {
                        approvedCount++;
                    } else if ("Rejected".equals(req.getStatus())) {
                        rejectedCount++;
                    }
                }

                request.setAttribute("isManagerView", false);
            }

            request.setAttribute("requests", requests);
            request.setAttribute("totalRequests", requests.size());
            request.setAttribute("pendingCount", pendingCount);
            request.setAttribute("approvedCount", approvedCount);
            request.setAttribute("rejectedCount", rejectedCount);
            request.setAttribute("statusFilter", statusFilter);
            request.setAttribute("employeeNameFilter", employeeNameFilter);
            request.setAttribute("employee", employee);

            // Forward to the list page
            request.getRequestDispatcher("/attendance-mgt/list-attendance-exceptions.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error in ListAttendanceExceptionsServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", 
                "An error occurred while loading attendance exception requests: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/dashboard/employee");
        }
    }
}

