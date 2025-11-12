package dal;

import model.Contract;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Contract
 * @author admin
 */
public class ContractDAO extends DBContext {
    
    /**
     * Create a new contract
     * @param contract Contract object with data
     * @return boolean success
     */
    public boolean createContract(Contract contract) {
        String sql = "INSERT INTO employment_contracts (employee_id, contract_number, contract_type, " +
                    "start_date, end_date, salary_amount, job_description, " +
                    "contract_status, created_by, approval_comment) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, contract.getEmployeeID());
            ps.setString(2, contract.getContractNumber());
            ps.setString(3, contract.getContractType());
            ps.setDate(4, contract.getStartDate());
            ps.setDate(5, contract.getEndDate()); // Can be null for indefinite contracts
            ps.setBigDecimal(6, contract.getSalaryAmount());
            ps.setString(7, contract.getJobDescription());
            ps.setString(8, contract.getContractStatus());
            
            if (contract.getCreatedBy() != null) {
                ps.setInt(9, contract.getCreatedBy());
            } else {
                ps.setNull(9, java.sql.Types.INTEGER);
            }
            
            ps.setString(10, contract.getApprovalComment());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                // Get generated contract ID
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        contract.setContractID(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error in createContract: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Approve a contract by HR Manager
     * @param contractID Contract ID to approve
     * @param approvedBy User ID of the HR Manager
     * @param approvalComment Comment from HR Manager
     * @return boolean success
     */
    public boolean approveContract(int contractID, int approvedBy, String approvalComment) {
        String sql = "UPDATE employment_contracts SET " +
                    "contract_status = 'Active', " +
                    "approved_by = ?, " +
                    "approval_comment = ?, " +
                    "approved_at = CURRENT_TIMESTAMP " +
                    "WHERE contract_id = ? AND contract_status = 'Pending Approval'";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, approvedBy);
            ps.setString(2, approvalComment);
            ps.setInt(3, contractID);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error in approveContract: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Reject a contract by HR Manager
     * @param contractID Contract ID to reject
     * @param rejectedBy User ID of the HR Manager
     * @param rejectionComment Reason for rejection
     * @return boolean success
     */
    public boolean rejectContract(int contractID, int rejectedBy, String rejectionComment) {
        String sql = "UPDATE employment_contracts SET " +
                    "contract_status = 'Rejected', " +
                    "approved_by = ?, " +
                    "approval_comment = ?, " +
                    "approved_at = CURRENT_TIMESTAMP " +
                    "WHERE contract_id = ? AND contract_status = 'Pending Approval'";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, rejectedBy);
            ps.setString(2, rejectionComment);
            ps.setInt(3, contractID);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error in rejectContract: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get contracts pending approval for HR Manager
     * @return List<Contract> pending approval
     */
    public List<Contract> getPendingApprovalContracts() {
        List<Contract> contracts = new ArrayList<>();
        String sql = "SELECT c.contract_id, c.employee_id, c.contract_number, c.contract_type, " +
                     "c.start_date, c.end_date, c.salary_amount, c.job_description, " +
                     "c.contract_status, c.signed_date, " +
                     "c.approved_by, c.approval_comment, c.approved_at, " +
                     "c.created_by, c.created_at, c.updated_at, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS employee_full_name, " +
                     "e.employee_code, e.phone_number, e.personal_email, " +
                     "CONCAT(u.username) AS created_by_name " +
                     "FROM employment_contracts c " +
                     "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
                     "LEFT JOIN users u ON c.created_by = u.user_id " +
                     "WHERE c.contract_status = 'Pending Approval' AND c.is_deleted = FALSE " +
                     "ORDER BY c.created_at ASC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Contract contract = mapResultSetToContract(rs);
                contracts.add(contract);
            }
        } catch (SQLException e) {
            System.err.println("Error in getPendingApprovalContracts: " + e.getMessage());
            e.printStackTrace();
        }
        
        return contracts;
    }
    
