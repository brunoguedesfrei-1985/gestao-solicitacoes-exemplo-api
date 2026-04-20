package com.empresa.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.empresa.dao.EnderecoDAO;
import com.empresa.dto.EnderecoDTO;
import com.empresa.dto.ViaCepDTO;
import com.empresa.model.Endereco;
import com.empresa.validator.EnderecoValidator;

class EnderecoServiceTest {
    @Mock
    EnderecoDAO enderecoDAO;
    @Mock
    ViaCepService viaCepService;
    @Mock
    EnderecoValidator enderecoValidator;
    @InjectMocks
    EnderecoService enderecoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodos_deveRetornarListaDeEnderecos() {
        Endereco e1 = new Endereco(); e1.id = 1L; e1.cep = "1";
        Endereco e2 = new Endereco(); e2.id = 2L; e2.cep = "2";
        when(enderecoDAO.listarTodos()).thenReturn(Arrays.asList(e1, e2));
        List<Endereco> result = enderecoService.listarTodos();
        assertEquals(2, result.size());
        assertEquals("1", result.get(0).cep);
        assertEquals("2", result.get(1).cep);
    }

    @Test
    void buscarPorId_deveValidarEDelegarParaDAO() {
        Endereco e = new Endereco(); e.id = 1L; e.cep = "1";
        when(enderecoDAO.buscarPorId(1L)).thenReturn(e);
        Endereco result = enderecoService.buscarPorId(1L);
        verify(enderecoValidator).validarParaBuscarPorId(1L);
        assertEquals("1", result.cep);
    }

    @Test
    void criar_deveRetornarEnderecoExistentePorUsuarioECep() {
        Endereco e = new Endereco(); e.id = 1L; e.cep = "1"; e.usuario = new com.empresa.model.Usuario(); e.usuario.id = 2L;
        when(enderecoDAO.buscarPorUsuarioECep(2L, "1")).thenReturn(e);
        EnderecoDTO dto = enderecoService.criar(e);
        assertEquals(e.cep, dto.cep);
    }

    @Test
    void criar_deveAtualizarEnderecoExistentePorUsuario() {
        Endereco e = new Endereco(); e.id = 1L; e.cep = "1"; e.usuario = new com.empresa.model.Usuario(); e.usuario.id = 2L;
        Endereco existente = new Endereco(); existente.id = 3L; existente.usuario = e.usuario;
        when(enderecoDAO.buscarPorUsuarioECep(2L, "1")).thenReturn(null);
        when(enderecoDAO.buscarPorUsuario(2L)).thenReturn(existente);
        EnderecoDTO dto = enderecoService.criar(e);
        verify(enderecoDAO).atualizar(3L, existente);
        assertEquals(existente.cep, dto.cep);
    }

    @Test
    void criar_deveCriarNovoEnderecoSeNaoExistir() {
        Endereco e = new Endereco(); e.id = 1L; e.cep = "1"; e.usuario = new com.empresa.model.Usuario(); e.usuario.id = 2L;
        when(enderecoDAO.buscarPorUsuarioECep(2L, "1")).thenReturn(null);
        when(enderecoDAO.buscarPorUsuario(2L)).thenReturn(null);
        when(enderecoDAO.criar(e)).thenReturn(e);
        EnderecoDTO dto = enderecoService.criar(e);
        assertEquals(e.cep, dto.cep);
    }

    @Test
    void buscarPorCep_deveRetornarEnderecoDTOCompleto() throws IOException, InterruptedException {
        ViaCepDTO viaCep = new ViaCepDTO();
        viaCep.logradouro = "Rua";
        viaCep.complemento = "Comp";
        viaCep.bairro = "Bairro";
        viaCep.localidade = "Cidade";
        viaCep.uf = "UF";
        when(viaCepService.buscarEnderecoPorCep("123")).thenReturn(viaCep);
        EnderecoDTO dto = enderecoService.buscarPorCep("123");
        assertTrue(dto.endereco.contains("Rua"));
        assertTrue(dto.endereco.contains("Comp"));
        assertTrue(dto.endereco.contains("Bairro"));
        assertTrue(dto.endereco.contains("Cidade"));
        assertTrue(dto.endereco.contains("UF"));
    }

    @Test
    void buscarPorCep_deveRetornarVazioSeViaCepFalhar() throws IOException, InterruptedException {
        when(viaCepService.buscarEnderecoPorCep("123")).thenThrow(new RuntimeException("erro"));
        EnderecoDTO dto = enderecoService.buscarPorCep("123");
        assertEquals("", dto.endereco);
    }

    @Test
    void atualizar_deveDelegarParaDAO() {
        Endereco e = new Endereco(); e.id = 1L;
        when(enderecoDAO.atualizar(1L, e)).thenReturn(e);
        Endereco result = enderecoService.atualizar(1L, e);
        assertEquals(1L, result.id);
    }

    @Test
    void deletar_deveDelegarParaDAO() {
        when(enderecoDAO.deletar(1L)).thenReturn(true);
        boolean result = enderecoService.deletar(1L);
        assertTrue(result);
    }
}
