package model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * SalaryComponent model class - mapped to salary_components table
 * Represents base salary and allowances for an employee
 * @author admin
 */
public class SalaryComponent {
    // Primary fields from salary_components table
    private int componentID;                    // component_id (Primary Key)
    private int employeeID;                     // employee_id (Foreign Key)
    private BigDecimal baseSalary;              // base_salary (NOT NULL)
    private BigDecimal positionAllowance;       // position_allowance
    private BigDecimal housingAllowance;        // housing_allowance
    private BigDecimal transportationAllowance; // transportation_allowance
    private BigDecimal mealAllowance;           // meal_allowance
    private BigDecimal otherAllowances;         // other_allowances
    private Date effectiveFrom;                 // effective_from (NOT NULL)
    private Date effectiveTo;                   // effective_to
    private boolean isActive;                   // is_active
    private Timestamp createdAt;                // created_at
    private Timestamp updatedAt;                // updated_at
    
    // Default constructor
    public SalaryComponent() {
        this.positionAllowance = BigDecimal.ZERO;
        this.housingAllowance = BigDecimal.ZERO;
        this.transportationAllowance = BigDecimal.ZERO;
        this.mealAllowance = BigDecimal.ZERO;
        this.otherAllowances = BigDecimal.ZERO;
        this.isActive = true;
    }
    
    // Constructor without ID (for new records)
    public SalaryComponent(int employeeID, BigDecimal baseSalary, BigDecimal positionAllowance,
                          BigDecimal housingAllowance, BigDecimal transportationAllowance,
                          BigDecimal mealAllowance, BigDecimal otherAllowances,
                          Date effectiveFrom, Date effectiveTo, boolean isActive) {
        this.employeeID = employeeID;
        this.baseSalary = baseSalary;
        this.positionAllowance = positionAllowance != null ? positionAllowance : BigDecimal.ZERO;
        this.housingAllowance = housingAllowance != null ? housingAllowance : BigDecimal.ZERO;
        this.transportationAllowance = transportationAllowance != null ? transportationAllowance : BigDecimal.ZERO;
        this.mealAllowance = mealAllowance != null ? mealAllowance : BigDecimal.ZERO;
        this.otherAllowances = otherAllowances != null ? otherAllowances : BigDecimal.ZERO;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
        this.isActive = isActive;
    }
    
    // Full constructor
    public SalaryComponent(int componentID, int employeeID, BigDecimal baseSalary,
                          BigDecimal positionAllowance, BigDecimal housingAllowance,
                          BigDecimal transportationAllowance, BigDecimal mealAllowance,
                          BigDecimal otherAllowances, Date effectiveFrom, Date effectiveTo,
                          boolean isActive, Timestamp createdAt, Timestamp updatedAt) {
        this.componentID = componentID;
        this.employeeID = employeeID;
        this.baseSalary = baseSalary;
        this.positionAllowance = positionAllowance;
        this.housingAllowance = housingAllowance;
        this.transportationAllowance = transportationAllowance;
        this.mealAllowance = mealAllowance;
        this.otherAllowances = otherAllowances;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getComponentID() {
        return componentID;
    }
    
    public void setComponentID(int componentID) {
        this.componentID = componentID;
    }
    
    public int getEmployeeID() {
        return employeeID;
    }
    
    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
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
    
    public BigDecimal getHousingAllowance() {
        return housingAllowance;
    }
    
    public void setHousingAllowance(BigDecimal housingAllowance) {
        this.housingAllowance = housingAllowance;
    }
    
    public BigDecimal getTransportationAllowance() {
        return transportationAllowance;
    }
    
    public void setTransportationAllowance(BigDecimal transportationAllowance) {
        this.transportationAllowance = transportationAllowance;
    }
    
    public BigDecimal getMealAllowance() {
        return mealAllowance;
    }
    
    public void setMealAllowance(BigDecimal mealAllowance) {
        this.mealAllowance = mealAllowance;
    }
    
    public BigDecimal getOtherAllowances() {
        return otherAllowances;
    }
    
    public void setOtherAllowances(BigDecimal otherAllowances) {
        this.otherAllowances = otherAllowances;
    }
    
    public Date getEffectiveFrom() {
        return effectiveFrom;
    }
    
    public void setEffectiveFrom(Date effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }
    
    public Date getEffectiveTo() {
        return effectiveTo;
    }
    
    public void setEffectiveTo(Date effectiveTo) {
        this.effectiveTo = effectiveTo;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
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
    
    /**
     * Calculate total monthly compensation
     * @return Total of base salary and all allowances
     */
    public BigDecimal getTotalMonthlySalary() {
        return baseSalary
                .add(positionAllowance != null ? positionAllowance : BigDecimal.ZERO)
                .add(housingAllowance != null ? housingAllowance : BigDecimal.ZERO)
                .add(transportationAllowance != null ? transportationAllowance : BigDecimal.ZERO)
                .add(mealAllowance != null ? mealAllowance : BigDecimal.ZERO)
                .add(otherAllowances != null ? otherAllowances : BigDecimal.ZERO);
    }
    
    @Override
    public String toString() {
        return "SalaryComponent{" +
                "componentID=" + componentID +
                ", employeeID=" + employeeID +
                ", baseSalary=" + baseSalary +
                ", positionAllowance=" + positionAllowance +
                ", housingAllowance=" + housingAllowance +
                ", transportationAllowance=" + transportationAllowance +
                ", mealAllowance=" + mealAllowance +
                ", otherAllowances=" + otherAllowances +
                ", effectiveFrom=" + effectiveFrom +
                ", effectiveTo=" + effectiveTo +
                ", isActive=" + isActive +
                ", totalMonthlySalary=" + getTotalMonthlySalary() +
                '}';
    }
}

