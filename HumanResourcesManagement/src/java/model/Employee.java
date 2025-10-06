package model;

import java.util.Date;

/**
 * Employee model class
 * @author admin
 */
public class Employee {
    private int employeeID;
    private String firstName;
    private String lastName;
    private Date dob;
    private String gender;
    private String contactInfo;
    private int departmentID;
    private int jobTitleID;
    
    // Default constructor
    public Employee() {
    }
    
    // Full constructor
    public Employee(int employeeID, String firstName, String lastName, Date dob, 
                   String gender, String contactInfo, int departmentID, int jobTitleID) {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.gender = gender;
        this.contactInfo = contactInfo;
        this.departmentID = departmentID;
        this.jobTitleID = jobTitleID;
    }

    // Getters and Setters
    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public int getJobTitleID() {
        return jobTitleID;
    }

    public void setJobTitleID(int jobTitleID) {
        this.jobTitleID = jobTitleID;
    }
    
    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeID=" + employeeID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dob=" + dob +
                ", gender='" + gender + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", departmentID=" + departmentID +
                ", jobTitleID=" + jobTitleID +
                '}';
    }
}

