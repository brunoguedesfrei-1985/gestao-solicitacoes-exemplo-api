package com.empresa.dao;

import com.empresa.model.Endereco;
import com.empresa.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class EnderecoDAO {

    public List<Endereco> listarTodos() {
        return Endereco.listAll();
    }

    public Endereco buscarPorId(Long id) {
        return Endereco.findById(id);
    }

    public Endereco criar(Endereco endereco) throws IllegalArgumentException {
        if (endereco.usuario != null && endereco.usuario.id != null) {
            Usuario usuario = Usuario.findById(endereco.usuario.id);
            if (usuario == null) {
                throw new IllegalArgumentException("Usuário informado não existe.");
            }
            endereco.usuario = usuario;
        } else {
            throw new IllegalArgumentException("Usuário é obrigatório para o endereço.");
        }
        endereco.persist();
        return endereco;
    }

    public Endereco atualizar(Long id, Endereco dados) throws IllegalArgumentException {
        Endereco endereco = Endereco.findById(id);
        if (endereco == null) {
            return null;
        }
        endereco.endereco = dados.endereco;
        endereco.cep = dados.cep;
        if (dados.usuario != null && dados.usuario.id != null) {
            Usuario usuario = Usuario.findById(dados.usuario.id);
            if (usuario == null) {
                throw new IllegalArgumentException("Usuário informado não existe.");
            }
            endereco.usuario = usuario;
        }
        endereco.persist();
        return endereco;
    }

    public boolean deletar(Long id) {
        return Endereco.deleteById(id);
    }

    public Endereco buscarPorUsuarioECep(Long usuarioId, String cep) {
        return Endereco.find("usuario.id = ?1 and cep = ?2", usuarioId, cep).firstResult();
    }
    
    public Endereco buscarPorUsuario(Long usuarioId) {
    	return Endereco.find("usuario.id = ?1", usuarioId).firstResult();
    }

}
