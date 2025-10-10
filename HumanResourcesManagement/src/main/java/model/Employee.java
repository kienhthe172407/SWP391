package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Employee model class - mapped to employees table
 * @author admin
 */
public class Employee {
    // Primary fields from employees table
    private int employeeID;                     // employee_id
    private Integer userID;                     // user_id (can be null)
    private String employeeCode;                // employee_code (UNIQUE, NOT NULL)
    private String firstName;                   // first_name
    private String lastName;                    // last_name
    private Date dateOfBirth;                   // date_of_birth
    private String gender;                      // gender (Male, Female, Other)
    private String phoneNumber;                 // phone_number
    private String personalEmail;               // personal_email
    private String homeAddress;                 // home_address
    private String emergencyContactName;        // emergency_contact_name
    private String emergencyContactPhone;       // emergency_contact_phone
    private Integer departmentID;               // department_id
    private Integer positionID;                 // position_id (was jobTitleID)
    private Integer managerID;                  // manager_id
    private Date hireDate;                      // hire_date
    private String employmentStatus;            // employment_status (Active, On Leave, Terminated)
    private Timestamp createdAt;                // created_at
    private Timestamp updatedAt;                // updated_at
    
    // Additional fields for display (from JOINs)
    private String departmentName;
    private String positionName;
    private String managerName;
    
    // Default constructor
    public Employee() {
    }
    
    // Basic constructor for backward compatibility
    public Employee(int employeeID, String firstName, String lastName, Date dateOfBirth, 
                   String gender, String phoneNumber, Integer departmentID, Integer positionID) {
        this.employeeID = employeeID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.departmentID = departmentID;
        this.positionID = positionID;
    }
    
    // Full constructor
    public Employee(int employeeID, Integer userID, String employeeCode, String firstName, String lastName,
                   Date dateOfBirth, String gender, String phoneNumber, String personalEmail,
                   String homeAddress, String emergencyContactName, String emergencyContactPhone,
                   Integer departmentID, Integer positionID, Integer managerID, Date hireDate,
                   String employmentStatus, Timestamp createdAt, Timestamp updatedAt) {
        this.employeeID = employeeID;
        this.userID = userID;
        this.employeeCode = employeeCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.personalEmail = personalEmail;
        this.homeAddress = homeAddress;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
        this.departmentID = departmentID;
        this.positionID = positionID;
        this.managerID = managerID;
        this.hireDate = hireDate;
        this.employmentStatus = employmentStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPersonalEmail() {
        return personalEmail;
    }

    public void setPersonalEmail(String personalEmail) {
        this.personalEmail = personalEmail;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    public Integer getPositionID() {
        return positionID;
    }

    public void setPositionID(Integer positionID) {
        this.positionID = positionID;
    }

    public Integer getManagerID() {
        return managerID;
    }

    public void setManagerID(Integer managerID) {
        this.managerID = managerID;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
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

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    // Backward compatibility methods
    @Deprecated
    public Date getDob() {
        return dateOfBirth;
    }

    @Deprecated
    public void setDob(Date dob) {
        this.dateOfBirth = dob;
    }

    @Deprecated
    public String getContactInfo() {
        // For backward compatibility - return phone or email
        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            return phoneNumber;
        }
        return personalEmail;
    }

    @Deprecated
    public void setContactInfo(String contactInfo) {
        this.phoneNumber = contactInfo;
    }

    @Deprecated
    public int getJobTitleID() {
        return positionID != null ? positionID : 0;
    }

    @Deprecated
    public void setJobTitleID(int jobTitleID) {
        this.positionID = jobTitleID;
    }
    
    // Helper method to get full name
    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeID=" + employeeID +
                ", employeeCode='" + employeeCode + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender='" + gender + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", personalEmail='" + personalEmail + '\'' +
                ", departmentID=" + departmentID +
                ", positionID=" + positionID +
                ", hireDate=" + hireDate +
                ", employmentStatus='" + employmentStatus + '\'' +
                '}';
    }
}

