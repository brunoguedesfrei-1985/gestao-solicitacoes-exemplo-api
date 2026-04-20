
package com.empresa.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

class JwtUtilTest {

     @BeforeAll
    static void setSecret() {
        // Garante que a variável de ambiente está definida para o teste
        System.setProperty("JWT_SECRET", "segredo-teste");
    }

    @Test
    void generateToken_deveGerarTokenComEmailERoles() {
        String email = "user@email.com";
        Set<String> roles = Set.of("ADMIN", "USER");
        // Mock do segredo
        String token = JwtUtil.generateToken(email, roles);
        assertNotNull(token);
        // Decodifica para validar claims
        var verifier = JWT.require(Algorithm.HMAC256("segredo-teste"))
            .withIssuer("gestao-solicitacoes").build();
        var decoded = verifier.verify(token);
        assertEquals(email, decoded.getSubject());
        Set<String> expected = Set.of("ADMIN", "USER");
        Set<String> actual = Set.of(decoded.getClaim("roles").asString().split(","));
        assertEquals(expected, actual);
        assertEquals("gestao-solicitacoes", decoded.getIssuer());
        assertTrue(decoded.getExpiresAt().getTime() > System.currentTimeMillis());
    }

    @Test
    void generateToken_deveGerarTokenSemRoles() {
        String email = "user@email.com";
        String token = JwtUtil.generateToken(email);
        assertNotNull(token);
        var verifier = JWT.require(Algorithm.HMAC256("segredo-teste"))
            .withIssuer("gestao-solicitacoes").build();
        var decoded = verifier.verify(token);
        assertEquals(email, decoded.getSubject());
        assertEquals("", decoded.getClaim("roles").asString());
    }
}
