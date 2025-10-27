package util;

import model.AttendanceRecord;
import model.AttendanceSummary;
import model.MonthlyAttendanceSummary;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Utility class for exporting attendance data to Excel format
 * Generates formatted Excel workbooks with summary and detailed records
 */
public class AttendanceExportUtil {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Generate Excel workbook for attendance records with optional summary
     * @param records List of attendance records to export
     * @param summary Optional attendance summary (can be null)
     * @param filterEmployeeCode Filter parameter for employee code
     * @param filterDepartmentId Filter parameter for department ID
     * @param filterStartDate Filter parameter for start date
     * @param filterEndDate Filter parameter for end date
     * @return byte array of Excel file
     * @throws IOException if error occurs during generation
     */
    public static byte[] generateAttendanceExcel(List<AttendanceRecord> records,
                                                  AttendanceSummary summary,
                                                  String filterEmployeeCode,
                                                  Integer filterDepartmentId,
                                                  String filterStartDate,
                                                  String filterEndDate) throws IOException {

        // Debug logging
        System.out.println("=== EXCEL EXPORT UTIL ===");
        System.out.println("Records count: " + (records != null ? records.size() : 0));
        System.out.println("Summary: " + (summary != null ? "YES" : "NO"));

        try (Workbook workbook = new XSSFWorkbook()) {

            // Create Summary Sheet if summary data is provided
            if (summary != null && (filterEmployeeCode != null || filterDepartmentId != null)) {
                System.out.println("Creating Summary Sheet...");
                createSummarySheet(workbook, summary, filterEmployeeCode, filterDepartmentId,
                                  filterStartDate, filterEndDate);
            } else {
                System.out.println("Skipping Summary Sheet (no summary or no employee/dept filter)");
            }

            // Create Detailed Records Sheet
            System.out.println("Creating Records Sheet with " + (records != null ? records.size() : 0) + " records...");
            createRecordsSheet(workbook, records, filterEmployeeCode, filterDepartmentId,
                              filterStartDate, filterEndDate);
            
            // Write to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
    
    /**
     * Create summary sheet with attendance statistics
     */
    private static void createSummarySheet(Workbook workbook, AttendanceSummary summary,
                                          String filterEmployeeCode, Integer filterDepartmentId,
                                          String filterStartDate, String filterEndDate) {
        Sheet sheet = workbook.createSheet("Summary");
        
        // Create styles
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle titleStyle = createTitleStyle(workbook);
        CellStyle labelStyle = createLabelStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        int rowNum = 0;
        
        // Title
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Attendance Summary Report");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
        rowNum++;
        
        // Filter Information
        Row filterHeaderRow = sheet.createRow(rowNum++);
        Cell filterHeaderCell = filterHeaderRow.createCell(0);
        filterHeaderCell.setCellValue("Filter Criteria");
        filterHeaderCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 3));
        
        if (filterEmployeeCode != null && !filterEmployeeCode.trim().isEmpty()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Employee Code:");
            row.getCell(0).setCellStyle(labelStyle);
            row.createCell(1).setCellValue(filterEmployeeCode);
            row.getCell(1).setCellStyle(dataStyle);
        }
        
        if (filterDepartmentId != null && filterDepartmentId > 0) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Department ID:");
            row.getCell(0).setCellStyle(labelStyle);
            row.createCell(1).setCellValue(filterDepartmentId);
            row.getCell(1).setCellStyle(dataStyle);
        }
        
        if (filterStartDate != null && !filterStartDate.trim().isEmpty()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("Start Date:");
            row.getCell(0).setCellStyle(labelStyle);
            row.createCell(1).setCellValue(filterStartDate);
            row.getCell(1).setCellStyle(dataStyle);
        }
        
