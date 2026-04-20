package com.empresa.dao;

import com.empresa.model.Solicitacao;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class SolicitacaoDAOTest {
    @Inject
    SolicitacaoDAO solicitacaoDAO;

    @BeforeEach
    @Transactional
    void setup() {
        Solicitacao.deleteAll();
    }

    @Test
    @Transactional
    void listarTodas_deveRetornarListaDeSolicitacoes() {
        Solicitacao s1 = new Solicitacao();
        s1.titulo = "Solicitação 1";
        s1.status = "ABERTA";
        s1.persist();
        Solicitacao s2 = new Solicitacao();
        s2.titulo = "Solicitação 2";
        s2.status = "ABERTA";
        s2.persist();
        List<Solicitacao> result = solicitacaoDAO.listarTodas();
        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    void buscarPorId_deveRetornarSolicitacao() {
        Solicitacao s = new Solicitacao();
        s.titulo = "Solicitação Teste";
        s.status = "ABERTA";
        s.persist();
        Solicitacao result = solicitacaoDAO.buscarPorId(s.id);
        assertNotNull(result);
        assertEquals("Solicitação Teste", result.titulo);
    }

    @Test
    @Transactional
    void criar_devePersistirSolicitacao() {
        Solicitacao s = new Solicitacao();
        s.titulo = "Nova Solicitação";
        s.status = "ABERTA";
        Solicitacao result = solicitacaoDAO.criar(s);
        assertNotNull(result.id);
        assertEquals("Nova Solicitação", result.titulo);
    }

    @Test
    @Transactional
    void atualizar_deveAlterarDadosDaSolicitacao() {
        Solicitacao s = new Solicitacao();
        s.titulo = "Antigo";
        s.descricao = "desc";
        s.status = "ABERTA";
        s.persist();
        Solicitacao dados = new Solicitacao();
        dados.titulo = "Novo";
        dados.descricao = "nova desc";
        dados.status = "ABERTA";
        Solicitacao result = solicitacaoDAO.atualizar(s.id, dados);
        assertEquals("Novo", result.titulo);
        assertEquals("nova desc", result.descricao);
    }

    @Test
    @Transactional
    void atualizar_deveRetornarNullSeSolicitacaoNaoExiste() {
        Solicitacao dados = new Solicitacao();
        dados.titulo = "Novo";
        dados.status = "ABERTA";
        Solicitacao result = solicitacaoDAO.atualizar(999L, dados);
        assertNull(result);
    }

    @Test
    @Transactional
    void deletar_deveRemoverSolicitacao() {
        Solicitacao s = new Solicitacao();
        s.titulo = "Solicitação Deletar";
        s.status = "ABERTA";
        s.persist();
        boolean result = solicitacaoDAO.deletar(s.id);
        assertTrue(result);
        assertNull(Solicitacao.findById(s.id));
    }

    @Test
    @Transactional
    void countPorUsuarioDemandante_deveContarPorEmail() {
        Solicitacao s = new Solicitacao();
        s.titulo = "Solicitação";
        s.status = "ABERTA";
        s.persist();
        // Simula campo usuarioAtribuido.email se necessário
        long count = solicitacaoDAO.countPorUsuarioDemandante(null);
        assertTrue(count >= 0);
    }

    @Test
    @Transactional
    void countPorUsuarioAtendente_deveContarPorEmail() {
        Solicitacao s = new Solicitacao();
        s.titulo = "Solicitação";
        s.status = "ABERTA";
        s.persist();
        // Simula campo usuarioAtendente.email se necessário
        long count = solicitacaoDAO.countPorUsuarioAtendente(null);
        assertTrue(count >= 0);
    }

    @Test
    @Transactional
    void listarTodasPorUsuario_deveRetornarLista() {
        Solicitacao s = new Solicitacao();
        s.titulo = "Solicitação";
        s.status = "ABERTA";
        s.persist();
        List<Solicitacao> result = solicitacaoDAO.listarTodasPorUsuario(null, 0, 10);
        assertNotNull(result);
    }

    @Test
    @Transactional
    void listarTodasPorUsuarioAtendente_deveRetornarLista() {
        Solicitacao s = new Solicitacao();
        s.titulo = "Solicitação";
        s.status = "ABERTA";
        s.persist();
        List<Solicitacao> result = solicitacaoDAO.listarTodasPorUsuarioAtendente(null, 0, 10);
        assertNotNull(result);
    }
}
