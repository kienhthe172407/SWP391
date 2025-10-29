package model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 * View model for salary summary display
 * Combines data from monthly_payroll, employees, and bonus_adjustments
 * @author admin
 */
public class SalarySummaryView {
    
    // Employee information
    private int employeeId;
    private String employeeCode;
    private String employeeName;
    private String departmentName;
    private String positionName;
    
    // Payroll information
    private int payrollId;
    private Date payrollMonth;
    private BigDecimal baseSalary;
    private BigDecimal totalAllowances;
    private BigDecimal overtimePay;
    private BigDecimal totalBonus;
    private BigDecimal totalBenefits;
    private BigDecimal totalDeductions;
    private BigDecimal grossSalary;
    private BigDecimal netSalary;
    private int workingDays;
    private int absentDays;
    private int lateDays;
    private BigDecimal overtimeHours;
    private String status;
    
    // Bonus adjustment information
    private BigDecimal bonusAdjustments;
    private int pendingAdjustmentsCount;
    
    // Constructors
    public SalarySummaryView() {
        this.bonusAdjustments = BigDecimal.ZERO;
        this.pendingAdjustmentsCount = 0;
    }
    
    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
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
    
    public int getPayrollId() {
        return payrollId;
    }
    
    public void setPayrollId(int payrollId) {
        this.payrollId = payrollId;
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
    
    public BigDecimal getBonusAdjustments() {
        return bonusAdjustments;
    }
    
    public void setBonusAdjustments(BigDecimal bonusAdjustments) {
        this.bonusAdjustments = bonusAdjustments;
    }
    
    public int getPendingAdjustmentsCount() {
        return pendingAdjustmentsCount;
    }
    
    public void setPendingAdjustmentsCount(int pendingAdjustmentsCount) {
        this.pendingAdjustmentsCount = pendingAdjustmentsCount;
    }
    
    // Utility methods
    public boolean hasPendingAdjustments() {
        return pendingAdjustmentsCount > 0;
    }
    
    public boolean hasBonusAdjustments() {
        return bonusAdjustments != null && bonusAdjustments.compareTo(BigDecimal.ZERO) != 0;
    }
    
    public BigDecimal getTotalBonusWithAdjustments() {
        if (totalBonus == null) {
            totalBonus = BigDecimal.ZERO;
        }
        if (bonusAdjustments == null) {
            bonusAdjustments = BigDecimal.ZERO;
        }
        return totalBonus.add(bonusAdjustments);
    }
}

