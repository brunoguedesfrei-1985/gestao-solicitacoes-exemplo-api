package com.empresa.rest;

import com.empresa.dto.EnderecoDTO;
import com.empresa.model.Endereco;
import com.empresa.service.EnderecoService;
import com.empresa.validator.EnderecoValidator;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnderecoResourceTest {
    @Mock
    EnderecoService enderecoService;
    @Mock
    EnderecoValidator enderecoValidator;
    @InjectMocks
    EnderecoResource enderecoResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buscarPorCep_deveRetornarOk() {
        EnderecoDTO dto = mock(EnderecoDTO.class);
        when(enderecoService.buscarPorCep("12345-678")).thenReturn(dto);
        Response resp = enderecoResource.buscarPorCep("12345-678");
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertEquals(dto, resp.getEntity());
    }

    @Test
    void buscarPorCep_deveRetornarNotFound() {
        when(enderecoService.buscarPorCep("00000-000")).thenReturn(null);
        Response resp = enderecoResource.buscarPorCep("00000-000");
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    }

    @Test
    void buscarPorCep_deveRetornarBadRequestEmExcecao() {
        when(enderecoService.buscarPorCep("fail")).thenThrow(new RuntimeException("erro"));
        Response resp = enderecoResource.buscarPorCep("fail");
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
        assertTrue(resp.getEntity().toString().contains("Erro ao consultar CEP"));
    }

    @Test
    void listarTodos_deveRetornarLista() {
        Endereco e1 = new Endereco();
        Endereco e2 = new Endereco();
        when(enderecoService.listarTodos()).thenReturn(Arrays.asList(e1, e2));
        List<Endereco> result = enderecoResource.listarTodos();
        assertEquals(2, result.size());
    }

    @Test
    void buscarPorId_deveRetornarOk() {
        Endereco e = new Endereco();
        when(enderecoService.buscarPorId(1L)).thenReturn(e);
        Response resp = enderecoResource.buscarPorId(1L);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertEquals(e, resp.getEntity());
    }

    @Test
    void buscarPorId_deveRetornarNotFound() {
        when(enderecoService.buscarPorId(1L)).thenReturn(null);
        Response resp = enderecoResource.buscarPorId(1L);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    }

    @Test
    void criar_deveRetornarCreatedParaEnderecoValido() {
        Endereco e = new Endereco();
        EnderecoDTO dto = mock(EnderecoDTO.class);
        doNothing().when(enderecoValidator).validarParaCriar(e);
        when(enderecoService.criar(e)).thenReturn(dto);
        Response resp = enderecoResource.criar(e);
        assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
        assertEquals(dto, resp.getEntity());
    }

    @Test
    void criar_deveRetornarBadRequestParaEnderecoInvalido() {
        Endereco e = new Endereco();
        doThrow(new IllegalArgumentException("erro")).when(enderecoValidator).validarParaCriar(e);
        Response resp = enderecoResource.criar(e);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
        assertTrue(((java.util.Map<?,?>)resp.getEntity()).get("erro").toString().contains("erro"));
    }

    @Test
    void criar_deveRetornarServerErrorEmExcecao() {
        Endereco e = new Endereco();
        doNothing().when(enderecoValidator).validarParaCriar(e);
        when(enderecoService.criar(e)).thenThrow(new RuntimeException("fail"));
        Response resp = enderecoResource.criar(e);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), resp.getStatus());
    }

    @Test
    void atualizar_deveRetornarOkParaAtualizacaoValida() {
        Endereco e = new Endereco();
        doNothing().when(enderecoValidator).validarParaAtualizar(1L, e);
        when(enderecoService.atualizar(1L, e)).thenReturn(e);
        Response resp = enderecoResource.atualizar(1L, e);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertEquals(e, resp.getEntity());
    }

    @Test
    void atualizar_deveRetornarBadRequestParaAtualizacaoInvalida() {
        Endereco e = new Endereco();
        doThrow(new IllegalArgumentException("erro")).when(enderecoValidator).validarParaAtualizar(1L, e);
        Response resp = enderecoResource.atualizar(1L, e);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
        assertTrue(((java.util.Map<?,?>)resp.getEntity()).get("erro").toString().contains("erro"));
    }

    @Test
    void atualizar_deveRetornarNotFoundSeEnderecoNaoExiste() {
        Endereco e = new Endereco();
        doNothing().when(enderecoValidator).validarParaAtualizar(1L, e);
        when(enderecoService.atualizar(1L, e)).thenReturn(null);
        Response resp = enderecoResource.atualizar(1L, e);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    }

    @Test
    void deletar_deveRetornarNoContentSeRemovido() {
        doNothing().when(enderecoValidator).validarParaDeletar(1L);
        when(enderecoService.deletar(1L)).thenReturn(true);
        Response resp = enderecoResource.deletar(1L);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), resp.getStatus());
    }

    @Test
    void deletar_deveRetornarNotFoundSeNaoRemovido() {
        doNothing().when(enderecoValidator).validarParaDeletar(1L);
        when(enderecoService.deletar(1L)).thenReturn(false);
        Response resp = enderecoResource.deletar(1L);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    }

    @Test
    void deletar_deveRetornarBadRequestParaIdInvalido() {
        doThrow(new IllegalArgumentException("erro")).when(enderecoValidator).validarParaDeletar(1L);
        Response resp = enderecoResource.deletar(1L);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
        assertTrue(((java.util.Map<?,?>)resp.getEntity()).get("erro").toString().contains("erro"));
    }
}
