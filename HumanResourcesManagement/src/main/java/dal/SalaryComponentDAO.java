package dal;

import model.SalaryComponent;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Salary Components
 * Handles CRUD operations for salary_components table
 * @author admin
 */
public class SalaryComponentDAO extends DBContext {
    
    /**
     * Get active salary component for an employee
     * @param employeeID Employee ID
     * @return Active SalaryComponent or null if not found
     */
    public SalaryComponent getActiveSalaryComponent(int employeeID) {
        String sql = "SELECT component_id, employee_id, base_salary, position_allowance, " +
                     "housing_allowance, transportation_allowance, meal_allowance, other_allowances, " +
                     "effective_from, effective_to, is_active, created_at, updated_at " +
                     "FROM salary_components " +
                     "WHERE employee_id = ? AND is_active = TRUE " +
                     "LIMIT 1";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToSalaryComponent(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getActiveSalaryComponent: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all salary components for an employee
     * @param employeeID Employee ID
     * @return List of SalaryComponents
     */
    public List<SalaryComponent> getSalaryComponentsByEmployee(int employeeID) {
        List<SalaryComponent> components = new ArrayList<>();
        String sql = "SELECT component_id, employee_id, base_salary, position_allowance, " +
                     "housing_allowance, transportation_allowance, meal_allowance, other_allowances, " +
                     "effective_from, effective_to, is_active, created_at, updated_at " +
                     "FROM salary_components " +
                     "WHERE employee_id = ? " +
                     "ORDER BY effective_from DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeID);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    components.add(mapResultSetToSalaryComponent(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getSalaryComponentsByEmployee: " + e.getMessage());
            e.printStackTrace();
        }
        
        return components;
    }
    
    /**
     * Add new salary component
     * @param component SalaryComponent to add
     * @return true if successful, false otherwise
     */
    public boolean addSalaryComponent(SalaryComponent component) {
        String sql = "INSERT INTO salary_components (employee_id, base_salary, position_allowance, " +
                     "housing_allowance, transportation_allowance, meal_allowance, other_allowances, " +
                     "effective_from, effective_to, is_active) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, component.getEmployeeID());
            ps.setBigDecimal(2, component.getBaseSalary());
            ps.setBigDecimal(3, component.getPositionAllowance());
            ps.setBigDecimal(4, component.getHousingAllowance());
            ps.setBigDecimal(5, component.getTransportationAllowance());
            ps.setBigDecimal(6, component.getMealAllowance());
            ps.setBigDecimal(7, component.getOtherAllowances());
            ps.setDate(8, component.getEffectiveFrom());
            
            if (component.getEffectiveTo() != null) {
                ps.setDate(9, component.getEffectiveTo());
            } else {
                ps.setNull(9, Types.DATE);
            }
            
            ps.setBoolean(10, component.isActive());
            
            int rowsAffected = ps.executeUpdate();
            
            if (rowsAffected > 0) {
                // Get generated ID
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        component.setComponentID(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            
        } catch (SQLException e) {
            System.err.println("Error in addSalaryComponent: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Update salary component
     * @param component SalaryComponent to update
     * @return true if successful, false otherwise
     */
    public boolean updateSalaryComponent(SalaryComponent component) {
        String sql = "UPDATE salary_components SET " +
                     "base_salary = ?, position_allowance = ?, housing_allowance = ?, " +
                     "transportation_allowance = ?, meal_allowance = ?, other_allowances = ?, " +
                     "effective_from = ?, effective_to = ?, is_active = ? " +
                     "WHERE component_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setBigDecimal(1, component.getBaseSalary());
            ps.setBigDecimal(2, component.getPositionAllowance());
            ps.setBigDecimal(3, component.getHousingAllowance());
            ps.setBigDecimal(4, component.getTransportationAllowance());
            ps.setBigDecimal(5, component.getMealAllowance());
            ps.setBigDecimal(6, component.getOtherAllowances());
            ps.setDate(7, component.getEffectiveFrom());
            
            if (component.getEffectiveTo() != null) {
                ps.setDate(8, component.getEffectiveTo());
            } else {
                ps.setNull(8, Types.DATE);
            }
            
            ps.setBoolean(9, component.isActive());
            ps.setInt(10, component.getComponentID());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in updateSalaryComponent: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Deactivate all active salary components for an employee
     * Sets effective_to to the specified date and is_active to FALSE
     * @param employeeID Employee ID
     * @param effectiveTo Date when components become inactive
     * @return true if successful, false otherwise
     */
    public boolean deactivateActiveSalaryComponents(int employeeID, Date effectiveTo) {
        String sql = "UPDATE salary_components SET " +
                     "effective_to = ?, is_active = FALSE " +
                     "WHERE employee_id = ? AND is_active = TRUE";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, effectiveTo);
            ps.setInt(2, employeeID);
            
            ps.executeUpdate(); // May affect 0 or more rows
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error in deactivateActiveSalaryComponents: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete salary component by ID
     * @param componentID Component ID
     * @return true if successful, false otherwise
     */
    public boolean deleteSalaryComponent(int componentID) {
        String sql = "DELETE FROM salary_components WHERE component_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, componentID);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error in deleteSalaryComponent: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all active salary components (for all employees)
     * Used for bulk salary calculation
     * @return List of active SalaryComponents with employee information
     */
    public List<SalaryComponent> getAllActiveSalaryComponents() {
        List<SalaryComponent> components = new ArrayList<>();
        String sql = "SELECT sc.component_id, sc.employee_id, sc.base_salary, sc.position_allowance, " +
                     "sc.housing_allowance, sc.transportation_allowance, sc.meal_allowance, sc.other_allowances, " +
                     "sc.effective_from, sc.effective_to, sc.is_active, sc.created_at, sc.updated_at " +
                     "FROM salary_components sc " +
                     "JOIN employees e ON sc.employee_id = e.employee_id " +
                     "WHERE sc.is_active = TRUE AND e.employment_status = 'Active' " +
                     "ORDER BY e.employee_code";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SalaryComponent component = mapResultSetToSalaryComponent(rs);
                components.add(component);
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllActiveSalaryComponents: " + e.getMessage());
            e.printStackTrace();
        }

        return components;
    }
    
    /**
     * Helper method to map ResultSet to SalaryComponent object
     * @param rs ResultSet
     * @return SalaryComponent object
     * @throws SQLException if database error occurs
     */
    private SalaryComponent mapResultSetToSalaryComponent(ResultSet rs) throws SQLException {
        SalaryComponent component = new SalaryComponent();
        
        component.setComponentID(rs.getInt("component_id"));
        component.setEmployeeID(rs.getInt("employee_id"));
        component.setBaseSalary(rs.getBigDecimal("base_salary"));
        component.setPositionAllowance(rs.getBigDecimal("position_allowance"));
        component.setHousingAllowance(rs.getBigDecimal("housing_allowance"));
        component.setTransportationAllowance(rs.getBigDecimal("transportation_allowance"));
        component.setMealAllowance(rs.getBigDecimal("meal_allowance"));
        component.setOtherAllowances(rs.getBigDecimal("other_allowances"));
        component.setEffectiveFrom(rs.getDate("effective_from"));
        component.setEffectiveTo(rs.getDate("effective_to"));
        component.setActive(rs.getBoolean("is_active"));
        component.setCreatedAt(rs.getTimestamp("created_at"));
        component.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return component;
    }
}

