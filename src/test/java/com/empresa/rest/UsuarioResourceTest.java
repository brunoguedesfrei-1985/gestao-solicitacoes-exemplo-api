package com.empresa.rest;

import com.empresa.dto.UsuarioDTO;
import com.empresa.service.UsuarioService;
import com.empresa.validator.UsuarioValidator;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioResourceTest {
    @Mock
    UsuarioValidator usuarioValidator;
    @Mock
    UsuarioService usuarioService;
    @InjectMocks
    UsuarioResource usuarioResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodos_deveRetornarLista() {
        UsuarioDTO u1 = new UsuarioDTO(1L, "Fulano", "a@b.com", "12345678901", null);
        UsuarioDTO u2 = new UsuarioDTO(2L, "Beltrano", "b@b.com", "98765432100", null);
        when(usuarioService.listarTodos()).thenReturn(Arrays.asList(u1, u2));
        List<UsuarioDTO> result = usuarioResource.listarTodos();
        assertEquals(2, result.size());
        assertEquals("Fulano", result.get(0).nome);
    }

    @Test
    void buscarPorId_deveRetornarUsuario() {
        UsuarioDTO u = new UsuarioDTO(1L, "Fulano", "a@b.com", "12345678901", null);
        when(usuarioService.buscarPorId(1L)).thenReturn(u);
        Response resp = usuarioResource.buscarPorId(1L);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertEquals(u, resp.getEntity());
    }

    @Test
    void buscarPorId_deveRetornarNotFound() {
        when(usuarioService.buscarPorId(1L)).thenReturn(null);
        Response resp = usuarioResource.buscarPorId(1L);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    }

    @Test
    void deletar_deveRetornarNoContentSeRemovido() {
        doNothing().when(usuarioValidator).validarParaDeletar(1L);
        when(usuarioService.deletar(1L)).thenReturn(true);
        Response resp = usuarioResource.deletar(1L);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), resp.getStatus());
    }

    @Test
    void deletar_deveRetornarNotFoundSeNaoRemovido() {
        doNothing().when(usuarioValidator).validarParaDeletar(1L);
        when(usuarioService.deletar(1L)).thenReturn(false);
        Response resp = usuarioResource.deletar(1L);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    }

    @Test
    void deletar_deveRetornarBadRequestParaIdInvalido() {
        doThrow(new IllegalArgumentException("erro")).when(usuarioValidator).validarParaDeletar(1L);
        Response resp = usuarioResource.deletar(1L);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
        assertTrue(((java.util.Map<?,?>)resp.getEntity()).get("erro").toString().contains("erro"));
    }
}
