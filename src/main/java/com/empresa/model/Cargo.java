package com.empresa.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Cargo extends PanacheEntity {

    @Column(nullable = false, unique = true, length = 255)
    public String nome;

    // Relacionamento reverso com usuarios (opcional, mas útil para queries)
    @OneToMany(mappedBy = "cargo")
    public List<Usuario> usuarios;
}
