package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * MonthlyPayroll model class - mapped to monthly_payroll table
 * Represents monthly salary calculation for an employee
 * @author admin
 */
public class MonthlyPayroll {
    // Primary fields from monthly_payroll table
    private int payrollID;                      // payroll_id (Primary Key)
    private int employeeID;                     // employee_id (Foreign Key)
    private Date payrollMonth;                  // payroll_month (YYYY-MM-01)
    private BigDecimal baseSalary;              // base_salary
    private BigDecimal totalAllowances;         // total_allowances
    private BigDecimal overtimePay;             // overtime_pay
    private BigDecimal totalBonus;              // total_bonus
    private BigDecimal totalBenefits;           // total_benefits
    private BigDecimal totalDeductions;         // total_deductions
    private BigDecimal grossSalary;             // gross_salary
    private BigDecimal netSalary;               // net_salary
    private int workingDays;                    // working_days
    private int absentDays;                     // absent_days
    private int lateDays;                       // late_days
    private BigDecimal overtimeHours;           // overtime_hours
    private String status;                      // status (Draft, Calculated, Approved, Paid)
    private Timestamp calculatedAt;             // calculated_at
    private Integer calculatedBy;               // calculated_by (user_id)
    private Timestamp approvedAt;               // approved_at
    private Integer approvedBy;                 // approved_by (user_id)
    private Timestamp paidAt;                   // paid_at
    private String notes;                       // notes
    private Timestamp createdAt;                // created_at
    private Timestamp updatedAt;                // updated_at
    
    // Additional fields for display (from JOINs)
    private String employeeCode;
    private String employeeName;
    private String departmentName;
    private String positionName;
    private String calculatedByName;
    private String approvedByName;
    
    // Default constructor
    public MonthlyPayroll() {
        this.totalAllowances = BigDecimal.ZERO;
        this.overtimePay = BigDecimal.ZERO;
        this.totalBonus = BigDecimal.ZERO;
        this.totalBenefits = BigDecimal.ZERO;
        this.totalDeductions = BigDecimal.ZERO;
        this.workingDays = 0;
        this.absentDays = 0;
        this.lateDays = 0;
        this.overtimeHours = BigDecimal.ZERO;
        this.status = "Draft";
    }
    
    // Constructor for creating new payroll record
    public MonthlyPayroll(int employeeID, Date payrollMonth, BigDecimal baseSalary,
                         BigDecimal totalAllowances, BigDecimal overtimePay,
                         BigDecimal totalBonus, BigDecimal totalBenefits,
                         BigDecimal totalDeductions, BigDecimal grossSalary,
                         BigDecimal netSalary, int workingDays, int absentDays,
                         int lateDays, BigDecimal overtimeHours, String status,
                         Integer calculatedBy) {
        this.employeeID = employeeID;
        this.payrollMonth = payrollMonth;
        this.baseSalary = baseSalary;
        this.totalAllowances = totalAllowances;
        this.overtimePay = overtimePay;
        this.totalBonus = totalBonus;
        this.totalBenefits = totalBenefits;
        this.totalDeductions = totalDeductions;
        this.grossSalary = grossSalary;
        this.netSalary = netSalary;
        this.workingDays = workingDays;
        this.absentDays = absentDays;
        this.lateDays = lateDays;
        this.overtimeHours = overtimeHours;
        this.status = status;
        this.calculatedBy = calculatedBy;
    }
    
    // Getters and Setters
    public int getPayrollID() {
        return payrollID;
    }
    
    public void setPayrollID(int payrollID) {
        this.payrollID = payrollID;
    }
    
    public int getEmployeeID() {
        return employeeID;
    }
    
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }
    
    public Date getPayrollMonth() {
        return payrollMonth;
    }
    
    public void setPayrollMonth(Date payrollMonth) {
        this.payrollMonth = payrollMonth;
    }
    
    public BigDecimal getBaseSalary() {
        return baseSalary;
    }
    
    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }
    
    public BigDecimal getTotalAllowances() {
        return totalAllowances;
    }
    
    public void setTotalAllowances(BigDecimal totalAllowances) {
        this.totalAllowances = totalAllowances;
    }
    
    public BigDecimal getOvertimePay() {
        return overtimePay;
    }
    
    public void setOvertimePay(BigDecimal overtimePay) {
        this.overtimePay = overtimePay;
    }
    
    public BigDecimal getTotalBonus() {
        return totalBonus;
    }
    
    public void setTotalBonus(BigDecimal totalBonus) {
        this.totalBonus = totalBonus;
    }
    
    public BigDecimal getTotalBenefits() {
        return totalBenefits;
    }
    
    public void setTotalBenefits(BigDecimal totalBenefits) {
        this.totalBenefits = totalBenefits;
    }
    
    public BigDecimal getTotalDeductions() {
        return totalDeductions;
    }
    
    public void setTotalDeductions(BigDecimal totalDeductions) {
        this.totalDeductions = totalDeductions;
    }
    
    public BigDecimal getGrossSalary() {
        return grossSalary;
    }
    
    public void setGrossSalary(BigDecimal grossSalary) {
        this.grossSalary = grossSalary;
    }
    
    public BigDecimal getNetSalary() {
        return netSalary;
    }
    
    public void setNetSalary(BigDecimal netSalary) {
        this.netSalary = netSalary;
    }
    
    public int getWorkingDays() {
        return workingDays;
    }
    
    public void setWorkingDays(int workingDays) {
        this.workingDays = workingDays;
    }
    
    public int getAbsentDays() {
        return absentDays;
    }
    
    public void setAbsentDays(int absentDays) {
        this.absentDays = absentDays;
    }
    
    public int getLateDays() {
        return lateDays;
    }
    
    public void setLateDays(int lateDays) {
        this.lateDays = lateDays;
    }
    
    public BigDecimal getOvertimeHours() {
        return overtimeHours;
    }
    
    public void setOvertimeHours(BigDecimal overtimeHours) {
        this.overtimeHours = overtimeHours;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getCalculatedAt() {
        return calculatedAt;
    }
    
    public void setCalculatedAt(Timestamp calculatedAt) {
        this.calculatedAt = calculatedAt;
    }
    
    public Integer getCalculatedBy() {
        return calculatedBy;
    }
    
    public void setCalculatedBy(Integer calculatedBy) {
        this.calculatedBy = calculatedBy;
    }
    
    public Timestamp getApprovedAt() {
        return approvedAt;
    }
    
    public void setApprovedAt(Timestamp approvedAt) {
        this.approvedAt = approvedAt;
    }
    
    public Integer getApprovedBy() {
        return approvedBy;
    }
    
    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }
    
    public Timestamp getPaidAt() {
        return paidAt;
    }
    
    public void setPaidAt(Timestamp paidAt) {
        this.paidAt = paidAt;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
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
    
    public String getDepartmentName() {
        return departmentName;
    }
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    public String getPositionName() {
        return positionName;
    }
    
    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
    
    public String getCalculatedByName() {
        return calculatedByName;
    }
    
    public void setCalculatedByName(String calculatedByName) {
        this.calculatedByName = calculatedByName;
    }
    
    public String getApprovedByName() {
        return approvedByName;
    }
    
    public void setApprovedByName(String approvedByName) {
        this.approvedByName = approvedByName;
    }
}

