package util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Utility class to generate Excel template for attendance import
 * Run this to create the template file
 */
public class ExcelTemplateGenerator {
    
    public static void generateTemplate(String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Attendance");
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "Employee Code",
                "Attendance Date",
                "Check-in Time",
                "Check-out Time",
                "Status",
                "Overtime Hours"
            };
            
            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            
            // Add headers
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create data style
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setAlignment(HorizontalAlignment.LEFT);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);

            // Generate 50 rows of sample data
            String[] statuses = {"Present", "Late", "Absent", "Early Leave", "Business Trip", "Remote"};
            String[] dates = {"2024-10-14", "2024-10-15", "2024-10-16", "2024-10-17", "2024-10-18", "2024-10-21", "2024-10-22"};

            for (int i = 1; i <= 50; i++) {
                Row row = sheet.createRow(i);

                // Employee Code
                Cell empCodeCell = row.createCell(0);
                empCodeCell.setCellValue(String.format("EMP%03d", i));
                empCodeCell.setCellStyle(dataStyle);

                // Attendance Date (cycle through dates)
                Cell dateCell = row.createCell(1);
                dateCell.setCellValue(dates[(i - 1) % dates.length]);
                dateCell.setCellStyle(dataStyle);

                // Status (cycle through statuses)
                int statusIndex = (i - 1) % statuses.length;
                String status = statuses[statusIndex];

                // Check-in Time, Check-out Time, Status, Overtime based on status
                Cell checkInCell = row.createCell(2);
                Cell checkOutCell = row.createCell(3);
                Cell statusCell = row.createCell(4);
                Cell overtimeCell = row.createCell(5);

                switch (status) {
                    case "Present":
                        checkInCell.setCellValue("08:00:00");
                        checkOutCell.setCellValue("17:00:00");
                        statusCell.setCellValue("Present");
                        overtimeCell.setCellValue("0");
                        break;
                    case "Late":
                        checkInCell.setCellValue(String.format("08:%02d:00", 10 + (i % 20)));
                        checkOutCell.setCellValue("17:30:00");
                        statusCell.setCellValue("Late");
                        overtimeCell.setCellValue("0.5");
                        break;
                    case "Absent":
                        checkInCell.setCellValue("");
                        checkOutCell.setCellValue("");
                        statusCell.setCellValue("Absent");
                        overtimeCell.setCellValue("0");
                        break;
                    case "Early Leave":
                        checkInCell.setCellValue("08:00:00");
                        checkOutCell.setCellValue("16:00:00");
                        statusCell.setCellValue("Early Leave");
                        overtimeCell.setCellValue("0");
                        break;
                    case "Business Trip":
                        checkInCell.setCellValue("08:00:00");
                        checkOutCell.setCellValue("17:00:00");
                        statusCell.setCellValue("Business Trip");
                        overtimeCell.setCellValue("0");
                        break;
                    case "Remote":
                        checkInCell.setCellValue("08:00:00");
                        checkOutCell.setCellValue("18:00:00");
                        statusCell.setCellValue("Remote");
                        overtimeCell.setCellValue("1");
                        break;
                }

                checkInCell.setCellStyle(dataStyle);
                checkOutCell.setCellStyle(dataStyle);
                statusCell.setCellStyle(dataStyle);
                overtimeCell.setCellStyle(dataStyle);
            }
            
            // Set column widths
            sheet.setColumnWidth(0, 15 * 256);  // Employee Code
            sheet.setColumnWidth(1, 18 * 256);  // Attendance Date
            sheet.setColumnWidth(2, 15 * 256);  // Check-in Time
            sheet.setColumnWidth(3, 15 * 256);  // Check-out Time
            sheet.setColumnWidth(4, 15 * 256);  // Status
            sheet.setColumnWidth(5, 15 * 256);  // Overtime Hours
            
            // Freeze header row
            sheet.createFreezePane(0, 1);
            
            // Write to file
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
            
            System.out.println("Template generated successfully at: " + filePath);
        }
    }
    
    public static void main(String[] args) {
        try {
            String templatePath = "src/main/webapp/templates/attendance-template.xlsx";
            generateTemplate(templatePath);
        } catch (IOException e) {
            System.err.println("Error generating template: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

