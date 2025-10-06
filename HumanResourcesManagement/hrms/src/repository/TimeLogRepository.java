package com.hrms.repository;

import com.hrms.model.TimeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;

public interface TimeLogRepository extends JpaRepository<TimeLog, Integer> {
    // Tìm chi tiết chấm công theo nhân viên và ngày
    Optional<TimeLog> findByEmployeeEmployeeIDAndLogDate(Integer employeeId, LocalDate logDate);
}