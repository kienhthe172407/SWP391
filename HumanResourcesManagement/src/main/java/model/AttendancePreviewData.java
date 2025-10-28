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

    // Leave request integration statistics
    private int recordsMatchingLeave;      // Records that match approved leave
    private int recordsWithConflict;       // Has approved leave but marked Present
    private int absentWithoutLeave;        // Absent with no approved leave request
    
    public AttendancePreviewData() {
        this.records = new ArrayList<>();
        this.totalRecords = 0;
        this.validRecords = 0;
        this.invalidRecords = 0;
        this.recordsMatchingLeave = 0;
        this.recordsWithConflict = 0;
        this.absentWithoutLeave = 0;
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

    public int getRecordsMatchingLeave() {
        return recordsMatchingLeave;
    }

    public void setRecordsMatchingLeave(int recordsMatchingLeave) {
        this.recordsMatchingLeave = recordsMatchingLeave;
    }

    public void incrementRecordsMatchingLeave() {
        this.recordsMatchingLeave++;
    }

    public int getRecordsWithConflict() {
        return recordsWithConflict;
    }

    public void setRecordsWithConflict(int recordsWithConflict) {
        this.recordsWithConflict = recordsWithConflict;
    }

    public void incrementRecordsWithConflict() {
        this.recordsWithConflict++;
    }

    public int getAbsentWithoutLeave() {
        return absentWithoutLeave;
    }

    public void setAbsentWithoutLeave(int absentWithoutLeave) {
        this.absentWithoutLeave = absentWithoutLeave;
    }

    public void incrementAbsentWithoutLeave() {
        this.absentWithoutLeave++;
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

        // Leave request integration fields
        private boolean hasApprovedLeave;           // True if employee has approved leave for this date
        private String leaveRequestType;            // Type of leave (Annual Leave, Sick Leave, etc.)
        private Integer leaveRequestID;             // ID of the leave request
        private String suggestedStatus;             // Suggested attendance status based on leave type
        private String statusIndicator;             // Visual indicator: "match", "conflict", "warning", "normal"
        private String statusMessage;               // Message to display about the status
        private boolean autoFilled;                 // True if status was auto-filled from leave request
        
        public AttendancePreviewRecord(int rowNumber) {
            this.rowNumber = rowNumber;
            this.valid = true;
            this.willUpdate = false;
            this.hasApprovedLeave = false;
            this.autoFilled = false;
            this.statusIndicator = "normal";
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

        public boolean isHasApprovedLeave() {
            return hasApprovedLeave;
        }

        public void setHasApprovedLeave(boolean hasApprovedLeave) {
            this.hasApprovedLeave = hasApprovedLeave;
        }

        public String getLeaveRequestType() {
            return leaveRequestType;
        }

        public void setLeaveRequestType(String leaveRequestType) {
            this.leaveRequestType = leaveRequestType;
        }

        public Integer getLeaveRequestID() {
            return leaveRequestID;
        }

        public void setLeaveRequestID(Integer leaveRequestID) {
            this.leaveRequestID = leaveRequestID;
        }

        public String getSuggestedStatus() {
            return suggestedStatus;
        }

        public void setSuggestedStatus(String suggestedStatus) {
            this.suggestedStatus = suggestedStatus;
        }

        public String getStatusIndicator() {
            return statusIndicator;
        }

        public void setStatusIndicator(String statusIndicator) {
            this.statusIndicator = statusIndicator;
        }

        public String getStatusMessage() {
            return statusMessage;
        }

        public void setStatusMessage(String statusMessage) {
            this.statusMessage = statusMessage;
        }

        public boolean isAutoFilled() {
            return autoFilled;
        }

        public void setAutoFilled(boolean autoFilled) {
            this.autoFilled = autoFilled;
        }
    }
}