    /**
     * Get contracts that need approval (Draft or Pending Approval status) with pagination
     * @param keyword Search keyword
     * @param page Current page
     * @param pageSize Records per page
     * @return List<Contract>
     */
    public List<Contract> getContractsForApproval(String keyword, int page, int pageSize) {
        List<Contract> contracts = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        
        StringBuilder sql = new StringBuilder(
            "SELECT c.contract_id, c.employee_id, c.contract_number, c.contract_type, " +
            "c.start_date, c.end_date, c.salary_amount, c.job_description, " +
            "c.contract_status, c.signed_date, " +
            "c.approved_by, c.approval_comment, c.approved_at, " +
            "c.created_by, c.created_at, c.updated_at, " +
            "CONCAT(e.first_name, ' ', e.last_name) AS employee_full_name, " +
            "e.employee_code, e.phone_number, e.personal_email " +
            "FROM employment_contracts c " +
            "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
            "WHERE (c.contract_status = 'Draft' OR c.contract_status = 'Pending Approval') " +
            "AND c.is_deleted = FALSE "
        );
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND ( " +
                "CONCAT(e.first_name, ' ', e.last_name) LIKE ? " +
                " OR c.contract_type LIKE ? " +
                " OR c.contract_number LIKE ? " +
                " OR e.employee_code LIKE ? " +
            ") ");
        }
        
