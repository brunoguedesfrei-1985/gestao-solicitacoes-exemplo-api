package com.empresa.dao;

import com.empresa.model.Cargo;
import com.empresa.model.Usuario;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class UsuarioDAOTest {
    @Inject
    UsuarioDAO usuarioDAO;

    @BeforeEach
    @Transactional
    void setup() {
        Usuario.deleteAll();
        Cargo.deleteAll();
    }

    @Test
    @Transactional
    void listarTodos_deveRetornarListaDeUsuarios() {
        Usuario u1 = new Usuario();
        u1.nome = "Usuário 1";
        u1.email = "u1@email.com";
        u1.senha = "senha";
        u1.cpf = "11111111111";
        u1.persist();
        Usuario u2 = new Usuario();
        u2.nome = "Usuário 2";
        u2.email = "u2@email.com";
        u2.senha = "senha";
        u2.cpf = "22222222222";
        u2.persist();
        List<Usuario> result = usuarioDAO.listarTodos();
        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    void buscarPorId_deveRetornarUsuario() {
        Usuario u = new Usuario();
        u.nome = "Usuário Teste";
        u.email = "teste@email.com";
        u.senha = "senha";
        u.cpf = "33333333333";
        u.persist();
        Usuario result = usuarioDAO.buscarPorId(u.id);
        assertNotNull(result);
        assertEquals("teste@email.com", result.email);
    }

    @Test
    @Transactional
    void criar_devePersistirUsuario() {
        Usuario u = new Usuario();
        u.nome = "Novo Usuário";
        u.email = "novo@email.com";
        u.senha = "senha";
        u.cpf = "44444444444";
        Usuario result = usuarioDAO.criar(u);
        assertNotNull(result.id);
        assertEquals("novo@email.com", result.email);
    }

    @Test
    @Transactional
    void criar_deveLancarExcecaoSeEmailJaCadastrado() {
        Usuario u1 = new Usuario();
        u1.nome = "Usuário";
        u1.email = "email@exemplo.com";
        u1.senha = "senha";
        u1.cpf = "55555555555";
        u1.persist();
        Usuario u2 = new Usuario();
        u2.nome = "Outro";
        u2.email = "email@exemplo.com";
        u2.senha = "senha";
        u2.cpf = "66666666666";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> usuarioDAO.criar(u2));
        assertTrue(ex.getMessage().contains("Email já cadastrado"));
    }

    @Test
    @Transactional
    void buscarPorEmail_deveRetornarUsuario() {
        Usuario u = new Usuario();
        u.nome = "Usuário Email";
        u.email = "buscar@email.com";
        u.senha = "senha";
        u.cpf = "77777777777";
        u.persist();
        Usuario result = usuarioDAO.buscarPorEmail("buscar@email.com");
        assertNotNull(result);
        assertEquals("Usuário Email", result.nome);
    }

    @Test
    @Transactional
    void atualizar_deveAlterarDadosDoUsuario() {
        Cargo c = new Cargo();
        c.nome = "Cargo Teste";
        c.persist();
        Usuario u = new Usuario();
        u.nome = "Antigo";
        u.email = "antigo@email.com";
        u.senha = "senha";
        u.cpf = "88888888888";
        u.persist();
        Usuario dados = new Usuario();
        dados.nome = "Novo";
        dados.email = "novo@email.com";
        dados.senha = "novaSenha";
        dados.cpf = "99999999999";
        dados.cargo = c;
        Usuario result = usuarioDAO.atualizar(u.id, dados);
        assertEquals("Novo", result.nome);
        assertEquals("novo@email.com", result.email);
        assertEquals("novaSenha", result.senha);
        assertEquals(c.id, result.cargo.id);
    }

    @Test
    @Transactional
    void atualizar_deveRetornarNullSeUsuarioNaoExiste() {
        Usuario dados = new Usuario();
        dados.nome = "Novo";
        dados.email = "novo@email.com";
        dados.senha = "novaSenha";
        dados.cpf = "10101010101";
        Usuario result = usuarioDAO.atualizar(999L, dados);
        assertNull(result);
    }

    @Test
    @Transactional
    void deletar_deveRemoverUsuario() {
        Usuario u = new Usuario();
        u.nome = "Usuário Deletar";
        u.email = "deletar@email.com";
        u.senha = "senha";
        u.cpf = "12121212121";
        u.persist();
        boolean result = usuarioDAO.deletar(u.id);
        assertTrue(result);
        assertNull(Usuario.findById(u.id));
    }
}
