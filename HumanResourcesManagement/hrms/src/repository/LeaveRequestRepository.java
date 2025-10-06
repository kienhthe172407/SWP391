package com.hrms.repository;

import com.hrms.model.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {
    // Tìm các đơn nghỉ phép theo EmployeeID
    List<LeaveRequest> findByEmployeeEmployeeID(Integer employeeId);

    // Tìm các đơn nghỉ phép đang chờ duyệt (Pending)
    List<LeaveRequest> findByStatus(LeaveRequest.LeaveStatus status);
}