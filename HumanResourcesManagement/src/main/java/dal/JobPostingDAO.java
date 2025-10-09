package dal;

import model.JobPosting;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Job Postings
 */
public class JobPostingDAO extends DBContext {
    
    /**
     * Get all job postings with pagination
     * @param page Current page number
     * @param pageSize Number of records per page
     * @return List of job postings
     */
    public List<JobPosting> getAllJobPostings(int page, int pageSize) {
        List<JobPosting> jobPostings = new ArrayList<>();
        
        // First try with JOINs
        String sql = "SELECT jp.*, d.department_name, p.position_name, u.full_name AS poster_name " +
                     "FROM job_postings jp " +
                     "LEFT JOIN departments d ON jp.department_id = d.department_id " +
                     "LEFT JOIN positions p ON jp.position_id = p.position_id " +
                     "LEFT JOIN users u ON jp.posted_by = u.user_id " +
                     "ORDER BY jp.posted_date DESC, jp.job_id DESC " +
                     "LIMIT ? OFFSET ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    JobPosting jobPosting = mapJobPosting(rs);
                    jobPostings.add(jobPosting);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllJobPostings with JOINs: " + e.getMessage());
            e.printStackTrace();
            
            // If JOIN fails, try without JOINs
            System.out.println("Trying without JOINs...");
            String simpleSql = "SELECT * FROM job_postings ORDER BY posted_date DESC, job_id DESC LIMIT ? OFFSET ?";
            
            try (PreparedStatement ps = connection.prepareStatement(simpleSql)) {
                ps.setInt(1, pageSize);
                ps.setInt(2, (page - 1) * pageSize);
                
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        JobPosting jobPosting = mapJobPostingSimple(rs);
                        jobPostings.add(jobPosting);
                    }
                }
            } catch (SQLException e2) {
                System.err.println("Error in getAllJobPostings without JOINs: " + e2.getMessage());
                e2.printStackTrace();
            }
        }
        
        return jobPostings;
    }
    
    /**
     * Search job postings with pagination
     * @param keyword Search keyword for job title, department, position
     * @param status Job status filter
     * @param page Current page number
     * @param pageSize Number of records per page
     * @return List of job postings matching search criteria
     */
    public List<JobPosting> searchJobPostings(String keyword, String status, int page, int pageSize) {
        List<JobPosting> jobPostings = new ArrayList<>();
        
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT jp.*, d.department_name, p.position_name, u.full_name AS poster_name ");
        sqlBuilder.append("FROM job_postings jp ");
        sqlBuilder.append("LEFT JOIN departments d ON jp.department_id = d.department_id ");
        sqlBuilder.append("LEFT JOIN positions p ON jp.position_id = p.position_id ");
        sqlBuilder.append("LEFT JOIN users u ON jp.posted_by = u.user_id ");
        sqlBuilder.append("WHERE 1=1 ");
        
        // Add search conditions
        if (keyword != null && !keyword.trim().isEmpty()) {
            sqlBuilder.append("AND (jp.job_title LIKE ? OR d.department_name LIKE ? OR p.position_name LIKE ?) ");
        }
        
        if (status != null && !status.trim().isEmpty()) {
            sqlBuilder.append("AND jp.job_status = ? ");
        }
        
        sqlBuilder.append("ORDER BY jp.posted_date DESC, jp.job_id DESC ");
        sqlBuilder.append("LIMIT ? OFFSET ?");
        
        try (PreparedStatement ps = connection.prepareStatement(sqlBuilder.toString())) {
            int paramIndex = 1;
            
            // Set search parameters
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchTerm = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, searchTerm);
                ps.setString(paramIndex++, searchTerm);
                ps.setString(paramIndex++, searchTerm);
            }
            
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(paramIndex++, status);
            }
            
            // Set pagination parameters
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex, (page - 1) * pageSize);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    JobPosting jobPosting = mapJobPosting(rs);
                    jobPostings.add(jobPosting);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in searchJobPostings with JOINs: " + e.getMessage());
            e.printStackTrace();
            
            // If JOIN fails, try without JOINs
            System.out.println("Trying search without JOINs...");
            StringBuilder simpleSqlBuilder = new StringBuilder();
            simpleSqlBuilder.append("SELECT * FROM job_postings WHERE 1=1 ");
            
            if (keyword != null && !keyword.trim().isEmpty()) {
                simpleSqlBuilder.append("AND job_title LIKE ? ");
            }
            
            if (status != null && !status.trim().isEmpty()) {
                simpleSqlBuilder.append("AND job_status = ? ");
            }
            
            simpleSqlBuilder.append("ORDER BY posted_date DESC, job_id DESC LIMIT ? OFFSET ?");
            
            try (PreparedStatement ps = connection.prepareStatement(simpleSqlBuilder.toString())) {
                int paramIndex = 1;
                
                if (keyword != null && !keyword.trim().isEmpty()) {
                    ps.setString(paramIndex++, "%" + keyword.trim() + "%");
                }
                
                if (status != null && !status.trim().isEmpty()) {
                    ps.setString(paramIndex++, status);
                }
                
                ps.setInt(paramIndex++, pageSize);
                ps.setInt(paramIndex, (page - 1) * pageSize);
                
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        JobPosting jobPosting = mapJobPostingSimple(rs);
                        jobPostings.add(jobPosting);
                    }
                }
            } catch (SQLException e2) {
                System.err.println("Error in searchJobPostings without JOINs: " + e2.getMessage());
                e2.printStackTrace();
            }
        }
        
        return jobPostings;
    }
    
    /**
     * Get total number of job postings
     * @return Total count
     */
    public int getTotalJobPostings() {
        String sql = "SELECT COUNT(*) FROM job_postings";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error in getTotalJobPostings: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get total number of job postings matching search criteria
     * @param keyword Search keyword
     * @param status Job status filter
     * @return Total count of matching records
     */
    public int getTotalSearchResults(String keyword, String status) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT COUNT(*) FROM job_postings jp ");
        sqlBuilder.append("LEFT JOIN departments d ON jp.department_id = d.department_id ");
        sqlBuilder.append("LEFT JOIN positions p ON jp.position_id = p.position_id ");
        sqlBuilder.append("WHERE 1=1 ");
        
        // Add search conditions
        if (keyword != null && !keyword.trim().isEmpty()) {
            sqlBuilder.append("AND (jp.job_title LIKE ? OR d.department_name LIKE ? OR p.position_name LIKE ?) ");
        }
        
        if (status != null && !status.trim().isEmpty()) {
            sqlBuilder.append("AND jp.job_status = ? ");
        }
        
        try (PreparedStatement ps = connection.prepareStatement(sqlBuilder.toString())) {
            int paramIndex = 1;
            
            // Set search parameters
            if (keyword != null && !keyword.trim().isEmpty()) {
                String searchTerm = "%" + keyword.trim() + "%";
                ps.setString(paramIndex++, searchTerm);
                ps.setString(paramIndex++, searchTerm);
                ps.setString(paramIndex++, searchTerm);
            }
            
            if (status != null && !status.trim().isEmpty()) {
                ps.setString(paramIndex, status);
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getTotalSearchResults: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get all job posting statuses
     * @return List of status values
     */
    public List<String> getAllJobStatuses() {
        List<String> statuses = new ArrayList<>();
        
        String sql = "SELECT DISTINCT job_status FROM job_postings ORDER BY job_status";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                statuses.add(rs.getString("job_status"));
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllJobStatuses: " + e.getMessage());
            e.printStackTrace();
            
            // Add default statuses if database query fails
            statuses.add("Open");
            statuses.add("Closed");
            statuses.add("Filled");
            statuses.add("Cancelled");
        }
        
        return statuses;
    }
    
    /**
     * Get job posting by ID
     * @param jobId Job posting ID
     * @return JobPosting object or null if not found
     */
    public JobPosting getJobPostingById(int jobId) {
        String sql = "SELECT jp.*, d.department_name, p.position_name, u.full_name AS poster_name " +
                     "FROM job_postings jp " +
                     "LEFT JOIN departments d ON jp.department_id = d.department_id " +
                     "LEFT JOIN positions p ON jp.position_id = p.position_id " +
                     "LEFT JOIN users u ON jp.posted_by = u.user_id " +
                     "WHERE jp.job_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapJobPosting(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getJobPostingById: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Delete a job posting
     * @param jobId Job posting ID to delete
     * @return boolean indicating success
     */
    public boolean deleteJobPosting(int jobId) {
        String sql = "DELETE FROM job_postings WHERE job_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error in deleteJobPosting: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get database connection for testing
     * @return Connection object
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Map ResultSet to JobPosting object (simple version without JOINs)
     * @param rs ResultSet containing job posting data
     * @return JobPosting object
     * @throws SQLException if a database error occurs
     */
    private JobPosting mapJobPostingSimple(ResultSet rs) throws SQLException {
        JobPosting jobPosting = new JobPosting();
        
        jobPosting.setJobId(rs.getInt("job_id"));
        jobPosting.setJobTitle(rs.getString("job_title"));
        jobPosting.setDepartmentId(rs.getInt("department_id"));
        jobPosting.setPositionId(rs.getInt("position_id"));
        jobPosting.setJobDescription(rs.getString("job_description"));
        jobPosting.setRequirements(rs.getString("requirements"));
        jobPosting.setBenefits(rs.getString("benefits"));
        jobPosting.setSalaryRangeFrom(rs.getBigDecimal("salary_range_from"));
        jobPosting.setSalaryRangeTo(rs.getBigDecimal("salary_range_to"));
        jobPosting.setNumberOfPositions(rs.getInt("number_of_positions"));
        jobPosting.setApplicationDeadline(rs.getDate("application_deadline"));
        jobPosting.setJobStatus(rs.getString("job_status"));
        jobPosting.setPostedBy(rs.getInt("posted_by"));
        jobPosting.setPostedDate(rs.getDate("posted_date"));
        jobPosting.setInternalNotes(rs.getString("internal_notes"));
        jobPosting.setCreatedAt(rs.getTimestamp("created_at"));
        jobPosting.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Set default values for JOIN fields
        jobPosting.setDepartmentName("N/A");
        jobPosting.setPositionName("N/A");
        jobPosting.setPosterName("N/A");
        
        return jobPosting;
    }
    
    /**
     * Map ResultSet to JobPosting object
     * @param rs ResultSet containing job posting data
     * @return JobPosting object
     * @throws SQLException if a database error occurs
     */
    private JobPosting mapJobPosting(ResultSet rs) throws SQLException {
        JobPosting jobPosting = new JobPosting();
        
        jobPosting.setJobId(rs.getInt("job_id"));
        jobPosting.setJobTitle(rs.getString("job_title"));
        jobPosting.setDepartmentId(rs.getInt("department_id"));
        jobPosting.setPositionId(rs.getInt("position_id"));
        jobPosting.setJobDescription(rs.getString("job_description"));
        jobPosting.setRequirements(rs.getString("requirements"));
        jobPosting.setBenefits(rs.getString("benefits"));
        jobPosting.setSalaryRangeFrom(rs.getBigDecimal("salary_range_from"));
        jobPosting.setSalaryRangeTo(rs.getBigDecimal("salary_range_to"));
        jobPosting.setNumberOfPositions(rs.getInt("number_of_positions"));
        jobPosting.setApplicationDeadline(rs.getDate("application_deadline"));
        jobPosting.setJobStatus(rs.getString("job_status"));
        jobPosting.setPostedBy(rs.getInt("posted_by"));
        jobPosting.setPostedDate(rs.getDate("posted_date"));
        jobPosting.setInternalNotes(rs.getString("internal_notes"));
        jobPosting.setCreatedAt(rs.getTimestamp("created_at"));
        jobPosting.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Get additional fields from joins
        jobPosting.setDepartmentName(rs.getString("department_name"));
        jobPosting.setPositionName(rs.getString("position_name"));
        jobPosting.setPosterName(rs.getString("poster_name"));
        
        return jobPosting;
    }
}
