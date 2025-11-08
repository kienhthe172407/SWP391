package model;

import java.util.HashMap;
import java.util.Map;

/**
 * HR Statistics model class
 * Contains various statistics and metrics for HR reporting
 * @author admin
 */
public class HRStatistics {
    
    // Employee Statistics
    private int totalEmployees;
    private int activeEmployees;
    private int terminatedEmployees;
    private int newHiresThisMonth;
    private int newHiresThisYear;
    private Map<String, Integer> employeesByDepartment;
    private Map<String, Integer> employeesByPosition;
    private Map<String, Integer> employeesByStatus;
    
    // Attendance Statistics
    private double averageAttendanceRate;
    private int totalPresentToday;
    private int totalAbsentToday;
    private int totalLateToday;
    private int totalRemoteToday;
    private Map<String, Double> attendanceRateByMonth;
    private Map<String, Integer> attendanceByStatus;
    
    // Recruitment Statistics
    private int totalJobPostings;
    private int activeJobPostings;
    private int totalApplications;
    private int applicationsThisMonth;
    private Map<String, Integer> applicationsByStatus;
    private Map<String, Integer> applicationsByMonth;
    private double averageTimeToHire; // in days
    
    // Task Statistics
    private int totalTasks;
    private int completedTasks;
    private int inProgressTasks;
    private int overdueTasksCount;
    private double taskCompletionRate;
    private Map<String, Integer> tasksByStatus;
    private Map<String, Integer> tasksByPriority;
    
    // Request Statistics
    private int totalRequests;
    private int pendingRequests;
    private int approvedRequestsThisMonth;
    private int rejectedRequestsThisMonth;
    private Map<String, Integer> requestsByType;
    private Map<String, Integer> requestsByStatus;
    
    // Contract Statistics
    private int totalContracts;
    private int activeContracts;
    private int expiringContractsThisMonth;
    private int expiredContracts;
    private Map<String, Integer> contractsByType;
    
    // Salary Statistics
    private double averageSalary;
    private double totalPayrollThisMonth;
    private double totalBonusThisMonth;
    private double totalDeductionThisMonth;
    private Map<String, Double> averageSalaryByDepartment;
    
    // Trend Data (for charts)
    private Map<String, Integer> employeeGrowthByMonth; // Last 12 months
    private Map<String, Integer> hiringTrendByMonth; // Last 12 months
    private Map<String, Double> attendanceTrendByMonth; // Last 6 months
    
    // Constructor
    public HRStatistics() {
        this.employeesByDepartment = new HashMap<>();
        this.employeesByPosition = new HashMap<>();
        this.employeesByStatus = new HashMap<>();
        this.attendanceRateByMonth = new HashMap<>();
        this.attendanceByStatus = new HashMap<>();
        this.applicationsByStatus = new HashMap<>();
        this.applicationsByMonth = new HashMap<>();
        this.tasksByStatus = new HashMap<>();
        this.tasksByPriority = new HashMap<>();
        this.requestsByType = new HashMap<>();
        this.requestsByStatus = new HashMap<>();
        this.contractsByType = new HashMap<>();
        this.averageSalaryByDepartment = new HashMap<>();
        this.employeeGrowthByMonth = new HashMap<>();
        this.hiringTrendByMonth = new HashMap<>();
        this.attendanceTrendByMonth = new HashMap<>();
    }

