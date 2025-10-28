package util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Utility class to generate Excel template for salary import
 * @author admin
 */
public class SalaryTemplateGenerator {

    /**
     * Generate Excel template for salary import with 50 rows of sample data
     * @return byte array of Excel file
     * @throws IOException if generation fails
     */
    public static byte[] generateTemplate() throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Salary Data");

            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            // Create data style
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.LEFT);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);

            // Create number style for currency
            CellStyle currencyStyle = workbook.createCellStyle();
            currencyStyle.cloneStyleFrom(dataStyle);
            DataFormat format = workbook.createDataFormat();
            currencyStyle.setDataFormat(format.getFormat("#,##0.00"));

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "Employee Code",
                "Base Salary",
                "Position Allowance",
                "Housing Allowance",
                "Transportation Allowance",
                "Meal Allowance",
                "Other Allowances",
                "Effective From (yyyy-MM-dd)"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Generate 50 rows of sample data
            Random random = new Random(12345); // Fixed seed for consistent data

            // Different salary ranges based on position level
            double[][] salaryRanges = {
                {3000, 4000},   // Junior level
                {4500, 6000},   // Mid level
                {7000, 9000},   // Senior level
                {10000, 15000}, // Manager level
                {16000, 25000}  // Executive level
            };

            String[] dates = {
                "2025-01-01", "2025-02-01", "2025-03-01", "2025-04-01",
                "2025-05-01", "2025-06-01", "2025-07-01"
            };

            for (int i = 1; i <= 50; i++) {
                Row row = sheet.createRow(i);

                // Employee Code
                Cell empCodeCell = row.createCell(0);
                empCodeCell.setCellValue(String.format("EMP%03d", i));
                empCodeCell.setCellStyle(dataStyle);

                // Determine salary level (cycle through levels)
                int levelIndex = (i - 1) % salaryRanges.length;
                double[] range = salaryRanges[levelIndex];

                // Base Salary (random within range)
                double baseSalary = range[0] + (range[1] - range[0]) * random.nextDouble();
                baseSalary = Math.round(baseSalary * 100.0) / 100.0; // Round to 2 decimals
                Cell baseSalaryCell = row.createCell(1);
                baseSalaryCell.setCellValue(baseSalary);
                baseSalaryCell.setCellStyle(currencyStyle);

                // Position Allowance (10-20% of base salary)
                double positionAllowance = baseSalary * (0.10 + 0.10 * random.nextDouble());
                positionAllowance = Math.round(positionAllowance * 100.0) / 100.0;
                Cell posAllowCell = row.createCell(2);
                posAllowCell.setCellValue(positionAllowance);
                posAllowCell.setCellStyle(currencyStyle);

                // Housing Allowance (varies by level)
                double housingAllowance = 300 + (levelIndex * 100) + (random.nextDouble() * 200);
                housingAllowance = Math.round(housingAllowance * 100.0) / 100.0;
                Cell housingCell = row.createCell(3);
                housingCell.setCellValue(housingAllowance);
                housingCell.setCellStyle(currencyStyle);

                // Transportation Allowance (fixed ranges)
                double transportAllowance = 150 + (random.nextDouble() * 100);
                transportAllowance = Math.round(transportAllowance * 100.0) / 100.0;
                Cell transportCell = row.createCell(4);
                transportCell.setCellValue(transportAllowance);
                transportCell.setCellStyle(currencyStyle);

                // Meal Allowance (fixed ranges)
                double mealAllowance = 100 + (random.nextDouble() * 100);
                mealAllowance = Math.round(mealAllowance * 100.0) / 100.0;
                Cell mealCell = row.createCell(5);
                mealCell.setCellValue(mealAllowance);
                mealCell.setCellStyle(currencyStyle);

                // Other Allowances (some have it, some don't)
                double otherAllowance = (i % 3 == 0) ? (random.nextDouble() * 200) : 0;
                otherAllowance = Math.round(otherAllowance * 100.0) / 100.0;
                Cell otherCell = row.createCell(6);
                otherCell.setCellValue(otherAllowance);
                otherCell.setCellStyle(currencyStyle);

                // Effective From (cycle through dates)
                Cell dateCell = row.createCell(7);
                dateCell.setCellValue(dates[(i - 1) % dates.length]);
                dateCell.setCellStyle(dataStyle);
            }

            // Set column widths
            sheet.setColumnWidth(0, 15 * 256);  // Employee Code
            sheet.setColumnWidth(1, 18 * 256);  // Base Salary
            sheet.setColumnWidth(2, 20 * 256);  // Position Allowance
            sheet.setColumnWidth(3, 20 * 256);  // Housing Allowance
            sheet.setColumnWidth(4, 25 * 256);  // Transportation Allowance
            sheet.setColumnWidth(5, 18 * 256);  // Meal Allowance
            sheet.setColumnWidth(6, 20 * 256);  // Other Allowances
            sheet.setColumnWidth(7, 28 * 256);  // Effective From

            // Freeze header row
            sheet.createFreezePane(0, 1);

            // Write to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
}

