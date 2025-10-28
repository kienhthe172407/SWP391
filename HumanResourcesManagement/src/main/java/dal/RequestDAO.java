package dal;

import model.Request;
import model.RequestType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

/**
 * Data Access Object for requests and request_types tables
 * Handles all database operations related to employee requests
 * @author admin
 */
public class RequestDAO extends DBContext {

    /**
     * Get all request types
     * @return List of RequestType
     */
    public List<RequestType> getAllRequestTypes() {
        List<RequestType> requestTypes = new ArrayList<>();
        String sql = "SELECT request_type_id, request_type_name, description, " +
                     "requires_approval, max_days_per_year, is_paid, " +
                     "created_at, updated_at " +
                     "FROM request_types " +
                     "ORDER BY request_type_name";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                RequestType rt = new RequestType();
                rt.setRequestTypeID(rs.getInt("request_type_id"));
                rt.setRequestTypeName(rs.getString("request_type_name"));
                rt.setDescription(rs.getString("description"));
                rt.setRequiresApproval(rs.getBoolean("requires_approval"));
                
                // Handle nullable max_days_per_year
                int maxDays = rs.getInt("max_days_per_year");
                if (!rs.wasNull()) {
                    rt.setMaxDaysPerYear(maxDays);
                }
                
                rt.setPaid(rs.getBoolean("is_paid"));
                rt.setCreatedAt(rs.getTimestamp("created_at"));
                rt.setUpdatedAt(rs.getTimestamp("updated_at"));
                
                requestTypes.add(rt);
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllRequestTypes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return requestTypes;
    }

