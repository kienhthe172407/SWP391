package com.hrms.controller;

import com.hrms.model.LeaveRequest;
import com.hrms.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leave")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;


    @PostMapping("/request")
    public ResponseEntity<LeaveRequest> submitRequest(@RequestBody LeaveRequest request) {
        try {
            LeaveRequest savedRequest = leaveService.submitLeaveRequest(request);
            return ResponseEntity.ok(savedRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PutMapping("/approve/{requestId}")
    public ResponseEntity<LeaveRequest> approveRequest(@PathVariable Integer requestId, @RequestParam Integer approverId) {
        try {
            LeaveRequest approvedRequest = leaveService.approveLeaveRequest(requestId, approverId);
            return ResponseEntity.ok(approvedRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/remaining/{employeeId}")
    public ResponseEntity<Double> getRemainingDays(@PathVariable Integer employeeId) {
        double remaining = leaveService.getRemainingLeaveDays(employeeId);
        return ResponseEntity.ok(remaining);
    }

    // TODO: Thêm API để Lấy danh sách đơn Pending, Lấy lịch sử đơn theo nhân viên.
}