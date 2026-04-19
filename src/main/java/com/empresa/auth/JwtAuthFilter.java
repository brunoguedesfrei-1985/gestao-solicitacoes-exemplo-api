package com.empresa.auth;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.security.Principal;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.annotation.Priority;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext) {
        String path = requestContext.getUriInfo().getPath();
        // Permitir acesso público a qualquer endpoint que comece com /public
        if (path.startsWith("/public")) {
            return;
        }
        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Token ausente ou mal formatado").build());
            return;
        }
        try {
            DecodedJWT jwt = JwtValidator.validateToken(authHeader.substring(7));
            final String email = jwt.getSubject();
            final SecurityContext originalContext = requestContext.getSecurityContext();
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> email;
                }
                @Override
                public boolean isUserInRole(String role) {
                    // Implemente se necessário, por enquanto retorna false
                    return false;
                }
                @Override
                public boolean isSecure() {
                    return originalContext != null && originalContext.isSecure();
                }
                @Override
                public String getAuthenticationScheme() {
                    return "Bearer";
                }
            });
        } catch (JWTVerificationException | IllegalArgumentException e) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Token inválido ou ausente").build());
        }
    }
}
