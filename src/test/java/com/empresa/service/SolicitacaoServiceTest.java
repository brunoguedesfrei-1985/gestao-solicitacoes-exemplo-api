package com.empresa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.empresa.dao.SolicitacaoDAO;
import com.empresa.dao.UsuarioDAO;
import com.empresa.domain.SolicitacaoRequest;
import com.empresa.dto.SolicitacaoDTO;
import com.empresa.model.Solicitacao;
import com.empresa.model.Usuario;
import com.empresa.validator.SolicitacaoValidator;

class SolicitacaoServiceTest {
    @Mock
    SolicitacaoDAO solicitacaoDAO;
    @Mock
    UsuarioDAO usuarioDAO;
    @Mock
    SolicitacaoValidator solicitacaoValidator;
    @InjectMocks
    SolicitacaoService solicitacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void countPorUsuarioDemandante_deveValidarEDelegarParaDAO() {
        when(solicitacaoDAO.countPorUsuarioDemandante("a@a.com")).thenReturn(5L);
        long result = solicitacaoService.countPorUsuarioDemandante("a@a.com");
        verify(solicitacaoValidator).validarParaCountPorUsuario("a@a.com");
        assertEquals(5L, result);
    }

    @Test
    void countPorUsuarioAtendente_deveValidarEDelegarParaDAO() {
        when(solicitacaoDAO.countPorUsuarioAtendente("b@b.com")).thenReturn(3L);
        long result = solicitacaoService.countPorUsuarioAtendente("b@b.com");
        verify(solicitacaoValidator).validarParaCountPorUsuarioAtendente("b@b.com");
        assertEquals(3L, result);
    }

    @Test
    void listarTodasPorUsuario_deveValidarEDelegarParaDAO() {
        Solicitacao s = new Solicitacao(); s.titulo = "Teste";
        when(solicitacaoDAO.listarTodasPorUsuario("a@a.com", 0, 10)).thenReturn(Arrays.asList(s));
        List<Solicitacao> result = solicitacaoService.listarTodasPorUsuario("a@a.com", 0, 10);
        verify(solicitacaoValidator).validarParaListarPorUsuario("a@a.com", 0, 10);
        assertEquals(1, result.size());
        assertEquals("Teste", result.get(0).titulo);
    }

    @Test
    void listarTodasPorUsuarioAtendente_deveValidarEDelegarParaDAO() {
        Solicitacao s = new Solicitacao(); s.titulo = "Atendente";
        when(solicitacaoDAO.listarTodasPorUsuarioAtendente("b@b.com", 1, 5)).thenReturn(Arrays.asList(s));
        List<Solicitacao> result = solicitacaoService.listarTodasPorUsuarioAtendente("b@b.com", 1, 5);
        verify(solicitacaoValidator).validarParaListarPorUsuarioAtendente("b@b.com", 1, 5);
        assertEquals(1, result.size());
        assertEquals("Atendente", result.get(0).titulo);
    }

    @Test
    void listarTodas_deveRetornarListaDeSolicitacoes() {
        Solicitacao s1 = new Solicitacao(); s1.titulo = "A";
        Solicitacao s2 = new Solicitacao(); s2.titulo = "B";
        when(solicitacaoDAO.listarTodas()).thenReturn(Arrays.asList(s1, s2));
        List<Solicitacao> result = solicitacaoService.listarTodas();
        assertEquals(2, result.size());
        assertEquals("A", result.get(0).titulo);
        assertEquals("B", result.get(1).titulo);
    }

    @Test
    void buscarPorId_deveValidarEDelegarParaDAO() {
        Solicitacao s = new Solicitacao(); s.titulo = "Teste";
        when(solicitacaoDAO.buscarPorId(1L)).thenReturn(s);
        Solicitacao result = solicitacaoService.buscarPorId(1L);
        verify(solicitacaoValidator).validarParaBuscarPorId(1L);
        assertEquals("Teste", result.titulo);
    }

    @Test
    void criar_deveCriarSolicitacaoComUsuarios() {
        SolicitacaoRequest req = new SolicitacaoRequest();
        req.titulo = "Nova"; req.descricao = "desc"; req.categoria = "cat"; req.status = "aberta";
        req.usuarioAtribuidoId = 2L; req.usuarioAtendenteId = 3L;
        Usuario usuarioAtribuido = new Usuario(); usuarioAtribuido.id = 2L;
        Usuario usuarioAtendente = new Usuario(); usuarioAtendente.id = 3L;
        when(usuarioDAO.buscarPorId(2L)).thenReturn(usuarioAtribuido);
        when(usuarioDAO.buscarPorId(3L)).thenReturn(usuarioAtendente);
        Solicitacao solicitacaoCriada = new Solicitacao(); solicitacaoCriada.titulo = "Nova";
        when(solicitacaoDAO.criar(any())).thenReturn(solicitacaoCriada);
        SolicitacaoDTO dto = solicitacaoService.criar(req);
        assertEquals("Nova", dto.titulo);
    }

    @Test
    void atualizar_deveDelegarParaDAO() {
        Solicitacao s = new Solicitacao(); s.titulo = "Up";
        when(solicitacaoDAO.atualizar(1L, s)).thenReturn(s);
        SolicitacaoDTO dto = solicitacaoService.atualizar(1L, s);
        assertEquals("Up", dto.titulo);
    }

    @Test
    void deletar_deveDelegarParaDAO() {
        when(solicitacaoDAO.deletar(1L)).thenReturn(true);
        boolean result = solicitacaoService.deletar(1L);
        assertTrue(result);
    }
}
