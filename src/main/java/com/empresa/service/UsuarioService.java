package com.empresa.service;

import java.util.List;

import com.empresa.dao.UsuarioDAO;
import com.empresa.domain.UsuarioRequest;
import com.empresa.dto.UsuarioDTO;
import com.empresa.mapper.UsuarioMapper;
import com.empresa.model.Usuario;
import com.empresa.util.CrypterUtil;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UsuarioService {
	
    @Inject
    UsuarioDAO usuarioDAO;
    
    @Inject
    CargoService cargoService;
    
    @Inject
    CpfValidatorService cpfValidatorService;

    public List<Usuario> listarTodos() {
        return usuarioDAO.listarTodos();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioDAO.buscarPorId(id);
    }

    @Transactional
    public UsuarioDTO criar(UsuarioRequest usuario) throws IllegalArgumentException {
        // Validar CPF usando serviço externo
        try {
            if (usuario.cpf == null || !cpfValidatorService.isCpfValido(usuario.cpf)) {
                throw new IllegalArgumentException("CPF inválido!");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Erro ao validar CPF: " + e.getMessage());
        }
        
        Usuario newUser = new Usuario();
        newUser.nome = usuario.nome;
        newUser.email = usuario.email;
        newUser.cpf = usuario.cpf;
        newUser.senha = CrypterUtil.getHash(usuario.senha);
        
        // Buscar o cargo completo pelo id informado
        if (usuario.cargoId != null) {
        	newUser.cargo = cargoService.buscarPorId(usuario.cargoId);
        }
        return UsuarioMapper.toDTO(usuarioDAO.criar(newUser));
    }

    @Transactional
    public Usuario atualizar(Long id, Usuario dados) {
        return usuarioDAO.atualizar(id, dados);
    }

    @Transactional
    public boolean deletar(Long id) {
        return usuarioDAO.deletar(id);
    }

    /**
     * Autentica um usuário pelo email e senha.
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @return O usuário autenticado, ou null se inválido
     */
    public UsuarioDTO autenticar(String email, String senha) {
        Usuario usuario = usuarioDAO.buscarPorEmail(email);
        if(usuario == null) {
        	return null;
        }
        boolean isPasswordOk = CrypterUtil.isPasswordValid(senha, usuario.senha);
        if (isPasswordOk) {
        	usuario.cargo = cargoService.buscarPorId(usuario.cargo.id);
            return UsuarioMapper.toDTO(usuario);
        }
        return null;
    }
}
