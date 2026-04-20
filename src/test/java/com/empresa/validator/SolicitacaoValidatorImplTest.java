package com.empresa.validator;

import com.empresa.domain.SolicitacaoRequest;
import com.empresa.model.Solicitacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SolicitacaoValidatorImplTest {
    SolicitacaoValidatorImpl validator;

    @BeforeEach
    void setUp() {
        validator = new SolicitacaoValidatorImpl();
    }

    @Test
    void validarParaCriar_deveAceitarRequestValido() {
        SolicitacaoRequest req = new SolicitacaoRequest();
        req.titulo = "Teste";
        req.categoria = "Categoria";
        req.status = "ABERTA";
        assertDoesNotThrow(() -> validator.validarParaCriar(req));
    }

    @Test
    void validarParaCriar_deveLancarSeRequestNulo() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCriar(null));
        assertTrue(ex.getMessage().contains("Solicitação não pode ser nula"));
    }

    @Test
    void validarParaCriar_deveLancarSeCamposVazios() {
        SolicitacaoRequest req = new SolicitacaoRequest();
        req.titulo = " ";
        req.categoria = "Categoria";
        req.status = "ABERTA";
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCriar(req));
        assertTrue(ex1.getMessage().contains("Título da solicitação não pode ser vazio"));
        req.titulo = "Teste";
        req.categoria = " ";
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCriar(req));
        assertTrue(ex2.getMessage().contains("Categoria da solicitação não pode ser vazia"));
        req.categoria = "Categoria";
        req.status = " ";
        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCriar(req));
        assertTrue(ex3.getMessage().contains("Status da solicitação não pode ser vazio"));
    }

    @Test
    void validarParaAtualizar_deveAceitarDadosValidos() {
        Solicitacao s = new Solicitacao();
        s.titulo = "Teste";
        s.categoria = "Categoria";
        s.status = "ABERTA";
        assertDoesNotThrow(() -> validator.validarParaAtualizar(1L, s));
    }

    @Test
    void validarParaAtualizar_deveLancarSeIdNuloOuInvalido() {
        Solicitacao s = new Solicitacao();
        s.titulo = "Teste";
        s.categoria = "Categoria";
        s.status = "ABERTA";
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(null, s));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(0L, s));
        assertTrue(ex1.getMessage().contains("ID inválido"));
        assertTrue(ex2.getMessage().contains("ID inválido"));
    }

    @Test
    void validarParaAtualizar_deveLancarSeSolicitacaoNula() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(1L, null));
        assertTrue(ex.getMessage().contains("Dados da solicitação não podem ser nulos"));
    }

    @Test
    void validarParaAtualizar_deveLancarSeCamposVazios() {
        Solicitacao s = new Solicitacao();
        s.titulo = " ";
        s.categoria = "Categoria";
        s.status = "ABERTA";
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(1L, s));
        assertTrue(ex1.getMessage().contains("Título da solicitação não pode ser vazio"));
        s.titulo = "Teste";
        s.categoria = " ";
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(1L, s));
        assertTrue(ex2.getMessage().contains("Categoria da solicitação não pode ser vazia"));
        s.categoria = "Categoria";
        s.status = " ";
        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(1L, s));
        assertTrue(ex3.getMessage().contains("Status da solicitação não pode ser vazio"));
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
    void validarParaListarPorUsuario_deveAceitarParametrosValidos() {
        assertDoesNotThrow(() -> validator.validarParaListarPorUsuario("email@teste.com", 0, 10));
    }

    @Test
    void validarParaListarPorUsuario_deveLancarSeParametrosInvalidos() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaListarPorUsuario(" ", 0, 10));
        assertTrue(ex1.getMessage().contains("Email do usuário não pode ser vazio"));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaListarPorUsuario("email@teste.com", -1, 10));
        assertTrue(ex2.getMessage().contains("Página não pode ser negativa"));
        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaListarPorUsuario("email@teste.com", 0, 0));
        assertTrue(ex3.getMessage().contains("Tamanho da página deve ser maior que zero"));
    }

    @Test
    void validarParaListarPorUsuarioAtendente_deveAceitarParametrosValidos() {
        assertDoesNotThrow(() -> validator.validarParaListarPorUsuarioAtendente("email@teste.com", 0, 10));
    }

    @Test
    void validarParaListarPorUsuarioAtendente_deveLancarSeParametrosInvalidos() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaListarPorUsuarioAtendente(" ", 0, 10));
        assertTrue(ex1.getMessage().contains("Email do atendente não pode ser vazio"));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaListarPorUsuarioAtendente("email@teste.com", -1, 10));
        assertTrue(ex2.getMessage().contains("Página não pode ser negativa"));
        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaListarPorUsuarioAtendente("email@teste.com", 0, 0));
        assertTrue(ex3.getMessage().contains("Tamanho da página deve ser maior que zero"));
    }

    @Test
    void validarParaCountPorUsuario_deveAceitarEmailValido() {
        assertDoesNotThrow(() -> validator.validarParaCountPorUsuario("email@teste.com"));
    }

    @Test
    void validarParaCountPorUsuario_deveLancarSeEmailVazio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCountPorUsuario(" "));
        assertTrue(ex.getMessage().contains("Email do usuário não pode ser vazio"));
    }

    @Test
    void validarParaCountPorUsuarioAtendente_deveAceitarEmailValido() {
        assertDoesNotThrow(() -> validator.validarParaCountPorUsuarioAtendente("email@teste.com"));
    }

    @Test
    void validarParaCountPorUsuarioAtendente_deveLancarSeEmailVazio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCountPorUsuarioAtendente(" "));
        assertTrue(ex.getMessage().contains("Email do atendente não pode ser vazio"));
    }
}
