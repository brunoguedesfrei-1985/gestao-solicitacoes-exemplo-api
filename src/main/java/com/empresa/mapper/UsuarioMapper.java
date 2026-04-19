package com.empresa.mapper;

import com.empresa.dto.UsuarioDTO;
import com.empresa.model.Usuario;
import com.empresa.dto.CargoDTO;

public class UsuarioMapper {
    public static UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) return null;
        CargoDTO cargoDTO = null;
        if (usuario.cargo != null) {
            cargoDTO = new CargoDTO(usuario.cargo.id, usuario.cargo.nome);
        }
        return new UsuarioDTO(
            usuario.id,
            usuario.nome,
            usuario.email,
            usuario.cpf,
            cargoDTO
        );
    }
}
