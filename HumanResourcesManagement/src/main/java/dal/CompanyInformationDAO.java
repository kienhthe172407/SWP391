package dal;

import model.CompanyInformation;
import java.sql.*;

/**
 * Data Access Object for Company Information
 */
public class CompanyInformationDAO extends DBContext {
    
    /**
     * Get active company information
     * @return CompanyInformation object or null if not found
     */
    public CompanyInformation getActiveCompanyInformation() {
        String sql = "SELECT company_id, company_name, company_logo_path, about_us, " +
                     "mission_statement, vision_statement, core_values, address, " +
                     "phone_number, email, website, founded_year, number_of_employees, " +
                     "industry, social_media_links, is_active, created_at, updated_at " +
                     "FROM company_information " +
                     "WHERE is_active = TRUE " +
                     "LIMIT 1";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            if (rs.next()) {
                return mapResultSetToCompanyInformation(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error in getActiveCompanyInformation: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get company information by ID
     * @param companyId Company ID
     * @return CompanyInformation object or null if not found
     */
    public CompanyInformation getCompanyInformationById(int companyId) {
        String sql = "SELECT company_id, company_name, company_logo_path, about_us, " +
                     "mission_statement, vision_statement, core_values, address, " +
                     "phone_number, email, website, founded_year, number_of_employees, " +
                     "industry, social_media_links, is_active, created_at, updated_at " +
                     "FROM company_information " +
                     "WHERE company_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, companyId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCompanyInformation(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getCompanyInformationById: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Map ResultSet to CompanyInformation object
     * @param rs ResultSet
     * @return CompanyInformation object
     * @throws SQLException if database access error occurs
     */
    private CompanyInformation mapResultSetToCompanyInformation(ResultSet rs) throws SQLException {
        CompanyInformation company = new CompanyInformation();
        
        company.setCompanyId(rs.getInt("company_id"));
        company.setCompanyName(rs.getString("company_name"));
        company.setCompanyLogoPath(rs.getString("company_logo_path"));
        company.setAboutUs(rs.getString("about_us"));
        company.setMissionStatement(rs.getString("mission_statement"));
        company.setVisionStatement(rs.getString("vision_statement"));
        company.setCoreValues(rs.getString("core_values"));
        company.setAddress(rs.getString("address"));
        company.setPhoneNumber(rs.getString("phone_number"));
        company.setEmail(rs.getString("email"));
        company.setWebsite(rs.getString("website"));
        
        // Handle nullable integer
        int foundedYear = rs.getInt("founded_year");
        if (!rs.wasNull()) {
            company.setFoundedYear(foundedYear);
        }
        
        company.setNumberOfEmployees(rs.getString("number_of_employees"));
        company.setIndustry(rs.getString("industry"));
        company.setSocialMediaLinks(rs.getString("social_media_links"));
        company.setActive(rs.getBoolean("is_active"));
        company.setCreatedAt(rs.getTimestamp("created_at"));
        company.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return company;
    }
}

