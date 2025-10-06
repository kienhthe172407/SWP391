package com.hrms.service;

import com.hrms.model.TimeLog;
import com.hrms.model.Employee;
import com.hrms.repository.TimeLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private TimeLogRepository timeLogRepository;

    // Lấy ca làm việc chuẩn
    private LocalTime getStandardCheckInTime(Integer employeeId, LocalDate date) {
        // TODO: Thực hiện logic tra cứu ShiftSchedule và EmployeeShift
        // hardcode: 9:00 AM
        return LocalTime.of(9, 0);
    }

    private LocalTime getStandardCheckOutTime(Integer employeeId, LocalDate date) {
        // Tạm thời hardcode cho ví dụ: 6:00 PM
        return LocalTime.of(18, 0);
    }


    public TimeLog checkIn(Integer employeeId) {
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // Kiểm tra đã Check-in hôm nay chưa
        Optional<TimeLog> existingLog = timeLogRepository.findByEmployeeEmployeeIDAndLogDate(employeeId, today);

        if (existingLog.isPresent()) {
            throw new IllegalStateException("Employee already checked in today.");
        }

        // Xác định trạng thái Check-in (OnTime/Late)
        LocalTime standardCheckIn = getStandardCheckInTime(employeeId, today);
        TimeLog.TimeLogStatus status = currentTime.isAfter(standardCheckIn.plusMinutes(10)) ? // Cho phép trễ 10 phút
                TimeLog.TimeLogStatus.Late : TimeLog.TimeLogStatus.OnTime;

        // Tạo và lưu TimeLog
        TimeLog newLog = new TimeLog();
        newLog.setEmployee(new Employee(employeeId)); // Tạo Employee giả để FK
        newLog.setLogDate(today);
        newLog.setCheckInTime(currentTime);
        newLog.setStatus(status);

        return timeLogRepository.save(newLog);
    }


    public TimeLog checkOut(Integer employeeId) {
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        // Tìm bản ghi Check-in hôm nay
        TimeLog log = timeLogRepository.findByEmployeeEmployeeIDAndLogDate(employeeId, today)
                .orElseThrow(() -> new IllegalStateException("Employee hasn't checked in today."));

        if (log.getCheckOutTime() != null) {
            throw new IllegalStateException("Employee already checked out today.");
        }

        // Xác định trạng thái Check-out (EarlyLeave/Overtime)
        LocalTime standardCheckOut = getStandardCheckOutTime(employeeId, today);
        if (currentTime.isBefore(standardCheckOut.minusMinutes(10))) { // Về sớm
            log.setStatus(TimeLog.TimeLogStatus.EarlyLeave);
        } else if (currentTime.isAfter(standardCheckOut.plusMinutes(10))) { // OT
            log.setStatus(TimeLog.TimeLogStatus.Overtime);
        }

        log.setCheckOutTime(currentTime);

        // Cập nhật và lưu TimeLog
        return timeLogRepository.save(log);
    }

    // TODO: Thêm phương thức thống kê giờ làm, đi muộn/về sớm, OT để liên kết với Payroll
}