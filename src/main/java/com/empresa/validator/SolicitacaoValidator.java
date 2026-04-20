package com.empresa.validator;

import com.empresa.domain.SolicitacaoRequest;
import com.empresa.model.Solicitacao;

public interface SolicitacaoValidator {
    void validarParaCriar(SolicitacaoRequest req);
    void validarParaAtualizar(Long id, Solicitacao dados);
    void validarParaBuscarPorId(Long id);
    void validarParaDeletar(Long id);
    void validarParaListarPorUsuario(String email, int page, int size);
    void validarParaListarPorUsuarioAtendente(String email, int page, int size);
    void validarParaCountPorUsuario(String email);
    void validarParaCountPorUsuarioAtendente(String email);
}
