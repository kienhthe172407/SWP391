package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Contract model class - mapped to employment_contracts table
 * @author admin
 */
public class Contract {
    // Primary fields from employment_contracts table
    private int contractID;              // contract_id
    private int employeeID;              // employee_id
    private String contractNumber;       // contract_number (UNIQUE, NOT NULL)
    private String contractType;         // contract_type (Probation, Fixed-term, Indefinite, Seasonal)
    private Date startDate;              // start_date
    private Date endDate;                // end_date
    private BigDecimal salaryAmount;     // salary_amount
    private String jobDescription;       // job_description (details + terms)
    private String contractStatus;       // contract_status (Draft, Pending Approval, Active, Expired, Terminated)
    private Date signedDate;             // signed_date
    private Integer approvedBy;          // approved_by (user_id)
    private String approvalComment;      // approval_comment
    private Timestamp approvedAt;        // approved_at
    private Integer createdBy;           // created_by (user_id)
    private Timestamp createdAt;         // created_at
    private Timestamp updatedAt;         // updated_at
    
    // Employee information (for display in list - from JOIN)
    private String employeeFullName;
    private String employeeContactInfo;  // Deprecated - use employeePhone or employeeEmail
    private String employeePhone;        // phone_number from employees
    private String employeeEmail;        // personal_email from employees
    private String employeeCode;         // employee_code from employees
    
    // Approver information (for display)
    private String approvedByName;
    
    // Default constructor
    public Contract() {
    }
    
    // Basic constructor for backward compatibility
    public Contract(int contractID, int employeeID, String contractType, 
                   Date startDate, Date endDate, String contractStatus) {
        this.contractID = contractID;
        this.employeeID = employeeID;
        this.contractType = contractType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.contractStatus = contractStatus;
    }
    
    // Full constructor
    public Contract(int contractID, int employeeID, String contractNumber, String contractType, 
                   Date startDate, Date endDate, BigDecimal salaryAmount, String jobDescription,
                   String contractStatus, Date signedDate,
                   Integer approvedBy, String approvalComment, Timestamp approvedAt,
                   Integer createdBy, Timestamp createdAt, Timestamp updatedAt) {
        this.contractID = contractID;
        this.employeeID = employeeID;
        this.contractNumber = contractNumber;
        this.contractType = contractType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.salaryAmount = salaryAmount;
        this.jobDescription = jobDescription;
        this.contractStatus = contractStatus;
        this.signedDate = signedDate;
        this.approvedBy = approvedBy;
        this.approvalComment = approvalComment;
        this.approvedAt = approvedAt;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getContractID() {
        return contractID;
    }

    public void setContractID(int contractID) {
        this.contractID = contractID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
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

    public BigDecimal getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(BigDecimal salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getContractStatus() {
        return contractStatus;
    }

    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    public Date getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(Date signedDate) {
        this.signedDate = signedDate;
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

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
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

    public String getEmployeeFullName() {
        return employeeFullName;
    }

    public void setEmployeeFullName(String employeeFullName) {
        this.employeeFullName = employeeFullName;
    }

    @Deprecated
    public String getEmployeeContactInfo() {
        // For backward compatibility - return phone or email
        if (employeePhone != null && !employeePhone.isEmpty()) {
            return employeePhone;
        }
        return employeeEmail;
    }

    @Deprecated
    public void setEmployeeContactInfo(String employeeContactInfo) {
        this.employeeContactInfo = employeeContactInfo;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getApprovedByName() {
        return approvedByName;
    }

    public void setApprovedByName(String approvedByName) {
        this.approvedByName = approvedByName;
    }

    // Backward compatibility methods
    @Deprecated
    public String getStatus() {
        return contractStatus;
    }

    @Deprecated
    public void setStatus(String status) {
        this.contractStatus = status;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "contractID=" + contractID +
                ", employeeID=" + employeeID +
                ", contractNumber='" + contractNumber + '\'' +
                ", contractType='" + contractType + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", salaryAmount=" + salaryAmount +
                ", contractStatus='" + contractStatus + '\'' +
                ", signedDate=" + signedDate +
                ", createdAt=" + createdAt +
                '}';
    }
}
