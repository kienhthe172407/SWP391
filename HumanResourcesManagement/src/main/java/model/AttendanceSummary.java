package model;

/**
 * AttendanceSummary model class
 * Holds summary statistics for attendance records
 * Used for displaying aggregated attendance data by employee, department, or date range
 */
public class AttendanceSummary {
    // Employee information
    private Integer employeeID;
    private String employeeCode;
    private String employeeName;
    
    // Department information
    private Integer departmentID;
    private String departmentName;
    
    // Date range
    private String startDate;
    private String endDate;
    
    // Summary statistics
    private int totalDays;              // Total attendance records
    private int presentDays;            // Days marked as Present
    private int absentDays;             // Days marked as Absent
    private int lateDays;               // Days marked as Late
    private int earlyLeaveDays;         // Days marked as Early Leave
    private int businessTripDays;       // Days marked as Business Trip
    private int remoteDays;             // Days marked as Remote
    private double totalOvertimeHours;  // Sum of overtime hours
    private int manualAdjustments;      // Count of manually adjusted records
    
    // Default constructor
    public AttendanceSummary() {
    }
    
    // Constructor with basic info
    public AttendanceSummary(Integer employeeID, String employeeCode, String employeeName) {
        this.employeeID = employeeID;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
    }
    
    // Getters and Setters
    public Integer getEmployeeID() {
        return employeeID;
    }
    
    public void setEmployeeID(Integer employeeID) {
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
    
    public Integer getDepartmentID() {
        return departmentID;
    }
    
    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }
    
    public String getDepartmentName() {
        return departmentName;
    }
    
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    
    public String getStartDate() {
        return startDate;
    }
    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    
    public String getEndDate() {
        return endDate;
    }
    
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    
    public int getTotalDays() {
        return totalDays;
    }
    
    public void setTotalDays(int totalDays) {
        this.totalDays = totalDays;
    }
    
    public int getPresentDays() {
        return presentDays;
    }
    
    public void setPresentDays(int presentDays) {
        this.presentDays = presentDays;
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
    
    public int getEarlyLeaveDays() {
        return earlyLeaveDays;
    }
    
    public void setEarlyLeaveDays(int earlyLeaveDays) {
        this.earlyLeaveDays = earlyLeaveDays;
    }
    
    public int getBusinessTripDays() {
        return businessTripDays;
    }
    
    public void setBusinessTripDays(int businessTripDays) {
        this.businessTripDays = businessTripDays;
    }
    
    public int getRemoteDays() {
        return remoteDays;
    }
    
    public void setRemoteDays(int remoteDays) {
        this.remoteDays = remoteDays;
    }
    
    public double getTotalOvertimeHours() {
        return totalOvertimeHours;
    }
    
    public void setTotalOvertimeHours(double totalOvertimeHours) {
        this.totalOvertimeHours = totalOvertimeHours;
    }
    
    public int getManualAdjustments() {
        return manualAdjustments;
    }
    
    public void setManualAdjustments(int manualAdjustments) {
        this.manualAdjustments = manualAdjustments;
    }
    
    // Calculated properties
    
    /**
     * Get working days (Present + Late + Remote + Business Trip)
     * @return int working days count
     */
    public int getWorkingDays() {
        return presentDays + lateDays + remoteDays + businessTripDays;
    }
    
    /**
     * Get attendance rate as percentage
     * @return double attendance rate (0-100)
     */
    public double getAttendanceRate() {
        if (totalDays == 0) return 0.0;
        return (double) getWorkingDays() / totalDays * 100;
    }
    
    /**
     * Get late rate as percentage
     * @return double late rate (0-100)
     */
    public double getLateRate() {
        if (totalDays == 0) return 0.0;
        return (double) lateDays / totalDays * 100;
    }
    
    @Override
    public String toString() {
        return "AttendanceSummary{" +
                "employeeCode='" + employeeCode + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", totalDays=" + totalDays +
                ", presentDays=" + presentDays +
                ", absentDays=" + absentDays +
                ", lateDays=" + lateDays +
                ", totalOvertimeHours=" + totalOvertimeHours +
                '}';
    }
}

