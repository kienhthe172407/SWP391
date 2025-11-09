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
import java.sql.Time;
import java.util.List;

/**
 * Servlet for handling employee attendance exception request submission
 * Allows employees to submit requests to explain/correct their attendance
 * @author admin
 */
@WebServlet(name = "SubmitAttendanceExceptionServlet", urlPatterns = {"/attendance/exception/submit"})
public class SubmitAttendanceExceptionServlet extends HttpServlet {

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
     * Handle GET request - display the submit exception request form
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
            session.setAttribute("errorMessage", "Employee record not found. Please contact HR.");
            response.sendRedirect(request.getContextPath() + "/dashboard/employee");
            return;
        }

        // Get attendance ID from request parameter
        String attendanceIdParam = request.getParameter("attendanceId");
        if (attendanceIdParam == null || attendanceIdParam.trim().isEmpty()) {
            session.setAttribute("errorMessage", 
                "Please select an attendance record from the Attendance Record page to submit an exception request.");
            response.sendRedirect(request.getContextPath() + "/attendance/summary");
            return;
        }
        
        try {
            int attendanceId = Integer.parseInt(attendanceIdParam);
            AttendanceRecord attendanceRecord = attendanceDAO.getAttendanceById(attendanceId);
            
            // Verify the attendance record belongs to the employee
            if (attendanceRecord != null && attendanceRecord.getEmployeeID() == employee.getEmployeeID()) {
                // Check if there's already a pending request for this attendance
                if (exceptionRequestDAO.hasPendingRequestForAttendance(attendanceId)) {
                    session.setAttribute("errorMessage", 
                        "You already have a pending exception request for this attendance record.");
                    response.sendRedirect(request.getContextPath() + "/attendance/exception/list");
                    return;
                }
                
                request.setAttribute("attendanceRecord", attendanceRecord);
            } else {
                session.setAttribute("errorMessage", 
                    "Attendance record not found or you don't have permission to access it.");
                response.sendRedirect(request.getContextPath() + "/attendance/summary");
                return;
            }
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid attendance record ID.");
            response.sendRedirect(request.getContextPath() + "/attendance/summary");
            return;
        }

        request.setAttribute("employee", employee);

        // Forward to the submit exception request form
        request.getRequestDispatcher("/attendance-mgt/submit-attendance-exception.jsp").forward(request, response);
    }

    /**
     * Handle POST request - process the submitted exception request
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
            response.sendRedirect(request.getContextPath() + "/dashboard/employee");
            return;
        }

        try {
            // Get form parameters
            String attendanceIdParam = request.getParameter("attendanceId");
            String requestReason = request.getParameter("requestReason");
            String proposedCheckInStr = request.getParameter("proposedCheckIn");
            String proposedCheckOutStr = request.getParameter("proposedCheckOut");
            String proposedStatus = request.getParameter("proposedStatus");

            // Validate required fields
            if (attendanceIdParam == null || attendanceIdParam.trim().isEmpty()) {
                session.setAttribute("errorMessage", 
                    "Attendance record ID is required. Please select an attendance record from the Attendance Record page.");
                response.sendRedirect(request.getContextPath() + "/attendance/summary");
                return;
            }

            if (requestReason == null || requestReason.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Request reason is required.");
                response.sendRedirect(request.getContextPath() + "/attendance/exception/submit?attendanceId=" + attendanceIdParam);
                return;
            }

            int attendanceId = Integer.parseInt(attendanceIdParam);

            // Verify the attendance record belongs to the employee
            AttendanceRecord attendanceRecord = attendanceDAO.getAttendanceById(attendanceId);
            if (attendanceRecord == null || attendanceRecord.getEmployeeID() != employee.getEmployeeID()) {
                session.setAttribute("errorMessage", 
                    "Attendance record not found or you don't have permission to access it.");
                response.sendRedirect(request.getContextPath() + "/attendance/exception/list");
                return;
            }

            // Check if there's already a pending request for this attendance
            if (exceptionRequestDAO.hasPendingRequestForAttendance(attendanceId)) {
                session.setAttribute("errorMessage", 
                    "You already have a pending exception request for this attendance record.");
                response.sendRedirect(request.getContextPath() + "/attendance/exception/list");
                return;
            }

            // Parse proposed check-in time
            Time proposedCheckIn = null;
            if (proposedCheckInStr != null && !proposedCheckInStr.trim().isEmpty()) {
                try {
                    String cleanTime = proposedCheckInStr.trim();
                    if (cleanTime.split(":").length == 2) {
                        cleanTime = cleanTime + ":00";
                    }
                    proposedCheckIn = Time.valueOf(cleanTime);
                } catch (IllegalArgumentException e) {
                    session.setAttribute("errorMessage", 
                        "Invalid check-in time format. Please use HH:MM format (e.g., 08:30).");
                    response.sendRedirect(request.getContextPath() + "/attendance/exception/submit?attendanceId=" + attendanceId);
                    return;
                }
            }

            // Parse proposed check-out time
            Time proposedCheckOut = null;
            if (proposedCheckOutStr != null && !proposedCheckOutStr.trim().isEmpty()) {
                try {
                    String cleanTime = proposedCheckOutStr.trim();
                    if (cleanTime.split(":").length == 2) {
                        cleanTime = cleanTime + ":00";
                    }
                    proposedCheckOut = Time.valueOf(cleanTime);
                } catch (IllegalArgumentException e) {
                    session.setAttribute("errorMessage", 
                        "Invalid check-out time format. Please use HH:MM format (e.g., 17:00).");
                    response.sendRedirect(request.getContextPath() + "/attendance/exception/submit?attendanceId=" + attendanceId);
                    return;
                }
            }

            // Normalize proposed status: if empty or null, set to null (keep current status)
            String normalizedProposedStatus = null;
            if (proposedStatus != null && !proposedStatus.trim().isEmpty()) {
                normalizedProposedStatus = proposedStatus.trim();
            }

            // Create the exception request
            AttendanceExceptionRequest exceptionRequest = new AttendanceExceptionRequest();
            exceptionRequest.setAttendanceID(attendanceId);
            exceptionRequest.setEmployeeID(employee.getEmployeeID());
            exceptionRequest.setRequestReason(requestReason.trim());
            exceptionRequest.setProposedCheckIn(proposedCheckIn);
            exceptionRequest.setProposedCheckOut(proposedCheckOut);
            exceptionRequest.setProposedStatus(normalizedProposedStatus);
            exceptionRequest.setStatus("Pending");

            // Insert the exception request
            boolean success = exceptionRequestDAO.insertAttendanceExceptionRequest(exceptionRequest);

            if (success) {
                session.setAttribute("successMessage", 
                    "Attendance exception request submitted successfully. Your manager will review it.");
                response.sendRedirect(request.getContextPath() + "/attendance/exception/list");
            } else {
                session.setAttribute("errorMessage", 
                    "Failed to submit attendance exception request. Please try again.");
                response.sendRedirect(request.getContextPath() + "/attendance/exception/submit?attendanceId=" + attendanceId);
            }

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid attendance record ID format.");
            response.sendRedirect(request.getContextPath() + "/attendance/exception/submit");
        } catch (Exception e) {
            System.err.println("Error in SubmitAttendanceExceptionServlet: " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", 
                "An error occurred while submitting the request: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/attendance/exception/submit");
        }
    }
}