    // Getters and Setters
    public int getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(int totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public int getActiveEmployees() {
        return activeEmployees;
    }

    public void setActiveEmployees(int activeEmployees) {
        this.activeEmployees = activeEmployees;
    }

    public int getTerminatedEmployees() {
        return terminatedEmployees;
    }

    public void setTerminatedEmployees(int terminatedEmployees) {
        this.terminatedEmployees = terminatedEmployees;
    }

    public int getNewHiresThisMonth() {
        return newHiresThisMonth;
    }

    public void setNewHiresThisMonth(int newHiresThisMonth) {
        this.newHiresThisMonth = newHiresThisMonth;
    }

    public int getNewHiresThisYear() {
        return newHiresThisYear;
    }

    public void setNewHiresThisYear(int newHiresThisYear) {
        this.newHiresThisYear = newHiresThisYear;
    }

    public Map<String, Integer> getEmployeesByDepartment() {
        return employeesByDepartment;
    }

    public void setEmployeesByDepartment(Map<String, Integer> employeesByDepartment) {
        this.employeesByDepartment = employeesByDepartment;
    }

    public Map<String, Integer> getEmployeesByPosition() {
        return employeesByPosition;
    }

    public void setEmployeesByPosition(Map<String, Integer> employeesByPosition) {
        this.employeesByPosition = employeesByPosition;
    }

    public Map<String, Integer> getEmployeesByStatus() {
        return employeesByStatus;
    }

    public void setEmployeesByStatus(Map<String, Integer> employeesByStatus) {
        this.employeesByStatus = employeesByStatus;
    }

    public double getAverageAttendanceRate() {
        return averageAttendanceRate;
    }

    public void setAverageAttendanceRate(double averageAttendanceRate) {
        this.averageAttendanceRate = averageAttendanceRate;
    }

    public int getTotalPresentToday() {
        return totalPresentToday;
    }

    public void setTotalPresentToday(int totalPresentToday) {
        this.totalPresentToday = totalPresentToday;
    }

    public int getTotalAbsentToday() {
        return totalAbsentToday;
    }

    public void setTotalAbsentToday(int totalAbsentToday) {
        this.totalAbsentToday = totalAbsentToday;
    }

    public int getTotalLateToday() {
        return totalLateToday;
    }

    public void setTotalLateToday(int totalLateToday) {
        this.totalLateToday = totalLateToday;
    }

    public int getTotalRemoteToday() {
        return totalRemoteToday;
    }

    public void setTotalRemoteToday(int totalRemoteToday) {
        this.totalRemoteToday = totalRemoteToday;
    }

    public Map<String, Double> getAttendanceRateByMonth() {
        return attendanceRateByMonth;
    }

    public void setAttendanceRateByMonth(Map<String, Double> attendanceRateByMonth) {
        this.attendanceRateByMonth = attendanceRateByMonth;
    }

    public Map<String, Integer> getAttendanceByStatus() {
        return attendanceByStatus;
    }

    public void setAttendanceByStatus(Map<String, Integer> attendanceByStatus) {
        this.attendanceByStatus = attendanceByStatus;
    }

    public int getTotalJobPostings() {
        return totalJobPostings;
    }

    public void setTotalJobPostings(int totalJobPostings) {
        this.totalJobPostings = totalJobPostings;
    }

    public int getActiveJobPostings() {
        return activeJobPostings;
    }

    public void setActiveJobPostings(int activeJobPostings) {
        this.activeJobPostings = activeJobPostings;
    }

    public int getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(int totalApplications) {
        this.totalApplications = totalApplications;
    }

    public int getApplicationsThisMonth() {
        return applicationsThisMonth;
    }

    public void setApplicationsThisMonth(int applicationsThisMonth) {
        this.applicationsThisMonth = applicationsThisMonth;
    }

    public Map<String, Integer> getApplicationsByStatus() {
        return applicationsByStatus;
    }

    public void setApplicationsByStatus(Map<String, Integer> applicationsByStatus) {
        this.applicationsByStatus = applicationsByStatus;
    }

    public Map<String, Integer> getApplicationsByMonth() {
        return applicationsByMonth;
    }

    public void setApplicationsByMonth(Map<String, Integer> applicationsByMonth) {
        this.applicationsByMonth = applicationsByMonth;
    }

    public double getAverageTimeToHire() {
        return averageTimeToHire;
    }

    public void setAverageTimeToHire(double averageTimeToHire) {
        this.averageTimeToHire = averageTimeToHire;
    }

    public int getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    public int getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(int completedTasks) {
        this.completedTasks = completedTasks;
    }

    public int getInProgressTasks() {
        return inProgressTasks;
    }

    public void setInProgressTasks(int inProgressTasks) {
        this.inProgressTasks = inProgressTasks;
    }

    public int getOverdueTasksCount() {
        return overdueTasksCount;
    }

    public void setOverdueTasksCount(int overdueTasksCount) {
        this.overdueTasksCount = overdueTasksCount;
    }

    public double getTaskCompletionRate() {
        return taskCompletionRate;
    }

    public void setTaskCompletionRate(double taskCompletionRate) {
        this.taskCompletionRate = taskCompletionRate;
    }

    public Map<String, Integer> getTasksByStatus() {
        return tasksByStatus;
    }

    public void setTasksByStatus(Map<String, Integer> tasksByStatus) {
        this.tasksByStatus = tasksByStatus;
    }

    public Map<String, Integer> getTasksByPriority() {
        return tasksByPriority;
    }

    public void setTasksByPriority(Map<String, Integer> tasksByPriority) {
        this.tasksByPriority = tasksByPriority;
    }

    public int getTotalRequests() {
        return totalRequests;
    }

    public void setTotalRequests(int totalRequests) {
        this.totalRequests = totalRequests;
    }

    public int getPendingRequests() {
        return pendingRequests;
    }

    public void setPendingRequests(int pendingRequests) {
        this.pendingRequests = pendingRequests;
    }

    public int getApprovedRequestsThisMonth() {
        return approvedRequestsThisMonth;
    }

    public void setApprovedRequestsThisMonth(int approvedRequestsThisMonth) {
        this.approvedRequestsThisMonth = approvedRequestsThisMonth;
    }

    public int getRejectedRequestsThisMonth() {
        return rejectedRequestsThisMonth;
    }

    public void setRejectedRequestsThisMonth(int rejectedRequestsThisMonth) {
        this.rejectedRequestsThisMonth = rejectedRequestsThisMonth;
    }

    public Map<String, Integer> getRequestsByType() {
        return requestsByType;
    }

    public void setRequestsByType(Map<String, Integer> requestsByType) {
        this.requestsByType = requestsByType;
    }

    public Map<String, Integer> getRequestsByStatus() {
        return requestsByStatus;
    }

    public void setRequestsByStatus(Map<String, Integer> requestsByStatus) {
        this.requestsByStatus = requestsByStatus;
    }

    public int getTotalContracts() {
        return totalContracts;
    }

    public void setTotalContracts(int totalContracts) {
        this.totalContracts = totalContracts;
    }

    public int getActiveContracts() {
        return activeContracts;
    }

    public void setActiveContracts(int activeContracts) {
        this.activeContracts = activeContracts;
    }

    public int getExpiringContractsThisMonth() {
        return expiringContractsThisMonth;
    }

    public void setExpiringContractsThisMonth(int expiringContractsThisMonth) {
        this.expiringContractsThisMonth = expiringContractsThisMonth;
    }

    public int getExpiredContracts() {
        return expiredContracts;
    }

    public void setExpiredContracts(int expiredContracts) {
        this.expiredContracts = expiredContracts;
    }

    public Map<String, Integer> getContractsByType() {
        return contractsByType;
    }

    public void setContractsByType(Map<String, Integer> contractsByType) {
        this.contractsByType = contractsByType;
    }

    public double getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(double averageSalary) {
        this.averageSalary = averageSalary;
    }

    public double getTotalPayrollThisMonth() {
        return totalPayrollThisMonth;
    }

    public void setTotalPayrollThisMonth(double totalPayrollThisMonth) {
        this.totalPayrollThisMonth = totalPayrollThisMonth;
    }

    public double getTotalBonusThisMonth() {
        return totalBonusThisMonth;
    }

    public void setTotalBonusThisMonth(double totalBonusThisMonth) {
        this.totalBonusThisMonth = totalBonusThisMonth;
    }

    public double getTotalDeductionThisMonth() {
        return totalDeductionThisMonth;
    }

    public void setTotalDeductionThisMonth(double totalDeductionThisMonth) {
        this.totalDeductionThisMonth = totalDeductionThisMonth;
    }

    public Map<String, Double> getAverageSalaryByDepartment() {
        return averageSalaryByDepartment;
    }

    public void setAverageSalaryByDepartment(Map<String, Double> averageSalaryByDepartment) {
        this.averageSalaryByDepartment = averageSalaryByDepartment;
    }

    public Map<String, Integer> getEmployeeGrowthByMonth() {
        return employeeGrowthByMonth;
    }

    public void setEmployeeGrowthByMonth(Map<String, Integer> employeeGrowthByMonth) {
        this.employeeGrowthByMonth = employeeGrowthByMonth;
    }

    public Map<String, Integer> getHiringTrendByMonth() {
        return hiringTrendByMonth;
    }

    public void setHiringTrendByMonth(Map<String, Integer> hiringTrendByMonth) {
        this.hiringTrendByMonth = hiringTrendByMonth;
    }

    public Map<String, Double> getAttendanceTrendByMonth() {
        return attendanceTrendByMonth;
    }

    public void setAttendanceTrendByMonth(Map<String, Double> attendanceTrendByMonth) {
        this.attendanceTrendByMonth = attendanceTrendByMonth;
    }

    @Override
    public String toString() {
        return "HRStatistics{" +
                "totalEmployees=" + totalEmployees +
                ", activeEmployees=" + activeEmployees +
                ", totalJobPostings=" + totalJobPostings +
                ", totalApplications=" + totalApplications +
                ", averageAttendanceRate=" + averageAttendanceRate +
                ", taskCompletionRate=" + taskCompletionRate +
                '}';
    }
}

