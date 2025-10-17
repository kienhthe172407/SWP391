package controller.attendanceMgt;

import model.AttendanceRecord;
import model.AttendancePreviewData;
import model.AttendancePreviewData.AttendancePreviewRecord;
import dal.AttendanceDAO;
import dal.EmployeeDAO;
import model.Employee;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

// Apache POI for Excel parsing
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Servlet for importing attendance records from Excel files
 * Handles file upload and delegates to AttendanceImportService
 */
@WebServlet(name = "ImportAttendanceServlet", urlPatterns = {"/attendance/import"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,      // 1 MB
    maxFileSize = 1024 * 1024 * 10,       // 10 MB
    maxRequestSize = 1024 * 1024 * 50     // 50 MB
)
public class ImportAttendanceServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Display import form
        request.getRequestDispatcher("/attendance-mgt/import-attendance.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            // Get uploaded file
            Part filePart = request.getPart("attendanceFile");

            if (filePart == null || filePart.getSize() == 0) {
                request.setAttribute("error", "Please select a file to upload");
                request.getRequestDispatcher("/attendance-mgt/import-attendance.jsp").forward(request, response);
                return;
            }

            // Validate file type
            String fileName = filePart.getSubmittedFileName();
            if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
                request.setAttribute("error", "Please upload an Excel file (.xlsx or .xls)");
                request.getRequestDispatcher("/attendance-mgt/import-attendance.jsp").forward(request, response);
                return;
            }

            // Generate unique batch ID for this import
            String importBatchID = "BATCH_" + UUID.randomUUID().toString();

            // Get input stream from uploaded file
            InputStream inputStream = filePart.getInputStream();

            // Parse Excel file for preview (inlined service logic)
            AttendancePreviewData previewData = parseExcelForPreview(inputStream, importBatchID);

            // Store preview data in session for later confirmation
            HttpSession session = request.getSession();
            session.setAttribute("attendancePreviewData", previewData);
            session.setAttribute("uploadedFileName", fileName);

            // Set attributes for display
            request.setAttribute("previewData", previewData);
            request.setAttribute("message", "File parsed successfully. Please review the data below and click 'Save' to confirm.");

            request.getRequestDispatcher("/attendance-mgt/import-attendance.jsp").forward(request, response);

        } catch (Exception e) {
            System.err.println("Error in ImportAttendanceServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Error processing file: " + e.getMessage());
            request.getRequestDispatcher("/attendance-mgt/import-attendance.jsp").forward(request, response);
        }
    }

    // ---------------- Inlined logic from AttendanceImportService ----------------
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    private AttendancePreviewData parseExcelForPreview(InputStream inputStream, String importBatchID) throws Exception {
        AttendancePreviewData previewData = new AttendancePreviewData(importBatchID);
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            if (sheet == null) {
                return previewData;
            }
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                AttendancePreviewRecord previewRecord = new AttendancePreviewRecord(rowIndex + 1);
                try {
                    // Employee code for display
                    Cell employeeCodeCell = row.getCell(0);
                    String employeeCode = getCellValueAsString(employeeCodeCell);
                    previewRecord.setEmployeeCode(employeeCode);

                    AttendanceRecord record = parseRowToAttendanceRecord(row, importBatchID);
                    if (record != null) {
                        previewRecord.setRecord(record);
                        Employee employee = employeeDAO.getEmployeeById(record.getEmployeeID());
                        if (employee != null) {
                            previewRecord.setEmployeeName(employee.getFirstName() + " " + employee.getLastName());
                        }
                        AttendanceRecord existing = attendanceDAO.getAttendanceRecord(record.getEmployeeID(), record.getAttendanceDate());
                        if (existing != null) {
                            previewRecord.setValid(false);
                            previewRecord.setErrorMessage("Duplicate: Attendance record already exists for this date. Use 'Adjust Attendance Record' to modify existing records.");
                            previewRecord.setWillUpdate(false);
                        } else {
                            previewRecord.setValid(true);
                            previewRecord.setWillUpdate(false);
                        }
                    }
                } catch (Exception e) {
                    previewRecord.setErrorMessage(e.getMessage());
                }
                previewData.addRecord(previewRecord);
            }
        }
        return previewData;
    }

    private AttendanceRecord parseRowToAttendanceRecord(Row row, String importBatchID) throws Exception {
        Cell employeeCodeCell = row.getCell(0);
        Cell attendanceDateCell = row.getCell(1);
        Cell checkInTimeCell = row.getCell(2);
        Cell checkOutTimeCell = row.getCell(3);
        Cell statusCell = row.getCell(4);
        Cell overtimeHoursCell = row.getCell(5);

        String employeeCode = getCellValueAsString(employeeCodeCell);
        if (employeeCode.isEmpty()) throw new Exception("Employee Code is required");
        Employee employee = employeeDAO.getEmployeeByCode(employeeCode);
        if (employee == null) throw new Exception("Employee with code '" + employeeCode + "' not found");

        if (attendanceDateCell == null) throw new Exception("Attendance Date is required");
        Date attendanceDate;
        if (attendanceDateCell.getCellType() == CellType.NUMERIC) {
            attendanceDate = new Date(attendanceDateCell.getDateCellValue().getTime());
        } else {
            String dateStr = attendanceDateCell.getStringCellValue().trim();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            attendanceDate = new Date(sdf.parse(dateStr).getTime());
        }

        Time checkInTime = null;
        if (checkInTimeCell != null && checkInTimeCell.getCellType() != CellType.BLANK) {
            checkInTime = parseTimeCell(checkInTimeCell);
        }

        Time checkOutTime = null;
        if (checkOutTimeCell != null && checkOutTimeCell.getCellType() != CellType.BLANK) {
            checkOutTime = parseTimeCell(checkOutTimeCell);
        }

        String status = getCellValueAsString(statusCell);
        if (status.isEmpty()) {
            status = "Present";
        } else if (!isValidStatus(status)) {
            throw new Exception("Invalid status: " + status);
        }

        Double overtimeHours = 0.0;
        if (overtimeHoursCell != null && overtimeHoursCell.getCellType() == CellType.NUMERIC) {
            overtimeHours = overtimeHoursCell.getNumericCellValue();
        }

        AttendanceRecord record = new AttendanceRecord();
        record.setEmployeeID(employee.getEmployeeID());
        record.setAttendanceDate(attendanceDate);
        record.setCheckInTime(checkInTime);
        record.setCheckOutTime(checkOutTime);
        record.setStatus(status);
        record.setOvertimeHours(overtimeHours);
        record.setIsManualAdjustment(false);
        record.setImportBatchID(importBatchID);
        return record;
    }

    private boolean isValidStatus(String status) {
        return status.equals("Present") || status.equals("Absent") ||
               status.equals("Late") || status.equals("Early Leave") ||
               status.equals("Business Trip") || status.equals("Remote");
    }

    private Time parseTimeCell(Cell cell) throws Exception {
        if (cell == null) return null;
        try {
            switch (cell.getCellType()) {
                case STRING:
                    String timeStr = cell.getStringCellValue().trim();
                    if (timeStr.isEmpty()) return null;
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    return new Time(sdf.parse(timeStr).getTime());
                case NUMERIC:
                    java.util.Date excelDate = cell.getDateCellValue();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(excelDate);
                    int hours = cal.get(Calendar.HOUR_OF_DAY);
                    int minutes = cal.get(Calendar.MINUTE);
                    int seconds = cal.get(Calendar.SECOND);
                    cal.clear();
                    cal.set(1970, 0, 1, hours, minutes, seconds);
                    return new Time(cal.getTimeInMillis());
                case BLANK:
                    return null;
                default:
                    return null;
            }
        } catch (Exception e) {
            throw new Exception("Invalid time format: " + e.getMessage());
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double numValue = cell.getNumericCellValue();
                    if (numValue == (long) numValue) {
                        return String.format("%d", (long) numValue);
                    } else {
                        return String.format("%s", numValue);
                    }
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case STRING:
                        return cell.getStringCellValue().trim();
                    case NUMERIC:
                        double numValue = cell.getNumericCellValue();
                        if (numValue == (long) numValue) {
                            return String.format("%d", (long) numValue);
                        } else {
                            return String.format("%s", numValue);
                        }
                    default:
                        return "";
                }
            case BLANK:
                return "";
            default:
                return "";
        }
    }
}

