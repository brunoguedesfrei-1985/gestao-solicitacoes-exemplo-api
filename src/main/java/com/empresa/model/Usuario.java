package com.empresa.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Usuario extends PanacheEntity {

    @Column(nullable = false, unique = true, length = 14)
    public String cpf;
    
    @Column(nullable = false, length = 255)
    public String nome;

    @Column(nullable = false, unique = true, length = 255)
    public String email;

    @Column(nullable = false, length = 255)
    public String senha;

    @ManyToOne
    @JoinColumn(name = "cargo_id")
    public Cargo cargo;

    // Relacionamento com endereços (um para muitos)
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Endereco> enderecos;

}
