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
        String sql = "SELECT c.ContractID, c.EmployeeID, c.ContractType, c.StartDate, c.EndDate, c.Status, " +
                     "CONCAT(e.FirstName, ' ', e.LastName) AS EmployeeFullName, e.ContactInfo " +
                     "FROM Contract c " +
                     "LEFT JOIN Employee e ON c.EmployeeID = e.EmployeeID";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Contract contract = new Contract();
                contract.setContractID(rs.getInt("ContractID"));
                contract.setEmployeeID(rs.getInt("EmployeeID"));
                contract.setContractType(rs.getString("ContractType"));
                contract.setStartDate(rs.getDate("StartDate"));
                contract.setEndDate(rs.getDate("EndDate"));
                contract.setStatus(rs.getString("Status"));
                contract.setEmployeeFullName(rs.getString("EmployeeFullName"));
                contract.setEmployeeContactInfo(rs.getString("ContactInfo"));
                
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
        
        String sql = "SELECT c.ContractID, c.EmployeeID, c.ContractType, c.StartDate, c.EndDate, c.Status, " +
                     "CONCAT(e.FirstName, ' ', e.LastName) AS EmployeeFullName, e.ContactInfo " +
                     "FROM Contract c " +
                     "LEFT JOIN Employee e ON c.EmployeeID = e.EmployeeID " +
                     "LIMIT ? OFFSET ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, offset);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Contract contract = new Contract();
                    contract.setContractID(rs.getInt("ContractID"));
                    contract.setEmployeeID(rs.getInt("EmployeeID"));
                    contract.setContractType(rs.getString("ContractType"));
                    contract.setStartDate(rs.getDate("StartDate"));
                    contract.setEndDate(rs.getDate("EndDate"));
                    contract.setStatus(rs.getString("Status"));
                    contract.setEmployeeFullName(rs.getString("EmployeeFullName"));
                    contract.setEmployeeContactInfo(rs.getString("ContactInfo"));
                    
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
        String sql = "SELECT COUNT(*) as total FROM Contract";
        
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
        String sql = "SELECT c.ContractID, c.EmployeeID, c.ContractType, c.StartDate, c.EndDate, c.Status, " +
                     "CONCAT(e.FirstName, ' ', e.LastName) AS EmployeeFullName, e.ContactInfo " +
                     "FROM Contract c " +
                     "LEFT JOIN Employee e ON c.EmployeeID = e.EmployeeID " +
                     "WHERE c.ContractID = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, contractID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Contract contract = new Contract();
                    contract.setContractID(rs.getInt("ContractID"));
                    contract.setEmployeeID(rs.getInt("EmployeeID"));
                    contract.setContractType(rs.getString("ContractType"));
                    contract.setStartDate(rs.getDate("StartDate"));
                    contract.setEndDate(rs.getDate("EndDate"));
                    contract.setStatus(rs.getString("Status"));
                    contract.setEmployeeFullName(rs.getString("EmployeeFullName"));
                    contract.setEmployeeContactInfo(rs.getString("ContactInfo"));
                    
                    return contract;
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
        String sql = "SELECT c.ContractID, c.EmployeeID, c.ContractType, c.StartDate, c.EndDate, c.Status, " +
                     "CONCAT(e.FirstName, ' ', e.LastName) AS EmployeeFullName, e.ContactInfo " +
                     "FROM Contract c " +
                     "LEFT JOIN Employee e ON c.EmployeeID = e.EmployeeID " +
                     "WHERE c.EmployeeID = ? " +
                     "ORDER BY c.StartDate DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, employeeID);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Contract contract = new Contract();
                    contract.setContractID(rs.getInt("ContractID"));
                    contract.setEmployeeID(rs.getInt("EmployeeID"));
                    contract.setContractType(rs.getString("ContractType"));
                    contract.setStartDate(rs.getDate("StartDate"));
                    contract.setEndDate(rs.getDate("EndDate"));
                    contract.setStatus(rs.getString("Status"));
                    contract.setEmployeeFullName(rs.getString("EmployeeFullName"));
                    contract.setEmployeeContactInfo(rs.getString("ContactInfo"));
                    
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
            "SELECT c.ContractID, c.EmployeeID, c.ContractType, c.StartDate, c.EndDate, c.Status, " +
            "CONCAT(e.FirstName, ' ', e.LastName) AS EmployeeFullName, e.ContactInfo " +
            "FROM Contract c " +
            "LEFT JOIN Employee e ON c.EmployeeID = e.EmployeeID " +
            "WHERE 1=1 "
        );
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (CONCAT(e.FirstName, ' ', e.LastName) LIKE ? OR c.ContractType LIKE ?) ");
        }
        
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND c.Status = ? ");
        }
        
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchPattern = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
            }
            
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(paramIndex++, status);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Contract contract = new Contract();
                    contract.setContractID(rs.getInt("ContractID"));
                    contract.setEmployeeID(rs.getInt("EmployeeID"));
                    contract.setContractType(rs.getString("ContractType"));
                    contract.setStartDate(rs.getDate("StartDate"));
                    contract.setEndDate(rs.getDate("EndDate"));
                    contract.setStatus(rs.getString("Status"));
                    contract.setEmployeeFullName(rs.getString("EmployeeFullName"));
                    contract.setEmployeeContactInfo(rs.getString("ContactInfo"));
                    
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
            "SELECT c.ContractID, c.EmployeeID, c.ContractType, c.StartDate, c.EndDate, c.Status, " +
            "CONCAT(e.FirstName, ' ', e.LastName) AS EmployeeFullName, e.ContactInfo " +
            "FROM Contract c " +
            "LEFT JOIN Employee e ON c.EmployeeID = e.EmployeeID " +
            "WHERE 1=1 "
        );
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (CONCAT(e.FirstName, ' ', e.LastName) LIKE ? OR c.ContractType LIKE ?) ");
        }
        
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND c.Status = ? ");
        }
        
        sql.append("LIMIT ? OFFSET ?");
        
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchPattern = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
            }
            
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(paramIndex++, status);
            }
            
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex++, offset);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Contract contract = new Contract();
                    contract.setContractID(rs.getInt("ContractID"));
                    contract.setEmployeeID(rs.getInt("EmployeeID"));
                    contract.setContractType(rs.getString("ContractType"));
                    contract.setStartDate(rs.getDate("StartDate"));
                    contract.setEndDate(rs.getDate("EndDate"));
                    contract.setStatus(rs.getString("Status"));
                    contract.setEmployeeFullName(rs.getString("EmployeeFullName"));
                    contract.setEmployeeContactInfo(rs.getString("ContactInfo"));
                    
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
            "SELECT COUNT(*) as total FROM Contract c " +
            "LEFT JOIN Employee e ON c.EmployeeID = e.EmployeeID " +
            "WHERE 1=1 "
        );
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (CONCAT(e.FirstName, ' ', e.LastName) LIKE ? OR c.ContractType LIKE ?) ");
        }
        
        if (status != null && !status.trim().isEmpty()) {
            sql.append("AND c.Status = ? ");
        }
        
        try (PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchPattern = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, searchPattern);
                ps.setString(paramIndex++, searchPattern);
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
}
