package model;

import java.sql.Date;
import java.sql.Timestamp;
import java.math.BigDecimal;

/**
 * Request model class - mapped to requests table
 * Represents employee requests for leave, remote work, business trips, etc.
 * @author admin
 */
public class Request {
    // Primary fields from requests table
    private int requestID;                      // request_id
    private int employeeID;                     // employee_id
    private int requestTypeID;                  // request_type_id
    private Date startDate;                     // start_date
    private Date endDate;                       // end_date
    private BigDecimal numberOfDays;            // number_of_days (can be 0.5 for half day)
    private String reason;                      // reason
    private String requestStatus;               // request_status (Pending, Approved, Rejected, Cancelled)
    private Integer reviewedBy;                 // reviewed_by (user_id)
    private String reviewComment;               // review_comment
    private Timestamp reviewedAt;               // reviewed_at
    private Timestamp cancelledAt;              // cancelled_at
    private Timestamp createdAt;                // created_at
    private Timestamp updatedAt;                // updated_at
    
    // Additional fields for display (from JOINs)
    private String employeeName;                // Employee full name
    private String employeeCode;                // Employee code
    private String requestTypeName;             // Request type name
    private String reviewerName;                // Reviewer full name
    private String departmentName;              // Employee's department
    
    // Default constructor
    public Request() {
    }
    
    // Constructor for creating new request
    public Request(int employeeID, int requestTypeID, Date startDate, Date endDate, 
                   BigDecimal numberOfDays, String reason) {
        this.employeeID = employeeID;
        this.requestTypeID = requestTypeID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfDays = numberOfDays;
        this.reason = reason;
        this.requestStatus = "Pending";
    }
    
    // Full constructor
    public Request(int requestID, int employeeID, int requestTypeID, Date startDate, Date endDate,
                   BigDecimal numberOfDays, String reason, String requestStatus, Integer reviewedBy,
                   String reviewComment, Timestamp reviewedAt, Timestamp cancelledAt,
                   Timestamp createdAt, Timestamp updatedAt) {
        this.requestID = requestID;
        this.employeeID = employeeID;
        this.requestTypeID = requestTypeID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfDays = numberOfDays;
        this.reason = reason;
        this.requestStatus = requestStatus;
        this.reviewedBy = reviewedBy;
        this.reviewComment = reviewComment;
        this.reviewedAt = reviewedAt;
        this.cancelledAt = cancelledAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getRequestTypeID() {
        return requestTypeID;
    }

    public void setRequestTypeID(int requestTypeID) {
        this.requestTypeID = requestTypeID;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(BigDecimal numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Integer getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Integer reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public String getReviewComment() {
        return reviewComment;
    }

    public void setReviewComment(String reviewComment) {
        this.reviewComment = reviewComment;
    }

    public Timestamp getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Timestamp reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public Timestamp getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Timestamp cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getRequestTypeName() {
        return requestTypeName;
    }

    public void setRequestTypeName(String requestTypeName) {
        this.requestTypeName = requestTypeName;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    // Helper methods
    public boolean isPending() {
        return "Pending".equals(requestStatus);
    }

    public boolean isApproved() {
        return "Approved".equals(requestStatus);
    }

    public boolean isRejected() {
        return "Rejected".equals(requestStatus);
    }

    public boolean isCancelled() {
        return "Cancelled".equals(requestStatus);
    }

    public boolean canBeEdited() {
        return isPending();
    }

    public boolean canBeCancelled() {
        return isPending();
    }

    public String getStatusBadgeClass() {
        switch (requestStatus) {
            case "Pending":
                return "badge-warning";
            case "Approved":
                return "badge-success";
            case "Rejected":
                return "badge-danger";
            case "Cancelled":
                return "badge-secondary";
            default:
                return "badge-secondary";
        }
    }

    @Override
    public String toString() {
        return "Request{" +
                "requestID=" + requestID +
                ", employeeID=" + employeeID +
                ", requestTypeID=" + requestTypeID +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", numberOfDays=" + numberOfDays +
                ", reason='" + reason + '\'' +
                ", requestStatus='" + requestStatus + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", requestTypeName='" + requestTypeName + '\'' +
                '}';
    }
}

