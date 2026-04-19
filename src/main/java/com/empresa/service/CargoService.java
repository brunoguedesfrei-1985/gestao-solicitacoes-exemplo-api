package com.empresa.service;

import com.empresa.model.Cargo;
import com.empresa.dao.CargoDAO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class CargoService {

    @Inject
    CargoDAO cargoDAO;

    public List<Cargo> listarTodos() {
        return cargoDAO.listarTodos();
    }

    public Cargo buscarPorId(Long id) {
        return cargoDAO.buscarPorId(id);
    }

    @Transactional
    public Cargo criar(Cargo cargo) {
        return cargoDAO.criar(cargo);
    }

    @Transactional
    public Cargo atualizar(Long id, Cargo dados) {
        return cargoDAO.atualizar(id, dados);
    }

    @Transactional
    public boolean deletar(Long id) {
        return cargoDAO.deletar(id);
    }
}
