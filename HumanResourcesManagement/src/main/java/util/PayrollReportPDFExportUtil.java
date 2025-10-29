package util;

import model.SalarySummaryView;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.font.PdfFont;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Utility class to generate PDF payroll reports for all employees
 * Used for finance, tax, and audit purposes
 * @author admin
 */
public class PayrollReportPDFExportUtil {
    
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    
    /**
     * Generate PDF payroll report for all employees in a month
     */
    public static byte[] generatePayrollReportPDF(List<SalarySummaryView> summaryViews, 
                                                   int year, int month,
                                                   BigDecimal totalGrossSalary,
                                                   BigDecimal totalNetSalary,
                                                   BigDecimal totalBonusAdjustments) throws Exception {
        
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            // Use landscape orientation for better fit
            Document document = new Document(pdf, PageSize.A4.rotate());
            
            // Set up fonts
            PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            PdfFont normalFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            
            // Title
            Paragraph title = new Paragraph("MONTHLY PAYROLL REPORT")
                .setFont(boldFont)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(5);
            document.add(title);
            
            // Period
            Paragraph period = new Paragraph(String.format("Period: %d-%02d", year, month))
                .setFont(boldFont)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20);
            document.add(period);
            
            // Create table with 13 columns - adjusted widths for landscape
            float[] columnWidths = {2, 5, 9, 7, 7, 6, 6, 5, 5, 5, 6, 6, 6};
            Table table = new Table(UnitValue.createPercentArray(columnWidths));
            table.setWidth(UnitValue.createPercentValue(100));
            table.setFontSize(7); // Smaller font for better fit
            
            // Header row
            DeviceRgb headerColor = new DeviceRgb(0, 51, 102);
            String[] headers = {
                "No.", "Emp Code", "Employee Name", "Department", "Position",
                "Base Salary", "Allowances", "Overtime", "Bonus", "Bonus Adj.",
                "Gross Salary", "Deductions", "Net Salary"
            };
            
            for (String header : headers) {
                Cell cell = new Cell()
                    .add(new Paragraph(header).setFont(boldFont).setFontSize(8))
                    .setBackgroundColor(headerColor)
                    .setFontColor(ColorConstants.WHITE)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(5);
                table.addHeaderCell(cell);
            }
            
            // Data rows
            int no = 1;
            for (SalarySummaryView view : summaryViews) {
                // No.
                table.addCell(createCell(String.valueOf(no++), normalFont, TextAlignment.CENTER));
                
                // Employee Code
                table.addCell(createCell(view.getEmployeeCode(), normalFont, TextAlignment.LEFT));
                
                // Employee Name
                table.addCell(createCell(view.getEmployeeName(), normalFont, TextAlignment.LEFT));
                
                // Department
                table.addCell(createCell(view.getDepartmentName(), normalFont, TextAlignment.LEFT));
                
                // Position
                table.addCell(createCell(view.getPositionName(), normalFont, TextAlignment.LEFT));
                
                // Base Salary
                table.addCell(createCell(currencyFormat.format(view.getBaseSalary()), normalFont, TextAlignment.RIGHT));
                
                // Allowances
                table.addCell(createCell(currencyFormat.format(view.getTotalAllowances()), normalFont, TextAlignment.RIGHT));
                
                // Overtime
                table.addCell(createCell(currencyFormat.format(view.getOvertimePay()), normalFont, TextAlignment.RIGHT));
                
                // Bonus
                table.addCell(createCell(currencyFormat.format(view.getTotalBonus()), normalFont, TextAlignment.RIGHT));
                
                // Bonus Adjustments
                table.addCell(createCell(currencyFormat.format(view.getBonusAdjustments()), normalFont, TextAlignment.RIGHT));
                
                // Gross Salary
                table.addCell(createCell(currencyFormat.format(view.getGrossSalary()), normalFont, TextAlignment.RIGHT));
                
                // Deductions
                table.addCell(createCell(currencyFormat.format(view.getTotalDeductions()), normalFont, TextAlignment.RIGHT));
                
                // Net Salary
                table.addCell(createCell(currencyFormat.format(view.getNetSalary()), normalFont, TextAlignment.RIGHT));
            }
            
