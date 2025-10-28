package dal;

import model.BonusAdjustment;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for payroll_adjustments table
 * Handles all database operations for payroll adjustments (bonus/deduction)
 * @author admin
 */
public class BonusAdjustmentDAO extends DBContext {
    
    /**
     * Create a new payroll adjustment
     * @param adjustment BonusAdjustment object
     * @return Generated adjustment ID, or -1 if failed
     */
    public int createAdjustment(BonusAdjustment adjustment) {
        String sql = "INSERT INTO payroll_adjustments (payroll_id, employee_id, adjustment_type, " +
                     "amount, reason, status, requested_by) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (adjustment.getPayrollId() != null) {
                ps.setInt(1, adjustment.getPayrollId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }
            ps.setInt(2, adjustment.getEmployeeId());
            ps.setString(3, adjustment.getAdjustmentType());
            ps.setBigDecimal(4, adjustment.getAmount());
            ps.setString(5, adjustment.getReason());
            ps.setString(6, adjustment.getStatus());
            ps.setInt(7, adjustment.getRequestedBy());
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating payroll adjustment: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Get adjustment by ID
     * @param adjustmentId Adjustment ID
     * @return BonusAdjustment object or null
     */
    public BonusAdjustment getAdjustmentById(int adjustmentId) {
        String sql = "SELECT pa.*, e.employee_code, CONCAT(e.first_name, ' ', e.last_name) as employee_name, " +
                     "mp.payroll_month " +
                     "FROM payroll_adjustments pa " +
                     "JOIN employees e ON pa.employee_id = e.employee_id " +
                     "LEFT JOIN monthly_payroll mp ON pa.payroll_id = mp.payroll_id " +
                     "WHERE pa.adjustment_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, adjustmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractAdjustmentFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting adjustment by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * Get all adjustments for an employee in a specific payroll
     * @param payrollId Payroll ID
     * @return List of adjustments
     */
    public List<BonusAdjustment> getAdjustmentsByPayrollId(int payrollId) {
        List<BonusAdjustment> adjustments = new ArrayList<>();
        String sql = "SELECT pa.*, e.employee_code, CONCAT(e.first_name, ' ', e.last_name) as employee_name, " +
                     "mp.payroll_month " +
                     "FROM payroll_adjustments pa " +
                     "JOIN employees e ON pa.employee_id = e.employee_id " +
                     "LEFT JOIN monthly_payroll mp ON pa.payroll_id = mp.payroll_id " +
                     "WHERE pa.payroll_id = ? " +
                     "ORDER BY pa.created_at DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, payrollId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    adjustments.add(extractAdjustmentFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting adjustments by payroll ID: " + e.getMessage());
            e.printStackTrace();
        }
        return adjustments;
    }
    