    /**
     * Get request type by ID
     * @param requestTypeID Request type ID
     * @return RequestType if found, null otherwise
     */
    public RequestType getRequestTypeById(int requestTypeID) {
        String sql = "SELECT request_type_id, request_type_name, description, " +
                     "requires_approval, max_days_per_year, is_paid, " +
                     "created_at, updated_at " +
                     "FROM request_types " +
                     "WHERE request_type_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, requestTypeID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RequestType rt = new RequestType();
                    rt.setRequestTypeID(rs.getInt("request_type_id"));
                    rt.setRequestTypeName(rs.getString("request_type_name"));
                    rt.setDescription(rs.getString("description"));
                    rt.setRequiresApproval(rs.getBoolean("requires_approval"));
                    
                    int maxDays = rs.getInt("max_days_per_year");
                    if (!rs.wasNull()) {
                        rt.setMaxDaysPerYear(maxDays);
                    }
                    
                    rt.setPaid(rs.getBoolean("is_paid"));
                    rt.setCreatedAt(rs.getTimestamp("created_at"));
                    rt.setUpdatedAt(rs.getTimestamp("updated_at"));
                    
                    return rt;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getRequestTypeById: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Create a new request
     * @param request Request object to insert
     * @return Generated request ID, or -1 if failed
     */
    public int createRequest(Request request) {
        String sql = "INSERT INTO requests (employee_id, request_type_id, start_date, end_date, " +
                     "number_of_days, reason, request_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, request.getEmployeeID());
            ps.setInt(2, request.getRequestTypeID());
            ps.setDate(3, request.getStartDate());
            ps.setDate(4, request.getEndDate());
            ps.setBigDecimal(5, request.getNumberOfDays());
            ps.setString(6, request.getReason());
            ps.setString(7, request.getRequestStatus() != null ? request.getRequestStatus() : "Pending");
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in createRequest: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }

    /**
     * Get request by ID with full details (including employee and request type info)
     * @param requestID Request ID
     * @return Request if found, null otherwise
     */
    public Request getRequestById(int requestID) {
        String sql = "SELECT r.request_id, r.employee_id, r.request_type_id, r.start_date, r.end_date, " +
                     "r.number_of_days, r.reason, r.request_status, r.reviewed_by, r.review_comment, " +
                     "r.reviewed_at, r.cancelled_at, r.created_at, r.updated_at, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS employee_name, " +
                     "e.employee_code, " +
                     "rt.request_type_name, " +
                     "d.department_name, " +
                     "CONCAT(u.first_name, ' ', u.last_name) AS reviewer_name " +
                     "FROM requests r " +
                     "LEFT JOIN employees e ON r.employee_id = e.employee_id " +
                     "LEFT JOIN request_types rt ON r.request_type_id = rt.request_type_id " +
                     "LEFT JOIN departments d ON e.department_id = d.department_id " +
                     "LEFT JOIN users u ON r.reviewed_by = u.user_id " +
                     "WHERE r.request_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, requestID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRequest(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getRequestById: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * Update an existing request (only if status is Pending)
     * @param request Request object with updated information
     * @return true if update successful, false otherwise
     */
    public boolean updateRequest(Request request) {
        String sql = "UPDATE requests SET request_type_id = ?, start_date = ?, end_date = ?, " +
                     "number_of_days = ?, reason = ?, updated_at = NOW() " +
                     "WHERE request_id = ? AND request_status = 'Pending'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, request.getRequestTypeID());
            ps.setDate(2, new java.sql.Date(request.getStartDate().getTime()));
            ps.setDate(3, new java.sql.Date(request.getEndDate().getTime()));
            ps.setBigDecimal(4, request.getNumberOfDays());
            ps.setString(5, request.getReason());
            ps.setInt(6, request.getRequestID());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error in updateRequest: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get all requests for a specific employee
     * @param employeeID Employee ID
     * @return List of Request
     */
    public List<Request> getRequestsByEmployeeId(int employeeID) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT r.request_id, r.employee_id, r.request_type_id, r.start_date, r.end_date, " +
                     "r.number_of_days, r.reason, r.request_status, r.reviewed_by, r.review_comment, " +
                     "r.reviewed_at, r.cancelled_at, r.created_at, r.updated_at, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS employee_name, " +
                     "e.employee_code, " +
                     "rt.request_type_name, " +
                     "d.department_name, " +
                     "CONCAT(u.first_name, ' ', u.last_name) AS reviewer_name " +
                     "FROM requests r " +
                     "LEFT JOIN employees e ON r.employee_id = e.employee_id " +
                     "LEFT JOIN request_types rt ON r.request_type_id = rt.request_type_id " +
                     "LEFT JOIN departments d ON e.department_id = d.department_id " +
                     "LEFT JOIN users u ON r.reviewed_by = u.user_id " +
                     "WHERE r.employee_id = ? " +
                     "ORDER BY r.created_at DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeID);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSetToRequest(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getRequestsByEmployeeId: " + e.getMessage());
            e.printStackTrace();
        }
        
        return requests;
    }

    /**
     * Update request status
     * @param requestID Request ID
     * @param status New status
     * @return true if successful, false otherwise
     */
    public boolean updateRequestStatus(int requestID, String status) {
        String sql = "UPDATE requests SET request_status = ? WHERE request_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, requestID);
            
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error in updateRequestStatus: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    /**
     * Cancel a request (set status to Cancelled and set cancelled_at timestamp)
     * @param requestID Request ID
     * @return true if successful, false otherwise
     */
    public boolean cancelRequest(int requestID) {
        String sql = "UPDATE requests SET request_status = 'Cancelled', cancelled_at = CURRENT_TIMESTAMP " +
                     "WHERE request_id = ? AND request_status = 'Pending'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, requestID);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error in cancelRequest: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Get all requests for employees in a specific department
     * Used by Department Managers to view their team's requests
     * @param departmentID Department ID
     * @return List of requests from employees in the department
     */
    public List<Request> getRequestsByDepartment(int departmentID) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT r.request_id, r.employee_id, r.request_type_id, r.start_date, r.end_date, " +
                     "r.number_of_days, r.reason, r.request_status, r.reviewed_by, r.review_comment, " +
                     "r.reviewed_at, r.cancelled_at, r.created_at, r.updated_at, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS employee_name, " +
                     "e.employee_code, " +
                     "rt.request_type_name, " +
                     "d.department_name, " +
                     "CONCAT(u.first_name, ' ', u.last_name) AS reviewer_name " +
                     "FROM requests r " +
                     "INNER JOIN employees e ON r.employee_id = e.employee_id " +
                     "INNER JOIN request_types rt ON r.request_type_id = rt.request_type_id " +
                     "LEFT JOIN departments d ON e.department_id = d.department_id " +
                     "LEFT JOIN users u ON r.reviewed_by = u.user_id " +
                     "WHERE e.department_id = ? " +
                     "ORDER BY r.created_at DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, departmentID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSetToRequest(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getRequestsByDepartment: " + e.getMessage());
            e.printStackTrace();
        }

