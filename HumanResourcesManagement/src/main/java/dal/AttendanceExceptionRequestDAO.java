package dal;

import model.AttendanceExceptionRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for AttendanceExceptionRequest
 * Handles all database operations for attendance exception requests
 */
public class AttendanceExceptionRequestDAO extends DBContext {
    
    /**
     * Insert a new attendance exception request
     * @param request AttendanceExceptionRequest to insert
     * @return true if successful, false otherwise
     */
    public boolean insertAttendanceExceptionRequest(AttendanceExceptionRequest request) {
        // Build SQL dynamically based on whether proposed_status is provided
        String proposedStatus = request.getProposedStatus();
        boolean hasProposedStatus = proposedStatus != null && !proposedStatus.trim().isEmpty();
        
        String sql;
        if (hasProposedStatus) {
            sql = "INSERT INTO attendance_exception_requests " +
                  "(attendance_id, employee_id, request_reason, proposed_check_in, " +
                  "proposed_check_out, proposed_status, status) " +
                  "VALUES (?, ?, ?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO attendance_exception_requests " +
                  "(attendance_id, employee_id, request_reason, proposed_check_in, " +
                  "proposed_check_out, status) " +
                  "VALUES (?, ?, ?, ?, ?, ?)";
        }
        
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, request.getAttendanceID());
            ps.setInt(2, request.getEmployeeID());
            ps.setString(3, request.getRequestReason());
            ps.setTime(4, request.getProposedCheckIn());
            ps.setTime(5, request.getProposedCheckOut());
            
            if (hasProposedStatus) {
                ps.setString(6, proposedStatus.trim());
                ps.setString(7, request.getStatus() != null ? request.getStatus() : "Pending");
            } else {
                ps.setString(6, request.getStatus() != null ? request.getStatus() : "Pending");
            }
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get generated request ID
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        request.setRequestID(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error inserting attendance exception request: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get attendance exception request by request ID
     * @param requestID Request ID
     * @return AttendanceExceptionRequest if found, null otherwise
     */
    public AttendanceExceptionRequest getAttendanceExceptionRequestById(int requestID) {
        String sql = "SELECT aer.request_id, aer.attendance_id, aer.employee_id, " +
                     "aer.request_reason, aer.proposed_check_in, aer.proposed_check_out, " +
                     "aer.proposed_status, aer.status, aer.reviewed_by, aer.review_comment, " +
                     "aer.reviewed_at, aer.created_at, aer.updated_at, " +
                     "e.employee_code, CONCAT(e.first_name, ' ', e.last_name) AS employee_name, " +
                     "d.department_name, ar.attendance_date, ar.check_in_time AS current_check_in, " +
                     "ar.check_out_time AS current_check_out, ar.status AS current_status, " +
                     "CONCAT(u.first_name, ' ', u.last_name) AS reviewer_name " +
                     "FROM attendance_exception_requests aer " +
                     "INNER JOIN attendance_records ar ON aer.attendance_id = ar.attendance_id " +
                     "INNER JOIN employees e ON aer.employee_id = e.employee_id " +
                     "LEFT JOIN departments d ON e.department_id = d.department_id " +
                     "LEFT JOIN users u ON aer.reviewed_by = u.user_id " +
                     "WHERE aer.request_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, requestID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAttendanceExceptionRequest(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance exception request by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all attendance exception requests for a specific employee
     * @param employeeID Employee ID
     * @return List of AttendanceExceptionRequest
     */
    public List<AttendanceExceptionRequest> getAttendanceExceptionRequestsByEmployee(int employeeID) {
        List<AttendanceExceptionRequest> requests = new ArrayList<>();
        String sql = "SELECT aer.request_id, aer.attendance_id, aer.employee_id, " +
                     "aer.request_reason, aer.proposed_check_in, aer.proposed_check_out, " +
                     "aer.proposed_status, aer.status, aer.reviewed_by, aer.review_comment, " +
                     "aer.reviewed_at, aer.created_at, aer.updated_at, " +
                     "e.employee_code, CONCAT(e.first_name, ' ', e.last_name) AS employee_name, " +
                     "d.department_name, ar.attendance_date, ar.check_in_time AS current_check_in, " +
                     "ar.check_out_time AS current_check_out, ar.status AS current_status, " +
                     "CONCAT(u.first_name, ' ', u.last_name) AS reviewer_name " +
                     "FROM attendance_exception_requests aer " +
                     "INNER JOIN attendance_records ar ON aer.attendance_id = ar.attendance_id " +
                     "INNER JOIN employees e ON aer.employee_id = e.employee_id " +
                     "LEFT JOIN departments d ON e.department_id = d.department_id " +
                     "LEFT JOIN users u ON aer.reviewed_by = u.user_id " +
                     "WHERE aer.employee_id = ? " +
                     "ORDER BY aer.created_at DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeID);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSetToAttendanceExceptionRequest(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance exception requests by employee: " + e.getMessage());
            e.printStackTrace();
        }
        
        return requests;
    }
    
