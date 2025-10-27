package util;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import model.AttendanceRecord;
import model.AttendanceSummary;
import model.MonthlyAttendanceSummary;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Utility class for generating PDF documents from attendance data
 * Uses iText 7 library for PDF creation
 */
public class AttendancePDFExportUtil {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    // Color definitions
    private static final DeviceRgb HEADER_COLOR = new DeviceRgb(52, 58, 64); // Dark gray
    private static final DeviceRgb LIGHT_GRAY = new DeviceRgb(248, 249, 250);
    
    /**
     * Generate PDF document for daily attendance records
     * @param records List of attendance records to export
     * @param summary Attendance summary (optional)
     * @param filterEmployeeCode Filter parameter for employee code
     * @param filterDepartmentId Filter parameter for department ID
     * @param filterStartDate Filter parameter for start date
     * @param filterEndDate Filter parameter for end date
     * @return byte array of PDF file
     * @throws IOException if error occurs during generation
     */
    public static byte[] generateAttendancePDF(List<AttendanceRecord> records,
                                               AttendanceSummary summary,
                                               String filterEmployeeCode,
                                               Integer filterDepartmentId,
                                               String filterStartDate,
                                               String filterEndDate) throws IOException {

        // Debug logging
        System.out.println("=== PDF EXPORT UTIL ===");
        System.out.println("Records count: " + (records != null ? records.size() : 0));
        System.out.println("Summary: " + (summary != null ? "YES" : "NO"));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        // Title
        Paragraph title = new Paragraph("Attendance Report")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10);
        document.add(title);
        
        // Export info
        Paragraph exportInfo = new Paragraph("Exported on: " + DATETIME_FORMAT.format(new Date()))
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(exportInfo);
        
        // Filter information
        if (filterEmployeeCode != null || filterDepartmentId != null || 
            filterStartDate != null || filterEndDate != null) {
            
            StringBuilder filterInfo = new StringBuilder("Filters Applied: ");
            if (filterEmployeeCode != null && !filterEmployeeCode.trim().isEmpty()) {
                filterInfo.append("Employee Code=").append(filterEmployeeCode).append(" ");
            }
            if (filterDepartmentId != null && filterDepartmentId > 0) {
                filterInfo.append("Department ID=").append(filterDepartmentId).append(" ");
            }
            if (filterStartDate != null && !filterStartDate.trim().isEmpty()) {
                filterInfo.append("Start Date=").append(filterStartDate).append(" ");
            }
            if (filterEndDate != null && !filterEndDate.trim().isEmpty()) {
                filterInfo.append("End Date=").append(filterEndDate).append(" ");
            }
            
            Paragraph filters = new Paragraph(filterInfo.toString())
                    .setFontSize(9)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(15);
            document.add(filters);
        }
        