        return requests;
    }

    /**
     * Get all requests for employees managed by a specific manager
     * Used by Department Managers to view requests from their direct reports
     * @param managerID Manager's employee ID
     * @return List of requests from employees reporting to this manager
     */
    public List<Request> getRequestsByManager(int managerID) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT r.request_id, r.employee_id, r.request_type_id, r.start_date, r.end_date, " +
                     "r.number_of_days, r.reason, r.request_status, r.reviewed_by, r.review_comment, " +
                     "r.reviewed_at, r.cancelled_at, r.created_at, r.updated_at, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS employee_name, " +
                     "e.employee_code, " +
                     "rt.request_type_name, " +
                     "d.department_name, " +
                     "CONCAT(u.first_name, ' ', u.last_name) AS reviewer_name " +
                     "FROM requests r " +
                     "INNER JOIN employees e ON r.employee_id = e.employee_id " +
                     "INNER JOIN request_types rt ON r.request_type_id = rt.request_type_id " +
                     "LEFT JOIN departments d ON e.department_id = d.department_id " +
                     "LEFT JOIN users u ON r.reviewed_by = u.user_id " +
                     "WHERE e.manager_id = ? " +
                     "ORDER BY r.created_at DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, managerID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSetToRequest(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getRequestsByManager: " + e.getMessage());
            e.printStackTrace();
        }

        return requests;
    }

    /**
     * Get pending requests for a department (for manager approval)
     * @param departmentID Department ID
     * @return List of pending requests
     */
    public List<Request> getPendingRequestsByDepartment(int departmentID) {
        List<Request> requests = new ArrayList<>();
        String sql = "SELECT r.request_id, r.employee_id, r.request_type_id, r.start_date, r.end_date, " +
                     "r.number_of_days, r.reason, r.request_status, r.reviewed_by, r.review_comment, " +
                     "r.reviewed_at, r.cancelled_at, r.created_at, r.updated_at, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS employee_name, " +
                     "e.employee_code, " +
                     "rt.request_type_name, " +
                     "d.department_name, " +
                     "CONCAT(u.first_name, ' ', u.last_name) AS reviewer_name " +
                     "FROM requests r " +
                     "INNER JOIN employees e ON r.employee_id = e.employee_id " +
                     "INNER JOIN request_types rt ON r.request_type_id = rt.request_type_id " +
                     "LEFT JOIN departments d ON e.department_id = d.department_id " +
                     "LEFT JOIN users u ON r.reviewed_by = u.user_id " +
                     "WHERE e.department_id = ? AND r.request_status = 'Pending' " +
                     "ORDER BY r.created_at ASC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, departmentID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSetToRequest(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getPendingRequestsByDepartment: " + e.getMessage());
            e.printStackTrace();
        }

        return requests;
    }

    /**
     * Approve or reject a request
     * @param requestID Request ID
     * @param status New status ("Approved" or "Rejected")
     * @param reviewerUserID User ID of the reviewer
     * @param reviewComment Optional comment from reviewer
     * @return true if successful, false otherwise
     */
    public boolean approveRejectRequest(int requestID, String status, int reviewerUserID, String reviewComment) {
        String sql = "UPDATE requests SET request_status = ?, reviewed_by = ?, review_comment = ?, reviewed_at = NOW() " +
                     "WHERE request_id = ? AND request_status = 'Pending'";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, reviewerUserID);
            ps.setString(3, reviewComment);
            ps.setInt(4, requestID);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error in approveRejectRequest: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Search and filter requests by department with multiple criteria
     * @param departmentID Department ID
     * @param status Filter by status (null or empty for all)
     * @param requestTypeName Filter by request type name (null or empty for all)
     * @param employeeName Search by employee name (null or empty for all)
     * @param startDate Filter by start date from (null for no filter)
     * @param endDate Filter by start date to (null for no filter)
     * @return List of filtered requests
     */
    public List<Request> searchRequestsByDepartment(int departmentID, String status, String requestTypeName,
                                                     String employeeName, java.sql.Date startDate, java.sql.Date endDate) {
        List<Request> requests = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT r.request_id, r.employee_id, r.request_type_id, r.start_date, r.end_date, ")
           .append("r.number_of_days, r.reason, r.request_status, r.reviewed_by, r.review_comment, ")
           .append("r.reviewed_at, r.cancelled_at, r.created_at, r.updated_at, ")
           .append("CONCAT(e.first_name, ' ', e.last_name) AS employee_name, ")
           .append("e.employee_code, ")
           .append("rt.request_type_name, ")
           .append("d.department_name, ")
           .append("CONCAT(u.first_name, ' ', u.last_name) AS reviewer_name ")
           .append("FROM requests r ")
           .append("INNER JOIN employees e ON r.employee_id = e.employee_id ")
           .append("INNER JOIN request_types rt ON r.request_type_id = rt.request_type_id ")
           .append("LEFT JOIN departments d ON e.department_id = d.department_id ")
           .append("LEFT JOIN users u ON r.reviewed_by = u.user_id ")
           .append("WHERE e.department_id = ? ");

        // Add filters
        if (status != null && !status.isEmpty() && !"All".equals(status)) {
            sql.append("AND r.request_status = ? ");
        }
        if (requestTypeName != null && !requestTypeName.isEmpty() && !"All".equals(requestTypeName)) {
            sql.append("AND rt.request_type_name = ? ");
        }
        if (employeeName != null && !employeeName.isEmpty()) {
            sql.append("AND CONCAT(e.first_name, ' ', e.last_name) LIKE ? ");
        }
        if (startDate != null) {
            sql.append("AND r.start_date >= ? ");
        }
        if (endDate != null) {
            sql.append("AND r.start_date <= ? ");
        }

        sql.append("ORDER BY r.created_at DESC");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, departmentID);

            if (status != null && !status.isEmpty() && !"All".equals(status)) {
                ps.setString(paramIndex++, status);
            }
            if (requestTypeName != null && !requestTypeName.isEmpty() && !"All".equals(requestTypeName)) {
                ps.setString(paramIndex++, requestTypeName);
            }
            if (employeeName != null && !employeeName.isEmpty()) {
                ps.setString(paramIndex++, "%" + employeeName + "%");
            }
            if (startDate != null) {
                ps.setDate(paramIndex++, startDate);
            }
            if (endDate != null) {
                ps.setDate(paramIndex++, endDate);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSetToRequest(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in searchRequestsByDepartment: " + e.getMessage());
            e.printStackTrace();
        }

        return requests;
    }

    /**
     * Search and filter requests by employee with multiple criteria
     * @param employeeID Employee ID
     * @param status Filter by status (null or empty for all)
     * @param requestTypeName Filter by request type name (null or empty for all)
     * @param startDate Filter by start date from (null for no filter)
     * @param endDate Filter by start date to (null for no filter)
     * @return List of filtered requests
     */
    public List<Request> searchRequestsByEmployee(int employeeID, String status, String requestTypeName,
                                                   java.sql.Date startDate, java.sql.Date endDate) {
        List<Request> requests = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT r.request_id, r.employee_id, r.request_type_id, r.start_date, r.end_date, ")
           .append("r.number_of_days, r.reason, r.request_status, r.reviewed_by, r.review_comment, ")
           .append("r.reviewed_at, r.cancelled_at, r.created_at, r.updated_at, ")
           .append("CONCAT(e.first_name, ' ', e.last_name) AS employee_name, ")
           .append("e.employee_code, ")
           .append("rt.request_type_name, ")
           .append("d.department_name, ")
           .append("CONCAT(u.first_name, ' ', u.last_name) AS reviewer_name ")
           .append("FROM requests r ")
           .append("INNER JOIN employees e ON r.employee_id = e.employee_id ")
           .append("INNER JOIN request_types rt ON r.request_type_id = rt.request_type_id ")
           .append("LEFT JOIN departments d ON e.department_id = d.department_id ")
           .append("LEFT JOIN users u ON r.reviewed_by = u.user_id ")
           .append("WHERE r.employee_id = ? ");

        // Add filters
        if (status != null && !status.isEmpty() && !"All".equals(status)) {
            sql.append("AND r.request_status = ? ");
        }
        if (requestTypeName != null && !requestTypeName.isEmpty() && !"All".equals(requestTypeName)) {
            sql.append("AND rt.request_type_name = ? ");
        }
        if (startDate != null) {
            sql.append("AND r.start_date >= ? ");
        }
        if (endDate != null) {
            sql.append("AND r.start_date <= ? ");
        }

        sql.append("ORDER BY r.created_at DESC");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            ps.setInt(paramIndex++, employeeID);

            if (status != null && !status.isEmpty() && !"All".equals(status)) {
                ps.setString(paramIndex++, status);
            }
            if (requestTypeName != null && !requestTypeName.isEmpty() && !"All".equals(requestTypeName)) {
                ps.setString(paramIndex++, requestTypeName);
            }
            if (startDate != null) {
                ps.setDate(paramIndex++, startDate);
            }
            if (endDate != null) {
                ps.setDate(paramIndex++, endDate);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    requests.add(mapResultSetToRequest(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in searchRequestsByEmployee: " + e.getMessage());
            e.printStackTrace();
        }

        return requests;
    }

    /**
     * Helper method to map ResultSet to Request object
     * @param rs ResultSet
     * @return Request object
     * @throws SQLException if database error occurs
     */
    private Request mapResultSetToRequest(ResultSet rs) throws SQLException {
        Request request = new Request();
        request.setRequestID(rs.getInt("request_id"));
        request.setEmployeeID(rs.getInt("employee_id"));
        request.setRequestTypeID(rs.getInt("request_type_id"));
        request.setStartDate(rs.getDate("start_date"));
        request.setEndDate(rs.getDate("end_date"));
        request.setNumberOfDays(rs.getBigDecimal("number_of_days"));
        request.setReason(rs.getString("reason"));
        request.setRequestStatus(rs.getString("request_status"));
        
        // Handle nullable reviewed_by
        int reviewedBy = rs.getInt("reviewed_by");
        if (!rs.wasNull()) {
            request.setReviewedBy(reviewedBy);
        }
        
        request.setReviewComment(rs.getString("review_comment"));
        request.setReviewedAt(rs.getTimestamp("reviewed_at"));
        request.setCancelledAt(rs.getTimestamp("cancelled_at"));
        request.setCreatedAt(rs.getTimestamp("created_at"));
        request.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Set display fields
        request.setEmployeeName(rs.getString("employee_name"));
        request.setEmployeeCode(rs.getString("employee_code"));
        request.setRequestTypeName(rs.getString("request_type_name"));
        request.setDepartmentName(rs.getString("department_name"));
        request.setReviewerName(rs.getString("reviewer_name"));

        return request;
    }

    /**
     * Calculate total approved days for a specific employee and request type in the current year
     * @param employeeID Employee ID
     * @param requestTypeID Request Type ID
     * @param excludeRequestID Request ID to exclude from calculation (for edit scenarios, pass 0 for new requests)
     * @return Total number of days (as BigDecimal)
     */
    public BigDecimal getTotalApprovedDaysInCurrentYear(int employeeID, int requestTypeID, int excludeRequestID) {
        String sql = "SELECT COALESCE(SUM(number_of_days), 0) AS total_days " +
                     "FROM requests " +
                     "WHERE employee_id = ? " +
                     "AND request_type_id = ? " +
                     "AND request_status = 'Approved' " +
                     "AND YEAR(start_date) = YEAR(CURDATE()) " +
                     "AND request_id != ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeID);
            ps.setInt(2, requestTypeID);
            ps.setInt(3, excludeRequestID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total_days");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getTotalApprovedDaysInCurrentYear: " + e.getMessage());
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

    /**
     * Calculate total pending days for a specific employee and request type in the current year
     * @param employeeID Employee ID
     * @param requestTypeID Request Type ID
     * @param excludeRequestID Request ID to exclude from calculation (for edit scenarios, pass 0 for new requests)
     * @return Total number of days (as BigDecimal)
     */
    public BigDecimal getTotalPendingDaysInCurrentYear(int employeeID, int requestTypeID, int excludeRequestID) {
        String sql = "SELECT COALESCE(SUM(number_of_days), 0) AS total_days " +
                     "FROM requests " +
                     "WHERE employee_id = ? " +
                     "AND request_type_id = ? " +
                     "AND request_status = 'Pending' " +
                     "AND YEAR(start_date) = YEAR(CURDATE()) " +
                     "AND request_id != ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeID);
            ps.setInt(2, requestTypeID);
            ps.setInt(3, excludeRequestID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("total_days");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getTotalPendingDaysInCurrentYear: " + e.getMessage());
            e.printStackTrace();
        }

        return BigDecimal.ZERO;
    }

    /**
     * Get approved leave request for a specific employee and date
     * Used during attendance import to check if employee has approved leave
     * @param employeeID Employee ID
     * @param attendanceDate Date to check
     * @return Request object if approved leave exists for that date, null otherwise
     */
    public Request getApprovedRequestForEmployeeAndDate(int employeeID, Date attendanceDate) {
        String sql = "SELECT r.request_id, r.employee_id, r.request_type_id, r.start_date, r.end_date, " +
                     "r.number_of_days, r.reason, r.request_status, r.reviewed_by, r.review_comment, " +
                     "r.reviewed_at, r.cancelled_at, r.created_at, r.updated_at, " +
                     "rt.request_type_name, rt.description, rt.requires_approval, rt.max_days_per_year, rt.is_paid, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS employee_name, " +
                     "e.employee_code, " +
                     "CONCAT(reviewer.first_name, ' ', reviewer.last_name) AS reviewer_name, " +
                     "d.department_name " +
                     "FROM requests r " +
                     "INNER JOIN request_types rt ON r.request_type_id = rt.request_type_id " +
                     "INNER JOIN employees e ON r.employee_id = e.employee_id " +
                     "LEFT JOIN departments d ON e.department_id = d.department_id " +
                     "LEFT JOIN users u ON r.reviewed_by = u.user_id " +
                     "LEFT JOIN employees reviewer ON u.user_id = reviewer.user_id " +
                     "WHERE r.employee_id = ? " +
                     "AND r.request_status = 'Approved' " +
                     "AND ? BETWEEN r.start_date AND r.end_date";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeID);
            ps.setDate(2, attendanceDate);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Request request = new Request();
                    request.setRequestID(rs.getInt("request_id"));
                    request.setEmployeeID(rs.getInt("employee_id"));
                    request.setRequestTypeID(rs.getInt("request_type_id"));
                    request.setStartDate(rs.getDate("start_date"));
                    request.setEndDate(rs.getDate("end_date"));
                    request.setNumberOfDays(rs.getBigDecimal("number_of_days"));
                    request.setReason(rs.getString("reason"));
                    request.setRequestStatus(rs.getString("request_status"));
                    request.setReviewedBy(rs.getInt("reviewed_by"));
                    request.setReviewComment(rs.getString("review_comment"));
                    request.setReviewedAt(rs.getTimestamp("reviewed_at"));
                    request.setCancelledAt(rs.getTimestamp("cancelled_at"));
                    request.setCreatedAt(rs.getTimestamp("created_at"));
                    request.setUpdatedAt(rs.getTimestamp("updated_at"));

                    // Set display fields
                    request.setRequestTypeName(rs.getString("request_type_name"));
                    request.setEmployeeName(rs.getString("employee_name"));
                    request.setEmployeeCode(rs.getString("employee_code"));
                    request.setReviewerName(rs.getString("reviewer_name"));
                    request.setDepartmentName(rs.getString("department_name"));

                    return request;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getApprovedRequestForEmployeeAndDate: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
}

