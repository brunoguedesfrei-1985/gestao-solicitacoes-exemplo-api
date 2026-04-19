package com.empresa.domain;

public class SolicitacaoRequest {
    public String titulo;
    public String descricao;
    public String categoria;
    public String status;
    public Long usuarioAtribuidoId;
    public Long usuarioAtendenteId;

    public SolicitacaoRequest() {}

    public SolicitacaoRequest(String titulo, String descricao, String categoria, String status, Long usuarioAtribuidoId, Long usuarioAtendenteId) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.status = status;
        this.usuarioAtribuidoId = usuarioAtribuidoId;
        this.usuarioAtendenteId = usuarioAtendenteId;
    }
}
