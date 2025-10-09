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
     * Get all employees with pagination (for HR/HR Manager)
     * @param page Current page number
     * @param pageSize Number of records per page
     * @return List<Employee>
     */
    public List<Employee> getAllEmployees(int page, int pageSize) {
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
                     "ORDER BY e.created_at DESC, e.employee_id DESC " +
                     "LIMIT ? OFFSET ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee employee = mapResultSetToEmployee(rs);
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllEmployees: " + e.getMessage());
            e.printStackTrace();
        }

        return employees;
    }

    /**
     * Get total count of all employees
     * @return int total count
     */
    public int getTotalEmployees() {
        String sql = "SELECT COUNT(*) as total FROM employees";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error in getTotalEmployees: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Search employees with filters and pagination
     * @param keyword Search keyword (name, employee code, email)
     * @param department Department filter
     * @param position Position filter
     * @param status Employment status filter
     * @param page Current page number
     * @param pageSize Number of records per page
     * @return List<Employee>
     */
    public List<Employee> searchEmployees(String keyword, String department, String position,
                                        String status, int page, int pageSize) {
        List<Employee> employees = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT e.employee_id, e.user_id, e.employee_code, e.first_name, e.last_name, " +
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
            "WHERE 1=1 "
        );

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND ( " +
                "MATCH(e.first_name, e.last_name, e.employee_code) AGAINST (? IN BOOLEAN MODE) " +
                " OR CONCAT(e.first_name, ' ', e.last_name) LIKE ? " +
                " OR e.employee_code LIKE ? " +
                " OR e.personal_email LIKE ? " +
            ") ");
        }

        if (department != null && !department.trim().isEmpty()) {
            sql.append("AND e.department_id = ? ");
        }

        if (position != null && !position.trim().isEmpty()) {
            sql.append("AND e.position_id = ? ");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND e.employment_status = ? ");
        }

        sql.append("ORDER BY e.created_at DESC, e.employee_id DESC LIMIT ? OFFSET ?");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(paramIndex++, "+" + keyword.trim() + "*");
                String likePattern = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
            }

            if (department != null && !department.trim().isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(department));
            }

            if (position != null && !position.trim().isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(position));
            }

            if (status != null && !status.trim().isEmpty()) {
                ps.setString(paramIndex++, status);
            }

            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex, (page - 1) * pageSize);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee employee = mapResultSetToEmployee(rs);
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in searchEmployees: " + e.getMessage());
            e.printStackTrace();
        }

        return employees;
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

    /**
     * Get total count of search results
     * @param keyword Search keyword
     * @param department Department filter
     * @param position Position filter
     * @param status Employment status filter
     * @return int total count
     */
    public int getTotalSearchResults(String keyword, String department, String position, String status) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) as total FROM employees e " +
            "LEFT JOIN departments d ON e.department_id = d.department_id " +
            "LEFT JOIN positions p ON e.position_id = p.position_id " +
            "WHERE 1=1 "
        );

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND ( " +
                "MATCH(e.first_name, e.last_name, e.employee_code) AGAINST (? IN BOOLEAN MODE) " +
                " OR CONCAT(e.first_name, ' ', e.last_name) LIKE ? " +
                " OR e.employee_code LIKE ? " +
                " OR e.personal_email LIKE ? " +
            ") ");
        }

        if (department != null && !department.trim().isEmpty()) {
            sql.append("AND e.department_id = ? ");
        }

        if (position != null && !position.trim().isEmpty()) {
            sql.append("AND e.position_id = ? ");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND e.employment_status = ? ");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(paramIndex++, "+" + keyword.trim() + "*");
                String likePattern = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
            }

            if (department != null && !department.trim().isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(department));
            }

            if (position != null && !position.trim().isEmpty()) {
                ps.setInt(paramIndex++, Integer.parseInt(position));
            }

            if (status != null && !status.trim().isEmpty()) {
                ps.setString(paramIndex++, status);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getTotalSearchResults: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Get all employment statuses
     * @return List<String>
     */
    public List<String> getAllEmploymentStatuses() {
        List<String> statuses = new ArrayList<>();
        String sql = "SELECT DISTINCT employment_status FROM employees WHERE employment_status IS NOT NULL ORDER BY employment_status";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                statuses.add(rs.getString("employment_status"));
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllEmploymentStatuses: " + e.getMessage());
            e.printStackTrace();
        }

        return statuses;
    }

    /**
     * Get all departments for filter dropdown
     * @return List<Department>
     */
    public List<model.Department> getAllDepartments() {
        List<model.Department> departments = new ArrayList<>();
        String sql = "SELECT department_id, department_name FROM departments ORDER BY department_name";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.Department dept = new model.Department();
                dept.setDepartmentId(rs.getInt("department_id"));
                dept.setDepartmentName(rs.getString("department_name"));
                departments.add(dept);
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllDepartments: " + e.getMessage());
            e.printStackTrace();
        }

        return departments;
    }

    /**
     * Get all positions for filter dropdown
     * @return List<Position>
     */
    public List<model.Position> getAllPositions() {
        List<model.Position> positions = new ArrayList<>();
        String sql = "SELECT position_id, position_name FROM positions ORDER BY position_name";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.Position pos = new model.Position();
                pos.setPositionId(rs.getInt("position_id"));
                pos.setPositionName(rs.getString("position_name"));
                positions.add(pos);
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllPositions: " + e.getMessage());
            e.printStackTrace();
        }

        return positions;
    }
}
