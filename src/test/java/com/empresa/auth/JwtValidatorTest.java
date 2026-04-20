package com.empresa.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtValidatorTest {
    @BeforeAll
    static void setSecret() {
        System.setProperty("JWT_SECRET", "segredo-teste");
    }

    @Test
    void validateToken_deveValidarTokenValido() {
        String email = "user@email.com";
        String token = JWT.create()
                .withIssuer("gestao-solicitacoes")
                .withSubject(email)
                .sign(Algorithm.HMAC256("segredo-teste"));
        DecodedJWT decoded = JwtValidator.validateToken(token);
        assertEquals(email, decoded.getSubject());
        assertEquals("gestao-solicitacoes", decoded.getIssuer());
    }

    @Test
    void validateToken_deveLancarExcecaoParaTokenInvalido() {
        String token = "token.invalido";
        assertThrows(Exception.class, () -> JwtValidator.validateToken(token));
    }
}
