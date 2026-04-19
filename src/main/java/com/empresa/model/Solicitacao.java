package com.empresa.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacao")
public class Solicitacao extends PanacheEntity {

    @Column(nullable = false, length = 255)
    public String titulo;

    @Column(columnDefinition = "text")
    public String descricao;

    @Column(length = 100)
    public String categoria;

    @Column(nullable = false, length = 50)
    public String status;

    @Column(name = "data_criacao", nullable = false)
    public LocalDateTime dataCriacao = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "usuario_atribuido_id")
    public Usuario usuarioAtribuido;

    @ManyToOne
    @JoinColumn(name = "usuario_atendente_id")
    public Usuario usuarioAtendente;
}