        // Summary section (if available)
        if (summary != null) {
            Paragraph summaryTitle = new Paragraph("Summary Statistics")
                    .setFontSize(14)
                    .setBold()
                    .setMarginBottom(5);
            document.add(summaryTitle);
            
            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{1, 1, 1, 1}))
                    .useAllAvailableWidth()
                    .setMarginBottom(15);
            
            // Summary data
            addSummaryCell(summaryTable, "Total Days Present", String.valueOf(summary.getPresentDays()));
            addSummaryCell(summaryTable, "Total Days Absent", String.valueOf(summary.getAbsentDays()));
            addSummaryCell(summaryTable, "Total Late Days", String.valueOf(summary.getLateDays()));
            addSummaryCell(summaryTable, "Total Early Leave", String.valueOf(summary.getEarlyLeaveDays()));
            addSummaryCell(summaryTable, "Business Trip Days", String.valueOf(summary.getBusinessTripDays()));
            addSummaryCell(summaryTable, "Remote Days", String.valueOf(summary.getRemoteDays()));
            addSummaryCell(summaryTable, "Total Overtime (hrs)", String.format("%.1f", summary.getTotalOvertimeHours()));
            // Calculate average overtime per day
            double avgOvertime = summary.getTotalDays() > 0 ? summary.getTotalOvertimeHours() / summary.getTotalDays() : 0.0;
            addSummaryCell(summaryTable, "Avg Overtime/Day", String.format("%.2f", avgOvertime));
            
            document.add(summaryTable);
        }
        
        // Attendance records section
        Paragraph recordsTitle = new Paragraph("Attendance Records (" + records.size() + " records)")
                .setFontSize(14)
                .setBold()
                .setMarginBottom(5);
        document.add(recordsTitle);
        
        // Records table
        Table table = new Table(UnitValue.createPercentArray(new float[]{0.5f, 1, 1.5f, 1, 1, 1, 1, 0.8f}))
                .useAllAvailableWidth()
                .setFontSize(8);
        
        // Header row
        addHeaderCell(table, "#");
        addHeaderCell(table, "Employee Code");
        addHeaderCell(table, "Employee Name");
        addHeaderCell(table, "Date");
        addHeaderCell(table, "Check-in");
        addHeaderCell(table, "Check-out");
        addHeaderCell(table, "Status");
        addHeaderCell(table, "Overtime");
        
        // Data rows
        int rowNum = 1;
        for (AttendanceRecord record : records) {
            boolean isEvenRow = (rowNum % 2 == 0);
            
            addDataCell(table, String.valueOf(rowNum++), isEvenRow);
            addDataCell(table, record.getEmployeeCode() != null ? record.getEmployeeCode() : "", isEvenRow);
            addDataCell(table, record.getEmployeeName() != null ? record.getEmployeeName() : "", isEvenRow);
            addDataCell(table, record.getAttendanceDate() != null ? DATE_FORMAT.format(record.getAttendanceDate()) : "", isEvenRow);
            addDataCell(table, record.getCheckInTime() != null ? TIME_FORMAT.format(record.getCheckInTime()) : "", isEvenRow);
            addDataCell(table, record.getCheckOutTime() != null ? TIME_FORMAT.format(record.getCheckOutTime()) : "", isEvenRow);
            addDataCell(table, record.getStatus() != null ? record.getStatus() : "", isEvenRow);
            addDataCell(table, String.format("%.1f", record.getOvertimeHours()), isEvenRow);
        }
        
        document.add(table);
        
        // Footer
        Paragraph footer = new Paragraph("Total Records: " + records.size())
                .setFontSize(9)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(10);
        document.add(footer);
        
        document.close();
        return baos.toByteArray();
    }
    
    /**
     * Generate PDF document for monthly attendance summaries
     * @param monthlyRecords List of monthly attendance summary records
     * @param filterMonth Filter parameter for month
     * @param filterYear Filter parameter for year
     * @param filterEmployeeCode Filter parameter for employee code
     * @param filterDepartmentId Filter parameter for department ID
     * @return byte array of PDF file
     * @throws IOException if error occurs during generation
     */
    public static byte[] generateMonthlyAttendancePDF(List<MonthlyAttendanceSummary> monthlyRecords,
                                                      Integer filterMonth,
                                                      Integer filterYear,
                                                      String filterEmployeeCode,
                                                      Integer filterDepartmentId) throws IOException {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        // Title
        Paragraph title = new Paragraph("Monthly Attendance Report")
                .setFontSize(20)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10);
        document.add(title);
        
        // Export info
        Paragraph exportInfo = new Paragraph("Exported on: " + DATETIME_FORMAT.format(new Date()))
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
        document.add(exportInfo);
        
        // Filter information
        if (filterYear != null || filterMonth != null || filterEmployeeCode != null || filterDepartmentId != null) {
            StringBuilder filterInfo = new StringBuilder("Filters Applied: ");
            if (filterYear != null) {
                filterInfo.append("Year=").append(filterYear).append(" ");
            }
            if (filterMonth != null) {
                filterInfo.append("Month=").append(filterMonth).append(" ");
            }
            if (filterEmployeeCode != null && !filterEmployeeCode.trim().isEmpty()) {
                filterInfo.append("Employee=").append(filterEmployeeCode).append(" ");
            }
            if (filterDepartmentId != null) {
                filterInfo.append("Department ID=").append(filterDepartmentId).append(" ");
            }
            
            Paragraph filters = new Paragraph(filterInfo.toString())
                    .setFontSize(9)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(15);
            document.add(filters);
        }
        
        // Records table
        Table table = new Table(UnitValue.createPercentArray(new float[]{0.5f, 1, 1.5f, 1.2f, 0.8f, 0.8f, 0.8f, 0.8f, 0.8f, 0.9f, 0.8f, 1f}))
                .useAllAvailableWidth()
                .setFontSize(7);
        
        // Header row
        addHeaderCell(table, "#");
        addHeaderCell(table, "Emp Code");
        addHeaderCell(table, "Employee Name");
        addHeaderCell(table, "Department");
        addHeaderCell(table, "Month");
        addHeaderCell(table, "Present");
        addHeaderCell(table, "Absent");
        addHeaderCell(table, "Late");
        addHeaderCell(table, "Remote");
        addHeaderCell(table, "Overtime");
        addHeaderCell(table, "Total Days");
        addHeaderCell(table, "Rate");
        
        // Data rows
        int rowNum = 1;
        for (MonthlyAttendanceSummary record : monthlyRecords) {
            boolean isEvenRow = (rowNum % 2 == 0);
            
            addDataCell(table, String.valueOf(rowNum++), isEvenRow);
            addDataCell(table, record.getEmployeeCode() != null ? record.getEmployeeCode() : "", isEvenRow);
            addDataCell(table, record.getEmployeeName() != null ? record.getEmployeeName() : "", isEvenRow);
            addDataCell(table, record.getDepartmentName() != null ? record.getDepartmentName() : "", isEvenRow);
            addDataCell(table, record.getAttendanceMonth() != null ? record.getAttendanceMonth() : "", isEvenRow);
            addDataCell(table, String.valueOf(record.getPresentDays()), isEvenRow);
            addDataCell(table, String.valueOf(record.getAbsentDays()), isEvenRow);
            addDataCell(table, String.valueOf(record.getLateDays()), isEvenRow);
            addDataCell(table, String.valueOf(record.getRemoteDays()), isEvenRow);
            addDataCell(table, String.format("%.1f", record.getTotalOvertimeHours()), isEvenRow);
            addDataCell(table, String.valueOf(record.getTotalWorkingDays()), isEvenRow);
            addDataCell(table, String.format("%.1f%%", record.getAttendanceRate()), isEvenRow);
        }
        
        document.add(table);
        
        // Footer
        Paragraph footer = new Paragraph("Total Records: " + monthlyRecords.size())
                .setFontSize(9)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginTop(10);
        document.add(footer);
        
        document.close();
        return baos.toByteArray();
    }
    
    /**
     * Add header cell to table
     */
    private static void addHeaderCell(Table table, String text) {
        Cell cell = new Cell()
                .add(new Paragraph(text).setBold().setFontColor(ColorConstants.WHITE))
                .setBackgroundColor(HEADER_COLOR)
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(5);
        table.addHeaderCell(cell);
    }
    
    /**
     * Add data cell to table
     */
    private static void addDataCell(Table table, String text, boolean isEvenRow) {
        Cell cell = new Cell()
                .add(new Paragraph(text))
                .setTextAlignment(TextAlignment.CENTER)
                .setPadding(3);
        
        if (isEvenRow) {
            cell.setBackgroundColor(LIGHT_GRAY);
        }
        
        table.addCell(cell);
    }
    
    /**
     * Add summary cell (label and value pair)
     */
    private static void addSummaryCell(Table table, String label, String value) {
        Cell labelCell = new Cell()
                .add(new Paragraph(label).setBold())
                .setPadding(5)
                .setBackgroundColor(LIGHT_GRAY);
        
        Cell valueCell = new Cell()
                .add(new Paragraph(value))
                .setPadding(5)
                .setTextAlignment(TextAlignment.CENTER);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}

