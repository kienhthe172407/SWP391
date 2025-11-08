package util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for hashing passwords using BCrypt
 * Usage: java util.HashPassword <plain-password>
 */
public class HashPassword {
    
    /**
     * Main method to hash a password from command line
     * @param args Command line arguments, expects password as first argument
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java util.HashPassword <plain-password>");
            System.exit(1);
        }
        String plain = args[0];
        String salt = BCrypt.gensalt(10);
        String hash = BCrypt.hashpw(plain, salt);
        System.out.println("Hashed password: " + hash);
    }
    
    /**
     * Hash a plain text password
     * @param plainPassword The plain text password to hash
     * @return The hashed password
     */
    public static String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt(10);
        return BCrypt.hashpw(plainPassword, salt);
    }
    
    /**
     * Verify a plain text password against a hashed password
     * @param plainPassword The plain text password
     * @param hashedPassword The hashed password to verify against
     * @return true if the password matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}

