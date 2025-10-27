package model;

/**
 * MonthlyAttendanceSummary model class
 * Represents monthly attendance summary data from vw_monthly_attendance_summary view
 * Used for displaying aggregated monthly attendance statistics per employee
 */
public class MonthlyAttendanceSummary {
    // Employee information
    private int employeeId;
    private String employeeCode;
    private String employeeName;
    
    // Department information
    private String departmentName;
    
    // Month information (format: YYYY-MM)
    private String attendanceMonth;
    
    // Attendance statistics
    private int presentDays;
    private int absentDays;
    private int lateDays;
    private int earlyLeaveDays;
    private int businessTripDays;
    private int remoteDays;
    private double totalOvertimeHours;
    private int totalWorkingDays;
    
    // Default constructor
    public MonthlyAttendanceSummary() {
    }
    
    // Constructor with basic info
    public MonthlyAttendanceSummary(int employeeId, String employeeCode, String employeeName, 
                                   String departmentName, String attendanceMonth) {
        this.employeeId = employeeId;
        this.employeeCode = employeeCode;
        this.employeeName = employeeName;
        this.departmentName = departmentName;
        this.attendanceMonth = attendanceMonth;
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
    
    public String getAttendanceMonth() {
        return attendanceMonth;
    }
    
    public void setAttendanceMonth(String attendanceMonth) {
        this.attendanceMonth = attendanceMonth;
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
    
    public int getTotalWorkingDays() {
        return totalWorkingDays;
    }
    
    public void setTotalWorkingDays(int totalWorkingDays) {
        this.totalWorkingDays = totalWorkingDays;
    }
    
    // Calculated properties
    
    /**
     * Get actual working days (Present + Late + Remote + Business Trip)
     * @return int working days count
     */
    public int getActualWorkingDays() {
        return presentDays + lateDays + remoteDays + businessTripDays;
    }
    
    /**
     * Get attendance rate as percentage
     * @return double attendance rate (0-100)
     */
    public double getAttendanceRate() {
        if (totalWorkingDays == 0) return 0.0;
        return (double) getActualWorkingDays() / totalWorkingDays * 100;
    }
    
    /**
     * Get late rate as percentage
     * @return double late rate (0-100)
     */
    public double getLateRate() {
        if (totalWorkingDays == 0) return 0.0;
        return (double) lateDays / totalWorkingDays * 100;
    }
    
    /**
     * Get absent rate as percentage
     * @return double absent rate (0-100)
     */
    public double getAbsentRate() {
        if (totalWorkingDays == 0) return 0.0;
        return (double) absentDays / totalWorkingDays * 100;
    }
    
    @Override
    public String toString() {
        return "MonthlyAttendanceSummary{" +
                "employeeId=" + employeeId +
                ", employeeCode='" + employeeCode + '\'' +
                ", employeeName='" + employeeName + '\'' +
                ", departmentName='" + departmentName + '\'' +
                ", attendanceMonth='" + attendanceMonth + '\'' +
                ", presentDays=" + presentDays +
                ", absentDays=" + absentDays +
                ", lateDays=" + lateDays +
                ", totalOvertimeHours=" + totalOvertimeHours +
                ", totalWorkingDays=" + totalWorkingDays +
                '}';
    }
}

