package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * SalaryCalculationDetail - Helper class to store detailed breakdown of salary calculation
 * This shows WHY an employee has a specific salary amount
 * @author admin
 */
public class SalaryCalculationDetail {
    private int employeeID;
    private String employeeCode;
    private String employeeName;
    private String departmentName;
    private String positionName;
    
    // Base components
    private BigDecimal baseSalary;
    private BigDecimal positionAllowance;
    private BigDecimal housingAllowance;
    private BigDecimal transportationAllowance;
    private BigDecimal mealAllowance;
    private BigDecimal otherAllowances;
    
    // Attendance-based
    private int workingDays;
    private int absentDays;
    private int lateDays;
    private BigDecimal overtimeHours;
    private BigDecimal overtimePay;
    
    // Benefits and Deductions
    private List<BenefitDetail> benefits;
    private List<DeductionDetail> deductions;
    
    // Totals
    private BigDecimal totalAllowances;
    private BigDecimal totalBenefits;
    private BigDecimal totalDeductions;
    private BigDecimal grossSalary;
    private BigDecimal netSalary;
    
    // Calculation notes
    private List<String> calculationNotes;
    
    public SalaryCalculationDetail() {
        this.benefits = new ArrayList<>();
        this.deductions = new ArrayList<>();
        this.calculationNotes = new ArrayList<>();
        this.baseSalary = BigDecimal.ZERO;
        this.positionAllowance = BigDecimal.ZERO;
        this.housingAllowance = BigDecimal.ZERO;
        this.transportationAllowance = BigDecimal.ZERO;
        this.mealAllowance = BigDecimal.ZERO;
        this.otherAllowances = BigDecimal.ZERO;
        this.overtimeHours = BigDecimal.ZERO;
        this.overtimePay = BigDecimal.ZERO;
        this.totalAllowances = BigDecimal.ZERO;
        this.totalBenefits = BigDecimal.ZERO;
        this.totalDeductions = BigDecimal.ZERO;
        this.grossSalary = BigDecimal.ZERO;
        this.netSalary = BigDecimal.ZERO;
    }
    
    // Inner class for benefit details
    public static class BenefitDetail {
        private String benefitName;
        private String calculationType;
        private BigDecimal amount;
        private String description;
        
        public BenefitDetail(String benefitName, String calculationType, BigDecimal amount, String description) {
            this.benefitName = benefitName;
            this.calculationType = calculationType;
            this.amount = amount;
            this.description = description;
        }
        
        public String getBenefitName() { return benefitName; }
        public String getCalculationType() { return calculationType; }
        public BigDecimal getAmount() { return amount; }
        public String getDescription() { return description; }
    }
    
    // Inner class for deduction details
    public static class DeductionDetail {
        private String deductionName;
        private String calculationType;
        private BigDecimal amount;
        private String description;
        
        public DeductionDetail(String deductionName, String calculationType, BigDecimal amount, String description) {
            this.deductionName = deductionName;
            this.calculationType = calculationType;
            this.amount = amount;
            this.description = description;
        }
        
        public String getDeductionName() { return deductionName; }
        public String getCalculationType() { return calculationType; }
        public BigDecimal getAmount() { return amount; }
        public String getDescription() { return description; }
    }
    
    // Getters and Setters
    public int getEmployeeID() {
        return employeeID;
    }
    
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
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
    
    public BigDecimal getBaseSalary() {
        return baseSalary;
    }
    
    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }
    
    public BigDecimal getPositionAllowance() {
        return positionAllowance;
    }
    
    public void setPositionAllowance(BigDecimal positionAllowance) {
        this.positionAllowance = positionAllowance;
    }
    
    public BigDecimal getHousingAllowance() {
        return housingAllowance;
    }
    
    public void setHousingAllowance(BigDecimal housingAllowance) {
        this.housingAllowance = housingAllowance;
    }
    
    public BigDecimal getTransportationAllowance() {
        return transportationAllowance;
    }
    
    public void setTransportationAllowance(BigDecimal transportationAllowance) {
        this.transportationAllowance = transportationAllowance;
    }
    
    public BigDecimal getMealAllowance() {
        return mealAllowance;
    }
    
    public void setMealAllowance(BigDecimal mealAllowance) {
        this.mealAllowance = mealAllowance;
    }
    
    public BigDecimal getOtherAllowances() {
        return otherAllowances;
    }
    
    public void setOtherAllowances(BigDecimal otherAllowances) {
        this.otherAllowances = otherAllowances;
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
    
    public BigDecimal getOvertimePay() {
        return overtimePay;
    }
    
    public void setOvertimePay(BigDecimal overtimePay) {
        this.overtimePay = overtimePay;
    }
    
    public List<BenefitDetail> getBenefits() {
        return benefits;
    }
    
    public void setBenefits(List<BenefitDetail> benefits) {
        this.benefits = benefits;
    }
    
    public void addBenefit(BenefitDetail benefit) {
        this.benefits.add(benefit);
    }
    
    public List<DeductionDetail> getDeductions() {
        return deductions;
    }
    
    public void setDeductions(List<DeductionDetail> deductions) {
        this.deductions = deductions;
    }
    
    public void addDeduction(DeductionDetail deduction) {
        this.deductions.add(deduction);
    }
    
    public BigDecimal getTotalAllowances() {
        return totalAllowances;
    }
    
    public void setTotalAllowances(BigDecimal totalAllowances) {
        this.totalAllowances = totalAllowances;
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
    
    public List<String> getCalculationNotes() {
        return calculationNotes;
    }
    
    public void setCalculationNotes(List<String> calculationNotes) {
        this.calculationNotes = calculationNotes;
    }
    
    public void addCalculationNote(String note) {
        this.calculationNotes.add(note);
    }
}

