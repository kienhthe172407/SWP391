package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Model class for bonus adjustments
 * Represents manual bonus adjustments made by HR staff
 * @author admin
 */
public class BonusAdjustment {
    
    // Adjustment types (matching database ENUM)
    public static final String TYPE_BONUS = "Bonus";
    public static final String TYPE_DEDUCTION = "Deduction";
    public static final String TYPE_RETROACTIVE = "Retroactive";
    public static final String TYPE_CORRECTION = "Correction";

    // Status values (matching database ENUM)
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_APPROVED = "Approved";
    public static final String STATUS_REJECTED = "Rejected";

    // Database fields
    private int adjustmentId;
    private Integer payrollId;  // Can be null when creating adjustment before payroll exists
    private int employeeId;
    private String adjustmentType;
    private BigDecimal amount;
    private String reason;
    private String status;
    private int requestedBy;
    private Integer approvedBy;
    private String approvalComment;
    private Timestamp approvedAt;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Display fields (joined from other tables)
    private String employeeName;
    private String employeeCode;
    private String requestedByName;
    private String approvedByName;
    private Date payrollMonth;  // For display purposes (from monthly_payroll)
    
    // Constructors
    public BonusAdjustment() {
    }
    
    public BonusAdjustment(int employeeId, Integer payrollId, String adjustmentType,
                          BigDecimal amount, String reason, int requestedBy) {
        this.employeeId = employeeId;
        this.payrollId = payrollId;
        this.adjustmentType = adjustmentType;
        this.amount = amount;
        this.reason = reason;
        this.requestedBy = requestedBy;
        this.status = STATUS_PENDING;
    }
    
    // Getters and Setters
    public int getAdjustmentId() {
        return adjustmentId;
    }
    
    public void setAdjustmentId(int adjustmentId) {
        this.adjustmentId = adjustmentId;
    }

    public Integer getPayrollId() {
        return payrollId;
    }

    public void setPayrollId(Integer payrollId) {
        this.payrollId = payrollId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getAdjustmentType() {
        return adjustmentType;
    }
    
    public void setAdjustmentType(String adjustmentType) {
        this.adjustmentType = adjustmentType;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getRequestedBy() {
        return requestedBy;
    }
    
    public void setRequestedBy(int requestedBy) {
        this.requestedBy = requestedBy;
    }

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovalComment() {
        return approvalComment;
    }

    public void setApprovalComment(String approvalComment) {
        this.approvalComment = approvalComment;
    }

    public Timestamp getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Timestamp approvedAt) {
        this.approvedAt = approvedAt;
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
    
    // Display fields
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
    
    public String getRequestedByName() {
        return requestedByName;
    }
    
    public void setRequestedByName(String requestedByName) {
        this.requestedByName = requestedByName;
    }
    
    public String getApprovedByName() {
        return approvedByName;
    }

    public void setApprovedByName(String approvedByName) {
        this.approvedByName = approvedByName;
    }

    public Date getPayrollMonth() {
        return payrollMonth;
    }

    public void setPayrollMonth(Date payrollMonth) {
        this.payrollMonth = payrollMonth;
    }

    // Utility methods
    public boolean isPending() {
        return STATUS_PENDING.equals(status);
    }

    public boolean isApproved() {
        return STATUS_APPROVED.equals(status);
    }

    public boolean isRejected() {
        return STATUS_REJECTED.equals(status);
    }

    public String getAdjustmentTypeDisplay() {
        if (adjustmentType == null) return "";
        switch (adjustmentType) {
            case TYPE_BONUS:
                return "Bonus";
            case TYPE_DEDUCTION:
                return "Deduction";
            case TYPE_RETROACTIVE:
                return "Retroactive";
            case TYPE_CORRECTION:
                return "Correction";
            default:
                return adjustmentType;
        }
    }

    @Override
    public String toString() {
        return "BonusAdjustment{" +
                "adjustmentId=" + adjustmentId +
                ", payrollId=" + payrollId +
                ", employeeId=" + employeeId +
                ", adjustmentType='" + adjustmentType + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}

