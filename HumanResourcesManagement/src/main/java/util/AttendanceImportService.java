package util;

import model.AttendanceRecord;
import dal.AttendanceDAO;
import dal.EmployeeDAO;
import model.Employee;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Service class for importing attendance records from Excel files
 * Handles parsing, validation, and database insertion
 */
public class AttendanceImportService {
    private AttendanceDAO attendanceDAO;
    private EmployeeDAO employeeDAO;
    private List<String> errorMessages;
    private List<String> successMessages;
    private int successCount;
    private int errorCount;
    
    public AttendanceImportService() {
        this.attendanceDAO = new AttendanceDAO();
        this.employeeDAO = new EmployeeDAO();
        this.errorMessages = new ArrayList<>();
        this.successMessages = new ArrayList<>();
        this.successCount = 0;
        this.errorCount = 0;
    }
    
    /**
     * Import attendance records from Excel file
     * Expected columns: Employee Code, Attendance Date, Check-in Time, Check-out Time, Status, Overtime Hours
     * 
     * @param inputStream InputStream of the Excel file
     * @param importBatchID Unique ID for this import batch
     * @return true if import completed (may have partial success)
     */
    public boolean importFromExcel(InputStream inputStream, String importBatchID) {
        try (Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            
            if (sheet == null) {
                errorMessages.add("Excel file has no sheets");
                return false;
            }
            
            // Skip header row (row 0)
            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;
                
                try {
                    AttendanceRecord record = parseRowToAttendanceRecord(row, importBatchID);
                    if (record != null) {
                        // Check if record already exists
                        AttendanceRecord existing = attendanceDAO.getAttendanceRecord(
                            record.getEmployeeID(), 
                            record.getAttendanceDate()
                        );
                        
                        if (existing != null) {
                            // Update existing record
                            record.setAttendanceID(existing.getAttendanceID());
                            if (attendanceDAO.updateAttendanceRecord(record)) {
                                successCount++;
                                successMessages.add("Row " + (rowIndex + 1) + ": Updated attendance record");
                            } else {
                                errorCount++;
                                errorMessages.add("Row " + (rowIndex + 1) + ": Failed to update record");
                            }
                        } else {
                            // Insert new record
                            if (attendanceDAO.insertAttendanceRecord(record)) {
                                successCount++;
                                successMessages.add("Row " + (rowIndex + 1) + ": Imported successfully");
                            } else {
                                errorCount++;
                                errorMessages.add("Row " + (rowIndex + 1) + ": Failed to insert record");
                            }
                        }
                    }
                } catch (Exception e) {
                    errorCount++;
                    errorMessages.add("Row " + (rowIndex + 1) + ": " + e.getMessage());
                }
            }
            
            return true;
        } catch (Exception e) {
            errorMessages.add("Error reading Excel file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Parse a single row from Excel to AttendanceRecord
     * @param row Excel row
     * @param importBatchID Batch ID for tracking
     * @return AttendanceRecord or null if parsing fails
     */
    private AttendanceRecord parseRowToAttendanceRecord(Row row, String importBatchID) throws Exception {
        // Get cells
        Cell employeeCodeCell = row.getCell(0);
        Cell attendanceDateCell = row.getCell(1);
        Cell checkInTimeCell = row.getCell(2);
        Cell checkOutTimeCell = row.getCell(3);
        Cell statusCell = row.getCell(4);
        Cell overtimeHoursCell = row.getCell(5);
        
        // Validate required fields
        String employeeCode = getCellValueAsString(employeeCodeCell);
        if (employeeCode.isEmpty()) {
            throw new Exception("Employee Code is required");
        }
        
        // Find employee by code
        Employee employee = employeeDAO.getEmployeeByCode(employeeCode);
        if (employee == null) {
            throw new Exception("Employee with code '" + employeeCode + "' not found");
        }
        
        // Parse attendance date
        if (attendanceDateCell == null) {
            throw new Exception("Attendance Date is required");
        }
        
        Date attendanceDate;
        if (attendanceDateCell.getCellType() == CellType.NUMERIC) {
            attendanceDate = new Date(attendanceDateCell.getDateCellValue().getTime());
        } else {
            String dateStr = attendanceDateCell.getStringCellValue().trim();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            attendanceDate = new Date(sdf.parse(dateStr).getTime());
        }
        
        // Parse times
        Time checkInTime = null;
        if (checkInTimeCell != null && checkInTimeCell.getCellType() != CellType.BLANK) {
            checkInTime = parseTimeCell(checkInTimeCell);
        }

        Time checkOutTime = null;
        if (checkOutTimeCell != null && checkOutTimeCell.getCellType() != CellType.BLANK) {
            checkOutTime = parseTimeCell(checkOutTimeCell);
        }
        
        // Parse status
        String status = getCellValueAsString(statusCell);
        if (status.isEmpty()) {
            status = "Present"; // Default status
        } else {
            // Validate status
            if (!isValidStatus(status)) {
                throw new Exception("Invalid status: " + status);
            }
        }
        
        // Parse overtime hours
        Double overtimeHours = 0.0;
        if (overtimeHoursCell != null && overtimeHoursCell.getCellType() == CellType.NUMERIC) {
            overtimeHours = overtimeHoursCell.getNumericCellValue();
        }
        
        // Create and return record
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
    
    /**
     * Validate attendance status
     * @param status Status to validate
     * @return true if valid
     */
    private boolean isValidStatus(String status) {
        return status.equals("Present") || status.equals("Absent") ||
               status.equals("Late") || status.equals("Early Leave") ||
               status.equals("Business Trip") || status.equals("Remote");
    }

    /**
     * Helper method to parse time from a cell
     * Handles both STRING (HH:mm:ss) and NUMERIC (Excel time format) cell types
     */
    private Time parseTimeCell(Cell cell) throws Exception {
        if (cell == null) {
            return null;
        }

        try {
            switch (cell.getCellType()) {
                case STRING:
                    String timeStr = cell.getStringCellValue().trim();
                    if (timeStr.isEmpty()) {
                        return null;
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    return new Time(sdf.parse(timeStr).getTime());

                case NUMERIC:
                    // Excel stores times as fractional days (0.0 to 1.0)
                    // We need to extract just the time portion
                    java.util.Date excelDate = cell.getDateCellValue();
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    cal.setTime(excelDate);

                    // Extract hours, minutes, seconds
                    int hours = cal.get(java.util.Calendar.HOUR_OF_DAY);
                    int minutes = cal.get(java.util.Calendar.MINUTE);
                    int seconds = cal.get(java.util.Calendar.SECOND);

                    // Create Time object
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

    /**
     * Helper method to get cell value as string, handling both STRING and NUMERIC cell types
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                // Check if it's a date
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    // Format numeric value as string (remove decimal if it's a whole number)
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
                // Evaluate formula and get the result
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
    
    // Getters
    public List<String> getErrorMessages() {
        return errorMessages;
    }
    
    public List<String> getSuccessMessages() {
        return successMessages;
    }
    
    public int getSuccessCount() {
        return successCount;
    }
    
    public int getErrorCount() {
        return errorCount;
    }
}

