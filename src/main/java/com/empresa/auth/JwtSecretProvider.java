package com.empresa.auth;

public class JwtSecretProvider {
	
    private static final String SECRET;

    static {
        SECRET = System.getenv("JWT_SECRET");
        if (SECRET == null || SECRET.isBlank()) {
            throw new IllegalStateException("JWT_SECRET não definida no ambiente!");
        }
    }

    private JwtSecretProvider() {}

    public static String getSecret() {
        return SECRET;
    }
}
