package com.hrms.model;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "ShiftSchedule")
public class ShiftSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer shiftID;

    @Column(name = "ShiftName", nullable = false, unique = true)
    private String shiftName;

    @Column(name = "StartTime", nullable = false)
    private LocalTime startTime;

    @Column(name = "EndTime", nullable = false)
    private LocalTime endTime;

    @Column(name = "DurationHours")
    private Double durationHours;

    @Column(name = "Description")
    private String description;



    public Integer getShiftID() {
        return shiftID;
    }

    public void setShiftID(Integer shiftID) {
        this.shiftID = shiftID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(Double durationHours) {
        this.durationHours = durationHours;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }
}