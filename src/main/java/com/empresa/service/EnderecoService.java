package com.empresa.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

import com.empresa.dao.EnderecoDAO;
import com.empresa.dto.EnderecoDTO;
import com.empresa.dto.ViaCepDTO;
import com.empresa.mapper.EnderecoMapper;
import com.empresa.model.Endereco;
import com.empresa.validator.EnderecoValidator;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class EnderecoService {

	private static final Logger LOG = Logger.getLogger(EnderecoService.class);

    @Inject
    EnderecoDAO enderecoDAO;
    
    @Inject
    ViaCepService viaCepService;

	public List<Endereco> listarTodos() {
		return enderecoDAO.listarTodos();
	}

	@Inject
	EnderecoValidator enderecoValidator;

	public Endereco buscarPorId(Long id) {
		enderecoValidator.validarParaBuscarPorId(id);
		return enderecoDAO.buscarPorId(id);
	}

	@Transactional
	public EnderecoDTO criar(Endereco endereco) throws IllegalArgumentException {
		// Validação movida para o resource
		// Idempotência: verifica se já existe endereço para o usuário e cep
		Endereco existente = enderecoDAO.buscarPorUsuarioECep(endereco.usuario.id, endereco.cep);
		if (existente != null) {
			return EnderecoMapper.toDTO(existente);
		}
		// Busca dados por id do usuario para verificar se tem outro endereco
		existente = enderecoDAO.buscarPorUsuario(endereco.usuario.id);
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
    
	public EnderecoDTO buscarPorCep(String cep) throws IllegalArgumentException {
		enderecoValidator.validarParaBuscarPorCep(cep);
		// Busca dados do endereço pelo CEP usando o serviço ViaCEP
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
			LOG.errorf("Erro ao consultar ViaCEP para o CEP %s: %s", cep, e.getMessage());
		}
		return new EnderecoDTO("", "");
	}

	@Transactional
	public Endereco atualizar(Long id, Endereco dados) throws IllegalArgumentException {
		// Validação movida para o resource
		return enderecoDAO.atualizar(id, dados);
	}

	@Transactional
	public boolean deletar(Long id) {
		// Validação movida para o resource
		return enderecoDAO.deletar(id);
	}
}
