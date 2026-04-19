package com.empresa.dao;

import com.empresa.model.Cargo;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class CargoDAO {

    public List<Cargo> listarTodos() {
        return Cargo.listAll();
    }

    public Cargo buscarPorId(Long id) {
        return Cargo.findById(id);
    }

    public Cargo criar(Cargo cargo) {
        cargo.persist();
        return cargo;
    }

    public Cargo atualizar(Long id, Cargo dados) {
        Cargo cargo = Cargo.findById(id);
        if (cargo != null) {
            cargo.nome = dados.nome;
            cargo.persist();
        }
        return cargo;
    }

    public boolean deletar(Long id) {
        return Cargo.deleteById(id);
    }
}
