package com.empresa.auth;

import org.jboss.logging.Logger;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtValidator {
	
	private static final Logger LOG = Logger.getLogger(JwtValidator.class);
	
	private static final String ISSUER = "gestao-solicitacoes";


    public static DecodedJWT validateToken(String token) {
    	LOG.infof("token: %s", token);
        Algorithm algorithm = Algorithm.HMAC256(JwtSecretProvider.getSecret());
        JWTVerifier verifier = JWT.require(algorithm)
            .withIssuer(ISSUER)
            .build();
        return verifier.verify(token);
    }
}
