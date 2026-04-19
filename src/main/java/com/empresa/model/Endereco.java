package com.empresa.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
public class Endereco extends PanacheEntity {

    @Column(nullable = false)
    public String endereco;

    @Column(nullable = false)
    public String cep;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    public Usuario usuario;
}
