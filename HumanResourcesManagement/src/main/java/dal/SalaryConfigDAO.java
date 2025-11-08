package dal;

import model.BenefitType;
import model.DeductionType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Salary Configuration (Benefits and Deductions)
 * Handles CRUD operations for benefit_types and deduction_types tables
 * @author admin
 */
public class SalaryConfigDAO extends DBContext {
    
    // ==================== BENEFIT TYPES METHODS ====================
    
    /**
     * Get all benefit types
     * @return List of all benefit types
     */
    public List<BenefitType> getAllBenefitTypes() {
        List<BenefitType> benefits = new ArrayList<>();
        String sql = "SELECT benefit_type_id, benefit_name, description, calculation_type, " +
                     "default_amount, default_percentage, is_taxable, created_at, updated_at " +
                     "FROM benefit_types " +
                     "ORDER BY benefit_name";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                BenefitType benefit = mapResultSetToBenefitType(rs);
                benefits.add(benefit);
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllBenefitTypes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return benefits;
    }
    
    /**
     * Get benefit type by ID
     * @param benefitTypeID Benefit type ID
     * @return BenefitType object or null if not found
     */
    public BenefitType getBenefitTypeById(int benefitTypeID) {
        String sql = "SELECT benefit_type_id, benefit_name, description, calculation_type, " +
                     "default_amount, default_percentage, is_taxable, created_at, updated_at " +
                     "FROM benefit_types " +
                     "WHERE benefit_type_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, benefitTypeID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToBenefitType(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getBenefitTypeById: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Add new benefit type
     * @param benefit BenefitType object to add
     * @return true if successful, false otherwise
     */
    public boolean addBenefitType(BenefitType benefit) {
        String sql = "INSERT INTO benefit_types (benefit_name, description, calculation_type, " +
                     "default_amount, default_percentage, is_taxable) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, benefit.getBenefitName());
            ps.setString(2, benefit.getDescription());
            ps.setString(3, benefit.getCalculationType());
            
            if (benefit.getDefaultAmount() != null) {
                ps.setDouble(4, benefit.getDefaultAmount());
            } else {
                ps.setNull(4, Types.DECIMAL);
            }
            
            if (benefit.getDefaultPercentage() != null) {
                ps.setDouble(5, benefit.getDefaultPercentage());
            } else {
                ps.setNull(5, Types.DECIMAL);
            }
            
            ps.setBoolean(6, benefit.isTaxable());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in addBenefitType: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Update existing benefit type
     * @param benefit BenefitType object with updated data
     * @return true if successful, false otherwise
     */
    public boolean updateBenefitType(BenefitType benefit) {
        String sql = "UPDATE benefit_types SET benefit_name = ?, description = ?, " +
                     "calculation_type = ?, default_amount = ?, default_percentage = ?, " +
                     "is_taxable = ? WHERE benefit_type_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, benefit.getBenefitName());
            ps.setString(2, benefit.getDescription());
            ps.setString(3, benefit.getCalculationType());
            
            if (benefit.getDefaultAmount() != null) {
                ps.setDouble(4, benefit.getDefaultAmount());
            } else {
                ps.setNull(4, Types.DECIMAL);
            }
            
            if (benefit.getDefaultPercentage() != null) {
                ps.setDouble(5, benefit.getDefaultPercentage());
            } else {
                ps.setNull(5, Types.DECIMAL);
            }
            
            ps.setBoolean(6, benefit.isTaxable());
            ps.setInt(7, benefit.getBenefitTypeID());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in updateBenefitType: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Delete benefit type by ID
     * @param benefitTypeID Benefit type ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteBenefitType(int benefitTypeID) {
        String sql = "UPDATE benefit_types SET is_deleted = TRUE WHERE benefit_type_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, benefitTypeID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in deleteBenefitType: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Check if benefit name already exists (for validation)
     * @param benefitName Benefit name to check
     * @param excludeID ID to exclude from check (for updates), null for new records
     * @return true if name exists, false otherwise
     */
    public boolean benefitNameExists(String benefitName, Integer excludeID) {
        String sql = "SELECT COUNT(*) FROM benefit_types WHERE benefit_name = ?";
        if (excludeID != null) {
            sql += " AND benefit_type_id != ?";
        }
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, benefitName);
            if (excludeID != null) {
                ps.setInt(2, excludeID);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in benefitNameExists: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    // ==================== DEDUCTION TYPES METHODS ====================
    
    /**
     * Get all deduction types
     * @return List of all deduction types
     */
    public List<DeductionType> getAllDeductionTypes() {
        List<DeductionType> deductions = new ArrayList<>();
        String sql = "SELECT deduction_type_id, deduction_name, description, calculation_type, " +
                     "default_amount, default_percentage, is_mandatory, created_at, updated_at " +
                     "FROM deduction_types " +
                     "ORDER BY deduction_name";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                DeductionType deduction = mapResultSetToDeductionType(rs);
                deductions.add(deduction);
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllDeductionTypes: " + e.getMessage());
            e.printStackTrace();
        }
        
        return deductions;
    }
    
    /**
     * Get deduction type by ID
     * @param deductionTypeID Deduction type ID
     * @return DeductionType object or null if not found
     */
    public DeductionType getDeductionTypeById(int deductionTypeID) {
        String sql = "SELECT deduction_type_id, deduction_name, description, calculation_type, " +
                     "default_amount, default_percentage, is_mandatory, created_at, updated_at " +
                     "FROM deduction_types " +
                     "WHERE deduction_type_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, deductionTypeID);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToDeductionType(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getDeductionTypeById: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Add new deduction type
     * @param deduction DeductionType object to add
     * @return true if successful, false otherwise
     */
    public boolean addDeductionType(DeductionType deduction) {
        String sql = "INSERT INTO deduction_types (deduction_name, description, calculation_type, " +
                     "default_amount, default_percentage, is_mandatory) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, deduction.getDeductionName());
            ps.setString(2, deduction.getDescription());
            ps.setString(3, deduction.getCalculationType());
            
            if (deduction.getDefaultAmount() != null) {
                ps.setDouble(4, deduction.getDefaultAmount());
            } else {
                ps.setNull(4, Types.DECIMAL);
            }
            
            if (deduction.getDefaultPercentage() != null) {
                ps.setDouble(5, deduction.getDefaultPercentage());
            } else {
                ps.setNull(5, Types.DECIMAL);
            }
            
            ps.setBoolean(6, deduction.isMandatory());
            
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in addDeductionType: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Update existing deduction type
     * @param deduction DeductionType object with updated data
     * @return true if successful, false otherwise
     */
    public boolean updateDeductionType(DeductionType deduction) {
        String sql = "UPDATE deduction_types SET deduction_name = ?, description = ?, " +
                     "calculation_type = ?, default_amount = ?, default_percentage = ?, " +
                     "is_mandatory = ? WHERE deduction_type_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, deduction.getDeductionName());
            ps.setString(2, deduction.getDescription());
            ps.setString(3, deduction.getCalculationType());

            if (deduction.getDefaultAmount() != null) {
                ps.setDouble(4, deduction.getDefaultAmount());
            } else {
                ps.setNull(4, Types.DECIMAL);
            }

            if (deduction.getDefaultPercentage() != null) {
                ps.setDouble(5, deduction.getDefaultPercentage());
            } else {
                ps.setNull(5, Types.DECIMAL);
            }

            ps.setBoolean(6, deduction.isMandatory());
            ps.setInt(7, deduction.getDeductionTypeID());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error in updateDeductionType: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete deduction type by ID
     * @param deductionTypeID Deduction type ID to delete
     * @return true if successful, false otherwise
     */
    public boolean deleteDeductionType(int deductionTypeID) {
        String sql = "UPDATE deduction_types SET is_deleted = TRUE WHERE deduction_type_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, deductionTypeID);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error in deleteDeductionType: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if deduction name already exists (for validation)
     * @param deductionName Deduction name to check
     * @param excludeID ID to exclude from check (for updates), null for new records
     * @return true if name exists, false otherwise
     */
    public boolean deductionNameExists(String deductionName, Integer excludeID) {
        String sql = "SELECT COUNT(*) FROM deduction_types WHERE deduction_name = ?";
        if (excludeID != null) {
            sql += " AND deduction_type_id != ?";
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, deductionName);
            if (excludeID != null) {
                ps.setInt(2, excludeID);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in deductionNameExists: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    // ==================== HELPER METHODS ====================

    /**
     * Map ResultSet to BenefitType object
     * @param rs ResultSet from query
     * @return BenefitType object
     * @throws SQLException if error occurs
     */
    private BenefitType mapResultSetToBenefitType(ResultSet rs) throws SQLException {
        BenefitType benefit = new BenefitType();
        benefit.setBenefitTypeID(rs.getInt("benefit_type_id"));
        benefit.setBenefitName(rs.getString("benefit_name"));
        benefit.setDescription(rs.getString("description"));
        benefit.setCalculationType(rs.getString("calculation_type"));

        // Handle nullable fields
        double defaultAmount = rs.getDouble("default_amount");
        benefit.setDefaultAmount(rs.wasNull() ? null : defaultAmount);

        double defaultPercentage = rs.getDouble("default_percentage");
        benefit.setDefaultPercentage(rs.wasNull() ? null : defaultPercentage);

        benefit.setTaxable(rs.getBoolean("is_taxable"));
        benefit.setCreatedAt(rs.getTimestamp("created_at"));
        benefit.setUpdatedAt(rs.getTimestamp("updated_at"));

        return benefit;
    }

    /**
     * Map ResultSet to DeductionType object
     * @param rs ResultSet from query
     * @return DeductionType object
     * @throws SQLException if error occurs
     */
    private DeductionType mapResultSetToDeductionType(ResultSet rs) throws SQLException {
        DeductionType deduction = new DeductionType();
        deduction.setDeductionTypeID(rs.getInt("deduction_type_id"));
        deduction.setDeductionName(rs.getString("deduction_name"));
        deduction.setDescription(rs.getString("description"));
        deduction.setCalculationType(rs.getString("calculation_type"));

        // Handle nullable fields
        double defaultAmount = rs.getDouble("default_amount");
        deduction.setDefaultAmount(rs.wasNull() ? null : defaultAmount);

        double defaultPercentage = rs.getDouble("default_percentage");
        deduction.setDefaultPercentage(rs.wasNull() ? null : defaultPercentage);

        deduction.setMandatory(rs.getBoolean("is_mandatory"));
        deduction.setCreatedAt(rs.getTimestamp("created_at"));
        deduction.setUpdatedAt(rs.getTimestamp("updated_at"));

        return deduction;
    }
}
