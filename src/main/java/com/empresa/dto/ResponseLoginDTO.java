package com.empresa.dto;

import com.empresa.domain.TokenResponse;

public class ResponseLoginDTO {
	
	public UsuarioDTO usuarioDTO;
	public TokenResponse tokenResponse;
	
	public ResponseLoginDTO(UsuarioDTO usuarioDTO, TokenResponse tokenResponse) {
		this.usuarioDTO = usuarioDTO;
		this.tokenResponse = tokenResponse;
	}

}
