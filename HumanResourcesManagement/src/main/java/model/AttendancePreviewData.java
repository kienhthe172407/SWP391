package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class to hold attendance import preview data
 * Used to display parsed records before saving to database
 */
public class AttendancePreviewData {
    private List<AttendancePreviewRecord> records;
    private int totalRecords;
    private int validRecords;
    private int invalidRecords;
    private String importBatchID;
    
    public AttendancePreviewData() {
        this.records = new ArrayList<>();
        this.totalRecords = 0;
        this.validRecords = 0;
        this.invalidRecords = 0;
    }
    
    public AttendancePreviewData(String importBatchID) {
        this();
        this.importBatchID = importBatchID;
    }
    
    // Getters and Setters
    public List<AttendancePreviewRecord> getRecords() {
        return records;
    }
    
    public void setRecords(List<AttendancePreviewRecord> records) {
        this.records = records;
    }
    
    public void addRecord(AttendancePreviewRecord record) {
        this.records.add(record);
        this.totalRecords++;
        if (record.isValid()) {
            this.validRecords++;
        } else {
            this.invalidRecords++;
        }
    }
    
    public int getTotalRecords() {
        return totalRecords;
    }
    
    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }
    
    public int getValidRecords() {
        return validRecords;
    }
    
    public void setValidRecords(int validRecords) {
        this.validRecords = validRecords;
    }
    
    public int getInvalidRecords() {
        return invalidRecords;
    }
    
    public void setInvalidRecords(int invalidRecords) {
        this.invalidRecords = invalidRecords;
    }
    
    public String getImportBatchID() {
        return importBatchID;
    }
    
    public void setImportBatchID(String importBatchID) {
        this.importBatchID = importBatchID;
    }
    
    /**
     * Inner class to represent a single preview record
     */
    public static class AttendancePreviewRecord {
        private int rowNumber;
        private AttendanceRecord record;
        private String employeeCode;
        private String employeeName;
        private boolean valid;
        private String errorMessage;
        private boolean willUpdate; // true if record exists and will be updated
        
        public AttendancePreviewRecord(int rowNumber) {
            this.rowNumber = rowNumber;
            this.valid = true;
            this.willUpdate = false;
        }
        
        // Getters and Setters
        public int getRowNumber() {
            return rowNumber;
        }
        
        public void setRowNumber(int rowNumber) {
            this.rowNumber = rowNumber;
        }
        
        public AttendanceRecord getRecord() {
            return record;
        }
        
        public void setRecord(AttendanceRecord record) {
            this.record = record;
        }
        
        public String getEmployeeCode() {
            return employeeCode;
        }
        
        public void setEmployeeCode(String employeeCode) {
            this.employeeCode = employeeCode;
        }
        
        public String getEmployeeName() {
            return employeeName;
        }
        
        public void setEmployeeName(String employeeName) {
            this.employeeName = employeeName;
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public void setValid(boolean valid) {
            this.valid = valid;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
        
        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            this.valid = false;
        }
        
        public boolean isWillUpdate() {
            return willUpdate;
        }
        
        public void setWillUpdate(boolean willUpdate) {
            this.willUpdate = willUpdate;
        }
    }
}

