package model;

import java.sql.Date;

/**
 * Contract model class
 * @author admin
 */
public class Contract {
    private int contractID;
    private int employeeID;
    private String contractType;
    private Date startDate;
    private Date endDate;
    private String status;
    
    // Employee information (for display in list)
    private String employeeFullName;
    private String employeeContactInfo;
    
    // Default constructor
    public Contract() {
    }
    
    // Full constructor
    public Contract(int contractID, int employeeID, String contractType, 
                   Date startDate, Date endDate, String status) {
        this.contractID = contractID;
        this.employeeID = employeeID;
        this.contractType = contractType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters and Setters
    public int getContractID() {
        return contractID;
    }

    public void setContractID(int contractID) {
        this.contractID = contractID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployeeFullName() {
        return employeeFullName;
    }

    public void setEmployeeFullName(String employeeFullName) {
        this.employeeFullName = employeeFullName;
    }

    public String getEmployeeContactInfo() {
        return employeeContactInfo;
    }

    public void setEmployeeContactInfo(String employeeContactInfo) {
        this.employeeContactInfo = employeeContactInfo;
    }

    @Override
    public String toString() {
        return "Contract{" +
                "contractID=" + contractID +
                ", employeeID=" + employeeID +
                ", contractType='" + contractType + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                '}';
    }
}
