package com.empresa.validator;

import com.empresa.model.Endereco;

public interface EnderecoValidator {
    void validarParaCriar(Endereco endereco);
    void validarParaAtualizar(Long id, Endereco endereco);
    void validarParaBuscarPorId(Long id);
    void validarParaBuscarPorCep(String cep);
    void validarParaDeletar(Long id);
}
