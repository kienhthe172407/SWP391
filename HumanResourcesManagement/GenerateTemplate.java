import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Standalone program to generate Excel template for attendance import
 * Compile: javac -cp "target\classes;target\dependency\*" GenerateTemplate.java
 * Run: java -cp "target\classes;target\dependency\*;." GenerateTemplate
 */
public class GenerateTemplate {
    
    public static void main(String[] args) {
        try {
            // Create a new workbook
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Attendance");
            
            // Define headers
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
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Sample data
            String[][] sampleData = {
                {"EMP001", "2024-10-16", "08:00:00", "17:00:00", "Present", "0"},
                {"EMP002", "2024-10-16", "08:15:00", "17:30:00", "Late", "0.5"},
                {"EMP003", "2024-10-16", "08:00:00", "18:00:00", "Present", "1"},
                {"EMP004", "2024-10-16", "", "", "Absent", "0"},
                {"EMP005", "2024-10-16", "08:00:00", "16:00:00", "Early Leave", "0"},
            };
            
            // Add sample data
            for (int rowNum = 0; rowNum < sampleData.length; rowNum++) {
                Row row = sheet.createRow(rowNum + 1);
                for (int colNum = 0; colNum < sampleData[rowNum].length; colNum++) {
                    Cell cell = row.createCell(colNum);
                    cell.setCellValue(sampleData[rowNum][colNum]);
                }
            }
            
            // Adjust column widths
            sheet.setColumnWidth(0, 15 * 256);
            sheet.setColumnWidth(1, 18 * 256);
            sheet.setColumnWidth(2, 15 * 256);
            sheet.setColumnWidth(3, 15 * 256);
            sheet.setColumnWidth(4, 15 * 256);
            sheet.setColumnWidth(5, 15 * 256);
            
            // Freeze header row
            sheet.createFreezePane(0, 1);
            
            // Save the file
            String outputPath = "src/main/webapp/templates/attendance-template.xlsx";
            try (FileOutputStream fos = new FileOutputStream(outputPath)) {
                workbook.write(fos);
            }
            workbook.close();
            
            System.out.println("[SUCCESS] Template generated successfully!");
            System.out.println("[INFO] Location: " + outputPath);
            System.out.println("[INFO] Sample data: 5 employees");
            System.out.println("[INFO] Headers: Employee Code, Attendance Date, Check-in Time, Check-out Time, Status, Overtime Hours");

        } catch (IOException e) {
            System.err.println("[ERROR] Error generating template: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

