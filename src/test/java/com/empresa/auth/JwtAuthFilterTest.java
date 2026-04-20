package com.empresa.auth;

// ...existing code...
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
// ...existing code...
// ...existing code...
// ...existing code...
import static org.mockito.Mockito.*;

class JwtAuthFilterTest {
    @BeforeAll
    static void setSecret() {
        System.setProperty("JWT_SECRET", "segredo-teste");
    }

    @Test
    void devePermitirAcessoPublico() {
        JwtAuthFilter filter = new JwtAuthFilter();
        ContainerRequestContext ctx = mock(ContainerRequestContext.class);
        when(ctx.getUriInfo()).thenReturn(mock(jakarta.ws.rs.core.UriInfo.class));
        when(ctx.getUriInfo().getPath()).thenReturn("/public/qualquer");
        filter.filter(ctx);
        verify(ctx, never()).abortWith(any());
    }
    
    @Test
    void devePermitirAcessoPublicoHealth() {
    	JwtAuthFilter filter = new JwtAuthFilter();
    	ContainerRequestContext ctx = mock(ContainerRequestContext.class);
    	when(ctx.getUriInfo()).thenReturn(mock(jakarta.ws.rs.core.UriInfo.class));
    	when(ctx.getUriInfo().getPath()).thenReturn("/health/qualquer");
    	filter.filter(ctx);
    	verify(ctx, never()).abortWith(any());
    }

    @Test
    void deveAbortarSemHeaderAuthorization() {
        JwtAuthFilter filter = new JwtAuthFilter();
        ContainerRequestContext ctx = mock(ContainerRequestContext.class);
        when(ctx.getUriInfo()).thenReturn(mock(jakarta.ws.rs.core.UriInfo.class));
        when(ctx.getUriInfo().getPath()).thenReturn("/api/privado");
        when(ctx.getHeaderString("Authorization")).thenReturn(null);
        filter.filter(ctx);
        verify(ctx).abortWith(argThat(resp -> resp.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()));
    }

    @Test
    void deveAbortarComHeaderMalFormatado() {
        JwtAuthFilter filter = new JwtAuthFilter();
        ContainerRequestContext ctx = mock(ContainerRequestContext.class);
        when(ctx.getUriInfo()).thenReturn(mock(jakarta.ws.rs.core.UriInfo.class));
        when(ctx.getUriInfo().getPath()).thenReturn("/api/privado");
        when(ctx.getHeaderString("Authorization")).thenReturn("Token xyz");
        filter.filter(ctx);
        verify(ctx).abortWith(argThat(resp -> resp.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()));
    }

    @Test
    void deveSetarSecurityContextComTokenValido() {
        JwtAuthFilter filter = new JwtAuthFilter();
        ContainerRequestContext ctx = mock(ContainerRequestContext.class);
        SecurityContext original = mock(SecurityContext.class);
        when(ctx.getUriInfo()).thenReturn(mock(jakarta.ws.rs.core.UriInfo.class));
        when(ctx.getUriInfo().getPath()).thenReturn("/api/privado");
        String email = "user@email.com";
        String token = JwtUtil.generateToken(email);
        when(ctx.getHeaderString("Authorization")).thenReturn("Bearer " + token);
        when(ctx.getSecurityContext()).thenReturn(original);
        filter.filter(ctx);
        verify(ctx).setSecurityContext(any(SecurityContext.class));
    }

    @Test
    void deveAbortarComTokenInvalido() {
        JwtAuthFilter filter = new JwtAuthFilter();
        ContainerRequestContext ctx = mock(ContainerRequestContext.class);
        when(ctx.getUriInfo()).thenReturn(mock(jakarta.ws.rs.core.UriInfo.class));
        when(ctx.getUriInfo().getPath()).thenReturn("/api/privado");
        when(ctx.getHeaderString("Authorization")).thenReturn("Bearer token.invalido");
        filter.filter(ctx);
        verify(ctx).abortWith(argThat(resp -> resp.getStatus() == Response.Status.UNAUTHORIZED.getStatusCode()));
    }
}
