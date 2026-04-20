package com.empresa.validator;

import com.empresa.domain.SolicitacaoRequest;
import com.empresa.model.Solicitacao;
import org.apache.commons.lang3.StringUtils;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SolicitacaoValidatorImpl implements SolicitacaoValidator {
    @Override
    public void validarParaCriar(SolicitacaoRequest req) {
        if (req == null) {
            throw new IllegalArgumentException("Solicitação não pode ser nula");
        }
        if (StringUtils.isBlank(req.titulo)) {
            throw new IllegalArgumentException("Título da solicitação não pode ser vazio");
        }
        if (StringUtils.isBlank(req.categoria)) {
            throw new IllegalArgumentException("Categoria da solicitação não pode ser vazia");
        }
        if (StringUtils.isBlank(req.status)) {
            throw new IllegalArgumentException("Status da solicitação não pode ser vazio");
        }
    }

    @Override
    public void validarParaAtualizar(Long id, Solicitacao dados) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para atualização de solicitação");
        }
        if (dados == null) {
            throw new IllegalArgumentException("Dados da solicitação não podem ser nulos");
        }
        if (StringUtils.isBlank(dados.titulo)) {
            throw new IllegalArgumentException("Título da solicitação não pode ser vazio");
        }
        if (StringUtils.isBlank(dados.categoria)) {
            throw new IllegalArgumentException("Categoria da solicitação não pode ser vazia");
        }
        if (StringUtils.isBlank(dados.status)) {
            throw new IllegalArgumentException("Status da solicitação não pode ser vazio");
        }
    }

    @Override
    public void validarParaBuscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para busca de solicitação");
        }
    }

    @Override
    public void validarParaDeletar(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID inválido para deleção de solicitação");
        }
    }

    @Override
    public void validarParaListarPorUsuario(String email, int page, int size) {
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("Email do usuário não pode ser vazio");
        }
        if (page < 0) {
            throw new IllegalArgumentException("Página não pode ser negativa");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Tamanho da página deve ser maior que zero");
        }
    }

    @Override
    public void validarParaListarPorUsuarioAtendente(String email, int page, int size) {
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("Email do atendente não pode ser vazio");
        }
        if (page < 0) {
            throw new IllegalArgumentException("Página não pode ser negativa");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Tamanho da página deve ser maior que zero");
        }
    }

    @Override
    public void validarParaCountPorUsuario(String email) {
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("Email do usuário não pode ser vazio");
        }
    }

    @Override
    public void validarParaCountPorUsuarioAtendente(String email) {
        if (StringUtils.isBlank(email)) {
            throw new IllegalArgumentException("Email do atendente não pode ser vazio");
        }
    }
}
