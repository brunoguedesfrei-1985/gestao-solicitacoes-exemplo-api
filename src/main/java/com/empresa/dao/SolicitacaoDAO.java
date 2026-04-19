package com.empresa.dao;

import com.empresa.model.Solicitacao;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class SolicitacaoDAO {

    public List<Solicitacao> listarTodas() {
        return Solicitacao.listAll();
    }

    public Solicitacao buscarPorId(Long id) {
        return Solicitacao.findById(id);
    }

    public Solicitacao criar(Solicitacao solicitacao) {
        solicitacao.persist();
        return solicitacao;
    }

    public Solicitacao atualizar(Long id, Solicitacao dados) {
        Solicitacao solicitacao = Solicitacao.findById(id);
        if (solicitacao == null) {
            return null;
        }
        solicitacao.titulo = dados.titulo;
        solicitacao.descricao = dados.descricao;
        solicitacao.categoria = dados.categoria;
        solicitacao.status = dados.status;
        solicitacao.persist();
        return solicitacao;
    }

    public boolean deletar(Long id) {
        return Solicitacao.deleteById(id);
    }
}
