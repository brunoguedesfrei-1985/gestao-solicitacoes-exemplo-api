package com.empresa.validator;

import com.empresa.domain.UsuarioRequest;
import com.empresa.model.Usuario;

public interface UsuarioValidator {
    void validarParaCriar(UsuarioRequest usuario);
    void validarParaAtualizar(Long id, Usuario usuario);
    void validarParaBuscarPorId(Long id);
    void validarParaDeletar(Long id);
    void validarParaAutenticar(String email, String senha);
}
