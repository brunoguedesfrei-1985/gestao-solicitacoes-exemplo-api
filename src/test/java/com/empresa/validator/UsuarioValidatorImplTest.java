package com.empresa.validator;

import com.empresa.domain.UsuarioRequest;
import com.empresa.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioValidatorImplTest {
    UsuarioValidatorImpl validator;

    @BeforeEach
    void setUp() {
        validator = new UsuarioValidatorImpl();
    }

    @Test
    void validarParaCriar_deveAceitarUsuarioValido() {
        UsuarioRequest u = new UsuarioRequest();
        u.nome = "Fulano";
        u.email = "fulano@teste.com";
        u.cpf = "12345678901";
        u.senha = "senha";
        assertDoesNotThrow(() -> validator.validarParaCriar(u));
    }

    @Test
    void validarParaCriar_deveLancarSeUsuarioNulo() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCriar(null));
        assertTrue(ex.getMessage().contains("Usuário não pode ser nulo"));
    }

    @Test
    void validarParaCriar_deveLancarSeCamposVazios() {
        UsuarioRequest u = new UsuarioRequest();
        u.nome = " ";
        u.email = "fulano@teste.com";
        u.cpf = "12345678901";
        u.senha = "senha";
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCriar(u));
        assertTrue(ex1.getMessage().contains("Nome do usuário não pode ser vazio"));
        u.nome = "Fulano";
        u.email = " ";
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCriar(u));
        assertTrue(ex2.getMessage().contains("Email do usuário não pode ser vazio"));
        u.email = "fulano@teste.com";
        u.cpf = " ";
        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCriar(u));
        assertTrue(ex3.getMessage().contains("CPF do usuário não pode ser vazio"));
        u.cpf = "12345678901";
        u.senha = " ";
        IllegalArgumentException ex4 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCriar(u));
        assertTrue(ex4.getMessage().contains("Senha do usuário não pode ser vazia"));
    }

    @Test
    void validarParaAtualizar_deveAceitarDadosValidos() {
        Usuario u = new Usuario();
        u.nome = "Fulano";
        u.email = "fulano@teste.com";
        u.cpf = "12345678901";
        assertDoesNotThrow(() -> validator.validarParaAtualizar(1L, u));
    }

    @Test
    void validarParaAtualizar_deveLancarSeIdNuloOuInvalido() {
        Usuario u = new Usuario();
        u.nome = "Fulano";
        u.email = "fulano@teste.com";
        u.cpf = "12345678901";
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(null, u));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(0L, u));
        assertTrue(ex1.getMessage().contains("ID inválido"));
        assertTrue(ex2.getMessage().contains("ID inválido"));
    }

    @Test
    void validarParaAtualizar_deveLancarSeUsuarioNulo() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(1L, null));
        assertTrue(ex.getMessage().contains("Dados do usuário não podem ser nulos"));
    }

    @Test
    void validarParaAtualizar_deveLancarSeCamposVazios() {
        Usuario u = new Usuario();
        u.nome = " ";
        u.email = "fulano@teste.com";
        u.cpf = "12345678901";
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(1L, u));
        assertTrue(ex1.getMessage().contains("Nome do usuário não pode ser vazio"));
        u.nome = "Fulano";
        u.email = " ";
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(1L, u));
        assertTrue(ex2.getMessage().contains("Email do usuário não pode ser vazio"));
        u.email = "fulano@teste.com";
        u.cpf = " ";
        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(1L, u));
        assertTrue(ex3.getMessage().contains("CPF do usuário não pode ser vazio"));
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

    @Test
    void validarParaAutenticar_deveAceitarCamposValidos() {
        assertDoesNotThrow(() -> validator.validarParaAutenticar("fulano@teste.com", "senha"));
    }

    @Test
    void validarParaAutenticar_deveLancarSeCamposVazios() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAutenticar(" ", "senha"));
        assertTrue(ex1.getMessage().contains("Email não pode ser vazio"));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAutenticar("fulano@teste.com", " "));
        assertTrue(ex2.getMessage().contains("Senha não pode ser vazia"));
    }
}
