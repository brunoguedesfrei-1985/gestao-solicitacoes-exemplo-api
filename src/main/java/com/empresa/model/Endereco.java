package com.empresa.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

@Entity
public class Endereco extends PanacheEntity {

    @Column(nullable = false, length = 255)
    public String endereco;

    @Column(nullable = false, length = 20)
    public String cep;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    public Usuario usuario;
}
