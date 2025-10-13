package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Utility class for generating employee codes
 * @author admin
 */
public class EmployeeCodeGenerator {
    
    private static final String EMPLOYEE_PREFIX = "EMP";
    private static final Random random = new Random();
    
    /**
     * Generate a new employee code based on current date and a random suffix
     * Format: EMP-YYYY-XXXX
     * Where XXXX is a random 4-digit number
     * 
     * @return String employee code
     */
    public static String generateEmployeeCode() {
        LocalDate now = LocalDate.now();
        String year = now.format(DateTimeFormatter.ofPattern("yyyy"));
        
        // Generate random 4-digit suffix
        int suffix = 1000 + random.nextInt(9000); // Random number between 1000 and 9999
        
        return String.format("%s-%s-%04d", EMPLOYEE_PREFIX, year, suffix);
    }
    
    /**
     * Generate a new employee code with custom prefix
     * 
     * @param prefix Custom prefix for employee code
     * @return String employee code
     */
    public static String generateEmployeeCode(String prefix) {
        LocalDate now = LocalDate.now();
        String year = now.format(DateTimeFormatter.ofPattern("yyyy"));
        
        // Generate random 4-digit suffix
        int suffix = 1000 + random.nextInt(9000); // Random number between 1000 and 9999
        
        return String.format("%s-%s-%04d", prefix, year, suffix);
    }
    
    /**
     * Generate a sequential employee code based on department
     * Format: DEPT-YYYY-XXXX
     * 
     * @param departmentCode Department code (e.g., "HR", "IT", "FIN")
     * @return String employee code
     */
    public static String generateEmployeeCodeByDepartment(String departmentCode) {
        LocalDate now = LocalDate.now();
        String year = now.format(DateTimeFormatter.ofPattern("yyyy"));
        
        // Generate random 4-digit suffix
        int suffix = 1000 + random.nextInt(9000);
        
        return String.format("%s-%s-%04d", departmentCode.toUpperCase(), year, suffix);
    }
    
    /**
     * Validate employee code format
     * 
     * @param employeeCode Employee code to validate
     * @return boolean true if valid format
     */
    public static boolean isValidEmployeeCodeFormat(String employeeCode) {
        if (employeeCode == null || employeeCode.trim().isEmpty()) {
            return false;
        }
        
        // Check if it matches the pattern: PREFIX-YYYY-XXXX
        String pattern = "^[A-Z]{2,5}-\\d{4}-\\d{4}$";
        return employeeCode.matches(pattern);
    }
}
