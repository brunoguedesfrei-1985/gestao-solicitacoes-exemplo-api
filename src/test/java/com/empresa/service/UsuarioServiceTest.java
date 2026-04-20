package com.empresa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.empresa.dao.UsuarioDAO;
import com.empresa.domain.UsuarioRequest;
import com.empresa.dto.UsuarioDTO;
import com.empresa.model.Cargo;
import com.empresa.model.Usuario;
import com.empresa.util.CrypterUtil;
import com.empresa.validator.UsuarioValidator;

class UsuarioServiceTest {
    @Mock
    UsuarioDAO usuarioDAO;
    @Mock
    UsuarioValidator usuarioValidator;
    @Mock
    CargoService cargoService;
    @Mock
    CpfValidatorService cpfValidatorService;
    @InjectMocks
    UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodos_deveRetornarListaDeDTOs() {
        Usuario u1 = new Usuario(); u1.id = 1L; u1.nome = "A";
        Usuario u2 = new Usuario(); u2.id = 2L; u2.nome = "B";
        when(usuarioDAO.listarTodos()).thenReturn(Arrays.asList(u1, u2));
        List<UsuarioDTO> result = usuarioService.listarTodos();
        assertEquals(2, result.size());
        assertEquals("A", result.get(0).nome);
        assertEquals("B", result.get(1).nome);
    }

    @Test
    void buscarPorId_deveValidarEDelegarParaDAO() {
        Usuario u = new Usuario(); u.id = 1L; u.nome = "A";
        when(usuarioDAO.buscarPorId(1L)).thenReturn(u);
        UsuarioDTO result = usuarioService.buscarPorId(1L);
        verify(usuarioValidator).validarParaBuscarPorId(1L);
        assertEquals("A", result.nome);
    }

    @Test
    void criar_deveValidarCpfEDelegarParaDAO() throws IOException, InterruptedException {
        UsuarioRequest req = new UsuarioRequest();
        req.nome = "A"; req.email = "a@a.com"; req.cpf = "123"; req.senha = "senha"; req.cargoId = 2L;
        when(cpfValidatorService.isCpfValido("123")).thenReturn(true);
        Cargo cargo = new Cargo(); cargo.id = 2L; cargo.nome = "Cargo";
        when(cargoService.buscarPorId(2L)).thenReturn(cargo);
        Usuario usuarioCriado = new Usuario(); usuarioCriado.nome = "A"; usuarioCriado.email = "a@a.com";
        when(usuarioDAO.criar(any())).thenReturn(usuarioCriado);
        UsuarioDTO dto = usuarioService.criar(req);
        assertEquals("A", dto.nome);
    }

    @Test
    void criar_deveLancarExcecaoSeCpfInvalido() throws IOException, InterruptedException {
        UsuarioRequest req = new UsuarioRequest();
        req.nome = "A"; req.email = "a@a.com"; req.cpf = "123"; req.senha = "senha";
        when(cpfValidatorService.isCpfValido("123")).thenReturn(false);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> usuarioService.criar(req));
        assertEquals("Erro ao validar CPF: CPF inválido!", ex.getMessage());
    }

    @Test
    void atualizar_deveDelegarParaDAO() {
        Usuario u = new Usuario(); u.id = 1L;
        when(usuarioDAO.atualizar(1L, u)).thenReturn(u);
        Usuario result = usuarioService.atualizar(1L, u);
        assertEquals(1L, result.id);
    }

    @Test
    void deletar_deveDelegarParaDAO() {
        when(usuarioDAO.deletar(1L)).thenReturn(true);
        boolean result = usuarioService.deletar(1L);
        assertTrue(result);
    }

    @Test
    void autenticar_deveValidarEAutenticarComSenhaCorreta() {
        Usuario u = new Usuario(); u.id = 1L; u.nome = "A"; u.email = "a@a.com"; u.senha = CrypterUtil.getHash("senha");
        u.cargo = new Cargo(); u.cargo.id = 2L; u.cargo.nome = "Cargo";
        when(usuarioDAO.buscarPorEmail("a@a.com")).thenReturn(u);
        when(cargoService.buscarPorId(2L)).thenReturn(u.cargo);
        UsuarioDTO dto = usuarioService.autenticar("a@a.com", "senha");
        verify(usuarioValidator).validarParaAutenticar("a@a.com", "senha");
        assertEquals("A", dto.nome);
    }

    @Test
    void autenticar_deveRetornarNullSeUsuarioNaoEncontrado() {
        when(usuarioDAO.buscarPorEmail("a@a.com")).thenReturn(null);
        UsuarioDTO dto = usuarioService.autenticar("a@a.com", "senha");
        assertNull(dto);
    }

    @Test
    void autenticar_deveRetornarNullSeSenhaIncorreta() {
        Usuario u = new Usuario(); u.id = 1L; u.nome = "A"; u.email = "a@a.com"; u.senha = CrypterUtil.getHash("outra");
        u.cargo = new Cargo(); u.cargo.id = 2L; u.cargo.nome = "Cargo";
        when(usuarioDAO.buscarPorEmail("a@a.com")).thenReturn(u);
        UsuarioDTO dto = usuarioService.autenticar("a@a.com", "senhaerrada");
        assertNull(dto);
    }
}