            // Total row
            DeviceRgb totalColor = new DeviceRgb(220, 220, 220);
            Cell totalLabelCell = new Cell(1, 10)
                .add(new Paragraph("TOTAL").setFont(boldFont).setFontSize(9))
                .setBackgroundColor(totalColor)
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(5)
                .setBorder(new SolidBorder(1));
            table.addCell(totalLabelCell);
            
            // Total Gross Salary
            Cell totalGrossCell = new Cell()
                .add(new Paragraph(currencyFormat.format(totalGrossSalary)).setFont(boldFont).setFontSize(9))
                .setBackgroundColor(totalColor)
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(5)
                .setBorder(new SolidBorder(1));
            table.addCell(totalGrossCell);
            
            // Total Deductions
            BigDecimal totalDeductions = summaryViews.stream()
                .map(SalarySummaryView::getTotalDeductions)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            Cell totalDeductionsCell = new Cell()
                .add(new Paragraph(currencyFormat.format(totalDeductions)).setFont(boldFont).setFontSize(9))
                .setBackgroundColor(totalColor)
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(5)
                .setBorder(new SolidBorder(1));
            table.addCell(totalDeductionsCell);
            
            // Total Net Salary
            Cell totalNetCell = new Cell()
                .add(new Paragraph(currencyFormat.format(totalNetSalary)).setFont(boldFont).setFontSize(9))
                .setBackgroundColor(totalColor)
                .setTextAlignment(TextAlignment.RIGHT)
                .setPadding(5)
                .setBorder(new SolidBorder(1));
            table.addCell(totalNetCell);
            
            document.add(table);
            
            // Summary section
            document.add(new Paragraph("\n"));
            Paragraph summaryTitle = new Paragraph("SUMMARY")
                .setFont(boldFont)
                .setFontSize(12)
                .setMarginTop(10)
                .setMarginBottom(10);
            document.add(summaryTitle);
            
            // Summary table
            Table summaryTable = new Table(2);
            summaryTable.setWidth(UnitValue.createPercentValue(50));
            
            // Total Employees
            summaryTable.addCell(createSummaryCell("Total Employees:", boldFont));
            summaryTable.addCell(createSummaryCell(String.valueOf(summaryViews.size()), normalFont));
            
            // Total Gross Salary
            summaryTable.addCell(createSummaryCell("Total Gross Salary:", boldFont));
            summaryTable.addCell(createSummaryCell(currencyFormat.format(totalGrossSalary), normalFont));
            
            // Total Net Salary
            summaryTable.addCell(createSummaryCell("Total Net Salary:", boldFont));
            summaryTable.addCell(createSummaryCell(currencyFormat.format(totalNetSalary), normalFont));
            
            // Total Bonus Adjustments
            summaryTable.addCell(createSummaryCell("Total Bonus Adjustments:", boldFont));
            summaryTable.addCell(createSummaryCell(currencyFormat.format(totalBonusAdjustments), normalFont));
            
            document.add(summaryTable);
            
            // Footer
            document.add(new Paragraph("\n"));
            Paragraph footer = new Paragraph("Generated by HR Management System")
                .setFont(normalFont)
                .setFontSize(8)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(ColorConstants.GRAY);
            document.add(footer);
            
            document.close();
            return out.toByteArray();
        }
    }
    
    private static Cell createCell(String content, PdfFont font, TextAlignment alignment) {
        return new Cell()
            .add(new Paragraph(content).setFont(font).setFontSize(7))
            .setTextAlignment(alignment)
            .setPadding(3)
            .setBorder(new SolidBorder(0.5f));
    }
    
    private static Cell createSummaryCell(String content, PdfFont font) {
        return new Cell()
            .add(new Paragraph(content).setFont(font).setFontSize(10))
            .setPadding(5)
            .setBorder(new SolidBorder(0.5f));
    }
}

