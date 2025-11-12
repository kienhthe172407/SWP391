package util;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import model.Contract;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for generating PDF documents from contract data
 * Uses iText 7 library for PDF creation
 */
public class ContractPDFExportUtil {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    
    // Color definitions
    private static final DeviceRgb HEADER_COLOR = new DeviceRgb(52, 58, 64); // Dark gray
    private static final DeviceRgb LIGHT_GRAY = new DeviceRgb(248, 249, 250);
    private static final DeviceRgb PRIMARY_COLOR = new DeviceRgb(37, 99, 235); // Blue
    
    /**
     * Generate PDF document for contract
     * @param contract Contract to export
     * @return byte array of PDF file
     * @throws IOException if error occurs during generation
     */
    public static byte[] generateContractPDF(Contract contract) throws IOException {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        // Title
        Paragraph title = new Paragraph("EMPLOYMENT CONTRACT")
                .setFontSize(24)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        document.add(title);
        
        // Contract Number and Status
        Paragraph contractInfo = new Paragraph()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
        
        String contractNumber = contract.getContractNumber() != null 
            ? contract.getContractNumber() 
            : "Contract #" + contract.getContractID();
        
        contractInfo.add(new Paragraph(contractNumber)
                .setFontSize(14)
                .setBold());
        contractInfo.add(new Paragraph("Status: " + contract.getContractStatus())
                .setFontSize(10)
                .setItalic());
        
        document.add(contractInfo);
        
        // Export date
        Paragraph exportDate = new Paragraph("Exported on: " + DATETIME_FORMAT.format(new Date()))
                .setFontSize(9)
                .setTextAlignment(TextAlignment.RIGHT)
                .setMarginBottom(20);
        document.add(exportDate);
        
        // Basic Information Section
        document.add(new Paragraph("BASIC INFORMATION")
                .setFontSize(14)
                .setBold()
                .setMarginTop(15)
                .setMarginBottom(10));
        
        Table basicInfoTable = new Table(UnitValue.createPercentArray(new float[]{40, 60}))
                .useAllAvailableWidth()
                .setMarginBottom(15);
        
        addTableRow(basicInfoTable, "Contract ID:", String.valueOf(contract.getContractID()));
        addTableRow(basicInfoTable, "Contract Number:", contractNumber);
        addTableRow(basicInfoTable, "Contract Type:", contract.getContractType() != null ? contract.getContractType() : "N/A");
        addTableRow(basicInfoTable, "Status:", contract.getContractStatus());
        
        document.add(basicInfoTable);
        
        // Employee Information Section
        document.add(new Paragraph("EMPLOYEE INFORMATION")
                .setFontSize(14)
                .setBold()
                .setMarginTop(15)
                .setMarginBottom(10));
        
        Table employeeTable = new Table(UnitValue.createPercentArray(new float[]{40, 60}))
                .useAllAvailableWidth()
                .setMarginBottom(15);
        
        addTableRow(employeeTable, "Employee Name:", 
            contract.getEmployeeFullName() != null ? contract.getEmployeeFullName() : "N/A");
        addTableRow(employeeTable, "Employee Code:", 
            contract.getEmployeeCode() != null ? contract.getEmployeeCode() : "N/A");
        addTableRow(employeeTable, "Phone:", 
            contract.getEmployeePhone() != null ? contract.getEmployeePhone() : "N/A");
        addTableRow(employeeTable, "Email:", 
            contract.getEmployeeEmail() != null ? contract.getEmployeeEmail() : "N/A");
        
        document.add(employeeTable);
        
        // Contract Dates Section
        document.add(new Paragraph("CONTRACT DATES")
                .setFontSize(14)
                .setBold()
                .setMarginTop(15)
                .setMarginBottom(10));
        
        Table datesTable = new Table(UnitValue.createPercentArray(new float[]{40, 60}))
                .useAllAvailableWidth()
                .setMarginBottom(15);
        
        if (contract.getStartDate() != null) {
            addTableRow(datesTable, "Start Date:", DATE_FORMAT.format(contract.getStartDate()));
        } else {
            addTableRow(datesTable, "Start Date:", "N/A");
        }
        
        if (contract.getEndDate() != null) {
            addTableRow(datesTable, "End Date:", DATE_FORMAT.format(contract.getEndDate()));
        } else {
            addTableRow(datesTable, "End Date:", "Indefinite");
        }
        
        if (contract.getSignedDate() != null) {
            addTableRow(datesTable, "Signed Date:", DATE_FORMAT.format(contract.getSignedDate()));
        }
        
        document.add(datesTable);
        
        // Salary Information Section
        if (contract.getSalaryAmount() != null) {
            document.add(new Paragraph("SALARY INFORMATION")
                    .setFontSize(14)
                    .setBold()
                    .setMarginTop(15)
                    .setMarginBottom(10));
            
            Table salaryTable = new Table(UnitValue.createPercentArray(new float[]{40, 60}))
                    .useAllAvailableWidth()
                    .setMarginBottom(15);
            
            BigDecimal salary = contract.getSalaryAmount();
            String salaryFormatted = String.format("$%,.2f", salary);
            addTableRow(salaryTable, "Salary Amount:", salaryFormatted + " per month");
            
            document.add(salaryTable);
        }
        
        // Contract Details Section
        if (contract.getJobDescription() != null && !contract.getJobDescription().trim().isEmpty()) {
            document.add(new Paragraph("CONTRACT DETAILS")
                    .setFontSize(14)
                    .setBold()
                    .setMarginTop(15)
                    .setMarginBottom(10));
            
            Paragraph details = new Paragraph(contract.getJobDescription())
                    .setMarginBottom(15)
                    .setPadding(10)
                    .setBackgroundColor(LIGHT_GRAY);
            document.add(details);
        }
        
        // Approval Information Section
        if (contract.getApprovedAt() != null) {
            document.add(new Paragraph("APPROVAL INFORMATION")
                    .setFontSize(14)
                    .setBold()
                    .setMarginTop(15)
                    .setMarginBottom(10));
            
            Table approvalTable = new Table(UnitValue.createPercentArray(new float[]{40, 60}))
                    .useAllAvailableWidth()
                    .setMarginBottom(15);
            
            addTableRow(approvalTable, "Approved Date:", 
                DATETIME_FORMAT.format(contract.getApprovedAt()));
            
            if (contract.getApprovedByName() != null) {
                addTableRow(approvalTable, "Approved By:", contract.getApprovedByName());
            }
            
            if (contract.getApprovalComment() != null && !contract.getApprovalComment().trim().isEmpty()) {
                addTableRow(approvalTable, "Approval Comment:", contract.getApprovalComment());
            }
            
            document.add(approvalTable);
        }
        
        // Contract Metadata Section
        document.add(new Paragraph("CONTRACT METADATA")
                .setFontSize(14)
                .setBold()
                .setMarginTop(15)
                .setMarginBottom(10));
        
        Table metadataTable = new Table(UnitValue.createPercentArray(new float[]{40, 60}))
                .useAllAvailableWidth()
                .setMarginBottom(15);
        
        if (contract.getCreatedAt() != null) {
            addTableRow(metadataTable, "Created Date:", 
                DATETIME_FORMAT.format(contract.getCreatedAt()));
        }
        
        if (contract.getUpdatedAt() != null) {
            addTableRow(metadataTable, "Last Updated:", 
                DATETIME_FORMAT.format(contract.getUpdatedAt()));
        }
        
        document.add(metadataTable);
        
        // Footer
        Paragraph footer = new Paragraph("This is an official document generated by HR Management System")
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(30)
                .setItalic();
        document.add(footer);
        
        document.close();
        
        return baos.toByteArray();
    }
    
    /**
     * Helper method to add a row to a table
     */
    private static void addTableRow(Table table, String label, String value) {
        com.itextpdf.layout.element.Cell labelCell = new com.itextpdf.layout.element.Cell()
                .add(new Paragraph(label).setBold())
                .setBackgroundColor(LIGHT_GRAY)
                .setPadding(8);
        
        com.itextpdf.layout.element.Cell valueCell = new com.itextpdf.layout.element.Cell()
                .add(new Paragraph(value != null ? value : "N/A"))
                .setPadding(8);
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}

