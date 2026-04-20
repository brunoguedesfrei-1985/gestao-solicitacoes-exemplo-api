package com.empresa.validator;

import org.apache.commons.lang3.StringUtils;

import com.empresa.model.Endereco;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EnderecoValidatorImpl implements EnderecoValidator {
    @Override
    public void validarParaCriar(Endereco endereco) {
        if (endereco == null) {
            throw new IllegalArgumentException("Endereço não pode ser nulo");
        }
        if (endereco.usuario == null || endereco.usuario.id == null || endereco.usuario.id <= 0) {
            throw new IllegalArgumentException("Usuário do endereço inválido");
        }
        if (StringUtils.isBlank(endereco.cep)) {
            throw new IllegalArgumentException("CEP do endereço não pode ser vazio");
        }
        if (StringUtils.isBlank(endereco.endereco)) {
            throw new IllegalArgumentException("Endereço não pode ser vazio");
        }
    }

    @Override
    public void validarParaAtualizar(Long id, Endereco endereco) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para atualização de endereço");
        }
        if (endereco == null) {
            throw new IllegalArgumentException("Dados do endereço não podem ser nulos");
        }
        if (StringUtils.isBlank(endereco.cep)) {
            throw new IllegalArgumentException("CEP do endereço não pode ser vazio");
        }
        if (StringUtils.isBlank(endereco.endereco)) {
            throw new IllegalArgumentException("Endereço não pode ser vazio");
        }
    }

    @Override
    public void validarParaBuscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para busca de endereço");
        }
    }

    @Override
    public void validarParaBuscarPorCep(String cep) {
        if (StringUtils.isBlank(cep)) {
            throw new IllegalArgumentException("CEP não pode ser vazio");
        }
    }

    @Override
    public void validarParaDeletar(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para deleção de endereço");
        }
    }
}
