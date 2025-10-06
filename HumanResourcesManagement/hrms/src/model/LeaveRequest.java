package com.hrms.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "LeaveRequest")
public class LeaveRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer requestID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeID")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LeaveTypeID")
    private LeaveType leaveType;

    @Column(name = "StartDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "EndDate", nullable = false)
    private LocalDate endDate;

    @Column(name = "TotalDays", nullable = false)
    private Double totalDays;

    @Column(name = "Reason")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private LeaveStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ApprovedBy")
    private Employee approvedBy; // Người duyệt

    public enum LeaveStatus {
        Pending, Approved, Rejected, Canceled
    }




    public Integer getRequestID() {
        return requestID;
    }

    public void setRequestID(Integer requestID) {
        this.requestID = requestID;
    }

    public Employee getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Employee approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Double getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(Double totalDays) {
        this.totalDays = totalDays;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}