        if (filterEndDate != null && !filterEndDate.trim().isEmpty()) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue("End Date:");
            row.getCell(0).setCellStyle(labelStyle);
            row.createCell(1).setCellValue(filterEndDate);
            row.getCell(1).setCellStyle(dataStyle);
        }
        
        rowNum++;
        
        // Summary Statistics
        Row summaryHeaderRow = sheet.createRow(rowNum++);
        Cell summaryHeaderCell = summaryHeaderRow.createCell(0);
        summaryHeaderCell.setCellValue("Summary Statistics");
        summaryHeaderCell.setCellStyle(headerStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 3));
        
        // Statistics data
        addSummaryRow(sheet, rowNum++, "Total Days", summary.getTotalDays(), labelStyle, dataStyle);
        addSummaryRow(sheet, rowNum++, "Present Days", summary.getPresentDays(), labelStyle, dataStyle);
        addSummaryRow(sheet, rowNum++, "Absent Days", summary.getAbsentDays(), labelStyle, dataStyle);
        addSummaryRow(sheet, rowNum++, "Late Days", summary.getLateDays(), labelStyle, dataStyle);
        addSummaryRow(sheet, rowNum++, "Early Leave Days", summary.getEarlyLeaveDays(), labelStyle, dataStyle);
        addSummaryRow(sheet, rowNum++, "Business Trip Days", summary.getBusinessTripDays(), labelStyle, dataStyle);
        addSummaryRow(sheet, rowNum++, "Remote Days", summary.getRemoteDays(), labelStyle, dataStyle);
        addSummaryRow(sheet, rowNum++, "Working Days", summary.getWorkingDays(), labelStyle, dataStyle);
        addSummaryRow(sheet, rowNum++, "Total Overtime Hours", summary.getTotalOvertimeHours(), labelStyle, dataStyle);
        addSummaryRow(sheet, rowNum++, "Attendance Rate", String.format("%.1f%%", summary.getAttendanceRate()), labelStyle, dataStyle);
        addSummaryRow(sheet, rowNum++, "Manual Adjustments", summary.getManualAdjustments(), labelStyle, dataStyle);
        
        // Auto-size columns
        for (int i = 0; i < 4; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    /**
     * Create detailed records sheet
     */
    private static void createRecordsSheet(Workbook workbook, List<AttendanceRecord> records,
                                          String filterEmployeeCode, Integer filterDepartmentId,
                                          String filterStartDate, String filterEndDate) {
        Sheet sheet = workbook.createSheet("Attendance Records");
        
        // Create styles
        CellStyle headerStyle = createHeaderStyle(workbook);
        CellStyle titleStyle = createTitleStyle(workbook);
        CellStyle dataStyle = createDataStyle(workbook);
        
        int rowNum = 0;
        
        // Title
        Row titleRow = sheet.createRow(rowNum++);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Attendance Records Export");
        titleCell.setCellStyle(titleStyle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));
        
        // Export info
        Row infoRow = sheet.createRow(rowNum++);
        Cell infoCell = infoRow.createCell(0);
        infoCell.setCellValue("Exported on: " + DATETIME_FORMAT.format(new java.util.Date()));
        infoCell.setCellStyle(dataStyle);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 9));
        
        rowNum++;
        
        // Header row
        Row headerRow = sheet.createRow(rowNum++);
        String[] headers = {
            "#", "Employee Code", "Employee Name", "Date", "Check-in", 
            "Check-out", "Status", "Overtime (hrs)", "Manual Adjustment", "Adjustment Reason"
        };
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Data rows
        int recordNum = 1;
        for (AttendanceRecord record : records) {
            Row row = sheet.createRow(rowNum++);
            
            row.createCell(0).setCellValue(recordNum++);
            row.createCell(1).setCellValue(record.getEmployeeCode() != null ? record.getEmployeeCode() : "");
            row.createCell(2).setCellValue(record.getEmployeeName() != null ? record.getEmployeeName() : "");
            row.createCell(3).setCellValue(record.getAttendanceDate() != null ? DATE_FORMAT.format(record.getAttendanceDate()) : "");
            row.createCell(4).setCellValue(record.getCheckInTime() != null ? TIME_FORMAT.format(record.getCheckInTime()) : "");
            row.createCell(5).setCellValue(record.getCheckOutTime() != null ? TIME_FORMAT.format(record.getCheckOutTime()) : "");
            row.createCell(6).setCellValue(record.getStatus() != null ? record.getStatus() : "");
            row.createCell(7).setCellValue(record.getOvertimeHours() != null ? record.getOvertimeHours() : 0);
            row.createCell(8).setCellValue(record.getIsManualAdjustment() != null && record.getIsManualAdjustment() ? "Yes" : "No");
            row.createCell(9).setCellValue(record.getAdjustmentReason() != null ? record.getAdjustmentReason() : "");
            
            // Apply data style to all cells
            for (int i = 0; i < headers.length; i++) {
                row.getCell(i).setCellStyle(dataStyle);
            }
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    
    /**
     * Helper method to add a summary row
     */
    private static void addSummaryRow(Sheet sheet, int rowNum, String label, Object value, 
                                     CellStyle labelStyle, CellStyle dataStyle) {
        Row row = sheet.createRow(rowNum);
        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(labelStyle);
        
        Cell valueCell = row.createCell(1);
        if (value instanceof Number) {
            valueCell.setCellValue(((Number) value).doubleValue());
        } else {
            valueCell.setCellValue(value.toString());
        }
        valueCell.setCellStyle(dataStyle);
    }
    
    /**
     * Create header cell style
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
    
    /**
     * Create title cell style
     */
    private static CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        font.setColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
    
    /**
     * Create label cell style
     */
    private static CellStyle createLabelStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
    
    /**
     * Create data cell style
     */
    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    /**
     * Generate Excel workbook for monthly attendance records
     * @param monthlyRecords List of monthly attendance summary records to export
     * @param filterMonth Filter parameter for month
     * @param filterYear Filter parameter for year
     * @param filterEmployeeCode Filter parameter for employee code
     * @param filterDepartmentId Filter parameter for department ID
     * @return byte array of Excel file
     * @throws IOException if error occurs during generation
     */
    public static byte[] generateMonthlyAttendanceExcel(List<MonthlyAttendanceSummary> monthlyRecords,
                                                         Integer filterMonth,
                                                         Integer filterYear,
                                                         String filterEmployeeCode,
                                                         Integer filterDepartmentId) throws IOException {

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Monthly Attendance Report");

            // Create styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            int rowNum = 0;

            // Title
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("Monthly Attendance Report");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 11));

            // Export info
            Row infoRow = sheet.createRow(rowNum++);
            Cell infoCell = infoRow.createCell(0);
            infoCell.setCellValue("Exported on: " + DATETIME_FORMAT.format(new java.util.Date()));
            infoCell.setCellStyle(dataStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 11));

            // Filter info
            if (filterYear != null || filterMonth != null || filterEmployeeCode != null || filterDepartmentId != null) {
                Row filterRow = sheet.createRow(rowNum++);
                Cell filterCell = filterRow.createCell(0);
                StringBuilder filterInfo = new StringBuilder("Filters: ");
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
                filterCell.setCellValue(filterInfo.toString());
                filterCell.setCellStyle(dataStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 11));
            }

            rowNum++;

            // Header row
            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = {
                "#", "Employee Code", "Employee Name", "Department", "Month",
                "Present", "Absent", "Late", "Remote", "Overtime (hrs)", "Total Days", "Attendance Rate"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            int recordNum = 1;
            for (MonthlyAttendanceSummary record : monthlyRecords) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(recordNum++);
                row.createCell(1).setCellValue(record.getEmployeeCode() != null ? record.getEmployeeCode() : "");
                row.createCell(2).setCellValue(record.getEmployeeName() != null ? record.getEmployeeName() : "");
                row.createCell(3).setCellValue(record.getDepartmentName() != null ? record.getDepartmentName() : "");
                row.createCell(4).setCellValue(record.getAttendanceMonth() != null ? record.getAttendanceMonth() : "");
                row.createCell(5).setCellValue(record.getPresentDays());
                row.createCell(6).setCellValue(record.getAbsentDays());
                row.createCell(7).setCellValue(record.getLateDays());
                row.createCell(8).setCellValue(record.getRemoteDays());
                row.createCell(9).setCellValue(record.getTotalOvertimeHours());
                row.createCell(10).setCellValue(record.getTotalWorkingDays());
                row.createCell(11).setCellValue(String.format("%.1f%%", record.getAttendanceRate()));

                // Apply data style to all cells
                for (int i = 0; i < headers.length; i++) {
                    row.getCell(i).setCellStyle(dataStyle);
                }
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Write to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}

