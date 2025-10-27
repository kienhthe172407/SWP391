package model;

import java.sql.Timestamp;

/**
 * Model class for Job Applications
 * Represents applicant submissions for job postings
 */
public class JobApplication {
    private int applicationId;
    private int jobId;
    private String applicantName;
    private String applicantEmail;
    private String applicantPhone;
    private String resumeFilePath;
    private String coverLetter;
    private String applicationStatus;
    private Timestamp appliedDate;
    private Integer reviewedBy;
    private String reviewNotes;
    private Timestamp interviewDate;
    private Timestamp updatedAt;
    
    // Additional fields from joins
    private String jobTitle;
    private String reviewerName;
    
    // Default constructor
    public JobApplication() {
    }
    
    // Constructor with essential fields
    public JobApplication(int jobId, String applicantName, String applicantEmail, 
                         String applicantPhone, String resumeFilePath, String coverLetter) {
        this.jobId = jobId;
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.applicantPhone = applicantPhone;
        this.resumeFilePath = resumeFilePath;
        this.coverLetter = coverLetter;
        this.applicationStatus = "Submitted";
    }
    
    // Getters and Setters
    public int getApplicationId() {
        return applicationId;
    }
    
    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }
    
    public int getJobId() {
        return jobId;
    }
    
    public void setJobId(int jobId) {
        this.jobId = jobId;
    }
    
    public String getApplicantName() {
        return applicantName;
    }
    
    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }
    
    public String getApplicantEmail() {
        return applicantEmail;
    }
    
    public void setApplicantEmail(String applicantEmail) {
        this.applicantEmail = applicantEmail;
    }
    
    public String getApplicantPhone() {
        return applicantPhone;
    }
    
    public void setApplicantPhone(String applicantPhone) {
        this.applicantPhone = applicantPhone;
    }
    
    public String getResumeFilePath() {
        return resumeFilePath;
    }
    
    public void setResumeFilePath(String resumeFilePath) {
        this.resumeFilePath = resumeFilePath;
    }
    
    public String getCoverLetter() {
        return coverLetter;
    }
    
    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }
    
    public String getApplicationStatus() {
        return applicationStatus;
    }
    
    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
    
    public Timestamp getAppliedDate() {
        return appliedDate;
    }
    
    public void setAppliedDate(Timestamp appliedDate) {
        this.appliedDate = appliedDate;
    }
    
    public Integer getReviewedBy() {
        return reviewedBy;
    }
    
    public void setReviewedBy(Integer reviewedBy) {
        this.reviewedBy = reviewedBy;
    }
    
    public String getReviewNotes() {
        return reviewNotes;
    }
    
    public void setReviewNotes(String reviewNotes) {
        this.reviewNotes = reviewNotes;
    }
    
    public Timestamp getInterviewDate() {
        return interviewDate;
    }
    
    public void setInterviewDate(Timestamp interviewDate) {
        this.interviewDate = interviewDate;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getJobTitle() {
        return jobTitle;
    }
    
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    
    public String getReviewerName() {
        return reviewerName;
    }
    
    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }
    
    @Override
    public String toString() {
        return "JobApplication{" +
                "applicationId=" + applicationId +
                ", jobId=" + jobId +
                ", applicantName='" + applicantName + '\'' +
                ", applicantEmail='" + applicantEmail + '\'' +
                ", applicantPhone='" + applicantPhone + '\'' +
                ", applicationStatus='" + applicationStatus + '\'' +
                ", appliedDate=" + appliedDate +
                '}';
    }
}

