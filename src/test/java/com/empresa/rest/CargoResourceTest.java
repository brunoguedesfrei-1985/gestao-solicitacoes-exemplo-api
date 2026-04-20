package com.empresa.rest;

import com.empresa.model.Cargo;
import com.empresa.service.CargoService;
import com.empresa.validator.CargoValidator;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CargoResourceTest {
    @Mock
    CargoService cargoService;
    @Mock
    CargoValidator cargoValidator;
    @InjectMocks
    CargoResource cargoResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodos_deveRetornarLista() {
        Cargo c1 = new Cargo();
        Cargo c2 = new Cargo();
        when(cargoService.listarTodos()).thenReturn(Arrays.asList(c1, c2));
        List<Cargo> result = cargoResource.listarTodos();
        assertEquals(2, result.size());
    }

    @Test
    void buscarPorId_deveRetornarCargo() {
        Cargo c = new Cargo();
        when(cargoService.buscarPorId(1L)).thenReturn(c);
        Response resp = cargoResource.buscarPorId(1L);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertEquals(c, resp.getEntity());
    }

    @Test
    void buscarPorId_deveRetornarNotFound() {
        when(cargoService.buscarPorId(1L)).thenReturn(null);
        Response resp = cargoResource.buscarPorId(1L);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    }

    @Test
    void criar_deveRetornarCreatedParaCargoValido() {
        Cargo c = new Cargo();
        doNothing().when(cargoValidator).validarParaCriar(c);
        when(cargoService.criar(c)).thenReturn(c);
        Response resp = cargoResource.criar(c);
        assertEquals(Response.Status.CREATED.getStatusCode(), resp.getStatus());
        assertEquals(c, resp.getEntity());
    }

    @Test
    void criar_deveRetornarBadRequestParaCargoInvalido() {
        Cargo c = new Cargo();
        doThrow(new IllegalArgumentException("erro")).when(cargoValidator).validarParaCriar(c);
        Response resp = cargoResource.criar(c);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
        assertTrue(((java.util.Map<?,?>)resp.getEntity()).get("erro").toString().contains("erro"));
    }

    @Test
    void atualizar_deveRetornarOkParaAtualizacaoValida() {
        Cargo c = new Cargo();
        doNothing().when(cargoValidator).validarParaAtualizar(1L, c);
        when(cargoService.atualizar(1L, c)).thenReturn(c);
        Response resp = cargoResource.atualizar(1L, c);
        assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
        assertEquals(c, resp.getEntity());
    }

    @Test
    void atualizar_deveRetornarBadRequestParaAtualizacaoInvalida() {
        Cargo c = new Cargo();
        doThrow(new IllegalArgumentException("erro")).when(cargoValidator).validarParaAtualizar(1L, c);
        Response resp = cargoResource.atualizar(1L, c);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
        assertTrue(((java.util.Map<?,?>)resp.getEntity()).get("erro").toString().contains("erro"));
    }

    @Test
    void atualizar_deveRetornarNotFoundSeCargoNaoExiste() {
        Cargo c = new Cargo();
        doNothing().when(cargoValidator).validarParaAtualizar(1L, c);
        when(cargoService.atualizar(1L, c)).thenReturn(null);
        Response resp = cargoResource.atualizar(1L, c);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    }

    @Test
    void deletar_deveRetornarNoContentSeExcluido() {
        doNothing().when(cargoValidator).validarParaDeletar(1L);
        when(cargoService.deletar(1L)).thenReturn(true);
        Response resp = cargoResource.deletar(1L);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), resp.getStatus());
    }

    @Test
    void deletar_deveRetornarNotFoundSeNaoExcluido() {
        doNothing().when(cargoValidator).validarParaDeletar(1L);
        when(cargoService.deletar(1L)).thenReturn(false);
        Response resp = cargoResource.deletar(1L);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    }

    @Test
    void deletar_deveRetornarBadRequestParaIdInvalido() {
        doThrow(new IllegalArgumentException("erro")).when(cargoValidator).validarParaDeletar(1L);
        Response resp = cargoResource.deletar(1L);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), resp.getStatus());
        assertTrue(((java.util.Map<?,?>)resp.getEntity()).get("erro").toString().contains("erro"));
    }
}
