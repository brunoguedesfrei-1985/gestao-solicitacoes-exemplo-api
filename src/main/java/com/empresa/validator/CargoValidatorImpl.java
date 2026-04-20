package com.empresa.validator;

import com.empresa.model.Cargo;
import org.apache.commons.lang3.StringUtils;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CargoValidatorImpl implements CargoValidator {
    @Override
    public void validarParaCriar(Cargo cargo) {
        if (cargo == null) {
            throw new IllegalArgumentException("Cargo não pode ser nulo");
        }
        if (StringUtils.isBlank(cargo.nome)) {
            throw new IllegalArgumentException("Nome do cargo não pode ser vazio");
        }
    }

    @Override
    public void validarParaAtualizar(Long id, Cargo cargo) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para atualização de cargo");
        }
        if (cargo == null) {
            throw new IllegalArgumentException("Dados do cargo não podem ser nulos");
        }
        if (StringUtils.isBlank(cargo.nome)) {
            throw new IllegalArgumentException("Nome do cargo não pode ser vazio");
        }
    }

    @Override
    public void validarParaBuscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para busca de cargo");
        }
    }

    @Override
    public void validarParaDeletar(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para deleção de cargo");
        }
    }
}