        sql.append("ORDER BY c.created_at DESC ");
        sql.append("LIMIT ? OFFSET ?");
        
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                String likePattern = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
            }
            
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex++, offset);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Contract contract = mapResultSetToContract(rs);
                    contracts.add(contract);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getContractsForApproval: " + e.getMessage());
            e.printStackTrace();
        }
        
        return contracts;
    }
    
    /**
     * Count contracts that need approval (Draft or Pending Approval status)
     * @param keyword Search keyword
     * @return Total count
     */
    public int countContractsForApproval(String keyword) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) as total FROM employment_contracts c " +
            "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
            "WHERE (c.contract_status = 'Draft' OR c.contract_status = 'Pending Approval') " +
            "AND c.is_deleted = FALSE " +
            "AND c.is_deleted = FALSE "
        );
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND ( " +
                "CONCAT(e.first_name, ' ', e.last_name) LIKE ? " +
                " OR c.contract_type LIKE ? " +
                " OR c.contract_number LIKE ? " +
                " OR e.employee_code LIKE ? " +
            ") ");
        }
        
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                String likePattern = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in countContractsForApproval: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get contracts by status with pagination
     * @param status Contract status
     * @param keyword Search keyword
     * @param page Current page
     * @param pageSize Records per page
     * @return List<Contract>
     */
    public List<Contract> getContractsByStatus(String status, String keyword, int page, int pageSize) {
        List<Contract> contracts = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        
        StringBuilder sql = new StringBuilder(
            "SELECT c.contract_id, c.employee_id, c.contract_number, c.contract_type, " +
            "c.start_date, c.end_date, c.salary_amount, c.job_description, " +
            "c.contract_status, c.signed_date, " +
            "c.approved_by, c.approval_comment, c.approved_at, " +
            "c.created_by, c.created_at, c.updated_at, " +
            "CONCAT(e.first_name, ' ', e.last_name) AS employee_full_name, " +
            "e.employee_code, e.phone_number, e.personal_email " +
            "FROM employment_contracts c " +
            "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
            "WHERE c.contract_status = ? AND c.is_deleted = FALSE "
        );
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND ( " +
                "CONCAT(e.first_name, ' ', e.last_name) LIKE ? " +
                " OR c.contract_type LIKE ? " +
                " OR c.contract_number LIKE ? " +
                " OR e.employee_code LIKE ? " +
            ") ");
        }
        
        sql.append("ORDER BY c.created_at DESC ");
        sql.append("LIMIT ? OFFSET ?");
        
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            ps.setString(paramIndex++, status);
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                String likePattern = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
            }
            
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex++, offset);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Contract contract = mapResultSetToContract(rs);
                    contracts.add(contract);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getContractsByStatus: " + e.getMessage());
            e.printStackTrace();
        }
        
        return contracts;
    }
    
    /**
     * Count contracts by status
     * @param status Contract status
     * @param keyword Search keyword
     * @return Total count
     */
    public int countContractsByStatus(String status, String keyword) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) as total FROM employment_contracts c " +
            "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
            "WHERE c.contract_status = ? AND c.is_deleted = FALSE "
        );
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND ( " +
                "CONCAT(e.first_name, ' ', e.last_name) LIKE ? " +
                " OR c.contract_type LIKE ? " +
                " OR c.contract_number LIKE ? " +
                " OR e.employee_code LIKE ? " +
            ") ");
        }
        
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            ps.setString(paramIndex++, status);
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                String likePattern = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in countContractsByStatus: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get all contracts with employee information
     * @return List<Contract>
     */
    public List<Contract> getAllContracts() {
        List<Contract> contracts = new ArrayList<>();
        String sql = "SELECT c.contract_id, c.employee_id, c.contract_number, c.contract_type, " +
                     "c.start_date, c.end_date, c.salary_amount, c.job_description, " +
                     "c.contract_status, c.signed_date, " +
                     "c.approved_by, c.approval_comment, c.approved_at, " +
                     "c.created_by, c.created_at, c.updated_at, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS employee_full_name, " +
                     "e.employee_code, e.phone_number, e.personal_email " +
                     "FROM employment_contracts c " +
                     "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
                     "WHERE c.is_deleted = FALSE " +
                     "ORDER BY c.created_at DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Contract contract = mapResultSetToContract(rs);
                contracts.add(contract);
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllContracts: " + e.getMessage());
            e.printStackTrace();
        }
        
        return contracts;
    }
    
    /**
     * Get contracts with pagination and role-based filtering
     * @param page Current page number (starting from 1)
     * @param pageSize Number of records per page
     * @param userRole User role (HR, HR Manager, etc.)
     * @param userId Current user ID
     * @return List<Contract>
     */
    public List<Contract> getAllContracts(int page, int pageSize, String userRole, Integer userId) {
        List<Contract> contracts = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        StringBuilder sql = new StringBuilder(
            "SELECT c.contract_id, c.employee_id, c.contract_number, c.contract_type, " +
            "c.start_date, c.end_date, c.salary_amount, c.job_description, " +
            "c.contract_status, c.signed_date, " +
            "c.approved_by, c.approval_comment, c.approved_at, " +
            "c.created_by, c.created_at, c.updated_at, " +
            "CONCAT(e.first_name, ' ', e.last_name) AS employee_full_name, " +
            "e.employee_code, e.phone_number, e.personal_email " +
            "FROM employment_contracts c " +
            "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
            "WHERE c.is_deleted = FALSE "
        );

        // Filter draft contracts by creator for HR and HR Manager roles
        if ("HR".equals(userRole) || "HR Manager".equals(userRole)) {
            sql.append("AND (c.contract_status != 'Draft' OR c.created_by = ?) ");
        }

        sql.append("ORDER BY c.created_at DESC ");
        sql.append("LIMIT ? OFFSET ?");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            // Add user ID parameter if filtering by creator
            if ("HR".equals(userRole) || "HR Manager".equals(userRole)) {
                ps.setInt(paramIndex++, userId);
            }

            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex++, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Contract contract = mapResultSetToContract(rs);
                    contracts.add(contract);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllContracts with role filtering: " + e.getMessage());
            e.printStackTrace();
        }

        return contracts;
    }

    /**
     * Get contracts with pagination (backward compatibility)
     * @param page Current page number (starting from 1)
     * @param pageSize Number of records per page
     * @return List<Contract>
     */
    public List<Contract> getAllContracts(int page, int pageSize) {
        List<Contract> contracts = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        
        String sql = "SELECT c.contract_id, c.employee_id, c.contract_number, c.contract_type, " +
                     "c.start_date, c.end_date, c.salary_amount, c.job_description, " +
                     "c.contract_status, c.signed_date, " +
                     "c.approved_by, c.approval_comment, c.approved_at, " +
                     "c.created_by, c.created_at, c.updated_at, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS employee_full_name, " +
                     "e.employee_code, e.phone_number, e.personal_email " +
                     "FROM employment_contracts c " +
                     "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
                     "WHERE c.is_deleted = FALSE " +
                     "ORDER BY c.created_at DESC " +
                     "LIMIT ? OFFSET ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, offset);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Contract contract = mapResultSetToContract(rs);
                    contracts.add(contract);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllContracts with pagination: " + e.getMessage());
            e.printStackTrace();
        }
        
        return contracts;
    }
    
    /**
     * Get all contract statuses from the database
     * @return List<String> of all available contract statuses
     */
    public List<String> getAllContractStatuses() {
        List<String> statuses = new ArrayList<>();
        
        String sql = "SHOW COLUMNS FROM employment_contracts LIKE 'contract_status'";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                // Extract ENUM values from the Type column
                String typeInfo = rs.getString("Type");
                
                // Parse ENUM values - format is typically: enum('value1','value2',...)
                if (typeInfo != null && typeInfo.startsWith("enum(")) {
                    String valuesStr = typeInfo.substring(5, typeInfo.length() - 1);
                    String[] values = valuesStr.split(",");
                    
                    for (String value : values) {
                        // Remove quotes and add to list
                        String cleanValue = value.trim().replace("'", "");
                        statuses.add(cleanValue);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting contract statuses: " + e.getMessage());
            e.printStackTrace();
        }
        
        return statuses;
    }

    /**
     * Get total number of contracts with role-based filtering
     * @param userRole User role (HR, HR Manager, etc.)
     * @param userId Current user ID
     * @return Total count
     */
    public int getTotalContracts(String userRole, Integer userId) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) as total FROM employment_contracts WHERE is_deleted = FALSE ");

        // Filter draft contracts by creator for HR and HR Manager roles
        if ("HR".equals(userRole) || "HR Manager".equals(userRole)) {
            sql.append("AND (contract_status != 'Draft' OR created_by = ?) ");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            // Add user ID parameter if filtering by creator
            if ("HR".equals(userRole) || "HR Manager".equals(userRole)) {
                ps.setInt(paramIndex++, userId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getTotalContracts with role filtering: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Get total number of contracts (backward compatibility)
     * @return Total count
     */
    public int getTotalContracts() {
        String sql = "SELECT COUNT(*) as total FROM employment_contracts WHERE is_deleted = FALSE";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            System.err.println("Error in getTotalContracts: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }
    
    /**
     * Get contract by ID
     * @param contractID
     * @return Contract
     */
    public Contract getContractById(int contractID) {
        String sql = "SELECT c.contract_id, c.employee_id, c.contract_number, c.contract_type, " +
                     "c.start_date, c.end_date, c.salary_amount, c.job_description, " +
                     "c.contract_status, c.signed_date, " +
                     "c.approved_by, c.approval_comment, c.approved_at, " +
                     "c.created_by, c.created_at, c.updated_at, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS employee_full_name, " +
                     "e.employee_code, e.phone_number, e.personal_email " +
                     "FROM employment_contracts c " +
                     "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
                     "WHERE c.contract_id = ? AND c.is_deleted = FALSE";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, contractID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToContract(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getContractById: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get contracts by Employee ID
     * @param employeeID
     * @return List<Contract>
     */
    public List<Contract> getContractsByEmployeeId(int employeeID) {
        List<Contract> contracts = new ArrayList<>();
        String sql = "SELECT c.contract_id, c.employee_id, c.contract_number, c.contract_type, " +
                     "c.start_date, c.end_date, c.salary_amount, c.job_description, " +
                     "c.contract_status, c.signed_date, " +
                     "c.approved_by, c.approval_comment, c.approved_at, " +
                     "c.created_by, c.created_at, c.updated_at, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS employee_full_name, " +
                     "e.employee_code, e.phone_number, e.personal_email " +
                     "FROM employment_contracts c " +
                     "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
                     "WHERE c.employee_id = ? AND c.is_deleted = FALSE " +
                     "ORDER BY c.start_date DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeID);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Contract contract = mapResultSetToContract(rs);
                    contracts.add(contract);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getContractsByEmployeeId: " + e.getMessage());
            e.printStackTrace();
        }
        
        return contracts;
    }
    
    /**
     * Search contracts by multiple criteria
     * @param keyword Search keyword (employee name or contract type)
     * @param status Contract status
     * @return List<Contract>
     */
    public List<Contract> searchContracts(String keyword, String status) {
        List<Contract> contracts = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
            "SELECT c.contract_id, c.employee_id, c.contract_number, c.contract_type, " +
            "c.start_date, c.end_date, c.salary_amount, c.job_description, " +
            "c.contract_status, c.signed_date, " +
            "c.approved_by, c.approval_comment, c.approved_at, " +
            "c.created_by, c.created_at, c.updated_at, " +
            "CONCAT(e.first_name, ' ', e.last_name) AS employee_full_name, " +
            "e.employee_code, e.phone_number, e.personal_email " +
            "FROM employment_contracts c " +
            "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
            "WHERE c.is_deleted = FALSE "
        );
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND ( " +
                "MATCH(e.first_name, e.last_name, e.employee_code) AGAINST (? IN BOOLEAN MODE) " +
                " OR MATCH(c.contract_number, c.job_description) AGAINST (? IN BOOLEAN MODE) " +
                " OR CONCAT(e.first_name, ' ', e.last_name) LIKE ? " +
                " OR c.contract_type LIKE ? " +
                " OR c.contract_number LIKE ? " +
                " OR e.employee_code LIKE ? " +
            ") ");
        }
        
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND c.contract_status = ? ");
        }
        
        sql.append("ORDER BY c.created_at DESC");
        
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                String booleanPattern = "+" + keyword.trim() + "*";
                String likePattern = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, booleanPattern);
                ps.setString(paramIndex++, booleanPattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
            }
            
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(paramIndex++, status);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Contract contract = mapResultSetToContract(rs);
                    contracts.add(contract);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in searchContracts: " + e.getMessage());
            e.printStackTrace();
        }
        
        return contracts;
    }
    
    /**
     * Search contracts with pagination and role-based filtering
     * @param keyword Search keyword
     * @param status Contract status
     * @param page Current page
     * @param pageSize Records per page
     * @param userRole User role (HR, HR Manager, etc.)
     * @param userId Current user ID
     * @return List<Contract>
     */
    public List<Contract> searchContracts(String keyword, String status, int page, int pageSize, String userRole, Integer userId) {
        List<Contract> contracts = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        StringBuilder sql = new StringBuilder(
            "SELECT c.contract_id, c.employee_id, c.contract_number, c.contract_type, " +
            "c.start_date, c.end_date, c.salary_amount, c.job_description, " +
            "c.contract_status, c.signed_date, " +
            "c.approved_by, c.approval_comment, c.approved_at, " +
            "c.created_by, c.created_at, c.updated_at, " +
            "CONCAT(e.first_name, ' ', e.last_name) AS employee_full_name, " +
            "e.employee_code, e.phone_number, e.personal_email " +
            "FROM employment_contracts c " +
            "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
            "WHERE c.is_deleted = FALSE "
        );

        // Filter draft contracts by creator for HR and HR Manager roles
        if ("HR".equals(userRole) || "HR Manager".equals(userRole)) {
            sql.append("AND (c.contract_status != 'Draft' OR c.created_by = ?) ");
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND ( " +
                "MATCH(e.first_name, e.last_name, e.employee_code) AGAINST (? IN BOOLEAN MODE) " +
                " OR MATCH(c.contract_number, c.job_description) AGAINST (? IN BOOLEAN MODE) " +
                " OR CONCAT(e.first_name, ' ', e.last_name) LIKE ? " +
                " OR c.contract_type LIKE ? " +
                " OR c.contract_number LIKE ? " +
                " OR e.employee_code LIKE ? " +
            ") ");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND c.contract_status = ? ");
        }

        sql.append("ORDER BY c.created_at DESC ");
        sql.append("LIMIT ? OFFSET ?");

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            // Add user ID parameter if filtering by creator
            if ("HR".equals(userRole) || "HR Manager".equals(userRole)) {
                ps.setInt(paramIndex++, userId);
            }

            if (keyword != null && !keyword.trim().isEmpty()) {
                String booleanPattern = "+" + keyword.trim() + "*";
                String likePattern = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, booleanPattern);
                ps.setString(paramIndex++, booleanPattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
            }

            if (status != null && !status.trim().isEmpty()) {
                ps.setString(paramIndex++, status);
            }

            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex++, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Contract contract = mapResultSetToContract(rs);
                    contracts.add(contract);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in searchContracts with role filtering: " + e.getMessage());
            e.printStackTrace();
        }

        return contracts;
    }

    /**
     * Search contracts with pagination (backward compatibility)
     * @param keyword Search keyword
     * @param status Contract status
     * @param page Current page
     * @param pageSize Records per page
     * @return List<Contract>
     */
    public List<Contract> searchContracts(String keyword, String status, int page, int pageSize) {
        List<Contract> contracts = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        
        StringBuilder sql = new StringBuilder(
            "SELECT c.contract_id, c.employee_id, c.contract_number, c.contract_type, " +
            "c.start_date, c.end_date, c.salary_amount, c.job_description, " +
            "c.contract_status, c.signed_date, " +
            "c.approved_by, c.approval_comment, c.approved_at, " +
            "c.created_by, c.created_at, c.updated_at, " +
            "CONCAT(e.first_name, ' ', e.last_name) AS employee_full_name, " +
            "e.employee_code, e.phone_number, e.personal_email " +
            "FROM employment_contracts c " +
            "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
            "WHERE c.is_deleted = FALSE "
        );
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND ( " +
                "MATCH(e.first_name, e.last_name, e.employee_code) AGAINST (? IN BOOLEAN MODE) " +
                " OR MATCH(c.contract_number, c.job_description) AGAINST (? IN BOOLEAN MODE) " +
                " OR CONCAT(e.first_name, ' ', e.last_name) LIKE ? " +
                " OR c.contract_type LIKE ? " +
                " OR c.contract_number LIKE ? " +
                " OR e.employee_code LIKE ? " +
            ") ");
        }
        
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND c.contract_status = ? ");
        }
        
        sql.append("ORDER BY c.created_at DESC ");
        sql.append("LIMIT ? OFFSET ?");
        
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                String booleanPattern = "+" + keyword.trim() + "*";
                String likePattern = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, booleanPattern);
                ps.setString(paramIndex++, booleanPattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
            }
            
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(paramIndex++, status);
            }
            
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex++, offset);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Contract contract = mapResultSetToContract(rs);
                    contracts.add(contract);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in searchContracts with pagination: " + e.getMessage());
            e.printStackTrace();
        }
        
        return contracts;
    }
    
    /**
     * Get total count of search results with role-based filtering
     * @param keyword Search keyword
     * @param status Contract status
     * @param userRole User role (HR, HR Manager, etc.)
     * @param userId Current user ID
     * @return Total count
     */
    public int getTotalSearchResults(String keyword, String status, String userRole, Integer userId) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) as total FROM employment_contracts c " +
            "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
            "WHERE c.is_deleted = FALSE "
        );

        // Filter draft contracts by creator for HR and HR Manager roles
        if ("HR".equals(userRole) || "HR Manager".equals(userRole)) {
            sql.append("AND (c.contract_status != 'Draft' OR c.created_by = ?) ");
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND ( " +
                "MATCH(e.first_name, e.last_name, c.contract_type, e.employee_code, c.job_description) AGAINST (? IN BOOLEAN MODE) " +
                " OR CONCAT(e.first_name, ' ', e.last_name) LIKE ? " +
                " OR c.contract_type LIKE ? " +
                " OR e.employee_code LIKE ? " +
            ") ");
        }

        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND c.contract_status = ? ");
        }

        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;

            // Add user ID parameter if filtering by creator
            if ("HR".equals(userRole) || "HR Manager".equals(userRole)) {
                ps.setInt(paramIndex++, userId);
            }

            if (keyword != null && !keyword.trim().isEmpty()) {
                ps.setString(paramIndex++, "+" + keyword.trim() + "*");
                String likePattern = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
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
            System.err.println("Error in getTotalSearchResults with role filtering: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Get total count of search results (backward compatibility)
     * @param keyword Search keyword
     * @param status Contract status
     * @return Total count
     */
    public int getTotalSearchResults(String keyword, String status) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) as total FROM employment_contracts c " +
            "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
            "WHERE c.is_deleted = FALSE "
        );
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND ( " +
                "MATCH(e.first_name, e.last_name, c.contract_type, e.employee_code, c.job_description) AGAINST (? IN BOOLEAN MODE) " +
                " OR CONCAT(e.first_name, ' ', e.last_name) LIKE ? " +
                " OR c.contract_type LIKE ? " +
                " OR e.employee_code LIKE ? " +
            ") ");
        }
        
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND c.contract_status = ? ");
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
     * Delete a contract by ID (soft delete)
     * @param contractID Contract ID to delete
     * @return boolean success
     */
    public boolean deleteContract(int contractID) {
        String sql = "UPDATE employment_contracts SET is_deleted = TRUE WHERE contract_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, contractID);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error in deleteContract: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Update an existing contract. 
     * - For Draft contracts: only creator can edit
     * - For other statuses: HR and HR Manager can edit
     * @param contract Contract object with updated data (must include contractID and createdBy)
     * @return boolean success
     */
    public boolean updateContract(Contract contract) {
        // First check if contract exists and get its current status
        Contract existingContract = getContractById(contract.getContractID());
        if (existingContract == null) {
            System.out.println("Contract not found: " + contract.getContractID());
            return false;
        }
        
        // Check permissions based on contract status
        if ("Draft".equals(existingContract.getContractStatus())) {
            // For Draft contracts, only creator can edit
            if (!existingContract.getCreatedBy().equals(contract.getCreatedBy())) {
                System.out.println("Draft contract can only be edited by creator. Contract created by: " + 
                    existingContract.getCreatedBy() + ", Current user: " + contract.getCreatedBy());
                return false;
            }
        }
        // For non-Draft contracts, HR and HR Manager can edit (no additional check needed)
        
        String sql = "UPDATE employment_contracts SET " +
                "employee_id = ?, " +
                "contract_number = ?, " +
                "contract_type = ?, " +
                "start_date = ?, " +
                "end_date = ?, " +
                "salary_amount = ?, " +
                "job_description = ?, " +
                "contract_status = ?, " +
                "approval_comment = ?, " +
                "updated_at = CURRENT_TIMESTAMP " +
                "WHERE contract_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, contract.getEmployeeID());
            ps.setString(2, contract.getContractNumber());
            ps.setString(3, contract.getContractType());
            ps.setDate(4, contract.getStartDate());
            ps.setDate(5, contract.getEndDate());
            ps.setBigDecimal(6, contract.getSalaryAmount());
            ps.setString(7, contract.getJobDescription());
            ps.setString(8, contract.getContractStatus());
            ps.setString(9, contract.getApprovalComment());
            ps.setInt(10, contract.getContractID());

            int affectedRows = ps.executeUpdate();
            System.out.println("Update contract affected rows: " + affectedRows + " for contract ID: " + contract.getContractID() + 
                " with status: " + existingContract.getContractStatus());
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error in updateContract: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
    
    /**
     * Helper method to map ResultSet to Contract object
     * @param rs ResultSet from query
     * @return Contract object
     * @throws SQLException if error occurs
     */
    private Contract mapResultSetToContract(ResultSet rs) throws SQLException {
        Contract contract = new Contract();
        
        // Basic contract fields
        contract.setContractID(rs.getInt("contract_id"));
        contract.setEmployeeID(rs.getInt("employee_id"));
        contract.setContractNumber(rs.getString("contract_number"));
        contract.setContractType(rs.getString("contract_type"));
        contract.setStartDate(rs.getDate("start_date"));
        contract.setEndDate(rs.getDate("end_date"));
        contract.setSalaryAmount(rs.getBigDecimal("salary_amount"));
        contract.setJobDescription(rs.getString("job_description"));
        contract.setContractStatus(rs.getString("contract_status"));
        contract.setSignedDate(rs.getDate("signed_date"));
        
        // Approval fields
        contract.setApprovedBy((Integer) rs.getObject("approved_by"));
        contract.setApprovalComment(rs.getString("approval_comment"));
        contract.setApprovedAt(rs.getTimestamp("approved_at"));
        
        // Audit fields
        contract.setCreatedBy((Integer) rs.getObject("created_by"));
        contract.setCreatedAt(rs.getTimestamp("created_at"));
        contract.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Employee information from JOIN
        contract.setEmployeeFullName(rs.getString("employee_full_name"));
        contract.setEmployeeCode(rs.getString("employee_code"));
        contract.setEmployeePhone(rs.getString("phone_number"));
        contract.setEmployeeEmail(rs.getString("personal_email"));
        
        return contract;
    }
}
