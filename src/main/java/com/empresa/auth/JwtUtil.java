package com.empresa.auth;

import java.sql.Date;
import java.util.Set;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JwtUtil {

    private static final long EXPIRATION = 3600_000; // 1 hora em ms

    public static String generateToken(String email, Set<String> roles) {
        Algorithm algorithm = Algorithm.HMAC256(JwtSecretProvider.getSecret());
        return JWT.create()
                .withIssuer("gestao-solicitacoes")
                .withSubject(email)
                .withClaim("roles", String.join(",", roles))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION))
                .sign(algorithm);
    }

    public static String generateToken(String email) {
        return generateToken(email, Set.of());
    }
}
