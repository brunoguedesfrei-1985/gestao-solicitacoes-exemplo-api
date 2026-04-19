package com.empresa.mapper;

import com.empresa.dto.EnderecoDTO;
import com.empresa.model.Endereco;

public class EnderecoMapper {
	
    public static EnderecoDTO toDTO(Endereco endereco) {
        if (endereco == null) return null;
        return new EnderecoDTO(endereco.cep, endereco.endereco);
    }
    
    public static EnderecoDTO fromViaCep(com.empresa.dto.ViaCepDTO viaCep, String enderecoCompleto) {
    	if (viaCep == null) return null;
    	return new EnderecoDTO(viaCep.cep, enderecoCompleto);
    }
}
