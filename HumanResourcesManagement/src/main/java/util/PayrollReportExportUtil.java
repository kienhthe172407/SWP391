package util;

import model.SalarySummaryView;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.List;

/**
 * Utility class to generate Excel payroll reports for all employees
 * Used for finance, tax, and audit purposes
 * @author admin
 */
public class PayrollReportExportUtil {
    
    /**
     * Generate Excel payroll report for all employees in a month
     */
    public static byte[] generatePayrollReportExcel(List<SalarySummaryView> summaryViews, 
                                                     int year, int month,
                                                     BigDecimal totalGrossSalary,
                                                     BigDecimal totalNetSalary,
                                                     BigDecimal totalBonusAdjustments) throws Exception {
        
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            
            Sheet sheet = workbook.createSheet("Payroll Report");
            
            // Create styles
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle titleStyle = createTitleStyle(workbook);
            CellStyle currencyStyle = createCurrencyStyle(workbook);
            CellStyle totalStyle = createTotalStyle(workbook);
            CellStyle normalStyle = createNormalStyle(workbook);
            
            int rowNum = 0;
            
            // Title
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("MONTHLY PAYROLL REPORT");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 12));
            
            // Period
            Row periodRow = sheet.createRow(rowNum++);
            Cell periodCell = periodRow.createCell(0);
            periodCell.setCellValue(String.format("Period: %d-%02d", year, month));
            periodCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 12));
            
            // Empty row
            rowNum++;
            
            // Header row
            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = {
                "No.", "Employee Code", "Employee Name", "Department", "Position",
                "Base Salary", "Allowances", "Overtime", "Bonus", "Bonus Adj.",
                "Gross Salary", "Deductions", "Net Salary"
            };
            
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Data rows
            int no = 1;
            for (SalarySummaryView view : summaryViews) {
                Row row = sheet.createRow(rowNum++);
                
                // No.
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(no++);
                cell0.setCellStyle(normalStyle);
                
                // Employee Code
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(view.getEmployeeCode());
                cell1.setCellStyle(normalStyle);
                
                // Employee Name
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(view.getEmployeeName());
                cell2.setCellStyle(normalStyle);
                
                // Department
                Cell cell3 = row.createCell(3);
                cell3.setCellValue(view.getDepartmentName());
                cell3.setCellStyle(normalStyle);
                
                // Position
                Cell cell4 = row.createCell(4);
                cell4.setCellValue(view.getPositionName());
                cell4.setCellStyle(normalStyle);
                
                // Base Salary
                Cell cell5 = row.createCell(5);
                cell5.setCellValue(view.getBaseSalary().doubleValue());
                cell5.setCellStyle(currencyStyle);
                
                // Allowances
                Cell cell6 = row.createCell(6);
                cell6.setCellValue(view.getTotalAllowances().doubleValue());
                cell6.setCellStyle(currencyStyle);
                
                // Overtime
                Cell cell7 = row.createCell(7);
                cell7.setCellValue(view.getOvertimePay().doubleValue());
                cell7.setCellStyle(currencyStyle);
                
                // Bonus
                Cell cell8 = row.createCell(8);
                cell8.setCellValue(view.getTotalBonus().doubleValue());
                cell8.setCellStyle(currencyStyle);
                
                // Bonus Adjustments
                Cell cell9 = row.createCell(9);
                cell9.setCellValue(view.getBonusAdjustments().doubleValue());
                cell9.setCellStyle(currencyStyle);
                
                // Gross Salary
                Cell cell10 = row.createCell(10);
                cell10.setCellValue(view.getGrossSalary().doubleValue());
                cell10.setCellStyle(currencyStyle);
                
                // Deductions
                Cell cell11 = row.createCell(11);
                cell11.setCellValue(view.getTotalDeductions().doubleValue());
                cell11.setCellStyle(currencyStyle);
                
                // Net Salary
                Cell cell12 = row.createCell(12);
                cell12.setCellValue(view.getNetSalary().doubleValue());
                cell12.setCellStyle(currencyStyle);
            }
            
            // Total row
            Row totalRow = sheet.createRow(rowNum++);
            Cell totalLabelCell = totalRow.createCell(0);
            totalLabelCell.setCellValue("TOTAL");
            totalLabelCell.setCellStyle(totalStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 9));
            
            Cell totalGrossCell = totalRow.createCell(10);
            totalGrossCell.setCellValue(totalGrossSalary.doubleValue());
            totalGrossCell.setCellStyle(totalStyle);
            
            Cell totalDeductionsCell = totalRow.createCell(11);
            BigDecimal totalDeductions = summaryViews.stream()
                .map(SalarySummaryView::getTotalDeductions)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            totalDeductionsCell.setCellValue(totalDeductions.doubleValue());
            totalDeductionsCell.setCellStyle(totalStyle);
            
            Cell totalNetCell = totalRow.createCell(12);
            totalNetCell.setCellValue(totalNetSalary.doubleValue());
            totalNetCell.setCellStyle(totalStyle);
            
            // Summary section
            rowNum += 2;
            Row summaryTitleRow = sheet.createRow(rowNum++);
            Cell summaryTitleCell = summaryTitleRow.createCell(0);
            summaryTitleCell.setCellValue("SUMMARY");
            summaryTitleCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum - 1, rowNum - 1, 0, 2));
            
            // Total Employees
            Row empCountRow = sheet.createRow(rowNum++);
            empCountRow.createCell(0).setCellValue("Total Employees:");
            empCountRow.createCell(1).setCellValue(summaryViews.size());
            
            // Total Gross Salary
            Row grossRow = sheet.createRow(rowNum++);
            grossRow.createCell(0).setCellValue("Total Gross Salary:");
            Cell grossCell = grossRow.createCell(1);
            grossCell.setCellValue(totalGrossSalary.doubleValue());
            grossCell.setCellStyle(currencyStyle);
            
            // Total Net Salary
            Row netRow = sheet.createRow(rowNum++);
            netRow.createCell(0).setCellValue("Total Net Salary:");
            Cell netCell = netRow.createCell(1);
            netCell.setCellValue(totalNetSalary.doubleValue());
            netCell.setCellStyle(currencyStyle);
            
            // Total Bonus Adjustments
            Row bonusAdjRow = sheet.createRow(rowNum++);
            bonusAdjRow.createCell(0).setCellValue("Total Bonus Adjustments:");
            Cell bonusAdjCell = bonusAdjRow.createCell(1);
            bonusAdjCell.setCellValue(totalBonusAdjustments.doubleValue());
            bonusAdjCell.setCellStyle(currencyStyle);
            
            // Set column widths manually for better control
            sheet.setColumnWidth(0, 1500);   // No. - narrow
            sheet.setColumnWidth(1, 3500);   // Employee Code
            sheet.setColumnWidth(2, 5500);   // Employee Name
            sheet.setColumnWidth(3, 5000);   // Department
            sheet.setColumnWidth(4, 5000);   // Position
            sheet.setColumnWidth(5, 3500);   // Base Salary
            sheet.setColumnWidth(6, 3500);   // Allowances
            sheet.setColumnWidth(7, 3000);   // Overtime
            sheet.setColumnWidth(8, 3000);   // Bonus
            sheet.setColumnWidth(9, 3500);   // Bonus Adj.
            sheet.setColumnWidth(10, 3500);  // Gross Salary
            sheet.setColumnWidth(11, 3500);  // Deductions
            sheet.setColumnWidth(12, 3500);  // Net Salary

            workbook.write(out);
            return out.toByteArray();
        }
    }
    
    private static CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
    
    private static CellStyle createTitleStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }
    
    private static CellStyle createCurrencyStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
    
    private static CellStyle createTotalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setDataFormat(workbook.createDataFormat().getFormat("$#,##0.00"));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
    
    private static CellStyle createNormalStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
}

