package controller.attendanceMgt;

import dal.AttendanceDAO;
import model.MonthlyAttendanceSummary;
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
 * Servlet to handle exporting monthly attendance data to Excel or PDF format
 * Supports filtering by month, year, department, and employee
 */
@WebServlet("/attendance/export-monthly")
public class ExportMonthlyAttendanceServlet extends HttpServlet {

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
            // Get filter parameters
            String monthStr = request.getParameter("month");
            String yearStr = request.getParameter("year");
            String employeeCode = request.getParameter("employeeCode");
            String departmentIdStr = request.getParameter("departmentId");
            String format = request.getParameter("format"); // "xlsx" or "pdf"

            // Default to Excel if format not specified
            if (format == null || format.trim().isEmpty()) {
                format = "xlsx";
            }
            format = format.toLowerCase();

            // Parse month and year
            Integer month = null;
            Integer year = null;

            if (monthStr != null && !monthStr.trim().isEmpty()) {
                try {
                    month = Integer.parseInt(monthStr);
                    if (month < 1 || month > 12) month = null;
                } catch (NumberFormatException e) {
                    // Invalid month, ignore
                }
            }

            if (yearStr != null && !yearStr.trim().isEmpty()) {
                try {
                    year = Integer.parseInt(yearStr);
                } catch (NumberFormatException e) {
                    // Invalid year, ignore
                }
            }

            // Parse department ID
            Integer departmentId = null;
            if (departmentIdStr != null && !departmentIdStr.trim().isEmpty()) {
                try {
                    departmentId = Integer.parseInt(departmentIdStr);
                } catch (NumberFormatException e) {
                    // Invalid department ID, ignore
                }
            }

            // Get ALL monthly attendance records with filters (no pagination for export)
            List<MonthlyAttendanceSummary> monthlyRecords = attendanceDAO.getMonthlyAttendanceRecords(
                month, year, departmentId, employeeCode, 1, Integer.MAX_VALUE
            );

            // Generate file based on format
            byte[] fileData;
            String filename;
            String contentType;
            String timestamp = FILE_DATE_FORMAT.format(new Date());

            if ("pdf".equals(format)) {
                // Generate PDF
                fileData = AttendancePDFExportUtil.generateMonthlyAttendancePDF(
                    monthlyRecords, month, year, employeeCode, departmentId
                );
                filename = "Monthly_Attendance_Report_" + timestamp + ".pdf";
                contentType = "application/pdf";
            } else {
                // Generate Excel (default)
                fileData = AttendanceExportUtil.generateMonthlyAttendanceExcel(
                    monthlyRecords, month, year, employeeCode, departmentId
                );
                filename = "Monthly_Attendance_Report_" + timestamp + ".xlsx";
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
            System.err.println("Error in ExportMonthlyAttendanceServlet: " + e.getMessage());
            e.printStackTrace();
            
            // Send error response
            response.setContentType("text/html");
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h2>Error Exporting Monthly Attendance Data</h2>");
            response.getWriter().println("<p>An error occurred while generating the export file.</p>");
            response.getWriter().println("<p>Error: " + e.getMessage() + "</p>");
            response.getWriter().println("<p><a href='" + request.getContextPath() + "/attendance/monthly-report'>Back to Monthly Report</a></p>");
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

