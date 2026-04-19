package com.empresa.mapper;

import com.empresa.dto.SolicitacaoDTO;
import com.empresa.model.Solicitacao;

public class SolicitacaoMapper {
    public static SolicitacaoDTO toDTO(Solicitacao s) {
        if (s == null) return null;
        SolicitacaoDTO dto = new SolicitacaoDTO();
        dto.id = s.id;
        dto.titulo = s.titulo;
        dto.descricao = s.descricao;
        dto.categoria = s.categoria;
        dto.status = s.status;
        dto.dataCriacao = s.dataCriacao;
        if (s.usuarioAtribuido != null) {
            dto.usuarioAtribuidoId = s.usuarioAtribuido.id;
            dto.usuarioAtribuidoNome = s.usuarioAtribuido.nome;
        }
        if (s.usuarioAtendente != null) {
            dto.usuarioAtendenteId = s.usuarioAtendente.id;
            dto.usuarioAtendenteNome = s.usuarioAtendente.nome;
        }
        return dto;
    }
}
