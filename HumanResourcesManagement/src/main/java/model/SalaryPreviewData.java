package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class to hold salary import preview data
 * Used to display parsed records before saving to database
 * @author admin
 */
public class SalaryPreviewData {
    private List<SalaryPreviewRecord> records;
    private int totalRecords;
    private int validRecords;
    private int invalidRecords;
    private int willUpdateRecords;
    private String importBatchID;
    
    public SalaryPreviewData() {
        this.records = new ArrayList<>();
        this.totalRecords = 0;
        this.validRecords = 0;
        this.invalidRecords = 0;
        this.willUpdateRecords = 0;
    }
    
    public SalaryPreviewData(String importBatchID) {
        this();
        this.importBatchID = importBatchID;
    }
    
    // Getters and Setters
    public List<SalaryPreviewRecord> getRecords() {
        return records;
    }
    
    public void setRecords(List<SalaryPreviewRecord> records) {
        this.records = records;
    }
    
    public void addRecord(SalaryPreviewRecord record) {
        this.records.add(record);
        this.totalRecords++;
        if (record.isValid()) {
            this.validRecords++;
            if (record.isWillUpdate()) {
                this.willUpdateRecords++;
            }
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
    
    public int getWillUpdateRecords() {
        return willUpdateRecords;
    }
    
    public void setWillUpdateRecords(int willUpdateRecords) {
        this.willUpdateRecords = willUpdateRecords;
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
    public static class SalaryPreviewRecord {
        private int rowNumber;
        private SalaryComponent salaryComponent;
        private String employeeCode;
        private String employeeName;
        private boolean valid;
        private String errorMessage;
        private boolean willUpdate; // true if active salary record exists and will be deactivated
        
        public SalaryPreviewRecord(int rowNumber) {
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
        
        public SalaryComponent getSalaryComponent() {
            return salaryComponent;
        }
        
        public void setSalaryComponent(SalaryComponent salaryComponent) {
            this.salaryComponent = salaryComponent;
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

