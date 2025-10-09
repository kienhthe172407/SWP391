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
     * Get all contracts with employee information
     * @return List<Contract>
     */
    public List<Contract> getAllContracts() {
        List<Contract> contracts = new ArrayList<>();
        String sql = "SELECT c.contract_id, c.employee_id, c.contract_number, c.contract_type, " +
                     "c.start_date, c.end_date, c.salary_amount, c.job_description, " +
                     "c.terms_and_conditions, c.contract_status, c.signed_date, " +
                     "c.approved_by, c.approval_comment, c.approved_at, " +
                     "c.created_by, c.created_at, c.updated_at, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS employee_full_name, " +
                     "e.employee_code, e.phone_number, e.personal_email " +
                     "FROM employment_contracts c " +
                     "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
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
     * Get contracts with pagination
     * @param page Current page number (starting from 1)
     * @param pageSize Number of records per page
     * @return List<Contract>
     */
    public List<Contract> getAllContracts(int page, int pageSize) {
        List<Contract> contracts = new ArrayList<>();
        int offset = (page - 1) * pageSize;
        
        String sql = "SELECT c.contract_id, c.employee_id, c.contract_number, c.contract_type, " +
                     "c.start_date, c.end_date, c.salary_amount, c.job_description, " +
                     "c.terms_and_conditions, c.contract_status, c.signed_date, " +
                     "c.approved_by, c.approval_comment, c.approved_at, " +
                     "c.created_by, c.created_at, c.updated_at, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS employee_full_name, " +
                     "e.employee_code, e.phone_number, e.personal_email " +
                     "FROM employment_contracts c " +
                     "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
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
     * Get total number of contracts
     * @return Total count
     */
    public int getTotalContracts() {
        String sql = "SELECT COUNT(*) as total FROM employment_contracts";
        
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
                     "c.terms_and_conditions, c.contract_status, c.signed_date, " +
                     "c.approved_by, c.approval_comment, c.approved_at, " +
                     "c.created_by, c.created_at, c.updated_at, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS employee_full_name, " +
                     "e.employee_code, e.phone_number, e.personal_email " +
                     "FROM employment_contracts c " +
                     "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
                     "WHERE c.contract_id = ?";
        
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
                     "c.terms_and_conditions, c.contract_status, c.signed_date, " +
                     "c.approved_by, c.approval_comment, c.approved_at, " +
                     "c.created_by, c.created_at, c.updated_at, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS employee_full_name, " +
                     "e.employee_code, e.phone_number, e.personal_email " +
                     "FROM employment_contracts c " +
                     "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
                     "WHERE c.employee_id = ? " +
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
            "c.terms_and_conditions, c.contract_status, c.signed_date, " +
            "c.approved_by, c.approval_comment, c.approved_at, " +
            "c.created_by, c.created_at, c.updated_at, " +
            "CONCAT(e.first_name, ' ', e.last_name) AS employee_full_name, " +
            "e.employee_code, e.phone_number, e.personal_email " +
            "FROM employment_contracts c " +
            "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
            "WHERE 1=1 "
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
     * Search contracts with pagination
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
            "c.terms_and_conditions, c.contract_status, c.signed_date, " +
            "c.approved_by, c.approval_comment, c.approved_at, " +
            "c.created_by, c.created_at, c.updated_at, " +
            "CONCAT(e.first_name, ' ', e.last_name) AS employee_full_name, " +
            "e.employee_code, e.phone_number, e.personal_email " +
            "FROM employment_contracts c " +
            "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
            "WHERE 1=1 "
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
     * Get total count of search results
     * @param keyword Search keyword
     * @param status Contract status
     * @return Total count
     */
    public int getTotalSearchResults(String keyword, String status) {
        StringBuilder sql = new StringBuilder(
            "SELECT COUNT(*) as total FROM employment_contracts c " +
            "LEFT JOIN employees e ON c.employee_id = e.employee_id " +
            "WHERE 1=1 "
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
        contract.setTermsAndConditions(rs.getString("terms_and_conditions"));
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
