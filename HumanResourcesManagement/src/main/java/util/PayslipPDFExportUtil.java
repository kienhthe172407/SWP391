package util;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import model.SalaryCalculationDetail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for generating PDF payslips
 * Uses iText 7 library for PDF creation
 */
public class PayslipPDFExportUtil {
    
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);
    
    // Color definitions
    private static final DeviceRgb HEADER_COLOR = new DeviceRgb(13, 110, 253); // Bootstrap primary blue
    private static final DeviceRgb LIGHT_GRAY = new DeviceRgb(248, 249, 250);
    private static final DeviceRgb DARK_GRAY = new DeviceRgb(52, 58, 64);
    private static final DeviceRgb SUCCESS_COLOR = new DeviceRgb(25, 135, 84); // Bootstrap success green
    private static final DeviceRgb LIGHT_YELLOW = new DeviceRgb(255, 243, 205);
    
    /**
     * Generate PDF payslip for a single employee
     * @param calc Salary calculation detail
     * @param year Year of payroll
     * @param month Month of payroll
     * @return byte array of PDF file
     * @throws IOException if error occurs during generation
     */
    public static byte[] generatePayslipPDF(SalaryCalculationDetail calc, int year, int month) throws IOException {
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        // Title
        Paragraph title = new Paragraph("PAYSLIP")
                .setFontSize(24)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(HEADER_COLOR)
                .setMarginBottom(5);
        document.add(title);
        
        // Period
        Paragraph period = new Paragraph(String.format("Period: %s-%02d", year, month))
                .setFontSize(14)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(15);
        document.add(period);
        
        // Employee Information Section
        addSectionHeader(document, "EMPLOYEE INFORMATION");
        
        Table empTable = new Table(UnitValue.createPercentArray(new float[]{1, 2}))
                .useAllAvailableWidth()
                .setMarginBottom(15);
        
        addInfoRow(empTable, "Employee Code:", calc.getEmployeeCode());
        addInfoRow(empTable, "Employee Name:", calc.getEmployeeName());
        addInfoRow(empTable, "Department:", calc.getDepartmentName());
        addInfoRow(empTable, "Position:", calc.getPositionName());
        
        document.add(empTable);
        
        // Base Salary & Allowances Section
        addSectionHeader(document, "BASE SALARY & ALLOWANCES");
        
        Table salaryTable = new Table(UnitValue.createPercentArray(new float[]{2, 1}))
                .useAllAvailableWidth()
                .setMarginBottom(15);
        
        addCurrencyRow(salaryTable, "Base Salary:", calc.getBaseSalary(), false);
        addCurrencyRow(salaryTable, "Position Allowance:", calc.getPositionAllowance(), false);
        addCurrencyRow(salaryTable, "Housing Allowance:", calc.getHousingAllowance(), false);
        addCurrencyRow(salaryTable, "Transportation Allowance:", calc.getTransportationAllowance(), false);
        addCurrencyRow(salaryTable, "Meal Allowance:", calc.getMealAllowance(), false);
        addCurrencyRow(salaryTable, "Other Allowances:", calc.getOtherAllowances(), false);
        addCurrencyRow(salaryTable, "Total Allowances:", calc.getTotalAllowances(), true);
        
        document.add(salaryTable);
        
        // Attendance & Overtime Section
        addSectionHeader(document, "ATTENDANCE & OVERTIME");
        
        Table attendanceTable = new Table(UnitValue.createPercentArray(new float[]{2, 1}))
                .useAllAvailableWidth()
                .setMarginBottom(15);
        
        addInfoRow(attendanceTable, "Working Days:", String.valueOf(calc.getWorkingDays()));
        addInfoRow(attendanceTable, "Absent Days:", String.valueOf(calc.getAbsentDays()));
        addInfoRow(attendanceTable, "Late Days:", String.valueOf(calc.getLateDays()));
        addInfoRow(attendanceTable, "Overtime Hours:", String.format("%.2f hours", calc.getOvertimeHours()));
        addCurrencyRow(attendanceTable, "Overtime Pay (1.5x):", calc.getOvertimePay(), false);
        
        document.add(attendanceTable);
        
        // Benefits Section (if any)
        if (calc.getBenefits() != null && !calc.getBenefits().isEmpty()) {
            addSectionHeader(document, "BENEFITS");
            
            Table benefitsTable = new Table(UnitValue.createPercentArray(new float[]{2, 1}))
                    .useAllAvailableWidth()
                    .setMarginBottom(15);
            
            for (SalaryCalculationDetail.BenefitDetail benefit : calc.getBenefits()) {
                String label = benefit.getBenefitName() + " (" + benefit.getCalculationType() + "):";
                addCurrencyRow(benefitsTable, label, benefit.getAmount(), false);
            }
            addCurrencyRow(benefitsTable, "Total Benefits:", calc.getTotalBenefits(), true);
            
            document.add(benefitsTable);
        }
        
        // Gross Salary Section
        addSectionHeader(document, "GROSS SALARY");
        
        Table grossTable = new Table(UnitValue.createPercentArray(new float[]{2, 1}))
                .useAllAvailableWidth()
                .setMarginBottom(15);
        
        Cell grossLabelCell = new Cell()
                .add(new Paragraph("Gross Salary:").setBold().setFontSize(12))
                .setBackgroundColor(HEADER_COLOR)
                .setFontColor(ColorConstants.WHITE)
                .setPadding(8)
                .setBorder(new SolidBorder(1));
        
        Cell grossValueCell = new Cell()
                .add(new Paragraph(formatCurrency(calc.getGrossSalary())).setBold().setFontSize(12))
                .setBackgroundColor(HEADER_COLOR)
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(8)
                .setBorder(new SolidBorder(1));
        
        grossTable.addCell(grossLabelCell);
        grossTable.addCell(grossValueCell);
        
        document.add(grossTable);
        
        // Deductions Section (if any)
        if (calc.getDeductions() != null && !calc.getDeductions().isEmpty()) {
            addSectionHeader(document, "DEDUCTIONS");
            
            Table deductionsTable = new Table(UnitValue.createPercentArray(new float[]{2, 1}))
                    .useAllAvailableWidth()
                    .setMarginBottom(15);
            
            for (SalaryCalculationDetail.DeductionDetail deduction : calc.getDeductions()) {
                String label = deduction.getDeductionName() + " (" + deduction.getCalculationType() + "):";
                addCurrencyRow(deductionsTable, label, deduction.getAmount(), false);
            }
            addCurrencyRow(deductionsTable, "Total Deductions:", calc.getTotalDeductions(), true);
            
            document.add(deductionsTable);
        }
        
        // Net Salary Section
        addSectionHeader(document, "NET SALARY (TAKE HOME)");
        
        Table netTable = new Table(UnitValue.createPercentArray(new float[]{2, 1}))
                .useAllAvailableWidth()
                .setMarginBottom(20);
        
        Cell netLabelCell = new Cell()
                .add(new Paragraph("Net Salary:").setBold().setFontSize(14))
                .setBackgroundColor(SUCCESS_COLOR)
                .setFontColor(ColorConstants.WHITE)
                .setPadding(10)
                .setBorder(new SolidBorder(1));
        
        Cell netValueCell = new Cell()
                .add(new Paragraph(formatCurrency(calc.getNetSalary())).setBold().setFontSize(14))
                .setBackgroundColor(SUCCESS_COLOR)
                .setFontColor(ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(10)
                .setBorder(new SolidBorder(1));
        
        netTable.addCell(netLabelCell);
        netTable.addCell(netValueCell);
        
        document.add(netTable);
        
        // Calculation Notes (if any)
        if (calc.getCalculationNotes() != null && !calc.getCalculationNotes().isEmpty()) {
            Paragraph notesHeader = new Paragraph("Notes:")
                    .setFontSize(10)
                    .setBold()
                    .setMarginBottom(5);
            document.add(notesHeader);
            
            for (String note : calc.getCalculationNotes()) {
                Paragraph notePara = new Paragraph("• " + note)
                        .setFontSize(9)
                        .setItalic()
                        .setMarginLeft(10);
                document.add(notePara);
            }
        }
        
        // Footer
        Paragraph footer = new Paragraph("Generated on: " + DATETIME_FORMAT.format(new Date()))
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20)
                .setItalic();
        document.add(footer);
        
        Paragraph disclaimer = new Paragraph("This is a computer-generated payslip and does not require a signature.")
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setItalic();
        document.add(disclaimer);
        
        document.close();
        return baos.toByteArray();
    }
    
    /**
     * Add section header to document
     */
    private static void addSectionHeader(Document document, String headerText) {
        Paragraph header = new Paragraph(headerText)
                .setFontSize(12)
                .setBold()
                .setBackgroundColor(LIGHT_GRAY)
                .setPadding(5)
                .setMarginTop(10)
                .setMarginBottom(5);
        document.add(header);
    }
    
    /**
     * Add info row to table (label + value)
     */
    private static void addInfoRow(Table table, String label, String value) {
        Cell labelCell = new Cell()
                .add(new Paragraph(label).setBold())
                .setBackgroundColor(LIGHT_GRAY)
                .setPadding(5)
                .setBorder(new SolidBorder(0.5f));
        
        Cell valueCell = new Cell()
                .add(new Paragraph(value != null ? value : ""))
                .setPadding(5)
                .setBorder(new SolidBorder(0.5f));
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
    
    /**
     * Add currency row to table (label + amount)
     */
    private static void addCurrencyRow(Table table, String label, BigDecimal amount, boolean isTotal) {
        Paragraph labelPara = new Paragraph(label);
        if (isTotal) {
            labelPara.setBold();
        }
        Cell labelCell = new Cell()
                .add(labelPara)
                .setBackgroundColor(isTotal ? LIGHT_YELLOW : LIGHT_GRAY)
                .setPadding(5)
                .setBorder(new SolidBorder(0.5f));

        Paragraph valuePara = new Paragraph(formatCurrency(amount));
        if (isTotal) {
            valuePara.setBold();
        }
        Cell valueCell = new Cell()
                .add(valuePara)
                .setBackgroundColor(isTotal ? LIGHT_YELLOW : ColorConstants.WHITE)
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(5)
                .setBorder(new SolidBorder(0.5f));
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
    
    /**
     * Format BigDecimal as currency string
     */
    private static String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "$0.00";
        }
        return CURRENCY_FORMAT.format(amount);
    }
}

