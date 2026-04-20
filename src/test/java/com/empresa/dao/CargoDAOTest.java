package com.empresa.dao;

import com.empresa.model.Cargo;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CargoDAOTest {
    @Inject
    CargoDAO cargoDAO;

    @BeforeEach
    @Transactional
    void setup() {
        // Limpa usuários antes para evitar violação de FK
        com.empresa.model.Usuario.deleteAll();
        Cargo.deleteAll();
    }

    @Test
    @Transactional
    void listarTodos_deveRetornarListaDeCargos() {
        Cargo c1 = new Cargo();
        c1.nome = "Cargo 1";
        c1.persist();
        Cargo c2 = new Cargo();
        c2.nome = "Cargo 2";
        c2.persist();
        List<Cargo> result = cargoDAO.listarTodos();
        assertEquals(2, result.size());
    }

    @Test
    @Transactional
    void buscarPorId_deveRetornarCargo() {
        Cargo c = new Cargo();
        c.nome = "Cargo Teste";
        c.persist();
        Cargo result = cargoDAO.buscarPorId(c.id);
        assertNotNull(result);
        assertEquals("Cargo Teste", result.nome);
    }

    @Test
    @Transactional
    void criar_devePersistirCargo() {
        Cargo c = new Cargo();
        c.nome = "Novo Cargo";
        Cargo result = cargoDAO.criar(c);
        assertNotNull(result.id);
        assertEquals("Novo Cargo", result.nome);
    }

    @Test
    @Transactional
    void atualizar_deveAlterarNomeDoCargo() {
        Cargo c = new Cargo();
        c.nome = "Antigo";
        c.persist();
        Cargo dados = new Cargo();
        dados.nome = "Novo";
        Cargo result = cargoDAO.atualizar(c.id, dados);
        assertEquals("Novo", result.nome);
    }

    @Test
    @Transactional
    void atualizar_deveRetornarNullSeCargoNaoExiste() {
        Cargo dados = new Cargo();
        dados.nome = "Novo";
        Cargo result = cargoDAO.atualizar(999L, dados);
        assertNull(result);
    }

    @Test
    @Transactional
    void deletar_deveRemoverCargo() {
        Cargo c = new Cargo();
        c.nome = "Cargo Deletar";
        c.persist();
        boolean result = cargoDAO.deletar(c.id);
        assertTrue(result);
        assertNull(Cargo.findById(c.id));
    }
}
