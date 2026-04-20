package com.empresa.validator;

import com.empresa.model.Cargo;

public interface CargoValidator {
    void validarParaCriar(Cargo cargo);
    void validarParaAtualizar(Long id, Cargo cargo);
    void validarParaBuscarPorId(Long id);
    void validarParaDeletar(Long id);
}
