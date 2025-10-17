package model;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * AttendanceRecord model class - mapped to attendance_records table
 * Represents an attendance record for an employee on a specific date
 */
public class AttendanceRecord {
    // Primary fields from attendance_records table
    private int attendanceID;                      // attendance_id (PRIMARY KEY)
    private int employeeID;                        // employee_id (NOT NULL)
    private Date attendanceDate;                   // attendance_date (NOT NULL)
    private Time checkInTime;                      // check_in_time
    private Time checkOutTime;                     // check_out_time
    private String status;                         // status (Present, Absent, Late, Early Leave, Business Trip, Remote)
    private Double overtimeHours;                  // overtime_hours (default 0)
    private Boolean isManualAdjustment;            // is_manual_adjustment (default FALSE)
    private String adjustmentReason;               // adjustment_reason
    private Integer adjustedBy;                    // adjusted_by (user_id)
    private Timestamp adjustedAt;                  // adjusted_at
    private String importBatchID;                  // import_batch_id (for tracing imports)
    private Timestamp createdAt;                   // created_at
    private Timestamp updatedAt;                   // updated_at
    
    // Additional fields for display (from JOINs)
    private String employeeCode;
    private String employeeName;
    
    // Default constructor
    public AttendanceRecord() {
    }
    
    // Constructor for basic attendance record
    public AttendanceRecord(int employeeID, Date attendanceDate, String status) {
        this.employeeID = employeeID;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.overtimeHours = 0.0;
        this.isManualAdjustment = false;
    }
    
    // Constructor with check-in/out times
    public AttendanceRecord(int employeeID, Date attendanceDate, Time checkInTime, 
                           Time checkOutTime, String status) {
        this.employeeID = employeeID;
        this.attendanceDate = attendanceDate;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
        this.overtimeHours = 0.0;
        this.isManualAdjustment = false;
    }
    
    // Full constructor
    public AttendanceRecord(int attendanceID, int employeeID, Date attendanceDate, 
                           Time checkInTime, Time checkOutTime, String status, 
                           Double overtimeHours, Boolean isManualAdjustment, 
                           String adjustmentReason, Integer adjustedBy, Timestamp adjustedAt,
                           String importBatchID, Timestamp createdAt, Timestamp updatedAt) {
        this.attendanceID = attendanceID;
        this.employeeID = employeeID;
        this.attendanceDate = attendanceDate;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
        this.overtimeHours = overtimeHours;
        this.isManualAdjustment = isManualAdjustment;
        this.adjustmentReason = adjustmentReason;
        this.adjustedBy = adjustedBy;
        this.adjustedAt = adjustedAt;
        this.importBatchID = importBatchID;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
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

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public Time getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(Time checkInTime) {
        this.checkInTime = checkInTime;
    }

    public Time getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(Time checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(Double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public Boolean getIsManualAdjustment() {
        return isManualAdjustment;
    }

    public void setIsManualAdjustment(Boolean isManualAdjustment) {
        this.isManualAdjustment = isManualAdjustment;
    }

    public String getAdjustmentReason() {
        return adjustmentReason;
    }

    public void setAdjustmentReason(String adjustmentReason) {
        this.adjustmentReason = adjustmentReason;
    }

    public Integer getAdjustedBy() {
        return adjustedBy;
    }

    public void setAdjustedBy(Integer adjustedBy) {
        this.adjustedBy = adjustedBy;
    }

    public Timestamp getAdjustedAt() {
        return adjustedAt;
    }

    public void setAdjustedAt(Timestamp adjustedAt) {
        this.adjustedAt = adjustedAt;
    }

    public String getImportBatchID() {
        return importBatchID;
    }

    public void setImportBatchID(String importBatchID) {
        this.importBatchID = importBatchID;
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

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    @Override
    public String toString() {
        return "AttendanceRecord{" +
                "attendanceID=" + attendanceID +
                ", employeeID=" + employeeID +
                ", attendanceDate=" + attendanceDate +
                ", checkInTime=" + checkInTime +
                ", checkOutTime=" + checkOutTime +
                ", status='" + status + '\'' +
                ", overtimeHours=" + overtimeHours +
                ", isManualAdjustment=" + isManualAdjustment +
                ", importBatchID='" + importBatchID + '\'' +
                '}';
    }
}

