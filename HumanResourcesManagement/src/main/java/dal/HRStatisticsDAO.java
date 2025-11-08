package dal;

import model.HRStatistics;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Data Access Object for HR Statistics and Analytics
 * Provides comprehensive reporting data for HR Manager dashboard
 * @author admin
 */
public class HRStatisticsDAO extends DBContext {

    /**
     * Get comprehensive HR statistics
     * @return HRStatistics object with all metrics
     */
    public HRStatistics getComprehensiveStatistics() {
        HRStatistics stats = new HRStatistics();
        
        // Gather all statistics
        getEmployeeStatistics(stats);
        getAttendanceStatistics(stats);
        getRecruitmentStatistics(stats);
        getTaskStatistics(stats);
        getRequestStatistics(stats);
        getContractStatistics(stats);
        getSalaryStatistics(stats);
        getTrendData(stats);
        
        return stats;
    }

    /**
     * Get employee statistics
     */
    private void getEmployeeStatistics(HRStatistics stats) {
        // Total and active employees
        String sql = "SELECT " +
                     "COUNT(*) as total, " +
                     "SUM(CASE WHEN employment_status = 'Active' THEN 1 ELSE 0 END) as active, " +
                     "SUM(CASE WHEN employment_status = 'Terminated' THEN 1 ELSE 0 END) as terminated " +
                     "FROM employees";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.setTotalEmployees(rs.getInt("total"));
                stats.setActiveEmployees(rs.getInt("active"));
                stats.setTerminatedEmployees(rs.getInt("terminated"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting employee statistics: " + e.getMessage());
        }

        // New hires this month
        sql = "SELECT COUNT(*) as count FROM employees " +
              "WHERE MONTH(hire_date) = MONTH(CURDATE()) AND YEAR(hire_date) = YEAR(CURDATE())";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.setNewHiresThisMonth(rs.getInt("count"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting new hires this month: " + e.getMessage());
        }

        // New hires this year
        sql = "SELECT COUNT(*) as count FROM employees WHERE YEAR(hire_date) = YEAR(CURDATE())";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.setNewHiresThisYear(rs.getInt("count"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting new hires this year: " + e.getMessage());
        }

        // Employees by department
        sql = "SELECT d.department_name, COUNT(e.employee_id) as count " +
              "FROM departments d " +
              "LEFT JOIN employees e ON d.department_id = e.department_id AND e.employment_status = 'Active' " +
              "GROUP BY d.department_id, d.department_name " +
              "ORDER BY count DESC";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Map<String, Integer> byDept = new HashMap<>();
            while (rs.next()) {
                byDept.put(rs.getString("department_name"), rs.getInt("count"));
            }
            stats.setEmployeesByDepartment(byDept);
        } catch (SQLException e) {
            System.err.println("Error getting employees by department: " + e.getMessage());
        }

        // Employees by position
        sql = "SELECT p.position_name, COUNT(e.employee_id) as count " +
              "FROM positions p " +
              "LEFT JOIN employees e ON p.position_id = e.position_id AND e.employment_status = 'Active' " +
              "GROUP BY p.position_id, p.position_name " +
              "ORDER BY count DESC LIMIT 10";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Map<String, Integer> byPos = new HashMap<>();
            while (rs.next()) {
                byPos.put(rs.getString("position_name"), rs.getInt("count"));
            }
            stats.setEmployeesByPosition(byPos);
        } catch (SQLException e) {
            System.err.println("Error getting employees by position: " + e.getMessage());
        }

        // Employees by status
        sql = "SELECT employment_status, COUNT(*) as count FROM employees GROUP BY employment_status";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Map<String, Integer> byStatus = new HashMap<>();
            while (rs.next()) {
                byStatus.put(rs.getString("employment_status"), rs.getInt("count"));
            }
            stats.setEmployeesByStatus(byStatus);
        } catch (SQLException e) {
            System.err.println("Error getting employees by status: " + e.getMessage());
        }
    }

