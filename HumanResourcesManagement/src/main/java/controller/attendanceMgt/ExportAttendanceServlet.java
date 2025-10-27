package controller.attendanceMgt;

import dal.AttendanceDAO;
import model.AttendanceRecord;
import model.AttendanceSummary;
import util.AttendanceExportUtil;
import util.AttendancePDFExportUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Servlet to handle exporting attendance data to Excel or PDF format
 * Supports filtering by employee, department, and date range
 * Exports both summary statistics and detailed records
 */
@WebServlet("/attendance/export")
public class ExportAttendanceServlet extends HttpServlet {

    private AttendanceDAO attendanceDAO;
    private static final SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");

    @Override
    public void init() throws ServletException {
        super.init();
        attendanceDAO = new AttendanceDAO();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Get filter parameters (same as ViewAttendanceSummaryServlet)
            String employeeCode = request.getParameter("employeeCode");
            String departmentIdStr = request.getParameter("departmentId");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String format = request.getParameter("format"); // "xlsx" or "pdf"

            // Default to Excel if format not specified
            if (format == null || format.trim().isEmpty()) {
                format = "xlsx";
            }
            format = format.toLowerCase();

            // Parse department ID
            Integer departmentId = null;
            if (departmentIdStr != null && !departmentIdStr.trim().isEmpty()) {
                try {
                    departmentId = Integer.parseInt(departmentIdStr);
                } catch (NumberFormatException e) {
                    // Invalid department ID, ignore
                }
            }

            // Get ALL attendance records with filters (no pagination for export)
            List<AttendanceRecord> attendanceRecords = attendanceDAO.getAttendanceWithFilters(
                employeeCode, departmentId, startDate, endDate, 1, Integer.MAX_VALUE
            );

            // Debug logging
            System.out.println("=== EXPORT DEBUG ===");
            System.out.println("Total records to export: " + (attendanceRecords != null ? attendanceRecords.size() : 0));
            System.out.println("Employee Code filter: " + employeeCode);
            System.out.println("Department ID filter: " + departmentId);
            System.out.println("Start Date filter: " + startDate);
            System.out.println("End Date filter: " + endDate);

            // Get summary statistics (only if employee or department is filtered)
            AttendanceSummary summary = null;
            if ((employeeCode != null && !employeeCode.trim().isEmpty()) ||
                (departmentId != null && departmentId > 0)) {
                summary = attendanceDAO.getAttendanceSummary(
                    employeeCode, departmentId, startDate, endDate
                );
                System.out.println("Summary retrieved: " + (summary != null ? "YES" : "NO"));
            } else {
                System.out.println("Summary NOT retrieved (no employee/department filter)");
            }

            // Generate file based on format
            byte[] fileData;
            String filename;
            String contentType;
            String timestamp = FILE_DATE_FORMAT.format(new Date());

            if ("pdf".equals(format)) {
                // Generate PDF
                fileData = AttendancePDFExportUtil.generateAttendancePDF(
                    attendanceRecords, summary, employeeCode, departmentId, startDate, endDate
                );
                filename = "Attendance_Report_" + timestamp + ".pdf";
                contentType = "application/pdf";
            } else {
                // Generate Excel (default)
                fileData = AttendanceExportUtil.generateAttendanceExcel(
                    attendanceRecords, summary, employeeCode, departmentId, startDate, endDate
                );
                filename = "Attendance_Report_" + timestamp + ".xlsx";
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            }

            // Set response headers for file download
            response.setContentType(contentType);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            response.setContentLength(fileData.length);

            // Write file data to response
            try (OutputStream out = response.getOutputStream()) {
                out.write(fileData);
                out.flush();
            }
            
        } catch (Exception e) {
            System.err.println("Error in ExportAttendanceServlet: " + e.getMessage());
            e.printStackTrace();
            
            // Send error response
            response.setContentType("text/html");
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h2>Error Exporting Attendance Data</h2>");
            response.getWriter().println("<p>An error occurred while generating the export file.</p>");
            response.getWriter().println("<p>Error: " + e.getMessage() + "</p>");
            response.getWriter().println("<p><a href='" + request.getContextPath() + "/attendance/summary'>Back to Attendance Summary</a></p>");
            response.getWriter().println("</body></html>");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Support both GET and POST
        doGet(request, response);
    }
}

