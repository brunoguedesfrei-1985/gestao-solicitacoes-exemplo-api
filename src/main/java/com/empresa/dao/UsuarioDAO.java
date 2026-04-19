package com.empresa.dao;

import com.empresa.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class UsuarioDAO {

    public List<Usuario> listarTodos() {
        return Usuario.listAll();
    }

    public Usuario buscarPorId(Long id) {
        return Usuario.findById(id);
    }


    public Usuario criar(Usuario usuario) throws IllegalArgumentException {
        if (Usuario.find("email", usuario.email).firstResult() != null) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }
        usuario.persist();
        return usuario;
    }

    public Usuario buscarPorEmail(String email) {
        return Usuario.find("email", email).firstResult();
    }

    public Usuario atualizar(Long id, Usuario dados) {
        Usuario usuario = Usuario.findById(id);
        if (usuario == null) {
            return null;
        }
        usuario.nome = dados.nome;
        usuario.email = dados.email;
        usuario.senha = dados.senha;
        usuario.cargo = dados.cargo;
        usuario.persist();
        return usuario;
    }

    public boolean deletar(Long id) {
        return Usuario.deleteById(id);
    }
}