    /**
     * Get attendance statistics
     */
    private void getAttendanceStatistics(HRStatistics stats) {
        // Today's attendance
        String sql = "SELECT " +
                     "SUM(CASE WHEN status = 'Present' THEN 1 ELSE 0 END) as present, " +
                     "SUM(CASE WHEN status = 'Absent' THEN 1 ELSE 0 END) as absent, " +
                     "SUM(CASE WHEN status = 'Late' THEN 1 ELSE 0 END) as late, " +
                     "SUM(CASE WHEN status = 'Remote' THEN 1 ELSE 0 END) as remote " +
                     "FROM attendance_records " +
                     "WHERE attendance_date = CURDATE()";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.setTotalPresentToday(rs.getInt("present"));
                stats.setTotalAbsentToday(rs.getInt("absent"));
                stats.setTotalLateToday(rs.getInt("late"));
                stats.setTotalRemoteToday(rs.getInt("remote"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting today's attendance: " + e.getMessage());
        }

        // Average attendance rate (last 30 days)
        sql = "SELECT " +
              "(SUM(CASE WHEN status IN ('Present', 'Late', 'Remote', 'Business Trip') THEN 1 ELSE 0 END) * 100.0 / " +
              "COUNT(*)) as rate " +
              "FROM attendance_records " +
              "WHERE attendance_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.setAverageAttendanceRate(rs.getDouble("rate"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting average attendance rate: " + e.getMessage());
        }

