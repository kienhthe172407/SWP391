package util;

import model.SalaryCalculationDetail;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for exporting payslip data to Excel format
 * Generates formatted Excel workbooks with detailed salary breakdown
 */
public class PayslipExportUtil {
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Generate Excel payslip for a single employee
     * @param calc Salary calculation detail
     * @param year Year of payroll
     * @param month Month of payroll
     * @return byte array of Excel file
     * @throws IOException if error occurs during generation
     */
    public static byte[] generatePayslipExcel(SalaryCalculationDetail calc, int year, int month) throws IOException {
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Payslip");
            
            // Create styles
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle labelStyle = createLabelStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            CellStyle totalStyle = createTotalStyle(workbook);
            
            int rowNum = 0;
            
            // Title
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("PAYSLIP");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 3));
            
            // Period
            Row periodRow = sheet.createRow(rowNum++);
            Cell periodCell = periodRow.createCell(0);
            periodCell.setCellValue(String.format("Period: %s-%02d", year, month));
            periodCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 3));
            
            rowNum++; // Empty row
            
            // Employee Information
            Row empHeaderRow = sheet.createRow(rowNum++);
            Cell empHeaderCell = empHeaderRow.createCell(0);
            empHeaderCell.setCellValue("EMPLOYEE INFORMATION");
            empHeaderCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 3));
            
            addInfoRow(sheet, rowNum++, "Employee Code:", calc.getEmployeeCode(), labelStyle, dataStyle);
            addInfoRow(sheet, rowNum++, "Employee Name:", calc.getEmployeeName(), labelStyle, dataStyle);
            addInfoRow(sheet, rowNum++, "Department:", calc.getDepartmentName(), labelStyle, dataStyle);
            addInfoRow(sheet, rowNum++, "Position:", calc.getPositionName(), labelStyle, dataStyle);
            
            rowNum++; // Empty row
            
            // Base Salary & Allowances
            Row salaryHeaderRow = sheet.createRow(rowNum++);
            Cell salaryHeaderCell = salaryHeaderRow.createCell(0);
            salaryHeaderCell.setCellValue("BASE SALARY & ALLOWANCES");
            salaryHeaderCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 3));
            
            addCurrencyRow(sheet, rowNum++, "Base Salary:", calc.getBaseSalary(), labelStyle, currencyStyle);
            addCurrencyRow(sheet, rowNum++, "Position Allowance:", calc.getPositionAllowance(), labelStyle, currencyStyle);
            addCurrencyRow(sheet, rowNum++, "Housing Allowance:", calc.getHousingAllowance(), labelStyle, currencyStyle);
            addCurrencyRow(sheet, rowNum++, "Transportation Allowance:", calc.getTransportationAllowance(), labelStyle, currencyStyle);
            addCurrencyRow(sheet, rowNum++, "Meal Allowance:", calc.getMealAllowance(), labelStyle, currencyStyle);
            addCurrencyRow(sheet, rowNum++, "Other Allowances:", calc.getOtherAllowances(), labelStyle, currencyStyle);
            addCurrencyRow(sheet, rowNum++, "Total Allowances:", calc.getTotalAllowances(), labelStyle, totalStyle);
            
            rowNum++; // Empty row
            
            // Attendance & Overtime
            Row attendanceHeaderRow = sheet.createRow(rowNum++);
            Cell attendanceHeaderCell = attendanceHeaderRow.createCell(0);
            attendanceHeaderCell.setCellValue("ATTENDANCE & OVERTIME");
            attendanceHeaderCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 3));
            
            addInfoRow(sheet, rowNum++, "Working Days:", String.valueOf(calc.getWorkingDays()), labelStyle, dataStyle);
            addInfoRow(sheet, rowNum++, "Absent Days:", String.valueOf(calc.getAbsentDays()), labelStyle, dataStyle);
            addInfoRow(sheet, rowNum++, "Late Days:", String.valueOf(calc.getLateDays()), labelStyle, dataStyle);
            addInfoRow(sheet, rowNum++, "Overtime Hours:", String.format("%.2f", calc.getOvertimeHours()), labelStyle, dataStyle);
            addCurrencyRow(sheet, rowNum++, "Overtime Pay (1.5x):", calc.getOvertimePay(), labelStyle, currencyStyle);
            
            rowNum++; // Empty row
            
            // Benefits
            if (calc.getBenefits() != null && !calc.getBenefits().isEmpty()) {
                Row benefitsHeaderRow = sheet.createRow(rowNum++);
                Cell benefitsHeaderCell = benefitsHeaderRow.createCell(0);
                benefitsHeaderCell.setCellValue("BENEFITS");
                benefitsHeaderCell.setCellStyle(headerStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 3));
                
                for (SalaryCalculationDetail.BenefitDetail benefit : calc.getBenefits()) {
                    String label = benefit.getBenefitName() + " (" + benefit.getCalculationType() + "):";
                    addCurrencyRow(sheet, rowNum++, label, benefit.getAmount(), labelStyle, currencyStyle);
                }
                addCurrencyRow(sheet, rowNum++, "Total Benefits:", calc.getTotalBenefits(), labelStyle, totalStyle);
                rowNum++; // Empty row
            }
            
            // Gross Salary
            Row grossHeaderRow = sheet.createRow(rowNum++);
            Cell grossHeaderCell = grossHeaderRow.createCell(0);
            grossHeaderCell.setCellValue("GROSS SALARY");
            grossHeaderCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 3));
            
            addCurrencyRow(sheet, rowNum++, "Gross Salary:", calc.getGrossSalary(), labelStyle, totalStyle);
            
            rowNum++; // Empty row
            
            // Deductions
            if (calc.getDeductions() != null && !calc.getDeductions().isEmpty()) {
                Row deductionsHeaderRow = sheet.createRow(rowNum++);
                Cell deductionsHeaderCell = deductionsHeaderRow.createCell(0);
                deductionsHeaderCell.setCellValue("DEDUCTIONS");
                deductionsHeaderCell.setCellStyle(headerStyle);
                sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 3));
                
                for (SalaryCalculationDetail.DeductionDetail deduction : calc.getDeductions()) {
                    String label = deduction.getDeductionName() + " (" + deduction.getCalculationType() + "):";
                    addCurrencyRow(sheet, rowNum++, label, deduction.getAmount(), labelStyle, currencyStyle);
                }
                addCurrencyRow(sheet, rowNum++, "Total Deductions:", calc.getTotalDeductions(), labelStyle, totalStyle);
                rowNum++; // Empty row
            }
            
            // Net Salary
            Row netHeaderRow = sheet.createRow(rowNum++);
            Cell netHeaderCell = netHeaderRow.createCell(0);
            netHeaderCell.setCellValue("NET SALARY (TAKE HOME)");
            netHeaderCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 3));
            
            addCurrencyRow(sheet, rowNum++, "Net Salary:", calc.getNetSalary(), labelStyle, totalStyle);
            
            rowNum += 2; // Empty rows
            
            // Footer
            Row footerRow = sheet.createRow(rowNum++);
            Cell footerCell = footerRow.createCell(0);
            footerCell.setCellValue("Generated on: " + DATETIME_FORMAT.format(new Date()));
            footerCell.setCellStyle(dataStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 3));
            
            // Auto-size columns
            for (int i = 0; i < 4; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Write to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
    
    /**
     * Helper method to add an info row (label + value)
     */
    private static void addInfoRow(Sheet sheet, int rowNum, String label, String value, 
                                   CellStyle labelStyle, CellStyle dataStyle) {
        Row row = sheet.createRow(rowNum);
        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(labelStyle);
        
        Cell valueCell = row.createCell(1);
        valueCell.setCellValue(value != null ? value : "");
        valueCell.setCellStyle(dataStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 3));
    }
    
    /**
     * Helper method to add a currency row (label + amount)
     */
    private static void addCurrencyRow(Sheet sheet, int rowNum, String label, BigDecimal amount, 
                                      CellStyle labelStyle, CellStyle currencyStyle) {
        Row row = sheet.createRow(rowNum);
        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(labelStyle);
        
        Cell valueCell = row.createCell(1);
        valueCell.setCellValue(amount != null ? amount.doubleValue() : 0.0);
        valueCell.setCellStyle(currencyStyle);
        sheet.addMergedRegion(new CellRangeAddress(rowNum, rowNum, 1, 3));
    }
    
    /**
     * Create title cell style
     */
    private static CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 18);
        font.setColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
    
    /**
     * Create header cell style
     */
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }
    
    /**
     * Create label cell style
     */
    private static CellStyle createLabelStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
    
    /**
     * Create data cell style
     */
    private static CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
    
    /**
     * Create currency cell style
     */
    private static CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00"));
        return style;
    }
    
    /**
     * Create total cell style (bold + currency)
     */
    private static CellStyle createTotalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00"));
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
}

