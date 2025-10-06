package com.hrms.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "TimeLog", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"EmployeeID", "LogDate"})
})
public class TimeLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer logID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employee; // Liên kết với bảng Employee

    @Column(name = "LogDate", nullable = false)
    private LocalDate logDate;

    @Column(name = "CheckInTime")
    private LocalTime checkInTime;

    @Column(name = "CheckOutTime")
    private LocalTime checkOutTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private TimeLogStatus status;

    @Column(name = "Notes")
    private String notes;

    public enum TimeLogStatus {
        OnTime, Late, EarlyLeave, Overtime
    }



    public Integer getLogID() {
        return logID;
    }

    public void setLogID(Integer logID) {
        this.logID = logID;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public TimeLogStatus getStatus() {
        return status;
    }

    public void setStatus(TimeLogStatus status) {
        this.status = status;
    }

    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}