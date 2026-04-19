package com.empresa.dto;

import java.time.LocalDateTime;

public class SolicitacaoDTO {
    public Long id;
    public String titulo;
    public String descricao;
    public String categoria;
    public String status;
    public LocalDateTime dataCriacao;
    public Long usuarioAtribuidoId;
    public String usuarioAtribuidoNome;
    public Long usuarioAtendenteId;
    public String usuarioAtendenteNome;
}