    /**
     * Get all adjustments for an employee in a specific month
     * @param employeeId Employee ID
     * @param payrollMonth Payroll month
     * @return List of adjustments
     */
    public List<BonusAdjustment> getAdjustmentsByEmployeeAndMonth(int employeeId, Date payrollMonth) {
        List<BonusAdjustment> adjustments = new ArrayList<>();
        String sql = "SELECT pa.*, e.employee_code, CONCAT(e.first_name, ' ', e.last_name) as employee_name, " +
                     "mp.payroll_month " +
                     "FROM payroll_adjustments pa " +
                     "JOIN employees e ON pa.employee_id = e.employee_id " +
                     "LEFT JOIN monthly_payroll mp ON pa.payroll_id = mp.payroll_id " +
                     "WHERE pa.employee_id = ? AND mp.payroll_month = ? " +
                     "ORDER BY pa.created_at DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ps.setDate(2, payrollMonth);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    adjustments.add(extractAdjustmentFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting adjustments by employee and month: " + e.getMessage());
            e.printStackTrace();
        }
        return adjustments;
    }
    
    /**
     * Get approved adjustments for an employee in a specific month
     * @param employeeId Employee ID
     * @param payrollMonth Payroll month
     * @return List of approved adjustments
     */
    public List<BonusAdjustment> getApprovedAdjustmentsByEmployeeAndMonth(int employeeId, Date payrollMonth) {
        List<BonusAdjustment> adjustments = new ArrayList<>();
        String sql = "SELECT pa.*, e.employee_code, CONCAT(e.first_name, ' ', e.last_name) as employee_name, " +
                     "mp.payroll_month " +
                     "FROM payroll_adjustments pa " +
                     "JOIN employees e ON pa.employee_id = e.employee_id " +
                     "LEFT JOIN monthly_payroll mp ON pa.payroll_id = mp.payroll_id " +
                     "WHERE pa.employee_id = ? AND mp.payroll_month = ? AND pa.status = 'Approved' " +
                     "ORDER BY pa.created_at DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ps.setDate(2, payrollMonth);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    adjustments.add(extractAdjustmentFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting approved adjustments: " + e.getMessage());
            e.printStackTrace();
        }
        return adjustments;
    }
    
    /**
     * Get total approved adjustments amount for an employee in a specific month
     * @param employeeId Employee ID
     * @param payrollMonth Payroll month
     * @return Total amount (can be negative)
     */
    public BigDecimal getTotalApprovedAdjustments(int employeeId, Date payrollMonth) {
        String sql = "SELECT COALESCE(SUM(pa.amount), 0) as total " +
                     "FROM payroll_adjustments pa " +
                     "LEFT JOIN monthly_payroll mp ON pa.payroll_id = mp.payroll_id " +
                     "WHERE pa.employee_id = ? AND mp.payroll_month = ? AND pa.status = 'Approved'";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            ps.setDate(2, payrollMonth);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting total approved adjustments: " + e.getMessage());
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
    
    /**
     * Get all pending adjustments (for approval queue)
     * @return List of pending adjustments
     */
    public List<BonusAdjustment> getPendingAdjustments() {
        List<BonusAdjustment> adjustments = new ArrayList<>();
        String sql = "SELECT pa.*, e.employee_code, CONCAT(e.first_name, ' ', e.last_name) as employee_name, " +
                     "mp.payroll_month " +
                     "FROM payroll_adjustments pa " +
                     "JOIN employees e ON pa.employee_id = e.employee_id " +
                     "LEFT JOIN monthly_payroll mp ON pa.payroll_id = mp.payroll_id " +
                     "WHERE pa.status = 'Pending' " +
                     "ORDER BY pa.created_at ASC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                adjustments.add(extractAdjustmentFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error getting pending adjustments: " + e.getMessage());
            e.printStackTrace();
        }
        return adjustments;
    }
    
    /**
     * Approve an adjustment
     * @param adjustmentId Adjustment ID
     * @param approvedBy User ID who approved
     * @param approvalComment Approval comment
     * @return true if successful
     */
    public boolean approveAdjustment(int adjustmentId, int approvedBy, String approvalComment) {
        String sql = "UPDATE payroll_adjustments SET status = 'Approved', approved_by = ?, " +
                     "approval_comment = ?, approved_at = NOW() WHERE adjustment_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, approvedBy);
            ps.setString(2, approvalComment);
            ps.setInt(3, adjustmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error approving adjustment: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Reject an adjustment
     * @param adjustmentId Adjustment ID
     * @param approvedBy User ID who rejected
     * @param approvalComment Rejection reason
     * @return true if successful
     */
    public boolean rejectAdjustment(int adjustmentId, int approvedBy, String approvalComment) {
        String sql = "UPDATE payroll_adjustments SET status = 'Rejected', approved_by = ?, " +
                     "approval_comment = ?, approved_at = NOW() WHERE adjustment_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, approvedBy);
            ps.setString(2, approvalComment);
            ps.setInt(3, adjustmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error rejecting adjustment: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Delete an adjustment (only if pending)
     * @param adjustmentId Adjustment ID
     * @return true if successful
     */
    public boolean deleteAdjustment(int adjustmentId) {
        String sql = "DELETE FROM payroll_adjustments WHERE adjustment_id = ? AND status = 'Pending'";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, adjustmentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting adjustment: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Extract BonusAdjustment object from ResultSet
     */
    private BonusAdjustment extractAdjustmentFromResultSet(ResultSet rs) throws SQLException {
        BonusAdjustment adjustment = new BonusAdjustment();
        adjustment.setAdjustmentId(rs.getInt("adjustment_id"));
        adjustment.setPayrollId(rs.getObject("payroll_id", Integer.class));
        adjustment.setEmployeeId(rs.getInt("employee_id"));
        adjustment.setAdjustmentType(rs.getString("adjustment_type"));
        adjustment.setAmount(rs.getBigDecimal("amount"));
        adjustment.setReason(rs.getString("reason"));
        adjustment.setStatus(rs.getString("status"));
        adjustment.setRequestedBy(rs.getInt("requested_by"));
        adjustment.setApprovedBy(rs.getObject("approved_by", Integer.class));
        adjustment.setApprovalComment(rs.getString("approval_comment"));
        adjustment.setApprovedAt(rs.getTimestamp("approved_at"));
        adjustment.setCreatedAt(rs.getTimestamp("created_at"));
        adjustment.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Display fields
        try {
            adjustment.setEmployeeCode(rs.getString("employee_code"));
            adjustment.setEmployeeName(rs.getString("employee_name"));
            adjustment.setPayrollMonth(rs.getDate("payroll_month"));
        } catch (SQLException e) {
            // These fields might not be present in all queries
        }
        
        return adjustment;
    }
}