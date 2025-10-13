package tools;

import org.mindrot.jbcrypt.BCrypt;

public class HashPassword {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java tools.HashPassword <plain-password>");
            System.exit(1);
        }
        String plain = args[0];
        String salt = BCrypt.gensalt(10);
        String hash = BCrypt.hashpw(plain, salt);
        System.out.println(hash);
    }
}