        // Attendance by status (last 30 days)
        sql = "SELECT status, COUNT(*) as count FROM attendance_records " +
              "WHERE attendance_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) " +
              "GROUP BY status";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Map<String, Integer> byStatus = new HashMap<>();
            while (rs.next()) {
                byStatus.put(rs.getString("status"), rs.getInt("count"));
            }
            stats.setAttendanceByStatus(byStatus);
        } catch (SQLException e) {
            System.err.println("Error getting attendance by status: " + e.getMessage());
        }
    }

    /**
     * Get recruitment statistics
     */
    private void getRecruitmentStatistics(HRStatistics stats) {
        // Job postings count
        String sql = "SELECT " +
                     "COUNT(*) as total, " +
                     "SUM(CASE WHEN status = 'Active' AND deadline >= CURDATE() THEN 1 ELSE 0 END) as active " +
                     "FROM job_postings WHERE is_deleted = FALSE";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.setTotalJobPostings(rs.getInt("total"));
                stats.setActiveJobPostings(rs.getInt("active"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting job postings count: " + e.getMessage());
        }

        // Applications count
        sql = "SELECT COUNT(*) as total FROM job_applications WHERE is_deleted = FALSE";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.setTotalApplications(rs.getInt("total"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting applications count: " + e.getMessage());
        }

        // Applications this month
        sql = "SELECT COUNT(*) as count FROM job_applications " +
              "WHERE MONTH(applied_date) = MONTH(CURDATE()) AND YEAR(applied_date) = YEAR(CURDATE()) " +
              "AND is_deleted = FALSE";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.setApplicationsThisMonth(rs.getInt("count"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting applications this month: " + e.getMessage());
        }

        // Applications by status
        sql = "SELECT application_status, COUNT(*) as count FROM job_applications " +
              "WHERE is_deleted = FALSE GROUP BY application_status";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Map<String, Integer> byStatus = new HashMap<>();
            while (rs.next()) {
                byStatus.put(rs.getString("application_status"), rs.getInt("count"));
            }
            stats.setApplicationsByStatus(byStatus);
        } catch (SQLException e) {
            System.err.println("Error getting applications by status: " + e.getMessage());
        }

        // Applications by month (last 6 months)
        sql = "SELECT DATE_FORMAT(applied_date, '%Y-%m') as month, COUNT(*) as count " +
              "FROM job_applications " +
              "WHERE applied_date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH) AND is_deleted = FALSE " +
              "GROUP BY DATE_FORMAT(applied_date, '%Y-%m') " +
              "ORDER BY month";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Map<String, Integer> byMonth = new HashMap<>();
            while (rs.next()) {
                byMonth.put(rs.getString("month"), rs.getInt("count"));
            }
            stats.setApplicationsByMonth(byMonth);
        } catch (SQLException e) {
            System.err.println("Error getting applications by month: " + e.getMessage());
        }
    }

    /**
     * Get task statistics
     */
    private void getTaskStatistics(HRStatistics stats) {
        // Task counts
        String sql = "SELECT " +
                     "COUNT(*) as total, " +
                     "SUM(CASE WHEN task_status = 'Done' THEN 1 ELSE 0 END) as completed, " +
                     "SUM(CASE WHEN task_status = 'In Progress' THEN 1 ELSE 0 END) as in_progress, " +
                     "SUM(CASE WHEN task_status NOT IN ('Done', 'Cancelled') AND due_date < CURDATE() THEN 1 ELSE 0 END) as overdue " +
                     "FROM tasks WHERE is_deleted = FALSE";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.setTotalTasks(rs.getInt("total"));
                stats.setCompletedTasks(rs.getInt("completed"));
                stats.setInProgressTasks(rs.getInt("in_progress"));
                stats.setOverdueTasksCount(rs.getInt("overdue"));
                
                // Calculate completion rate
                int total = rs.getInt("total");
                if (total > 0) {
                    double rate = (rs.getInt("completed") * 100.0) / total;
                    stats.setTaskCompletionRate(rate);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting task statistics: " + e.getMessage());
        }

        // Tasks by status
        sql = "SELECT task_status, COUNT(*) as count FROM tasks " +
              "WHERE is_deleted = FALSE GROUP BY task_status";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Map<String, Integer> byStatus = new HashMap<>();
            while (rs.next()) {
                byStatus.put(rs.getString("task_status"), rs.getInt("count"));
            }
            stats.setTasksByStatus(byStatus);
        } catch (SQLException e) {
            System.err.println("Error getting tasks by status: " + e.getMessage());
        }

        // Tasks by priority
        sql = "SELECT priority, COUNT(*) as count FROM tasks " +
              "WHERE is_deleted = FALSE GROUP BY priority";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Map<String, Integer> byPriority = new HashMap<>();
            while (rs.next()) {
                byPriority.put(rs.getString("priority"), rs.getInt("count"));
            }
            stats.setTasksByPriority(byPriority);
        } catch (SQLException e) {
            System.err.println("Error getting tasks by priority: " + e.getMessage());
        }
    }

    /**
     * Get request statistics
     */
    private void getRequestStatistics(HRStatistics stats) {
        // Request counts
        String sql = "SELECT " +
                     "COUNT(*) as total, " +
                     "SUM(CASE WHEN request_status = 'Pending' THEN 1 ELSE 0 END) as pending " +
                     "FROM requests";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.setTotalRequests(rs.getInt("total"));
                stats.setPendingRequests(rs.getInt("pending"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting request counts: " + e.getMessage());
        }

        // Approved/Rejected this month
        sql = "SELECT " +
              "SUM(CASE WHEN request_status = 'Approved' THEN 1 ELSE 0 END) as approved, " +
              "SUM(CASE WHEN request_status = 'Rejected' THEN 1 ELSE 0 END) as rejected " +
              "FROM requests " +
              "WHERE MONTH(reviewed_at) = MONTH(CURDATE()) AND YEAR(reviewed_at) = YEAR(CURDATE())";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.setApprovedRequestsThisMonth(rs.getInt("approved"));
                stats.setRejectedRequestsThisMonth(rs.getInt("rejected"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting approved/rejected requests: " + e.getMessage());
        }

        // Requests by type
        sql = "SELECT rt.request_type_name, COUNT(r.request_id) as count " +
              "FROM request_types rt " +
              "LEFT JOIN requests r ON rt.request_type_id = r.request_type_id " +
              "GROUP BY rt.request_type_id, rt.request_type_name";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Map<String, Integer> byType = new HashMap<>();
            while (rs.next()) {
                byType.put(rs.getString("request_type_name"), rs.getInt("count"));
            }
            stats.setRequestsByType(byType);
        } catch (SQLException e) {
            System.err.println("Error getting requests by type: " + e.getMessage());
        }

        // Requests by status
        sql = "SELECT request_status, COUNT(*) as count FROM requests GROUP BY request_status";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Map<String, Integer> byStatus = new HashMap<>();
            while (rs.next()) {
                byStatus.put(rs.getString("request_status"), rs.getInt("count"));
            }
            stats.setRequestsByStatus(byStatus);
        } catch (SQLException e) {
            System.err.println("Error getting requests by status: " + e.getMessage());
        }
    }

    /**
     * Get contract statistics
     */
    private void getContractStatistics(HRStatistics stats) {
        // Contract counts
        String sql = "SELECT " +
                     "COUNT(*) as total, " +
                     "SUM(CASE WHEN contract_status = 'Active' THEN 1 ELSE 0 END) as active, " +
                     "SUM(CASE WHEN contract_status = 'Expired' THEN 1 ELSE 0 END) as expired, " +
                     "SUM(CASE WHEN contract_status = 'Active' AND end_date BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY) THEN 1 ELSE 0 END) as expiring " +
                     "FROM contracts WHERE is_deleted = FALSE";
        
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.setTotalContracts(rs.getInt("total"));
                stats.setActiveContracts(rs.getInt("active"));
                stats.setExpiredContracts(rs.getInt("expired"));
                stats.setExpiringContractsThisMonth(rs.getInt("expiring"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting contract statistics: " + e.getMessage());
        }

        // Contracts by type
        sql = "SELECT contract_type, COUNT(*) as count FROM contracts " +
              "WHERE is_deleted = FALSE GROUP BY contract_type";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Map<String, Integer> byType = new HashMap<>();
            while (rs.next()) {
                byType.put(rs.getString("contract_type"), rs.getInt("count"));
            }
            stats.setContractsByType(byType);
        } catch (SQLException e) {
            System.err.println("Error getting contracts by type: " + e.getMessage());
        }
    }

    /**
     * Get salary statistics
     */
    private void getSalaryStatistics(HRStatistics stats) {
        // Average salary
        String sql = "SELECT AVG(base_salary) as avg_salary FROM employees WHERE employment_status = 'Active'";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.setAverageSalary(rs.getDouble("avg_salary"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting average salary: " + e.getMessage());
        }

        // Average salary by department
        sql = "SELECT d.department_name, AVG(e.base_salary) as avg_salary " +
              "FROM departments d " +
              "LEFT JOIN employees e ON d.department_id = e.department_id AND e.employment_status = 'Active' " +
              "GROUP BY d.department_id, d.department_name " +
              "HAVING AVG(e.base_salary) IS NOT NULL";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Map<String, Double> byDept = new HashMap<>();
            while (rs.next()) {
                byDept.put(rs.getString("department_name"), rs.getDouble("avg_salary"));
            }
            stats.setAverageSalaryByDepartment(byDept);
        } catch (SQLException e) {
            System.err.println("Error getting average salary by department: " + e.getMessage());
        }

        // Monthly payroll (current month)
        sql = "SELECT " +
              "COALESCE(SUM(net_salary), 0) as total_payroll, " +
              "COALESCE(SUM(total_bonus), 0) as total_bonus, " +
              "COALESCE(SUM(total_deduction), 0) as total_deduction " +
              "FROM monthly_payroll " +
              "WHERE month = MONTH(CURDATE()) AND year = YEAR(CURDATE())";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                stats.setTotalPayrollThisMonth(rs.getDouble("total_payroll"));
                stats.setTotalBonusThisMonth(rs.getDouble("total_bonus"));
                stats.setTotalDeductionThisMonth(rs.getDouble("total_deduction"));
            }
        } catch (SQLException e) {
            System.err.println("Error getting monthly payroll: " + e.getMessage());
        }
    }

    /**
     * Get trend data for charts
     */
    private void getTrendData(HRStatistics stats) {
        // Employee growth by month (last 12 months)
        String sql = "SELECT DATE_FORMAT(hire_date, '%Y-%m') as month, COUNT(*) as count " +
                     "FROM employees " +
                     "WHERE hire_date >= DATE_SUB(CURDATE(), INTERVAL 12 MONTH) " +
                     "GROUP BY DATE_FORMAT(hire_date, '%Y-%m') " +
                     "ORDER BY month";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Map<String, Integer> growth = new HashMap<>();
            while (rs.next()) {
                growth.put(rs.getString("month"), rs.getInt("count"));
            }
            stats.setEmployeeGrowthByMonth(growth);
            stats.setHiringTrendByMonth(growth); // Same data
        } catch (SQLException e) {
            System.err.println("Error getting employee growth trend: " + e.getMessage());
        }

        // Attendance trend by month (last 6 months)
        sql = "SELECT DATE_FORMAT(attendance_date, '%Y-%m') as month, " +
              "(SUM(CASE WHEN status IN ('Present', 'Late', 'Remote', 'Business Trip') THEN 1 ELSE 0 END) * 100.0 / COUNT(*)) as rate " +
              "FROM attendance_records " +
              "WHERE attendance_date >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH) " +
              "GROUP BY DATE_FORMAT(attendance_date, '%Y-%m') " +
              "ORDER BY month";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Map<String, Double> trend = new HashMap<>();
            while (rs.next()) {
                trend.put(rs.getString("month"), rs.getDouble("rate"));
            }
            stats.setAttendanceTrendByMonth(trend);
        } catch (SQLException e) {
            System.err.println("Error getting attendance trend: " + e.getMessage());
        }
    }
}

