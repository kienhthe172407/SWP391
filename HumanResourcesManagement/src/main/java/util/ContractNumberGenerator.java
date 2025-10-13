package util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Utility class for generating contract numbers
 * @author admin
 */
public class ContractNumberGenerator {
    
    private static final String CONTRACT_PREFIX = "HD";
    private static final Random random = new Random();
    
    /**
     * Generate a new contract number based on current date and a random suffix
     * Format: HD-YYYY-MM-XXXX
     * Where XXXX is a random 4-digit number
     * 
     * @return String contract number
     */
    public static String generateContractNumber() {
        LocalDate now = LocalDate.now();
        String yearMonth = now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        
        // Generate random 4-digit suffix
        int suffix = 1000 + random.nextInt(9000); // Random number between 1000 and 9999
        
        return String.format("%s-%s-%04d", CONTRACT_PREFIX, yearMonth, suffix);
    }
    
    /**
     * Generate a new contract number with custom prefix
     * 
     * @param prefix Custom prefix for contract number
     * @return String contract number
     */
    public static String generateContractNumber(String prefix) {
        LocalDate now = LocalDate.now();
        String yearMonth = now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
        
        // Generate random 4-digit suffix
        int suffix = 1000 + random.nextInt(9000); // Random number between 1000 and 9999
        
        return String.format("%s-%s-%04d", prefix, yearMonth, suffix);
    }
}
