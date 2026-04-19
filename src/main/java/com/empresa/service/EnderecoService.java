package com.empresa.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.empresa.dao.EnderecoDAO;
import com.empresa.dto.EnderecoDTO;
import com.empresa.dto.ViaCepDTO;
import com.empresa.mapper.EnderecoMapper;
import com.empresa.model.Endereco;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class EnderecoService {

    @Inject
    EnderecoDAO enderecoDAO;
    
    @Inject
    ViaCepService viaCepService;

    public List<Endereco> listarTodos() {
        return enderecoDAO.listarTodos();
    }

    public Endereco buscarPorId(Long id) {
        return enderecoDAO.buscarPorId(id);
    }

    @Transactional
    public EnderecoDTO criar(Endereco endereco) throws IllegalArgumentException {
		// Idempotência: verifica se já existe endereço para o usuário e cep
		if (endereco.usuario != null && endereco.usuario.id != null && endereco.cep != null) {
			Endereco existente = enderecoDAO.buscarPorUsuarioECep(endereco.usuario.id, endereco.cep);
			if (existente != null) {
				return EnderecoMapper.toDTO(existente);
			}
		}
		// Busca dados por id do usuario para verificar se tem outro endereco
		if (StringUtils.isNotBlank(endereco.cep) && StringUtils.isNotBlank(endereco.endereco)) {
			
			Endereco existente = enderecoDAO.buscarPorUsuario(endereco.usuario.id);
			
			if (existente != null) {
				existente.cep = endereco.cep;
				existente.endereco = endereco.endereco;
				enderecoDAO.atualizar(existente.id, existente);
				return EnderecoMapper.toDTO(existente);
			} else {
				Endereco novoEndereco = enderecoDAO.criar(endereco);
				return EnderecoMapper.toDTO(novoEndereco);
			}
		}
		return null;
    }
    
    public EnderecoDTO buscarPorCep(String cep) throws IllegalArgumentException {
    	// Busca dados do endereço pelo CEP usando o serviço ViaCEP
    	if (StringUtils.isNotBlank(cep)) {
    		try {
    			ViaCepDTO viaCep = viaCepService.buscarEnderecoPorCep(cep);
    			if (viaCep != null && viaCep.logradouro != null) {
    				StringBuilder enderecoCompleto = new StringBuilder(viaCep.logradouro);
    				if (StringUtils.isNotBlank(viaCep.complemento)) {
    					enderecoCompleto.append(", ").append(viaCep.complemento);
    				}
    				if (StringUtils.isNotBlank(viaCep.bairro)) {
    					enderecoCompleto.append(", ").append(viaCep.bairro);
    				}
    				if (StringUtils.isNotBlank(viaCep.localidade)) {
    					enderecoCompleto.append(", ").append(viaCep.localidade);
    				}
    				if (StringUtils.isNotBlank(viaCep.uf)) {
    					enderecoCompleto.append(", ").append(viaCep.uf);
    				}       
    				return EnderecoMapper.fromViaCep(viaCep, enderecoCompleto.toString());
    			}
    		} catch (Exception e) {
    			// Logar ou tratar erro de consulta ao ViaCEP, mas não impedir cadastro
    		}
    	}
		return new EnderecoDTO("", "");
    }

    @Transactional
    public Endereco atualizar(Long id, Endereco dados) throws IllegalArgumentException {
        return enderecoDAO.atualizar(id, dados);
    }

    @Transactional
    public boolean deletar(Long id) {
        return enderecoDAO.deletar(id);
    }
}
