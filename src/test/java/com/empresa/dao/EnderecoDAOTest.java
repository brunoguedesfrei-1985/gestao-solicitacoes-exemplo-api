
package com.empresa.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.empresa.model.Endereco;
import com.empresa.model.Usuario;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
class EnderecoDAOTest {

    @Inject
    EnderecoDAO enderecoDAO;

    Usuario usuario;

    @BeforeEach
    @Transactional
    void setup() {
        // Limpa entidades dependentes primeiro para evitar violação de FK
        com.empresa.model.Solicitacao.deleteAll();
        Endereco.deleteAll();
        Usuario.deleteAll();
        usuario = new Usuario();
        usuario.nome = "Usuário Teste";
        usuario.cpf = "12345678901";
        usuario.email = "teste@exemplo.com";
        usuario.senha = "senha";
        usuario.persist();
    }

    @Test
    @Transactional
    void listarTodos_deveRetornarListaDeEnderecos() {
        Endereco e1 = new Endereco();
        e1.cep = "1";
        e1.endereco = "Rua 1";
        e1.usuario = usuario;
        e1.persist();
        Endereco e2 = new Endereco();
        e2.cep = "2";
        e2.endereco = "Rua 2";
        e2.usuario = usuario;
        e2.persist();
        List<Endereco> result = enderecoDAO.listarTodos();
        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    void buscarPorId_deveRetornarEndereco() {
        Endereco e = new Endereco();
        e.cep = "1";
        e.endereco = "Rua 1";
        e.usuario = usuario;
        e.persist();
        Endereco result = enderecoDAO.buscarPorId(e.id);
        assertNotNull(result);
        assertEquals("1", result.cep);
    }

    @Test
    @Transactional
    void criar_devePersistirEnderecoComUsuarioExistente() {
        Endereco e = new Endereco();
        e.cep = "12345";
        e.endereco = "Rua Teste";
        e.usuario = usuario;
        Endereco result = enderecoDAO.criar(e);
        assertNotNull(result.id);
        assertEquals(usuario.id, result.usuario.id);
    }

    @Test
    @Transactional
    void criar_deveLancarExcecaoSeUsuarioNaoExiste() {
        Endereco e = new Endereco();
        e.cep = "12345";
        e.endereco = "Rua Teste";
        Usuario u = new Usuario();
        u.id = 999L;
        e.usuario = u;
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> enderecoDAO.criar(e));
        assertTrue(ex.getMessage().contains("Usuário informado não existe"));
    }

    @Test
    @Transactional
    void criar_deveLancarExcecaoSeUsuarioNulo() {
        Endereco e = new Endereco();
        e.cep = "12345";
        e.endereco = "Rua Teste";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> enderecoDAO.criar(e));
        assertTrue(ex.getMessage().contains("Usuário é obrigatório"));
    }

    @Test
    @Transactional
    void atualizar_deveAlterarEnderecoEUsuario() {
        Endereco e = new Endereco();
        e.cep = "velho";
        e.endereco = "velho";
        e.usuario = usuario;
        e.persist();
        Usuario novoUsuario = new Usuario();
        novoUsuario.nome = "Novo Usuário";
        novoUsuario.cpf = "98765432100";
        novoUsuario.email = "novo@exemplo.com";
        novoUsuario.senha = "senha";
        novoUsuario.persist();
        Endereco dados = new Endereco();
        dados.cep = "novo";
        dados.endereco = "novo";
        dados.usuario = novoUsuario;
        Endereco result = enderecoDAO.atualizar(e.id, dados);
        assertEquals("novo", result.cep);
        assertEquals(novoUsuario.id, result.usuario.id);
    }

    @Test
    @Transactional
    void atualizar_deveRetornarNullSeEnderecoNaoExiste() {
        Endereco dados = new Endereco();
        dados.cep = "novo";
        dados.endereco = "novo";
        dados.usuario = usuario;
        Endereco result = enderecoDAO.atualizar(999L, dados);
        assertNull(result);
    }

    @Test
    @Transactional
    void deletar_deveChamarDeleteById() {
        Endereco e = new Endereco();
        e.cep = "12345";
        e.endereco = "Rua Teste";
        e.usuario = usuario;
        e.persist();
        boolean result = enderecoDAO.deletar(e.id);
        assertTrue(result);
        assertNull(Endereco.findById(e.id));
    }

    @Test
    @Transactional
    void buscarPorUsuarioECep_deveDelegarParaFind() {
        Endereco e = new Endereco();
        e.cep = "12345";
        e.endereco = "Rua Teste";
        e.usuario = usuario;
        e.persist();
        Endereco result = enderecoDAO.buscarPorUsuarioECep(usuario.id, "12345");
        assertNotNull(result);
        assertEquals(e.id, result.id);
    }

    @Test
    @Transactional
    void buscarPorUsuario_deveDelegarParaFind() {
        Endereco e = new Endereco();
        e.cep = "12345";
        e.endereco = "Rua Teste";
        e.usuario = usuario;
        e.persist();
        Endereco result = enderecoDAO.buscarPorUsuario(usuario.id);
        assertNotNull(result);
        assertEquals(e.id, result.id);
    }
}
