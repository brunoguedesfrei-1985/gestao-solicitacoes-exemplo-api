package com.empresa.service;

import java.util.List;

import org.jboss.logging.Logger;

import com.empresa.dao.SolicitacaoDAO;
import com.empresa.dao.UsuarioDAO;
import com.empresa.domain.SolicitacaoRequest;
import com.empresa.dto.SolicitacaoDTO;
import com.empresa.mapper.SolicitacaoMapper;
import com.empresa.model.Solicitacao;
import com.empresa.validator.SolicitacaoValidator;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class SolicitacaoService {
    @Inject
    SolicitacaoValidator solicitacaoValidator;

    
    private static final Logger LOG = Logger.getLogger(SolicitacaoService.class);
    
    @Inject
    UsuarioDAO usuarioDAO;
    
    @Inject
    SolicitacaoDAO solicitacaoDAO;
    
    public long countPorUsuarioDemandante(String email) {
        solicitacaoValidator.validarParaCountPorUsuario(email);
        return solicitacaoDAO.countPorUsuarioDemandante(email);
    }
    
    public long countPorUsuarioAtendente(String email) {
        solicitacaoValidator.validarParaCountPorUsuarioAtendente(email);
        return solicitacaoDAO.countPorUsuarioAtendente(email);
    }

    public List<Solicitacao> listarTodasPorUsuario(String email, int page, int size) {
        solicitacaoValidator.validarParaListarPorUsuario(email, page, size);
        return solicitacaoDAO.listarTodasPorUsuario(email, page, size);
    }

    public List<Solicitacao> listarTodasPorUsuarioAtendente(String email, int page, int size) {
        solicitacaoValidator.validarParaListarPorUsuarioAtendente(email, page, size);
        return solicitacaoDAO.listarTodasPorUsuarioAtendente(email, page, size);
    }

    public List<Solicitacao> listarTodas() {
        LOG.info("Listando todas as solicitações");
        return solicitacaoDAO.listarTodas();
    }

    public Solicitacao buscarPorId(Long id) {
        solicitacaoValidator.validarParaBuscarPorId(id);
        LOG.debugf("Buscando solicitação por id %d", id);
        return solicitacaoDAO.buscarPorId(id);
    }

    @Transactional
    public SolicitacaoDTO criar(SolicitacaoRequest req) {
        // Validação movida para o resource
        LOG.infof("Criando solicitação: %s", req.titulo);
        Solicitacao solicitacao = new Solicitacao();
        solicitacao.titulo = req.titulo;
        solicitacao.descricao = req.descricao;
        solicitacao.categoria = req.categoria;
        solicitacao.status = req.status;
        if (req.usuarioAtribuidoId != null) {
            solicitacao.usuarioAtribuido = usuarioDAO.buscarPorId(req.usuarioAtribuidoId);
        }
        if (req.usuarioAtendenteId != null) {
            solicitacao.usuarioAtendente = usuarioDAO.buscarPorId(req.usuarioAtendenteId);
        }
        return SolicitacaoMapper.toDTO(solicitacaoDAO.criar(solicitacao));
    }

    @Transactional
    public SolicitacaoDTO atualizar(Long id, Solicitacao dados) {
        // Validação movida para o resource
        LOG.infof("Atualizando solicitação id %d", id);
        return SolicitacaoMapper.toDTO(solicitacaoDAO.atualizar(id, dados));
    }

    @Transactional
    public boolean deletar(Long id) {
        // Validação movida para o resource
        LOG.infof("Deletando solicitação id %d", id);
        return solicitacaoDAO.deletar(id);
    }
}
