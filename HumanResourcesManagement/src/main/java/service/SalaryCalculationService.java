package service;

import dal.*;
import model.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for salary calculation business logic
 * Handles the complex calculation of monthly salaries based on:
 * - Base salary and allowances
 * - Attendance records (working days, overtime)
 * - Configured benefits
 * - Configured deductions
 * @author admin
 */
public class SalaryCalculationService {
    
    private SalaryComponentDAO salaryComponentDAO;
    private AttendanceDAO attendanceDAO;
    private SalaryConfigDAO salaryConfigDAO;
    private EmployeeDAO employeeDAO;
    
    // Constants for calculation
    private static final int STANDARD_WORKING_DAYS = 22;
    private static final int STANDARD_WORKING_HOURS_PER_DAY = 8;
    private static final BigDecimal OVERTIME_MULTIPLIER = new BigDecimal("1.5");
    
    public SalaryCalculationService() {
        this.salaryComponentDAO = new SalaryComponentDAO();
        this.attendanceDAO = new AttendanceDAO();
        this.salaryConfigDAO = new SalaryConfigDAO();
        this.employeeDAO = new EmployeeDAO();
    }
    
    /**
     * Calculate salary for a single employee for a specific month
     * @param employeeID Employee ID
     * @param year Year (e.g., 2025)
     * @param month Month (1-12)
     * @return SalaryCalculationDetail with complete breakdown
     */
    public SalaryCalculationDetail calculateEmployeeSalary(int employeeID, int year, int month) {
        SalaryCalculationDetail detail = new SalaryCalculationDetail();
        detail.setEmployeeID(employeeID);
        
        // Get employee information
        Employee employee = employeeDAO.getEmployeeById(employeeID);
        if (employee == null) {
            detail.addCalculationNote("ERROR: Employee not found");
            return detail;
        }
        
        detail.setEmployeeCode(employee.getEmployeeCode());
        detail.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
        detail.setDepartmentName(employee.getDepartmentName());
        detail.setPositionName(employee.getPositionName());
        
        // Get active salary component
        SalaryComponent salaryComponent = salaryComponentDAO.getActiveSalaryComponent(employeeID);
        if (salaryComponent == null) {
            detail.addCalculationNote("ERROR: No active salary component found for this employee");
            return detail;
        }
        
        // Set base salary and allowances
        detail.setBaseSalary(salaryComponent.getBaseSalary());
        detail.setPositionAllowance(salaryComponent.getPositionAllowance());
        detail.setHousingAllowance(salaryComponent.getHousingAllowance());
        detail.setTransportationAllowance(salaryComponent.getTransportationAllowance());
        detail.setMealAllowance(salaryComponent.getMealAllowance());
        detail.setOtherAllowances(salaryComponent.getOtherAllowances());
        
        // Calculate total allowances
        BigDecimal totalAllowances = salaryComponent.getPositionAllowance()
                .add(salaryComponent.getHousingAllowance())
                .add(salaryComponent.getTransportationAllowance())
                .add(salaryComponent.getMealAllowance())
                .add(salaryComponent.getOtherAllowances());
        detail.setTotalAllowances(totalAllowances);
        
        // Get attendance data for the month
        Date startDate = Date.valueOf(String.format("%d-%02d-01", year, month));
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(startDate);
        cal.set(java.util.Calendar.DAY_OF_MONTH, cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
        Date endDate = new Date(cal.getTimeInMillis());
        
        List<AttendanceRecord> attendanceRecords = attendanceDAO.getAttendanceByEmployeeAndDateRange(
                employeeID, startDate, endDate);
        
        // Calculate attendance statistics
        int workingDays = 0;
        int absentDays = 0;
        int lateDays = 0;
        BigDecimal totalOvertimeHours = BigDecimal.ZERO;
        
        for (AttendanceRecord record : attendanceRecords) {
            String status = record.getStatus();
            if ("Present".equals(status) || "Late".equals(status) || 
                "Remote".equals(status) || "Business Trip".equals(status)) {
                workingDays++;
            }
            if ("Absent".equals(status)) {
                absentDays++;
            }
            if ("Late".equals(status)) {
                lateDays++;
            }
            if (record.getOvertimeHours() != null && record.getOvertimeHours() > 0) {
                totalOvertimeHours = totalOvertimeHours.add(BigDecimal.valueOf(record.getOvertimeHours()));
            }
        }
        
        detail.setWorkingDays(workingDays);
        detail.setAbsentDays(absentDays);
        detail.setLateDays(lateDays);
        detail.setOvertimeHours(totalOvertimeHours);
        
        // Calculate overtime pay
        // Formula: (Base Salary / (Standard Days * 8 hours)) * 1.5 * Overtime Hours
        BigDecimal hourlyRate = salaryComponent.getBaseSalary()
                .divide(new BigDecimal(STANDARD_WORKING_DAYS * STANDARD_WORKING_HOURS_PER_DAY), 
                        2, RoundingMode.HALF_UP);
        BigDecimal overtimePay = hourlyRate
                .multiply(OVERTIME_MULTIPLIER)
                .multiply(totalOvertimeHours)
                .setScale(2, RoundingMode.HALF_UP);
        detail.setOvertimePay(overtimePay);
        
        if (totalOvertimeHours.compareTo(BigDecimal.ZERO) > 0) {
            detail.addCalculationNote(String.format("Overtime: %.2f hours × $%.2f/hour × 1.5 = $%.2f",
                    totalOvertimeHours, hourlyRate, overtimePay));
        }
        
        // Calculate benefits
        List<BenefitType> benefits = salaryConfigDAO.getAllBenefitTypes();
        BigDecimal totalBenefits = BigDecimal.ZERO;
        
        for (BenefitType benefit : benefits) {
            BigDecimal benefitAmount = calculateBenefitAmount(benefit, salaryComponent.getBaseSalary());
            if (benefitAmount.compareTo(BigDecimal.ZERO) > 0) {
                detail.addBenefit(new SalaryCalculationDetail.BenefitDetail(
                        benefit.getBenefitName(),
                        benefit.getCalculationType(),
                        benefitAmount,
                        benefit.getDescription()
                ));
                totalBenefits = totalBenefits.add(benefitAmount);
            }
        }
        detail.setTotalBenefits(totalBenefits);
        
        // Calculate gross salary
        BigDecimal grossSalary = salaryComponent.getBaseSalary()
                .add(totalAllowances)
                .add(overtimePay)
                .add(totalBenefits);
        detail.setGrossSalary(grossSalary);
        
        // Calculate deductions
        List<DeductionType> deductions = salaryConfigDAO.getAllDeductionTypes();
        BigDecimal totalDeductions = BigDecimal.ZERO;
        
        for (DeductionType deduction : deductions) {
            BigDecimal deductionAmount = calculateDeductionAmount(deduction, grossSalary);
            if (deductionAmount.compareTo(BigDecimal.ZERO) > 0) {
                detail.addDeduction(new SalaryCalculationDetail.DeductionDetail(
                        deduction.getDeductionName(),
                        deduction.getCalculationType(),
                        deductionAmount,
                        deduction.getDescription()
                ));
                totalDeductions = totalDeductions.add(deductionAmount);
            }
        }
        detail.setTotalDeductions(totalDeductions);
        
        // Calculate net salary
        BigDecimal netSalary = grossSalary.subtract(totalDeductions);
        detail.setNetSalary(netSalary);
        
        // Add summary note
        detail.addCalculationNote(String.format("Working Days: %d/%d, Absent: %d, Late: %d",
                workingDays, STANDARD_WORKING_DAYS, absentDays, lateDays));
        
        return detail;
    }
    
    /**
     * Calculate benefit amount based on benefit type configuration
     */
    private BigDecimal calculateBenefitAmount(BenefitType benefit, BigDecimal baseSalary) {
        String calcType = benefit.getCalculationType();
        
        if ("Fixed".equals(calcType) && benefit.getDefaultAmount() != null) {
            return BigDecimal.valueOf(benefit.getDefaultAmount()).setScale(2, RoundingMode.HALF_UP);
        } else if ("Percentage".equals(calcType) && benefit.getDefaultPercentage() != null) {
            return baseSalary
                    .multiply(BigDecimal.valueOf(benefit.getDefaultPercentage()))
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Calculate deduction amount based on deduction type configuration
     */
    private BigDecimal calculateDeductionAmount(DeductionType deduction, BigDecimal grossSalary) {
        String calcType = deduction.getCalculationType();
        
        if ("Fixed".equals(calcType) && deduction.getDefaultAmount() != null) {
            return BigDecimal.valueOf(deduction.getDefaultAmount()).setScale(2, RoundingMode.HALF_UP);
        } else if ("Percentage".equals(calcType) && deduction.getDefaultPercentage() != null) {
            return grossSalary
                    .multiply(BigDecimal.valueOf(deduction.getDefaultPercentage()))
                    .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }
        
        return BigDecimal.ZERO;
    }
    
    /**
     * Calculate salaries for all employees with active salary components
     * @param year Year
     * @param month Month
     * @return List of SalaryCalculationDetail
     */
    public List<SalaryCalculationDetail> calculateAllEmployeesSalary(int year, int month) {
        List<SalaryCalculationDetail> calculations = new ArrayList<>();
        
        // Get all active employees with salary components
        List<SalaryComponent> activeComponents = salaryComponentDAO.getAllActiveSalaryComponents();
        
        for (SalaryComponent component : activeComponents) {
            SalaryCalculationDetail detail = calculateEmployeeSalary(component.getEmployeeID(), year, month);
            calculations.add(detail);
        }
        
        return calculations;
    }
}

