package model;

import java.sql.Timestamp;

/**
 * BenefitType model class - mapped to benefit_types table
 * Represents different types of benefits that can be applied to employee salaries
 * @author admin
 */
public class BenefitType {
    // Primary fields from benefit_types table
    private int benefitTypeID;              // benefit_type_id (Primary Key)
    private String benefitName;             // benefit_name (UNIQUE, NOT NULL)
    private String description;             // description
    private String calculationType;         // calculation_type (Fixed, Percentage, Formula)
    private Double defaultAmount;           // default_amount (for Fixed type)
    private Double defaultPercentage;       // default_percentage (for Percentage type)
    private boolean isTaxable;              // is_taxable
    private Timestamp createdAt;            // created_at
    private Timestamp updatedAt;            // updated_at
    
    // Default constructor
    public BenefitType() {
    }
    
    // Constructor without ID (for creating new benefit)
    public BenefitType(String benefitName, String description, String calculationType,
                      Double defaultAmount, Double defaultPercentage, boolean isTaxable) {
        this.benefitName = benefitName;
        this.description = description;
        this.calculationType = calculationType;
        this.defaultAmount = defaultAmount;
        this.defaultPercentage = defaultPercentage;
        this.isTaxable = isTaxable;
    }
    
    // Full constructor
    public BenefitType(int benefitTypeID, String benefitName, String description,
                      String calculationType, Double defaultAmount, Double defaultPercentage,
                      boolean isTaxable, Timestamp createdAt, Timestamp updatedAt) {
        this.benefitTypeID = benefitTypeID;
        this.benefitName = benefitName;
        this.description = description;
        this.calculationType = calculationType;
        this.defaultAmount = defaultAmount;
        this.defaultPercentage = defaultPercentage;
        this.isTaxable = isTaxable;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getBenefitTypeID() {
        return benefitTypeID;
    }
    
    public void setBenefitTypeID(int benefitTypeID) {
        this.benefitTypeID = benefitTypeID;
    }
    
    public String getBenefitName() {
        return benefitName;
    }
    
    public void setBenefitName(String benefitName) {
        this.benefitName = benefitName;
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
    
    public boolean isTaxable() {
        return isTaxable;
    }
    
    public void setTaxable(boolean taxable) {
        isTaxable = taxable;
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
        return "BenefitType{" +
                "benefitTypeID=" + benefitTypeID +
                ", benefitName='" + benefitName + '\'' +
                ", description='" + description + '\'' +
                ", calculationType='" + calculationType + '\'' +
                ", defaultAmount=" + defaultAmount +
                ", defaultPercentage=" + defaultPercentage +
                ", isTaxable=" + isTaxable +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

