package model;

import java.math.BigDecimal;

/**
 * Model class for Position
 */
public class Position {
    private int positionId;
    private String positionName;
    private String description;
    private BigDecimal baseSalary;
    private BigDecimal positionAllowance;
    
    // Default constructor
    public Position() {
    }
    
    // Getters and Setters
    public int getPositionId() {
        return positionId;
    }
    
    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }
    
    public String getPositionName() {
        return positionName;
    }
    
    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getBaseSalary() {
        return baseSalary;
    }
    
    public void setBaseSalary(BigDecimal baseSalary) {
        this.baseSalary = baseSalary;
    }
    
    public BigDecimal getPositionAllowance() {
        return positionAllowance;
    }
    
    public void setPositionAllowance(BigDecimal positionAllowance) {
        this.positionAllowance = positionAllowance;
    }
}
