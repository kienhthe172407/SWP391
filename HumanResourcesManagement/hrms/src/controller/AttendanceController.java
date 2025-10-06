package com.hrms.controller;

import com.hrms.model.TimeLog;
import com.hrms.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // POST /api/attendance/checkin/1
    @PostMapping("/checkin/{employeeId}")
    public ResponseEntity<TimeLog> checkIn(@PathVariable Integer employeeId) {
        try {
            TimeLog log = attendanceService.checkIn(employeeId);
            return ResponseEntity.ok(log);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // POST /api/attendance/checkout/1
    @PostMapping("/checkout/{employeeId}")
    public ResponseEntity<TimeLog> checkOut(@PathVariable Integer employeeId) {
        try {
            TimeLog log = attendanceService.checkOut(employeeId);
            return ResponseEntity.ok(log);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // TODO: Thêm API cho Quản lý Lịch làm việc (ShiftSchedule)
}