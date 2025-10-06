package com.hrms.service;

import com.hrms.model.Employee;
import com.hrms.model.LeaveRequest;
import com.hrms.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LeaveService {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    // Số ngày phép tiêu chuẩn còn lại mỗi năm
    private static final Map<Integer, Double> MAX_LEAVE_DAYS = Map.of(
            1, 12.0, // EmployeeID 1 có 12 ngày phép/năm
            2, 12.0
    );


    public LeaveRequest submitLeaveRequest(LeaveRequest request) {
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }
        request.setStatus(LeaveRequest.LeaveStatus.Pending);
        // Logic tính TotalDays (cần phức tạp hơn để loại trừ cuối tuần/ngày lễ)
        // Hiện tại giả định là số ngày trong khoảng
        long daysBetween = request.getEndDate().toEpochDay() - request.getStartDate().toEpochDay() + 1;
        request.setTotalDays((double) daysBetween);

        return leaveRequestRepository.save(request);
    }


    public LeaveRequest approveLeaveRequest(Integer requestId, Integer approverId) {
        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Request ID."));

        if (request.getStatus() != LeaveRequest.LeaveStatus.Pending) {
            throw new IllegalStateException("Only pending requests can be approved.");
        }

        // TODO: Logic kiểm tra số ngày phép còn lại trước khi Approve
        // Nếu đã hết phép thì ném lỗi hoặc chuyển sang loại phép không lương

        request.setStatus(LeaveRequest.LeaveStatus.Approved);
        request.setApprovedBy(new Employee(approverId));
        return leaveRequestRepository.save(request);
    }


    public double getRemainingLeaveDays(Integer employeeId) {
        // Lấy số ngày phép tối đa của nhân viên
        double maxDays = MAX_LEAVE_DAYS.getOrDefault(employeeId, 0.0);
        if (maxDays == 0.0) return 0.0;

        // Tính tổng số ngày phép đã được DUYỆT trong năm hiện tại
        LocalDate currentYearStart = LocalDate.now().withDayOfYear(1);
        List<LeaveRequest> approvedLeaves = leaveRequestRepository.findByEmployeeEmployeeID(employeeId).stream()
                .filter(req -> req.getStatus() == LeaveRequest.LeaveStatus.Approved && req.getStartDate().isAfter(currentYearStart))
                .collect(Collectors.toList());

        double usedDays = approvedLeaves.stream().mapToDouble(LeaveRequest::getTotalDays).sum();

        // Số ngày phép còn lại
        return maxDays - usedDays;
    }
}