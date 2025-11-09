package model;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * AttendanceExceptionRequest model class - mapped to attendance_exception_requests table
 * Represents an employee's request to explain/correct their attendance record
 * @author admin
 */
public class AttendanceExceptionRequest {
    // Primary fields from attendance_exception_requests table
    private int requestID;                      // request_id (PRIMARY KEY)
    private int attendanceID;                   // attendance_id (NOT NULL)
    private int employeeID;                     // employee_id (NOT NULL)
    private String requestReason;                // request_reason (TEXT NOT NULL)
    private Time proposedCheckIn;                // proposed_check_in (TIME)
    private Time proposedCheckOut;               // proposed_check_out (TIME)
    private String proposedStatus;               // proposed_status (ENUM)
    private String status;                       // status (Pending, Approved, Rejected)
    private Integer reviewedBy;                  // reviewed_by (user_id)
    private String reviewComment;                // review_comment (TEXT)
    private Timestamp reviewedAt;                // reviewed_at (TIMESTAMP)
    private Timestamp createdAt;                // created_at
    private Timestamp updatedAt;                 // updated_at
    
    // Additional fields for display (from JOINs)
    private String employeeName;                 // Employee full name
    private String employeeCode;                 // Employee code
    private String reviewerName;                 // Reviewer full name
    private String departmentName;              // Employee's department
    private java.sql.Date attendanceDate;        // Attendance date
    private Time currentCheckIn;                  // Current check-in time
    private Time currentCheckOut;                // Current check-out time
    private String currentStatus;                // Current attendance status
    
    // Default constructor
    public AttendanceExceptionRequest() {
        this.status = "Pending";
    }
    
    // Constructor for creating new request
    public AttendanceExceptionRequest(int attendanceID, int employeeID, String requestReason,
                                     Time proposedCheckIn, Time proposedCheckOut, String proposedStatus) {
        this.attendanceID = attendanceID;
        this.employeeID = employeeID;
        this.requestReason = requestReason;
        this.proposedCheckIn = proposedCheckIn;
        this.proposedCheckOut = proposedCheckOut;
        this.proposedStatus = proposedStatus;
        this.status = "Pending";
    }
    
    // Full constructor
    public AttendanceExceptionRequest(int requestID, int attendanceID, int employeeID,
                                     String requestReason, Time proposedCheckIn, Time proposedCheckOut,
                                     String proposedStatus, String status, Integer reviewedBy,
                                     String reviewComment, Timestamp reviewedAt,
                                     Timestamp createdAt, Timestamp updatedAt) {
        this.requestID = requestID;
        this.attendanceID = attendanceID;
        this.employeeID = employeeID;
        this.requestReason = requestReason;
        this.proposedCheckIn = proposedCheckIn;
        this.proposedCheckOut = proposedCheckOut;
        this.proposedStatus = proposedStatus;
        this.status = status;
        this.reviewedBy = reviewedBy;
        this.reviewComment = reviewComment;
        this.reviewedAt = reviewedAt;
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

    public int getAttendanceID() {
        return attendanceID;
    }

    public void setAttendanceID(int attendanceID) {
        this.attendanceID = attendanceID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getRequestReason() {
        return requestReason;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public Time getProposedCheckIn() {
        return proposedCheckIn;
    }

    public void setProposedCheckIn(Time proposedCheckIn) {
        this.proposedCheckIn = proposedCheckIn;
    }

    public Time getProposedCheckOut() {
        return proposedCheckOut;
    }

    public void setProposedCheckOut(Time proposedCheckOut) {
        this.proposedCheckOut = proposedCheckOut;
    }

    public String getProposedStatus() {
        return proposedStatus;
    }

    public void setProposedStatus(String proposedStatus) {
        this.proposedStatus = proposedStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public java.sql.Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(java.sql.Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Time getCurrentCheckIn() {
        return currentCheckIn;
    }

    public void setCurrentCheckIn(Time currentCheckIn) {
        this.currentCheckIn = currentCheckIn;
    }

    public Time getCurrentCheckOut() {
        return currentCheckOut;
    }

    public void setCurrentCheckOut(Time currentCheckOut) {
        this.currentCheckOut = currentCheckOut;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    // Helper methods
    public boolean isPending() {
        return "Pending".equals(status);
    }

    public boolean isApproved() {
        return "Approved".equals(status);
    }

    public boolean isRejected() {
        return "Rejected".equals(status);
    }

    public String getStatusBadgeClass() {
        switch (status) {
            case "Pending":
                return "badge-warning";
            case "Approved":
                return "badge-success";
            case "Rejected":
                return "badge-danger";
            default:
                return "badge-secondary";
        }
    }

    @Override
    public String toString() {
        return "AttendanceExceptionRequest{" +
                "requestID=" + requestID +
                ", attendanceID=" + attendanceID +
                ", employeeID=" + employeeID +
                ", requestReason='" + requestReason + '\'' +
                ", proposedStatus='" + proposedStatus + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

