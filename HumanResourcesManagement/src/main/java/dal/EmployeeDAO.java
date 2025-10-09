package dal;

import model.Employee;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Employee
 * @author admin
 */
public class EmployeeDAO extends DBContext {
    
    /**
     * Get all active employees
     * @return List<Employee>
     */
    public List<Employee> getAllActiveEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT e.employee_id, e.user_id, e.employee_code, e.first_name, e.last_name, " +
                     "e.date_of_birth, e.gender, e.phone_number, e.personal_email, " +
                     "e.home_address, e.emergency_contact_name, e.emergency_contact_phone, " +
                     "e.department_id, e.position_id, e.manager_id, e.hire_date, e.employment_status, " +
                     "e.created_at, e.updated_at, " +
                     "d.department_name, p.position_name, " +
                     "CONCAT(m.first_name, ' ', m.last_name) AS manager_name " +
                     "FROM employees e " +
                     "LEFT JOIN departments d ON e.department_id = d.department_id " +
                     "LEFT JOIN positions p ON e.position_id = p.position_id " +
                     "LEFT JOIN employees m ON e.manager_id = m.employee_id " +
                     "WHERE e.employment_status = 'Active' " +
                     "ORDER BY e.first_name, e.last_name";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Employee employee = mapResultSetToEmployee(rs);
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllActiveEmployees: " + e.getMessage());
            e.printStackTrace();
        }
        
        return employees;
    }
    
    /**
     * Get employee by ID
     * @param employeeID
     * @return Employee
     */
    public Employee getEmployeeById(int employeeID) {
        String sql = "SELECT e.employee_id, e.user_id, e.employee_code, e.first_name, e.last_name, " +
                     "e.date_of_birth, e.gender, e.phone_number, e.personal_email, " +
                     "e.home_address, e.emergency_contact_name, e.emergency_contact_phone, " +
                     "e.department_id, e.position_id, e.manager_id, e.hire_date, e.employment_status, " +
                     "e.created_at, e.updated_at, " +
                     "d.department_name, p.position_name, " +
                     "CONCAT(m.first_name, ' ', m.last_name) AS manager_name " +
                     "FROM employees e " +
                     "LEFT JOIN departments d ON e.department_id = d.department_id " +
                     "LEFT JOIN positions p ON e.position_id = p.position_id " +
                     "LEFT JOIN employees m ON e.manager_id = m.employee_id " +
                     "WHERE e.employee_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployee(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getEmployeeById: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Helper method to map ResultSet to Employee object
     * @param rs ResultSet from query
     * @return Employee object
     * @throws SQLException if error occurs
     */
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        
        // Basic employee fields
        employee.setEmployeeID(rs.getInt("employee_id"));
        employee.setUserID((Integer) rs.getObject("user_id"));
        employee.setEmployeeCode(rs.getString("employee_code"));
        employee.setFirstName(rs.getString("first_name"));
        employee.setLastName(rs.getString("last_name"));
        employee.setDateOfBirth(rs.getDate("date_of_birth"));
        employee.setGender(rs.getString("gender"));
        employee.setPhoneNumber(rs.getString("phone_number"));
        employee.setPersonalEmail(rs.getString("personal_email"));
        employee.setHomeAddress(rs.getString("home_address"));
        employee.setEmergencyContactName(rs.getString("emergency_contact_name"));
        employee.setEmergencyContactPhone(rs.getString("emergency_contact_phone"));
        employee.setDepartmentID((Integer) rs.getObject("department_id"));
        employee.setPositionID((Integer) rs.getObject("position_id"));
        employee.setManagerID((Integer) rs.getObject("manager_id"));
        employee.setHireDate(rs.getDate("hire_date"));
        employee.setEmploymentStatus(rs.getString("employment_status"));
        employee.setCreatedAt(rs.getTimestamp("created_at"));
        employee.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Additional information from JOINs
        employee.setDepartmentName(rs.getString("department_name"));
        employee.setPositionName(rs.getString("position_name"));
        employee.setManagerName(rs.getString("manager_name"));
        
        return employee;
    }
}
