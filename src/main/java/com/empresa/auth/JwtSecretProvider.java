package com.empresa.auth;

public class JwtSecretProvider {
	
    private static final String SECRET;

    static {
        String prop = System.getProperty("JWT_SECRET");
        SECRET = prop != null && !prop.isBlank() ? prop : System.getenv("JWT_SECRET");
        if (SECRET == null || SECRET.isBlank()) {
            throw new IllegalStateException("JWT_SECRET não definida no ambiente!");
        }
    }

    private JwtSecretProvider() {}

    public static String getSecret() {
        return SECRET;
    }
}
