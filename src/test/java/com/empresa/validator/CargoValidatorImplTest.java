package com.empresa.validator;

import com.empresa.model.Cargo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CargoValidatorImplTest {
    CargoValidatorImpl validator;

    @BeforeEach
    void setUp() {
        validator = new CargoValidatorImpl();
    }

    @Test
    void validarParaCriar_deveAceitarCargoValido() {
        Cargo cargo = new Cargo();
        cargo.nome = "Analista";
        assertDoesNotThrow(() -> validator.validarParaCriar(cargo));
    }

    @Test
    void validarParaCriar_deveLancarSeCargoNulo() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCriar(null));
        assertTrue(ex.getMessage().contains("Cargo não pode ser nulo"));
    }

    @Test
    void validarParaCriar_deveLancarSeNomeVazio() {
        Cargo cargo = new Cargo();
        cargo.nome = " ";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validarParaCriar(cargo));
        assertTrue(ex.getMessage().contains("Nome do cargo não pode ser vazio"));
    }

    @Test
    void validarParaAtualizar_deveAceitarDadosValidos() {
        Cargo cargo = new Cargo();
        cargo.nome = "Gerente";
        assertDoesNotThrow(() -> validator.validarParaAtualizar(1L, cargo));
    }

    @Test
    void validarParaAtualizar_deveLancarSeIdNuloOuInvalido() {
        Cargo cargo = new Cargo();
        cargo.nome = "Gerente";
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(null, cargo));
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(0L, cargo));
        assertTrue(ex1.getMessage().contains("ID inválido"));
        assertTrue(ex2.getMessage().contains("ID inválido"));
    }

    @Test
    void validarParaAtualizar_deveLancarSeCargoNulo() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(1L, null));
        assertTrue(ex.getMessage().contains("Dados do cargo não podem ser nulos"));
    }

    @Test
    void validarParaAtualizar_deveLancarSeNomeVazio() {
        Cargo cargo = new Cargo();
        cargo.nome = " ";
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> validator.validarParaAtualizar(1L, cargo));
        assertTrue(ex.getMessage().contains("Nome do cargo não pode ser vazio"));
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
}
