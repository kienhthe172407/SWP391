package dal;

import model.MonthlyPayroll;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Monthly Payroll
 * Handles CRUD operations for monthly_payroll table
 * @author admin
 */
public class PayrollDAO extends DBContext {
    
    /**
     * Insert a new monthly payroll record
     * @param payroll MonthlyPayroll object
     * @return true if successful, false otherwise
     */
    public boolean insertPayroll(MonthlyPayroll payroll) {
        String sql = "INSERT INTO monthly_payroll (employee_id, payroll_month, base_salary, " +
                     "total_allowances, overtime_pay, total_bonus, total_benefits, total_deductions, " +
                     "gross_salary, net_salary, working_days, absent_days, late_days, overtime_hours, " +
                     "status, calculated_by, calculated_at, notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, payroll.getEmployeeID());
            ps.setDate(2, payroll.getPayrollMonth());
            ps.setBigDecimal(3, payroll.getBaseSalary());
            ps.setBigDecimal(4, payroll.getTotalAllowances());
            ps.setBigDecimal(5, payroll.getOvertimePay());
            ps.setBigDecimal(6, payroll.getTotalBonus());
            ps.setBigDecimal(7, payroll.getTotalBenefits());
            ps.setBigDecimal(8, payroll.getTotalDeductions());
            ps.setBigDecimal(9, payroll.getGrossSalary());
            ps.setBigDecimal(10, payroll.getNetSalary());
            ps.setInt(11, payroll.getWorkingDays());
            ps.setInt(12, payroll.getAbsentDays());
            ps.setInt(13, payroll.getLateDays());
            ps.setBigDecimal(14, payroll.getOvertimeHours());
            ps.setString(15, payroll.getStatus());
            
            if (payroll.getCalculatedBy() != null) {
                ps.setInt(16, payroll.getCalculatedBy());
            } else {
                ps.setNull(16, Types.INTEGER);
            }
            
            ps.setString(17, payroll.getNotes());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get generated payroll_id
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        payroll.setPayrollID(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error in insertPayroll: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Update existing payroll record
     * @param payroll MonthlyPayroll object
     * @return true if successful, false otherwise
     */
    public boolean updatePayroll(MonthlyPayroll payroll) {
        String sql = "UPDATE monthly_payroll SET base_salary = ?, total_allowances = ?, " +
                     "overtime_pay = ?, total_bonus = ?, total_benefits = ?, total_deductions = ?, " +
                     "gross_salary = ?, net_salary = ?, working_days = ?, absent_days = ?, " +
                     "late_days = ?, overtime_hours = ?, status = ?, calculated_by = ?, " +
                     "calculated_at = NOW(), notes = ? WHERE payroll_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBigDecimal(1, payroll.getBaseSalary());
            ps.setBigDecimal(2, payroll.getTotalAllowances());
            ps.setBigDecimal(3, payroll.getOvertimePay());
            ps.setBigDecimal(4, payroll.getTotalBonus());
            ps.setBigDecimal(5, payroll.getTotalBenefits());
            ps.setBigDecimal(6, payroll.getTotalDeductions());
            ps.setBigDecimal(7, payroll.getGrossSalary());
            ps.setBigDecimal(8, payroll.getNetSalary());
            ps.setInt(9, payroll.getWorkingDays());
            ps.setInt(10, payroll.getAbsentDays());
            ps.setInt(11, payroll.getLateDays());
            ps.setBigDecimal(12, payroll.getOvertimeHours());
            ps.setString(13, payroll.getStatus());
            
            if (payroll.getCalculatedBy() != null) {
                ps.setInt(14, payroll.getCalculatedBy());
            } else {
                ps.setNull(14, Types.INTEGER);
            }
            
            ps.setString(15, payroll.getNotes());
            ps.setInt(16, payroll.getPayrollID());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error in updatePayroll: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get payroll by employee ID and month
     * @param employeeID Employee ID
     * @param payrollMonth Payroll month (YYYY-MM-01)
     * @return MonthlyPayroll object or null if not found
     */
    public MonthlyPayroll getPayrollByEmployeeAndMonth(int employeeID, Date payrollMonth) {
        String sql = "SELECT * FROM monthly_payroll WHERE employee_id = ? AND payroll_month = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeID);
            ps.setDate(2, payrollMonth);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPayroll(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getPayrollByEmployeeAndMonth: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all payroll records for a specific month
     * @param payrollMonth Payroll month (YYYY-MM-01)
     * @return List of MonthlyPayroll objects
     */
    public List<MonthlyPayroll> getPayrollByMonth(Date payrollMonth) {
        List<MonthlyPayroll> payrolls = new ArrayList<>();
        String sql = "SELECT mp.*, e.employee_code, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS employee_name, " +
                     "d.department_name, p.position_name, " +
                     "CONCAT(u1.first_name, ' ', u1.last_name) AS calculated_by_name " +
                     "FROM monthly_payroll mp " +
                     "JOIN employees e ON mp.employee_id = e.employee_id " +
                     "LEFT JOIN departments d ON e.department_id = d.department_id " +
                     "LEFT JOIN positions p ON e.position_id = p.position_id " +
                     "LEFT JOIN users u1 ON mp.calculated_by = u1.user_id " +
                     "WHERE mp.payroll_month = ? " +
                     "ORDER BY e.employee_code";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, payrollMonth);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MonthlyPayroll payroll = mapResultSetToPayroll(rs);
                    payroll.setEmployeeCode(rs.getString("employee_code"));
                    payroll.setEmployeeName(rs.getString("employee_name"));
                    payroll.setDepartmentName(rs.getString("department_name"));
                    payroll.setPositionName(rs.getString("position_name"));
                    payroll.setCalculatedByName(rs.getString("calculated_by_name"));
                    payrolls.add(payroll);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getPayrollByMonth: " + e.getMessage());
            e.printStackTrace();
        }
        
        return payrolls;
    }
    
    /**
     * Get all payroll records for a specific employee
     * @param employeeID Employee ID
     * @return List of MonthlyPayroll objects ordered by month (newest first)
     */
    public List<MonthlyPayroll> getPayrollsByEmployeeId(int employeeID) {
        List<MonthlyPayroll> payrolls = new ArrayList<>();
        String sql = "SELECT mp.*, e.employee_code, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS employee_name, " +
                     "d.department_name, p.position_name, " +
                     "CONCAT(u1.first_name, ' ', u1.last_name) AS calculated_by_name " +
                     "FROM monthly_payroll mp " +
                     "JOIN employees e ON mp.employee_id = e.employee_id " +
                     "LEFT JOIN departments d ON e.department_id = d.department_id " +
                     "LEFT JOIN positions p ON e.position_id = p.position_id " +
                     "LEFT JOIN users u1 ON mp.calculated_by = u1.user_id " +
                     "WHERE mp.employee_id = ? " +
                     "ORDER BY mp.payroll_month DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeID);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MonthlyPayroll payroll = mapResultSetToPayroll(rs);
                    payroll.setEmployeeCode(rs.getString("employee_code"));
                    payroll.setEmployeeName(rs.getString("employee_name"));
                    payroll.setDepartmentName(rs.getString("department_name"));
                    payroll.setPositionName(rs.getString("position_name"));
                    payroll.setCalculatedByName(rs.getString("calculated_by_name"));
                    payrolls.add(payroll);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getPayrollsByEmployeeId: " + e.getMessage());
            e.printStackTrace();
        }
        
        return payrolls;
    }
    
    /**
     * Check if payroll exists for employee and month
     * @param employeeID Employee ID
     * @param payrollMonth Payroll month
     * @return true if exists, false otherwise
     */
    public boolean payrollExists(int employeeID, Date payrollMonth) {
        String sql = "SELECT COUNT(*) FROM monthly_payroll WHERE employee_id = ? AND payroll_month = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeID);
            ps.setDate(2, payrollMonth);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in payrollExists: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Map ResultSet to MonthlyPayroll object
     */
    private MonthlyPayroll mapResultSetToPayroll(ResultSet rs) throws SQLException {
        MonthlyPayroll payroll = new MonthlyPayroll();
        payroll.setPayrollID(rs.getInt("payroll_id"));
        payroll.setEmployeeID(rs.getInt("employee_id"));
        payroll.setPayrollMonth(rs.getDate("payroll_month"));
        payroll.setBaseSalary(rs.getBigDecimal("base_salary"));
        payroll.setTotalAllowances(rs.getBigDecimal("total_allowances"));
        payroll.setOvertimePay(rs.getBigDecimal("overtime_pay"));
        payroll.setTotalBonus(rs.getBigDecimal("total_bonus"));
        payroll.setTotalBenefits(rs.getBigDecimal("total_benefits"));
        payroll.setTotalDeductions(rs.getBigDecimal("total_deductions"));
        payroll.setGrossSalary(rs.getBigDecimal("gross_salary"));
        payroll.setNetSalary(rs.getBigDecimal("net_salary"));
        payroll.setWorkingDays(rs.getInt("working_days"));
        payroll.setAbsentDays(rs.getInt("absent_days"));
        payroll.setLateDays(rs.getInt("late_days"));
        payroll.setOvertimeHours(rs.getBigDecimal("overtime_hours"));
        payroll.setStatus(rs.getString("status"));
        payroll.setCalculatedAt(rs.getTimestamp("calculated_at"));
        
        int calculatedBy = rs.getInt("calculated_by");
        if (!rs.wasNull()) {
            payroll.setCalculatedBy(calculatedBy);
        }
        
        payroll.setApprovedAt(rs.getTimestamp("approved_at"));
        
        int approvedBy = rs.getInt("approved_by");
        if (!rs.wasNull()) {
            payroll.setApprovedBy(approvedBy);
        }
        
        payroll.setPaidAt(rs.getTimestamp("paid_at"));
        payroll.setNotes(rs.getString("notes"));
        payroll.setCreatedAt(rs.getTimestamp("created_at"));
        payroll.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return payroll;
    }
}

