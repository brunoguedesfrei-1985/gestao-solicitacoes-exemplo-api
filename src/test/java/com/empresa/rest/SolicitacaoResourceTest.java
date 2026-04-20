package com.empresa.rest;

import com.empresa.domain.SolicitacaoRequest;
import com.empresa.dto.SolicitacaoDTO;
import com.empresa.mapper.SolicitacaoMapper;
import com.empresa.model.Solicitacao;
import com.empresa.model.Usuario;
import com.empresa.service.SolicitacaoService;
import com.empresa.validator.SolicitacaoValidator;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.security.Principal;
// ...existing code...
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SolicitacaoResourceTest {
    @Mock
    SolicitacaoValidator solicitacaoValidator;
    @Mock
    SolicitacaoService solicitacaoService;
    @Mock
    SecurityContext securityContext;
    @Mock
    Principal principal;
    @InjectMocks
    SolicitacaoResource resource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(securityContext.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("user@email.com");
    }

    @Test
    void listarTodasDemandante_deveRetornarListaComTotal() {
        Solicitacao s = new Solicitacao();
        SolicitacaoDTO dto = mock(SolicitacaoDTO.class);
        when(solicitacaoService.countPorUsuarioDemandante("user@email.com")).thenReturn(1L);
        when(solicitacaoService.listarTodasPorUsuario("user@email.com", 0, 10)).thenReturn(List.of(s));
        try (org.mockito.MockedStatic<SolicitacaoMapper> mocked = mockStatic(SolicitacaoMapper.class)) {
            mocked.when(() -> SolicitacaoMapper.toDTO(s)).thenReturn(dto);
            Response resp = resource.listarTodas(0, 10);
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) resp.getEntity();
            assertEquals(1L, map.get("total"));
            assertEquals(List.of(dto), map.get("items"));
        }
    }

    @Test
    void listarTodasAtendente_deveRetornarListaComTotal() {
        Solicitacao s = new Solicitacao();
        SolicitacaoDTO dto = mock(SolicitacaoDTO.class);
        when(solicitacaoService.countPorUsuarioAtendente("user@email.com")).thenReturn(1L);
        when(solicitacaoService.listarTodasPorUsuarioAtendente("user@email.com", 0, 10)).thenReturn(List.of(s));
        try (org.mockito.MockedStatic<SolicitacaoMapper> mocked = mockStatic(SolicitacaoMapper.class)) {
            mocked.when(() -> SolicitacaoMapper.toDTO(s)).thenReturn(dto);
            Response resp = resource.listarTodasUsuarioAtendente(0, 10);
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) resp.getEntity();
            assertEquals(1L, map.get("total"));
            assertEquals(List.of(dto), map.get("items"));
        }
    }

    @Test
    void buscarPorIdDemandante_deveRetornarOk() {
        Solicitacao s = new Solicitacao();
        Usuario u = new Usuario();
        u.email = "user@email.com";
        s.usuarioAtribuido = u;
        SolicitacaoDTO dto = mock(SolicitacaoDTO.class);
        when(solicitacaoService.buscarPorId(1L)).thenReturn(s);
        try (org.mockito.MockedStatic<SolicitacaoMapper> mocked = mockStatic(SolicitacaoMapper.class)) {
            mocked.when(() -> SolicitacaoMapper.toDTO(s)).thenReturn(dto);
            Response resp = resource.buscarPorIdDemandante(1L);
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals(dto, resp.getEntity());
        }
    }

    @Test
    void buscarPorIdDemandante_deveRetornarNotFound() {
        when(solicitacaoService.buscarPorId(1L)).thenReturn(null);
        Response resp = resource.buscarPorIdDemandante(1L);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    }

    @Test
    void buscarPorIdDemandante_deveRetornarForbidden() {
        Solicitacao s = new Solicitacao();
        Usuario u = new Usuario();
        u.email = "other@email.com";
        s.usuarioAtribuido = u;
        when(solicitacaoService.buscarPorId(1L)).thenReturn(s);
        Response resp = resource.buscarPorIdDemandante(1L);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), resp.getStatus());
        assertTrue(resp.getEntity().toString().contains("Acesso negado"));
    }

    @Test
    void buscarPorIdAtendente_deveRetornarOk() {
        Solicitacao s = new Solicitacao();
        Usuario u = new Usuario();
        u.email = "user@email.com";
        s.usuarioAtendente = u;
        SolicitacaoDTO dto = mock(SolicitacaoDTO.class);
        when(solicitacaoService.buscarPorId(1L)).thenReturn(s);
        try (org.mockito.MockedStatic<SolicitacaoMapper> mocked = mockStatic(SolicitacaoMapper.class)) {
            mocked.when(() -> SolicitacaoMapper.toDTO(s)).thenReturn(dto);
            Response resp = resource.buscarPorIdAtendente(1L);
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals(dto, resp.getEntity());
        }
    }

    @Test
    void buscarPorIdAtendente_deveRetornarNotFound() {
        when(solicitacaoService.buscarPorId(1L)).thenReturn(null);
        Response resp = resource.buscarPorIdAtendente(1L);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    }

    @Test
    void buscarPorIdAtendente_deveRetornarForbidden() {
        Solicitacao s = new Solicitacao();
        Usuario u = new Usuario();
        u.email = "other@email.com";
        s.usuarioAtendente = u;
        when(solicitacaoService.buscarPorId(1L)).thenReturn(s);
        Response resp = resource.buscarPorIdAtendente(1L);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), resp.getStatus());
        assertTrue(resp.getEntity().toString().contains("Acesso negado"));
    }

    @Test
    void criar_deveRetornarCreated() {
        SolicitacaoRequest req = mock(SolicitacaoRequest.class);
        SolicitacaoDTO dto = mock(SolicitacaoDTO.class);
        doNothing().when(solicitacaoValidator).validarParaCriar(req);
        when(solicitacaoService.criar(req)).thenReturn(dto);
        Response resp = resource.criar(req);
        assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
        assertEquals(dto, resp.getEntity());
    }

    @Test
    void criar_deveRetornarBadRequestParaValidacao() {
        SolicitacaoRequest req = mock(SolicitacaoRequest.class);
        doThrow(new IllegalArgumentException("erro")).when(solicitacaoValidator).validarParaCriar(req);
        Response resp = resource.criar(req);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
        assertTrue(((Map<?,?>)resp.getEntity()).get("erro").toString().contains("erro"));
    }

    @Test
    void criar_deveRetornarServerErrorEmExcecao() {
        SolicitacaoRequest req = mock(SolicitacaoRequest.class);
        doNothing().when(solicitacaoValidator).validarParaCriar(req);
        when(solicitacaoService.criar(req)).thenThrow(new RuntimeException("fail"));
        Response resp = resource.criar(req);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), resp.getStatus());
    }

    @Test
    void atualizarDemandante_deveRetornarOk() {
        Solicitacao dados = new Solicitacao();
        Solicitacao s = new Solicitacao();
        Usuario u = new Usuario();
        u.email = "user@email.com";
        s.usuarioAtribuido = u;
        SolicitacaoDTO dto = mock(SolicitacaoDTO.class);
        when(solicitacaoService.buscarPorId(1L)).thenReturn(s);
        doNothing().when(solicitacaoValidator).validarParaAtualizar(1L, dados);
        when(solicitacaoService.atualizar(1L, dados)).thenReturn(dto);
        try (org.mockito.MockedStatic<SolicitacaoMapper> mocked = mockStatic(SolicitacaoMapper.class)) {
            mocked.when(() -> SolicitacaoMapper.toDTO(s)).thenReturn(dto);
            Response resp = resource.atualizarDemandante(1L, dados);
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals(dto, resp.getEntity());
        }
    }

    @Test
    void atualizarDemandante_deveRetornarNotFound() {
        Solicitacao dados = new Solicitacao();
        when(solicitacaoService.buscarPorId(1L)).thenReturn(null);
        Response resp = resource.atualizarDemandante(1L, dados);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    }

    @Test
    void atualizarDemandante_deveRetornarForbidden() {
        Solicitacao dados = new Solicitacao();
        Solicitacao s = new Solicitacao();
        Usuario u = new Usuario();
        u.email = "other@email.com";
        s.usuarioAtribuido = u;
        when(solicitacaoService.buscarPorId(1L)).thenReturn(s);
        Response resp = resource.atualizarDemandante(1L, dados);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), resp.getStatus());
    }

    @Test
    void atualizarDemandante_deveRetornarBadRequestParaValidacao() {
        Solicitacao dados = new Solicitacao();
        Solicitacao s = new Solicitacao();
        Usuario u = new Usuario();
        u.email = "user@email.com";
        s.usuarioAtribuido = u;
        when(solicitacaoService.buscarPorId(1L)).thenReturn(s);
        doThrow(new IllegalArgumentException("erro")).when(solicitacaoValidator).validarParaAtualizar(1L, dados);
        Response resp = resource.atualizarDemandante(1L, dados);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
        assertTrue(((Map<?,?>)resp.getEntity()).get("erro").toString().contains("erro"));
    }

    @Test
    void atualizarAtendente_deveRetornarOk() {
        Solicitacao dados = new Solicitacao();
        Solicitacao s = new Solicitacao();
        Usuario u = new Usuario();
        u.email = "user@email.com";
        s.usuarioAtendente = u;
        SolicitacaoDTO dto = mock(SolicitacaoDTO.class);
        when(solicitacaoService.buscarPorId(1L)).thenReturn(s);
        doNothing().when(solicitacaoValidator).validarParaAtualizar(1L, dados);
        when(solicitacaoService.atualizar(1L, dados)).thenReturn(dto);
        try (org.mockito.MockedStatic<SolicitacaoMapper> mocked = mockStatic(SolicitacaoMapper.class)) {
            mocked.when(() -> SolicitacaoMapper.toDTO(s)).thenReturn(dto);
            Response resp = resource.atualizarAtendente(1L, dados);
            assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
            assertEquals(dto, resp.getEntity());
        }
    }

    @Test
    void atualizarAtendente_deveRetornarNotFound() {
        Solicitacao dados = new Solicitacao();
        when(solicitacaoService.buscarPorId(1L)).thenReturn(null);
        Response resp = resource.atualizarAtendente(1L, dados);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    }

    @Test
    void atualizarAtendente_deveRetornarForbidden() {
        Solicitacao dados = new Solicitacao();
        Solicitacao s = new Solicitacao();
        Usuario u = new Usuario();
        u.email = "other@email.com";
        s.usuarioAtendente = u;
        when(solicitacaoService.buscarPorId(1L)).thenReturn(s);
        Response resp = resource.atualizarAtendente(1L, dados);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), resp.getStatus());
    }

    @Test
    void atualizarAtendente_deveRetornarBadRequestParaValidacao() {
        Solicitacao dados = new Solicitacao();
        Solicitacao s = new Solicitacao();
        Usuario u = new Usuario();
        u.email = "user@email.com";
        s.usuarioAtendente = u;
        when(solicitacaoService.buscarPorId(1L)).thenReturn(s);
        doThrow(new IllegalArgumentException("erro")).when(solicitacaoValidator).validarParaAtualizar(1L, dados);
        Response resp = resource.atualizarAtendente(1L, dados);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
        assertTrue(((Map<?,?>)resp.getEntity()).get("erro").toString().contains("erro"));
    }
}
