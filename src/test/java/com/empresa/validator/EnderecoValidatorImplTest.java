package com.empresa.validator;

import com.empresa.model.Endereco;
import com.empresa.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnderecoValidatorImplTest {
    EnderecoValidatorImpl validator;

    @BeforeEach
    void setUp() {
        validator = new EnderecoValidatorImpl();
    }

    @Test
    void validarParaCriar_deveAceitarEnderecoValido() {
        Endereco e = new Endereco();
        Usuario u = new Usuario();
        u.id = 1L;
        e.usuario = u;
        e.cep = "12345-678";
        e.endereco = "Rua Teste";
        assertDoesNotThrow(() -> validator.validarParaCriar(e));
    }

    @Test
    void validarParaCriar_deveLancarSeEnderecoNulo() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCriar(null));
        assertTrue(ex.getMessage().contains("Endereço não pode ser nulo"));
    }

    @Test
    void validarParaCriar_deveLancarSeUsuarioNuloOuIdInvalido() {
        Endereco e = new Endereco();
        e.usuario = null;
        e.cep = "12345-678";
        e.endereco = "Rua Teste";
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCriar(e));
        assertTrue(ex1.getMessage().contains("Usuário do endereço inválido"));
        Usuario u = new Usuario();
        u.id = null;
        e.usuario = u;
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCriar(e));
        assertTrue(ex2.getMessage().contains("Usuário do endereço inválido"));
        u.id = 0L;
        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCriar(e));
        assertTrue(ex3.getMessage().contains("Usuário do endereço inválido"));
    }

    @Test
    void validarParaCriar_deveLancarSeCepOuEnderecoVazio() {
        Endereco e = new Endereco();
        Usuario u = new Usuario();
        u.id = 1L;
        e.usuario = u;
        e.cep = " ";
        e.endereco = "Rua Teste";
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCriar(e));
        assertTrue(ex1.getMessage().contains("CEP do endereço não pode ser vazio"));
        e.cep = "12345-678";
        e.endereco = " ";
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCriar(e));
        assertTrue(ex2.getMessage().contains("Endereço não pode ser vazio"));
    }

    @Test
    void validarParaAtualizar_deveAceitarDadosValidos() {
        Endereco e = new Endereco();
        e.cep = "12345-678";
        e.endereco = "Rua Teste";
        assertDoesNotThrow(() -> validator.validarParaAtualizar(1L, e));
    }

    @Test
    void validarParaAtualizar_deveLancarSeIdNuloOuInvalido() {
        Endereco e = new Endereco();
        e.cep = "12345-678";
        e.endereco = "Rua Teste";
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(null, e));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(0L, e));
        assertTrue(ex1.getMessage().contains("ID inválido"));
        assertTrue(ex2.getMessage().contains("ID inválido"));
    }

    @Test
    void validarParaAtualizar_deveLancarSeEnderecoNulo() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(1L, null));
        assertTrue(ex.getMessage().contains("Dados do endereço não podem ser nulos"));
    }

    @Test
    void validarParaAtualizar_deveLancarSeCepOuEnderecoVazio() {
        Endereco e = new Endereco();
        e.cep = " ";
        e.endereco = "Rua Teste";
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(1L, e));
        assertTrue(ex1.getMessage().contains("CEP do endereço não pode ser vazio"));
        e.cep = "12345-678";
        e.endereco = " ";
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(1L, e));
        assertTrue(ex2.getMessage().contains("Endereço não pode ser vazio"));
    }

    @Test
    void validarParaBuscarPorId_deveAceitarIdValido() {
        assertDoesNotThrow(() -> validator.validarParaBuscarPorId(1L));
    }

    @Test
    void validarParaBuscarPorId_deveLancarSeIdNuloOuInvalido() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaBuscarPorId(null));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaBuscarPorId(0L));
        assertTrue(ex1.getMessage().contains("ID inválido"));
        assertTrue(ex2.getMessage().contains("ID inválido"));
    }

    @Test
    void validarParaBuscarPorCep_deveAceitarCepValido() {
        assertDoesNotThrow(() -> validator.validarParaBuscarPorCep("12345-678"));
    }

    @Test
    void validarParaBuscarPorCep_deveLancarSeCepVazio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validarParaBuscarPorCep(" "));
        assertTrue(ex.getMessage().contains("CEP não pode ser vazio"));
    }

    @Test
    void validarParaDeletar_deveAceitarIdValido() {
        assertDoesNotThrow(() -> validator.validarParaDeletar(1L));
    }

    @Test
    void validarParaDeletar_deveLancarSeIdNuloOuInvalido() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaDeletar(null));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaDeletar(0L));
        assertTrue(ex1.getMessage().contains("ID inválido"));
        assertTrue(ex2.getMessage().contains("ID inválido"));
    }
}
