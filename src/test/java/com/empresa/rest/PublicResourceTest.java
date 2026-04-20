package com.empresa.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.empresa.domain.LoginRequest;
import com.empresa.domain.UsuarioRequest;
import com.empresa.dto.CargoDTO;
import com.empresa.dto.ResponseLoginDTO;
import com.empresa.dto.UsuarioDTO;
import com.empresa.service.CargoService;
import com.empresa.service.UsuarioService;
import com.empresa.validator.UsuarioValidator;

import jakarta.ws.rs.core.Response;

class PublicResourceTest {
    @Mock
    UsuarioValidator usuarioValidator;
    @Mock
    UsuarioService usuarioService;
    @Mock
    CargoService cargoService;
    @InjectMocks
    PublicResource publicResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarCargosPublico_deveRetornarLista() {
        var cargo1 = mock(com.empresa.model.Cargo.class);
        cargo1.id = 1L; cargo1.nome = "A";
        var cargo2 = mock(com.empresa.model.Cargo.class);
        cargo2.id = 2L; cargo2.nome = "B";
        when(cargoService.listarTodos()).thenReturn(Arrays.asList(cargo1, cargo2));
        List<CargoDTO> result = publicResource.listarCargosPublico();
        assertEquals(2, result.size());
        assertEquals("A", result.get(0).nome);
    }

    @Test
    void criarUsuario_deveRetornarCreatedParaUsuarioValido() {
        UsuarioRequest req = new UsuarioRequest();
        req.email = "a@b.com";
        UsuarioDTO dto = new UsuarioDTO(1L, "Fulano", "a@b.com", "12345678901", null);
        doNothing().when(usuarioValidator).validarParaCriar(req);
        when(usuarioService.criar(req)).thenReturn(dto);
        Response resp = publicResource.criarUsuario(req);
        assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
        assertEquals(dto, resp.getEntity());
    }

    @Test
    void criarUsuario_deveRetornarBadRequestParaUsuarioInvalido() {
        UsuarioRequest req = new UsuarioRequest();
        req.email = "a@b.com";
        doThrow(new IllegalArgumentException("erro")).when(usuarioValidator).validarParaCriar(req);
        Response resp = publicResource.criarUsuario(req);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
        assertTrue(((java.util.Map<?,?>)resp.getEntity()).get("erro").toString().contains("erro"));
    }

    @Test
    void criarUsuario_deveRetornarConflictSeServiceLancar() {
        UsuarioRequest req = new UsuarioRequest();
        req.email = "a@b.com";
        doNothing().when(usuarioValidator).validarParaCriar(req);
        when(usuarioService.criar(req)).thenThrow(new IllegalArgumentException("conflito"));
        Response resp = publicResource.criarUsuario(req);
        assertEquals(Response.Status.CONFLICT.getStatusCode(), resp.getStatus());
        assertTrue(resp.getEntity().toString().contains("conflito"));
    }

    @Test
    void login_deveRetornarTokenParaUsuarioValido() {
        LoginRequest login = new LoginRequest();
        login.setEmail("a@b.com");
        login.setSenha("senha");
        UsuarioDTO usuario = new UsuarioDTO(1L, "Fulano", "a@b.com", "12345678901", null);
        when(usuarioService.autenticar("a@b.com", "senha")).thenReturn(usuario);
        try (var jwtUtilMock = mockStatic(com.empresa.auth.JwtUtil.class)) {
            jwtUtilMock.when(() -> com.empresa.auth.JwtUtil.generateToken("a@b.com")).thenReturn("token123");
            Response resp = publicResource.login(login);
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals("Bearer token123", resp.getHeaderString("Authorization"));
            assertTrue(resp.getEntity() instanceof ResponseLoginDTO);
            ResponseLoginDTO dto = (ResponseLoginDTO) resp.getEntity();
            assertEquals("token123", dto.tokenResponse.accessToken);
        }
    }

    @Test
    void login_deveRetornarUnauthorizedParaUsuarioInvalido() {
        LoginRequest login = new LoginRequest();
        login.setEmail("a@b.com");
        login.setSenha("senha");
        when(usuarioService.autenticar("a@b.com", "senha")).thenReturn(null);
        Response resp = publicResource.login(login);
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), resp.getStatus());
        assertTrue(resp.getEntity().toString().contains("Usuário ou senha inválidos"));
    }
}
