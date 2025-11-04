package util;

import java.security.SecureRandom;

public class PasswordGenerator {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*";
    private static final String ALL = UPPERCASE + LOWERCASE + DIGITS + SPECIAL;
    private static final SecureRandom random = new SecureRandom();
    
    public static String generate() {
        StringBuilder password = new StringBuilder(12);
        
        // Đảm bảo có ít nhất 1 ký tự mỗi loại
        password.append(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        password.append(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        password.append(DIGITS.charAt(random.nextInt(DIGITS.length())));
        password.append(SPECIAL.charAt(random.nextInt(SPECIAL.length())));
        
        // Thêm 8 ký tự ngẫu nhiên nữa
        for (int i = 4; i < 12; i++) {
            password.append(ALL.charAt(random.nextInt(ALL.length())));
        }
        
        // Shuffle
        return shuffle(password.toString());
    }
    
    private static String shuffle(String input) {
        char[] chars = input.toCharArray();
        for (int i = chars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }
        return new String(chars);
    }
}
