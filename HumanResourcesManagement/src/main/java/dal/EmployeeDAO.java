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
                     "WHERE e.employment_status = 'Active' AND e.is_deleted = FALSE " +
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
                     "WHERE e.employee_id = ? AND e.is_deleted = FALSE";
        
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
     * Get employee by employee code
     * @param employeeCode Employee code
     * @return Employee if found, null otherwise
     */
    public Employee getEmployeeByCode(String employeeCode) {
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
                     "WHERE e.employee_code = ? AND e.is_deleted = FALSE";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, employeeCode);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployee(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getEmployeeByCode: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get employee by user ID
     * @param userID User ID
     * @return Employee if found, null otherwise
     */
    public Employee getEmployeeByUserId(int userID) {
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
                     "WHERE e.user_id = ? AND e.is_deleted = FALSE";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployee(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getEmployeeByUserId: " + e.getMessage());
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
                     "WHERE e.is_deleted = FALSE " +
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
        String sql = "SELECT COUNT(*) as total FROM employees WHERE is_deleted = FALSE";

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
            "WHERE e.is_deleted = FALSE "
        );

        if (keyword != null && !keyword.trim().isEmpty()) {
            // Split keywords for better full-text search
            String[] keywords = keyword.trim().split("\\s+");
            StringBuilder fullTextQuery = new StringBuilder();
            
            // Build full-text search query with boolean operators
            for (String word : keywords) {
                if (fullTextQuery.length() > 0) {
                    fullTextQuery.append(" +");
                }
                fullTextQuery.append(word).append("*");
            }
            
            sql.append("AND ( " +
                // Full-text search on indexed fields
                "MATCH(e.first_name, e.last_name, e.employee_code) AGAINST (? IN BOOLEAN MODE) " +
                // Fallback to LIKE searches for better coverage
                " OR CONCAT(e.first_name, ' ', e.last_name) LIKE ? " +
                " OR e.employee_code LIKE ? " +
                " OR e.personal_email LIKE ? " +
                " OR e.phone_number LIKE ? " +
                " OR d.department_name LIKE ? " +
                " OR p.position_name LIKE ? " +
            ") ");
        }

        if (department != null && !department.trim().isEmpty()) {
            sql.append("AND e.department_id = ? ");
        }

        if (position != null && !position.trim().isEmpty()) {
            sql.append("AND e.position_id = ? ");
        }

        if (status != null && !status.trim().isEmpty()) {
            if (status.equals("!Terminated")) {
                sql.append("AND e.employment_status != 'Terminated' ");
            } else {
                sql.append("AND e.employment_status = ? ");
            }
        }

        sql.append("ORDER BY e.created_at DESC, e.employee_id DESC LIMIT ? OFFSET ?");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (keyword != null && !keyword.trim().isEmpty()) {
                // Use the constructed full-text search query
                String[] keywords = keyword.trim().split("\\s+");
                StringBuilder fullTextQuery = new StringBuilder();
                
                // Build full-text search query with boolean operators
                for (String word : keywords) {
                    if (fullTextQuery.length() > 0) {
                        fullTextQuery.append(" +");
                    }
                    fullTextQuery.append(word).append("*");
                }
                
                ps.setString(paramIndex++, fullTextQuery.toString());
                
                // Set LIKE parameters for broader matching
                String likePattern = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
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

            if (status != null && !status.trim().isEmpty() && !status.equals("!Terminated")) {
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
     * Add employee information record
     * @param employee Employee object with data
     * @return boolean success
     */
    public boolean addEmployeeInformation(Employee employee) {
        String sql = "INSERT INTO employees (user_id, employee_code, first_name, last_name, " +
                    "date_of_birth, gender, phone_number, personal_email, home_address, " +
                    "emergency_contact_name, emergency_contact_phone, department_id, position_id, " +
                    "manager_id, hire_date, employment_status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            // Set user_id (can be null)
            if (employee.getUserID() != null) {
                ps.setInt(1, employee.getUserID());
            } else {
                ps.setNull(1, java.sql.Types.INTEGER);
            }

            ps.setString(2, employee.getEmployeeCode());
            ps.setString(3, employee.getFirstName());
            ps.setString(4, employee.getLastName());

            // Set date_of_birth (can be null)
            if (employee.getDateOfBirth() != null) {
                ps.setDate(5, employee.getDateOfBirth());
            } else {
                ps.setNull(5, java.sql.Types.DATE);
            }

            // Set gender (can be null)
            if (employee.getGender() != null) {
                ps.setString(6, employee.getGender());
            } else {
                ps.setNull(6, java.sql.Types.VARCHAR);
            }
            ps.setString(7, employee.getPhoneNumber());
            ps.setString(8, employee.getPersonalEmail());
            ps.setString(9, employee.getHomeAddress());
            ps.setString(10, employee.getEmergencyContactName());
            ps.setString(11, employee.getEmergencyContactPhone());

            // Set department_id (can be null)
            if (employee.getDepartmentID() != null) {
                ps.setInt(12, employee.getDepartmentID());
            } else {
                ps.setNull(12, java.sql.Types.INTEGER);
            }

            // Set position_id (can be null)
            if (employee.getPositionID() != null) {
                ps.setInt(13, employee.getPositionID());
            } else {
                ps.setNull(13, java.sql.Types.INTEGER);
            }

            // Set manager_id (can be null)
            if (employee.getManagerID() != null) {
                ps.setInt(14, employee.getManagerID());
            } else {
                ps.setNull(14, java.sql.Types.INTEGER);
            }

            // Set hire_date (can be null, but default to current date)
            if (employee.getHireDate() != null) {
                ps.setDate(15, employee.getHireDate());
            } else {
                // Default to current date if not provided
                ps.setDate(15, new java.sql.Date(System.currentTimeMillis()));
            }

            ps.setString(16, employee.getEmploymentStatus() != null ? employee.getEmploymentStatus() : "Active");

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                // Get generated employee ID
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        employee.setEmployeeID(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error in addEmployeeInformation: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Check if employee code already exists
     * @param employeeCode Employee code to check
     * @return boolean true if exists
     */
    public boolean isEmployeeCodeExists(String employeeCode) {
        String sql = "SELECT COUNT(*) FROM employees WHERE employee_code = ? AND is_deleted = FALSE";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, employeeCode);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in isEmployeeCodeExists: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Get all employees for manager dropdown (excluding the employee being created/edited)
     * @param excludeEmployeeId Employee ID to exclude (can be null)
     * @return List<Employee>
     */
    public List<Employee> getAllEmployeesForManagerDropdown(Integer excludeEmployeeId) {
        List<Employee> employees = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT employee_id, employee_code, first_name, last_name, department_id, position_id " +
            "FROM employees WHERE employment_status = 'Active' AND is_deleted = FALSE"
        );

        if (excludeEmployeeId != null) {
            sql.append(" AND employee_id != ?");
        }

        sql.append(" ORDER BY first_name, last_name");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            if (excludeEmployeeId != null) {
                ps.setInt(1, excludeEmployeeId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Employee employee = new Employee();
                    employee.setEmployeeID(rs.getInt("employee_id"));
                    employee.setEmployeeCode(rs.getString("employee_code"));
                    employee.setFirstName(rs.getString("first_name"));
                    employee.setLastName(rs.getString("last_name"));
                    employee.setDepartmentID((Integer) rs.getObject("department_id"));
                    employee.setPositionID((Integer) rs.getObject("position_id"));
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllEmployeesForManagerDropdown: " + e.getMessage());
            e.printStackTrace();
        }

        return employees;
    }

    /**
     * Get all users without employee records (for linking new employees to existing user accounts)
     * @return List<User> users that don't have employee records yet
     */
    public List<model.User> getUsersWithoutEmployeeRecords() {
        List<model.User> users = new ArrayList<>();
        String sql = "SELECT u.user_id, u.username, u.email, u.role " +
                     "FROM users u " +
                     "LEFT JOIN employees e ON u.user_id = e.user_id " +
                     "WHERE e.user_id IS NULL AND u.status = 'Active' " +
                     "AND u.role IN ('Employee', 'Dept Manager', 'HR', 'HR Manager', 'HR_MANAGER') " +
                     "ORDER BY u.username";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                model.User user = new model.User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error in getUsersWithoutEmployeeRecords: " + e.getMessage());
            e.printStackTrace();
        }

        return users;
    }

    /**
     * Check if user already has an employee record
     * @param userId User ID to check
     * @return boolean true if user already has employee record
     */
    public boolean isUserAlreadyLinkedToEmployee(int userId) {
        String sql = "SELECT COUNT(*) FROM employees WHERE user_id = ? AND is_deleted = FALSE";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in isUserAlreadyLinkedToEmployee: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
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
            // Split keywords for better full-text search
            String[] keywords = keyword.trim().split("\\s+");
            StringBuilder fullTextQuery = new StringBuilder();
            
            // Build full-text search query with boolean operators
            for (String word : keywords) {
                if (fullTextQuery.length() > 0) {
                    fullTextQuery.append(" +");
                }
                fullTextQuery.append(word).append("*");
            }
            
            sql.append("AND ( " +
                // Full-text search on indexed fields
                "MATCH(e.first_name, e.last_name, e.employee_code) AGAINST (? IN BOOLEAN MODE) " +
                // Fallback to LIKE searches for better coverage
                " OR CONCAT(e.first_name, ' ', e.last_name) LIKE ? " +
                " OR e.employee_code LIKE ? " +
                " OR e.personal_email LIKE ? " +
                " OR e.phone_number LIKE ? " +
                " OR d.department_name LIKE ? " +
                " OR p.position_name LIKE ? " +
            ") ");
        }

        if (department != null && !department.trim().isEmpty()) {
            sql.append("AND e.department_id = ? ");
        }

        if (position != null && !position.trim().isEmpty()) {
            sql.append("AND e.position_id = ? ");
        }

        if (status != null && !status.trim().isEmpty()) {
            if (status.equals("!Terminated")) {
                sql.append("AND e.employment_status != 'Terminated' ");
            } else {
                sql.append("AND e.employment_status = ? ");
            }
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            if (keyword != null && !keyword.trim().isEmpty()) {
                // Use the constructed full-text search query
                String[] keywords = keyword.trim().split("\\s+");
                StringBuilder fullTextQuery = new StringBuilder();
                
                // Build full-text search query with boolean operators
                for (String word : keywords) {
                    if (fullTextQuery.length() > 0) {
                        fullTextQuery.append(" +");
                    }
                    fullTextQuery.append(word).append("*");
                }
                
                ps.setString(paramIndex++, fullTextQuery.toString());
                
                // Set LIKE parameters for broader matching
                String likePattern = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
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

            if (status != null && !status.trim().isEmpty() && !status.equals("!Terminated")) {
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
        String sql = "SELECT DISTINCT employment_status FROM employees WHERE employment_status IS NOT NULL AND is_deleted = FALSE ORDER BY employment_status";

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
        String sql = "SELECT department_id, department_name FROM departments WHERE is_deleted = FALSE ORDER BY department_name";

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
        String sql = "SELECT position_id, position_name FROM positions WHERE is_deleted = FALSE ORDER BY position_name";

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
    
    /**
     * Update employee information record
     * @param employee Employee object with updated data
     * @return boolean success
     */
    public boolean updateEmployeeInformation(Employee employee) {
        String sql = "UPDATE employees SET first_name = ?, last_name = ?, " +
                    "date_of_birth = ?, gender = ?, phone_number = ?, personal_email = ?, " +
                    "home_address = ?, emergency_contact_name = ?, emergency_contact_phone = ?, " +
                    "department_id = ?, position_id = ?, manager_id = ?, hire_date = ?, " +
                    "employment_status = ? WHERE employee_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, employee.getFirstName());
            ps.setString(2, employee.getLastName());
            
            // Set date_of_birth (can be null)
            if (employee.getDateOfBirth() != null) {
                ps.setDate(3, employee.getDateOfBirth());
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }
            
            // Set gender (can be null)
            if (employee.getGender() != null) {
                ps.setString(4, employee.getGender());
            } else {
                ps.setNull(4, java.sql.Types.VARCHAR);
            }
            
            ps.setString(5, employee.getPhoneNumber());
            ps.setString(6, employee.getPersonalEmail());
            ps.setString(7, employee.getHomeAddress());
            ps.setString(8, employee.getEmergencyContactName());
            ps.setString(9, employee.getEmergencyContactPhone());
            
            // Set department_id (can be null)
            if (employee.getDepartmentID() != null) {
                ps.setInt(10, employee.getDepartmentID());
            } else {
                ps.setNull(10, java.sql.Types.INTEGER);
            }
            
            // Set position_id (can be null)
            if (employee.getPositionID() != null) {
                ps.setInt(11, employee.getPositionID());
            } else {
                ps.setNull(11, java.sql.Types.INTEGER);
            }
            
            // Set manager_id (can be null)
            if (employee.getManagerID() != null) {
                ps.setInt(12, employee.getManagerID());
            } else {
                ps.setNull(12, java.sql.Types.INTEGER);
            }
            
            // Set hire_date (can be null, but default to current date)
            if (employee.getHireDate() != null) {
                ps.setDate(13, employee.getHireDate());
            } else {
                // Default to current date if not provided
                ps.setDate(13, new java.sql.Date(System.currentTimeMillis()));
            }
            
            ps.setString(14, employee.getEmploymentStatus() != null ? employee.getEmploymentStatus() : "Active");
            ps.setInt(15, employee.getEmployeeID());
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in updateEmployeeInformation: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Check if employee code already exists (excluding the current employee being edited)
     * @param employeeCode Employee code to check
     * @param excludeEmployeeId Employee ID to exclude from check
     * @return boolean true if exists
     */
    public boolean isEmployeeCodeExistsExcludingCurrent(String employeeCode, int excludeEmployeeId) {
        String sql = "SELECT COUNT(*) FROM employees WHERE employee_code = ? AND employee_id != ? AND is_deleted = FALSE";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, employeeCode);
            ps.setInt(2, excludeEmployeeId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in isEmployeeCodeExistsExcludingCurrent: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
    
    /**
     * Delete an employee record by ID (soft delete)
     * @param employeeId ID of the employee to delete
     * @return boolean success
     */
    public boolean deleteEmployee(int employeeId) {
        // First check if employee exists
        if (getEmployeeById(employeeId) == null) {
            return false;
        }
        
        // Soft delete the employee record
        String sql = "UPDATE employees SET is_deleted = TRUE WHERE employee_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error in deleteEmployee: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get employee name by ID (for use in success messages)
     * @param employeeId ID of the employee
     * @return String full name of employee or null if not found
     */
    public String getEmployeeNameById(int employeeId) {
        String sql = "SELECT first_name, last_name FROM employees WHERE employee_id = ? AND is_deleted = FALSE";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    return firstName + " " + lastName;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getEmployeeNameById: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
}
