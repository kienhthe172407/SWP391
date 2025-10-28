package model;

import java.sql.Timestamp;

/**
 * RequestType model class - mapped to request_types table
 * Represents different types of requests (Annual Leave, Sick Leave, etc.)
 * @author admin
 */
public class RequestType {
    // Primary fields from request_types table
    private int requestTypeID;                  // request_type_id
    private String requestTypeName;             // request_type_name
    private String description;                 // description
    private boolean requiresApproval;           // requires_approval
    private Integer maxDaysPerYear;             // max_days_per_year (NULL = unlimited)
    private boolean isPaid;                     // is_paid
    private Timestamp createdAt;                // created_at
    private Timestamp updatedAt;                // updated_at
    
    // Default constructor
    public RequestType() {
    }
    
    // Constructor for basic request type
    public RequestType(int requestTypeID, String requestTypeName, String description) {
        this.requestTypeID = requestTypeID;
        this.requestTypeName = requestTypeName;
        this.description = description;
    }
    
    // Full constructor
    public RequestType(int requestTypeID, String requestTypeName, String description,
                       boolean requiresApproval, Integer maxDaysPerYear, boolean isPaid,
                       Timestamp createdAt, Timestamp updatedAt) {
        this.requestTypeID = requestTypeID;
        this.requestTypeName = requestTypeName;
        this.description = description;
        this.requiresApproval = requiresApproval;
        this.maxDaysPerYear = maxDaysPerYear;
        this.isPaid = isPaid;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getRequestTypeID() {
        return requestTypeID;
    }

    public void setRequestTypeID(int requestTypeID) {
        this.requestTypeID = requestTypeID;
    }

    public String getRequestTypeName() {
        return requestTypeName;
    }

    public void setRequestTypeName(String requestTypeName) {
        this.requestTypeName = requestTypeName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }

    public Integer getMaxDaysPerYear() {
        return maxDaysPerYear;
    }

    public void setMaxDaysPerYear(Integer maxDaysPerYear) {
        this.maxDaysPerYear = maxDaysPerYear;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Helper methods
    public boolean hasMaxDaysLimit() {
        return maxDaysPerYear != null;
    }

    public String getMaxDaysDisplay() {
        if (maxDaysPerYear == null) {
            return "Unlimited";
        }
        return maxDaysPerYear + " days/year";
    }

    public String getPaidStatusDisplay() {
        return isPaid ? "Paid" : "Unpaid";
    }

    @Override
    public String toString() {
        return "RequestType{" +
                "requestTypeID=" + requestTypeID +
                ", requestTypeName='" + requestTypeName + '\'' +
                ", description='" + description + '\'' +
                ", requiresApproval=" + requiresApproval +
                ", maxDaysPerYear=" + maxDaysPerYear +
                ", isPaid=" + isPaid +
                '}';
    }
}

