package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration class for Google OAuth
 */
public class GoogleAuthConfig {
    
    private static final Properties properties = new Properties();
    
    static {
        try (InputStream input = GoogleAuthConfig.class.getClassLoader()
                .getResourceAsStream("google-oauth.properties")) {
            if (input == null) {
                System.err.println("Unable to find google-oauth.properties");
            } else {
                properties.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static String getClientId() {
        return properties.getProperty("google.client.id");
    }
    
    public static String getClientSecret() {
        return properties.getProperty("google.client.secret");
    }
    
    public static String getRedirectUri() {
        return properties.getProperty("google.redirect.uri");
    }
    
    public static String getScope() {
        return properties.getProperty("google.scope");
    }
    
    public static String getAuthUri() {
        return properties.getProperty("google.auth.uri");
    }
    
    public static String getTokenUri() {
        return properties.getProperty("google.token.uri");
    }
    
    public static String getUserInfoUri() {
        return properties.getProperty("google.userinfo.uri");
    }
}
