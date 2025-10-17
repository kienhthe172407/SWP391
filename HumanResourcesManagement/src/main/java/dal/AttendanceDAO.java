package dal;

import model.AttendanceRecord;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for AttendanceRecord
 * Handles all database operations for attendance records
 */
public class AttendanceDAO extends DBContext {
    
    /**
     * Insert a new attendance record
     * @param record AttendanceRecord to insert
     * @return true if successful, false otherwise
     */
    public boolean insertAttendanceRecord(AttendanceRecord record) {
        String sql = "INSERT INTO attendance_records " +
                     "(employee_id, attendance_date, check_in_time, check_out_time, status, " +
                     "overtime_hours, is_manual_adjustment, adjustment_reason, adjusted_by, " +
                     "adjusted_at, import_batch_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, record.getEmployeeID());
            ps.setDate(2, record.getAttendanceDate());
            ps.setTime(3, record.getCheckInTime());
            ps.setTime(4, record.getCheckOutTime());
            ps.setString(5, record.getStatus());
            ps.setDouble(6, record.getOvertimeHours() != null ? record.getOvertimeHours() : 0);
            ps.setBoolean(7, record.getIsManualAdjustment() != null ? record.getIsManualAdjustment() : false);
            ps.setString(8, record.getAdjustmentReason());
            ps.setObject(9, record.getAdjustedBy());
            ps.setObject(10, record.getAdjustedAt());
            ps.setString(11, record.getImportBatchID());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error inserting attendance record: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get attendance record by employee ID and date
     * @param employeeID Employee ID
     * @param attendanceDate Attendance date
     * @return AttendanceRecord if found, null otherwise
     */
    public AttendanceRecord getAttendanceRecord(int employeeID, Date attendanceDate) {
        String sql = "SELECT ar.attendance_id, ar.employee_id, ar.attendance_date, " +
                     "ar.check_in_time, ar.check_out_time, ar.status, ar.overtime_hours, " +
                     "ar.is_manual_adjustment, ar.adjustment_reason, ar.adjusted_by, " +
                     "ar.adjusted_at, ar.import_batch_id, ar.created_at, ar.updated_at, " +
                     "e.employee_code, CONCAT(e.first_name, ' ', e.last_name) AS employee_name " +
                     "FROM attendance_records ar " +
                     "LEFT JOIN employees e ON ar.employee_id = e.employee_id " +
                     "WHERE ar.employee_id = ? AND ar.attendance_date = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeID);
            ps.setDate(2, attendanceDate);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAttendanceRecord(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance record: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all attendance records for a specific date
     * @param attendanceDate Date to query
     * @return List of AttendanceRecord
     */
    public List<AttendanceRecord> getAttendanceByDate(Date attendanceDate) {
        List<AttendanceRecord> records = new ArrayList<>();
        String sql = "SELECT ar.attendance_id, ar.employee_id, ar.attendance_date, " +
                     "ar.check_in_time, ar.check_out_time, ar.status, ar.overtime_hours, " +
                     "ar.is_manual_adjustment, ar.adjustment_reason, ar.adjusted_by, " +
                     "ar.adjusted_at, ar.import_batch_id, ar.created_at, ar.updated_at, " +
                     "e.employee_code, CONCAT(e.first_name, ' ', e.last_name) AS employee_name " +
                     "FROM attendance_records ar " +
                     "LEFT JOIN employees e ON ar.employee_id = e.employee_id " +
                     "WHERE ar.attendance_date = ? " +
                     "ORDER BY e.employee_code";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, attendanceDate);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToAttendanceRecord(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance by date: " + e.getMessage());
            e.printStackTrace();
        }
        
        return records;
    }
    
    /**
     * Update an existing attendance record
     * @param record AttendanceRecord to update
     * @return true if successful, false otherwise
     */
    public boolean updateAttendanceRecord(AttendanceRecord record) {
        String sql = "UPDATE attendance_records SET " +
                     "check_in_time = ?, check_out_time = ?, status = ?, " +
                     "overtime_hours = ?, is_manual_adjustment = ?, adjustment_reason = ?, " +
                     "adjusted_by = ?, adjusted_at = ? " +
                     "WHERE attendance_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTime(1, record.getCheckInTime());
            ps.setTime(2, record.getCheckOutTime());
            ps.setString(3, record.getStatus());
            ps.setDouble(4, record.getOvertimeHours() != null ? record.getOvertimeHours() : 0);
            ps.setBoolean(5, record.getIsManualAdjustment() != null ? record.getIsManualAdjustment() : false);
            ps.setString(6, record.getAdjustmentReason());
            ps.setObject(7, record.getAdjustedBy());
            ps.setObject(8, record.getAdjustedAt());
            ps.setInt(9, record.getAttendanceID());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error updating attendance record: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Map ResultSet to AttendanceRecord object
     * @param rs ResultSet from database query
     * @return AttendanceRecord object
     * @throws SQLException
     */
    private AttendanceRecord mapResultSetToAttendanceRecord(ResultSet rs) throws SQLException {
        AttendanceRecord record = new AttendanceRecord();
        record.setAttendanceID(rs.getInt("attendance_id"));
        record.setEmployeeID(rs.getInt("employee_id"));
        record.setAttendanceDate(rs.getDate("attendance_date"));
        record.setCheckInTime(rs.getTime("check_in_time"));
        record.setCheckOutTime(rs.getTime("check_out_time"));
        record.setStatus(rs.getString("status"));
        record.setOvertimeHours(rs.getDouble("overtime_hours"));
        record.setIsManualAdjustment(rs.getBoolean("is_manual_adjustment"));
        record.setAdjustmentReason(rs.getString("adjustment_reason"));
        record.setAdjustedBy((Integer) rs.getObject("adjusted_by"));
        record.setAdjustedAt(rs.getTimestamp("adjusted_at"));
        record.setImportBatchID(rs.getString("import_batch_id"));
        record.setCreatedAt(rs.getTimestamp("created_at"));
        record.setUpdatedAt(rs.getTimestamp("updated_at"));
        record.setEmployeeCode(rs.getString("employee_code"));
        record.setEmployeeName(rs.getString("employee_name"));
        return record;
    }
}

