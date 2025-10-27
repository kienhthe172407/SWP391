package model;

import java.sql.Timestamp;

/**
 * Model class for Company Information
 */
public class CompanyInformation {
    private int companyId;
    private String companyName;
    private String companyLogoPath;
    private String aboutUs;
    private String missionStatement;
    private String visionStatement;
    private String coreValues;
    private String address;
    private String phoneNumber;
    private String email;
    private String website;
    private Integer foundedYear;
    private String numberOfEmployees;
    private String industry;
    private String socialMediaLinks; // JSON string
    private boolean isActive;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Default constructor
    public CompanyInformation() {
    }
    
    // Getters and Setters
    public int getCompanyId() {
        return companyId;
    }
    
    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getCompanyLogoPath() {
        return companyLogoPath;
    }
    
    public void setCompanyLogoPath(String companyLogoPath) {
        this.companyLogoPath = companyLogoPath;
    }
    
    public String getAboutUs() {
        return aboutUs;
    }
    
    public void setAboutUs(String aboutUs) {
        this.aboutUs = aboutUs;
    }
    
    public String getMissionStatement() {
        return missionStatement;
    }
    
    public void setMissionStatement(String missionStatement) {
        this.missionStatement = missionStatement;
    }
    
    public String getVisionStatement() {
        return visionStatement;
    }
    
    public void setVisionStatement(String visionStatement) {
        this.visionStatement = visionStatement;
    }
    
    public String getCoreValues() {
        return coreValues;
    }
    
    public void setCoreValues(String coreValues) {
        this.coreValues = coreValues;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getWebsite() {
        return website;
    }
    
    public void setWebsite(String website) {
        this.website = website;
    }
    
    public Integer getFoundedYear() {
        return foundedYear;
    }
    
    public void setFoundedYear(Integer foundedYear) {
        this.foundedYear = foundedYear;
    }
    
    public String getNumberOfEmployees() {
        return numberOfEmployees;
    }
    
    public void setNumberOfEmployees(String numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }
    
    public String getIndustry() {
        return industry;
    }
    
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    
    public String getSocialMediaLinks() {
        return socialMediaLinks;
    }
    
    public void setSocialMediaLinks(String socialMediaLinks) {
        this.socialMediaLinks = socialMediaLinks;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean isActive) {
        this.isActive = isActive;
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
}

