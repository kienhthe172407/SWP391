package controller.attendanceMgt;

import dal.AttendanceDAO;
import model.AttendanceRecord;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Servlet for manually adjusting attendance records
 * This is NOT a regular CRUD edit - it's an exception handling workflow
 * that requires a reason and maintains an audit trail
 */
@WebServlet(name = "AdjustAttendanceServlet", urlPatterns = {"/attendance/adjust"})
public class AdjustAttendanceServlet extends HttpServlet {
    
    private AttendanceDAO attendanceDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        attendanceDAO = new AttendanceDAO();
    }
    
    /**
     * Handle GET request - display adjustment form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check if user is logged in
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        // Check permission
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (!util.PermissionChecker.hasPermission(currentUser, util.PermissionConstants.ATTENDANCE_ADJUST)) {
            request.setAttribute("errorMessage", "Bạn không có quyền điều chỉnh chấm công");
            request.getRequestDispatcher("/error/403.jsp").forward(request, response);
            return;
        }
        
        try {
            // Get attendance ID from request
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.trim().isEmpty()) {
                session.setAttribute("errorMessage", "Invalid attendance record ID.");
                response.sendRedirect(request.getContextPath() + "/attendance/summary");
                return;
            }
            
            int attendanceId = Integer.parseInt(idParam);
            
            // Load the attendance record
            AttendanceRecord record = attendanceDAO.getAttendanceById(attendanceId);
            
            if (record == null) {
                session.setAttribute("errorMessage", "Attendance record not found.");
                response.sendRedirect(request.getContextPath() + "/attendance/summary");
                return;
            }
            
            // Set record as request attribute for the form
            request.setAttribute("record", record);
            
            // Forward to adjustment form
            request.getRequestDispatcher("/attendance-mgt/adjust-attendance.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid attendance record ID format.");
            response.sendRedirect(request.getContextPath() + "/attendance/summary");
        } catch (Exception e) {
            System.err.println("Error in AdjustAttendanceServlet (GET): " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("errorMessage", "An error occurred while loading the attendance record.");
            response.sendRedirect(request.getContextPath() + "/attendance/summary");
        }
    }
    
    /**
     * Handle POST request - save manual adjustment
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        
        // Check if user is logged in
        if (session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        
        // Check permission
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        if (!util.PermissionChecker.hasPermission(currentUser, util.PermissionConstants.ATTENDANCE_ADJUST)) {
            request.setAttribute("errorMessage", "Bạn không có quyền điều chỉnh chấm công");
            request.getRequestDispatcher("/error/403.jsp").forward(request, response);
            return;
        }
        
        try {
            // Get form parameters
            String idParam = request.getParameter("attendanceId");
            String checkInTimeStr = request.getParameter("checkInTime");
            String checkOutTimeStr = request.getParameter("checkOutTime");
            String status = request.getParameter("status");
            String overtimeHoursStr = request.getParameter("overtimeHours");
            String adjustmentReason = request.getParameter("adjustmentReason");

            // Debug logging
            System.out.println("DEBUG - Received checkInTime: [" + checkInTimeStr + "]");
            System.out.println("DEBUG - Received checkOutTime: [" + checkOutTimeStr + "]");
            
            // Validate required fields
            if (idParam == null || idParam.trim().isEmpty()) {
                request.setAttribute("error", "Invalid attendance record ID.");
                doGet(request, response);
                return;
            }
            
            if (adjustmentReason == null || adjustmentReason.trim().isEmpty()) {
                request.setAttribute("error", "Adjustment reason is required for manual corrections.");
                doGet(request, response);
                return;
            }
            
            int attendanceId = Integer.parseInt(idParam);
            
            // Load existing record
            AttendanceRecord record = attendanceDAO.getAttendanceById(attendanceId);
            if (record == null) {
                session.setAttribute("errorMessage", "Attendance record not found.");
                response.sendRedirect(request.getContextPath() + "/attendance/summary");
                return;
            }
            
            // Update record fields
            // Parse check-in time
            if (checkInTimeStr != null && !checkInTimeStr.trim().isEmpty()) {
                try {
                    // Trim and ensure proper format (HH:mm)
                    String cleanTime = checkInTimeStr.trim();
                    // If already has seconds, use as is; otherwise append :00
                    if (cleanTime.split(":").length == 2) {
                        cleanTime = cleanTime + ":00";
                    }
                    record.setCheckInTime(Time.valueOf(cleanTime));
                } catch (IllegalArgumentException e) {
                    session.setAttribute("errorMessage", "Invalid check-in time format. Please use HH:MM format (e.g., 08:30).");
                    response.sendRedirect(request.getContextPath() + "/attendance/adjust?id=" + attendanceId);
                    return;
                }
            } else {
                record.setCheckInTime(null);
            }

            // Parse check-out time
            if (checkOutTimeStr != null && !checkOutTimeStr.trim().isEmpty()) {
                try {
                    // Trim and ensure proper format (HH:mm)
                    String cleanTime = checkOutTimeStr.trim();
                    // If already has seconds, use as is; otherwise append :00
                    if (cleanTime.split(":").length == 2) {
                        cleanTime = cleanTime + ":00";
                    }
                    record.setCheckOutTime(Time.valueOf(cleanTime));
                } catch (IllegalArgumentException e) {
                    session.setAttribute("errorMessage", "Invalid check-out time format. Please use HH:MM format (e.g., 17:00).");
                    response.sendRedirect(request.getContextPath() + "/attendance/adjust?id=" + attendanceId);
                    return;
                }
            } else {
                record.setCheckOutTime(null);
            }
            
            // Set status
            if (status != null && !status.trim().isEmpty()) {
                record.setStatus(status);
            }
            
            // Parse overtime hours
            if (overtimeHoursStr != null && !overtimeHoursStr.trim().isEmpty()) {
                record.setOvertimeHours(Double.parseDouble(overtimeHoursStr));
            } else {
                record.setOvertimeHours(0.0);
            }
            
            // Set manual adjustment fields
            record.setIsManualAdjustment(true);
            record.setAdjustmentReason(adjustmentReason.trim());
            record.setAdjustedBy(currentUser.getUserId());
            record.setAdjustedAt(new Timestamp(System.currentTimeMillis()));
            
            // Update in database
            boolean success = attendanceDAO.updateAttendanceRecord(record);
            
            if (success) {
                session.setAttribute("successMessage", 
                    "Attendance record adjusted successfully. Employee: " + record.getEmployeeCode() + 
                    ", Date: " + record.getAttendanceDate());
                response.sendRedirect(request.getContextPath() + "/attendance/summary");
            } else {
                request.setAttribute("error", "Failed to update attendance record. Please try again.");
                request.setAttribute("record", record);
                request.getRequestDispatcher("/attendance-mgt/adjust-attendance.jsp").forward(request, response);
            }
            
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid number format. Please check your input.");
            doGet(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", "Invalid time format. Please use HH:MM format (e.g., 08:30).");
            doGet(request, response);
        } catch (Exception e) {
            System.err.println("Error in AdjustAttendanceServlet (POST): " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while saving the adjustment: " + e.getMessage());
            doGet(request, response);
        }
    }
}

