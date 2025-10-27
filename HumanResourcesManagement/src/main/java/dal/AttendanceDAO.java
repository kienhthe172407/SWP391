package dal;

import model.AttendanceRecord;
import model.AttendanceSummary;
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
     * Get attendance record by attendance ID
     * @param attendanceID Attendance ID
     * @return AttendanceRecord if found, null otherwise
     */
    public AttendanceRecord getAttendanceById(int attendanceID) {
        String sql = "SELECT ar.attendance_id, ar.employee_id, ar.attendance_date, " +
                     "ar.check_in_time, ar.check_out_time, ar.status, ar.overtime_hours, " +
                     "ar.is_manual_adjustment, ar.adjustment_reason, ar.adjusted_by, " +
                     "ar.adjusted_at, ar.import_batch_id, ar.created_at, ar.updated_at, " +
                     "e.employee_code, CONCAT(e.first_name, ' ', e.last_name) AS employee_name " +
                     "FROM attendance_records ar " +
                     "LEFT JOIN employees e ON ar.employee_id = e.employee_id " +
                     "WHERE ar.attendance_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, attendanceID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAttendanceRecord(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance record by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
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
     * Get attendance records for a specific employee within a date range
     * Used for salary calculation
     * @param employeeID Employee ID
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return List of AttendanceRecord for the employee in the date range
     */
    public List<AttendanceRecord> getAttendanceByEmployeeAndDateRange(int employeeID, Date startDate, Date endDate) {
        List<AttendanceRecord> records = new ArrayList<>();
        String sql = "SELECT ar.attendance_id, ar.employee_id, ar.attendance_date, " +
                     "ar.check_in_time, ar.check_out_time, ar.status, ar.overtime_hours, " +
                     "ar.is_manual_adjustment, ar.adjustment_reason, ar.adjusted_by, " +
                     "ar.adjusted_at, ar.import_batch_id, ar.created_at, ar.updated_at, " +
                     "e.employee_code, CONCAT(e.first_name, ' ', e.last_name) AS employee_name " +
                     "FROM attendance_records ar " +
                     "JOIN employees e ON ar.employee_id = e.employee_id " +
                     "WHERE ar.employee_id = ? AND ar.attendance_date BETWEEN ? AND ? " +
                     "ORDER BY ar.attendance_date";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeID);
            ps.setDate(2, startDate);
            ps.setDate(3, endDate);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                AttendanceRecord record = mapResultSetToAttendanceRecord(rs);
                records.add(record);
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance by employee and date range: " + e.getMessage());
            e.printStackTrace();
        }

        return records;
    }

    /**
     * Get attendance records with filters and pagination
     * @param employeeCode Employee code filter (optional)
     * @param departmentId Department ID filter (optional)
     * @param startDate Start date filter (optional)
     * @param endDate End date filter (optional)
     * @param page Current page number
     * @param pageSize Number of records per page
     * @return List of AttendanceRecord
     */
    public List<AttendanceRecord> getAttendanceWithFilters(String employeeCode, Integer departmentId,
                                                           String startDate, String endDate,
                                                           int page, int pageSize) {
        List<AttendanceRecord> records = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT ar.attendance_id, ar.employee_id, ar.attendance_date, " +
            "ar.check_in_time, ar.check_out_time, ar.status, ar.overtime_hours, " +
            "ar.is_manual_adjustment, ar.adjustment_reason, ar.adjusted_by, " +
            "ar.adjusted_at, ar.import_batch_id, ar.created_at, ar.updated_at, " +
            "e.employee_code, CONCAT(e.first_name, ' ', e.last_name) AS employee_name, " +
            "d.department_name " +
            "FROM attendance_records ar " +
            "LEFT JOIN employees e ON ar.employee_id = e.employee_id " +
            "LEFT JOIN departments d ON e.department_id = d.department_id " +
            "WHERE 1=1 "
        );

        // Add filters
        if (employeeCode != null && !employeeCode.trim().isEmpty()) {
            sql.append("AND e.employee_code = ? ");
        }
        if (departmentId != null && departmentId > 0) {
            sql.append("AND e.department_id = ? ");
        }
        if (startDate != null && !startDate.trim().isEmpty()) {
            sql.append("AND ar.attendance_date >= ? ");
        }
        if (endDate != null && !endDate.trim().isEmpty()) {
            sql.append("AND ar.attendance_date <= ? ");
        }

        sql.append("ORDER BY ar.attendance_date DESC, e.employee_code ");
        sql.append("LIMIT ? OFFSET ?");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (employeeCode != null && !employeeCode.trim().isEmpty()) {
                ps.setString(paramIndex++, employeeCode);
            }
            if (departmentId != null && departmentId > 0) {
                ps.setInt(paramIndex++, departmentId);
            }
            if (startDate != null && !startDate.trim().isEmpty()) {
                ps.setString(paramIndex++, startDate);
            }
            if (endDate != null && !endDate.trim().isEmpty()) {
                ps.setString(paramIndex++, endDate);
            }

            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex++, (page - 1) * pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToAttendanceRecord(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance with filters: " + e.getMessage());
            e.printStackTrace();
        }

        return records;
    }

    /**
     * Get total count of attendance records with filters
     * @param employeeCode Employee code filter (optional)
     * @param departmentId Department ID filter (optional)
     * @param startDate Start date filter (optional)
     * @param endDate End date filter (optional)
     * @return int total count
     */
    public int getTotalAttendanceWithFilters(String employeeCode, Integer departmentId,
                                             String startDate, String endDate) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) as total " +
            "FROM attendance_records ar " +
            "LEFT JOIN employees e ON ar.employee_id = e.employee_id " +
            "WHERE 1=1 "
        );

        // Add filters
        if (employeeCode != null && !employeeCode.trim().isEmpty()) {
            sql.append("AND e.employee_code = ? ");
        }
        if (departmentId != null && departmentId > 0) {
            sql.append("AND e.department_id = ? ");
        }
        if (startDate != null && !startDate.trim().isEmpty()) {
            sql.append("AND ar.attendance_date >= ? ");
        }
        if (endDate != null && !endDate.trim().isEmpty()) {
            sql.append("AND ar.attendance_date <= ? ");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (employeeCode != null && !employeeCode.trim().isEmpty()) {
                ps.setString(paramIndex++, employeeCode);
            }
            if (departmentId != null && departmentId > 0) {
                ps.setInt(paramIndex++, departmentId);
            }
            if (startDate != null && !startDate.trim().isEmpty()) {
                ps.setString(paramIndex++, startDate);
            }
            if (endDate != null && !endDate.trim().isEmpty()) {
                ps.setString(paramIndex++, endDate);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting total attendance with filters: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Get attendance summary statistics with filters
     * @param employeeCode Employee code filter (optional)
     * @param departmentId Department ID filter (optional)
     * @param startDate Start date filter (optional)
     * @param endDate End date filter (optional)
     * @return AttendanceSummary object with statistics
     */
    public AttendanceSummary getAttendanceSummary(String employeeCode, Integer departmentId,
                                                  String startDate, String endDate) {
        AttendanceSummary summary = new AttendanceSummary();

        StringBuilder sql = new StringBuilder(
            "SELECT " +
            "COUNT(*) as total_days, " +
            "SUM(CASE WHEN ar.status = 'Present' THEN 1 ELSE 0 END) as present_days, " +
            "SUM(CASE WHEN ar.status = 'Absent' THEN 1 ELSE 0 END) as absent_days, " +
            "SUM(CASE WHEN ar.status = 'Late' THEN 1 ELSE 0 END) as late_days, " +
            "SUM(CASE WHEN ar.status = 'Early Leave' THEN 1 ELSE 0 END) as early_leave_days, " +
            "SUM(CASE WHEN ar.status = 'Business Trip' THEN 1 ELSE 0 END) as business_trip_days, " +
            "SUM(CASE WHEN ar.status = 'Remote' THEN 1 ELSE 0 END) as remote_days, " +
            "COALESCE(SUM(ar.overtime_hours), 0) as total_overtime, " +
            "SUM(CASE WHEN ar.is_manual_adjustment = TRUE THEN 1 ELSE 0 END) as manual_adjustments " +
            "FROM attendance_records ar " +
            "LEFT JOIN employees e ON ar.employee_id = e.employee_id " +
            "WHERE 1=1 "
        );

        // Add filters
        if (employeeCode != null && !employeeCode.trim().isEmpty()) {
            sql.append("AND e.employee_code = ? ");
        }
        if (departmentId != null && departmentId > 0) {
            sql.append("AND e.department_id = ? ");
        }
        if (startDate != null && !startDate.trim().isEmpty()) {
            sql.append("AND ar.attendance_date >= ? ");
        }
        if (endDate != null && !endDate.trim().isEmpty()) {
            sql.append("AND ar.attendance_date <= ? ");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (employeeCode != null && !employeeCode.trim().isEmpty()) {
                ps.setString(paramIndex++, employeeCode);
            }
            if (departmentId != null && departmentId > 0) {
                ps.setInt(paramIndex++, departmentId);
            }
            if (startDate != null && !startDate.trim().isEmpty()) {
                ps.setString(paramIndex++, startDate);
            }
            if (endDate != null && !endDate.trim().isEmpty()) {
                ps.setString(paramIndex++, endDate);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    summary.setTotalDays(rs.getInt("total_days"));
                    summary.setPresentDays(rs.getInt("present_days"));
                    summary.setAbsentDays(rs.getInt("absent_days"));
                    summary.setLateDays(rs.getInt("late_days"));
                    summary.setEarlyLeaveDays(rs.getInt("early_leave_days"));
                    summary.setBusinessTripDays(rs.getInt("business_trip_days"));
                    summary.setRemoteDays(rs.getInt("remote_days"));
                    summary.setTotalOvertimeHours(rs.getDouble("total_overtime"));
                    summary.setManualAdjustments(rs.getInt("manual_adjustments"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting attendance summary: " + e.getMessage());
            e.printStackTrace();
        }

        // Set filter parameters
        summary.setEmployeeCode(employeeCode);
        summary.setStartDate(startDate);
        summary.setEndDate(endDate);

        return summary;
    }

    /**
     * Get monthly attendance records from view with filters and pagination
     * @param month Month number (1-12), null for all months
     * @param year Year (e.g., 2024), null for all years
     * @param departmentId Department ID filter (optional)
     * @param employeeCode Employee code filter (optional)
     * @param page Current page number
     * @param pageSize Number of records per page
     * @return List of MonthlyAttendanceSummary
     */
    public List<model.MonthlyAttendanceSummary> getMonthlyAttendanceRecords(Integer month, Integer year,
                                                                             Integer departmentId, String employeeCode,
                                                                             int page, int pageSize) {
        List<model.MonthlyAttendanceSummary> records = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT employee_id, employee_code, employee_name, department_name, " +
            "attendance_month, present_days, absent_days, late_days, early_leave_days, " +
            "business_trip_days, remote_days, total_overtime_hours, total_working_days " +
            "FROM vw_monthly_attendance_summary " +
            "WHERE 1=1 "
        );

        // Add filters
        if (month != null && year != null) {
            sql.append("AND attendance_month = ? ");
        } else if (year != null) {
            sql.append("AND YEAR(STR_TO_DATE(CONCAT(attendance_month, '-01'), '%Y-%m-%d')) = ? ");
        }

        if (departmentId != null && departmentId > 0) {
            sql.append("AND employee_id IN (SELECT employee_id FROM employees WHERE department_id = ?) ");
        }

        if (employeeCode != null && !employeeCode.trim().isEmpty()) {
            sql.append("AND employee_code = ? ");
        }

        sql.append("ORDER BY attendance_month DESC, employee_code ASC ");
        sql.append("LIMIT ? OFFSET ?");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (month != null && year != null) {
                String monthStr = String.format("%04d-%02d", year, month);
                ps.setString(paramIndex++, monthStr);
            } else if (year != null) {
                ps.setInt(paramIndex++, year);
            }

            if (departmentId != null && departmentId > 0) {
                ps.setInt(paramIndex++, departmentId);
            }

            if (employeeCode != null && !employeeCode.trim().isEmpty()) {
                ps.setString(paramIndex++, employeeCode);
            }

            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex++, (page - 1) * pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToMonthlyAttendanceSummary(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting monthly attendance records: " + e.getMessage());
            e.printStackTrace();
        }

        return records;
    }

    /**
     * Get total count of monthly attendance records with filters
     * @param month Month number (1-12), null for all months
     * @param year Year (e.g., 2024), null for all years
     * @param departmentId Department ID filter (optional)
     * @param employeeCode Employee code filter (optional)
     * @return int total count
     */
    public int getTotalMonthlyAttendanceRecords(Integer month, Integer year,
                                                Integer departmentId, String employeeCode) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) as total " +
            "FROM vw_monthly_attendance_summary " +
            "WHERE 1=1 "
        );

        // Add filters
        if (month != null && year != null) {
            sql.append("AND attendance_month = ? ");
        } else if (year != null) {
            sql.append("AND YEAR(STR_TO_DATE(CONCAT(attendance_month, '-01'), '%Y-%m-%d')) = ? ");
        }

        if (departmentId != null && departmentId > 0) {
            sql.append("AND employee_id IN (SELECT employee_id FROM employees WHERE department_id = ?) ");
        }

        if (employeeCode != null && !employeeCode.trim().isEmpty()) {
            sql.append("AND employee_code = ? ");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (month != null && year != null) {
                String monthStr = String.format("%04d-%02d", year, month);
                ps.setString(paramIndex++, monthStr);
            } else if (year != null) {
                ps.setInt(paramIndex++, year);
            }

            if (departmentId != null && departmentId > 0) {
                ps.setInt(paramIndex++, departmentId);
            }

            if (employeeCode != null && !employeeCode.trim().isEmpty()) {
                ps.setString(paramIndex++, employeeCode);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting total monthly attendance records: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
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

    /**
     * Map ResultSet to MonthlyAttendanceSummary object
     * @param rs ResultSet from database query
     * @return MonthlyAttendanceSummary object
     * @throws SQLException
     */
    private model.MonthlyAttendanceSummary mapResultSetToMonthlyAttendanceSummary(ResultSet rs) throws SQLException {
        model.MonthlyAttendanceSummary summary = new model.MonthlyAttendanceSummary();
        summary.setEmployeeId(rs.getInt("employee_id"));
        summary.setEmployeeCode(rs.getString("employee_code"));
        summary.setEmployeeName(rs.getString("employee_name"));
        summary.setDepartmentName(rs.getString("department_name"));
        summary.setAttendanceMonth(rs.getString("attendance_month"));
        summary.setPresentDays(rs.getInt("present_days"));
        summary.setAbsentDays(rs.getInt("absent_days"));
        summary.setLateDays(rs.getInt("late_days"));
        summary.setEarlyLeaveDays(rs.getInt("early_leave_days"));
        summary.setBusinessTripDays(rs.getInt("business_trip_days"));
        summary.setRemoteDays(rs.getInt("remote_days"));
        summary.setTotalOvertimeHours(rs.getDouble("total_overtime_hours"));
        summary.setTotalWorkingDays(rs.getInt("total_working_days"));
        return summary;
    }
}

