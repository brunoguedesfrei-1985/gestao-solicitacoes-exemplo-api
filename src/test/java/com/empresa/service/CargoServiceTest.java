package com.empresa.service;

import com.empresa.dao.CargoDAO;
import com.empresa.model.Cargo;
import com.empresa.validator.CargoValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CargoServiceTest {
    @Mock
    CargoDAO cargoDAO;
    @Mock
    CargoValidator cargoValidator;
    @InjectMocks
    CargoService cargoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listarTodos_deveRetornarListaDeCargos() {
        Cargo cargo1 = new Cargo(); cargo1.id = 1L; cargo1.nome = "A";
        Cargo cargo2 = new Cargo(); cargo2.id = 2L; cargo2.nome = "B";
        when(cargoDAO.listarTodos()).thenReturn(Arrays.asList(cargo1, cargo2));
        List<Cargo> result = cargoService.listarTodos();
        assertEquals(2, result.size());
        assertEquals("A", result.get(0).nome);
        assertEquals("B", result.get(1).nome);
    }

    @Test
    void buscarPorId_deveValidarEDelegarParaDAO() {
        Cargo cargo = new Cargo(); cargo.id = 1L; cargo.nome = "A";
        when(cargoDAO.buscarPorId(1L)).thenReturn(cargo);
        Cargo result = cargoService.buscarPorId(1L);
        verify(cargoValidator).validarParaBuscarPorId(1L);
        assertEquals("A", result.nome);
    }

    @Test
    void criar_deveDelegarParaDAO() {
        Cargo cargo = new Cargo(); cargo.id = 1L; cargo.nome = "A";
        when(cargoDAO.criar(cargo)).thenReturn(cargo);
        Cargo result = cargoService.criar(cargo);
        assertEquals("A", result.nome);
    }

    @Test
    void atualizar_deveDelegarParaDAO() {
        Cargo cargo = new Cargo(); cargo.id = 1L; cargo.nome = "A";
        when(cargoDAO.atualizar(1L, cargo)).thenReturn(cargo);
        Cargo result = cargoService.atualizar(1L, cargo);
        assertEquals("A", result.nome);
    }

    @Test
    void deletar_deveDelegarParaDAO() {
        when(cargoDAO.deletar(1L)).thenReturn(true);
        boolean result = cargoService.deletar(1L);
        assertTrue(result);
    }
}