    /**
     * Get all pending attendance exception requests for a department manager
     * @param departmentID Department ID
     * @return List of AttendanceExceptionRequest
     */
    public List<AttendanceExceptionRequest> getPendingAttendanceExceptionRequestsByDepartment(int departmentID) {
        List<AttendanceExceptionRequest> requests = new ArrayList<>();
        String sql = "SELECT aer.request_id, aer.attendance_id, aer.employee_id, " +
                     "aer.request_reason, aer.proposed_check_in, aer.proposed_check_out, " +
                     "aer.proposed_status, aer.status, aer.reviewed_by, aer.review_comment, " +
                     "aer.reviewed_at, aer.created_at, aer.updated_at, " +
                     "e.employee_code, CONCAT(e.first_name, ' ', e.last_name) AS employee_name, " +
                     "d.department_name, ar.attendance_date, ar.check_in_time AS current_check_in, " +
                     "ar.check_out_time AS current_check_out, ar.status AS current_status, " +
                     "CONCAT(u.first_name, ' ', u.last_name) AS reviewer_name " +
                     "FROM attendance_exception_requests aer " +
                     "INNER JOIN attendance_records ar ON aer.attendance_id = ar.attendance_id " +
                     "INNER JOIN employees e ON aer.employee_id = e.employee_id " +
                     "LEFT JOIN departments d ON e.department_id = d.department_id " +
                     "LEFT JOIN users u ON aer.reviewed_by = u.user_id " +
                     "WHERE e.department_id = ? AND aer.status = 'Pending' " +
                     "ORDER BY aer.created_at DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, departmentID);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSetToAttendanceExceptionRequest(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting pending attendance exception requests by department: " + e.getMessage());
            e.printStackTrace();
        }
        
        return requests;
    }
    
    /**
     * Get all attendance exception requests (for HR Manager - all departments)
     * @param status Filter by status (null or empty for all)
     * @param employeeName Search by employee name (null or empty for all)
     * @return List of AttendanceExceptionRequest
     */
    public List<AttendanceExceptionRequest> getAllAttendanceExceptionRequests(String status, String employeeName) {
        List<AttendanceExceptionRequest> requests = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT aer.request_id, aer.attendance_id, aer.employee_id, " +
            "aer.request_reason, aer.proposed_check_in, aer.proposed_check_out, " +
            "aer.proposed_status, aer.status, aer.reviewed_by, aer.review_comment, " +
            "aer.reviewed_at, aer.created_at, aer.updated_at, " +
            "e.employee_code, CONCAT(e.first_name, ' ', e.last_name) AS employee_name, " +
            "d.department_name, ar.attendance_date, ar.check_in_time AS current_check_in, " +
            "ar.check_out_time AS current_check_out, ar.status AS current_status, " +
            "CONCAT(u.first_name, ' ', u.last_name) AS reviewer_name " +
            "FROM attendance_exception_requests aer " +
            "INNER JOIN attendance_records ar ON aer.attendance_id = ar.attendance_id " +
            "INNER JOIN employees e ON aer.employee_id = e.employee_id " +
            "LEFT JOIN departments d ON e.department_id = d.department_id " +
            "LEFT JOIN users u ON aer.reviewed_by = u.user_id " +
            "WHERE 1=1 "
        );
        
        if (status != null && !status.trim().isEmpty() && !"All".equals(status)) {
            sql.append("AND aer.status = ? ");
        }
        
        if (employeeName != null && !employeeName.trim().isEmpty()) {
            sql.append("AND (e.first_name LIKE ? OR e.last_name LIKE ? OR CONCAT(e.first_name, ' ', e.last_name) LIKE ?) ");
        }
        
        sql.append("ORDER BY aer.created_at DESC");
        
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            
            if (status != null && !status.trim().isEmpty() && !"All".equals(status)) {
                ps.setString(paramIndex++, status);
            }
            
            if (employeeName != null && !employeeName.trim().isEmpty()) {
                String searchPattern = "%" + employeeName.trim() + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSetToAttendanceExceptionRequest(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting all attendance exception requests: " + e.getMessage());
            e.printStackTrace();
        }
        
        return requests;
    }
    
    /**
     * Get all attendance exception requests for a department manager (with filters)
     * @param departmentID Department ID
     * @param status Filter by status (null or empty for all)
     * @param employeeName Search by employee name (null or empty for all)
     * @return List of AttendanceExceptionRequest
     */
    public List<AttendanceExceptionRequest> getAttendanceExceptionRequestsByDepartment(
            int departmentID, String status, String employeeName) {
        List<AttendanceExceptionRequest> requests = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT aer.request_id, aer.attendance_id, aer.employee_id, " +
            "aer.request_reason, aer.proposed_check_in, aer.proposed_check_out, " +
            "aer.proposed_status, aer.status, aer.reviewed_by, aer.review_comment, " +
            "aer.reviewed_at, aer.created_at, aer.updated_at, " +
            "e.employee_code, CONCAT(e.first_name, ' ', e.last_name) AS employee_name, " +
            "d.department_name, ar.attendance_date, ar.check_in_time AS current_check_in, " +
            "ar.check_out_time AS current_check_out, ar.status AS current_status, " +
            "CONCAT(u.first_name, ' ', u.last_name) AS reviewer_name " +
            "FROM attendance_exception_requests aer " +
            "INNER JOIN attendance_records ar ON aer.attendance_id = ar.attendance_id " +
            "INNER JOIN employees e ON aer.employee_id = e.employee_id " +
            "LEFT JOIN departments d ON e.department_id = d.department_id " +
            "LEFT JOIN users u ON aer.reviewed_by = u.user_id " +
            "WHERE e.department_id = ? "
        );
        
        if (status != null && !status.trim().isEmpty() && !"All".equals(status)) {
            sql.append("AND aer.status = ? ");
        }
        
        if (employeeName != null && !employeeName.trim().isEmpty()) {
            sql.append("AND (e.first_name LIKE ? OR e.last_name LIKE ? OR CONCAT(e.first_name, ' ', e.last_name) LIKE ?) ");
        }
        
        sql.append("ORDER BY aer.created_at DESC");
        
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, departmentID);
            
            if (status != null && !status.trim().isEmpty() && !"All".equals(status)) {
                ps.setString(paramIndex++, status);
            }
            
            if (employeeName != null && !employeeName.trim().isEmpty()) {
                String searchPattern = "%" + employeeName.trim() + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSetToAttendanceExceptionRequest(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance exception requests by department: " + e.getMessage());
            e.printStackTrace();
        }
        
        return requests;
    }
    
    /**
     * Approve or reject an attendance exception request
     * @param requestID Request ID
     * @param status New status ("Approved" or "Rejected")
     * @param reviewerUserID User ID of the reviewer
     * @param reviewComment Optional comment from reviewer
     * @return true if successful, false otherwise
     */
    public boolean approveRejectAttendanceExceptionRequest(int requestID, String status,
                                                           int reviewerUserID, String reviewComment) {
        String sql = "UPDATE attendance_exception_requests SET " +
                     "status = ?, reviewed_by = ?, review_comment = ?, reviewed_at = NOW() " +
                     "WHERE request_id = ? AND status = 'Pending'";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, reviewerUserID);
            ps.setString(3, reviewComment);
            ps.setInt(4, requestID);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error in approveRejectAttendanceExceptionRequest: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update attendance record when exception request is approved
     * @param requestID Request ID
     * @return true if successful, false otherwise
     */
    public boolean applyApprovedExceptionRequest(int requestID) {
        String sql = "UPDATE attendance_records ar " +
                     "INNER JOIN attendance_exception_requests aer ON ar.attendance_id = aer.attendance_id " +
                     "SET ar.check_in_time = COALESCE(aer.proposed_check_in, ar.check_in_time), " +
                     "ar.check_out_time = COALESCE(aer.proposed_check_out, ar.check_out_time), " +
                     "ar.status = COALESCE(aer.proposed_status, ar.status), " +
                     "ar.is_manual_adjustment = TRUE, " +
                     "ar.adjustment_reason = CONCAT('Approved exception request #', aer.request_id, ': ', aer.request_reason), " +
                     "ar.adjusted_by = aer.reviewed_by, " +
                     "ar.adjusted_at = aer.reviewed_at " +
                     "WHERE aer.request_id = ? AND aer.status = 'Approved'";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, requestID);
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error in applyApprovedExceptionRequest: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if there's already a pending request for the same attendance record
     * @param attendanceID Attendance ID
     * @return true if pending request exists, false otherwise
     */
    public boolean hasPendingRequestForAttendance(int attendanceID) {
        String sql = "SELECT COUNT(*) as count FROM attendance_exception_requests " +
                     "WHERE attendance_id = ? AND status = 'Pending'";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, attendanceID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("count") > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking pending request: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Map ResultSet to AttendanceExceptionRequest object
     * @param rs ResultSet from database query
     * @return AttendanceExceptionRequest object
     * @throws SQLException
     */
    private AttendanceExceptionRequest mapResultSetToAttendanceExceptionRequest(ResultSet rs) throws SQLException {
        AttendanceExceptionRequest request = new AttendanceExceptionRequest();
        request.setRequestID(rs.getInt("request_id"));
        request.setAttendanceID(rs.getInt("attendance_id"));
        request.setEmployeeID(rs.getInt("employee_id"));
        request.setRequestReason(rs.getString("request_reason"));
        request.setProposedCheckIn(rs.getTime("proposed_check_in"));
        request.setProposedCheckOut(rs.getTime("proposed_check_out"));
        request.setProposedStatus(rs.getString("proposed_status"));
        request.setStatus(rs.getString("status"));
        request.setReviewedBy((Integer) rs.getObject("reviewed_by"));
        request.setReviewComment(rs.getString("review_comment"));
        request.setReviewedAt(rs.getTimestamp("reviewed_at"));
        request.setCreatedAt(rs.getTimestamp("created_at"));
        request.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Additional fields from JOINs
        request.setEmployeeCode(rs.getString("employee_code"));
        request.setEmployeeName(rs.getString("employee_name"));
        request.setDepartmentName(rs.getString("department_name"));
        request.setReviewerName(rs.getString("reviewer_name"));
        request.setAttendanceDate(rs.getDate("attendance_date"));
        request.setCurrentCheckIn(rs.getTime("current_check_in"));
        request.setCurrentCheckOut(rs.getTime("current_check_out"));
        request.setCurrentStatus(rs.getString("current_status"));
        
        return request;
    }
}

