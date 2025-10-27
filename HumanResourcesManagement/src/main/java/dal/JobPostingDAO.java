package dal;

import model.JobPosting;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Department;
import model.Position;

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
        String sql = "SELECT jp.*, d.department_name, p.position_name, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS poster_name " +
                     "FROM job_postings jp " +
                     "LEFT JOIN departments d ON jp.department_id = d.department_id " +
                     "LEFT JOIN positions p ON jp.position_id = p.position_id " +
                     "LEFT JOIN users u ON jp.posted_by = u.user_id " +
                     "LEFT JOIN employees e ON u.user_id = e.user_id " +
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
                System.out.println("Successfully retrieved " + jobPostings.size() + " job postings with JOINs");
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
     * @param department Department filter
     * @param position Position filter
     * @param page Current page number
     * @param pageSize Number of records per page
     * @return List of job postings matching search criteria
     */
    public List<JobPosting> searchJobPostings(String keyword, String status, String department, String position, int page, int pageSize) {
        List<JobPosting> jobPostings = new ArrayList<>();
        
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT jp.*, d.department_name, p.position_name, ");
        sqlBuilder.append("CONCAT(e.first_name, ' ', e.last_name) AS poster_name ");
        sqlBuilder.append("FROM job_postings jp ");
        sqlBuilder.append("LEFT JOIN departments d ON jp.department_id = d.department_id ");
        sqlBuilder.append("LEFT JOIN positions p ON jp.position_id = p.position_id ");
        sqlBuilder.append("LEFT JOIN users u ON jp.posted_by = u.user_id ");
        sqlBuilder.append("LEFT JOIN employees e ON u.user_id = e.user_id ");
        sqlBuilder.append("WHERE 1=1 ");
        
        // Add search conditions
        if (keyword != null && !keyword.trim().isEmpty()) {
            sqlBuilder.append("AND (jp.job_title LIKE ? OR d.department_name LIKE ? OR p.position_name LIKE ?) ");
        }
        
        if (status != null && !status.trim().isEmpty()) {
            sqlBuilder.append("AND jp.job_status = ? ");
        }
        
        if (department != null && !department.trim().isEmpty()) {
            sqlBuilder.append("AND jp.department_id = ? ");
        }
        
        if (position != null && !position.trim().isEmpty()) {
            sqlBuilder.append("AND jp.position_id = ? ");
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
            
            if (department != null && !department.trim().isEmpty()) {
                ps.setString(paramIndex++, department);
            }
            
            if (position != null && !position.trim().isEmpty()) {
                ps.setString(paramIndex++, position);
            }
            
            // Set pagination parameters
            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex, (page - 1) * pageSize);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    JobPosting jobPosting = mapJobPosting(rs);
                    jobPostings.add(jobPosting);
                }
                System.out.println("Successfully retrieved " + jobPostings.size() + " job postings with JOINs (search)");
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
            
            if (department != null && !department.trim().isEmpty()) {
                simpleSqlBuilder.append("AND department_id = ? ");
            }
            
            if (position != null && !position.trim().isEmpty()) {
                simpleSqlBuilder.append("AND position_id = ? ");
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
                
                if (department != null && !department.trim().isEmpty()) {
                    ps.setString(paramIndex++, department);
                }
                
                if (position != null && !position.trim().isEmpty()) {
                    ps.setString(paramIndex++, position);
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
     * @param department Department filter
     * @param position Position filter
     * @return Total count of matching records
     */
    public int getTotalSearchResults(String keyword, String status, String department, String position) {
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
        
        if (department != null && !department.trim().isEmpty()) {
            sqlBuilder.append("AND jp.department_id = ? ");
        }
        
        if (position != null && !position.trim().isEmpty()) {
            sqlBuilder.append("AND jp.position_id = ? ");
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
                ps.setString(paramIndex++, status);
            }
            
            if (department != null && !department.trim().isEmpty()) {
                ps.setString(paramIndex++, department);
            }
            
            if (position != null && !position.trim().isEmpty()) {
                ps.setString(paramIndex++, position);
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
        // First try with JOINs to get related information
        String sql = "SELECT jp.*, d.department_name, p.position_name, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS poster_name " +
                     "FROM job_postings jp " +
                     "LEFT JOIN departments d ON jp.department_id = d.department_id " +
                     "LEFT JOIN positions p ON jp.position_id = p.position_id " +
                     "LEFT JOIN users u ON jp.posted_by = u.user_id " +
                     "LEFT JOIN employees e ON u.user_id = e.user_id " +
                     "WHERE jp.job_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapJobPosting(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getJobPostingById with JOINs: " + e.getMessage());
            e.printStackTrace();
            
            // If JOIN fails, try simple query without JOINs
            System.out.println("Trying getJobPostingById without JOINs...");
            String simpleSql = "SELECT * FROM job_postings WHERE job_id = ?";
            
            try (PreparedStatement ps = connection.prepareStatement(simpleSql)) {
                ps.setInt(1, jobId);
                
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return mapJobPostingSimple(rs);
                    }
                }
            } catch (SQLException e2) {
                System.err.println("Error in getJobPostingById without JOINs: " + e2.getMessage());
                e2.printStackTrace();
            }
        }
        
        return null;
    }
    
    /**
     * Create a new job posting
     * @param jobPosting JobPosting object with data to insert
     * @return boolean indicating success
     */
    public boolean createJobPosting(JobPosting jobPosting) {
        String sql = "INSERT INTO job_postings (job_title, department_id, position_id, job_description, " +
                     "requirements, benefits, salary_range_from, salary_range_to, number_of_positions, " +
                     "application_deadline, job_status, posted_by, posted_date, internal_notes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, jobPosting.getJobTitle());
            ps.setObject(2, jobPosting.getDepartmentId());
            ps.setObject(3, jobPosting.getPositionId());
            ps.setString(4, jobPosting.getJobDescription());
            ps.setString(5, jobPosting.getRequirements());
            ps.setString(6, jobPosting.getBenefits());
            ps.setObject(7, jobPosting.getSalaryRangeFrom());
            ps.setObject(8, jobPosting.getSalaryRangeTo());
            ps.setObject(9, jobPosting.getNumberOfPositions());
            ps.setObject(10, jobPosting.getApplicationDeadline());
            ps.setString(11, jobPosting.getJobStatus());
            ps.setObject(12, jobPosting.getPostedBy());
            ps.setObject(13, jobPosting.getPostedDate());
            ps.setString(14, jobPosting.getInternalNotes());
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        jobPosting.setJobId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error in createJobPosting: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Update an existing job posting
     * @param jobPosting JobPosting object with updated data
     * @return boolean indicating success
     */
    public boolean updateJobPosting(JobPosting jobPosting) {
        String sql = "UPDATE job_postings SET job_title = ?, department_id = ?, position_id = ?, " +
                     "job_description = ?, requirements = ?, benefits = ?, salary_range_from = ?, " +
                     "salary_range_to = ?, number_of_positions = ?, application_deadline = ?, " +
                     "job_status = ?, internal_notes = ?, updated_at = NOW() " +
                     "WHERE job_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, jobPosting.getJobTitle());
            ps.setObject(2, jobPosting.getDepartmentId());
            ps.setObject(3, jobPosting.getPositionId());
            ps.setString(4, jobPosting.getJobDescription());
            ps.setString(5, jobPosting.getRequirements());
            ps.setString(6, jobPosting.getBenefits());
            ps.setObject(7, jobPosting.getSalaryRangeFrom());
            ps.setObject(8, jobPosting.getSalaryRangeTo());
            ps.setObject(9, jobPosting.getNumberOfPositions());
            ps.setObject(10, jobPosting.getApplicationDeadline());
            ps.setString(11, jobPosting.getJobStatus());
            ps.setString(12, jobPosting.getInternalNotes());
            ps.setInt(13, jobPosting.getJobId());
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Error in updateJobPosting: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
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
     * Get all departments
     * @return List of departments
     */
    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT department_id, department_name FROM departments ORDER BY department_name";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Department dept = new Department();
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
     * Get all positions
     * @return List of positions
     */
    public List<Position> getAllPositions() {
        List<Position> positions = new ArrayList<>();
        String sql = "SELECT position_id, position_name FROM positions ORDER BY position_name";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Position pos = new Position();
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
     * Get database connection for testing
     * @return Connection object
     */
    public Connection getConnection() {
        return connection;
    }
    
    /**
     * Get department name by ID
     * @param departmentId Department ID
     * @return Department name or null if not found
     */
    private String getDepartmentNameById(Integer departmentId) {
        if (departmentId == null) return null;
        
        String sql = "SELECT department_name FROM departments WHERE department_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, departmentId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("department_name");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getDepartmentNameById: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get position name by ID
     * @param positionId Position ID
     * @return Position name or null if not found
     */
    private String getPositionNameById(Integer positionId) {
        if (positionId == null) return null;
        
        String sql = "SELECT position_name FROM positions WHERE position_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, positionId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("position_name");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getPositionNameById: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
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
        
        // Try to fetch department and position names separately
        try {
            if (jobPosting.getDepartmentId() != null) {
                String deptName = getDepartmentNameById(jobPosting.getDepartmentId());
                jobPosting.setDepartmentName(deptName != null ? deptName : "N/A");
            } else {
                jobPosting.setDepartmentName("N/A");
            }
            
            if (jobPosting.getPositionId() != null) {
                String posName = getPositionNameById(jobPosting.getPositionId());
                jobPosting.setPositionName(posName != null ? posName : "N/A");
            } else {
                jobPosting.setPositionName("N/A");
            }
        } catch (Exception e) {
            System.err.println("Error fetching department/position names: " + e.getMessage());
            jobPosting.setDepartmentName("N/A");
            jobPosting.setPositionName("N/A");
        }
        
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

    /**
     * Get open job postings for public home page
     * Only returns jobs with 'Open' status and deadline not passed
     * @param page Current page number
     * @param pageSize Number of records per page
     * @return List of open job postings
     */
    public List<JobPosting> getOpenJobPostings(int page, int pageSize) {
        List<JobPosting> jobPostings = new ArrayList<>();

        String sql = "SELECT jp.*, d.department_name, p.position_name, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS poster_name " +
                     "FROM job_postings jp " +
                     "LEFT JOIN departments d ON jp.department_id = d.department_id " +
                     "LEFT JOIN positions p ON jp.position_id = p.position_id " +
                     "LEFT JOIN users u ON jp.posted_by = u.user_id " +
                     "LEFT JOIN employees e ON u.user_id = e.user_id " +
                     "WHERE jp.job_status = 'Open' " +
                     "AND (jp.application_deadline IS NULL OR jp.application_deadline >= CURDATE()) " +
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
            System.err.println("Error in getOpenJobPostings: " + e.getMessage());
            e.printStackTrace();
        }

        return jobPostings;
    }

    /**
     * Get total count of open job postings
     * @return Total number of open job postings
     */
    public int getTotalOpenJobPostings() {
        String sql = "SELECT COUNT(*) FROM job_postings " +
                     "WHERE job_status = 'Open' " +
                     "AND (application_deadline IS NULL OR application_deadline >= CURDATE())";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error in getTotalOpenJobPostings: " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }
}
