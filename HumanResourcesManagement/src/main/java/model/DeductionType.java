package model;

import java.sql.Timestamp;

/**
 * DeductionType model class - mapped to deduction_types table
 * Represents different types of deductions that can be applied to employee salaries
 * @author admin
 */
public class DeductionType {
    // Primary fields from deduction_types table
    private int deductionTypeID;            // deduction_type_id (Primary Key)
    private String deductionName;           // deduction_name (UNIQUE, NOT NULL)
    private String description;             // description
    private String calculationType;         // calculation_type (Fixed, Percentage, Formula)
    private Double defaultAmount;           // default_amount (for Fixed type)
    private Double defaultPercentage;       // default_percentage (for Percentage type)
    private boolean isMandatory;            // is_mandatory
    private Timestamp createdAt;            // created_at
    private Timestamp updatedAt;            // updated_at
    
    // Default constructor
    public DeductionType() {
    }
    
    // Constructor without ID (for creating new deduction)
    public DeductionType(String deductionName, String description, String calculationType,
                        Double defaultAmount, Double defaultPercentage, boolean isMandatory) {
        this.deductionName = deductionName;
        this.description = description;
        this.calculationType = calculationType;
        this.defaultAmount = defaultAmount;
        this.defaultPercentage = defaultPercentage;
        this.isMandatory = isMandatory;
    }
    
    // Full constructor
    public DeductionType(int deductionTypeID, String deductionName, String description,
                        String calculationType, Double defaultAmount, Double defaultPercentage,
                        boolean isMandatory, Timestamp createdAt, Timestamp updatedAt) {
        this.deductionTypeID = deductionTypeID;
        this.deductionName = deductionName;
        this.description = description;
        this.calculationType = calculationType;
        this.defaultAmount = defaultAmount;
        this.defaultPercentage = defaultPercentage;
        this.isMandatory = isMandatory;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getDeductionTypeID() {
        return deductionTypeID;
    }
    
    public void setDeductionTypeID(int deductionTypeID) {
        this.deductionTypeID = deductionTypeID;
    }
    
    public String getDeductionName() {
        return deductionName;
    }
    
    public void setDeductionName(String deductionName) {
        this.deductionName = deductionName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCalculationType() {
        return calculationType;
    }
    
    public void setCalculationType(String calculationType) {
        this.calculationType = calculationType;
    }
    
    public Double getDefaultAmount() {
        return defaultAmount;
    }
    
    public void setDefaultAmount(Double defaultAmount) {
        this.defaultAmount = defaultAmount;
    }
    
    public Double getDefaultPercentage() {
        return defaultPercentage;
    }
    
    public void setDefaultPercentage(Double defaultPercentage) {
        this.defaultPercentage = defaultPercentage;
    }
    
    public boolean isMandatory() {
        return isMandatory;
    }
    
    public void setMandatory(boolean mandatory) {
        isMandatory = mandatory;
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
    
    @Override
    public String toString() {
        return "DeductionType{" +
                "deductionTypeID=" + deductionTypeID +
                ", deductionName='" + deductionName + '\'' +
                ", description='" + description + '\'' +
                ", calculationType='" + calculationType + '\'' +
                ", defaultAmount=" + defaultAmount +
                ", defaultPercentage=" + defaultPercentage +
                ", isMandatory=" + isMandatory +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

