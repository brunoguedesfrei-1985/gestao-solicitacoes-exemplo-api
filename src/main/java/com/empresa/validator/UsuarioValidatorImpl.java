package com.empresa.validator;

import com.empresa.domain.UsuarioRequest;
import com.empresa.model.Usuario;
import org.apache.commons.lang3.StringUtils;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UsuarioValidatorImpl implements UsuarioValidator {
    @Override
    public void validarParaCriar(UsuarioRequest usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo");
        }
        if (StringUtils.isBlank(usuario.nome)) {
            throw new IllegalArgumentException("Nome do usuário não pode ser vazio");
        }
        if (StringUtils.isBlank(usuario.email)) {
            throw new IllegalArgumentException("Email do usuário não pode ser vazio");
        }
        if (StringUtils.isBlank(usuario.cpf)) {
            throw new IllegalArgumentException("CPF do usuário não pode ser vazio");
        }
        if (StringUtils.isBlank(usuario.senha)) {
            throw new IllegalArgumentException("Senha do usuário não pode ser vazia");
        }
    }

    @Override
    public void validarParaAtualizar(Long id, Usuario usuario) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para atualização de usuário");
        }
        if (usuario == null) {
            throw new IllegalArgumentException("Dados do usuário não podem ser nulos");
        }
        if (StringUtils.isBlank(usuario.nome)) {
            throw new IllegalArgumentException("Nome do usuário não pode ser vazio");
        }
        if (StringUtils.isBlank(usuario.email)) {
            throw new IllegalArgumentException("Email do usuário não pode ser vazio");
        }
        if (StringUtils.isBlank(usuario.cpf)) {
            throw new IllegalArgumentException("CPF do usuário não pode ser vazio");
        }
    }

    @Override
    public void validarParaBuscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para busca de usuário");
        }
    }

    @Override
    public void validarParaDeletar(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para deleção de usuário");
        }
    }

    @Override
    public void validarParaAutenticar(String email, String senha) {
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("Email não pode ser vazio");
        }
        if (StringUtils.isBlank(senha)) {
            throw new IllegalArgumentException("Senha não pode ser vazia");
        }
    }
}
