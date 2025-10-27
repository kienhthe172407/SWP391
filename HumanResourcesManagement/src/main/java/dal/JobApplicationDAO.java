package dal;

import model.JobApplication;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data Access Object for Job Applications
 */
public class JobApplicationDAO extends DBContext {
    
    /**
     * Create a new job application
     * @param application JobApplication object with data to insert
     * @return boolean indicating success
     */
    public boolean createJobApplication(JobApplication application) {
        String sql = "INSERT INTO job_applications (job_id, applicant_name, applicant_email, " +
                     "applicant_phone, resume_file_path, cover_letter, application_status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, application.getJobId());
            ps.setString(2, application.getApplicantName());
            ps.setString(3, application.getApplicantEmail());
            ps.setString(4, application.getApplicantPhone());
            ps.setString(5, application.getResumeFilePath());
            ps.setString(6, application.getCoverLetter());
            ps.setString(7, application.getApplicationStatus() != null ? 
                         application.getApplicationStatus() : "Submitted");
            
            int affectedRows = ps.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        application.setApplicationId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error in createJobApplication: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get all applications for a specific job posting
     * @param jobId Job posting ID
     * @return List of job applications
     */
    public List<JobApplication> getApplicationsByJobId(int jobId) {
        List<JobApplication> applications = new ArrayList<>();
        
        String sql = "SELECT ja.*, jp.job_title, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS reviewer_name " +
                     "FROM job_applications ja " +
                     "LEFT JOIN job_postings jp ON ja.job_id = jp.job_id " +
                     "LEFT JOIN users u ON ja.reviewed_by = u.user_id " +
                     "LEFT JOIN employees e ON u.user_id = e.user_id " +
                     "WHERE ja.job_id = ? " +
                     "ORDER BY ja.applied_date DESC";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    JobApplication application = mapJobApplication(rs);
                    applications.add(application);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getApplicationsByJobId: " + e.getMessage());
            e.printStackTrace();
        }
        
        return applications;
    }
    
    /**
     * Get job application by ID
     * @param applicationId Application ID
     * @return JobApplication object or null if not found
     */
    public JobApplication getApplicationById(int applicationId) {
        String sql = "SELECT ja.*, jp.job_title, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS reviewer_name " +
                     "FROM job_applications ja " +
                     "LEFT JOIN job_postings jp ON ja.job_id = jp.job_id " +
                     "LEFT JOIN users u ON ja.reviewed_by = u.user_id " +
                     "LEFT JOIN employees e ON u.user_id = e.user_id " +
                     "WHERE ja.application_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapJobApplication(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getApplicationById: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Get all job applications with pagination
     * @param page Current page number
     * @param pageSize Number of records per page
     * @return List of job applications
     */
    public List<JobApplication> getAllApplications(int page, int pageSize) {
        List<JobApplication> applications = new ArrayList<>();
        
        String sql = "SELECT ja.*, jp.job_title, " +
                     "CONCAT(e.first_name, ' ', e.last_name) AS reviewer_name " +
                     "FROM job_applications ja " +
                     "LEFT JOIN job_postings jp ON ja.job_id = jp.job_id " +
                     "LEFT JOIN users u ON ja.reviewed_by = u.user_id " +
                     "LEFT JOIN employees e ON u.user_id = e.user_id " +
                     "ORDER BY ja.applied_date DESC " +
                     "LIMIT ? OFFSET ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, (page - 1) * pageSize);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    JobApplication application = mapJobApplication(rs);
                    applications.add(application);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getAllApplications: " + e.getMessage());
            e.printStackTrace();
        }
        
        return applications;
    }
    
    /**
     * Update application status
     * @param applicationId Application ID
     * @param status New status
     * @param reviewedBy User ID of reviewer
     * @param reviewNotes Review notes
     * @return boolean indicating success
     */
    public boolean updateApplicationStatus(int applicationId, String status, 
                                           Integer reviewedBy, String reviewNotes) {
        String sql = "UPDATE job_applications SET application_status = ?, " +
                     "reviewed_by = ?, review_notes = ? WHERE application_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setObject(2, reviewedBy);
            ps.setString(3, reviewNotes);
            ps.setInt(4, applicationId);
            
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error in updateApplicationStatus: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }
    
    /**
     * Get total count of applications
     * @return Total number of applications
     */
    public int getTotalApplications() {
        String sql = "SELECT COUNT(*) FROM job_applications";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error in getTotalApplications: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Get count of applications for a specific job
     * @param jobId Job posting ID
     * @return Number of applications
     */
    public int getApplicationCountByJobId(int jobId) {
        String sql = "SELECT COUNT(*) FROM job_applications WHERE job_id = ?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, jobId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getApplicationCountByJobId: " + e.getMessage());
            e.printStackTrace();
        }
        
        return 0;
    }
    
    /**
     * Map ResultSet to JobApplication object
     * @param rs ResultSet from query
     * @return JobApplication object
     * @throws SQLException if database access error occurs
     */
    private JobApplication mapJobApplication(ResultSet rs) throws SQLException {
        JobApplication application = new JobApplication();
        
        application.setApplicationId(rs.getInt("application_id"));
        application.setJobId(rs.getInt("job_id"));
        application.setApplicantName(rs.getString("applicant_name"));
        application.setApplicantEmail(rs.getString("applicant_email"));
        application.setApplicantPhone(rs.getString("applicant_phone"));
        application.setResumeFilePath(rs.getString("resume_file_path"));
        application.setCoverLetter(rs.getString("cover_letter"));
        application.setApplicationStatus(rs.getString("application_status"));
        application.setAppliedDate(rs.getTimestamp("applied_date"));
        
        // Handle nullable fields
        int reviewedBy = rs.getInt("reviewed_by");
        if (!rs.wasNull()) {
            application.setReviewedBy(reviewedBy);
        }
        
        application.setReviewNotes(rs.getString("review_notes"));
        application.setInterviewDate(rs.getTimestamp("interview_date"));
        application.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        // Additional fields from joins
        application.setJobTitle(rs.getString("job_title"));
        application.setReviewerName(rs.getString("reviewer_name"));
        
        return application;
    }

    /**
     * Get application counts for all jobs
     * Returns a map of job_id -> application count
     * @return Map with job IDs as keys and application counts as values
     */
    public Map<Integer, Integer> getApplicationCountsForAllJobs() {
        Map<Integer, Integer> counts = new HashMap<>();
        String sql = "SELECT job_id, COUNT(*) as application_count " +
                     "FROM job_applications " +
                     "GROUP BY job_id";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int jobId = rs.getInt("job_id");
                int count = rs.getInt("application_count");
                counts.put(jobId, count);
            }

        } catch (SQLException e) {
            System.err.println("Error in getApplicationCountsForAllJobs: " + e.getMessage());
            e.printStackTrace();
        }

        return counts;
    }
}

