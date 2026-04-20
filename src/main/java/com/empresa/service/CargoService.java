package com.empresa.service;

import com.empresa.model.Cargo;
import com.empresa.dao.CargoDAO;
import com.empresa.validator.CargoValidator;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class CargoService {
    @Inject
    CargoValidator cargoValidator;

    @Inject
    CargoDAO cargoDAO;

    public List<Cargo> listarTodos() {
        return cargoDAO.listarTodos();
    }

    public Cargo buscarPorId(Long id) {
        cargoValidator.validarParaBuscarPorId(id);
        return cargoDAO.buscarPorId(id);
    }

    @Transactional
    public Cargo criar(Cargo cargo) {
        // Validação movida para o resource
        return cargoDAO.criar(cargo);
    }

    @Transactional
    public Cargo atualizar(Long id, Cargo dados) {
        // Validação movida para o resource
        return cargoDAO.atualizar(id, dados);
    }

    @Transactional
    public boolean deletar(Long id) {
        // Validação movida para o resource
        return cargoDAO.deletar(id);
    }
}
